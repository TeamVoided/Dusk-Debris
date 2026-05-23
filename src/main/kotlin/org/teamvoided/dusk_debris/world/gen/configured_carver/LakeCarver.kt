package org.teamvoided.dusk_debris.world.gen.configured_carver

import com.mojang.serialization.Codec
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.Holder
import net.minecraft.util.Mth
import net.minecraft.util.RandomSource
import net.minecraft.world.level.ChunkPos
import net.minecraft.world.level.biome.Biome
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.chunk.CarvingMask
import net.minecraft.world.level.chunk.ChunkAccess
import net.minecraft.world.level.levelgen.Aquifer
import net.minecraft.world.level.levelgen.DensityFunction
import net.minecraft.world.level.levelgen.LegacyRandomSource
import net.minecraft.world.level.levelgen.WorldgenRandom
import net.minecraft.world.level.levelgen.carver.CarvingContext
import net.minecraft.world.level.levelgen.carver.WorldCarver
import net.minecraft.world.level.levelgen.synth.NormalNoise
import org.apache.commons.lang3.mutable.MutableBoolean
import org.teamvoided.dusk_debris.world.gen.configured_carver.config.LakeCarverConfig
import org.teamvoided.dusk_debris.world.gen.configured_carver.config.debug.LakeCarverDebugConfig
import java.util.function.Function
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

class LakeCarver(codec: Codec<LakeCarverConfig>) : WorldCarver<LakeCarverConfig>(codec) {
    override fun isStartChunk(ravineCarverConfig: LakeCarverConfig, random: RandomSource): Boolean {
        return random.nextFloat() <= ravineCarverConfig.probability
    }

    override fun carve(
        carverContext: CarvingContext,
        config: LakeCarverConfig,
        chunk: ChunkAccess,
        function: Function<BlockPos, Holder<Biome>>,
        random: RandomSource,
        aquiferSampler: Aquifer,
        chunkPos: ChunkPos,
        carvingMask: CarvingMask
    ): Boolean {
        val posX: Double = chunkPos.getBlockX(random.nextInt(16)).toDouble()
        val posY: Double = config.y.sample(random, carverContext).toDouble()
        val posZ: Double = chunkPos.getBlockZ(random.nextInt(16)).toDouble()
        val waterLevel: Double = config.waterLevel.sample(random).toDouble()


//        chunk.setBlockState(Vec3d(posX, posY, posZ).toBlockPos(), Blocks.GLOWSTONE.defaultState, false)

        val skipPredicate =
            SkipOrWaterPredicate { context: CarvingContext, scaledRelativeX: Double, scaledRelativeY: Double, scaledRelativeZ: Double, y: Int, dps: Double ->
                isPositionExcludedOrWater(
                    scaledRelativeX,
                    scaledRelativeY,
                    scaledRelativeZ,
                    waterLevel,
                    dps
                )
            }

        val height: Double = config.yScale.sample(random).toDouble() //radius*this+2
        val radius: Float = config.horizontalRadius.sample(random).toFloat() //2*this+2 //1.0f + random.nextFloat() * 6.0f
        this.carveCave(
            carverContext,
            config,
            chunk,
            function,
            aquiferSampler,
            posX,
            posY,
            posZ,
            radius,
            height,
            carvingMask,
            skipPredicate
        )

        return true
    }

    protected fun carveCave(
        context: CarvingContext,
        config: LakeCarverConfig,
        chunk: ChunkAccess,
        posToBiome: Function<BlockPos, Holder<Biome>>,
        aquiferSampler: Aquifer,
        x: Double,
        y: Double,
        z: Double,
        radius: Float,
        height: Double,
        carvingMask: CarvingMask,
        predicate: SkipOrWaterPredicate
    ) {
        val horizontalScale = 1.5 + radius
        val verticalScale = horizontalScale * height
        this.carveRegion(
            context,
            config,
            chunk,
            posToBiome,
            aquiferSampler,
            x + 1,
            y,
            z,
            horizontalScale,
            verticalScale,
            carvingMask,
            predicate
        )
    }

