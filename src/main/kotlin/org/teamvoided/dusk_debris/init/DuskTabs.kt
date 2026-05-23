package org.teamvoided.dusk_debris.init

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents
import net.minecraft.core.Holder
import net.minecraft.core.HolderLookup
import net.minecraft.core.Registry
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.*
import net.minecraft.world.level.ItemLike
import org.teamvoided.dusk_debris.DuskDebris.id
import org.teamvoided.dusk_debris.component.SpellComponent
import org.teamvoided.dusk_debris.spell.Spell
import kotlin.jvm.optionals.getOrNull


object DuskTabs {
//    val DUSK_TAB: ItemGroup = register("dusk_items",
//        FabricItemGroup.builder()
//            .icon { ItemStack(DuskBlocks.GUNPOWDER_BARREL) }
//            .name(Text.translatable("itemGroup.dusk_debris.dusk_items"))
//            .entries { _, entries ->
//                entries.addItem(DuskItemLists.GUNPOWDER_BARREL_ITEM_LIST)
//                entries.addItem(DuskBlockLists.THROWABLE_BOMB_BLOCK_LIST)
//                entries.addItem(
//                    DuskItems.BONECALLER_BANDANA,
//                    DuskBlocks.NETHERSHROOM_STEM,
//                    DuskBlocks.BLUE_NETHERSHROOM_BLOCK,
//                    DuskBlocks.PURPLE_NETHERSHROOM_BLOCK,
//                    DuskBlocks.BLUE_NETHERSHROOM,
//                    DuskBlocks.PURPLE_NETHERSHROOM
//                )
//                entries.addItem(DuskItemLists.OCEAN_METALS_ITEM_LIST)
//                entries.addItem(DuskItemLists.SPAWN_EGGS_ITEM_LIST)
//                entries.addItem(
//                    DuskBlocks.CYPRESS_LOG,
//                    DuskBlocks.CYPRESS_WOOD,
//                    DuskBlocks.STRIPPED_CYPRESS_LOG,
//                    DuskBlocks.STRIPPED_CYPRESS_WOOD,
//                    DuskBlocks.CYPRESS_PLANKS,
//                    DuskBlocks.CYPRESS_STAIRS,
//                    DuskBlocks.CYPRESS_SLAB,
//                    DuskBlocks.CYPRESS_DOOR,
//                    DuskBlocks.CYPRESS_TRAPDOOR,
//                    DuskBlocks.CYPRESS_FENCE,
//                    DuskBlocks.CYPRESS_FENCE_GATE,
//                    DuskBlocks.CYPRESS_BUTTON,
//                    DuskBlocks.CYPRESS_PRESSURE_PLATE,
//                    DuskBlocks.CYPRESS_SIGN,
//                    DuskBlocks.CYPRESS_HANGING_SIGN
//                )
//                entries.addItem(
//                    DuskBlocks.VOLCANIC_SAND,
//                    DuskBlocks.SUSPICIOUS_VOLCANIC_SAND,
//                    DuskBlocks.VOLCANIC_SANDSTONE,
//                    DuskBlocks.VOLCANIC_SANDSTONE_STAIRS,
//                    DuskBlocks.VOLCANIC_SANDSTONE_SLAB,
//                    DuskBlocks.VOLCANIC_SANDSTONE_WALL,
//                    DuskBlocks.CHISELED_VOLCANIC_SANDSTONE,
//                    DuskBlocks.SMOOTH_VOLCANIC_SANDSTONE,
//                    DuskBlocks.SMOOTH_VOLCANIC_SANDSTONE_STAIRS,
//                    DuskBlocks.SMOOTH_VOLCANIC_SANDSTONE_SLAB,
//                    DuskBlocks.CUT_VOLCANIC_SANDSTONE,
//                    DuskBlocks.CUT_VOLCANIC_SANDSTONE_SLAB
//                )
//                entries.addItem(
//                    DuskBlocks.CHARRED_LOG,
//                    DuskBlocks.CHARRED_WOOD,
//                    DuskBlocks.STRIPPED_CHARRED_LOG,
//                    DuskBlocks.STRIPPED_CHARRED_WOOD,
//                    DuskBlocks.CHARRED_PLANKS,
//                    DuskBlocks.CHARRED_STAIRS,
//                    DuskBlocks.CHARRED_SLAB,
//                    DuskBlocks.CHARRED_DOOR,
//                    DuskBlocks.CHARRED_TRAPDOOR,
//                    DuskBlocks.CHARRED_FENCE,
//                    DuskBlocks.CHARRED_FENCE_GATE,
//                    DuskBlocks.CHARRED_BUTTON,
//                    DuskBlocks.CHARRED_PRESSURE_PLATE,
//                    DuskBlocks.CHARRED_SIGN,
//                    DuskBlocks.CHARRED_HANGING_SIGN,
//
//                    DuskBlocks.GUNPOWDER
//                )
//            }
//            .build()
//    )

