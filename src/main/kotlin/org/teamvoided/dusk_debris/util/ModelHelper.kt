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
import org.teamvoided.dusk_debris.block.FanBlock
import org.teamvoided.dusk_debris.block.GildedChaliceBlock
import org.teamvoided.dusk_debris.block.NethershroomPlantBlock
import org.teamvoided.dusk_debris.block.RoaringGeyserBlock
import org.teamvoided.dusk_debris.block.not_blocks.GodhomeBronzePhase
import java.util.*
import java.util.stream.IntStream


val ALL_KRY: TextureKey = TextureKey.of("all")

val OFFSET_WALL_POST = block("parent/offset_wall_post", "_post", TextureKey.WALL)
val OFFSET_WALL_INVENTORY = block("parent/offset_wall_inventory", "_inventory", TextureKey.WALL)
fun BlockStateModelGenerator.wallOffset(block: Block) = wallOffset(block, block.model())
fun BlockStateModelGenerator.wallOffset(block: Block, texture: Block) = wallOffset(block, texture.model())
fun BlockStateModelGenerator.wallOffset(wallBlock: Block, inId: Identifier) {
    val texture = Texture.texture(wallBlock.model()).put(TextureKey.WALL, inId)
    val id = OFFSET_WALL_POST.upload(wallBlock, texture, this.modelCollector)
    val id2 = Models.TEMPLATE_WALL_SIDE.upload(wallBlock, texture, this.modelCollector)
    val id3 = Models.TEMPLATE_WALL_SIDE_TALL.upload(wallBlock, texture, this.modelCollector)
    this.blockStateCollector.accept(BlockStateModelGenerator.createWallBlockState(wallBlock, id, id2, id3))
    this.registerParentedItemModel(wallBlock, OFFSET_WALL_INVENTORY.upload(wallBlock, texture, this.modelCollector))
}
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

