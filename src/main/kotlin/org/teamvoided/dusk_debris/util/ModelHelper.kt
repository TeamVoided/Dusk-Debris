package org.teamvoided.dusk_debris.util

import net.minecraft.core.FrontAndTop
import net.minecraft.data.models.BlockModelGenerators
import net.minecraft.data.models.blockstates.*
import net.minecraft.data.models.blockstates.VariantProperties.Rotation
import net.minecraft.data.models.model.*
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.world.level.block.state.properties.RedstoneSide
import org.teamvoided.dusk_debris.DuskDebris.MODID
import org.teamvoided.dusk_debris.DuskDebris.id
import org.teamvoided.dusk_debris.block.NethershroomPlantBlock
import org.teamvoided.dusk_debris.block.not_blocks.DuskProperties
import org.teamvoided.dusk_debris.block.not_blocks.GodhomeBronzePhase
import org.teamvoided.dusk_debris.block.sot.GildedChaliceBlock
import org.teamvoided.dusk_debris.block.sot.RoaringGeyserBlock
import java.util.*
import java.util.stream.IntStream


val ALL_KRY: TextureSlot = TextureSlot.create("all")

val OFFSET_WALL_POST = block("parent/offset_wall_post", "_post", TextureSlot.WALL)
val OFFSET_WALL_INVENTORY = block("parent/offset_wall_inventory", "_inventory", TextureSlot.WALL)
fun BlockModelGenerators.wallOffset(block: Block) = wallOffset(block, block.model())
fun BlockModelGenerators.wallOffset(block: Block, texture: Block) = wallOffset(block, texture.model())
fun BlockModelGenerators.wallOffset(wallBlock: Block, inId: ResourceLocation) {
    val texture = TextureMapping.defaultTexture(wallBlock.model()).put(TextureSlot.WALL, inId)
    val id = OFFSET_WALL_POST.create(wallBlock, texture, this.modelOutput)
    val id2 = ModelTemplates.WALL_LOW_SIDE.create(wallBlock, texture, this.modelOutput)
    val id3 = ModelTemplates.WALL_TALL_SIDE.create(wallBlock, texture, this.modelOutput)
    this.blockStateOutput.accept(BlockModelGenerators.createWall(wallBlock, id, id2, id3))
    this.delegateItemModel(wallBlock, OFFSET_WALL_INVENTORY.create(wallBlock, texture, this.modelOutput))
}

fun BlockModelGenerators.sixDirectionalBlock(block: Block) {
    this.blockStateOutput.accept(
        MultiVariantGenerator.multiVariant(
            block,
            Variant.variant()
                .with(VariantProperties.MODEL, TexturedModel.CUBE_TOP_BOTTOM.create(block, this.modelOutput))
        ).with(this.createColumnWithFacing())
    )
    this.registerParentedItemModel(block)
}

fun BlockModelGenerators.rollableBlock(block: Block) {
    val front = TextureMapping.getBlockTexture(block)
    val bottom = TextureMapping.getBlockTexture(block, "_bottom")
    val side = TextureMapping.getBlockTexture(block, "_side")
    val top = TextureMapping.getBlockTexture(block, "_top")
    val texture = TextureMapping()
        .put(TextureSlot.PARTICLE, front)
        .put(TextureSlot.NORTH, front)
        .put(TextureSlot.SOUTH, bottom)
        .put(TextureSlot.EAST, side)
        .put(TextureSlot.WEST, side)
        .put(TextureSlot.DOWN, side)
        .put(TextureSlot.UP, top)
    val model = ModelTemplates.CUBE_DIRECTIONAL.create(block, texture, this.modelOutput)
    this.blockStateOutput.accept(
        MultiVariantGenerator.multiVariant(
            block,
            Variant.variant().with(VariantProperties.MODEL, model)
        ).with(
            PropertyDispatch.property(
            BlockStateProperties.ORIENTATION
        ).generate { jigsawOrientation: FrontAndTop ->
            this.applyRotation(
                jigsawOrientation,
                Variant.variant()
            )
        })
    )
}

