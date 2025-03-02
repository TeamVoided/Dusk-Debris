package org.teamvoided.dusk_debris.particle

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.network.RegistryByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.codec.PacketCodecs
import net.minecraft.particle.ParticleEffect
import net.minecraft.particle.ParticleType
import net.minecraft.util.math.Direction
import org.teamvoided.dusk_debris.init.DuskParticles

class ShriekDirectionalParticleEffect(val direction: Direction = Direction.UP, val delay: Int = 0) : ParticleEffect {

    override fun getType(): ParticleType<ShriekDirectionalParticleEffect> = DuskParticles.SHRIEK_DIRECTIONAL

    companion object {
        val CODEC: MapCodec<ShriekDirectionalParticleEffect> =
            RecordCodecBuilder.mapCodec { instance ->
                instance.group(
                    Direction.CODEC.fieldOf("direction").orElse(Direction.UP).forGetter { it.direction },
                    Codec.INT.fieldOf("delay").orElse(0).forGetter { it.delay }
                ).apply(instance, ::ShriekDirectionalParticleEffect)
            }
        val PACKET_CODEC: PacketCodec<RegistryByteBuf, ShriekDirectionalParticleEffect> = PacketCodec.tuple(
            Direction.PACKET_CODEC,
            { it.direction },
            PacketCodecs.VAR_INT,
            { it.delay },
            ::ShriekDirectionalParticleEffect
        )
    }
}