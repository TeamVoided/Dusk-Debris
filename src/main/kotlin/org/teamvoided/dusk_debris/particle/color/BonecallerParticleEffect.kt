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

class BonecallerParticleEffect(val color1: Color, val color2: Color) : ParticleOptions {
    constructor(color1: Int, color2: Int) : this(Color(color1), Color(color2))

    override fun getType(): ParticleType<BonecallerParticleEffect> = DuskParticles.BONECALLER

    companion object {
        val CODEC: MapCodec<BonecallerParticleEffect> = RecordCodecBuilder.mapCodec { instance ->
            instance.group(
                Codec.INT.fieldOf("color_1").forGetter { it.color1.rgb },
                Codec.INT.fieldOf("color_2").forGetter { it.color2.rgb }
            ).apply(instance, ::BonecallerParticleEffect)
        }
        val PACKET_CODEC: StreamCodec<RegistryFriendlyByteBuf, BonecallerParticleEffect> = StreamCodec.composite(
            ByteBufCodecs.INT, { it.color1.rgb },
            ByteBufCodecs.INT, { it.color2.rgb },
            ::BonecallerParticleEffect
        )
    }
}

