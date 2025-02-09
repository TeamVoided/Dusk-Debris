package org.teamvoided.dusk_debris.data.worldgen

import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings
import org.teamvoided.dusk_debris.DuskDebris.id

object DuskNoiseSettings {
    val NETHER = create("nether")

    fun create(id: String): RegistryKey<ChunkGeneratorSettings> =
        RegistryKey.of(RegistryKeys.CHUNK_GENERATOR_SETTINGS, id(id))

}
