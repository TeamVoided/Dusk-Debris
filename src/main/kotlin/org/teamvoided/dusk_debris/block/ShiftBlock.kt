package org.teamvoided.dusk_debris.block

import com.mojang.serialization.MapCodec
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.enums.JigsawOrientation
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.projectile.ProjectileEntity
import net.minecraft.item.ItemPlacementContext
import net.minecraft.server.world.ServerWorld
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.state.StateManager
import net.minecraft.state.property.EnumProperty
import net.minecraft.state.property.Properties
import net.minecraft.util.BlockMirror
import net.minecraft.util.BlockRotation
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.*
import net.minecraft.util.random.RandomGenerator
import net.minecraft.world.DimensionTransition
import net.minecraft.world.World
import org.teamvoided.dusk_debris.block.not_blocks.DuskProperties
import org.teamvoided.dusk_debris.block.not_blocks.GodhomeBronzePhase
import org.teamvoided.dusk_debris.data.tags.DuskEntityTypeTags
import org.teamvoided.dusk_debris.module.DuskGameRules
import org.teamvoided.dusk_debris.particle.GodhomeParticleEffect
import org.teamvoided.dusk_debris.util.Utils.rotate90
import org.teamvoided.dusk_debris.util.addParticle
import org.teamvoided.dusk_debris.util.spawnParticles
import kotlin.math.cos
import kotlin.math.sin

class ShiftBlock(settings: Settings) : Block(settings) {
    public override fun getCodec(): MapCodec<out ShiftBlock> {
        return CODEC
    }

    init {
        this.defaultState = stateManager.defaultState
            .with(ORIENTATION, JigsawOrientation.NORTH_UP)
            .with(PHASE, GodhomeBronzePhase.SOMBER)
    }

    override fun getPlacementState(ctx: ItemPlacementContext): BlockState {
        val direction = ctx.playerLookDirection.opposite
        val facing = when (direction) {
            Direction.DOWN -> ctx.playerFacing.opposite
            Direction.UP -> ctx.playerFacing
            Direction.NORTH, Direction.SOUTH, Direction.WEST, Direction.EAST -> Direction.UP
            else -> throw MatchException("playerLookDirection in ShiftBlock is null", null as Throwable?)
        }
        return defaultState
            .with(ORIENTATION, JigsawOrientation.byDirections(direction, facing))
    }

    override fun onProjectileHit(world: World, state: BlockState, hit: BlockHitResult, projectile: ProjectileEntity) {
        if (!world.isClient) {
            val blockPos = hit.blockPos
            if (state.get(PHASE) == GodhomeBronzePhase.SOMBER &&
                projectile.canModifyAt(world, blockPos)
            ) {
                val serverWorld = world as ServerWorld
                godhomeStrongParticles(serverWorld, blockPos, 200)
                world.setBlockState(blockPos, state.with(PHASE, GodhomeBronzePhase.RADIANT))
                world.scheduleBlockTick(blockPos, this, 30)
            }
        }
    }

    override fun randomDisplayTick(state: BlockState, world: World, pos: BlockPos, random: RandomGenerator) {
        if (state.get(PHASE) != GodhomeBronzePhase.SOMBER) {
            godhomeStrongParticles(world, pos)
        }
    }

    override fun scheduledTick(state: BlockState, world: ServerWorld, pos: BlockPos, random: RandomGenerator) {
        if (state.get(PHASE) == GodhomeBronzePhase.RADIANT) {
            val direction = state.get(ORIENTATION).facing
            findTargetTeleport(world, state, pos, pos.offset(direction), direction, travelDistance)
        }
        world.setBlockState(pos, state.with(PHASE, GodhomeBronzePhase.SOMBER))
    }

    private fun findTargetTeleport(
        world: ServerWorld,
        startState: BlockState,
        originPos: BlockPos,
        pos: BlockPos,
        direction: Direction,
        step: Int
    ) {
        val worldBlockState = world.getBlockState(pos)
        if (worldBlockState.isOf(this)) {
            godhomeStrongParticles(world, pos, 200)
            world.setBlockState(pos, worldBlockState.with(PHASE, GodhomeBronzePhase.SHINING))
            world.scheduleBlockTick(pos, this, 60)
            val oldPosVec = originPos.ofCenter()
            val newPosVec = pos.ofCenter()
            shiftEntities(
                world,
                oldPosVec,
                newPosVec,
                startState.get(ORIENTATION),
                worldBlockState.get(ORIENTATION)
            )
            world.playSound(
                null as PlayerEntity?,
                oldPosVec.x,
                oldPosVec.y,
                oldPosVec.z,
                SoundEvents.ENTITY_PLAYER_TELEPORT,
                SoundCategory.BLOCKS
            )
            world.playSound(
                null as PlayerEntity?,
                newPosVec.x,
                newPosVec.y,
                newPosVec.z,
                SoundEvents.ENTITY_PLAYER_TELEPORT,
                SoundCategory.BLOCKS
            )
        } else if (step > 0) {
            findTargetTeleport(world, startState, originPos, pos.offset(direction), direction, step - 1)
        }
    }

