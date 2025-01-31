package org.teamvoided.dusk_debris.util.model_helper

import net.minecraft.block.Block
import net.minecraft.data.client.model.BlockStateModelGenerator
import net.minecraft.data.client.model.Models
import net.minecraft.data.client.model.Texture
import net.minecraft.data.client.model.TextureKey
import org.teamvoided.dusk_debris.util.godhomeShiftBlock

fun BlockStateModelGenerator.bubbleBlock(block: Block, explosiveBlock: Block? = null) {
    var texture = Texture().put(TextureKey.ALL, Texture.getId(block))
    val model = Models.CUBE_ALL.upload(block, texture, this.modelCollector)
}