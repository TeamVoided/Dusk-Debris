package org.teamvoided.dusk_debris.entity

import net.minecraft.entity.*
import net.minecraft.entity.ai.goal.*
import net.minecraft.entity.attribute.DefaultAttributeContainer
import net.minecraft.entity.attribute.EntityAttributeModifier
import net.minecraft.entity.attribute.EntityAttributes
import net.minecraft.entity.data.DataTracker
import net.minecraft.entity.data.TrackedData
import net.minecraft.entity.data.TrackedDataHandlerRegistry
import net.minecraft.entity.mob.*
import net.minecraft.entity.passive.TurtleEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.item.RangedWeaponItem
import net.minecraft.nbt.NbtCompound
import net.minecraft.registry.tag.ItemTags
import net.minecraft.sound.SoundEvent
import net.minecraft.sound.SoundEvents
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.Identifier
import net.minecraft.util.TimeHelper
import net.minecraft.util.math.int_provider.UniformIntProvider
import net.minecraft.util.random.RandomGenerator
import net.minecraft.world.LocalDifficulty
import net.minecraft.world.ServerWorldAccess
import net.minecraft.world.World
import org.teamvoided.dusk_debris.data.tags.DuskEntityTypeTags
import org.teamvoided.dusk_debris.init.DuskEntities
import java.util.*

