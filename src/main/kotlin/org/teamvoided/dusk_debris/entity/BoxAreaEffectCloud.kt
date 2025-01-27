package org.teamvoided.dusk_debris.entity

import net.minecraft.entity.AreaEffectCloudEntity
import net.minecraft.entity.EntityDimensions
import net.minecraft.entity.EntityPose
import net.minecraft.entity.EntityType
import net.minecraft.particle.ColoredParticleEffect
import net.minecraft.particle.ParticleTypes
import net.minecraft.util.math.MathHelper
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World

class BoxAreaEffectCloud(entityType: EntityType<out BoxAreaEffectCloud>, world: World) :
    AreaEffectCloudEntity(entityType, world) {

    override fun getDimensions(pose: EntityPose): EntityDimensions {
        val dimensions = radius * 2f
        return EntityDimensions.changing(dimensions, dimensions)
    }

    override fun tick() {
        if (!this.world.isClient)
            super.tick()
        else {
            val isWaiting = this.isWaiting
            val setRadius = this.radius
            if (isWaiting && random.nextBoolean()) {
                return
            }

            val particleEffect = this.particleType
            val count: Int
            val radius: Float
            if (isWaiting) {
                count = 2
                radius = 0.2f
            } else {
                count = MathHelper.ceil((3.1415927f * setRadius * setRadius) / 5)
                radius = setRadius
            }

            for (j in 0 until count) {
                val randInRadius = MathHelper.sqrt(random.nextFloat()) * radius * 1.5f
                val inSphere = Vec3d(
                    random.nextDouble() - random.nextDouble(),
                    random.nextDouble() - random.nextDouble(),
                    random.nextDouble() - random.nextDouble()
                ).normalize().multiply(randInRadius.toDouble()).add(x, y + setRadius / 2, z)
                if (particleEffect.type === ParticleTypes.ENTITY_EFFECT) {
                    if (isWaiting && random.nextBoolean()) {
                        world.addParticle(
                            ColoredParticleEffect.create(ParticleTypes.ENTITY_EFFECT, -1),
                            inSphere.x,
                            inSphere.y,
                            inSphere.z,
                            0.0,
                            0.0,
                            0.0
                        )
                    } else {
                        world.addParticle(particleEffect, inSphere.x, inSphere.y, inSphere.z, 0.0, 0.0, 0.0)
                    }
                } else if (isWaiting) {
                    world.addParticle(particleEffect, inSphere.x, inSphere.y, inSphere.z, 0.0, 0.0, 0.0)
                } else {
                    if (j <= 3) {
                        world.addImportantParticle(particleEffect, inSphere.x, inSphere.y, inSphere.z, 0.0, 0.0, 0.0)
                    }
                    world.addParticle(
                        particleEffect,
                        inSphere.x,
                        inSphere.y,
                        inSphere.z,
                        0.0,
                        0.0,
                        0.0
//                        (0.5 - random.nextDouble()) * 0.15,
//                        (0.5 - random.nextDouble()) * 0.15,
//                        (0.5 - random.nextDouble()) * 0.15
                    )
                }
            }
        }
        val y = this.y - this.radiusGrowth
        this.setPosition(this.x, y, this.z)
    }
}