package org.teamvoided.dusk_debris.world.gen.density_functions

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.util.KeyDispatchDataCodec
import net.minecraft.world.level.levelgen.DensityFunction
import net.minecraft.world.level.levelgen.DensityFunction.ContextProvider
import org.teamvoided.dusk_debris.util.world_helper.makeCodec

class NoiseRange(
    val horizontalScale: Double,
    val verticalScale: Double,
    val minYRange: Double,
    val maxYRange: Double,
    val noise: DensityFunction.NoiseHolder
) : DensityFunction {

    override fun compute(c: DensityFunction.FunctionContext): Double {
        val y2: Int = c.blockY()
        val y3: Int =
            if (y2 < minYRange) minYRange.toInt()
            else if (y2 > maxYRange) maxYRange.toInt()
            else y2

        val x: Double = c.blockX() * this.horizontalScale
        val y: Double = y3 * this.verticalScale
        val z: Double = c.blockZ() * this.horizontalScale

        return noise.getValue(x, y, z)
    }

    override fun fillArray(array: DoubleArray, context: ContextProvider) = context.fillAllDirectly(array, this)

    override fun mapAll(visitor: DensityFunction.Visitor): DensityFunction {
        return visitor.apply(
            NoiseRange(
                horizontalScale,
                verticalScale,
                minYRange,
                maxYRange,
                visitor.visitNoise(this.noise)
            )
        )
    }

    override fun minValue(): Double = -this.maxValue()

    override fun maxValue(): Double = noise.maxValue()

    override fun codec(): KeyDispatchDataCodec<NoiseRange> = CODEC

    companion object {
        private val DATA_CODEC: MapCodec<NoiseRange> =
            RecordCodecBuilder.mapCodec { instance ->
                instance.group(
                    Codec.DOUBLE.fieldOf("xz_scale").forGetter { it.horizontalScale },
                    Codec.DOUBLE.fieldOf("y_scale").forGetter { it.verticalScale },
                    Codec.DOUBLE.fieldOf("min_y_range").forGetter { it.minYRange },
                    Codec.DOUBLE.fieldOf("max_y_range").forGetter { it.maxYRange },
                    DensityFunction.NoiseHolder.CODEC.fieldOf("noise").forGetter { it.noise })
                    .apply(instance, ::NoiseRange)
            }

        val CODEC: KeyDispatchDataCodec<NoiseRange> = makeCodec(DATA_CODEC)
    }
}