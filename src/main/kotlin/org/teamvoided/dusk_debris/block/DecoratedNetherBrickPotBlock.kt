package org.teamvoided.dusk_debris.block

import net.minecraft.block.BlockState
import net.minecraft.block.DecoratedPotBlock
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.entity.DecoratedPotBlockEntity
import net.minecraft.client.item.TooltipConfig
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.text.Text
import net.minecraft.util.math.BlockPos

class DecoratedNetherBrickPotBlock(settings: Settings) : DecoratedPotBlock(settings) {
    override fun createBlockEntity(pos: BlockPos, state: BlockState): BlockEntity {
        return DecoratedPotBlockEntity(pos, state)
    }

    override fun appendTooltip(
        stack: ItemStack,
        tooltipContext: Item.TooltipContext,
        tooltip: MutableList<Text>,
        options: TooltipConfig
    ) {
        super.appendTooltip(stack, tooltipContext, tooltip, options)
    }
}
