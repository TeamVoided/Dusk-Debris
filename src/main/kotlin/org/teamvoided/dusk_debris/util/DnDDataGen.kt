package org.teamvoided.dusk_debris.util

import net.minecraft.core.Direction
import net.minecraft.data.models.BlockModelGenerators
import net.minecraft.data.models.BlockModelGenerators.TintState
import net.minecraft.data.models.blockstates.*
import net.minecraft.data.models.blockstates.VariantProperties.Rotation
import net.minecraft.data.models.model.ModelLocationUtils
import net.minecraft.data.models.model.ModelTemplates
import net.minecraft.data.models.model.TextureMapping
import net.minecraft.data.models.model.TextureSlot
import net.minecraft.data.models.model.TextureSlot.*
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.properties.BellAttachType
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf
import org.teamvoided.dusk_debris.DuskDebris.id
import org.teamvoided.dusk_debris.block.PaintedRoseBlock
import org.teamvoided.dusk_debris.block.SpiderlilyBlock
import org.teamvoided.dusk_debris.block.TripleBlockSection

// TODO clean up
val BAR: TextureSlot = create("bar")
val POST: TextureSlot = create("post")
val RABBIT: TextureSlot = create("rabbit")

val modelDirectionRotation = listOf(
    (Direction.NORTH to Rotation.R0),
    (Direction.EAST to Rotation.R90),
    (Direction.SOUTH to Rotation.R180),
    (Direction.WEST to Rotation.R270)
)

fun BlockModelGenerators.registerGalleryRose(block: Block, tintType: TintState) {
    this.createSimpleFlatItemModel(block, "_top")
    val top = this.createSuffixedVariant(block, "_top", tintType.cross, TextureMapping::cross)
    val middle = this.createSuffixedVariant(block, "_middle", tintType.cross, TextureMapping::cross)
    val bottom = this.createSuffixedVariant(block, "_bottom", tintType.cross, TextureMapping::cross)
    blockStateOutput.accept(
        MultiVariantGenerator.multiVariant(block).with(
            PropertyDispatch.property(
                PaintedRoseBlock.SECTION
            ).select(
                TripleBlockSection.TOP,
                Variant.variant().with(VariantProperties.MODEL, top)
            ).select(
                TripleBlockSection.MIDDLE,
                Variant.variant().with(VariantProperties.MODEL, middle)
            ).select(
                TripleBlockSection.BOTTOM,
                Variant.variant().with(VariantProperties.MODEL, bottom)
            )
        )
    )
}

fun BlockModelGenerators.registerBigChain(block: Block) {
    this.createSimpleFlatItemModel(block.asItem())
    val texture = TextureMapping().put(PARTICLE, TextureMapping.getBlockTexture(block)).put(ALL, TextureMapping.getBlockTexture(block))
    val model = block("parent/big_chain", PARTICLE, ALL).create(block, texture, this.modelOutput)
    this.createAxisAlignedPillarBlockCustomModel(block, model)
}

fun BlockModelGenerators.registerBigLantern(
    block: Block, bottom: ResourceLocation = id("block/big_lantern_bottom")
) {
    this.createSimpleFlatItemModel(block)
    val texture = TextureMapping()
        .put(PARTICLE, TextureMapping.getBlockTexture(block))
        .put(SIDE, TextureMapping.getBlockTexture(block))
        .put(END, bottom)
    val model = block(
        "parent/big_lantern",
        PARTICLE,
        SIDE,
        END
    )
    val modelHanging = model.createWithSuffix(block, "_hanging", texture, this.modelOutput)
    this.blockStateOutput.accept(
        MultiVariantGenerator.multiVariant(block).with(
            PropertyDispatch.property(BlockStateProperties.HANGING)
                .select(
                    false, Variant.variant()
                        .with(VariantProperties.MODEL, model.create(block, texture, this.modelOutput))
                )
                .select(
                    true, Variant.variant()
                        .with(VariantProperties.X_ROT, Rotation.R180)
                        .with(VariantProperties.MODEL, modelHanging)
                )
        )
    )
}



fun BlockModelGenerators.registerBell(
    block: Block,
    bar: ResourceLocation = TextureMapping.getBlockTexture(Blocks.DARK_OAK_PLANKS),
    post: ResourceLocation = TextureMapping.getBlockTexture(Blocks.STONE)
) {
    this.createSimpleFlatItemModel(block.asItem())
    val variants = PropertyDispatch.properties(BlockStateProperties.HORIZONTAL_FACING, BlockStateProperties.BELL_ATTACHMENT)
    val texture1 = TextureMapping()
        .put(PARTICLE, TextureMapping.getBlockTexture(block))
        .put(BAR, bar)
    val texture2 = texture1.put(POST, post)

    BlockStateProperties.BELL_ATTACHMENT.possibleValues.forEach { attachment ->
        val attach = attachment.toString().lowercase()
        if (attachment == BellAttachType.FLOOR) {
            block("parent/bell_$attach", PARTICLE, BAR, POST)
                .createWithSuffix(block, "_$attach", texture2, this.modelOutput)
        } else {
            block("parent/bell_$attach", PARTICLE, BAR)
                .createWithSuffix(block, "_$attach", texture1, this.modelOutput)
        }
        BlockStateProperties.HORIZONTAL_FACING.possibleValues.forEach { direction ->
            val variant = Variant.variant()
                .with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(block, "_$attach"))
            val variant2 = when (direction) {
                Direction.EAST -> variant.with(VariantProperties.Y_ROT, Rotation.R90)
                Direction.SOUTH -> variant.with(VariantProperties.Y_ROT, Rotation.R180)
                Direction.WEST -> variant.with(VariantProperties.Y_ROT, Rotation.R270)
                else -> variant
            }
            variants.select(
                direction, attachment,
                variant2
            )
        }
    }
    this.blockStateOutput.accept(
        MultiVariantGenerator.multiVariant(block).with(
            variants
        )
    )
}
fun BlockModelGenerators.registerTallCrystal(block: Block) {
    this.createSimpleFlatItemModel(block, "_top")
    val model = ModelTemplates.CROSS
    val lowerHalfModelId: ResourceLocation = this.createSuffixedVariant(block, "_top", model, TextureMapping::cross)
    val upperHalfModelId: ResourceLocation = this.createSuffixedVariant(block, "_bottom", model, TextureMapping::cross)
    this.blockStateOutput.accept(
        MultiVariantGenerator.multiVariant(block).with(
            PropertyDispatch.property(BlockStateProperties.DOUBLE_BLOCK_HALF)
                .select(
                    DoubleBlockHalf.LOWER,
                    Variant.variant().with(VariantProperties.MODEL, lowerHalfModelId)
                )
                .select(
                    DoubleBlockHalf.UPPER,
                    Variant.variant().with(VariantProperties.MODEL, upperHalfModelId)
                )
        ).with(this.createColumnWithFacing())
    )
}

