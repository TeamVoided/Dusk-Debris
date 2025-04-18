package org.teamvoided.dusk_debris.util.model_helper

import net.minecraft.block.Block
import net.minecraft.block.enums.ChestType
import net.minecraft.data.client.model.*
import net.minecraft.state.property.Properties
import net.minecraft.util.Identifier
import org.teamvoided.dusk_debris.block.not_blocks.ChestPhase
import org.teamvoided.dusk_debris.block.not_blocks.DuskProperties
import org.teamvoided.dusk_debris.util.block
import org.teamvoided.dusk_debris.util.model


//fun BlockStateModelGenerator.stoneChest(block: Block) {
//    this.blockStateCollector.accept(
//        VariantsBlockStateSupplier.create(block)
//            .coordinate(BlockStateModelGenerator.createNorthDefaultHorizontalRotationStates())
//            .coordinate(this.chestPhases(block))
//    )
//}

//fun BlockStateModelGenerator.chestPhases(block: Block): BlockStateVariantMap.TripleProperty<ChestType, ChestPhase, Boolean> {
//    val variants = BlockStateVariantMap.create(Properties.CHEST_TYPE, DuskProperties.CHEST_PHASE, DuskProperties.LID)
//
//    ChestType.entries.forEach { type ->
//        val typeS = if (type.ordinal == 0) "" else "_" + type.asString()
//        val lidModel = ModelIds.getBlockSubModelId(block, typeS + "_lid")
//        this.stoneChestModel(block, typeS, "_lid")
//        ChestPhase.entries.forEach { phase ->
//            val phaseS = if (phase.ordinal == 2) "_open" else ""
//
////            println("t: ${type.ordinal}, p: ${phase.ordinal}")
//            val model: Identifier = if (phase == ChestPhase.CLOSING)
//                ModelIds.getBlockSubModelId(block, typeS + "_open")
//            else
//                this.stoneChestModel(block, typeS, phaseS)
//
//
//            variants
//                .register(type, phase, false, BlockStateVariant.create().put(VariantSettings.MODEL, model))
//                .register(type, phase, true, BlockStateVariant.create().put(VariantSettings.MODEL, lidModel))
//        }
//    }
//    return variants
//}

private fun BlockStateModelGenerator.stoneChestModel(
    block: Block,
    variant: String = "",
    part: String = ""
): Identifier {
    val texture: Texture = Texture()
        .put(TextureKey.FRONT, block.model("_front$variant"))
        .put(TextureKey.SIDE, block.model("_side"))
        .put(TextureKey.BACK, block.model("_back$variant"))
        .put(TextureKey.TOP, block.model("_top" + if (part == "_open") "_open" else variant))
        .put(TextureKey.BOTTOM, block.model("_bottom" + if (part == "_lid") "_open$variant" else variant))

//    val bloc = Blocks.STONE_BRICKS
//    val texture: Texture = Texture()
//        .put(TextureKey.FRONT, bloc.model())
//        .put(TextureKey.SIDE, bloc.model())
//        .put(TextureKey.BACK, bloc.model())
//        .put(TextureKey.TOP, bloc.model())
//        .put(TextureKey.BOTTOM, bloc.model())
    return block(
        "parent/stone_chest$variant$part",
        TextureKey.FRONT,
        TextureKey.SIDE,
        TextureKey.BACK,
        TextureKey.TOP,
        TextureKey.BOTTOM
    ).upload(block, variant + part, texture, this.modelCollector)
}