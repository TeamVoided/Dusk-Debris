package org.teamvoided.dusk_debris.world.gen.configured_feature

import com.mojang.serialization.Codec
import net.minecraft.world.level.levelgen.LegacyRandomSource
import net.minecraft.world.level.levelgen.WorldgenRandom
import net.minecraft.world.level.levelgen.feature.Feature
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext
import net.minecraft.world.level.levelgen.synth.NormalNoise
import org.teamvoided.dusk_debris.world.gen.configured_feature.config.NoiseFeatureConfig

class RandomNoiseFeature(codec: Codec<NoiseFeatureConfig>) : Feature<NoiseFeatureConfig>(codec) {

    override fun place(context: FeaturePlaceContext<NoiseFeatureConfig>): Boolean {
        val cfg = context.config()
        val random = context.random()
        val world = context.level()
        val gen = context.chunkGenerator()
        val pos = context.origin()

        val chunkRandom = WorldgenRandom(LegacyRandomSource(world.seed))
        val dps = NormalNoise.create(chunkRandom, cfg.firstNoiseOctave, *cfg.amplitudes.toDoubleArray())
        val sample = dps.getValue(pos.x.toDouble(), 0.0, pos.z.toDouble())

        for (features in cfg.features) {
            if (sample >= features.threshold)
                return features.generate(world, gen, random, pos)
        }

        return cfg.defaultFeature.value().place(world, gen, random, pos)
    }
}