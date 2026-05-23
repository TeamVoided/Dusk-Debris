package org.teamvoided.dusk_debris.block

import net.minecraft.core.BlockPos
import net.minecraft.network.chat.Component
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.level.block.DecoratedPotBlock
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.DecoratedPotBlockEntity
import net.minecraft.world.level.block.state.BlockState

class DecoratedNetherBrickPotBlock(settings: Properties) : DecoratedPotBlock(settings) {
    override fun newBlockEntity(pos: BlockPos, state: BlockState): BlockEntity {
        return DecoratedPotBlockEntity(pos, state)
    }

    override fun appendHoverText(
        stack: ItemStack,
        tooltipContext: Item.TooltipContext,
        tooltip: MutableList<Component>,
        options: TooltipFlag
    ) {
        super.appendHoverText(stack, tooltipContext, tooltip, options)
    }
}
