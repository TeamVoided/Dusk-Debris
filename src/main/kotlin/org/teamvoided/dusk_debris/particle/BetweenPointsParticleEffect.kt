package org.teamvoided.dusk_debris.particle

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.network.RegistryByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.codec.PacketCodecs
import net.minecraft.particle.ParticleEffect
import net.minecraft.particle.ParticleType
import net.minecraft.util.math.Vec3d
import org.teamvoided.dusk_debris.init.DuskParticles

class BetweenPointsParticleEffect(
    val targetPosX: Double,
    val targetPosY: Double,
    val targetPosZ: Double,
    val ominous: Boolean,
    val particleDistance: Int,
    val rate: Int
) : ParticleEffect {

    constructor(targetPos: Vec3d, ominous: Boolean, particleDistance: Int, rate: Int) :
            this(targetPos.x,targetPos.y,targetPos.z,ominous,particleDistance,rate)

    override fun getType(): ParticleType<BetweenPointsParticleEffect> = DuskParticles.BETWEEN_POINTS

    fun getTargetPos(): Vec3d = Vec3d(targetPosX, targetPosY, targetPosZ)


    companion object {
        val CODEC: MapCodec<BetweenPointsParticleEffect> =
            RecordCodecBuilder.mapCodec { instance ->
                instance.group(
                    Codec.DOUBLE.fieldOf("target_x").forGetter { it.targetPosX },
                    Codec.DOUBLE.fieldOf("target_y").forGetter { it.targetPosY },
                    Codec.DOUBLE.fieldOf("target_z").forGetter { it.targetPosZ },
                    Codec.BOOL.fieldOf("particle").forGetter { it.ominous },
                    Codec.INT.fieldOf("particle_distance").forGetter { it.particleDistance },
                    Codec.INT.fieldOf("rate").forGetter { it.rate }
                ).apply(instance, ::BetweenPointsParticleEffect)
            }
        val PACKET_CODEC: PacketCodec<RegistryByteBuf, BetweenPointsParticleEffect> =
            PacketCodec.tuple(
                PacketCodecs.DOUBLE, { it.targetPosX },
                PacketCodecs.DOUBLE, { it.targetPosY },
                PacketCodecs.DOUBLE, { it.targetPosZ },
                PacketCodecs.BOOL, { it.ominous },
                PacketCodecs.INT, { it.particleDistance },
                PacketCodecs.INT, { it.rate },
                ::BetweenPointsParticleEffect
            )
    }
}

