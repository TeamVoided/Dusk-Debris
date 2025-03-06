package org.teamvoided.dusk_debris.init

import net.minecraft.entity.effect.StatusEffect
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.entity.effect.StatusEffectType
import net.minecraft.entity.effect.StatusEffects
import net.minecraft.potion.Potion
import net.minecraft.registry.Holder
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import org.teamvoided.dusk_debris.effect.MadnessEffect
import org.teamvoided.dusk_debris.DuskDebris.id

object DuskPotions {
    fun init() = Unit


    private fun register(id: String, entry: Potion): Holder<Potion> =
        Registry.registerHolder(Registries.POTION, id(id), entry)
}