package org.teamvoided.dusk_debris.world.gen.structure

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.Holder
import net.minecraft.world.level.EmptyBlockGetter
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.Rotation
import net.minecraft.world.level.levelgen.WorldGenerationContext
import net.minecraft.world.level.levelgen.heightproviders.ConstantHeight
import net.minecraft.world.level.levelgen.heightproviders.HeightProvider
import net.minecraft.world.level.levelgen.structure.Structure
import net.minecraft.world.level.levelgen.structure.StructureType
import net.minecraft.world.level.levelgen.structure.pools.EmptyPoolElement
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool
import net.minecraft.world.level.levelgen.structure.templatesystem.LiquidSettings
import org.teamvoided.dusk_debris.init.worldgen.structure.DuskStructureType
import org.teamvoided.dusk_debris.world.gen.structure.piece.PoolNoJigsawStructurePiece
import java.util.*
import kotlin.math.floor

class CaveStructureFeature(
    settings: StructureSettings,
    val startPool: Holder<StructureTemplatePool>,
    val height: HeightProvider,
    val minHeightSearch: ConstantHeight
) : Structure(settings) {
    public override fun findGenerationPoint(context: GenerationContext): Optional<GenerationStub> {
        val chunkRandom = context.random()

        val structurePool = startPool.value()
        val structurePoolElement = structurePool.getRandomTemplate(chunkRandom)
        if (structurePoolElement is EmptyPoolElement) return Optional.empty()

        val rotation = Rotation.getRandom(chunkRandom)

//        val size = structurePoolElement.getStart(context.structureTemplateManager, rotation)

//        val offset = when (rotation) {
//            BlockRotation.NONE -> size2.x / 2.0 to size2.z / 2.0
//            BlockRotation.CLOCKWISE_90 -> -size2.x / 2.0 to size2.z / 2.0
//            BlockRotation.CLOCKWISE_180 -> -size2.x / 2.0 to -size2.z / 2.0
//            BlockRotation.COUNTERCLOCKWISE_90 -> size2.x / 2.0 to -size2.z / 2.0
//            else -> 0.0 to 0.0
//        }

        val posX = context.chunkPos().minBlockX + chunkRandom.nextInt(16)
        val posZ = context.chunkPos().minBlockZ + chunkRandom.nextInt(16)

        val size2 = structurePoolElement.getSize(context.structureTemplateManager, rotation)
        val sampleX = floor(posX + (size2.x / if (rotXNeg(rotation)) -2.0 else 2.0)).toInt()
        val sampleZ = floor(posZ + (size2.z / if (rotZNeg(rotation)) -2.0 else 2.0)).toInt()


        val heightContext = WorldGenerationContext(context.chunkGenerator(), context.heightAccessor())
        val minY = minHeightSearch.sample(chunkRandom, heightContext)
        var posY = height.sample(chunkRandom, heightContext)

        val verticalBlockSample = context.chunkGenerator().getBaseColumn(
            sampleX,
            sampleZ,
            context.heightAccessor(),
            context.randomState()
        )
        val mutable = BlockPos.MutableBlockPos(sampleX, posY, sampleZ)

        while (posY > minY) {
            val blockState = verticalBlockSample.getBlock(posY)
            --posY
            val blockState2 = verticalBlockSample.getBlock(posY)
            if (blockState.isAir &&
                (blockState2.isFaceSturdy(EmptyBlockGetter.INSTANCE, mutable.setY(posY), Direction.UP) ||
                        blockState2.`is`(Blocks.SOUL_SAND)) &&
                !blockState2.`is`(Blocks.BEDROCK)
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

    fun rotXNeg(r: Rotation): Boolean = r == Rotation.CLOCKWISE_180 || r == Rotation.CLOCKWISE_90
    fun rotZNeg(r: Rotation): Boolean = r == Rotation.CLOCKWISE_180 || r == Rotation.COUNTERCLOCKWISE_90

    override fun type(): StructureType<*> {
        return DuskStructureType.SIMPLE_POOL
    }

    companion object {
        val CODEC: MapCodec<CaveStructureFeature> =
            RecordCodecBuilder.mapCodec { instance ->
                instance.group(
                    settingsCodec(instance),
                    StructureTemplatePool.CODEC.fieldOf("start_pool").forGetter { it.startPool },
                    HeightProvider.CODEC.fieldOf("height").forGetter { it.height },
                    ConstantHeight.CODEC.fieldOf("min_height_search").forGetter { it.minHeightSearch }
                ).apply(instance, ::CaveStructureFeature)
            }
    }
}
