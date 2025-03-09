package org.teamvoided.dusk_debris.world.gen.structure

import com.google.common.collect.Lists
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.block.Blocks
import net.minecraft.registry.Holder
import net.minecraft.structure.StructurePiecesCollector
import net.minecraft.structure.StructureType
import net.minecraft.structure.piece.PoolStructurePiece
import net.minecraft.structure.pool.EmptyPoolElement
import net.minecraft.structure.pool.StructurePool
import net.minecraft.structure.pool.StructurePoolBasedGenerator
import net.minecraft.util.BlockRotation
import net.minecraft.util.function.BooleanBiFunction
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Box
import net.minecraft.util.math.Direction
import net.minecraft.util.shape.VoxelShapes
import net.minecraft.world.EmptyBlockView
import net.minecraft.world.gen.HeightContext
import net.minecraft.world.gen.feature.LiquidSettings
import net.minecraft.world.gen.feature.StructureFeature
import net.minecraft.world.gen.heightprovider.ConstantHeightProvider
import net.minecraft.world.gen.heightprovider.HeightProvider
import org.teamvoided.dusk_debris.init.worldgen.DuskStructureType
import org.teamvoided.dusk_debris.world.gen.structure.piece.PoolNoJigsawStructurePiece
import java.util.*
import java.util.function.Consumer
import kotlin.math.floor
import kotlin.math.max
import kotlin.math.min

class CaveStructureFeature(
    settings: StructureSettings,
    val startPool: Holder<StructurePool>,
    val height: HeightProvider,
    val minHeightSearch: ConstantHeightProvider
) : StructureFeature(settings) {
    public override fun findGenerationPos(context: GenerationContext): Optional<GenerationStub> {
        val chunkRandom = context.random()

        val structurePool = startPool.value()
        val structurePoolElement = structurePool.getRandomElement(chunkRandom)
        if (structurePoolElement is EmptyPoolElement) return Optional.empty()

        val rotation = BlockRotation.random(chunkRandom)

//        val size = structurePoolElement.getStart(context.structureTemplateManager, rotation)

//        val offset = when (rotation) {
//            BlockRotation.NONE -> size2.x / 2.0 to size2.z / 2.0
//            BlockRotation.CLOCKWISE_90 -> -size2.x / 2.0 to size2.z / 2.0
//            BlockRotation.CLOCKWISE_180 -> -size2.x / 2.0 to -size2.z / 2.0
//            BlockRotation.COUNTERCLOCKWISE_90 -> size2.x / 2.0 to -size2.z / 2.0
//            else -> 0.0 to 0.0
//        }

        val posX = context.chunkPos().startX + chunkRandom.nextInt(16)
        val posZ = context.chunkPos().startZ + chunkRandom.nextInt(16)

        val size2 = structurePoolElement.getStart(context.structureTemplateManager, rotation)
        val sampleX = floor(posX + (size2.x / if (rotXNeg(rotation)) -2.0 else 2.0)).toInt()
        val sampleZ = floor(posZ + (size2.z / if (rotZNeg(rotation)) -2.0 else 2.0)).toInt()


        val heightContext = HeightContext(context.chunkGenerator(), context.world())
        val minY = minHeightSearch[chunkRandom, heightContext]
        var posY = height[chunkRandom, heightContext]

        val verticalBlockSample = context.chunkGenerator().getColumnSample(
            sampleX,
            sampleZ,
            context.world(),
            context.randomState()
        )
        val mutable = BlockPos.Mutable(sampleX, posY, sampleZ)

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
            val blockPos = BlockPos(posX, posY, posZ)
            val box = structurePoolElement.getBoundingBox(context.structureTemplateManager, blockPos, rotation)
            return Optional.of(GenerationStub(blockPos) {
                it.addPiece(
                    PoolNoJigsawStructurePiece(
                        context.structureTemplateManager,
                        structurePoolElement,
                        blockPos,
                        structurePoolElement.groundLevelDelta,
                        rotation,
                        box,
                        LiquidSettings.APPLY_WATERLOGGING
                    )
                )
            })
        }
    }

    fun rotXNeg(r: BlockRotation): Boolean = r == BlockRotation.CLOCKWISE_180 || r == BlockRotation.CLOCKWISE_90
    fun rotZNeg(r: BlockRotation): Boolean = r == BlockRotation.CLOCKWISE_180 || r == BlockRotation.COUNTERCLOCKWISE_90

    override fun getType(): StructureType<*> {
        return DuskStructureType.SIMPLE_POOL
    }

    companion object {
        val CODEC: MapCodec<CaveStructureFeature> =
            RecordCodecBuilder.mapCodec { instance ->
                instance.group(
                    settingsCodec(instance),
                    StructurePool.REGISTRY_CODEC.fieldOf("start_pool").forGetter { it.startPool },
                    HeightProvider.CODEC.fieldOf("height").forGetter { it.height },
                    ConstantHeightProvider.CODEC.fieldOf("min_height_search").forGetter { it.minHeightSearch }
                ).apply(instance, ::CaveStructureFeature)
            }
    }
}
