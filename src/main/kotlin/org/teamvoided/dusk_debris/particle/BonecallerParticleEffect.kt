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

class BonecallerParticleEffect(
    val color1: Color,
    val color2: Color
) : ParticleEffect {
    constructor(
        color1: Int,
        color2: Int
    ) : this(Color(color1), Color(color2))

    override fun getType(): ParticleType<BonecallerParticleEffect> =
        DuskParticles.BONECALLER

    companion object {
        val CODEC: MapCodec<BonecallerParticleEffect> =
            RecordCodecBuilder.mapCodec { instance ->
                instance.group(
                    Codec.INT.fieldOf("color_1").forGetter { it.color1.rgb },
                            Codec.INT.fieldOf("color_2").forGetter { it.color2.rgb }
                ).apply(instance, ::BonecallerParticleEffect)
            }
        val PACKET_CODEC: PacketCodec<RegistryByteBuf, BonecallerParticleEffect> =
            PacketCodec.tuple(
                PacketCodecs.INT, { it.color1.rgb },
                PacketCodecs.INT, { it.color2.rgb },
                ::BonecallerParticleEffect
            )
    }
}

