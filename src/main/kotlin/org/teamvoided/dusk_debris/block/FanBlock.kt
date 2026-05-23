package org.teamvoided.dusk_debris.block

import com.mojang.serialization.MapCodec
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.util.RandomSource
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.world.level.block.state.properties.BooleanProperty
import net.minecraft.world.level.block.state.properties.DirectionProperty
import net.minecraft.world.level.gameevent.GameEvent
import net.minecraft.world.phys.AABB
import net.minecraft.world.phys.Vec3
import org.teamvoided.dusk_debris.block.not_blocks.DuskProperties
import org.teamvoided.dusk_debris.data.tags.DuskEntityTypeTags
import org.teamvoided.dusk_debris.entity.helper.WindLogic
import org.teamvoided.dusk_debris.entity.helper.WindLogic.inFanWind
import org.teamvoided.dusk_debris.particle.WindParticleEffect
import org.teamvoided.dusk_debris.util.spawnParticles

open class FanBlock(val strength: Int, settings: Properties) : SixWayFacingBlock(settings) {

    init {
        this.registerDefaultState(
            stateDefinition.any()
                .setValue(ACTIVE, false)
                .setValue(POWERED, false)
                .setValue(FACING, Direction.UP)
        )
    }

    override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block, BlockState>) {
        super.createBlockStateDefinition(builder)
        builder.add(ACTIVE, POWERED)
    }

    private fun fanTick(state: BlockState, world: Level, pos: BlockPos) {
        if (state.getValue(ACTIVE)) world.scheduleTick(pos, this, 0)
    }

    override fun onPlace(state: BlockState, world: Level, pos: BlockPos, oldState: BlockState, notify: Boolean) {
        if (oldState.block != state.block && world is ServerLevel) {
            this.setState(state, world, pos)
        }
    }

    override fun neighborChanged(
        state: BlockState,
        world: Level,
        pos: BlockPos,
        block: Block,
        fromPos: BlockPos,
        notify: Boolean
    ) {
        if (world is ServerLevel) {
            this.setState(state, world, pos)
        }
        fanTick(state, world, pos)
    }

    private fun setState(state: BlockState, world: ServerLevel, pos: BlockPos) {
        val bl = world.hasNeighborSignal(pos)
        if (bl != state.getValue(POWERED)) {
            var blockState = state
            if (!state.getValue(POWERED)) {
                blockState = state.cycle(ACTIVE)
                world.gameEvent(null, GameEvent.BLOCK_CHANGE, pos)
                world.playSound(
                    null as Player?,
                    pos,
                    if (blockState.getValue(ACTIVE)) SoundEvents.COPPER_BULB_TURN_ON
                    else SoundEvents.COPPER_BULB_TURN_OFF,
                    SoundSource.BLOCKS
                )
                if (blockState.getValue(ACTIVE))
                    fanTick(state, world, pos)
            }
            world.setBlock(pos, blockState.setValue(POWERED, bl), 3)
        }
    }

    override fun hasAnalogOutputSignal(state: BlockState): Boolean = state.getValue(ACTIVE)

    override fun getAnalogOutputSignal(state: BlockState, world: Level, pos: BlockPos): Int {
        return if (world.getBlockState(pos).getValue(ACTIVE)) getWindLength(world, pos, state) else 0
    }

    override fun tick(state: BlockState, world: ServerLevel, pos: BlockPos, random: RandomSource) {
        if (state.getValue(ACTIVE)) {
            val windLength = getWindLength(world, pos, state)
            if (windLength > 0) {
                moveEntities(world, pos, state, windLength)
                particles(world, pos, state, windLength)
                val the90 = -strength + 90
                if ((pos.asLong() + world.gameTime) % the90 == 0L) {
                    world.playSound(
                        null as Player?,
                        pos,
                        SoundEvents.BREEZE_WHIRL,
                        SoundSource.BLOCKS,
                        the90 / 30f,
                        (strength - 8) / 7f
                    )
                }
            }
            fanTick(state, world, pos)
        }
    }

    fun power(): Double {
        return strength * 0.33333 + 10
    }

    fun getWindLength(world: Level, pos: BlockPos, state: BlockState): Int =
        WindLogic.windLength(world, pos, state.getValue(BlockStateProperties.FACING), power().toInt())


    fun particles(world: ServerLevel, pos: BlockPos, state: BlockState, windLength: Int) {
        val rand = world.random
        if (rand.nextDouble() < (windLength + strength) / 30.0) {
            val limitor = rand.nextDouble()
            val maxAge = (((20 - strength) * windLength / 4) * limitor).toInt()
            if (maxAge > 0) {
                val facing = state.getValue(BlockStateProperties.FACING)
                val particlePos = pos.relative(facing)
                world.spawnParticles(
                    WindParticleEffect(
                        windLength * limitor,
                        facing,
                        maxAge
                    ),
                    Vec3(
                        particlePos.x + rand.nextDouble(),
                        particlePos.y + rand.nextDouble(),
                        particlePos.z + rand.nextDouble(),
                    ),
                    Vec3.ZERO,
                    128.0
                )
            }
        }
    }

    private fun moveEntities(world: Level, pos: BlockPos, state: BlockState, windLength: Int) {
        val facing = state.getValue(BlockStateProperties.FACING)
        val entitiesInRange =
            world.getEntities(null, getBox(facing, windLength.toDouble()).move(pos.center))
            { !it.type.`is`(DuskEntityTypeTags.FANS_DONT_AFFECT) }
        if (entitiesInRange.isNotEmpty()) {
            entitiesInRange.forEach {
                addEntitySpeed(it, facing)
            }
        }
    }

    private fun addEntitySpeed(entity: Entity, direction: Direction) {
        val power: Double = strength * 0.0175 + 0.03
        val velocity = when (direction) {
            Direction.UP -> Vec3(0.0, power + entity.gravity * 0.4, 0.0)
            Direction.DOWN -> Vec3(0.0, -power, 0.0)
            Direction.SOUTH -> Vec3(0.0, 0.0, power)
            Direction.NORTH -> Vec3(0.0, 0.0, -power)
            Direction.EAST -> Vec3(power, 0.0, 0.0)
            Direction.WEST -> Vec3(-power, 0.0, 0.0)
        }

        entity.inFanWind(velocity)
    }

    companion object {
        val CODEC: MapCodec<FanBlock> = simpleCodec { settings: Properties ->
            FanBlock(
                15,
                settings
            )
        }
        val POWERED: BooleanProperty = BlockStateProperties.POWERED
        val ACTIVE: BooleanProperty = DuskProperties.ACTIVE
        val FACING: DirectionProperty = BlockStateProperties.FACING

        fun getBox(direction: Direction, windLength: Double): AABB {
            val horizRange = 0.5
            val vertRange = windLength
            val vertRangeBottom = -0.5
            return when (direction) {
                Direction.UP -> AABB(
                    -horizRange,
                    -vertRangeBottom,
                    -horizRange,
                    horizRange,
                    vertRange,
                    horizRange
                )

                Direction.DOWN -> AABB(
                    -horizRange,
                    -vertRange,
                    -horizRange,
                    horizRange,
                    vertRangeBottom,
                    horizRange
                )

                Direction.NORTH -> AABB(
                    -horizRange,
                    -horizRange,
                    -vertRange,
                    horizRange,
                    horizRange,
                    vertRangeBottom
                )

                Direction.SOUTH -> AABB(
                    -horizRange,
                    -horizRange,
                    -vertRangeBottom,
                    horizRange,
                    horizRange,
                    vertRange
                )

                Direction.EAST -> AABB(
                    -vertRangeBottom,
                    -horizRange,
                    -horizRange,
                    vertRange,
                    horizRange,
                    horizRange
                )

                Direction.WEST -> AABB(
                    -vertRange,
                    -horizRange,
                    -horizRange,
                    vertRangeBottom,
                    horizRange,
                    horizRange
                )
            }
        }
    }
}