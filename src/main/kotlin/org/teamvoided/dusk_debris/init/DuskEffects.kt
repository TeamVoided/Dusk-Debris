package org.teamvoided.dusk_debris.init

import net.minecraft.core.Holder
import net.minecraft.core.Registry
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.effect.MobEffect
import net.minecraft.world.effect.MobEffectCategory
import org.teamvoided.dusk_debris.DuskDebris.id
import org.teamvoided.dusk_debris.effect.MadnessEffect

object DuskEffects {
    fun init() = Unit

    val MADNESS = register("madness", MadnessEffect(MobEffectCategory.HARMFUL, 0x3E1663))
    private fun register(id: String, entry: MobEffect): Holder<MobEffect> =
        Registry.registerForHolder(BuiltInRegistries.MOB_EFFECT, id(id), entry)


//    fun modifyDamage(entity: LivingEntity, damage: Float): Float {
//        var output = damage
//        if (entity.hasStatusEffect(REDUCE))
//            output *= 1.3f
//        return output
//    }
}