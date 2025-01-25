package org.teamvoided.dusk_debris.data.tags

import net.minecraft.entity.damage.DamageType
import net.minecraft.fluid.Fluid
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.tag.TagKey
import org.teamvoided.dusk_debris.DuskDebris

object DuskFluidTags {

    val ACID = create("acid")

    val CORAL_HYDRATOR = create("coral_hydrator")
    val FARMLAND_HYDRATOR = create("farmland_hydrator")
    val SPONGE_ABSORBABLE = create("sponge_absorbable")
    val WATER_BUBBLE_PARTICLE_PERSISTS = create("water_bubble_particle_persists")
    //the drip particle uses #minecraft:water internally to determine its color?
    val ENABLES_WATER_FOG = create("enables_water_fog")
    val ENTITY_WATER_MOVEMENT = create("entity_water_movement")
    val ENTITY_WATER_PATHFIND = create("entity_water_pathfind")
    val BOATS_FLOAT_ON = create("boats_float_on")
    val SOLIDIFIES_CONCRETE = create("solidifies_concrete")
    val ITEMS_AND_EXPERIENCE_ORBS_FLOAT_IN = create("items_and_experience_orbs_float_in")
    //val AQUATIC_ENTITY_CHECKERS = create("aquatic_entity_checkers") guardians use this for pathfinding, movement, and spawning, havent checked squids and turtles
    val FISHING_BOBBER_BOBS = create("fishing_bobber_bobs")
    val GLASS_BOTTLES_FILL_WATER = create("glass_bottles_fill_water")
    val ULTRAWARM_RESTRICTION = create("ultrawarm_restriction")


    val BREAKS_CACTUS = create("breaks_cactus")
    //Used to enable the lava texture on a fluid. ???
    val SMOKE_INSTEAD_OF_RAINSPLASH = create("smoke_instead_of_rainsplash")
    val ENABLES_LAVA_FOG = create("enables_lava_fog")
    val ITEMS_AND_EXPERIENCE_ORBS_BURN_IN = create("items_and_experience_orbs_burn_in")
    val BUCKET_USES_LAVA_SOUND = create("bucket_uses_lava_sound")
    val ENABLES_LAVA_PATHFIND = create("enables_lava_pathfind")
    val FORMS_COBBLESTONE_STONE_OBSIDIAN = create("forms_cobblestone_stone_obsidian")
    val STRIDER_PATHFIND = create("strider_pathfind")
    val STRIDER_DISMOUNTS_RIDER_IN = create("strider_dismounts_rider_in")

    fun create(id: String): TagKey<Fluid> = TagKey.of(RegistryKeys.FLUID, DuskDebris.id(id))
    fun create(modId: String, path: String): TagKey<Fluid> = TagKey.of(
        RegistryKeys.FLUID,
        DuskDebris.id(modId, path)
    )

}