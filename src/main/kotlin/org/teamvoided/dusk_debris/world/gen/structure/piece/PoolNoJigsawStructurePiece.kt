package org.teamvoided.dusk_debris.world.gen.structure.piece

import com.mojang.logging.LogUtils
import com.mojang.serialization.DataResult
import com.mojang.serialization.DynamicOps
import net.minecraft.core.BlockPos
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.NbtOps
import net.minecraft.nbt.Tag
import net.minecraft.util.RandomSource
import net.minecraft.world.level.ChunkPos
import net.minecraft.world.level.StructureManager
import net.minecraft.world.level.WorldGenLevel
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.Rotation
import net.minecraft.world.level.chunk.ChunkGenerator
import net.minecraft.world.level.levelgen.structure.BoundingBox
import net.minecraft.world.level.levelgen.structure.StructurePiece
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement
import net.minecraft.world.level.levelgen.structure.structures.JigsawStructure
import net.minecraft.world.level.levelgen.structure.templatesystem.LiquidSettings
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager
import org.slf4j.Logger
import org.teamvoided.dusk_debris.init.worldgen.structure.DuskStructurePieceType
import java.util.*

class PoolNoJigsawStructurePiece : StructurePiece {
    private val poolElement: StructurePoolElement
    var pos: BlockPos
    private val groundLevelDelta: Int
    private val rotationSet: Rotation
    private val structureTemplateManager: StructureTemplateManager
    private val liquidSettings: LiquidSettings

    constructor(
        structureTemplateManager: StructureTemplateManager,
        poolElement: StructurePoolElement,
        pos: BlockPos,
        groundLevelDelta: Int,
        rotation: Rotation,
        boundingBox: BoundingBox,
        liquidSettings: LiquidSettings
    ) : super(DuskStructurePieceType.SIMPLE, 0, boundingBox) {
        this.structureTemplateManager = structureTemplateManager
        this.poolElement = poolElement
        this.pos = pos
        this.groundLevelDelta = groundLevelDelta
        this.rotationSet = rotation
        this.liquidSettings = liquidSettings
    }

    constructor(world: StructurePieceSerializationContext, nbt: CompoundTag) :
            super(DuskStructurePieceType.SIMPLE, nbt) {
        this.structureTemplateManager = world.structureTemplateManager()
        this.pos = BlockPos(nbt.getInt("PosX"), nbt.getInt("PosY"), nbt.getInt("PosZ"))
        this.groundLevelDelta = nbt.getInt("ground_level_delta")
        val dynamicOps: DynamicOps<Tag> = world.registryAccess().createSerializationContext(NbtOps.INSTANCE)
        this.poolElement = StructurePoolElement.CODEC.parse(dynamicOps, nbt.getCompound("pool_element"))
            .getPartialOrThrow { IllegalStateException("Invalid pool element found: $it") }
        this.rotationSet = Rotation.valueOf(nbt.getString("rotation"))
        this.boundingBox = poolElement.getBoundingBox(this.structureTemplateManager, this.pos, this.rotation)
        this.liquidSettings = LiquidSettings.CODEC.parse(NbtOps.INSTANCE, nbt["liquid_settings"]).result()
            .orElse(JigsawStructure.DEFAULT_LIQUID_SETTINGS)
    }

    override fun addAdditionalSaveData(context: StructurePieceSerializationContext, nbt: CompoundTag) {
        nbt.putInt("PosX", pos.x)
        nbt.putInt("PosY", pos.y)
        nbt.putInt("PosZ", pos.z)
        nbt.putInt("ground_level_delta", this.groundLevelDelta)
        val dynamicOps: DynamicOps<Tag> = context.registryAccess().createSerializationContext(NbtOps.INSTANCE)
        val dataResult: DataResult<Tag> = StructurePoolElement.CODEC.encodeStart(dynamicOps, this.poolElement)
        val logger = LOGGER
        Objects.requireNonNull(logger)
        dataResult.resultOrPartial(logger::error).ifPresent { nbt.put("pool_element", it) }
        nbt.putString("rotation", this.rotation.name)

        if (this.liquidSettings != JigsawStructure.DEFAULT_LIQUID_SETTINGS) {
            nbt.put(
                "liquid_settings",
                LiquidSettings.CODEC.encodeStart(NbtOps.INSTANCE, this.liquidSettings).getOrThrow()
            )
        }
    }

    override fun postProcess(
        world: WorldGenLevel,
        structureManager: StructureManager,
        chunkGenerator: ChunkGenerator,
        random: RandomSource,
        boundingBox: BoundingBox,
        chunkPos: ChunkPos,
        pos: BlockPos
    ) {
        this.generate(world, structureManager, chunkGenerator, random, boundingBox, pos, false)
    }

    fun generate(
        world: WorldGenLevel,
        structureManager: StructureManager,
        chunkGenerator: ChunkGenerator,
        random: RandomSource,
        boundingBox: BoundingBox,
        pos: BlockPos,
        keepJigsaws: Boolean
    ) {
        poolElement.place(
            this.structureTemplateManager,
            world,
            structureManager,
            chunkGenerator,
            this.pos,
            pos,
            this.rotation,
            boundingBox,
            random,
            this.liquidSettings,
            keepJigsaws
        )
        world.setBlocksBoxCorners(boundingBox)
//        net.minecraft.structure.piece.StructurePiece.createBox
    }

    private fun WorldGenLevel.setBlocksBoxCorners(box: BoundingBox) {
        val poses = listOf(
            BlockPos(box.minX(), box.minY(), box.minZ()),
            BlockPos(box.maxX(), box.minY(), box.minZ()),
            BlockPos(box.minX(), box.minY(), box.maxZ()),
            BlockPos(box.maxX(), box.minY(), box.maxZ()),
            BlockPos(box.minX(), box.maxY(), box.minZ()),
            BlockPos(box.maxX(), box.maxY(), box.minZ()),
            BlockPos(box.minX(), box.maxY(), box.maxZ()),
            BlockPos(box.maxX(), box.maxY(), box.maxZ())
        )
        poses.forEach {
            this.setBlock(it, Blocks.TINTED_GLASS.defaultBlockState(), 2)
        }
    }

    override fun move(x: Int, y: Int, z: Int) {
        super.move(x, y, z)
        this.pos = pos.offset(x, y, z)
    }

    override fun getRotation(): Rotation {
        return this.rotationSet
    }

    override fun toString(): String {
        return String.format(
            Locale.ROOT,
            "<%s | %s | %s | %s>",
            this.javaClass.simpleName,
            this.pos,
            this.rotation,
            this.poolElement
        )
    }

    companion object {
        private val LOGGER: Logger = LogUtils.getLogger()
    }
}