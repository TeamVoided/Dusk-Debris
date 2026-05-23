package org.teamvoided.dusk_debris.data.tags

import net.minecraft.core.registries.Registries
import net.minecraft.tags.TagKey
import net.minecraft.world.item.enchantment.Enchantment
import org.teamvoided.dusk_debris.DuskDebris

object DuskEnchantmentTags {

    val PARTICLE_EXCLUSIVE_SET = create("exclusive_set/particles")

    val MENDING_EXCLUSIVE_SET = create("exclusive_set/mending")
    val UNBREAKING_EXCLUSIVE_SET = create("exclusive_set/unbreaking")


    fun create(id: String): TagKey<Enchantment> = TagKey.create(Registries.ENCHANTMENT, DuskDebris.id(id))
    fun create(modId: String, path: String): TagKey<Enchantment> = TagKey.create(
        Registries.ENCHANTMENT,
        DuskDebris.id(modId, path)
    )

}