package org.teamvoided.dusk_debris.data.gen.providers.models

import net.minecraft.data.client.model.BlockStateModelGenerator
import org.teamvoided.dusk_debris.init.DuskBlocks
import org.teamvoided.dusk_debris.util.model_helper.stoneChest

object StoneModelProvider {
    fun BlockStateModelGenerator.generateStoneModels() {
        this.stoneChests()
    }

    private fun BlockStateModelGenerator.stoneChests() {
        this.stoneChest(DuskBlocks.STONE_CHEST)
    }
}