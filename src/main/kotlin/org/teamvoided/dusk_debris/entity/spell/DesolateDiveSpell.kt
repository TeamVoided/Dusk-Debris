package org.teamvoided.dusk_debris.entity.spell

import net.minecraft.entity.LivingEntity
import net.minecraft.entity.attribute.EntityAttributeModifier
import net.minecraft.entity.attribute.EntityAttributes
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.util.math.Vec3d

class DesolateDiveSpell : AbstractSpell() {
    override val cooldown: Int = 12
    private var hitGround: Boolean
    private var divingTicks: Int

    init {
        hitGround = false
        divingTicks = 0
    }

    override fun castRequirements(castor: LivingEntity): Boolean {
        return if (castor is PlayerEntity) castor.pitch < -45 && castor.isSneaking()
        else true
    }

    override fun onCast(castor: LivingEntity) {
        val entityAttributeSpeedInstance = castor.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED)
        entityAttributeSpeedInstance!!.removeModifier(SPELL_CASTING_MODIFIER_ID)
        entityAttributeSpeedInstance.addTemporaryModifier(SPELL_CASTING_SPEED_PENALTY_MODIFIER)
        addGravitySpeed(castor, -castor.gravity)
        castor.velocity = Vec3d.ZERO
        castor.velocityDirty = true
    }

    override fun castTick(castor: LivingEntity) {
        super.castTick(castor)
        if (!hitGround) {
            castor.resetFallDistance()
            //if (castor.spellCooldown < 8) {
            //    castor.spellCooldown = 8
            //    removeGravitySpeed(castor)
            //    addGravitySpeed(castor)   // go look how LivingEntity adds addPowderSnowSlowIfNeeded()
            //    if (castor.isOnGround || divingTicks++ > 60)
            //        actualSpell(castor)
            //}
        }
    }
    //castor.velocity = castor.velocity.add(0.0, -1.0, 0.0)
    //castor.velocityDirty = true

    override fun onCastEnd(castor: LivingEntity) {
        castor.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED)!!.removeModifier(SPELL_CASTING_MODIFIER_ID)
        removeGravitySpeed(castor)
    }

    override fun priority(castor: LivingEntity): Int = 5

    override fun actualSpell(castor: LivingEntity) {
        onCastEnd(castor)
        hitGround = true
    }

    private fun addGravitySpeed(castor: LivingEntity, value: Double = 0.16) {
        val modifier =
            EntityAttributeModifier(SPELL_CASTING_MODIFIER_ID, value, EntityAttributeModifier.Operation.ADD_VALUE)
        val entityAttributeGravityInstance = castor.getAttributeInstance(EntityAttributes.GENERIC_GRAVITY)
        entityAttributeGravityInstance!!.removeModifier(SPELL_CASTING_MODIFIER_ID)
        entityAttributeGravityInstance.addTemporaryModifier(modifier)
    }

    private fun removeGravitySpeed(castor: LivingEntity) {
        castor.getAttributeInstance(EntityAttributes.GENERIC_GRAVITY)!!.removeModifier(SPELL_CASTING_MODIFIER_ID)
    }

    companion object {
        val SPELL_CASTING_SPEED_PENALTY_MODIFIER =
            EntityAttributeModifier(SPELL_CASTING_MODIFIER_ID, -25.0, EntityAttributeModifier.Operation.ADD_VALUE)
    }
}