fun BlockModelGenerators.vesselLantern(block: Block) {
    val side = TextureMapping.getBlockTexture(block)
    val end = TextureMapping.getBlockTexture(block, "_end")
    val texture = TextureMapping()
        .put(TextureSlot.SIDE, side)
        .put(TextureSlot.END, end)
    val model = block(
        "parent/vessel_lantern",
        TextureSlot.SIDE,
        TextureSlot.END
    ).create(block, texture, this.modelOutput)
    this.createSimpleFlatItemModel(block)
    this.blockStateOutput.accept(BlockModelGenerators.createAxisAlignedPillarBlock(block, model))
}


fun BlockModelGenerators.godhomeShiftBlock(block: Block) {
    val textureSomber = TextureMapping()
        .put(TextureSlot.FRONT, TextureMapping.getBlockTexture(block, "_somber_front"))
        .put(TextureSlot.TOP, TextureMapping.getBlockTexture(block, "_somber_top"))
        .put(TextureSlot.SIDE, TextureMapping.getBlockTexture(block, "_somber_side"))
        .put(TextureSlot.BACK, TextureMapping.getBlockTexture(block, "_somber_back"))
    val textureShining = TextureMapping()
        .put(TextureSlot.FRONT, TextureMapping.getBlockTexture(block, "_shining_front"))
        .put(TextureSlot.TOP, TextureMapping.getBlockTexture(block, "_shining_top"))
        .put(TextureSlot.SIDE, TextureMapping.getBlockTexture(block, "_shining_side"))
        .put(TextureSlot.BACK, TextureMapping.getBlockTexture(block, "_shining_back"))
    val textureRadiant = TextureMapping()
        .put(TextureSlot.FRONT, TextureMapping.getBlockTexture(block, "_radiant_front"))
        .put(TextureSlot.TOP, TextureMapping.getBlockTexture(block, "_radiant_top"))
        .put(TextureSlot.SIDE, TextureMapping.getBlockTexture(block, "_radiant_side"))
        .put(TextureSlot.BACK, TextureMapping.getBlockTexture(block, "_radiant_back"))
    val somberModel = block(
        "parent/front_top_side_back",
        TextureSlot.FRONT,
        TextureSlot.TOP,
        TextureSlot.SIDE,
        TextureSlot.BACK
    ).createWithSuffix(block, "_somber", textureSomber, this.modelOutput)
    val shiningModel = block(
        "parent/front_top_side_back",
        TextureSlot.FRONT,
        TextureSlot.TOP,
        TextureSlot.SIDE,
        TextureSlot.BACK
    ).createWithSuffix(block, "_shining", textureShining, this.modelOutput)
    val radiantModel = block(
        "parent/front_top_side_back",
        TextureSlot.FRONT,
        TextureSlot.TOP,
        TextureSlot.SIDE,
        TextureSlot.BACK
    ).createWithSuffix(block, "_radiant", textureRadiant, this.modelOutput)
    this.delegateItemModel(block.asItem(), somberModel)
    this.blockStateOutput.accept(
        MultiVariantGenerator.multiVariant(block)
            .with(
                PropertyDispatch.property(
                BlockStateProperties.ORIENTATION
            ).generate { orientation: FrontAndTop? ->
                this.applyRotation(
                    orientation,
                    Variant.variant()
                )
            }).with(
                PropertyDispatch.property(DuskProperties.GODHOME_BRONZE_PHASE)
                    .select(
                        GodhomeBronzePhase.SOMBER,
                        Variant.variant().with(VariantProperties.MODEL, somberModel)
                    )
                    .select(
                        GodhomeBronzePhase.SHINING,
                        Variant.variant().with(VariantProperties.MODEL, shiningModel)
                    )
                    .select(
                        GodhomeBronzePhase.RADIANT,
                        Variant.variant().with(VariantProperties.MODEL, radiantModel)
                    )
            )
    )
}

fun BlockModelGenerators.registerHandheldItem(item: Item) {
    ModelTemplates.FLAT_HANDHELD_ITEM.create(
        ModelLocationUtils.getModelLocation(item),
        TextureMapping.layer0(item),
        this.modelOutput
    )
}

fun BlockModelGenerators.registerDustBlock(block: Block) {
    this.registerDustBlock(block, MODID, block.toString())
}

fun BlockModelGenerators.registerDustBlockFromRedstone(block: Block) {
    this.registerDustBlock(block, "minecraft", "redstone")
}