    //overide but custom predicate
    private fun carveRegion(
        context: CarvingContext,
        config: LakeCarverConfig,
        chunk: ChunkAccess,
        posToBiome: Function<BlockPos, Holder<Biome>>,
        sampler: Aquifer,
        x: Double,
        y: Double,
        z: Double,
        horizontalScale: Double,
        verticalScale: Double,
        mask: CarvingMask,
        predicate: SkipOrWaterPredicate
    ): Boolean {
        val chunkPos = chunk.pos
        val posX = chunkPos.middleBlockX.toDouble()
        val posZ = chunkPos.middleBlockZ.toDouble()
        val f = 16.0 + horizontalScale * 2.0
        if (!(abs(x - posX) > f) && !(abs(z - posZ) > f)) {
            val startX = chunkPos.minBlockX
            val startZ = chunkPos.minBlockZ
            val xMin = max((Mth.floor(x - horizontalScale) - startX - 1.0), 0.0).toInt()
            val xMax = min((Mth.floor(x + horizontalScale) - startX).toDouble(), 15.0).toInt()
            val yMin = max((Mth.floor(y - verticalScale) - 1.0), (context.minGenY + 1.0)).toInt()
            val n = if (chunk.isUpgrading) 0 else 7
            val yMax =
                min(
                    (Mth.floor(y + verticalScale) + 1.0),
                    (context.minGenY + context.genDepth - 1.0 - n)
                ).toInt()
            val zMin = max((Mth.floor(z - horizontalScale) - startZ - 1.0), 0.0).toInt()
            val zMax = min((Mth.floor(z + horizontalScale) - startZ).toDouble(), 15.0).toInt()
            var bl = false
            val mutable = BlockPos.MutableBlockPos()
            val mutable2 = BlockPos.MutableBlockPos()

            val chunkRandom = WorldgenRandom(LegacyRandomSource(0)) ////////HOW TO GET SEED OF WORLD
            val dps = NormalNoise.create(chunkRandom, -4, 2.0, 1.0, 0.0)

            for (loopX in xMin..xMax) {
                val chunkX = chunkPos.getBlockX(loopX)
                val funX = (chunkX + 0.5 - x) / horizontalScale

                for (loopZ in zMin..zMax) {
                    val chunkZ = chunkPos.getBlockZ(loopZ)
                    val funZ = (chunkZ + 0.5 - z) / horizontalScale
                    if (!(funX * funX + funZ * funZ >= 1.0)) {
                        val mutableBoolean = MutableBoolean(false)

                        for (loopY in yMax downTo yMin + 1) {
                            val funY = (loopY - 0.5 - y) / verticalScale

                            val sample = dps.getValue(chunkX.toDouble(), loopY * 0.25, chunkZ.toDouble())
                            val sampleMathed = abs(sample)
                            val output = predicate.shouldSkip(context, funX, funY, funZ, loopY, sampleMathed)
                            if (output != 0 && (!mask[loopX, loopY, loopZ] || isDebugEnabled(config))) {
                                mask[loopX, loopY] = loopZ
                                mutable[chunkX, loopY] = chunkZ
                                bl = bl or carveAtPoint(
                                    context,
                                    config,
                                    chunk,
                                    posToBiome,
                                    mask,
                                    mutable,
                                    mutable2,
                                    sampler,
                                    mutableBoolean,
                                    output
                                )
                            }
                        }
                    }
                }
            }
            return bl
        } else {
            return false
        }
    }

    private fun carveAtPoint(
        context: CarvingContext,
        config: LakeCarverConfig,
        chunk: ChunkAccess,
        posToBiome: Function<BlockPos, Holder<Biome>>,
        mask: CarvingMask,
        pos: BlockPos.MutableBlockPos,
        downPos: BlockPos.MutableBlockPos,
        sampler: Aquifer,
        foundSurface: MutableBoolean,
        predicateResult: Int,

        ): Boolean {
        val blockState = chunk.getBlockState(pos)
        if (blockState.`is`(Blocks.GRASS_BLOCK) || blockState.`is`(Blocks.MYCELIUM)) {
            foundSurface.setTrue()
        }

        if (!this.canReplaceBlock(config, blockState) && !isDebugEnabled(config)) {
            return false
        } else {
            val blockState2 =
                this.getState(context, config, pos, sampler, predicateResult, chunk)
            if (blockState2 == null) {
                return false
            } else {
                chunk.setBlockState(pos, blockState2, false)
                if (sampler.shouldScheduleFluidUpdate() && !blockState2.fluidState.isEmpty) {
                    chunk.markPosForPostprocessing(pos)
                }

                if (foundSurface.isTrue) {
                    downPos.setWithOffset(pos,Direction.DOWN)
                    if (chunk.getBlockState(downPos).`is`(Blocks.DIRT)) {
                        context.topMaterial(posToBiome, chunk, downPos, !blockState2.fluidState.isEmpty)
                            .ifPresent { state: BlockState ->
                                chunk.setBlockState(downPos, state, false)
                                if (!state.fluidState.isEmpty) {
                                    chunk.markPosForPostprocessing(downPos)
                                }
                            }
                    }
                }

                return true
            }
        }
    }


    private fun getState(
        context: CarvingContext,
        config: LakeCarverConfig,
        pos: BlockPos,
        sampler: Aquifer,
        predicateResult: Int,
        chunk: ChunkAccess,
    ): BlockState? {
        val debug = isDebugEnabled(config)
        if (pos.y <= config.lavaLevel.resolveY(context)) {
            return if (debug) config.debugSettings.lavaState else LAVA.createLegacyBlock()
        } else {
            val blockState = sampler.computeSubstance(DensityFunction.SinglePointContext(pos.x, pos.y, pos.z), 0.0)
            if (blockState == null) {
                return if (debug) config.debugSettings.barrierState else null
//            } else if (predicateResult == 2) {
//                return if (debug) Blocks.WHITE_STAINED_GLASS.defaultState else blockState
            } else if (predicateResult >= 2) {
                return if (debug) LakeCarverDebugConfig.default().fluidState else config.fluidState
            } else {
                return if (debug) getDebugState(config, blockState) else blockState
            }
        }
    }

    private fun isPositionExcludedOrWater(
        scaledRelativeX: Double,
        scaledRelativeY: Double,
        scaledRelativeZ: Double,
        waterY: Double,
        dps: Double
    ): Int {
        val value =
            scaledRelativeX * scaledRelativeX + scaledRelativeY * scaledRelativeY + scaledRelativeZ * scaledRelativeZ + dps
        return if (value <= 1.0) if (scaledRelativeY <= waterY) if (value <= 0.8) 3 else 2 else 1 else 0

        //0 means nothing
        //1 means air
        //2 means fluid barrier
        //3 means fluid
    }


    fun interface SkipOrWaterPredicate {
        fun shouldSkip(carverContext: CarvingContext, x: Double, y: Double, z: Double, waterY: Int, sample: Double): Int
    }
}