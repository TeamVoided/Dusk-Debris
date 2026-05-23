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

class DivingParticleEffect(val entity: Int) : ParticleOptions {
    constructor(entity: Entity) : this(entity.id)
    override fun getType(): ParticleType<DivingParticleEffect> = DuskParticles.SPELL_DIVE

    companion object {
        val CODEC: MapCodec<DivingParticleEffect> =
            RecordCodecBuilder.mapCodec { instance ->
                instance.group(
                    Codec.INT.fieldOf("entity").forGetter { it.entity }
                ).apply(instance, ::DivingParticleEffect)
            }
        val PACKET_CODEC: StreamCodec<RegistryFriendlyByteBuf, DivingParticleEffect> =
            StreamCodec.composite(
                ByteBufCodecs.INT, { it.entity },
                ::DivingParticleEffect
            )
    }
}

