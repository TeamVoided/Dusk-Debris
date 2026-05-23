//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//
package org.teamvoided.dusk_debris.entity.throwable_bomb

import net.minecraft.core.particles.ParticleTypes
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.projectile.Projectile
import net.minecraft.world.entity.projectile.ProjectileUtil
import net.minecraft.world.level.Level
import net.minecraft.world.phys.HitResult

abstract class temp protected constructor(entityType: EntityType<out temp?>?, world: Level?) :
    Projectile(entityType, world) {
    protected constructor(type: EntityType<out temp?>?, x: Double, y: Double, z: Double, world: Level?) : this(
        type,
        world
    ) {
        this.setPos(x, y, z)
    }

    protected constructor(type: EntityType<out temp?>?, owner: LivingEntity, world: Level?) : this(
        type,
        owner.x,
        owner.eyeY - 0.10000000149011612,
        owner.z,
        world
    ) {
        this.owner = owner
    }

    override fun shouldRenderAtSqrDistance(distance: Double): Boolean {
        var d = this.boundingBox.size * 4.0
        if (java.lang.Double.isNaN(d)) {
            d = 4.0
        }

        d *= 64.0
        return distance < d * d
    }

    override fun canUsePortal(allowVehicles: Boolean): Boolean {
        return true
    }

    override fun tick() {
        super.tick()
        val hitResult = ProjectileUtil.getHitResultOnMoveVector(this) { entity: Entity? -> this.canHitEntity(entity) }
        if (hitResult.type != HitResult.Type.MISS) {
            this.hitTargetOrDeflectSelf(hitResult)
        }

        this.checkInsideBlocks()
        val vec3d = this.deltaMovement
        val d = this.x + vec3d.x
        val e = this.y + vec3d.y
        val f = this.z + vec3d.z
        this.updateRotation()
        val h: Float
        if (this.isInWater) {
            for (i in 0..3) {
                val g = 0.25f
                level().addParticle(
                    ParticleTypes.BUBBLE,
                    d - vec3d.x * 0.25,
                    e - vec3d.y * 0.25,
                    f - vec3d.z * 0.25,
                    vec3d.x,
                    vec3d.y,
                    vec3d.z
                )
            }

            h = 0.8f
        } else {
            h = 0.99f
        }

        this.setDeltaMovement(vec3d.scale(h.toDouble()))
        this.applyGravity()
        this.setPos(d, e, f)
    }

    override fun getDefaultGravity(): Double {
        return 0.03
    }
}
