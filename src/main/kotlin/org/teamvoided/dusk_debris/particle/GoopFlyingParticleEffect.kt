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

class GoopFlyingParticleEffect(
    private val maxAge: Int
) : ParticleEffect {
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
        val PACKET_CODEC: PacketCodec<RegistryByteBuf, GoopFlyingParticleEffect> = PacketCodec.tuple(
            PacketCodecs.VAR_INT,
            { obj: GoopFlyingParticleEffect -> obj.maxAge() },
            ::GoopFlyingParticleEffect
        )
    }
}