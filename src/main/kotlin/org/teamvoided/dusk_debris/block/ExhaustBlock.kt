package org.teamvoided.dusk_debris.block

import net.minecraft.block.*
import net.minecraft.entity.Entity
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemPlacementContext
import net.minecraft.item.ItemStack
import net.minecraft.server.world.ServerWorld
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvent
import net.minecraft.state.StateManager
import net.minecraft.state.property.*
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.math.Vec3d
import net.minecraft.util.random.RandomGenerator
import net.minecraft.world.World
import net.minecraft.world.WorldAccess
import net.minecraft.world.chunk.Chunk
import net.minecraft.world.event.GameEvent
import org.teamvoided.dusk_debris.block.ExhaustBlock.Companion.getExhaustAttachment
import org.teamvoided.dusk_debris.block.ExhaustBlock.Companion.setExhaustAttachment
import org.teamvoided.dusk_debris.block.attachments.ExhaustData
import org.teamvoided.dusk_debris.block.not_blocks.DirectionOrNullState
import org.teamvoided.dusk_debris.block.not_blocks.DuskProperties
import org.teamvoided.dusk_debris.data.gen.providers.variants.SnifferVariants
import org.teamvoided.dusk_debris.data.tags.DuskEntityTypeTags
import org.teamvoided.dusk_debris.entity.helper.WindLogic
import org.teamvoided.dusk_debris.entity.helper.WindLogic.inFanWind
import org.teamvoided.dusk_debris.entity.variant.SnifferVariant
import org.teamvoided.dusk_debris.init.DuskAttachmentTypes
import org.teamvoided.dusk_debris.init.DuskParticles
import org.teamvoided.dusk_debris.init.DuskSoundEvents
import org.teamvoided.dusk_debris.util.spawnParticles
import org.teamvoided.dusk_debris.util.toVec3d
import kotlin.math.absoluteValue

class ExhaustBlock(settings: Settings) : SixWayFacingBlock(settings) {
    init {
        this.defaultState = stateManager.defaultState
            .with(ACTIVE, 0)
            .with(POWERED, false)
            .with(FACING, Direction.UP)
            .with(SOURCE, DirectionOrNullState.NONE)
            .with(AGE, COOLDOWN_DURATION)
            .with(NOTE, 0)

    }

    override fun onPlaced(
        world: World,
        pos: BlockPos,
        state: BlockState,
        placer: LivingEntity?,
        itemStack: ItemStack
    ) {
        super.onPlaced(world, pos, state, placer, itemStack)
        exhaustTick(state, world, pos)

    }

    override fun onBreak(world: World, pos: BlockPos, state: BlockState, player: PlayerEntity?): BlockState {
        world.getWorldChunk(pos).removeExhaustAttachment(pos)
        return super.onBreak(world, pos, state, player)
    }

