package org.teamvoided.dusk_debris.entity

import net.minecraft.core.BlockPos
import net.minecraft.core.Holder
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.syncher.EntityDataAccessor
import net.minecraft.network.syncher.EntityDataSerializers
import net.minecraft.network.syncher.SynchedEntityData
import net.minecraft.sounds.SoundEvent
import net.minecraft.sounds.SoundEvents
import net.minecraft.util.RandomSource
import net.minecraft.world.DifficultyInstance
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.effect.MobEffect
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.effect.MobEffects
import net.minecraft.world.entity.*
import net.minecraft.world.entity.ai.attributes.AttributeSupplier
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.entity.ai.goal.*
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal
import net.minecraft.world.entity.animal.Turtle
import net.minecraft.world.entity.monster.AbstractSkeleton
import net.minecraft.world.entity.monster.Monster
import net.minecraft.world.entity.player.Player
import net.minecraft.world.entity.projectile.AbstractArrow
import net.minecraft.world.entity.projectile.Arrow
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.level.Level
import net.minecraft.world.level.ServerLevelAccessor
import org.teamvoided.dusk_debris.data.tags.DuskDamageTypeTags
import org.teamvoided.dusk_debris.data.tags.DuskEntityTypeTags
import org.teamvoided.dusk_debris.entity.ai.goal.EnterDarknessGoal
import java.awt.Color

