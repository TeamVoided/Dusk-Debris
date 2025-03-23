package org.teamvoided.dusk_debris.entity

import net.minecraft.entity.EntityData
import net.minecraft.entity.EntityType
import net.minecraft.entity.EquipmentSlot
import net.minecraft.entity.SpawnReason
import net.minecraft.entity.ai.goal.*
import net.minecraft.entity.attribute.DefaultAttributeContainer
import net.minecraft.entity.attribute.EntityAttributes
import net.minecraft.entity.damage.DamageSource
import net.minecraft.entity.data.DataTracker
import net.minecraft.entity.data.TrackedData
import net.minecraft.entity.data.TrackedDataHandlerRegistry
import net.minecraft.entity.mob.Angerable
import net.minecraft.entity.mob.MobEntity
import net.minecraft.entity.passive.GolemEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtCompound
import net.minecraft.sound.SoundEvent
import net.minecraft.sound.SoundEvents
import net.minecraft.util.TimeHelper
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.int_provider.UniformIntProvider
import net.minecraft.world.LocalDifficulty
import net.minecraft.world.ServerWorldAccess
import net.minecraft.world.World
import org.teamvoided.dusks_and_dungeons.entity.goal.WanderAroundPoint
import java.util.*
import kotlin.jvm.optionals.getOrNull


class PifflingPumpkinEntity(entityType: EntityType<out PifflingPumpkinEntity>, world: World) :
    GolemEntity(entityType, world), Angerable {
    private var targetUuid: UUID? = null
//    var stateTicks: Int = 0
//    val twitchAnimationState: AnimationState = AnimationState()

    init {
        this.setCanPickUpLoot(true)
        Arrays.fill(this.handDropChances, 2f)
        Arrays.fill(this.armorDropChances, 0f)
    }

    override fun initGoals() {
        goalSelector.add(0, MeleeAttackGoal(this, 1.0, true))
        goalSelector.add(1, EscapeDangerGoal(this, 2.0))
        goalSelector.add(2, WanderAroundPoint(this, this.summonedPos, 1.0))
        goalSelector.add(4, GoToWalkTargetGoal(this, 1.0))
        goalSelector.add(8, WanderAroundFarGoal(this, 1.0, 1f))
        goalSelector.add(9, LookAtEntityGoal(this, PlayerEntity::class.java, 6f))
        goalSelector.add(10, LookAroundGoal(this))
        targetSelector.add(1, RevengeGoal(this, *arrayOfNulls(0)))
        targetSelector.add(2, UniversalAngerGoal(this, true))
    }

    override fun initialize(
        world: ServerWorldAccess,
        difficulty: LocalDifficulty,
        spawnReason: SpawnReason,
        entityData: EntityData?
    ): EntityData? {
        summonedPos = this.blockPos
        super.initialize(world, difficulty, spawnReason, entityData)
        this.isLeftHanded = world.random.nextInt(1) == 1
        return entityData
    }

    override fun initDataTracker(builder: DataTracker.Builder) {
        super.initDataTracker(builder)
        builder.add(SUMMON_POS, Optional.empty())
        builder.add(ANGER_TIME, 0)
    }

    override fun writeCustomDataToNbt(nbt: NbtCompound) {
        super.writeCustomDataToNbt(nbt)
        this.writeAngerToNbt(nbt)
    }

    override fun readCustomDataFromNbt(nbt: NbtCompound) {
        super.readCustomDataFromNbt(nbt)
        this.readAngerFromNbt(world, nbt)
    }

    override fun tick() {
        if (world.isClient()) {
            this.updateAnimationStates()
        }
        super.tick()
    }

    override fun tryEquip(equipment: ItemStack): ItemStack {
        if (this.canPickupItem(equipment)) {
            var currentStack = this.getEquippedStack(EquipmentSlot.MAINHAND)
            if (this.prefersNewEquipment(equipment, currentStack))
                return tryEquip(equipment, currentStack, EquipmentSlot.MAINHAND)
            currentStack = this.getEquippedStack(EquipmentSlot.OFFHAND)
            if (this.prefersNewEquipment(equipment, currentStack))
                return tryEquip(equipment, currentStack, EquipmentSlot.OFFHAND)
        }
        return ItemStack.EMPTY
    }

    private fun tryEquip(newItem: ItemStack, oldItem: ItemStack, equipmentSlot: EquipmentSlot): ItemStack {
        if (!oldItem.isEmpty) {
            this.dropStack(oldItem)
        }
        val itemStack2 = equipmentSlot.split(newItem)
        this.equipLootStack(equipmentSlot, itemStack2)
        return itemStack2
    }

    override fun canEquip(stack: ItemStack): Boolean {
        return super.canEquip(stack)
    }

    override fun canPickupItem(stack: ItemStack): Boolean = this.canPickUpLoot()

    var summonedPos: BlockPos?
        get() = dataTracker[SUMMON_POS].getOrNull()
        set(summonedPos) {
            dataTracker[SUMMON_POS] = Optional.ofNullable(summonedPos)
        }

    override fun getAngerTime(): Int {
        return dataTracker.get(ANGER_TIME)
    }

    override fun setAngerTime(ticks: Int) {
        dataTracker.set(ANGER_TIME, ticks)
    }

    override fun getAngryAt(): UUID? {
        return this.targetUuid
    }

    override fun setAngryAt(uuid: UUID?) {
        this.targetUuid = uuid
    }

    override fun chooseRandomAngerTime() {
        this.angerTime = ANGER_TIME_RANGE[random]
    }

    override fun getHurtSound(source: DamageSource): SoundEvent? {
        return SoundEvents.ENTITY_IRON_GOLEM_HURT
    }

    override fun getDeathSound(): SoundEvent? {
        return SoundEvents.ENTITY_IRON_GOLEM_DEATH
    }

    private fun updateAnimationStates() {
//        if (this.stateTicks < twitchLength) {
//            this.stateTicks++
//            twitchAnimationState.start(this.age)
//        } else {
//            twitchAnimationState.stop()
//        }
    }

    override fun isPersistent(): Boolean {
        return true
    }

    companion object {
        private val SUMMON_POS: TrackedData<Optional<BlockPos>> = DataTracker.registerData(
            PifflingPumpkinEntity::class.java,
            TrackedDataHandlerRegistry.OPTIONAL_BLOCK_POS
        )
        private val ANGER_TIME: TrackedData<Int> = DataTracker.registerData(
            PifflingPumpkinEntity::class.java,
            TrackedDataHandlerRegistry.INTEGER
        )
        private val ANGER_TIME_RANGE: UniformIntProvider = TimeHelper.betweenSeconds(60, 180)

        fun createAttributes(): DefaultAttributeContainer.Builder {
            return MobEntity.createAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 10.0)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.2)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 3.0)
        }
    }
}