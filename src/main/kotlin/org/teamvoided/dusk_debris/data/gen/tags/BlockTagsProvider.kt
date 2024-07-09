package org.teamvoided.dusk_debris.data.gen.tags

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider
import net.minecraft.block.Blocks
import net.minecraft.registry.HolderLookup
import net.minecraft.registry.tag.BlockTags
import org.teamvoided.dusk_debris.block.DuskBlockLists.GUNPOWDER_BARREL_BLOCK_LIST
import org.teamvoided.dusk_debris.block.DuskBlockLists.RIBBON_BLOCKS_LIST
import org.teamvoided.dusk_debris.block.DuskBlockLists.THROWABLE_BOMB_BLOCK_LIST
import org.teamvoided.dusk_debris.data.tags.DuskBlockTags
import org.teamvoided.dusk_debris.init.DuskBlocks
import java.util.concurrent.CompletableFuture

class BlockTagsProvider(output: FabricDataOutput, registriesFuture: CompletableFuture<HolderLookup.Provider>) :
    FabricTagProvider.BlockTagProvider(output, registriesFuture) {
    override fun configure(arg: HolderLookup.Provider) {
        duskTags()
        vanillaTags()
        conventionTags()
    }

    private fun duskTags() {
        getOrCreateTagBuilder(DuskBlockTags.RIBBON_BLOCK)
            .add(*RIBBON_BLOCKS_LIST.toTypedArray())
        getOrCreateTagBuilder(DuskBlockTags.THROWABLE_BOMB_BLOCK)
            .add(*THROWABLE_BOMB_BLOCK_LIST.toTypedArray())
        getOrCreateTagBuilder(DuskBlockTags.GUNPOWDER_BARRELS)
            .add(*GUNPOWDER_BARREL_BLOCK_LIST.toTypedArray())
        getOrCreateTagBuilder(DuskBlockTags.GUNPOWDER_CONNECTS_TO)
            .forceAddTag(DuskBlockTags.GUNPOWDER_BARRELS)
            .forceAddTag(BlockTags.FIRE)
            .add(DuskBlocks.GUNPOWDER)
            .add(Blocks.TNT)
        getOrCreateTagBuilder(DuskBlockTags.FIREBOMB_DESTROYS)
            .forceAddTag(DuskBlockTags.GUNPOWDER_BARRELS)
            .forceAddTag(BlockTags.CAMPFIRES)
            .forceAddTag(BlockTags.CANDLES)
            .add(DuskBlocks.GUNPOWDER)
            .add(Blocks.TNT)
        getOrCreateTagBuilder(DuskBlockTags.BLUNDERBOMB_DESTROYS)
            .forceAddTag(DuskBlockTags.FIREBOMB_DESTROYS)
            .forceAddTag(DuskBlockTags.THROWABLE_BOMB_BLOCK)
        getOrCreateTagBuilder(DuskBlockTags.GUNPOWDER_BARREL_DESTROYS)
            .forceAddTag(DuskBlockTags.BLUNDERBOMB_DESTROYS)
            .add(Blocks.NETHER_PORTAL)
        getOrCreateTagBuilder(DuskBlockTags.NETHERSHROOM_GROWABLE_ON)
            .forceAddTag(BlockTags.MUSHROOM_GROW_BLOCK)
            .forceAddTag(BlockTags.NYLIUM)
            .add(Blocks.SOUL_SOIL)
        getOrCreateTagBuilder(DuskBlockTags.NETHERSHROOM_REPLACEABLE)
            .forceAddTag(BlockTags.REPLACEABLE)
            .forceAddTag(BlockTags.REPLACEABLE_BY_TREES)
            .forceAddTag(BlockTags.WART_BLOCKS)
            .add(DuskBlocks.BLUE_NETHERSHROOM_BLOCK)
            .add(DuskBlocks.PURPLE_NETHERSHROOM_BLOCK)
            .add(Blocks.WEEPING_VINES_PLANT)
            .add(Blocks.WEEPING_VINES)
            .add(Blocks.TWISTING_VINES_PLANT)
            .add(Blocks.TWISTING_VINES)
        getOrCreateTagBuilder(DuskBlockTags.NETHERSHROOM_IGNORE)
            .forceAddTag(DuskBlockTags.NETHERSHROOM_GROWABLE_ON)
            .forceAddTag(DuskBlockTags.NETHERSHROOM_REPLACEABLE)
            .add(DuskBlocks.NETHERSHROOM_STEM)
    }

    private fun vanillaTags() {
        getOrCreateTagBuilder(BlockTags.REPLACEABLE_BY_TREES)
            .add(DuskBlocks.BLUE_NETHERSHROOM)
            .add(DuskBlocks.PURPLE_NETHERSHROOM)

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
            .add(DuskBlocks.ANCIENT_BLACK_POWDER_BARREL)
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

        getOrCreateTagBuilder(BlockTags.WOODEN_FENCES)
            .add(DuskBlocks.CYPRESS_FENCE)
            .add(DuskBlocks.CYPRESS_FENCE_GATE)
            .add(DuskBlocks.CHARRED_FENCE)
            .add(DuskBlocks.CHARRED_FENCE_GATE)
    }

    private fun conventionTags() {}
}