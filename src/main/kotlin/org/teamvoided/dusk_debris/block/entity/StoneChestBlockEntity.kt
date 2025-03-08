package org.teamvoided.dusk_debris.block.entity

import net.minecraft.block.BlockState
import net.minecraft.block.entity.ChestBlockEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.registry.Registries
import net.minecraft.state.property.Properties
import net.minecraft.text.Text
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import org.teamvoided.dusk_debris.block.not_blocks.ChestPhase
import org.teamvoided.dusk_debris.block.not_blocks.DuskProperties
import org.teamvoided.dusk_debris.init.DuskBlockEntities

class StoneChestBlockEntity(pos: BlockPos, state: BlockState) :
    ChestBlockEntity(DuskBlockEntities.STONE_CHEST, pos, state) {
    var lidOpeningTicks = 0
    var renderingDelay = false

    override fun onOpen(player: PlayerEntity) {
        super.onOpen(player)
        setOpen(cachedState, 2)
    }

    override fun onClose(player: PlayerEntity) {
        super.onClose(player)
        setOpen(cachedState, 1)
    }

    private fun setOpen(state: BlockState, open: Int) {
        world!!.setBlockState(this.getPos(), state.with(DuskProperties.CHEST_PHASE, ChestPhase.fromInt(open)), 3)
        if (open == 0) lidOpeningTicks = 0
    }

    override fun getContainerName(): Text {
        val blockName = Registries.BLOCK.getId(this.cachedState.block).path //this.cachedState.block.name
        return Text.translatable("container.$blockName")
    }

    companion object {
        const val MAX_OPENING_TICKS = 20
        fun tick(world: World, pos: BlockPos, state: BlockState, blockEntity: StoneChestBlockEntity) {
            val phase = state.get(DuskProperties.CHEST_PHASE)
            if (phase.ordinal == 2) {
                blockEntity.renderingDelay = true
                if (blockEntity.lidOpeningTicks < MAX_OPENING_TICKS)
                    blockEntity.lidOpeningTicks++
            } else if (blockEntity.lidOpeningTicks > 0) {
                blockEntity.lidOpeningTicks--
            } else if (phase.ordinal != 0) {
                blockEntity.setOpen(state, 0)
            } else if (blockEntity.renderingDelay) {
                blockEntity.renderingDelay = false
            }
        }

        fun StoneChestBlockEntity.shouldRenderLid(): Boolean {
            return this.renderingDelay || this.lidOpeningTicks > 0 || this.cachedState.get(DuskProperties.CHEST_PHASE) != ChestPhase.CLOSED
        }
    }
}