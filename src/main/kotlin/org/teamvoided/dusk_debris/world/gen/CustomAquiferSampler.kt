package org.teamvoided.dusk_debris.world.gen

import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.ChunkPos
import net.minecraft.util.math.MathHelper
import net.minecraft.util.random.PositionalRandomFactory
import net.minecraft.world.biome.source.util.OverworldBiomeParameters
import net.minecraft.world.dimension.DimensionType
import net.minecraft.world.gen.DensityFunction
import net.minecraft.world.gen.chunk.AquiferSampler
import net.minecraft.world.gen.chunk.ChunkNoiseSampler
import net.minecraft.world.gen.noise.NoiseRouter

interface CustomAquiferSampler : AquiferSampler {
    class ImplCustomNether internal constructor(
        private val chunkNoiseSampler: ChunkNoiseSampler,
        pos: ChunkPos,
        noise: NoiseRouter,
        private val positionalRandomFactory: PositionalRandomFactory,
        startY: Int,
        endY: Int,
        private val globalFluidPicker: AquiferSampler.FluidPicker
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


        override fun apply(c: DensityFunction.FunctionContext, noiseIDWJ: Double): BlockState? {
            if (noiseIDWJ > 0.0) return null

            val posX = c.blockX()
            val posY = c.blockY()
            val posZ = c.blockZ()
            val floodedness = fluidLevelFloodednessNoise.compute(c)

            if (floodedness > 0) {
                val posY2 = (posY - ((floodedness * 32) / 4).toInt() * 4)
                return globalFluidPicker.computeFluid(posX, posY2, posZ).getBlockState(posY2)
            }

            return AquiferSampler.seaLevel(globalFluidPicker).apply(c, noiseIDWJ)
        }

        override fun needsFluidTick(): Boolean = needsFluidTick
        
        
    }

    companion object {
        const val NETHER_SEA_LEVEL = 32

        fun netherSeaLevel(
            chunkNoiseSampler: ChunkNoiseSampler,
            pos: ChunkPos,
            noiseRouter: NoiseRouter,
            positionalRandomFactory: PositionalRandomFactory,
            startY: Int,
            height: Int,
            globalFluidPicker: AquiferSampler.FluidPicker
        ): AquiferSampler {
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

        fun netherSeaLevel(fluidPicker: AquiferSampler.FluidPicker): AquiferSampler {
            return object : AquiferSampler {
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