    val EVERYTHING: CreativeModeTab = register("dusk_everything",
        FabricItemGroup.builder()
            .icon { ItemStack(DuskBlocks.OXIDIZED_COPPER_FAN.asItem()) }
            .title(Component.translatable("Dusk Debris Debug"))
            .displayItems { _, entries -> entries.addItem(DuskItems.ITEMS) }
            .build()
    )
    val SPELLS: CreativeModeTab = register("dusk_spells",
        FabricItemGroup.builder()
            .icon { ItemStack(DuskItems.DEBUG_SPELL_ITEM) }
            .title(Component.translatable("itemGroup.dusk_debris.dusk_spells"))
            .displayItems { params, entries ->
                params.holders().lookup(DuskRegistryKeys.SPELL).ifPresent { registryLookup ->
                    generateSpellEntries(entries, registryLookup)
                }
            }
            .build())

    private fun generateSpellEntries(
        collector: CreativeModeTab.Output,
        lookup: HolderLookup<Spell<*, *>>
    ) {
        lookup.listElements().map { forSpell(it) }.forEach { stack: ItemStack ->
            collector.accept(stack, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS)
        }
    }

    private fun forSpell(info: Holder.Reference<Spell<*, *>>): ItemStack {
        val itemStack = ItemStack(DuskItems.DEBUG_SPELL_ITEM)
        itemStack.set(DuskComponents.SPELL, SpellComponent(info))
        return itemStack
    }

    fun init() {
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.BUILDING_BLOCKS)
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
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.NATURAL_BLOCKS)
            .register(ItemGroupEvents.ModifyEntries {
                it.addAfter(
                    Items.RED_SANDSTONE,
                    DuskBlocks.VOLCANIC_SAND,
                    DuskBlocks.VOLCANIC_SANDSTONE
                )
            })
    }

    private fun CreativeModeTab.Output.addItem(vararg list: ItemLike) = this.addItem(list.toList())
    private fun CreativeModeTab.Output.addItem(list: Collection<ItemLike>) =
        this.acceptAll(list.toStacks())

    private fun CreativeModeTab.Output.addItem(vararg lists: Collection<ItemLike>) =
        this.acceptAll(lists.flatMap { it.toStacks() })

    private fun Collection<ItemLike>.toStacks() = this.toItems().map(Item::getDefaultInstance)
    private fun Collection<ItemLike>.toItems() = this.map(ItemLike::asItem)


    @Suppress("SameParameterValue")
    private fun register(name: String, itemGroup: CreativeModeTab): CreativeModeTab {
        return Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, id(name), itemGroup)
    }

    fun getKey(itemGroup: CreativeModeTab): ResourceKey<CreativeModeTab>? {
        return BuiltInRegistries.CREATIVE_MODE_TAB.getResourceKey(itemGroup)?.getOrNull()
    }

    fun CreativeModeTab.Output.addItems(list: Collection<Item>) {
        this.acceptAll(list.map(Item::getDefaultInstance))
    }
}