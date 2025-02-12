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
import kotlin.math.acos
import kotlin.math.cos

class DebugAxis(
    val axis: Direction.Axis,
    val axisScale: Double
) : DensityFunction {

    override fun compute(c: DensityFunction.FunctionContext): Double {
        val axis = when (axis) {
            Direction.Axis.X -> c.blockX()
            Direction.Axis.Y -> c.blockY()
            Direction.Axis.Z -> c.blockZ()
        }
        val mod1 = (axis - 1) % 2 - 1
        val mod2 = (-axis - 1) % 4 - 1
        val mod = if (mod2 < 1) mod2 else mod1
        return mod / axisScale
    }

    override fun fillArray(array: DoubleArray, context: ContextProvider) = context.fillAllDirectly(array, this)

    override fun mapAll(visitor: DensityFunction.Visitor): DensityFunction {
        return visitor.apply(
            DebugAxis(
                axis,
                axisScale
            )
        )
    }

    override fun minValue(): Double = 0.0

    override fun maxValue(): Double = 1.0

    override fun codec(): CodecHolder<DebugAxis> = CODEC

    companion object {
        private val DATA_CODEC: MapCodec<DebugAxis> =
            RecordCodecBuilder.mapCodec { instance ->
                instance.group(
                    Direction.Axis.CODEC.fieldOf("axis").forGetter { it.axis },
                    Codec.DOUBLE.fieldOf("axis_scale").forGetter { it.axisScale })
                    .apply(instance, ::DebugAxis)
            }

        val CODEC: CodecHolder<DebugAxis> = makeCodec(DATA_CODEC)
    }
}