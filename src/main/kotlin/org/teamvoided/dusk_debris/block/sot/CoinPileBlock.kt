package org.teamvoided.dusk_debris.block.sot

import com.mojang.serialization.MapCodec
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.server.level.ServerLevel
import net.minecraft.util.RandomSource
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
import net.minecraft.world.level.block.state.properties.IntegerProperty
import net.minecraft.world.level.material.FluidState
import net.minecraft.world.level.material.Fluids
import net.minecraft.world.level.pathfinder.PathComputationType
import net.minecraft.world.phys.shapes.CollisionContext
import net.minecraft.world.phys.shapes.Shapes
import net.minecraft.world.phys.shapes.VoxelShape

class CoinPileBlock(settings: Properties) : Block(settings), SimpleWaterloggedBlock {
    public override fun codec(): MapCodec<CoinPileBlock> {
        return CODEC
    }

    init {
        this.registerDefaultState(
            stateDefinition.any()
                .setValue(LAYERS, 1)
                .setValue(WATERLOGGED, false)
        )
    }

    override fun isPathfindable(state: BlockState, navigationType: PathComputationType): Boolean {
        return when (navigationType) {
            PathComputationType.LAND -> state.getValue(LAYERS) < IMPASSABLE_HEIGHT
            PathComputationType.WATER -> false
            PathComputationType.AIR -> false
            else -> false
        }
    }

    override fun getShape(
        state: BlockState,
        world: BlockGetter,
        pos: BlockPos,
        context: CollisionContext
    ): VoxelShape {
        return LAYERS_TO_SHAPE[state.getValue(LAYERS)]
    }

    override fun getCollisionShape(
        state: BlockState,
        world: BlockGetter,
        pos: BlockPos,
        context: CollisionContext
    ): VoxelShape {
        return LAYERS_TO_SHAPE[state.getValue(LAYERS)]
    }

    override fun getBlockSupportShape(state: BlockState, world: BlockGetter, pos: BlockPos): VoxelShape {
        return LAYERS_TO_SHAPE[state.getValue(LAYERS)]
    }

    override fun getVisualShape(
        state: BlockState,
        world: BlockGetter,
        pos: BlockPos,
        context: CollisionContext
    ): VoxelShape {
        return LAYERS_TO_SHAPE[state.getValue(LAYERS)]
    }

    override fun useShapeForLightOcclusion(state: BlockState): Boolean {
        return true
    }

    override fun getShadeBrightness(state: BlockState, world: BlockGetter, pos: BlockPos): Float {
        return if (state.getValue(LAYERS) == MAX_LAYERS) 0.2f else 1.0f
    }

    override fun canSurvive(state: BlockState, world: LevelReader, pos: BlockPos): Boolean {
        val blockStateDown = world.getBlockState(pos.below())
        return isFaceFull(blockStateDown.getCollisionShape(world, pos.below()), Direction.UP) ||
                (blockStateDown.`is`(this) && blockStateDown.getValue(LAYERS) == MAX_LAYERS)
    }

    override fun updateShape(
        state: BlockState,
        direction: Direction,
        neighborState: BlockState,
        world: LevelAccessor,
        pos: BlockPos,
        neighborPos: BlockPos
    ): BlockState {
        if (state.getValue(WATERLOGGED)) {
            world.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(world))
        }
        if (!state.canSurvive(world, pos)) {
            world.scheduleTick(pos, this, 1)
        }
        return super.updateShape(state, direction, neighborState, world, pos, neighborPos)
    }

    override fun tick(state: BlockState, world: ServerLevel, pos: BlockPos?, random: RandomSource?) {
        if (!state.canSurvive(world, pos)) {
            world.destroyBlock(pos, true)
        }
    }

    override fun canBeReplaced(state: BlockState, context: BlockPlaceContext): Boolean {
        val layers = state.getValue(LAYERS)
        return if (context.itemInHand.`is`(this.asItem()) && layers < MAX_LAYERS) {
            if (context.replacingClickedOnBlock()) {
                context.clickedFace == Direction.UP
            } else {
                true
            }
        } else false
    }

    override fun getStateForPlacement(ctx: BlockPlaceContext): BlockState {
        var waterlog = ctx.level.getFluidState(ctx.clickedPos).type == Fluids.WATER
        val blockState = ctx.level.getBlockState(ctx.clickedPos)
        if (blockState.`is`(this)) {
            return blockState.cycle(LAYERS)
        }
        return defaultBlockState().setValue(WATERLOGGED, waterlog)
    }

    override fun getFluidState(state: BlockState): FluidState {
        return if (state.getValue(WATERLOGGED)) Fluids.WATER.getSource(false)
        else super.getFluidState(state)
    }

    override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block, BlockState>) {
        builder.add(LAYERS, WATERLOGGED)
    }

    companion object {
        val CODEC: MapCodec<CoinPileBlock> = simpleCodec(::CoinPileBlock)
        const val MAX_LAYERS: Int = 8
        const val IMPASSABLE_HEIGHT: Int = 5
        val LAYERS: IntegerProperty = BlockStateProperties.LAYERS
        val WATERLOGGED: BooleanProperty = BlockStateProperties.WATERLOGGED
        protected val LAYERS_TO_SHAPE: Array<VoxelShape> = arrayOf(
            Shapes.empty(),
            box(0.0, 0.0, 0.0, 16.0, 2.0, 16.0),
            box(0.0, 0.0, 0.0, 16.0, 4.0, 16.0),
            box(0.0, 0.0, 0.0, 16.0, 6.0, 16.0),
            box(0.0, 0.0, 0.0, 16.0, 8.0, 16.0),
            box(0.0, 0.0, 0.0, 16.0, 10.0, 16.0),
            box(0.0, 0.0, 0.0, 16.0, 12.0, 16.0),
            box(0.0, 0.0, 0.0, 16.0, 14.0, 16.0),
            box(0.0, 0.0, 0.0, 16.0, 16.0, 16.0)
        )
    }
}