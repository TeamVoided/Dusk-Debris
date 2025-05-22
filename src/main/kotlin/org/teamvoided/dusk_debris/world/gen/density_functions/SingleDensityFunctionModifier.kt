package org.teamvoided.dusk_debris.world.gen.density_functions

import net.minecraft.util.StringIdentifiable
import net.minecraft.util.dynamic.CodecHolder
import net.minecraft.world.gen.DensityFunction
import net.minecraft.world.gen.DensityFunctions
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


    enum class Type(private val type: String, val neverBelow0: Boolean = false) : StringIdentifiable {
        ROUND("round"),
        FLOOR("floor"),
        CEIL("ceil"),
        SQUARE_ROOT("square_root", true),
        CUBE_ROOT("cube_root"), ;

        val codec: CodecHolder<SingleDensityFunctionModifier> =
            DensityFunctions.singleFunctionArgumentCodec({ create(this, it) }, { it.input() })

        override fun asString(): String = this.type
    }

    companion object {
        fun create(type: Type, input: DensityFunction): SingleDensityFunctionModifier {
            val max1 = input.minValue()
            val max2 = transform(type, max1)
            val max3 = transform(type, input.maxValue())
            return if (type.neverBelow0)
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
                else -> {
                    throw MatchException(
                        "no transform found for: $type, ordinal integer: ${type.ordinal}",
                        null
                    )
                }
            }
            return computed
        }
    }
}
