package org.teamvoided.dusk_debris.util

import com.mojang.blaze3d.systems.RenderSystem
import net.minecraft.client.render.BackgroundRenderer
import net.minecraft.client.render.Camera
import net.minecraft.client.render.CameraSubmersionType
import net.minecraft.entity.Entity

fun customizeFog(
    camera: Camera,
    entity: Entity,
    fogType: BackgroundRenderer.FogType,
    viewDistance: Float,
    thickFog: Boolean,
    tickDelta: Float,
    exsistingParams: BackgroundRenderer.FogParameters
) {
    val submergeType = camera.submersionType
    if (submergeType != CameraSubmersionType.NONE) return
    val world = entity.world
    if (!world.getBiome(entity.blockPos).value().hasPrecipitation()) return
    RenderSystem.setShaderFogStart(10.0f)
    RenderSystem.setShaderFogEnd(viewDistance / 2)
}