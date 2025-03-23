package org.teamvoided.dusk_debris.util

import net.minecraft.block.Block
import net.minecraft.block.Blocks
import net.minecraft.block.enums.Attachment
import net.minecraft.block.enums.DoubleBlockHalf
import net.minecraft.data.client.model.*
import net.minecraft.data.client.model.BlockStateModelGenerator.TintType
import net.minecraft.data.client.model.TextureKey.*
import net.minecraft.data.client.model.VariantSettings.Rotation
import net.minecraft.state.property.Properties
import net.minecraft.util.Identifier
import net.minecraft.util.math.Direction
import org.teamvoided.dusk_debris.DuskDebris.id
import org.teamvoided.dusk_debris.block.PaintedRoseBlock
import org.teamvoided.dusk_debris.block.SpiderlilyBlock
import org.teamvoided.dusk_debris.block.TripleBlockSection

// TODO clean up
val BAR: TextureKey = of("bar")
val POST: TextureKey = of("post")
val RABBIT: TextureKey = of("rabbit")

val modelDirectionRotation = listOf(
    (Direction.NORTH to Rotation.R0),
    (Direction.EAST to Rotation.R90),
    (Direction.SOUTH to Rotation.R180),
    (Direction.WEST to Rotation.R270)
)

fun BlockStateModelGenerator.registerGalleryRose(block: Block, tintType: TintType) {
    this.registerItemModel(block, "_top")
    val top = this.createSubModel(block, "_top", tintType.crossModel, Texture::cross)
    val middle = this.createSubModel(block, "_middle", tintType.crossModel, Texture::cross)
    val bottom = this.createSubModel(block, "_bottom", tintType.crossModel, Texture::cross)
    blockStateCollector.accept(
        VariantsBlockStateSupplier.create(block).coordinate(
            BlockStateVariantMap.create(
                PaintedRoseBlock.SECTION
            ).register(
                TripleBlockSection.TOP,
                BlockStateVariant.create().put(VariantSettings.MODEL, top)
            ).register(
                TripleBlockSection.MIDDLE,
                BlockStateVariant.create().put(VariantSettings.MODEL, middle)
            ).register(
                TripleBlockSection.BOTTOM,
                BlockStateVariant.create().put(VariantSettings.MODEL, bottom)
            )
        )
    )
}

fun BlockStateModelGenerator.registerBigChain(block: Block) {
    this.registerItemModel(block.asItem())
    val texture = Texture().put(PARTICLE, Texture.getId(block)).put(ALL, Texture.getId(block))
    val model = block("parent/big_chain", PARTICLE, ALL).upload(block, texture, this.modelCollector)
    this.registerAxisRotated(block, model)
}

fun BlockStateModelGenerator.registerBigLantern(
    block: Block, bottom: Identifier = id("block/big_lantern_bottom")
) {
    this.registerItemModel(block)
    val texture = Texture()
        .put(PARTICLE, Texture.getId(block))
        .put(SIDE, Texture.getId(block))
        .put(END, bottom)
    val model = block(
        "parent/big_lantern",
        PARTICLE,
        SIDE,
        END
    )
    val modelHanging = model.upload(block, "_hanging", texture, this.modelCollector)
    this.blockStateCollector.accept(
        VariantsBlockStateSupplier.create(block).coordinate(
            BlockStateVariantMap.create(Properties.HANGING)
                .register(
                    false, BlockStateVariant.create()
                        .put(VariantSettings.MODEL, model.upload(block, texture, this.modelCollector))
                )
                .register(
                    true, BlockStateVariant.create()
                        .put(VariantSettings.X, Rotation.R180)
                        .put(VariantSettings.MODEL, modelHanging)
                )
        )
    )
}



fun BlockStateModelGenerator.registerBell(
    block: Block,
    bar: Identifier = Texture.getId(Blocks.DARK_OAK_PLANKS),
    post: Identifier = Texture.getId(Blocks.STONE)
) {
    this.registerItemModel(block.asItem())
    val variants = BlockStateVariantMap.create(Properties.HORIZONTAL_FACING, Properties.ATTACHMENT)
    val texture1 = Texture()
        .put(PARTICLE, Texture.getId(block))
        .put(BAR, bar)
    val texture2 = texture1.put(POST, post)

    Properties.ATTACHMENT.values.forEach { attachment ->
        val attach = attachment.toString().lowercase()
        if (attachment == Attachment.FLOOR) {
            block("parent/bell_$attach", PARTICLE, BAR, POST)
                .upload(block, "_$attach", texture2, this.modelCollector)
        } else {
            block("parent/bell_$attach", PARTICLE, BAR)
                .upload(block, "_$attach", texture1, this.modelCollector)
        }
        Properties.HORIZONTAL_FACING.values.forEach { direction ->
            val variant = BlockStateVariant.create()
                .put(VariantSettings.MODEL, ModelIds.getBlockSubModelId(block, "_$attach"))
            val variant2 = when (direction) {
                Direction.EAST -> variant.put(VariantSettings.Y, Rotation.R90)
                Direction.SOUTH -> variant.put(VariantSettings.Y, Rotation.R180)
                Direction.WEST -> variant.put(VariantSettings.Y, Rotation.R270)
                else -> variant
            }
            variants.register(
                direction, attachment,
                variant2
            )
        }
    }
    this.blockStateCollector.accept(
        VariantsBlockStateSupplier.create(block).coordinate(
            variants
        )
    )
}
fun BlockStateModelGenerator.registerTallCrystal(block: Block) {
    this.registerItemModel(block, "_top")
    val model = Models.CROSS
    val lowerHalfModelId: Identifier = this.createSubModel(block, "_top", model, Texture::cross)
    val upperHalfModelId: Identifier = this.createSubModel(block, "_bottom", model, Texture::cross)
    this.blockStateCollector.accept(
        VariantsBlockStateSupplier.create(block).coordinate(
            BlockStateVariantMap.create(Properties.DOUBLE_BLOCK_HALF)
                .register(
                    DoubleBlockHalf.LOWER,
                    BlockStateVariant.create().put(VariantSettings.MODEL, lowerHalfModelId)
                )
                .register(
                    DoubleBlockHalf.UPPER,
                    BlockStateVariant.create().put(VariantSettings.MODEL, upperHalfModelId)
                )
        ).coordinate(this.createUpDefaultFacingVariantMap())
    )
}

