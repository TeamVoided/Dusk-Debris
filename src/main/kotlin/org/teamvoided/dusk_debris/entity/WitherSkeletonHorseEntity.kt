package org.teamvoided.dusk_debris.entity

import net.minecraft.core.BlockPos
import net.minecraft.util.RandomSource
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.effect.MobEffects
import net.minecraft.world.entity.*
import net.minecraft.world.entity.ai.attributes.AttributeSupplier
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.entity.animal.Animal
import net.minecraft.world.entity.animal.horse.SkeletonHorse
import net.minecraft.world.level.Level
import net.minecraft.world.level.LevelAccessor
import org.teamvoided.dusk_debris.entity.ai.goal.WitherSkeletonHorseTrapTriggerGoal
import org.teamvoided.dusk_debris.init.DuskEntities
import java.util.*

class WitherSkeletonHorseEntity : SkeletonHorse {
    constructor(
        entityType: EntityType<out WitherSkeletonHorseEntity>,
        world: Level
    ) : super(entityType, world)

    private val witherTrapTriggerGoal = WitherSkeletonHorseTrapTriggerGoal(this)
    override fun randomizeAttributes(random: RandomSource) {
        var var10000 = this.getAttribute(Attributes.MAX_HEALTH)
        Objects.requireNonNull(random)
        var10000!!.baseValue = generateMaxHealth(random::nextInt).toDouble()
        var10000 = this.getAttribute(Attributes.MOVEMENT_SPEED)
        Objects.requireNonNull(random)
        var10000!!.baseValue = generateSpeed(random::nextDouble)
        var10000 = this.getAttribute(Attributes.JUMP_STRENGTH)
        Objects.requireNonNull(random)
        var10000!!.baseValue = generateJumpStrength(random::nextDouble)
    }

    override fun setTrap(trapped: Boolean) {
        if (trapped != this.isTrap) {
            this.isTrap = trapped
            if (trapped) {
                goalSelector.addGoal(1, this.witherTrapTriggerGoal)
            } else {
                goalSelector.removeGoal(this.witherTrapTriggerGoal)
            }
        }
    }

//    override fun level(): Level = this.level()

    override fun doHurtTarget(target: Entity?): Boolean {
        if (!super.doHurtTarget(target)) {
            return false
        } else {
            if (target is LivingEntity) {
                target.addEffect(MobEffectInstance(MobEffects.WITHER, 200), this)
            }
            return true
        }
    }

//    override fun getAmbientSound(): SoundEvent {
//        return if (this.isSubmergedIn(FluidTags.WATER)) SoundEvents.SKELETON_HORSE_AMBIENT_WATER
//        else SoundEvents.SKELETON_HORSE_AMBIENT
//    }
//
//    override fun getDeathSound(): SoundEvent {
//        return SoundEvents.SKELETON_HORSE_DEATH
//    }
//
//    override fun getHurtSound(source: DamageSource?): SoundEvent {
//        return SoundEvents.SKELETON_HORSE_HURT
//    }
//
//    override fun getSwimSound(): SoundEvent {
//        if (this.isOnGround) {
//            if (!this.hasPassengers()) {
//                return SoundEvents.SKELETON_HORSE_STEP_WATER
//            }
//
//            ++this.soundTicks
//            if (this.soundTicks > 5 && this.soundTicks % 3 == 0) {
//                return SoundEvents.SKELETON_HORSE_GALLOP_WATER
//            }
//
//            if (this.soundTicks <= 5) {
//                return SoundEvents.SKELETON_HORSE_STEP_WATER
//            }
//        }
//
//        return SoundEvents.SKELETON_HORSE_SWIM
//    }
//
//    override fun playJumpSound() {
//        if (this.isTouchingWater) {
//            this.playSound(SoundEvents.SKELETON_HORSE_JUMP_WATER, 0.4f, 1.0f)
//        } else {
//            super.playJumpSound()
//        }
//    }

    override fun getDefaultDimensions(pose: Pose?): EntityDimensions {
        return if (this.isBaby) BABY_DIMENSIONS else super.getDefaultDimensions(pose)
    }

    companion object {
        val BABY_DIMENSIONS = DuskEntities.WITHER_SKELETON_HORSE.dimensions
            .withAttachments(
                EntityAttachments.builder()
                    .attach(
                        EntityAttachment.PASSENGER,
                        0.0f,
                        DuskEntities.WITHER_SKELETON_HORSE.height - 0.03125f,
                        0.0f
                    )
            ).scale(0.5f)

        fun createAttributes(): AttributeSupplier.Builder {
            return createBaseHorseAttributes()
        }

        fun canSpawn(
            type: EntityType<out Animal>,
            world: LevelAccessor,
            reason: MobSpawnType,
            pos: BlockPos,
            random: RandomSource
        ): Boolean {
            return if (!MobSpawnType.isSpawner(reason)) {
                checkAnimalSpawnRules(type, world, reason, pos, random)
            } else {
                MobSpawnType.ignoresLightRequirements(reason) || isBrightEnoughToSpawn(world, pos)
            }
        }
    }

}