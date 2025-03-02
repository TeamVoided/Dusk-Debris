package org.teamvoided.dusk_debris.data.gen.providers.models

import net.minecraft.block.Blocks
import net.minecraft.data.client.model.BlockStateModelGenerator
import org.teamvoided.dusk_debris.util.model_helper.addAxis
import org.teamvoided.dusk_debris.util.model_helper.sculkCatalystRotate
import org.teamvoided.dusk_debris.util.model_helper.sculkSensorRotate
import org.teamvoided.dusk_debris.util.model_helper.sculkShriekerRotate

object MinecraftModelProvider {
    fun BlockStateModelGenerator.generateAlternativeMinecraftModels() {
        this.addAxis(Blocks.MANGROVE_ROOTS)
        this.sculkCatalystRotate(Blocks.SCULK_CATALYST)
        this.sculkShriekerRotate(Blocks.SCULK_SHRIEKER)
        this.sculkSensorRotate(Blocks.SCULK_SENSOR)
    }
}