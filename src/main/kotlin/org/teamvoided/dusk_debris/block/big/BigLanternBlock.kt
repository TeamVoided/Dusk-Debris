package org.teamvoided.dusk_debris.block.big

import com.mojang.serialization.MapCodec
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.LevelAccessor
import net.minecraft.world.level.LevelReader
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.SimpleWaterloggedBlock
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.world.level.block.state.properties.BooleanProperty
import net.minecraft.world.level.material.FluidState
import net.minecraft.world.level.material.Fluids
import net.minecraft.world.level.pathfinder.PathComputationType
import net.minecraft.world.phys.shapes.CollisionContext
import net.minecraft.world.phys.shapes.Shapes
import net.minecraft.world.phys.shapes.VoxelShape

open class BigLanternBlock(settings: Properties) : Block(settings), SimpleWaterloggedBlock {
    init {
        registerDefaultState(
            stateDefinition.any()
                .setValue(HANGING, false)
                .setValue(WATERLOGGED, false)
        )
    }
    public override fun codec(): MapCodec<BigLanternBlock> = CODEC
    override fun getStateForPlacement(ctx: BlockPlaceContext): BlockState? {
        val fluidState = ctx.level.getFluidState(ctx.clickedPos)
        val player = ctx.player?.isShiftKeyDown ?: false
        return defaultBlockState()
            .setValue(HANGING, player && ctx.clickedFace == Direction.DOWN)
            .setValue(WATERLOGGED, fluidState.type === Fluids.WATER)
    }

    override fun canSurvive(state: BlockState, world: LevelReader, pos: BlockPos): Boolean =
        canSupportCenter(world, pos.relative(Direction.DOWN), Direction.UP) ||
                canSupportCenter(world, pos.relative(Direction.UP), Direction.DOWN)

    override fun getShape(
        state: BlockState, world: BlockGetter, pos: BlockPos, context: CollisionContext
    ): VoxelShape = if (state.getValue(HANGING)) HANGING_SHAPE else SHAPE

    override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block, BlockState>) {
        builder.add(HANGING, WATERLOGGED)
    }

    override fun updateShape(
        state: BlockState, direction: Direction, neighborState: BlockState,
        world: LevelAccessor, pos: BlockPos, neighborPos: BlockPos
    ): BlockState {
        if (state.getValue(WATERLOGGED)) {
            world.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(world))
        }
        return super.updateShape(state, direction, neighborState, world, pos, neighborPos)
    }

    override fun getFluidState(state: BlockState): FluidState = if (state.getValue(WATERLOGGED)) Fluids.WATER.getSource(false) else super.getFluidState(state)
    override fun isPathfindable(state: BlockState, navigationType: PathComputationType): Boolean = false
    companion object {
        val CODEC: MapCodec<BigLanternBlock> = simpleCodec(::BigLanternBlock)
        val WATERLOGGED: BooleanProperty = BlockStateProperties.WATERLOGGED
        val HANGING: BooleanProperty = BlockStateProperties.HANGING
        val minSize = 2.5
        val maxSize = 13.5
        val minSizeTop = 4.5
        val maxSizeTop = 11.5
        protected val SHAPE: VoxelShape =
            Shapes.or(
                box(minSize, 0.0, minSize, maxSize, 13.0, maxSize),
                box(minSizeTop, 13.0, minSizeTop, maxSizeTop, 16.0, maxSizeTop)
            )
        protected val HANGING_SHAPE: VoxelShape =
            Shapes.or(
                box(minSize, 3.0, minSize, maxSize, 16.0, maxSize),
                box(minSizeTop, 0.0, minSizeTop, maxSizeTop, 3.0, maxSizeTop)
            )
    }
}