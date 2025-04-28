package org.teamvoided.dusk_debris.entity.spell

import net.minecraft.entity.LivingEntity
import net.minecraft.entity.attribute.EntityAttributeModifier
import net.minecraft.entity.attribute.EntityAttributes
import net.minecraft.util.math.Vec3d

class VengefulSpiritSpell : AbstractSpell() {
    override fun castRequirements(castor: LivingEntity): Boolean = true

    override fun onCast(castor: LivingEntity) {
        val entityAttributeSpeedInstance = castor.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED)
        entityAttributeSpeedInstance!!.removeModifier(SPELL_CASTING_MODIFIER_ID)
        entityAttributeSpeedInstance.addTemporaryModifier(SPELL_CASTING_PENALTY_MODIFIER)
        castor.velocity = Vec3d.ZERO
        castor.velocityDirty = true
        actualSpell(castor)
    }

    override fun onCastEnd(castor: LivingEntity) {
        castor.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED)!!
            .removeModifier(SPELL_CASTING_MODIFIER_ID)
    }

    override fun priority(castor: LivingEntity): Int = 10

    override fun actualSpell(castor: LivingEntity) {
        val world = castor.world
        if (!world.isClient()) {
            val spellEntity = VengefulSpiritEntity(
                world,
                castor.pos.x,
                castor.pos.y + castor.height / 2,
                castor.pos.z,
                castor.velocity
            )
            spellEntity.owner = castor
            spellEntity.setProperties(castor, castor.pitch, castor.yaw, 0.0f, 1.5f, 1.0f)
            world.spawnEntity(spellEntity)
        }
    }

    companion object {
        private val SPELL_CASTING_PENALTY_MODIFIER =
            EntityAttributeModifier(SPELL_CASTING_MODIFIER_ID, -0.25, EntityAttributeModifier.Operation.ADD_VALUE)
    }
}