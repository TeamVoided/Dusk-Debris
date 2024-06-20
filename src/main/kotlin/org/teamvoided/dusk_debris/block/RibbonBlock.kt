//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//
package net.minecraft.block

import com.mojang.serialization.MapCodec
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

class RibbonBlock(settings: Settings) : Block(settings), Waterloggable {
    override fun getCodec(): MapCodec<RibbonBlock> {
        return CODEC
    }

    override fun getOutlineShape(
        state: BlockState,
        world: BlockView,
        pos: BlockPos,
        context: ShapeContext
    ): VoxelShape {
        return SHAPE
    }

    override fun getPlacementState(ctx: ItemPlacementContext): BlockState {
        val fluidState = ctx.world.getFluidState(ctx.blockPos)
        val bl = fluidState.fluid === Fluids.WATER
        return super.getPlacementState(ctx)!!.with(WATERLOGGED, bl)
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
        builder.add(WATERLOGGED)
    }

    override fun getFluidState(state: BlockState): FluidState {
        return if (state.get(WATERLOGGED)) Fluids.WATER.getStill(false) else super.getFluidState(state)
    }

    override fun canPathfindThrough(state: BlockState, navigationType: NavigationType): Boolean {
        return false
    }

    init {
        this.defaultState = ((stateManager.defaultState).with(WATERLOGGED, false))
    }

    companion object {
        val CODEC: MapCodec<RibbonBlock> = createCodec { settings: Settings ->
            RibbonBlock(
                settings
            )
        }
        val WATERLOGGED: BooleanProperty = Properties.WATERLOGGED
        protected val SHAPE: VoxelShape = createCuboidShape(6.5, 0.0, 6.5, 9.5, 16.0, 9.5)
    }
}