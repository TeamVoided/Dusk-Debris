package org.teamvoided.dusk_debris.block

import com.mojang.serialization.MapCodec
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.entity.Entity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.state.StateManager
import net.minecraft.state.property.BooleanProperty
import net.minecraft.state.property.DirectionProperty
import net.minecraft.state.property.Properties
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Box
import net.minecraft.util.math.Direction
import net.minecraft.util.math.Vec3d
import net.minecraft.util.random.RandomGenerator
import net.minecraft.world.World
import net.minecraft.world.event.GameEvent
import org.teamvoided.dusk_debris.block.not_blocks.DuskProperties
import org.teamvoided.dusk_debris.data.tags.DuskEntityTypeTags
import org.teamvoided.dusk_debris.entity.helper.FanLogic.inFanWind
import org.teamvoided.dusk_debris.particle.WindParticleEffect
import org.teamvoided.dusk_debris.util.spawnParticles

open class FanBlock(val strength: Int, settings: Settings) :
    SixWayFacingBlock(settings) {

    init {
        this.defaultState = stateManager.defaultState
            .with(ACTIVE, false)
            .with(POWERED, false)
            .with(FACING, Direction.UP)
    }

    override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) {
        super.appendProperties(builder)
        builder.add(ACTIVE, POWERED)
    }

    private fun fanTick(state: BlockState, world: World, pos: BlockPos) {
        if (state.get(ACTIVE)) world.scheduleBlockTick(pos, this, 0)
    }

    override fun onBlockAdded(state: BlockState, world: World, pos: BlockPos, oldState: BlockState, notify: Boolean) {
        if (oldState.block != state.block && world is ServerWorld) {
            this.setState(state, world, pos)
        }
    }

    override fun neighborUpdate(
        state: BlockState,
        world: World,
        pos: BlockPos,
        block: Block,
        fromPos: BlockPos,
        notify: Boolean
    ) {
        if (world is ServerWorld) {
            this.setState(state, world, pos)
        }
        fanTick(state, world, pos)
    }

    private fun setState(state: BlockState, world: ServerWorld, pos: BlockPos) {
        val bl = world.isReceivingRedstonePower(pos)
        if (bl != state.get(POWERED)) {
            var blockState = state
            if (!state.get(POWERED)) {
                blockState = state.cycle(ACTIVE)
                world.emitGameEvent(null, GameEvent.BLOCK_CHANGE, pos)
                world.playSound(
                    null as PlayerEntity?,
                    pos,
                    if (blockState.get(ACTIVE)) SoundEvents.BLOCK_COPPER_BULB_TURN_ON
                    else SoundEvents.BLOCK_COPPER_BULB_TURN_OFF,
                    SoundCategory.BLOCKS
                )
                if (blockState.get(ACTIVE))
                    fanTick(state, world, pos)
            }
            world.setBlockState(pos, blockState.with(POWERED, bl), 3)
        }
    }

    override fun hasComparatorOutput(state: BlockState): Boolean = state.get(ACTIVE)

    override fun getComparatorOutput(state: BlockState, world: World, pos: BlockPos): Int {
        return if (world.getBlockState(pos).get(ACTIVE)) getWindLength(world, pos, state) else 0
    }

    override fun scheduledTick(state: BlockState, world: ServerWorld, pos: BlockPos, random: RandomGenerator) {
        if (state.get(ACTIVE)) {
            val windLength = getWindLength(world, pos, state)
            if (windLength > 0) {
                moveEntities(world, pos, state, windLength)
                particles(world, pos, state, windLength)
                val the90 = -strength + 90
                if ((pos.asLong() + world.time) % the90 == 0L) {
                    world.playSound(
                        null as PlayerEntity?,
                        pos,
                        SoundEvents.ENTITY_BREEZE_WHIRL,
                        SoundCategory.BLOCKS,
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

    fun getWindLength(world: World, pos: BlockPos, state: BlockState): Int {
        val facing = state.get(Properties.FACING)
        var windLength = power().toInt()
        for (it in 0 until power().toInt()) {
            val posCheck = pos.offset(facing, it + 1)
            val worldBlock = world.getBlockState(pos.offset(facing, it + 1))
            if (
                !worldBlock.materialReplaceable() &&
                (worldBlock.isSideSolidFullSquare(world, posCheck, facing) ||
                        worldBlock.isSideSolidFullSquare(world, posCheck, facing.opposite))
            ) {
                windLength = it
                break
            }
        }
        return windLength
    }

    fun particles(world: ServerWorld, pos: BlockPos, state: BlockState, windLength: Int) {
        val rand = world.random
        if (rand.nextDouble() < (windLength + strength) / 30.0) {
            val limitor = rand.nextDouble()
            val maxAge = (((20 - strength) * windLength / 4) * limitor).toInt()
            if (maxAge > 0) {
                val facing = state.get(Properties.FACING)
                val particlePos = pos.offset(facing)
                world.spawnParticles(
                    WindParticleEffect(
                        windLength * limitor,
                        facing.id,
                        maxAge
                    ),
                    Vec3d(
                        particlePos.x + rand.nextDouble(),
                        particlePos.y + rand.nextDouble(),
                        particlePos.z + rand.nextDouble(),
                    ),
                    Vec3d.ZERO,
                    128.0
                )
            }
        }
    }

    private fun moveEntities(world: World, pos: BlockPos, state: BlockState, windLength: Int) {
        val facing = state.get(Properties.FACING)
        val entitiesInRange =
            world.getOtherEntities(null, getBox(facing, windLength.toDouble()).offset(pos.ofCenter()))
            { !it.type.isIn(DuskEntityTypeTags.FANS_DONT_AFFECT) }
        if (entitiesInRange.isNotEmpty()) {
            entitiesInRange.forEach {
                addEntitySpeed(it, facing)
            }
        }
    }

    private fun addEntitySpeed(entity: Entity, direction: Direction) {
        val power: Double = strength * 0.0175 + 0.03
        val velocity = when (direction) {
            Direction.UP -> Vec3d(0.0, power + entity.gravity * 0.4, 0.0)
            Direction.DOWN -> Vec3d(0.0, -power, 0.0)
            Direction.SOUTH -> Vec3d(0.0, 0.0, power)
            Direction.NORTH -> Vec3d(0.0, 0.0, -power)
            Direction.EAST -> Vec3d(power, 0.0, 0.0)
            Direction.WEST -> Vec3d(-power, 0.0, 0.0)
        }

        entity.inFanWind(velocity)
    }

    private fun getBox(direction: Direction, windLength: Double): Box {
        val horizRange = 0.5
        val vertRange = windLength
        val vertRangeBottom = -0.5
        return when (direction) {
            Direction.UP -> Box(
                -horizRange,
                -vertRangeBottom,
                -horizRange,
                horizRange,
                vertRange,
                horizRange
            )

            Direction.DOWN -> Box(
                -horizRange,
                -vertRange,
                -horizRange,
                horizRange,
                vertRangeBottom,
                horizRange
            )

            Direction.NORTH -> Box(
                -horizRange,
                -horizRange,
                -vertRange,
                horizRange,
                horizRange,
                vertRangeBottom
            )

            Direction.SOUTH -> Box(
                -horizRange,
                -horizRange,
                -vertRangeBottom,
                horizRange,
                horizRange,
                vertRange
            )

            Direction.EAST -> Box(
                -vertRangeBottom,
                -horizRange,
                -horizRange,
                vertRange,
                horizRange,
                horizRange
            )

            Direction.WEST -> Box(
                -vertRange,
                -horizRange,
                -horizRange,
                vertRangeBottom,
                horizRange,
                horizRange
            )
        }
    }

    companion object {
        val CODEC: MapCodec<FanBlock> = createCodec { settings: Settings ->
            FanBlock(
                15,
                settings
            )
        }
        val POWERED: BooleanProperty = Properties.POWERED
        val ACTIVE: BooleanProperty = DuskProperties.ACTIVE
        val FACING: DirectionProperty = Properties.FACING
    }
}