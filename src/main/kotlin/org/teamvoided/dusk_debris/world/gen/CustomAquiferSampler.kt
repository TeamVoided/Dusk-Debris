package org.teamvoided.dusk_debris.world.gen

import net.minecraft.world.level.ChunkPos
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.levelgen.*

interface CustomAquiferSampler : Aquifer {
    class ImplCustomNether internal constructor(
        private val chunkNoiseSampler: NoiseChunk,
        pos: ChunkPos,
        noise: NoiseRouter,
        private val positionalRandomFactory: PositionalRandomFactory,
        startY: Int,
        endY: Int,
        private val globalFluidPicker: Aquifer.FluidPicker
    ) : CustomAquiferSampler {
        private val barrierNoise: DensityFunction = noise.barrierNoise()
        private val fluidLevelFloodednessNoise: DensityFunction = noise.fluidLevelFloodednessNoise()
        private val fluidLevelSpreadNoise: DensityFunction = noise.fluidLevelSpreadNoise()

        private var needsFluidTick = false
//        private val startX: Int
//        private val startZ: Int
//        private val sizeX: Int
//        private val sizeZ: Int
//
//        init {
//            this.startX = this.getLocalX(pos.startX) - 1
//            val endX = this.getLocalX(pos.endX) + 1
//            this.sizeX = endX - this.startX + 1
//            this.startZ = this.getLocalZ(pos.startZ) - 1
//            val endZ = this.getLocalZ(pos.endZ) + 1
//            this.sizeZ = endZ - this.startZ + 1
//        }
//
//        private fun getLocalX(x: Int): Int {
//            return Math.floorDiv(x, 16)
//        }
//
//        private fun getLocalZ(z: Int): Int {
//            return Math.floorDiv(z, 16)
//        }


        override fun computeSubstance(c: DensityFunction.FunctionContext, baseNoise: Double): BlockState? {
            if (baseNoise > 0.0) return null

            val posX = c.blockX()
            val posY = c.blockY()
            val posZ = c.blockZ()
            val floodedness = fluidLevelFloodednessNoise.compute(c)
            val posY2 = (posY - floodedness).toInt()
            return globalFluidPicker.computeFluid(posX, posY2, posZ).at(posY2)

//            if (floodedness > 0) {
//                val posY2 = (posY - ((floodedness * 32) / 4).toInt() * 4)
//                return globalFluidPicker.computeFluid(posX, posY2, posZ).getBlockState(posY2)
//            }

//            return AquiferSampler.seaLevel(globalFluidPicker).apply(c, noiseIDWJ)
        }

        override fun shouldScheduleFluidUpdate(): Boolean = needsFluidTick
    }

    companion object {
        const val NETHER_SEA_LEVEL = 32

        fun netherSeaLevel(
            chunkNoiseSampler: NoiseChunk,
            pos: ChunkPos,
            noiseRouter: NoiseRouter,
            positionalRandomFactory: PositionalRandomFactory,
            startY: Int,
            height: Int,
            globalFluidPicker: Aquifer.FluidPicker
        ): Aquifer {
            return ImplCustomNether(
                chunkNoiseSampler,
                pos,
                noiseRouter,
                positionalRandomFactory,
                startY,
                height,
                globalFluidPicker
            )
        }

        fun netherSeaLevel(fluidPicker: Aquifer.FluidPicker): Aquifer {
            return object : Aquifer {
                override fun computeSubstance(
                    densityFunctionContext: DensityFunction.FunctionContext,
                    baseNoise: Double
                ): BlockState? {
                    return if (baseNoise > 0.0) null else fluidPicker.computeFluid(
                        densityFunctionContext.blockX(),
                        densityFunctionContext.blockY(),
                        densityFunctionContext.blockZ()
                    ).at(densityFunctionContext.blockY())
                }

                override fun shouldScheduleFluidUpdate(): Boolean {
                    return false
                }
            }
        }
    }
}