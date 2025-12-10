package org.teamvoided.dusk_debris.block

import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.entity.Entity
import net.minecraft.item.ItemPlacementContext
import net.minecraft.server.world.ServerWorld
import net.minecraft.state.StateManager
import net.minecraft.state.property.*
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.math.Vec3d
import net.minecraft.util.random.RandomGenerator
import net.minecraft.world.World
import net.minecraft.world.WorldAccess
import net.minecraft.world.event.GameEvent
import org.teamvoided.dusk_debris.block.not_blocks.DirectionOrNullState
import org.teamvoided.dusk_debris.block.not_blocks.DuskProperties
import org.teamvoided.dusk_debris.data.tags.DuskEntityTypeTags
import org.teamvoided.dusk_debris.entity.helper.WindLogic
import org.teamvoided.dusk_debris.entity.helper.WindLogic.inFanWind
import org.teamvoided.dusk_debris.init.DuskParticles
import org.teamvoided.dusk_debris.particle.WindParticleEffect
import org.teamvoided.dusk_debris.util.spawnParticles
import org.teamvoided.dusk_debris.util.toVec3d

class ExhaustBlock(settings: Settings) : SixWayFacingBlock(settings) {
    init {
        this.defaultState = stateManager.defaultState
            .with(ACTIVE, 0)
            .with(POWERED, false)
            .with(FACING, Direction.UP)
            .with(SOURCE, DirectionOrNullState.NONE)
            .with(AGE, 6)

    }