fun BlockStateModelGenerator.registerWaterFern(block: Block) {
//    this.registerItemModel(block.asItem())
    val texture = Texture()
        .put(PARTICLE, Texture.getId(block))
        .put(TOP, Texture.getSubId(block, "_roots"))
        .put(PLANT, Texture.getSubId(block, "_roots"))
    val model = block(
        "parent/water_plant",
        PARTICLE,
        TOP,
        PLANT
    ).upload(block, texture, this.modelCollector)
    this.registerAxisRotated(block, model)
}

fun BlockStateModelGenerator.registerBunnyGrave(
    block: Block, referenceTexture: Block, referenceTexture2: Block
) {
    val model = MultipartBlockStateSupplier.create(block)
    val textureBunny: Texture = Texture()
        .put(RABBIT, Texture.getId(block))
    val texturePlate: Texture = Texture()
        .put(DOWN, Texture.getId(referenceTexture))
        .put(FRONT, Texture.getId(referenceTexture2))
    val bunnyModel = block("parent/bunny_grave", RABBIT)
        .upload(block, textureBunny, this.modelCollector)
    val plateModel = block("parent/bunny_grave_base", DOWN, FRONT)
        .upload(block, "_plate", texturePlate, this.modelCollector)
    modelDirectionRotation.forEach { (direction, rotation) ->
        model.with(
            When.create().set(Properties.HORIZONTAL_FACING, direction),
            BlockStateVariant.create()
                .put(VariantSettings.MODEL, plateModel)
                .put(VariantSettings.Y, rotation)
        ).with(
            When.create().set(Properties.HORIZONTAL_FACING, direction),
            BlockStateVariant.create()
                .put(VariantSettings.MODEL, bunnyModel)
                .put(VariantSettings.Y, rotation)
        )
    }
    this.blockStateCollector.accept(model)
}

fun BlockStateModelGenerator.genPsudoFamily(stairs: Block, slab: Block, wall: Block, texture: Block) {
//    this.stairs(stairs, texture)
//    this.slab(slab, texture)
//    this.wall(wall, texture)
}

fun BlockStateModelGenerator.genPsudoFamily(stairs: Block, slab: Block, wall: Block, texture: Block, fullSlab: Block) {
    println("This function is a lie and it doesnt generate anything you have been tricked ")
    println("im not porting all of the stuff rn wait for voidlib")
//    this.stairs(stairs, texture)
//    this.slab(slab, texture, fullSlab)
//    this.wall(wall, texture)
}

fun BlockStateModelGenerator.registerSpiderlilly(doubleBlock: Block, tintType: TintType) {
    this.registerItemModel(doubleBlock, "_top")
    val top: Identifier = this.createSubModel(doubleBlock, "_top", tintType.crossModel, Texture::cross)
    val bottom: Identifier = this.createSubModel(doubleBlock, "_bottom", tintType.crossModel, Texture::cross)
    val topFalse: Identifier = this.createSubModel(doubleBlock, "_top_false", tintType.crossModel, Texture::cross)
    val bottomFalse: Identifier = this.createSubModel(doubleBlock, "_bottom_false", tintType.crossModel, Texture::cross)
    this.blockStateCollector.accept(
        VariantsBlockStateSupplier.create(doubleBlock).coordinate(
            BlockStateVariantMap.create(Properties.DOUBLE_BLOCK_HALF, SpiderlilyBlock.FLOWERING)
                .register(
                    DoubleBlockHalf.LOWER, true,
                    BlockStateVariant.create().put(VariantSettings.MODEL, bottom)
                )
                .register(
                    DoubleBlockHalf.UPPER, true,
                    BlockStateVariant.create().put(VariantSettings.MODEL, top)
                )
                .register(
                    DoubleBlockHalf.LOWER, false,
                    BlockStateVariant.create().put(VariantSettings.MODEL, bottomFalse)
                )
                .register(
                    DoubleBlockHalf.UPPER, false,
                    BlockStateVariant.create().put(VariantSettings.MODEL, topFalse)
                )
        )
    )
}
