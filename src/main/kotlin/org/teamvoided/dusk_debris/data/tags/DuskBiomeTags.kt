package org.teamvoided.dusk_debris.data.tags

import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.tag.TagKey
import net.minecraft.world.biome.Biome
import org.teamvoided.dusk_debris.DuskDebris.id

object DuskBiomeTags {
    val TEST = create("test")
    val CRIMSON = create("crimson")
    val WARPED = create("warped")

    val FOG_0_100 = create("fog/0_100")
    val FOG_0_50 = create("fog/0_50")
    val FOG_0_10 = create("fog/0_10")
    val FOG_20_100 = create("fog/20_100")

    val FOG_FREEZING_FOREST = create("fog/special/freezing_forest")

    fun create(id: String): TagKey<Biome> = TagKey.of(RegistryKeys.BIOME, id(id))
}