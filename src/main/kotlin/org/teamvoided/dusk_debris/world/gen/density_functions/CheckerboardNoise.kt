package org.teamvoided.dusk_debris.world.gen.density_functions

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.util.dynamic.CodecHolder
import net.minecraft.util.math.MathHelper
import net.minecraft.world.gen.DensityFunction
import net.minecraft.world.gen.DensityFunction.ContextProvider
import org.teamvoided.dusk_debris.util.world_helper.makeCodec
import kotlin.math.ceil
import kotlin.math.floor

class CheckerboardNoise(
    val shiftX: DensityFunction,
    val shiftY: DensityFunction,
    val shiftZ: DensityFunction,
    val horizontalSize: Double,
    val transitionPercent: Double,
    val noise: DensityFunction.NoiseHolder
) : DensityFunction {

    override fun compute(c: DensityFunction.FunctionContext): Double {
        if (horizontalSize != 1.0 && transitionPercent != 0.0) {
            val y: Double = 0.0//c.blockY() + shiftY.compute(c)
            val xFloor: Double = checkerboardFloor(c.blockX(), horizontalSize, shiftX.compute(c))
            val zFloor: Double = checkerboardFloor(c.blockZ(), horizontalSize, shiftZ.compute(c))
            val xCeil: Double = checkerboardCeil(c.blockX(), horizontalSize, shiftX.compute(c))
            val zCeil: Double = checkerboardCeil(c.blockZ(), horizontalSize, shiftZ.compute(c))

            val noiseXFZF = noise.sample(xFloor, y, zFloor)
            val noiseXCZF = noise.sample(xCeil, y, zFloor)
            val noiseXFZC = noise.sample(xFloor, y, zCeil)
            val noiseXCZC = noise.sample(xCeil, y, zCeil)

            val xLerp: Double
            val zLerp: Double
            if (transitionPercent != 1.0) {
                xLerp = lerpNum(c.blockX(), (shiftX.compute(c) / 20))
                zLerp = lerpNum(c.blockZ(), (shiftZ.compute(c) / 20))
            } else {
                xLerp = (((c.blockX() % horizontalSize) / horizontalSize))
                zLerp = (((c.blockZ() % horizontalSize) / horizontalSize))
            }

            val lerpXF = MathHelper.clampedLerp(noiseXFZF, noiseXCZF, xLerp)
            val lerpXC = MathHelper.clampedLerp(noiseXFZC, noiseXCZC, xLerp)
            val lerpZ = MathHelper.clampedLerp(lerpXF, lerpXC, zLerp)

            return lerpZ
        } else {
            val x: Double = checkerboardFloor(c.blockX(), horizontalSize, shiftX.compute(c))
            val y: Double = c.blockY() + shiftY.compute(c)
            val z: Double = checkerboardFloor(c.blockZ(), horizontalSize, shiftZ.compute(c))
            return noise.sample(x, y, z)
        }
    }

    fun lerpNum(posAxis: Int, shift: Double): Double {
        val tP = 0.5
        val mod = (((posAxis % horizontalSize) / horizontalSize) + shift)
        val sub = horizontalSize * ((1 - tP) / 2)
        return (mod - sub) / (horizontalSize * tP)
    }

//    private fun checkerboard(pos: Int, scale: Double, shift: Double): Double {
//        return scale * round((pos) / scale)
//    }

    private fun checkerboardFloor(pos: Int, scale: Double, shift: Double): Double {
        return scale * floor((pos) / scale)
    }

    private fun checkerboardCeil(pos: Int, scale: Double, shift: Double): Double {
        return scale * ceil((pos) / scale)
    }

    override fun fillArray(array: DoubleArray, context: ContextProvider) = context.fillAllDirectly(array, this)

    override fun mapAll(visitor: DensityFunction.Visitor): DensityFunction {
        return visitor.apply(
            CheckerboardNoise(
                shiftX.mapAll(visitor),
                shiftY.mapAll(visitor),
                shiftZ.mapAll(visitor),
                horizontalSize,
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
                    RANGE_SMALL.fieldOf("y_scale").orElse(0.0).forGetter { it.transitionPercent },
                    DensityFunction.NoiseHolder.CODEC.fieldOf("noise").forGetter { it.noise })
                    .apply(instance, ::CheckerboardNoise)
            }

        val CODEC: CodecHolder<CheckerboardNoise> = makeCodec(DATA_CODEC)
    }
}