package org.teamvoided.dusk_debris.block

import com.mojang.serialization.MapCodec
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.particles.ParticleTypes
import net.minecraft.server.level.ServerLevel
import net.minecraft.tags.BlockTags
import net.minecraft.util.ParticleUtils
import net.minecraft.util.RandomSource
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.Level
import net.minecraft.world.level.LevelAccessor
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.SimpleWaterloggedBlock
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.world.level.block.state.properties.BooleanProperty
import net.minecraft.world.level.block.state.properties.IntegerProperty
import net.minecraft.world.level.block.state.properties.Property
import net.minecraft.world.level.material.FluidState
import net.minecraft.world.level.material.Fluids
import net.minecraft.world.phys.shapes.Shapes
import net.minecraft.world.phys.shapes.VoxelShape
import org.teamvoided.dusk_debris.block.not_blocks.DuskProperties
import java.util.*
import kotlin.math.min

class LongLeavesBlock(settings: Properties) : Block(settings), SimpleWaterloggedBlock {

    init {
        this.registerDefaultState(this.stateDefinition.any()
            .setValue(DISTANCE, MAX_DISTANCE)
            .setValue(PERSISTENT, false)
            .setValue(WATERLOGGED, false))
    }

    override fun codec(): MapCodec<LongLeavesBlock> {
        return CODEC
    }

    override fun isRandomlyTicking(state: BlockState): Boolean {
        return state.getValue(DISTANCE) >= MAX_DISTANCE && !state.getValue(PERSISTENT)
    }

    private fun getDistanceFromLog(state: BlockState): Int {
        return getOptionalDistanceFromLog(state).orElse(MAX_DISTANCE)
    }


    override fun getBlockSupportShape(state: BlockState?, world: BlockGetter?, pos: BlockPos?): VoxelShape {
        return Shapes.empty()
    }

    override fun randomTick(state: BlockState, world: ServerLevel, pos: BlockPos?, random: RandomSource?) {
        if (this.canDecay(state)) {
            dropResources(state, world, pos)
            world.removeBlock(pos, false)
        }
    }

    fun canDecay(state: BlockState): Boolean {
        return !state.getValue(PERSISTENT) && state.getValue(DISTANCE) >= MAX_DISTANCE
    }

    override fun tick(state: BlockState, world: ServerLevel, pos: BlockPos, random: RandomSource?) {
        world.setBlock(pos, updateDistanceFromLogs(state, world, pos), 3)
    }

    override fun getLightBlock(state: BlockState?, world: BlockGetter?, pos: BlockPos?): Int {
        return 1
    }

    override fun updateShape(
        state: BlockState,
        direction: Direction?,
        neighborState: BlockState,
        world: LevelAccessor,
        pos: BlockPos?,
        neighborPos: BlockPos?
    ): BlockState {
        if (state.getValue(WATERLOGGED)) {
            world.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(world))
        }

        val i = getDistanceFromLog(neighborState) + 1
        if (i != 1 || state.getValue(DISTANCE) as Int != i) {
            world.scheduleTick(pos, this, 1)
        }

        return state
    }

    override fun getFluidState(state: BlockState): FluidState {
        return if (state.getValue(WATERLOGGED)) Fluids.WATER.getSource(false) else super.getFluidState(state)
    }

    override fun animateTick(state: BlockState?, world: Level, pos: BlockPos, random: RandomSource) {
        if (world.isRainingAt(pos.above())) {
            if (random.nextInt(15) == 1) {
                val blockPos = pos.below()
                val blockState: BlockState = world.getBlockState(blockPos)
                if (!blockState.canOcclude() || !blockState.isFaceSturdy(world, blockPos, Direction.UP)) {
                    ParticleUtils.spawnParticleBelow(world, pos, random, ParticleTypes.DRIPPING_WATER)
                }
            }
        }
    }

    override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block?, BlockState?>) {
        builder.add(*arrayOf<Property<*>>(DISTANCE, PERSISTENT, WATERLOGGED))
    }

    override fun getStateForPlacement(ctx: BlockPlaceContext): BlockState {
        val fluidState = ctx.level.getFluidState(ctx.clickedPos)
        val blockState: BlockState = (this.defaultBlockState().setValue(PERSISTENT, true) as BlockState).setValue(
            WATERLOGGED,
            fluidState.type === Fluids.WATER
        ) as BlockState
        return updateDistanceFromLogs(blockState, ctx.level, ctx.clickedPos)
    }

    companion object {
        val CODEC: MapCodec<LongLeavesBlock> = simpleCodec(::LongLeavesBlock)
        const val MAX_DISTANCE: Int = 15
        val DISTANCE: IntegerProperty = DuskProperties.DISTANCE_1_15
        val PERSISTENT: BooleanProperty = BlockStateProperties.PERSISTENT
        val WATERLOGGED: BooleanProperty = BlockStateProperties.WATERLOGGED
        private const val TICK_DELAY = 1

        private fun updateDistanceFromLogs(state: BlockState, world: LevelAccessor, pos: BlockPos): BlockState {
            var i = MAX_DISTANCE
            val mutable = BlockPos.MutableBlockPos()
            val var5 = Direction.entries.toTypedArray()

            for (direction in var5) {
                mutable.setWithOffset(pos,direction)
                i = min(
                    i.toDouble(),
                    (getDistanceFromLog(world.getBlockState(mutable)) + 1).toDouble()
                ).toInt()
                if (i == 1) {
                    break
                }
            }

            return state.setValue(DISTANCE, i) as BlockState
        }

        private fun getDistanceFromLog(state: BlockState): Int {
            return getOptionalDistanceFromLog(state).orElse(MAX_DISTANCE)
        }

        fun getOptionalDistanceFromLog(state: BlockState): OptionalInt {
            return if (state.`is`(BlockTags.LOGS)) {
                OptionalInt.of(0)
            } else {
                if (state.hasProperty(DISTANCE)) OptionalInt.of(state.getValue(DISTANCE))
                else if (state.hasProperty(BlockStateProperties.DISTANCE)) OptionalInt.of(state.getValue(BlockStateProperties.DISTANCE))
                else OptionalInt.empty()
            }
        }
    }
}