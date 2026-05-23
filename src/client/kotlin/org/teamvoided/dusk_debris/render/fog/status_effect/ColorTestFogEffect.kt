package org.teamvoided.dusk_debris.render.fog.status_effect

import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.renderer.FogRenderer
import net.minecraft.core.Holder
import net.minecraft.util.Mth
import net.minecraft.world.effect.MobEffect
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.entity.LivingEntity
import org.teamvoided.dusk_debris.init.DuskEffects


@Environment(EnvType.CLIENT)
class ColorTestFogEffect : FogRenderer.MobEffectFogFunction {
    override fun getMobEffect(): Holder<MobEffect> {
        return DuskEffects.MADNESS
    }

    override fun setupFog(
        parameters: FogRenderer.FogData,
        entity: LivingEntity?,
        effect: MobEffectInstance,
        viewDistance: Float,
        tickDelta: Float
    ) {
        val f = Mth.lerp(effect.getBlendFactor(entity, tickDelta), viewDistance, 15.0f)
        parameters.start = if (parameters.mode == FogRenderer.FogMode.FOG_SKY) 0.0f else f * 0.75f
        parameters.end = f
    }

    override fun getModifiedVoidDarkness(
        entity: LivingEntity?,
        effect: MobEffectInstance,
        horizonShading: Float,
        tickDelta: Float
    ): Float {
        return 1.0f - effect.getBlendFactor(entity, tickDelta)
    }
}