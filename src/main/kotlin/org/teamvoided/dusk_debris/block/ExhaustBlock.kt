package org.teamvoided.dusk_debris.block

import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvent
import net.minecraft.sounds.SoundSource
import net.minecraft.util.RandomSource
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.level.Level
import net.minecraft.world.level.LevelAccessor
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.GlazedTerracottaBlock
import net.minecraft.world.level.block.NoteBlock
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.*
import net.minecraft.world.level.chunk.ChunkAccess
import net.minecraft.world.level.gameevent.GameEvent
import net.minecraft.world.phys.Vec3
import org.teamvoided.dusk_debris.block.attachments.ExhaustData
import org.teamvoided.dusk_debris.block.not_blocks.DirectionOrNullState
import org.teamvoided.dusk_debris.block.not_blocks.DuskProperties
import org.teamvoided.dusk_debris.data.tags.DuskEntityTypeTags
import org.teamvoided.dusk_debris.entity.helper.WindLogic
import org.teamvoided.dusk_debris.entity.helper.WindLogic.inFanWind
import org.teamvoided.dusk_debris.init.DuskAttachmentTypes
import org.teamvoided.dusk_debris.init.DuskParticles
import org.teamvoided.dusk_debris.init.DuskSoundEvents
import org.teamvoided.dusk_debris.util.spawnParticles
import org.teamvoided.dusk_debris.util.toVec3d
import kotlin.math.absoluteValue

class ExhaustBlock(settings: Properties) : SixWayFacingBlock(settings) {
    init {
        this.registerDefaultState(
            stateDefinition.any()
                .setValue(ACTIVE, 0)
                .setValue(POWERED, false)
                .setValue(FACING, Direction.UP)
                .setValue(SOURCE, DirectionOrNullState.NONE)
                .setValue(AGE, COOLDOWN_DURATION)
                .setValue(NOTE, 0)
        )

    }

    override fun setPlacedBy(
        world: Level,
        pos: BlockPos,
        state: BlockState,
        placer: LivingEntity?,
        itemStack: ItemStack
    ) {
        super.setPlacedBy(world, pos, state, placer, itemStack)
        exhaustTick(state, world, pos)

    }

    override fun playerWillDestroy(world: Level, pos: BlockPos, state: BlockState, player: Player?): BlockState {
        world.getChunkAt(pos).removeExhaustAttachment(pos)
        return super.playerWillDestroy(world, pos, state, player)
    }

