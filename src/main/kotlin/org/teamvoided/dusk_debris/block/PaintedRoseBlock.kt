package org.teamvoided.dusk_debris.block

import com.mojang.serialization.MapCodec
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.server.level.ServerLevel
import net.minecraft.tags.BlockTags
import net.minecraft.util.RandomSource
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.level.Level
import net.minecraft.world.level.LevelAccessor
import net.minecraft.world.level.LevelReader
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.BonemealableBlock
import net.minecraft.world.level.block.BushBlock
import net.minecraft.world.level.block.SimpleWaterloggedBlock
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.world.level.block.state.properties.BooleanProperty
import net.minecraft.world.level.block.state.properties.EnumProperty
import net.minecraft.world.level.material.FluidState
import net.minecraft.world.level.material.Fluids

class PaintedRoseBlock(settings: Properties) : BushBlock(settings), SimpleWaterloggedBlock, BonemealableBlock {

    init {
        this.registerDefaultState(
            stateDefinition.any()
                .setValue(SECTION, TripleBlockSection.TOP)
                .setValue(WATERLOGGED, false)
        )
    }

    override fun codec(): MapCodec<out BushBlock> = CODEC

    override fun getStateForPlacement(ctx: BlockPlaceContext): BlockState =
        getSection(ctx.level, ctx.clickedPos, defaultBlockState())

    override fun canSurvive(state: BlockState, world: LevelReader, pos: BlockPos): Boolean {
        val blockPosDown = pos.below()
        val blockState = world.getBlockState(blockPosDown)
        return blockState.`is`(this) || super.canSurvive(state, world, pos)
    }

    override fun updateShape(
        state: BlockState,
        direction: Direction,
        neighborState: BlockState,
        world: LevelAccessor,
        pos: BlockPos,
        neighborPos: BlockPos
    ): BlockState {
        if (direction == Direction.DOWN) {
            if (!state.canSurvive(world, pos)) {
                world.scheduleTick(pos, this, 1)
                return state
            }
        } else if (direction == Direction.UP) {
            return getSection(world, pos, state)
        }
        return super.updateShape(state, direction, neighborState, world, pos, neighborPos)
    }

    override fun tick(state: BlockState, world: ServerLevel, pos: BlockPos, random: RandomSource) {
        if (!state.canSurvive(world, pos)) {
            world.destroyBlock(pos, true)
        }
    }

    fun getSection(world: LevelAccessor, pos: BlockPos, thisState: BlockState): BlockState {
        val returnState = if (!world.getBlockState(pos.above()).`is`(this)) {
            thisState.setValue(SECTION, TripleBlockSection.TOP)
        } else if (world.getBlockState(pos.below()).isFaceSturdy(world, pos.below(), Direction.UP)) {
            thisState.setValue(SECTION, TripleBlockSection.BOTTOM)
        } else {
            thisState.setValue(SECTION, TripleBlockSection.MIDDLE)
        }
        return returnState.setValue(WATERLOGGED, world.getFluidState(pos).type == Fluids.WATER)
    }

    override fun isValidBonemealTarget(world: LevelReader, pos: BlockPos, state: BlockState): Boolean {
        return true
    }

    override fun isBonemealSuccess(world: Level, random: RandomSource, pos: BlockPos, state: BlockState): Boolean {
        return true
    }

    override fun performBonemeal(world: ServerLevel, random: RandomSource, pos: BlockPos, state: BlockState) {
        var blockPos = pos.above()
        val length: Int = random.nextInt(random.nextInt(5))

        var loop = 0
        var worldBlockState = world.getBlockState(blockPos)
        while (loop <= length && (worldBlockState.`is`(BlockTags.REPLACEABLE) || worldBlockState.`is`(this))) {
            world.setBlockAndUpdate(blockPos, getSection(world, blockPos, state))
            blockPos = blockPos.above()
            worldBlockState = world.getBlockState(blockPos)
            ++loop
        }
    }

//    override fun fertilize(world: ServerWorld, random: RandomGenerator, pos: BlockPos, state: BlockState) {
//        dropStack(world, pos, ItemStack(this))
//    }

    override fun getFluidState(state: BlockState): FluidState {
        return if (state.getValue(WATERLOGGED)) Fluids.WATER.getSource(false) else super.getFluidState(state)
    }

    override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block, BlockState>) {
        builder.add(SECTION, WATERLOGGED)
    }

    companion object {
        val CODEC: MapCodec<PaintedRoseBlock> = simpleCodec(::PaintedRoseBlock)

        val SECTION: EnumProperty<TripleBlockSection> =
            EnumProperty.create("section", TripleBlockSection::class.java)
        val WATERLOGGED: BooleanProperty = BlockStateProperties.WATERLOGGED
    }
}