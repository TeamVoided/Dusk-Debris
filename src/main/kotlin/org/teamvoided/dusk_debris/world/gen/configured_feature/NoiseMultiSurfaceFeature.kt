package org.teamvoided.dusk_debris.world.gen.configured_feature

import com.mojang.serialization.Codec
import net.minecraft.block.AbstractLichenBlock
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.gen.feature.Feature
import net.minecraft.world.gen.feature.util.DripstoneHelper
import net.minecraft.world.gen.feature.util.FeatureContext
import org.teamvoided.dusk_debris.util.Utils.vec3d
import org.teamvoided.dusk_debris.util.asProperty
import org.teamvoided.dusk_debris.world.gen.configured_feature.config.NoiseSurfaceFeatureConfig
import org.teamvoided.dusk_debris.world.gen.noise.FastNoise

class NoiseMultiSurfaceFeature(codec: Codec<NoiseSurfaceFeatureConfig>) :
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
        for (x: Int in -8..8) {
            for (z: Int in -8..8) {
                for (y: Int in -8..8) {
                    pos.set(origin.add(x, y, z))
                    val noise0 = noise.GetNoise(pos.ofCenter())
                    if (noise0 > config.threshold) {
                        val worldState = world.getBlockState(pos)
                        if (worldState.isIn(config.replaceable)) {
                            var blockstate = config.blockstate.getBlockState(random, pos)
                            var canPlace = false
                            for (direction: Direction in Direction.entries) {
                                val pos2 = pos.add(direction.vector)
                                val worldState2 = world.getBlockState(pos2)
                                if (AbstractLichenBlock.canGrowOn(world, direction, pos2, worldState2)) {
                                    val pos3 = pos.ofCenter().add(direction.vector.vec3d().multiply(0.33333))
                                    val noise1 = noise.GetNoise(pos3)
                                    if (noise1 > config.threshold) {
                                        blockstate = blockstate.with(direction.asProperty(), true)
                                        canPlace = true
                                    }
                                }
                            }
                            if (canPlace)
                                world.setBlockState(pos, blockstate, 2)
                        }
                    }
                }
            }
        }
        return true
    }
}