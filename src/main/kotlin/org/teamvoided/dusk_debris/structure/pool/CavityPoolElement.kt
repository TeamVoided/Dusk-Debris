package org.teamvoided.dusk_debris.structure.pool

import com.google.common.annotations.VisibleForTesting
import com.google.common.collect.Lists
import com.mojang.datafixers.util.Either
import com.mojang.serialization.DataResult
import com.mojang.serialization.DynamicOps
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.block.Block
import net.minecraft.block.Blocks
import net.minecraft.block.JigsawBlock
import net.minecraft.block.entity.JigsawBlockEntity
import net.minecraft.block.enums.JigsawOrientation
import net.minecraft.nbt.NbtCompound
import net.minecraft.registry.Holder
import net.minecraft.structure.Structure
import net.minecraft.structure.StructureManager
import net.minecraft.structure.StructurePlacementData
import net.minecraft.structure.StructureTemplateManager
import net.minecraft.structure.pool.StructurePool
import net.minecraft.structure.pool.StructurePoolElement
import net.minecraft.structure.pool.StructurePoolElementType
import net.minecraft.structure.processor.BlockIgnoreStructureProcessor
import net.minecraft.structure.processor.JigsawReplacementStructureProcessor
import net.minecraft.structure.processor.StructureProcessorList
import net.minecraft.structure.processor.StructureProcessorType
import net.minecraft.util.BlockMirror
import net.minecraft.util.BlockRotation
import net.minecraft.util.Identifier
import net.minecraft.util.Nullables
import net.minecraft.util.math.*
import net.minecraft.util.random.RandomGenerator
import net.minecraft.world.StructureWorldAccess
import net.minecraft.world.gen.chunk.ChunkGenerator
import net.minecraft.world.gen.feature.LiquidSettings
import org.teamvoided.dusk_debris.init.worldgen.structure.DuskStructurePoolElementType
import java.util.*
import kotlin.math.absoluteValue

