package org.teamvoided.dusk_debris.init

import net.minecraft.entity.effect.StatusEffect
import net.minecraft.entity.effect.StatusEffectType
import net.minecraft.registry.Holder
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import org.teamvoided.dusk_debris.effect.MadnessEffect
import org.teamvoided.dusk_debris.DuskDebris.id

object DuskEffects {
    fun init() = Unit

    val MADNESS = register("madness", MadnessEffect(StatusEffectType.HARMFUL, 0x3E1663))

    private fun register(id: String, entry: StatusEffect): Holder<StatusEffect> {
        return Registry.registerHolder(Registries.STATUS_EFFECT, id(id), entry)
    }
//    fun modifyDamage(entity: LivingEntity, damage: Float): Float {
//        var output = damage
//        if (entity.hasStatusEffect(REDUCE))
//            output *= 1.3f
//        return output
//    }
}