package org.teamvoided.dusk_debris.world.gen.density_functions

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.util.dynamic.CodecHolder
import net.minecraft.world.gen.DensityFunction
import org.teamvoided.dusk_debris.util.world_helper.makeCodec
import kotlin.math.abs

class Fold(val densityFunction: DensityFunction) : DensityFunction {
    override fun compute(context: DensityFunction.FunctionContext): Double {
//        var function = densityFunction.compute(context)
//        function = abs(function)
//        function += -0.6666666666666666
//        function = abs(function)
//        function += -0.3333333333333333
//        function *= -3
        return (abs(abs(densityFunction.compute(context)) - 0.6666666666666666) - 0.3333333333333333) * -3
    }

    override fun fillArray(array: DoubleArray, context: DensityFunction.ContextProvider) =
        //ask why the two input density functions don't use fillArray
        context.fillAllDirectly(array, this)

    override fun mapAll(visitor: DensityFunction.Visitor): DensityFunction =
        visitor.apply(Fold(densityFunction.mapAll(visitor)))

    override fun minValue(): Double = densityFunction.minValue()

    override fun maxValue(): Double = densityFunction.maxValue()


    override fun codec(): CodecHolder<out DensityFunction> = CODEC

    companion object {
        private val DATA_CODEC: MapCodec<Fold> =
            RecordCodecBuilder.mapCodec { instance ->
                instance.group(
                    DensityFunction.HOLDER_HELPER_CODEC.fieldOf("density_function").forGetter { it.densityFunction })
                    .apply(instance, ::Fold)
            }

        val CODEC: CodecHolder<Fold> = makeCodec(DATA_CODEC)

    }
}