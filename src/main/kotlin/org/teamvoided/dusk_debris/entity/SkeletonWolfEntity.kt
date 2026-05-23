package org.teamvoided.dusk_debris.entity

import net.minecraft.nbt.CompoundTag
import net.minecraft.network.syncher.EntityDataAccessor
import net.minecraft.network.syncher.EntityDataSerializers
import net.minecraft.network.syncher.SynchedEntityData
import net.minecraft.resources.ResourceLocation
import net.minecraft.sounds.SoundEvent
import net.minecraft.sounds.SoundEvents
import net.minecraft.tags.ItemTags
import net.minecraft.util.RandomSource
import net.minecraft.util.TimeUtil
import net.minecraft.util.valueproviders.UniformInt
import net.minecraft.world.DifficultyInstance
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.*
import net.minecraft.world.entity.ai.attributes.AttributeModifier
import net.minecraft.world.entity.ai.attributes.AttributeSupplier
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal
import net.minecraft.world.entity.ai.goal.target.ResetUniversalAngerTargetGoal
import net.minecraft.world.entity.animal.Turtle
import net.minecraft.world.entity.monster.AbstractSkeleton
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.ProjectileWeaponItem
import net.minecraft.world.level.Level
import net.minecraft.world.level.ServerLevelAccessor
import org.teamvoided.dusk_debris.data.tags.DuskEntityTypeTags
import org.teamvoided.dusk_debris.init.DuskEntities
import java.util.*

