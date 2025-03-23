package org.teamvoided.dusk_debris.block

import com.mojang.serialization.MapCodec
import net.minecraft.block.*
import net.minecraft.fluid.FluidState
import net.minecraft.fluid.Fluids
import net.minecraft.item.ItemPlacementContext
import net.minecraft.registry.tag.BlockTags
import net.minecraft.server.world.ServerWorld
import net.minecraft.state.StateManager
import net.minecraft.state.property.BooleanProperty
import net.minecraft.state.property.EnumProperty
import net.minecraft.state.property.Properties
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.random.RandomGenerator
import net.minecraft.world.World
import net.minecraft.world.WorldAccess
import net.minecraft.world.WorldView

class PaintedRoseBlock(settings: Settings) : AbstractPlantBlock(settings), Waterloggable, Fertilizable {

    init {
        this.defaultState = stateManager.defaultState
            .with(SECTION, TripleBlockSection.TOP)
            .with(WATERLOGGED, false)
    }

    override fun getCodec(): MapCodec<out AbstractPlantBlock> = CODEC

    override fun getPlacementState(ctx: ItemPlacementContext): BlockState =
        getSection(ctx.world, ctx.blockPos, defaultState)

    override fun canPlaceAt(state: BlockState, world: WorldView, pos: BlockPos): Boolean {
        val blockPosDown = pos.down()
        val blockState = world.getBlockState(blockPosDown)
        return blockState.isOf(this) || super.canPlaceAt(state, world, pos)
    }

    override fun getStateForNeighborUpdate(
        state: BlockState,
        direction: Direction,
        neighborState: BlockState,
        world: WorldAccess,
        pos: BlockPos,
        neighborPos: BlockPos
    ): BlockState {
        if (direction == Direction.DOWN) {
            if (!state.canPlaceAt(world, pos)) {
                world.scheduleBlockTick(pos, this, 1)
                return state
            }
        } else if (direction == Direction.UP) {
            return getSection(world, pos, state)
        }
        return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos)
    }

    override fun scheduledTick(state: BlockState, world: ServerWorld, pos: BlockPos, random: RandomGenerator) {
        if (!state.canPlaceAt(world, pos)) {
            world.breakBlock(pos, true)
        }
    }

    fun getSection(world: WorldAccess, pos: BlockPos, thisState: BlockState): BlockState {
        val returnState = if (!world.getBlockState(pos.up()).isOf(this)) {
            thisState.with(SECTION, TripleBlockSection.TOP)
        } else if (world.getBlockState(pos.down()).isSideSolidFullSquare(world, pos.down(), Direction.UP)) {
            thisState.with(SECTION, TripleBlockSection.BOTTOM)
        } else {
            thisState.with(SECTION, TripleBlockSection.MIDDLE)
        }
        return returnState.with(WATERLOGGED, world.getFluidState(pos).fluid == Fluids.WATER)
    }

    override fun isFertilizable(world: WorldView, pos: BlockPos, state: BlockState): Boolean {
        return true
    }

    override fun canFertilize(world: World, random: RandomGenerator, pos: BlockPos, state: BlockState): Boolean {
        return true
    }

    override fun fertilize(world: ServerWorld, random: RandomGenerator, pos: BlockPos, state: BlockState) {
        var blockPos = pos.up()
        val length: Int = random.nextInt(random.nextInt(5))

        var loop = 0
        var worldBlockState = world.getBlockState(blockPos)
        while (loop <= length && (worldBlockState.isIn(BlockTags.REPLACEABLE) || worldBlockState.isOf(this))) {
            world.setBlockState(blockPos, getSection(world, blockPos, state))
            blockPos = blockPos.up()
            worldBlockState = world.getBlockState(blockPos)
            ++loop
        }
    }

//    override fun fertilize(world: ServerWorld, random: RandomGenerator, pos: BlockPos, state: BlockState) {
//        dropStack(world, pos, ItemStack(this))
//    }

    override fun getFluidState(state: BlockState): FluidState {
        return if (state.get(WATERLOGGED)) Fluids.WATER.getStill(false) else super.getFluidState(state)
    }

    override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) {
        builder.add(SECTION, WATERLOGGED)
    }

    companion object {
        val CODEC: MapCodec<PaintedRoseBlock> = createCodec(::PaintedRoseBlock)

        val SECTION: EnumProperty<TripleBlockSection> =
            EnumProperty.of("section", TripleBlockSection::class.java)
        val WATERLOGGED: BooleanProperty = Properties.WATERLOGGED
    }
}