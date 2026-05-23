package org.teamvoided.dusk_debris.block

import com.mojang.serialization.MapCodec
import net.minecraft.core.BlockPos
import net.minecraft.tags.FluidTags
import net.minecraft.world.entity.projectile.Projectile
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.block.BushBlock
import net.minecraft.world.level.block.IceBlock
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.material.Fluids
import net.minecraft.world.phys.shapes.CollisionContext
import net.minecraft.world.phys.shapes.EntityCollisionContext
import net.minecraft.world.phys.shapes.Shapes
import net.minecraft.world.phys.shapes.VoxelShape

class WaterFernBlock(settings: Properties) : BushBlock(settings) {
    override fun codec(): MapCodec<out BushBlock> = CODEC

    override fun getCollisionShape(
        state: BlockState, world: BlockGetter, pos: BlockPos, context: CollisionContext
    ): VoxelShape {
        val entity = (context as EntityCollisionContext).entity
        return if (entity != null && (/*entity.type.isIn(DuskEntityTypeTags.NO_COLLIDE_WATER_FERN) ||*/ entity is Projectile)) Shapes.empty()
        else super.getCollisionShape(state, world, pos, context)
    }

    override fun mayPlaceOn(floor: BlockState, world: BlockGetter, pos: BlockPos): Boolean {
        val fluidState = world.getFluidState(pos)
        val fluidState2 = world.getFluidState(pos.above())
        return (fluidState.`is`(FluidTags.WATER) || floor.block is IceBlock) && fluidState2.type == Fluids.EMPTY
    }

    override fun getShape(state: BlockState, world: BlockGetter, pos: BlockPos, context: CollisionContext):
            VoxelShape = SHAPE

    companion object {
        val CODEC: MapCodec<WaterFernBlock> = simpleCodec(::WaterFernBlock)
        val SHAPE: VoxelShape = box(1.0, -2.0, 1.0, 15.0, 2.0, 15.0)
    }
}