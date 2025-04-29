package org.teamvoided.dusk_debris.spell

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.attribute.EntityAttribute
import net.minecraft.entity.attribute.EntityAttributeModifier
import net.minecraft.entity.attribute.EntityAttributes
import net.minecraft.registry.Holder
import net.minecraft.sound.SoundEvents
import net.minecraft.util.math.Vec3d
import org.teamvoided.dusk_debris.entity.helper.DuskSpellStuff

class DesolateDiveSpell(override val priority: Int = 5, override val cooldown: Int = 62) : Spell {
    override fun castRequirements(castor: LivingEntity): Boolean {
        return if (castor is DuskSpellStuff) castor.pitch < -45 && castor.isSneaking
        else true
    }

    override fun onCast(castor: LivingEntity) {
        addSpeedStop(castor)
        addGravitySpeed(castor, -castor.gravity)
        castor.velocity = Vec3d.ZERO
        castor.velocityDirty = true
    }

    override fun castTick(castor: LivingEntity) {
        super.castTick(castor)
        if (castor is DuskSpellStuff && castor.getSpellTicksLeft() >= 9) {
            castor.resetFallDistance()
            removeTempAttribute(castor)
            addGravitySpeed(castor)   // go look how LivingEntity adds addPowderSnowSlowIfNeeded()
            if (castor.isOnGround)
                actualSpell(castor)
        } else {
            actualSpell(castor)
            if (castor is DuskSpellStuff) castor.setSpellTicksLeft(0)
        }
    }
    //castor.velocity = castor.velocity.add(0.0, -1.0, 0.0)
    //castor.velocityDirty = true

    override fun onCastEnd(castor: LivingEntity) {
        removeTempAttribute(castor, EntityAttributes.GENERIC_MOVEMENT_SPEED)
        removeTempAttribute(castor)
    }

    override fun actualSpell(castor: LivingEntity) {
        onCastEnd(castor)
        if (castor is DuskSpellStuff) castor.setSpellTicksLeft(8)
        val world = castor.world
        val random = castor.random

        if (world.isClient) {
            world.playSound(
                castor.x, castor.y, castor.z,
                SoundEvents.ENTITY_WITHER_AMBIENT,
                castor.soundCategory,
                0.2f,
                1.3f + random.nextFloat() * 0.4f,
                false
            )
        }
    }

    private fun addGravitySpeed(castor: LivingEntity, value: Double = 0.16) {
        val modifier =
            EntityAttributeModifier(Spell.SPELL_CASTING_MODIFIER_ID, value, EntityAttributeModifier.Operation.ADD_VALUE)
        val entityAttributeGravityInstance = castor.getAttributeInstance(EntityAttributes.GENERIC_GRAVITY)
        entityAttributeGravityInstance!!.removeModifier(Spell.SPELL_CASTING_MODIFIER_ID)
        entityAttributeGravityInstance.addTemporaryModifier(modifier)
    }

    private fun addSpeedStop(castor: LivingEntity) {
        val entityAttributeSpeedInstance = castor.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED)
        entityAttributeSpeedInstance!!.removeModifier(Spell.SPELL_CASTING_MODIFIER_ID)
        entityAttributeSpeedInstance.addTemporaryModifier(SPELL_CASTING_SPEED_PENALTY_MODIFIER)
    }

    private fun removeTempAttribute(
        castor: LivingEntity,
        attribute: Holder<EntityAttribute> = EntityAttributes.GENERIC_GRAVITY
    ) = castor.getAttributeInstance(attribute)!!.removeModifier(Spell.SPELL_CASTING_MODIFIER_ID)


    companion object {
        val SPELL_CASTING_SPEED_PENALTY_MODIFIER =
            EntityAttributeModifier(Spell.SPELL_CASTING_MODIFIER_ID, -25.0, EntityAttributeModifier.Operation.ADD_VALUE)
    }
}