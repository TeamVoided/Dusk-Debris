package org.teamvoided.dusk_debris.util

import net.minecraft.block.Block
import net.minecraft.data.client.model.*
import net.minecraft.util.Identifier
import java.util.*


val ALL_KRY: TextureKey = TextureKey.of("all")

fun BlockStateModelGenerator.sixDirectionalBlock(block: Block) {
    this.blockStateCollector.accept(
        VariantsBlockStateSupplier.create(
            block,
            BlockStateVariant.create()
                .put(VariantSettings.MODEL, TexturedModel.CUBE_BOTTOM_TOP.create(block, this.modelCollector))
        ).coordinate(this.createUpDefaultFacingVariantMap())
    )
    this.registerParentedItemModel(block)
}

fun BlockStateModelGenerator.registerParentedItemModel(block: Block) =
    this.registerParentedItemModel(block, block.model())

//    fun BlockStateModelGenerator.parentedModel(block: Block, parent: Identifier): Identifier = this.parentedModel(block, block, parent)
fun BlockStateModelGenerator.parentedModel(
    block: Block,
    textBlock: Block,
    parent: Identifier
): Identifier =
    Model(parent.myb, Optional.empty(), ALL_KRY)
        .upload(block.model(), Texture().put(ALL_KRY, textBlock.model()), this.modelCollector)

fun BlockStateModelGenerator.parentedModel(
    block: Identifier,
    textBlock: Block,
    parent: Identifier
): Identifier =
    Model(parent.myb, Optional.empty(), ALL_KRY)
        .upload(block, Texture().put(ALL_KRY, textBlock.model()), this.modelCollector)


private
val <T : Any?> T.myb get() = Optional.ofNullable(this)

fun Block.modelSuffix(str: String) = this.model().suffix(str)

fun Identifier.suffix(str: String) = Identifier(this.namespace, "${this.path}$str")
fun Block.model(): Identifier = ModelIds.getBlockModelId(this)