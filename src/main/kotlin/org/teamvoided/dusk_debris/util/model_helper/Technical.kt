package org.teamvoided.dusk_debris.util.model_helper

import com.google.gson.JsonElement
import net.minecraft.block.Block
import net.minecraft.data.client.model.*
import net.minecraft.state.property.Properties
import net.minecraft.util.Identifier
import net.minecraft.util.math.Direction
import org.teamvoided.dusk_debris.block.ExhaustBlock
import org.teamvoided.dusk_debris.block.FanBlock
import org.teamvoided.dusk_debris.util.model
import org.teamvoided.dusk_debris.util.suffix
import java.util.function.BiConsumer
import java.util.function.Supplier

fun BlockStateModelGenerator.registerCopperFan(fan: Block, waxedFan: Block? = null) {
    val default: Identifier = Models.CUBE_BOTTOM_TOP.upload(fan, Texture.sideTopBottom(fan), this.modelCollector)
    val power: Identifier =
        this.createSubModel(fan, "_powered", Models.CUBE_BOTTOM_TOP, ::topSideBottomTexture) // this is the bulb way
    val active: Identifier =
        Models.CUBE_BOTTOM_TOP.upload(fan, "_active", topSideBottomTexture(fan, "_active"), this.modelCollector)
    val activePower: Identifier =
        Models.CUBE_BOTTOM_TOP.upload(
            fan,
            "_active_powered",
            topSideBottomTexture(fan, "_active", "_powered"),
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


fun topSideBottomTexture(id: Identifier): Texture {
    return Texture()
        .put(TextureKey.TOP, id.suffix("_top"))
        .put(TextureKey.SIDE, id.suffix("_side"))
        .put(TextureKey.BOTTOM, id.suffix("_bottom"))
}

fun topSideBottomTexture(block: Block, suffix: String, powered: String = ""): Texture {
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

fun BlockStateModelGenerator.registerExhaust(block: Block) {
    val item = Models.CUBE_COLUMN.upload(block, exhaustBlockTexture(block), modelCollector)

    val bsvMap = BlockStateVariantMap.create(Properties.FACING, ExhaustBlock.POWERED, ExhaustBlock.ACTIVE)

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
                bsvMap.register(
                    direction, powered, active,
                    BlockStateVariant.create().put(
                        VariantSettings.MODEL, when (direction.axis) {
                            Direction.Axis.X -> modelX
                            Direction.Axis.Y -> modelY
                            Direction.Axis.Z -> modelZ
                        }
                    )
                )
            }
        }
    }

    this.blockStateCollector.accept(
        VariantsBlockStateSupplier.create(block, BlockStateVariant.create().put(VariantSettings.MODEL, item))
            .coordinate(bsvMap)
    )
}


private fun exhaustBlockTexture(
    block: Block,
    powered: Boolean = false,
    activity: String = "_idle"
): Texture {
    val powr = if (powered) "_powered" else ""
    return Texture()
        .put(TextureKey.END, block.model(activity))
        .put(TextureKey.SIDE, block.model("_side$powr"))
}

private fun BlockStateModelGenerator.exhaustBlockModel(
    block: Block,
    direction: Direction.Axis = Direction.Axis.Y,
    powered: Boolean = false,
    activity: String = "_idle"
): Identifier {
    val texture = exhaustBlockTexture(block, powered, activity)
    val suffix = activity + if (powered) "_powered" else ""
    return when (direction) {
        Direction.Axis.X -> Models.CUBE_COLUMN_UV_LOCKED_X.upload(block, suffix, texture, modelCollector)
        Direction.Axis.Y -> Models.CUBE_COLUMN_UV_LOCKED_Y.upload(block, suffix, texture, modelCollector)
        Direction.Axis.Z -> Models.CUBE_COLUMN_UV_LOCKED_Z.upload(block, suffix, texture, modelCollector)
    }
}