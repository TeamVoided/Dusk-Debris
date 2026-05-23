package org.teamvoided.dusk_debris.particle.color

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.core.particles.ParticleOptions
import net.minecraft.core.particles.ParticleType
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import org.teamvoided.dusk_debris.init.DuskParticles
import java.awt.Color

class FlashParticleEffect(
    val color: Color,
    val maxAge: Int = 4
) : ParticleOptions {
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
        val PACKET_CODEC: StreamCodec<RegistryFriendlyByteBuf, FlashParticleEffect> =
            StreamCodec.composite(
                ByteBufCodecs.INT, { it.color.rgb },
                ByteBufCodecs.INT, { it.maxAge },
                ::FlashParticleEffect
            )
    }
}

