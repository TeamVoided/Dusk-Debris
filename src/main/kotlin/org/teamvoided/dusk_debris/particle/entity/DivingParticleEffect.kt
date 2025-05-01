package org.teamvoided.dusk_debris.particle.entity

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.entity.Entity
import net.minecraft.network.RegistryByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.codec.PacketCodecs
import net.minecraft.particle.ParticleEffect
import net.minecraft.particle.ParticleType
import org.teamvoided.dusk_debris.init.DuskParticles

class DivingParticleEffect(val entity: Int) : ParticleEffect {
    constructor(entity: Entity) : this(entity.id)
    override fun getType(): ParticleType<DivingParticleEffect> = DuskParticles.SPELL_DIVE

    companion object {
        val CODEC: MapCodec<DivingParticleEffect> =
            RecordCodecBuilder.mapCodec { instance ->
                instance.group(
                    Codec.INT.fieldOf("entity").forGetter { it.entity }
                ).apply(instance, ::DivingParticleEffect)
            }
        val PACKET_CODEC: PacketCodec<RegistryByteBuf, DivingParticleEffect> =
            PacketCodec.tuple(
                PacketCodecs.INT, { it.entity },
                ::DivingParticleEffect
            )
    }
}

