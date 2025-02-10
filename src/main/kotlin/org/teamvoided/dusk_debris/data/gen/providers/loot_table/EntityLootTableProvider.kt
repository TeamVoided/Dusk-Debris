package org.teamvoided.dusk_debris.data.gen.providers.loot_table

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.SimpleFabricLootTableProvider
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalBiomeTags
import net.minecraft.item.Item
import net.minecraft.item.Items
import net.minecraft.loot.LootPool
import net.minecraft.loot.LootTable
import net.minecraft.loot.condition.LocationCheckLootCondition
import net.minecraft.loot.context.LootContextTypes
import net.minecraft.loot.entry.EmptyEntry
import net.minecraft.loot.entry.ItemEntry
import net.minecraft.loot.entry.LootTableEntry
import net.minecraft.predicate.entity.LocationPredicate
import net.minecraft.registry.HolderLookup
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.tag.TagKey
import net.minecraft.world.biome.Biome
import org.teamvoided.dusk_debris.util.Utils
import org.teamvoided.dusk_debris.data.DuskLootTables
import java.util.concurrent.CompletableFuture
import java.util.function.BiConsumer

class EntityLootTableProvider(o: FabricDataOutput, val r: CompletableFuture<HolderLookup.Provider>) :
    SimpleFabricLootTableProvider(o, r, LootContextTypes.CHEST) {


//    val dropsItSelf = listOf()

    override fun generate(gen: BiConsumer<RegistryKey<LootTable>, LootTable.Builder>) {
        gen.accept(
            DuskLootTables.ENDERMAN_HOLDS,
            LootTable.builder().pool(
                LootPool.builder().rolls(Utils.constantNum(1))
                    .with(EmptyEntry.builder().weight(1000))
                    .with(LootTableEntry.method_428(DuskLootTables.ENDERMAN_OVERWORLD_GENERIC).weight(25))
                    .with(DuskLootTables.ENDERMAN_NETHER_GENERIC, ConventionalBiomeTags.IS_NETHER)
                    .with(DuskLootTables.ENDERMAN_END_GENERIC, ConventionalBiomeTags.IS_END)
                    .with(DuskLootTables.ENDERMAN_OVERWORLD_FLOWER, ConventionalBiomeTags.IS_FLORAL, 50)
                    .with(DuskLootTables.ENDERMAN_OVERWORLD_ICE, ConventionalBiomeTags.IS_COLD_OVERWORLD, 25)
                    .with(DuskLootTables.ENDERMAN_OVERWORLD_DESERT, ConventionalBiomeTags.IS_DESERT, 25)
                    .with(DuskLootTables.ENDERMAN_OVERWORLD_BADLANDS, ConventionalBiomeTags.IS_BADLANDS, 25)
            )
        )
        gen.endermanHolding(
            DuskLootTables.ENDERMAN_OVERWORLD_GENERIC,
            item(Items.GRASS_BLOCK, 10),
            item(Items.DIRT, 3),
            item(Items.POPPY, 5),
            item(Items.DANDELION, 5),
            item(Items.PUMPKIN, 5),
            item(Items.MELON),
            item(Items.STONE, 3),
            item(Items.GRAVEL, 3),
            item(Items.DEEPSLATE),
            item(Items.RED_MUSHROOM, 3),
            item(Items.BROWN_MUSHROOM, 3)
        )
        gen.endermanHolding(
            DuskLootTables.ENDERMAN_NETHER_GENERIC,
            item(Items.NETHERRACK, 10),
            item(Items.CRIMSON_NYLIUM, 3),
            item(Items.WARPED_NYLIUM, 7),
            item(Items.BLACKSTONE, 7),
            item(Items.SOUL_SAND, 3),
            item(Items.SOUL_SOIL, 3),
            item(Items.BASALT, 3)
        )
        gen.accept(
            DuskLootTables.ENDERMAN_END_GENERIC,
            LootTable.builder().pool(
                LootPool.builder().rolls(Utils.constantNum(1))
                    .with(EmptyEntry.builder().weight(250))
                    .with(ItemEntry.builder(Items.END_STONE).weight(250))
                    .with(ItemEntry.builder(Items.CHORUS_FLOWER).weight(50))
                    .with(ItemEntry.builder(Items.OBSIDIAN))
            )
        )
        gen.endermanHolding(
            DuskLootTables.ENDERMAN_OVERWORLD_FLOWER,
            item(Items.POPPY, 10),
            item(Items.DANDELION, 10),
            item(Items.ALLIUM, 10),
            item(Items.CORNFLOWER, 10),
            item(Items.LILY_OF_THE_VALLEY, 10),
            item(Items.OXEYE_DAISY, 10),
            item(Items.ORANGE_TULIP, 10),
            item(Items.PINK_TULIP, 10),
            item(Items.RED_TULIP, 10),
            item(Items.WHITE_TULIP, 10),
            item(Items.BLUE_ORCHID)
        )
        gen.endermanHolding(
            DuskLootTables.ENDERMAN_OVERWORLD_ICE,
            item(Items.SNOW_BLOCK, 10),
            item(Items.POWDER_SNOW_BUCKET, 10),
            item(Items.ICE, 10),
            item(Items.PACKED_ICE, 10),
            item(Items.BLUE_ICE)
        )
        gen.endermanHolding(
            DuskLootTables.ENDERMAN_OVERWORLD_DESERT,
            item(Items.SAND, 25),
            item(Items.SANDSTONE, 25),
            item(Items.RED_SAND, 10),
            item(Items.RED_SANDSTONE, 10),
            item(Items.DEAD_BUSH, 15),
            item(Items.CACTUS, 7)
        )
        gen.endermanHolding(
            DuskLootTables.ENDERMAN_OVERWORLD_BADLANDS,
            item(Items.RED_SAND, 100),
            item(Items.RED_SANDSTONE, 100),
            item(Items.DEAD_BUSH, 150),
            item(Items.CACTUS, 70),
            item(Items.TERRACOTTA, 50),
            item(Items.WHITE_TERRACOTTA, 5),
            item(Items.LIGHT_GRAY_TERRACOTTA),
            item(Items.BROWN_TERRACOTTA),
            item(Items.RED_TERRACOTTA),
            item(Items.ORANGE_TERRACOTTA),
            item(Items.YELLOW_TERRACOTTA)
        )
    }

    fun LootPool.Builder.with(
        lootTable: RegistryKey<LootTable>,
        biomeTag: TagKey<Biome>,
        weight: Int = 1
    ): LootPool.Builder {
        val biomes = r.get().getLookupOrThrow(RegistryKeys.BIOME)
        val retorn =
            LootTableEntry.method_428(lootTable).conditionally(
                LocationCheckLootCondition.builder(
                    LocationPredicate.Builder.create().biomes(
                        biomes.getTagOrThrow(biomeTag)
                    )
                )
            )
        if (weight > 1)
            retorn.weight(weight)
        return this.with(retorn)
    }

    private fun item(item: Item, weight: Int = 1): Pair<Item, Int> = (item to weight)

    fun BiConsumer<RegistryKey<LootTable>, LootTable.Builder>.endermanHolding(
        lootTable: RegistryKey<LootTable>,
        vararg items: Pair<Item, Int>
    ) {
        val pool = LootPool.builder().rolls(Utils.constantNum(1))

        items.forEach {
            val builder = ItemEntry.builder(it.first)
            if (it.second > 1)
                builder.weight(it.second)
            pool.with(builder)
        }

        return this.accept(lootTable, LootTable.builder().pool(pool))
    }
}