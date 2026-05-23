package org.teamvoided.dusk_debris.particle

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes
import net.minecraft.core.particles.ParticleOptions
import net.minecraft.core.particles.ParticleType
import net.minecraft.core.particles.ParticleTypes
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import org.teamvoided.dusk_debris.init.DuskParticles

class StationaryEmitterParticleEffect(
    private val particle: ParticleOptions,
    private val maxAge: Int,
    private val delayBetween: Int
) : ParticleOptions {
    override fun getType(): ParticleType<StationaryEmitterParticleEffect> {
        return DuskParticles.STATIONARY_EMITTER
    }

    fun particle(): ParticleOptions {
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
                    ParticleTypes.CODEC.fieldOf("particle")
                        .forGetter { obj: StationaryEmitterParticleEffect -> obj.particle() },
                    Codec.INT.fieldOf("max_age")
                        .forGetter { obj: StationaryEmitterParticleEffect -> obj.maxAge() },
                    Codec.INT.fieldOf("delay_between")
                        .forGetter { obj: StationaryEmitterParticleEffect -> obj.delayBetween() }
                ).apply(instance, ::StationaryEmitterParticleEffect)
            }
        val PACKET_CODEC: StreamCodec<RegistryFriendlyByteBuf, StationaryEmitterParticleEffect> = StreamCodec.composite(
            ParticleTypes.STREAM_CODEC,
            { obj: StationaryEmitterParticleEffect -> obj.particle() },
            ByteBufCodecs.VAR_INT,
            { obj: StationaryEmitterParticleEffect -> obj.maxAge() },
            ByteBufCodecs.VAR_INT,
            { obj: StationaryEmitterParticleEffect -> obj.delayBetween() },
            ::StationaryEmitterParticleEffect
        )
        val REGISTER = FabricParticleTypes.complex(CODEC, PACKET_CODEC)
    }
}