fun BlockModelGenerators.registerDustBlock(block: Block, namespace: String, texture: String) {
    val dustUp = ResourceLocation(namespace, "block/" + texture + "_dust_up")
    val dustDot = ResourceLocation(namespace, "block/" + texture + "_dust_dot")
    val dustSide0 = ResourceLocation(namespace, "block/" + texture + "_dust_side0")
    val dustSide1 = ResourceLocation(namespace, "block/" + texture + "_dust_side1")
    val dustSideAlt0 = ResourceLocation(namespace, "block/" + texture + "_dust_side_alt0")
    val dustSideAlt1 = ResourceLocation(namespace, "block/" + texture + "_dust_side_alt1")
    this.blockStateOutput.accept(
        MultiPartGenerator.multiPart(block).with(
            Condition.or(
                *arrayOf<Condition>(
                    Condition.condition()
                        .term(BlockStateProperties.NORTH_REDSTONE, RedstoneSide.NONE)
                        .term(BlockStateProperties.EAST_REDSTONE, RedstoneSide.NONE)
                        .term(BlockStateProperties.SOUTH_REDSTONE, RedstoneSide.NONE)
                        .term(BlockStateProperties.WEST_REDSTONE, RedstoneSide.NONE),
                    Condition.condition()
                        .term(BlockStateProperties.NORTH_REDSTONE, RedstoneSide.SIDE, *arrayOf(RedstoneSide.UP))
                        .term(BlockStateProperties.EAST_REDSTONE, RedstoneSide.SIDE, *arrayOf(RedstoneSide.UP)),
                    Condition.condition()
                        .term(BlockStateProperties.EAST_REDSTONE, RedstoneSide.SIDE, *arrayOf(RedstoneSide.UP))
                        .term(BlockStateProperties.SOUTH_REDSTONE, RedstoneSide.SIDE, *arrayOf(RedstoneSide.UP)),
                    Condition.condition()
                        .term(BlockStateProperties.SOUTH_REDSTONE, RedstoneSide.SIDE, *arrayOf(RedstoneSide.UP))
                        .term(BlockStateProperties.WEST_REDSTONE, RedstoneSide.SIDE, *arrayOf(RedstoneSide.UP)),
                    Condition.condition()
                        .term(BlockStateProperties.WEST_REDSTONE, RedstoneSide.SIDE, *arrayOf(RedstoneSide.UP))
                        .term(BlockStateProperties.NORTH_REDSTONE, RedstoneSide.SIDE, *arrayOf(RedstoneSide.UP))
                )
            ),
            Variant.variant()
                .with(VariantProperties.MODEL, dustDot)
        ).with(
            Condition.condition().term(BlockStateProperties.NORTH_REDSTONE, RedstoneSide.SIDE, *arrayOf(RedstoneSide.UP)),
            Variant.variant()
                .with(VariantProperties.MODEL, dustSide0)
        ).with(
            Condition.condition().term(BlockStateProperties.SOUTH_REDSTONE, RedstoneSide.SIDE, *arrayOf(RedstoneSide.UP)),
            Variant.variant()
                .with(VariantProperties.MODEL, dustSideAlt0)
        ).with(
            Condition.condition().term(BlockStateProperties.EAST_REDSTONE, RedstoneSide.SIDE, *arrayOf(RedstoneSide.UP)),
            Variant.variant()
                .with(VariantProperties.MODEL, dustSideAlt1)
                .with(VariantProperties.Y_ROT, Rotation.R270)
        ).with(
            Condition.condition().term(BlockStateProperties.WEST_REDSTONE, RedstoneSide.SIDE, *arrayOf(RedstoneSide.UP)),
            Variant.variant()
                .with(VariantProperties.MODEL, dustSide1)
                .with(VariantProperties.Y_ROT, Rotation.R270)
        ).with(
            Condition.condition().term(BlockStateProperties.NORTH_REDSTONE, RedstoneSide.UP),
            Variant.variant()
                .with(VariantProperties.MODEL, dustUp)
        ).with(
            Condition.condition().term(BlockStateProperties.EAST_REDSTONE, RedstoneSide.UP),
            Variant.variant()
                .with(VariantProperties.MODEL, dustUp)
                .with(VariantProperties.Y_ROT, Rotation.R90)
        ).with(
            Condition.condition().term(BlockStateProperties.SOUTH_REDSTONE, RedstoneSide.UP),
            Variant.variant()
                .with(VariantProperties.MODEL, dustUp)
                .with(VariantProperties.Y_ROT, Rotation.R180)
        ).with(
            Condition.condition().term(BlockStateProperties.WEST_REDSTONE, RedstoneSide.UP),
            Variant.variant()
                .with(VariantProperties.MODEL, dustUp)
                .with(VariantProperties.Y_ROT, Rotation.R270)
        )
    )
}

