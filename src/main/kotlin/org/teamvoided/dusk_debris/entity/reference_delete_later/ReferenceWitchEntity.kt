package org.teamvoided.dusk_debris.entity.reference_delete_later

import net.minecraft.component.DataComponentTypes
import net.minecraft.component.type.PotionContentsComponent
import net.minecraft.entity.EntityType
import net.minecraft.entity.EquipmentSlot
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.ai.RangedAttackMob
import net.minecraft.entity.ai.goal.*
import net.minecraft.entity.attribute.DefaultAttributeContainer
import net.minecraft.entity.attribute.EntityAttributeModifier
import net.minecraft.entity.attribute.EntityAttributes
import net.minecraft.entity.damage.DamageSource
import net.minecraft.entity.data.DataTracker
import net.minecraft.entity.data.TrackedData
import net.minecraft.entity.data.TrackedDataHandlerRegistry
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.entity.effect.StatusEffects
import net.minecraft.entity.mob.HostileEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.projectile.thrown.PotionEntity
import net.minecraft.entity.raid.RaiderEntity
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.particle.ParticleTypes
import net.minecraft.potion.Potion
import net.minecraft.potion.Potions
import net.minecraft.registry.Holder
import net.minecraft.registry.tag.DamageTypeTags
import net.minecraft.registry.tag.FluidTags
import net.minecraft.server.world.ServerWorld
import net.minecraft.sound.SoundEvent
import net.minecraft.sound.SoundEvents
import net.minecraft.util.Identifier
import net.minecraft.world.World
import net.minecraft.world.event.GameEvent
import java.util.function.Consumer
import java.util.function.Predicate
import kotlin.math.sqrt

