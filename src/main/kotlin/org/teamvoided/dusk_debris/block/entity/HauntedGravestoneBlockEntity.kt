package org.teamvoided.dusks_and_dungeons.block.entity

import net.minecraft.core.BlockPos
import net.minecraft.core.particles.ParticleTypes
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.trialspawner.PlayerDetector
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.Vec3
import org.teamvoided.dusk_debris.block.HauntedGravestoneBlock
import org.teamvoided.dusk_debris.init.DuskBlockEntities
import kotlin.math.cos
import kotlin.math.sin

class HauntedGravestoneBlockEntity(pos: BlockPos, state: BlockState) :
    BlockEntity(DuskBlockEntities.HAUNTED_GRAVESTONE_BLOCK, pos, state) {
    var cursedPlayer: Player? = null
    var cursePos: Vec3 = pos.center
    var curseTime = 0
    var curseVelocity: Vec3 = Vec3(0.0, 0.0, 0.0)

    override fun triggerEvent(type: Int, data: Int): Boolean {
        when (type) {
            0 -> {
                cursedPlayer = null
                cursePos = Vec3.ZERO
                curseTime = 0
                curseVelocity = Vec3(0.0, 0.0, 0.0)
                return true
            }

            1 -> {
                cursedPlayer =
                    level?.getNearestPlayer(worldPosition.x.toDouble(), worldPosition.y.toDouble(), worldPosition.z.toDouble(), RANGE, false)
                cursePos = worldPosition.center
                return true
            }
        }
        return super.triggerEvent(type, data)
    }

    companion object {
        var RANGE = 9.0

        fun serverTick(world: Level, pos: BlockPos, state: BlockState, blockEntity: HauntedGravestoneBlockEntity) {
            if (world.isClientSide) {
                if (blockEntity.cursedPlayer == null) {
                    return
                }
                val oldPos: Vec3 = blockEntity.cursePos
                val playerPos: Vec3 = blockEntity.cursedPlayer!!.position().add(0.0, 1.0, 0.0)
                val distanceVec = Vec3(
                    playerPos.x - oldPos.x,
                    playerPos.y - oldPos.y,
                    playerPos.z - oldPos.z
                )
                val distance = distanceVec.length()
                blockEntity.curseVelocity = if (distance < 0.5) {
                    blockEntity.curseVelocity.scale(0.8)
                } else {
                    blockEntity.curseVelocity.add(distanceVec.scale((0.075 / distance)))
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

            if ((pos.asLong() + world.gameTime) % 20L != 0L) {
                val players = PlayerDetector.INCLUDING_CREATIVE_PLAYERS.detect(
                    world as ServerLevel?,
                    PlayerDetector.EntitySelector.SELECT_FROM_LEVEL,
                    pos,
                    RANGE,
                    true
                )

                val isActive = state.getValue(HauntedGravestoneBlock.IS_ACTIVE)
                if (isActive && world.random.nextInt(1000) == 0) {
                    world.blockEvent(pos, state.block, 1, 0)
                    world.playLocalSound(
                        pos.x + 0.5,
                        pos.y + 0.5,
                        pos.z + 0.5,
                        SoundEvents.VEX_CHARGE,
                        SoundSource.BLOCKS,
                        1f,
                        world.random.nextFloat() * 0.3f,
                        false
                    )
                }
                if ((isActive && players.isEmpty()) || (!isActive && players.isNotEmpty())) {
                    world.setBlockAndUpdate(pos, state.setValue(HauntedGravestoneBlock.IS_ACTIVE, !isActive))
                    if (isActive)
                        world.blockEvent(pos, state.block, 0, 0)
                }
            }
        }
    }
}