    override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block, BlockState>) {
        super.createBlockStateDefinition(builder)
        builder.add(ACTIVE, POWERED, SOURCE, AGE, NOTE)
    }

    override fun getStateForPlacement(ctx: BlockPlaceContext): BlockState {
        val state = defaultBlockState()
            .setValue(FACING, ctx.nearestLookingDirection.opposite)
            .setValue(NOTE, (ctx.clickedPos.x + ctx.clickedPos.y + ctx.clickedPos.z).absoluteValue % 25)
        return if (ctx.nearestLookingDirection.axis != ctx.clickedFace.axis) {
            matchWithSourceIfPresent(
                state.setValue(SOURCE, DirectionOrNullState.fromDirection(ctx.clickedFace.opposite)),
                ctx.level,
                ctx.clickedPos
            )
        } else state
    }

    override fun updateShape(
        state: BlockState,
        direction: Direction,
        neighborState: BlockState,
        world: LevelAccessor,
        pos: BlockPos,
        neighborPos: BlockPos
    ): BlockState {
        exhaustTick(state, world, pos)
        return matchWithSourceIfPresent(state, world, pos)
    }

    private fun matchWithSourceIfPresent(
        state: BlockState,
        world: LevelAccessor,
        pos: BlockPos,
    ): BlockState {
        //world.getWorldChunk(pos).setExhaustAttachment(pos, )


        val source = state.getValue(SOURCE)
        if (source != DirectionOrNullState.NONE) {
            //getSource(state, world, pos) ?: return state.with(SOURCE, DirectionOrNullState.NONE)

            val targetState: BlockState = world.getBlockState(pos.relative(source.direction))
            if (!targetState.`is`(this) || targetState.getValue(SOURCE) == source.direction!!.opposite)
                return state.setValue(SOURCE, DirectionOrNullState.NONE)
            val targetActivity = targetState.getValue(ACTIVE)
            if (targetActivity != state.getValue(ACTIVE) && targetActivity != 0) {
                if (targetActivity == 1) playAnticipation(state, world, pos, world.random)
                else playBlast(state, world, pos, world.random)
            }

            return state
                .setValue(ACTIVE, targetActivity)
                .setValue(POWERED, targetState.getValue(POWERED))
                .setValue(AGE, targetState.getValue(AGE))
                .setValue(NOTE, targetState.getValue(NOTE))
        }
        return state
    }

    private fun getSource(state: BlockState, world: LevelAccessor, pos: BlockPos): BlockPos? {
        val source = state.getValue(SOURCE).direction ?: return pos
        val sourcePos = pos.relative(source)
        val sourceBlock = world.getBlockState(sourcePos)
        return if (sourceBlock.`is`(this)) getSource(sourceBlock, world, sourcePos)
        else null
    }

    private fun setThisAsSource(state: BlockState, world: Level, pos: BlockPos) {
        world.setBlockAndUpdate(pos, state.setValue(SOURCE, DirectionOrNullState.NONE))
        var prevDirection: Direction? = state.getValue(SOURCE).direction
        if (prevDirection != null) {
            var propPos: BlockPos = pos
            val propagation: MutableList<Pair<BlockPos, Direction>> = mutableListOf()

            for (idx in 0 until MAX_CHECK_DISTANCE) {
                val sourcePos = propPos.relative(prevDirection)
                val sourceState = world.getBlockState(sourcePos)
                if (sourcePos == pos || !sourceState.`is`(this)) break
                val source = sourceState.getValue(SOURCE).direction
                propagation.addFirst(sourcePos to prevDirection!!)
                if (source == null) {
                    world.setBlockAndUpdate(sourcePos.north(5), Blocks.DIAMOND_BLOCK.defaultBlockState())
                    break
                }
                propPos = sourcePos
                prevDirection = source
            }
            propagation.forEach { (pPos, pDir) ->
                val pState = world.getBlockState(pPos).setValue(SOURCE, DirectionOrNullState.fromDirection(pDir))
                world.setBlockAndUpdate(pPos, pState)
                setDebugMagTer(pState, world, pPos)
            }
        }
        setDebugMagTer(state.setValue(SOURCE, DirectionOrNullState.NONE), world, pos)
    }

    fun setDebugMagTer(state: BlockState, world: Level, pos: BlockPos) {
        val dir = state.getValue(SOURCE).direction
        val tarPos = pos.north(5)
        val worldState = world.getBlockState(tarPos)
        println(worldState.block)
        if (worldState.canBeReplaced() || worldState.`is`(Blocks.CHERRY_LOG) || worldState.`is`(Blocks.MAGENTA_GLAZED_TERRACOTTA)) {
            val newState = if (dir == null) {
                Blocks.CHERRY_LOG.defaultBlockState()
            } else {
                Blocks.MAGENTA_GLAZED_TERRACOTTA.defaultBlockState().setValue(GlazedTerracottaBlock.FACING, dir)
            }
            world.setBlockAndUpdate(tarPos, newState)
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
        exhaustTick(state, world, pos)
    }

    override fun onPlace(state: BlockState, world: Level, pos: BlockPos, oldState: BlockState, notify: Boolean) {
        if (oldState.block != state.block && world is ServerLevel) {
            this.setState(state, world, pos)
        }
    }

    private fun setState(state: BlockState, world: ServerLevel, pos: BlockPos) {
        val bl = world.hasNeighborSignal(pos)
        if (bl != state.getValue(POWERED)) {
            if (!state.getValue(POWERED)) {
                setThisAsSource(state.setValue(POWERED, true), world, pos)
                world.gameEvent(null, GameEvent.BLOCK_CHANGE, pos)
            }
            world.setBlock(pos, state.setValue(POWERED, bl), 3)
        }
        exhaustTick(state, world, pos)
    }

    override fun isRandomlyTicking(state: BlockState): Boolean =
        state.getValue(SOURCE) == DirectionOrNullState.NONE || state.getValue(ACTIVE) != 0

    override fun randomTick(state: BlockState, world: ServerLevel, pos: BlockPos, random: RandomSource) =
        world.scheduleTick(pos, state.block, 0)


    private fun exhaustTick(state: BlockState, world: LevelAccessor, pos: BlockPos) {
        if (isRandomlyTicking(state)) world.scheduleTick(pos, this, 1)
    }

    override fun tick(state: BlockState, world: ServerLevel, pos: BlockPos, random: RandomSource) {
        val worldTime = (world.gameTime % WORLD_TIME_MOD).toInt()
        val activity = state.getValue(ACTIVE)
        val facing = state.getValue(FACING)

        if (worldTime == 0 && state.getValue(SOURCE) == DirectionOrNullState.NONE) {
            if (state.getValue(AGE) == 0) {
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
                world.setBlockAndUpdate(pos, state.setValue(AGE, age).cycle(ACTIVE))
            } else {
                world.setBlockAndUpdate(pos, state.setValue(AGE, state.getValue(AGE) - 1))
            }
        }
        if (activity != 0) {
            if (activity == 2) {
                val windLength = windLength(state, world, pos)
                val entityList = getEntityList(world, pos, facing, windLength)
                if (entityList.isNotEmpty()) {
                    entityList.forEach {
                        it.hurt(world.damageSources().magic(), 6f)
                        it.inFanWind(
                            facing.normal.toVec3d().scale(0.1)
                                .add(0.0, if (facing == Direction.UP) it.gravity * 0.4 else 0.0, 0.0)
                        )
                    }
                }
                blastParticles(state, world, pos, world.random)

            } else if (state.getValue(AGE) == WARMUP_DURATION) {
                warmUpParticles(state, world, pos, world.random)
            }
        }
        exhaustTick(state, world, pos)
    }

    private fun windLength(state: BlockState, world: ServerLevel, pos: BlockPos): Int =
        WindLogic.windLength(world, pos, state.getValue(BlockStateProperties.FACING), MAX_BLAST_HEIGHT)

    private fun getPower(state: BlockState, world: Level, pos: BlockPos): Int {
        val source = state.getValue(SOURCE).direction ?: return world.getBestNeighborSignal(pos)
        val sourcePos = pos.relative(source)
        val sourceBlock = world.getBlockState(sourcePos)
        if (sourceBlock.`is`(this)) {
            return getPower(sourceBlock, world, sourcePos)
        } else {
            world.setBlockAndUpdate(pos, state.setValue(SOURCE, DirectionOrNullState.NONE))
            return getPower(state, world, pos)
        }
    }

    private fun getPower(state: BlockState, world: LevelAccessor, pos: BlockPos): Int {
        val source = state.getValue(SOURCE).direction ?: return world.getBestNeighborSignal(pos)
        val sourcePos = pos.relative(source)
        val sourceBlock = world.getBlockState(sourcePos)
        return if (sourceBlock.`is`(this)) getPower(sourceBlock, world, sourcePos)
        else 12
    }

    private fun playBlast(state: BlockState, world: LevelAccessor, pos: BlockPos, random: RandomSource) {
        playsound(DuskSoundEvents.BLOCK_ORGAN_NOTE, NoteBlock.getPitchFromNote(state.getValue(NOTE)), world, pos)
        playsound(DuskSoundEvents.BLOCK_EXHAUST_ATTACK, random.nextFloat() * 0.2f + 0.9f, world, pos)
    }

    private fun playAnticipation(state: BlockState, world: LevelAccessor, pos: BlockPos, random: RandomSource) {
        playsound(DuskSoundEvents.BLOCK_EXHAUST_ANTICIPATION, random.nextFloat() * 0.2f + 0.9f, world, pos)
    }


    private fun playsound(
        soundEvent: SoundEvent,
        pitch: Float,
        world: LevelAccessor,
        pos: BlockPos
    ) {
        world.playSound(
            null as Player?,
            pos,
            soundEvent,
            SoundSource.BLOCKS,
            1f, pitch
        )
    }

    private fun getEntityList(
        world: Level,
        pos: BlockPos,
        facing: Direction,
        windLength: Int
    ): MutableList<Entity> {
        return world.getEntities(null, FanBlock.getBox(facing, windLength.toDouble()).move(pos.center))
        { !it.type.`is`(DuskEntityTypeTags.FANS_DONT_AFFECT) }
    }

    private fun warmUpParticles(state: BlockState, world: ServerLevel, pos: BlockPos, random: RandomSource) {
        val windLength = windLength(state, world, pos)
        if (windLength != 0) {
            val facing = state.getValue(FACING)
            val offset = if (facing.get3DDataValue() % 2 == 1) facing.normal.toVec3d() else Vec3.ZERO
            repeat((windLength / 3) + 1) {
                world.spawnParticles(
                    DuskParticles.EXHAUST_WARMUP,
                    Vec3(
                        random.nextDouble(),
                        random.nextDouble() * windLength - 1.5,
                        random.nextDouble()
                    ).rotateFromUp(facing).add(pos.x.toDouble(), pos.y.toDouble(), pos.z.toDouble()).add(offset),
                    Vec3(0.0, 0.15 + random.nextDouble() * 0.05, 0.0).rotateFromUp(facing),
                    32.0
                )
            }
        }
    }

    private fun blastParticles(state: BlockState, world: ServerLevel, pos: BlockPos, random: RandomSource) {
        val facing = state.getValue(FACING)
        val offset = if (facing.get3DDataValue() % 2 == 1) facing.normal.toVec3d() else Vec3.ZERO
        repeat(random.nextInt(3, 7)) {
            world.spawnParticles(
                DuskParticles.EXHAUST_BLAST,
                Vec3(random.nextDouble(), random.nextDouble(), random.nextDouble()).rotateFromUp(facing)
                    .add(pos.x.toDouble(), pos.y.toDouble(), pos.z.toDouble()).add(offset),
                Vec3(0.0, 0.5 + random.nextDouble() * 0.25, 0.0).rotateFromUp(facing),
                128.0
            )
        }
    }

    fun Vec3.rotateFromUp(direction: Direction): Vec3 {
        return when (direction) {
            Direction.DOWN -> this.multiply(1.0, -1.0, 1.0)
            Direction.UP -> this
            Direction.NORTH -> Vec3(x, z, -y)
            Direction.SOUTH -> Vec3(x, z, y)
            Direction.WEST -> Vec3(-y, x, z)
            Direction.EAST -> Vec3(y, x, z)
        }
    }

    companion object {
        val POWERED: BooleanProperty = BlockStateProperties.POWERED
        val ACTIVE: IntegerProperty = DuskProperties.ACTIVE_STATE_INT
        val FACING: DirectionProperty = BlockStateProperties.FACING
        val SOURCE: EnumProperty<DirectionOrNullState> = DuskProperties.FACING_OR_NULL
        val AGE: IntegerProperty = BlockStateProperties.AGE_15
        val NOTE: IntegerProperty = BlockStateProperties.NOTE

        const val WORLD_TIME_MOD = 10
        const val COOLDOWN_DURATION = 12
        const val WARMUP_DURATION = 2
        const val BLAST_DURATION = 3
        const val MAX_BLAST_HEIGHT = 15
        const val MAX_CHECK_DISTANCE = 32

        fun ChunkAccess.getExhaustAttachment(blockPos: BlockPos): ExhaustData? =
            this.getAttached(DuskAttachmentTypes.EXHAUST_DATA)?.get(blockPos)

        fun ChunkAccess.setExhaustAttachment(blockPos: BlockPos, data: ExhaustData) {
            val current = (this.getAttached(DuskAttachmentTypes.EXHAUST_DATA) ?: mutableMapOf()).toMutableMap()
            current[blockPos] = data
            this.setAttached(DuskAttachmentTypes.EXHAUST_DATA, current)
        }
        fun ChunkAccess.removeExhaustAttachment(blockPos: BlockPos) {
            val current = this.getAttached(DuskAttachmentTypes.EXHAUST_DATA)?.toMutableMap() ?: return
            current.remove(blockPos)
            this.setAttached(DuskAttachmentTypes.EXHAUST_DATA, current)
        }
    }
}