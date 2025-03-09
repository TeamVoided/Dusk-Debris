package org.teamvoided.dusk_debris.world.gen.structure

import com.mojang.serialization.Codec
import com.mojang.serialization.DataResult
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.block.Blocks
import net.minecraft.registry.Holder
import net.minecraft.structure.StructureType
import net.minecraft.structure.pool.StructurePool
import net.minecraft.structure.pool.StructurePoolBasedGenerator
import net.minecraft.structure.pool.alias.StructurePoolAliasBinding
import net.minecraft.structure.pool.alias.StructurePoolAliasLookup
import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.EmptyBlockView
import net.minecraft.world.Heightmap
import net.minecraft.world.gen.HeightContext
import net.minecraft.world.gen.feature.DimensionPadding
import net.minecraft.world.gen.feature.JigsawFeature
import net.minecraft.world.gen.feature.LiquidSettings
import net.minecraft.world.gen.feature.StructureFeature
import net.minecraft.world.gen.heightprovider.HeightProvider
import net.minecraft.world.gen.structure.TerrainAdjustment
import org.teamvoided.dusk_debris.init.worldgen.DuskStructureType
import java.util.*

class CaveJigsawStructureFeature(
    settings: StructureSettings,
    private val startPool: Holder<StructurePool>,
    private val startJigsawName: Optional<Identifier>,
    private val size: Int,
    private val startHeight: HeightProvider,
    private val useExpansionHack: Boolean = false,
    private val maxDistanceFromCenter: Int = 80,
    private val poolAliases: List<StructurePoolAliasBinding> = listOf(),
    private val dimensionPadding: DimensionPadding = JigsawFeature.DEFAULT_PADDING,
    private val liquidSettings: LiquidSettings = JigsawFeature.DEFAULT_LIQUID_SETTING
) : StructureFeature(settings) {
    constructor(
        settings: StructureSettings,
        startPool: Holder<StructurePool>,
        size: Int,
        startHeight: HeightProvider,
        dimensionPadding: DimensionPadding
    ) : this(
        settings,
        startPool,
        Optional.empty(),
        size,
        startHeight,
        false,
        80,
        listOf<StructurePoolAliasBinding>(),
        dimensionPadding,
        JigsawFeature.DEFAULT_LIQUID_SETTING
    )

    constructor(
        settings: StructureSettings,
        startPool: Holder<StructurePool>,
        size: Int,
        startHeight: HeightProvider,
        useExpansionHack: Boolean = false
    ) : this(
        settings,
        startPool,
        Optional.empty(),
        size,
        startHeight,
        useExpansionHack,
        80,
        listOf<StructurePoolAliasBinding>(),
        JigsawFeature.DEFAULT_PADDING,
        JigsawFeature.DEFAULT_LIQUID_SETTING
    )

    public override fun findGenerationPos(context: GenerationContext): Optional<GenerationStub> {
        val chunkPos = context.chunkPos()
        val startHeight = startHeight[context.random(), HeightContext(context.chunkGenerator(), context.world())]

        var posY = startHeight
        val minY = dimensionPadding.bottom

        val verticalBlockSample = context.chunkGenerator().getColumnSample(
            chunkPos.startX,
            chunkPos.startZ,
            context.world(),
            context.randomState()
        )
        val mutable = BlockPos.Mutable(chunkPos.startX, posY, chunkPos.startZ)

        while (posY > minY) {
            val blockState = verticalBlockSample.getState(posY)
            --posY
            val blockState2 = verticalBlockSample.getState(posY)
            if (blockState.isAir &&
                (blockState2.isSideSolidFullSquare(EmptyBlockView.INSTANCE, mutable.setY(posY), Direction.UP) ||
                        blockState2.isOf(Blocks.SOUL_SAND)) &&
                !blockState2.isOf(Blocks.BEDROCK)
            ) {
                break
            }
        }

        if (posY <= minY) {
            return Optional.empty()
        } else {
            val blockPos = BlockPos(chunkPos.startX, posY, chunkPos.startZ)
            return StructurePoolBasedGenerator.method_30419(
                context,
                this.startPool,
                this.startJigsawName,
                this.size,
                blockPos,
                this.useExpansionHack,
                Optional.empty(),
                this.maxDistanceFromCenter,
                StructurePoolAliasLookup.create(this.poolAliases, blockPos, context.seed()),
                this.dimensionPadding,
                this.liquidSettings
            )
        }
    }

    override fun getType(): StructureType<*> {
        return DuskStructureType.CAVE_JIGSAW
    }

    companion object {
        const val MAX_TOTAL_STRUCTURE_RANGE: Int = 128
        const val MIN_DEPTH: Int = 0
        const val MAX_DEPTH: Int = 20
        private val RAW_CODEC: MapCodec<CaveJigsawStructureFeature> =
            RecordCodecBuilder.mapCodec { instance ->
                instance.group(
                    settingsCodec(instance),
                    StructurePool.REGISTRY_CODEC.fieldOf("start_pool").forGetter { it.startPool },
                    Identifier.CODEC.optionalFieldOf("start_jigsaw_name").forGetter { it.startJigsawName },
                    Codec.intRange(0, 20).fieldOf("size").forGetter { it.size },
                    HeightProvider.CODEC.fieldOf("start_height").forGetter { it.startHeight },
                    Codec.BOOL.fieldOf("use_expansion_hack").forGetter { it.useExpansionHack },
                    Codec.intRange(1, 128).fieldOf("max_distance_from_center").forGetter { it.maxDistanceFromCenter },
                    Codec.list(StructurePoolAliasBinding.CODEC)
                        .optionalFieldOf("pool_aliases", listOf<StructurePoolAliasBinding>())
                        .forGetter { it.poolAliases },
                    DimensionPadding.CODEC.optionalFieldOf("dimension_padding", JigsawFeature.DEFAULT_PADDING)
                        .forGetter { it.dimensionPadding },
                    LiquidSettings.codec.optionalFieldOf("liquid_settings", JigsawFeature.DEFAULT_LIQUID_SETTING)
                        .forGetter { it.liquidSettings }
                ).apply(instance, ::CaveJigsawStructureFeature)
            }
        val CODEC: MapCodec<CaveJigsawStructureFeature> = RAW_CODEC.validate { verifyRange(it) }

        private fun verifyRange(feature: CaveJigsawStructureFeature): DataResult<CaveJigsawStructureFeature> {
            val var10000: Byte = when (feature.terrainAdaptation) {
                TerrainAdjustment.NONE -> 0
                TerrainAdjustment.BURY, TerrainAdjustment.BEARD_THIN, TerrainAdjustment.BEARD_BOX, TerrainAdjustment.ENCAPSULATE -> 12
                else -> throw MatchException(null, null)
            }
            val i = var10000.toInt()
            return if (feature.maxDistanceFromCenter + i > 128) DataResult.error { "Structure size including terrain adaptation must not exceed 128" } else DataResult.success(
                feature
            )
        }
    }
}
