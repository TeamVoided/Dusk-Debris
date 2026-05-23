package org.teamvoided.dusk_debris.data.gen.providers.models

import net.minecraft.data.models.BlockModelGenerators
import net.minecraft.data.models.model.TexturedModel
import net.minecraft.world.level.block.Block
import org.teamvoided.dusk_debris.init.DuskBlocks
import org.teamvoided.dusk_debris.util.model_helper.registerOvergrowthBush
import org.teamvoided.dusk_debris.util.model_helper.strongScaffolding

object WoodModelProvider {
    fun BlockModelGenerators.generateWoodModels() {
        this.woods()
        this.strongScaffolding(DuskBlocks.STRONG_SCAFFOLDING)
        this.registerOvergrowthBush(DuskBlocks.OVERGROWTH_BUSH)
        this.createTrivialBlock(DuskBlocks.OVERGROWTH_BLOCK, TexturedModel.LEAVES)
        this.createTrivialBlock(DuskBlocks.OVERGROWTH_LEAVES, TexturedModel.LEAVES)
    }

    private fun BlockModelGenerators.woods() {
        this.logWoodStrip(
            DuskBlocks.CYPRESS_LEAVES,
            DuskBlocks.CYPRESS_LOG,
            DuskBlocks.CYPRESS_WOOD,
            DuskBlocks.STRIPPED_CYPRESS_LOG,
            DuskBlocks.STRIPPED_CYPRESS_WOOD,
            DuskBlocks.CYPRESS_HANGING_SIGN,
            DuskBlocks.CYPRESS_WALL_HANGING_SIGN
        )
        this.logWoodStrip(
            DuskBlocks.CHARRED_LOG,
            DuskBlocks.CHARRED_WOOD,
            DuskBlocks.STRIPPED_CHARRED_LOG,
            DuskBlocks.STRIPPED_CHARRED_WOOD,
            DuskBlocks.CHARRED_HANGING_SIGN,
            DuskBlocks.CHARRED_WALL_HANGING_SIGN
        )
        this.logWoodStrip(
            DuskBlocks.SEQUOIA_LEAVES,
            DuskBlocks.SEQUOIA_LOG,
            DuskBlocks.SEQUOIA_WOOD,
            DuskBlocks.STRIPPED_SEQUOIA_LOG,
            DuskBlocks.STRIPPED_SEQUOIA_WOOD,
            DuskBlocks.SEQUOIA_HANGING_SIGN,
            DuskBlocks.SEQUOIA_WALL_HANGING_SIGN
        )
        this.createTrivialBlock(DuskBlocks.POISON_BIRCH_LEAVES, TexturedModel.LEAVES)
        this.logWoodStrip(
            DuskBlocks.GALLERY_MAPLE_SAPLING,
            DuskBlocks.POTTED_GALLERY_MAPLE_SAPLING,
            DuskBlocks.GALLERY_MAPLE_LEAVES,
            DuskBlocks.GALLERY_MAPLE_LOG,
            DuskBlocks.GALLERY_MAPLE_WOOD,
            DuskBlocks.STRIPPED_GALLERY_MAPLE_LOG,
            DuskBlocks.STRIPPED_GALLERY_MAPLE_WOOD,
            DuskBlocks.GALLERY_MAPLE_HANGING_SIGN,
            DuskBlocks.GALLERY_MAPLE_WALL_HANGING_SIGN
        )
    }

    private fun BlockModelGenerators.logWoodStrip(
        sapling: Block,
        saplingPot: Block,
        leaves: Block,
        log: Block,
        wood: Block,
        strippedLog: Block,
        strippedWood: Block,
        hangingSign: Block,
        wallHangingSign: Block
    ) {
        this.createPlant(sapling, saplingPot, BlockModelGenerators.TintState.NOT_TINTED)
        this.logWoodStrip(leaves, log, wood, strippedLog, strippedWood, hangingSign, wallHangingSign)
    }

    private fun BlockModelGenerators.logWoodStrip(
        leaves: Block,
        log: Block,
        wood: Block,
        strippedLog: Block,
        strippedWood: Block,
        hangingSign: Block,
        wallHangingSign: Block
    ) {
        this.createTrivialBlock(leaves, TexturedModel.LEAVES)
        this.logWoodStrip(log, wood, strippedLog, strippedWood, hangingSign, wallHangingSign)
    }

    private fun BlockModelGenerators.logWoodStrip(
        log: Block,
        wood: Block,
        strippedLog: Block,
        strippedWood: Block,
        hangingSign: Block,
        wallHangingSign: Block
    ) {
        this.createHangingSign(strippedLog, hangingSign, wallHangingSign)
        this.logWoodStrip(log, wood, strippedLog, strippedWood)
    }

    private fun BlockModelGenerators.logWoodStrip(
        log: Block,
        wood: Block,
        strippedLog: Block,
        strippedWood: Block
    ) {
        this.woodProvider(log).logWithHorizontal(log).wood(wood)
        this.woodProvider(strippedLog).logWithHorizontal(strippedLog).wood(strippedWood)
    }
}