package org.teamvoided.dusk_debris.particle.color

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes
import net.minecraft.core.particles.ParticleOptions
import net.minecraft.core.particles.ParticleType
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import org.teamvoided.dusk_debris.init.DuskParticles
import java.awt.Color

class GunpowderExplosionSmokeParticleEffect(
    val color: Color
) : ParticleOptions {
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
        val PACKET_CODEC: StreamCodec<RegistryFriendlyByteBuf, GunpowderExplosionSmokeParticleEffect> =
            StreamCodec.composite(
                ByteBufCodecs.INT, { it.color.rgb },
                ::GunpowderExplosionSmokeParticleEffect
            )
        val REGISTER = FabricParticleTypes.complex(CODEC, PACKET_CODEC)
    }
}

