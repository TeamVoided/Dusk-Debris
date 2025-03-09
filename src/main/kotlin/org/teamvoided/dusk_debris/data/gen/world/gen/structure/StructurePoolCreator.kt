package org.teamvoided.dusk_debris.data.gen.world.gen.structure

import com.mojang.datafixers.util.Pair
import net.minecraft.registry.*
import net.minecraft.structure.pool.*
import net.minecraft.structure.processor.StructureProcessorList
import net.minecraft.structure.processor.StructureProcessorLists
import net.minecraft.world.gen.feature.PlacedFeature
import org.teamvoided.dusk_debris.DuskDebris.MODID
import org.teamvoided.dusk_debris.data.worldgen.structure.DuskStructurePools
import java.util.function.Function

object StructurePoolCreator {
    fun bootstrap(c: BootstrapContext<StructurePool>) {
        val structurePools = c.getRegistryLookup(RegistryKeys.STRUCTURE_POOL)
        val placedFeatures = c.getRegistryLookup(RegistryKeys.PLACED_FEATURE)
        val procLists = c.getRegistryLookup(RegistryKeys.STRUCTURE_PROCESSOR_LIST)

        val poolEmpty = structurePools.getHolderOrThrow(StructurePools.EMPTY)
        val procEmpty = procLists.getHolderOrThrow(StructureProcessorLists.EMPTY)

        c.register(
            DuskStructurePools.NETHER_FOSSILS,
            poolEmpty,
            StructurePool.Projection.RIGID,
            legacySingleMC("nether_fossils/fossil_1", procEmpty),
            legacySingleMC("nether_fossils/fossil_2", procEmpty),
            legacySingleMC("nether_fossils/fossil_3", procEmpty),
            legacySingleMC("nether_fossils/fossil_4", procEmpty),
            legacySingleMC("nether_fossils/fossil_5", procEmpty),
            legacySingleMC("nether_fossils/fossil_6", procEmpty),
            legacySingleMC("nether_fossils/fossil_7", procEmpty),
            legacySingleMC("nether_fossils/fossil_8", procEmpty),
            legacySingleMC("nether_fossils/fossil_9", procEmpty),
            legacySingleMC("nether_fossils/fossil_10", procEmpty),
            legacySingleMC("nether_fossils/fossil_11", procEmpty),
            legacySingleMC("nether_fossils/fossil_12", procEmpty),
            legacySingleMC("nether_fossils/fossil_13", procEmpty),
            legacySingleMC("nether_fossils/fossil_14", procEmpty),
        )
        c.register(
            DuskStructurePools.TEST,
            poolEmpty,
            StructurePool.Projection.RIGID,
            legacySingle("test", procEmpty)
        )
    }

    fun BootstrapContext<StructurePool>.register(
        key: RegistryKey<StructurePool>,
        fallback: Holder.Reference<StructurePool>,
        projection: StructurePool.Projection,
        vararg piece: Pair<Function<StructurePool.Projection, out StructurePoolElement>, Int>
    ): Holder.Reference<StructurePool> {
        return this.register(key, StructurePool(fallback, piece.toList(), projection))
    }

    private fun id(str: String) = "$MODID:$str"
    private fun mc(str: String) = "minecraft:$str"

    private fun single(
        str: String,
        processors: Holder<StructureProcessorList>,
        weight: Int = 1
    ): Pair<Function<StructurePool.Projection, out StructurePoolElement>, Int> =
        Pair(StructurePoolElement.ofProcessedSingle(id(str), processors), weight)

    private fun legacySingle(
        str: String,
        processors: Holder<StructureProcessorList>,
        weight: Int = 1
    ): Pair<Function<StructurePool.Projection, out StructurePoolElement>, Int> =
        Pair(StructurePoolElement.ofProcessedLegacySingle(id(str), processors), weight)

    private fun legacySingleMC(
        str: String,
        processors: Holder<StructureProcessorList>,
        weight: Int = 1
    ): Pair<Function<StructurePool.Projection, out StructurePoolElement>, Int> =
        Pair(StructurePoolElement.ofProcessedLegacySingle(mc(str), processors), weight)


    private fun feature(
        placedFeatures: Holder<PlacedFeature>,
        weight: Int = 1
    ): Pair<Function<StructurePool.Projection, out StructurePoolElement>, Int> =
        Pair(StructurePoolElement.ofFeature(placedFeatures), weight)
}