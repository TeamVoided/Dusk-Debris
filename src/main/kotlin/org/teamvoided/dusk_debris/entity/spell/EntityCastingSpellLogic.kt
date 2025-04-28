package org.teamvoided.dusk_debris.entity.spell

import net.minecraft.entity.LivingEntity
import net.minecraft.entity.attribute.EntityAttributeModifier
import net.minecraft.entity.attribute.EntityAttributes
import net.minecraft.util.Identifier
import org.teamvoided.dusk_debris.DuskDebris.id

object EntityCastingSpellLogic {

    fun startStopForCast(entity: LivingEntity, spell: AbstractSpell): Int {
        spell.onCast(entity)
        return spell.cooldown
    }

    fun endStopForCast(entity: LivingEntity, spell: AbstractSpell) {
        spell.onCastEnd(entity)
    }

    private val SPELL_CASTING_MODIFIER_ID: Identifier = id("spell_casting")
    private val SPELL_CASTING_SPEED_PENALTY_MODIFIER =
        EntityAttributeModifier(SPELL_CASTING_MODIFIER_ID, -0.25, EntityAttributeModifier.Operation.ADD_VALUE)
    private val SPELL_CASTING_GRAVITY_PENALTY_MODIFIER =
        EntityAttributeModifier(SPELL_CASTING_MODIFIER_ID, -0.08, EntityAttributeModifier.Operation.ADD_VALUE)
    private const val DEFAULT_STOP_DURATION = 10
}