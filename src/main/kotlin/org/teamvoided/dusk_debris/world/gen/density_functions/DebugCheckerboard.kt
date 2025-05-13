package org.teamvoided.dusk_debris.world.gen.density_functions

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import it.unimi.dsi.fastutil.doubles.DoubleArrayList
import it.unimi.dsi.fastutil.doubles.DoubleList
import it.unimi.dsi.fastutil.floats.FloatArrayList
import net.minecraft.util.Util
import net.minecraft.util.dynamic.CodecHolder
import net.minecraft.util.math.Direction
import net.minecraft.world.gen.DensityFunction
import net.minecraft.world.gen.DensityFunction.ContextProvider
import org.teamvoided.dusk_debris.util.world_helper.makeCodec
import kotlin.math.abs
import kotlin.math.floor

class DebugCheckerboard(
    val axis: Direction.Axis,
    val period: Int,
    val list: List<Float>,
) : DensityFunction {
    constructor(axis: Direction.Axis, period: Int, value1: Float, vararg values: Float) :
            this(axis, period, Util.make(FloatArrayList(values)) { it.add(0, value1) })

    override fun compute(c: DensityFunction.FunctionContext): Double {
        val axis = floor(
            when (axis) {
                Direction.Axis.X -> abs(c.blockX())
                Direction.Axis.Y -> abs(c.blockY())
                Direction.Axis.Z -> abs(c.blockZ())
            }.toDouble()
        ).toInt() / period
        val idx = (abs(axis) % list.size)
        return list[idx].toDouble()
    }

    override fun fillArray(array: DoubleArray, context: ContextProvider) = context.fillAllDirectly(array, this)

    override fun mapAll(visitor: DensityFunction.Visitor): DensityFunction =
        visitor.apply(DebugCheckerboard(axis, period, list))

    override fun minValue(): Double = list.min().toDouble()
    override fun maxValue(): Double = list.max().toDouble()

    override fun codec(): CodecHolder<DebugCheckerboard> = CODEC

    companion object {
        private val DATA_CODEC: MapCodec<DebugCheckerboard> =
            RecordCodecBuilder.mapCodec { instance ->
                instance.group(
                    Direction.Axis.CODEC.fieldOf("axis").forGetter { it.axis },
                    Codec.intRange(1, 1000000).fieldOf("period").forGetter { it.period },
                    Codec.FLOAT.listOf().fieldOf("values").forGetter { it.list }
                ).apply(instance, ::DebugCheckerboard)
            }
        val CODEC: CodecHolder<DebugCheckerboard> = makeCodec(DATA_CODEC)
    }
}