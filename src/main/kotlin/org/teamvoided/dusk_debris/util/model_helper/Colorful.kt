package org.teamvoided.dusk_debris.util.model_helper

import net.minecraft.core.Direction
import net.minecraft.data.models.BlockModelGenerators
import net.minecraft.data.models.blockstates.Condition
import net.minecraft.data.models.blockstates.MultiPartGenerator
import net.minecraft.data.models.blockstates.Variant
import net.minecraft.data.models.blockstates.VariantProperties
import net.minecraft.data.models.model.TextureMapping
import net.minecraft.data.models.model.TextureSlot
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.world.level.block.state.properties.StairsShape
import org.teamvoided.dusk_debris.util.block
import org.teamvoided.dusk_debris.util.model


fun BlockModelGenerators.carpetStairs(
    carpetStair: Block, wool: Block
) {

    val texture: TextureMapping = TextureMapping().put(TextureSlot.WOOL, wool.model())
    val ner: ResourceLocation = block("parent/carpet_stairs", TextureSlot.WOOL)
        .create(carpetStair, texture, this.modelOutput)
    val inner: ResourceLocation =
        block("parent/inner_carpet_stairs", "_inner", TextureSlot.WOOL)
            .create(carpetStair, texture, this.modelOutput)
    val outer: ResourceLocation =
        block("parent/outer_carpet_stairs", "_outer", TextureSlot.WOOL)
            .create(carpetStair, texture, this.modelOutput)
    val directions = listOf(
        (Direction.EAST to VariantProperties.Rotation.R0),
        (Direction.SOUTH to VariantProperties.Rotation.R90),
        (Direction.WEST to VariantProperties.Rotation.R180),
        (Direction.NORTH to VariantProperties.Rotation.R270)
    )
    val stairShape = listOf(
        (StairsShape.STRAIGHT to ner),
        (StairsShape.INNER_LEFT to inner),
        (StairsShape.INNER_RIGHT to inner),
        (StairsShape.OUTER_LEFT to outer),
        (StairsShape.OUTER_RIGHT to outer)
    )


    val model = MultiPartGenerator.multiPart(carpetStair)
    var rotatY: VariantProperties.Rotation

    stairShape.forEach { (shape, models) ->
//        if (!(shape == StairShape.INNER_LEFT || shape == StairShape.OUTER_LEFT)) {
//
//        }
        directions.forEach { (direction, rotationY) ->
            rotatY = if (shape == StairsShape.INNER_LEFT || shape == StairsShape.OUTER_LEFT) {
                when (rotationY) {
                    VariantProperties.Rotation.R0 -> VariantProperties.Rotation.R270
                    VariantProperties.Rotation.R90 -> VariantProperties.Rotation.R0
                    VariantProperties.Rotation.R180 -> VariantProperties.Rotation.R90
                    else -> VariantProperties.Rotation.R180
                }
            } else {
                rotationY
            }
            val variant = Variant.variant()
                .with(VariantProperties.MODEL, models)
                .with(VariantProperties.UV_LOCK, true)
            if (rotatY != VariantProperties.Rotation.R0) variant.with(VariantProperties.Y_ROT, rotatY)
            model.with(
                Condition.condition()
                    .term(BlockStateProperties.HORIZONTAL_FACING, direction)
                    .term(BlockStateProperties.STAIRS_SHAPE, shape),
                variant
            )
        }
    }
    this.blockStateOutput.accept(model)
}