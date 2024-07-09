package org.teamvoided.dusk_debris.data.gen.tags

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider
import net.minecraft.item.Items
import net.minecraft.registry.HolderLookup
import org.teamvoided.dusk_debris.data.tags.DuskItemTags
import org.teamvoided.dusk_debris.item.DuskItemLists.THROWABLE_BOMB_ITEM_LIST
import java.util.concurrent.CompletableFuture

class ItemTagsProvider(output: FabricDataOutput, registriesFuture: CompletableFuture<HolderLookup.Provider>) :
    FabricTagProvider.ItemTagProvider(output, registriesFuture) {
    override fun configure(arg: HolderLookup.Provider) {
        duskTags()
        vanillaTags()
        conventionTags()
    }

    fun duskTags() {
        getOrCreateTagBuilder(DuskItemTags.THROWABLE_BOMB_ITEM)
            .add(*THROWABLE_BOMB_ITEM_LIST.toTypedArray())
        getOrCreateTagBuilder(DuskItemTags.IGNITES_GUNPOWDER)
            .add(Items.FLINT_AND_STEEL)
            .add(Items.FIRE_CHARGE)
//        getOrCreateTagBuilder(DuskItemTags.CRAB_FOOD)
//            .addOptionalTag(ConventionalItemTags.RAW_FISHES_FOODS)
//            .add(Items.ROTTEN_FLESH)
    }

    fun vanillaTags() {}

    fun conventionTags() {}
}