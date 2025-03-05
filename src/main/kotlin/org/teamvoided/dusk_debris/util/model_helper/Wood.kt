package org.teamvoided.dusk_debris.util.model_helper

import net.minecraft.block.Block
import net.minecraft.block.Blocks
import net.minecraft.data.client.model.*
import net.minecraft.state.property.Properties
import net.minecraft.util.Identifier
import org.teamvoided.dusk_debris.util.block
import org.teamvoided.dusk_debris.util.model


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
        VariantsBlockStateSupplier.create(block).coordinate(
            BlockStateModelGenerator.createBooleanModelMap(Properties.BOTTOM, unstable, stable)
        )
    )
}