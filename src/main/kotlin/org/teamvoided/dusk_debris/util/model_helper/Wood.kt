package org.teamvoided.dusk_debris.util.model_helper

import net.minecraft.core.Direction
import net.minecraft.data.models.BlockModelGenerators
import net.minecraft.data.models.blockstates.MultiVariantGenerator
import net.minecraft.data.models.blockstates.PropertyDispatch
import net.minecraft.data.models.blockstates.Variant
import net.minecraft.data.models.blockstates.VariantProperties
import net.minecraft.data.models.model.TextureMapping
import net.minecraft.data.models.model.TextureSlot
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import org.teamvoided.dusk_debris.DuskDebris.mc
import org.teamvoided.dusk_debris.util.block
import org.teamvoided.dusk_debris.util.model


val WOOD: TextureSlot = TextureSlot.create("wood")
val PLANKS: TextureSlot = TextureSlot.create("planks")
val PLANT: TextureSlot = TextureSlot.create("plant")
val STEM: TextureSlot = TextureSlot.create("stem")


fun BlockModelGenerators.registerOvergrowthBush(block: Block) {
    val texture = TextureMapping()
        .put(TextureSlot.TOP, TextureMapping.getBlockTexture(block, "_top"))
        .put(TextureSlot.SIDE, TextureMapping.getBlockTexture(block, "_side"))
        .put(TextureSlot.PLANT, TextureMapping.getBlockTexture(block, "_plant"))
        .put(TextureSlot.STEM, TextureMapping.getBlockTexture(block, "_plant_stem"))
    val identifier =
        block("parent/foliage/template_tinted_bush", TextureSlot.TOP, TextureSlot.SIDE, TextureSlot.PLANT, TextureSlot.STEM)
            .create(block, texture, this.modelOutput)
    this.blockStateOutput.accept(
        MultiVariantGenerator.multiVariant(block, Variant.variant().with(VariantProperties.MODEL, identifier))
            .with(createDownDefaultRotationStates())
    )
}

fun createDownDefaultRotationStates(): PropertyDispatch {
    return PropertyDispatch.property(BlockStateProperties.FACING)
        .select(Direction.DOWN, Variant.variant())
        .select(Direction.UP, Variant.variant().with(VariantProperties.X_ROT, VariantProperties.Rotation.R180))
        .select(
            Direction.NORTH, Variant.variant()
                .with(VariantProperties.X_ROT, VariantProperties.Rotation.R90)
                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180)
        )
        .select(Direction.SOUTH, Variant.variant().with(VariantProperties.X_ROT, VariantProperties.Rotation.R90))
        .select(
            Direction.WEST, Variant.variant()
                .with(VariantProperties.X_ROT, VariantProperties.Rotation.R90)
                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90)
        )
        .select(
            Direction.EAST, Variant.variant()
                .with(VariantProperties.X_ROT, VariantProperties.Rotation.R90)
                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270)
        )
}

fun BlockModelGenerators.strongScaffolding(block: Block) {
    val texture: TextureMapping = TextureMapping()
        .put(TextureSlot.TOP, block.model("_top"))
        .put(TextureSlot.SIDE, block.model("_side"))
        .put(TextureSlot.BOTTOM, block.model("_bottom"))

    val stable: ResourceLocation =
        block("parent/strong_stable_scaffolding", "_stable", TextureSlot.TOP, TextureSlot.SIDE, TextureSlot.BOTTOM)
            .create(block, texture, this.modelOutput)
    val unstable: ResourceLocation =
        block("parent/strong_unstable_scaffolding", "_unstable", TextureSlot.TOP, TextureSlot.SIDE, TextureSlot.BOTTOM)
            .create(block, texture, this.modelOutput)

    this.delegateItemModel(block, stable)
    this.blockStateOutput.accept(
        MultiVariantGenerator.multiVariant(block)
            .with(BlockModelGenerators.createBooleanModelDispatch(BlockStateProperties.BOTTOM, unstable, stable))
    )
}

fun BlockModelGenerators.pixelAccurateSign(sign: Block, wallSign: Block, planks: ResourceLocation, log: ResourceLocation) {
    this.pixelAccurateSign(sign, planks, log)
    this.pixelAccurateWallSign(wallSign, planks)
}

fun BlockModelGenerators.pixelAccurateSign(sign: Block, wallSign: Block, planks: Block, log: Block) {
    this.pixelAccurateSign(sign, wallSign, planks.model(), log.model())
}

fun BlockModelGenerators.pixelAccurateSign(sign: Block, planks: ResourceLocation, log: ResourceLocation) {
    val texture: TextureMapping = TextureMapping()
        .put(PLANKS, planks)
        .put(WOOD, log)
    val default: ResourceLocation =
        block(mc("block/parent/sign_0"), PLANKS, WOOD).create(sign.model("_0"), texture, this.modelOutput)
    val rotate225: ResourceLocation =
        block(mc("block/parent/sign_1"), PLANKS, WOOD).create(sign.model("_1"), texture, this.modelOutput)
    val rotate45: ResourceLocation =
        block(mc("block/parent/sign_2"), PLANKS, WOOD).create(sign.model("_2"), texture, this.modelOutput)
    val rotate675: ResourceLocation =
        block(mc("block/parent/sign_3"), PLANKS, WOOD).create(sign.model("_3"), texture, this.modelOutput)

    this.blockStateOutput.accept(
        MultiVariantGenerator.multiVariant(sign)
            .with(create16RotationStates(default, rotate225, rotate45, rotate675))
    )
}

fun BlockModelGenerators.pixelAccurateWallSign(wallSign: Block, planks: ResourceLocation) {
    val texture: TextureMapping = TextureMapping()
        .put(PLANKS, planks)
    block(mc("block/parent/wall_sign"), PLANKS).create(wallSign, texture, this.modelOutput)
    this.createNonTemplateHorizontalBlock(wallSign)
}

fun create16RotationStates(
    model0: ResourceLocation,
    model1: ResourceLocation,
    model2: ResourceLocation,
    model3: ResourceLocation
): PropertyDispatch {
    val variants = PropertyDispatch.property(BlockStateProperties.ROTATION_16)
    listOf(
        VariantProperties.Rotation.R180,
        VariantProperties.Rotation.R270,
        VariantProperties.Rotation.R0,
        VariantProperties.Rotation.R90
    ).forEachIndexed { idx, rot ->
        variants.select(idx * 4, putRot(Variant.variant().with(VariantProperties.MODEL, model0), rot))
        variants.select(idx * 4 + 1, putRot(Variant.variant().with(VariantProperties.MODEL, model1), rot))
        variants.select(idx * 4 + 2, putRot(Variant.variant().with(VariantProperties.MODEL, model2), rot))
        variants.select(idx * 4 + 3, putRot(Variant.variant().with(VariantProperties.MODEL, model3), rot))
    }
    return variants
}


private fun putRot(
    variant: Variant,
    value: VariantProperties.Rotation
): Variant {
    return if (value.ordinal == 0)
        variant
    else
        variant.with(VariantProperties.Y_ROT, value)
}