package org.teamvoided.dusk_debris.particle

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.core.Direction
import net.minecraft.core.particles.ParticleOptions
import net.minecraft.core.particles.ParticleType
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import org.teamvoided.dusk_debris.init.DuskParticles

class WindParticleEffect(
    val distance: Double,
    val direction: Direction,
    val arrivalTicks: Int
) :
    ParticleOptions {
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
        val PACKET_CODEC: StreamCodec<RegistryFriendlyByteBuf, WindParticleEffect> = StreamCodec.composite(
            ByteBufCodecs.DOUBLE,
            { it.distance },
            Direction.STREAM_CODEC,
            { it.direction },
            ByteBufCodecs.VAR_INT,
            { it.arrivalTicks },
            ::WindParticleEffect
        )
    }
}