package org.teamvoided.dusk_debris.block.not_blocks

import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.entity.Entity
import net.minecraft.item.ItemPlacementContext
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.state.property.Properties
import net.minecraft.util.BlockMirror
import net.minecraft.util.BlockRotation
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.shape.VoxelShape
import net.minecraft.world.World
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
import org.teamvoided.dusk_debris.util.rotateVoxelShape

object SculkDirectionalStuff {
    private val HALF_BLOCK: VoxelShape = Block.createCuboidShape(0.0, 8.0, 0.0, 16.0, 16.0, 16.0)
    private val HALF_NORTH_BLOCK: VoxelShape = Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 16.0, 8.0)
    val FACING = Properties.FACING

    @JvmStatic
    fun getDirectionalSlabShape(state: BlockState, cir: CallbackInfoReturnable<VoxelShape>) {
        val facing = state.get(FACING)
        if (facing.axis != Direction.Axis.Y) {
            val times = state.get(FACING).horizontal
            cir.setReturnValue(rotateVoxelShape(times, HALF_NORTH_BLOCK))
        } else if (facing == Direction.DOWN) {
            cir.returnValue = HALF_BLOCK
        }
    }

    @JvmStatic
    fun getPlacementState(supr: BlockState?, ctx: ItemPlacementContext): BlockState? {
        return if (ctx == null || supr == null) supr
        else supr.with(FACING, ctx.side)
    }

    @JvmStatic
    fun isNotUp(state: BlockState) = state.get(Properties.FACING) != Direction.UP

    @JvmStatic
    fun spin(state: BlockState, rotation: BlockRotation): BlockState {
        return state.with(FACING, rotation.rotate(state.get(FACING)))
    }

    @JvmStatic
    fun spin(state: BlockState, mirror: BlockMirror): BlockState {
        return state.rotate(mirror.getRotation(state.get(FACING)))
    }
}