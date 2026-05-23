package org.teamvoided.dusk_debris.block

import com.mojang.serialization.MapCodec
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.FrontAndTop
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.util.Mth
import net.minecraft.util.RandomSource
import net.minecraft.world.entity.player.Player
import net.minecraft.world.entity.projectile.Projectile
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Mirror
import net.minecraft.world.level.block.Rotation
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.world.level.block.state.properties.EnumProperty
import net.minecraft.world.level.portal.DimensionTransition
import net.minecraft.world.phys.AABB
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.Vec3
import org.teamvoided.dusk_debris.block.not_blocks.DuskProperties
import org.teamvoided.dusk_debris.block.not_blocks.GodhomeBronzePhase
import org.teamvoided.dusk_debris.data.tags.DuskEntityTypeTags
import org.teamvoided.dusk_debris.module.DuskGameRules
import org.teamvoided.dusk_debris.particle.color.GodhomeParticleEffect
import org.teamvoided.dusk_debris.util.Utils.rotate90
import org.teamvoided.dusk_debris.util.addParticle
import org.teamvoided.dusk_debris.util.spawnParticles
import kotlin.math.cos
import kotlin.math.sin

class ShiftBlock(settings: Properties) : Block(settings) {
    public override fun codec(): MapCodec<out ShiftBlock> {
        return CODEC
    }

    init {
        this.registerDefaultState(
            stateDefinition.any()
                .setValue(ORIENTATION, FrontAndTop.NORTH_UP)
                .setValue(PHASE, GodhomeBronzePhase.SOMBER)
        )
    }

    override fun getStateForPlacement(ctx: BlockPlaceContext): BlockState {
        val direction = ctx.nearestLookingDirection.opposite
        val facing = when (direction) {
            Direction.DOWN -> ctx.horizontalDirection.opposite
            Direction.UP -> ctx.horizontalDirection
            Direction.NORTH, Direction.SOUTH, Direction.WEST, Direction.EAST -> Direction.UP
            else -> throw MatchException("playerLookDirection in ShiftBlock is null", null as Throwable?)
        }
        return defaultBlockState()
            .setValue(ORIENTATION, FrontAndTop.fromFrontAndTop(direction, facing))
    }

    override fun onProjectileHit(world: Level, state: BlockState, hit: BlockHitResult, projectile: Projectile) {
        if (!world.isClientSide) {
            val blockPos = hit.blockPos
            if (state.getValue(PHASE) == GodhomeBronzePhase.SOMBER &&
                projectile.mayInteract(world, blockPos)
            ) {
                val serverWorld = world as ServerLevel
                godhomeStrongParticles(serverWorld, blockPos, 200)
                world.setBlockAndUpdate(blockPos, state.setValue(PHASE, GodhomeBronzePhase.RADIANT))
                world.scheduleTick(blockPos, this, 30)
            }
        }
    }

    override fun animateTick(state: BlockState, world: Level, pos: BlockPos, random: RandomSource) {
        if (state.getValue(PHASE) != GodhomeBronzePhase.SOMBER) {
            godhomeStrongParticles(world, pos)
        }
    }

    override fun tick(state: BlockState, world: ServerLevel, pos: BlockPos, random: RandomSource) {
        if (state.getValue(PHASE) == GodhomeBronzePhase.RADIANT) {
            val direction = state.getValue(ORIENTATION).front()
            findTargetTeleport(world, state, pos, pos.relative(direction), direction, travelDistance)
        }
        world.setBlockAndUpdate(pos, state.setValue(PHASE, GodhomeBronzePhase.SOMBER))
    }

    private fun findTargetTeleport(
        world: ServerLevel,
        startState: BlockState,
        originPos: BlockPos,
        pos: BlockPos,
        direction: Direction,
        step: Int
    ) {
        val worldBlockState = world.getBlockState(pos)
        if (worldBlockState.`is`(this)) {
            godhomeStrongParticles(world, pos, 200)
            world.setBlockAndUpdate(pos, worldBlockState.setValue(PHASE, GodhomeBronzePhase.SHINING))
            world.scheduleTick(pos, this, 60)
            val oldPosVec = originPos.center
            val newPosVec = pos.center
            shiftEntities(
                world,
                oldPosVec,
                newPosVec,
                startState.getValue(ORIENTATION),
                worldBlockState.getValue(ORIENTATION)
            )
            world.playSound(
                null as Player?,
                oldPosVec.x,
                oldPosVec.y,
                oldPosVec.z,
                SoundEvents.PLAYER_TELEPORT,
                SoundSource.BLOCKS
            )
            world.playSound(
                null as Player?,
                newPosVec.x,
                newPosVec.y,
                newPosVec.z,
                SoundEvents.PLAYER_TELEPORT,
                SoundSource.BLOCKS
            )
        } else if (step > 0) {
            findTargetTeleport(world, startState, originPos, pos.relative(direction), direction, step - 1)
        }
    }

