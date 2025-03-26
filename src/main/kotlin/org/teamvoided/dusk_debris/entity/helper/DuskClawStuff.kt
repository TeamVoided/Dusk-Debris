package org.teamvoided.dusk_debris.entity.helper

import net.minecraft.util.math.Vec3d

interface DuskClawStuff {
    fun setHangingDirection(direction: Vec3d)
    fun getHangingDirection(): Vec3d
    //fun setHanging(hanging: Boolean)
    //fun getHanging(): Boolean
}