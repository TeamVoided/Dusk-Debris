package org.teamvoided.dusk_debris.world.gen.density_functions

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.util.StringIdentifiable
import net.minecraft.util.dynamic.CodecHolder
import net.minecraft.world.gen.DensityFunction
import org.teamvoided.dusk_debris.util.world_helper.makeCodec
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.round

open class SingleDensityFunctionInput(val type: String, val densityFunction: DensityFunction) : DensityFunction {
    constructor(type: Type, densityFunction: DensityFunction) : this(type.asString(), densityFunction)

    override fun compute(context: DensityFunction.FunctionContext): Double =
        typeRound(Type.fromString(type), densityFunction.compute(context))


    override fun fillArray(array: DoubleArray, context: DensityFunction.ContextProvider) =
        //ask why the two input density functions don't use fillArray
        context.fillAllDirectly(array, this)

    override fun mapAll(visitor: DensityFunction.Visitor): DensityFunction =
        visitor.apply(SingleDensityFunctionInput(type, densityFunction.mapAll(visitor)))

    override fun minValue(): Double = typeRound(Type.fromString(type), densityFunction.minValue())

    override fun maxValue(): Double = typeRound(Type.fromString(type), densityFunction.maxValue())

    override fun codec(): CodecHolder<out DensityFunction> = CODEC

    enum class Type(private val type: String) : StringIdentifiable {
        ROUND("round"),
        FLOOR("floor"),
        CEIL("ceil");

        override fun asString(): String {
            return this.type
        }

        companion object {
            fun fromString(string: String): Type {
                return when (string) {
                    ROUND.type -> ROUND
                    FLOOR.type -> FLOOR
                    CEIL.type -> CEIL
                    else -> {
                        var acceptableValues = ""
                        Type.entries.forEach {
                            acceptableValues += it.type + " - "
                        }
                        throw MatchException(
                            "incorrect input for 'type' in SingleDensityFunction, given type $string, acceptable values are: [$acceptableValues]",
                            null
                        )
                    }
                }
            }
        }
    }

    fun typeRound(type: Type, double: Double): Double {
        return when (type) {
            Type.ROUND -> round(double)
            Type.FLOOR -> floor(double)
            Type.CEIL -> ceil(double)
            else -> {
                throw MatchException(
                    "typeRound call in SingleDensityFunction does not have a case for given type: $type",
                    null
                )
            }
        }
    }

    companion object {
        private val DATA_CODEC: MapCodec<SingleDensityFunctionInput> =
            RecordCodecBuilder.mapCodec { instance ->
                instance.group(
                    Codec.STRING.fieldOf("density_function").orElse("round").forGetter { it.type },
                    DensityFunction.HOLDER_HELPER_CODEC.fieldOf("density_function").forGetter { it.densityFunction }
                ).apply(instance, ::SingleDensityFunctionInput)
            }
        val CODEC: CodecHolder<SingleDensityFunctionInput> = makeCodec(DATA_CODEC)
    }
}