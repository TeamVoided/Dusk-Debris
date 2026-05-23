package org.teamvoided.dusk_debris.world.gen.structure

import com.mojang.serialization.Codec
import com.mojang.serialization.DataResult
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.Holder
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.EmptyBlockGetter
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.levelgen.WorldGenerationContext
import net.minecraft.world.level.levelgen.heightproviders.HeightProvider
import net.minecraft.world.level.levelgen.structure.Structure
import net.minecraft.world.level.levelgen.structure.StructureType
import net.minecraft.world.level.levelgen.structure.TerrainAdjustment
import net.minecraft.world.level.levelgen.structure.pools.DimensionPadding
import net.minecraft.world.level.levelgen.structure.pools.JigsawPlacement
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool
import net.minecraft.world.level.levelgen.structure.pools.alias.PoolAliasBinding
import net.minecraft.world.level.levelgen.structure.pools.alias.PoolAliasLookup
import net.minecraft.world.level.levelgen.structure.structures.JigsawStructure
import net.minecraft.world.level.levelgen.structure.templatesystem.LiquidSettings
import org.teamvoided.dusk_debris.init.worldgen.structure.DuskStructureType
import java.util.*

class CaveJigsawStructureFeature(
    settings: StructureSettings,
    private val startPool: Holder<StructureTemplatePool>,
    private val startJigsawName: Optional<ResourceLocation>,
    private val size: Int,
    private val startHeight: HeightProvider,
    private val bottomUpSearch: Boolean = false,
    private val placeIfReachRange: Boolean = false,
    private val maxDistanceFromCenter: Int = 80,
    private val poolAliases: List<PoolAliasBinding> = listOf(),
    private val dimensionPadding: DimensionPadding = JigsawStructure.DEFAULT_DIMENSION_PADDING,
    private val liquidSettings: LiquidSettings = JigsawStructure.DEFAULT_LIQUID_SETTINGS
) : Structure(settings) {
    constructor(
        settings: StructureSettings,
        startPool: Holder<StructureTemplatePool>,
        size: Int,
        startHeight: HeightProvider,
        bottomUpSearch: Boolean = false,
        placeIfReachRange: Boolean = false,
        dimensionPadding: DimensionPadding = JigsawStructure.DEFAULT_DIMENSION_PADDING,
        liquidSettings: LiquidSettings = JigsawStructure.DEFAULT_LIQUID_SETTINGS
    ) : this(
        settings,
        startPool,
        Optional.empty(),
        size,
        startHeight,
        bottomUpSearch,
        placeIfReachRange,
        80,
        listOf<PoolAliasBinding>(),
        dimensionPadding,
        liquidSettings
    )

    public override fun findGenerationPoint(context: GenerationContext): Optional<GenerationStub> {
        val chunkPos = context.chunkPos()
        val startHeight = startHeight.sample(
            context.random(),
            WorldGenerationContext(context.chunkGenerator(), context.heightAccessor())
        )

        var posY = startHeight
        val minY = dimensionPadding.bottom
        val maxY = dimensionPadding.top

        val verticalBlockSample = context.chunkGenerator()
            .getBaseColumn(chunkPos.minBlockX, chunkPos.minBlockZ, context.heightAccessor(), context.randomState())

        val mutablePos = BlockPos.MutableBlockPos(chunkPos.minBlockX, posY, chunkPos.minBlockZ)

        if (bottomUpSearch) {
            while (posY < maxY) {
                val blockStateSolid = verticalBlockSample.getBlock(posY)
                ++posY
                val blockStateAir = verticalBlockSample.getBlock(posY)
                mutablePos.setY(posY)
                if (checkIfCanPlace(mutablePos, blockStateSolid, blockStateAir)) {
                    break
                }
            }
        } else {
            while (posY > minY) {
                val blockStateAir = verticalBlockSample.getBlock(posY)
                --posY
                val blockStateSolid = verticalBlockSample.getBlock(posY)
                mutablePos.setY(posY)
                if (checkIfCanPlace(mutablePos, blockStateSolid, blockStateAir)) {
                    break
                }
            }
        }

        if (!placeIfReachRange && (posY <= minY || posY >= maxY)) {
            return Optional.empty()
        } else {
            return JigsawPlacement.addPieces(
                context,
                this.startPool,
                this.startJigsawName,
                this.size,
                mutablePos,
                false,
                Optional.empty(),
                this.maxDistanceFromCenter,
                PoolAliasLookup.create(this.poolAliases, mutablePos, context.seed()),
                this.dimensionPadding,
                this.liquidSettings
            )
        }
    }

    private fun checkIfCanPlace(pos: BlockPos.MutableBlockPos, solid: BlockState, air: BlockState): Boolean {
        val airCheck = air.isAir
        val solidCheck = (solid.isFaceSturdy(EmptyBlockGetter.INSTANCE, pos, Direction.UP) ||
                solid.`is`(Blocks.SOUL_SAND))
        return airCheck && solidCheck
    }

    override fun type(): StructureType<*> = DuskStructureType.CAVE_JIGSAW

    companion object {
        private val RAW_CODEC: MapCodec<CaveJigsawStructureFeature> =
            RecordCodecBuilder.mapCodec { instance ->
                instance.group(
                    settingsCodec(instance),
                    StructureTemplatePool.CODEC.fieldOf("start_pool").forGetter { it.startPool },
                    ResourceLocation.CODEC.optionalFieldOf("start_jigsaw_name").forGetter { it.startJigsawName },
                    Codec.intRange(0, JigsawStructure.MAX_DEPTH).fieldOf("size").forGetter { it.size },
                    HeightProvider.CODEC.fieldOf("start_height").forGetter { it.startHeight },
                    Codec.BOOL.fieldOf("use_inverted_search").orElse(false).forGetter { it.bottomUpSearch },
                    Codec.BOOL.fieldOf("place_if_reach_range").orElse(false).forGetter { it.placeIfReachRange },
                    Codec.intRange(1, 128).fieldOf("max_distance_from_center").forGetter { it.maxDistanceFromCenter },
                    Codec.list(PoolAliasBinding.CODEC)
                        .optionalFieldOf("pool_aliases", listOf<PoolAliasBinding>())
                        .forGetter { it.poolAliases },
                    DimensionPadding.CODEC.optionalFieldOf("dimension_padding", JigsawStructure.DEFAULT_DIMENSION_PADDING)
                        .forGetter { it.dimensionPadding },
                    LiquidSettings.CODEC.optionalFieldOf("liquid_settings", JigsawStructure.DEFAULT_LIQUID_SETTINGS)
                        .forGetter { it.liquidSettings }
                ).apply(instance, ::CaveJigsawStructureFeature)
            }
        val CODEC: MapCodec<CaveJigsawStructureFeature> = RAW_CODEC.validate { verifyRange(it) }

        private fun verifyRange(feature: CaveJigsawStructureFeature): DataResult<CaveJigsawStructureFeature> {
            val var10000: Byte = when (feature.terrainAdaptation()) {
                TerrainAdjustment.NONE -> 0
                TerrainAdjustment.BURY, TerrainAdjustment.BEARD_THIN, TerrainAdjustment.BEARD_BOX, TerrainAdjustment.ENCAPSULATE -> 12
                else -> 12 //throw MatchException(null, null)
            }
            val i = var10000.toInt()
            return if (feature.maxDistanceFromCenter + i > JigsawStructure.MAX_TOTAL_STRUCTURE_RANGE)
                DataResult.error { "Structure size including terrain adaptation must not exceed 128" }
            else
                DataResult.success(feature)
        }
    }
}
