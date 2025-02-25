package org.teamvoided.dusk_debris.block

import net.minecraft.block.*
import net.minecraft.block.enums.BlockHalf
import net.minecraft.block.enums.StairShape
import net.minecraft.item.ItemPlacementContext
import net.minecraft.state.StateManager
import net.minecraft.state.property.Properties
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.shape.VoxelShape
import net.minecraft.util.shape.VoxelShapes
import net.minecraft.world.BlockView
import net.minecraft.world.WorldAccess
import net.minecraft.world.WorldView
import org.teamvoided.dusk_debris.util.rotate

class CarpetStairBlock(settings: Settings) : CarpetBlock(settings) {
    init {
        this.defaultState = stateManager.defaultState
            .with(Properties.HORIZONTAL_FACING, Direction.NORTH)
            .with(Properties.STAIR_SHAPE, StairShape.STRAIGHT)
    }

    override fun getPlacementState(ctx: ItemPlacementContext): BlockState? {
        val downState = ctx.world.getBlockState(ctx.blockPos.down())
        val supr = super.getPlacementState(ctx)
        return if (supr != null && canPlaceAt(supr, ctx.world, ctx.blockPos)) stateFromBelowStair(supr, downState)
        else null
    }

    override fun getStateForNeighborUpdate(
        state: BlockState,
        direction: Direction,
        neighborState: BlockState,
        world: WorldAccess,
        pos: BlockPos,
        neighborPos: BlockPos
    ): BlockState {
        return if (!state.canPlaceAt(world, pos)) Blocks.AIR.defaultState
        else if (direction == Direction.DOWN) {
            val downState = world.getBlockState(pos.down())
            val supr = super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos)
            stateFromBelowStair(supr, downState)
        } else state
    }

    override fun canPlaceAt(state: BlockState, world: WorldView, pos: BlockPos): Boolean {
        val downState = world.getBlockState(pos.down())
        return StairsBlock.isStairs(downState) && downState.get(Properties.BLOCK_HALF) == BlockHalf.BOTTOM
    }

    fun stateFromBelowStair(state: BlockState, downState: BlockState): BlockState {
        return state
            .with(Properties.HORIZONTAL_FACING, downState.get(Properties.HORIZONTAL_FACING))
            .with(Properties.STAIR_SHAPE, downState.get(Properties.STAIR_SHAPE))
    }

    override fun getOutlineShape(
        state: BlockState,
        world: BlockView,
        pos: BlockPos,
        context: ShapeContext
    ): VoxelShape {
        return when (state.get(Properties.STAIR_SHAPE)) {
            StairShape.STRAIGHT -> STRAIGHT_SHAPE
            StairShape.INNER_LEFT -> INNER_SHAPE
            StairShape.INNER_RIGHT -> INNER_SHAPE.rotate(1)
            StairShape.OUTER_LEFT -> OUTER_SHAPE
            StairShape.OUTER_RIGHT -> OUTER_SHAPE.rotate(1)
            else -> super.getOutlineShape(state, world, pos, context)
        }.rotate(state.get(Properties.HORIZONTAL_FACING).horizontal)
    }

    override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) {
        builder.add(Properties.HORIZONTAL_FACING, Properties.STAIR_SHAPE)
    }

    companion object {
        val STRAIGHT_SHAPE: VoxelShape = VoxelShapes.union(
            createCuboidShape(0.0, 0.0, 7.0, 16.0, 1.0, 16.0),
            createCuboidShape(0.0, -7.0, 7.0, 16.0, 0.0, 8.0),
            createCuboidShape(0.0, -8.0, 0.0, 16.0, -7.0, 8.0),
            createCuboidShape(0.0, -15.0, -1.0, 16.0, -7.0, 0.0)
        )
        val INNER_SHAPE: VoxelShape = VoxelShapes.union(
            createCuboidShape(0.0, 0.0, 7.0, 16.0, 1.0, 16.0),
            createCuboidShape(7.0, 0.0, 0.0, 16.0, 1.0, 7.0),
            createCuboidShape(0.0, -8.0, 7.0, 7.0, 0.0, 8.0),
            createCuboidShape(7.0, -8.0, 0.0, 8.0, 0.0, 7.0),
            createCuboidShape(0.0, -8.0, 0.0, 7.0, -7.0, 7.0)
        )
        val OUTER_SHAPE: VoxelShape = VoxelShapes.union(
            createCuboidShape(0.0, 0.0, 7.0, 16.0, 1.0, 16.0),
            createCuboidShape(7.0, 0.0, 0.0, 16.0, 1.0, 7.0),
            createCuboidShape(0.0, -8.0, 7.0, 7.0, 0.0, 8.0),
            createCuboidShape(7.0, -8.0, 0.0, 8.0, 0.0, 7.0),
            createCuboidShape(0.0, -8.0, 0.0, 7.0, -7.0, 7.0),
        )
    }
}