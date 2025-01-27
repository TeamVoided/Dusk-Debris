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

class FlashParticleEffect(
    val color: Color,
    val maxAge: Int = 4
) : ParticleEffect {
    constructor(
        color: Int,
        maxAge: Int = 4
    ) : this(Color(color), maxAge)

    override fun getType(): ParticleType<FlashParticleEffect> =
        DuskParticles.FLASH

    companion object {
        val CODEC: MapCodec<FlashParticleEffect> =
            RecordCodecBuilder.mapCodec { instance ->
                instance.group(
                    Codec.INT.fieldOf("color").forGetter { it.color.rgb },
                    Codec.INT.fieldOf("max_age").forGetter { it.maxAge }
                ).apply(instance, ::FlashParticleEffect)
            }
        val PACKET_CODEC: PacketCodec<RegistryByteBuf, FlashParticleEffect> =
            PacketCodec.tuple(
                PacketCodecs.INT, { it.color.rgb },
                PacketCodecs.INT, { it.maxAge },
                ::FlashParticleEffect
            )
    }
}

