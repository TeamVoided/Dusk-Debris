package org.teamvoided.dusk_debris.particle

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.network.RegistryByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.codec.PacketCodecs
import net.minecraft.particle.ParticleEffect
import net.minecraft.particle.ParticleType
import org.teamvoided.dusk_debris.init.DuskParticles
import java.awt.Color

class GunpowderExplosionEmitterParticleEffect(
    val radius: Float,
    val color: Color
) : ParticleEffect {
    constructor(
        radius: Float,
        color: Int
    ) : this(radius, Color(color))

    override fun getType(): ParticleType<GunpowderExplosionEmitterParticleEffect> =
        DuskParticles.GUNPOWDER_EXPLOSION_EMMITER

    companion object {
        val CODEC: MapCodec<GunpowderExplosionEmitterParticleEffect> =
            RecordCodecBuilder.mapCodec { instance ->
                instance.group(
                    Codec.FLOAT.fieldOf("radius").forGetter { it.radius },
                    Codec.INT.fieldOf("color").forGetter { it.color.rgb }
                ).apply(instance, ::GunpowderExplosionEmitterParticleEffect)
            }
        val PACKET_CODEC: PacketCodec<RegistryByteBuf, GunpowderExplosionEmitterParticleEffect> =
            PacketCodec.tuple(
                PacketCodecs.FLOAT, { it.radius },
                PacketCodecs.INT, { it.color.rgb },
                ::GunpowderExplosionEmitterParticleEffect
            )
    }
}

