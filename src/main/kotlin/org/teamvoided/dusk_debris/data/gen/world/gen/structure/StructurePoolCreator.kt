package org.teamvoided.dusk_debris.data.gen.world.gen.structure

import com.mojang.datafixers.util.Pair
import net.minecraft.registry.BootstrapContext
import net.minecraft.registry.Holder
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.structure.pool.StructurePool
import net.minecraft.structure.pool.StructurePoolElement
import net.minecraft.structure.pool.StructurePools
import net.minecraft.structure.processor.StructureProcessorList
import net.minecraft.structure.processor.StructureProcessorLists
import net.minecraft.world.gen.feature.PlacedFeature
import org.teamvoided.dusk_debris.DuskDebris.MODID
import org.teamvoided.dusk_debris.data.worldgen.structure.DuskStructurePools
import org.teamvoided.dusk_debris.structure.pool.CavityPoolElement
import java.util.*
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
            //cavity(procEmpty, 5),
            single("crystal_mineshaft/shaft/corridor_i_1", procEmpty, 10),
            single("crystal_mineshaft/shaft/corridor_l_1", procEmpty, 5),
            single("crystal_mineshaft/shaft/corridor_t_1", procEmpty, 5),
            single("crystal_mineshaft/shaft/corridor_x_1", procEmpty, 5)
        )
        val fallUp = c.register(
            DuskStructurePools.TEST_UP_FALL,
            poolEmpty,
            StructurePool.Projection.RIGID,
            single("crystal_mineshaft/shaft/vertical/roof_1", procEmpty),
            single("crystal_mineshaft/shaft/vertical/blocked_1", procEmpty)
        )
        c.register(
            DuskStructurePools.TEST_UP,
            fallUp,
            StructurePool.Projection.RIGID,
            single("crystal_mineshaft/shaft/vertical/crossing_i_1", procEmpty, 10),
            single("crystal_mineshaft/shaft/vertical/crossing_x_1", procEmpty, 5),
            single("crystal_mineshaft/shaft/vertical/roof_1", procEmpty, 3),
            single("crystal_mineshaft/shaft/vertical/blocked_1", procEmpty, 3)
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

    private fun cavity(
        processors: Holder<StructureProcessorList>,
        weight: Int = 1
    ): Pair<Function<StructurePool.Projection, out StructurePoolElement>, Int> =
        Pair(
            Function {
                CavityPoolElement(
                    processors,
                    it,
                    Optional.empty()//.of(LiquidSettings.IGNORE_WATERLOGGING)
                )
            },
            weight
        )

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