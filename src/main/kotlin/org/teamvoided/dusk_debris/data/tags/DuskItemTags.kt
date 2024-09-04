package org.teamvoided.dusk_debris.data.tags

import net.minecraft.item.Item
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.tag.TagKey
import org.teamvoided.dusk_debris.DuskDebris.id

object DuskItemTags {
    val TEST = create("test")

    val TUFF_GOLEM_CLOAK = create("tuff_golem/cloak")
    val TUFF_GOLEM_EYES = create("tuff_golem/eyes")

    val THROWABLE_BOMB_ITEM = create("throwable_bomb_item")
    val IGNITES_GUNPOWDER = create("ignites_gunpowder")

    fun create(id: String): TagKey<Item> = TagKey.of(RegistryKeys.ITEM, id(id))
}