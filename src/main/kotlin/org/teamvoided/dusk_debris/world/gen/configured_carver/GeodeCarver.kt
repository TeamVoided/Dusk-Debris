package org.teamvoided.dusk_debris.world.gen.configured_carver

import com.mojang.serialization.Codec
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.registry.Holder
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.ChunkPos
import net.minecraft.util.math.MathHelper
import net.minecraft.util.math.noise.DoublePerlinNoiseSampler
import net.minecraft.util.random.LegacySimpleRandom
import net.minecraft.util.random.RandomGenerator
import net.minecraft.world.biome.Biome
import net.minecraft.world.chunk.Chunk
import net.minecraft.world.gen.ChunkRandom
import net.minecraft.world.gen.DensityFunction
import net.minecraft.world.gen.carver.Carver
import net.minecraft.world.gen.carver.CarverContext
import net.minecraft.world.gen.carver.CarvingMask
import net.minecraft.world.gen.chunk.AquiferSampler
import org.teamvoided.dusk_debris.world.gen.configured_carver.GeodeCarver.SkipOrWaterPredicate
import org.teamvoided.dusk_debris.world.gen.configured_carver.config.GeodeCarverConfig
import java.util.function.Function
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

class GeodeCarver(codec: Codec<GeodeCarverConfig>) : Carver<GeodeCarverConfig>(codec) {
    override fun shouldCarve(ravineCarverConfig: GeodeCarverConfig, random: RandomGenerator): Boolean {
        return random.nextFloat() <= ravineCarverConfig.probability
    }

