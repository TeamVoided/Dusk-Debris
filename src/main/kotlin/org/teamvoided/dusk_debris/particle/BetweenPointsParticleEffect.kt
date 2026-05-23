package org.teamvoided.dusk_debris.particle

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.core.particles.ParticleOptions
import net.minecraft.core.particles.ParticleType
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.phys.Vec3
import org.teamvoided.dusk_debris.init.DuskParticles

class BetweenPointsParticleEffect(
    val targetPosX: Double,
    val targetPosY: Double,
    val targetPosZ: Double,
    val ominous: Boolean,
    val particleDistance: Int,
    val rate: Int
) : ParticleOptions {

    constructor(targetPos: Vec3, ominous: Boolean, particleDistance: Int, rate: Int) :
            this(targetPos.x,targetPos.y,targetPos.z,ominous,particleDistance,rate)

    override fun getType(): ParticleType<BetweenPointsParticleEffect> = DuskParticles.BETWEEN_POINTS

    fun getTargetPos(): Vec3 = Vec3(targetPosX, targetPosY, targetPosZ)


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
        val PACKET_CODEC: StreamCodec<RegistryFriendlyByteBuf, BetweenPointsParticleEffect> =
            StreamCodec.composite(
                ByteBufCodecs.DOUBLE, { it.targetPosX },
                ByteBufCodecs.DOUBLE, { it.targetPosY },
                ByteBufCodecs.DOUBLE, { it.targetPosZ },
                ByteBufCodecs.BOOL, { it.ominous },
                ByteBufCodecs.INT, { it.particleDistance },
                ByteBufCodecs.INT, { it.rate },
                ::BetweenPointsParticleEffect
            )
    }
}

