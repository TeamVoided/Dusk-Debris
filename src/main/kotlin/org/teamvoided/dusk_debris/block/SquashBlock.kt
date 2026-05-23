package org.teamvoided.dusk_debris.block

import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf
import net.minecraft.world.phys.shapes.CollisionContext
import net.minecraft.world.phys.shapes.Shapes
import net.minecraft.world.phys.shapes.VoxelShape

class SquashBlock(settings: Properties) : TallDirectionalBlock(settings) {

    override fun getShape(
        state: BlockState,
        world: BlockGetter,
        pos: BlockPos,
        context: CollisionContext
    ): VoxelShape {
        val direction = state.getValue(FACING).axis
        return if (state.getValue(HALF) == DoubleBlockHalf.UPPER) Shapes.block()
        else when (direction) {
            Direction.Axis.Z -> zShape
            Direction.Axis.X -> xShape
            Direction.Axis.Y -> yShape
            else -> yShape
        }
    }

    companion object {
        val yShape = box(
            2.0, 0.0, 2.0,
            14.0, 16.0, 14.0
        )
        val xShape = box(
            0.0, 2.0, 2.0,
            16.0, 14.0, 14.0
        )
        val zShape = box(
            2.0, 2.0, 0.0,
            14.0, 14.0, 16.0
        )
    }
}