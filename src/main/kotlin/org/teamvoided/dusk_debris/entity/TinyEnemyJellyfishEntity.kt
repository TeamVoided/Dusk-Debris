package org.teamvoided.dusk_debris.entity

import com.mojang.serialization.Dynamic
import net.minecraft.entity.EntityType
import net.minecraft.entity.ai.brain.Brain
import net.minecraft.entity.ai.control.FlightMoveControl
import net.minecraft.entity.ai.pathing.BirdNavigation
import net.minecraft.entity.ai.pathing.EntityNavigation
import net.minecraft.entity.ai.pathing.SwimNavigation
import net.minecraft.entity.attribute.DefaultAttributeContainer
import net.minecraft.entity.attribute.EntityAttributes
import net.minecraft.entity.data.DataTracker
import net.minecraft.entity.data.TrackedData
import net.minecraft.entity.data.TrackedDataHandlerRegistry
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtCompound
import net.minecraft.particle.ParticleTypes
import net.minecraft.server.network.DebugInfoSender
import net.minecraft.server.world.ServerWorld
import net.minecraft.sound.SoundEvent
import net.minecraft.sound.SoundEvents
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.TimeHelper
import net.minecraft.world.World
import org.teamvoided.dusk_debris.init.DuskItems

class TinyEnemyJellyfishEntity(entityType: EntityType<TinyEnemyJellyfishEntity>, world: World) :
    AbstractJellyfishEntity(entityType, world), Pickupable {


    init {
        this.moveControl = FlightMoveControl(this, 10, true)
    }

    override fun initDataTracker(builder: DataTracker.Builder) {
        super.initDataTracker(builder)
        builder.add(PLACED, false)
    }

    override fun writeCustomDataToNbt(nbt: NbtCompound) {
        super.writeCustomDataToNbt(nbt)
        nbt.putBoolean("FromBucket", placed);
    }

    override fun readCustomDataFromNbt(nbt: NbtCompound) {
        super.readCustomDataFromNbt(nbt)
        placed = nbt.getBoolean("FromBucket")
    }

    override fun createNavigation(world: World): EntityNavigation {
        val birdNavigation = BirdNavigation(this, world)
        birdNavigation.setCanPathThroughDoors(false)
        birdNavigation.setCanSwim(true)
        birdNavigation.setCanEnterOpenDoors(true)
        return birdNavigation
    }

    override fun deserializeBrain(dynamic: Dynamic<*>): Brain<*> {
        return TinyEnemyJellyfishBrain.create(createBrainProfile().deserialize(dynamic))
    }

    override fun createBrainProfile(): Brain.Profile<TinyEnemyJellyfishEntity> {
        return TinyEnemyJellyfishBrain.createProfile()
    }

    override fun mobTick() {
        this.getWorld().profiler.push("tinyEnemyJellyfishBrain")
        (brain as Brain<TinyEnemyJellyfishEntity>).tick(this.getWorld() as ServerWorld, this)
        this.getWorld().profiler.pop()
        this.getWorld().profiler.push("tinyEnemyJellyfishActivityUpdate")
        TinyEnemyJellyfishBrain.updateActivities(this)
        super.mobTick()
    }

    override fun tickMovement() {
        super.tickMovement()
        if (!world.isClient) {

        }
    }

    override fun sendAiDebugData() {
        super.sendAiDebugData()
        DebugInfoSender.sendBrainDebugData(this)
    }

    override fun chooseRandomAngerTime() {
        this.angerTime = ANGER_TIME_RANGE.get(this.random);
    }

    override fun shouldDropXp(): Boolean = false

    override fun canAvoidTraps(): Boolean = true

    override fun cannotDespawn(): Boolean = super.cannotDespawn() || placed

    override fun canImmediatelyDespawn(distanceSquared: Double): Boolean = !placed && !this.hasCustomName()

    override var placed: Boolean
        get() = dataTracker.get(PLACED)
        set(boolean) = dataTracker.set(PLACED, boolean)

    override val pickupItem: ItemStack = DuskItems.TINY_JELLYFISH.defaultStack
    override val pickupSound: SoundEvent? = SoundEvents.ITEM_BUCKET_FILL_TADPOLE

    override fun copyDataToStack(stack: ItemStack) = Pickupable.copyDataToStack(this, stack)

    override fun copyDataFromNbt(nbt: NbtCompound) = Pickupable.copyDataFromNbt(this, nbt)

    override fun interactMob(player: PlayerEntity, hand: Hand): ActionResult =
        Pickupable.tryPickup(player, hand, this).orElse(super.interactMob(player, hand))

    override fun updateAnimations() {
        if (hurtTime > 0)
            this.idleAnimationState.stop()
        else
            this.idleAnimationState.start(this.age)
    }


    override fun handleStatus(status: Byte) {
        if (status.toInt() == 60)
            addDeathParticles()
        else
            super.handleStatus(status)
    }

    private fun addDeathParticles() {
//        for (i in 0..19)
        repeat(20) {
            val velX = random.nextGaussian() * 0.02
            val velY = random.nextGaussian() * 0.02
            val velZ = random.nextGaussian() * 0.02
            world.addParticle(
                ParticleTypes.SONIC_BOOM,
                this.getParticleX(1.0),
                this.y + this.height / 2,
                this.getParticleZ(1.0),
                velX,
                velY,
                velZ
            )
        }
    }

    companion object {
        val ANGER_TIME_RANGE = TimeHelper.betweenSeconds(20, 39)
        val PLACED: TrackedData<Boolean> =
            DataTracker.registerData(TinyEnemyJellyfishEntity::class.java, TrackedDataHandlerRegistry.BOOLEAN)

        fun createAttributes(): DefaultAttributeContainer.Builder {
            return AbstractJellyfishEntity.createAttributesNoSpecial()
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.1)
                .add(EntityAttributes.GENERIC_FLYING_SPEED, 0.1)
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 4.0)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 4.0)
        }
    }
}
