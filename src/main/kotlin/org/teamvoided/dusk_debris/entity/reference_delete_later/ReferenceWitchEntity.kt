package org.teamvoided.dusk_debris.entity.reference_delete_later

import net.minecraft.core.Holder
import net.minecraft.core.component.DataComponents
import net.minecraft.core.particles.ParticleTypes
import net.minecraft.network.syncher.EntityDataAccessor
import net.minecraft.network.syncher.EntityDataSerializers
import net.minecraft.network.syncher.SynchedEntityData
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvent
import net.minecraft.sounds.SoundEvents
import net.minecraft.tags.DamageTypeTags
import net.minecraft.tags.FluidTags
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.effect.MobEffects
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.ai.attributes.AttributeModifier
import net.minecraft.world.entity.ai.attributes.AttributeSupplier
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.entity.ai.goal.*
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal
import net.minecraft.world.entity.ai.goal.target.NearestAttackableWitchTargetGoal
import net.minecraft.world.entity.ai.goal.target.NearestHealableRaiderTargetGoal
import net.minecraft.world.entity.monster.Monster
import net.minecraft.world.entity.monster.RangedAttackMob
import net.minecraft.world.entity.player.Player
import net.minecraft.world.entity.projectile.ThrownPotion
import net.minecraft.world.entity.raid.Raider
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.alchemy.Potion
import net.minecraft.world.item.alchemy.PotionContents
import net.minecraft.world.item.alchemy.Potions
import net.minecraft.world.level.Level
import net.minecraft.world.level.gameevent.GameEvent
import java.util.function.Predicate
import kotlin.math.sqrt