    override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) {
        super.appendProperties(builder)
        builder.add(ACTIVE, POWERED, SOURCE, AGE)
    }

    override fun getPlacementState(ctx: ItemPlacementContext): BlockState {
        return if (ctx.playerLookDirection.axis != ctx.side.axis) {
            defaultState
                .with(FACING, ctx.playerLookDirection.opposite)
                .with(SOURCE, DirectionOrNullState.fromDirection(ctx.side.opposite))
        } else defaultState.with(FACING, ctx.playerLookDirection.opposite)
    }

    override fun getStateForNeighborUpdate(
        state: BlockState,
        direction: Direction,
        neighborState: BlockState,
        world: WorldAccess,
        pos: BlockPos,
        neighborPos: BlockPos
    ): BlockState {
        exhaustTick(state, world, pos)
        val source = state.get(SOURCE)
        if (source != DirectionOrNullState.NONE) {
            if (!world.getBlockState(pos.offset(source.direction)).isOf(this))
                return state.with(SOURCE, DirectionOrNullState.NONE)

            val targetState: BlockState = world.getBlockState(pos.offset(source.direction))
            return state
                .with(ACTIVE, targetState.get(ACTIVE))
                .with(POWERED, targetState.get(POWERED))
                .with(AGE, targetState.get(AGE))
        }
        return state
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
        exhaustTick(state, world, pos)
    }

    override fun onBlockAdded(state: BlockState, world: World, pos: BlockPos, oldState: BlockState, notify: Boolean) {
        if (oldState.block != state.block && world is ServerWorld) {
            this.setState(state, world, pos)
        }
    }

    private fun setState(state: BlockState, world: ServerWorld, pos: BlockPos) {
        val bl = world.isReceivingRedstonePower(pos)
        if (bl != state.get(POWERED)) {
            var blockState = state
            if (!state.get(POWERED)) {
                blockState = state.cycle(ACTIVE)
                world.emitGameEvent(null, GameEvent.BLOCK_CHANGE, pos)
            }
            world.setBlockState(pos, blockState.with(POWERED, bl), 3)
        }
        exhaustTick(state, world, pos)
    }

    override fun getRandomTicks(state: BlockState): Boolean =
        state.get(SOURCE) == DirectionOrNullState.NONE || state.get(ACTIVE) != 0

    override fun randomTick(state: BlockState, world: ServerWorld, pos: BlockPos, random: RandomGenerator) =
        world.scheduleBlockTick(pos, state.block, 0)


    private fun exhaustTick(state: BlockState, world: WorldAccess, pos: BlockPos) {
        if (getRandomTicks(state)) world.scheduleBlockTick(pos, this, 1)
    }

    override fun scheduledTick(state: BlockState, world: ServerWorld, pos: BlockPos, random: RandomGenerator) {
        val worldSecond = (world.time % 20).toInt()
        val activity = state.get(ACTIVE)
        val facing = state.get(FACING)

        if (worldSecond == 0 && state.get(SOURCE) == DirectionOrNullState.NONE) {
            if (state.get(AGE) == 0) {
                val age = when (activity) {
                    2 -> 6 //2
                    1 -> 2 //1
                    0 -> 1 //6
                    else -> 25
                }
                world.setBlockState(pos, state.with(AGE, age).cycle(ACTIVE))
            } else {
                world.setBlockState(pos, state.with(AGE, state.get(AGE) - 1))
            }
        }
        if (activity != 0) {
            if (activity == 2) {
                val windLength = WindLogic.windLength(world, pos, state.get(Properties.FACING), MAX_BLAST_HEIGHT)
                val entityList = getEntityList(world, pos, facing, windLength)
                if (entityList.isNotEmpty()) {
                    entityList.forEach {
                        it.damage(world.damageSources.magic(), 6f)
                        it.inFanWind(
                            facing.vector.toVec3d().multiply(0.1)
                                .add(0.0, if (facing == Direction.UP) it.gravity * 0.4 else 0.0, 0.0)
                        )
                    }
                }
                blastParticles(state, world, pos, world.random)
            } else warmUpParticles(state, world, pos, world.random)
        }
        exhaustTick(state, world, pos)
    }

    private fun getEntityList(
        world: World,
        pos: BlockPos,
        facing: Direction,
        windLength: Int
    ): MutableList<Entity> {
        return world.getOtherEntities(null, FanBlock.getBox(facing, windLength.toDouble()).offset(pos.ofCenter()))
        { !it.type.isIn(DuskEntityTypeTags.FANS_DONT_AFFECT) }
    }

    private fun warmUpParticles(state: BlockState, world: ServerWorld, pos: BlockPos, random: RandomGenerator) {
        repeat(random.range(1, 3)) {
            world.spawnParticles(
                DuskParticles.EXHAUST_WARMUP,
                Vec3d(
                    random.nextDouble(),
                    random.nextDouble() * random.nextInt(MAX_BLAST_HEIGHT - 10),
                    random.nextDouble()
                ).rotateFromUp(state.get(FACING)).add(pos.x.toDouble(), pos.y.toDouble(), pos.z.toDouble()),
                Vec3d(0.0, 0.25 + random.nextDouble() * 0.05, 0.0).rotateFromUp(state.get(FACING)),
                32.0
            )
        }
    }

    private fun blastParticles(state: BlockState, world: ServerWorld, pos: BlockPos, random: RandomGenerator) {
        repeat(random.range(7, 15)) {
            world.spawnParticles(
                DuskParticles.EXHAUST_BLAST,
                Vec3d(random.nextDouble(), random.nextDouble(), random.nextDouble()).rotateFromUp(state.get(FACING))
                    .add(pos.x.toDouble(), pos.y.toDouble(), pos.z.toDouble()),
                Vec3d(0.0, 0.5 + random.nextDouble() * 0.25, 0.0).rotateFromUp(state.get(FACING)),
                128.0
            )
        }
    }

    fun Vec3d.rotateFromUp(direction: Direction): Vec3d {
        return when (direction) {
            Direction.DOWN -> this.multiply(1.0, -1.0, 1.0)
            Direction.UP -> this
            Direction.NORTH -> Vec3d(x, z, -y)
            Direction.SOUTH -> Vec3d(x, z, y)
            Direction.WEST -> Vec3d(-y, x, z)
            Direction.EAST -> Vec3d(y, x, z)
        }
    }

    companion object {
        val POWERED: BooleanProperty = Properties.POWERED
        val ACTIVE: IntProperty = DuskProperties.ACTIVE_STATE_INT
        val FACING: DirectionProperty = Properties.FACING
        val SOURCE: EnumProperty<DirectionOrNullState> = DuskProperties.FACING_OR_NULL
        val AGE: IntProperty = Properties.AGE_25

        val MAX_BLAST_HEIGHT = 15
    }
}