package org.teamvoided.dusk_debris.spell

import com.mojang.serialization.Codec
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.attribute.EntityAttributeModifier
import net.minecraft.entity.attribute.EntityAttributes
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.text.Text
import net.minecraft.util.math.Vec3d
import org.teamvoided.dusk_debris.entity.helper.DuskSpellStuff
import org.teamvoided.dusk_debris.entity.spell.VengefulSpiritEntity
import org.teamvoided.dusk_debris.util.spellTicksLeft

class VengefulSpiritSpell(codec: Codec<GenericSpellSettings>) : SpellType<GenericSpellSettings>(codec) {

    override fun onCast(castor: LivingEntity, settings: GenericSpellSettings) {
        val entityAttributeSpeedInstance = castor.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED)
        entityAttributeSpeedInstance!!.removeModifier(SpellType.SPELL_CASTING_MODIFIER_ID)
        entityAttributeSpeedInstance.addTemporaryModifier(SPELL_CASTING_PENALTY_MODIFIER)
        castor.velocity = Vec3d.ZERO
        castor.velocityDirty = true
        castor.spellTicksLeft = settings.cooldown
        actualSpell(castor, settings)
    }

    override fun castTick(castor: LivingEntity, settings: GenericSpellSettings) {
    }

    override fun onCastEnd(castor: LivingEntity, settings: GenericSpellSettings) {
        castor.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED)!!
            .removeModifier(SpellType.SPELL_CASTING_MODIFIER_ID)
    }

    override fun actualSpell(castor: LivingEntity, settings: GenericSpellSettings) {
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
            EntityAttributeModifier(
                SpellType.SPELL_CASTING_MODIFIER_ID,
                -0.25,
                EntityAttributeModifier.Operation.ADD_VALUE
            )
    }
}