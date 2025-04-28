package org.teamvoided.dusk_debris.entity.spell

import net.minecraft.entity.LivingEntity
import net.minecraft.entity.attribute.EntityAttributeModifier
import net.minecraft.util.Identifier
import org.teamvoided.dusk_debris.DuskDebris

abstract class AbstractSpell {

    open val cooldown: Int = 8

    abstract fun castRequirements(castor: LivingEntity): Boolean

    abstract fun onCast(castor: LivingEntity)

    open fun castTick(castor: LivingEntity) {}

    abstract fun onCastEnd(castor: LivingEntity)

    abstract fun priority(castor: LivingEntity): Int

    abstract fun actualSpell(castor: LivingEntity)

    companion object {
        val SPELL_CASTING_MODIFIER_ID: Identifier = DuskDebris.id("spell_casting")
        //val SPELL_CASTING_SPEED_PENALTY_MODIFIER =
        //    EntityAttributeModifier(SPELL_CASTING_MODIFIER_ID, -0.25, EntityAttributeModifier.Operation.ADD_VALUE)
        //val SPELL_CASTING_GRAVITY_PENALTY_MODIFIER =
        //    EntityAttributeModifier(SPELL_CASTING_MODIFIER_ID, -0.08, EntityAttributeModifier.Operation.ADD_VALUE)
    }

}