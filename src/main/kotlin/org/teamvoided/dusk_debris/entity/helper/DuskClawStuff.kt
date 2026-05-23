package org.teamvoided.dusk_debris.entity.helper

import net.minecraft.world.phys.Vec3

interface DuskClawStuff {
    fun setHangingDirection(direction: Vec3)
    fun getHangingDirection(): Vec3
    fun getHanging(): Boolean
    fun setHanging(hanging: Boolean)
}