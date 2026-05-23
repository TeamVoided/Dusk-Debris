package org.teamvoided.dusk_debris.entity

import net.minecraft.core.BlockPos
import net.minecraft.nbt.CompoundTag
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.AnimationState
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.NeutralMob
import net.minecraft.world.entity.ai.attributes.AttributeSupplier
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.entity.monster.Monster
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import java.util.*

abstract class AbstractJellyfishEntity(entityType: EntityType<out AbstractJellyfishEntity>, world: Level) :
    Monster(entityType, world), NeutralMob {
    var angerTicks = 0
    var targetUuid: UUID? = null
    val idleAnimationState: AnimationState = AnimationState()

    init {
        this.setNoGravity(true)
    }

//    override fun initDataTracker(builder: DataTracker.Builder) {
//        super.initDataTracker(builder)
//    }

    override fun tick() {
        super.tick()
        this.updateAnimations()
    }

    override fun addAdditionalSaveData(nbt: CompoundTag) {
        super.addAdditionalSaveData(nbt)
        this.addPersistentAngerSaveData(nbt)
    }

    override fun readAdditionalSaveData(nbt: CompoundTag) {
        super.readAdditionalSaveData(nbt)
        this.readPersistentAngerSaveData(this.level(), nbt)
    }

    override fun aiStep() {
//        if (world.isClient) {
//                world.addParticle(
//                    ParticleTypes.PORTAL,
//                    this.getParticleX(0.5),
//                    this.randomBodyY - 0.25,
//                    this.getParticleZ(0.5),
//                    (random.nextDouble() - 0.5) * 2.0,
//                    -random.nextDouble(),
//                    (random.nextDouble() - 0.5) * 2.0
//                )
//        }

        this.jumping = false
        if (!level().isClientSide) {
            this.updatePersistentAnger(level() as ServerLevel, true)
        }
        super.aiStep()
    }

    override fun onClimbable(): Boolean = false

    override fun checkFallDamage(fallDistance: Double, onGround: Boolean, landedState: BlockState, landedPosition: BlockPos) {}

    override fun playStepSound(pos: BlockPos, state: BlockState) {}

    override fun setRemainingPersistentAngerTime(ticks: Int) {
        this.angerTicks = ticks
    }

    override fun getRemainingPersistentAngerTime(): Int {
        return this.angerTicks
    }

    override fun setPersistentAngerTarget(uuid: UUID?) {
        this.targetUuid = uuid
    }

    override fun getPersistentAngerTarget(): UUID? {
        return this.targetUuid
    }

    override fun canAttackType(type: EntityType<*>): Boolean {
        return true
    }

    abstract fun updateAnimations()

    companion object {
        const val GRAVITY_VALUE = 0.003

        fun createAttributes(): AttributeSupplier.Builder {
            return createAttributesNoSpecial()
                .add(Attributes.ARMOR, 30.0)
                .add(Attributes.ARMOR_TOUGHNESS, 20.0)
                .add(Attributes.FOLLOW_RANGE, 64.0)
        }

        fun createAttributesNoSpecial(): AttributeSupplier.Builder {
            return Monster.createMonsterAttributes()
                .add(Attributes.MOVEMENT_SPEED, 0.3)
                .add(Attributes.FLYING_SPEED, 0.3)
                .add(Attributes.GRAVITY, 0.0)
        }
    }


}