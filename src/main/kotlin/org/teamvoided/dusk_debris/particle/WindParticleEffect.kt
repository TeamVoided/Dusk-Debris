package org.teamvoided.dusk_debris.particle

import com.mojang.serialization.Codec
import com.mojang.serialization.DataResult
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.network.RegistryByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.codec.PacketCodecs
import net.minecraft.particle.ParticleEffect
import net.minecraft.particle.ParticleType
import net.minecraft.util.math.Direction
import net.minecraft.world.event.EntityPositionSource
import net.minecraft.world.event.PositionSource
import org.teamvoided.dusk_debris.init.DuskParticles

class WindParticleEffect(
    private val distance: Double,
    private val direction: Int,
    private val arrivalTicks: Int
) :
    ParticleEffect {
    override fun getType(): ParticleType<WindParticleEffect> {
        return DuskParticles.WIND
    }

    fun distance(): Double {
        return this.distance
    }

    fun direction(): Int {
        return this.direction
    }

    fun arrivalTicks(): Int {
        return this.arrivalTicks
    }

    companion object {
        val CODEC: MapCodec<WindParticleEffect> =
            RecordCodecBuilder.mapCodec { instance: RecordCodecBuilder.Instance<WindParticleEffect> ->
                instance.group(
                    Codec.DOUBLE.fieldOf("destination")
                        .forGetter { obj: WindParticleEffect -> obj.distance() },
                    Codec.INT.fieldOf("direction")
                        .forGetter { obj: WindParticleEffect -> obj.direction() },
                    Codec.INT.fieldOf("arrival_in_ticks")
                        .forGetter { obj: WindParticleEffect -> obj.arrivalTicks() }
                ).apply(instance, ::WindParticleEffect)
            }
        val PACKET_CODEC: PacketCodec<RegistryByteBuf, WindParticleEffect> = PacketCodec.tuple(
            PacketCodecs.DOUBLE,
            { obj: WindParticleEffect -> obj.distance() },
            PacketCodecs.VAR_INT,
            { obj: WindParticleEffect -> obj.direction() },
            PacketCodecs.VAR_INT,
            { obj: WindParticleEffect -> obj.arrivalTicks() },
            ::WindParticleEffect
        )
    }
}