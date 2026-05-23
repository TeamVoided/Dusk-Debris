package org.teamvoided.dusk_debris.world.gen.density_functions

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.core.Direction
import net.minecraft.util.KeyDispatchDataCodec
import net.minecraft.world.level.levelgen.DensityFunction
import net.minecraft.world.level.levelgen.DensityFunction.ContextProvider
import org.teamvoided.dusk_debris.util.world_helper.makeCodec
import kotlin.math.abs

class DebugAxis(
    val axis: Direction.Axis,
    val period: Double
) : DensityFunction {
    constructor(axis: Direction.Axis, period: Number) : this(axis, period.toDouble())

    override fun compute(c: DensityFunction.FunctionContext): Double {
        val axis = when (axis) {
            Direction.Axis.X -> abs(c.blockX())
            Direction.Axis.Y -> abs(c.blockY())
            Direction.Axis.Z -> abs(c.blockZ())
        }

        val p = period
        val the = 2 * abs((((axis + p) / p) % 2.0) - 1) - 1
        return the  // abs(a * ((axis / p) % 2.0) - a) - (a / 2.0)
    }

    override fun fillArray(array: DoubleArray, context: ContextProvider) = context.fillAllDirectly(array, this)

    override fun mapAll(visitor: DensityFunction.Visitor): DensityFunction =
        visitor.apply(DebugAxis(axis, period))

    override fun minValue(): Double = -maxValue()

    override fun maxValue(): Double = 1.0

    override fun codec(): KeyDispatchDataCodec<DebugAxis> = CODEC

    companion object {
        private val DATA_CODEC: MapCodec<DebugAxis> =
            RecordCodecBuilder.mapCodec { instance ->
                instance.group(
                    Direction.Axis.CODEC.fieldOf("axis").forGetter { it.axis },
                    Codec.doubleRange(0.0, 1000000.0).fieldOf("period").forGetter { it.period })
                    .apply(instance, ::DebugAxis)
            }
        val CODEC: KeyDispatchDataCodec<DebugAxis> = makeCodec(DATA_CODEC)
    }
}