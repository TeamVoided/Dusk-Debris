package org.teamvoided.dusk_debris.particle

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.network.RegistryByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.codec.PacketCodecs
import net.minecraft.particle.ParticleEffect
import net.minecraft.particle.ParticleType
import net.minecraft.particle.ParticleTypes
import org.teamvoided.dusk_debris.init.DuskParticles

class StationaryEmitterParticleEffect(
    private val particle: ParticleEffect,
    private val maxAge: Int,
    private val delayBetween: Int
) : ParticleEffect {
    override fun getType(): ParticleType<StationaryEmitterParticleEffect> {
        return DuskParticles.STATIONARY_EMITTER
    }

    fun particle(): ParticleEffect {
        if (particle.type == this.type) return ParticleTypes.SMOKE
        else return this.particle
    }

    fun maxAge(): Int {
        return this.maxAge
    }

    fun delayBetween(): Int {
        return this.delayBetween
    }

    companion object {
        val CODEC: MapCodec<StationaryEmitterParticleEffect> =
            RecordCodecBuilder.mapCodec { instance: RecordCodecBuilder.Instance<StationaryEmitterParticleEffect> ->
                instance.group(
                    ParticleTypes.TYPE_CODEC.fieldOf("particle")
                        .forGetter { obj: StationaryEmitterParticleEffect -> obj.particle() },
                    Codec.INT.fieldOf("max_age")
                        .forGetter { obj: StationaryEmitterParticleEffect -> obj.maxAge() },
                    Codec.INT.fieldOf("delay_between")
                        .forGetter { obj: StationaryEmitterParticleEffect -> obj.delayBetween() }
                ).apply(instance, ::StationaryEmitterParticleEffect)
            }
        val PACKET_CODEC: PacketCodec<RegistryByteBuf, StationaryEmitterParticleEffect> = PacketCodec.tuple(
            ParticleTypes.CODEC,
            { obj: StationaryEmitterParticleEffect -> obj.particle() },
            PacketCodecs.VAR_INT,
            { obj: StationaryEmitterParticleEffect -> obj.maxAge() },
            PacketCodecs.VAR_INT,
            { obj: StationaryEmitterParticleEffect -> obj.delayBetween() },
            ::StationaryEmitterParticleEffect
        )
    }
}