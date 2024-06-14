package org.teamvoided.dusk_debris.data.gen.tags

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider
import net.minecraft.registry.HolderLookup
import net.minecraft.registry.tag.BlockTags
import org.teamvoided.dusk_debris.init.DuskBlocks
import java.util.concurrent.CompletableFuture

class BlockTagsProvider(output: FabricDataOutput, registriesFuture: CompletableFuture<HolderLookup.Provider>) :
    FabricTagProvider.BlockTagProvider(output, registriesFuture) {
    override fun configure(arg: HolderLookup.Provider) {
        duskTags()
        vanillaTags()
        conventionTags()
    }

    fun duskTags() {
    }

    fun vanillaTags() {
        getOrCreateTagBuilder(BlockTags.SAND)
            .add(DuskBlocks.VOLCANIC_SAND)
            .add(DuskBlocks.SUSPICIOUS_VOLCANIC_SAND)
        getOrCreateTagBuilder(BlockTags.OVERWORLD_CARVER_REPLACEABLES)
            .add(DuskBlocks.VOLCANIC_SANDSTONE)
        getOrCreateTagBuilder(BlockTags.NETHER_CARVER_REPLACEABLES)
            .add(DuskBlocks.VOLCANIC_SAND)
            .add(DuskBlocks.VOLCANIC_SANDSTONE)
        getOrCreateTagBuilder(BlockTags.SCULK_REPLACEABLE)
            .add(DuskBlocks.VOLCANIC_SAND)
            .add(DuskBlocks.VOLCANIC_SANDSTONE)
        getOrCreateTagBuilder(BlockTags.SOUL_FIRE_BASE_BLOCKS)
            .add(DuskBlocks.VOLCANIC_SAND)
            .add(DuskBlocks.SUSPICIOUS_VOLCANIC_SAND)
            .add(DuskBlocks.VOLCANIC_SANDSTONE)
            .add(DuskBlocks.VOLCANIC_SANDSTONE_STAIRS)
            .add(DuskBlocks.VOLCANIC_SANDSTONE_SLAB)
            .add(DuskBlocks.VOLCANIC_SANDSTONE_WALL)
            .add(DuskBlocks.CUT_VOLCANIC_SANDSTONE)
            .add(DuskBlocks.CUT_VOLCANIC_SANDSTONE_SLAB)
            .add(DuskBlocks.CHISELED_VOLCANIC_SANDSTONE)
            .add(DuskBlocks.SMOOTH_VOLCANIC_SANDSTONE)
            .add(DuskBlocks.SMOOTH_VOLCANIC_SANDSTONE_STAIRS)
            .add(DuskBlocks.SMOOTH_VOLCANIC_SANDSTONE_SLAB)
        getOrCreateTagBuilder(BlockTags.WALLS)
            .add(DuskBlocks.VOLCANIC_SANDSTONE_WALL)
    }

    fun conventionTags() {}
}