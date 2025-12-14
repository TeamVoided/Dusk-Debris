package org.teamvoided.dusk_debris.util.model_helper

import net.minecraft.block.Block
import net.minecraft.data.client.model.*
import net.minecraft.state.property.Properties
import net.minecraft.util.Identifier
import net.minecraft.util.math.Direction
import org.teamvoided.dusk_debris.DuskDebris.mc
import org.teamvoided.dusk_debris.util.block
import org.teamvoided.dusk_debris.util.model


val WOOD: TextureKey = TextureKey.of("wood")
val PLANKS: TextureKey = TextureKey.of("planks")
val PLANT: TextureKey = TextureKey.of("plant")
val STEM: TextureKey = TextureKey.of("stem")


fun BlockStateModelGenerator.registerOvergrowthBush(block: Block) {
    val texture = Texture()
        .put(TextureKey.TOP, Texture.getSubId(block, "_top"))
        .put(TextureKey.SIDE, Texture.getSubId(block, "_side"))
        .put(TextureKey.PLANT, Texture.getSubId(block, "_plant"))
        .put(TextureKey.STEM, Texture.getSubId(block, "_plant_stem"))
    val identifier =
        block("parent/foliage/template_tinted_bush", TextureKey.TOP, TextureKey.SIDE, TextureKey.PLANT, TextureKey.STEM)
            .upload(block, texture, this.modelCollector)
    this.blockStateCollector.accept(
        VariantsBlockStateSupplier.create(block, BlockStateVariant.create().put(VariantSettings.MODEL, identifier))
            .coordinate(createDownDefaultRotationStates())
    )
}

fun createDownDefaultRotationStates(): BlockStateVariantMap {
    return BlockStateVariantMap.create(Properties.FACING)
        .register(Direction.DOWN, BlockStateVariant.create())
        .register(Direction.UP, BlockStateVariant.create().put(VariantSettings.X, VariantSettings.Rotation.R180))
        .register(
            Direction.NORTH, BlockStateVariant.create()
                .put(VariantSettings.X, VariantSettings.Rotation.R90)
                .put(VariantSettings.Y, VariantSettings.Rotation.R180)
        )
        .register(Direction.SOUTH, BlockStateVariant.create().put(VariantSettings.X, VariantSettings.Rotation.R90))
        .register(
            Direction.WEST, BlockStateVariant.create()
                .put(VariantSettings.X, VariantSettings.Rotation.R90)
                .put(VariantSettings.Y, VariantSettings.Rotation.R90)
        )
        .register(
            Direction.EAST, BlockStateVariant.create()
                .put(VariantSettings.X, VariantSettings.Rotation.R90)
                .put(VariantSettings.Y, VariantSettings.Rotation.R270)
        )
}

fun BlockStateModelGenerator.strongScaffolding(block: Block) {
    val texture: Texture = Texture()
        .put(TextureKey.TOP, block.model("_top"))
        .put(TextureKey.SIDE, block.model("_side"))
        .put(TextureKey.BOTTOM, block.model("_bottom"))

    val stable: Identifier =
        block("parent/strong_stable_scaffolding", "_stable", TextureKey.TOP, TextureKey.SIDE, TextureKey.BOTTOM)
            .upload(block, texture, this.modelCollector)
    val unstable: Identifier =
        block("parent/strong_unstable_scaffolding", "_unstable", TextureKey.TOP, TextureKey.SIDE, TextureKey.BOTTOM)
            .upload(block, texture, this.modelCollector)

    this.registerParentedItemModel(block, stable)
    this.blockStateCollector.accept(
        VariantsBlockStateSupplier.create(block)
            .coordinate(BlockStateModelGenerator.createBooleanModelMap(Properties.BOTTOM, unstable, stable))
    )
}

fun BlockStateModelGenerator.pixelAccurateSign(sign: Block, wallSign: Block, planks: Identifier, log: Identifier) {
    this.pixelAccurateSign(sign, planks, log)
    this.pixelAccurateWallSign(wallSign, planks)
}

fun BlockStateModelGenerator.pixelAccurateSign(sign: Block, wallSign: Block, planks: Block, log: Block) {
    this.pixelAccurateSign(sign, wallSign, planks.model(), log.model())
}

fun BlockStateModelGenerator.pixelAccurateSign(sign: Block, planks: Identifier, log: Identifier) {
    val texture: Texture = Texture()
        .put(PLANKS, planks)
        .put(WOOD, log)
    val default: Identifier =
        block(mc("block/parent/sign_0"), PLANKS, WOOD).upload(sign.model("_0"), texture, this.modelCollector)
    val rotate225: Identifier =
        block(mc("block/parent/sign_1"), PLANKS, WOOD).upload(sign.model("_1"), texture, this.modelCollector)
    val rotate45: Identifier =
        block(mc("block/parent/sign_2"), PLANKS, WOOD).upload(sign.model("_2"), texture, this.modelCollector)
    val rotate675: Identifier =
        block(mc("block/parent/sign_3"), PLANKS, WOOD).upload(sign.model("_3"), texture, this.modelCollector)

    this.blockStateCollector.accept(
        VariantsBlockStateSupplier.create(sign)
            .coordinate(create16RotationStates(default, rotate225, rotate45, rotate675))
    )
}

fun BlockStateModelGenerator.pixelAccurateWallSign(wallSign: Block, planks: Identifier) {
    val texture: Texture = Texture()
        .put(PLANKS, planks)
    block(mc("block/parent/wall_sign"), PLANKS).upload(wallSign, texture, this.modelCollector)
    this.registerNorthDefaultHorizontalRotation(wallSign)
}

fun create16RotationStates(
    model0: Identifier,
    model1: Identifier,
    model2: Identifier,
    model3: Identifier
): BlockStateVariantMap {
    val variants = BlockStateVariantMap.create(Properties.ROTATION)
    listOf(
        VariantSettings.Rotation.R180,
        VariantSettings.Rotation.R270,
        VariantSettings.Rotation.R0,
        VariantSettings.Rotation.R90
    ).forEachIndexed { idx, rot ->
        variants.register(idx * 4, putRot(BlockStateVariant.create().put(VariantSettings.MODEL, model0), rot))
        variants.register(idx * 4 + 1, putRot(BlockStateVariant.create().put(VariantSettings.MODEL, model1), rot))
        variants.register(idx * 4 + 2, putRot(BlockStateVariant.create().put(VariantSettings.MODEL, model2), rot))
        variants.register(idx * 4 + 3, putRot(BlockStateVariant.create().put(VariantSettings.MODEL, model3), rot))
    }
    return variants
}


private fun putRot(
    variant: BlockStateVariant,
    value: VariantSettings.Rotation
): BlockStateVariant {
    return if (value.ordinal == 0)
        variant
    else
        variant.put(VariantSettings.Y, value)
}