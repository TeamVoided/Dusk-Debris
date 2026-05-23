package org.teamvoided.dusk_debris.data.gen.providers.models

import net.minecraft.data.models.BlockModelGenerators
import org.teamvoided.dusk_debris.init.DuskBlocks
import org.teamvoided.dusk_debris.util.model_helper.registerActive

object StoneModelProvider {
    fun BlockModelGenerators.generateStoneModels() {
        this.mythrock()
        this.stoneChests()
    }

    private fun BlockModelGenerators.mythrock() {
        this.createTrivialCube(DuskBlocks.MYTHROCK)
        this.registerActive(DuskBlocks.MYTHROCK_ARTERY)
        this.registerActive(DuskBlocks.MYTHROCK_HEART)

    }

    private fun BlockModelGenerators.stoneChests() {
        //this.stoneChest(DuskBlocks.STONE_CHEST)
    }
}