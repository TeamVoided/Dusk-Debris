package org.teamvoided.dusk_debris.init

import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry
import net.minecraft.client.color.item.ItemColor
import net.minecraft.client.renderer.item.ClampedItemPropertyFunction
import net.minecraft.client.renderer.item.ItemProperties
import net.minecraft.resources.ResourceLocation
import net.minecraft.resources.ResourceLocation.withDefaultNamespace
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.component.DyedItemColor
import net.minecraft.world.level.FoliageColor
import net.minecraft.world.level.GrassColor

object DuskItemsClient {
    fun init() {
        ColorProviderRegistry.ITEM.register(
            { itemStack: ItemStack, i: Int ->
                DyedItemColor.getOrDefault(itemStack, 0x7F7F7F)
            }, DuskItems.BONECALLER_BANDANA
        )
        ColorProviderRegistry.ITEM.register({ _, _ -> FoliageColor.getDefaultColor() }, DuskBlocks.CYPRESS_LEAVES)
        registerTint(
            { _, _ -> GrassColor.getDefaultColor() },
            *DuskBlocks.GRASS_TINT_BLOCKS.map { it.asItem() }.toTypedArray()
        )

        // Experimental
        modelPredicate(DuskItems.WEB_WEAVER, withDefaultNamespace("pull")) { stack, _, entity, _ ->
            if (entity == null || entity.useItem != stack) 0.0f
            else (stack.getUseDuration(entity) - entity.useItemRemainingTicks) / 20.0f
        }
        modelPredicate(DuskItems.WEB_WEAVER, withDefaultNamespace("pulling")) { stack, _, entity, _ ->
            if (entity != null && entity.isUsingItem && entity.useItem == stack) 1.0f else 0.0f
        }
    }

    private fun registerTint(provider: ItemColor, vararg items: Item) =
        ColorProviderRegistry.ITEM.register(provider, *items)

    private fun modelPredicate(item: Item, id: ResourceLocation, provider: ClampedItemPropertyFunction) =
        ItemProperties.register(item, id, provider)
}