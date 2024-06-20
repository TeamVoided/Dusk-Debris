package org.teamvoided.dusk_debris.util

import net.minecraft.block.Block
import net.minecraft.block.enums.JigsawOrientation
import net.minecraft.data.client.model.*
import net.minecraft.item.Item
import net.minecraft.state.property.Properties
import net.minecraft.util.Identifier
import org.teamvoided.dusk_debris.DuskDebris
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

fun BlockStateModelGenerator.rollableBlock(block: Block) {
    val front = Texture.getSubId(block, "")
    val bottom = Texture.getSubId(block, "_bottom")
    val side = Texture.getSubId(block, "_side")
    val top = Texture.getSubId(block, "_top")
    val texture = Texture()
        .put(TextureKey.PARTICLE, front)
        .put(TextureKey.NORTH, front)
        .put(TextureKey.SOUTH, bottom)
        .put(TextureKey.EAST, side)
        .put(TextureKey.WEST, side)
        .put(TextureKey.DOWN, side)
        .put(TextureKey.UP, top)
    val model = Models.CUBE_DIRECTIONAL.upload(block, texture, this.modelCollector)
    this.blockStateCollector.accept(
        VariantsBlockStateSupplier.create(
            block,
            BlockStateVariant.create().put(VariantSettings.MODEL, model)
        ).coordinate(BlockStateVariantMap.create(
            Properties.ORIENTATION
        ).register { jigsawOrientation: JigsawOrientation ->
            this.addJigsawOrientationToVariant(
                jigsawOrientation,
                BlockStateVariant.create()
            )
        })
    )
}

fun BlockStateModelGenerator.gunpowderBarrelBlock(block: Block) {
    val front = Texture.getSubId(block, "_front")
    val side = Texture.getSubId(block, "_side")
    val top = Texture.getSubId(block, "_top")
    val texture = Texture()
        .put(TextureKey.PARTICLE, side)
        .put(TextureKey.FRONT, front)
        .put(TextureKey.SIDE, side)
        .put(TextureKey.TOP, top)
    val model = block(
        "parent/gunpowder_barrel",
        TextureKey.PARTICLE,
        TextureKey.FRONT,
        TextureKey.SIDE,
        TextureKey.TOP
    ).upload(block, texture, this.modelCollector)
    this.blockStateCollector.accept(
        VariantsBlockStateSupplier.create(
            block,
            BlockStateVariant.create().put(VariantSettings.MODEL, model)
        ).coordinate(BlockStateVariantMap.create(
            Properties.ORIENTATION
        ).register { jigsawOrientation: JigsawOrientation ->
            this.addJigsawOrientationToVariant(
                jigsawOrientation,
                BlockStateVariant.create()
            )
        })
    )
}

fun BlockStateModelGenerator.throwableBlock(item: Item, block: Block) {
    this.registerItemModel(item)
    val texture = Texture()
        .put(TextureKey.PARTICLE, Texture.getId(item))
        .put(TextureKey.ALL, Texture.getId(block))
    val model = block(
        "parent/throwable_block",
        TextureKey.PARTICLE,
        TextureKey.ALL
    ).upload(block, texture, this.modelCollector)
    val hangingModel = block(
        "parent/throwable_block_hanging",
        "_hanging",
        TextureKey.PARTICLE,
        TextureKey.ALL
    ).upload(block, texture, this.modelCollector)
    this.blockStateCollector.accept(
        VariantsBlockStateSupplier.create(block).coordinate(
            BlockStateModelGenerator.createBooleanModelMap(
                Properties.HANGING,
                hangingModel,
                model
            )
        ).coordinate(BlockStateModelGenerator.createNorthDefaultHorizontalRotationStates())
    )
}

//fun BlockStateModelGenerator.temp(block: Block, texture: Texture) {
//    val identifier = Models.ORIENTABLE.upload(
//        block, texture.copyAndAdd(TextureKey.FRONT, Texture.getId(block)),
//        this.modelCollector
//    )
//    this.blockStateCollector.accept(
//        VariantsBlockStateSupplier.create(
//            block,
//            BlockStateVariant.create().put(VariantSettings.MODEL, identifier)
//        ).coordinate(BlockStateModelGenerator.createNorthDefaultHorizontalRotationStates())
//    )
//}


//fun BlockStateModelGenerator.throwableBlock2(item: Item, block: Block) {
//    this.registerItemModel(item.asItem())
//    this.blockStateCollector.accept(
//        VariantsBlockStateSupplier.create(block).coordinate(
//            BlockStateModelGenerator.createBooleanModelMap(
//                Properties.HANGING,
//                DuskDebris.id("block/blunderbomb"),
//                DuskDebris.id("block/blunderbomb_hanging")
//            )
//        )
//    )
//}

fun BlockStateModelGenerator.registerParentedItemModel(block: Block) =
    this.registerParentedItemModel(block, block.model())

//    fun BlockStateModelGenerator.parentedModel(block: Block, parent: Identifier): Identifier = this.parentedModel(block, block, parent)
fun block(parent: String, vararg requiredTextures: TextureKey): Model {
    return Model(
        Optional.of(
            DuskDebris.id("block/$parent")
        ), Optional.empty(), *requiredTextures
    )
}

fun block(parent: String, variant: String, vararg requiredTextures: TextureKey): Model {
    return Model(
        Optional.of(
            DuskDebris.id("block/$parent")
        ), Optional.of(variant), *requiredTextures
    )
}

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