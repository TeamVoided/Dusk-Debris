package net.minecraft.entity.mob

import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.ai.goal.*
import net.minecraft.entity.attribute.DefaultAttributeContainer
import net.minecraft.entity.attribute.EntityAttributeModifier
import net.minecraft.entity.attribute.EntityAttributes
import net.minecraft.entity.damage.DamageSource
import net.minecraft.entity.data.DataTracker
import net.minecraft.entity.data.TrackedData
import net.minecraft.entity.data.TrackedDataHandlerRegistry
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.nbt.NbtCompound
import net.minecraft.particle.ParticleTypes
import net.minecraft.server.world.ServerWorld
import net.minecraft.sound.SoundEvent
import net.minecraft.sound.SoundEvents
import net.minecraft.util.Identifier
import net.minecraft.util.TimeHelper
import net.minecraft.util.math.int_provider.UniformIntProvider
import net.minecraft.world.World
import java.util.*

class Temp(entityType: EntityType<out Temp>, world: World) : HostileEntity(entityType, world), Angerable {
    private var angerTime = 0
    private var targetUuid: UUID? = null

    override fun initGoals() {
        goalSelector.add(0, SwimGoal(this))
        goalSelector.add(
            8, LookAtEntityGoal(
                this,
                PlayerEntity::class.java, 8.0f
            )
        )
        goalSelector.add(8, LookAroundGoal(this))
        targetSelector.add(2, RevengeGoal(this, *arrayOfNulls(0)))
        targetSelector.add(4, UniversalAngerGoal(this, false))
    }

    override fun setTarget(target: LivingEntity?) {
        super.setTarget(target)
        val entityAttributeInstance = this.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED)
        if (target == null) {
            dataTracker.set(ANGRY, false)
            dataTracker.set(PROVOKED, false)
            entityAttributeInstance!!.removeModifier(ATTACKING_SPEED_MODIFIER_ID)
        } else {
            dataTracker.set(ANGRY, true)
            if (!entityAttributeInstance!!.hasModifier(ATTACKING_SPEED_MODIFIER_ID)) {
                entityAttributeInstance.addTemporaryModifier(ATTACKING_SPEED_MODIFIER)
            }
        }
    }

    override fun initDataTracker(builder: DataTracker.Builder) {
        super.initDataTracker(builder)
        builder.add(ANGRY, false)
        builder.add(PROVOKED, false)
    }

    override fun chooseRandomAngerTime() {
        this.setAngerTime(ANGER_TIME_RANGE[random])
    }

    override fun setAngerTime(ticks: Int) {
        this.angerTime = ticks
    }

    override fun getAngerTime(): Int {
        return this.angerTime
    }

    override fun setAngryAt(uuid: UUID?) {
        this.targetUuid = uuid
    }

    override fun getAngryAt(): UUID? {
        return this.targetUuid
    }

    override fun writeCustomDataToNbt(nbt: NbtCompound) {
        super.writeCustomDataToNbt(nbt)
        this.writeAngerToNbt(nbt)
    }

    override fun readCustomDataFromNbt(nbt: NbtCompound) {
        super.readCustomDataFromNbt(nbt)
        this.readAngerFromNbt(this.world, nbt)
    }

    override fun tickMovement() {
        if (world.isClient) {
            for (i in 0..1) {
                world.addParticle(
                    ParticleTypes.PORTAL,
                    this.getParticleX(0.5),
                    this.randomBodyY - 0.25,
                    this.getParticleZ(0.5),
                    (random.nextDouble() - 0.5) * 2.0, -random.nextDouble(),
                    (random.nextDouble() - 0.5) * 2.0
                )
            }
        }

        this.jumping = false
        if (!world.isClient) {
            this.tickAngerLogic(world as ServerWorld, true)
        }

        super.tickMovement()
    }

    override fun mobTick() {

        super.mobTick()
    }

    override fun getAmbientSound(): SoundEvent? {
        return if (this.isAngry) SoundEvents.ENTITY_ENDERMAN_SCREAM else SoundEvents.ENTITY_ENDERMAN_AMBIENT
    }

    override fun getHurtSound(source: DamageSource): SoundEvent? {
        return SoundEvents.ENTITY_ENDERMAN_HURT
    }

    override fun getDeathSound(): SoundEvent? {
        return SoundEvents.ENTITY_ENDERMAN_DEATH
    }

    override fun damage(source: DamageSource, amount: Float): Boolean {
        return false
    }

    val isAngry: Boolean
        get() = dataTracker.get(ANGRY)

    companion object {
        private val ATTACKING_SPEED_MODIFIER_ID: Identifier = Identifier.ofDefault("attacking")
        private val ATTACKING_SPEED_MODIFIER = EntityAttributeModifier(
            ATTACKING_SPEED_MODIFIER_ID,
            0.15000000596046448,
            EntityAttributeModifier.Operation.ADD_VALUE
        )
        private const val CREEPY_STARE_SOUND_DELAY = 400
        private const val MIN_ANGER_LOSS_TIME = 600
        private val ANGRY: TrackedData<Boolean> =
            DataTracker.registerData(EndermanEntity::class.java, TrackedDataHandlerRegistry.BOOLEAN)
        private val PROVOKED: TrackedData<Boolean> =
            DataTracker.registerData(EndermanEntity::class.java, TrackedDataHandlerRegistry.BOOLEAN)
        private val ANGER_TIME_RANGE: UniformIntProvider = TimeHelper.betweenSeconds(20, 39)
        fun createAttributes(): DefaultAttributeContainer.Builder {
            return HostileEntity.createAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH, 40.0)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.30000001192092896)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 7.0).add(EntityAttributes.GENERIC_FOLLOW_RANGE, 64.0)
                .add(EntityAttributes.GENERIC_STEP_HEIGHT, 1.0)
        }
    }
}