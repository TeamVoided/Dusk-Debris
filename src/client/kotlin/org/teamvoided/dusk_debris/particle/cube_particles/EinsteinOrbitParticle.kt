package org.teamvoided.dusk_debris.particle.cube_particles

import com.mojang.blaze3d.vertex.VertexConsumer
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.Camera
import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.client.particle.Particle
import net.minecraft.client.particle.ParticleProvider
import net.minecraft.client.particle.SpriteSet
import net.minecraft.world.entity.Entity
import net.minecraft.world.phys.Vec3
import org.teamvoided.dusk_debris.particle.cube_particles.abstracts.AbstractCubeParticle
import org.teamvoided.dusk_debris.particle.entity.EinsteinParticleEffect
import org.teamvoided.dusk_debris.util.Utils
import kotlin.math.cos
import kotlin.math.sin

class EinsteinOrbitParticle(
    world: ClientLevel,
    x: Double,
    y: Double,
    z: Double,
    val entity: Entity?
) : AbstractCubeParticle(world, x, y, z, 0.0, 0.0, 0.0) {
    private val radius: Float
    private val rate: Double

    init {
        this.scale = random.nextFloat() * 0.7f + 0.3f
        this.lifetime = random.nextInt(80) + 60
        this.rotation = Vec3(
            random.nextDouble() * Utils.rotate360,
            random.nextDouble() * Utils.rotate360,
            random.nextDouble() * Utils.rotate360
        )
        this.prevRot = rotation
        this.rotSpeed = Vec3(0.0, random.nextDouble(), 0.0)
        this.radius = random.nextFloat() * 10
        this.rate = random.nextDouble() * 15 + 5.0

        if (entity == null) {
            this.remove()
        }
    }

    override fun tick() {
        xo = x
        yo = y
        zo = z
        prevRot = rotation
        if (entity == null || this.age++ >= this.lifetime) {
            this.remove()
        } else {
            val orbitOffsetXZ = (entity.bbWidth) + 1 + radius
            val orbitOffsetY = (entity.bbHeight / 2.0)
            val velocity = Vec3(
                orbitOffsetXZ * sin(age / rate),
                orbitOffsetY,
                orbitOffsetXZ * cos(age / rate)
            ).add(entity.position())
            x = velocity.x
            y = velocity.y
            z = velocity.z
            rotation = rotation.add(rotSpeed)
        }
    }

    override fun render(vertexConsumer: VertexConsumer, camera: Camera, tickDelta: Float) {
        if (age > 1)
            super.render(vertexConsumer, camera, tickDelta)
    }

    @Environment(EnvType.CLIENT)
    class Factory(private val spriteProvider: SpriteSet) : ParticleProvider<EinsteinParticleEffect> {
        override fun createParticle(
            type: EinsteinParticleEffect,
            world: ClientLevel,
            posX: Double,
            posY: Double,
            posZ: Double,
            velX: Double,
            velY: Double,
            velZ: Double,
        ): Particle {
            val entity = type.entity
            val target = if (entity != null) world.getEntity(entity) else null
            val particle = EinsteinOrbitParticle(world, posX, posY, posZ, target)
            particle.setSprite(spriteProvider)
            return particle
        }
    }
}