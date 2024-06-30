package org.teamvoided.dusk_debris.data.gen.providers


import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider
import net.minecraft.block.Blocks
import net.minecraft.data.client.ItemModelGenerator
import net.minecraft.data.client.model.*
import org.teamvoided.dusk_debris.block.DuskBlockLists
import org.teamvoided.dusk_debris.init.DuskBlocks
import org.teamvoided.dusk_debris.init.DuskItems
import org.teamvoided.dusk_debris.util.gunpowderBarrelBlock
import org.teamvoided.dusk_debris.util.registerNethershroom
import org.teamvoided.dusk_debris.util.registerNethershroomBlock
import org.teamvoided.dusk_debris.util.throwableBlock

class ModelProvider(o: FabricDataOutput) : FabricModelProvider(o) {

    override fun generateBlockStateModels(gen: BlockStateModelGenerator) {
        gen.sandstoneModels = mapOf(
            DuskBlocks.VOLCANIC_SANDSTONE to TexturedModel.SIDE_TOP_BOTTOM_WALL[DuskBlocks.VOLCANIC_SANDSTONE],
            DuskBlocks.SMOOTH_VOLCANIC_SANDSTONE to TexturedModel.getCubeAll(
                Texture.getSubId(DuskBlocks.VOLCANIC_SANDSTONE, "_top")
            ),
            DuskBlocks.CUT_VOLCANIC_SANDSTONE to TexturedModel.CUBE_COLUMN[DuskBlocks.VOLCANIC_SANDSTONE].texture { texture: Texture ->
                texture.put(
                    TextureKey.SIDE,
                    Texture.getId(DuskBlocks.CUT_VOLCANIC_SANDSTONE)
                )
            },
            DuskBlocks.CHISELED_VOLCANIC_SANDSTONE to TexturedModel.CUBE_COLUMN[DuskBlocks.CHISELED_VOLCANIC_SANDSTONE].texture { texture: Texture ->
                texture.put(TextureKey.END, Texture.getSubId(DuskBlocks.VOLCANIC_SANDSTONE, "_top"))
                texture.put(TextureKey.SIDE, Texture.getId(DuskBlocks.CHISELED_VOLCANIC_SANDSTONE))
            }
        )
        DuskBlockLists.RIBBON_BLOCKS_LIST.forEach {
            gen.registerAxisRotated(it, ModelIds.getBlockModelId(it))
        }
        DuskBlockLists.THROWABLE_BLOCK_LIST.forEach {
            gen.throwableBlock(it)
        }
        DuskBlockLists.blockFamily.forEach {
            gen.registerCubeAllModelTexturePool(it.baseBlock).family(it)
        }

        gen.registerNethershroom(DuskBlocks.BLUE_NETHERSHROOM)
        gen.registerNethershroomBlock(DuskBlocks.BLUE_NETHERSHROOM_BLOCK)
        gen.registerNethershroom(DuskBlocks.PURPLE_NETHERSHROOM)
        gen.registerNethershroomBlock(DuskBlocks.PURPLE_NETHERSHROOM_BLOCK)
        gen.registerNethershroomBlock(DuskBlocks.NETHERSHROOM_STEM)

        gen.gunpowderBarrelBlock(DuskBlocks.GUNPOWDER_BARREL)
        gen.gunpowderBarrelBlock(DuskBlocks.STRONGHOLD_GUNPOWDER_BARREL)
        gen.gunpowderBarrelBlock(DuskBlocks.ANCIENT_BLACK_POWDER_BARREL)
        gen.registerLog(DuskBlocks.CYPRESS_LOG)
            .log(DuskBlocks.CYPRESS_LOG)
            .wood(DuskBlocks.CYPRESS_WOOD)
        gen.registerLog(DuskBlocks.STRIPPED_CYPRESS_LOG)
            .log(DuskBlocks.STRIPPED_CYPRESS_LOG)
            .wood(DuskBlocks.STRIPPED_CYPRESS_WOOD)

        gen.registerSimpleCubeAll(DuskBlocks.VOLCANIC_SAND)
        gen.registerDustable(DuskBlocks.SUSPICIOUS_VOLCANIC_SAND)
        gen.registerLog(DuskBlocks.CHARRED_LOG)
            .log(DuskBlocks.CHARRED_LOG)
            .wood(DuskBlocks.CHARRED_WOOD)
        gen.registerLog(DuskBlocks.STRIPPED_CHARRED_LOG)
            .log(DuskBlocks.STRIPPED_CHARRED_LOG)
            .wood(DuskBlocks.STRIPPED_CHARRED_WOOD)
        gen.registerHangingSign(
            DuskBlocks.STRIPPED_CHARRED_LOG,
            DuskBlocks.CHARRED_HANGING_SIGN,
            DuskBlocks.CHARRED_WALL_HANGING_SIGN
        )
        gen.registerHangingSign(
            DuskBlocks.STRIPPED_CYPRESS_LOG,
            DuskBlocks.CYPRESS_HANGING_SIGN,
            DuskBlocks.CYPRESS_WALL_HANGING_SIGN
        )
//        gen.registerSingleton(DuskBlocks.SMOOTH_VOLCANIC_SANDSTONE) {
//            TexturedModel.getCubeAll(Texture.getSubId(DuskBlocks.VOLCANIC_SANDSTONE, "_top"))
//        }
    }

    override fun generateItemModels(gen: ItemModelGenerator) {}
}
