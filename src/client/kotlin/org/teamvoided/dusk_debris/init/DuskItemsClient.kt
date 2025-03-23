package org.teamvoided.dusk_debris.init

import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry
import net.minecraft.client.color.world.FoliageColors
import net.minecraft.client.item.ModelPredicateProviderRegistry
import net.minecraft.client.item.UnclampedModelPredicateProvider
import net.minecraft.component.type.DyedColorComponent
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.util.Identifier
import net.minecraft.util.Identifier.ofDefault

object DuskItemsClient {
    fun init() {
        ColorProviderRegistry.ITEM.register(
            { itemStack: ItemStack, i: Int ->
                DyedColorComponent.getColorOrDefault(itemStack, 0x7F7F7F)
            }, DuskItems.BONECALLER_BANDANA
        )
        ColorProviderRegistry.ITEM.register(
            { _, _ -> FoliageColors.getDefaultColor() },
            DuskBlocks.CYPRESS_LEAVES
        )

        // Experimental
        modelPredicate(DuskItems.WEB_WEAVER, ofDefault("pull")) { stack, _, entity, _ ->
            if (entity == null || entity.activeItem != stack) 0.0f
            else (stack.getUseTicks(entity) - entity.itemUseTimeLeft) / 20.0f
        }
        modelPredicate(DuskItems.WEB_WEAVER, ofDefault("pulling")) { stack, _, entity, _ ->
            if (entity != null && entity.isUsingItem && entity.activeItem == stack) 1.0f else 0.0f
        }
    }

    fun modelPredicate(item: Item, id: Identifier, provider: UnclampedModelPredicateProvider) =
        ModelPredicateProviderRegistry.register(item, id, provider)
}