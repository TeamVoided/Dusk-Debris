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

class GodhomeParticleEffect(
    val color: Color = Color(0xFFFFFF)
) : ParticleOptions {
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
        val PACKET_CODEC: StreamCodec<RegistryFriendlyByteBuf, GodhomeParticleEffect> =
            StreamCodec.composite(
                ByteBufCodecs.INT, { it.color.rgb },
                ::GodhomeParticleEffect
            )
    }
}

