package org.teamvoided.dusk_debris.structure.pool

import com.google.common.annotations.VisibleForTesting
import com.google.common.collect.Lists
import com.mojang.datafixers.util.Either
import com.mojang.serialization.DataResult
import com.mojang.serialization.DynamicOps
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.Optionull
import net.minecraft.core.*
import net.minecraft.nbt.CompoundTag
import net.minecraft.resources.ResourceLocation
import net.minecraft.util.RandomSource
import net.minecraft.world.level.StructureManager
import net.minecraft.world.level.WorldGenLevel
import net.minecraft.world.level.block.*
import net.minecraft.world.level.block.entity.JigsawBlockEntity
import net.minecraft.world.level.chunk.ChunkGenerator
import net.minecraft.world.level.levelgen.structure.BoundingBox
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElementType
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool
import net.minecraft.world.level.levelgen.structure.templatesystem.*
import net.minecraft.world.phys.Vec3
import org.teamvoided.dusk_debris.init.worldgen.structure.DuskStructurePoolElementType
import org.teamvoided.dusk_debris.world.gen.noise.FastNoise
import java.util.*

class CavityPoolElement(
    //protected val template: Either<Identifier, Structure>,
    private val processors: Holder<StructureProcessorList>,
    projection: StructureTemplatePool.Projection,
    private val overrideLiquidSettings: Optional<LiquidSettings>
) : StructurePoolElement(projection) {
    private var blockBox: BoundingBox? = null
    private var jigsawBlocks: MutableList<StructureTemplate.StructureBlockInfo> = Lists.newArrayList()
    //private var structure: Structure = Structure()

    override fun getType(): StructurePoolElementType<*> = DuskStructurePoolElementType.CAVITY

    override fun getSize(structureTemplateManager: StructureTemplateManager, rotation: Rotation): Vec3i {
        //val blockBox = this.blockBox!!
        return Vec3i.ZERO //(blockBox.blockCountX, blockBox.blockCountY, blockBox.blockCountZ)
    }

    override fun getShuffledJigsawBlocks(
        structureTemplateManager: StructureTemplateManager,
        pos: BlockPos,
        rotation: Rotation,
        random: RandomSource
    ): List<StructureTemplate.StructureBlockInfo> {
        if (this.jigsawBlocks.isNotEmpty())
            return this.jigsawBlocks
        if (this.blockBox == null)
            createBoxForStructure(structureTemplateManager, pos, rotation, random)
        val blockBox = this.blockBox!!
        val list: MutableList<StructureTemplate.StructureBlockInfo> = Lists.newArrayList()
        list.add(sbi(blockBox.center, Direction.UP, this.createJigsawNbt("minecraft:center")))

        val size2 = Vec3i(blockBox.xSpan / 3, blockBox.ySpan / 3, blockBox.zSpan / 3)
        val min2 = Vec3i(blockBox.minX(), blockBox.minY(), blockBox.minZ()).offset(size2)
        //val max2 = Vec3i(blockBox.maxX, blockBox.maxY, blockBox.maxZ).subtract(size2)

        val debugJigsaw = this.createJigsawNbt(
            "minecraft:test",
            "minecraft:air",
            "dusk_debris:test",
            "minecraft:test"
        )
        Direction.entries.forEach {
            val x = when (it) {
                Direction.EAST -> blockBox.maxX()
                Direction.WEST -> blockBox.minX()
                else -> min2.x + random.nextInt(size2.x)
            }
            val y = when (it) {
                Direction.UP -> blockBox.maxY()
                Direction.DOWN -> blockBox.minY()
                else -> min2.y + random.nextInt(size2.y)
            }
            val z = when (it) {
                Direction.SOUTH -> blockBox.maxZ()
                Direction.NORTH -> blockBox.minZ()
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
        nbt: CompoundTag = this.createJigsawNbt("minecraft:bottom")
    ): StructureTemplate.StructureBlockInfo {
        val direction2: Direction = if (direction1.axis == Direction.Axis.Y) Direction.SOUTH else Direction.UP
        return StructureTemplate.StructureBlockInfo(
            pos,
            Blocks.JIGSAW.defaultBlockState().setValue(
                JigsawBlock.ORIENTATION,
                FrontAndTop.fromFrontAndTop(direction1, direction2)
            ),
            nbt
        )
    }


    override fun getBoundingBox(
        structureTemplateManager: StructureTemplateManager,
        pos: BlockPos,
        rotation: Rotation
    ): BoundingBox {
        if (blockBox == null)
            createBoxForStructure(structureTemplateManager, pos, rotation)
        return this.blockBox!!
    }

    private fun createBoxForStructure(
        structureTemplateManager: StructureTemplateManager,
        pos: BlockPos,
        rotation: Rotation,
        random: RandomSource = RandomSource.create(pos.asLong() + rotation.ordinal)
    ) {
        val x = (random.nextInt(16) + 16)
        val y = (random.nextInt(16) + 16)
        val z = (random.nextInt(16) + 16)
        //val x = (Math.random() * 24 + 8).toInt()
        //val y = (Math.random() * 24 + 8).toInt()
        //val z = (Math.random() * 24 + 8).toInt()

        val size: Vec3i = Vec3i(x, y, z).offset(-1, -1, -1)
        val blockPos = StructureTemplate.transform(BlockPos.ZERO, Mirror.NONE, rotation, BlockPos.ZERO)
        val blockPos2 =
            StructureTemplate.transform(BlockPos.ZERO.offset(size), Mirror.NONE, rotation, BlockPos.ZERO)
        this.blockBox = BoundingBox.fromCorners(blockPos, blockPos2).moved(pos.x, pos.y, pos.z)
    }


    override fun place(
        structureTemplateManager: StructureTemplateManager,
        world: WorldGenLevel,
        structureManager: StructureManager,
        chunkGenerator: ChunkGenerator,
        pos: BlockPos,
        pivot: BlockPos,
        rotation: Rotation,
        box: BoundingBox,
        random: RandomSource,
        liquidSettings: LiquidSettings,
        keepJigsaws: Boolean
    ): Boolean {
        if (this.blockBox == null)
            createBoxForStructure(structureTemplateManager, pos, rotation)
        //throw IllegalStateException("Invalid call to CavityPoolElement.generate, blockBox is null! Call the CavityPoolElement.createBoxForStructure function before CavityPoolElement.generate!")

        val blockBox: BoundingBox = this.blockBox!!
        val structurePlacementData = this.createPlacementData(rotation, box, liquidSettings, keepJigsaws)
        createCavity(structurePlacementData, world, blockBox, 18)
        world.setBlock(pos, Blocks.GOLD_BLOCK.defaultBlockState(), 3)
        getShuffledJigsawBlocks(structureTemplateManager, pos, rotation, random).forEach {
            world.setBlock(it.pos, it.state, 3)
            world.getBlockEntity(it.pos)?.loadWithComponents(it.nbt, world.registryAccess())
        }
        this.blockBox = null
        this.jigsawBlocks.clear()
        return true
    }

    private fun createCavity(
        placementData: StructurePlaceSettings,
        world: WorldGenLevel,
        blockBox: BoundingBox,
        flags: Int
    ) {
        val noise = FastNoise(world.seed.toInt())
        noise.SetFractalType(FastNoise.FractalType.FBM)
        noise.SetNoiseType(FastNoise.NoiseType.Perlin) //Cubic
        noise.SetFrequency(0.1f)
        val boxCenter = Vec3(
            blockBox.minX() + blockBox.xSpan / 2.0,
            blockBox.minY() + blockBox.ySpan / 2.0,
            blockBox.minZ() + blockBox.zSpan / 2.0
        )
        for (y in blockBox.minY()..blockBox.maxY()) {
            for (x in blockBox.minX()..blockBox.maxX()) {
                for (z in blockBox.minZ()..blockBox.maxZ()) {
                    val setPos = BlockPos(x, y, z)
                    val sample = noise.GetNoise(x.toFloat(), y * 0.25f, z.toFloat()) * 2
                    if (getSquaredDistance(setPos, blockBox, boxCenter, sample * sample) >= 1) {
                        world.setBlock(setPos, Blocks.STONE.defaultBlockState(), flags)
                        updateCavityState(placementData, world, setPos, flags)
                    } else {
                        world.setBlock(setPos, Blocks.AIR.defaultBlockState(), flags)
                    }
                }
            }
        }
    }

    private fun updateCavityState(
        placementData: StructurePlaceSettings,
        world: WorldGenLevel,
        pos: BlockPos,
        flags: Int
    ) {
        if (!placementData.knownShape) {
            val worldState = world.getBlockState(pos)
            val afterUpdateState = Block.updateFromNeighbourShapes(worldState, world, pos)
            if (worldState != afterUpdateState) {
                world.setBlock(pos, afterUpdateState, flags and -2 or 16)
            }
            world.blockUpdated(pos, afterUpdateState.block)
        }
    }


    private fun getSquaredDistance(pos: BlockPos, blockBox: BoundingBox, centerBox: Vec3, sample: Float): Double {
        val distance = pos.center.subtract(centerBox)
        val x = (distance.x / (blockBox.xSpan / 2))
        val y = (distance.y / (blockBox.ySpan / 2))
        val z = (distance.z / (blockBox.zSpan / 2))
        val retorn = (x * x + y * y + z * z)
        return if (retorn > 0.9 && sample < 0)
            retorn + (sample * (1 - (retorn - 0.9)))
        else
            retorn + sample
    }

    private fun createPlacementData(
        rotation: Rotation,
        box: BoundingBox,
        other: LiquidSettings,
        keepJigsaws: Boolean
    ): StructurePlaceSettings {
        val structurePlacementData = StructurePlaceSettings()
        structurePlacementData.setBoundingBox(box)
        structurePlacementData.setRotation(rotation)
        structurePlacementData.setKnownShape(true)
        structurePlacementData.setIgnoreEntities(false)
        structurePlacementData.addProcessor(BlockIgnoreProcessor.STRUCTURE_BLOCK)
        structurePlacementData.setFinalizeEntities(true)
        structurePlacementData.setLiquidSettings(overrideLiquidSettings.orElse(other) as LiquidSettings)
        if (!keepJigsaws) structurePlacementData.addProcessor(JigsawReplacementProcessor.INSTANCE)
        Objects.requireNonNull(structurePlacementData)
        processors.value().list().forEach { structurePlacementData.addProcessor(it) }
        Objects.requireNonNull(structurePlacementData)
        projection.processors.forEach { structurePlacementData.addProcessor(it) }
        return structurePlacementData
    }

    private fun createJigsawNbt(
        name: String = "minecraft:bottom",
        finalState: String = "minecraft:air",
        pool: String = "minecraft:empty",
        target: String = "minecraft:empty",
        joint: String = JigsawBlockEntity.JointType.ROLLABLE.serializedName
    ): CompoundTag {
        val nbtCompound = CompoundTag()
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
            template: Either<ResourceLocation, StructureTemplate>,
            ops: DynamicOps<T>,
            value: T
        ): DataResult<T> {
            val optional = template.left()
            return if (optional.isEmpty) DataResult.error { "Can not serialize a runtime pool element" }
            else ResourceLocation.CODEC.encode(optional.get(), ops, value)
        }

        protected fun processorsCodec(): RecordCodecBuilder<CavityPoolElement, Holder<StructureProcessorList>> {
            return StructureProcessorType.LIST_CODEC.fieldOf("processors")
                .forGetter { it!!.processors }
        }

        protected fun liquidSettingsCodec(): RecordCodecBuilder<CavityPoolElement, Optional<LiquidSettings>> {
            return LiquidSettings.CODEC.optionalFieldOf("override_liquid_settings")
                .forGetter { it!!.overrideLiquidSettings }
        }

//        protected fun <E : CavityPoolElement?> templateCodec(): RecordCodecBuilder<E, Either<Identifier, Structure>> {
//            return TEMPLATE_CODEC.fieldOf("location").forGetter { it!!.template }
//        }

        @VisibleForTesting
        fun selectionPriority(list: MutableList<StructureTemplate.StructureBlockInfo>) {
            list.sortWith(Comparator.comparingInt<StructureTemplate.StructureBlockInfo> { info ->
                Optionull.mapOrDefault(info.nbt(), { it.getInt("selection_priority") }, 0)!!
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
