package org.teamvoided.dusk_debris.particle.cube_particles.abstracts

import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.client.particle.SpriteSet
import net.minecraft.client.renderer.texture.TextureAtlasSprite

abstract class SpriteBillboardKotlinParticle : BillboardKotlinParticle {
    private var sprite: TextureAtlasSprite? = null

    protected constructor(world: ClientLevel, x: Double, y: Double, z: Double) : super(world, x, y, z)

    protected constructor(
        world: ClientLevel,
        x: Double,
        y: Double,
        z: Double,
        xVel: Double,
        yVel: Double,
        zVel: Double
    ) : super(world, x, y, z, xVel, yVel, zVel)

    init {}

    protected fun setSprite(sprite: TextureAtlasSprite) {
        this.sprite = sprite
    }

    override fun minU(): Float = sprite?.u0 ?: error("null sprite provided")

    override fun maxU(): Float = sprite?.u1 ?: error("null sprite provided")

    override fun minV(): Float = sprite?.v0 ?: error("null sprite provided")

    override fun maxV(): Float = sprite?.v1 ?: error("null sprite provided")

    open fun setSprite(spriteProvider: SpriteSet) {
        this.setSprite(spriteProvider.get(this.random))
    }

    open fun setSpriteForAge(spriteProvider: SpriteSet) {
        if (!this.removed) {
            this.setSprite(spriteProvider.get(this.age, this.lifetime))
        }
    }
}
