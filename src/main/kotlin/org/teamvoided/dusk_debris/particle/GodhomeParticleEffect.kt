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

class GodhomeParticleEffect(
    val color: Color = Color(0xFFFFFF)
) : ParticleEffect {
    constructor(
        color: Int
    ) : this(Color(color))

    override fun getType(): ParticleType<GodhomeParticleEffect> =
        DuskParticles.GODHOME

    companion object {
        val CODEC: MapCodec<GodhomeParticleEffect> =
            RecordCodecBuilder.mapCodec { instance ->
                instance.group(
                    Codec.INT.fieldOf("color").forGetter { it.color.rgb }
                ).apply(instance, ::GodhomeParticleEffect)
            }
        val PACKET_CODEC: PacketCodec<RegistryByteBuf, GodhomeParticleEffect> =
            PacketCodec.tuple(
                PacketCodecs.INT, { it.color.rgb },
                ::GodhomeParticleEffect
            )
    }
}

