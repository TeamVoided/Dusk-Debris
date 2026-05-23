package org.teamvoided.dusk_debris.world.gen.configured_feature

import com.mojang.serialization.Codec
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.level.block.MultifaceBlock
import net.minecraft.world.level.levelgen.feature.Feature
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext
import org.teamvoided.dusk_debris.util.Utils.vec3d
import org.teamvoided.dusk_debris.util.asProperty
import org.teamvoided.dusk_debris.world.gen.configured_feature.config.NoiseSurfaceFeatureConfig
import org.teamvoided.dusk_debris.world.gen.noise.FastNoise

class NoiseMultiSurfaceFeature(codec: Codec<NoiseSurfaceFeatureConfig>) :
    Feature<NoiseSurfaceFeatureConfig>(codec) {
    override fun place(context: FeaturePlaceContext<NoiseSurfaceFeatureConfig>): Boolean {
        val origin = context.origin()
        val random = context.random()
        val world = context.level()
        val config = context.config()
        val noise = FastNoise(world.seed.toInt())
        noise.SetFractalType(FastNoise.FractalType.RigidMulti)
        noise.SetNoiseType(FastNoise.NoiseType.SimplexFractal)
        noise.SetFractalOctaves(1)
        noise.SetFrequency(0.004f)
        noise.SetGradientPerturbAmp(0.3f)
        val pos = BlockPos.MutableBlockPos()
        for (x: Int in -8..8) {
            for (z: Int in -8..8) {
                for (y: Int in -8..8) {
                    pos.set(origin.offset(x, y, z))
                    val noise0 = noise.GetNoise(pos.center)
                    if (noise0 > config.threshold) {
                        val worldState = world.getBlockState(pos)
                        if (worldState.`is`(config.replaceable)) {
                            var blockstate = config.blockstate.getState(random, pos)
                            var canPlace = false
                            for (direction: Direction in Direction.entries) {
                                val pos2 = pos.offset(direction.normal)
                                val worldState2 = world.getBlockState(pos2)
                                if (MultifaceBlock.canAttachTo(world, direction, pos2, worldState2)) {
                                    val pos3 = pos.center.add(direction.normal.vec3d().scale(0.33333))
                                    val noise1 = noise.GetNoise(pos3)
                                    if (noise1 > config.threshold) {
                                        blockstate = blockstate.setValue(direction.asProperty(), true)
                                        canPlace = true
                                    }
                                }
                            }
                            if (canPlace)
                                world.setBlock(pos, blockstate, 2)
                        }
                    }
                }
            }
        }
        return true
    }
}