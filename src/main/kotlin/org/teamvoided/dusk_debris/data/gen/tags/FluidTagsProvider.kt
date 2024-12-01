package org.teamvoided.dusk_debris.data.gen.tags

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider
import net.minecraft.fluid.Fluid
import net.minecraft.registry.HolderLookup
import net.minecraft.registry.RegistryKeys
import org.teamvoided.dusk_debris.data.tags.DuskFluidTags
import org.teamvoided.dusk_debris.init.DuskFluids
import java.util.concurrent.CompletableFuture

class FluidTagsProvider(o: FabricDataOutput, r: CompletableFuture<HolderLookup.Provider>) :
    FabricTagProvider<Fluid>(o, RegistryKeys.FLUID, r) {
    override fun configure(wrapperLookup: HolderLookup.Provider) {
        duskTags()
        vanillaTags()
        conventionTags()
    }

    fun duskTags() {
        getOrCreateTagBuilder(DuskFluidTags.ACID)
            .add(DuskFluids.ACID)
            .add(DuskFluids.FLOWING_ACID)
    }

    fun vanillaTags() {
    }

    fun conventionTags() {
    }
}