class GloomEntity(entityType: EntityType<out GloomEntity>, world: Level) :
    AbstractSkeleton(entityType, world) {
    var darkModeTransitionTime: Int = 0

    override fun registerGoals() {
        goalSelector.addGoal(3, RestrictSunGoal(this))
        goalSelector.addGoal(3, EnterDarknessGoal(this, 1.0, lightThreshold))
        goalSelector.addGoal(
            3, AvoidEntityGoal(
                this, LivingEntity::class.java, 6.0f, 1.0, 1.2
            ) { it.type.`is`(DuskEntityTypeTags.DUSK_SKELETON_RETREATS) })
        goalSelector.addGoal(5, WaterAvoidingRandomStrollGoal(this, 1.0))
        goalSelector.addGoal(
            6, LookAtPlayerGoal(
                this, Player::class.java, 8.0f
            )
        )
        goalSelector.addGoal(6, RandomLookAroundGoal(this))
        targetSelector.addGoal(1, HurtByTargetGoal(this, *arrayOfNulls(0)))
        targetSelector.addGoal(
            2, NearestAttackableTargetGoal(
                this, Player::class.java, true
            )
        )
        targetSelector.addGoal(
            3,
            NearestAttackableTargetGoal(this, LivingEntity::class.java, true) { it.type.`is`(DuskEntityTypeTags.DUSK_SKELETON_ATTACKS) })
        targetSelector.addGoal(
            3, NearestAttackableTargetGoal(
                this, Turtle::class.java, 10, true, false, Turtle.BABY_ON_LAND_SELECTOR
            )
        )
    }

    override fun defineSynchedData(builder: SynchedEntityData.Builder) {
        super.defineSynchedData(builder)
        builder
            .define(CONVERTING_TO_STRAY, false)
            .define(CONVERTING_TO_DARK_MODE, false)
            .define(EYE_COLOR, eyeColorDefault)
    }

    override fun readAdditionalSaveData(nbt: CompoundTag) {
        super.readAdditionalSaveData(nbt)
        if (nbt.contains(STRAY_CONVERSION_TIME_KEY, 99) && nbt.getInt(STRAY_CONVERSION_TIME_KEY) > -1)
            setConversionToStrayTime(nbt.getInt(STRAY_CONVERSION_TIME_KEY))
        if (nbt.contains(MODE_CONVERSION_TIME_KEY, 99) && nbt.getInt(MODE_CONVERSION_TIME_KEY) > -1)
            countdownToDarkMode = nbt.getInt(MODE_CONVERSION_TIME_KEY)
        if (nbt.contains(EYE_COLOR_KEY))
            this.eyeColor = nbt.getInt(EYE_COLOR_KEY)
    }

    override fun addAdditionalSaveData(nbt: CompoundTag) {
        super.addAdditionalSaveData(nbt)
        nbt.putInt(STRAY_CONVERSION_TIME_KEY, if (isConvertingToStray()) conversionToStrayTime else -1)
        nbt.putInt(MODE_CONVERSION_TIME_KEY, if (isLightMode()) countdownToDarkMode else -1)
        nbt.putInt(EYE_COLOR_KEY, this.eyeColor)
    }

    override fun tick() {
        if (!level().isClientSide && isAlive && !isNoAi) {
            if (isInPowderSnow) {
                if (isConvertingToStray()) {
                    --conversionToStrayTime
                    if (conversionToStrayTime < 0) {
                        convertToStray()
                    }
                } else {
                    ++inPowderSnowTime
                    if (inPowderSnowTime >= 140) {
                        setConversionToStrayTime(STRAY_CONVERSION_TIME)
                    }
                }
            } else {
                inPowderSnowTime = -1
                setConvertingToStray(false)
            }
            if (inDarkness()) {
                if (isLightMode()) {
                    if (countdownToDarkMode > 0) {
                        --countdownToDarkMode
                    } else {
                        this.addEffect(MobEffectInstance(MobEffects.DARKNESS, 60), this)
                        setConvertingToDarkMode(false)
                        countdownToDarkMode = -1
                    }
                }
            } else if (countdownToDarkMode <= 240 || !isLightMode()) {
//                getAttributeInstance(EntityAttributes.MOVEMENT_SPEED)!!.removeModifier(STUNNED_MOVEMENT_PENALTY_MODIFIER.id())
                if (!isLightMode()) {
//          found some funky things in the witch file, no idea why they are the most complex mob with such a small file :)
//                    val entityAttributeInstance = this.getAttributeInstance(EntityAttributes.MOVEMENT_SPEED)
//                    entityAttributeInstance!!.removeModifier(stunned)
//                    entityAttributeInstance.addTemporaryModifier(STUNNED_MOVEMENT_PENALTY_MODIFIER)
                    this.addEffect(MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 60, 255), this)
                }
                setConvertingToDarkMode(true)
                countdownToDarkMode = LIGHT_MODE_TIME

            }
        } else if (level().isClientSide) {
            if (isLightMode()) {
                if (darkModeTransitionTime < 60)
                    darkModeTransitionTime++
            } else if (darkModeTransitionTime >= 0) {
                darkModeTransitionTime--
            }
        }
        super.tick()
    }

    //Become Skeleton
    fun isConvertingToStray(): Boolean {
        return entityData.get(CONVERTING_TO_STRAY)
    }

    fun setConvertingToStray(converting: Boolean) {
        entityData.set(CONVERTING_TO_STRAY, converting)
    }


    private fun setConversionToStrayTime(time: Int) {
        conversionToStrayTime = time
        setConvertingToStray(true)
    }

    protected fun convertToStray() {
        convertTo(EntityType.SKELETON, true)
        if (!isSilent) {
            level().levelEvent(null as Player?, 1048, blockPosition(), 0)
        }
    }

    //Become Edgy

    fun isLightMode(): Boolean {
        return entityData.get(CONVERTING_TO_DARK_MODE)
    }

    fun setConvertingToDarkMode(converting: Boolean) {
        entityData.set(CONVERTING_TO_DARK_MODE, converting)
    }

    var eyeColor: Int
        get() = entityData.get(EYE_COLOR)
        set(color) = entityData.set(EYE_COLOR, color)


    fun inDarkness(): Boolean {
        return level().getMaxLocalRawBrightness(blockPosition()) < lightThreshold
    }

    //Other things

    override fun isShaking(): Boolean {
        return isConvertingToStray()
    }

    override fun canFreeze(): Boolean {
        return false
    }

    override fun isSunBurnTick(): Boolean {
        return false
    }

    override fun dampensVibrations(): Boolean {
//        line 373 of the WardenEntity.class to make wardens not anger at this entity
        return !isLightMode()
    }

    override fun getAmbientSound(): SoundEvent {
        return SoundEvents.STRAY_AMBIENT
    }

    override fun getHurtSound(source: DamageSource): SoundEvent {
        return SoundEvents.STRAY_HURT
    }

    override fun getDeathSound(): SoundEvent {
        return SoundEvents.STRAY_DEATH
    }

    override fun getStepSound(): SoundEvent {
        return SoundEvents.STRAY_STEP
    }

    override fun getDamageAfterMagicAbsorb(source: DamageSource, amount: Float): Float {
        var damage = amount
        if (!isLightMode() && !source.`is`(DuskDamageTypeTags.BYPASSES_GLOOM_RESISTANCE)) {
            damage *= 0.01f
        }
        return super.getDamageAfterMagicAbsorb(source, damage)
    }


    override fun doHurtTarget(target: Entity): Boolean {
        if (!super.doHurtTarget(target)) {
            return false
        } else {
            if (target is LivingEntity && !isLightMode()) {
                target.addEffect(MobEffectInstance(statusEffect, 200), this)
            }
            return true
        }
    }

    override fun getArrow(
        itemStack: ItemStack,
        f: Float,
        itemStack2: ItemStack?
    ): AbstractArrow {
        val persistentProjectileEntity = super.getArrow(itemStack, f, itemStack2)
        if (persistentProjectileEntity is Arrow) {
            persistentProjectileEntity.addEffect(MobEffectInstance(statusEffect, 600))
        }
        return persistentProjectileEntity
    }

    override fun populateDefaultEquipmentSlots(random: RandomSource, difficulty: DifficultyInstance) {
        val weaponMaterial = random.nextFloat()
        val weaponTypeAxe = random.nextInt(25) == 0
        if (weaponMaterial > 0.95) {
            if (weaponTypeAxe) {
                this.setItemSlot(EquipmentSlot.MAINHAND, ItemStack(Items.IRON_AXE))
            } else {
                this.setItemSlot(EquipmentSlot.MAINHAND, ItemStack(Items.IRON_SWORD))
            }
//        } else if (weaponMaterial > 0.85) {
//            if (weaponTypeAxe) {
//                this.equipStack(EquipmentSlot.MAINHAND, ItemStack(DuskItems.BLACKSTONE_AXE))
//            } else {
//                this.equipStack(EquipmentSlot.MAINHAND, ItemStack(DuskItems.BLACKSTONE_SWORD))
//            }
        } else if (weaponMaterial > 0.5) {
            if (weaponTypeAxe) {
                this.setItemSlot(EquipmentSlot.MAINHAND, ItemStack(Items.STONE_AXE))
            } else {
                this.setItemSlot(EquipmentSlot.MAINHAND, ItemStack(Items.STONE_SWORD))
            }
        } else {
            this.setItemSlot(EquipmentSlot.MAINHAND, ItemStack(Items.BOW))
        }
    }

    companion object {
        private const val lightThreshold: Int = 10
        val statusEffect: Holder<MobEffect> = MobEffects.DARKNESS

        private val CONVERTING_TO_STRAY: EntityDataAccessor<Boolean> =
            SynchedEntityData.defineId(GloomEntity::class.java, EntityDataSerializers.BOOLEAN)
        const val STRAY_CONVERSION_TIME_KEY: String = "StrayConversionTime"
        private const val STRAY_CONVERSION_TIME = 300
        private var inPowderSnowTime = 0
        private var conversionToStrayTime = 0

        private val CONVERTING_TO_DARK_MODE: EntityDataAccessor<Boolean> =
            SynchedEntityData.defineId(GloomEntity::class.java, EntityDataSerializers.BOOLEAN)
        const val MODE_CONVERSION_TIME_KEY: String = "LightModeTime"
        private const val LIGHT_MODE_TIME = 300
        private var countdownToDarkMode = -1
//        val stunned: Identifier = Identifier.ofDefault("stunned")
//        val STUNNED_MOVEMENT_PENALTY_MODIFIER =
//            EntityAttributeModifier(stunned, -0.25, EntityAttributeModifier.Operation.ADD_VALUE)

        private val EYE_COLOR: EntityDataAccessor<Int> =
            SynchedEntityData.defineId(GloomEntity::class.java, EntityDataSerializers.INT)
        val EYE_COLOR_KEY: String = "EyeColor"
        private var eyeColorDefault: Int = Color(217, 230, 244).rgb


        fun createAttributes(): AttributeSupplier.Builder {
            return Monster.createMonsterAttributes()
                .add(Attributes.MOVEMENT_SPEED, 0.25)
        }

        fun canSpawn(
            type: EntityType<GloomEntity>,
            world: ServerLevelAccessor,
            spawnReason: MobSpawnType,
            pos: BlockPos,
            random: RandomSource
        ): Boolean {
            return checkMonsterSpawnRules(type, world, spawnReason, pos, random) ||
                    (MobSpawnType.isSpawner(spawnReason))
        }
    }
}