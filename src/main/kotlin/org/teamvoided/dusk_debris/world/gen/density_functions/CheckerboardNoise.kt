package org.teamvoided.dusk_debris.world.gen.density_functions

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.util.dynamic.CodecHolder
import net.minecraft.world.gen.DensityFunction
import net.minecraft.world.gen.DensityFunction.ContextProvider
import net.minecraft.world.gen.DensityFunctions
import org.teamvoided.dusk_debris.util.world_helper.makeCodec
import kotlin.math.round

class CheckerboardNoise(
    val shiftX: DensityFunction,
    val shiftY: DensityFunction,
    val shiftZ: DensityFunction,
    val horizontalSize: Double,
    val verticalScale: Double,
    val transitionPercent: Double,
    val noise: DensityFunction.NoiseHolder
) : DensityFunction {

    override fun compute(c: DensityFunction.FunctionContext): Double {
        if (horizontalSize != 1.0 && transitionPercent != 0.0) {
            val trans = transitionPercent / 2
            val transX = ((c.blockX() / horizontalSize) % 1)


            if (transX < trans) {

            } else if (transX > 1 - trans) {
                val x: Double = checkerboard(c.blockX(), horizontalSize, shiftX.compute(c))
//                val x1: Double = checkerboard()
            } else {

            }
        }


        val x: Double = checkerboard(c.blockX(), horizontalSize, shiftX.compute(c))
        val y: Double = c.blockY() + shiftY.compute(c)
        val z: Double = checkerboard(c.blockZ(), horizontalSize, shiftZ.compute(c))
        val noise1 = noise.sample(x, y, z)

//        if (horizontalScale != 1.0) {
//            if (((c.blockX() / horizontalScale) % 1) > threshold) {
//                val noisex = noise.sample(x, y, z)
//
//            }
//        }

        return noise1
    }

    private fun checkerboard(pos: Int, scale: Double, shift: Double): Double {
        return scale * round((pos + shift) / scale)
    }

    override fun fillArray(array: DoubleArray, context: ContextProvider) = context.fillAllDirectly(array, this)

    override fun mapAll(visitor: DensityFunction.Visitor): DensityFunction {
        return visitor.apply(
            CheckerboardNoise(
                shiftX.mapAll(visitor),
                shiftY.mapAll(visitor),
                shiftZ.mapAll(visitor),
                horizontalSize,
                verticalScale,
                transitionPercent,
                visitor.visitNoise(this.noise)
            )
        )
    }

    override fun minValue(): Double = -this.maxValue()

    override fun maxValue(): Double = noise.maxValue

    override fun codec(): CodecHolder<CheckerboardNoise> = CODEC

    companion object {
        val threshold = 0.8
        private val RANGE_BIG = Codec.doubleRange(1.0, 1000000.0)
        private val RANGE_SMALL = Codec.doubleRange(0.0, 1.0)
        private val DATA_CODEC: MapCodec<CheckerboardNoise> =
            RecordCodecBuilder.mapCodec { instance ->
                instance.group(
                    DensityFunction.HOLDER_HELPER_CODEC.fieldOf("shift_x").forGetter { it.shiftX },
                    DensityFunction.HOLDER_HELPER_CODEC.fieldOf("shift_y").forGetter { it.shiftY },
                    DensityFunction.HOLDER_HELPER_CODEC.fieldOf("shift_z").forGetter { it.shiftZ },
                    RANGE_BIG.fieldOf("xz_size").forGetter { it.horizontalSize },
                    RANGE_BIG.fieldOf("y_scale").orElse(1.0).forGetter { it.verticalScale },
                    RANGE_SMALL.fieldOf("y_scale").orElse(0.0).forGetter { it.transitionPercent },
                    DensityFunction.NoiseHolder.CODEC.fieldOf("noise").forGetter { it.noise })
                    .apply(instance, ::CheckerboardNoise)
            }

        val CODEC: CodecHolder<CheckerboardNoise> = makeCodec(DATA_CODEC)
    }
}