package org.teamvoided.dusk_debris.data.gen.providers.models

import net.minecraft.data.client.model.BlockStateModelGenerator
import org.teamvoided.dusk_debris.init.DuskBlocks
import org.teamvoided.dusk_debris.util.model_helper.registerActive

object StoneModelProvider {
    fun BlockStateModelGenerator.generateStoneModels() {
        this.mythrock()
        this.stoneChests()
    }

    private fun BlockStateModelGenerator.mythrock() {
        this.registerSimpleCubeAll(DuskBlocks.MYTHROCK)
        this.registerActive(DuskBlocks.MYTHROCK_ARTERY)
        this.registerActive(DuskBlocks.MYTHROCK_HEART)

    }

    private fun BlockStateModelGenerator.stoneChests() {
        //this.stoneChest(DuskBlocks.STONE_CHEST)
    }
}