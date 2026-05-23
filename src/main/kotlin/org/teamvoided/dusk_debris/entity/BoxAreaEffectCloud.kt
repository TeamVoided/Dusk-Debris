package org.teamvoided.dusk_debris.entity

import net.minecraft.core.particles.ColorParticleOption
import net.minecraft.core.particles.ParticleTypes
import net.minecraft.util.Mth
import net.minecraft.world.entity.AreaEffectCloud
import net.minecraft.world.entity.EntityDimensions
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.Pose
import net.minecraft.world.level.Level
import net.minecraft.world.phys.Vec3

class BoxAreaEffectCloud(entityType: EntityType<out BoxAreaEffectCloud>, world: Level) :
    AreaEffectCloud(entityType, world) {

    override fun getDimensions(pose: Pose): EntityDimensions {
        val dimensions = radius * 2f
        return EntityDimensions.scalable(dimensions, dimensions)
    }

    override fun tick() {
        if (!this.level().isClientSide)
            super.tick()
        else {
            val isWaiting = this.isWaiting
            val setRadius = this.radius
            if (isWaiting && random.nextBoolean()) {
                return
            }

            val particleEffect = this.particle
            val count: Int
            val radius: Float
            if (isWaiting) {
                count = 2
                radius = 0.2f
            } else {
                count = Mth.ceil((3.1415927f * setRadius * setRadius) / 5)
                radius = setRadius
            }

            for (j in 0 until count) {
                val randInRadius = Mth.sqrt(random.nextFloat()) * radius * 1.5f
                val inSphere = Vec3(
                    random.nextDouble() - random.nextDouble(),
                    random.nextDouble() - random.nextDouble(),
                    random.nextDouble() - random.nextDouble()
                ).normalize().scale(randInRadius.toDouble()).add(x, y + setRadius / 2, z)
                if (particleEffect.type === ParticleTypes.ENTITY_EFFECT) {
                    if (isWaiting && random.nextBoolean()) {
                        level().addParticle(
                            ColorParticleOption.create(ParticleTypes.ENTITY_EFFECT, -1),
                            inSphere.x,
                            inSphere.y,
                            inSphere.z,
                            0.0,
                            0.0,
                            0.0
                        )
                    } else {
                        level().addParticle(particleEffect, inSphere.x, inSphere.y, inSphere.z, 0.0, 0.0, 0.0)
                    }
                } else if (isWaiting) {
                    level().addParticle(particleEffect, inSphere.x, inSphere.y, inSphere.z, 0.0, 0.0, 0.0)
                } else {
                    if (j <= 3) {
                        level().addAlwaysVisibleParticle(particleEffect, inSphere.x, inSphere.y, inSphere.z, 0.0, 0.0, 0.0)
                    }
                    level().addParticle(
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
        val y = this.y - this.radiusPerTick
        this.setPos(this.x, y, this.z)
    }
}