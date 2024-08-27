package org.teamvoided.dusk_debris.entity

import net.minecraft.entity.*
import net.minecraft.entity.attribute.DefaultAttributeContainer
import net.minecraft.entity.attribute.EntityAttributes
import net.minecraft.entity.damage.DamageSource
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.entity.effect.StatusEffects
import net.minecraft.entity.mob.SkeletonHorseEntity
import net.minecraft.entity.passive.AnimalEntity
import net.minecraft.registry.tag.FluidTags
import net.minecraft.sound.SoundEvent
import net.minecraft.sound.SoundEvents
import net.minecraft.util.math.BlockPos
import net.minecraft.util.random.RandomGenerator
import net.minecraft.world.EntityView
import net.minecraft.world.World
import net.minecraft.world.WorldAccess
import org.teamvoided.dusk_debris.entity.ai.goal.WitherSkeletonHorseTrapTriggerGoal
import org.teamvoided.dusk_debris.init.DuskEntities
import java.util.*
import kotlin.math.min

class WitherSkeletonHorseEntity : SkeletonHorseEntity {
    constructor(
        entityType: EntityType<out WitherSkeletonHorseEntity>,
        world: World
    ) : super(entityType, world)

    private val witherTrapTriggerGoal = WitherSkeletonHorseTrapTriggerGoal(this)
    override fun initAttributes(random: RandomGenerator) {
        var var10000 = this.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH)
        Objects.requireNonNull(random)
        var10000!!.baseValue = generateMaxHealthBonus(random::nextInt).toDouble()
        var10000 = this.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED)
        Objects.requireNonNull(random)
        var10000!!.baseValue = generateSpeedBonus(random::nextDouble)
        var10000 = this.getAttributeInstance(EntityAttributes.GENERIC_JUMP_STRENGTH)
        Objects.requireNonNull(random)
        var10000!!.baseValue = generateJumpStrengthBonus(random::nextDouble)
    }

    override fun setTrapped(trapped: Boolean) {
        if (trapped != this.trapped) {
            this.trapped = trapped
            if (trapped) {
                goalSelector.add(1, this.witherTrapTriggerGoal)
            } else {
                goalSelector.remove(this.witherTrapTriggerGoal)
            }
        }
    }

    override fun getEntityView(): EntityView = this.world

    override fun tryAttack(target: Entity?): Boolean {
        if (!super.tryAttack(target)) {
            return false
        } else {
            if (target is LivingEntity) {
                target.addStatusEffect(StatusEffectInstance(StatusEffects.WITHER, 200), this)
            }
            return true
        }
    }

//    override fun getAmbientSound(): SoundEvent {
//        return if (this.isSubmergedIn(FluidTags.WATER)) SoundEvents.ENTITY_SKELETON_HORSE_AMBIENT_WATER
//        else SoundEvents.ENTITY_SKELETON_HORSE_AMBIENT
//    }
//
//    override fun getDeathSound(): SoundEvent {
//        return SoundEvents.ENTITY_SKELETON_HORSE_DEATH
//    }
//
//    override fun getHurtSound(source: DamageSource?): SoundEvent {
//        return SoundEvents.ENTITY_SKELETON_HORSE_HURT
//    }
//
//    override fun getSwimSound(): SoundEvent {
//        if (this.isOnGround) {
//            if (!this.hasPassengers()) {
//                return SoundEvents.ENTITY_SKELETON_HORSE_STEP_WATER
//            }
//
//            ++this.soundTicks
//            if (this.soundTicks > 5 && this.soundTicks % 3 == 0) {
//                return SoundEvents.ENTITY_SKELETON_HORSE_GALLOP_WATER
//            }
//
//            if (this.soundTicks <= 5) {
//                return SoundEvents.ENTITY_SKELETON_HORSE_STEP_WATER
//            }
//        }
//
//        return SoundEvents.ENTITY_SKELETON_HORSE_SWIM
//    }
//
//    override fun playJumpSound() {
//        if (this.isTouchingWater) {
//            this.playSound(SoundEvents.ENTITY_SKELETON_HORSE_JUMP_WATER, 0.4f, 1.0f)
//        } else {
//            super.playJumpSound()
//        }
//    }

    override fun getDefaultDimensions(pose: EntityPose?): EntityDimensions {
        return if (this.isBaby) BABY_DIMENSIONS else super.getDefaultDimensions(pose)
    }

    companion object {
        val BABY_DIMENSIONS = DuskEntities.WITHER_SKELETON_HORSE.dimensions
            .withAttachments(
                EntityAttachments.builder()
                    .attach(
                        EntityAttachmentType.PASSENGER,
                        0.0f,
                        DuskEntities.WITHER_SKELETON_HORSE.height - 0.03125f,
                        0.0f
                    )
            ).scaled(0.5f)

        fun createAttributes(): DefaultAttributeContainer.Builder {
            return createBaseAttributes()
        }

        fun canSpawn(
            type: EntityType<out AnimalEntity>,
            world: WorldAccess,
            reason: SpawnReason,
            pos: BlockPos,
            random: RandomGenerator
        ): Boolean {
            return if (!SpawnReason.isSpawner(reason)) {
                isValidNaturalSpawn(type, world, reason, pos, random)
            } else {
                SpawnReason.isTrialSpawner(reason) || isBrightEnoughForNaturalSpawn(world, pos)
            }
        }
    }

}