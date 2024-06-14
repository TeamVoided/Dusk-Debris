package org.teamvoided.dusk_debris.data.gen.tags

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider
import net.minecraft.registry.HolderLookup
import java.util.concurrent.CompletableFuture

class ItemTagsProvider(output: FabricDataOutput, registriesFuture: CompletableFuture<HolderLookup.Provider>) :
    FabricTagProvider.ItemTagProvider(output, registriesFuture) {
    override fun configure(arg: HolderLookup.Provider) {
        duskTags()
        vanillaTags()
        conventionTags()
    }

    fun duskTags() {
//        getOrCreateTagBuilder(DuskItemTags.CRAB_FOOD)
//            .addOptionalTag(ConventionalItemTags.RAW_FISHES_FOODS)
//            .add(Items.ROTTEN_FLESH)
    }

    fun vanillaTags() {}

    fun conventionTags() {}
}