fun BlockModelGenerators.registerWaterFern(block: Block) {
//    this.registerItemModel(block.asItem())
    val texture = TextureMapping()
        .put(PARTICLE, TextureMapping.getBlockTexture(block))
        .put(TOP, TextureMapping.getBlockTexture(block, "_roots"))
        .put(PLANT, TextureMapping.getBlockTexture(block, "_roots"))
    val model = block(
        "parent/water_plant",
        PARTICLE,
        TOP,
        PLANT
    ).create(block, texture, this.modelOutput)
    this.createAxisAlignedPillarBlockCustomModel(block, model)
}

fun BlockModelGenerators.registerBunnyGrave(
    block: Block, referenceTexture: Block, referenceTexture2: Block
) {
    val model = MultiPartGenerator.multiPart(block)
    val textureBunny: TextureMapping = TextureMapping()
        .put(RABBIT, TextureMapping.getBlockTexture(block))
    val texturePlate: TextureMapping = TextureMapping()
        .put(DOWN, TextureMapping.getBlockTexture(referenceTexture))
        .put(FRONT, TextureMapping.getBlockTexture(referenceTexture2))
    val bunnyModel = block("parent/bunny_grave", RABBIT)
        .create(block, textureBunny, this.modelOutput)
    val plateModel = block("parent/bunny_grave_base", DOWN, FRONT)
        .createWithSuffix(block, "_plate", texturePlate, this.modelOutput)
    modelDirectionRotation.forEach { (direction, rotation) ->
        model.with(
            Condition.condition().term(BlockStateProperties.HORIZONTAL_FACING, direction),
            Variant.variant()
                .with(VariantProperties.MODEL, plateModel)
                .with(VariantProperties.Y_ROT, rotation)
        ).with(
            Condition.condition().term(BlockStateProperties.HORIZONTAL_FACING, direction),
            Variant.variant()
                .with(VariantProperties.MODEL, bunnyModel)
                .with(VariantProperties.Y_ROT, rotation)
        )
    }
    this.blockStateOutput.accept(model)
}

fun BlockModelGenerators.genPsudoFamily(stairs: Block, slab: Block, wall: Block, texture: Block) {
//    this.stairs(stairs, texture)
//    this.slab(slab, texture)
//    this.wall(wall, texture)
}

fun BlockModelGenerators.genPsudoFamily(stairs: Block, slab: Block, wall: Block, texture: Block, fullSlab: Block) {
    println("This function is a lie and it doesnt generate anything you have been tricked ")
    println("im not porting all of the stuff rn wait for voidlib")
//    this.stairs(stairs, texture)
//    this.slab(slab, texture, fullSlab)
//    this.wall(wall, texture)
}

fun BlockModelGenerators.registerSpiderlilly(doubleBlock: Block, tintType: TintState) {
    this.createSimpleFlatItemModel(doubleBlock, "_top")
    val top: ResourceLocation = this.createSuffixedVariant(doubleBlock, "_top", tintType.cross, TextureMapping::cross)
    val bottom: ResourceLocation = this.createSuffixedVariant(doubleBlock, "_bottom", tintType.cross, TextureMapping::cross)
    val topFalse: ResourceLocation = this.createSuffixedVariant(doubleBlock, "_top_false", tintType.cross, TextureMapping::cross)
    val bottomFalse: ResourceLocation = this.createSuffixedVariant(doubleBlock, "_bottom_false", tintType.cross, TextureMapping::cross)
    this.blockStateOutput.accept(
        MultiVariantGenerator.multiVariant(doubleBlock).with(
            PropertyDispatch.properties(BlockStateProperties.DOUBLE_BLOCK_HALF, SpiderlilyBlock.FLOWERING)
                .select(
                    DoubleBlockHalf.LOWER, true,
                    Variant.variant().with(VariantProperties.MODEL, bottom)
                )
                .select(
                    DoubleBlockHalf.UPPER, true,
                    Variant.variant().with(VariantProperties.MODEL, top)
                )
                .select(
                    DoubleBlockHalf.LOWER, false,
                    Variant.variant().with(VariantProperties.MODEL, bottomFalse)
                )
                .select(
                    DoubleBlockHalf.UPPER, false,
                    Variant.variant().with(VariantProperties.MODEL, topFalse)
                )
        )
    )
}
