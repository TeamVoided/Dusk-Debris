package org.teamvoided.dusk_debris.data

import net.minecraft.entity.EntityType
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.tag.TagKey
import org.teamvoided.dusk_debris.DuskDebris.id

object DuskEntityTypeTags {
    val CRAB_ATTACKS = create("crab_attacks")

    fun create(id: String): TagKey<EntityType<*>> = TagKey.of(RegistryKeys.ENTITY_TYPE, id(id))
}