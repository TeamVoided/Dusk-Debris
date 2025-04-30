package org.teamvoided.dusk_debris.particle.stupid_particles.abstracts

import net.minecraft.client.particle.SpriteProvider
import net.minecraft.client.texture.Sprite
import net.minecraft.client.world.ClientWorld

abstract class SpriteBillboardKotlinParticle : BillboardKotlinParticle {
    private var sprite: Sprite? = null

    protected constructor(world: ClientWorld, x: Double, y: Double, z: Double) : super(world, x, y, z)

    protected constructor(
        world: ClientWorld,
        x: Double,
        y: Double,
        z: Double,
        xVel: Double,
        yVel: Double,
        zVel: Double
    ) : super(world, x, y, z, xVel, yVel, zVel)

    protected fun setSprite(sprite: Sprite) {
        this.sprite = sprite
    }

    override val minU: Float = sprite!!.minU

    override val maxU: Float = sprite!!.maxU

    override val minV: Float = sprite!!.minV

    override val maxV: Float = sprite!!.maxV

    fun setSprite(spriteProvider: SpriteProvider) {
        this.setSprite(spriteProvider.getRandom(this.random))
    }

    fun setSpriteForAge(spriteProvider: SpriteProvider) {
        if (!this.dead) {
            this.setSprite(spriteProvider.getSprite(this.age, this.maxAge))
        }
    }
}
