package org.teamvoided.dusk_debris.entity

import net.minecraft.entity.AreaEffectCloudEntity
import net.minecraft.entity.EntityDimensions
import net.minecraft.entity.EntityPose
import net.minecraft.entity.EntityType
import net.minecraft.world.World

class BoxAreaEffectCloud(entityType: EntityType<out BoxAreaEffectCloud>, world: World) :
    AreaEffectCloudEntity(entityType, world) {
    override fun getDimensions(pose: EntityPose): EntityDimensions {
        return EntityDimensions.changing(this.radius * 2.0f, this.radius * 2.0f)
    }

    override fun tick() {
        super.tick()
        val y = this.y - this.radiusGrowth
        this.setPosition(this.x, y, this.z)
    }
}