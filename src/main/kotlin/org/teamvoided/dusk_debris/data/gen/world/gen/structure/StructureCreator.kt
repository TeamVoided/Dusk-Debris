package org.teamvoided.dusk_debris.data.gen.world.gen.structure

import net.minecraft.registry.BootstrapContext
import net.minecraft.registry.HolderProvider
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.tag.BiomeTags
import net.minecraft.registry.tag.TagKey
import net.minecraft.structure.pool.StructurePool
import net.minecraft.structure.pool.StructurePools
import net.minecraft.world.biome.Biome
import net.minecraft.world.gen.GenerationStep
import net.minecraft.world.gen.YOffset
import net.minecraft.world.gen.feature.DimensionPadding
import net.minecraft.world.gen.feature.JigsawFeature
import net.minecraft.world.gen.feature.StructureFeature
import net.minecraft.world.gen.heightprovider.ConstantHeightProvider
import net.minecraft.world.gen.heightprovider.HeightProvider
import net.minecraft.world.gen.heightprovider.UniformHeightProvider
import net.minecraft.world.gen.structure.TerrainAdjustment
import org.teamvoided.dusk_debris.data.tags.DuskBiomeTags
import org.teamvoided.dusk_debris.data.worldgen.structure.DuskStructurePools
import org.teamvoided.dusk_debris.data.worldgen.structure.DuskStructures
import org.teamvoided.dusk_debris.world.gen.structure.CaveJigsawStructureFeature
import org.teamvoided.dusk_debris.world.gen.structure.CaveStructureFeature
import java.util.Map

object StructureCreator {
    fun bootstrap(c: BootstrapContext<StructureFeature>) {
        val biomes: HolderProvider<Biome> = c.getRegistryLookup(RegistryKeys.BIOME)
        val pool: HolderProvider<StructurePool> = c.getRegistryLookup(RegistryKeys.STRUCTURE_POOL)

        c.register(
            DuskStructures.TEST,
            DuskBiomeTags.TEST,
            DuskStructurePools.TEST,
            20,
            UniformHeightProvider.create(YOffset.aboveBottom(30), YOffset.belowTop(180))
        )
        c.registerCave(
            DuskStructures.ANCIENT_STRUCTURES,
            BiomeTags.HAS_ANCIENT_CITY_STRUCTURE,
            StructurePools.createKey("ancient_city/structures"),
            3,
            ConstantHeightProvider.create(YOffset.aboveBottom(17)),
            DimensionPadding(16, 128),
            true
        )
    }

    private fun BootstrapContext<StructureFeature>.registerCave(
        key: RegistryKey<StructureFeature>,
        biomeTag: TagKey<Biome>,
        structurePool: RegistryKey<StructurePool>,
        size: Int,
        heightProvider: HeightProvider,
        dimensionPadding: DimensionPadding,
        bottomUpSearch: Boolean,
        placeIfReachLimit: Boolean = false
    ) {
        val biomes: HolderProvider<Biome> = this.getRegistryLookup(RegistryKeys.BIOME)
        val pool: HolderProvider<StructurePool> = this.getRegistryLookup(RegistryKeys.STRUCTURE_POOL)

        this.register(
            key,
            CaveJigsawStructureFeature(
                StructureFeature.StructureSettings(
                    biomes.getTagOrThrow(biomeTag),
                    Map.of(),
                    GenerationStep.Feature.UNDERGROUND_STRUCTURES,
                    TerrainAdjustment.BEARD_BOX
                ),
                pool.getHolderOrThrow(structurePool),
                size,
                heightProvider,
                placeIfReachLimit,
                bottomUpSearch,
                dimensionPadding
            )
        )
    }

    private fun BootstrapContext<StructureFeature>.registerCave(
        key: RegistryKey<StructureFeature>,
        biomeTag: TagKey<Biome>,
        structurePool: RegistryKey<StructurePool>,
        initialHeight: HeightProvider,
        bottom: YOffset
    ) {
        val biomes: HolderProvider<Biome> = this.getRegistryLookup(RegistryKeys.BIOME)
        val pool: HolderProvider<StructurePool> = this.getRegistryLookup(RegistryKeys.STRUCTURE_POOL)

        this.register(
            key,
            CaveStructureFeature(
                StructureFeature.StructureSettings(
                    biomes.getTagOrThrow(biomeTag),
                    Map.of(),
                    GenerationStep.Feature.UNDERGROUND_STRUCTURES,
                    TerrainAdjustment.BEARD_THIN
                ),
                pool.getHolderOrThrow(structurePool),
                initialHeight,
                ConstantHeightProvider.create(bottom)
            )
        )
    }

    private fun BootstrapContext<StructureFeature>.register(
        key: RegistryKey<StructureFeature>,
        biomeTag: TagKey<Biome>,
        structurePool: RegistryKey<StructurePool>,
        size: Int = 1,
        initialHeight: HeightProvider = ConstantHeightProvider.create(YOffset.fixed(0)),
    ) {
        val biomes: HolderProvider<Biome> = this.getRegistryLookup(RegistryKeys.BIOME)
        val pool: HolderProvider<StructurePool> = this.getRegistryLookup(RegistryKeys.STRUCTURE_POOL)

        this.register(
            key,
            JigsawFeature(
                StructureFeature.StructureSettings(
                    biomes.getTagOrThrow(biomeTag),
                    Map.of(),
                    GenerationStep.Feature.UNDERGROUND_STRUCTURES,
                    TerrainAdjustment.NONE
                ),
                pool.getHolderOrThrow(structurePool),
                size,
                initialHeight,
                false
            )
        )
    }
}