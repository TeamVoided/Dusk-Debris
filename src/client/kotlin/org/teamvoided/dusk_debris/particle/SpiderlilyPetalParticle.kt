package org.teamvoided.dusk_debris.particle

import net.minecraft.client.particle.*
import net.minecraft.client.world.ClientWorld
import net.minecraft.particle.DefaultParticleType
import java.util.*

class SpiderlilyPetalParticle(
    world: ClientWorld, x: Double, y: Double, z: Double, velX: Double, velY: Double, velZ: Double
) : SpriteBillboardParticle(world, x, y, z, velX, velY, velZ) {

    constructor(
        world: ClientWorld, x: Double, y: Double, z: Double
    ) : this(
        world,
        x,
        y,
        z,
        (Math.random() * 2.0 - 1.0) * 0.05,
        (Math.random() * 2.0 - 1.0) * 0.05,
        (Math.random() * 2.0 - 1.0) * 0.05
    )

    private val rotationSpeed: Float

    init {
        this.velocityMultiplier = 1.0f
        this.velocityX = velX
        this.velocityY = velY
        this.velocityZ = velZ
        this.scale = 0.1f * (this.random.nextFloat() * this.random.nextFloat() * 1.0f + 1.0f)
        this.rotationSpeed = (Math.random().toFloat() - 0.5f) * 0.01f
        this.maxAge = (this.random.nextFloat() * 900).toInt() + 600
    }

    override fun getType(): ParticleTextureSheet = ParticleTextureSheet.PARTICLE_SHEET_OPAQUE
    override fun getGroup(): Optional<ParticleGroup> {
        return Optional.of(SNOWFLAKE_PARTICLE_GROUP)
    }

    override fun tick() {
        this.prevPosX = this.x
        this.prevPosY = this.y
        this.prevPosZ = this.z
        if (this.age++ >= this.maxAge) {
            this.markDead()
        } else {
            if (this.onGround) {
                this.prevAngle = this.angle
                age += 4
            } else {
                this.prevAngle = this.angle
                this.angle += Math.PI.toFloat() * rotationSpeed * 2.0f
            }
            this.move(this.velocityX, this.velocityY, this.velocityZ)
            this.velocityX =
                if (velocityX < 0.1) velocityX + 0.005 else if (velocityX > 0.15) velocityX * 0.9 else velocityX
            this.velocityZ =
                if (velocityZ < 0.1) velocityZ + 0.005 else if (velocityZ > 0.15) velocityZ * 0.9 else velocityZ
            this.velocityY =
                if (velocityY < 0.05) velocityY + 0.01 else if (velocityY > 0.1) velocityY * 0.9 else velocityY
        }
    }

    class Factory(private val spriteProvider: SpriteProvider) : ParticleFactory<DefaultParticleType> {
        override fun createParticle(
            defaultParticleType: DefaultParticleType, world: ClientWorld,
            x: Double, y: Double, z: Double,
            velX: Double, velY: Double, velZ: Double
        ): Particle {
            val snowflakeParticle = SpiderlilyPetalParticle(world, x, y, z, velX, velY, velZ)
            snowflakeParticle.setSprite(spriteProvider)
            return snowflakeParticle
        }
    }

    companion object {
        val SNOWFLAKE_PARTICLE_GROUP: ParticleGroup = ParticleGroup(32768)
    }
}