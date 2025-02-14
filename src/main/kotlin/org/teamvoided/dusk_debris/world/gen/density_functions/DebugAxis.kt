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
    val period: Double
) : DensityFunction {
    constructor(axis: Direction.Axis, period: Int) : this(axis, period.toDouble())

    override fun compute(c: DensityFunction.FunctionContext): Double {
        val axis = when (axis) {
            Direction.Axis.X -> kotlin.math.abs(c.blockX())
            Direction.Axis.Y -> kotlin.math.abs(c.blockY())
            Direction.Axis.Z -> kotlin.math.abs(c.blockZ())
        }

        val p = period
        val the = 2 * kotlin.math.abs(((axis / p) % 2.0) - 1) - 1
        return the  // abs(a * ((axis / p) % 2.0) - a) - (a / 2.0)
    }

    override fun fillArray(array: DoubleArray, context: ContextProvider) = context.fillAllDirectly(array, this)

    override fun mapAll(visitor: DensityFunction.Visitor): DensityFunction =
        visitor.apply(DebugAxis(axis, period))

    override fun minValue(): Double = -maxValue()

    override fun maxValue(): Double = 1.0

    override fun codec(): CodecHolder<DebugAxis> = CODEC

    companion object {
        private val DATA_CODEC: MapCodec<DebugAxis> =
            RecordCodecBuilder.mapCodec { instance ->
                instance.group(
                    Direction.Axis.CODEC.fieldOf("axis").forGetter { it.axis },
                    Codec.doubleRange(0.0, 1000000.0).fieldOf("period").forGetter { it.period })
                    .apply(instance, ::DebugAxis)
            }
        val CODEC: CodecHolder<DebugAxis> = makeCodec(DATA_CODEC)
    }
}