package org.teamvoided.dusk_debris.data.tags

import net.minecraft.core.registries.Registries
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import org.teamvoided.dusk_debris.DuskDebris.id

object DuskItemTags {
    val ITEM_TAGS = mutableSetOf<TagKey<Item>>()

    val TEST = create("test")

    val TUFF_GOLEM_CLOAK = create("tuff_golem/cloak")
    val TUFF_GOLEM_EYES = create("tuff_golem/eyes")

    val THROWABLE_BOMB_ITEM = create("throwable_bomb_item")
    val IGNITES_GUNPOWDER = create("ignites_gunpowder")

    val HARVESTER_SCYTHE_AMMO = create("harvester_scythe_ammo")
    val REPAIR_HARVESTER_SCYTHE = create("repair_harvester_scythe")

    val LEAVES_DONT_POISON = create("leaves_dont_poison")

    fun create(id: String): TagKey<Item> {
        val regTag = TagKey.create(Registries.ITEM, id(id))
        ITEM_TAGS.add(regTag)
        return regTag
    }
}