    override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) {
        super.appendProperties(builder)
        builder.add(ACTIVE, POWERED, SOURCE, AGE, NOTE)
    }

    override fun getPlacementState(ctx: ItemPlacementContext): BlockState {
        val state = defaultState
            .with(FACING, ctx.playerLookDirection.opposite)
            .with(NOTE, (ctx.blockPos.x + ctx.blockPos.y + ctx.blockPos.z).absoluteValue % 25)
        return if (ctx.playerLookDirection.axis != ctx.side.axis) {
            matchWithSourceIfPresent(
                state.with(SOURCE, DirectionOrNullState.fromDirection(ctx.side.opposite)),
                ctx.world,
                ctx.blockPos
            )
        } else state
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
        return matchWithSourceIfPresent(state, world, pos)
    }

    private fun matchWithSourceIfPresent(
        state: BlockState,
        world: WorldAccess,
        pos: BlockPos,
    ): BlockState {
        //world.getWorldChunk(pos).setExhaustAttachment(pos, )


        val source = state.get(SOURCE)
        if (source != DirectionOrNullState.NONE) {
            //getSource(state, world, pos) ?: return state.with(SOURCE, DirectionOrNullState.NONE)

            val targetState: BlockState = world.getBlockState(pos.offset(source.direction))
            if (!targetState.isOf(this) || targetState.get(SOURCE) == source.direction!!.opposite)
                return state.with(SOURCE, DirectionOrNullState.NONE)
            val targetActivity = targetState.get(ACTIVE)
            if (targetActivity != state.get(ACTIVE) && targetActivity != 0) {
                if (targetActivity == 1) playAnticipation(state, world, pos, world.random)
                else playBlast(state, world, pos, world.random)
            }

            return state
                .with(ACTIVE, targetActivity)
                .with(POWERED, targetState.get(POWERED))
                .with(AGE, targetState.get(AGE))
                .with(NOTE, targetState.get(NOTE))
        }
        return state
    }

    private fun getSource(state: BlockState, world: WorldAccess, pos: BlockPos): BlockPos? {
        val source = state.get(SOURCE).direction ?: return pos
        val sourcePos = pos.offset(source)
        val sourceBlock = world.getBlockState(sourcePos)
        return if (sourceBlock.isOf(this)) getSource(sourceBlock, world, sourcePos)
        else null
    }

    private fun setThisAsSource(state: BlockState, world: World, pos: BlockPos) {
        world.setBlockState(pos, state.with(SOURCE, DirectionOrNullState.NONE))
        var prevDirection: Direction? = state.get(SOURCE).direction
        if (prevDirection != null) {
            var propPos: BlockPos = pos
            val propagation: MutableList<Pair<BlockPos, Direction>> = mutableListOf()

            for (idx in 0 until MAX_CHECK_DISTANCE) {
                val sourcePos = propPos.offset(prevDirection)
                val sourceState = world.getBlockState(sourcePos)
                if (sourcePos == pos || !sourceState.isOf(this)) break
                val source = sourceState.get(SOURCE).direction
                propagation.addFirst(sourcePos to prevDirection!!)
                if (source == null) {
                    world.setBlockState(sourcePos.north(5), Blocks.DIAMOND_BLOCK.defaultState)
                    break
                }
                propPos = sourcePos
                prevDirection = source
            }
            propagation.forEach { (pPos, pDir) ->
                val pState = world.getBlockState(pPos).with(SOURCE, DirectionOrNullState.fromDirection(pDir))
                world.setBlockState(pPos, pState)
                setDebugMagTer(pState, world, pPos)
            }
        }
        setDebugMagTer(state.with(SOURCE, DirectionOrNullState.NONE), world, pos)
    }

    fun setDebugMagTer(state: BlockState, world: World, pos: BlockPos) {
        val dir = state.get(SOURCE).direction
        val tarPos = pos.north(5)
        val worldState = world.getBlockState(tarPos)
        println(worldState.block)
        if (worldState.materialReplaceable() || worldState.isOf(Blocks.CHERRY_LOG) || worldState.isOf(Blocks.MAGENTA_GLAZED_TERRACOTTA)) {
            val newState = if (dir == null) {
                Blocks.CHERRY_LOG.defaultState
            } else {
                Blocks.MAGENTA_GLAZED_TERRACOTTA.defaultState.with(GlazedTerracottaBlock.FACING, dir)
            }
            world.setBlockState(tarPos, newState)
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
            if (!state.get(POWERED)) {
                setThisAsSource(state.with(POWERED, true), world, pos)
                world.emitGameEvent(null, GameEvent.BLOCK_CHANGE, pos)
            }
            world.setBlockState(pos, state.with(POWERED, bl), 3)
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
        val worldTime = (world.time % WORLD_TIME_MOD).toInt()
        val activity = state.get(ACTIVE)
        val facing = state.get(FACING)

        if (worldTime == 0 && state.get(SOURCE) == DirectionOrNullState.NONE) {
            if (state.get(AGE) == 0) {
                val age = when (activity) {
                    2 -> COOLDOWN_DURATION
                    1 -> {
                        playBlast(state, world, pos, random)
                        BLAST_DURATION
                    }

                    0 -> {
                        playAnticipation(state, world, pos, random)
                        WARMUP_DURATION
                    }

                    else -> COOLDOWN_DURATION
                }
                world.setBlockState(pos, state.with(AGE, age).cycle(ACTIVE))
            } else {
                world.setBlockState(pos, state.with(AGE, state.get(AGE) - 1))
            }
        }
        if (activity != 0) {
            if (activity == 2) {
                val windLength = windLength(state, world, pos)
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

            } else if (state.get(AGE) == WARMUP_DURATION) {
                warmUpParticles(state, world, pos, world.random)
            }
        }
        exhaustTick(state, world, pos)
    }

    private fun windLength(state: BlockState, world: ServerWorld, pos: BlockPos): Int =
        WindLogic.windLength(world, pos, state.get(Properties.FACING), MAX_BLAST_HEIGHT)

    private fun getPower(state: BlockState, world: World, pos: BlockPos): Int {
        val source = state.get(SOURCE).direction ?: return world.getReceivedRedstonePower(pos)
        val sourcePos = pos.offset(source)
        val sourceBlock = world.getBlockState(sourcePos)
        if (sourceBlock.isOf(this)) {
            return getPower(sourceBlock, world, sourcePos)
        } else {
            world.setBlockState(pos, state.with(SOURCE, DirectionOrNullState.NONE))
            return getPower(state, world, pos)
        }
    }

    private fun getPower(state: BlockState, world: WorldAccess, pos: BlockPos): Int {
        val source = state.get(SOURCE).direction ?: return world.getReceivedRedstonePower(pos)
        val sourcePos = pos.offset(source)
        val sourceBlock = world.getBlockState(sourcePos)
        return if (sourceBlock.isOf(this)) getPower(sourceBlock, world, sourcePos)
        else 12
    }

    private fun playBlast(state: BlockState, world: WorldAccess, pos: BlockPos, random: RandomGenerator) {
        playsound(DuskSoundEvents.BLOCK_ORGAN_NOTE, NoteBlock.getNotePitch(state.get(NOTE)), world, pos)
        playsound(DuskSoundEvents.BLOCK_EXHAUST_ATTACK, random.nextFloat() * 0.2f + 0.9f, world, pos)
    }

    private fun playAnticipation(state: BlockState, world: WorldAccess, pos: BlockPos, random: RandomGenerator) {
        playsound(DuskSoundEvents.BLOCK_EXHAUST_ANTICIPATION, random.nextFloat() * 0.2f + 0.9f, world, pos)
    }


    private fun playsound(
        soundEvent: SoundEvent,
        pitch: Float,
        world: WorldAccess,
        pos: BlockPos
    ) {
        world.playSound(
            null as PlayerEntity?,
            pos,
            soundEvent,
            SoundCategory.BLOCKS,
            1f, pitch
        )
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
        val windLength = windLength(state, world, pos)
        if (windLength != 0) {
            val facing = state.get(FACING)
            val offset = if (facing.id % 2 == 1) facing.vector.toVec3d() else Vec3d.ZERO
            repeat((windLength / 3) + 1) {
                world.spawnParticles(
                    DuskParticles.EXHAUST_WARMUP,
                    Vec3d(
                        random.nextDouble(),
                        random.nextDouble() * windLength - 1.5,
                        random.nextDouble()
                    ).rotateFromUp(facing).add(pos.x.toDouble(), pos.y.toDouble(), pos.z.toDouble()).add(offset),
                    Vec3d(0.0, 0.15 + random.nextDouble() * 0.05, 0.0).rotateFromUp(facing),
                    32.0
                )
            }
        }
    }

    private fun blastParticles(state: BlockState, world: ServerWorld, pos: BlockPos, random: RandomGenerator) {
        val facing = state.get(FACING)
        val offset = if (facing.id % 2 == 1) facing.vector.toVec3d() else Vec3d.ZERO
        repeat(random.range(3, 7)) {
            world.spawnParticles(
                DuskParticles.EXHAUST_BLAST,
                Vec3d(random.nextDouble(), random.nextDouble(), random.nextDouble()).rotateFromUp(facing)
                    .add(pos.x.toDouble(), pos.y.toDouble(), pos.z.toDouble()).add(offset),
                Vec3d(0.0, 0.5 + random.nextDouble() * 0.25, 0.0).rotateFromUp(facing),
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
        val AGE: IntProperty = Properties.AGE_15
        val NOTE: IntProperty = Properties.NOTE

        const val WORLD_TIME_MOD = 10
        const val COOLDOWN_DURATION = 12
        const val WARMUP_DURATION = 2
        const val BLAST_DURATION = 3
        const val MAX_BLAST_HEIGHT = 15
        const val MAX_CHECK_DISTANCE = 32

        fun Chunk.getExhaustAttachment(blockPos: BlockPos): ExhaustData? =
            this.getAttached(DuskAttachmentTypes.EXHAUST_DATA)?.get(blockPos)

        fun Chunk.setExhaustAttachment(blockPos: BlockPos, data: ExhaustData) {
            val current = (this.getAttached(DuskAttachmentTypes.EXHAUST_DATA) ?: mutableMapOf()).toMutableMap()
            current[blockPos] = data
            this.setAttached(DuskAttachmentTypes.EXHAUST_DATA, current)
        }
        fun Chunk.removeExhaustAttachment(blockPos: BlockPos) {
            val current = this.getAttached(DuskAttachmentTypes.EXHAUST_DATA)?.toMutableMap() ?: return
            current.remove(blockPos)
            this.setAttached(DuskAttachmentTypes.EXHAUST_DATA, current)
        }
    }
}