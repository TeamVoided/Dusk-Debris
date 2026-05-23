package org.teamvoided.dusk_debris.data.worldgen

import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings
import org.teamvoided.dusk_debris.DuskDebris.id

object DuskNoiseSettings {
    val NETHER = create("nether")
    val OVERWORLD = create("overworld")

    private   fun create(id: String): ResourceKey<NoiseGeneratorSettings> =
        ResourceKey.create(Registries.NOISE_SETTINGS, id(id))
}