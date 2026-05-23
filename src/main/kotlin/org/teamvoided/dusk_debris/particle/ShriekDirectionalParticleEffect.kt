package org.teamvoided.dusk_debris.particle

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes
import net.minecraft.core.Direction
import net.minecraft.core.particles.ParticleOptions
import net.minecraft.core.particles.ParticleType
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import org.teamvoided.dusk_debris.init.DuskParticles

class ShriekDirectionalParticleEffect(val direction: Direction = Direction.UP, val delay: Int = 0) : ParticleOptions {

    override fun getType(): ParticleType<ShriekDirectionalParticleEffect> = DuskParticles.SHRIEK_DIRECTIONAL

    companion object {
        val CODEC: MapCodec<ShriekDirectionalParticleEffect> =
            RecordCodecBuilder.mapCodec { instance ->
                instance.group(
                    Direction.CODEC.fieldOf("direction").orElse(Direction.UP).forGetter { it.direction },
                    Codec.INT.fieldOf("delay").orElse(0).forGetter { it.delay }
                ).apply(instance, ::ShriekDirectionalParticleEffect)
            }
        val PACKET_CODEC: StreamCodec<RegistryFriendlyByteBuf, ShriekDirectionalParticleEffect> = StreamCodec.composite(
            Direction.STREAM_CODEC,
            { it.direction },
            ByteBufCodecs.VAR_INT,
            { it.delay },
            ::ShriekDirectionalParticleEffect
        )
        val REGISTER = FabricParticleTypes.complex(CODEC, PACKET_CODEC)
    }
}