package org.teamvoided.dusk_debris.spell.type

import com.mojang.serialization.Codec
import net.minecraft.CrashReport
import net.minecraft.CrashReportCategory
import net.minecraft.ReportedException
import net.minecraft.core.BlockPos
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.ai.attributes.AttributeModifier
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.AABB
import net.minecraft.world.phys.Vec3
import org.teamvoided.dusk_debris.DuskDebris
import org.teamvoided.dusk_debris.spell.SpellType
import org.teamvoided.dusk_debris.spell.settings.GenericSpellSettings
import org.teamvoided.dusk_debris.util.spellController
import kotlin.math.abs

class DesolateDiveSpell(codec: Codec<GenericSpellSettings>) : SpellType<GenericSpellSettings>(codec) {

    override fun castRequirements(castor: LivingEntity, settings: GenericSpellSettings): Boolean {
        return if (castor is Player) castor.xRot < -45 && castor.isShiftKeyDown
        else true
    }

    override fun onCast(castor: LivingEntity, settings: GenericSpellSettings) {
        addGravity(castor, -castor.gravity)
        castor.setDeltaMovement(Vec3.ZERO)
        castor.hasImpulse = true
        castor.spellController.spellTicksLeft = settings.cooldown
    }

    override fun castTick(castor: LivingEntity, settings: GenericSpellSettings) {
        if (castor.spellController.spellTicksLeft < 9) {
            if (castor.spellController.spellTicksLeft == 8)
                actualSpell(castor, settings)
        } else if (castor.spellController.spellTicksLeft <= settings.cooldown - 4) {
            castor.resetFallDistance()
            addGravity(castor)
            castor.setDeltaMovement(castor.deltaMovement.multiply(0.0, 4.0, 0.0))
            castor.hasImpulse = true
            tryCheckDiveBlockActions(castor)
            if (castor.onGround())
                actualSpell(castor, settings)
        } else {
            castor.setDeltaMovement(castor.deltaMovement.scale(0.0))
            castor.hasImpulse = true
        }
    }

    override fun onCastEnd(castor: LivingEntity, settings: GenericSpellSettings) {
        removeTempAttribute(castor)
    }

    override fun actualSpell(castor: LivingEntity, settings: GenericSpellSettings) {
        onCastEnd(castor, settings)
        castor.spellController.spellTicksLeft = 8
        val world = castor.level()
        val random = castor.random

        world.levelEvent(1501, castor.blockPosition(), 750)
        //repeat(10) {
        //    val particlePos = Vec3d(
        //        (random.nextDouble() - 0.5),
        //        (random.nextDouble() - 0.5),
        //        (random.nextDouble() - 0.5)
        //    ).add(castor.pos)
        //    world.addParticle(
        //        DuskParticles.DRAINED_SOUL,
        //        particlePos.x, particlePos.y, particlePos.z,
        //        (random.nextDouble() - 0.5), 0.0, (random.nextDouble() - 0.5)
        //    )
        //}
//
        //world.playSound(
        //    castor.x, castor.y, castor.z,
        //    SoundEvents.WITHER_AMBIENT,
        //    castor.soundCategory,
        //    0.2f,
        //    1.3f + random.nextFloat() * 0.4f,
        //    false
        //)
    }

    private fun addGravity(castor: LivingEntity, value: Double = 0.4) {
        val modifier =
            AttributeModifier(MODIFIER_ID, value, AttributeModifier.Operation.ADD_VALUE)
        val entityAttributeGravityInstance = castor.getAttribute(Attributes.GRAVITY)
        entityAttributeGravityInstance!!.removeModifier(MODIFIER_ID)
        entityAttributeGravityInstance.addTransientModifier(modifier)
    }

    private fun removeTempAttribute(
        castor: LivingEntity,
    ) = castor.getAttribute(Attributes.GRAVITY)!!.removeModifier(MODIFIER_ID)


    private fun tryCheckDiveBlockActions(castor: LivingEntity) {
        try {
            this.checkBlockCollision(castor)
        } catch (var4: Throwable) {
            val crashReport = CrashReport.forThrowable(var4, "Checking entity block collision for dive block logic")
            val crashReportSection = crashReport.addCategory("Entity being checked for dive block logic")
            castor.fillCrashReportCategory(crashReportSection)
            throw ReportedException(crashReport)
        }
    }

    private fun checkBlockCollision(castor: LivingEntity) {
        val box: AABB = castor.boundingBox
        val minY = box.minY - 0.5 - abs(castor.deltaMovement.y)
        val cornerMin = BlockPos.containing(box.minX - 0.5, minY, box.minZ - 0.5)
        val cornerMax = BlockPos.containing(box.maxX + 0.5, box.minY + 0.5, box.maxZ + 0.5)
        if (castor.level().hasChunksAt(cornerMin, cornerMax)) {
            val mutable = BlockPos.MutableBlockPos()
            for (x in cornerMin.x..cornerMax.x) {
                for (y in cornerMin.y..cornerMax.y) {
                    for (z in cornerMin.z..cornerMax.z) {
                        if (!castor.isAlive) return
                        mutable.set(x, y, z)
                        val blockState: BlockState = castor.level().getBlockState(mutable)
                        try {
                            blockState.entityInside(castor.level(), mutable, castor)
                            //castor.onBlockCollision(blockState)
                        } catch (throwable: Throwable) {
                            val crashReport = CrashReport.forThrowable(throwable, "Colliding entity with block")
                            val crashReportSection = crashReport.addCategory("Block being collided with")
                            CrashReportCategory.populateBlockDetails(crashReportSection, castor.level(), mutable, blockState)
                            throw ReportedException(crashReport)
                        }
                    }
                }
            }
        }
    }

    companion object {
        val MODIFIER_ID: ResourceLocation = DuskDebris.id("spell.desolate_dive")
    }
}