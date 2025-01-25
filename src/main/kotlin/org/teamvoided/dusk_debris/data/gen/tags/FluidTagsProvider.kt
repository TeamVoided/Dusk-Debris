package org.teamvoided.dusk_debris.data.gen.tags

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider
import net.minecraft.fluid.Fluid
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

    //    https://minecraft.wiki/w/Tag#Fluid_tags
    private fun waterTags() {
        getOrCreateTagBuilder(DuskFluidTags.CORAL_HYDRATOR)
            .forceAddTag(FluidTags.WATER)
            .forceAddTag(DuskFluidTags.ACID)
        getOrCreateTagBuilder(DuskFluidTags.FARMLAND_HYDRATOR)
            .forceAddTag(FluidTags.WATER)
        getOrCreateTagBuilder(DuskFluidTags.SPONGE_ABSORBABLE)
            .forceAddTag(FluidTags.WATER)
        getOrCreateTagBuilder(DuskFluidTags.WATER_BUBBLE_PARTICLE_PERSISTS)
            .forceAddTag(FluidTags.WATER)
            .forceAddTag(DuskFluidTags.ACID)
        getOrCreateTagBuilder(DuskFluidTags.ENABLES_WATER_FOG)
            .forceAddTag(FluidTags.WATER)
        getOrCreateTagBuilder(DuskFluidTags.ENTITY_WATER_MOVEMENT)
            .forceAddTag(FluidTags.WATER)
            .forceAddTag(DuskFluidTags.ACID)
        getOrCreateTagBuilder(DuskFluidTags.ENTITY_WATER_PATHFIND)
            .forceAddTag(FluidTags.WATER)
        getOrCreateTagBuilder(DuskFluidTags.BOATS_FLOAT_ON)
            .forceAddTag(FluidTags.WATER)
            .forceAddTag(DuskFluidTags.ACID)
        getOrCreateTagBuilder(DuskFluidTags.SOLIDIFIES_CONCRETE)
            .forceAddTag(FluidTags.WATER)
            .forceAddTag(DuskFluidTags.ACID)
        getOrCreateTagBuilder(DuskFluidTags.ITEMS_AND_EXPERIENCE_ORBS_FLOAT_IN)
            .forceAddTag(FluidTags.WATER)
            .forceAddTag(DuskFluidTags.ACID)
        getOrCreateTagBuilder(DuskFluidTags.FISHING_BOBBER_BOBS)
            .forceAddTag(FluidTags.WATER)
            .forceAddTag(DuskFluidTags.ACID)
        getOrCreateTagBuilder(DuskFluidTags.GLASS_BOTTLES_FILL_WATER)
            .forceAddTag(FluidTags.WATER)
        getOrCreateTagBuilder(DuskFluidTags.ULTRAWARM_RESTRICTION)
            .forceAddTag(FluidTags.WATER)
            .forceAddTag(DuskFluidTags.ACID)
    }

    private fun lavaTags() {
        getOrCreateTagBuilder(DuskFluidTags.BREAKS_CACTUS)
            .forceAddTag(FluidTags.LAVA)
            .forceAddTag(DuskFluidTags.ACID)
        getOrCreateTagBuilder(DuskFluidTags.SMOKE_INSTEAD_OF_RAINSPLASH)
            .forceAddTag(FluidTags.LAVA)
        getOrCreateTagBuilder(DuskFluidTags.ENABLES_LAVA_FOG)
            .forceAddTag(FluidTags.LAVA)
        getOrCreateTagBuilder(DuskFluidTags.ITEMS_AND_EXPERIENCE_ORBS_BURN_IN)
            .forceAddTag(FluidTags.LAVA)
        getOrCreateTagBuilder(DuskFluidTags.BUCKET_USES_LAVA_SOUND)
            .forceAddTag(FluidTags.LAVA)
        getOrCreateTagBuilder(DuskFluidTags.ENABLES_LAVA_PATHFIND)
            .forceAddTag(FluidTags.LAVA)
        getOrCreateTagBuilder(DuskFluidTags.FORMS_COBBLESTONE_STONE_OBSIDIAN)
            .forceAddTag(FluidTags.LAVA)
        getOrCreateTagBuilder(DuskFluidTags.STRIDER_PATHFIND)
            .forceAddTag(FluidTags.LAVA)
        getOrCreateTagBuilder(DuskFluidTags.STRIDER_DISMOUNTS_RIDER_IN)
            .forceAddTag(FluidTags.LAVA)

    }
}