package org.teamvoided.dusk_debris.data.gen.world.gen.structure

import com.mojang.datafixers.util.Pair
import net.minecraft.core.Holder
import net.minecraft.core.registries.Registries
import net.minecraft.data.worldgen.BootstrapContext
import net.minecraft.data.worldgen.Pools
import net.minecraft.data.worldgen.ProcessorLists
import net.minecraft.resources.ResourceKey
import net.minecraft.world.level.levelgen.placement.PlacedFeature
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList
import org.teamvoided.dusk_debris.DuskDebris.MODID
import org.teamvoided.dusk_debris.data.worldgen.structure.DuskStructurePools
import org.teamvoided.dusk_debris.structure.pool.CavityPoolElement
import java.util.*
import java.util.function.Function

object StructurePoolCreator {
    fun bootstrap(c: BootstrapContext<StructureTemplatePool>) {
        val structurePools = c.lookup(Registries.TEMPLATE_POOL)
        val placedFeatures = c.lookup(Registries.PLACED_FEATURE)
        val procLists = c.lookup(Registries.PROCESSOR_LIST)

        val poolEmpty = structurePools.getOrThrow(Pools.EMPTY)
        val procEmpty = procLists.getOrThrow(ProcessorLists.EMPTY)

        c.register(
            DuskStructurePools.NETHER_FOSSILS,
            poolEmpty,
            StructureTemplatePool.Projection.RIGID,
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
            StructureTemplatePool.Projection.RIGID,
            //cavity(procEmpty, 5),
            single("crystal_mineshaft/shaft/corridor_i_1", procEmpty, 10),
            single("crystal_mineshaft/shaft/corridor_l_1", procEmpty, 5),
            single("crystal_mineshaft/shaft/corridor_t_1", procEmpty, 5),
            single("crystal_mineshaft/shaft/corridor_x_1", procEmpty, 5)
        )
        val fallUp = c.register(
            DuskStructurePools.TEST_UP_FALL,
            poolEmpty,
            StructureTemplatePool.Projection.RIGID,
            single("crystal_mineshaft/shaft/vertical/roof_1", procEmpty),
            single("crystal_mineshaft/shaft/vertical/blocked_1", procEmpty)
        )
        c.register(
            DuskStructurePools.TEST_UP,
            fallUp,
            StructureTemplatePool.Projection.RIGID,
            single("crystal_mineshaft/shaft/vertical/crossing_i_1", procEmpty, 10),
            single("crystal_mineshaft/shaft/vertical/crossing_x_1", procEmpty, 5),
            single("crystal_mineshaft/shaft/vertical/roof_1", procEmpty, 3),
            single("crystal_mineshaft/shaft/vertical/blocked_1", procEmpty, 3)
        )
    }

    fun BootstrapContext<StructureTemplatePool>.register(
        key: ResourceKey<StructureTemplatePool>,
        fallback: Holder.Reference<StructureTemplatePool>,
        projection: StructureTemplatePool.Projection,
        vararg piece: Pair<Function<StructureTemplatePool.Projection, out StructurePoolElement>, Int>
    ): Holder.Reference<StructureTemplatePool> {
        return this.register(key, StructureTemplatePool(fallback, piece.toList(), projection))
    }

    private fun id(str: String) = "$MODID:$str"
    private fun mc(str: String) = "minecraft:$str"

    private fun cavity(
        processors: Holder<StructureProcessorList>,
        weight: Int = 1
    ): Pair<Function<StructureTemplatePool.Projection, out StructurePoolElement>, Int> =
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
    ): Pair<Function<StructureTemplatePool.Projection, out StructurePoolElement>, Int> =
        Pair(StructurePoolElement.single(id(str), processors), weight)

    private fun legacySingle(
        str: String,
        processors: Holder<StructureProcessorList>,
        weight: Int = 1
    ): Pair<Function<StructureTemplatePool.Projection, out StructurePoolElement>, Int> =
        Pair(StructurePoolElement.legacy(id(str), processors), weight)

    private fun legacySingleMC(
        str: String,
        processors: Holder<StructureProcessorList>,
        weight: Int = 1
    ): Pair<Function<StructureTemplatePool.Projection, out StructurePoolElement>, Int> =
        Pair(StructurePoolElement.legacy(mc(str), processors), weight)


    private fun feature(
        placedFeatures: Holder<PlacedFeature>,
        weight: Int = 1
    ): Pair<Function<StructureTemplatePool.Projection, out StructurePoolElement>, Int> =
        Pair(StructurePoolElement.feature(placedFeatures), weight)
}