package org.teamvoided.dusk_debris.block.not_blocks

import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.CalibratedSculkSensorBlock
import net.minecraft.block.sculk.SculkBlock
import net.minecraft.entity.Entity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemPlacementContext
import net.minecraft.registry.tag.BlockTags
import net.minecraft.sound.SoundCategory
import net.minecraft.state.property.Properties
import net.minecraft.util.BlockMirror
import net.minecraft.util.BlockRotation
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.random.RandomGenerator
import net.minecraft.util.shape.VoxelShape
import net.minecraft.world.WorldAccess
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
    fun getPlacementState(supr: BlockState?, ctx: ItemPlacementContext?): BlockState? {
        return if (ctx == null || supr == null) supr
        else supr.with(FACING, ctx.side)
    }

    @JvmStatic
    fun isNotUp(state: BlockState) = state.get(Properties.FACING) != Direction.UP

    @JvmStatic
    fun isNotCalibrated(block: Block) = block !is CalibratedSculkSensorBlock

    @JvmStatic
    fun isNotUpCalibrated(state: BlockState) =
        isNotCalibrated(state.block) && isNotUp(state)

    @JvmStatic
    fun spin(state: BlockState, rotation: BlockRotation): BlockState =
        state.with(FACING, rotation.rotate(state.get(FACING)))

    @JvmStatic
    fun spin(state: BlockState, mirror: BlockMirror): BlockState =
        state.rotate(mirror.getRotation(state.get(FACING)))

    @JvmStatic
    fun noCreativeFlightAnnoyance(entity: Entity): Boolean =
        !(entity is PlayerEntity && entity.isCreative && entity.abilities.flying)


    /* - - - SPREADING FUNCTIONS - - - */
    @JvmStatic
    fun getRandomGrowthStateWithoutOffset(
        world: WorldAccess,
        pos: BlockPos,
        random: RandomGenerator,
        canSummon: Boolean
    ) {
        val worldState = world.getBlockState(pos)
        val worldBlock = worldState.block
        if (worldBlock is SculkBlock) {
            Direction.entries.forEach {
                val posOffset: BlockPos = pos.offset(it)
                var blockState: BlockState = worldBlock.getRandomGrowthState(world, posOffset, random, canSummon)
                if (blockState.contains(Properties.FACING) || it == Direction.UP) {
                    blockState = blockState.with(Properties.FACING, it)
                    if (world.getBlockState(posOffset).isIn(BlockTags.REPLACEABLE)) {
                        world.setBlockState(posOffset, blockState, 3)
                        world.playSound(null, pos, blockState.soundGroup.placeSound, SoundCategory.BLOCKS, 1.0f, 1.0f)
                        return
                    }
                }
            }
        }
    }
}