class ReferenceWitchEntity(entityType: EntityType<out ReferenceWitchEntity?>?, world: Level?) :
    Raider(entityType, world), RangedAttackMob {
    private var drinkTimeLeft = 0
    private var raidGoal: NearestHealableRaiderTargetGoal<Raider?>? = null
    private var attackPlayerGoal: NearestAttackableWitchTargetGoal<Player?>? = null

    override fun registerGoals() {
        super.registerGoals()
        this.raidGoal = NearestHealableRaiderTargetGoal(
            this,
            Raider::class.java,
            true,
            Predicate { entity: Any? -> entity != null && this.hasActiveRaid() && entity !== EntityType.WITCH })
        this.attackPlayerGoal =
            NearestAttackableWitchTargetGoal(this, Player::class.java, 10, true, false, null)
        goalSelector.addGoal(1, FloatGoal(this))
        goalSelector.addGoal(2, RangedAttackGoal(this, 1.0, 60, 10.0f))
        goalSelector.addGoal(2, WaterAvoidingRandomStrollGoal(this, 1.0))
        goalSelector.addGoal(3, LookAtPlayerGoal(this, Player::class.java, 8.0f))
        goalSelector.addGoal(3, RandomLookAroundGoal(this))
        targetSelector.addGoal(
            1, HurtByTargetGoal(
                this, *arrayOf<Class<*>>(
                    Raider::class.java
                )
            )
        )
        targetSelector.addGoal(2, this.raidGoal)
        targetSelector.addGoal(3, this.attackPlayerGoal)
    }

    override fun defineSynchedData(builder: SynchedEntityData.Builder) {
        super.defineSynchedData(builder)
        builder.define(DRINKING, false)
    }

    override fun getAmbientSound(): SoundEvent? {
        return SoundEvents.WITCH_AMBIENT
    }

    override fun getHurtSound(source: DamageSource): SoundEvent? {
        return SoundEvents.WITCH_HURT
    }

    override fun getDeathSound(): SoundEvent? {
        return SoundEvents.WITCH_DEATH
    }

    var isDrinking: Boolean
        get() = entityData.get(DRINKING) as Boolean
        set(drinking) {
            entityData.set(DRINKING, drinking)
        }

    override fun aiStep() {
        if (!level().isClientSide && this.isAlive) {
            raidGoal!!.decrementCooldown()
            if (raidGoal!!.cooldown <= 0) {
                attackPlayerGoal!!.setCanAttack(true)
            } else {
                attackPlayerGoal!!.setCanAttack(false)
            }

            if (this.isDrinking) {
                if (drinkTimeLeft-- <= 0) {
                    this.isDrinking = false
                    val itemStack = this.mainHandItem
                    this.setItemSlot(EquipmentSlot.MAINHAND, ItemStack.EMPTY)
                    val potionContentsComponent = itemStack.get(DataComponents.POTION_CONTENTS)
                    if (itemStack.`is`(Items.POTION) && potionContentsComponent != null) {
                        potionContentsComponent.forEachEffect(::addEffect)
                    }

                    this.gameEvent(GameEvent.DRINK)
                    getAttribute(Attributes.MOVEMENT_SPEED)!!
                        .removeModifier(DRINKING_SPEED_PENALTY_MODIFIER.id())
                }
            } else {
                var holder: Holder<Potion?>? = null
                if (random.nextFloat() < 0.15f && this.isEyeInFluid(FluidTags.WATER) && !this.hasEffect(
                        MobEffects.WATER_BREATHING
                    )
                ) {
                    holder = Potions.WATER_BREATHING
                } else if (random.nextFloat() < 0.15f && (this.isOnFire || this.lastDamageSource != null && this.lastDamageSource!!
                        .`is`(DamageTypeTags.IS_FIRE)) && !this.hasEffect(MobEffects.FIRE_RESISTANCE)
                ) {
                    holder = Potions.FIRE_RESISTANCE
                } else if (random.nextFloat() < 0.05f && this.health < this.maxHealth) {
                    holder = Potions.HEALING
                } else if (random.nextFloat() < 0.5f && (this.target != null) && !this.hasEffect(MobEffects.MOVEMENT_SPEED) && (target!!
                        .distanceToSqr(this) > 121.0)
                ) {
                    holder = Potions.SWIFTNESS
                }

                if (holder != null) {
                    this.setItemSlot(EquipmentSlot.MAINHAND, PotionContents.createItemStack(Items.POTION, holder))
                    this.drinkTimeLeft = this.mainHandItem.getUseDuration(this)
                    this.isDrinking = true
                    if (!this.isSilent) {
                        level().playSound(
                            null as Player?,
                            this.x,
                            this.y,
                            this.z,
                            SoundEvents.WITCH_DRINK,
                            this.soundSource,
                            1.0f,
                            0.8f + random.nextFloat() * 0.4f
                        )
                    }

                    val entityAttributeInstance = this.getAttribute(Attributes.MOVEMENT_SPEED)
                    entityAttributeInstance!!.removeModifier(DRINKING_SPEED_MODIFIER_ID)
                    entityAttributeInstance.addTransientModifier(DRINKING_SPEED_PENALTY_MODIFIER)
                }
            }

            if (random.nextFloat() < 7.5E-4f) {
                level().broadcastEntityEvent(this, 15.toByte())
            }
        }

        super.aiStep()
    }

    override fun getCelebrateSound(): SoundEvent {
        return SoundEvents.WITCH_CELEBRATE
    }

    override fun handleEntityEvent(status: Byte) {
        if (status.toInt() == 15) {
            for (i in 0 until random.nextInt(35) + 10) {
                level().addParticle(
                    ParticleTypes.WITCH,
                    this.x + random.nextGaussian() * 0.12999999523162842,
                    this.boundingBox.maxY + 0.5 + (random.nextGaussian() * 0.12999999523162842),
                    this.z + random.nextGaussian() * 0.12999999523162842,
                    0.0,
                    0.0,
                    0.0
                )
            }
        } else {
            super.handleEntityEvent(status)
        }
    }

    override fun getDamageAfterMagicAbsorb(source: DamageSource, amount: Float): Float {
        var amount = amount
        amount = super.getDamageAfterMagicAbsorb(source, amount)
        if (source.entity === this) {
            amount = 0.0f
        }

        if (source.`is`(DamageTypeTags.WITCH_RESISTANT_TO)) {
            amount *= 0.15f
        }

        return amount
    }

    override fun performRangedAttack(target: LivingEntity, pullProgress: Float) {
        if (!this.isDrinking) {
            val vec3d = target.deltaMovement
            val d = target.x + vec3d.x - this.x
            val e = target.eyeY - 1.100000023841858 - this.y
            val f = target.z + vec3d.z - this.z
            val g = sqrt(d * d + f * f)
            var holder = Potions.HARMING
            if (target is Raider) {
                holder = if (target.getHealth() <= 4.0f) {
                    Potions.HEALING
                } else {
                    Potions.REGENERATION
                }

                this.target = null as LivingEntity?
            } else if (g >= 8.0 && !target.hasEffect(MobEffects.MOVEMENT_SLOWDOWN)) {
                holder = Potions.SLOWNESS
            } else if (target.health >= 8.0f && !target.hasEffect(MobEffects.POISON)) {
                holder = Potions.POISON
            } else if (g <= 3.0 && !target.hasEffect(MobEffects.WEAKNESS) && random.nextFloat() < 0.25f) {
                holder = Potions.WEAKNESS
            }

            val potionEntity = ThrownPotion(this.level(), this)
            potionEntity.setItem(PotionContents.createItemStack(Items.SPLASH_POTION, holder))
            potionEntity.xRot -= -20.0f
            potionEntity.shoot(d, e + g * 0.2, f, 0.75f, 8.0f)
            if (!this.isSilent) {
                level().playSound(
                    null as Player?,
                    this.x,
                    this.y,
                    this.z,
                    SoundEvents.WITCH_THROW,
                    this.soundSource,
                    1.0f,
                    0.8f + random.nextFloat() * 0.4f
                )
            }

            level().addFreshEntity(potionEntity)
        }
    }

    override fun applyRaidBuffs(world: ServerLevel, wave: Int, unused: Boolean) {
    }

    override fun canBeLeader(): Boolean {
        return false
    }

    companion object {
        private val DRINKING_SPEED_MODIFIER_ID: ResourceLocation = ResourceLocation.withDefaultNamespace("drinking")
        private val DRINKING_SPEED_PENALTY_MODIFIER =
            AttributeModifier(DRINKING_SPEED_MODIFIER_ID, -0.25, AttributeModifier.Operation.ADD_VALUE)
        private val DRINKING: EntityDataAccessor<Boolean> = SynchedEntityData.defineId(
            ReferenceWitchEntity::class.java, EntityDataSerializers.BOOLEAN
        )

        fun createAttributes(): AttributeSupplier.Builder {
            return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 26.0)
                .add(Attributes.MOVEMENT_SPEED, 0.25)
        }
    }
}