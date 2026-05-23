package org.teamvoided.dusk_debris.data.tags

import net.minecraft.core.registries.Registries
import net.minecraft.tags.TagKey
import net.minecraft.world.entity.EntityType
import org.teamvoided.dusk_debris.DuskDebris.id

object DuskEntityTypeTags {
    val CRAB_ATTACKS = create("crab_attacks")
    val THROWABLE_BOMB = create("throwable_bomb")
    val DUSK_SKELETON_ATTACKS = create("dusk_skeleton_attacks")
    val DUSK_SKELETON_RETREATS = create("dusk_skeleton_retreats")
    val IS_NOT_AFFECTED_BY_NETHERSHROOM = create("does_not_trigger_nethershroom")
    val GEYSERS_DONT_PROPEL = create("geysers_dont_propel")
    val FANS_DONT_AFFECT = create("fans_dont_affect")
    val FIREBOMB_DOES_NOT_DAMAGE = create("firebomb_does_not_damage")
    val BLUNDERBOMB_DOES_NOT_DAMAGE = create("blunderbomb_does_not_damage")
    val GUNPOWDER_BARREL_DOES_NOT_DAMAGE = create("gunpowder_barrel_does_not_damage")

    val JELLYFISH = create("jellyfish")
    val FOG_CANYON_ENTITIES = create("fog_canyon_entities")
    val DONT_POP_FOG_BUBBLES = create("dont_pop_fog_bubbles")
    val NOT_DAMAGED_BY_FOG_EXPLOSIONS = create("not_damaged_by_fog_explosions")

    // DnD
    val CHILL_CHARGE_GOES_THROUGH = create("chill_charge_goes_through")

    fun create(id: String): TagKey<EntityType<*>> = TagKey.create(Registries.ENTITY_TYPE, id(id))
}