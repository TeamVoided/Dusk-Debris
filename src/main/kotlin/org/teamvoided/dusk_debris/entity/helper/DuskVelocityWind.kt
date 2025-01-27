package org.teamvoided.dusk_debris.entity.helper

import net.minecraft.util.math.Vec3d

interface DuskVelocityWind {
    fun setWind(wind: Vec3d)
    fun getWind(): Vec3d
}