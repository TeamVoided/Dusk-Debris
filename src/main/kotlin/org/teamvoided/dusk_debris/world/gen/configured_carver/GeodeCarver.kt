package org.teamvoided.dusk_debris.world.gen.configured_carver

import com.mojang.serialization.Codec
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.registry.Holder
import net.minecraft.state.property.Properties
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.ChunkPos
import net.minecraft.util.math.Direction
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
            SkipOrWaterPredicate { scaledRelativeX: Double, scaledRelativeY: Double, scaledRelativeZ: Double, dps: Double ->
                getState(scaledRelativeX, scaledRelativeY, scaledRelativeZ, dps)
            }

        val height: Double = config.yScale.get(random).toDouble()
        val radius: Float = config.horizontalRadius.get(random).toFloat()
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
                            val output = predicate.shouldSkip(funX, funY, funZ, sample)
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
            var blockState2 = this.getState(context, config, random, pos, sampler, predicateResult)
            if (blockState2 == null) {
                return false
            } else {
                if (predicateResult == 4 && random.nextInt(20) == 0 && !blockState2.isOf(Blocks.LAVA)) {
                    val crysDir = crystalDirection(
                        chunk,
                        pos,
                        config.extraInnerBlock.getBlockState(random, pos),
                        blockState2 == Blocks.WATER.defaultState
                    )
                    if (crysDir != null)
                        blockState2 = crysDir
                }

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
        val state = when (predicateResult) {
            1 -> config.outerLayerBlock.getBlockState(random, pos)
            2 -> config.middleLayerBlock.getBlockState(random, pos)
            3 -> config.innerLayerBlock.getBlockState(random, pos)
            4, 5 -> Blocks.CAVE_AIR.defaultState
            else -> Blocks.CAVE_AIR.defaultState
        }

        val debug = isDebug(config)
        if (state.isAir && pos.y <= config.lavaLevel.getY(context)) {
            return LAVA.blockState
        } else {
            val aquiferState = sampler.apply(DensityFunction.SinglePointContext(pos.x, pos.y, pos.z), 0.0)
            return if (aquiferState == null) {
                if (debug)
                    config.debugConfig.barrierState
                else if (state.isAir)
                    config.innerLayerBlock.getBlockState(random, pos)
                else
                    state
            } else {
                if (state.isAir) {
                    if (debug) getDebugState(config, aquiferState)
                    else aquiferState
                } else
                    state
            }
        }
    }

    private fun crystalDirection(
        chunk: Chunk,
        pos: BlockPos,
        crystal: BlockState,
        waterlogged: Boolean = false
    ): BlockState? {
        var retorn = crystal
        Direction.entries.forEach {
            if (chunk.getBlockState(pos.offset(it.opposite)).isFullCube(chunk, pos)) {
                if (crystal.contains(Properties.FACING)) {
                    retorn = retorn.with(Properties.FACING, it)
                }

                if (crystal.contains(Properties.WATERLOGGED)) {
                    retorn = retorn.with(Properties.WATERLOGGED, waterlogged)
                }

                return retorn
            }
        }
        return null
    }

    private fun getState(
        scaledRelativeX: Double,
        scaledRelativeY: Double,
        scaledRelativeZ: Double,
        dps: Double
    ): Int {
        val distance =
            scaledRelativeX * scaledRelativeX + scaledRelativeY * scaledRelativeY + scaledRelativeZ * scaledRelativeZ
        val value = distance + (dps * (1 - (distance * 0.9)))

        return when {
            value > 1.00 -> 0         // Nothing
            value > 0.85 -> 1         // Smooth Basalt
            value > 0.75 -> 2         // Calcite
            value > 0.60 -> 3         // Amethyst
            value > 0.55 -> 4         // Random Inner Decorator
            else -> 5                 // Air
        }
    }


    fun interface SkipOrWaterPredicate {
        fun shouldSkip( x: Double, y: Double, z: Double, sample: Double): Int
    }
}