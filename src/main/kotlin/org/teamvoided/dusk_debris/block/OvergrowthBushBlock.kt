package org.teamvoided.dusk_debris.block

import net.minecraft.block.AzaleaBlock
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.ShapeContext
import net.minecraft.block.Waterloggable
import net.minecraft.block.sapling.TreeGrower
import net.minecraft.fluid.FluidState
import net.minecraft.fluid.Fluids
import net.minecraft.item.ItemPlacementContext
import net.minecraft.server.world.ServerWorld
import net.minecraft.state.StateManager
import net.minecraft.state.property.Properties
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.random.RandomGenerator
import net.minecraft.util.shape.VoxelShape
import net.minecraft.world.BlockView
import net.minecraft.world.WorldAccess
import net.minecraft.world.WorldView
import org.teamvoided.dusk_debris.util.rotateFromDown

class OvergrowthBushBlock(settings: Settings) : AzaleaBlock(settings), Waterloggable {
    init {
        this.defaultState = stateManager.defaultState
            .with(Properties.FACING, Direction.DOWN)
            .with(Properties.WATERLOGGED, false)
    }

    override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) {
        builder.add(Properties.FACING, Properties.WATERLOGGED)
    }

    override fun canPlaceAt(state: BlockState, world: WorldView, pos: BlockPos): Boolean {
        val blockPos = pos.offset(state.get(Properties.FACING))
        return this.canPlantOnTop(world.getBlockState(blockPos), world, blockPos)
    }

    override fun isFertilizable(world: WorldView, pos: BlockPos, state: BlockState): Boolean {
        return world.getBlockState(pos.offset(state.get(Properties.FACING).opposite)).materialReplaceable()
    }

    override fun fertilize(world: ServerWorld, random: RandomGenerator, pos: BlockPos, state: BlockState) {
        TreeGrower.AZALEA.growTree(world, world.chunkManager.chunkGenerator, pos, state, random)
    }

    override fun getOutlineShape(
        state: BlockState,
        world: BlockView,
        pos: BlockPos,
        context: ShapeContext
    ): VoxelShape {
        return super.getOutlineShape(state, world, pos, context).rotateFromDown(state.get(Properties.FACING))
    }

    override fun getStateForNeighborUpdate(
        state: BlockState,
        direction: Direction,
        neighborState: BlockState,
        world: WorldAccess,
        pos: BlockPos,
        neighborPos: BlockPos
    ): BlockState {
        if (state.get(Properties.WATERLOGGED)) {
            world.scheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world))
        }
        return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos)
    }

    override fun getPlacementState(ctx: ItemPlacementContext): BlockState {
        return super.getPlacementState(ctx)!!
            .with(Properties.WATERLOGGED, ctx.world.getFluidState(ctx.blockPos).fluid == Fluids.WATER)
            .with(Properties.FACING, ctx.side.opposite)
    }

    override fun getFluidState(state: BlockState): FluidState {
        return if (state.get(Properties.WATERLOGGED)) Fluids.WATER.getStill(false)
        else super.getFluidState(state)
    }

    companion object {

    }
}