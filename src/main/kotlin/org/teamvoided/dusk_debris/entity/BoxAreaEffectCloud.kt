package org.teamvoided.dusk_debris.entity

import net.minecraft.entity.AreaEffectCloudEntity
import net.minecraft.entity.EntityDimensions
import net.minecraft.entity.EntityPose
import net.minecraft.entity.EntityType
import net.minecraft.particle.ColoredParticleEffect
import net.minecraft.particle.ParticleTypes
import net.minecraft.util.math.MathHelper
import net.minecraft.world.World
import kotlin.math.sqrt

class BoxAreaEffectCloud(entityType: EntityType<out BoxAreaEffectCloud>, world: World) :
    AreaEffectCloudEntity(entityType, world) {
    override fun getDimensions(pose: EntityPose): EntityDimensions {
        return EntityDimensions.changing(this.radius * 2.0f, this.radius * 2.0f)
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
            val i: Int
            val radius: Float
            if (isWaiting) {
                i = 2
                radius = 0.2f
            } else {
                i = MathHelper.ceil((3.1415927f * setRadius * setRadius) / 2)
                radius = setRadius
            }

            for (j in 0 until i) {
                val pi2Rand = random.nextFloat() * 6.2831855f
                val randInRadius = MathHelper.sqrt(random.nextFloat()) * radius * 1.25
                var x = (random.nextDouble() - random.nextDouble())
                var y = (random.nextDouble() - random.nextDouble())
                var z = (random.nextDouble() - random.nextDouble())
                val a = sqrt(x * x + y * y + z * z)
                x = this.x + (randInRadius * x) / a
                y = this.y + (randInRadius * y) / a + radius
                z = this.z + (randInRadius * z) / a
                if (particleEffect.type === ParticleTypes.ENTITY_EFFECT) {
                    if (isWaiting && random.nextBoolean()) {
                        world.addParticle(
                            ColoredParticleEffect.create(ParticleTypes.ENTITY_EFFECT, -1),
                            x,
                            y,
                            z,
                            0.0,
                            0.0,
                            0.0
                        )
                    } else {
                        world.addParticle(particleEffect, x, y, z, 0.0, 0.0, 0.0)
                    }
                } else if (isWaiting) {
                    world.addParticle(particleEffect, x, y, z, 0.0, 0.0, 0.0)
                } else {
                    world.addParticle(
                        particleEffect, x, y, z,
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