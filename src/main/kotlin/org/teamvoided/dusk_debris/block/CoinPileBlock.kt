package org.teamvoided.dusk_debris.block

import com.mojang.serialization.MapCodec
import net.minecraft.block.*
import net.minecraft.entity.ai.pathing.NavigationType
import net.minecraft.fluid.FluidState
import net.minecraft.fluid.Fluids
import net.minecraft.item.ItemPlacementContext
import net.minecraft.server.world.ServerWorld
import net.minecraft.state.StateManager
import net.minecraft.state.property.BooleanProperty
import net.minecraft.state.property.IntProperty
import net.minecraft.state.property.Properties
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.random.RandomGenerator
import net.minecraft.util.shape.VoxelShape
import net.minecraft.util.shape.VoxelShapes
import net.minecraft.world.BlockView
import net.minecraft.world.WorldAccess
import net.minecraft.world.WorldView

class CoinPileBlock(settings: Settings) : Block(settings), Waterloggable {
    public override fun getCodec(): MapCodec<CoinPileBlock> {
        return CODEC
    }

    init {
        this.defaultState = stateManager.defaultState
            .with(LAYERS, 1)
            .with(WATERLOGGED, false)
    }

    override fun canPathfindThrough(state: BlockState, navigationType: NavigationType): Boolean {
        return when (navigationType) {
            NavigationType.LAND -> state.get(LAYERS) < IMPASSABLE_HEIGHT
            NavigationType.WATER -> false
            NavigationType.AIR -> false
            else -> false
        }
    }

    override fun getOutlineShape(
        state: BlockState,
        world: BlockView,
        pos: BlockPos,
        context: ShapeContext
    ): VoxelShape {
        return LAYERS_TO_SHAPE[state.get(LAYERS)]
    }

    override fun getCollisionShape(
        state: BlockState,
        world: BlockView,
        pos: BlockPos,
        context: ShapeContext
    ): VoxelShape {
        return LAYERS_TO_SHAPE[state.get(LAYERS)]
    }

    override fun getSidesShape(state: BlockState, world: BlockView, pos: BlockPos): VoxelShape {
        return LAYERS_TO_SHAPE[state.get(LAYERS)]
    }

    override fun getCameraCollisionShape(
        state: BlockState,
        world: BlockView,
        pos: BlockPos,
        context: ShapeContext
    ): VoxelShape {
        return LAYERS_TO_SHAPE[state.get(LAYERS)]
    }

    override fun hasSidedTransparency(state: BlockState): Boolean {
        return true
    }

    override fun getAmbientOcclusionLightLevel(state: BlockState, world: BlockView, pos: BlockPos): Float {
        return if (state.get(LAYERS) == MAX_LAYERS) 0.2f else 1.0f
    }

    override fun canPlaceAt(state: BlockState, world: WorldView, pos: BlockPos): Boolean {
        val blockStateDown = world.getBlockState(pos.down())
        return isFaceFullSquare(blockStateDown.getCollisionShape(world, pos.down()), Direction.UP) ||
                (blockStateDown.isOf(this) && blockStateDown.get(LAYERS) == MAX_LAYERS)
    }

    override fun getStateForNeighborUpdate(
        state: BlockState,
        direction: Direction,
        neighborState: BlockState,
        world: WorldAccess,
        pos: BlockPos,
        neighborPos: BlockPos
    ): BlockState {
        if (state.get(WATERLOGGED)) {
            world.scheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world))
        }
        if (!state.canPlaceAt(world, pos)) {
            world.scheduleBlockTick(pos, this, 1)
        }
        return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos)
    }

    override fun scheduledTick(state: BlockState, world: ServerWorld, pos: BlockPos?, random: RandomGenerator?) {
        if (!state.canPlaceAt(world, pos)) {
            world.breakBlock(pos, true)
        }
    }

    override fun canReplace(state: BlockState, context: ItemPlacementContext): Boolean {
        val layers = state.get(LAYERS)
        return if (context.stack.isOf(this.asItem()) && layers < MAX_LAYERS) {
            if (context.canReplaceExisting()) {
                context.side == Direction.UP
            } else {
                true
            }
        } else false
    }

    override fun getPlacementState(ctx: ItemPlacementContext): BlockState {
        var waterlog = ctx.world.getFluidState(ctx.blockPos).fluid == Fluids.WATER
        val blockState = ctx.world.getBlockState(ctx.blockPos)
        if (blockState.isOf(this)) {
            return blockState.cycle(LAYERS)
        }
        return defaultState.with(WATERLOGGED, waterlog)
    }

    override fun getFluidState(state: BlockState): FluidState {
        return if (state.get(WATERLOGGED)) Fluids.WATER.getStill(false)
        else super.getFluidState(state)
    }

    override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) {
        builder.add(LAYERS, WATERLOGGED)
    }

    companion object {
        val CODEC: MapCodec<CoinPileBlock> = createCodec { settings: Settings ->
            CoinPileBlock(
                settings
            )
        }
        const val MAX_LAYERS: Int = 8
        const val IMPASSABLE_HEIGHT: Int = 5
        val LAYERS: IntProperty = Properties.LAYERS
        val WATERLOGGED: BooleanProperty = Properties.WATERLOGGED
        protected val LAYERS_TO_SHAPE: Array<VoxelShape> = arrayOf(
            VoxelShapes.empty(),
            createCuboidShape(0.0, 0.0, 0.0, 16.0, 2.0, 16.0),
            createCuboidShape(0.0, 0.0, 0.0, 16.0, 4.0, 16.0),
            createCuboidShape(0.0, 0.0, 0.0, 16.0, 6.0, 16.0),
            createCuboidShape(0.0, 0.0, 0.0, 16.0, 8.0, 16.0),
            createCuboidShape(0.0, 0.0, 0.0, 16.0, 10.0, 16.0),
            createCuboidShape(0.0, 0.0, 0.0, 16.0, 12.0, 16.0),
            createCuboidShape(0.0, 0.0, 0.0, 16.0, 14.0, 16.0),
            createCuboidShape(0.0, 0.0, 0.0, 16.0, 16.0, 16.0)
        )
    }
}