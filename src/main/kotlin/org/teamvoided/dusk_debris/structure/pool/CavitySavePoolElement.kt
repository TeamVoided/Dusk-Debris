//package org.teamvoided.dusk_debris.structure.pool
//
//import com.google.common.annotations.VisibleForTesting
//import com.google.common.collect.Lists
//import com.mojang.datafixers.util.Either
//import com.mojang.serialization.DataResult
//import com.mojang.serialization.DynamicOps
//import com.mojang.serialization.MapCodec
//import com.mojang.serialization.codecs.RecordCodecBuilder
//import net.minecraft.block.Blocks
//import net.minecraft.block.JigsawBlock
//import net.minecraft.block.entity.JigsawBlockEntity
//import net.minecraft.block.enums.JigsawOrientation
//import net.minecraft.nbt.NbtCompound
//import net.minecraft.registry.Holder
//import net.minecraft.structure.Structure
//import net.minecraft.structure.StructureManager
//import net.minecraft.structure.StructurePlacementData
//import net.minecraft.structure.StructureTemplateManager
//import net.minecraft.structure.pool.StructurePool
//import net.minecraft.structure.pool.StructurePoolElement
//import net.minecraft.structure.pool.StructurePoolElementType
//import net.minecraft.structure.processor.BlockIgnoreStructureProcessor
//import net.minecraft.structure.processor.JigsawReplacementStructureProcessor
//import net.minecraft.structure.processor.StructureProcessorList
//import net.minecraft.structure.processor.StructureProcessorType
//import net.minecraft.util.BlockMirror
//import net.minecraft.util.BlockRotation
//import net.minecraft.util.Identifier
//import net.minecraft.util.Nullables
//import net.minecraft.util.math.*
//import net.minecraft.util.random.RandomGenerator
//import net.minecraft.world.StructureWorldAccess
//import net.minecraft.world.gen.chunk.ChunkGenerator
//import net.minecraft.world.gen.feature.LiquidSettings
//import org.teamvoided.dusk_debris.init.worldgen.structure.DuskStructurePoolElementType
//import java.util.*
//
//class CavitySavePoolElement(
//    //protected val template: Either<Identifier, Structure>,
//    private val processors: Holder<StructureProcessorList>,
//    projection: StructurePool.Projection,
//    private val overrideLiquidSettings: Optional<LiquidSettings>
//) : StructurePoolElement(projection) {
//    private var jigsawBlocks: MutableList<Structure.StructureBlockInfo> = Lists.newArrayList()
//    private var structure: Structure = Structure()
//
//    override fun getType(): StructurePoolElementType<*> = DuskStructurePoolElementType.CAVITY
//
//    override fun getStart(structureTemplateManager: StructureTemplateManager, rotation: BlockRotation): Vec3i {
//        return Vec3i.ZERO
//    }
//
//    override fun getStructureBlockInfos(
//        structureTemplateManager: StructureTemplateManager,
//        pos: BlockPos,
//        rotation: BlockRotation,
//        random: RandomGenerator
//    ): List<Structure.StructureBlockInfo> {
//
//    }
//
//
//    override fun getBoundingBox(
//        structureTemplateManager: StructureTemplateManager,
//        pos: BlockPos,
//        rotation: BlockRotation
//    ): BlockBox {
//
//    }
//
//    private fun createBoxForStructure(
//        structureTemplateManager: StructureTemplateManager,
//        pos: BlockPos,
//        rotation: BlockRotation,
//        random: RandomGenerator = RandomGenerator.createLegacy(pos.asLong() + rotation.ordinal)
//    ) {
//        val x = (random.nextInt(16) + 16)
//        val y = (random.nextInt(16) + 16)
//        val z = (random.nextInt(16) + 16)
//        //val x = (Math.random() * 24 + 8).toInt()
//        //val y = (Math.random() * 24 + 8).toInt()
//        //val z = (Math.random() * 24 + 8).toInt()
//
//        val size: Vec3i = Vec3i(x, y, z).add(-1, -1, -1)
//        val blockPos = Structure.transformAround(BlockPos.ORIGIN, BlockMirror.NONE, rotation, BlockPos.ORIGIN)
//        val blockPos2 =
//            Structure.transformAround(BlockPos.ORIGIN.add(size), BlockMirror.NONE, rotation, BlockPos.ORIGIN)
//        this.blockBox = BlockBox.create(blockPos, blockPos2).offset(pos.x, pos.y, pos.z)
//    }
//
//
//    override fun generate(
//        structureTemplateManager: StructureTemplateManager,
//        world: StructureWorldAccess,
//        structureManager: StructureManager,
//        chunkGenerator: ChunkGenerator,
//        pos: BlockPos,
//        pivot: BlockPos,
//        rotation: BlockRotation,
//        box: BlockBox,
//        random: RandomGenerator,
//        liquidSettings: LiquidSettings,
//        keepJigsaws: Boolean
//    ): Boolean {
//        val structure = this.getStructure(structureTemplateManager)
//        val structurePlacementData = this.createPlacementData(rotation, box, liquidSettings, keepJigsaws)
//        if (!structure.place(world, pos, pivot, structurePlacementData, random, 18)) {
//            return false
//        } else {
//            val list = Structure.process(
//                world, pos, pivot, structurePlacementData,
//                this.getDataStructureBlocks(structureTemplateManager, pos, rotation, false)
//            )
//            val var15: Iterator<*> = list.iterator()
//
//            while (var15.hasNext()) {
//                val structureBlockInfo = var15.next() as Structure.StructureBlockInfo
//                this.handleDataMarker(world, structureBlockInfo, pos, rotation, random, box)
//            }
//
//            return true
//        }
//    }
//
//    private fun getStructure(structureTemplateManager: StructureTemplateManager): Structure {
//        val var10000 = this.structure
//        Objects.requireNonNull(structureTemplateManager)
//        return this.structure
//    }
//
//    private fun getOrCreateStructure(structureTemplateManager: StructureTemplateManager): Structure{
//        val nbtCompound = NbtCompound()
//        nbtCompound.putString("size", "minecraft:stone")
//        nbtCompound.putString("blocks", "minecraft:stone")
//        val structure = Structure().writeNbt(nbtCompound)
//        return structure
//    }
//
//
//    private fun getSquaredDistance(pos: BlockPos, blockBox: BlockBox, centerBox: Vec3d, sample: Float): Double {
//        val distance = pos.ofCenter().subtract(centerBox)
//        val x = (distance.x / (blockBox.blockCountX / 2))
//        val y = (distance.y / (blockBox.blockCountY / 2))
//        val z = (distance.z / (blockBox.blockCountZ / 2))
//        val retorn = (x * x + y * y + z * z)
//        return if (retorn > 0.9 && sample < 0)
//            retorn + (sample * (1 - (retorn - 0.9)))
//        else
//            retorn + sample
//    }
//
//    private fun sbi(
//        pos: BlockPos,
//        direction1: Direction = Direction.DOWN,
//        nbt: NbtCompound = this.createJigsawNbt("minecraft:bottom")
//    ): Structure.StructureBlockInfo {
//        val direction2: Direction = if (direction1.axis == Direction.Axis.Y) Direction.SOUTH else Direction.UP
//        return Structure.StructureBlockInfo(
//            pos,
//            Blocks.JIGSAW.defaultState.with(
//                JigsawBlock.ORIENTATION,
//                JigsawOrientation.byDirections(direction1, direction2)
//            ),
//            nbt
//        )
//    }
//
//    private fun createPlacementData(
//        rotation: BlockRotation,
//        box: BlockBox,
//        other: LiquidSettings,
//        keepJigsaws: Boolean
//    ): StructurePlacementData {
//        val structurePlacementData = StructurePlacementData()
//        structurePlacementData.setBoundingBox(box)
//        structurePlacementData.setRotation(rotation)
//        structurePlacementData.setUpdateNeighbors(true)
//        structurePlacementData.setIgnoreEntities(false)
//        structurePlacementData.addProcessor(BlockIgnoreStructureProcessor.IGNORE_STRUCTURE_BLOCKS)
//        structurePlacementData.setInitializeEntities(true)
//        structurePlacementData.method_61020(overrideLiquidSettings.orElse(other) as LiquidSettings)
//        if (!keepJigsaws) structurePlacementData.addProcessor(JigsawReplacementStructureProcessor.INSTANCE)
//        Objects.requireNonNull(structurePlacementData)
//        processors.value().list.forEach { structurePlacementData.addProcessor(it) }
//        Objects.requireNonNull(structurePlacementData)
//        projection.processors.forEach { structurePlacementData.addProcessor(it) }
//        return structurePlacementData
//    }
//
//    private fun createJigsawNbt(
//        name: String = "minecraft:bottom",
//        finalState: String = "minecraft:air",
//        pool: String = "minecraft:empty",
//        target: String = "minecraft:empty",
//        joint: String = JigsawBlockEntity.Joint.ROLLABLE.asString()
//    ): NbtCompound {
//        val nbtCompound = NbtCompound()
//        nbtCompound.putString("name", name)
//        nbtCompound.putString("final_state", finalState)
//        nbtCompound.putString("pool", pool)
//        nbtCompound.putString("target", target)
//        nbtCompound.putString("joint", joint)
//        return nbtCompound
//    }
//
//    override fun toString(): String {
//        return "Cavity[${structure.size}]"
//    }
//
//    companion object {
//        val CODEC: MapCodec<CavitySavePoolElement>
//        private fun <T> encodeTemplate(
//            template: Either<Identifier, Structure>,
//            ops: DynamicOps<T>,
//            value: T
//        ): DataResult<T> {
//            val optional = template.left()
//            return if (optional.isEmpty) DataResult.error { "Can not serialize a runtime pool element" }
//            else Identifier.CODEC.encode(optional.get(), ops, value)
//        }
//
//        protected fun processorsCodec(): RecordCodecBuilder<CavitySavePoolElement, Holder<StructureProcessorList>> {
//            return StructureProcessorType.REGISTRY_CODEC.fieldOf("processors")
//                .forGetter { it!!.processors }
//        }
//
//        protected fun liquidSettingsCodec(): RecordCodecBuilder<CavitySavePoolElement, Optional<LiquidSettings>> {
//            return LiquidSettings.codec.optionalFieldOf("override_liquid_settings")
//                .forGetter { it!!.overrideLiquidSettings }
//        }
//
////        protected fun <E : CavityPoolElement?> templateCodec(): RecordCodecBuilder<E, Either<Identifier, Structure>> {
////            return TEMPLATE_CODEC.fieldOf("location").forGetter { it!!.template }
////        }
//
//        @VisibleForTesting
//        fun selectionPriority(list: MutableList<Structure.StructureBlockInfo>) {
//            list.sortWith(Comparator.comparingInt<Structure.StructureBlockInfo> { info ->
//                Nullables.mapOrDefault(info.nbt(), { it.getInt("selection_priority") }, 0)!!
//            }.reversed())
//        }
//
//
//        init {
//            CODEC = RecordCodecBuilder.mapCodec {
//                it.group(
//                    //templateCodec(),
//                    processorsCodec(),
//                    projectionCodec(),
//                    liquidSettingsCodec()
//                ).apply(it, ::CavitySavePoolElement)
//            }
//        }
//    }
//}
