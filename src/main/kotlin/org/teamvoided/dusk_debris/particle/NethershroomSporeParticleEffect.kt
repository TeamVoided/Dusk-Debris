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

class NethershroomSporeParticleEffect(
    val color: Color
) : ParticleEffect {
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
        val PACKET_CODEC: PacketCodec<RegistryByteBuf, NethershroomSporeParticleEffect> =
            PacketCodec.tuple(
                PacketCodecs.INT, { it.color.rgb },
                ::NethershroomSporeParticleEffect
            )
    }
}