    private fun shiftEntities(
        world: ServerLevel,
        prevPos: Vec3,
        newPos: Vec3,
        startOrientation: FrontAndTop,
        endOrientation: FrontAndTop
    ) {
        val entitiesInRange =
            world.getEntities(null, rangeBox.move(prevPos))
            {
                !it.type.`is`(DuskEntityTypeTags.FANS_DONT_AFFECT)
                it.distanceToSqr(prevPos) <= range * range
            }
        entitiesInRange.sortBy { it.position().distanceToSqr(prevPos) }
        entitiesInRange.forEachIndexed { idx, entity ->
            if (idx > world.gameRules.getInt(DuskGameRules.MAX_ENTITY_SHIFT))
                return
            val entityVel = entity.deltaMovement
            var entityPos = entity.position().subtract(prevPos)
            entityPos = rotate(entityPos, startOrientation, endOrientation)
            entityPos = entityPos.add(newPos)
            var worldBlock = world.getBlockState(
                BlockPos(
                    entityPos.x.toInt(),
                    (entityPos.y + entity.eyeHeight).toInt(),
                    entityPos.z.toInt()
                )
            )
            if (worldBlock.isSolid) {
                worldBlock = world.getBlockState(
                    BlockPos(
                        newPos.x.toInt(),
                        (newPos.y + entity.eyeHeight + 0.5).toInt(),
                        newPos.z.toInt()
                    )
                )
                entityPos = if (worldBlock.isSolid) {
                    entity.position()
                } else {
                    newPos.add(0.0, 1.0, 0.0)
                }
            }
            val facing = rotate(entity.yRot, startOrientation, endOrientation)
            entity.changeDimension(
                DimensionTransition(
                    world,
                    entityPos,
                    entity.deltaMovement,
                    facing, entity.xRot,
                    DimensionTransition.DO_NOTHING
                )
            )

            entity.resetFallDistance()
            entity.setDeltaMovement(rotate(entityVel, startOrientation, endOrientation))
            entity.hurtMarked = true
        }
    }

    fun rotate(input: Vec3, startDirection: FrontAndTop, endDirection: FrontAndTop): Vec3 {
        val yaw =
            if (startDirection.front().axis == Direction.Axis.Y) {
                (startDirection.top().get2DDataValue() - endDirection.front().get2DDataValue()) * -rotate90
            } else if (endDirection.front().axis == Direction.Axis.Y) {
                (startDirection.front().get2DDataValue() - endDirection.top().get2DDataValue()) * -rotate90
            } else {
                (startDirection.front().get2DDataValue() - endDirection.front().get2DDataValue()) * -rotate90
            }
//        val yaw = (
//                (if (startDirection.facing.axis == Direction.Axis.Y) startDirection.rotation.vector.y
//                else startDirection.facing.horizontal) +
//                        (if (endDirection.facing.axis == Direction.Axis.Y) endDirection.rotation.vector.y
//                        else endDirection.facing.horizontal)) * rotate90
//
        val x = input.x * cos(yaw) - input.z * sin(yaw)
        val z = input.x * sin(yaw) + input.z * cos(yaw)

        return Vec3(x, input.y, z)
    }

    fun rotate(
        yaw: Float,
        startDirection: FrontAndTop,
        endDirection: FrontAndTop
    ): Float {
        val yaw2 =
            if (startDirection.front().axis == Direction.Axis.Y) {
                (startDirection.top().get2DDataValue() - endDirection.front().get2DDataValue()) * -90
            } else if (endDirection.front().axis == Direction.Axis.Y) {
                (startDirection.front().get2DDataValue() - endDirection.top().get2DDataValue()) * -90
            } else {
                (startDirection.front().get2DDataValue() - endDirection.front().get2DDataValue()) * -90
            }
        return yaw + yaw2
    }

    override fun rotate(state: BlockState, rotation: Rotation): BlockState {
        return state.setValue(ORIENTATION, rotation.rotation().rotate(state.getValue(ORIENTATION)))
    }

    override fun mirror(state: BlockState, mirror: Mirror): BlockState {
        return state.setValue(ORIENTATION, mirror.rotation().rotate(state.getValue(ORIENTATION)))
    }

    override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block, BlockState>) {
        super.createBlockStateDefinition(builder)
        builder.add(ORIENTATION, PHASE)
    }

    companion object {
        val CODEC: MapCodec<ShiftBlock> = simpleCodec(::ShiftBlock)
        val ORIENTATION: EnumProperty<FrontAndTop> = BlockStateProperties.ORIENTATION
        val PHASE: EnumProperty<GodhomeBronzePhase> = DuskProperties.GODHOME_BRONZE_PHASE

        private val travelDistance = 40
        private val range = 15.0
        val rangeBox = AABB(
            -range, -range, -range,
            range, range, range
        )

        private fun godhomeStrongParticles(world: Level, pos: BlockPos, repeat: Int = 5) {
            val rand = world.random
            repeat(repeat) {
                val randInRadius = Mth.sqrt(rand.nextFloat()) * range
                val particlePos = Vec3(
                    rand.nextDouble() - rand.nextDouble(),
                    rand.nextDouble() - rand.nextDouble(),
                    rand.nextDouble() - rand.nextDouble()
                ).normalize().scale(randInRadius).add(pos.center)
                val particleVel = Vec3(
                    0.0,
                    world.random.nextDouble() * 0.5 + 0.01,
                    0.0
                )
                if (world.isClientSide) {
                    world.addParticle(
                        GodhomeParticleEffect(0xFFF1CC),
                        true,
                        particlePos,
                        particleVel
                    )
                } else {
                    (world as ServerLevel).spawnParticles(
                        GodhomeParticleEffect(0xFFF1CC),
                        particlePos,
                        particleVel,
                        128.0
                    )
                }
            }
        }
    }
}