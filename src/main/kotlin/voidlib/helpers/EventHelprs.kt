package org.teamvoided.voidlib.helpers

import net.fabricmc.fabric.api.registry.CompostingChanceRegistry
import net.minecraft.item.ItemConvertible
import net.minecraft.loot.LootPool
import net.minecraft.loot.LootTable
import net.minecraft.loot.entry.LeafEntry
import net.minecraft.loot.entry.LootTableEntry
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.util.Identifier
import net.minecraft.village.TradeOffers
import net.minecraft.village.TradeOffers.BuyForOneEmeraldFactory

/*
     Put all this in Voidlib
*/

//Trader
fun MutableList<TradeOffers.Factory>.add1for1(item: ItemConvertible, maxUses: Int) =
    addSell(item.asItem(), 1, 1, maxUses)

fun MutableList<TradeOffers.Factory>.addSell(
    item: ItemConvertible, price: Int, count: Int, maxUses: Int, experience: Int = 1
) = add(TradeOffers.SellItemFactory(item.asItem(), price, count, maxUses, experience))

fun MutableList<TradeOffers.Factory>.buyFor1(item: ItemConvertible, price: Int, maxUses: Int, experience: Int = 1) =
    add(BuyForOneEmeraldFactory(item, price, maxUses, experience))


// Loot Table modification
fun addToExistingPools(tableBuilder: LootTable.Builder, table: Identifier): LootTable.Builder =
    tableBuilder.modifyPools { it.with(addTable(table)).build() }

fun addNewPool(tableBuilder: LootTable.Builder, table: Identifier): LootTable.Builder =
    tableBuilder.pool(LootPool.builder().with(addTable(table)).build())

fun addTable(table: Identifier): LeafEntry.Builder<*> =
    LootTableEntry.method_428(RegistryKey.of(RegistryKeys.LOOT_TABLE, table))


// Compositing
fun compost(item: ItemConvertible, chance: Double = 1.0) = CompostingChanceRegistry.INSTANCE.add(item, chance.toFloat())
