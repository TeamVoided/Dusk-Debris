package org.teamvoided.dusk_debris.block

import net.minecraft.block.BellBlock
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.entity.BlockEntityTicker
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.entity.Entity
import net.minecraft.sound.SoundCategory
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.World
import net.minecraft.world.event.GameEvent
import org.teamvoided.dusk_debris.init.DuskSoundEvents
import org.teamvoided.dusks_and_dungeons.block.entity.CelestalBellBlockEntity

class CelestalBellBlock(settings: Settings) : BellBlock(settings) {
    override fun ring(entity: Entity?, world: World, pos: BlockPos, direction: Direction?): Boolean {
        var direction2 = direction
        val blockEntity = world.getBlockEntity(pos)
        if (!world.isClient && blockEntity is CelestalBellBlockEntity) {
            if (direction == null) {
                direction2 = world.getBlockState(pos).get(FACING) as Direction
            }

            blockEntity.activate(direction2)
            world.playSound(
                null,
                pos,
                DuskSoundEvents.BLOCK_CELESTAL_BELL_USE,
                SoundCategory.BLOCKS,
                2.0f,
                1.0f
            )
            world.emitGameEvent(entity, GameEvent.BLOCK_CHANGE, pos)
            return true
        } else {
            return false
        }
    }


    override fun createBlockEntity(pos: BlockPos, state: BlockState): BlockEntity {
        return CelestalBellBlockEntity(pos, state)
    }

    override fun <T : BlockEntity> getTicker(
        world: World,
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
