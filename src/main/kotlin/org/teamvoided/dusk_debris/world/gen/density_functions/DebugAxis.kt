package org.teamvoided.dusk_debris.world.gen.density_functions

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.util.dynamic.CodecHolder
import net.minecraft.util.math.Direction
import net.minecraft.world.gen.DensityFunction
import net.minecraft.world.gen.DensityFunction.ContextProvider
import org.teamvoided.dusk_debris.util.Utils
import org.teamvoided.dusk_debris.util.world_helper.makeCodec
import kotlin.math.abs
import kotlin.math.acos
import kotlin.math.cos

class DebugAxis(
    val axis: Direction.Axis,
    val amplitude: Double,
    val period: Double
) : DensityFunction {
    constructor(axis: Direction.Axis, amplitude: Int, period: Int) : this(axis, amplitude.toDouble(), period.toDouble())
    constructor(axis: Direction.Axis, period: Double) : this(axis, 1.0, period)
    constructor(axis: Direction.Axis, period: Int) : this(axis, period.toDouble())


    override fun compute(c: DensityFunction.FunctionContext): Double {
        val axis = when (axis) {
            Direction.Axis.X -> c.blockX()
            Direction.Axis.Y -> c.blockY()
            Direction.Axis.Z -> c.blockZ()
        }
        val a = amplitude * 2.0
        val p = period
        return abs(a * ((axis / p) % 2.0) - a) - (a / 2.0)
    }

    override fun fillArray(array: DoubleArray, context: ContextProvider) = context.fillAllDirectly(array, this)

    override fun mapAll(visitor: DensityFunction.Visitor): DensityFunction =
        visitor.apply(DebugAxis(axis, amplitude, period))


    override fun minValue(): Double = -maxValue()

    override fun maxValue(): Double = amplitude * 2

    override fun codec(): CodecHolder<DebugAxis> = CODEC

    companion object {
        private val DATA_CODEC: MapCodec<DebugAxis> =
            RecordCodecBuilder.mapCodec { instance ->
                instance.group(
                    Direction.Axis.CODEC.fieldOf("axis").forGetter { it.axis },
                    Codec.doubleRange(0.0, 1000000.0).fieldOf("amplitude").forGetter { it.amplitude },
                    Codec.doubleRange(0.0, 1000000.0).fieldOf("period").forGetter { it.period })
                    .apply(instance, ::DebugAxis)
            }
        val CODEC: CodecHolder<DebugAxis> = makeCodec(DATA_CODEC)
    }
}