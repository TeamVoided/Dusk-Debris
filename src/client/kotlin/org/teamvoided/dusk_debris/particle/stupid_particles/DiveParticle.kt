package org.teamvoided.dusk_debris.particle.stupid_particles

import com.mojang.blaze3d.vertex.VertexConsumer
import net.minecraft.client.particle.*
import net.minecraft.client.world.ClientWorld
import net.minecraft.entity.Entity
import org.joml.Quaternionf
import org.teamvoided.dusk_debris.particle.entity.EntityTestParticleEffect
import org.teamvoided.dusk_debris.particle.stupid_particles.abstracts.SpriteBillboardKotlinParticle

class DiveParticle(world: ClientWorld, x: Double, y: Double, z: Double, val entity: Entity?) :
    SpriteBillboardKotlinParticle(world, x, y, z, 0.0, 0.0, 0.0) {
    init {
        maxAge = 4
    }

    override fun tick() {
        if (entity == null) {
            markDead()
            return
        }
        prevPosX = entity.prevX
        prevPosY = entity.prevY
        prevPosZ = entity.prevZ
        if (age++ >= maxAge) {
            markDead()
        } else {
            x = entity.x //* 0.05
            y = entity.y //* 0.05
            z = entity.z //* 0.05
        }
    }

    override fun drawPlane(
        vertexConsumer: VertexConsumer,
        quaternionf: Quaternionf,
        x: Float,
        y: Float,
        z: Float,
        tickDelta: Float
    ) {
        val size = this.getSize(tickDelta)
        val minU = this.minU
        val maxU = this.maxU
        val minV = this.minV
        val maxV = this.maxV
        val brightness = this.getBrightness(tickDelta)
        this.corner(vertexConsumer, quaternionf, x, y, z, 1f, -1f, size, maxU, maxV, brightness)
        this.corner(vertexConsumer, quaternionf, x, y, z, 1f, 1f, size, maxU, minV, brightness)
        this.corner(vertexConsumer, quaternionf, x, y, z, -1f, 1f, size, minU, minV, brightness)
        this.corner(vertexConsumer, quaternionf, x, y, z, -1f, -1f, size, minU, maxV, brightness)
    }

    override val facingCameraMode = NONE

    override fun getType(): ParticleTextureSheet = ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT

    public override fun getBrightness(tickDelta: Float): Int = 240

    class Factory(private val spriteProvider: SpriteProvider) : ParticleFactory<EntityTestParticleEffect> {
        override fun createParticle(
            type: EntityTestParticleEffect,
            world: ClientWorld,
            posX: Double,
            posY: Double,
            posZ: Double,
            velX: Double,
            velY: Double,
            velZ: Double,
        ): Particle {
            val entity = type.entity
            val target = if (entity != null) {
                world.getEntityById(entity)
            } else null
            val particle = DiveParticle(world, posX, posY, posZ, target)
            particle.setSprite(spriteProvider)
            return particle
        }
    }
}
