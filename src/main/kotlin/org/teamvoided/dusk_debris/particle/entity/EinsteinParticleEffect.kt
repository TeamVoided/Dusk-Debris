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

class EinsteinParticleEffect(
    val entity: Int? = null
) : ParticleEffect {

    constructor(entity: Entity?) : this(entity?.id)  //UUID.fromString(entity)

    override fun getType(): ParticleType<EinsteinParticleEffect> = DuskParticles.EINSTEIN_ORBIT

    companion object {
        val CODEC: MapCodec<EinsteinParticleEffect> =
            RecordCodecBuilder.mapCodec { instance ->
                instance.group(
                    Codec.INT.fieldOf("entity").forGetter { it.entity }
                ).apply(instance, ::EinsteinParticleEffect)
            }
        val PACKET_CODEC: PacketCodec<RegistryByteBuf, EinsteinParticleEffect> =
            PacketCodec.tuple(
                PacketCodecs.INT, { it.entity },
                ::EinsteinParticleEffect
            )
    }
}

