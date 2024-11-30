package org.teamvoided.dusk_debris.render.fog.status_effect

import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.render.BackgroundRenderer
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.effect.StatusEffect
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.registry.Holder
import net.minecraft.util.math.MathHelper
import org.teamvoided.dusk_debris.init.DuskEffects


@Environment(EnvType.CLIENT)
class ColorTestFogEffect : BackgroundRenderer.FogEffect {
    override fun getStatusEffect(): Holder<StatusEffect> {
        return DuskEffects.MADNESS
    }

    override fun applyFogEffects(
        parameters: BackgroundRenderer.FogParameters,
        entity: LivingEntity?,
        effect: StatusEffectInstance,
        viewDistance: Float,
        tickDelta: Float
    ) {
        val f = MathHelper.lerp(effect.getBlendFactor(entity, tickDelta), viewDistance, 15.0f)
        parameters.fogStart = if (parameters.fogType == BackgroundRenderer.FogType.FOG_SKY) 0.0f else f * 0.75f
        parameters.fogEnd = f
    }

    override fun fadeAsEffectWearsOff(
        entity: LivingEntity?,
        effect: StatusEffectInstance,
        horizonShading: Float,
        tickDelta: Float
    ): Float {
        return 1.0f - effect.getBlendFactor(entity, tickDelta)
    }
}