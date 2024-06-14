package org.teamvoided.dusk_debris.data.gen.providers


import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider
import net.minecraft.block.Block
import net.minecraft.block.Blocks
import net.minecraft.data.client.ItemModelGenerator
import net.minecraft.data.client.model.*
import net.minecraft.util.Identifier
import org.teamvoided.dusk_debris.block.DuskBlockFamilies
import org.teamvoided.dusk_debris.init.DuskBlocks
import java.util.*

class ModelProvider(o: FabricDataOutput) : FabricModelProvider(o) {

    private val ALL_KRY: TextureKey = TextureKey.of("all")

    val blockFamily = listOf(
        DuskBlockFamilies.VOLCANIC_SANDSTONE,
        DuskBlockFamilies.CUT_VOLCANIC_SANDSTONE,
        DuskBlockFamilies.SMOOTH_VOLCANIC_SANDSTONE,
        DuskBlockFamilies.CHARRED
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
        gen.registerCubeAllModelTexturePool(DuskBlocks.VOLCANIC_SAND)
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
        blockFamily.forEach {
            gen.registerCubeAllModelTexturePool(it.baseBlock).family(it)
        }
//        gen.registerSingleton(DuskBlocks.SMOOTH_VOLCANIC_SANDSTONE) {
//            TexturedModel.getCubeAll(Texture.getSubId(DuskBlocks.VOLCANIC_SANDSTONE, "_top"))
//        }
    }

    override fun generateItemModels(gen: ItemModelGenerator) {}

//    private fun BlockStateModelGenerator.parentedModel(block: Block, parent: Identifier): Identifier = this.parentedModel(block, block, parent)

    private fun BlockStateModelGenerator.parentedModel(
        block: Block,
        textBlock: Block,
        parent: Identifier
    ): Identifier =
        Model(parent.myb, Optional.empty(), ALL_KRY)
            .upload(block.model(), Texture().put(ALL_KRY, textBlock.model()), this.modelCollector)

    private fun BlockStateModelGenerator.parentedModel(
        block: Identifier,
        textBlock: Block,
        parent: Identifier
    ): Identifier =
        Model(parent.myb, Optional.empty(), ALL_KRY)
            .upload(block, Texture().put(ALL_KRY, textBlock.model()), this.modelCollector)


    private
    val <T : Any?> T.myb get() = Optional.ofNullable(this)

    private fun Block.modelSuffix(str: String) = this.model().suffix(str)

    private fun Identifier.suffix(str: String) = Identifier(this.namespace, "${this.path}$str")
    private fun Block.model(): Identifier = ModelIds.getBlockModelId(this)
}