package org.teamvoided.dusk_debris.particle.stupid_particles

import com.mojang.blaze3d.vertex.VertexConsumer
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.particle.Particle
import net.minecraft.client.particle.ParticleFactory
import net.minecraft.client.particle.SpriteProvider
import net.minecraft.client.render.Camera
import net.minecraft.client.world.ClientWorld
import net.minecraft.entity.Entity
import net.minecraft.util.math.Vec3d
import org.teamvoided.dusk_debris.particle.entity.EinsteinParticleEffect
import org.teamvoided.dusk_debris.particle.stupid_particles.abstracts.AbstractCubeParticle
import org.teamvoided.dusk_debris.util.Utils
import org.teamvoided.dusk_debris.util.sendMessageIngame
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.tan

class EinsteinOrbitParticle(
    world: ClientWorld,
    x: Double,
    y: Double,
    z: Double,
    val entity: Entity?
) : AbstractCubeParticle(world, x, y, z, 0.0, 0.0, 0.0) {
    private val radius: Float
    private val rate: Double

    init {
        this.scale = random.nextFloat() * 0.7f + 0.3f
        this.maxAge = random.nextInt(80) + 60
        this.rotation = Vec3d(
            random.nextDouble() * Utils.rotate360,
            random.nextDouble() * Utils.rotate360,
            random.nextDouble() * Utils.rotate360
        )
        this.prevRot = rotation
        this.rotSpeed = Vec3d(0.0, random.nextDouble(), 0.0)
        this.radius = random.nextFloat() * 10
        this.rate = random.nextDouble() * 15 + 5.0

        if (entity == null) {
            this.markDead()
        }
    }

    override fun tick() {
        prevPosX = x
        prevPosY = y
        prevPosZ = z
        prevRot = rotation
        if (entity == null || this.age++ >= this.maxAge) {
            this.markDead()
        } else {
            val orbitOffsetXZ = (entity.width) + 1 + radius
            val orbitOffsetY = (entity.height / 2.0)
            val velocity = Vec3d(
                orbitOffsetXZ * sin(age / rate),
                orbitOffsetY,
                orbitOffsetXZ * cos(age / rate)
            ).add(entity.pos)
            x = velocity.x
            y = velocity.y
            z = velocity.z
            rotation = rotation.add(rotSpeed)
        }
    }

    override fun buildGeometry(vertexConsumer: VertexConsumer, camera: Camera, tickDelta: Float) {
        if (age > 1)
            super.buildGeometry(vertexConsumer, camera, tickDelta)
    }

    @Environment(EnvType.CLIENT)
    class Factory(private val spriteProvider: SpriteProvider) : ParticleFactory<EinsteinParticleEffect> {
        override fun createParticle(
            type: EinsteinParticleEffect,
            world: ClientWorld,
            posX: Double,
            posY: Double,
            posZ: Double,
            velX: Double,
            velY: Double,
            velZ: Double,
        ): Particle {
            val entity = type.entity
            val target = if (entity != null) world.getEntityById(entity) else null
            val particle = EinsteinOrbitParticle(world, posX, posY, posZ, target)
            particle.setSprite(spriteProvider)
            return particle
        }
    }
}