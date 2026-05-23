package org.teamvoided.dusk_debris.particle

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.core.particles.ParticleOptions
import net.minecraft.core.particles.ParticleType
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import org.teamvoided.dusk_debris.init.DuskParticles

class GoopFlyingParticleEffect(
    private val maxAge: Int
) : ParticleOptions {
    override fun getType(): ParticleType<GoopFlyingParticleEffect> {
        return DuskParticles.ASTRAS_FLYING_GOOP
    }

    fun maxAge(): Int {
        return this.maxAge
    }

    companion object {
        val CODEC: MapCodec<GoopFlyingParticleEffect> =
            RecordCodecBuilder.mapCodec { instance: RecordCodecBuilder.Instance<GoopFlyingParticleEffect> ->
                instance.group(
                    Codec.INT.fieldOf("max_age")
                        .forGetter { obj: GoopFlyingParticleEffect -> obj.maxAge() },
                ).apply(instance, ::GoopFlyingParticleEffect)
            }
        val PACKET_CODEC: StreamCodec<RegistryFriendlyByteBuf, GoopFlyingParticleEffect> = StreamCodec.composite(
            ByteBufCodecs.VAR_INT,
            { obj: GoopFlyingParticleEffect -> obj.maxAge() },
            ::GoopFlyingParticleEffect
        )
    }
}