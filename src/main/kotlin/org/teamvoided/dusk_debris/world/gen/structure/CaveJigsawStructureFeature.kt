package org.teamvoided.dusk_debris.world.gen.structure

import com.mojang.serialization.Codec
import com.mojang.serialization.DataResult
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.block.BlockState
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
    private val bottomUpSearch: Boolean = false,
    private val placeIfReachRange: Boolean = false,
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
        bottomUpSearch: Boolean = false,
        placeIfReachRange: Boolean = false,
        dimensionPadding: DimensionPadding = JigsawFeature.DEFAULT_PADDING,
        liquidSettings: LiquidSettings = JigsawFeature.DEFAULT_LIQUID_SETTING
    ) : this(
        settings,
        startPool,
        Optional.empty(),
        size,
        startHeight,
        bottomUpSearch,
        placeIfReachRange,
        80,
        listOf<StructurePoolAliasBinding>(),
        dimensionPadding,
        liquidSettings
    )

    public override fun findGenerationPos(context: GenerationContext): Optional<GenerationStub> {
        val chunkPos = context.chunkPos()
        val startHeight = startHeight[context.random(), HeightContext(context.chunkGenerator(), context.world())]

        var posY = startHeight
        val minY = dimensionPadding.bottom
        val maxY = dimensionPadding.top

        val verticalBlockSample = context.chunkGenerator()
            .getColumnSample(chunkPos.startX, chunkPos.startZ, context.world(), context.randomState())

        val mutablePos = BlockPos.Mutable(chunkPos.startX, posY, chunkPos.startZ)

        if (bottomUpSearch) {
            while (posY < maxY) {
                val blockStateSolid = verticalBlockSample.getState(posY)
                ++posY
                val blockStateAir = verticalBlockSample.getState(posY)
                mutablePos.setY(posY)
                if (checkIfCanPlace(mutablePos, blockStateSolid, blockStateAir)) {
                    break
                }
            }
        } else {
            while (posY > minY) {
                val blockStateAir = verticalBlockSample.getState(posY)
                --posY
                val blockStateSolid = verticalBlockSample.getState(posY)
                mutablePos.setY(posY)
                if (checkIfCanPlace(mutablePos, blockStateSolid, blockStateAir)) {
                    break
                }
            }
        }

        if (!placeIfReachRange && (posY <= minY || posY >= maxY)) {
            return Optional.empty()
        } else {
            return StructurePoolBasedGenerator.method_30419(
                context,
                this.startPool,
                this.startJigsawName,
                this.size,
                mutablePos,
                false,
                Optional.empty(),
                this.maxDistanceFromCenter,
                StructurePoolAliasLookup.create(this.poolAliases, mutablePos, context.seed()),
                this.dimensionPadding,
                this.liquidSettings
            )
        }
    }

    private fun checkIfCanPlace(pos: BlockPos.Mutable, solid: BlockState, air: BlockState): Boolean {
        val airCheck = air.isAir
        val solidCheck = (solid.isSideSolidFullSquare(EmptyBlockView.INSTANCE, pos, Direction.UP) ||
                solid.isOf(Blocks.SOUL_SAND))
        return airCheck && solidCheck
    }

    override fun getType(): StructureType<*> = DuskStructureType.CAVE_JIGSAW

    companion object {
        private val RAW_CODEC: MapCodec<CaveJigsawStructureFeature> =
            RecordCodecBuilder.mapCodec { instance ->
                instance.group(
                    settingsCodec(instance),
                    StructurePool.REGISTRY_CODEC.fieldOf("start_pool").forGetter { it.startPool },
                    Identifier.CODEC.optionalFieldOf("start_jigsaw_name").forGetter { it.startJigsawName },
                    Codec.intRange(0, JigsawFeature.MAX_DEPTH).fieldOf("size").forGetter { it.size },
                    HeightProvider.CODEC.fieldOf("start_height").forGetter { it.startHeight },
                    Codec.BOOL.fieldOf("use_inverted_search").orElse(false).forGetter { it.bottomUpSearch },
                    Codec.BOOL.fieldOf("place_if_reach_range").orElse(false).forGetter { it.placeIfReachRange },
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
                else -> 12 //throw MatchException(null, null)
            }
            val i = var10000.toInt()
            return if (feature.maxDistanceFromCenter + i > JigsawFeature.MAX_TOTAL_STRUCTURE_RANGE)
                DataResult.error { "Structure size including terrain adaptation must not exceed 128" }
            else
                DataResult.success(feature)
        }
    }
}
