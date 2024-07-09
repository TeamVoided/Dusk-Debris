package org.teamvoided.dusk_debris.data.tags

import net.minecraft.entity.EntityType
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.tag.TagKey
import org.teamvoided.dusk_debris.DuskDebris.id

object DuskEntityTypeTags {
    val CRAB_ATTACKS = create("crab_attacks")
    val THROWABLE_BOMB = create("throwable_bomb")
    val DUSK_SKELETON_ATTACKS = create("dusk_skeleton_attacks")
    val DUSK_SKELETON_RETREATS = create("dusk_skeleton_retreats")
    val IS_NOT_AFFECTED_BY_NETHERSHROOM = create("does_not_trigger_nethershroom")
    val FIREBOMB_DOES_NOT_DAMAGE = create("firebomb_does_not_damage")
    val BLUNDERBOMB_DOES_NOT_DAMAGE = create("blunderbomb_does_not_damage")
    val GUNPOWDER_BARREL_DOES_NOT_DAMAGE = create("gunpowder_barrel_does_not_damage")

    fun create(id: String): TagKey<EntityType<*>> = TagKey.of(RegistryKeys.ENTITY_TYPE, id(id))
}