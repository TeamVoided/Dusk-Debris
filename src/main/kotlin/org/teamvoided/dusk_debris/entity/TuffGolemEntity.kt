package org.teamvoided.dusk_debris.entity

import net.minecraft.block.BlockState
import net.minecraft.entity.*
import net.minecraft.entity.ai.goal.WanderAroundFarGoal
import net.minecraft.entity.attribute.DefaultAttributeContainer
import net.minecraft.entity.attribute.EntityAttributes
import net.minecraft.entity.damage.DamageSource
import net.minecraft.entity.data.DataTracker
import net.minecraft.entity.data.TrackedData
import net.minecraft.entity.data.TrackedDataHandlerRegistry
import net.minecraft.entity.mob.MobEntity
import net.minecraft.entity.passive.GolemEntity
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtCompound
import net.minecraft.nbt.NbtOps
import net.minecraft.sound.SoundEvent
import net.minecraft.sound.SoundEvents
import net.minecraft.util.math.BlockPos
import net.minecraft.world.LocalDifficulty
import net.minecraft.world.ServerWorldAccess
import net.minecraft.world.World
import org.teamvoided.dusk_debris.entity.ai.goal.PickupAndDropItemGoal
import org.teamvoided.dusk_debris.entity.ai.goal.TuffGolemHome
import java.util.*
import kotlin.jvm.optionals.getOrNull

