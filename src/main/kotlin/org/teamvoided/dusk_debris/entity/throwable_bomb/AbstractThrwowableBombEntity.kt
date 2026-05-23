package org.teamvoided.dusk_debris.entity.throwable_bomb

import net.minecraft.core.particles.ItemParticleOption
import net.minecraft.core.particles.ParticleOptions
import net.minecraft.core.particles.ParticleTypes
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.projectile.ThrowableItemProjectile
import net.minecraft.world.item.Item
import net.minecraft.world.level.ExplosionDamageCalculator
import net.minecraft.world.level.Level
import net.minecraft.world.phys.EntityHitResult
import net.minecraft.world.phys.HitResult
import org.teamvoided.dusk_debris.init.DuskBlocks

open class AbstractThrwowableBombEntity : ThrowableItemProjectile {
    constructor(entityType: EntityType<out AbstractThrwowableBombEntity>, world: Level) : super(entityType, world)
    constructor(entityType: EntityType<out AbstractThrwowableBombEntity>, owner: LivingEntity?, world: Level) :
            super(entityType, owner, world)

    constructor(
        entityType: EntityType<out AbstractThrwowableBombEntity>,
        x: Double,
        y: Double,
        z: Double,
        world: Level
    ) :
            super(entityType, x, y, z, world)

    constructor(world: Level, owner: LivingEntity?) : super(null, owner, world)

    constructor(world: Level, x: Double, y: Double, z: Double) : super(null, x, y, z, world)


    override fun onHitEntity(entityHitResult: EntityHitResult) {
        if (getHitDamage() > 0f)
            entityHitResult.entity.hurt(this.damageSources().thrown(this, this.owner), getHitDamage())
        super.onHitEntity(entityHitResult)
    }

    override fun onHit(hitResult: HitResult) {
        super.onHit(hitResult)
        if (!level().isClientSide) {
            this.explode()
            this.discard()
        }
    }

    override fun tick() {
        if (level().isClientSide && tickCount >= 2) {
            level().addParticle(
                getTrailingParticle(),
                this.x,
                this.y + 0.15,
                this.z,
                0.0,
                0.0,
                0.0
            )
        }
        return super.tick()
    }

    open fun explode() {
        val particleEffect = (ItemParticleOption(ParticleTypes.ITEM, defaultItem.defaultInstance))
        val velocityMultiplier = 0.33
        val serverWorld = this.level() as ServerLevel
        serverWorld.sendParticles(
            particleEffect,
            this.x,
            this.y,
            this.z,
            14,
            0.0,
            0.0,
            0.0,
            velocityMultiplier
        )

//        println("this should not occur, please check that you override the explode function")
    }

    override fun getDefaultItem(): Item {
        println("this should not occur, please check that you override the getDefaultItem function")
        return DuskBlocks.BLUNDERBOMB_BLOCK.asItem()
    }

    open fun getTrailingParticle(): ParticleOptions = ParticleTypes.SMOKE
    open fun getHitDamage(): Float = 0f
    open fun getExplosionBehavior(): ExplosionDamageCalculator = ExplosionDamageCalculator()
}