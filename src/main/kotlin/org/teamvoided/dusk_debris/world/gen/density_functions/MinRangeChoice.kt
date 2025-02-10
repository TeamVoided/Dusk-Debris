package org.teamvoided.dusk_debris.world.gen.density_functions

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.util.dynamic.CodecHolder
import net.minecraft.world.gen.DensityFunction
import org.teamvoided.dusk_debris.util.world_helper.makeCodec
import kotlin.math.max
import kotlin.math.min

//class MinRangeChoice(
//    val inverted: Boolean,
//    val input: DensityFunction,
//    val inclusive: Double,
//    val whenRange: DensityFunction
//) : DensityFunction {
//
//    override fun compute(context: DensityFunction.FunctionContext): Double {
//        val input = input.compute(context)
//        return if (comparable(input, this.inclusive)) input else whenRange.compute(context)
//    }
//
//    override fun fillArray(array: DoubleArray, context: DensityFunction.ContextProvider) {
//        this.input.fillArray(array, context)
//
//        for (i in array.indices) {
//            val input = array[i]
//            if (comparable(input, this.inclusive)) {
//                array[i] = this.input.compute(context.forIndex(i))
//            } else {
//                array[i] = this.whenRange.compute(context.forIndex(i))
//            }
//        }
//    }
//
//    override fun mapAll(visitor: DensityFunction.Visitor): DensityFunction {
//        return visitor.apply(
//            MinRangeChoice(
//                inverted,
//                input.mapAll(visitor),
//                inclusive,
//                whenRange.mapAll(visitor)
//            )
//        )
//    }
//
//
//    private fun comparable(input: Double, inclusive: Double): Boolean =
//        if (inverted)
//            input >= inclusive
//        else
//            input <= inclusive
//
//
//    override fun minValue(): Double = min(input.minValue(), whenRange.minValue())
//
//    override fun maxValue(): Double = max(input.maxValue(), whenRange.maxValue())
//
//    override fun codec(): CodecHolder<out DensityFunction> = CODEC
//
//    companion object {
//        val DATA_CODEC: MapCodec<MinRangeChoice> =
//            RecordCodecBuilder.mapCodec { instance: RecordCodecBuilder.Instance<MinRangeChoice> ->
//                instance.group(
//                    Codec.BOOL.fieldOf("inverted").forGetter { it.inverted },
//                    DensityFunction.HOLDER_HELPER_CODEC.fieldOf("input").forGetter { it.input },
//                    NOISE_VALUE_CODEC.fieldOf("inclusive").forGetter { it.inclusive },
//                    DensityFunction.HOLDER_HELPER_CODEC.fieldOf("when_range").forGetter { it.whenRange })
//                    .apply(instance, ::MinRangeChoice)
//            }
//        val NOISE_VALUE_CODEC: Codec<Double> =
//            Codec.doubleRange(-1000000.0, 1000000.0) //DensityFunctions.NOISE_VALUE_CODEC
//        val CODEC: CodecHolder<MinRangeChoice> = makeCodec(DATA_CODEC)
//    }
//}