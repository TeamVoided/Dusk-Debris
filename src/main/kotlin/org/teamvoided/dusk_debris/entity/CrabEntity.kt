//package org.teamvoided.dusk_autumn.entity
//
//
//import net.minecraft.block.BlockState
//import net.minecraft.component.DataComponentTypes
//import net.minecraft.entity.*
//import net.minecraft.entity.ai.control.AquaticMoveControl
//import net.minecraft.entity.ai.goal.*
//import net.minecraft.entity.ai.pathing.PathNodeType
//import net.minecraft.entity.attribute.DefaultAttributeContainer
//import net.minecraft.entity.attribute.EntityAttributes
//import net.minecraft.entity.damage.DamageSource
//import net.minecraft.entity.data.DataTracker
//import net.minecraft.entity.data.TrackedData
//import net.minecraft.entity.data.TrackedDataHandlerRegistry
//import net.minecraft.entity.mob.Angerable
//import net.minecraft.entity.mob.MobEntity
//import net.minecraft.entity.passive.AnimalEntity
//import net.minecraft.entity.passive.PassiveEntity
//import org.teamvoided.dusk_autumn.data.DuskEntityTypeTags
//import net.minecraft.entity.player.PlayerEntity
//import net.minecraft.item.ItemStack
//import net.minecraft.server.world.ServerWorld
//import net.minecraft.sound.SoundEvent
//import net.minecraft.sound.SoundEvents
//import net.minecraft.util.TimeHelper
//import net.minecraft.util.math.BlockPos
//import net.minecraft.util.math.int_provider.UniformIntProvider
//import net.minecraft.world.LocalDifficulty
//import net.minecraft.world.ServerWorldAccess
//import net.minecraft.world.World
//import org.teamvoided.dusk_autumn.data.DuskItemTags
//import org.teamvoided.dusk_debris.init.DuskEntities
//import software.bernie.geckolib.animatable.GeoEntity
//import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache
//import software.bernie.geckolib.animation.AnimatableManager
//import software.bernie.geckolib.animation.AnimationController
//import software.bernie.geckolib.animation.RawAnimation
//import software.bernie.geckolib.util.GeckoLibUtil
//import java.util.*
//
//
//class CrabEntity : AnimalEntity, Angerable, GeoEntity {
//    private val cache: AnimatableInstanceCache = GeckoLibUtil.createInstanceCache(this)
//    private val fallDamageReduction = 5
//    private val TARGET_ENTITY_ID: TrackedData<OptionalInt>? = null
//    private var targetUuid: UUID? = null
//
//    init {
//        this.addPathfindingPenalty(PathNodeType.WATER, 0.0f)
//        this.addPathfindingPenalty(PathNodeType.TRAPDOOR, -1.0f)
//        this.moveControl = AquaticMoveControl(this, 85, 20, swimSpeed, landSpeed, false)
//    }
//
//    constructor(entityType: EntityType<out AnimalEntity>, world: World) : super(entityType, world)
//    constructor(world: World) : super(DuskEntities.CRAB, world)
//
//    override fun registerControllers(controllers: AnimatableManager.ControllerRegistrar) {
//        controllers.add(
//            AnimationController(
//                this, "wave", 5
//            ) { state ->
//                state.setAndContinue(
//                    RawAnimation.begin().thenPlay("wave")
//                )
//            }
//        )
//    }
//
//    override fun initGoals() {
////        goalSelector.add(1, SwimGoal(this))
////        goalSelector.add(1, WolfEscapeDangerGoal(this, 1.5))
////        goalSelector.add(
////            3, AvoidLlamaGoal(
////                this,
////                LlamaEntity::class.java, 24.0f, 1.5, 1.5
////            )
////        )
//        goalSelector.add(4, MeleeAttackGoal(this, 1.0, true))
//        goalSelector.add(5, PounceAtTargetGoal(this, 0.4f))
//        goalSelector.add(7, AnimalMateGoal(this, 1.0))
////        goalSelector.add(8, PickupItemGoal())
//        goalSelector.add(9, WanderAroundFarGoal(this, 1.0))
//        goalSelector.add(
//            10, LookAtEntityGoal(
//                this,
//                PlayerEntity::class.java, 8.0f
//            )
//        )
//        goalSelector.add(
//            10, LookAtEntityGoal(
//                this,
//                LivingEntity::class.java, 5.0f
//            )
//        )
//        goalSelector.add(10, LookAroundGoal(this))
//        targetSelector.add(
//            1, RevengeGoal(this, *arrayOfNulls(0)).setGroupRevenge(*arrayOfNulls(0))
//        )
//        targetSelector.add(2, UniversalAngerGoal(this, true))
//        targetSelector.add(3,
//            TargetGoal(
//                this,
//                LivingEntity::class.java, 10, true, true
//            ) { entity -> entity.type.isIn(DuskEntityTypeTags.CRAB_ATTACKS) })
//    }
//
//    override fun initDataTracker(builder: DataTracker.Builder) {
//        super.initDataTracker(builder)
//        builder.add(ANGER_TIME, 0)
//    }
//
////    internal class PickupItemGoal : Goal() {
////        init {
////            this.controls = EnumSet.of(Control.MOVE)
////        }
////
////        override fun canStart(): Boolean {
////            if (!this.getEquippedStack(EquipmentSlot.MAINHAND).isEmpty()) {
////                return false
////            } else if (this.getTarget() == null && this.getAttacker() == null) {
////                if (!this.wantsToPickupItem()) {
////                    return false
////                } else if (this.getRandom().nextInt(toGoalTicks(10)) != 0) {
////                    return false
////                } else {
////                    val list: List<ItemEntity> = this.getWorld().getEntitiesByClass<ItemEntity>(
////                        ItemEntity::class.java,
////                        this.getBounds().expand(8.0, 8.0, 8.0),
////                        PICKABLE_DROP_FILTER
////                    )
////                    return list.isNotEmpty() && this.getEquippedStack(EquipmentSlot.MAINHAND).isEmpty()
////                }
////            } else {
////                return false
////            }
////        }
////
////        override fun tick() {
////            val list: List<ItemEntity> = this.getWorld().getEntitiesByClass<ItemEntity>(
////                ItemEntity::class.java, this.getBounds().expand(8.0, 8.0, 8.0), PICKABLE_DROP_FILTER
////            )
////            val itemStack: ItemStack = this.getEquippedStack(EquipmentSlot.MAINHAND)
////            if (itemStack.isEmpty && list.isNotEmpty()) {
////                this.getNavigation().startMovingTo(list[0], 1.2)
////            }
////        }
////
////        override fun start() {
////            val list: List<ItemEntity> = this.getWorld().getEntitiesByClass<ItemEntity>(
////                ItemEntity::class.java, this.getBounds().expand(8.0, 8.0, 8.0), PICKABLE_DROP_FILTER
////            )
////            if (list.isNotEmpty()) {
////                this.getNavigation().startMovingTo(list[0], 1.2)
////            }
////        }
////    }
//
//    fun wantsToPickupItem(): Boolean {
//        return !this.isSleeping
//    }
//
//    override fun tickMovement() {
//        if (!world.isClient && this.isAlive && this.canAiMove()) {
//            ++eatingTime
//            val itemStack = this.getEquippedStack(EquipmentSlot.MAINHAND)
//            if (this.canEat(itemStack)) {
//                if (eatingTime > 600) {
//                    val itemStack2 = itemStack.finishUsing(this.world, this)
//                    if (!itemStack2.isEmpty) {
//                        this.equipStack(EquipmentSlot.MAINHAND, itemStack2)
//                    }
//
//                    eatingTime = 0
//                } else if (eatingTime > 560 && random.nextFloat() < 0.1f) {
//                    this.playSound(this.getEatSound(itemStack), 1.0f, 1.0f)
//                    world.sendEntityStatus(this, 45.toByte())
//                }
//            }
//        }
//        super.tickMovement()
//    }
//
//    private fun canEat(stack: ItemStack): Boolean {
//        return stack.contains(DataComponentTypes.FOOD) && this.target == null && this.isOnGround
//    }
//
//    override fun canEquip(stack: ItemStack?): Boolean {
//        val equipmentSlot = getPreferredEquipmentSlot(stack)
//        return if (!getEquippedStack(equipmentSlot).isEmpty) {
//            false
//        } else {
//            equipmentSlot == EquipmentSlot.MAINHAND && super.canEquip(stack)
//        }
//    }
//
//    override fun canPickupItem(stack: ItemStack): Boolean {
//        val itemStack = this.getEquippedStack(EquipmentSlot.MAINHAND)
//        return itemStack.isEmpty || eatingTime > 0 && stack.contains(DataComponentTypes.FOOD) && !itemStack.contains(
//            DataComponentTypes.FOOD
//        )
//    }
//
//    override fun loot(item: ItemEntity) {
//        val itemStack = item.stack
//        if (this.canPickupItem(itemStack)) {
//            val i = itemStack.count
//            if (i > 1) {
////                this.dropItem(itemStack.split(i - 1))
//            }
//
//            this.spit(this.getEquippedStack(EquipmentSlot.MAINHAND))
//            this.triggerItemPickedUpByEntityCriteria(item)
//            this.equipStack(EquipmentSlot.MAINHAND, itemStack.split(1))
//            this.updateDropChances(EquipmentSlot.MAINHAND)
//            this.sendPickup(item, itemStack.count)
//            item.discard()
//            eatingTime = 0
//        }
//    }
//
//    private fun spit(stack: ItemStack) {
//        if (!stack.isEmpty && !world.isClient) {
//            val itemEntity = ItemEntity(
//                this.world,
//                this.x + this.rotationVector.x,
//                this.y + 1.0,
//                this.z + this.rotationVector.z, stack
//            )
//            itemEntity.setPickupDelay(40)
//            itemEntity.setThrower(this)
//            this.playSound(SoundEvents.ENTITY_FOX_SPIT, 1.0f, 1.0f)
//            world.spawnEntity(itemEntity)
//        }
//    }
//
//    override fun createChild(world: ServerWorld, entity: PassiveEntity): PassiveEntity? = null
//
//    override fun isBreedingItem(stack: ItemStack): Boolean = stack.isIn(DuskItemTags.CRAB_FOOD)
//
//    override fun computeFallDamage(fallDistance: Float, damageMultiplier: Float): Int {
//        return super.computeFallDamage(fallDistance, damageMultiplier) - fallDamageReduction
//    }
//
//    override fun initialize(
//        world: ServerWorldAccess,
//        difficulty: LocalDifficulty,
//        spawnReason: SpawnReason,
//        entityData: EntityData?
//    ): EntityData? {
//        return super.initialize(world, difficulty, spawnReason, entityData)
//    }
//
//    override fun getAmbientSound(): SoundEvent = SoundEvents.ENTITY_WITHER_SKELETON_AMBIENT
//    override fun playStepSound(pos: BlockPos, state: BlockState) {
//        this.playSound(SoundEvents.ENTITY_SPIDER_STEP, 0.15f, 1.0f)
//    }
//
//    override fun getHurtSound(source: DamageSource): SoundEvent = SoundEvents.ENTITY_WITHER_SKELETON_HURT
//    override fun getDeathSound(): SoundEvent = SoundEvents.ENTITY_WITHER_SKELETON_DEATH
//
//    override fun isPushedByFluids(): Boolean = false
//    override fun getAnimatableInstanceCache(): AnimatableInstanceCache = cache
//    override fun getAngerTime(): Int {
//        return dataTracker.get(ANGER_TIME)
//    }
//
//    override fun setAngerTime(ticks: Int) {
//        dataTracker.set(ANGER_TIME, ticks)
//    }
//
//    override fun chooseRandomAngerTime() {
//        this.angerTime = ANGER_TIME_RANGE[random]
//    }
//
//    override fun getAngryAt(): UUID? {
//        return this.targetUuid
//    }
//
//    override fun setAngryAt(uuid: UUID?) {
//        this.targetUuid = uuid
//    }
//
//    companion object {
//        fun createAttributes(): DefaultAttributeContainer.Builder {
//            return MobEntity.createAttributes()
//                .add(EntityAttributes.GENERIC_ARMOR, 4.0)
//                .add(EntityAttributes.GENERIC_MAX_HEALTH, 7.0)
//                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 4.0)
//                .add(EntityAttributes.GENERIC_STEP_HEIGHT, 1.0)
//        }
//
//        private val landSpeed = 0.6f
//        private val swimSpeed = 0.02f
//        private var eatingTime = 0
//        private val ANGER_TIME: TrackedData<Int> =
//            DataTracker.registerData(CrabEntity::class.java, TrackedDataHandlerRegistry.INTEGER)
//        private val ANGER_TIME_RANGE: UniformIntProvider = TimeHelper.betweenSeconds(20, 39)
//        private val PICKABLE_DROP_FILTER: (ItemEntity) -> Boolean = ({ item: ItemEntity -> !item.cannotPickup() && item.isAlive })
//    }
//}
