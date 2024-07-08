package org.teamvoided.dusk_debris.util

import net.minecraft.block.Block
import net.minecraft.block.enums.JigsawOrientation
import net.minecraft.block.enums.WireConnection
import net.minecraft.data.client.model.*
import net.minecraft.data.client.model.VariantSettings.Rotation
import net.minecraft.item.Item
import net.minecraft.state.property.Properties
import net.minecraft.util.Identifier
import org.teamvoided.dusk_debris.DuskDebris.MODID
import org.teamvoided.dusk_debris.DuskDebris.id
import org.teamvoided.dusk_debris.block.NethershroomPlantBlock
import java.util.*
import java.util.stream.IntStream


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
    val front = Texture.getId(block)
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

fun BlockStateModelGenerator.registerHandheldItem(item: Item) {
    Models.HANDHELD.upload(
        ModelIds.getItemModelId(item),
        Texture.layer0(item),
        this.modelCollector
    )
}

fun BlockStateModelGenerator.registerDustBlock(block: Block) {
    this.registerDustBlock(block, MODID, block.toString())
}

fun BlockStateModelGenerator.registerDustBlockFromRedstone(block: Block) {
    this.registerDustBlock(block, "minecraft", "redstone")
}

fun BlockStateModelGenerator.registerDustBlock(block: Block, namespace: String, texture: String) {
    val dustUp = Identifier(namespace, "block/" + texture + "_dust_up")
    val dustDot = Identifier(namespace, "block/" + texture + "_dust_dot")
    val dustSide0 = Identifier(namespace, "block/" + texture + "_dust_side0")
    val dustSide1 = Identifier(namespace, "block/" + texture + "_dust_side1")
    val dustSideAlt0 = Identifier(namespace, "block/" + texture + "_dust_side_alt0")
    val dustSideAlt1 = Identifier(namespace, "block/" + texture + "_dust_side_alt1")
    this.blockStateCollector.accept(
        MultipartBlockStateSupplier.create(block).with(
            When.anyOf(
                *arrayOf<When>(
                    When.create()
                        .set(Properties.NORTH_WIRE_CONNECTION, WireConnection.NONE)
                        .set(Properties.EAST_WIRE_CONNECTION, WireConnection.NONE)
                        .set(Properties.SOUTH_WIRE_CONNECTION, WireConnection.NONE)
                        .set(Properties.WEST_WIRE_CONNECTION, WireConnection.NONE),
                    When.create()
                        .set(Properties.NORTH_WIRE_CONNECTION, WireConnection.SIDE, *arrayOf(WireConnection.UP))
                        .set(Properties.EAST_WIRE_CONNECTION, WireConnection.SIDE, *arrayOf(WireConnection.UP)),
                    When.create()
                        .set(Properties.EAST_WIRE_CONNECTION, WireConnection.SIDE, *arrayOf(WireConnection.UP))
                        .set(Properties.SOUTH_WIRE_CONNECTION, WireConnection.SIDE, *arrayOf(WireConnection.UP)),
                    When.create()
                        .set(Properties.SOUTH_WIRE_CONNECTION, WireConnection.SIDE, *arrayOf(WireConnection.UP))
                        .set(Properties.WEST_WIRE_CONNECTION, WireConnection.SIDE, *arrayOf(WireConnection.UP)),
                    When.create()
                        .set(Properties.WEST_WIRE_CONNECTION, WireConnection.SIDE, *arrayOf(WireConnection.UP))
                        .set(Properties.NORTH_WIRE_CONNECTION, WireConnection.SIDE, *arrayOf(WireConnection.UP))
                )
            ),
            BlockStateVariant.create()
                .put(VariantSettings.MODEL, dustDot)
        ).with(
            When.create().set(Properties.NORTH_WIRE_CONNECTION, WireConnection.SIDE, *arrayOf(WireConnection.UP)),
            BlockStateVariant.create()
                .put(VariantSettings.MODEL, dustSide0)
        ).with(
            When.create().set(Properties.SOUTH_WIRE_CONNECTION, WireConnection.SIDE, *arrayOf(WireConnection.UP)),
            BlockStateVariant.create()
                .put(VariantSettings.MODEL, dustSideAlt0)
        ).with(
            When.create().set(Properties.EAST_WIRE_CONNECTION, WireConnection.SIDE, *arrayOf(WireConnection.UP)),
            BlockStateVariant.create()
                .put(VariantSettings.MODEL, dustSideAlt1)
                .put(VariantSettings.Y, Rotation.R270)
        ).with(
            When.create().set(Properties.WEST_WIRE_CONNECTION, WireConnection.SIDE, *arrayOf(WireConnection.UP)),
            BlockStateVariant.create()
                .put(VariantSettings.MODEL, dustSide1)
                .put(VariantSettings.Y, Rotation.R270)
        ).with(
            When.create().set(Properties.NORTH_WIRE_CONNECTION, WireConnection.UP),
            BlockStateVariant.create()
                .put(VariantSettings.MODEL, dustUp)
        ).with(
            When.create().set(Properties.EAST_WIRE_CONNECTION, WireConnection.UP),
            BlockStateVariant.create()
                .put(VariantSettings.MODEL, dustUp)
                .put(VariantSettings.Y, Rotation.R90)
        ).with(
            When.create().set(Properties.SOUTH_WIRE_CONNECTION, WireConnection.UP),
            BlockStateVariant.create()
                .put(VariantSettings.MODEL, dustUp)
                .put(VariantSettings.Y, Rotation.R180)
        ).with(
            When.create().set(Properties.WEST_WIRE_CONNECTION, WireConnection.UP),
            BlockStateVariant.create()
                .put(VariantSettings.MODEL, dustUp)
                .put(VariantSettings.Y, Rotation.R270)
        )
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

fun BlockStateModelGenerator.throwableBlock(block: Block) {
    this.registerItemModel(block.asItem())
    val texture = Texture()
        .put(TextureKey.PARTICLE, Texture.getId(block.asItem()))
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

fun BlockStateModelGenerator.registerVesselBlock(block: Block) {
    this.registerItemModel(block.asItem())
    val texture = Texture()
        .put(TextureKey.PARTICLE, Texture.getId(block.asItem()))
        .put(TextureKey.ALL, Texture.getId(block))
    val model = block(
        "parent/mysterious_vessel",
        TextureKey.PARTICLE,
        TextureKey.ALL
    ).upload(block, texture, this.modelCollector)
    this.blockStateCollector.accept(
        VariantsBlockStateSupplier.create(
            block,
            BlockStateVariant.create().put(VariantSettings.MODEL, model)
        ).coordinate(BlockStateModelGenerator.createNorthDefaultHorizontalRotationStates())
    )
}
fun BlockStateModelGenerator.registerRelicBlock(block: Block) {
    this.registerItemModel(block.asItem())
    val texture = Texture()
        .put(TextureKey.PARTICLE, Texture.getId(block.asItem()))
        .put(TextureKey.ALL, Texture.getId(block))
    val model = block(
        "parent/peculiar_relic",
        TextureKey.PARTICLE,
        TextureKey.ALL
    ).upload(block, texture, this.modelCollector)
    this.blockStateCollector.accept(
        VariantsBlockStateSupplier.create(
            block,
            BlockStateVariant.create().put(VariantSettings.MODEL, model)
        ).coordinate(BlockStateModelGenerator.createNorthDefaultHorizontalRotationStates())
    )
}

fun BlockStateModelGenerator.registerNethershroom(block: Block) {
    this.excludeFromSimpleItemModelGeneration(block)
    this.registerItemModel(block)
    val texture = Texture()
        .put(TextureKey.PARTICLE, Texture.getId(block))
        .put(TextureKey.CROSS, Texture.getId(block))
    val textureSquish = Texture()
        .put(TextureKey.PARTICLE, Texture.getId(block))
        .put(TextureKey.CROSS, Texture.getSubId(block, "_squished"))
    val model = Models.CROSS.upload(
        block,
        texture,
        this.modelCollector
    )
    val modelSquished = Models.CROSS.upload(
        block,
        "_squished",
        textureSquish,
        this.modelCollector
    )
    this.blockStateCollector.accept(
        VariantsBlockStateSupplier.create(block).coordinate(
            BlockStateModelGenerator.createBooleanModelMap(
                NethershroomPlantBlock.SQUISHED,
                modelSquished,
                model
            )
        )
    )
}

fun BlockStateModelGenerator.registerNethershroomBlock(nethershroomBlock: Block) {
    val texture = Models.TEMPLATE_SINGLE_FACE.upload(
        nethershroomBlock, Texture.texture(nethershroomBlock),
        this.modelCollector
    )
    val insideTexture = id("block/nethershroom_block_inside")
    this.blockStateCollector.accept(
        MultipartBlockStateSupplier.create(nethershroomBlock).with(
            When.create().set(Properties.NORTH, true),
            BlockStateVariant.create().put(VariantSettings.MODEL, texture)
        ).with(
            When.create().set(Properties.EAST, true),
            BlockStateVariant.create().put(VariantSettings.MODEL, texture).put(VariantSettings.Y, Rotation.R90)
                .put(VariantSettings.UVLOCK, true)
        ).with(
            When.create().set(Properties.SOUTH, true),
            BlockStateVariant.create().put(VariantSettings.MODEL, texture).put(VariantSettings.Y, Rotation.R180)
                .put(VariantSettings.UVLOCK, true)
        ).with(
            When.create().set(Properties.WEST, true),
            BlockStateVariant.create().put(VariantSettings.MODEL, texture).put(VariantSettings.Y, Rotation.R270)
                .put(VariantSettings.UVLOCK, true)
        ).with(
            When.create().set(Properties.UP, true),
            BlockStateVariant.create().put(VariantSettings.MODEL, texture).put(VariantSettings.X, Rotation.R270)
                .put(VariantSettings.UVLOCK, true)
        ).with(
            When.create().set(Properties.DOWN, true),
            BlockStateVariant.create().put(VariantSettings.MODEL, texture).put(VariantSettings.X, Rotation.R90)
                .put(VariantSettings.UVLOCK, true)
        ).with(
            When.create().set(Properties.NORTH, false),
            BlockStateVariant.create().put(VariantSettings.MODEL, insideTexture)
        ).with(
            When.create().set(Properties.EAST, false),
            BlockStateVariant.create().put(VariantSettings.MODEL, insideTexture).put(VariantSettings.Y, Rotation.R90)
                .put(VariantSettings.UVLOCK, false)
        ).with(
            When.create().set(Properties.SOUTH, false),
            BlockStateVariant.create().put(VariantSettings.MODEL, insideTexture).put(VariantSettings.Y, Rotation.R180)
                .put(VariantSettings.UVLOCK, false)
        ).with(
            When.create().set(Properties.WEST, false),
            BlockStateVariant.create().put(VariantSettings.MODEL, insideTexture).put(VariantSettings.Y, Rotation.R270)
                .put(VariantSettings.UVLOCK, false)
        ).with(
            When.create().set(Properties.UP, false),
            BlockStateVariant.create().put(VariantSettings.MODEL, insideTexture).put(VariantSettings.X, Rotation.R270)
                .put(VariantSettings.UVLOCK, false)
        ).with(
            When.create().set(Properties.DOWN, false),
            BlockStateVariant.create().put(VariantSettings.MODEL, insideTexture).put(VariantSettings.X, Rotation.R90)
                .put(VariantSettings.UVLOCK, false)
        )
    )
    this.registerParentedItemModel(
        nethershroomBlock, TexturedModel.CUBE_ALL.createWithSuffix(
            nethershroomBlock, "_inventory",
            this.modelCollector
        )
    )
}

fun BlockStateModelGenerator.registerRibbon(block: Block) {
    this.excludeFromSimpleItemModelGeneration(block)
    this.registerItemModel(block.asItem())
    this.blockStateCollector.accept(
        VariantsBlockStateSupplier.create(block, *(getRibbonBlockStateVariants(block, 4).toTypedArray()))
            .coordinate(BlockStateModelGenerator.createAxisRotatedVariantMap())
    )
}

fun BlockStateModelGenerator.getRibbonBlockStateVariants(block: Block, variants: Int): List<BlockStateVariant> {
    return IntStream.range(1, variants + 1).mapToObj { variant ->
        BlockStateVariant.create().put(
            VariantSettings.MODEL,
            this.makeRibbonModel(block, variant)
        )
    }.toList()
}

fun BlockStateModelGenerator.makeRibbonModel(block: Block, variant: Int): Identifier {
    val texture = Texture()
        .put(TextureKey.PARTICLE, Texture.getId(block))
        .put(TextureKey.ALL, Texture.getId(block))
    return block(
        "parent/ribbon_$variant",
        TextureKey.PARTICLE,
        TextureKey.ALL
    ).upload(block.modelSuffix("_$variant"), texture, this.modelCollector)
}
fun parentedItemModel(id:Identifier) = Model(Optional.of(id.withPrefix("item/")), Optional.empty())
fun BlockStateModelGenerator.registerParentedItemModel(block: Block) =
    this.registerParentedItemModel(block, block.model())

//    fun BlockStateModelGenerator.parentedModel(block: Block, parent: Identifier): Identifier = this.parentedModel(block, block, parent)
fun block(parent: String, vararg requiredTextures: TextureKey): Model {
    return Model(
        Optional.of(
            id("block/$parent")
        ), Optional.empty(), *requiredTextures
    )
}

fun block(parent: String, variant: String, vararg requiredTextures: TextureKey): Model {
    return Model(
        Optional.of(
            id("block/$parent")
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