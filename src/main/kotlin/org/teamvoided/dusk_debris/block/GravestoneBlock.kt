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
import net.minecraft.world.BlockView
import net.minecraft.world.WorldAccess
import org.teamvoided.dusk_debris.util.centerGravestoneShape
import org.teamvoided.dusk_debris.util.gravestoneShape
import org.teamvoided.dusk_debris.util.rotate

open class GravestoneBlock(val shape: VoxelShape, val centerShape: VoxelShape, settings: Settings) :
    HorizontalFacingBlock(settings), Waterloggable {
    init {
        this.defaultState = stateManager.defaultState
            .with(Properties.WATERLOGGED, false)
            .with(CENTERED, true)
            .with(FACING, Direction.NORTH)
    }

    public override fun getCodec(): MapCodec<GravestoneBlock> = CODEC
    override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) {
        super.appendProperties(builder)
        builder.add(Properties.WATERLOGGED)
        builder.add(CENTERED)
        builder.add(FACING)
    }

    override fun getStateForNeighborUpdate(
        state: BlockState, direction: Direction, neighborState: BlockState,
        world: WorldAccess, pos: BlockPos, neighborPos: BlockPos
    ): BlockState {
        if (state.get(Properties.WATERLOGGED)) {
            world.scheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world))
        }
        return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos)
    }

    override fun getFluidState(state: BlockState): FluidState {
        return if (state.get(Properties.WATERLOGGED)) Fluids.WATER.getStill(false)
        else super.getFluidState(state)
    }

    override fun getPlacementState(ctx: ItemPlacementContext): BlockState {
        val fluidState = ctx.world.getFluidState(ctx.blockPos)
        val player = ctx.player?.isSneaking == true
        return defaultState
            .with(Properties.WATERLOGGED, fluidState.isOf(Fluids.WATER))
            .with(CENTERED, !player)
            .with(FACING, ctx.playerFacing.opposite)
    }

    override fun getOutlineShape(
        state: BlockState, world: BlockView, pos: BlockPos, context: ShapeContext
    ): VoxelShape {
        val rotations = state.get(FACING).horizontal
        val shape = if (state.get(CENTERED)) centerShape else shape
        return shape.rotate(rotations)
    }

    override fun canPathfindThrough(state: BlockState, navigationType: NavigationType): Boolean = false

    companion object {
        val CENTERED: BooleanProperty = BooleanProperty.of("centered")
        val CODEC: MapCodec<GravestoneBlock> =
            createCodec { GravestoneBlock(gravestoneShape, centerGravestoneShape, it) }
    }
}