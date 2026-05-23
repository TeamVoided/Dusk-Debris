package org.teamvoided.dusk_debris.particle

import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.client.particle.*
import net.minecraft.core.particles.ParticleGroup
import net.minecraft.core.particles.SimpleParticleType
import java.util.*

class SpiderlilyPetalParticle(
    world: ClientLevel, x: Double, y: Double, z: Double, velX: Double, velY: Double, velZ: Double
) : TextureSheetParticle(world, x, y, z, velX, velY, velZ) {

    constructor(
        world: ClientLevel, x: Double, y: Double, z: Double
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
        this.friction = 1.0f
        this.xd = velX
        this.yd = velY
        this.zd = velZ
        this.quadSize = 0.1f * (this.random.nextFloat() * this.random.nextFloat() * 1.0f + 1.0f)
        this.rotationSpeed = (Math.random().toFloat() - 0.5f) * 0.01f
        this.lifetime = (this.random.nextFloat() * 900).toInt() + 600
    }

    override fun getRenderType(): ParticleRenderType = ParticleRenderType.PARTICLE_SHEET_OPAQUE
    override fun getParticleGroup(): Optional<ParticleGroup> {
        return Optional.of(SNOWFLAKE_PARTICLE_GROUP)
    }

    override fun tick() {
        this.xo = this.x
        this.yo = this.y
        this.zo = this.z
        if (this.age++ >= this.lifetime) {
            this.remove()
        } else {
            if (this.onGround) {
                this.oRoll = this.roll
                age += 4
            } else {
                this.oRoll = this.roll
                this.roll += Math.PI.toFloat() * rotationSpeed * 2.0f
            }
            this.move(this.xd, this.yd, this.zd)
            this.xd =
                if (xd < 0.1) xd + 0.005 else if (xd > 0.15) xd * 0.9 else xd
            this.zd =
                if (zd < 0.1) zd + 0.005 else if (zd > 0.15) zd * 0.9 else zd
            this.yd =
                if (yd < 0.05) yd + 0.01 else if (yd > 0.1) yd * 0.9 else yd
        }
    }

    class Factory(private val spriteProvider: SpriteSet) : ParticleProvider<SimpleParticleType> {
        override fun createParticle(
            defaultParticleType: SimpleParticleType, world: ClientLevel,
            x: Double, y: Double, z: Double,
            velX: Double, velY: Double, velZ: Double
        ): Particle {
            val snowflakeParticle = SpiderlilyPetalParticle(world, x, y, z, velX, velY, velZ)
            snowflakeParticle.pickSprite(spriteProvider)
            return snowflakeParticle
        }
    }

    companion object {
        val SNOWFLAKE_PARTICLE_GROUP: ParticleGroup = ParticleGroup(32768)
    }
}