class TuffGolemEntity(entityType: EntityType<out TuffGolemEntity>, world: World) :
    GolemEntity(entityType, world) {
    init {
        this.setCanPickUpLoot(!wasGivenItem() && !isStatue())
    }

    override fun initGoals() {
        goalSelector.add(1, TuffGolemHome(this, 1.0, 0.0001f))
        goalSelector.add(2, WanderAroundFarGoal(this, 1.0, 0.0001f))
//        goalSelector.add(3, LookAtEntityGoal(this, PlayerEntity::class.java, 6.0f))
//        goalSelector.add(4, LookAroundGoal(this))
        goalSelector.add(5, PickupAndDropItemGoal(this, !wasGivenItem() && !isStatue(), 0.5))
    }

    override fun initialize(
        world: ServerWorldAccess,
        difficulty: LocalDifficulty,
        spawnReason: SpawnReason,
        entityData: EntityData?
    ): EntityData? {
        setNewCoordinate(this.blockPos)
        return super.initialize(world, difficulty, spawnReason, entityData)
    }

    override fun initDataTracker(builder: DataTracker.Builder) {
        super.initDataTracker(builder)
        builder.add(SUMMON_POS, Optional.empty())
        builder.add(WAS_GIVEN_ITEM, false)
        builder.add(IS_STATUE, false)
        builder.add(CLOAK_BLOCK, Optional.empty())
    }

    override fun writeCustomDataToNbt(nbt: NbtCompound) {
        super.writeCustomDataToNbt(nbt)
        if (this.getSummonedPos() != null) {
            val posCompound = NbtCompound()
            BlockPos.CODEC.encode(this.getSummonedPos(), NbtOps.INSTANCE, posCompound)
            nbt.put("SummonedPos", posCompound)
        }
        nbt.putBoolean("WasGivenItem", this.wasGivenItem())
        nbt.putBoolean("IsStatue", this.isStatue())
        if (this.geCloakBlock() != null) {
            val stateCompound = NbtCompound()
            BlockState.CODEC.encode(this.geCloakBlock(), NbtOps.INSTANCE, stateCompound)
            nbt.put("CloakBlock", stateCompound)
        }
    }

    override fun readCustomDataFromNbt(nbt: NbtCompound) {
        super.readCustomDataFromNbt(nbt)
        if (nbt.contains("SummonedPos")) {
            val posCompound = nbt.get("SummonedPos")
            this.setNewCoordinate(BlockPos.CODEC.decode(NbtOps.INSTANCE, posCompound).getOrThrow().first)
        }
        if (nbt.contains("WasGivenItem")) {
            this.setWasGivenItem(nbt.getBoolean("WasGivenItem"))
        }
        if (nbt.contains("IsStatue")) {
            this.setStatue(nbt.getBoolean("IsStatue"))
        }
        if (nbt.contains("CloakBlock")) {
            val stateCompound = nbt.get("CloakBlock")
            this.setCloakBlock(BlockState.CODEC.decode(NbtOps.INSTANCE, stateCompound).getOrThrow().first)
        }
    }

    override fun canEquip(stack: ItemStack): Boolean {
        val equipmentSlot = this.getPreferredEquipmentSlot(stack)
        return if (!getEquippedStack(equipmentSlot).isEmpty) {
            false
        } else {
            equipmentSlot == EquipmentSlot.MAINHAND && super.canEquip(stack)
        }
    }

    override fun canPickupItem(stack: ItemStack): Boolean {
        val itemStack = this.getEquippedStack(EquipmentSlot.MAINHAND)
        return itemStack.isEmpty
    }

//    override fun loot(item: ItemEntity) {
//        val itemStack = item.stack
//        if (this.canPickupItem(itemStack)) {
//            val i = itemStack.count
//            if (i > 1) {
//                this.dropItem(itemStack.split(i - 1).item)
//            }
//
//            this.spit(this.getEquippedStack(EquipmentSlot.MAINHAND))
//            this.triggerItemPickedUpByEntityCriteria(item)
//            this.equipStack(EquipmentSlot.MAINHAND, itemStack.split(1))
//            this.updateDropChances(EquipmentSlot.MAINHAND)
//            this.sendPickup(item, itemStack.count)
//            item.discard()
//        }
//    }

//    fun spit(stack: ItemStack) {
//        if (!stack.isEmpty && !world.isClient) {
//            val itemEntity = ItemEntity(
//                this.world,
//                this.x + this.rotationVector.x,
//                this.y + 1.0,
//                this.z + this.rotationVector.z, stack
//            )
//            itemEntity.setPickupDelay(40)
//            itemEntity.setThrower(this)
////            this.playSound(SoundEvents.ENTITY_FOX_SPIT, 1.0f, 1.0f)
//            world.spawnEntity(itemEntity)
//        }
//    }

//    override fun drop(world: ServerWorld, source: DamageSource) {
//        val itemStack = this.getEquippedStack(EquipmentSlot.MAINHAND)
//        if (!itemStack.isEmpty) {
//            this.dropStack(itemStack)
//            this.equipStack(EquipmentSlot.MAINHAND, ItemStack.EMPTY)
//        }
//
//        super.drop(world, source)
//    }


    fun setNewCoordinate(blockPos: BlockPos?) {
        dataTracker.set(SUMMON_POS, Optional.ofNullable(blockPos))
    }

    fun getSummonedPos(): BlockPos? {
        return dataTracker.get(SUMMON_POS).getOrNull()
    }

    fun isHoldingItem(): Boolean {
        return this.hasStackEquipped(EquipmentSlot.MAINHAND)
    }

    fun setWasGivenItem(wasGivenItem: Boolean) {
        dataTracker.set(WAS_GIVEN_ITEM, wasGivenItem)
    }

    fun wasGivenItem(): Boolean {
        return dataTracker.get(WAS_GIVEN_ITEM)
    }

    fun setStatue(statue: Boolean) {
        dataTracker.set(IS_STATUE, statue)
    }

    fun isStatue(): Boolean {
        return dataTracker.get(IS_STATUE)
    }


    fun setCloakBlock(blockState: BlockState?) {
        dataTracker.set(CLOAK_BLOCK, Optional.ofNullable(blockState))
    }

    fun geCloakBlock(): BlockState? {
        return dataTracker.get(CLOAK_BLOCK).getOrNull()
    }

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
        private val SUMMON_POS: TrackedData<Optional<BlockPos>> =
            DataTracker.registerData(TuffGolemEntity::class.java, TrackedDataHandlerRegistry.OPTIONAL_BLOCK_POS)
        private val WAS_GIVEN_ITEM: TrackedData<Boolean> =
            DataTracker.registerData(TuffGolemEntity::class.java, TrackedDataHandlerRegistry.BOOLEAN)
        private val IS_STATUE: TrackedData<Boolean> =
            DataTracker.registerData(TuffGolemEntity::class.java, TrackedDataHandlerRegistry.BOOLEAN)
        private val CLOAK_BLOCK: TrackedData<Optional<BlockState>> =
            DataTracker.registerData(TuffGolemEntity::class.java, TrackedDataHandlerRegistry.OPTIONAL_BLOCK_STATE)

        fun createAttributes(): DefaultAttributeContainer.Builder {
            return MobEntity.createAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 26.0)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.1)
        }
    }
}