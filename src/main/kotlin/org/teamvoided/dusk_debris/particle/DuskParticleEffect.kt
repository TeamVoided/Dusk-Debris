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

class DuskParticleEffect(val radius: Float) : ParticleEffect {
    override fun getType(): ParticleType<DuskParticleEffect> = DuskParticles.GUNPOWDER_EXPLOSION_EMMITER

    companion object {
        val CODEC: MapCodec<DuskParticleEffect> =
            RecordCodecBuilder.mapCodec { instance ->
                instance.group(Codec.FLOAT.fieldOf("range").forGetter { it.radius })
                    .apply(instance, ::DuskParticleEffect)
            }
        val PACKET_CODEC: PacketCodec<RegistryByteBuf, DuskParticleEffect> =
            PacketCodec.tuple(PacketCodecs.FLOAT, { it.radius }, ::DuskParticleEffect)
    }
}

