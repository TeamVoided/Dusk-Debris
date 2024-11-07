package org.teamvoided.dusk_debris.init

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents
import net.minecraft.item.*
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.registry.RegistryKey
import net.minecraft.text.Text
import org.teamvoided.dusk_debris.DuskDebris.id
import org.teamvoided.dusk_debris.block.DuskBlockLists
import org.teamvoided.dusk_debris.item.DuskItemLists
import kotlin.jvm.optionals.getOrNull


object DuskTabs {
    val DUSK_TAB: ItemGroup = register("dusk_items",
        FabricItemGroup.builder()
            .icon { ItemStack(DuskBlocks.GUNPOWDER_BARREL) }
            .name(Text.translatable("itemGroup.dusk_debris.dusk_items"))
            .entries { _, entries ->
                entries.addItem(DuskItemLists.GUNPOWDER_BARREL_ITEM_LIST)
                entries.addItem(DuskBlockLists.THROWABLE_BOMB_BLOCK_LIST)
                entries.addItem(
                    DuskItems.BONECALLER_BANDANA,
                    DuskBlocks.NETHERSHROOM_STEM,
                    DuskBlocks.BLUE_NETHERSHROOM_BLOCK,
                    DuskBlocks.PURPLE_NETHERSHROOM_BLOCK,
                    DuskBlocks.BLUE_NETHERSHROOM,
                    DuskBlocks.PURPLE_NETHERSHROOM
                )
                entries.addItem(DuskItemLists.OCEAN_METALS_ITEM_LIST)
                entries.addItem(DuskItemLists.SPAWN_EGGS_ITEM_LIST)
                entries.addItem(
                    DuskBlocks.CYPRESS_LOG,
                    DuskBlocks.CYPRESS_WOOD,
                    DuskBlocks.STRIPPED_CYPRESS_LOG,
                    DuskBlocks.STRIPPED_CYPRESS_WOOD,
                    DuskBlocks.CYPRESS_PLANKS,
                    DuskBlocks.CYPRESS_STAIRS,
                    DuskBlocks.CYPRESS_SLAB,
                    DuskBlocks.CYPRESS_DOOR,
                    DuskBlocks.CYPRESS_TRAPDOOR,
                    DuskBlocks.CYPRESS_FENCE,
                    DuskBlocks.CYPRESS_FENCE_GATE,
                    DuskBlocks.CYPRESS_BUTTON,
                    DuskBlocks.CYPRESS_PRESSURE_PLATE,
                    DuskBlocks.CYPRESS_SIGN,
                    DuskBlocks.CYPRESS_HANGING_SIGN
                )
                entries.addItem(
                    DuskBlocks.VOLCANIC_SAND,
                    DuskBlocks.SUSPICIOUS_VOLCANIC_SAND,
                    DuskBlocks.VOLCANIC_SANDSTONE,
                    DuskBlocks.VOLCANIC_SANDSTONE_STAIRS,
                    DuskBlocks.VOLCANIC_SANDSTONE_SLAB,
                    DuskBlocks.VOLCANIC_SANDSTONE_WALL,
                    DuskBlocks.CHISELED_VOLCANIC_SANDSTONE,
                    DuskBlocks.SMOOTH_VOLCANIC_SANDSTONE,
                    DuskBlocks.SMOOTH_VOLCANIC_SANDSTONE_STAIRS,
                    DuskBlocks.SMOOTH_VOLCANIC_SANDSTONE_SLAB,
                    DuskBlocks.CUT_VOLCANIC_SANDSTONE,
                    DuskBlocks.CUT_VOLCANIC_SANDSTONE_SLAB
                )
                entries.addItem(
                    DuskBlocks.CHARRED_LOG,
                    DuskBlocks.CHARRED_WOOD,
                    DuskBlocks.STRIPPED_CHARRED_LOG,
                    DuskBlocks.STRIPPED_CHARRED_WOOD,
                    DuskBlocks.CHARRED_PLANKS,
                    DuskBlocks.CHARRED_STAIRS,
                    DuskBlocks.CHARRED_SLAB,
                    DuskBlocks.CHARRED_DOOR,
                    DuskBlocks.CHARRED_TRAPDOOR,
                    DuskBlocks.CHARRED_FENCE,
                    DuskBlocks.CHARRED_FENCE_GATE,
                    DuskBlocks.CHARRED_BUTTON,
                    DuskBlocks.CHARRED_PRESSURE_PLATE,
                    DuskBlocks.CHARRED_SIGN,
                    DuskBlocks.CHARRED_HANGING_SIGN,

                    DuskBlocks.GUNPOWDER
                )
            }
            .build()
    )

    val EVERYTHING: ItemGroup = register("dusk_everything",
        FabricItemGroup.builder()
            .icon { ItemStack(DuskBlocks.OXIDIZED_COPPER_FAN.asItem()) }
            .name(Text.translatable("Dusk Debris Debug"))
            .entries { _, entries -> entries.addItem(DuskItems.ITEMS) }
            .build()
    )

    fun init() {
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.BUILDING_BLOCKS)
            .register(ItemGroupEvents.ModifyEntries {
                it.addAfter(
                    Items.CUT_RED_SANDSTONE_SLAB,
                    DuskBlocks.SUSPICIOUS_VOLCANIC_SAND,
                    DuskBlocks.VOLCANIC_SANDSTONE,
                    DuskBlocks.VOLCANIC_SANDSTONE_STAIRS,
                    DuskBlocks.VOLCANIC_SANDSTONE_SLAB,
                    DuskBlocks.VOLCANIC_SANDSTONE_WALL,
                    DuskBlocks.CHISELED_VOLCANIC_SANDSTONE,
                    DuskBlocks.SMOOTH_VOLCANIC_SANDSTONE,
                    DuskBlocks.SMOOTH_VOLCANIC_SANDSTONE_STAIRS,
                    DuskBlocks.SMOOTH_VOLCANIC_SANDSTONE_SLAB,
                    DuskBlocks.CUT_VOLCANIC_SANDSTONE,
                    DuskBlocks.CUT_VOLCANIC_SANDSTONE_SLAB,
                )
            })
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.NATURAL_BLOCKS)
            .register(ItemGroupEvents.ModifyEntries {
                it.addAfter(
                    Items.RED_SANDSTONE,
                    DuskBlocks.VOLCANIC_SAND,
                    DuskBlocks.VOLCANIC_SANDSTONE
                )
            })
    }

    fun ItemGroup.ItemStackCollector.addItem(vararg list: ItemConvertible) = this.addItem(list.toList())
    fun ItemGroup.ItemStackCollector.addItem(list: Collection<ItemConvertible>) = this.addStacks(list.toStacks())
    fun ItemGroup.ItemStackCollector.addItem(vararg lists: Collection<ItemConvertible>) =
        this.addStacks(lists.flatMap { it.toStacks() })

    fun Collection<ItemConvertible>.toStacks() = this.toItems().map(Item::getDefaultStack)
    fun Collection<ItemConvertible>.toItems() = this.map(ItemConvertible::asItem)


    @Suppress("SameParameterValue")
    private fun register(name: String, itemGroup: ItemGroup): ItemGroup {
        return Registry.register(Registries.ITEM_GROUP, id(name), itemGroup)
    }

    fun getKey(itemGroup: ItemGroup): RegistryKey<ItemGroup>? {
        return Registries.ITEM_GROUP.getKey(itemGroup)?.getOrNull()
    }

    fun ItemGroup.ItemStackCollector.addItems(list: Collection<Item>) {
        this.addStacks(list.map(Item::getDefaultStack))
    }
}