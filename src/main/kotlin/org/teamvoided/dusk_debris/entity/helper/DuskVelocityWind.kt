package org.teamvoided.dusk_debris.entity.helper

import net.minecraft.world.phys.Vec3

interface DuskVelocityWind {
    fun setWind(wind: Vec3)
    fun getWind(): Vec3
}