package org.teamvoided.dusk_autumn.init

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup
import net.minecraft.item.ItemGroup
import net.minecraft.item.ItemStack
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.registry.RegistryKey
import net.minecraft.text.Text
import org.teamvoided.dusk_debris.DuskDebris.id
import kotlin.jvm.optionals.getOrNull


object DuskItemGroups {
    val DUSK_TAB: ItemGroup = register("dusk_items",
        FabricItemGroup.builder()
            .icon { ItemStack(DuskItems.VOLCANIC_SAND) }
            .name(Text.translatable("itemGroup.dusk_debris.dusk_items"))
            .entries { _, entries ->
                entries.addStacks(
                    mutableSetOf(
                        ItemStack(DuskItems.VOLCANIC_SAND),
                        ItemStack(DuskItems.SUSPICIOUS_VOLCANIC_SAND),
                        ItemStack(DuskItems.VOLCANIC_SANDSTONE),
                        ItemStack(DuskItems.VOLCANIC_SANDSTONE_STAIRS),
                        ItemStack(DuskItems.VOLCANIC_SANDSTONE_SLAB),
                        ItemStack(DuskItems.VOLCANIC_SANDSTONE_WALL),
                        ItemStack(DuskItems.CHISELED_VOLCANIC_SANDSTONE),
                        ItemStack(DuskItems.SMOOTH_VOLCANIC_SANDSTONE),
                        ItemStack(DuskItems.SMOOTH_VOLCANIC_SANDSTONE_STAIRS),
                        ItemStack(DuskItems.SMOOTH_VOLCANIC_SANDSTONE_SLAB),
                        ItemStack(DuskItems.CUT_VOLCANIC_SANDSTONE),
                        ItemStack(DuskItems.CUT_VOLCANIC_SANDSTONE_SLAB),

                        ItemStack(DuskItems.CHARRED_LOG),
                        ItemStack(DuskItems.CHARRED_WOOD),
                        ItemStack(DuskItems.STRIPPED_CHARRED_LOG),
                        ItemStack(DuskItems.STRIPPED_CHARRED_WOOD),
                        ItemStack(DuskItems.CHARRED_PLANKS),
                        ItemStack(DuskItems.CHARRED_STAIRS),
                        ItemStack(DuskItems.CHARRED_SLAB),
                        ItemStack(DuskItems.CHARRED_DOOR),
                        ItemStack(DuskItems.CHARRED_TRAPDOOR),
                        ItemStack(DuskItems.CHARRED_FENCE),
                        ItemStack(DuskItems.CHARRED_FENCE_GATE),
                        ItemStack(DuskItems.CHARRED_BUTTON),
                        ItemStack(DuskItems.CHARRED_PRESSURE_PLATE),
                        ItemStack(DuskItems.CHARRED_SIGN),
                        ItemStack(DuskItems.CHARRED_HANGING_SIGN),
                    )
                )
            }
            .build()
    )

    fun init() {}

    @Suppress("SameParameterValue")
    private fun register(name: String, itemGroup: ItemGroup): ItemGroup {
        return Registry.register(Registries.ITEM_GROUP, id(name), itemGroup)
    }
    fun getKey(itemGroup: ItemGroup): RegistryKey<ItemGroup>? {
        return Registries.ITEM_GROUP.getKey(itemGroup)?.getOrNull()
    }
}