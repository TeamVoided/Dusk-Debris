package org.teamvoided.dusk_debris.util

import com.mojang.blaze3d.systems.RenderSystem
import net.minecraft.client.Camera
import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.client.renderer.FogRenderer
import net.minecraft.core.Holder
import net.minecraft.util.CubicSampler
import net.minecraft.world.entity.Entity
import net.minecraft.world.level.biome.Biome
import net.minecraft.world.level.biome.BiomeManager
import net.minecraft.world.level.material.FogType
import net.minecraft.world.phys.Vec3
import org.teamvoided.dusk_debris.DuskDebris.id
import org.teamvoided.dusk_debris.data.gen.providers.FogModifiers
import kotlin.math.cos

fun customizeFog(
    camera: Camera,
    entity: Entity,
    fogType: FogRenderer.FogMode,
    viewDistance: Float,
    thickFog: Boolean,
    tickDelta: Float,
    exsistingParams: FogRenderer.FogData,
    fogEffect: FogRenderer.MobEffectFogFunction?
) {
    if (fogEffect != null) return

    val submergeType = camera.fluidInCamera
    if (submergeType != FogType.NONE) return

    val world = entity.level() as ClientLevel
    val biomeAccess: BiomeManager = world.biomeManager
    val position: Vec3 = camera.position.subtract(2.0, 2.0, 2.0).scale(0.25)
    val sampler = CubicSampler.gaussianSampleVec3(position) { x: Int, y: Int, z: Int ->
        getFogRange(biomeAccess.getNoiseBiomeAtQuart(x, y, z), world)
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

fun getFogRange(biome: Holder<Biome>, world: ClientLevel): Vec3 {
    val modifier = FogModifiers.fogFromBiome(world.registryAccess(), biome).value()
    var start = modifier.start
    var end = modifier.end
    if (modifier.modifier == id("boreal_valley")) {
        val mult = 0.33333 + ((cos(0.005f * world.gameTime) + 1) * 0.33333)
        start *= mult
        end *= mult
    }
    return Vec3(start, end, 0.0)
}