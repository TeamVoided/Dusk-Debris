package org.teamvoided.dusk_debris.block

import com.mojang.serialization.MapCodec
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.Waterloggable
import net.minecraft.client.util.ParticleUtil
import net.minecraft.fluid.FluidState
import net.minecraft.fluid.Fluids
import net.minecraft.item.ItemPlacementContext
import net.minecraft.particle.ParticleTypes
import net.minecraft.registry.tag.BlockTags
import net.minecraft.server.world.ServerWorld
import net.minecraft.state.StateManager
import net.minecraft.state.property.BooleanProperty
import net.minecraft.state.property.IntProperty
import net.minecraft.state.property.Properties
import net.minecraft.state.property.Property
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.random.RandomGenerator
import net.minecraft.util.shape.VoxelShape
import net.minecraft.util.shape.VoxelShapes
import net.minecraft.world.BlockView
import net.minecraft.world.World
import net.minecraft.world.WorldAccess
import org.teamvoided.dusk_debris.block.not_blocks.DuskProperties
import java.util.*
import kotlin.math.min

class LongLeavesBlock(settings: Settings) : Block(settings), Waterloggable {

    init {
        this.defaultState = this.stateManager.defaultState
            .with(DISTANCE, MAX_DISTANCE)
            .with(PERSISTENT, false)
            .with(WATERLOGGED, false)
    }

    override fun getCodec(): MapCodec<LongLeavesBlock> {
        return CODEC
    }

    override fun getRandomTicks(state: BlockState): Boolean {
        return state.get(DISTANCE) >= MAX_DISTANCE && !state.get(PERSISTENT)
    }

    private fun getDistanceFromLog(state: BlockState): Int {
        return getOptionalDistanceFromLog(state).orElse(MAX_DISTANCE)
    }


    override fun getSidesShape(state: BlockState?, world: BlockView?, pos: BlockPos?): VoxelShape {
        return VoxelShapes.empty()
    }

    override fun randomTick(state: BlockState, world: ServerWorld, pos: BlockPos?, random: RandomGenerator?) {
        if (this.canDecay(state)) {
            dropStacks(state, world, pos)
            world.removeBlock(pos, false)
        }
    }

    fun canDecay(state: BlockState): Boolean {
        return !state.get(PERSISTENT) && state.get(DISTANCE) >= MAX_DISTANCE
    }

    override fun scheduledTick(state: BlockState, world: ServerWorld, pos: BlockPos, random: RandomGenerator?) {
        world.setBlockState(pos, updateDistanceFromLogs(state, world, pos), 3)
    }

    override fun getOpacity(state: BlockState?, world: BlockView?, pos: BlockPos?): Int {
        return 1
    }

    override fun getStateForNeighborUpdate(
        state: BlockState,
        direction: Direction?,
        neighborState: BlockState,
        world: WorldAccess,
        pos: BlockPos?,
        neighborPos: BlockPos?
    ): BlockState {
        if (state.get(WATERLOGGED)) {
            world.scheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world))
        }

        val i = getDistanceFromLog(neighborState) + 1
        if (i != 1 || state.get(DISTANCE) as Int != i) {
            world.scheduleBlockTick(pos, this, 1)
        }

        return state
    }

    override fun getFluidState(state: BlockState): FluidState {
        return if (state.get(WATERLOGGED)) Fluids.WATER.getStill(false) else super.getFluidState(state)
    }

    override fun randomDisplayTick(state: BlockState?, world: World, pos: BlockPos, random: RandomGenerator) {
        if (world.hasRain(pos.up())) {
            if (random.nextInt(15) == 1) {
                val blockPos = pos.down()
                val blockState: BlockState = world.getBlockState(blockPos)
                if (!blockState.isOpaque() || !blockState.isSideSolidFullSquare(world, blockPos, Direction.UP)) {
                    ParticleUtil.spawnParticle(world, pos, random, ParticleTypes.DRIPPING_WATER)
                }
            }
        }
    }

    override fun appendProperties(builder: StateManager.Builder<Block?, BlockState?>) {
        builder.add(*arrayOf<Property<*>>(DISTANCE, PERSISTENT, WATERLOGGED))
    }

    override fun getPlacementState(ctx: ItemPlacementContext): BlockState {
        val fluidState = ctx.world.getFluidState(ctx.blockPos)
        val blockState: BlockState = (this.defaultState.with(PERSISTENT, true) as BlockState).with(
            WATERLOGGED,
            fluidState.fluid === Fluids.WATER
        ) as BlockState
        return updateDistanceFromLogs(blockState, ctx.world, ctx.blockPos)
    }

    companion object {
        val CODEC: MapCodec<LongLeavesBlock> = createCodec(::LongLeavesBlock)
        const val MAX_DISTANCE: Int = 15
        val DISTANCE: IntProperty = DuskProperties.DISTANCE_1_15
        val PERSISTENT: BooleanProperty = Properties.PERSISTENT
        val WATERLOGGED: BooleanProperty = Properties.WATERLOGGED
        private const val TICK_DELAY = 1

        private fun updateDistanceFromLogs(state: BlockState, world: WorldAccess, pos: BlockPos): BlockState {
            var i = MAX_DISTANCE
            val mutable = BlockPos.Mutable()
            val var5 = Direction.entries.toTypedArray()

            for (direction in var5) {
                mutable[pos] = direction
                i = min(
                    i.toDouble(),
                    (getDistanceFromLog(world.getBlockState(mutable)) + 1).toDouble()
                ).toInt()
                if (i == 1) {
                    break
                }
            }

            return state.with(DISTANCE, i) as BlockState
        }

        private fun getDistanceFromLog(state: BlockState): Int {
            return getOptionalDistanceFromLog(state).orElse(MAX_DISTANCE)
        }

        fun getOptionalDistanceFromLog(state: BlockState): OptionalInt {
            return if (state.isIn(BlockTags.LOGS)) {
                OptionalInt.of(0)
            } else {
                if (state.contains(DISTANCE)) OptionalInt.of(state.get(DISTANCE))
                else if (state.contains(Properties.DISTANCE_1_7)) OptionalInt.of(state.get(Properties.DISTANCE_1_7))
                else OptionalInt.empty()
            }
        }
    }
}