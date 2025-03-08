package org.teamvoided.dusk_debris.world.gen.structure.piece

import com.mojang.logging.LogUtils
import com.mojang.serialization.DataResult
import com.mojang.serialization.DynamicOps
import net.minecraft.nbt.NbtCompound
import net.minecraft.nbt.NbtElement
import net.minecraft.nbt.NbtOps
import net.minecraft.structure.StructureManager
import net.minecraft.structure.StructureTemplateManager
import net.minecraft.structure.piece.StructurePiece
import net.minecraft.structure.piece.StructurePieceSerializationContext
import net.minecraft.structure.pool.StructurePoolElement
import net.minecraft.util.BlockRotation
import net.minecraft.util.math.BlockBox
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.ChunkPos
import net.minecraft.util.random.RandomGenerator
import net.minecraft.world.StructureWorldAccess
import net.minecraft.world.gen.chunk.ChunkGenerator
import net.minecraft.world.gen.feature.JigsawFeature
import net.minecraft.world.gen.feature.LiquidSettings
import org.slf4j.Logger
import org.teamvoided.dusk_debris.init.worldgen.DuskStructurePieceType
import java.util.*

class PoolNoJigsawStructurePiece : StructurePiece {
    private val poolElement: StructurePoolElement
    var pos: BlockPos
    private val groundLevelDelta: Int
    private val rotationSet: BlockRotation
    private val structureTemplateManager: StructureTemplateManager
    private val liquidSettings: LiquidSettings

    constructor(
        structureTemplateManager: StructureTemplateManager,
        poolElement: StructurePoolElement,
        pos: BlockPos,
        groundLevelDelta: Int,
        rotation: BlockRotation,
        boundingBox: BlockBox,
        liquidSettings: LiquidSettings
    ) : super(DuskStructurePieceType.SIMPLE, 0, boundingBox) {
        this.structureTemplateManager = structureTemplateManager
        this.poolElement = poolElement
        this.pos = pos
        this.groundLevelDelta = groundLevelDelta
        this.rotationSet = rotation
        this.liquidSettings = liquidSettings
    }

    constructor(world: StructurePieceSerializationContext, nbt: NbtCompound) :
            super(DuskStructurePieceType.SIMPLE, nbt) {
        this.structureTemplateManager = world.structureTemplateManager()
        this.pos = BlockPos(nbt.getInt("PosX"), nbt.getInt("PosY"), nbt.getInt("PosZ"))
        this.groundLevelDelta = nbt.getInt("ground_level_delta")
        val dynamicOps: DynamicOps<NbtElement> = world.registryManager().createSerializationContext(NbtOps.INSTANCE)
        this.poolElement = StructurePoolElement.CODEC.parse(dynamicOps, nbt.getCompound("pool_element"))
            .getPartialOrThrow { IllegalStateException("Invalid pool element found: $it") }
        this.rotationSet = BlockRotation.valueOf(nbt.getString("rotation"))
        this.boundingBox = poolElement.getBoundingBox(this.structureTemplateManager, this.pos, this.rotation)
        this.liquidSettings = LiquidSettings.codec.parse(NbtOps.INSTANCE, nbt["liquid_settings"]).result()
            .orElse(JigsawFeature.DEFAULT_LIQUID_SETTING)
    }

    override fun writeNbt(context: StructurePieceSerializationContext, nbt: NbtCompound) {
        nbt.putInt("PosX", pos.x)
        nbt.putInt("PosY", pos.y)
        nbt.putInt("PosZ", pos.z)
        nbt.putInt("ground_level_delta", this.groundLevelDelta)
        val dynamicOps: DynamicOps<NbtElement> = context.registryManager().createSerializationContext(NbtOps.INSTANCE)
        val dataResult: DataResult<NbtElement> = StructurePoolElement.CODEC.encodeStart(dynamicOps, this.poolElement)
        val logger = LOGGER
        Objects.requireNonNull(logger)
        dataResult.resultOrPartial(logger::error).ifPresent { nbt.put("pool_element", it) }
        nbt.putString("rotation", this.rotation.name)

        if (this.liquidSettings != JigsawFeature.DEFAULT_LIQUID_SETTING) {
            nbt.put(
                "liquid_settings",
                LiquidSettings.codec.encodeStart(NbtOps.INSTANCE, this.liquidSettings).getOrThrow()
            )
        }
    }

    override fun generate(
        world: StructureWorldAccess,
        structureManager: StructureManager,
        chunkGenerator: ChunkGenerator,
        random: RandomGenerator,
        boundingBox: BlockBox,
        chunkPos: ChunkPos,
        pos: BlockPos
    ) {
        this.generate(world, structureManager, chunkGenerator, random, boundingBox, pos, false)
    }

    fun generate(
        world: StructureWorldAccess,
        structureManager: StructureManager,
        chunkGenerator: ChunkGenerator,
        random: RandomGenerator,
        boundingBox: BlockBox,
        pos: BlockPos,
        keepJigsaws: Boolean
    ) {
        poolElement.generate(
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
    }

    override fun translate(x: Int, y: Int, z: Int) {
        super.translate(x, y, z)
        this.pos = pos.add(x, y, z)
    }

    override fun getRotation(): BlockRotation {
        return this.rotation
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