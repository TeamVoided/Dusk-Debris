package org.teamvoided.dusk_debris.block.sot

import com.mojang.serialization.MapCodec
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.LevelAccessor
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.RotatedPillarBlock
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

class RibbonBlock(settings: Properties) : RotatedPillarBlock(settings), SimpleWaterloggedBlock {
    override fun codec(): MapCodec<RibbonBlock> {
        return CODEC
    }

    init {
        this.registerDefaultState(
            ((stateDefinition.any())
                .setValue(WATERLOGGED, false))
                .setValue(AXIS, Direction.Axis.Y)
        )
    }

    override fun getShape(
        state: BlockState,
        world: BlockGetter,
        pos: BlockPos,
        context: CollisionContext
    ): VoxelShape {
        return when (state.getValue(AXIS)) {
            Direction.Axis.X -> X_SHAPE
            Direction.Axis.Z -> Z_SHAPE
            Direction.Axis.Y -> Y_SHAPE
            else -> X_SHAPE
        }
    }

    override fun getCollisionShape(
        state: BlockState?,
        world: BlockGetter?,
        pos: BlockPos?,
        context: CollisionContext?
    ): VoxelShape {
        return Shapes.empty()
    }

    override fun getStateForPlacement(ctx: BlockPlaceContext): BlockState {
        val fluidState = ctx.level.getFluidState(ctx.clickedPos)
        val bl = fluidState.type === Fluids.WATER
        return super.getStateForPlacement(ctx)!!
            .setValue(WATERLOGGED, bl)
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
        return super.updateShape(state, direction, neighborState, world, pos, neighborPos)
    }

    override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block, BlockState>) {
        builder.add(WATERLOGGED).add(AXIS)
    }

    override fun getFluidState(state: BlockState): FluidState {
        return if (state.getValue(WATERLOGGED)) Fluids.WATER.getSource(false)
        else super.getFluidState(state)
    }

    override fun isPathfindable(state: BlockState, navigationType: PathComputationType): Boolean {
        return false
    }

    companion object {
        val CODEC: MapCodec<RibbonBlock> = simpleCodec { settings: Properties ->
            RibbonBlock(
                settings
            )
        }
        val WATERLOGGED: BooleanProperty = BlockStateProperties.WATERLOGGED
        protected const val SHAPE_MIN = 6.5
        protected const val SHAPE_MAX = 9.5
        protected val X_SHAPE: VoxelShape = box(0.0, SHAPE_MIN, SHAPE_MIN, 16.0, SHAPE_MAX, SHAPE_MAX)
        protected val Y_SHAPE: VoxelShape = box(SHAPE_MIN, 0.0, SHAPE_MIN, SHAPE_MAX, 16.0, SHAPE_MAX)
        protected val Z_SHAPE: VoxelShape = box(SHAPE_MIN, SHAPE_MIN, 0.0, SHAPE_MAX, SHAPE_MAX, 16.0)
    }
}