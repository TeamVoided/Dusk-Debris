package org.teamvoided.dusk_debris.data.tags

import net.minecraft.fluid.Fluid
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.tag.TagKey
import org.teamvoided.dusk_debris.DuskDebris

object DuskFluidTags {

    val ACID = create("acid")

    val WATERLIKE = create("waterlike")

    //ACID TAGS
    val ENABLES_ACID_FOG = create("enables_acid_fog") //probably too hard?
    val ACID_BUBBLE_PARTICLE_PERSISTS = create("acid_bubble_particle_persists")
    val ACID_DOES_NOT_REPLACE_BELOW = create("acid_does_not_replace_below")
    //no, unless??? val GLASS_BOTTLES_FILL_ACID = create("glass_bottles_fill_acid")


    //WATER TAGS
    val CORAL_HYDRATOR = create("coral_hydrator")
    val FARMLAND_HYDRATOR = create("farmland_hydrator")
    //    val SPONGE_ABSORBABLE = create("sponge_absorbable") // mixin cannot find target
    val WATER_BUBBLE_PARTICLE_PERSISTS = create("water_bubble_particle_persists")
    val ENABLES_WATER_FOG = create("enables_water_fog")
    //    val RESETS_FALL_DISTANCE = create("resets_fall_distance") //eunim
    val ENTITY_WATER_MOVEMENT = create("entity_water_movement")
    //    val ENTITY_WATER_PATHFIND = create("entity_water_pathfind")
    val WATER_COMPONENT_COBBLESTONES = create("water_component_stones")
    val WATER_COMPONENT_STONE = create("water_component_stone")
    val REPLACES_LAVA = create("replaces_lava")
    val WATER_DOES_NOT_REPLACE_BELOW = create("water_does_not_replace_below")
    //    val BOATS_FLOAT_ON = create("boats_float_on")
    val SOLIDIFIES_CONCRETE = create("solidifies_concrete")
    val ITEMS_AND_EXPERIENCE_ORBS_FLOAT_WATER = create("items_and_experience_orbs_float_water")
    //val AQUATIC_ENTITY_CHECKERS = create("aquatic_entity_checkers") guardians use this for pathfinding, movement, and spawning, havent checked squids and turtles
    val FISHING_BOBBER_BOBS = create("fishing_bobber_bobs")
    val GLASS_BOTTLES_FILL_WATER = create("glass_bottles_fill_water")
    val ULTRAWARM_BUCKET_RESTRICTION = create("ultrawarm_restriction")


    //LAVA TAGS
    val BREAKS_CACTUS = create("breaks_cactus")
    //    val SMOKE_INSTEAD_OF_RAINSPLASH = create("smoke_instead_of_rainsplash")
    val ENABLES_LAVA_FOG = create("enables_lava_fog")
    val ENTITIES_DONT_PATHFIND_THROUGH = create("entities_dont_pathfind_through")
    val BUCKET_USES_LAVA_SOUND = create("bucket_uses_lava_sound")
    //    val ENTITY_LAVA_PATHFIND = create("entity_lava_pathfind")
    val LAVA_COMPONENT_BASALT_AND_COBBLESTONES = create("lava_component_basalt_and_cobblestones")
    val LAVA_COMPONENT_STONE = create("lava_component_stone")
    val ITEMS_AND_EXPERIENCE_ORBS_FLOAT_LAVA = create("items_and_experience_orbs_float_lava")

    fun create(id: String): TagKey<Fluid> = TagKey.of(RegistryKeys.FLUID, DuskDebris.id(id))
    fun create(modId: String, path: String): TagKey<Fluid> = TagKey.of(
        RegistryKeys.FLUID,
        DuskDebris.id(modId, path)
    )

}