package org.teamvoided.dusk_debris.entity

import net.minecraft.core.BlockPos
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.syncher.EntityDataAccessor
import net.minecraft.network.syncher.EntityDataSerializers
import net.minecraft.network.syncher.SynchedEntityData
import net.minecraft.sounds.SoundEvent
import net.minecraft.sounds.SoundEvents
import net.minecraft.util.TimeUtil
import net.minecraft.util.valueproviders.UniformInt
import net.minecraft.world.DifficultyInstance
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.entity.*
import net.minecraft.world.entity.ai.attributes.AttributeSupplier
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.entity.ai.goal.*
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal
import net.minecraft.world.entity.ai.goal.target.ResetUniversalAngerTargetGoal
import net.minecraft.world.entity.animal.AbstractGolem
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.ServerLevelAccessor
import org.teamvoided.dusks_and_dungeons.entity.goal.WanderAroundPoint
import java.util.*
import kotlin.jvm.optionals.getOrNull


class PifflingPumpkinEntity(entityType: EntityType<out PifflingPumpkinEntity>, world: Level) :
    AbstractGolem(entityType, world), NeutralMob {
    private var targetUuid: UUID? = null
//    var stateTicks: Int = 0
//    val twitchAnimationState: AnimationState = AnimationState()

    init {
        this.setCanPickUpLoot(true)
        Arrays.fill(this.handDropChances, 2f)
        Arrays.fill(this.armorDropChances, 0f)
    }

    override fun registerGoals() {
        goalSelector.addGoal(0, MeleeAttackGoal(this, 1.0, true))
        goalSelector.addGoal(1, PanicGoal(this, 2.0))
        goalSelector.addGoal(2, WanderAroundPoint(this, this.summonedPos, 1.0))
        goalSelector.addGoal(4, MoveTowardsRestrictionGoal(this, 1.0))
        goalSelector.addGoal(8, WaterAvoidingRandomStrollGoal(this, 1.0, 1f))
        goalSelector.addGoal(9, LookAtPlayerGoal(this, Player::class.java, 6f))
        goalSelector.addGoal(10, RandomLookAroundGoal(this))
        targetSelector.addGoal(1, HurtByTargetGoal(this, *arrayOfNulls(0)))
        targetSelector.addGoal(2, ResetUniversalAngerTargetGoal(this, true))
    }

    override fun finalizeSpawn(
        world: ServerLevelAccessor,
        difficulty: DifficultyInstance,
        spawnReason: MobSpawnType,
        entityData: SpawnGroupData?
    ): SpawnGroupData? {
        summonedPos = this.blockPosition()
        super.finalizeSpawn(world, difficulty, spawnReason, entityData)
        this.isLeftHanded = world.random.nextInt(1) == 1
        return entityData
    }

    override fun defineSynchedData(builder: SynchedEntityData.Builder) {
        super.defineSynchedData(builder)
        builder.define(SUMMON_POS, Optional.empty())
        builder.define(ANGER_TIME, 0)
    }

    override fun addAdditionalSaveData(nbt: CompoundTag) {
        super.addAdditionalSaveData(nbt)
        this.addPersistentAngerSaveData(nbt)
    }

    override fun readAdditionalSaveData(nbt: CompoundTag) {
        super.readAdditionalSaveData(nbt)
        this.readPersistentAngerSaveData(level(), nbt)
    }

    override fun tick() {
        if (level().isClientSide) {
            this.updateAnimationStates()
        }
        super.tick()
    }

    override fun equipItemIfPossible(equipment: ItemStack): ItemStack {
        if (this.canHoldItem(equipment)) {
            var currentStack = this.getItemBySlot(EquipmentSlot.MAINHAND)
            if (this.canReplaceCurrentItem(equipment, currentStack))
                return tryEquip(equipment, currentStack, EquipmentSlot.MAINHAND)
            currentStack = this.getItemBySlot(EquipmentSlot.OFFHAND)
            if (this.canReplaceCurrentItem(equipment, currentStack))
                return tryEquip(equipment, currentStack, EquipmentSlot.OFFHAND)
        }
        return ItemStack.EMPTY
    }

    private fun tryEquip(newItem: ItemStack, oldItem: ItemStack, equipmentSlot: EquipmentSlot): ItemStack {
        if (!oldItem.isEmpty) {
            this.spawnAtLocation(oldItem)
        }
        val itemStack2 = equipmentSlot.limit(newItem)
        this.setItemSlotAndDropWhenKilled(equipmentSlot, itemStack2)
        return itemStack2
    }

    override fun canTakeItem(stack: ItemStack): Boolean {
        return super.canTakeItem(stack)
    }

    override fun canHoldItem(stack: ItemStack): Boolean = this.canPickUpLoot()

    var summonedPos: BlockPos?
        get() = entityData[SUMMON_POS].getOrNull()
        set(summonedPos) {
            entityData[SUMMON_POS] = Optional.ofNullable(summonedPos)
        }

    override fun getRemainingPersistentAngerTime(): Int {
        return entityData.get(ANGER_TIME)
    }

    override fun setRemainingPersistentAngerTime(ticks: Int) {
        entityData.set(ANGER_TIME, ticks)
    }

    override fun getPersistentAngerTarget(): UUID? {
        return this.targetUuid
    }

    override fun setPersistentAngerTarget(uuid: UUID?) {
        this.targetUuid = uuid
    }

    override fun startPersistentAngerTimer() {
        this.remainingPersistentAngerTime = ANGER_TIME_RANGE.sample(random)
    }

    override fun getHurtSound(source: DamageSource): SoundEvent? {
        return SoundEvents.IRON_GOLEM_HURT
    }

    override fun getDeathSound(): SoundEvent? {
        return SoundEvents.IRON_GOLEM_DEATH
    }

    private fun updateAnimationStates() {
//        if (this.stateTicks < twitchLength) {
//            this.stateTicks++
//            twitchAnimationState.start(this.age)
//        } else {
//            twitchAnimationState.stop()
//        }
    }

    override fun isPersistenceRequired(): Boolean {
        return true
    }

    companion object {
        private val SUMMON_POS: EntityDataAccessor<Optional<BlockPos>> = SynchedEntityData.defineId(
            PifflingPumpkinEntity::class.java,
            EntityDataSerializers.OPTIONAL_BLOCK_POS
        )
        private val ANGER_TIME: EntityDataAccessor<Int> = SynchedEntityData.defineId(
            PifflingPumpkinEntity::class.java,
            EntityDataSerializers.INT
        )
        private val ANGER_TIME_RANGE: UniformInt = TimeUtil.rangeOfSeconds(60, 180)

        fun createAttributes(): AttributeSupplier.Builder {
            return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 10.0)
                .add(Attributes.MOVEMENT_SPEED, 0.2)
                .add(Attributes.ATTACK_DAMAGE, 3.0)
        }
    }
}