fun BlockModelGenerators.gunpowderBarrelBlock(block: Block) {
    val front = TextureMapping.getBlockTexture(block, "_front")
    val side = TextureMapping.getBlockTexture(block, "_side")
    val top = TextureMapping.getBlockTexture(block, "_top")
    val texture = TextureMapping()
        .put(TextureSlot.PARTICLE, side)
        .put(TextureSlot.FRONT, front)
        .put(TextureSlot.SIDE, side)
        .put(TextureSlot.TOP, top)
    val model = block(
        "parent/gunpowder_barrel",
        TextureSlot.PARTICLE,
        TextureSlot.FRONT,
        TextureSlot.SIDE,
        TextureSlot.TOP
    ).create(block, texture, this.modelOutput)
    this.blockStateOutput.accept(
        MultiVariantGenerator.multiVariant(
            block,
            Variant.variant().with(VariantProperties.MODEL, model)
        ).with(
            PropertyDispatch.property(
            BlockStateProperties.ORIENTATION
        ).generate { jigsawOrientation: FrontAndTop ->
            this.applyRotation(
                jigsawOrientation,
                Variant.variant()
            )
        })
    )
}

fun BlockModelGenerators.throwableBlock(block: Block) {
    this.createSimpleFlatItemModel(block.asItem())
    val texture = TextureMapping()
        .put(TextureSlot.PARTICLE, TextureMapping.getItemTexture(block.asItem()))
        .put(TextureSlot.ALL, TextureMapping.getBlockTexture(block))
    val model = block(
        "parent/throwable_block",
        TextureSlot.PARTICLE,
        TextureSlot.ALL
    ).create(block, texture, this.modelOutput)
    val hangingModel = block(
        "parent/throwable_block_hanging",
        "_hanging",
        TextureSlot.PARTICLE,
        TextureSlot.ALL
    ).create(block, texture, this.modelOutput)
    this.blockStateOutput.accept(
        MultiVariantGenerator.multiVariant(block).with(
            BlockModelGenerators.createBooleanModelDispatch(
                BlockStateProperties.HANGING,
                hangingModel,
                model
            )
        ).with(BlockModelGenerators.createHorizontalFacingDispatch())
    )
}

fun BlockModelGenerators.registerDecorativeGoldBlock(block: Block, modelString: String, asItem: Boolean) {
    val texture: TextureMapping
    if (asItem) {
        this.createSimpleFlatItemModel(block.asItem())
        texture = TextureMapping()
            .put(TextureSlot.PARTICLE, TextureMapping.getItemTexture(block.asItem()))
            .put(TextureSlot.ALL, TextureMapping.getBlockTexture(block))
    } else {
        this.registerParentedItemModel(block)
        texture = TextureMapping()
            .put(TextureSlot.PARTICLE, TextureMapping.getBlockTexture(block))
            .put(TextureSlot.ALL, TextureMapping.getBlockTexture(block))
    }
    val model = block(
        modelString,
        TextureSlot.PARTICLE,
        TextureSlot.ALL
    ).create(block, texture, this.modelOutput)
    this.blockStateOutput.accept(
        MultiVariantGenerator.multiVariant(
            block,
            Variant.variant().with(VariantProperties.MODEL, model)
        ).with(BlockModelGenerators.createHorizontalFacingDispatch())
    )
}

