package org.teamvoided.dusk_debris.data.gen.tags

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider
import net.minecraft.item.Items
import net.minecraft.registry.HolderLookup
import net.minecraft.registry.tag.ItemTags
import org.teamvoided.dusk_debris.data.tags.DuskBlockTags
import org.teamvoided.dusk_debris.data.tags.DuskItemTags
import java.util.concurrent.CompletableFuture

class ItemTagsProvider(
    output: FabricDataOutput,
    registriesFuture: CompletableFuture<HolderLookup.Provider>,
    blockTags: BlockTagsProvider
) : FabricTagProvider.ItemTagProvider(output, registriesFuture, blockTags) {
    override fun configure(arg: HolderLookup.Provider) {
        duskTags()
        vanillaTags()
        conventionTags()
    }

    fun duskTags() {
        getOrCreateTagBuilder(DuskItemTags.TUFF_GOLEM_CLOAK)
            .forceAddTag(ItemTags.WOOL_CARPETS)
            .add(Items.MOSS_CARPET)
        getOrCreateTagBuilder(DuskItemTags.TUFF_GOLEM_EYES)
            .add(Items.WATER_BUCKET)
            .add(Items.LANTERN)
            .add(Items.SOUL_LANTERN)
            .add(Items.BLAZE_ROD)
            .add(Items.BREEZE_ROD)

        copy(DuskBlockTags.THROWABLE_BOMB_BLOCK, DuskItemTags.THROWABLE_BOMB_ITEM)
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