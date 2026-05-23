package org.teamvoided.dusk_debris.block

import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.sounds.SoundSource
import net.minecraft.world.entity.Entity
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.BellBlock
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityTicker
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.gameevent.GameEvent
import org.teamvoided.dusk_debris.init.DuskSoundEvents
import org.teamvoided.dusks_and_dungeons.block.entity.CelestalBellBlockEntity

class CelestalBellBlock(settings: Properties) : BellBlock(settings) {
    override fun attemptToRing(entity: Entity?, world: Level, pos: BlockPos, direction: Direction?): Boolean {
        var direction2 = direction
        val blockEntity = world.getBlockEntity(pos)
        if (!world.isClientSide && blockEntity is CelestalBellBlockEntity) {
            if (direction == null) {
                direction2 = world.getBlockState(pos).getValue(FACING) as Direction
            }

            blockEntity.onHit(direction2)
            world.playSound(
                null,
                pos,
                DuskSoundEvents.BLOCK_CELESTAL_BELL_USE,
                SoundSource.BLOCKS,
                2.0f,
                1.0f
            )
            world.gameEvent(entity, GameEvent.BLOCK_CHANGE, pos)
            return true
        } else {
            return false
        }
    }


    override fun newBlockEntity(pos: BlockPos, state: BlockState): BlockEntity {
        return CelestalBellBlockEntity(pos, state)
    }

    override fun <T : BlockEntity> getTicker(
        world: Level,
        state: BlockState,
        type: BlockEntityType<T>
    ): BlockEntityTicker<T>? {
        return null /*checkType(
            type,
            DuskBlockEntities.CELESTAL_BELL,
            if (world.isClient) BlockEntityTicker { world: World?, pos: BlockPos?, state: BlockState?, blockEntity: BellBlockEntity? ->
                CelestalBellBlockEntity.clientTick(
                    world,
                    pos,
                    state,
                    blockEntity
                )
            } else BlockEntityTicker { world: World?, pos: BlockPos?, state: BlockState?, blockEntity: BellBlockEntity? ->
                BellBlockEntity.serverTick(
                    world,
                    pos,
                    state,
                    blockEntity
                )
            })*/
    }
}
