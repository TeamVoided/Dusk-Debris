package org.teamvoided.dusk_debris.util.model_helper

import net.minecraft.data.models.BlockModelGenerators
import net.minecraft.data.models.model.ModelTemplates
import net.minecraft.data.models.model.TextureMapping
import net.minecraft.data.models.model.TextureSlot
import net.minecraft.world.level.block.Block
import org.teamvoided.dusk_debris.util.block

val FLOWER: TextureSlot = TextureSlot.create("flower")
val BASE: TextureSlot = TextureSlot.create("base")


fun BlockModelGenerators.bubbleBlock(block: Block, explosiveBlock: Block? = null) {
    //you must write custom blockstate files

    val texture = TextureMapping().put(TextureSlot.ALL, TextureMapping.getBlockTexture(block))
    val model = ModelTemplates.CUBE_ALL.create(block, texture, this.modelOutput)

    val poppedTexture = TextureMapping().put(TextureSlot.ALL, TextureMapping.getBlockTexture(block, "_popped"))
    val modelPopped = block("parent/plane", "_popped", TextureSlot.ALL).create(block, poppedTexture, this.modelOutput)

    if (explosiveBlock != null) {
        val explosiveTexture = texture.put(TextureSlot.INSIDE, TextureMapping.getBlockTexture(block, "_core"))
        val modelExplosive = ModelTemplates.CUBE_ALL.createWithSuffix(block, "_volatile", explosiveTexture, this.modelOutput)
    }
}

fun BlockModelGenerators.bubbleBlossomBlock(block: Block) {
    val texture = TextureMapping()
        .put(FLOWER, TextureMapping.getBlockTexture(block))
        .put(TextureSlot.STEM, TextureMapping.getBlockTexture(block, "_stem"))
        .put(BASE, TextureMapping.getBlockTexture(block, "_base"))
    val model =
        block("parent/bubble_blossom", FLOWER, TextureSlot.STEM, BASE).create(block, texture, this.modelOutput)
    blockStateOutput.accept(BlockModelGenerators.createSimpleBlock(block, model))
}