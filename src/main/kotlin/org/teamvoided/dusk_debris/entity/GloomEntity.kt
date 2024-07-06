package org.teamvoided.dusk_debris.entity

import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.SpawnReason
import net.minecraft.entity.ai.goal.*
import net.minecraft.entity.attribute.DefaultAttributeContainer
import net.minecraft.entity.damage.DamageSource
import net.minecraft.entity.data.DataTracker
import net.minecraft.entity.data.TrackedData
import net.minecraft.entity.data.TrackedDataHandlerRegistry
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.entity.effect.StatusEffects
import net.minecraft.entity.mob.AbstractSkeletonEntity
import net.minecraft.entity.mob.MobEntity
import net.minecraft.entity.passive.TurtleEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.projectile.ArrowEntity
import net.minecraft.entity.projectile.PersistentProjectileEntity
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtCompound
import net.minecraft.sound.SoundEvent
import net.minecraft.sound.SoundEvents
import net.minecraft.util.math.BlockPos
import net.minecraft.util.random.RandomGenerator
import net.minecraft.world.ServerWorldAccess
import net.minecraft.world.World
import org.teamvoided.dusk_debris.data.DuskEntityTypeTags
import org.teamvoided.dusk_debris.entity.ai.goal.EnterDarknessGoal

class GloomEntity(entityType: EntityType<out GloomEntity>, world: World) :
    AbstractSkeletonEntity(entityType, world) {
    override fun initGoals() {
//        goalSelector.add(1, StunThisEntity(this, time, criteria))
        goalSelector.add(2, AvoidSunlightGoal(this))
        goalSelector.add(3, EnterDarknessGoal(this, 1.0, lightThreshold))
        goalSelector.add(
            3, FleeEntityGoal(
                this,
                LivingEntity::class.java, 6.0f, 1.0, 1.2
            ) { entity -> entity.type.isIn(DuskEntityTypeTags.DUSK_SKELETON_RETREATS) })
        goalSelector.add(5, WanderAroundFarGoal(this, 1.0))
        goalSelector.add(
            6, LookAtEntityGoal(
                this,
                PlayerEntity::class.java, 8.0f
            )
        )
        goalSelector.add(6, LookAroundGoal(this))
        targetSelector.add(1, RevengeGoal(this, *arrayOfNulls(0)))
        targetSelector.add(
            2, TargetGoal(
                this,
                PlayerEntity::class.java, true
            )
        )
        targetSelector.add(
            3, TargetGoal(
                this,
                LivingEntity::class.java, true
            ) { entity -> entity.type.isIn(DuskEntityTypeTags.DUSK_SKELETON_ATTACKS) })
        targetSelector.add(
            3, TargetGoal(
                this,
                TurtleEntity::class.java, 10, true, false, TurtleEntity.BABY_TURTLE_ON_LAND_FILTER
            )
        )
    }

    override fun initDataTracker(builder: DataTracker.Builder) {
        println("initDataTracker")
        super.initDataTracker(builder)
        builder
            .add(CONVERTING_TO_STRAY, false)
            .add(DARK_MODE, true)
    }

    override fun writeCustomDataToNbt(nbt: NbtCompound) {
        println("writeCustomDataToNbt")
        super.writeCustomDataToNbt(nbt)
        nbt.putInt(STRAY_CONVERSION_TIME_KEY, if (isConvertingToStray()) conversionToStrayTime else -1)
        nbt.putInt(
            DARK_MODE_CONVERSION_TIME_KEY,
            if (isDarkMode()) conversionToDarkModeTime else -1
        )
    }

    override fun readCustomDataFromNbt(nbt: NbtCompound) {
        println("readCustomDataFromNbt")
        super.readCustomDataFromNbt(nbt)
        if (nbt.contains(STRAY_CONVERSION_TIME_KEY, 99) && nbt.getInt(STRAY_CONVERSION_TIME_KEY) > -1) {
            setConversionToStrayTime(nbt.getInt(STRAY_CONVERSION_TIME_KEY))
        }
        if (nbt.contains(DARK_MODE_CONVERSION_TIME_KEY, 99) && nbt.getInt(DARK_MODE_CONVERSION_TIME_KEY) > -1) {
            setConversionToStrayTime(nbt.getInt(DARK_MODE_CONVERSION_TIME_KEY))
        }
    }

    override fun tick() {
        if (!world.isClient && isAlive && !isAiDisabled) {
            if (inPowderSnow) {
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
            if (inDarkness() && !isOnFire) {
                if (conversionToDarkModeTime > 0) {
                    --conversionToDarkModeTime
                } else {
                    conversionToDarkModeTime = -1
                    convertToDarkMode()
                }
            } else if (conversionToDarkModeTime <= 140) {
                conversionToDarkModeTime = LIGHT_MODE_TIME
                if (isDarkMode()) {
                    convertToLightMode()
                }
            }
        }

        super.tick()
    }

    fun isConvertingToStray(): Boolean {
        return getDataTracker().get(CONVERTING_TO_STRAY)
    }

    fun setConvertingToStray(converting: Boolean) {
        dataTracker.set(CONVERTING_TO_STRAY, converting)
    }


    private fun setConversionToStrayTime(time: Int) {
        conversionToStrayTime = time
        setConvertingToStray(true)
    }

    protected fun convertToStray() {
        convertTo(EntityType.STRAY, true)
        if (!isSilent) {
            world.syncWorldEvent(null as PlayerEntity?, 1048, blockPos, 0)
        }
    }

    fun isDarkMode(): Boolean {
        return getDataTracker().get(DARK_MODE)
    }

    protected fun convertToDarkMode() {
        dataTracker.set(DARK_MODE, true)
    }

    protected fun convertToLightMode() {
        dataTracker.set(DARK_MODE, false)
    }

    fun inDarkness(): Boolean {
        return world.getLightLevel(blockPos) < lightThreshold
    }

    override fun isShaking(): Boolean {
        return isConvertingToStray() || conversionToDarkModeTime > LIGHT_MODE_TIME - 10
    }

    override fun canFreeze(): Boolean {
        return false
    }

    override fun getAmbientSound(): SoundEvent {
        return SoundEvents.ENTITY_STRAY_AMBIENT
    }

    override fun getHurtSound(source: DamageSource): SoundEvent {
        return SoundEvents.ENTITY_STRAY_HURT
    }

    override fun getDeathSound(): SoundEvent {
        return SoundEvents.ENTITY_STRAY_DEATH
    }

    override fun getStepSound(): SoundEvent {
        return SoundEvents.ENTITY_STRAY_STEP
    }

    override fun tryAttack(target: Entity): Boolean {
        if (!super.tryAttack(target)) {
            return false
        } else {
            if (target is LivingEntity && isDarkMode()) {
                target.addStatusEffect(StatusEffectInstance(statusEffect, 200), this)
            }

            return true
        }
    }

    override fun method_6996(itemStack: ItemStack, f: Float, itemStack2: ItemStack?): PersistentProjectileEntity {
        val persistentProjectileEntity = super.method_6996(itemStack, f, itemStack2)
        if (persistentProjectileEntity is ArrowEntity && isDarkMode()) {
            persistentProjectileEntity.addEffect(StatusEffectInstance(statusEffect, 600))
        }
        return persistentProjectileEntity
    }

    companion object {
        private val lightThreshold = 10
        val statusEffect = StatusEffects.DARKNESS

        private val CONVERTING_TO_STRAY: TrackedData<Boolean> =
            DataTracker.registerData(GloomEntity::class.java, TrackedDataHandlerRegistry.BOOLEAN)
        val STRAY_CONVERSION_TIME_KEY: String = "StrayConversionTime"
        private val STRAY_CONVERSION_TIME = 300
        private var inPowderSnowTime = 0
        private var conversionToStrayTime = 0

        private val DARK_MODE: TrackedData<Boolean> =
            DataTracker.registerData(GloomEntity::class.java, TrackedDataHandlerRegistry.BOOLEAN)
        val DARK_MODE_CONVERSION_TIME_KEY: String = "DarkModeConversionTime"
        private val LIGHT_MODE_TIME = 300
        private var conversionToDarkModeTime = 0

//        private val EYE_COLOR: TrackedData<Int> =
//            DataTracker.registerData(GloomEntity::class.java, TrackedDataHandlerRegistry.INTEGER)
//        val EYE_COLOR_KEY: String = "EyeColor"
//        private var eyeColorDefault: Int = Color(217, 230, 244).rgb

        fun createAttributes(): DefaultAttributeContainer.Builder {
            return MobEntity.createAttributes()
        }

        fun canSpawn(
            type: EntityType<GloomEntity>,
            world: ServerWorldAccess,
            spawnReason: SpawnReason,
            pos: BlockPos,
            random: RandomGenerator
        ): Boolean {
            return canSpawnInDark(type, world, spawnReason, pos, random) ||
                    (SpawnReason.isSpawner(spawnReason))
        }
    }
}