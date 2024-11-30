package org.teamvoided.dusk_debris.particle

import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.particle.*
import net.minecraft.client.world.ClientWorld
import net.minecraft.entity.Entity
import net.minecraft.util.math.Vec3d
import org.teamvoided.dusk_debris.util.Utils.rotate360
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.sin

class EntityTestParticle(
    world: ClientWorld,
    posX: Double,
    posY: Double,
    posZ: Double,
    velX: Double,
    velY: Double,
    velZ: Double,
    val entity: Entity?
) : SpriteBillboardParticle(world, posX, posY, posZ, velX, velY, velZ) {
    var rotationOffset = 0f

    init {
        this.maxAge = 50 + random.nextInt(50)
        this.scale = random.nextFloat() * 0.1f + 0.01f
        this.prevAngle = angle
        this.velocityMultiplier = 0.9f
        this.velocityX = velX
        this.velocityY = velY
        this.velocityZ = velZ
        rotationOffset = random.nextFloat()*rotate360
    }

    override fun getType(): ParticleTextureSheet {
        return ParticleTextureSheet.PARTICLE_SHEET_LIT
    }

    public override fun getBrightness(tint: Float): Int {
        return 15728880
    }

    override fun tick() {
        if (age++ >= this.maxAge) {
            this.markDead()
        } else {
            this.prevPosX = this.x
            this.prevPosY = this.y
            this.prevPosZ = this.z
            velocityX *= velocityMultiplier
            velocityY *= velocityMultiplier
            velocityZ *= velocityMultiplier
            if (this.age % 3 == 0 && entity != null) {
                val orbitOffsetXZ = (entity.width) + 1
                val orbitOffsetY = (entity.height) * (age.toDouble() / maxAge)
                val velocity = Vec3d(
                    orbitOffsetXZ * sin((age.toDouble() / 10)),
                    orbitOffsetY,
                    orbitOffsetXZ * cos((age.toDouble() / 10))
                ).add(entity.pos).subtract(Vec3d(this.x, this.y, this.z)).multiply(0.1)
                velocityX += velocity.x
                velocityY += velocity.y
                velocityZ += velocity.z
            }
            this.x += this.velocityX
            this.y += this.velocityY
            this.z += this.velocityZ
        }
    }

    @Environment(EnvType.CLIENT)
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
            val particle = EntityTestParticle(world, posX, posY, posZ, velX, velY, velZ, target)
            particle.setSprite(spriteProvider)
            return particle
        }
    }
}