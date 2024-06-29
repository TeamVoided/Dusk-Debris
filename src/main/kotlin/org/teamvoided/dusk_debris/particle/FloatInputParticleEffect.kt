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

class FloatInputParticleEffect(val radius: Float) : ParticleEffect {
    override fun getType(): ParticleType<FloatInputParticleEffect> = DuskParticles.GUNPOWDER_EXPLOSION_EMMITER

    companion object {
        val CODEC: MapCodec<FloatInputParticleEffect> =
            RecordCodecBuilder.mapCodec { instance ->
                instance.group(Codec.FLOAT.fieldOf("radius").forGetter { it.radius })
                    .apply(instance, ::FloatInputParticleEffect)
            }
        val PACKET_CODEC: PacketCodec<RegistryByteBuf, FloatInputParticleEffect> =
            PacketCodec.tuple(PacketCodecs.FLOAT, { it.radius }, ::FloatInputParticleEffect)
    }
}

