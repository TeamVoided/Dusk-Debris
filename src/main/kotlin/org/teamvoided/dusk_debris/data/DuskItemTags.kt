package org.teamvoided.dusk_debris.data

import net.minecraft.item.Item
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.tag.TagKey
import org.teamvoided.dusk_debris.DuskDebris.id

object DuskItemTags {
    val TEST = create("test")
    val THROWABLE_BOMB_ITEM = create("throwable_bomb_item")
    val IGNITES_GUNPOWDER = create("ignites_gunpowder")

    fun create(id: String): TagKey<Item> = TagKey.of(RegistryKeys.ITEM, id(id))
}