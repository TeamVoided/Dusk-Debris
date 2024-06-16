package org.teamvoided.dusk_debris.data.gen.providers


import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider
import net.minecraft.data.client.ItemModelGenerator
import net.minecraft.data.client.model.BlockStateModelGenerator
import net.minecraft.data.client.model.Texture
import net.minecraft.data.client.model.TextureKey
import net.minecraft.data.client.model.TexturedModel
import org.teamvoided.dusk_debris.block.DuskBlockFamilies
import org.teamvoided.dusk_debris.init.DuskBlocks
import org.teamvoided.dusk_debris.util.sixDirectionalBlock

class ModelProvider(o: FabricDataOutput) : FabricModelProvider(o) {


    val blockFamily = listOf(
        DuskBlockFamilies.VOLCANIC_SANDSTONE_FAMILY,
        DuskBlockFamilies.CUT_VOLCANIC_SANDSTONE_FAMILY,
        DuskBlockFamilies.SMOOTH_VOLCANIC_SANDSTONE_FAMILY,
        DuskBlockFamilies.CYPRUS_FAMILY,
        DuskBlockFamilies.CHARRED_FAMILY
    )

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
        blockFamily.forEach {
            gen.registerCubeAllModelTexturePool(it.baseBlock).family(it)
        }

        gen.sixDirectionalBlock(DuskBlocks.GUNPOWDER_BARREL)

        gen.registerLog(DuskBlocks.CYPRUS_LOG)
            .log(DuskBlocks.CYPRUS_LOG)
            .wood(DuskBlocks.CYPRUS_WOOD)
        gen.registerLog(DuskBlocks.STRIPPED_CYPRUS_LOG)
            .log(DuskBlocks.STRIPPED_CYPRUS_LOG)
            .wood(DuskBlocks.STRIPPED_CYPRUS_WOOD)


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
            DuskBlocks.STRIPPED_CYPRUS_LOG,
            DuskBlocks.CYPRUS_HANGING_SIGN,
            DuskBlocks.CYPRUS_WALL_HANGING_SIGN
        )
//        gen.registerSingleton(DuskBlocks.SMOOTH_VOLCANIC_SANDSTONE) {
//            TexturedModel.getCubeAll(Texture.getSubId(DuskBlocks.VOLCANIC_SANDSTONE, "_top"))
//        }
    }

    override fun generateItemModels(gen: ItemModelGenerator) {}
}