fun BlockModelGenerators.registerChalice(chalice: Block) {
    this.createSimpleFlatItemModel(chalice.asItem())
    val texture = TextureMapping()
        .put(TextureSlot.PARTICLE, TextureMapping.getItemTexture(chalice.asItem()))
        .put(TextureSlot.ALL, TextureMapping.getBlockTexture(chalice))
    val templateChalice1 = block("parent/gilded_chalice", TextureSlot.PARTICLE, TextureSlot.ALL)
    val templateChalice2 = block("parent/gilded_chalices_2", TextureSlot.PARTICLE, TextureSlot.ALL)
    val templateChalice3 = block("parent/gilded_chalices_3", TextureSlot.PARTICLE, TextureSlot.ALL)
    val templateChalice4 = block("parent/gilded_chalices_4", TextureSlot.PARTICLE, TextureSlot.ALL)
    val chalices1 = templateChalice1.createWithSuffix(chalice, "_one_chalice", texture, this.modelOutput)
    val chalices2 = templateChalice2.createWithSuffix(chalice, "_two_chalices", texture, this.modelOutput)
    val chalices3 = templateChalice3.createWithSuffix(chalice, "_three_chalices", texture, this.modelOutput)
    val chalices4 = templateChalice4.createWithSuffix(chalice, "_four_chalices", texture, this.modelOutput)
    this.blockStateOutput.accept(
        MultiVariantGenerator.multiVariant(chalice).with(
            PropertyDispatch.property(GildedChaliceBlock.CHALICES)
                .select(1, Variant.variant().with(VariantProperties.MODEL, chalices1))
                .select(2, Variant.variant().with(VariantProperties.MODEL, chalices2))
                .select(3, Variant.variant().with(VariantProperties.MODEL, chalices3))
                .select(4, Variant.variant().with(VariantProperties.MODEL, chalices4))
        ).with(BlockModelGenerators.createHorizontalFacingDispatch())
    )
}

fun BlockModelGenerators.registerCoinStack(block: Block) {
    this.createSimpleFlatItemModel(block, "_top")
    val texture = TextureMapping()
        .put(TextureSlot.PARTICLE, TextureMapping.getBlockTexture(block, "_top"))
        .put(TextureSlot.TOP, TextureMapping.getBlockTexture(block, "_top"))
        .put(TextureSlot.SIDE, TextureMapping.getBlockTexture(block, "_side"))
    this.blockStateOutput.accept(
        MultiVariantGenerator.multiVariant(block).with(
            PropertyDispatch.property(BlockStateProperties.LAYERS)
                .generate { integer: Int ->
                    Variant.variant()
                        .with(
                            VariantProperties.MODEL,
                            block(
                                "parent/template_coin_stack_$integer",
                                TextureSlot.PARTICLE,
                                TextureSlot.TOP,
                                TextureSlot.SIDE
                            ).createWithSuffix(block, "_$integer", texture, this.modelOutput)
                        )
                })
            .with(BlockModelGenerators.createHorizontalFacingDispatch())
    )
}

fun BlockModelGenerators.registerGoldPileBlock(block: Block, sideTexture: ResourceLocation) {
    this.createSimpleFlatItemModel(block.asItem())
    val texture = TextureMapping()
        .put(TextureSlot.PARTICLE, TextureMapping.getItemTexture(block.asItem()))
        .put(TextureSlot.TOP, TextureMapping.getBlockTexture(block, "_top"))
        .put(TextureSlot.SIDE, sideTexture)
    this.blockStateOutput.accept(
        MultiVariantGenerator.multiVariant(block).with(
            PropertyDispatch.property(BlockStateProperties.LAYERS)
                .generate { integer: Int ->
                    Variant.variant()
                        .with(
                            VariantProperties.MODEL,
                            block(
                                "parent/layered_column_block_" + integer * 2,
                                TextureSlot.PARTICLE,
                                TextureSlot.TOP,
                                TextureSlot.SIDE
                            ).createWithSuffix(block, "_$integer", texture, this.modelOutput)
                        )
                })
    )
}