open class SkeletonWolfEntity(entityType: EntityType<out SkeletonWolfEntity>, world: World) :
    AbstractSkeletonEntity(entityType, world), Angerable {

    private var targetUuid: UUID? = null
    open var fleeEntity = DuskEntityTypeTags.DUSK_SKELETON_RETREATS
    open var attackEntity = DuskEntityTypeTags.DUSK_SKELETON_ATTACKS

    override fun initGoals() {
        goalSelector.add(
            3, FleeEntityGoal(
                this, LivingEntity::class.java, 6.0f, 1.0, 1.2
            ) { entity -> entity.type.isIn(fleeEntity) })
        goalSelector.add(5, WanderAroundFarGoal(this, 1.0))
        goalSelector.add(6, LookAtEntityGoal(this, PlayerEntity::class.java, 8.0f))
        goalSelector.add(6, LookAroundGoal(this))
        targetSelector.add(1, RevengeGoal(this, *arrayOfNulls(0)))
        targetSelector.add(
            2, TargetGoal(
                this, LivingEntity::class.java, true
            ) { entity ->
                (entity.type.isIn(attackEntity))
            })
        targetSelector.add(
            3, TargetGoal(
                this, TurtleEntity::class.java, 10, true, false, TurtleEntity.BABY_TURTLE_ON_LAND_FILTER
            )
        )
        targetSelector.add(8, UniversalAngerGoal(this, true))
    }

    override fun initialize(
        world: ServerWorldAccess,
        difficulty: LocalDifficulty,
        spawnReason: SpawnReason,
        entityData: EntityData?
    ): EntityData? {
        val randomGenerator = world.random
        var entityData2 = super.initialize(world, difficulty, spawnReason, entityData)
        //val localDifficulty = difficulty.clampedLocalDifficulty
        if (entityData2 == null)
            entityData2 = SkeletonWolfData(shouldBeBaby(randomGenerator))
        if (entityData is SkeletonWolfData && entityData.baby) {
            this.isBaby = true
        }
        return entityData2
    }

    override fun initEquipment(random: RandomGenerator, difficulty: LocalDifficulty) {
//        this.equipStack(EquipmentSlot.BODY, ItemStack(Items.BOW))
    }

    override fun method_6110(): Int {
        if (this.isBaby) {
            this.experiencePoints = (experiencePoints.toDouble() * 2.5).toInt()
        }
        return super.method_6110()
    }

    public override fun interactMob(player: PlayerEntity, hand: Hand): ActionResult {
        val itemStack = player.getStackInHand(hand)
        if (isEdibleItem(itemStack)) {
            if (!world.isClient && this.canEat()) {
                this.eat(player, itemStack)
                this.stopAnger()
//                othersInRange.stream().filter { entity: MobEntity -> entity !== this }
//                    .map { entity: MobEntity -> entity as Angerable }
//                    .forEach { obj: Angerable -> obj.stopAnger() }
                return ActionResult.SUCCESS
            }
            if (world.isClient) {
                return ActionResult.CONSUME
            }
        }
        return super.interactMob(player, hand)
    }

//    private val othersInRange: List<MobEntity>
//        get() {
//            val range = this.getAttributeValue(EntityAttributes.GENERIC_FOLLOW_RANGE)
//            val box = Box.from(this.pos).expand(range, 10.0, range)
//            return this.world.getEntitiesByClass(this.javaClass, box, EntityPredicates.EXCEPT_SPECTATOR)
//        }

    fun isEdibleItem(stack: ItemStack): Boolean {
        return stack.isIn(ItemTags.BOATS)
    }

    private fun eat(player: PlayerEntity?, stack: ItemStack) {
        this.health++
        stack.consume(1, player)
    }

    private fun canEat(): Boolean {
        return this.health < this.maxHealth
    }

    override fun canUseRangedWeapon(weapon: RangedWeaponItem): Boolean {
        return false
    }

    override fun getStepSound(): SoundEvent {
        return SoundEvents.ENTITY_SKELETON_STEP
    }

    override fun isAffectedByDaylight(): Boolean {
        return false
    }

    fun getTailAngle(): Float {
        if (this.hasAngerTime()) {
            return 1.5393804f
        } else {
            val f = this.maxHealth
            val g = (f - this.health) / f
            return (0.55f - g * 0.4f) * 3.1415927f
        }
    }

    override fun initDataTracker(builder: DataTracker.Builder) {
        super.initDataTracker(builder)
        builder.add(ANGER_TIME, 0)
        builder.add(BABY, false)
    }

    override fun writeCustomDataToNbt(nbt: NbtCompound) {
        super.writeCustomDataToNbt(nbt)
        this.writeAngerToNbt(nbt)
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

    override fun setBaby(baby: Boolean) {
        getDataTracker().set(BABY, baby)
        if (this.world != null && !world.isClient) {
            val entityAttributeInstance = this.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED)
            entityAttributeInstance!!.removeModifier(babyKey)
            if (baby) {
                entityAttributeInstance.addTemporaryModifier(BABY_SPEED_BONUS)
            }
        }
    }

    public override fun getDefaultDimensions(pose: EntityPose): EntityDimensions {
        return if (this.isBaby) BABY_DIMENSIONS else super.getDefaultDimensions(pose)
    }


    override fun isBaby(): Boolean {
        return getDataTracker().get(BABY) as Boolean
    }

    override fun onTrackedDataSet(data: TrackedData<*>) {
        if (BABY == data) {
            this.calculateDimensions()
        }
        super.onTrackedDataSet(data)
    }

    class SkeletonWolfData(val baby: Boolean) : EntityData

    companion object {
        private val babyKey: Identifier = Identifier.ofDefault("baby")
        private val BABY_SPEED_BONUS =
            EntityAttributeModifier(babyKey, 0.5, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE)
        private val ANGER_TIME: TrackedData<Int> =
            DataTracker.registerData(SkeletonWolfEntity::class.java, TrackedDataHandlerRegistry.INTEGER)
        private val BABY: TrackedData<Boolean> =
            DataTracker.registerData(SkeletonWolfEntity::class.java, TrackedDataHandlerRegistry.BOOLEAN)
        private val ANGER_TIME_RANGE: UniformIntProvider = TimeHelper.betweenSeconds(20, 39)
        private val BABY_DIMENSIONS: EntityDimensions =
            DuskEntities.SKELETON_WOLF.dimensions.scaled(0.5f).withEyeHeight(0.34f)

        fun createAttributes(): DefaultAttributeContainer.Builder {
            return MobEntity.createAttributes()
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.3)
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 8.0)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 4.0)
        }

        fun shouldBeBaby(random: RandomGenerator): Boolean {
            return random.nextFloat() < 0.05f
        }

    }
}