package org.teamvoided.dusk_debris.world.gen.configured_feature.rock_spires

import com.mojang.serialization.Codec
import net.minecraft.core.BlockPos
import net.minecraft.util.RandomSource
import net.minecraft.world.level.WorldGenLevel
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.BlockState
import org.teamvoided.dusk_debris.world.gen.configured_feature.config.rock_spires.SurfaceFormationFeatureConfig
import org.teamvoided.dusk_debris.world.gen.noise.FastNoise

class SurfaceFormationFeature(codec: Codec<SurfaceFormationFeatureConfig>) : RockFormationFeature<SurfaceFormationFeatureConfig>(codec) {
    override fun getBlock(
        world: WorldGenLevel,
        pos: BlockPos,
        config: SurfaceFormationFeatureConfig,
        random: RandomSource,
        distance: Double,
        yDepth: Int,
        noiseDecorator: FastNoise
    ): Pair<Boolean?, BlockState> {
        var boolean: Boolean? = null
        val block = when (yDepth) {
            0 -> {
                if (world.getBlockState(pos.above()).`is`(Blocks.AIR)) {
                    boolean = true
                    config.topState
                } else {
                    boolean = false
                    config.rockState
                }
            }

            1 -> {
                boolean = true
                config.underState
            }

            2, 3, 4 -> {
                boolean = true
                val toge = (yDepth - 3.5) / 3
                if (noiseDecorator.GetNoise(pos.x.toFloat(), 0f, pos.z.toFloat()) >= toge)
                    config.underState
                else config.rockState
            }

            else -> config.rockState
        }.getState(random, pos)
        return (boolean to block)
    }
}