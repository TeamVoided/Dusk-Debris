package org.teamvoided.dusk_debris.data.gen.providers.models

import net.minecraft.block.Blocks
import net.minecraft.data.client.model.BlockStateModelGenerator
import org.teamvoided.dusk_debris.data.gen.providers.models.WoodModelProvider.generateWoodModels
import org.teamvoided.dusk_debris.init.DuskBlocks
import org.teamvoided.dusk_debris.util.model_helper.*

object WoodModelProvider {
    fun BlockStateModelGenerator.generateWoodModels() {
        this.strongScaffolding()
    }

    private fun BlockStateModelGenerator.strongScaffolding() {
        this.strongScaffolding(DuskBlocks.STRONG_SCAFFOLDING)
    }
}