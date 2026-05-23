package org.teamvoided.dusk_debris.particle.color

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.core.particles.ParticleOptions
import net.minecraft.core.particles.ParticleType
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import org.teamvoided.dusk_debris.init.DuskParticles
import java.awt.Color

class DustBunnyParticleEffect(val color1: Color, val color2: Color) : ParticleOptions {
    constructor(color1: Int, color2: Int) : this(Color(color1), Color(color2))

    override fun getType(): ParticleType<DustBunnyParticleEffect> = DuskParticles.DUST_BUNNY

    companion object {
        val CODEC: MapCodec<DustBunnyParticleEffect> = RecordCodecBuilder.mapCodec { inst ->
            inst.group(
                Codec.INT.fieldOf("color1").forGetter { it.color1.rgb },
                Codec.INT.fieldOf("color2").forGetter { it.color2.rgb }
            ).apply(inst, ::DustBunnyParticleEffect)
        }
        val PACKET_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, { it.color1.rgb },
            ByteBufCodecs.INT, { it.color2.rgb },
            ::DustBunnyParticleEffect
        )
    }
}
