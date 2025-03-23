package org.teamvoided.dusk_debris.particle

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.network.RegistryByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.codec.PacketCodecs
import net.minecraft.particle.ParticleEffect
import net.minecraft.particle.ParticleType
import net.minecraft.util.math.Direction
import org.teamvoided.dusk_debris.init.DuskParticles

class WindParticleEffect(
    val distance: Double,
    val direction: Direction,
    val arrivalTicks: Int
) :
    ParticleEffect {
    override fun getType(): ParticleType<WindParticleEffect> {
        return DuskParticles.WIND
    }

    companion object {
        val CODEC: MapCodec<WindParticleEffect> =
            RecordCodecBuilder.mapCodec { instance: RecordCodecBuilder.Instance<WindParticleEffect> ->
                instance.group(
                    Codec.DOUBLE.fieldOf("destination").forGetter { it.distance },
                    Direction.CODEC.fieldOf("direction").forGetter { it.direction },
                    Codec.INT.fieldOf("arrival_in_ticks").forGetter { it.arrivalTicks }
                ).apply(instance, ::WindParticleEffect)
            }
        val PACKET_CODEC: PacketCodec<RegistryByteBuf, WindParticleEffect> = PacketCodec.tuple(
            PacketCodecs.DOUBLE,
            { it.distance },
            Direction.PACKET_CODEC,
            { it.direction },
            PacketCodecs.VAR_INT,
            { it.arrivalTicks },
            ::WindParticleEffect
        )
    }
}