package org.teamvoided.dusk_debris.data.gen.providers

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.SimpleFabricLootTableProvider
import net.minecraft.item.Items
import net.minecraft.loot.LootPool
import net.minecraft.loot.LootTable
import net.minecraft.loot.condition.LocationCheckLootCondition
import net.minecraft.loot.context.LootContextTypes
import net.minecraft.loot.entry.EmptyEntry
import net.minecraft.loot.entry.ItemEntry
import net.minecraft.loot.entry.LootTableEntry
import net.minecraft.predicate.entity.LocationPredicate
import net.minecraft.registry.*
import net.minecraft.registry.tag.BiomeTags
import net.minecraft.world.biome.Biome
import net.minecraft.world.biome.Biomes
import org.teamvoided.dusk_autumn.util.Utils
import org.teamvoided.dusk_debris.data.DuskLootTables
import java.util.*
import java.util.concurrent.CompletableFuture
import java.util.function.BiConsumer

class EntityLootTableProvider(o: FabricDataOutput, val r: CompletableFuture<HolderLookup.Provider>) :
    SimpleFabricLootTableProvider(o, r, LootContextTypes.CHEST) {


//    val dropsItSelf = listOf()

    override fun generate(gen: BiConsumer<RegistryKey<LootTable>, LootTable.Builder>) {
        val biomes = r.get().getLookupOrThrow(RegistryKeys.BIOME)
        gen.accept(
            DuskLootTables.ENDERMAN_HOLDS,
            LootTable.builder().pool(
                LootPool.builder().rolls(Utils.constantNum(1))
                    .with(EmptyEntry.builder().weight(10))
                    .with(LootTableEntry.method_428(DuskLootTables.ENDERMAN_OVERWORLD_GENERIC).weight(10))
                    .with(
                        LootTableEntry.method_428(DuskLootTables.ENDERMAN_NETHER_GENERIC)
                            .conditionally(
                                LocationCheckLootCondition.builder(
                                    LocationPredicate.Builder.create().method_9024(
                                        biomes.getTagOrThrow(BiomeTags.NETHER)
                                    )
                                )
                            ).weight(10)
                    )
//                    .with(
//                        LootTableEntry.method_428(DuskLootTables.ENDERMAN_NETHER_GENERIC)
//                            .conditionally(
//                                LocationCheckLootCondition.builder(
//                                    LocationPredicate.Builder.create().method_9024(
//                                        HolderSet.createDirect(
//                                            biomes.getHolderOrThrow(Biomes.JUNGLE),
//                                            biomes.getHolderOrThrow(Biomes.SPARSE_JUNGLE),
//                                            biomes.getHolderOrThrow(Biomes.BAMBOO_JUNGLE)
//                                        )
//                                    )
//                                )
//                            ).weight(10)
//                    )
                    .with(LootTableEntry.method_428(DuskLootTables.ENDERMAN_OVERWORLD_DESERT).weight(10))

            )
        )
        gen.accept(
            DuskLootTables.ENDERMAN_OVERWORLD_GENERIC,
            LootTable.builder().pool(
                LootPool.builder().rolls(Utils.constantNum(1))
                    .with(ItemEntry.builder(Items.GRASS_BLOCK).weight(10))
                    .with(ItemEntry.builder(Items.DIRT).weight(3))
                    .with(ItemEntry.builder(Items.POPPY).weight(5))
                    .with(ItemEntry.builder(Items.DANDELION).weight(5))
                    .with(ItemEntry.builder(Items.STONE).weight(3))
                    .with(ItemEntry.builder(Items.GRAVEL).weight(3))
                    .with(ItemEntry.builder(Items.DEEPSLATE))
                    .with(ItemEntry.builder(Items.RED_MUSHROOM).weight(3))
                    .with(ItemEntry.builder(Items.BROWN_MUSHROOM).weight(3))
            )
        )
        gen.accept(
            DuskLootTables.ENDERMAN_NETHER_GENERIC,
            LootTable.builder().pool(
                LootPool.builder().rolls(Utils.constantNum(1))
                    .with(ItemEntry.builder(Items.NETHERRACK).weight(10))
                    .with(ItemEntry.builder(Items.CRIMSON_NYLIUM).weight(3))
                    .with(ItemEntry.builder(Items.WARPED_NYLIUM).weight(7))
                    .with(ItemEntry.builder(Items.BLACKSTONE).weight(7))
                    .with(ItemEntry.builder(Items.SOUL_SAND).weight(3))
                    .with(ItemEntry.builder(Items.SOUL_SOIL).weight(3))
                    .with(ItemEntry.builder(Items.BASALT).weight(3))
            )
        )
        gen.accept(
            DuskLootTables.ENDERMAN_OVERWORLD_DESERT,
            LootTable.builder().pool(
                LootPool.builder().rolls(Utils.constantNum(1))
                    .with(ItemEntry.builder(Items.SAND).weight(25))
                    .with(ItemEntry.builder(Items.SANDSTONE).weight(25))
                    .with(ItemEntry.builder(Items.RED_SAND).weight(10))
                    .with(ItemEntry.builder(Items.RED_SANDSTONE).weight(10))
                    .with(ItemEntry.builder(Items.DEAD_BUSH).weight(15))
                    .with(ItemEntry.builder(Items.CACTUS).weight(7))
            )
        )
    }
}