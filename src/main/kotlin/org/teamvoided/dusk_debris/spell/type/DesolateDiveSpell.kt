package org.teamvoided.dusk_debris.spell.type

import com.mojang.serialization.Codec
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.attribute.EntityAttributeModifier
import net.minecraft.entity.attribute.EntityAttributes
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.sound.SoundEvents
import net.minecraft.util.Identifier
import net.minecraft.util.math.Vec3d
import org.teamvoided.dusk_debris.DuskDebris
import org.teamvoided.dusk_debris.entity.helper.DuskSpellStuff
import org.teamvoided.dusk_debris.init.DuskParticles
import org.teamvoided.dusk_debris.spell.SpellType
import org.teamvoided.dusk_debris.spell.settings.GenericSpellSettings
import org.teamvoided.dusk_debris.util.spellController

class DesolateDiveSpell(codec: Codec<GenericSpellSettings>) : SpellType<GenericSpellSettings>(codec) {

    override fun castRequirements(castor: LivingEntity, settings: GenericSpellSettings): Boolean {
        return if (castor is PlayerEntity) castor.pitch < -45 && castor.isSneaking
        else true
    }

    override fun onCast(castor: LivingEntity, settings: GenericSpellSettings) {
        addGravity(castor, -castor.gravity)
        castor.velocity = Vec3d.ZERO
        castor.velocityDirty = true
        castor.spellController.spellTicksLeft = settings.cooldown
    }

    override fun castTick(castor: LivingEntity, settings: GenericSpellSettings) {
        if (castor.spellController.spellTicksLeft >= 9) {
            castor.resetFallDistance()
            addGravity(castor)
            castor.velocity = castor.velocity.multiply(0.0, 2.0, 0.0)
            castor.velocityDirty = true
            if (castor.isOnGround)
                actualSpell(castor, settings)
        } else if (castor.spellController.spellTicksLeft == 8) {
            actualSpell(castor, settings)
        }
    }

    override fun onCastEnd(castor: LivingEntity, settings: GenericSpellSettings) {
        removeTempAttribute(castor)
    }

    override fun actualSpell(castor: LivingEntity, settings: GenericSpellSettings) {
        onCastEnd(castor, settings)
        castor.spellController.spellTicksLeft = 8
        val world = castor.world
        val random = castor.random

        world.syncWorldEvent(2013, castor.blockPos, 750)
        repeat(10) {
            val particlePos = Vec3d(
                (random.nextDouble() - 0.5),
                (random.nextDouble() - 0.5),
                (random.nextDouble() - 0.5)
            ).add(castor.pos)
            world.addParticle(
                DuskParticles.DRAINED_SOUL,
                particlePos.x, particlePos.y, particlePos.z,
                (random.nextDouble() - 0.5), 0.0, (random.nextDouble() - 0.5)
            )
        }

        world.playSound(
            castor.x, castor.y, castor.z,
            SoundEvents.ENTITY_WITHER_AMBIENT,
            castor.soundCategory,
            0.2f,
            1.3f + random.nextFloat() * 0.4f,
            false
        )
    }

    private fun addGravity(castor: LivingEntity, value: Double = 0.4) {
        val modifier =
            EntityAttributeModifier(MODIFIER_ID, value, EntityAttributeModifier.Operation.ADD_VALUE)
        val entityAttributeGravityInstance = castor.getAttributeInstance(EntityAttributes.GENERIC_GRAVITY)
        entityAttributeGravityInstance!!.removeModifier(MODIFIER_ID)
        entityAttributeGravityInstance.addTemporaryModifier(modifier)
    }

    private fun removeTempAttribute(
        castor: LivingEntity,
    ) = castor.getAttributeInstance(EntityAttributes.GENERIC_GRAVITY)!!.removeModifier(MODIFIER_ID)

    companion object {
        val MODIFIER_ID: Identifier = DuskDebris.id("spell.desolate_dive")
    }
}