fun BlockModelGenerators.registerNethershroom(block: Block) {
    this.skipAutoItemBlock(block)
    this.createSimpleFlatItemModel(block)
    val texture = TextureMapping()
        .put(TextureSlot.PARTICLE, TextureMapping.getBlockTexture(block))
        .put(TextureSlot.CROSS, TextureMapping.getBlockTexture(block))
    val textureSquish = TextureMapping()
        .put(TextureSlot.PARTICLE, TextureMapping.getBlockTexture(block))
        .put(TextureSlot.CROSS, TextureMapping.getBlockTexture(block, "_squished"))
    val model = ModelTemplates.CROSS.create(
        block,
        texture,
        this.modelOutput
    )
    val modelSquished = ModelTemplates.CROSS.createWithSuffix(
        block,
        "_squished",
        textureSquish,
        this.modelOutput
    )
    this.blockStateOutput.accept(
        MultiVariantGenerator.multiVariant(block).with(
            BlockModelGenerators.createBooleanModelDispatch(
                NethershroomPlantBlock.SQUISHED,
                modelSquished,
                model
            )
        )
    )
}

fun BlockModelGenerators.registerNethershroomBlock(nethershroomBlock: Block) {
    val texture = ModelTemplates.SINGLE_FACE.create(
        nethershroomBlock, TextureMapping.defaultTexture(nethershroomBlock),
        this.modelOutput
    )
    val insideTexture = id("block/nethershroom_block_inside")
    this.blockStateOutput.accept(
        MultiPartGenerator.multiPart(nethershroomBlock).with(
            Condition.condition().term(BlockStateProperties.NORTH, true),
            Variant.variant().with(VariantProperties.MODEL, texture)
        ).with(
            Condition.condition().term(BlockStateProperties.EAST, true),
            Variant.variant().with(VariantProperties.MODEL, texture).with(VariantProperties.Y_ROT, Rotation.R90)
                .with(VariantProperties.UV_LOCK, true)
        ).with(
            Condition.condition().term(BlockStateProperties.SOUTH, true),
            Variant.variant().with(VariantProperties.MODEL, texture).with(VariantProperties.Y_ROT, Rotation.R180)
                .with(VariantProperties.UV_LOCK, true)
        ).with(
            Condition.condition().term(BlockStateProperties.WEST, true),
            Variant.variant().with(VariantProperties.MODEL, texture).with(VariantProperties.Y_ROT, Rotation.R270)
                .with(VariantProperties.UV_LOCK, true)
        ).with(
            Condition.condition().term(BlockStateProperties.UP, true),
            Variant.variant().with(VariantProperties.MODEL, texture).with(VariantProperties.X_ROT, Rotation.R270)
                .with(VariantProperties.UV_LOCK, true)
        ).with(
            Condition.condition().term(BlockStateProperties.DOWN, true),
            Variant.variant().with(VariantProperties.MODEL, texture).with(VariantProperties.X_ROT, Rotation.R90)
                .with(VariantProperties.UV_LOCK, true)
        ).with(
            Condition.condition().term(BlockStateProperties.NORTH, false),
            Variant.variant().with(VariantProperties.MODEL, insideTexture)
        ).with(
            Condition.condition().term(BlockStateProperties.EAST, false),
            Variant.variant().with(VariantProperties.MODEL, insideTexture).with(VariantProperties.Y_ROT, Rotation.R90)
                .with(VariantProperties.UV_LOCK, false)
        ).with(
            Condition.condition().term(BlockStateProperties.SOUTH, false),
            Variant.variant().with(VariantProperties.MODEL, insideTexture).with(VariantProperties.Y_ROT, Rotation.R180)
                .with(VariantProperties.UV_LOCK, false)
        ).with(
            Condition.condition().term(BlockStateProperties.WEST, false),
            Variant.variant().with(VariantProperties.MODEL, insideTexture).with(VariantProperties.Y_ROT, Rotation.R270)
                .with(VariantProperties.UV_LOCK, false)
        ).with(
            Condition.condition().term(BlockStateProperties.UP, false),
            Variant.variant().with(VariantProperties.MODEL, insideTexture).with(VariantProperties.X_ROT, Rotation.R270)
                .with(VariantProperties.UV_LOCK, false)
        ).with(
            Condition.condition().term(BlockStateProperties.DOWN, false),
            Variant.variant().with(VariantProperties.MODEL, insideTexture).with(VariantProperties.X_ROT, Rotation.R90)
                .with(VariantProperties.UV_LOCK, false)
        )
    )
    this.delegateItemModel(
        nethershroomBlock, TexturedModel.CUBE.createWithSuffix(
            nethershroomBlock, "_inventory",
            this.modelOutput
        )
    )
}

