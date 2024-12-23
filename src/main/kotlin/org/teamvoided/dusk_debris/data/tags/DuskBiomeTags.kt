package org.teamvoided.dusk_debris.data.tags

import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.tag.TagKey
import net.minecraft.world.biome.Biome
import org.teamvoided.dusk_debris.DuskDebris.id

object DuskBiomeTags {
    val TEST = create("test")
    val CRIMSON = create("crimson")
    val WARPED = create("warped")

    val FOG_START_0 = create("fog/start/0")
    val FOG_START_20 = create("fog/start/20")
    val FOG_START_50 = create("fog/start/50")
    val FOG_START_80 = create("fog/start/80")

    val FOG_END_0 = create("fog/end/0")
    val FOG_END_20 = create("fog/end/20")
    val FOG_END_50 = create("fog/end/50")
    val FOG_END_80 = create("fog/end/80")

    val FOG_BOREAL_VALLEY = create("fog/special/boreal_valley")

    fun create(id: String): TagKey<Biome> = TagKey.of(RegistryKeys.BIOME, id(id))
}