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
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.world.level.chunk.CarvingMask
import net.minecraft.world.level.chunk.ChunkAccess
import net.minecraft.world.level.levelgen.Aquifer
import net.minecraft.world.level.levelgen.DensityFunction
import net.minecraft.world.level.levelgen.LegacyRandomSource
import net.minecraft.world.level.levelgen.WorldgenRandom
import net.minecraft.world.level.levelgen.carver.CarvingContext
import net.minecraft.world.level.levelgen.carver.WorldCarver
import net.minecraft.world.level.levelgen.synth.NormalNoise
import org.teamvoided.dusk_debris.world.gen.configured_carver.config.GeodeCarverConfig
import java.util.function.Function
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

class GeodeCarver(codec: Codec<GeodeCarverConfig>) : WorldCarver<GeodeCarverConfig>(codec) {
    override fun isStartChunk(ravineCarverConfig: GeodeCarverConfig, random: RandomSource): Boolean {
        return random.nextFloat() <= ravineCarverConfig.probability
    }

    override fun carve(
        carverContext: CarvingContext,
        config: GeodeCarverConfig,
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


//        chunk.setBlockState(Vec3d(posX, posY, posZ).toBlockPos(), Blocks.GLOWSTONE.defaultState, false)

        val skipPredicate =
            SkipOrWaterPredicate { scaledRelativeX: Double, scaledRelativeY: Double, scaledRelativeZ: Double, dps: Double ->
                getState(scaledRelativeX, scaledRelativeY, scaledRelativeZ, dps)
            }

        val height: Double = config.yScale.sample(random).toDouble()
        val radius: Float = config.horizontalRadius.sample(random).toFloat()
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
        context: CarvingContext,
        config: GeodeCarverConfig,
        chunk: ChunkAccess,
        aquiferSampler: Aquifer,
        random: RandomSource,
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
        context: CarvingContext,
        config: GeodeCarverConfig,
        chunk: ChunkAccess,
        sampler: Aquifer,
        random: RandomSource,
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

            val chunkRandom = WorldgenRandom(LegacyRandomSource(0)) ////////HOW TO GET SEED OF WORLD
            val dps = NormalNoise.create(chunkRandom, -4, 2.0, 1.0, 0.0)

            for (loopX in xMin..xMax) {
                val chunkX = chunkPos.getBlockX(loopX)
                val funX = (chunkX + 0.5 - x) / horizontalScale

                for (loopZ in zMin..zMax) {
                    val chunkZ = chunkPos.getBlockZ(loopZ)
                    val funZ = (chunkZ + 0.5 - z) / horizontalScale
                    if (!(funX * funX + funZ * funZ >= 1.0)) {

                        for (loopY in yMax downTo yMin + 1) {
                            val funY = (loopY - 0.5 - y) / verticalScale

                            val sample = dps.getValue(chunkX.toDouble(), loopY * 0.25, chunkZ.toDouble())
                            val output = predicate.shouldSkip(funX, funY, funZ, sample)
                            if (output != 0 && (!mask[loopX, loopY, loopZ] || isDebugEnabled(config))) {
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
        context: CarvingContext,
        config: GeodeCarverConfig,
        chunk: ChunkAccess,
        random: RandomSource,
        pos: BlockPos.MutableBlockPos,
        sampler: Aquifer,
        predicateResult: Int,
    ): Boolean {
        val blockState = chunk.getBlockState(pos)
        if (!this.canReplaceBlock(config, blockState) && !isDebugEnabled(config)) {
            return false
        } else {
            var blockState2 = this.getState(context, config, random, pos, sampler, predicateResult)
            if (blockState2 == null) {
                return false
            } else {
                if (predicateResult == 4 && random.nextInt(20) == 0 && !blockState2.`is`(Blocks.LAVA)) {
                    val crysDir = crystalDirection(
                        chunk,
                        pos,
                        config.extraInnerBlock.getState(random, pos),
                        blockState2 == Blocks.WATER.defaultBlockState()
                    )
                    if (crysDir != null)
                        blockState2 = crysDir
                }

                chunk.setBlockState(pos, blockState2, false)
                if (sampler.shouldScheduleFluidUpdate() && !blockState2.fluidState.isEmpty) {
                    chunk.markPosForPostprocessing(pos)
                }
                return true
            }
        }
    }


    private fun getState(
        context: CarvingContext,
        config: GeodeCarverConfig,
        random: RandomSource,
        pos: BlockPos,
        sampler: Aquifer,
        predicateResult: Int,
    ): BlockState? {
        val state = when (predicateResult) {
            1 -> config.outerLayerBlock.getState(random, pos)
            2 -> config.middleLayerBlock.getState(random, pos)
            3 -> config.innerLayerBlock.getState(random, pos)
            4, 5 -> Blocks.CAVE_AIR.defaultBlockState()
            else -> Blocks.CAVE_AIR.defaultBlockState()
        }

        val debug = isDebugEnabled(config)
        if (state.isAir && pos.y <= config.lavaLevel.resolveY(context)) {
            return LAVA.createLegacyBlock()
        } else {
            val aquiferState = sampler.computeSubstance(DensityFunction.SinglePointContext(pos.x, pos.y, pos.z), 0.0)
            return if (aquiferState == null) {
                if (debug)
                    config.debugSettings.barrierState
                else if (state.isAir)
                    config.innerLayerBlock.getState(random, pos)
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
        chunk: ChunkAccess,
        pos: BlockPos,
        crystal: BlockState,
        waterlogged: Boolean = false
    ): BlockState? {
        var retorn = crystal
        Direction.entries.forEach {
            if (chunk.getBlockState(pos.relative(it.opposite)).isCollisionShapeFullBlock(chunk, pos)) {
                if (crystal.hasProperty(BlockStateProperties.FACING)) {
                    retorn = retorn.setValue(BlockStateProperties.FACING, it)
                }

                if (crystal.hasProperty(BlockStateProperties.WATERLOGGED)) {
                    retorn = retorn.setValue(BlockStateProperties.WATERLOGGED, waterlogged)
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