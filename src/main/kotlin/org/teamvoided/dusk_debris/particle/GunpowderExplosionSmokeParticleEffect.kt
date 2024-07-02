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

class GunpowderExplosionSmokeParticleEffect(
    val color: Color
) : ParticleEffect {
    constructor(
        color: Int
    ) : this(Color(color))

    override fun getType(): ParticleType<GunpowderExplosionSmokeParticleEffect> =
        DuskParticles.GUNPOWDER_EXPLOSION_SMOKE

    companion object {
        val CODEC: MapCodec<GunpowderExplosionSmokeParticleEffect> =
            RecordCodecBuilder.mapCodec { instance ->
                instance.group(
                    Codec.INT.fieldOf("color").forGetter { it.color.rgb }
                ).apply(instance, ::GunpowderExplosionSmokeParticleEffect)
            }
        val PACKET_CODEC: PacketCodec<RegistryByteBuf, GunpowderExplosionSmokeParticleEffect> =
            PacketCodec.tuple(
                PacketCodecs.INT, { it.color.rgb },
                ::GunpowderExplosionSmokeParticleEffect
            )
    }
}

