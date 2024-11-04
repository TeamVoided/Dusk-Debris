package org.teamvoided.dusk_debris.block.entity

import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.entity.Entity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.particle.ParticleTypes
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.state.property.Properties
import net.minecraft.util.math.*
import net.minecraft.world.World
import org.teamvoided.dusk_debris.block.FanBlock
import org.teamvoided.dusk_debris.data.tags.DuskEntityTypeTags
import org.teamvoided.dusk_debris.init.DuskBlockEntities

class FanBlockEntity(pos: BlockPos, val state: BlockState) :
    BlockEntity(DuskBlockEntities.FAN_BLOCK, pos, state) {
    var windLength: Int = getStrength()

    override fun onSyncedBlockEvent(type: Int, data: Int): Boolean {
        when (type) {
            0 -> {
                windLength = data
                return true
            }
        }
        return super.onSyncedBlockEvent(type, data)
    }

    fun getStrength(): Int {
        return (this.state.block as FanBlock).strength
    }

    companion object {
        fun windPower(blockEntity: FanBlockEntity): Double {
            return blockEntity.getStrength() * 0.66667 + 10
        }

        fun clientTick(world: World, pos: BlockPos, state: BlockState, blockEntity: FanBlockEntity) {
            if (world.time % 30L == 0L) {
                world.playSound(
                    null as PlayerEntity?,
                    pos,
                    SoundEvents.ENTITY_BREEZE_CHARGE,
                    SoundCategory.BLOCKS,
                    //1f,
                    //windPower(blockEntity).toFloat() / 15f
                )
            }
            val facing = state.get(Properties.FACING)
            val box = getBox(facing, blockEntity)
            val centerPos = pos.ofCenter()
            val x = world.random.nextDouble() * (box.xLength)
            val y = world.random.nextDouble() * (box.yLength)
            val z = world.random.nextDouble() * (box.zLength)
            val direction = facing.vector
            world.addParticle(
                ParticleTypes.SMALL_GUST,
                true,
                centerPos.x + (box.minX + x),
                centerPos.y + (box.minY + y),
                centerPos.z + (box.minZ + z),
                direction.x / windPower(blockEntity),
                direction.y / windPower(blockEntity),
                direction.z / windPower(blockEntity)
            )
        }

        fun serverTick(world: World, pos: BlockPos, state: BlockState, blockEntity: FanBlockEntity) {
            val facing = state.get(Properties.FACING)
            if (world.time % 60L == 0L) {
                var empty = true
                for (it in 0 until windPower(blockEntity).toInt()) {
                    val worldBlock = world.getBlockState(pos.offset(facing, it + 1))
                    if (worldBlock.isSolid) {
                        world.addSyncedBlockEvent(pos, state.block, 0, it)
                        empty = false
                        break
                    }
                }
                if (empty) world.addSyncedBlockEvent(pos, state.block, 0, windPower(blockEntity).toInt())
            }

            val entitiesInRange =
                world.getOtherEntities(null, getBox(facing, blockEntity).offset(pos.ofCenter()))
                { obj: Entity -> !obj.type.isIn(DuskEntityTypeTags.FANS_DONT_AFFECT) }
            if (entitiesInRange.isNotEmpty()) {
                val facing2 = facing.vector
                val scale = windPower(blockEntity) / 100
                val velocity = Vec3d(facing2.x * scale, facing2.y * scale, facing2.z * scale)
                entitiesInRange.forEach {
                    it.resetFallDistance()
                    it.addVelocity(
                        velocity.x,
                        velocity.y,
                        velocity.z
                    )
                    it.velocityModified = true
                }
            }
        }

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