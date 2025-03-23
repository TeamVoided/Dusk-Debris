package org.teamvoided.dusk_debris.block

import net.minecraft.block.BlockState
import net.minecraft.block.ShapeContext
import net.minecraft.block.enums.DoubleBlockHalf
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.shape.VoxelShape
import net.minecraft.util.shape.VoxelShapes
import net.minecraft.world.BlockView

class SquashBlock(settings: Settings) : TallDirectionalBlock(settings) {

    override fun getOutlineShape(
        state: BlockState,
        world: BlockView,
        pos: BlockPos,
        context: ShapeContext
    ): VoxelShape {
        val direction = state.get(FACING).axis
        return if (state.get(HALF) == DoubleBlockHalf.UPPER) VoxelShapes.fullCube()
        else when (direction) {
            Direction.Axis.Z -> zShape
            Direction.Axis.X -> xShape
            Direction.Axis.Y -> yShape
            else -> yShape
        }
    }

    companion object {
        val yShape = createCuboidShape(
            2.0, 0.0, 2.0,
            14.0, 16.0, 14.0
        )
        val xShape = createCuboidShape(
            0.0, 2.0, 2.0,
            16.0, 14.0, 14.0
        )
        val zShape = createCuboidShape(
            2.0, 2.0, 0.0,
            14.0, 14.0, 16.0
        )
    }
}