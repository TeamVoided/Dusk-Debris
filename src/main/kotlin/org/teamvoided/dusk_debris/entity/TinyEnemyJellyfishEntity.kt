package org.teamvoided.dusk_debris.entity

import com.mojang.serialization.Dynamic
import net.minecraft.core.particles.ParticleTypes
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.protocol.game.DebugPackets
import net.minecraft.network.syncher.EntityDataAccessor
import net.minecraft.network.syncher.EntityDataSerializers
import net.minecraft.network.syncher.SynchedEntityData
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvent
import net.minecraft.sounds.SoundEvents
import net.minecraft.util.TimeUtil
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.ai.Brain
import net.minecraft.world.entity.ai.attributes.AttributeSupplier
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.entity.ai.control.FlyingMoveControl
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation
import net.minecraft.world.entity.ai.navigation.PathNavigation
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import org.teamvoided.dusk_debris.init.DuskItems

class TinyEnemyJellyfishEntity(entityType: EntityType<TinyEnemyJellyfishEntity>, world: Level) :
    AbstractJellyfishEntity(entityType, world), Pickupable {


    init {
        this.moveControl = FlyingMoveControl(this, 10, true)
    }

    override fun defineSynchedData(builder: SynchedEntityData.Builder) {
        super.defineSynchedData(builder)
        builder.define(PLACED, false)
    }

    override fun addAdditionalSaveData(nbt: CompoundTag) {
        super.addAdditionalSaveData(nbt)
        nbt.putBoolean("FromBucket", placed)
    }

    override fun readAdditionalSaveData(nbt: CompoundTag) {
        super.readAdditionalSaveData(nbt)
        placed = nbt.getBoolean("FromBucket")
    }

    override fun createNavigation(world: Level): PathNavigation {
        val birdNavigation = FlyingPathNavigation(this, world)
        birdNavigation.setCanOpenDoors(false)
        birdNavigation.setCanFloat(true)
        birdNavigation.setCanPassDoors(true)
        return birdNavigation
    }

    override fun makeBrain(dynamic: Dynamic<*>): Brain<*> {
        return TinyEnemyJellyfishBrain.create(brainProvider().makeBrain(dynamic))
    }

    override fun brainProvider(): Brain.Provider<TinyEnemyJellyfishEntity> {
        return TinyEnemyJellyfishBrain.createProfile()
    }

    override fun customServerAiStep() {
        this.level().profiler.push("tinyEnemyJellyfishBrain")
        (brain as Brain<TinyEnemyJellyfishEntity>).tick(this.level() as ServerLevel, this)
        this.level().profiler.pop()
        this.level().profiler.push("tinyEnemyJellyfishActivityUpdate")
        TinyEnemyJellyfishBrain.updateActivities(this)
        super.customServerAiStep()
    }

    override fun aiStep() {
        super.aiStep()
        if (!level().isClientSide) {

        }
    }

    override fun sendDebugPackets() {
        super.sendDebugPackets()
        DebugPackets.sendEntityBrain(this)
    }

    override fun startPersistentAngerTimer() {
        this.remainingPersistentAngerTime = ANGER_TIME_RANGE.sample(this.random)
    }

    override fun shouldDropExperience(): Boolean = false

    override fun isIgnoringBlockTriggers(): Boolean = true

    override fun requiresCustomPersistence(): Boolean = super.requiresCustomPersistence() || placed

    override fun removeWhenFarAway(distanceSquared: Double): Boolean = !placed && !this.hasCustomName()

    override var placed: Boolean
        get() = entityData.get(PLACED)
        set(boolean) = entityData.set(PLACED, boolean)

    override val pickupItem: ItemStack = DuskItems.TINY_JELLYFISH.defaultInstance
    override val pickupSound: SoundEvent? = SoundEvents.BUCKET_FILL_TADPOLE

    override fun copyDataToStack(stack: ItemStack) = Pickupable.copyDataToStack(this, stack)

    override fun copyDataFromNbt(nbt: CompoundTag) = Pickupable.copyDataFromNbt(this, nbt)

    override fun mobInteract(player: Player, hand: InteractionHand): InteractionResult =
        Pickupable.tryPickup(player, hand, this).orElse(super.mobInteract(player, hand))

    override fun updateAnimations() {
        if (hurtTime > 0)
            this.idleAnimationState.stop()
        else
            this.idleAnimationState.startIfStopped(this.tickCount)
    }


    override fun handleEntityEvent(status: Byte) {
        if (status.toInt() == 60)
            addDeathParticles()
        else
            super.handleEntityEvent(status)
    }

    private fun addDeathParticles() {
//        for (i in 0..19)
        repeat(20) {
            val velX = random.nextGaussian() * 0.02
            val velY = random.nextGaussian() * 0.02
            val velZ = random.nextGaussian() * 0.02
            level().addParticle(
                ParticleTypes.SONIC_BOOM,
                this.getRandomX(1.0),
                this.y + this.bbHeight / 2,
                this.getRandomZ(1.0),
                velX,
                velY,
                velZ
            )
        }
    }

    companion object {
        val ANGER_TIME_RANGE = TimeUtil.rangeOfSeconds(20, 39)
        val PLACED: EntityDataAccessor<Boolean> =
            SynchedEntityData.defineId(TinyEnemyJellyfishEntity::class.java, EntityDataSerializers.BOOLEAN)

        fun createAttributes(): AttributeSupplier.Builder {
            return createAttributesNoSpecial()
                .add(Attributes.MOVEMENT_SPEED, 0.1)
                .add(Attributes.FLYING_SPEED, 0.1)
                .add(Attributes.MAX_HEALTH, 4.0)
                .add(Attributes.ATTACK_DAMAGE, 4.0)
        }
    }
}
