package org.teamvoided.dusk_debris.world.gen.density_functions

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.state.property.EnumProperty
import net.minecraft.util.StringIdentifiable
import net.minecraft.util.dynamic.CodecHolder
import net.minecraft.world.gen.DensityFunction
import org.teamvoided.dusk_debris.util.world_helper.makeCodec
import kotlin.math.max
import kotlin.math.min

class MinMaxRangeChoice private constructor(
    val input: DensityFunction,
    val inclusive: Double,
    val type: String,
    val whenRange: DensityFunction
) : DensityFunction {

    constructor(
        input: DensityFunction,
        inclusive: Double,
        type: Enum<minOrMax>,
        whenRange: DensityFunction
    ) : this(input, inclusive, type.toString(), whenRange)

    override fun compute(context: DensityFunction.FunctionContext): Double {
        val input = input.compute(context)
        return if (comparable(input, this.inclusive)) input else whenRange.compute(context)
    }

    override fun fillArray(array: DoubleArray, context: DensityFunction.ContextProvider) {
        this.input.fillArray(array, context)

        for (i in array.indices) {
            val input = array[i]
            if (comparable(input, this.inclusive)) {
                array[i] = this.input.compute(context.forIndex(i))
            } else {
                array[i] = this.whenRange.compute(context.forIndex(i))
            }
        }
    }

    override fun mapAll(visitor: DensityFunction.Visitor): DensityFunction {
        return visitor.apply(
            MinMaxRangeChoice(
                input.mapAll(visitor),
                inclusive,
                type,
                whenRange.mapAll(visitor)
            )
        )
    }


    private fun comparable(input: Double, inclusive: Double): Boolean =
        when (type) {
            minOrMax.MIN.toString() -> input >= inclusive
            minOrMax.MAX.toString() -> input <= inclusive
            else -> throw MatchException(
                "somehow managed to put in the wrong string value in a min_max_range_choice density function, acceptable values are 'min' or 'max'",
                null as Throwable?
            )
        }


    override fun minValue(): Double = min(input.minValue(), whenRange.minValue())

    override fun maxValue(): Double = max(input.maxValue(), whenRange.maxValue())

    override fun codec(): CodecHolder<out DensityFunction> = CODEC

    enum class minOrMax(val type: String) : StringIdentifiable {
        MIN("min"),
        MAX("max");

        override fun toString(): String = type
        override fun asString(): String = type
    }

    companion object {
        val DATA_CODEC: MapCodec<MinMaxRangeChoice> =
            RecordCodecBuilder.mapCodec { instance: RecordCodecBuilder.Instance<MinMaxRangeChoice> ->
                instance.group(
                    DensityFunction.HOLDER_HELPER_CODEC.fieldOf("input").forGetter { it.input },
                    NOISE_VALUE_CODEC.fieldOf("inclusive").forGetter { it.inclusive },
                    Codec.STRING.fieldOf("type").forGetter { it.type },
                    DensityFunction.HOLDER_HELPER_CODEC.fieldOf("when_range").forGetter { it.whenRange })
                    .apply(instance, ::MinMaxRangeChoice)
            }
        val NOISE_VALUE_CODEC: Codec<Double> =
            Codec.doubleRange(-1000000.0, 1000000.0) //DensityFunctions.NOISE_VALUE_CODEC
        val CODEC: CodecHolder<MinMaxRangeChoice> = makeCodec(DATA_CODEC)
    }
}