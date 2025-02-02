package org.teamvoided.dusk_debris.util.model_helper

import net.minecraft.block.Block
import net.minecraft.data.client.model.BlockStateModelGenerator
import net.minecraft.data.client.model.Models
import net.minecraft.data.client.model.Texture
import net.minecraft.data.client.model.TextureKey
import org.teamvoided.dusk_debris.util.block

val FLOWER: TextureKey = TextureKey.of("flower")
val BASE: TextureKey = TextureKey.of("base")


fun BlockStateModelGenerator.bubbleBlock(block: Block, explosiveBlock: Block? = null) {
    //you must write custom blockstate files

    val texture = Texture().put(TextureKey.ALL, Texture.getId(block))
    val model = Models.CUBE_ALL.upload(block, texture, this.modelCollector)

    val poppedTexture = Texture().put(TextureKey.ALL, Texture.getSubId(block, "_popped"))
    val modelPopped = block("parent/plane", "_popped", TextureKey.ALL).upload(block, poppedTexture, this.modelCollector)

    if (explosiveBlock != null) {
        val explosiveTexture = texture.put(TextureKey.INSIDE, Texture.getSubId(block, "_core"))
        val modelExplosive = Models.CUBE_ALL.upload(block, "_volatile", explosiveTexture, this.modelCollector)
    }
}

fun BlockStateModelGenerator.bubbleBlossomBlock(block: Block) {
    val texture = Texture()
        .put(FLOWER, Texture.getId(block))
        .put(TextureKey.STEM, Texture.getSubId(block, "_stem"))
        .put(BASE, Texture.getSubId(block, "_base"))
    val model =
        block("parent/bubble_blossom", FLOWER, TextureKey.STEM, BASE).upload(block, texture, this.modelCollector)
    blockStateCollector.accept(BlockStateModelGenerator.createSingletonBlockState(block, model))
}