    override fun carve(
        carverContext: CarverContext,
        config: GeodeCarverConfig,
        chunk: Chunk,
        function: Function<BlockPos, Holder<Biome>>,
        random: RandomGenerator,
        aquiferSampler: AquiferSampler,
        chunkPos: ChunkPos,
        carvingMask: CarvingMask
    ): Boolean {
        val posX: Double = chunkPos.getOffsetX(random.nextInt(16)).toDouble()
        val posY: Double = config.y.get(random, carverContext).toDouble()
        val posZ: Double = chunkPos.getOffsetZ(random.nextInt(16)).toDouble()


//        chunk.setBlockState(Vec3d(posX, posY, posZ).toBlockPos(), Blocks.GLOWSTONE.defaultState, false)

        val skipPredicate =
            SkipOrWaterPredicate { context: CarverContext, scaledRelativeX: Double, scaledRelativeY: Double, scaledRelativeZ: Double, y: Int, dps: Double ->
                getState(scaledRelativeX, scaledRelativeY, scaledRelativeZ, dps)
            }

        val height: Double = config.yScale.get(random).toDouble() //radius*this+2
        val radius: Float = config.horizontalRadius.get(random).toFloat() //2*this+2 //1.0f + random.nextFloat() * 6.0f
        this.carveCave(
            carverContext,
            config,
            chunk,
            aquiferSampler,
            random,
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
        context: CarverContext,
        config: GeodeCarverConfig,
        chunk: Chunk,
        aquiferSampler: AquiferSampler,
        random: RandomGenerator,
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
            aquiferSampler,
            random,
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
        context: CarverContext,
        config: GeodeCarverConfig,
        chunk: Chunk,
        sampler: AquiferSampler,
        random: RandomGenerator,
        x: Double,
        y: Double,
        z: Double,
        horizontalScale: Double,
        verticalScale: Double,
        mask: CarvingMask,
        predicate: SkipOrWaterPredicate
    ): Boolean {
        val chunkPos = chunk.pos
        val posX = chunkPos.centerX.toDouble()
        val posZ = chunkPos.centerZ.toDouble()
        val f = 16.0 + horizontalScale * 2.0
        if (!(abs(x - posX) > f) && !(abs(z - posZ) > f)) {
            val startX = chunkPos.startX
            val startZ = chunkPos.startZ
            val xMin = max((MathHelper.floor(x - horizontalScale) - startX - 1.0), 0.0).toInt()
            val xMax = min((MathHelper.floor(x + horizontalScale) - startX).toDouble(), 15.0).toInt()
            val yMin = max((MathHelper.floor(y - verticalScale) - 1.0), (context.minY + 1.0)).toInt()
            val n = if (chunk.hasBelowZeroRetrogen()) 0 else 7
            val yMax =
                min(
                    (MathHelper.floor(y + verticalScale) + 1.0),
                    (context.minY + context.height - 1.0 - n)
                ).toInt()
            val zMin = max((MathHelper.floor(z - horizontalScale) - startZ - 1.0), 0.0).toInt()
            val zMax = min((MathHelper.floor(z + horizontalScale) - startZ).toDouble(), 15.0).toInt()
            var bl = false
            val mutable = BlockPos.Mutable()

            val chunkRandom = ChunkRandom(LegacySimpleRandom(0)) ////////HOW TO GET SEED OF WORLD
            val dps = DoublePerlinNoiseSampler.create(chunkRandom, -4, 2.0, 1.0, 0.0)

            for (loopX in xMin..xMax) {
                val chunkX = chunkPos.getOffsetX(loopX)
                val funX = (chunkX + 0.5 - x) / horizontalScale

                for (loopZ in zMin..zMax) {
                    val chunkZ = chunkPos.getOffsetZ(loopZ)
                    val funZ = (chunkZ + 0.5 - z) / horizontalScale
                    if (!(funX * funX + funZ * funZ >= 1.0)) {

                        for (loopY in yMax downTo yMin + 1) {
                            val funY = (loopY - 0.5 - y) / verticalScale

                            val sample = dps.sample(chunkX.toDouble(), loopY * 0.25, chunkZ.toDouble())
                            val sampleMathed = abs(sample)
                            val output = predicate.shouldSkip(context, funX, funY, funZ, loopY, sampleMathed)
                            if (output != 0 && (!mask[loopX, loopY, loopZ] || isDebug(config))) {
                                mask[loopX, loopY] = loopZ
                                mutable[chunkX, loopY] = chunkZ
                                bl = bl or carveAtPoint(
                                    context,
                                    config,
                                    chunk,
                                    random,
                                    mutable,
                                    sampler,
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
        context: CarverContext,
        config: GeodeCarverConfig,
        chunk: Chunk,
        random: RandomGenerator,
        pos: BlockPos.Mutable,
        sampler: AquiferSampler,
        predicateResult: Int,
    ): Boolean {
        val blockState = chunk.getBlockState(pos)
        if (!this.canReplaceBlock(config, blockState) && !isDebug(config)) {
            return false
        } else {
            val blockState2 =
                this.getState(context, config, random, pos, sampler, predicateResult)
            if (blockState2 == null) {
                return false
            } else {
                chunk.setBlockState(pos, blockState2, false)
                if (sampler.needsFluidTick() && !blockState2.fluidState.isEmpty) {
                    chunk.markBlockForPostProcessing(pos)
                }
                return true
            }
        }
    }


    private fun getState(
        context: CarverContext,
        config: GeodeCarverConfig,
        random: RandomGenerator,
        pos: BlockPos,
        sampler: AquiferSampler,
        predicateResult: Int,
    ): BlockState? {
        val amethyst =
            if (random.nextInt(10) == 0)
                Blocks.BUDDING_AMETHYST.defaultState
            else
                Blocks.AMETHYST_BLOCK.defaultState
        val state = when (predicateResult) {
            1 -> Blocks.SMOOTH_BASALT.defaultState
            2 -> Blocks.CALCITE.defaultState
            3 -> amethyst
            4 -> Blocks.AIR.defaultState
            else -> Blocks.AIR.defaultState
        }

        val debug = isDebug(config)
        if (state.isAir && pos.y <= config.lavaLevel.getY(context)) {
            return LAVA.blockState
        } else {
            val aquiferState = sampler.apply(DensityFunction.SinglePointContext(pos.x, pos.y, pos.z), 0.0)
            if (aquiferState == null) {
                return if (debug) config.debugConfig.barrierState
                else if (state.isAir) amethyst
                else state
            } else {
                if (state.isAir) {
                    return if (debug) getDebugState(config, aquiferState)
                    else aquiferState
                }

                return state
            }
        }
    }

    private fun getState(
        scaledRelativeX: Double,
        scaledRelativeY: Double,
        scaledRelativeZ: Double,
        dps: Double
    ): Int {
        val value =
            scaledRelativeX * scaledRelativeX + scaledRelativeY * scaledRelativeY + scaledRelativeZ * scaledRelativeZ + dps

        return if (value <= 1.0) {
            if (value <= 0.8) {
                if (value <= 0.65) {
                    if (value <= 0.5) {
                        4
                    } else 3
                } else 2
            } else 1
        } else 0


        // 0 means nothing
        // 1 means smooth basalt
        // 2 means calcite
        // 3 means amethyst
        // 4 means air
    }


    fun interface SkipOrWaterPredicate {
        fun shouldSkip(carverContext: CarverContext, x: Double, y: Double, z: Double, waterY: Int, sample: Double): Int
    }
}