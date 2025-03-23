package org.teamvoided.dusk_debris.structure.pool

import com.google.common.annotations.VisibleForTesting
import com.google.common.collect.Lists
import com.mojang.datafixers.util.Either
import com.mojang.serialization.*
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.block.Blocks
import net.minecraft.block.enums.StructureBlockMode
import net.minecraft.nbt.NbtCompound
import net.minecraft.nbt.NbtList
import net.minecraft.registry.Holder
import net.minecraft.structure.Structure
import net.minecraft.structure.StructureManager
import net.minecraft.structure.StructurePlacementData
import net.minecraft.structure.StructureTemplateManager
import net.minecraft.structure.pool.StructurePool
import net.minecraft.structure.pool.StructurePoolElement
import net.minecraft.structure.pool.StructurePoolElementType
import net.minecraft.structure.processor.*
import net.minecraft.util.BlockRotation
import net.minecraft.util.Identifier
import net.minecraft.util.Nullables
import net.minecraft.util.Util
import net.minecraft.util.math.BlockBox
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3i
import net.minecraft.util.random.RandomGenerator
import net.minecraft.world.StructureWorldAccess
import net.minecraft.world.gen.chunk.ChunkGenerator
import net.minecraft.world.gen.feature.LiquidSettings
import java.util.*

