package org.teamvoided.dusk_debris.util.model_helper

import net.minecraft.core.Direction
import net.minecraft.data.models.BlockModelGenerators
import net.minecraft.data.models.blockstates.*
import net.minecraft.data.models.model.ModelLocationUtils
import net.minecraft.data.models.model.ModelTemplates
import net.minecraft.data.models.model.TextureMapping
import net.minecraft.data.models.model.TextureSlot
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import org.teamvoided.dusk_debris.block.ExhaustBlock
import org.teamvoided.dusk_debris.block.FanBlock
import org.teamvoided.dusk_debris.util.model
import org.teamvoided.dusk_debris.util.suffix

fun BlockModelGenerators.registerCopperFan(fan: Block, waxedFan: Block? = null) {
    val default: ResourceLocation = ModelTemplates.CUBE_BOTTOM_TOP.create(fan, TextureMapping.cubeBottomTop(fan), this.modelOutput)
    val power: ResourceLocation =
        this.createSuffixedVariant(fan, "_powered", ModelTemplates.CUBE_BOTTOM_TOP, ::topSideBottomTexture) // this is the bulb way
    val active: ResourceLocation =
        ModelTemplates.CUBE_BOTTOM_TOP.createWithSuffix(fan, "_active", topSideBottomTexture(fan, "_active"), this.modelOutput)
    val activePower: ResourceLocation =
        ModelTemplates.CUBE_BOTTOM_TOP.createWithSuffix(
            fan,
            "_active_powered",
            topSideBottomTexture(fan, "_active", "_powered"),
            this.modelOutput
        )
    this.blockStateOutput.accept(
        this.createCopperFanBlockState(
            fan,
            default,
            active,
            power,
            activePower
        )
    )
    if (waxedFan != null) {
        this.delegateItemModel(waxedFan, ModelLocationUtils.getModelLocation(fan.asItem()))
        this.blockStateOutput.accept(
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


fun topSideBottomTexture(id: ResourceLocation): TextureMapping {
    return TextureMapping()
        .put(TextureSlot.TOP, id.suffix("_top"))
        .put(TextureSlot.SIDE, id.suffix("_side"))
        .put(TextureSlot.BOTTOM, id.suffix("_bottom"))
}

fun topSideBottomTexture(block: Block, suffix: String, powered: String = ""): TextureMapping {
    return TextureMapping()
        .put(TextureSlot.TOP, block.model(suffix + powered + "_top"))
        .put(TextureSlot.SIDE, block.model(powered + "_side"))
        .put(TextureSlot.BOTTOM, block.model(powered + "_bottom"))
}


fun BlockModelGenerators.createCopperFanBlockState(
    block: Block,
    base: ResourceLocation,
    active: ResourceLocation,
    powered: ResourceLocation,
    activeAndPowered: ResourceLocation
): BlockStateGenerator {
    return MultiVariantGenerator.multiVariant(block).with(
        PropertyDispatch.properties(FanBlock.ACTIVE, BlockStateProperties.POWERED)
            .generate { activex: Boolean, poweredx: Boolean ->
                if (activex) Variant.variant()
                    .with(VariantProperties.MODEL, if (poweredx) activeAndPowered else active)
                else Variant.variant()
                    .with(VariantProperties.MODEL, if (poweredx) powered else base)
            }).with(this.createColumnWithFacing())
}

fun BlockModelGenerators.registerExhaust(block: Block) {
    val item = ModelTemplates.CUBE_COLUMN.create(block, exhaustBlockTexture(block), modelOutput)

    val bsvMap = PropertyDispatch.properties(BlockStateProperties.FACING, ExhaustBlock.POWERED, ExhaustBlock.ACTIVE)

    listOf(false, true).forEach { powered ->
        repeat(3) { active ->
            val activity = when (active) {
                0 -> "_idle"
                1 -> "_warm"
                2 -> "_blast"
                else -> throw MatchException("sent wrong number in exhaust datagen: $active", null as Throwable?)
            }
            val modelX = exhaustBlockModel(block, Direction.Axis.X, powered, activity)
            val modelY = exhaustBlockModel(block, Direction.Axis.Y, powered, activity)
            val modelZ = exhaustBlockModel(block, Direction.Axis.Z, powered, activity)

            Direction.entries.forEach { direction ->
                bsvMap.select(
                    direction, powered, active,
                    Variant.variant().with(
                        VariantProperties.MODEL, when (direction.axis) {
                            Direction.Axis.X -> modelX
                            Direction.Axis.Y -> modelY
                            Direction.Axis.Z -> modelZ
                        }
                    )
                )
            }
        }
    }

    this.blockStateOutput.accept(
        MultiVariantGenerator.multiVariant(block, Variant.variant().with(VariantProperties.MODEL, item))
            .with(bsvMap)
    )
}


private fun exhaustBlockTexture(
    block: Block,
    powered: Boolean = false,
    activity: String = "_idle"
): TextureMapping {
    val powr = if (powered) "_powered" else ""
    return TextureMapping()
        .put(TextureSlot.END, block.model(activity))
        .put(TextureSlot.SIDE, block.model("_side$powr"))
}

private fun BlockModelGenerators.exhaustBlockModel(
    block: Block,
    direction: Direction.Axis = Direction.Axis.Y,
    powered: Boolean = false,
    activity: String = "_idle"
): ResourceLocation {
    val texture = exhaustBlockTexture(block, powered, activity)
    val suffix = activity + if (powered) "_powered" else ""
    return when (direction) {
        Direction.Axis.X -> ModelTemplates.CUBE_COLUMN_UV_LOCKED_X.createWithSuffix(block, suffix, texture, modelOutput)
        Direction.Axis.Y -> ModelTemplates.CUBE_COLUMN_UV_LOCKED_Y.createWithSuffix(block, suffix, texture, modelOutput)
        Direction.Axis.Z -> ModelTemplates.CUBE_COLUMN_UV_LOCKED_Z.createWithSuffix(block, suffix, texture, modelOutput)
    }
}