package org.teamvoided.dusk_debris.world.gen.configured_feature;

import com.mojang.serialization.Codec
import it.unimi.dsi.fastutil.doubles.DoubleArrayList
import net.minecraft.util.math.noise.DoublePerlinNoiseSampler
import net.minecraft.util.random.LegacySimpleRandom
import net.minecraft.util.random.RandomGenerator
import net.minecraft.world.gen.ChunkRandom
import net.minecraft.world.gen.feature.Feature
import net.minecraft.world.gen.feature.util.FeatureContext
import org.teamvoided.dusk_debris.world.gen.configured_feature.config.NoiseFeatureConfig

class RandomNoiseFeature(codec: Codec<NoiseFeatureConfig>) : Feature<NoiseFeatureConfig>(codec) {

    override fun place(context: FeatureContext<NoiseFeatureConfig>): Boolean {
        val cfg = context.config
        val random = context.random
        val world = context.world
        val gen = context.generator
        val pos = context.origin

        val chunkRandom = ChunkRandom(LegacySimpleRandom(world.seed))
        val dps = DoublePerlinNoiseSampler.create(chunkRandom, cfg.firstNoiseOctave, *cfg.amplitudes.toDoubleArray())
        val sample = dps.sample(pos.x.toDouble(), 0.0, pos.z.toDouble())

        for (features in cfg.features) {
            if (sample < features.threshold)
                return features.generate(world, gen, random, pos)
        }

        return cfg.defaultFeature.value().place(world, gen, random, pos)
    }
}