fun BlockStateModelGenerator.godhomeShiftBlock(block: Block) {
    val textureSomber = Texture()
        .put(TextureKey.FRONT, Texture.getSubId(block, "_somber_front"))
        .put(TextureKey.TOP, Texture.getSubId(block, "_somber_top"))
        .put(TextureKey.SIDE, Texture.getSubId(block, "_somber_side"))
        .put(TextureKey.BACK, Texture.getSubId(block, "_somber_back"))
    val textureShining = Texture()
        .put(TextureKey.FRONT, Texture.getSubId(block, "_shining_front"))
        .put(TextureKey.TOP, Texture.getSubId(block, "_shining_top"))
        .put(TextureKey.SIDE, Texture.getSubId(block, "_shining_side"))
        .put(TextureKey.BACK, Texture.getSubId(block, "_shining_back"))
    val textureRadiant = Texture()
        .put(TextureKey.FRONT, Texture.getSubId(block, "_radiant_front"))
        .put(TextureKey.TOP, Texture.getSubId(block, "_radiant_top"))
        .put(TextureKey.SIDE, Texture.getSubId(block, "_radiant_side"))
        .put(TextureKey.BACK, Texture.getSubId(block, "_radiant_back"))
    val somberModel = block(
        "parent/front_top_side_back",
        TextureKey.FRONT,
        TextureKey.TOP,
        TextureKey.SIDE,
        TextureKey.BACK
    ).upload(block, "_somber", textureSomber, this.modelCollector)
    val shiningModel = block(
        "parent/front_top_side_back",
        TextureKey.FRONT,
        TextureKey.TOP,
        TextureKey.SIDE,
        TextureKey.BACK
    ).upload(block, "_shining", textureShining, this.modelCollector)
    val radiantModel = block(
        "parent/front_top_side_back",
        TextureKey.FRONT,
        TextureKey.TOP,
        TextureKey.SIDE,
        TextureKey.BACK
    ).upload(block, "_radiant", textureRadiant, this.modelCollector)
    this.blockStateCollector.accept(
        VariantsBlockStateSupplier.create(block)
            .coordinate(BlockStateVariantMap.create(
                Properties.ORIENTATION
            ).register { orientation: JigsawOrientation? ->
                this.addJigsawOrientationToVariant(
                    orientation,
                    BlockStateVariant.create()
                )
            }).coordinate(
                BlockStateVariantMap.create(GodhomeBronzePhase.GODHOME_BRONZE_PHASE)
                    .register(
                        GodhomeBronzePhase.SOMBER,
                        BlockStateVariant.create().put(VariantSettings.MODEL, somberModel)
                    )
                    .register(
                        GodhomeBronzePhase.SHINING,
                        BlockStateVariant.create().put(VariantSettings.MODEL, shiningModel)
                    )
                    .register(
                        GodhomeBronzePhase.RADIANT,
                        BlockStateVariant.create().put(VariantSettings.MODEL, radiantModel)
                    )
            )
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

fun BlockStateModelGenerator.registerDecorativeGoldBlock(block: Block, modelString: String, asItem: Boolean) {
    val texture: Texture
    if (asItem) {
        this.registerItemModel(block.asItem())
        texture = Texture()
            .put(TextureKey.PARTICLE, Texture.getId(block.asItem()))
            .put(TextureKey.ALL, Texture.getId(block))
    } else {
        this.registerParentedItemModel(block)
        texture = Texture()
            .put(TextureKey.PARTICLE, Texture.getId(block))
            .put(TextureKey.ALL, Texture.getId(block))
    }
    val model = block(
        modelString,
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

fun BlockStateModelGenerator.registerChalice(chalice: Block) {
    this.registerItemModel(chalice.asItem())
    val texture = Texture()
        .put(TextureKey.PARTICLE, Texture.getId(chalice.asItem()))
        .put(TextureKey.ALL, Texture.getId(chalice))
    val templateChalice1 = block("parent/gilded_chalice", TextureKey.PARTICLE, TextureKey.ALL)
    val templateChalice2 = block("parent/gilded_chalices_2", TextureKey.PARTICLE, TextureKey.ALL)
    val templateChalice3 = block("parent/gilded_chalices_3", TextureKey.PARTICLE, TextureKey.ALL)
    val templateChalice4 = block("parent/gilded_chalices_4", TextureKey.PARTICLE, TextureKey.ALL)
    val chalices1 = templateChalice1.upload(chalice, "_one_chalice", texture, this.modelCollector)
    val chalices2 = templateChalice2.upload(chalice, "_two_chalices", texture, this.modelCollector)
    val chalices3 = templateChalice3.upload(chalice, "_three_chalices", texture, this.modelCollector)
    val chalices4 = templateChalice4.upload(chalice, "_four_chalices", texture, this.modelCollector)
    this.blockStateCollector.accept(
        VariantsBlockStateSupplier.create(chalice).coordinate(
            BlockStateVariantMap.create(GildedChaliceBlock.CHALICES)
                .register(1, BlockStateVariant.create().put(VariantSettings.MODEL, chalices1))
                .register(2, BlockStateVariant.create().put(VariantSettings.MODEL, chalices2))
                .register(3, BlockStateVariant.create().put(VariantSettings.MODEL, chalices3))
                .register(4, BlockStateVariant.create().put(VariantSettings.MODEL, chalices4))
        ).coordinate(BlockStateModelGenerator.createNorthDefaultHorizontalRotationStates())
    )
}

fun BlockStateModelGenerator.registerCoinStack(block: Block) {
    this.registerItemModel(block, "_top")
    val texture = Texture()
        .put(TextureKey.PARTICLE, Texture.getSubId(block, "_top"))
        .put(TextureKey.TOP, Texture.getSubId(block, "_top"))
        .put(TextureKey.SIDE, Texture.getSubId(block, "_side"))
    this.blockStateCollector.accept(
        VariantsBlockStateSupplier.create(block).coordinate(
            BlockStateVariantMap.create(Properties.LAYERS)
                .register { integer: Int ->
                    BlockStateVariant.create()
                        .put(
                            VariantSettings.MODEL,
                            block(
                                "parent/template_coin_stack_$integer",
                                TextureKey.PARTICLE,
                                TextureKey.TOP,
                                TextureKey.SIDE
                            ).upload(block, "_$integer", texture, this.modelCollector)
                        )
                })
            .coordinate(BlockStateModelGenerator.createNorthDefaultHorizontalRotationStates())
    )
}

fun BlockStateModelGenerator.registerGoldPileBlock(block: Block, sideTexture: Identifier) {
    this.registerItemModel(block.asItem())
    val texture = Texture()
        .put(TextureKey.PARTICLE, Texture.getId(block.asItem()))
        .put(TextureKey.TOP, Texture.getSubId(block, "_top"))
        .put(TextureKey.SIDE, sideTexture)
    this.blockStateCollector.accept(
        VariantsBlockStateSupplier.create(block).coordinate(
            BlockStateVariantMap.create(Properties.LAYERS)
                .register { integer: Int ->
                    BlockStateVariant.create()
                        .put(
                            VariantSettings.MODEL,
                            block(
                                "parent/layered_column_block_" + integer * 2,
                                TextureKey.PARTICLE,
                                TextureKey.TOP,
                                TextureKey.SIDE
                            ).upload(block, "_$integer", texture, this.modelCollector)
                        )
                })
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
        VariantsBlockStateSupplier.create(block, *(getRibbonBlockStateVariants(block).toTypedArray()))
            .coordinate(BlockStateModelGenerator.createAxisRotatedVariantMap())
    )
}

fun BlockStateModelGenerator.getRibbonBlockStateVariants(block: Block): List<BlockStateVariant> {
    return IntStream.range(1, 5).mapToObj { variant ->
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
    ).upload(block.model("_$variant"), texture, this.modelCollector)
}

fun BlockStateModelGenerator.registerGeyser(block: Block) {
    this.registerItemModel(block.asItem())
    val texture = Texture()
        .put(TextureKey.SIDE, Texture.getSubId(block, "_side"))

    val modelActive: Identifier = Models.CUBE_BOTTOM_TOP.upload(
        block.model("_active"),
        texture
            .put(TextureKey.TOP, Texture.getSubId(block, "_active"))
            .put(TextureKey.BOTTOM, Texture.getSubId(block, "_inactive")),
        this.modelCollector
    )
    val modelInctive: Identifier = Models.CUBE_COLUMN.upload(
        block.model("_inactive"),
        texture.put(TextureKey.END, Texture.getSubId(block, "_inactive")),
        this.modelCollector
    )
    this.blockStateCollector.accept(
        VariantsBlockStateSupplier.create(block).coordinate(
            BlockStateModelGenerator.createBooleanModelMap(
                RoaringGeyserBlock.ACTIVE,
                modelActive,
                modelInctive
            )
        )
    )
}


fun BlockStateModelGenerator.registerCopperFan(fan: Block, waxedFan: Block? = null) {
    val default: Identifier = Models.CUBE_BOTTOM_TOP.upload(fan, Texture.sideTopBottom(fan), this.modelCollector)
    val power: Identifier =
        this.createSubModel(fan, "_powered", Models.CUBE_BOTTOM_TOP, ::copperFan) // this is the bulb way
    val active: Identifier =
        Models.CUBE_BOTTOM_TOP.upload(fan, "_active", copperFan(fan, "_active"), this.modelCollector)
    val activePower: Identifier =
        Models.CUBE_BOTTOM_TOP.upload(
            fan,
            "_active_powered",
            copperFan(fan, "_active", "_powered"),
            this.modelCollector
        )
    this.blockStateCollector.accept(
        this.createCopperFanBlockState(
            fan,
            default,
            active,
            power,
            activePower
        )
    )
    if (waxedFan != null) {
        this.registerParentedItemModel(waxedFan, ModelIds.getItemModelId(fan.asItem()))
        this.blockStateCollector.accept(
            this.createCopperFanBlockState(
                waxedFan,
                default,
                active,
                power,
                activePower
            )
        )
    }
}

fun copperFan(id: Identifier): Texture {
    return Texture()
        .put(TextureKey.TOP, id.suffix("_top"))
        .put(TextureKey.SIDE, id.suffix("_side"))
        .put(TextureKey.BOTTOM, id.suffix("_bottom"))
}

fun copperFan(block: Block, suffix: String, powered: String = ""): Texture {
    return Texture()
        .put(TextureKey.TOP, block.model(suffix + powered + "_top"))
        .put(TextureKey.SIDE, block.model(powered + "_side"))
        .put(TextureKey.BOTTOM, block.model(powered + "_bottom"))
}

fun BlockStateModelGenerator.createCopperFanBlockState(
    block: Block,
    base: Identifier,
    active: Identifier,
    powered: Identifier,
    activeAndPowered: Identifier
): BlockStateSupplier {
    return VariantsBlockStateSupplier.create(block).coordinate(
        BlockStateVariantMap.create(FanBlock.ACTIVE, Properties.POWERED)
            .register { activex: Boolean, poweredx: Boolean ->
                if (activex) BlockStateVariant.create()
                    .put(VariantSettings.MODEL, if (poweredx) activeAndPowered else active)
                else BlockStateVariant.create()
                    .put(VariantSettings.MODEL, if (poweredx) powered else base)
            }).coordinate(this.createUpDefaultFacingVariantMap())
}


fun parentedItemModel(id: Identifier) = Model(Optional.of(id.withPrefix("item/")), Optional.empty())
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

fun Block.model(str: String) = this.model().suffix(str)

fun Identifier.suffix(str: String) = Identifier(this.namespace, "${this.path}$str")
fun Block.model(): Identifier = ModelIds.getBlockModelId(this)