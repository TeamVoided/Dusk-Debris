//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//
package org.teamvoided.dusk_debris.entity.throwable_bomb

import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.projectile.ProjectileEntity
import net.minecraft.entity.projectile.ProjectileUtil
import net.minecraft.particle.ParticleTypes
import net.minecraft.util.hit.HitResult
import net.minecraft.world.World

abstract class temp protected constructor(entityType: EntityType<out temp?>?, world: World?) :
    ProjectileEntity(entityType, world) {
    protected constructor(type: EntityType<out temp?>?, x: Double, y: Double, z: Double, world: World?) : this(
        type,
        world
    ) {
        this.setPosition(x, y, z)
    }

    protected constructor(type: EntityType<out temp?>?, owner: LivingEntity, world: World?) : this(
        type,
        owner.x,
        owner.eyeY - 0.10000000149011612,
        owner.z,
        world
    ) {
        this.owner = owner
    }

    override fun shouldRender(distance: Double): Boolean {
        var d = this.bounds.averageSideLength * 4.0
        if (java.lang.Double.isNaN(d)) {
            d = 4.0
        }

        d *= 64.0
        return distance < d * d
    }

    override fun canUsePortals(allowVehicles: Boolean): Boolean {
        return true
    }

    override fun tick() {
        super.tick()
        val hitResult = ProjectileUtil.getCollision(this) { entity: Entity? -> this.canHit(entity) }
        if (hitResult.type != HitResult.Type.MISS) {
            this.hitOrDeflect(hitResult)
        }

        this.checkBlockCollision()
        val vec3d = this.velocity
        val d = this.x + vec3d.x
        val e = this.y + vec3d.y
        val f = this.z + vec3d.z
        this.updateRotation()
        val h: Float
        if (this.isTouchingWater) {
            for (i in 0..3) {
                val g = 0.25f
                world.addParticle(
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

        this.velocity = vec3d.multiply(h.toDouble())
        this.applyGravity()
        this.setPosition(d, e, f)
    }

    override fun getDefaultGravity(): Double {
        return 0.03
    }
}
