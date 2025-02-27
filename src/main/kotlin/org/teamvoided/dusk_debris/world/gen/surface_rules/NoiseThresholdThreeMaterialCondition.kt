package org.teamvoided.dusk_debris.world.gen.surface_rules

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.util.dynamic.CodecHolder
import net.minecraft.util.math.noise.DoublePerlinNoiseSampler
import net.minecraft.world.gen.surfacebuilder.SurfaceRules

class NoiseThresholdThreeMaterialCondition(
    val noise: RegistryKey<DoublePerlinNoiseSampler.NoiseParameters>,
    val minThreshold: Double,
    val maxThreshold: Double
) : SurfaceRules.MaterialCondition {

    override fun codec(): CodecHolder<out SurfaceRules.MaterialCondition> = CONDITION_CODEC

    override fun apply(context: SurfaceRules.Context): SurfaceRules.Condition {
        val doublePerlinNoiseSampler = context.randomState.getOrCreateNoiseSampler(this.noise)

        class Condition : SurfaceRules.LazyHorizontalCondition(context) {
            override fun compute(): Boolean {
                val d = doublePerlinNoiseSampler.sample(
                    this.context.x.toDouble(),
                    this.context.y.toDouble(),
                    this.context.z.toDouble()
                )
                return d >= this@NoiseThresholdThreeMaterialCondition.minThreshold && d <= this@NoiseThresholdThreeMaterialCondition.maxThreshold
            }
        }
        return Condition()
    }

    companion object {
        val CONDITION_CODEC: CodecHolder<NoiseThresholdThreeMaterialCondition> =
            CodecHolder.method_42116(RecordCodecBuilder.mapCodec { instance: RecordCodecBuilder.Instance<NoiseThresholdThreeMaterialCondition> ->
                instance.group(
                    RegistryKey.codec(RegistryKeys.NOISE_PARAMETERS).fieldOf("noise").forGetter { it.noise },
                    Codec.DOUBLE.fieldOf("min_threshold").forGetter { it.minThreshold },
                    Codec.DOUBLE.fieldOf("max_threshold").forGetter { it.maxThreshold }
                ).apply(instance, ::NoiseThresholdThreeMaterialCondition)
            })
    }
}