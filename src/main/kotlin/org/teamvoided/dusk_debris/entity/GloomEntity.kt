package org.teamvoided.dusk_debris.entity

import net.minecraft.entity.*
import net.minecraft.entity.ai.goal.*
import net.minecraft.entity.attribute.DefaultAttributeContainer
import net.minecraft.entity.attribute.EntityAttributes
import net.minecraft.entity.damage.DamageSource
import net.minecraft.entity.data.DataTracker
import net.minecraft.entity.data.TrackedData
import net.minecraft.entity.data.TrackedDataHandlerRegistry
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.entity.effect.StatusEffects
import net.minecraft.entity.mob.AbstractSkeletonEntity
import net.minecraft.entity.mob.HostileEntity
import net.minecraft.entity.passive.TurtleEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.projectile.ArrowEntity
import net.minecraft.entity.projectile.PersistentProjectileEntity
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.nbt.NbtCompound
import net.minecraft.sound.SoundEvent
import net.minecraft.sound.SoundEvents
import net.minecraft.util.math.BlockPos
import net.minecraft.util.random.RandomGenerator
import net.minecraft.world.LocalDifficulty
import net.minecraft.world.ServerWorldAccess
import net.minecraft.world.World
import org.teamvoided.dusk_debris.data.tags.DuskDamageTypeTags
import org.teamvoided.dusk_debris.data.tags.DuskEntityTypeTags
import org.teamvoided.dusk_debris.entity.ai.goal.EnterDarknessGoal
import org.teamvoided.dusk_debris.init.DuskItems