open class SkeletonWolfEntity(entityType: EntityType<out SkeletonWolfEntity>, world: Level) :
    AbstractSkeleton(entityType, world), NeutralMob {

    private var targetUuid: UUID? = null
    open var fleeEntity = DuskEntityTypeTags.DUSK_SKELETON_RETREATS
    open var attackEntity = DuskEntityTypeTags.DUSK_SKELETON_ATTACKS

    override fun registerGoals() {
        goalSelector.addGoal(
            3, AvoidEntityGoal(
                this, LivingEntity::class.java, 6.0f, 1.0, 1.2
            ) { entity -> entity.type.`is`(fleeEntity) })
        goalSelector.addGoal(5, WaterAvoidingRandomStrollGoal(this, 1.0))
        goalSelector.addGoal(6, LookAtPlayerGoal(this, Player::class.java, 8.0f))
        goalSelector.addGoal(6, RandomLookAroundGoal(this))
        targetSelector.addGoal(1, HurtByTargetGoal(this, *arrayOfNulls(0)))
        targetSelector.addGoal(
            2, NearestAttackableTargetGoal(
                this, LivingEntity::class.java, true
            ) { entity ->
                (entity.type.`is`(attackEntity))
            })
        targetSelector.addGoal(
            3, NearestAttackableTargetGoal(
                this, Turtle::class.java, 10, true, false, Turtle.BABY_ON_LAND_SELECTOR
            )
        )
        targetSelector.addGoal(8, ResetUniversalAngerTargetGoal(this, true))
    }

    override fun finalizeSpawn(
        world: ServerLevelAccessor,
        difficulty: DifficultyInstance,
        spawnReason: MobSpawnType,
        entityData: SpawnGroupData?
    ): SpawnGroupData? {
        val randomGenerator = world.random
        var entityData2 = super.finalizeSpawn(world, difficulty, spawnReason, entityData)
        //val localDifficulty = difficulty.clampedLocalDifficulty
        if (entityData2 == null)
            entityData2 = SkeletonWolfData(shouldBeBaby(randomGenerator))
        if (entityData is SkeletonWolfData && entityData.baby) {
            this.isBaby = true
        }
        return entityData2
    }

    override fun populateDefaultEquipmentSlots(random: RandomSource, difficulty: DifficultyInstance) {
//        this.equipStack(EquipmentSlot.BODY, ItemStack(Items.BOW))
    }

    public override fun mobInteract(player: Player, hand: InteractionHand): InteractionResult {
        val itemStack = player.getItemInHand(hand)
        if (isEdibleItem(itemStack)) {
            if (!level().isClientSide && this.canEat()) {
                this.eat(player, itemStack)
                this.stopBeingAngry()
//                othersInRange.stream().filter { entity: MobEntity -> entity !== this }
//                    .map { entity: MobEntity -> entity as Angerable }
//                    .forEach { obj: Angerable -> obj.stopAnger() }
                return InteractionResult.SUCCESS
            }
            if (level().isClientSide) {
                return InteractionResult.CONSUME
            }
        }
        return super.mobInteract(player, hand)
    }

//    private val othersInRange: List<MobEntity>
//        get() {
//            val range = this.getAttributeValue(EntityAttributes.FOLLOW_RANGE)
//            val box = Box.from(this.pos).expand(range, 10.0, range)
//            return this.world.getEntitiesByClass(this.javaClass, box, EntityPredicates.EXCEPT_SPECTATOR)
//        }

    fun isEdibleItem(stack: ItemStack): Boolean {
        return stack.`is`(ItemTags.BOATS)
    }

    private fun eat(player: Player?, stack: ItemStack) {
        this.health++
        stack.consume(1, player)
    }

    private fun canEat(): Boolean {
        return this.health < this.maxHealth
    }

    override fun canFireProjectileWeapon(weapon: ProjectileWeaponItem): Boolean {
        return false
    }

    override fun getStepSound(): SoundEvent {
        return SoundEvents.SKELETON_STEP
    }

    override fun isSunBurnTick(): Boolean {
        return false
    }

    fun getTailAngle(): Float {
        if (this.isAngry) {
            return 1.5393804f
        } else {
            val f = this.maxHealth
            val g = (f - this.health) / f
            return (0.55f - g * 0.4f) * 3.1415927f
        }
    }

    override fun defineSynchedData(builder: SynchedEntityData.Builder) {
        super.defineSynchedData(builder)
        builder.define(ANGER_TIME, 0)
        builder.define(BABY, false)
    }

    override fun addAdditionalSaveData(nbt: CompoundTag) {
        super.addAdditionalSaveData(nbt)
        this.addPersistentAngerSaveData(nbt)
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

    override fun setBaby(baby: Boolean) {
        entityData.set(BABY, baby)
        if (this.level() != null && !level().isClientSide) {
            val entityAttributeInstance = this.getAttribute(Attributes.MOVEMENT_SPEED)
            entityAttributeInstance!!.removeModifier(babyKey)
            if (baby) {
                entityAttributeInstance.addTransientModifier(BABY_SPEED_BONUS)
            }
        }
    }

    public override fun getDefaultDimensions(pose: Pose): EntityDimensions {
        return if (this.isBaby) BABY_DIMENSIONS else super.getDefaultDimensions(pose)
    }


    override fun isBaby(): Boolean {
        return entityData.get(BABY) as Boolean
    }

    override fun onSyncedDataUpdated(data: EntityDataAccessor<*>) {
        if (BABY == data) {
            this.refreshDimensions()
        }
        super.onSyncedDataUpdated(data)
    }

    class SkeletonWolfData(val baby: Boolean) : SpawnGroupData

    companion object {
        private val babyKey: ResourceLocation = ResourceLocation.withDefaultNamespace("baby")
        private val BABY_SPEED_BONUS =
            AttributeModifier(babyKey, 0.5, AttributeModifier.Operation.ADD_MULTIPLIED_BASE)
        private val ANGER_TIME: EntityDataAccessor<Int> =
            SynchedEntityData.defineId(SkeletonWolfEntity::class.java, EntityDataSerializers.INT)
        private val BABY: EntityDataAccessor<Boolean> =
            SynchedEntityData.defineId(SkeletonWolfEntity::class.java, EntityDataSerializers.BOOLEAN)
        private val ANGER_TIME_RANGE: UniformInt = TimeUtil.rangeOfSeconds(20, 39)
        private val BABY_DIMENSIONS: EntityDimensions =
            DuskEntities.SKELETON_WOLF.dimensions.scale(0.5f).withEyeHeight(0.34f)

        fun createAttributes(): AttributeSupplier.Builder {
            return Mob.createMobAttributes()
                .add(Attributes.MOVEMENT_SPEED, 0.3)
                .add(Attributes.MAX_HEALTH, 8.0)
                .add(Attributes.ATTACK_DAMAGE, 4.0)
        }

        fun shouldBeBaby(random: RandomSource): Boolean {
            return random.nextFloat() < 0.05f
        }

    }
}