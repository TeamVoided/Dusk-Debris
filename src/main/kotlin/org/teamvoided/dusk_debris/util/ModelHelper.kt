package org.teamvoided.dusk_debris.util

import net.minecraft.block.Block
import net.minecraft.block.Blocks
import net.minecraft.block.enums.JigsawOrientation
import net.minecraft.data.client.model.*
import net.minecraft.data.client.model.VariantSettings.Rotation
import net.minecraft.state.property.Properties
import net.minecraft.util.Identifier
import org.teamvoided.dusk_debris.DuskDebris
import org.teamvoided.dusk_debris.DuskDebris.id
import org.teamvoided.dusk_debris.block.NethershroomPlantBlock
import java.util.*
import java.util.stream.Collectors
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

fun BlockStateModelGenerator.ribbon(block: Block) {
    this.excludeFromSimpleItemModelGeneration(block)
    this.registerItemModel(block.asItem())
    this.blockStateCollector.accept(
        MultipartBlockStateSupplier.create(block).with(
            getBlockStateVariants(block, 4)
        )
    )
}

fun getBlockStateVariants(block: Block, variants: Int): List<BlockStateVariant> {
    return IntStream.range(1, variants + 1).mapToObj { i: Int ->
        BlockStateVariant.create().put(
            VariantSettings.MODEL,
            ModelIds.getBlockSubModelId(block, "_$i")
        )
    }.collect(Collectors.toList())
}

private fun BlockStateModelGenerator.registerBamboo() {
    this.excludeFromSimpleItemModelGeneration(Blocks.BAMBOO)
    this.blockStateCollector.accept(
        MultipartBlockStateSupplier.create(Blocks.BAMBOO).with(
            getBambooBlockStateVariants(0)
        )
    )
}

fun getBambooBlockStateVariants(age: Int): List<BlockStateVariant> {
    val string = "_age$age"
    return IntStream.range(1, 5).mapToObj { i: Int ->
        BlockStateVariant.create().put(
            VariantSettings.MODEL,
            ModelIds.getBlockSubModelId(Blocks.BAMBOO, "" + i + string)
        )
    }.collect(Collectors.toList())
}

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