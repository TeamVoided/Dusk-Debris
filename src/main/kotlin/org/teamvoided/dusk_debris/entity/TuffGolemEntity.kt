package org.teamvoided.dusk_debris.entity

import net.minecraft.block.BlockState
import net.minecraft.entity.EntityType
import net.minecraft.entity.EquipmentSlot
import net.minecraft.entity.ai.goal.LookAroundGoal
import net.minecraft.entity.ai.goal.LookAtEntityGoal
import net.minecraft.entity.ai.goal.WanderAroundFarGoal
import net.minecraft.entity.attribute.DefaultAttributeContainer
import net.minecraft.entity.attribute.EntityAttributes
import net.minecraft.entity.damage.DamageSource
import net.minecraft.entity.data.DataTracker
import net.minecraft.entity.data.TrackedData
import net.minecraft.entity.data.TrackedDataHandlerRegistry
import net.minecraft.entity.mob.MobEntity
import net.minecraft.entity.passive.GolemEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtCompound
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvent
import net.minecraft.sound.SoundEvents
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.world.World
import org.teamvoided.dusk_debris.entity.ai.goal.PickupAndDropItemGoal

class TuffGolemEntity(entityType: EntityType<out TuffGolemEntity>, world: World) :
    GolemEntity(entityType, world) {
    override fun initGoals() {
//        goalSelector.add(1, ProjectileAttackGoal(this, 1.25, 20, 10.0f))
        goalSelector.add(2, WanderAroundFarGoal(this, 1.0, 1E-5f))
        goalSelector.add(3, LookAtEntityGoal(this, PlayerEntity::class.java, 6.0f))
        goalSelector.add(4, LookAroundGoal(this))
        goalSelector.add(5, PickupAndDropItemGoal(this, !wasGivenBlock()))
    }

    override fun initDataTracker(builder: DataTracker.Builder) {
        super.initDataTracker(builder)
        builder.add(WAS_GIVEN_BLOCK, false)
    }

    override fun writeCustomDataToNbt(nbt: NbtCompound) {
        super.writeCustomDataToNbt(nbt)
        nbt.putBoolean("WasGivenBlock", this.wasGivenBlock())
    }

    override fun readCustomDataFromNbt(nbt: NbtCompound) {
        super.readCustomDataFromNbt(nbt)
        if (nbt.contains("WasGivenBlock")) {
            this.setWasGivenBlock(nbt.getBoolean("WasGivenBlock"))
        }
    }

    fun setWasGivenBlock(wasGivenBlock: Boolean) {
        dataTracker.set(WAS_GIVEN_BLOCK, wasGivenBlock)
    }

    private fun wasGivenBlock(): Boolean {
        return dataTracker.get(WAS_GIVEN_BLOCK)
    }

    override fun tickMovement() {
        super.tickMovement()
    }

    override fun interactMob(player: PlayerEntity, hand: Hand): ActionResult {
        val playerItemStack = player.getStackInHand(hand)
        val golemItemStack = this.getStackInHand(Hand.MAIN_HAND)
//        if (itemStack.isOf(Items.SHEARS)) {
//            if (!world.isClient) {
//                itemStack.damageEquipment(1, player, getHand(hand))
//            }
//            return ActionResult.success(world.isClient)
//        } else {
//            return ActionResult.PASS
//        }

        if (golemItemStack.isEmpty && !playerItemStack.isEmpty) {
            val playerItemStack2 = playerItemStack.copyWithCount(1)
            this.setStackInHand(Hand.MAIN_HAND, playerItemStack2)
            world.playSoundFromEntity(
                player,
                this,
                SoundEvents.ENTITY_ALLAY_ITEM_GIVEN,
                SoundCategory.NEUTRAL,
                2.0f,
                0.0f
            )
            return ActionResult.SUCCESS
        } else if (!golemItemStack.isEmpty && hand == Hand.MAIN_HAND && playerItemStack.isEmpty) {
            this.equipStack(EquipmentSlot.MAINHAND, ItemStack.EMPTY)
            world.playSoundFromEntity(
                player,
                this,
                SoundEvents.ENTITY_ALLAY_ITEM_TAKEN,
                SoundCategory.NEUTRAL,
                2.0f,
                0.0f
            )
            this.swingHand(Hand.MAIN_HAND)
            return ActionResult.SUCCESS
        } else {
            return super.interactMob(player, hand)
        }
    }


//    fun setHasPumpkin(hasPumpkin: Boolean) {
//        val b = dataTracker.get(HELD_BLOCK)
//        if (hasPumpkin) {
//            dataTracker.set(SNOW_GOLEM_FLAGS, (b.toInt() or 16).toByte())
//        } else {
//            dataTracker.set(SNOW_GOLEM_FLAGS, (b.toInt() and -17).toByte())
//        }
//    }

    override fun getAmbientSound(): SoundEvent? {
        return SoundEvents.ENTITY_SNOW_GOLEM_AMBIENT
    }

    override fun getHurtSound(source: DamageSource): SoundEvent? {
        return SoundEvents.ENTITY_SNOW_GOLEM_HURT
    }

    override fun getDeathSound(): SoundEvent? {
        return SoundEvents.ENTITY_SNOW_GOLEM_DEATH
    }

    companion object {
//        private val CARRIED_BLOCK: TrackedData<Optional<BlockState>> =
//            DataTracker.registerData(TuffGolemEntity::class.java, TrackedDataHandlerRegistry.OPTIONAL_BLOCK_STATE)
        private val WAS_GIVEN_BLOCK: TrackedData<Boolean> =
            DataTracker.registerData(TuffGolemEntity::class.java, TrackedDataHandlerRegistry.BOOLEAN)

        fun createAttributes(): DefaultAttributeContainer.Builder {
            return MobEntity.createAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 4.0)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.2)
        }
    }
}