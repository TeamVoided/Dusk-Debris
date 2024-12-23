package org.teamvoided.dusk_debris.util

import com.mojang.blaze3d.systems.RenderSystem
import net.minecraft.client.render.BackgroundRenderer
import net.minecraft.client.render.Camera
import net.minecraft.client.render.CameraSubmersionType
import net.minecraft.client.world.ClientWorld
import net.minecraft.entity.Entity
import net.minecraft.registry.Holder
import net.minecraft.util.CubicSampler
import net.minecraft.util.math.Vec3d
import net.minecraft.world.biome.Biome
import net.minecraft.world.biome.source.BiomeAccess
import org.teamvoided.dusk_debris.data.tags.DuskBiomeTags
import kotlin.math.cos

fun customizeFog(
    camera: Camera,
    entity: Entity,
    fogType: BackgroundRenderer.FogType,
    viewDistance: Float,
    thickFog: Boolean,
    tickDelta: Float,
    exsistingParams: BackgroundRenderer.FogParameters,
    fogEffect: BackgroundRenderer.FogEffect?
) {
    if (fogEffect != null) return

    val submergeType = camera.submersionType
    if (submergeType != CameraSubmersionType.NONE) return

    val world = entity.world as ClientWorld
    val biomeAccess: BiomeAccess = world.biomeAccess
    val position: Vec3d = camera.pos.subtract(2.0, 2.0, 2.0).multiply(0.25)
    val sampler = CubicSampler.sampleVec3d(position) { x: Int, y: Int, z: Int ->
        getFogRange(biomeAccess.getBiomeForNoiseGen(x, y, z), world)
    }
    if (sampler.x == 1.0 && sampler.y == 1.0) return

    var start = RenderSystem.getShaderFogStart() * sampler.x.toFloat()
    var end = RenderSystem.getShaderFogEnd() * sampler.y.toFloat()
    if (start > end) {
        val switch = start
        start = end
        end = switch
    }
    RenderSystem.setShaderFogStart(start)
    RenderSystem.setShaderFogEnd(end)
}

fun getFogRange(biome: Holder<Biome>, world: ClientWorld): Vec3d {
    var start = 1.0
    var end = 1.0
    if (biome.isIn(DuskBiomeTags.FOG_START_0)) {
        start = 0.0
    } else if (biome.isIn(DuskBiomeTags.FOG_START_20)) {
        start = 0.2
    } else if (biome.isIn(DuskBiomeTags.FOG_START_50)) {
        start = 0.5
    } else if (biome.isIn(DuskBiomeTags.FOG_START_80)) {
        start = 0.8
    }
    if (biome.isIn(DuskBiomeTags.FOG_END_0)) {
        end = 0.0
    } else if (biome.isIn(DuskBiomeTags.FOG_END_20)) {
        end = 0.2
    } else if (biome.isIn(DuskBiomeTags.FOG_END_50)) {
        end = 0.5
    } else if (biome.isIn(DuskBiomeTags.FOG_END_80)) {
        end = 0.8
    }
    if (biome.isIn(DuskBiomeTags.FOG_BOREAL_VALLEY)) {
        val mult = 0.33333 + ((cos(0.005f * world.time) + 1) * 0.33333)
        start *= mult
        end *= mult
    }
    return Vec3d(start, end, 0.0)
}