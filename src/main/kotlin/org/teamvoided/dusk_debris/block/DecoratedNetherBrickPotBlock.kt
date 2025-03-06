package org.teamvoided.dusk_debris.block

import net.minecraft.block.BlockState
import net.minecraft.block.DecoratedPotBlock
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.entity.DecoratedPotBlockEntity
import net.minecraft.util.math.BlockPos

class DecoratedNetherBrickPotBlock(settings: Settings) : DecoratedPotBlock(settings) {
    override fun createBlockEntity(pos: BlockPos, state: BlockState): BlockEntity {
        return DecoratedPotBlockEntity(pos, state)
    }
}