fun BlockModelGenerators.registerRibbon(block: Block) {
    this.skipAutoItemBlock(block)
    this.createSimpleFlatItemModel(block.asItem())
    this.blockStateOutput.accept(
        MultiVariantGenerator.multiVariant(block, *(getRibbonBlockStateVariants(block).toTypedArray()))
            .with(BlockModelGenerators.createRotatedPillar())
    )
}

fun BlockModelGenerators.getRibbonBlockStateVariants(block: Block): List<Variant> {
    return IntStream.range(1, 5).mapToObj { variant ->
        Variant.variant().with(
            VariantProperties.MODEL,
            this.makeRibbonModel(block, variant)
        )
    }.toList()
}

fun BlockModelGenerators.makeRibbonModel(block: Block, variant: Int): ResourceLocation {
    val texture = TextureMapping()
        .put(TextureSlot.PARTICLE, TextureMapping.getBlockTexture(block))
        .put(TextureSlot.ALL, TextureMapping.getBlockTexture(block))
    return block(
        "parent/ribbon_$variant",
        TextureSlot.PARTICLE,
        TextureSlot.ALL
    ).create(block.model("_$variant"), texture, this.modelOutput)
}

fun BlockModelGenerators.registerGeyser(block: Block) {
    val texture = TextureMapping()
        .put(TextureSlot.SIDE, TextureMapping.getBlockTexture(block, "_side"))
    val modelActive: ResourceLocation = ModelTemplates.CUBE_BOTTOM_TOP.create(
        block.model("_active"),
        texture
            .put(TextureSlot.TOP, TextureMapping.getBlockTexture(block, "_active"))
            .put(TextureSlot.BOTTOM, TextureMapping.getBlockTexture(block, "_inactive")),
        this.modelOutput
    )
    val modelInctive: ResourceLocation = ModelTemplates.CUBE_COLUMN.create(
        block.model("_inactive"),
        texture.put(TextureSlot.END, TextureMapping.getBlockTexture(block, "_inactive")),
        this.modelOutput
    )
    this.blockStateOutput.accept(
        MultiVariantGenerator.multiVariant(block).with(
            BlockModelGenerators.createBooleanModelDispatch(
                RoaringGeyserBlock.ACTIVE,
                modelActive,
                modelInctive
            )
        )
    )
}





fun parentedItemModel(id: ResourceLocation) = ModelTemplate(Optional.of(id.withPrefix("item/")), Optional.empty())
fun BlockModelGenerators.registerParentedItemModel(block: Block) =
    this.delegateItemModel(block, block.model())

//    fun BlockStateModelGenerator.parentedModel(block: Block, parent: Identifier): Identifier = this.parentedModel(block, block, parent)
fun block(parent: ResourceLocation, vararg requiredTextures: TextureSlot): ModelTemplate =
    ModelTemplate(Optional.of(parent), Optional.empty(), *requiredTextures)

fun block(parent: String, vararg requiredTextures: TextureSlot): ModelTemplate =
    ModelTemplate(Optional.of(id("block/$parent")), Optional.empty(), *requiredTextures)

fun block(parent: String, variant: String, vararg requiredTextures: TextureSlot): ModelTemplate {
    return ModelTemplate(Optional.of(id("block/$parent")), Optional.of(variant), *requiredTextures)
}

fun BlockModelGenerators.parentedModel(
    block: Block,
    textBlock: Block,
    parent: ResourceLocation
): ResourceLocation =
    ModelTemplate(parent.myb, Optional.empty(), ALL_KRY)
        .create(block.model(), TextureMapping().put(ALL_KRY, textBlock.model()), this.modelOutput)

fun BlockModelGenerators.parentedModel(
    block: ResourceLocation,
    textBlock: Block,
    parent: ResourceLocation
): ResourceLocation =
    ModelTemplate(parent.myb, Optional.empty(), ALL_KRY)
        .create(block, TextureMapping().put(ALL_KRY, textBlock.model()), this.modelOutput)


private
val <T : Any?> T.myb get() = Optional.ofNullable(this)

fun Block.model(str: String) = this.model().suffix(str)

fun ResourceLocation.suffix(str: String) = ResourceLocation(this.namespace, "${this.path}$str")
fun Block.model(): ResourceLocation = ModelLocationUtils.getModelLocation(this)