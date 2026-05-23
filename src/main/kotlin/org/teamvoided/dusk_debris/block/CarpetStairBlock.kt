package org.teamvoided.dusk_debris.block

import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.LevelAccessor
import net.minecraft.world.level.LevelReader
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.CarpetBlock
import net.minecraft.world.level.block.StairBlock
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.world.level.block.state.properties.Half
import net.minecraft.world.level.block.state.properties.StairsShape
import net.minecraft.world.phys.shapes.CollisionContext
import net.minecraft.world.phys.shapes.Shapes
import net.minecraft.world.phys.shapes.VoxelShape
import org.teamvoided.dusk_debris.util.rotate

class CarpetStairBlock(settings: Properties) : CarpetBlock(settings) {
    init {
        this.registerDefaultState(
            stateDefinition.any()
                .setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.NORTH)
                .setValue(BlockStateProperties.STAIRS_SHAPE, StairsShape.STRAIGHT)
        )
    }

    override fun getStateForPlacement(ctx: BlockPlaceContext): BlockState? {
        val downState = ctx.level.getBlockState(ctx.clickedPos.below())
        val supr = super.getStateForPlacement(ctx)
        return if (supr != null && canSurvive(supr, ctx.level, ctx.clickedPos)) stateFromBelowStair(supr, downState)
        else null
    }

    override fun updateShape(
        state: BlockState,
        direction: Direction,
        neighborState: BlockState,
        world: LevelAccessor,
        pos: BlockPos,
        neighborPos: BlockPos
    ): BlockState {
        return if (!state.canSurvive(world, pos)) Blocks.AIR.defaultBlockState()
        else if (direction == Direction.DOWN) {
            val downState = world.getBlockState(pos.below())
            val supr = super.updateShape(state, direction, neighborState, world, pos, neighborPos)
            stateFromBelowStair(supr, downState)
        } else state
    }

    override fun canSurvive(state: BlockState, world: LevelReader, pos: BlockPos): Boolean {
        val downState = world.getBlockState(pos.below())
        return StairBlock.isStairs(downState) && downState.getValue(BlockStateProperties.HALF) == Half.BOTTOM
    }

    fun stateFromBelowStair(state: BlockState, downState: BlockState): BlockState {
        return state
            .setValue(BlockStateProperties.HORIZONTAL_FACING, downState.getValue(BlockStateProperties.HORIZONTAL_FACING))
            .setValue(BlockStateProperties.STAIRS_SHAPE, downState.getValue(BlockStateProperties.STAIRS_SHAPE))
    }

    override fun getShape(
        state: BlockState,
        world: BlockGetter,
        pos: BlockPos,
        context: CollisionContext
    ): VoxelShape {
        return when (state.getValue(BlockStateProperties.STAIRS_SHAPE)) {
            StairsShape.STRAIGHT -> STRAIGHT_SHAPE
            StairsShape.INNER_LEFT -> INNER_SHAPE
            StairsShape.INNER_RIGHT -> INNER_SHAPE.rotate(1)
            StairsShape.OUTER_LEFT -> OUTER_SHAPE
            StairsShape.OUTER_RIGHT -> OUTER_SHAPE.rotate(1)
            else -> super.getShape(state, world, pos, context)
        }.rotate(state.getValue(BlockStateProperties.HORIZONTAL_FACING).get2DDataValue())
    }

    override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block, BlockState>) {
        builder.add(BlockStateProperties.HORIZONTAL_FACING, BlockStateProperties.STAIRS_SHAPE)
    }

    companion object {
        val STRAIGHT_SHAPE: VoxelShape = Shapes.or(
            box(0.0, 0.0, 7.0, 16.0, 1.0, 16.0),
            box(0.0, -7.0, 7.0, 16.0, 0.0, 8.0),
            box(0.0, -8.0, 0.0, 16.0, -7.0, 8.0),
            box(0.0, -15.0, -1.0, 16.0, -7.0, 0.0)
        )
        val INNER_SHAPE: VoxelShape = Shapes.or(
            box(0.0, 0.0, 7.0, 16.0, 1.0, 16.0),
            box(7.0, 0.0, 0.0, 16.0, 1.0, 7.0),
            box(0.0, -8.0, 7.0, 7.0, 0.0, 8.0),
            box(7.0, -8.0, 0.0, 8.0, 0.0, 7.0),
            box(0.0, -8.0, 0.0, 7.0, -7.0, 7.0)
        )
        val OUTER_SHAPE: VoxelShape = Shapes.or(
            box(0.0, 0.0, 7.0, 16.0, 1.0, 16.0),
            box(7.0, 0.0, 0.0, 16.0, 1.0, 7.0),
            box(0.0, -8.0, 7.0, 7.0, 0.0, 8.0),
            box(7.0, -8.0, 0.0, 8.0, 0.0, 7.0),
            box(0.0, -8.0, 0.0, 7.0, -7.0, 7.0),
        )
    }
}