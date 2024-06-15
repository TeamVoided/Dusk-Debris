package net.minecraft.data.server.loot_table

class FishingLootTableGenerator(registries: HolderLookup.Provider) : LootTableGenerator {
    override fun generate(biConsumer: java.util.function.BiConsumer<net.minecraft.registry.RegistryKey<LootTable>, LootTable.Builder>) {
        val registryLookup: RegistryLookup<Biome> = registries.getLookupOrThrow<Biome>(RegistryKeys.BIOME)
        biConsumer.accept(
            LootTables.FISHING_GAMEPLAY,
            LootTable.builder().pool(
                LootPool.builder().rolls(ConstantLootNumberProvider.create(1.0f))
                    .with(LootTableEntry.method_428(LootTables.FISHING_JUNK_GAMEPLAY).weight(10).quality(-2)).with(
                        LootTableEntry.method_428(LootTables.FISHING_TREASURE_GAMEPLAY).weight(5).quality(2).conditionally(
                            EntityPropertiesLootCondition.builder(
                                EntityTarget.THIS,
                                EntityPredicate.Builder.create().typeSpecific(FishingHookPredicate.create(true))
                            )
                        )
                    ).with(LootTableEntry.method_428(LootTables.FISHING_FISH_GAMEPLAY).weight(85).quality(-1))
            )
        )
        biConsumer.accept(LootTables.FISHING_FISH_GAMEPLAY, fishingTable())
        biConsumer.accept(
            LootTables.FISHING_JUNK_GAMEPLAY, LootTable.builder().pool(
                LootPool.builder().with(ItemEntry.builder(Blocks.LILY_PAD).weight(17)).with(
                    ItemEntry.builder(net.minecraft.item.Items.LEATHER_BOOTS).weight(10)
                        .apply(SetDamageLootFunction.builder(UniformLootNumberProvider.create(0.0f, 0.9f)))
                ).with(ItemEntry.builder(net.minecraft.item.Items.LEATHER).weight(10))
                    .with(ItemEntry.builder(net.minecraft.item.Items.BONE).weight(10)).with(
                        ItemEntry.builder(net.minecraft.item.Items.POTION).weight(10)
                            .apply(SetPotionFunction.builder(Potions.WATER))
                    ).with(ItemEntry.builder(net.minecraft.item.Items.STRING).weight(5)).with(
                        ItemEntry.builder(net.minecraft.item.Items.FISHING_ROD).weight(2)
                            .apply(SetDamageLootFunction.builder(UniformLootNumberProvider.create(0.0f, 0.9f)))
                    ).with(ItemEntry.builder(net.minecraft.item.Items.BOWL).weight(10))
                    .with(ItemEntry.builder(net.minecraft.item.Items.STICK).weight(5)).with(
                        ItemEntry.builder(net.minecraft.item.Items.INK_SAC).weight(1)
                            .apply(SetCountLootFunction.builder(ConstantLootNumberProvider.create(10.0f)))
                    ).with(ItemEntry.builder(Blocks.TRIPWIRE_HOOK).weight(10))
                    .with(ItemEntry.builder(net.minecraft.item.Items.ROTTEN_FLESH).weight(10))
                    .with(
                        (ItemEntry.builder(Blocks.BAMBOO).conditionally(
                            LocationCheckLootCondition.builder(
                                net.minecraft.predicate.entity.LocationPredicate.Builder.create().method_9024(
                                    HolderSet.createDirect<Any>(
                                        *arrayOf<net.minecraft.registry.Holder<*>>(
                                            registryLookup.getHolderOrThrow(Biomes.JUNGLE),
                                            registryLookup.getHolderOrThrow(Biomes.SPARSE_JUNGLE),
                                            registryLookup.getHolderOrThrow(Biomes.BAMBOO_JUNGLE)
                                        )
                                    )
                                )
                            )
                        )).weight(10)
                    )
            )
        )
        biConsumer.accept(
            LootTables.FISHING_TREASURE_GAMEPLAY, LootTable.builder().pool(
                LootPool.builder().with(ItemEntry.builder(net.minecraft.item.Items.NAME_TAG))
                    .with(ItemEntry.builder(net.minecraft.item.Items.SADDLE)).with(
                        ItemEntry.builder(net.minecraft.item.Items.BOW)
                            .apply(SetDamageLootFunction.builder(UniformLootNumberProvider.create(0.0f, 0.25f))).apply(
                                EnchantWithLevelsLootFunction.method_481(
                                    this.registries, ConstantLootNumberProvider.create(30.0f)
                                )
                            )
                    ).with(
                        ItemEntry.builder(net.minecraft.item.Items.FISHING_ROD)
                            .apply(SetDamageLootFunction.builder(UniformLootNumberProvider.create(0.0f, 0.25f))).apply(
                                EnchantWithLevelsLootFunction.method_481(
                                    this.registries, ConstantLootNumberProvider.create(30.0f)
                                )
                            )
                    ).with(
                        ItemEntry.builder(net.minecraft.item.Items.BOOK).apply(
                            EnchantWithLevelsLootFunction.method_481(
                                this.registries, ConstantLootNumberProvider.create(30.0f)
                            )
                        )
                    ).with(ItemEntry.builder(net.minecraft.item.Items.NAUTILUS_SHELL))
            )
        )
    }

    fun registries(): HolderLookup.Provider {
        return this.registries
    }

    val registries: HolderLookup.Provider = registries

    companion object {
        fun fishingTable(): LootTable.Builder {
            return LootTable.builder().pool(
                LootPool.builder().with(ItemEntry.builder(net.minecraft.item.Items.COD).weight(60))
                    .with(ItemEntry.builder(net.minecraft.item.Items.SALMON).weight(25))
                    .with(ItemEntry.builder(net.minecraft.item.Items.TROPICAL_FISH).weight(2))
                    .with(ItemEntry.builder(net.minecraft.item.Items.PUFFERFISH).weight(13))
            )
        }
    }
}