package org.teamvoided.dusk_debris.data.gen.providers.loot_table

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.SimpleFabricLootTableProvider
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalBiomeTags
import net.minecraft.advancements.critereon.EntityPredicate
import net.minecraft.advancements.critereon.LocationPredicate
import net.minecraft.advancements.critereon.RaiderPredicate
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.minecraft.world.level.biome.Biome
import net.minecraft.world.level.storage.loot.LootContext
import net.minecraft.world.level.storage.loot.LootPool
import net.minecraft.world.level.storage.loot.LootTable
import net.minecraft.world.level.storage.loot.entries.EmptyLootItem
import net.minecraft.world.level.storage.loot.entries.LootItem
import net.minecraft.world.level.storage.loot.entries.NestedLootTable
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction
import net.minecraft.world.level.storage.loot.functions.SetOminousBottleAmplifierFunction
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets
import net.minecraft.world.level.storage.loot.predicates.LocationCheck
import net.minecraft.world.level.storage.loot.predicates.LootItemEntityPropertyCondition
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator
import org.teamvoided.dusk_debris.data.DuskLootTables
import org.teamvoided.dusk_debris.util.Utils
import java.util.concurrent.CompletableFuture
import java.util.function.BiConsumer

class EntityLootTableProvider(o: FabricDataOutput, val r: CompletableFuture<HolderLookup.Provider>) :
    SimpleFabricLootTableProvider(o, r, LootContextParamSets.CHEST) {


//    val dropsItSelf = listOf()

    override fun generate(gen: BiConsumer<ResourceKey<LootTable>, LootTable.Builder>) {
        gen.accept(
            DuskLootTables.RAIDER_BAD_OMEN_BOTTLE,
            LootTable.lootTable().withPool(
                LootPool.lootPool().setRolls(ConstantValue.exactly(1.0f)).add(
                    LootItem.lootTableItem(Items.OMINOUS_BOTTLE)
                        .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0f)))
                        .apply(SetOminousBottleAmplifierFunction.setAmplifier(UniformGenerator.between(0.0f, 4.0f)))
                ).`when`(
                    LootItemEntityPropertyCondition.hasProperties(
                        LootContext.EntityTarget.THIS,
                        EntityPredicate.Builder.entity().subPredicate(RaiderPredicate.CAPTAIN_WITHOUT_RAID)
                    )
                )
            )
        )
        gen.endermanHolds()
    }

    private fun BiConsumer<ResourceKey<LootTable>, LootTable.Builder>.endermanHolds() {
        this.accept(
            DuskLootTables.ENDERMAN_HOLDS,
            LootTable.lootTable().withPool(
                LootPool.lootPool().setRolls(Utils.constantNum(1))
                    .add(EmptyLootItem.emptyItem().setWeight(1000))
                    .add(NestedLootTable.lootTableReference(DuskLootTables.ENDERMAN_OVERWORLD_GENERIC).setWeight(5))
                    .with(DuskLootTables.ENDERMAN_NETHER_GENERIC, ConventionalBiomeTags.IS_NETHER)
                    .with(DuskLootTables.ENDERMAN_END_GENERIC, ConventionalBiomeTags.IS_END)
                    .with(DuskLootTables.ENDERMAN_OVERWORLD_FLOWER, ConventionalBiomeTags.IS_FLORAL, 2)
                    .with(DuskLootTables.ENDERMAN_OVERWORLD_ICE, ConventionalBiomeTags.IS_COLD_OVERWORLD, 5)
                    .with(DuskLootTables.ENDERMAN_OVERWORLD_DESERT, ConventionalBiomeTags.IS_DESERT, 5)
                    .with(DuskLootTables.ENDERMAN_OVERWORLD_BADLANDS, ConventionalBiomeTags.IS_BADLANDS, 5)
            )
        )
        this.endermanHolding(
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
        this.endermanHolding(
            DuskLootTables.ENDERMAN_NETHER_GENERIC,
            item(Items.NETHERRACK, 10),
            item(Items.CRIMSON_NYLIUM, 3),
            item(Items.WARPED_NYLIUM, 7),
            item(Items.BLACKSTONE, 7),
            item(Items.SOUL_SAND, 3),
            item(Items.SOUL_SOIL, 3),
            item(Items.BASALT, 3)
        )
        this.accept(
            DuskLootTables.ENDERMAN_END_GENERIC,
            LootTable.lootTable().withPool(
                LootPool.lootPool().setRolls(Utils.constantNum(1))
                    .add(EmptyLootItem.emptyItem().setWeight(250))
                    .add(LootItem.lootTableItem(Items.END_STONE).setWeight(250))
                    .add(LootItem.lootTableItem(Items.CHORUS_FLOWER).setWeight(50))
                    .add(LootItem.lootTableItem(Items.OBSIDIAN))
            )
        )
        this.endermanHolding(
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
        this.endermanHolding(
            DuskLootTables.ENDERMAN_OVERWORLD_ICE,
            item(Items.SNOW_BLOCK, 10),
            item(Items.POWDER_SNOW_BUCKET, 10),
            item(Items.ICE, 10),
            item(Items.PACKED_ICE, 10),
            item(Items.BLUE_ICE)
        )
        this.endermanHolding(
            DuskLootTables.ENDERMAN_OVERWORLD_DESERT,
            item(Items.SAND, 25),
            item(Items.SANDSTONE, 25),
            item(Items.RED_SAND, 10),
            item(Items.RED_SANDSTONE, 10),
            item(Items.DEAD_BUSH, 15),
            item(Items.CACTUS, 7)
        )
        this.endermanHolding(
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
        lootTable: ResourceKey<LootTable>,
        biomeTag: TagKey<Biome>,
        weight: Int = 1
    ): LootPool.Builder {
        val biomes = r.get().lookupOrThrow(Registries.BIOME)
        val retorn =
            NestedLootTable.lootTableReference(lootTable).`when`(
                LocationCheck.checkLocation(
                    LocationPredicate.Builder.location().setBiomes(
                        biomes.getOrThrow(biomeTag)
                    )
                )
            )
        if (weight > 1)
            retorn.setWeight(weight)
        return this.add(retorn)
    }

    private fun item(item: Item, weight: Int = 1): Pair<Item, Int> = (item to weight)

    fun BiConsumer<ResourceKey<LootTable>, LootTable.Builder>.endermanHolding(
        lootTable: ResourceKey<LootTable>,
        vararg items: Pair<Item, Int>
    ) {
        val pool = LootPool.lootPool().setRolls(Utils.constantNum(1))

        items.forEach {
            val builder = LootItem.lootTableItem(it.first)
            if (it.second > 1)
                builder.setWeight(it.second)
            pool.add(builder)
        }

        return this.accept(lootTable, LootTable.lootTable().withPool(pool))
    }
}