    private fun shiftEntities(
        world: ServerWorld,
        prevPos: Vec3d,
        newPos: Vec3d,
        startOrientation: JigsawOrientation,
        endOrientation: JigsawOrientation
    ) {
        val entitiesInRange =
            world.getOtherEntities(null, rangeBox.offset(prevPos))
            {
                !it.type.isIn(DuskEntityTypeTags.FANS_DONT_AFFECT)
                it.squaredDistanceTo(prevPos) <= range * range
            }
        entitiesInRange.sortBy { it.pos.squaredDistanceTo(prevPos) }
        entitiesInRange.forEachIndexed { idx, entity ->
            if (idx > world.gameRules.getIntValue(DuskGameRules.MAX_ENTITY_SHIFT))
                return
            val entityVel = entity.velocity
            var entityPos = entity.pos.subtract(prevPos)
            entityPos = rotate(entityPos, startOrientation, endOrientation)
            entityPos = entityPos.add(newPos)
            var worldBlock = world.getBlockState(
                BlockPos(
                    entityPos.x.toInt(),
                    (entityPos.y + entity.standingEyeHeight).toInt(),
                    entityPos.z.toInt()
                )
            )
            if (worldBlock.isSolid) {
                worldBlock = world.getBlockState(
                    BlockPos(
                        newPos.x.toInt(),
                        (newPos.y + entity.standingEyeHeight + 0.5).toInt(),
                        newPos.z.toInt()
                    )
                )
                entityPos = if (worldBlock.isSolid) {
                    entity.pos
                } else {
                    newPos.add(0.0, 1.0, 0.0)
                }
            }
            val facing = rotate(entity.yaw, startOrientation, endOrientation)
            entity.moveToWorld(
                DimensionTransition(
                    world,
                    entityPos,
                    entity.velocity,
                    facing, entity.pitch,
                    DimensionTransition.NO_OP
                )
            )

            entity.resetFallDistance()
            entity.velocity = rotate(entityVel, startOrientation, endOrientation)
            entity.velocityModified = true
        }
    }

    fun rotate(input: Vec3d, startDirection: JigsawOrientation, endDirection: JigsawOrientation): Vec3d {
        val yaw =
            if (startDirection.facing.axis == Direction.Axis.Y) {
                (startDirection.rotation.horizontal - endDirection.facing.horizontal) * -rotate90
            } else if (endDirection.facing.axis == Direction.Axis.Y) {
                (startDirection.facing.horizontal - endDirection.rotation.horizontal) * -rotate90
            } else {
                (startDirection.facing.horizontal - endDirection.facing.horizontal) * -rotate90
            }
//        val yaw = (
//                (if (startDirection.facing.axis == Direction.Axis.Y) startDirection.rotation.vector.y
//                else startDirection.facing.horizontal) +
//                        (if (endDirection.facing.axis == Direction.Axis.Y) endDirection.rotation.vector.y
//                        else endDirection.facing.horizontal)) * rotate90
//
        val x = input.x * cos(yaw) - input.z * sin(yaw)
        val z = input.x * sin(yaw) + input.z * cos(yaw)

        return Vec3d(x, input.y, z)
    }

    fun rotate(
        yaw: Float,
        startDirection: JigsawOrientation,
        endDirection: JigsawOrientation
    ): Float {
        val yaw2 =
            if (startDirection.facing.axis == Direction.Axis.Y) {
                (startDirection.rotation.horizontal - endDirection.facing.horizontal) * -90
            } else if (endDirection.facing.axis == Direction.Axis.Y) {
                (startDirection.facing.horizontal - endDirection.rotation.horizontal) * -90
            } else {
                (startDirection.facing.horizontal - endDirection.facing.horizontal) * -90
            }
        return yaw + yaw2
    }

    override fun rotate(state: BlockState, rotation: BlockRotation): BlockState {
        return state.with(ORIENTATION, rotation.directionTransformation.mapJigsawOrientation(state.get(ORIENTATION)))
    }

    override fun mirror(state: BlockState, mirror: BlockMirror): BlockState {
        return state.with(ORIENTATION, mirror.directionTransformation.mapJigsawOrientation(state.get(ORIENTATION)))
    }

    override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) {
        super.appendProperties(builder)
        builder.add(ORIENTATION, PHASE)
    }

    companion object {
        val CODEC: MapCodec<ShiftBlock> = createCodec(::ShiftBlock)
        val ORIENTATION: EnumProperty<JigsawOrientation> = Properties.ORIENTATION
        val PHASE: EnumProperty<GodhomeBronzePhase> = DuskProperties.GODHOME_BRONZE_PHASE

        val travelDistance = 40
        val range = 15.0
        val rangeBox = Box(
            -range, -range, -range,
            range, range, range
        )

        fun godhomeStrongParticles(world: World, pos: BlockPos, repeat: Int = 5) {
            val rand = world.random
            repeat(repeat) {
                val randInRadius = MathHelper.sqrt(rand.nextFloat()) * range
                val particlePos = Vec3d(
                    rand.nextDouble() - rand.nextDouble(),
                    rand.nextDouble() - rand.nextDouble(),
                    rand.nextDouble() - rand.nextDouble()
                ).normalize().multiply(randInRadius).add(pos.ofCenter())
                val particleVel = Vec3d(
                    0.0,
                    world.random.nextDouble() * 0.5 + 0.01,
                    0.0
                )
                if (world.isClient) {
                    world.addParticle(
                        GodhomeParticleEffect(0xFFF1CC),
                        true,
                        particlePos,
                        particleVel
                    )
                } else {
                    (world as ServerWorld).spawnParticles(
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