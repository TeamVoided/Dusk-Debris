package org.teamvoided.dusk_debris.particle

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.core.Direction
import net.minecraft.core.particles.ParticleOptions
import net.minecraft.core.particles.ParticleType
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import org.teamvoided.dusk_debris.init.DuskParticles

class GoopLandedParticleEffect(
    private val maxAge: Int,
    private val stoppedDirection: Direction
) : ParticleOptions {
    override fun getType(): ParticleType<GoopLandedParticleEffect> {
        return DuskParticles.ASTRAS_LANDED_GOOP
    }

    fun maxAge(): Int {
        return this.maxAge
    }

    fun direction(): Direction {
        return this.stoppedDirection
    }

    companion object {
        val CODEC: MapCodec<GoopLandedParticleEffect> =
            RecordCodecBuilder.mapCodec { instance: RecordCodecBuilder.Instance<GoopLandedParticleEffect> ->
                instance.group(
                    Codec.INT.fieldOf("max_age")
                        .forGetter { obj: GoopLandedParticleEffect -> obj.maxAge() },
                    Direction.CODEC.fieldOf("direction")
                        .forGetter { obj: GoopLandedParticleEffect -> obj.direction() },
                ).apply(instance, ::GoopLandedParticleEffect)
            }
        val PACKET_CODEC: StreamCodec<RegistryFriendlyByteBuf, GoopLandedParticleEffect> = StreamCodec.composite(
            ByteBufCodecs.VAR_INT,
            { obj: GoopLandedParticleEffect -> obj.maxAge() },
            Direction.STREAM_CODEC,
            { obj: GoopLandedParticleEffect -> obj.direction() },
            ::GoopLandedParticleEffect
        )
    }
}