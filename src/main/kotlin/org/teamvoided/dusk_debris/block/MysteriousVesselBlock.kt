package org.teamvoided.dusk_debris.block

import com.mojang.serialization.MapCodec
import net.minecraft.block.*
import net.minecraft.fluid.FluidState
import net.minecraft.fluid.Fluids
import net.minecraft.item.ItemPlacementContext
import net.minecraft.state.StateManager
import net.minecraft.state.property.BooleanProperty
import net.minecraft.state.property.DirectionProperty
import net.minecraft.state.property.Properties
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.shape.VoxelShape
import net.minecraft.util.shape.VoxelShapes
import net.minecraft.world.*

open class MysteriousVesselBlock(settings: Settings) : HorizontalFacingBlock(settings), Waterloggable {
    override fun getCodec(): MapCodec<out HorizontalFacingBlock> {
        return CODEC
    }

    init {
        this.defaultState =
            (stateManager.defaultState)
                .with(FACING, Direction.NORTH)
                .with(WATERLOGGED, false)
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
        return super.getStateForNeighborUpdate(
            state,
            direction,
            neighborState,
            world,
            pos,
            neighborPos
        )
    }

    override fun getPlacementState(ctx: ItemPlacementContext): BlockState {
        val waterlog = ctx.world.getFluidState(ctx.blockPos).fluid === Fluids.WATER
        return defaultState
            .with(FACING, ctx.playerFacing.opposite)
            .with(WATERLOGGED, waterlog)
    }

    override fun getFluidState(state: BlockState): FluidState {
        return if (state.get(WATERLOGGED)) Fluids.WATER.getStill(false)
        else super.getFluidState(state)
    }

    override fun canPlaceAt(state: BlockState, world: WorldView, pos: BlockPos): Boolean {
        return sideCoversSmallSquare(world, pos.offset(Direction.DOWN), Direction.DOWN.opposite)
    }

    override fun getOutlineShape(
        state: BlockState,
        world: BlockView,
        pos: BlockPos,
        context: ShapeContext
    ): VoxelShape {
        return SHAPE
    }

    override fun getCullingShape(state: BlockState, world: BlockView, pos: BlockPos): VoxelShape {
        return VoxelShapes.empty()
    }

    override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) {
        builder.add(FACING, WATERLOGGED)
    }


    companion object {
        val CODEC: MapCodec<MysteriousVesselBlock> = createCodec { settings: Settings ->
            MysteriousVesselBlock(
                settings
            )
        }
        val SHAPE: VoxelShape = VoxelShapes.union(
            createCuboidShape(5.0, 0.0, 5.0, 11.0, 8.0, 11.0),
            createCuboidShape(6.0, 8.0, 6.0, 10.0, 10.0, 10.0)
        )
        val FACING: DirectionProperty = HorizontalFacingBlock.FACING
        val WATERLOGGED: BooleanProperty = Properties.WATERLOGGED
    }
}