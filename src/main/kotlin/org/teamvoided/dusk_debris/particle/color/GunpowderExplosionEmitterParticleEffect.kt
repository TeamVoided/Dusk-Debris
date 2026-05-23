package org.teamvoided.dusk_debris.particle.color

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes
import net.minecraft.core.particles.ParticleOptions
import net.minecraft.core.particles.ParticleType
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import org.teamvoided.dusk_debris.init.DuskParticles
import java.awt.Color

class GunpowderExplosionEmitterParticleEffect(
    val radius: Float,
    val color: Color
) : ParticleOptions {
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
        val PACKET_CODEC: StreamCodec<RegistryFriendlyByteBuf, GunpowderExplosionEmitterParticleEffect> =
            StreamCodec.composite(
                ByteBufCodecs.FLOAT, { it.radius },
                ByteBufCodecs.INT, { it.color.rgb },
                ::GunpowderExplosionEmitterParticleEffect
            )
        val REGISTER = FabricParticleTypes.complex(CODEC, PACKET_CODEC)
    }
}

