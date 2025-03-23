package org.teamvoided.dusks_and_dungeons.block.entity

import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.particle.ParticleTypes
import net.minecraft.server.world.ServerWorld
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3d
import net.minecraft.world.EntityDetector
import net.minecraft.world.World
import org.teamvoided.dusks_and_dungeons.block.HauntedGravestoneBlock
import org.teamvoided.dusks_and_dungeons.init.DnDBlockEntities
import kotlin.math.cos
import kotlin.math.sin

class HauntedGravestoneBlockEntity(pos: BlockPos, state: BlockState) :
    BlockEntity(DnDBlockEntities.HAUNTED_GRAVESTONE_BLOCK, pos, state) {
    var cursedPlayer: PlayerEntity? = null
    var cursePos: Vec3d = pos.ofCenter()
    var curseTime = 0
    var curseVelocity: Vec3d = Vec3d(0.0, 0.0, 0.0)

    override fun onSyncedBlockEvent(type: Int, data: Int): Boolean {
        when (type) {
            0 -> {
                cursedPlayer = null
                cursePos = Vec3d.ZERO
                curseTime = 0
                curseVelocity = Vec3d(0.0, 0.0, 0.0)
                return true
            }

            1 -> {
                cursedPlayer =
                    world?.getClosestPlayer(pos.x.toDouble(), pos.y.toDouble(), pos.z.toDouble(), RANGE, false)
                cursePos = pos.ofCenter()
                return true
            }
        }
        return super.onSyncedBlockEvent(type, data)
    }

    companion object {
        var RANGE = 9.0

        fun serverTick(world: World, pos: BlockPos, state: BlockState, blockEntity: HauntedGravestoneBlockEntity) {
            if (world.isClient) {
                if (blockEntity.cursedPlayer == null) {
                    return
                }
                val oldPos: Vec3d = blockEntity.cursePos
                val playerPos: Vec3d = blockEntity.cursedPlayer!!.pos.add(0.0, 1.0, 0.0)
                val distanceVec = Vec3d(
                    playerPos.x - oldPos.x,
                    playerPos.y - oldPos.y,
                    playerPos.z - oldPos.z
                )
                val distance = distanceVec.length()
                blockEntity.curseVelocity = if (distance < 0.5) {
                    blockEntity.curseVelocity.multiply(0.8)
                } else {
                    blockEntity.curseVelocity.add(distanceVec.multiply((0.075 / distance)))
                }
                blockEntity.cursePos = blockEntity.cursePos.add(blockEntity.curseVelocity)
                blockEntity.curseTime++
                val spinner: Double = blockEntity.curseTime / 10.0
                val xSin = sin(spinner)
                val ySin = sin(spinner / 3)
                val zCos = cos(spinner)

                world.addParticle(
                    ParticleTypes.SOUL,
                    blockEntity.cursePos.x + xSin,
                    blockEntity.cursePos.y + ySin,
                    blockEntity.cursePos.z + zCos,
                    xSin / 10,
                    ySin / 10,
                    zCos / 10
                )
                return
            }

            if ((pos.asLong() + world.time) % 20L != 0L) {
                val players = EntityDetector.NON_SPECTATING_PLAYERS.detect(
                    world as ServerWorld?,
                    EntityDetector.EntitySelector.WORLD_ENTITY_SELECTOR,
                    pos,
                    RANGE,
                    true
                )

                val isActive = state.get(HauntedGravestoneBlock.IS_ACTIVE)
                if (isActive && world.random.nextInt(1000) == 0) {
                    world.addSyncedBlockEvent(pos, state.block, 1, 0)
                    world.playSound(
                        pos.x + 0.5,
                        pos.y + 0.5,
                        pos.z + 0.5,
                        SoundEvents.ENTITY_VEX_CHARGE,
                        SoundCategory.BLOCKS,
                        1f,
                        world.random.nextFloat() * 0.3f,
                        false
                    )
                }
                if ((isActive && players.isEmpty()) || (!isActive && players.isNotEmpty())) {
                    world.setBlockState(pos, state.with(HauntedGravestoneBlock.IS_ACTIVE, !isActive))
                    if (isActive)
                        world.addSyncedBlockEvent(pos, state.block, 0, 0)
                }
            }
        }
    }
}