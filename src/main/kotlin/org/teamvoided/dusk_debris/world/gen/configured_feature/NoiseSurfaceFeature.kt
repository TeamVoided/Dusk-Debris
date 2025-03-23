package org.teamvoided.dusk_debris.world.gen.configured_feature

import com.mojang.serialization.Codec
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.gen.feature.Feature
import net.minecraft.world.gen.feature.util.DripstoneHelper
import net.minecraft.world.gen.feature.util.FeatureContext
import org.teamvoided.dusk_debris.world.gen.configured_feature.config.NoiseSurfaceFeatureConfig
import org.teamvoided.dusk_debris.world.gen.noise.FastNoise

class NoiseSurfaceFeature(codec: Codec<NoiseSurfaceFeatureConfig>) :
    Feature<NoiseSurfaceFeatureConfig>(codec) {
    override fun place(context: FeatureContext<NoiseSurfaceFeatureConfig>): Boolean {
        val origin = context.origin
        val random = context.random
        val world = context.world
        val config = context.config
        val noise = FastNoise(world.seed.toInt())
        noise.SetFractalType(FastNoise.FractalType.RigidMulti)
        noise.SetNoiseType(FastNoise.NoiseType.SimplexFractal)
        noise.SetFractalOctaves(1)
        noise.SetFrequency(0.004f)
        noise.SetGradientPerturbAmp(0.3f)
        val pos = BlockPos.Mutable()
        val chunkSizeOver2 = 8
        for (x: Int in -chunkSizeOver2..chunkSizeOver2) {
            for (z: Int in -chunkSizeOver2..chunkSizeOver2) {
                for (y: Int in -chunkSizeOver2..chunkSizeOver2) {
                    pos.set(origin.add(x, y, z))
                    val noise0 = noise.GetNoise(pos.x.toFloat(), pos.y.toFloat(), pos.z.toFloat())
                    if (noise0 > config.threshold) {
                        val worldState = world.getBlockState(pos)
                        if (worldState.isIn(config.replaceable)) {
                            for (direction: Direction in Direction.entries) {
                                if (world.testBlockState(pos.offset(direction), DripstoneHelper::canGenerateOrLava)) {
                                    world.setBlockState(pos, config.blockstate.getBlockState(random, pos), 2)
                                    break
                                }
                            }
                        }
                    }
                }
            }
        }
        return true
    }
}