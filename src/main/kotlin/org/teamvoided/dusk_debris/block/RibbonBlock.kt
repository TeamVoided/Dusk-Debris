package org.teamvoided.dusk_debris.block

import com.mojang.serialization.MapCodec
import net.minecraft.block.*
import net.minecraft.entity.ai.pathing.NavigationType
import net.minecraft.fluid.FluidState
import net.minecraft.fluid.Fluids
import net.minecraft.item.ItemPlacementContext
import net.minecraft.state.StateManager
import net.minecraft.state.property.BooleanProperty
import net.minecraft.state.property.Properties
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.shape.VoxelShape
import net.minecraft.util.shape.VoxelShapes
import net.minecraft.world.BlockView
import net.minecraft.world.WorldAccess

class RibbonBlock(settings: Settings) : PillarBlock(settings), Waterloggable {
    override fun getCodec(): MapCodec<RibbonBlock> {
        return CODEC
    }

    init {
        this.defaultState = ((stateManager.defaultState)
            .with(WATERLOGGED, false))
            .with(AXIS, Direction.Axis.Y)
    }

    override fun getOutlineShape(
        state: BlockState,
        world: BlockView,
        pos: BlockPos,
        context: ShapeContext
    ): VoxelShape {
        return when (state.get(AXIS)) {
            Direction.Axis.X -> X_SHAPE
            Direction.Axis.Z -> Z_SHAPE
            Direction.Axis.Y -> Y_SHAPE
            else -> X_SHAPE
        }
    }

    override fun getCollisionShape(
        state: BlockState?,
        world: BlockView?,
        pos: BlockPos?,
        context: ShapeContext?
    ): VoxelShape {
        return VoxelShapes.empty()
    }

    override fun getPlacementState(ctx: ItemPlacementContext): BlockState {
        val fluidState = ctx.world.getFluidState(ctx.blockPos)
        val bl = fluidState.fluid === Fluids.WATER
        return super.getPlacementState(ctx)!!
            .with(WATERLOGGED, bl)
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
        return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos)
    }

    override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) {
        builder.add(WATERLOGGED).add(AXIS)
    }

    override fun getFluidState(state: BlockState): FluidState {
        return if (state.get(WATERLOGGED)) Fluids.WATER.getStill(false)
        else super.getFluidState(state)
    }

    override fun canPathfindThrough(state: BlockState, navigationType: NavigationType): Boolean {
        return false
    }

    companion object {
        val CODEC: MapCodec<RibbonBlock> = createCodec { settings: Settings ->
            RibbonBlock(
                settings
            )
        }
        val WATERLOGGED: BooleanProperty = Properties.WATERLOGGED
        protected const val SHAPE_MIN = 6.5
        protected const val SHAPE_MAX = 9.5
        protected val X_SHAPE: VoxelShape = createCuboidShape(0.0, SHAPE_MIN, SHAPE_MIN, 16.0, SHAPE_MAX, SHAPE_MAX)
        protected val Y_SHAPE: VoxelShape = createCuboidShape(SHAPE_MIN, 0.0, SHAPE_MIN, SHAPE_MAX, 16.0, SHAPE_MAX)
        protected val Z_SHAPE: VoxelShape = createCuboidShape(SHAPE_MIN, SHAPE_MIN, 0.0, SHAPE_MAX, SHAPE_MAX, 16.0)
    }
}