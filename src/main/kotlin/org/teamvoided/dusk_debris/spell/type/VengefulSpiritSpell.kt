package org.teamvoided.dusk_debris.spell.type

import com.mojang.serialization.Codec
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.ai.attributes.AttributeModifier
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.phys.Vec3
import org.teamvoided.dusk_debris.DuskDebris
import org.teamvoided.dusk_debris.entity.spell.VengefulSpiritEntity
import org.teamvoided.dusk_debris.spell.SpellType
import org.teamvoided.dusk_debris.spell.settings.GenericSpellSettings
import org.teamvoided.dusk_debris.util.spellController

class VengefulSpiritSpell(codec: Codec<GenericSpellSettings>) : SpellType<GenericSpellSettings>(codec) {

    override fun onCast(castor: LivingEntity, settings: GenericSpellSettings) {
        val entityAttributeSpeedInstance = castor.getAttribute(Attributes.MOVEMENT_SPEED)
        entityAttributeSpeedInstance!!.removeModifier(MODIFIER_ID)
        entityAttributeSpeedInstance.addTransientModifier(SPELL_CASTING_PENALTY_MODIFIER)
        castor.setDeltaMovement(Vec3.ZERO)
        castor.hasImpulse = true
        castor.spellController.spellTicksLeft = settings.cooldown
        actualSpell(castor, settings)
    }

    override fun castTick(castor: LivingEntity, settings: GenericSpellSettings) {
    }

    override fun onCastEnd(castor: LivingEntity, settings: GenericSpellSettings) {
        castor.getAttribute(Attributes.MOVEMENT_SPEED)!!.removeModifier(MODIFIER_ID)
    }

    override fun actualSpell(castor: LivingEntity, settings: GenericSpellSettings) {
        val world = castor.level()
        if (!world.isClientSide) {
            val spellEntity = VengefulSpiritEntity(
                world,
                castor.position().x,
                castor.position().y + castor.eyeHeight / 2,
                castor.position().z,
                castor.deltaMovement
            )
            spellEntity.owner = castor
            spellEntity.shootFromRotation(castor, castor.xRot, castor.yRot, 0.0f, 1.5f, 1.0f)
            world.addFreshEntity(spellEntity)
        }
    }

    companion object {
        val MODIFIER_ID: ResourceLocation = DuskDebris.id("spell.vengeful_spirit")
        private val SPELL_CASTING_PENALTY_MODIFIER =
            AttributeModifier(
                MODIFIER_ID,
                -0.25,
                AttributeModifier.Operation.ADD_VALUE
            )

    }
}