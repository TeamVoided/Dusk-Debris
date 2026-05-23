//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//
package org.teamvoided.dusk_debris.particle

import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.client.particle.*
import net.minecraft.core.BlockPos
import net.minecraft.core.particles.SimpleParticleType
import net.minecraft.util.Mth
import org.teamvoided.dusk_debris.data.tags.DuskFluidTags
import org.teamvoided.dusk_debris.init.DuskParticles

@Environment(EnvType.CLIENT)
class SpinningAcidBubbleParticle internal constructor(world: ClientLevel?, d: Double, e: Double, f: Double) :
    TextureSheetParticle(world, d, e, f) {
    private var accelerationAngle = 0f

    init {
        this.lifetime = random.nextInt(60) + 30
        this.hasPhysics = false
        this.xd = 0.0
        this.yd = 0.05
        this.zd = 0.0
        this.setSize(0.02f, 0.02f)
        this.quadSize *= random.nextFloat() * 0.6f + 0.2f
        this.gravity = 0f
    }

    override fun getRenderType(): ParticleRenderType {
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE
    }

    override fun tick() {
        this.xo = this.x
        this.yo = this.y
        this.zo = this.z
        if (age++ >= this.lifetime) {
            this.remove()
        } else {
            val radius = 0.6f
            this.xd += (radius * Mth.cos(this.accelerationAngle)).toDouble()
            this.zd += (radius * Mth.sin(this.accelerationAngle)).toDouble()
            this.xd *= 0.07
            this.zd *= 0.07
            this.move(this.xd, this.yd, this.zd)
            if (!level.getFluidState(BlockPos.containing(this.x, this.y, this.z))
                    .`is`(DuskFluidTags.ACID_BUBBLE_PARTICLES) || this.onGround
            ) {
                this.remove()
            }

            this.accelerationAngle += 0.08f
        }
    }

    override fun remove() {
        level.addParticle(
            DuskParticles.ACID_BUBBLE_POP,
            x, y, z,
            0.0, 0.0, 0.0
        )
        super.remove()
    }

    @Environment(EnvType.CLIENT)
    class Factory(private val spriteProvider: SpriteSet) : ParticleProvider<SimpleParticleType> {
        override fun createParticle(
            defaultParticleType: SimpleParticleType,
            world: ClientLevel,
            d: Double,
            e: Double,
            f: Double,
            g: Double,
            h: Double,
            i: Double
        ): Particle {
            val currentDownParticle = SpinningAcidBubbleParticle(world, d, e, f)
            currentDownParticle.pickSprite(this.spriteProvider)
            return currentDownParticle
        }
    }
}
