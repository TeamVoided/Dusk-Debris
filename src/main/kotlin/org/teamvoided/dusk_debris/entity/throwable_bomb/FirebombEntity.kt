package org.teamvoided.dusk_debris.entity.throwable_bomb

import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.item.Item
import net.minecraft.particle.ParticleTypes
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.util.math.Box
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World
import net.minecraft.world.explosion.Explosion
import net.minecraft.world.explosion.ExplosionBehavior
import org.teamvoided.dusk_autumn.init.DuskEntities
import org.teamvoided.dusk_debris.data.DuskBlockTags
import org.teamvoided.dusk_debris.data.DuskEntityTypeTags
import org.teamvoided.dusk_debris.init.DuskItems
import org.teamvoided.dusk_debris.world.explosion.FirebombExplosionBehavior

class FirebombEntity : AbstractThrwowableBombEntity {
    constructor(entityType: EntityType<out FirebombEntity>, world: World) : super(entityType, world)

    constructor(world: World, owner: LivingEntity?) : super(DuskEntities.FIREBOMB, owner, world)

    constructor(world: World, x: Double, y: Double, z: Double) : super(DuskEntities.FIREBOMB, x, y, z, world)


    override val trailingParticle = ParticleTypes.FLAME
    override var explosionBehavior: ExplosionBehavior = FirebombExplosionBehavior(
        DuskBlockTags.FIREBOMB_DESTROYS
    )
//    DustColorTransitionParticleEffect(FIRE, GREY, 1.0F)

    override fun explode() {
        world.playSound(
            this,
            this.blockPos,
            SoundEvents.BLOCK_GLASS_BREAK,
            SoundCategory.BLOCKS,
            0.7f,
            0.9f + world.random.nextFloat() * 0.2f
        )
        world.createExplosion(
            this, Explosion.createDamageSource(
                this.world,
                this
            ), explosionBehavior,
            this.x,
            this.getBodyY(0.0625),
            this.z,
            BlunderbombEntity.DEFAULT_EXPLOSION_POWER,
            false,
            World.ExplosionSourceType.TNT,
            ParticleTypes.BUBBLE,
            ParticleTypes.BUBBLE,
            SoundEvents.BLOCK_RESPAWN_ANCHOR_DEPLETE
        )
        val firebombRadius = 4.5
        val entitiesNearby = world.getOtherEntities(
            this, Box(
                this.x - firebombRadius,
                this.y - firebombRadius,
                this.z - firebombRadius,
                this.x + firebombRadius,
                this.y + firebombRadius,
                this.z + firebombRadius
            )
        ) { obj: Entity -> obj.isAlive && !obj.type.isIn(DuskEntityTypeTags.FIREBOMB_DOES_NOT_DAMAGE) }

        entitiesNearby.forEach {
            it.damage(this.damageSources.onFire(), 3f)
            it.fireTicks += 200
        }
        super.explode()
    }

    override fun getDefaultItem(): Item {
        return DuskItems.FIREBOMB
    }

    companion object {
        val FIRE = Vec3d.unpackRgb(0xFFCA2B).toVector3f()
        val GREY = Vec3d.unpackRgb(0x0C0400).toVector3f()
    }
}