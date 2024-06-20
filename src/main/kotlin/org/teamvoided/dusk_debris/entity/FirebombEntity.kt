package org.teamvoided.dusk_debris.entity

import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.item.Item
import net.minecraft.particle.DustColorTransitionParticleEffect
import net.minecraft.particle.ParticleTypes
import net.minecraft.util.math.Box
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World
import net.minecraft.world.explosion.ExplosionBehavior
import org.teamvoided.dusk_autumn.init.DuskEntities
import org.teamvoided.dusk_debris.data.DuskBlockTags
import org.teamvoided.dusk_debris.data.DuskEntityTypeTags
import org.teamvoided.dusk_debris.init.DuskItems
import org.teamvoided.dusk_debris.world.explosion.FirebombExplosionBehavior
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

class FirebombEntity : BlunderbombEntity {
    constructor(entityType: EntityType<out FirebombEntity>, world: World) : super(entityType, world)

    constructor(world: World, owner: LivingEntity?) : super(DuskEntities.FIREBOMB, owner, world)

    constructor(world: World, x: Double, y: Double, z: Double) : super(DuskEntities.FIREBOMB, x, y, z, world)



    override val trailingParticle = ParticleTypes.FLAME
//    DustColorTransitionParticleEffect(FIRE, GREY, 1.0F)
    override var explosionBehavior: ExplosionBehavior = FirebombExplosionBehavior(
        DuskBlockTags.FIREBOMB_DESTROYS
    )

    override fun explode() {
        val firebombRadius = 4.0
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
            it.fireTicks += 160
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