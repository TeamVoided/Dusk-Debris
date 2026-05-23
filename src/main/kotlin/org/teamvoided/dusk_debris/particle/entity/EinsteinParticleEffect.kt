package org.teamvoided.dusk_debris.particle.entity

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.core.particles.ParticleOptions
import net.minecraft.core.particles.ParticleType
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.entity.Entity
import org.teamvoided.dusk_debris.init.DuskParticles

class EinsteinParticleEffect(
    val entity: Int? = null
) : ParticleOptions {

    constructor(entity: Entity?) : this(entity?.id)  //UUID.fromString(entity)

    override fun getType(): ParticleType<EinsteinParticleEffect> = DuskParticles.EINSTEIN_ORBIT

    companion object {
        val CODEC: MapCodec<EinsteinParticleEffect> =
            RecordCodecBuilder.mapCodec { instance ->
                instance.group(
                    Codec.INT.fieldOf("entity").forGetter { it.entity }
                ).apply(instance, ::EinsteinParticleEffect)
            }
        val PACKET_CODEC: StreamCodec<RegistryFriendlyByteBuf, EinsteinParticleEffect> =
            StreamCodec.composite(
                ByteBufCodecs.INT, { it.entity },
                ::EinsteinParticleEffect
            )
    }
}

