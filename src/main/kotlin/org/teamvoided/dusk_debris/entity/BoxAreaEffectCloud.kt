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
                i = MathHelper.ceil((3.1415927f * setRadius * setRadius) / 5)
                radius = setRadius
            }

            for (j in 0 until i) {
                val randInRadius = MathHelper.sqrt(random.nextFloat()) * radius * 1.25
                var inSphereX = (random.nextDouble() - random.nextDouble())
                var inSphereY = (random.nextDouble() - random.nextDouble())
                var inSphereZ = (random.nextDouble() - random.nextDouble())
                val theSphereFunction = sqrt(inSphereX * inSphereX + inSphereY * inSphereY + inSphereZ * inSphereZ)
                inSphereX = this.x + (randInRadius * inSphereX) / theSphereFunction
                inSphereY = this.y + (randInRadius * inSphereY) / theSphereFunction + radius
                inSphereZ = this.z + (randInRadius * inSphereZ) / theSphereFunction
                if (particleEffect.type === ParticleTypes.ENTITY_EFFECT) {
                    if (isWaiting && random.nextBoolean()) {
                        world.addParticle(
                            ColoredParticleEffect.create(ParticleTypes.ENTITY_EFFECT, -1),
                            inSphereX,
                            inSphereY,
                            inSphereZ,
                            0.0,
                            0.0,
                            0.0
                        )
                    } else {
                        world.addParticle(particleEffect, inSphereX, inSphereY, inSphereZ, 0.0, 0.0, 0.0)
                    }
                } else if (isWaiting) {
                    world.addParticle(particleEffect, inSphereX, inSphereY, inSphereZ, 0.0, 0.0, 0.0)
                } else {
                    if (j <= 3){
                        world.addImportantParticle(particleEffect, inSphereX, inSphereY, inSphereZ, 0.0, 0.0, 0.0)}
                    world.addParticle(
                        particleEffect,
                        inSphereX,
                        inSphereY,
                        inSphereZ,
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