package org.teamvoided.dusk_debris.particle

import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.client.particle.Particle
import net.minecraft.client.particle.ParticleProvider
import net.minecraft.client.particle.ParticleRenderType
import net.minecraft.client.particle.SpriteSet
import net.minecraft.core.particles.SimpleParticleType
import org.teamvoided.dusk_debris.util.Utils
import kotlin.math.cos
import kotlin.math.sin

class BubbleCubeParticle(
    world: ClientLevel,
    x: Double,
    y: Double,
    z: Double,
    velocityX: Double,
    velocityY: Double,
    velocityZ: Double
) : CubeOldParticle(world, x, y, z, velocityX, velocityY, velocityZ) {
    val rotOffset: Float
    val radius: Float
    val speed: Float

    init {
        this.x = x
        this.y = y
        this.z = z
        this.xd = velocityX
        this.yd = velocityY
        this.zd = velocityZ
        this.lifetime = 500
        this.quadSize = 0.4f + 0.125f * random.nextFloat()
        this.friction = 0.005f
        this.gravity = -(0.005f * random.nextFloat() + 0.001f)
        this.alpha = 0f

        this.rotOffset = random.nextFloat() * Utils.rotate360
        this.radius = random.nextFloat() * 0.1f + 0.01f
        this.speed = random.nextFloat() * 0.1f + 0.01f
    }

    override fun tick() {
        this.xo = this.x
        this.yo = this.y
        this.zo = this.z
        if (age + 20 >= lifetime) {
            this.alpha -= 0.05f
        } else if (this.alpha < 1) {
            this.alpha += 0.05f
        }
        if (age++ < this.lifetime) {
            val the = age * 0.05f * Utils.rotate360 * speed + rotOffset // * 3.18318
            this.xd += cos(the) * 0.05f * radius
            this.zd += sin(the) * 0.05f * radius
            this.yd -= gravity
            this.move(this.xd, this.yd, this.zd)
            this.xd = 0.0
            this.yd = 0.0
            this.zd = 0.0

            if (this.x == this.xo || this.y == this.yo || this.z == this.zo) {
                this.remove()
            }
        } else {
            this.remove()
        }
    }

    override fun getRenderType(): ParticleRenderType = ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT


    @Environment(EnvType.CLIENT)
    open class Factory(private val spriteProvider: SpriteSet) : ParticleProvider<SimpleParticleType> {
        override fun createParticle(
            type: SimpleParticleType,
            world: ClientLevel,
            posX: Double, posY: Double, posZ: Double,
            velX: Double, velY: Double, velZ: Double,
        ): Particle {
            val particle = BubbleCubeParticle(world, posX, posY, posZ, velX, velY, velZ)
            particle.pickSprite(spriteProvider)
            return particle
        }
    }

    @Environment(EnvType.CLIENT)
    class SmallFactory(spriteProvider: SpriteSet) : Factory(spriteProvider) {
        override fun createParticle(
            type: SimpleParticleType,
            world: ClientLevel,
            posX: Double, posY: Double, posZ: Double,
            velX: Double, velY: Double, velZ: Double,
        ): Particle {
            val particle = super.createParticle(type, world, posX, posY, posZ, velX, velY, velZ)
            particle.scale(0.5f)
            return particle
        }
    }
}