class CavityPoolElement(
    //protected val template: Either<Identifier, Structure>,
    protected val processors: Holder<StructureProcessorList>,
    projection: StructurePool.Projection,
    protected val overrideLiquidSettings: Optional<LiquidSettings>
) : StructurePoolElement(projection) {
    var structure: Structure? = null
    override fun getType(): StructurePoolElementType<*> {
        return StructurePoolElementType.SINGLE_POOL_ELEMENT
    }

    override fun getStart(structureTemplateManager: StructureTemplateManager, rotation: BlockRotation): Vec3i {

        val structure = this.getStructure(structureTemplateManager)
        return structure.getRotatedSize(rotation)
    }

    private fun getStructure(structureTemplateManager: StructureTemplateManager): Structure {

        Objects.requireNonNull(structureTemplateManager)
        return Structure()
//        this.template.map(
//            { structureTemplateManager.getStructureOrBlank(it) },
//            Function.identity()
//        ) as Structure
    }


    private fun getOrCreateStructure(structureTemplateManager: StructureTemplateManager): Structure {
        if (structure == null) {

            val nbt = Structure().writeNbt(NbtCompound())
            nbt.put("blocks", NbtList())
            nbt.put("palette", NbtList())

            structureTemplateManager.createStructureFromNbt(nbt)
        }

        return structure!!
    }

    private fun getDataStructureBlocks(
        structureTemplateManager: StructureTemplateManager,
        pos: BlockPos?,
        rotation: BlockRotation?,
        mirroredAndRotated: Boolean
    ): List<Structure.StructureBlockInfo> {
        val structure = this.getStructure(structureTemplateManager)
        val structureBlockInfos: List<Structure.StructureBlockInfo> = structure.getInfosForBlock(
            pos,
            StructurePlacementData().setRotation(rotation), Blocks.STRUCTURE_BLOCK, mirroredAndRotated
        )
        val retorn: MutableList<Structure.StructureBlockInfo> = Lists.newArrayList()
        val infoIterator: Iterator<Structure.StructureBlockInfo> = structureBlockInfos.iterator()

        while (infoIterator.hasNext()) {
            val structureBlockInfo = infoIterator.next()
            val nbtCompound = structureBlockInfo.nbt()
            if (nbtCompound != null) {
                val structureBlockMode = StructureBlockMode.valueOf(nbtCompound.getString("mode"))
                if (structureBlockMode == StructureBlockMode.DATA) {
                    retorn.add(structureBlockInfo)
                }
            }
        }

        return retorn
    }

    override fun getStructureBlockInfos(
        structureTemplateManager: StructureTemplateManager,
        pos: BlockPos,
        rotation: BlockRotation,
        random: RandomGenerator
    ): List<Structure.StructureBlockInfo> {
        val structure = this.getStructure(structureTemplateManager)
        val objectArrayList = structure.getInfosForBlock(
            pos,
            StructurePlacementData().setRotation(rotation), Blocks.JIGSAW, true
        )
        Util.shuffle(objectArrayList, random)
        selectionPriority(objectArrayList)
        return objectArrayList
    }

    override fun getBoundingBox(
        structureTemplateManager: StructureTemplateManager,
        pos: BlockPos,
        rotation: BlockRotation
    ): BlockBox {
        val structure = this.getStructure(structureTemplateManager)
        return structure.calculateBoundingBox(StructurePlacementData().setRotation(rotation), pos)
    }

    override fun generate(
        structureTemplateManager: StructureTemplateManager,
        world: StructureWorldAccess,
        structureManager: StructureManager,
        chunkGenerator: ChunkGenerator,
        pos: BlockPos,
        pivot: BlockPos,
        rotation: BlockRotation,
        box: BlockBox,
        random: RandomGenerator,
        liquidSettings: LiquidSettings,
        keepJigsaws: Boolean
    ): Boolean {
        val structure = this.getStructure(structureTemplateManager)
        val structurePlacementData = this.createPlacementData(rotation, box, liquidSettings, keepJigsaws)
        if (!structure.place(world, pos, pivot, structurePlacementData, random, 18)) {
            return false
        } else {
            val list = Structure.process(
                world,
                pos,
                pivot,
                structurePlacementData,
                this.getDataStructureBlocks(structureTemplateManager, pos, rotation, false)
            )
            val var15: Iterator<Structure.StructureBlockInfo> = list.iterator()

            while (var15.hasNext()) {
                val structureBlockInfo = var15.next()
                this.handleDataMarker(world, structureBlockInfo, pos, rotation, random, box)
            }

            return true
        }
    }

    protected fun createPlacementData(
        rotation: BlockRotation,
        box: BlockBox,
        other: LiquidSettings,
        keepJigsaws: Boolean
    ): StructurePlacementData {
        val structurePlacementData = StructurePlacementData()
        structurePlacementData.setBoundingBox(box)
        structurePlacementData.setRotation(rotation)
        structurePlacementData.setUpdateNeighbors(true)
        structurePlacementData.setIgnoreEntities(false)
        structurePlacementData.addProcessor(BlockIgnoreStructureProcessor.IGNORE_STRUCTURE_BLOCKS)
        structurePlacementData.setInitializeEntities(true)
        structurePlacementData.method_61020(overrideLiquidSettings.orElse(other) as LiquidSettings)
        if (!keepJigsaws) {
            structurePlacementData.addProcessor(JigsawReplacementStructureProcessor.INSTANCE)
        }

        Objects.requireNonNull(structurePlacementData)
        (processors.value() as StructureProcessorList).list.forEach { structurePlacementData.addProcessor(it) }
        Objects.requireNonNull(structurePlacementData)
        projection.processors.forEach { structurePlacementData.addProcessor(it) }
        return structurePlacementData
    }

    override fun toString(): String {
//        return "Cavity[$template]"
        return "Cavity[TODO]"
    }

    companion object {
        val CODEC: MapCodec<CavityPoolElement>
        private fun <T> encodeTemplate(
            template: Either<Identifier, Structure>,
            ops: DynamicOps<T>,
            value: T
        ): DataResult<T> {
            val optional = template.left()
            return if (optional.isEmpty) DataResult.error { "Can not serialize a runtime pool element" } else
                Identifier.CODEC.encode(optional.get(), ops, value)
        }

        protected fun <E : CavityPoolElement?> processorsCodec(): RecordCodecBuilder<E, Holder<StructureProcessorList>> {
            return StructureProcessorType.REGISTRY_CODEC.fieldOf("processors")
                .forGetter { it!!.processors }
        }

        protected fun <E : CavityPoolElement?> liquidSettingsCodec(): RecordCodecBuilder<E, Optional<LiquidSettings>> {
            return LiquidSettings.codec.optionalFieldOf("override_liquid_settings")
                .forGetter { it!!.overrideLiquidSettings }
        }

//        protected fun <E : CavityPoolElement?> templateCodec(): RecordCodecBuilder<E, Either<Identifier, Structure>> {
//            return TEMPLATE_CODEC.fieldOf("location").forGetter { it!!.template }
//        }

        @VisibleForTesting
        fun selectionPriority(list: MutableList<Structure.StructureBlockInfo>) {
            list.sortWith(Comparator.comparingInt<Structure.StructureBlockInfo> { info ->
                Nullables.mapOrDefault(info.nbt(), { it.getInt("selection_priority") }, 0)!!
            }.reversed())
        }


        init {
            CODEC = RecordCodecBuilder.mapCodec {
                it.group(
                    //templateCodec(),
                    processorsCodec(),
                    projectionCodec(),
                    liquidSettingsCodec()
                ).apply(it, ::CavityPoolElement)
            }
        }
    }
}
