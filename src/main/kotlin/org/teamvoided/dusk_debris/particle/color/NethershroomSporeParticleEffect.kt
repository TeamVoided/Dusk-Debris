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

class NethershroomSporeParticleEffect(
    val color: Color
) : ParticleOptions {
    constructor(
        color: Int
    ) : this(Color(color))

    override fun getType(): ParticleType<NethershroomSporeParticleEffect> =
        DuskParticles.TOXIC_SMOKE_PARTICLE

    companion object {
        val CODEC: MapCodec<NethershroomSporeParticleEffect> =
            RecordCodecBuilder.mapCodec { instance ->
                instance.group(
                    Codec.INT.fieldOf("color").forGetter { it.color.rgb }
                ).apply(instance, ::NethershroomSporeParticleEffect)
            }
        val PACKET_CODEC: StreamCodec<RegistryFriendlyByteBuf, NethershroomSporeParticleEffect> =
            StreamCodec.composite(
                ByteBufCodecs.INT, { it.color.rgb },
                ::NethershroomSporeParticleEffect
            )
        val REGISTER = FabricParticleTypes.complex(CODEC, PACKET_CODEC)
    }
}

