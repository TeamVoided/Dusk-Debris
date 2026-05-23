package org.teamvoided.dusks_and_dungeons.block.entity

import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.trialspawner.PlayerDetector
import net.minecraft.world.level.block.state.BlockState
import org.teamvoided.dusk_debris.block.HauntedGravestoneBlock
import org.teamvoided.dusk_debris.init.DuskBlockEntities

open class HauntedBlockEntity(pos: BlockPos, state: BlockState) :
    BlockEntity(DuskBlockEntities.HAUNTED_BLOCK, pos, state) {

    companion object {
        fun serverTick(world: Level, pos: BlockPos, state: BlockState, blockEntity: HauntedBlockEntity) {
            if ((pos.asLong() + world.gameTime) % 20L != 0L) {
                val players = PlayerDetector.INCLUDING_CREATIVE_PLAYERS.detect(
                    world as ServerLevel?,
                    PlayerDetector.EntitySelector.SELECT_FROM_LEVEL,
                    pos,
                    9.0,
                    true
                )

                val isActive = state.getValue(HauntedGravestoneBlock.IS_ACTIVE)
                if ((isActive && players.isEmpty()) || (!isActive && players.isNotEmpty())) {
                    world.setBlockAndUpdate(pos, state.setValue(HauntedGravestoneBlock.IS_ACTIVE, !isActive))
                }
            }
        }
    }
}