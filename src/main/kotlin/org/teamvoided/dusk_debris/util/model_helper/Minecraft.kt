package org.teamvoided.dusk_debris.util.model_helper

import net.minecraft.block.Block
import net.minecraft.block.Blocks
import net.minecraft.block.enums.SculkSensorPhase
import net.minecraft.data.client.model.*
import net.minecraft.state.property.Properties
import net.minecraft.util.math.Direction


fun BlockStateModelGenerator.sculkCatalystRotate(block: Block) {
    val identifier = ModelIds.getBlockModelId(block)
    val identifierBloom = ModelIds.getBlockSubModelId(block, "_bloom")
    blockStateCollector.accept(
        VariantsBlockStateSupplier.create(Blocks.SCULK_CATALYST)
            .coordinate(
                BlockStateVariantMap.create(Properties.BLOOM).register {
                    BlockStateVariant.create().put(VariantSettings.MODEL, if (it) identifierBloom else identifier)
                })
            .coordinate(createUpDefaultRotationStates())
    )
}

fun BlockStateModelGenerator.sculkShriekerRotate(block: Block) {
    val identifier = ModelIds.getBlockModelId(block)
    val identifier2 = ModelIds.getBlockSubModelId(block, "_can_summon")
    blockStateCollector.accept(
        VariantsBlockStateSupplier.create(block)
            .coordinate(BlockStateModelGenerator.createBooleanModelMap(Properties.CAN_SUMMON, identifier2, identifier))
            .coordinate(createUpDefaultRotationStates())
    )
}

fun BlockStateModelGenerator.sculkSensorRotate(block: Block) {
    val identifier = ModelIds.getBlockSubModelId(block, "_inactive")
    val identifier2 = ModelIds.getBlockSubModelId(block, "_active")
    blockStateCollector.accept(
        VariantsBlockStateSupplier.create(block)
            .coordinate(
                BlockStateVariantMap.create(Properties.SCULK_SENSOR_PHASE).register {
                    BlockStateVariant.create().put(
                        VariantSettings.MODEL,
                        if (it == SculkSensorPhase.INACTIVE) identifier
                        else identifier2
                    )
                })
            .coordinate(createUpDefaultRotationStates())
    )
}

fun BlockStateModelGenerator.addAxis(block: Block) {
    val identifier = ModelIds.getBlockModelId(block)
    this.blockStateCollector.accept(
        BlockStateModelGenerator.createAxisRotatedBlockState(block, identifier)
    )
}


fun createUpDefaultRotationStates(): BlockStateVariantMap {
    return BlockStateVariantMap.create(Properties.FACING)
        .register(Direction.UP, BlockStateVariant.create())
        .register(
            Direction.DOWN, BlockStateVariant.create()
                .put(VariantSettings.X, VariantSettings.Rotation.R180)
        )
        .register(
            Direction.NORTH, BlockStateVariant.create()
                .put(VariantSettings.X, VariantSettings.Rotation.R90)
        )
        .register(
            Direction.SOUTH, BlockStateVariant.create()
                .put(VariantSettings.X, VariantSettings.Rotation.R90)
                .put(VariantSettings.Y, VariantSettings.Rotation.R180)
        )
        .register(
            Direction.WEST, BlockStateVariant.create()
                .put(VariantSettings.X, VariantSettings.Rotation.R90)
                .put(VariantSettings.Y, VariantSettings.Rotation.R270)
        )
        .register(
            Direction.EAST, BlockStateVariant.create()
                .put(VariantSettings.X, VariantSettings.Rotation.R90)
                .put(VariantSettings.Y, VariantSettings.Rotation.R90)
        )
}
