package org.teamvoided.dusk_debris.particle

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.codec.PacketCodecs
import net.minecraft.particle.ParticleEffect
import net.minecraft.particle.ParticleType
import org.teamvoided.dusks_and_dungeons.init.DnDParticles
import java.awt.Color

class SpiralParticleEffect(val color1: Color, val color2: Color) : ParticleEffect {
    constructor(color1: Int, color2: Int) : this(Color(color1), Color(color2))

    override fun getType(): ParticleType<SpiralParticleEffect> = DnDParticles.SPIRAL

    companion object {
        val CODEC: MapCodec<SpiralParticleEffect> = RecordCodecBuilder.mapCodec { inst ->
            inst.group(
                Codec.INT.fieldOf("color1").forGetter { it.color1.rgb },
                Codec.INT.fieldOf("color2").forGetter { it.color2.rgb }
            ).apply(inst, ::SpiralParticleEffect)
        }
        val PACKET_CODEC = PacketCodec.tuple(
            PacketCodecs.INT, { it.color1.rgb }, PacketCodecs.INT, { it.color2.rgb }, ::SpiralParticleEffect
        )
    }
}

