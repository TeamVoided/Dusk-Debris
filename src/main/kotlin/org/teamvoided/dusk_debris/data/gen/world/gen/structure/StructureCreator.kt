package org.teamvoided.dusk_debris.data.gen.world.gen.structure

import net.minecraft.core.HolderGetter
import net.minecraft.core.registries.Registries
import net.minecraft.data.worldgen.BootstrapContext
import net.minecraft.data.worldgen.Pools
import net.minecraft.resources.ResourceKey
import net.minecraft.tags.BiomeTags
import net.minecraft.tags.TagKey
import net.minecraft.world.level.biome.Biome
import net.minecraft.world.level.levelgen.GenerationStep
import net.minecraft.world.level.levelgen.VerticalAnchor
import net.minecraft.world.level.levelgen.heightproviders.ConstantHeight
import net.minecraft.world.level.levelgen.heightproviders.HeightProvider
import net.minecraft.world.level.levelgen.heightproviders.UniformHeight
import net.minecraft.world.level.levelgen.structure.Structure
import net.minecraft.world.level.levelgen.structure.TerrainAdjustment
import net.minecraft.world.level.levelgen.structure.pools.DimensionPadding
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool
import net.minecraft.world.level.levelgen.structure.structures.JigsawStructure
import org.teamvoided.dusk_debris.data.tags.DuskBiomeTags
import org.teamvoided.dusk_debris.data.worldgen.structure.DuskStructurePools
import org.teamvoided.dusk_debris.data.worldgen.structure.DuskStructures
import org.teamvoided.dusk_debris.world.gen.structure.CaveJigsawStructureFeature
import org.teamvoided.dusk_debris.world.gen.structure.CaveStructureFeature
import java.util.Map

object StructureCreator {
    fun bootstrap(c: BootstrapContext<Structure>) {
        val biomes: HolderGetter<Biome> = c.lookup(Registries.BIOME)
        val pool: HolderGetter<StructureTemplatePool> = c.lookup(Registries.TEMPLATE_POOL)

        c.register(
            DuskStructures.TEST,
            DuskBiomeTags.TEST,
            DuskStructurePools.TEST,
            20,
            UniformHeight.of(VerticalAnchor.aboveBottom(30), VerticalAnchor.belowTop(180))
        )
        c.registerCave(
            DuskStructures.ANCIENT_STRUCTURES,
            BiomeTags.HAS_ANCIENT_CITY,
            Pools.createKey("ancient_city/structures"),
            3,
            ConstantHeight.of(VerticalAnchor.aboveBottom(17)),
            DimensionPadding(16, 128),
            true
        )
    }

    private fun BootstrapContext<Structure>.registerCave(
        key: ResourceKey<Structure>,
        biomeTag: TagKey<Biome>,
        structurePool: ResourceKey<StructureTemplatePool>,
        size: Int,
        heightProvider: HeightProvider,
        dimensionPadding: DimensionPadding,
        bottomUpSearch: Boolean,
        placeIfReachLimit: Boolean = false
    ) {
        val biomes: HolderGetter<Biome> = this.lookup(Registries.BIOME)
        val pool: HolderGetter<StructureTemplatePool> = this.lookup(Registries.TEMPLATE_POOL)

        this.register(
            key,
            CaveJigsawStructureFeature(
                Structure.StructureSettings(
                    biomes.getOrThrow(biomeTag),
                    Map.of(),
                    GenerationStep.Decoration.UNDERGROUND_STRUCTURES,
                    TerrainAdjustment.BEARD_BOX
                ),
                pool.getOrThrow(structurePool),
                size,
                heightProvider,
                placeIfReachLimit,
                bottomUpSearch,
                dimensionPadding
            )
        )
    }

    private fun BootstrapContext<Structure>.registerCave(
        key: ResourceKey<Structure>,
        biomeTag: TagKey<Biome>,
        structurePool: ResourceKey<StructureTemplatePool>,
        initialHeight: HeightProvider,
        bottom: VerticalAnchor
    ) {
        val biomes: HolderGetter<Biome> = this.lookup(Registries.BIOME)
        val pool: HolderGetter<StructureTemplatePool> = this.lookup(Registries.TEMPLATE_POOL)

        this.register(
            key,
            CaveStructureFeature(
                Structure.StructureSettings(
                    biomes.getOrThrow(biomeTag),
                    Map.of(),
                    GenerationStep.Decoration.UNDERGROUND_STRUCTURES,
                    TerrainAdjustment.BEARD_THIN
                ),
                pool.getOrThrow(structurePool),
                initialHeight,
                ConstantHeight.of(bottom)
            )
        )
    }

    private fun BootstrapContext<Structure>.register(
        key: ResourceKey<Structure>,
        biomeTag: TagKey<Biome>,
        structurePool: ResourceKey<StructureTemplatePool>,
        size: Int = 1,
        initialHeight: HeightProvider = ConstantHeight.of(VerticalAnchor.absolute(0)),
    ) {
        val biomes: HolderGetter<Biome> = this.lookup(Registries.BIOME)
        val pool: HolderGetter<StructureTemplatePool> = this.lookup(Registries.TEMPLATE_POOL)

        this.register(
            key,
            JigsawStructure(
                Structure.StructureSettings(
                    biomes.getOrThrow(biomeTag),
                    Map.of(),
                    GenerationStep.Decoration.UNDERGROUND_STRUCTURES,
                    TerrainAdjustment.NONE
                ),
                pool.getOrThrow(structurePool),
                size,
                initialHeight,
                false
            )
        )
    }
}