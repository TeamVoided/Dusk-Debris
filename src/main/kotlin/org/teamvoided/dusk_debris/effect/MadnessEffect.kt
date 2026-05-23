package org.teamvoided.dusk_debris.effect

import net.minecraft.core.particles.ParticleOptions
import net.minecraft.world.effect.MobEffectCategory
import net.minecraft.world.entity.LivingEntity

class MadnessEffect : DnDStatusEffect {
    constructor(type: MobEffectCategory, color: Int) : super(type, color)
    constructor(type: MobEffectCategory, color: Int, particle: ParticleOptions) : super(type, color, particle)


    override fun shouldApplyEffectTickThisTick(tick: Int, amplifier: Int): Boolean = true

    override fun applyEffectTick(entity: LivingEntity, amplifier: Int): Boolean {
        return true
    }
}