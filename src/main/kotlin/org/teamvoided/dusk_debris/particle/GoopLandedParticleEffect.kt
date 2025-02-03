package org.teamvoided.dusk_debris.particle

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.network.RegistryByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.codec.PacketCodecs
import net.minecraft.particle.ParticleEffect
import net.minecraft.particle.ParticleType
import net.minecraft.particle.ParticleTypes
import net.minecraft.util.math.Direction
import org.teamvoided.dusk_debris.init.DuskParticles

class GoopLandedParticleEffect(
    private val maxAge: Int,
    private val stoppedDirection: Direction
) : ParticleEffect {
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
        val PACKET_CODEC: PacketCodec<RegistryByteBuf, GoopLandedParticleEffect> = PacketCodec.tuple(
            PacketCodecs.VAR_INT,
            { obj: GoopLandedParticleEffect -> obj.maxAge() },
            Direction.PACKET_CODEC,
            { obj: GoopLandedParticleEffect -> obj.direction() },
            ::GoopLandedParticleEffect
        )
    }
}