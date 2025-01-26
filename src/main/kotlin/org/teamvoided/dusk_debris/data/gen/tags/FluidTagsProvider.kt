package org.teamvoided.dusk_debris.data.gen.tags

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider
import net.minecraft.fluid.Fluid
import net.minecraft.fluid.Fluids
import net.minecraft.registry.HolderLookup
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.tag.FluidTags
import org.teamvoided.dusk_debris.data.tags.DuskFluidTags
import org.teamvoided.dusk_debris.init.DuskFluids
import java.util.concurrent.CompletableFuture

class FluidTagsProvider(o: FabricDataOutput, r: CompletableFuture<HolderLookup.Provider>) :
    FabricTagProvider<Fluid>(o, RegistryKeys.FLUID, r) {
    override fun configure(wrapperLookup: HolderLookup.Provider) {
        duskTags()
        vanillaTags()
        conventionTags()
        waterTags()
        lavaTags()
    }

    private fun duskTags() {
        getOrCreateTagBuilder(DuskFluidTags.ACID)
            .add(DuskFluids.ACID)
            .add(DuskFluids.FLOWING_ACID)
    }

    private fun vanillaTags() {
    }

    private fun conventionTags() {
    }

    private fun acidTags() {
//        getOrCreateTagBuilder(DuskFluidTags.ENABLES_ACID_FOG)
//            .forceAddTag(DuskFluidTags.ACID)
        getOrCreateTagBuilder(DuskFluidTags.ACID_BUBBLE_PARTICLE_PERSISTS)
            .forceAddTag(FluidTags.WATER)
            .forceAddTag(DuskFluidTags.ACID)
        getOrCreateTagBuilder(DuskFluidTags.ACID_DOES_NOT_REPLACE_BELOW)
            .forceAddTag(DuskFluidTags.ACID)
            .add(Fluids.WATER)
    }

    //    https://minecraft.wiki/w/Tag#Fluid_tags
    private fun waterTags() {
        getOrCreateTagBuilder(DuskFluidTags.ENABLES_WATER_FOG)
            .forceAddTag(FluidTags.WATER)
        getOrCreateTagBuilder(DuskFluidTags.CORAL_HYDRATOR)
            .forceAddTag(FluidTags.WATER)
        getOrCreateTagBuilder(DuskFluidTags.FARMLAND_HYDRATOR)
            .forceAddTag(FluidTags.WATER)
//        getOrCreateTagBuilder(DuskFluidTags.SPONGE_ABSORBABLE)
//            .forceAddTag(FluidTags.WATER)
        getOrCreateTagBuilder(DuskFluidTags.WATER_BUBBLE_PARTICLE_PERSISTS)
            .forceAddTag(FluidTags.WATER)
            .forceAddTag(DuskFluidTags.ACID)
//        getOrCreateTagBuilder(DuskFluidTags.RESETS_FALL_DISTANCE)
//            .forceAddTag(FluidTags.WATER)
//            .forceAddTag(DuskFluidTags.ACID)
        getOrCreateTagBuilder(DuskFluidTags.ENTITY_WATER_MOVEMENT)
            .forceAddTag(FluidTags.WATER)
            .forceAddTag(DuskFluidTags.ACID)
//        getOrCreateTagBuilder(DuskFluidTags.ENTITY_WATER_PATHFIND)
//            .forceAddTag(FluidTags.WATER)
        getOrCreateTagBuilder(DuskFluidTags.WATER_COMPONENT_COBBLESTONES)
            .forceAddTag(FluidTags.WATER)
            .forceAddTag(DuskFluidTags.ACID)
        getOrCreateTagBuilder(DuskFluidTags.WATER_COMPONENT_STONE)
            .forceAddTag(FluidTags.WATER)
            .forceAddTag(DuskFluidTags.ACID)
        getOrCreateTagBuilder(DuskFluidTags.REPLACES_LAVA)
            .forceAddTag(FluidTags.WATER)
            .forceAddTag(DuskFluidTags.ACID)
        getOrCreateTagBuilder(DuskFluidTags.WATER_DOES_NOT_REPLACE_BELOW)
            .forceAddTag(FluidTags.WATER)
            .add(DuskFluids.ACID)
//        getOrCreateTagBuilder(DuskFluidTags.BOATS_FLOAT_ON)
//            .forceAddTag(FluidTags.WATER)
//            .forceAddTag(DuskFluidTags.ACID)
        getOrCreateTagBuilder(DuskFluidTags.SOLIDIFIES_CONCRETE)
            .forceAddTag(FluidTags.WATER)
            .forceAddTag(DuskFluidTags.ACID)
        getOrCreateTagBuilder(DuskFluidTags.ITEMS_AND_EXPERIENCE_ORBS_FLOAT_WATER)
            .forceAddTag(FluidTags.WATER)
            .forceAddTag(DuskFluidTags.ACID)
        getOrCreateTagBuilder(DuskFluidTags.FISHING_BOBBER_BOBS)
            .forceAddTag(FluidTags.WATER)
            .forceAddTag(DuskFluidTags.ACID)
        getOrCreateTagBuilder(DuskFluidTags.GLASS_BOTTLES_FILL_WATER)
            .forceAddTag(FluidTags.WATER)
        getOrCreateTagBuilder(DuskFluidTags.ULTRAWARM_BUCKET_RESTRICTION)
            .forceAddTag(FluidTags.WATER)
            .forceAddTag(DuskFluidTags.ACID)
    }

    private fun lavaTags() {
        getOrCreateTagBuilder(DuskFluidTags.BREAKS_CACTUS)
            .forceAddTag(FluidTags.LAVA)
            .forceAddTag(DuskFluidTags.ACID)
//        getOrCreateTagBuilder(DuskFluidTags.SMOKE_INSTEAD_OF_RAINSPLASH)
//            .forceAddTag(FluidTags.LAVA)
        getOrCreateTagBuilder(DuskFluidTags.ENABLES_LAVA_FOG)
            .forceAddTag(FluidTags.LAVA)
        getOrCreateTagBuilder(DuskFluidTags.ENTITIES_DONT_PATHFIND_THROUGH)
            .forceAddTag(FluidTags.LAVA)
            .forceAddTag(DuskFluidTags.ACID)
        getOrCreateTagBuilder(DuskFluidTags.BUCKET_USES_LAVA_SOUND)
            .forceAddTag(FluidTags.LAVA)
//        getOrCreateTagBuilder(DuskFluidTags.ENTITY_LAVA_PATHFIND)
//            .forceAddTag(FluidTags.LAVA)
        getOrCreateTagBuilder(DuskFluidTags.LAVA_COMPONENT_BASALT_AND_COBBLESTONES)
            .forceAddTag(FluidTags.LAVA)
        getOrCreateTagBuilder(DuskFluidTags.ITEMS_AND_EXPERIENCE_ORBS_FLOAT_LAVA)
            .forceAddTag(FluidTags.LAVA)
    }
}