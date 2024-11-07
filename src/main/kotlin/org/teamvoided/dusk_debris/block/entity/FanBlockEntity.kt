package org.teamvoided.dusk_debris.block.entity

import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.entity.Entity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.state.property.Properties
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Box
import net.minecraft.util.math.Direction
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World
import org.teamvoided.dusk_debris.block.FanBlock
import org.teamvoided.dusk_debris.data.tags.DuskEntityTypeTags
import org.teamvoided.dusk_debris.init.DuskBlockEntities
import org.teamvoided.dusk_debris.particle.WindParticleEffect
import kotlin.math.max
import kotlin.math.min

class FanBlockEntity(pos: BlockPos, val state: BlockState) :
    BlockEntity(DuskBlockEntities.FAN_BLOCK, pos, state) {
    var windLength: Int = 0

    override fun onSyncedBlockEvent(type: Int, data: Int): Boolean {
        when (type) {
            0 -> {
                windLength = data
                return true
            }
        }
        return super.onSyncedBlockEvent(type, data)
    }

    fun strength(): Int {
        return (this.state.block as FanBlock).strength
    }

    fun power(): Double {
        return strength() * 0.33333 + 10
    }

    companion object {
        fun tick(world: World, pos: BlockPos, state: BlockState, blockEntity: FanBlockEntity) {
            if (world.isClient) {
                clientTick(world, pos, state, blockEntity)
            } else {
                serverTick(world, pos, state, blockEntity)
            }
            if ((pos.asLong() + world.time) % (-blockEntity.strength() + 90) == 0L) {
                world.playSound(
                    null as PlayerEntity?,
                    pos,
                    SoundEvents.ENTITY_BREEZE_WHIRL,
                    SoundCategory.BLOCKS,
                    (-blockEntity.strength() + 90) / 30f,
                    (blockEntity.strength() - 8) / 7f
                )
            }
        }

        fun clientTick(world: World, pos: BlockPos, state: BlockState, blockEntity: FanBlockEntity) {
            val rand = world.random
            val windStrength = blockEntity.strength()
            val windLength = blockEntity.windLength.toDouble()
            if (rand.nextDouble() < (windLength + windStrength) / 30) {
                val limitor = rand.nextDouble()
                val maxAge = (((20 - windStrength) * windLength / 4) * limitor).toInt()
                if (maxAge > 0) {
                    val facing = state.get(Properties.FACING)
                    val particlePos = pos.offset(facing)
                    world.addParticle(
                        WindParticleEffect(
                            blockEntity.windLength * limitor,
                            facing.id,
                            maxAge
                        ),
                        true,
                        particlePos.x + rand.nextDouble(),
                        particlePos.y + rand.nextDouble(),
                        particlePos.z + rand.nextDouble(),
                        0.0,
                        0.0,
                        0.0
                    )
                }
            }
        }

        fun serverTick(world: World, pos: BlockPos, state: BlockState, blockEntity: FanBlockEntity) {
            val facing = state.get(Properties.FACING)
            if (world.time % 20L == 0L) {
                var empty = true
                for (it in 0 until blockEntity.power().toInt()) {
                    val posCheck = pos.offset(facing, it + 1)
                    val worldBlock = world.getBlockState(pos.offset(facing, it + 1))
                    if (
                        !worldBlock.materialReplaceable() &&
                        (worldBlock.isSideSolidFullSquare(world, posCheck, facing) ||
                                worldBlock.isSideSolidFullSquare(world, posCheck, facing.opposite))
                    ) {
                        world.addSyncedBlockEvent(pos, state.block, 0, it)
                        empty = false
                        break
                    }
                }
                if (empty) world.addSyncedBlockEvent(pos, state.block, 0, blockEntity.power().toInt())
            }

            val entitiesInRange =
                world.getOtherEntities(null, getBox(facing, blockEntity).offset(pos.ofCenter()))
                { obj: Entity ->
                    !obj.type.isIn(DuskEntityTypeTags.FANS_DONT_AFFECT)
                }
            if (entitiesInRange.isNotEmpty()) {
//                val facing2 = facing.vector
//                val scale = blockEntity.windPower() / 100
//                val velocity = Vec3d(facing2.x * scale, facing2.y * scale, facing2.z * scale)
                entitiesInRange.forEach {
                    it.resetFallDistance()
                    addEntitySpeed(it, facing, blockEntity)
                    it.velocityModified = true
                }
            }
        }

        fun addEntitySpeed(entity: Entity, direction: Direction, blockEntity: FanBlockEntity) {
            val power: Double = blockEntity.strength().toDouble()
            var velocity: Vec3d = entity.velocity
            velocity = when (direction) {
                Direction.UP -> Vec3d(velocity.x, theee(velocity.y, power), velocity.z)
                Direction.DOWN -> Vec3d(velocity.x, theee(velocity.y, -power), velocity.z)
                Direction.SOUTH -> Vec3d(velocity.x, velocity.y, theee(velocity.z, power))
                Direction.NORTH -> Vec3d(velocity.x, velocity.y, theee(velocity.z, -power))
                Direction.EAST -> Vec3d(theee(velocity.x, power), velocity.y, velocity.z)
                Direction.WEST -> Vec3d(theee(velocity.x, -power), velocity.y, velocity.z)
            }

            entity.velocity = velocity
        }

        fun theee(velocity: Double, power: Double): Double {
            val upperBound = power * 0.05
            val newVelocity = velocity + power * 0.05
            return if (power < 0)
                max(upperBound, newVelocity)
            else
                min(upperBound, newVelocity)
        }

//        fun exceedsMaxVelocity(entity: Entity, direction: Direction, blockEntity: FanBlockEntity): Boolean {
//            val maxSpeed = blockEntity.windPower() / 20
//            return when (direction) {
//                Direction.UP -> entity.velocity.y < maxSpeed
//                Direction.DOWN -> entity.velocity.y > -maxSpeed
//                Direction.SOUTH -> entity.velocity.z < maxSpeed
//                Direction.NORTH -> entity.velocity.z > -maxSpeed
//                Direction.EAST -> entity.velocity.x < maxSpeed
//                Direction.WEST -> entity.velocity.x > -maxSpeed
//            }
//        }

        fun getBox(direction: Direction, blockEntity: FanBlockEntity): Box {
            val horizRange = 0.5
            val vertRange = blockEntity.windLength.toDouble()
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
    }
}