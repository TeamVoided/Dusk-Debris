package org.teamvoided.dusk_debris.world.gen.configured_feature.rock_spires

import com.mojang.serialization.Codec
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.util.math.BlockPos
import net.minecraft.util.random.RandomGenerator
import net.minecraft.world.StructureWorldAccess
import net.minecraft.world.gen.feature.DefaultFeatureConfig
import org.teamvoided.dusk_debris.world.gen.configured_feature.config.rock_spires.RockFormationFeatureConfig
import org.teamvoided.dusk_debris.world.gen.configured_feature.config.rock_spires.SurfaceFormationFeatureConfig
import org.teamvoided.dusk_debris.world.gen.noise.FastNoise

class SurfaceFormationFeature(codec: Codec<SurfaceFormationFeatureConfig>) : RockFormationFeature<SurfaceFormationFeatureConfig>(codec) {
    override fun getBlock(
        world: StructureWorldAccess,
        pos: BlockPos,
        config: SurfaceFormationFeatureConfig,
        random: RandomGenerator,
        distance: Double,
        yDepth: Int,
        noiseDecorator: FastNoise
    ): Pair<Boolean?, BlockState> {
        var boolean: Boolean? = null
        val block = when (yDepth) {
            0 -> {
                if (world.getBlockState(pos.up()).isOf(Blocks.AIR)) {
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
        }.getBlockState(random, pos)
        return (boolean to block)
    }
}