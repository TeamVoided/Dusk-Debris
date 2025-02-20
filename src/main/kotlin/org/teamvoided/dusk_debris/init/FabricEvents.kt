package org.teamvoided.dusk_debris.init

import net.fabricmc.fabric.api.loot.v3.LootTableEvents
import net.fabricmc.fabric.api.loot.v3.LootTableSource
import net.minecraft.entity.EntityType
import net.minecraft.loot.LootPool
import net.minecraft.loot.LootTable
import net.minecraft.loot.entry.LeafEntry
import net.minecraft.loot.entry.LootTableEntry
import net.minecraft.registry.HolderLookup
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.util.Identifier
import org.teamvoided.dusk_debris.data.DuskLootTables

@Suppress("FunctionName")
fun InitializeFabricEvents() {
    LootTableEvents.MODIFY.register(::modifyLootTables)
}

@Suppress("UNUSED_PARAMETER")
fun modifyLootTables(
    key: RegistryKey<LootTable>, builder: LootTable.Builder, source: LootTableSource, lookup: HolderLookup.Provider
) = when (key) {
    EntityType.VINDICATOR.lootTableId -> addNewPool(builder, DuskLootTables.RAIDER_BAD_OMEN_BOTTLE.value)
    else -> Unit
}

// No Voidlib? ||megamind||
fun addNewPool(tableBuilder: LootTable.Builder, table: Identifier): LootTable.Builder =
    tableBuilder.pool(LootPool.builder().with(addTable(table)).build())

fun addTable(table: Identifier): LeafEntry.Builder<*> =
    LootTableEntry.method_428(RegistryKey.of(RegistryKeys.LOOT_TABLE, table))