class ReferenceWitchEntity(entityType: EntityType<out ReferenceWitchEntity?>?, world: World?) :
    RaiderEntity(entityType, world), RangedAttackMob {
    private var drinkTimeLeft = 0
    private var raidGoal: RaidGoal<RaiderEntity?>? = null
    private var attackPlayerGoal: ToggleableTargetGoal<PlayerEntity?>? = null

    override fun initGoals() {
        super.initGoals()
        this.raidGoal = RaidGoal(
            this,
            RaiderEntity::class.java,
            true,
            Predicate { entity: Any? -> entity != null && this.hasActiveRaid() && entity !== EntityType.WITCH })
        this.attackPlayerGoal =
            ToggleableTargetGoal(this, PlayerEntity::class.java, 10, true, false, null)
        goalSelector.add(1, SwimGoal(this))
        goalSelector.add(2, ProjectileAttackGoal(this, 1.0, 60, 10.0f))
        goalSelector.add(2, WanderAroundFarGoal(this, 1.0))
        goalSelector.add(3, LookAtEntityGoal(this, PlayerEntity::class.java, 8.0f))
        goalSelector.add(3, LookAroundGoal(this))
        targetSelector.add(
            1, RevengeGoal(
                this, *arrayOf<Class<*>>(
                    RaiderEntity::class.java
                )
            )
        )
        targetSelector.add(2, this.raidGoal)
        targetSelector.add(3, this.attackPlayerGoal)
    }

    override fun initDataTracker(builder: DataTracker.Builder) {
        super.initDataTracker(builder)
        builder.add(DRINKING, false)
    }

    override fun getAmbientSound(): SoundEvent? {
        return SoundEvents.ENTITY_WITCH_AMBIENT
    }

    override fun getHurtSound(source: DamageSource): SoundEvent? {
        return SoundEvents.ENTITY_WITCH_HURT
    }

    override fun getDeathSound(): SoundEvent? {
        return SoundEvents.ENTITY_WITCH_DEATH
    }

    var isDrinking: Boolean
        get() = getDataTracker().get(DRINKING) as Boolean
        set(drinking) {
            getDataTracker().set(DRINKING, drinking)
        }

    override fun tickMovement() {
        if (!world.isClient && this.isAlive) {
            raidGoal!!.decreaseCooldown()
            if (raidGoal!!.cooldown <= 0) {
                attackPlayerGoal!!.setEnabled(true)
            } else {
                attackPlayerGoal!!.setEnabled(false)
            }

            if (this.isDrinking) {
                if (drinkTimeLeft-- <= 0) {
                    this.isDrinking = false
                    val itemStack = this.mainHandStack
                    this.equipStack(EquipmentSlot.MAINHAND, ItemStack.EMPTY)
                    val potionContentsComponent = itemStack.get(DataComponentTypes.POTION_CONTENTS)
                    if (itemStack.isOf(Items.POTION) && potionContentsComponent != null) {
                        potionContentsComponent.forEachEffect(::addStatusEffect)
                    }

                    this.emitGameEvent(GameEvent.DRINK)
                    getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED)!!
                        .removeModifier(DRINKING_SPEED_PENALTY_MODIFIER.id())
                }
            } else {
                var holder: Holder<Potion?>? = null
                if (random.nextFloat() < 0.15f && this.isSubmergedIn(FluidTags.WATER) && !this.hasStatusEffect(
                        StatusEffects.WATER_BREATHING
                    )
                ) {
                    holder = Potions.WATER_BREATHING
                } else if (random.nextFloat() < 0.15f && (this.isOnFire || this.recentDamageSource != null && this.recentDamageSource!!
                        .isTypeIn(DamageTypeTags.IS_FIRE)) && !this.hasStatusEffect(StatusEffects.FIRE_RESISTANCE)
                ) {
                    holder = Potions.FIRE_RESISTANCE
                } else if (random.nextFloat() < 0.05f && this.health < this.maxHealth) {
                    holder = Potions.HEALING
                } else if (random.nextFloat() < 0.5f && (this.target != null) && !this.hasStatusEffect(StatusEffects.SPEED) && (target!!
                        .squaredDistanceTo(this) > 121.0)
                ) {
                    holder = Potions.SWIFTNESS
                }

                if (holder != null) {
                    this.equipStack(EquipmentSlot.MAINHAND, PotionContentsComponent.createStack(Items.POTION, holder))
                    this.drinkTimeLeft = this.mainHandStack.getUseTicks(this)
                    this.isDrinking = true
                    if (!this.isSilent) {
                        world.playSound(
                            null as PlayerEntity?,
                            this.x,
                            this.y,
                            this.z,
                            SoundEvents.ENTITY_WITCH_DRINK,
                            this.soundCategory,
                            1.0f,
                            0.8f + random.nextFloat() * 0.4f
                        )
                    }

                    val entityAttributeInstance = this.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED)
                    entityAttributeInstance!!.removeModifier(DRINKING_SPEED_MODIFIER_ID)
                    entityAttributeInstance.addTemporaryModifier(DRINKING_SPEED_PENALTY_MODIFIER)
                }
            }

            if (random.nextFloat() < 7.5E-4f) {
                world.sendEntityStatus(this, 15.toByte())
            }
        }

        super.tickMovement()
    }

    override fun getCelebratingSound(): SoundEvent {
        return SoundEvents.ENTITY_WITCH_CELEBRATE
    }

    override fun handleStatus(status: Byte) {
        if (status.toInt() == 15) {
            for (i in 0 until random.nextInt(35) + 10) {
                world.addParticle(
                    ParticleTypes.WITCH,
                    this.x + random.nextGaussian() * 0.12999999523162842,
                    this.bounds.maxY + 0.5 + (random.nextGaussian() * 0.12999999523162842),
                    this.z + random.nextGaussian() * 0.12999999523162842,
                    0.0,
                    0.0,
                    0.0
                )
            }
        } else {
            super.handleStatus(status)
        }
    }

    override fun applyEnchantmentsToDamage(source: DamageSource, amount: Float): Float {
        var amount = amount
        amount = super.applyEnchantmentsToDamage(source, amount)
        if (source.attacker === this) {
            amount = 0.0f
        }

        if (source.isTypeIn(DamageTypeTags.WITCH_RESISTANT_TO)) {
            amount *= 0.15f
        }

        return amount
    }

    override fun attack(target: LivingEntity, pullProgress: Float) {
        if (!this.isDrinking) {
            val vec3d = target.velocity
            val d = target.x + vec3d.x - this.x
            val e = target.eyeY - 1.100000023841858 - this.y
            val f = target.z + vec3d.z - this.z
            val g = sqrt(d * d + f * f)
            var holder = Potions.HARMING
            if (target is RaiderEntity) {
                holder = if (target.getHealth() <= 4.0f) {
                    Potions.HEALING
                } else {
                    Potions.REGENERATION
                }

                this.target = null as LivingEntity?
            } else if (g >= 8.0 && !target.hasStatusEffect(StatusEffects.SLOWNESS)) {
                holder = Potions.SLOWNESS
            } else if (target.health >= 8.0f && !target.hasStatusEffect(StatusEffects.POISON)) {
                holder = Potions.POISON
            } else if (g <= 3.0 && !target.hasStatusEffect(StatusEffects.WEAKNESS) && random.nextFloat() < 0.25f) {
                holder = Potions.WEAKNESS
            }

            val potionEntity = PotionEntity(this.world, this)
            potionEntity.setItem(PotionContentsComponent.createStack(Items.SPLASH_POTION, holder))
            potionEntity.pitch -= -20.0f
            potionEntity.setVelocity(d, e + g * 0.2, f, 0.75f, 8.0f)
            if (!this.isSilent) {
                world.playSound(
                    null as PlayerEntity?,
                    this.x,
                    this.y,
                    this.z,
                    SoundEvents.ENTITY_WITCH_THROW,
                    this.soundCategory,
                    1.0f,
                    0.8f + random.nextFloat() * 0.4f
                )
            }

            world.spawnEntity(potionEntity)
        }
    }

    override fun addBonusForWave(world: ServerWorld, wave: Int, unused: Boolean) {
    }

    override fun canLead(): Boolean {
        return false
    }

    companion object {
        private val DRINKING_SPEED_MODIFIER_ID: Identifier = Identifier.ofDefault("drinking")
        private val DRINKING_SPEED_PENALTY_MODIFIER =
            EntityAttributeModifier(DRINKING_SPEED_MODIFIER_ID, -0.25, EntityAttributeModifier.Operation.ADD_VALUE)
        private val DRINKING: TrackedData<Boolean> = DataTracker.registerData(
            ReferenceWitchEntity::class.java, TrackedDataHandlerRegistry.BOOLEAN
        )

        fun createAttributes(): DefaultAttributeContainer.Builder {
            return HostileEntity.createAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH, 26.0)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.25)
        }
    }
}