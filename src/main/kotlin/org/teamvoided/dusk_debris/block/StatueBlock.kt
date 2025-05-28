package org.teamvoided.dusk_debris.block

import com.mojang.serialization.MapCodec
import net.minecraft.block.BlockRenderType
import net.minecraft.block.BlockState
import net.minecraft.block.BlockWithEntity
import net.minecraft.block.entity.BlockEntity
import net.minecraft.util.math.BlockPos
import org.teamvoided.dusk_debris.block.entity.StatueBlockEntity

class StatueBlock(settings: Settings) : BlockWithEntity(settings) {
    override fun getCodec(): MapCodec<StatueBlock> = createCodec(::StatueBlock)
    override fun createBlockEntity(pos: BlockPos, state: BlockState): BlockEntity = StatueBlockEntity(pos, state)
    override fun getRenderType(state: BlockState) = BlockRenderType.MODEL
}