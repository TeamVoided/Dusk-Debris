package org.teamvoided.dusk_debris.world.gen.structure

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.block.Blocks
import net.minecraft.registry.Holder
import net.minecraft.structure.StructureType
import net.minecraft.structure.pool.SinglePoolElement
import net.minecraft.structure.pool.StructurePool
import net.minecraft.structure.processor.StructureProcessorList
import net.minecraft.structure.processor.StructureProcessorType
import net.minecraft.util.BlockRotation
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.EmptyBlockView
import net.minecraft.world.gen.HeightContext
import net.minecraft.world.gen.feature.LiquidSettings
import net.minecraft.world.gen.feature.StructureFeature
import net.minecraft.world.gen.heightprovider.ConstantHeightProvider
import net.minecraft.world.gen.heightprovider.HeightProvider
import org.teamvoided.dusk_debris.init.worldgen.DuskStructureType
import org.teamvoided.dusk_debris.world.gen.structure.piece.PoolNoJigsawStructurePiece
import java.util.*

class CaveStructureFeature(
    settings: StructureSettings,
    val startPool: Holder<StructurePool>,
    val height: HeightProvider,
    val minHeightSearch: ConstantHeightProvider
) : StructureFeature(settings) {
    public override fun findGenerationPos(context: GenerationContext): Optional<GenerationStub> {
        val chunkRandom = context.random()
        val posX = context.chunkPos().startX + chunkRandom.nextInt(16)
        val posZ = context.chunkPos().startZ + chunkRandom.nextInt(16)

        val structurePool = startPool.value()
        val structurePoolElement = structurePool.getRandomElement(chunkRandom)

        val heightContext = HeightContext(context.chunkGenerator(), context.world())
        val minY = minHeightSearch[chunkRandom, heightContext]
        var posY = height[chunkRandom, heightContext]
        val verticalBlockSample =
            context.chunkGenerator().getColumnSample(posX, posZ, context.world(), context.randomState())
        val mutable = BlockPos.Mutable(posX, posY, posZ)

        while (posY > minY) {
            val blockState = verticalBlockSample.getState(posY)
            --posY
            val blockState2 = verticalBlockSample.getState(posY)
            if (blockState.isAir &&
                (blockState2.isSideSolidFullSquare(EmptyBlockView.INSTANCE, mutable.setY(posY), Direction.UP) ||
                        blockState2.isOf(Blocks.SOUL_SAND))
            ) {
                break
            }
        }

        if (posY <= minY) {
            return Optional.empty()
        } else {
            val blockPos = BlockPos(posX, posY, posZ)
            val rotation = BlockRotation.random(chunkRandom)
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
