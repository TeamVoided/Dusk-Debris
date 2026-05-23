package org.teamvoided.dusk_debris.init

import net.fabricmc.fabric.api.loot.v3.LootTableEvents
import net.fabricmc.fabric.api.loot.v3.LootTableSource
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.EntityType
import net.minecraft.world.level.storage.loot.LootPool
import net.minecraft.world.level.storage.loot.LootTable
import net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer
import net.minecraft.world.level.storage.loot.entries.NestedLootTable
import org.teamvoided.dusk_debris.data.DuskLootTables

@Suppress("FunctionName")
fun InitializeFabricEvents() {
    LootTableEvents.MODIFY.register(::modifyLootTables)
}

@Suppress("UNUSED_PARAMETER")
fun modifyLootTables(
    key: ResourceKey<LootTable>, builder: LootTable.Builder, source: LootTableSource, lookup: HolderLookup.Provider
) = when (key) {
    EntityType.VINDICATOR.defaultLootTable -> addNewPool(builder, DuskLootTables.RAIDER_BAD_OMEN_BOTTLE.location())
    else -> Unit
}

// No Voidlib? ||megamind||
fun addNewPool(tableBuilder: LootTable.Builder, table: ResourceLocation): LootTable.Builder =
    tableBuilder.pool(LootPool.lootPool().add(addTable(table)).build())

fun addTable(table: ResourceLocation): LootPoolSingletonContainer.Builder<*> =
    NestedLootTable.lootTableReference(ResourceKey.create(Registries.LOOT_TABLE, table))