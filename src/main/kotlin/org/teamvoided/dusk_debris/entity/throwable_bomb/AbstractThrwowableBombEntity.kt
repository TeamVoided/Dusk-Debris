package org.teamvoided.dusk_debris.entity.throwable_bomb

import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.projectile.thrown.ThrownItemEntity
import net.minecraft.item.Item
import net.minecraft.particle.ItemStackParticleEffect
import net.minecraft.particle.ParticleEffect
import net.minecraft.particle.ParticleTypes
import net.minecraft.util.hit.EntityHitResult
import net.minecraft.util.hit.HitResult
import net.minecraft.world.World
import net.minecraft.world.explosion.ExplosionBehavior
import org.teamvoided.dusk_debris.init.DuskItems

open class AbstractThrwowableBombEntity : ThrownItemEntity {
    constructor(entityType: EntityType<out AbstractThrwowableBombEntity>, world: World) : super(entityType, world)
    constructor(entityType: EntityType<out AbstractThrwowableBombEntity>, owner: LivingEntity?, world: World) :
            super(entityType, owner, world)

    constructor(
        entityType: EntityType<out AbstractThrwowableBombEntity>,
        x: Double,
        y: Double,
        z: Double,
        world: World
    ) :
            super(entityType, x, y, z, world)

    constructor(world: World, owner: LivingEntity?) : super(null, owner, world)

    constructor(world: World, x: Double, y: Double, z: Double) : super(null, x, y, z, world)

    open var explosionBehavior: ExplosionBehavior = ExplosionBehavior()
    open val hitDamage = 0f
    open val trailingParticle: ParticleEffect = ParticleTypes.SMOKE

    private fun particlesOnImpact() {
        val particleEffect = (ItemStackParticleEffect(ParticleTypes.ITEM, defaultItem.defaultStack))
        for (i in 0..7) {
            world.addParticle(
                particleEffect,
                this.x,
                this.y,
                this.z,
                random.nextDouble() * 4,
                random.nextDouble() * 4,
                random.nextDouble() * 4
            )
        }
    }

    override fun onEntityHit(entityHitResult: EntityHitResult) {
        entityHitResult.entity.damage(this.damageSources.thrown(this, this.owner), hitDamage)
        super.onEntityHit(entityHitResult)
    }

    override fun onCollision(hitResult: HitResult) {
        super.onCollision(hitResult)
        if (!world.isClient) {
            this.discard()
            this.explode()
        }
    }

    override fun tick() {
        if (world.isClient) {
            world.addParticle(
                trailingParticle,
                this.x,
                this.y,
                this.z,
                0.0,
                0.0,
                0.0
            )
        }
        return super.tick()
    }

    open fun explode() {
        particlesOnImpact()
//        println("this should not occur, please check that you override the explode function")
    }

    override fun getDefaultItem(): Item {
        println("this should not occur, please check that you overide the default item")
        return DuskItems.BLUNDERBOMB
    }
}