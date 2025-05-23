package org.teamvoided.dusk_debris.world.gen.density_functions

import net.minecraft.util.StringIdentifiable
import net.minecraft.util.dynamic.CodecHolder
import net.minecraft.world.gen.DensityFunction
import net.minecraft.world.gen.DensityFunctions
import org.teamvoided.dusk_debris.util.Utils
import kotlin.math.*

data class SingleDensityFunctionModifier(
    val type: Type,
    val input: DensityFunction,
    val minValue: Double,
    val maxValue: Double
) : DensityFunctions.PureTransformer {
    override fun transform(input: Double): Double = transform(this.type, input)

    override fun mapAll(visitor: DensityFunction.Visitor): SingleDensityFunctionModifier =
        create(this.type, input.mapAll(visitor))

    override fun codec(): CodecHolder<out DensityFunction> = type.codec

    override fun input(): DensityFunction = this.input

    override fun minValue(): Double = this.minValue

    override fun maxValue(): Double = this.maxValue

    enum class Type(private val type: String) : StringIdentifiable {
        ROUND("round"),
        FLOOR("floor"),
        CEIL("ceil"),
        SQUARE_ROOT("square_root"), //never below 0
        CUBE_ROOT("cube_root"),
        SIN("sin"), //only between 1 and -1
        COS("cos"), //only between 1 and -1
        TAN("tan"); //this one has to do something if NAN, probably should just remove this one, too unpredictable?

        val codec: CodecHolder<SingleDensityFunctionModifier> =
            DensityFunctions.singleFunctionArgumentCodec({ create(this, it) }, { it.input })

        override fun asString(): String = this.type
    }

    companion object {
        fun create(type: Type, input: DensityFunction): SingleDensityFunctionModifier {
            if (type == Type.SIN || type == Type.COS) return SingleDensityFunctionModifier(type, input, -1.0, 1.0)

            val max1 = input.minValue()
            val max2 = transform(type, max1)
            val max3 = transform(type, input.maxValue())
            return if (type == Type.SQUARE_ROOT)
                SingleDensityFunctionModifier(type, input, max(0.0, max1), max(max2, max3))
            else
                SingleDensityFunctionModifier(type, input, max2, max3)
        }

        private fun transform(type: Type, density: Double): Double {
            val computed: Double
            when (type.ordinal) {
                0 -> computed = round(density)
                1 -> computed = floor(density)
                2 -> computed = ceil(density)
                3 -> computed = if (density > 0) sqrt(density) else 0.0
                4 -> computed = cbrt(density)
                5 -> computed = sin(Utils.rotate180 * density)
                6 -> computed = cos(Utils.rotate180 * density)
                7 -> {
                    val isNan = tan(Utils.rotate180 * density)
                    computed = if (!isNan.isNaN()) isNan else 0.0
                }

                else -> throw MatchException(
                    "no transform found for: $type, ordinal integer: ${type.ordinal}",
                    null
                )

            }
            return computed
        }
    }
}