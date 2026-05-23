package org.teamvoided.dusk_debris.util.model_helper

import net.minecraft.core.Direction
import net.minecraft.data.models.BlockModelGenerators
import net.minecraft.data.models.blockstates.MultiVariantGenerator
import net.minecraft.data.models.blockstates.PropertyDispatch
import net.minecraft.data.models.blockstates.Variant
import net.minecraft.data.models.blockstates.VariantProperties
import net.minecraft.data.models.model.ModelLocationUtils
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.world.level.block.state.properties.SculkSensorPhase
import org.teamvoided.dusk_debris.util.model


fun BlockModelGenerators.sculkCatalystRotate(block: Block) {
    val identifier = ModelLocationUtils.getModelLocation(block)
    val identifierBloom = ModelLocationUtils.getModelLocation(block, "_bloom")
    blockStateOutput.accept(
        MultiVariantGenerator.multiVariant(block)
            .with(
                PropertyDispatch.property(BlockStateProperties.BLOOM).generate {
                    Variant.variant().with(VariantProperties.MODEL, if (it) identifierBloom else identifier)
                })
            .with(createUpDefaultRotationStates())
    )
}

fun BlockModelGenerators.sculkShriekerRotate(block: Block) {
    val identifier = ModelLocationUtils.getModelLocation(block)
    val identifier2 = ModelLocationUtils.getModelLocation(block, "_can_summon")
    blockStateOutput.accept(
        MultiVariantGenerator.multiVariant(block)
            .with(BlockModelGenerators.createBooleanModelDispatch(BlockStateProperties.CAN_SUMMON, identifier2, identifier))
            .with(createUpDefaultRotationStates())
    )
}

fun BlockModelGenerators.sculkSensorRotate(block: Block) {
    val identifier = ModelLocationUtils.getModelLocation(block, "_inactive")
    val identifier2 = ModelLocationUtils.getModelLocation(block, "_active")
    blockStateOutput.accept(
        MultiVariantGenerator.multiVariant(block)
            .with(
                PropertyDispatch.property(BlockStateProperties.SCULK_SENSOR_PHASE).generate {
                    Variant.variant().with(
                        VariantProperties.MODEL,
                        if (it == SculkSensorPhase.INACTIVE) identifier
                        else identifier2
                    )
                })
            .with(createUpDefaultRotationStates())
    )
}

fun BlockModelGenerators.addAxis(block: Block) =
    this.blockStateOutput.accept(BlockModelGenerators.createAxisAlignedPillarBlock(block, block.model()))

fun createUpDefaultRotationStates(): PropertyDispatch {
    return PropertyDispatch.property(BlockStateProperties.FACING)
        .select(Direction.UP, Variant.variant())
        .select(
            Direction.DOWN, Variant.variant()
                .with(VariantProperties.X_ROT, VariantProperties.Rotation.R180)
        )
        .select(
            Direction.NORTH, Variant.variant()
                .with(VariantProperties.X_ROT, VariantProperties.Rotation.R90)
        )
        .select(
            Direction.SOUTH, Variant.variant()
                .with(VariantProperties.X_ROT, VariantProperties.Rotation.R90)
                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180)
        )
        .select(
            Direction.WEST, Variant.variant()
                .with(VariantProperties.X_ROT, VariantProperties.Rotation.R90)
                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270)
        )
        .select(
            Direction.EAST, Variant.variant()
                .with(VariantProperties.X_ROT, VariantProperties.Rotation.R90)
                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90)
        )
}
