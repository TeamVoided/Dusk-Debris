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
import org.teamvoided.dusk_debris.world.gen.noise.FastNoise
import java.util.*

class CavityPoolElement(
    //protected val template: Either<Identifier, Structure>,
    private val processors: Holder<StructureProcessorList>,
    projection: StructurePool.Projection,
    private val overrideLiquidSettings: Optional<LiquidSettings>
) : StructurePoolElement(projection) {
    private var blockBox: BlockBox? = null
    private var jigsawBlocks: MutableList<Structure.StructureBlockInfo> = Lists.newArrayList()
    //private var structure: Structure = Structure()

    override fun getType(): StructurePoolElementType<*> = DuskStructurePoolElementType.CAVITY

    override fun getStart(structureTemplateManager: StructureTemplateManager, rotation: BlockRotation): Vec3i {
        //val blockBox = this.blockBox!!
        return Vec3i.ZERO //(blockBox.blockCountX, blockBox.blockCountY, blockBox.blockCountZ)
    }

    override fun getStructureBlockInfos(
        structureTemplateManager: StructureTemplateManager,
        pos: BlockPos,
        rotation: BlockRotation,
        random: RandomGenerator
    ): List<Structure.StructureBlockInfo> {
        if (this.jigsawBlocks.isNotEmpty())
            return this.jigsawBlocks
        if (this.blockBox == null)
            createBoxForStructure(structureTemplateManager, pos, rotation, random)
        val blockBox = this.blockBox!!
        val list: MutableList<Structure.StructureBlockInfo> = Lists.newArrayList()
        list.add(sbi(blockBox.center, Direction.UP, this.createJigsawNbt("minecraft:center")))

        val size2 = Vec3i(blockBox.blockCountX / 3, blockBox.blockCountY / 3, blockBox.blockCountZ / 3)
        val min2 = Vec3i(blockBox.minX, blockBox.minY, blockBox.minZ).add(size2)
        //val max2 = Vec3i(blockBox.maxX, blockBox.maxY, blockBox.maxZ).subtract(size2)

        val debugJigsaw = this.createJigsawNbt(
            "minecraft:test",
            "minecraft:air",
            "dusk_debris:test",
            "minecraft:test"
        )
        Direction.entries.forEach {
            val x = when (it) {
                Direction.EAST -> blockBox.maxX
                Direction.WEST -> blockBox.minX
                else -> min2.x + random.nextInt(size2.x)
            }
            val y = when (it) {
                Direction.UP -> blockBox.maxY
                Direction.DOWN -> blockBox.minY
                else -> min2.y + random.nextInt(size2.y)
            }
            val z = when (it) {
                Direction.SOUTH -> blockBox.maxZ
                Direction.NORTH -> blockBox.minZ
                else -> min2.z + random.nextInt(size2.z)
            }
            list.add(sbi(BlockPos(x, y, z), it, debugJigsaw))
        }
        this.jigsawBlocks = list
        return list
    }

    private fun sbi(
        pos: BlockPos,
        direction1: Direction = Direction.DOWN,
        nbt: NbtCompound = this.createJigsawNbt("minecraft:bottom")
    ): Structure.StructureBlockInfo {
        val direction2: Direction = if (direction1.axis == Direction.Axis.Y) Direction.SOUTH else Direction.UP
        return Structure.StructureBlockInfo(
            pos,
            Blocks.JIGSAW.defaultState.with(
                JigsawBlock.ORIENTATION,
                JigsawOrientation.byDirections(direction1, direction2)
            ),
            nbt
        )
    }


    override fun getBoundingBox(
        structureTemplateManager: StructureTemplateManager,
        pos: BlockPos,
        rotation: BlockRotation
    ): BlockBox {
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
        val x = (random.nextInt(16) + 16)
        val y = (random.nextInt(16) + 16)
        val z = (random.nextInt(16) + 16)
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
        if (this.blockBox == null)
            createBoxForStructure(structureTemplateManager, pos, rotation)
        //throw IllegalStateException("Invalid call to CavityPoolElement.generate, blockBox is null! Call the CavityPoolElement.createBoxForStructure function before CavityPoolElement.generate!")

        val blockBox: BlockBox = this.blockBox!!
        val structurePlacementData = this.createPlacementData(rotation, box, liquidSettings, keepJigsaws)
        createCavity(structurePlacementData, world, blockBox, 18)
        world.setBlockState(pos, Blocks.GOLD_BLOCK.defaultState, 3)
        getStructureBlockInfos(structureTemplateManager, pos, rotation, random).forEach {
            world.setBlockState(it.pos, it.state, 3)
            world.getBlockEntity(it.pos)?.read(it.nbt, world.registryManager)
        }
        this.blockBox = null
        this.jigsawBlocks.clear()
        return true
    }

    private fun createCavity(
        placementData: StructurePlacementData,
        world: StructureWorldAccess,
        blockBox: BlockBox,
        flags: Int
    ) {
        val noise = FastNoise(world.seed.toInt())
        noise.SetFractalType(FastNoise.FractalType.FBM)
        noise.SetNoiseType(FastNoise.NoiseType.Perlin) //Cubic
        noise.SetFrequency(0.1f)
        val boxCenter = Vec3d(
            blockBox.minX + blockBox.blockCountX / 2.0,
            blockBox.minY + blockBox.blockCountY / 2.0,
            blockBox.minZ + blockBox.blockCountZ / 2.0
        )
        for (y in blockBox.minY..blockBox.maxY) {
            for (x in blockBox.minX..blockBox.maxX) {
                for (z in blockBox.minZ..blockBox.maxZ) {
                    val setPos = BlockPos(x, y, z)
                    val sample = noise.GetNoise(x.toFloat(), y * 0.25f, z.toFloat()) * 2
                    if (getSquaredDistance(setPos, blockBox, boxCenter, sample * sample) >= 1) {
                        world.setBlockState(setPos, Blocks.STONE.defaultState, flags)
                        updateCavityState(placementData, world, setPos, flags)
                    } else {
                        world.setBlockState(setPos, Blocks.AIR.defaultState, flags)
                    }
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


    private fun getSquaredDistance(pos: BlockPos, blockBox: BlockBox, centerBox: Vec3d, sample: Float): Double {
        val distance = pos.ofCenter().subtract(centerBox)
        val x = (distance.x / (blockBox.blockCountX / 2))
        val y = (distance.y / (blockBox.blockCountY / 2))
        val z = (distance.z / (blockBox.blockCountZ / 2))
        val retorn = (x * x + y * y + z * z)
        return if (retorn > 0.9 && sample < 0)
            retorn + (sample * (1 - (retorn - 0.9)))
        else
            retorn + sample
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
