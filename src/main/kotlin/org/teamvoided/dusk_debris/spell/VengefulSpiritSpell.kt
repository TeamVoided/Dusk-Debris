package org.teamvoided.dusk_debris.spell

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.attribute.EntityAttributeModifier
import net.minecraft.entity.attribute.EntityAttributes
import net.minecraft.util.StringIdentifiable
import net.minecraft.util.math.Vec3d
import net.minecraft.world.gen.stateprovider.BlockStateProvider
import org.teamvoided.dusk_debris.entity.spell.VengefulSpiritEntity
import org.teamvoided.dusk_debris.world.gen.configured_feature.config.rock_spires.RockFormationFeatureConfig

class VengefulSpiritSpell(priority: Int = 10, coolup: Int = 8) : AbstractSpell(priority, coolup) {
    constructor(
        config: AbstractSpell,
    ) : this(
        config.priority,
        config.coolup
    )

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

        val CODEC: Codec<VengefulSpiritSpell> =
            RecordCodecBuilder.create { instance ->
                instance.group(
                    AbstractSpell.MAP_CODEC.forGetter { it }
                ).apply(instance, ::VengefulSpiritSpell)
            }
    }
}