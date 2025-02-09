package org.teamvoided.dusk_debris.world.gen

import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.ChunkPos
import net.minecraft.util.math.ChunkSectionPos
import net.minecraft.util.math.MathHelper
import net.minecraft.util.random.PositionalRandomFactory
import net.minecraft.world.biome.source.util.OverworldBiomeParameters
import net.minecraft.world.dimension.DimensionType
import net.minecraft.world.gen.DensityFunction
import net.minecraft.world.gen.chunk.ChunkNoiseSampler
import net.minecraft.world.gen.noise.NoiseRouter
import org.apache.commons.lang3.mutable.MutableDouble
import java.util.*
import kotlin.math.abs
import kotlin.math.min

interface ConvertedAquiferSampler {
    fun apply(densityFunctionContext: DensityFunction.FunctionContext, baseNoise: Double): BlockState?

    fun needsFluidTick(): Boolean

    class Impl internal constructor(
        private val chunkNoiseSampler: ChunkNoiseSampler,
        pos: ChunkPos,
        noise: NoiseRouter,
        private val positionalRandomFactory: PositionalRandomFactory,
        startY: Int,
        endY: Int,
        private val globalFluidPicker: FluidPicker
    ) : ConvertedAquiferSampler {
        private val barrierNoise: DensityFunction = noise.barrierNoise()
        private val fluidLevelFloodednessNoise: DensityFunction = noise.fluidLevelFloodednessNoise()
        private val fluidLevelSpreadNoise: DensityFunction = noise.fluidLevelSpreadNoise()
        private val lavaNoise: DensityFunction = noise.lavaNoise()
        private val aquiferCache: Array<FluidStatus?>
        private val aquiferLocationCache: LongArray
        private val erosion: DensityFunction = noise.erosion()
        private val depth: DensityFunction = noise.depth()
        private var needsFluidTick = false
        private val startX: Int
        private val startY: Int
        private val startZ: Int
        private val sizeX: Int
        private val sizeZ: Int

        init {
            this.startX = this.getLocalX(pos.startX) - 1
            val i = this.getLocalX(pos.endX) + 1
            this.sizeX = i - this.startX + 1
            this.startY = this.getLocalY(startY) - 1
            val j = this.getLocalY(startY + endY) + 1
            val k = j - this.startY + 1
            this.startZ = this.getLocalZ(pos.startZ) - 1
            val l = this.getLocalZ(pos.endZ) + 1
            this.sizeZ = l - this.startZ + 1
            val m = this.sizeX * k * this.sizeZ
            this.aquiferCache = arrayOfNulls(m)
            this.aquiferLocationCache = LongArray(m)
            Arrays.fill(this.aquiferLocationCache, Long.MAX_VALUE)
        }

        private fun index(x: Int, y: Int, z: Int): Int {
            val i = x - this.startX
            val j = y - this.startY
            val k = z - this.startZ
            return (j * this.sizeZ + k) * this.sizeX + i
        }

        override fun apply(densityFunctionContext: DensityFunction.FunctionContext, baseNoise: Double): BlockState? {
            val posX = densityFunctionContext.blockX()
            val posY = densityFunctionContext.blockY()
            val posz = densityFunctionContext.blockZ()
            if (baseNoise > 0.0) {
                this.needsFluidTick = false
                return null
            } else {
                val fluidStatus = globalFluidPicker.computeFluid(posX, posY, posz)
                if (fluidStatus.getBlockState(posY).isOf(Blocks.LAVA)) {
                    this.needsFluidTick = false
                    return Blocks.LAVA.defaultState
                } else {
                    val l = Math.floorDiv(posX - 5, 16)
                    val m = Math.floorDiv(posY + 1, 12)
                    val n = Math.floorDiv(posz - 5, 16)
                    var o = Int.MAX_VALUE
                    var p = Int.MAX_VALUE
                    var q = Int.MAX_VALUE
                    var r = 0L
                    var s = 0L
                    var t = 0L

                    for (xLoop in 0..1) {
                        for (yLoop in -1..1) {
                            for (zLoop in 0..1) {
                                val x = l + xLoop
                                val y = m + yLoop
                                val z = n + zLoop
                                val aa = this.index(x, y, z)
                                val ab = aquiferLocationCache[aa]
                                var ac: Long
                                if (ab != Long.MAX_VALUE) {
                                    ac = ab
                                } else {
                                    val randomGenerator = positionalRandomFactory.create(x, y, z)
                                    ac = BlockPos.asLong(
                                        x * 16 + randomGenerator.nextInt(10),
                                        y * 12 + randomGenerator.nextInt(9),
                                        z * 16 + randomGenerator.nextInt(10)
                                    )
                                    aquiferLocationCache[aa] = ac
                                }

                                val ad = BlockPos.unpackLongX(ac) - posX
                                val ae = BlockPos.unpackLongY(ac) - posY
                                val af = BlockPos.unpackLongZ(ac) - posz
                                val ag = ad * ad + ae * ae + af * af
                                if (o >= ag) {
                                    t = s
                                    s = r
                                    r = ac
                                    q = p
                                    p = o
                                    o = ag
                                } else if (p >= ag) {
                                    t = s
                                    s = ac
                                    q = p
                                    p = ag
                                } else if (q >= ag) {
                                    t = ac
                                    q = ag
                                }
                            }
                        }
                    }

                    val fluidStatus2 = this.getWaterLevel(r)
                    val d = maxDistance(o, p)
                    val blockState = fluidStatus2.getBlockState(posY)
                    if (d <= 0.0) {
                        this.needsFluidTick = d >= FLOWING_UPDATE_SIMILARITY
                        return blockState
                    } else if (blockState.isOf(Blocks.WATER) &&
                        globalFluidPicker.computeFluid(posX, posY - 1, posz).getBlockState(posY - 1).isOf(Blocks.LAVA)
                    ) {
                        this.needsFluidTick = true
                        return blockState
                    } else {
                        val mutableDouble = MutableDouble(Double.NaN)
                        val fluidStatus3 = this.getWaterLevel(s)
                        val e =
                            d * this.calculateDensity(densityFunctionContext, mutableDouble, fluidStatus2, fluidStatus3)
                        if (baseNoise + e > 0.0) {
                            this.needsFluidTick = false
                            return null
                        } else {
                            val fluidStatus4 = this.getWaterLevel(t)
                            val f = maxDistance(o, q)
                            var g: Double
                            if (f > 0.0) {
                                g = d * f * this.calculateDensity(
                                    densityFunctionContext,
                                    mutableDouble,
                                    fluidStatus2,
                                    fluidStatus4
                                )
                                if (baseNoise + g > 0.0) {
                                    this.needsFluidTick = false
                                    return null
                                }
                            }

                            g = maxDistance(p, q)
                            if (g > 0.0) {
                                val h = d * g * this.calculateDensity(
                                    densityFunctionContext,
                                    mutableDouble,
                                    fluidStatus3,
                                    fluidStatus4
                                )
                                if (baseNoise + h > 0.0) {
                                    this.needsFluidTick = false
                                    return null
                                }
                            }

                            this.needsFluidTick = true
                            return blockState
                        }
                    }
                }
            }
        }

        override fun needsFluidTick(): Boolean {
            return this.needsFluidTick
        }

        private fun calculateDensity(
            context: DensityFunction.FunctionContext,
            barrierNoise: MutableDouble,
            firstFluidStatus: FluidStatus,
            secondFluidStatus: FluidStatus
        ): Double {
            val i = context.blockY()
            val blockState = firstFluidStatus.getBlockState(i)
            val blockState2 = secondFluidStatus.getBlockState(i)
            if ((!blockState.isOf(Blocks.LAVA) || !blockState2.isOf(Blocks.WATER)) &&
                (!blockState.isOf(Blocks.WATER) || !blockState2.isOf(Blocks.LAVA))
            ) {
                val j = abs((firstFluidStatus.fluidLevel - secondFluidStatus.fluidLevel).toDouble()).toInt()
                if (j == 0) {
                    return 0.0
                } else {
                    val d = 0.5 * (firstFluidStatus.fluidLevel + secondFluidStatus.fluidLevel).toDouble()
                    val e = i.toDouble() + 0.5 - d
                    val f = j.toDouble() / 2.0
                    val g = 0.0
                    val h = 2.5
                    val k = 1.5
                    val l = 3.0
                    val m = 10.0
                    val n = 3.0
                    val o = f - abs(e)
                    val q: Double
                    var p: Double
                    if (e > 0.0) {
                        p = 0.0 + o
                        q = if (p > 0.0) {
                            p / 1.5
                        } else {
                            p / 2.5
                        }
                    } else {
                        p = 3.0 + o
                        q = if (p > 0.0) {
                            p / 3.0
                        } else {
                            p / 10.0
                        }
                    }

                    p = 2.0
                    val r: Double
                    if (!(q < -2.0) && !(q > 2.0)) {
                        val s = barrierNoise.value
                        if (java.lang.Double.isNaN(s)) {
                            val t = this.barrierNoise.compute(context)
                            barrierNoise.setValue(t)
                            r = t
                        } else {
                            r = s
                        }
                    } else {
                        r = 0.0
                    }

                    return 2.0 * (r + q)
                }
            } else {
                return 2.0
            }
        }

        private fun getLocalX(x: Int): Int {
            return Math.floorDiv(x, 16)
        }

        private fun getLocalY(y: Int): Int {
            return Math.floorDiv(y, 12)
        }

        private fun getLocalZ(z: Int): Int {
            return Math.floorDiv(z, 16)
        }

        private fun getWaterLevel(pos: Long): FluidStatus {
            val i = BlockPos.unpackLongX(pos)
            val j = BlockPos.unpackLongY(pos)
            val k = BlockPos.unpackLongZ(pos)
            val l = this.getLocalX(i)
            val m = this.getLocalY(j)
            val n = this.getLocalZ(k)
            val o = this.index(l, m, n)
            val fluidStatus = aquiferCache[o]
            if (fluidStatus != null) {
                return fluidStatus
            } else {
                val fluidStatus2 = this.computeFluid(i, j, k)
                aquiferCache[o] = fluidStatus2
                return fluidStatus2
            }
        }

        private fun computeFluid(x: Int, y: Int, z: Int): FluidStatus {
            val fluidStatus = globalFluidPicker.computeFluid(x, y, z)
            var i = Int.MAX_VALUE
            val j = y + 12
            val k = y - 12
            var bl = false
            val offsetInChunks = SURFACE_SAMPLING_OFFSETS_IN_CHUNKS

            for (element in offsetInChunks) {
                val l = x + ChunkSectionPos.getBlockCoord(element[0])
                val m = z + ChunkSectionPos.getBlockCoord(element[1])
                val n = chunkNoiseSampler.getPreliminarySurfaceLevel(l, m)
                val o = n + 8
                val bl2 = element[0] == 0 && element[1] == 0
                if (bl2 && k > o) {
                    return fluidStatus
                }

                val bl3 = j > o
                if (bl3 || bl2) {
                    val fluidStatus2 = globalFluidPicker.computeFluid(l, o, m)
                    if (!fluidStatus2.getBlockState(o).isAir) {
                        if (bl2) {
                            bl = true
                        }

                        if (bl3) {
                            return fluidStatus2
                        }
                    }
                }

                i = min(i.toDouble(), n.toDouble()).toInt()
            }

            val p = this.computeFluidLevel(x, y, z, fluidStatus, i, bl)
            return FluidStatus(p, this.computeFluidType(x, y, z, fluidStatus, p))
        }

        private fun computeFluidLevel(
            blockX: Int,
            blockY: Int,
            blockZ: Int,
            status: FluidStatus,
            defaultFluidLevel: Int,
            surfaceHeightEstimate: Boolean
        ): Int {
            val singlePointContext = DensityFunction.SinglePointContext(blockX, blockY, blockZ)
            val d: Double
            val e: Double
            var i: Int
            if (OverworldBiomeParameters.deepDarkRegion(this.erosion, this.depth, singlePointContext)) {
                d = -1.0
                e = -1.0
            } else {
                i = defaultFluidLevel + 8 - blockY
                val f = if (surfaceHeightEstimate) MathHelper.clampedMap(i.toDouble(), 0.0, 64.0, 1.0, 0.0) else 0.0
                val g = MathHelper.clamp(fluidLevelFloodednessNoise.compute(singlePointContext), -1.0, 1.0)
                val h = MathHelper.map(f, 1.0, 0.0, -0.3, 0.8)
                val k = MathHelper.map(f, 1.0, 0.0, -0.8, 0.4)
                d = g - k
                e = g - h
            }

            i = if (e > 0.0) {
                status.fluidLevel
            } else if (d > 0.0) {
                computeRandomFluidLevel(blockX, blockY, blockZ, defaultFluidLevel)
            } else {
                DimensionType.FAR_BELOW_MIN_Y
            }

            return i
        }

        private fun computeRandomFluidLevel(x: Int, y: Int, z: Int, defaultFluidLevel: Int): Int {
            val k = Math.floorDiv(x, 16)
            val l = Math.floorDiv(y, 40)
            val m = Math.floorDiv(z, 16)
            val n = l * 40 + 20
            val d =
                fluidLevelSpreadNoise.compute(DensityFunction.SinglePointContext(k, l, m)) * 10.0
            val p = MathHelper.quantize(d, 3)
            val q = n + p
            return min(defaultFluidLevel.toDouble(), q.toDouble()).toInt()
        }

        private fun computeFluidType(x: Int, y: Int, z: Int, status: FluidStatus, fluidLevel: Int): BlockState {
            var blockState = status.state
            if (fluidLevel <= -10 && fluidLevel != DimensionType.FAR_BELOW_MIN_Y && status.state !== Blocks.LAVA.defaultState) {
                val k = Math.floorDiv(x, 64)
                val l = Math.floorDiv(y, 40)
                val m = Math.floorDiv(z, 64)
                val computedLavaNoise = lavaNoise.compute(DensityFunction.SinglePointContext(k, l, m))
                if (abs(computedLavaNoise) > 0.3) {
                    blockState = Blocks.LAVA.defaultState
                }
            }

            return blockState
        }

        companion object {
            private const val X_RANGE = 10
            private const val Y_RANGE = 9
            private const val Z_RANGE = 10
            private const val X_SEPARATION = 6
            private const val Y_SEPARATION = 3
            private const val Z_SEPARATION = 6
            private const val X_SPACING = 16
            private const val Y_SPACING = 12
            private const val Z_SPACING = 16
            private const val MAX_REASONABLE_DISTANCE_TO_AQUIFER_CENTER = 11
            private val FLOWING_UPDATE_SIMILARITY = maxDistance(MathHelper.square(10), MathHelper.square(12))
            private val SURFACE_SAMPLING_OFFSETS_IN_CHUNKS = arrayOf(
                intArrayOf(0, 0),
                intArrayOf(-2, -1),
                intArrayOf(-1, -1),
                intArrayOf(0, -1),
                intArrayOf(1, -1),
                intArrayOf(-3, 0),
                intArrayOf(-2, 0),
                intArrayOf(-1, 0),
                intArrayOf(1, 0),
                intArrayOf(-2, 1),
                intArrayOf(-1, 1),
                intArrayOf(0, 1),
                intArrayOf(1, 1)
            )

            private fun maxDistance(a: Int, b: Int): Double {
                val d = 25.0
                return 1.0 - abs((b - a).toDouble()) / 25.0
            }
        }
    }

