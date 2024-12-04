package org.teamvoided.dusk_debris.data.tags

import net.minecraft.enchantment.Enchantment
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.tag.TagKey
import org.teamvoided.dusk_debris.DuskDebris

object DuskEnchantmentTags {

    val PARTICLE_EXCLUSIVE_SET = create("exclusive_set/particles")

    val MENDING_EXCLUSIVE_SET = create("exclusive_set/mending")
    val UNBREAKING_EXCLUSIVE_SET = create("exclusive_set/unbreaking")


    fun create(id: String): TagKey<Enchantment> = TagKey.of(RegistryKeys.ENCHANTMENT, DuskDebris.id(id))
    fun create(modId: String, path: String): TagKey<Enchantment> = TagKey.of(RegistryKeys.ENCHANTMENT,
        DuskDebris.id(modId, path)
    )

}