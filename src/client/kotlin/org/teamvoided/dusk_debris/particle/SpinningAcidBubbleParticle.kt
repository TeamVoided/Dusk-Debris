//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//
package org.teamvoided.dusk_debris.particle

import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.particle.*
import net.minecraft.client.world.ClientWorld
import net.minecraft.particle.DefaultParticleType
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.MathHelper
import org.teamvoided.dusk_debris.data.tags.DuskFluidTags
import org.teamvoided.dusk_debris.init.DuskParticles

@Environment(EnvType.CLIENT)
class SpinningAcidBubbleParticle internal constructor(world: ClientWorld?, d: Double, e: Double, f: Double) :
    SpriteBillboardParticle(world, d, e, f) {
    private var accelerationAngle = 0f

    init {
        this.maxAge = random.nextInt(60) + 30
        this.collidesWithWorld = false
        this.velocityX = 0.0
        this.velocityY = 0.05
        this.velocityZ = 0.0
        this.setBoundingBoxSpacing(0.02f, 0.02f)
        this.scale *= random.nextFloat() * 0.6f + 0.2f
        this.gravityStrength = 0f
    }

    override fun getType(): ParticleTextureSheet {
        return ParticleTextureSheet.PARTICLE_SHEET_OPAQUE
    }

    override fun tick() {
        this.prevPosX = this.x
        this.prevPosY = this.y
        this.prevPosZ = this.z
        if (age++ >= this.maxAge) {
            this.markDead()
        } else {
            val radius = 0.6f
            this.velocityX += (radius * MathHelper.cos(this.accelerationAngle)).toDouble()
            this.velocityZ += (radius * MathHelper.sin(this.accelerationAngle)).toDouble()
            this.velocityX *= 0.07
            this.velocityZ *= 0.07
            this.move(this.velocityX, this.velocityY, this.velocityZ)
            if (!world.getFluidState(BlockPos.create(this.x, this.y, this.z))
                    .isIn(DuskFluidTags.ACID_BUBBLE_PARTICLES) || this.onGround
            ) {
                this.markDead()
            }

            this.accelerationAngle += 0.08f
        }
    }

    override fun markDead() {
        world.addParticle(
            DuskParticles.ACID_BUBBLE_POP,
            x, y, z,
            0.0, 0.0, 0.0
        )
        super.markDead()
    }

    @Environment(EnvType.CLIENT)
    class Factory(private val spriteProvider: SpriteProvider) : ParticleFactory<DefaultParticleType> {
        override fun createParticle(
            defaultParticleType: DefaultParticleType,
            world: ClientWorld,
            d: Double,
            e: Double,
            f: Double,
            g: Double,
            h: Double,
            i: Double
        ): Particle {
            val currentDownParticle = SpinningAcidBubbleParticle(world, d, e, f)
            currentDownParticle.setSprite(this.spriteProvider)
            return currentDownParticle
        }
    }
}
