package org.teamvoided.dusk_debris.util.model_helper

import net.minecraft.block.Block
import net.minecraft.block.enums.BlockHalf
import net.minecraft.block.enums.StairShape
import net.minecraft.data.client.model.*
import net.minecraft.state.property.Properties
import net.minecraft.util.Identifier
import net.minecraft.util.math.Direction
import org.teamvoided.dusk_debris.util.block
import org.teamvoided.dusk_debris.util.model
import org.teamvoided.dusk_debris.util.suffix


fun BlockStateModelGenerator.carpetStairs(
    carpetStair: Block, wool: Block
) {

    val texture: Texture = Texture().put(TextureKey.WOOL, wool.model())
    val ner: Identifier = block("parent/carpet_stairs", TextureKey.WOOL)
        .upload(carpetStair, texture, this.modelCollector)
    val inner: Identifier =
        block("parent/inner_carpet_stairs", "_inner", TextureKey.WOOL)
            .upload(carpetStair, texture, this.modelCollector)
    val outer: Identifier =
        block("parent/outer_carpet_stairs", "_outer", TextureKey.WOOL)
            .upload(carpetStair, texture, this.modelCollector)
    val directions = listOf(
        (Direction.EAST to VariantSettings.Rotation.R0),
        (Direction.SOUTH to VariantSettings.Rotation.R90),
        (Direction.WEST to VariantSettings.Rotation.R180),
        (Direction.NORTH to VariantSettings.Rotation.R270)
    )
    val stairShape = listOf(
        (StairShape.STRAIGHT to ner),
        (StairShape.INNER_LEFT to inner),
        (StairShape.INNER_RIGHT to inner),
        (StairShape.OUTER_LEFT to outer),
        (StairShape.OUTER_RIGHT to outer)
    )


    val model = MultipartBlockStateSupplier.create(carpetStair)
    var rotatY: VariantSettings.Rotation

    stairShape.forEach { (shape, models) ->
//        if (!(shape == StairShape.INNER_LEFT || shape == StairShape.OUTER_LEFT)) {
//
//        }
        directions.forEach { (direction, rotationY) ->
            rotatY = if (shape == StairShape.INNER_LEFT || shape == StairShape.OUTER_LEFT) {
                when (rotationY) {
                    VariantSettings.Rotation.R0 -> VariantSettings.Rotation.R270
                    VariantSettings.Rotation.R90 -> VariantSettings.Rotation.R0
                    VariantSettings.Rotation.R180 -> VariantSettings.Rotation.R90
                    else -> VariantSettings.Rotation.R180
                }
            } else {
                rotationY
            }
            var variant = BlockStateVariant.create()
                .put(VariantSettings.MODEL, models)
                .put(VariantSettings.UVLOCK, true)
            if (rotatY != VariantSettings.Rotation.R0) variant.put(VariantSettings.Y, rotatY)
            model.with(
                When.create()
                    .set(Properties.HORIZONTAL_FACING, direction)
                    .set(Properties.STAIR_SHAPE, shape),
                variant
            )
        }
    }
    this.blockStateCollector.accept(model)
}