    interface FluidPicker {
        fun computeFluid(x: Int, y: Int, z: Int): FluidStatus
    }

    class FluidStatus(val fluidLevel: Int, val state: BlockState) {
        fun getBlockState(y: Int): BlockState {
            return if (y < this.fluidLevel) this.state else Blocks.AIR.defaultState
        }
    }

    companion object {
        fun aquifer(
            chunkNoiseSampler: ChunkNoiseSampler,
            pos: ChunkPos,
            noiseRouter: NoiseRouter,
            positionalRandomFactory: PositionalRandomFactory,
            startY: Int,
            height: Int,
            globalFluidPicker: FluidPicker
        ): ConvertedAquiferSampler {
            return Impl(chunkNoiseSampler, pos, noiseRouter, positionalRandomFactory, startY, height, globalFluidPicker)
        }

        fun seaLevel(fluidPicker: FluidPicker): ConvertedAquiferSampler {
            return object : ConvertedAquiferSampler {
                override fun apply(
                    densityFunctionContext: DensityFunction.FunctionContext,
                    baseNoise: Double
                ): BlockState? {
                    return if (baseNoise > 0.0) null else fluidPicker.computeFluid(
                        densityFunctionContext.blockX(),
                        densityFunctionContext.blockY(),
                        densityFunctionContext.blockZ()
                    ).getBlockState(densityFunctionContext.blockY())
                }

                override fun needsFluidTick(): Boolean {
                    return false
                }
            }
        }
    }
}
