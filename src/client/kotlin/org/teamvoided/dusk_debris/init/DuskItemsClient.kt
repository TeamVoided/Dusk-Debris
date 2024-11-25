package org.teamvoided.dusk_debris.init

import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry
import net.minecraft.client.color.world.FoliageColors
import net.minecraft.component.type.DyedColorComponent
import net.minecraft.item.ItemStack

object DuskItemsClient {
    fun init(){
        ColorProviderRegistry.ITEM.register(
            { itemStack: ItemStack, i: Int ->
                DyedColorComponent.getColorOrDefault(itemStack, 0x7F7F7F)
            }, DuskItems.BONECALLER_BANDANA
        )
        ColorProviderRegistry.ITEM.register(
            { _, _ -> FoliageColors.getDefaultColor() },
            DuskBlocks.CYPRESS_LEAVES
        )
    }
}