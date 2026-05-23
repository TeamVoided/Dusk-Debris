package org.teamvoided.dusk_debris.particle

import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.client.particle.*
import net.minecraft.core.particles.SimpleParticleType
import org.teamvoided.dusk_debris.util.Utils
import kotlin.math.cos
import kotlin.math.sin

@Deprecated("Just a copy of BubbleCubeParticle without the cube, will wait until Cube is finished before messing with it")
class DuskBubbleParticle(
    world: ClientLevel,
    x: Double,
    y: Double,
    z: Double,
    velocityX: Double,
    velocityY: Double,
    velocityZ: Double
) : TextureSheetParticle(world, x, y, z, velocityX, velocityY, velocityZ) {
    val rotOffset: Float
    val speed: Float

    init {
        this.x = x
        this.y = y
        this.z = z
        this.xd = velocityX
        this.yd = velocityY
        this.zd = velocityZ
        this.lifetime = 300 + random.nextInt(700)
        this.quadSize = 0.1f + 0.125f * random.nextFloat()
        this.gravity = -(0.007f * random.nextFloat() + 0.002f)
        this.alpha = 0f
        this.rotOffset = random.nextFloat() * Utils.rotate360
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
            this.xd += cos(the) * 0.003f
            this.zd += sin(the) * 0.003f
            this.yd -= gravity
            this.move(this.xd, this.yd, this.zd)
            if (this.x == this.xo || this.y == this.yo || this.z == this.zo) {
                this.remove()
            }
            this.xd = 0.0
            this.yd = 0.0
            this.zd = 0.0
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
            val particle = DuskBubbleParticle(world, posX, posY, posZ, velX, velY, velZ)
            particle.pickSprite(spriteProvider)
            return particle
        }
    }
}