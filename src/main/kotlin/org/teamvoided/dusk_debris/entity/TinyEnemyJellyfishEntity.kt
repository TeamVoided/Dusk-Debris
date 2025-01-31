package org.teamvoided.dusk_debris.entity

import com.google.common.annotations.VisibleForTesting
import com.google.common.collect.ImmutableList
import com.mojang.serialization.Dynamic
import net.minecraft.component.DataComponentTypes
import net.minecraft.component.type.NbtComponent
import net.minecraft.entity.Bucketable
import net.minecraft.entity.EntityData
import net.minecraft.entity.EntityType
import net.minecraft.entity.SpawnReason
import net.minecraft.entity.ai.brain.Brain
import net.minecraft.entity.ai.brain.MemoryModuleType
import net.minecraft.entity.ai.brain.sensor.Sensor
import net.minecraft.entity.ai.brain.sensor.SensorType
import net.minecraft.entity.ai.control.AquaticLookControl
import net.minecraft.entity.ai.control.AquaticMoveControl
import net.minecraft.entity.ai.pathing.EntityNavigation
import net.minecraft.entity.ai.pathing.SwimNavigation
import net.minecraft.entity.attribute.DefaultAttributeContainer
import net.minecraft.entity.attribute.EntityAttributes
import net.minecraft.entity.damage.DamageSource
import net.minecraft.entity.mob.MobEntity
import net.minecraft.entity.passive.FishEntity
import net.minecraft.entity.passive.TadpoleEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.nbt.NbtCompound
import net.minecraft.particle.ParticleTypes
import net.minecraft.registry.tag.ItemTags
import net.minecraft.server.network.DebugInfoSender
import net.minecraft.server.world.ServerWorld
import net.minecraft.sound.SoundEvent
import net.minecraft.sound.SoundEvents
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.TimeHelper
import net.minecraft.world.World
import kotlin.math.abs
import kotlin.math.max

class TinyEnemyJellyfishEntity(entityType: EntityType<TinyEnemyJellyfishEntity>, world: World) :
    AbstractJellyfishEntity(entityType, world) {

    init {
        this.moveControl = AquaticMoveControl(this, 85, 10, 0.02f, 0.1f, true)
        this.lookControl = AquaticLookControl(this, 10)
    }

    override fun createNavigation(world: World): EntityNavigation {
        return SwimNavigation(this, world)
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

    override fun chooseRandomAngerTime() {
        this.angerTime = ANGER_TIME_RANGE.get(this.random);
    }

    override fun sendAiDebugData() {
        super.sendAiDebugData()
        DebugInfoSender.sendBrainDebugData(this)
    }

    override fun shouldDropXp(): Boolean {
        return false
    }

    companion object {
        val ANGER_TIME_RANGE = TimeHelper.betweenSeconds(20, 39)

        fun createAttributes(): DefaultAttributeContainer.Builder {
            return AbstractJellyfishEntity.createAttributesNoSpecial()
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 1.0)
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 4.0)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 4.0)
        }
    }
}