class GloomEntity(entityType: EntityType<out GloomEntity>, world: World) :
    AbstractSkeletonEntity(entityType, world) {


    override fun initGoals() {
        goalSelector.add(3, AvoidSunlightGoal(this))
        goalSelector.add(3, EnterDarknessGoal(this, 1.0, lightThreshold))
        goalSelector.add(
            3, FleeEntityGoal(
                this, LivingEntity::class.java, 6.0f, 1.0, 1.2
            ) { entity -> entity.type.isIn(DuskEntityTypeTags.DUSK_SKELETON_RETREATS) })
        goalSelector.add(5, WanderAroundFarGoal(this, 1.0))
        goalSelector.add(
            6, LookAtEntityGoal(
                this, PlayerEntity::class.java, 8.0f
            )
        )
        goalSelector.add(6, LookAroundGoal(this))
        targetSelector.add(1, RevengeGoal(this, *arrayOfNulls(0)))
        targetSelector.add(
            2, TargetGoal(
                this, PlayerEntity::class.java, true
            )
        )
        targetSelector.add(
            3, TargetGoal(
                this, LivingEntity::class.java, true
            ) { entity ->
                (entity.type.isIn(DuskEntityTypeTags.DUSK_SKELETON_ATTACKS))
            })
        targetSelector.add(
            3, TargetGoal(
                this, TurtleEntity::class.java, 10, true, false, TurtleEntity.BABY_TURTLE_ON_LAND_FILTER
            )
        )
    }

    override fun initDataTracker(builder: DataTracker.Builder) {
        super.initDataTracker(builder)
        builder
            .add(CONVERTING_TO_STRAY, false)
            .add(CONVERTING_TO_DARK_MODE, false)
    }

    override fun writeCustomDataToNbt(nbt: NbtCompound) {
        super.writeCustomDataToNbt(nbt)
        nbt.putInt(STRAY_CONVERSION_TIME_KEY, if (isConvertingToStray()) conversionToStrayTime else -1)
        nbt.putInt(MODE_CONVERSION_TIME_KEY, if (isLightMode()) countdownToDarkMode else -1)
    }

    override fun readCustomDataFromNbt(nbt: NbtCompound) {
        super.readCustomDataFromNbt(nbt)
        if (nbt.contains(STRAY_CONVERSION_TIME_KEY, 99) && nbt.getInt(STRAY_CONVERSION_TIME_KEY) > -1) {
            setConversionToStrayTime(nbt.getInt(STRAY_CONVERSION_TIME_KEY))
        }
        if (nbt.contains(MODE_CONVERSION_TIME_KEY, 99) && nbt.getInt(MODE_CONVERSION_TIME_KEY) > -1) {
            setConversionModeTime(nbt.getInt(MODE_CONVERSION_TIME_KEY))
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
            if (inDarkness()) {
                if (isLightMode()) {
                    if (countdownToDarkMode > 0) {
                        --countdownToDarkMode
                    } else {
                        this.addStatusEffect(StatusEffectInstance(StatusEffects.DARKNESS, 60), this)
                        setConvertingToDarkMode(false)
                        setConversionModeTime(-1)
                    }
                }
            } else if (countdownToDarkMode <= 240 || !isLightMode()) {
//                getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED)!!.removeModifier(STUNNED_MOVEMENT_PENALTY_MODIFIER.id())
                if (!isLightMode()) {
//          found some funky things in the witch file, no idea why they are the most complex mob with such a small file :)
//                    val entityAttributeInstance = this.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED)
//                    entityAttributeInstance!!.removeModifier(stunned)
//                    entityAttributeInstance.addTemporaryModifier(STUNNED_MOVEMENT_PENALTY_MODIFIER)
                    this.addStatusEffect(StatusEffectInstance(StatusEffects.SLOWNESS, 60, 255), this)
                }
                setConvertingToDarkMode(true)
                setConversionModeTime(LIGHT_MODE_TIME)

            }
        }
        super.tick()
    }

    //Become Skeleton
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
        convertTo(EntityType.SKELETON, true)
        if (!isSilent) {
            world.syncWorldEvent(null as PlayerEntity?, 1048, blockPos, 0)
        }
    }

    //Become Edgy

    fun isLightMode(): Boolean {
        return getDataTracker().get(CONVERTING_TO_DARK_MODE)
    }

    fun setConvertingToDarkMode(converting: Boolean) {
        dataTracker.set(CONVERTING_TO_DARK_MODE, converting)
    }

    private fun setConversionModeTime(time: Int) {
        countdownToDarkMode = time
    }

    fun inDarkness(): Boolean {
        return world.getLightLevel(blockPos) < lightThreshold
    }

    //Other things

    override fun isShaking(): Boolean {
        return isConvertingToStray()
    }

    override fun canFreeze(): Boolean {
        return false
    }

    override fun isAffectedByDaylight(): Boolean {
        return false
    }

    override fun dampensVibrations(): Boolean {
//        line 373 of the WardenEntity.class to make wardens not anger at this entity
        return !isLightMode()
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

    override fun applyEnchantmentsToDamage(source: DamageSource, amount: Float): Float {
        var damage = amount
        if (!isLightMode() && !source.isTypeIn(DuskDamageTypeTags.BYPASSES_GLOOM_RESISTANCE)) {
            damage *= 0.01f
        }
        return super.applyEnchantmentsToDamage(source, damage)
    }


    override fun tryAttack(target: Entity): Boolean {
        if (!super.tryAttack(target)) {
            return false
        } else {
            if (target is LivingEntity && !isLightMode()) {
                target.addStatusEffect(StatusEffectInstance(statusEffect, 200), this)
            }
            return true
        }
    }

    override fun createArrowProjectile(itemStack: ItemStack, f: Float, itemStack2: ItemStack?): PersistentProjectileEntity {
        val persistentProjectileEntity = super.createArrowProjectile(itemStack, f, itemStack2)
        if (persistentProjectileEntity is ArrowEntity) {
            persistentProjectileEntity.addEffect(StatusEffectInstance(statusEffect, 600))
        }
        return persistentProjectileEntity
    }

    override fun initEquipment(random: RandomGenerator, difficulty: LocalDifficulty) {
        val weaponMaterial = random.nextFloat()
        val weaponTypeAxe = random.nextInt(25) > 24
        if (weaponMaterial > 0.95) {
            if (weaponTypeAxe) {
                this.equipStack(EquipmentSlot.MAINHAND, ItemStack(Items.IRON_AXE))
            } else {
                this.equipStack(EquipmentSlot.MAINHAND, ItemStack(Items.IRON_SWORD))
            }
//        } else if (weaponMaterial > 0.85) {
//            if (weaponTypeAxe) {
//                this.equipStack(EquipmentSlot.MAINHAND, ItemStack(DuskItems.BLACKSTONE_AXE))
//            } else {
//                this.equipStack(EquipmentSlot.MAINHAND, ItemStack(DuskItems.BLACKSTONE_SWORD))
//            }
        } else if (weaponMaterial > 0.5) {
            if (weaponTypeAxe) {
                this.equipStack(EquipmentSlot.MAINHAND, ItemStack(Items.STONE_AXE))
            } else {
                this.equipStack(EquipmentSlot.MAINHAND, ItemStack(Items.STONE_SWORD))
            }
        } else {
            this.equipStack(EquipmentSlot.MAINHAND, ItemStack(Items.BOW))
        }
    }

    companion object {

        private const val lightThreshold = 10
        val statusEffect = StatusEffects.DARKNESS

        private val CONVERTING_TO_STRAY: TrackedData<Boolean> =
            DataTracker.registerData(GloomEntity::class.java, TrackedDataHandlerRegistry.BOOLEAN)
        const val STRAY_CONVERSION_TIME_KEY: String = "StrayConversionTime"
        private const val STRAY_CONVERSION_TIME = 300
        private var inPowderSnowTime = 0
        private var conversionToStrayTime = 0

        private val CONVERTING_TO_DARK_MODE: TrackedData<Boolean> =
            DataTracker.registerData(GloomEntity::class.java, TrackedDataHandlerRegistry.BOOLEAN)
        const val MODE_CONVERSION_TIME_KEY: String = "LightModeTime"
        private const val LIGHT_MODE_TIME = 300
        private var countdownToDarkMode = -1
//        val stunned: Identifier = Identifier.ofDefault("stunned")
//        val STUNNED_MOVEMENT_PENALTY_MODIFIER =
//            EntityAttributeModifier(stunned, -0.25, EntityAttributeModifier.Operation.ADD_VALUE)

//        private val EYE_COLOR: TrackedData<Int> =
//            DataTracker.registerData(GloomEntity::class.java, TrackedDataHandlerRegistry.INTEGER)
//        val EYE_COLOR_KEY: String = "EyeColor"
//        private var eyeColorDefault: Int = Color(217, 230, 244).rgb


        fun createAttributes(): DefaultAttributeContainer.Builder {
            return HostileEntity.createAttributes()
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.25)
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