class CavityPoolElement(
    //protected val template: Either<Identifier, Structure>,
    private val processors: Holder<StructureProcessorList>,
    projection: StructurePool.Projection,
    private val overrideLiquidSettings: Optional<LiquidSettings>
) : StructurePoolElement(projection) {
    private var blockBox: BlockBox? = null

    override fun getType(): StructurePoolElementType<*> {
        return DuskStructurePoolElementType.CAVITY
    }

    override fun getStart(structureTemplateManager: StructureTemplateManager, rotation: BlockRotation): Vec3i {
        println("getStart")
        val blockBox = this.blockBox!!
        return Vec3i(blockBox.blockCountX, blockBox.blockCountY, blockBox.blockCountZ)
    }

    override fun getStructureBlockInfos(
        structureTemplateManager: StructureTemplateManager,
        pos: BlockPos,
        rotation: BlockRotation,
        random: RandomGenerator
    ): List<Structure.StructureBlockInfo> {
        println("getStructureBlockInfos")
        if (blockBox == null)
            createBoxForStructure(structureTemplateManager, pos, rotation, random)

        val list: MutableList<Structure.StructureBlockInfo> = Lists.newArrayList()
        list.add(
            Structure.StructureBlockInfo(
                pos,
                Blocks.JIGSAW.defaultState.with(
                    JigsawBlock.ORIENTATION,
                    JigsawOrientation.byDirections(Direction.DOWN, Direction.SOUTH)
                ),
                this.createJigsawNbt()
            )
        )
        return list
    }


    override fun getBoundingBox(
        structureTemplateManager: StructureTemplateManager,
        pos: BlockPos,
        rotation: BlockRotation
    ): BlockBox {
        println("getBoundingBox")
        if (blockBox == null)
            createBoxForStructure(structureTemplateManager, pos, rotation)
        return this.blockBox!!
    }

    private fun createBoxForStructure(
        structureTemplateManager: StructureTemplateManager,
        pos: BlockPos,
        rotation: BlockRotation,
        random: RandomGenerator = RandomGenerator.createLegacy(pos.asLong() + rotation.ordinal)
    ) {
        println("createBoxForStructure")
        val x = 32 //(random.nextInt(24) + 8)
        val y = 17 //(random.nextInt(24) + 8)
        val z = 1 //(random.nextInt(24) + 8)
        //val x = (Math.random() * 24 + 8).toInt()
        //val y = (Math.random() * 24 + 8).toInt()
        //val z = (Math.random() * 24 + 8).toInt()

        val size: Vec3i = Vec3i(x, y, z).add(-1, -1, -1)
        val blockPos = Structure.transformAround(BlockPos.ORIGIN, BlockMirror.NONE, rotation, BlockPos.ORIGIN)
        val blockPos2 =
            Structure.transformAround(BlockPos.ORIGIN.add(size), BlockMirror.NONE, rotation, BlockPos.ORIGIN)
        this.blockBox = BlockBox.create(blockPos, blockPos2).offset(pos.x, pos.y, pos.z)
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
        if (this.blockBox != null) {
            val blockBox: BlockBox = this.blockBox!!
            val structurePlacementData = this.createPlacementData(rotation, box, liquidSettings, keepJigsaws)
            createCavity(structurePlacementData, world, blockBox, 18)
            this.blockBox = null
            world.setBlockState(pos, Blocks.GOLD_BLOCK.defaultState, 3)
            return true
        } else {
            throw IllegalStateException("Invalid call to CavityPoolElement.generate, blockBox is null! Call the CavityPoolElement.createBoxForStructure function before CavityPoolElement.generate!")
        }

    }

    private fun createCavity(
        placementData: StructurePlacementData,
        world: StructureWorldAccess,
        blockBox: BlockBox,
        flags: Int
    ) {
        val boxCenter = Vec3d(
            this.blockBox!!.minX + (this.blockBox!!.blockCountX / 2.0),
            this.blockBox!!.minY + (this.blockBox!!.blockCountY / 2.0),
            this.blockBox!!.minZ + (this.blockBox!!.blockCountZ / 2.0)
        )
        for (y in blockBox.minY..blockBox.maxY) {
            for (x in blockBox.minX..blockBox.maxX) {
                for (z in blockBox.minZ..blockBox.maxZ) {
                    val setPos = BlockPos(x, y, z)
                    val blockState = if (getSquaredDistance(setPos, blockBox, boxCenter) <= 1) {
                        Blocks.TINTED_GLASS.defaultState
                    } else {
                        Blocks.DIAMOND_BLOCK.defaultState
                    }
                    world.setBlockState(setPos, blockState, flags)
                    updateCavityState(placementData, world, setPos, flags)
                }
            }
        }
    }

    private fun updateCavityState(
        placementData: StructurePlacementData,
        world: StructureWorldAccess,
        pos: BlockPos,
        flags: Int
    ) {
        if (!placementData.shouldUpdateNeighbors()) {
            val worldState = world.getBlockState(pos)
            val afterUpdateState = Block.postProcessState(worldState, world, pos)
            if (worldState != afterUpdateState) {
                world.setBlockState(pos, afterUpdateState, flags and -2 or 16)
            }
            world.updateNeighbors(pos, afterUpdateState.block)
        }
    }


    private fun getSquaredDistance(pos: BlockPos, blockBox: BlockBox, centerBox: Vec3d): Double {
        val distance = pos.ofCenter().subtract(centerBox)
        val x = (distance.x / (blockBox.blockCountX / 2))
        val y = (distance.y / (blockBox.blockCountY / 2))
        val z = (distance.z / (blockBox.blockCountZ / 2))
        return (x * x + y * y + z * z)
    }

    private fun createPlacementData(
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
        if (!keepJigsaws) structurePlacementData.addProcessor(JigsawReplacementStructureProcessor.INSTANCE)
        Objects.requireNonNull(structurePlacementData)
        processors.value().list.forEach { structurePlacementData.addProcessor(it) }
        Objects.requireNonNull(structurePlacementData)
        projection.processors.forEach { structurePlacementData.addProcessor(it) }
        return structurePlacementData
    }

    private fun createJigsawNbt(
        name: String = "minecraft:bottom",
        finalState: String = "minecraft:air",
        pool: String = "minecraft:empty",
        target: String = "minecraft:empty",
        joint: String = JigsawBlockEntity.Joint.ROLLABLE.asString()
    ): NbtCompound {
        val nbtCompound = NbtCompound()
        nbtCompound.putString("name", name)
        nbtCompound.putString("final_state", finalState)
        nbtCompound.putString("pool", pool)
        nbtCompound.putString("target", target)
        nbtCompound.putString("joint", joint)
        return nbtCompound
    }

    override fun toString(): String {
        return "Cavity[$blockBox]"
    }

    companion object {
        val CODEC: MapCodec<CavityPoolElement>
        private fun <T> encodeTemplate(
            template: Either<Identifier, Structure>,
            ops: DynamicOps<T>,
            value: T
        ): DataResult<T> {
            val optional = template.left()
            return if (optional.isEmpty) DataResult.error { "Can not serialize a runtime pool element" }
            else Identifier.CODEC.encode(optional.get(), ops, value)
        }

        protected fun processorsCodec(): RecordCodecBuilder<CavityPoolElement, Holder<StructureProcessorList>> {
            return StructureProcessorType.REGISTRY_CODEC.fieldOf("processors")
                .forGetter { it!!.processors }
        }

        protected fun liquidSettingsCodec(): RecordCodecBuilder<CavityPoolElement, Optional<LiquidSettings>> {
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
