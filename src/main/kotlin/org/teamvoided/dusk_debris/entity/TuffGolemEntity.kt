package org.teamvoided.dusk_debris.entity

import net.minecraft.core.BlockPos
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.syncher.EntityDataAccessor
import net.minecraft.network.syncher.EntityDataSerializers
import net.minecraft.network.syncher.SynchedEntityData
import net.minecraft.sounds.SoundEvent
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.DifficultyInstance
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.effect.MobEffects
import net.minecraft.world.entity.*
import net.minecraft.world.entity.ai.attributes.AttributeSupplier
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.entity.ai.goal.*
import net.minecraft.world.entity.animal.AbstractGolem
import net.minecraft.world.entity.item.ItemEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.ServerLevelAccessor
import net.minecraft.world.level.gameevent.GameEvent
import org.teamvoided.dusk_debris.data.tags.DuskEntityTypeTags
import org.teamvoided.dusk_debris.data.tags.DuskItemTags
import org.teamvoided.dusk_debris.entity.ai.goal.PickupAndDropItemGoal
import org.teamvoided.dusk_debris.entity.ai.goal.ShowOffGoal
import org.teamvoided.dusk_debris.entity.ai.goal.TuffGolemHome
import java.util.*

class TuffGolemEntity(entityType: EntityType<out TuffGolemEntity>, world: Level) : AbstractGolem(entityType, world) {
    var stateTicks: Int = 0
    val statueAnimationState: AnimationState = AnimationState()
    val risingAnimationState: AnimationState = AnimationState()

    init {
        this.setCanPickUpLoot(true)
        Arrays.fill(this.armorDropChances, 2f)
        Arrays.fill(this.handDropChances, 2f)
        this.bodyArmorDropChance = 2f
    }

    override fun registerGoals() {
        goalSelector.addGoal(0, PanicGoal(this, 2.0))
        goalSelector.addGoal(1, TuffGolemHome(this, 1.0))
        goalSelector.addGoal(2, WaterAvoidingRandomStrollGoal(this, 1.0, 1f))
        goalSelector.addGoal(
            3, ShowOffGoal(
                this,
                this.isHoldingItem() && this.state < 1,
                { entity -> entity.type.`is`(DuskEntityTypeTags.DUSK_SKELETON_RETREATS) },
                0.01
            )
        )
        goalSelector.addGoal(4, LookAtPlayerGoal(this, Player::class.java, 6f))
        goalSelector.addGoal(5, RandomLookAroundGoal(this))
        goalSelector.addGoal(10, PickupAndDropItemGoal(this, canPickUpItem() && navigation.isDone, 0.001))
    }

    override fun finalizeSpawn(
        world: ServerLevelAccessor,
        difficulty: DifficultyInstance,
        spawnReason: MobSpawnType,
        entityData: SpawnGroupData?
    ): SpawnGroupData? {
        summonedPos = this.blockPosition()
//        if (this.getStackInHand(Hand.MAIN_HAND) == ItemStack.EMPTY && !SpawnReason.isSpawner(spawnReason)) {
//            getLootTableHand(world.toServerWorld(), this)
//        }
        return super.finalizeSpawn(world, difficulty, spawnReason, entityData)
    }
//
//    private fun getLootTableHand(world: ServerWorld, golem: TuffGolemEntity) {
//        val states = world.getLootTable(NulliumLootTables.ENDERMAN_HOLDS)
//            .generateLoot(LootContextParameterSet())
//            .mapNotNull {
//                it
//            }
//
//        if (states.isNotEmpty()) {
//            states.random().let {
//                if (!it == ItemStack.EMPTY) golem.carriedBlock = it
//            }
//        }
//    }

    override fun defineSynchedData(builder: SynchedEntityData.Builder) {
        super.defineSynchedData(builder)
        builder.define(GOLEM_STATE, statueState)
        builder.define(STATUE_TICKS, 100)
        builder.define(SUMMON_POS, BlockPos.ZERO)
        builder.define(WAS_GIVEN_ITEM, false)
        builder.define(EYE_BLOCK, "water_bucket")
    }

    override fun addAdditionalSaveData(nbt: CompoundTag) {
        super.addAdditionalSaveData(nbt)
        nbt.putInt("StatueTicks", this.statueTicks)
        nbt.putInt("GolemState", this.state)
        nbt.putInt("SummonedPosX", this.summonedPos.x)
        nbt.putInt("SummonedPosY", this.summonedPos.y)
        nbt.putInt("SummonedPosZ", this.summonedPos.z)
        nbt.putBoolean("WasGivenItem", this.wasGivenItem)
        nbt.putString("EyeBlock", this.eyeBlock)
    }

    override fun readAdditionalSaveData(nbt: CompoundTag) {
        super.readAdditionalSaveData(nbt)
        if (nbt.contains("GolemState")) {
            this.state = nbt.getInt("GolemState")
        }
        if (nbt.contains("StatueTicks")) {
            this.statueTicks = nbt.getInt("StatueTicks")
        }
        if (nbt.contains("SummonedPosX") && nbt.contains("SummonedPosY") && nbt.contains("SummonedPosZ")) {
            val summonX = nbt.getInt("SummonedPosX")
            val summonY = nbt.getInt("SummonedPosY")
            val summonZ = nbt.getInt("SummonedPosZ")
            summonedPos = BlockPos(summonX, summonY, summonZ)
        }
        if (nbt.contains("WasGivenItem")) {
            this.wasGivenItem = nbt.getBoolean("WasGivenItem")
        }
        if (nbt.contains("EyeBlock")) {
            this.eyeBlock = nbt.getString("EyeBlock")
        }
    }

    override fun tick() {
        if (level().isClientSide) {
            this.updateAnimationStates()
        }
        if (state == statueState) {
            if (statueTicks > 0) {
                val ticks = entityData.get(STATUE_TICKS)
                statueTicks = ticks - 1
                if (statueTicks % 200 == 0 &&
                    this.health < this.maxHealth &&
                    !this.hasEffect(MobEffects.REGENERATION) &&
                    random.nextInt(100) == 0
                ) {
                    this.addEffect(MobEffectInstance(MobEffects.REGENERATION, 200))
                }
            } else {
                setStateRise()
            }
        } else if (state == risingState && this.stateTicks > risingLength) {
            setStateWander()
        } else if (this.stateTicks > 100 && statueTicks < 1 && random.nextInt(10000) == 0) {
            println("------------------------------the chance occured------------------------------")
            setStateTire()
        }
        this.stateTicks++
        super.tick()
    }

    override fun updateControlFlags() {
        val bl = this.controllingPassenger !is Mob
        val bl2 = state < 1
        goalSelector.setControlFlag(Goal.Flag.MOVE, bl)
        goalSelector.setControlFlag(Goal.Flag.JUMP, bl && bl2)
        goalSelector.setControlFlag(Goal.Flag.LOOK, bl && bl2)
    }

    override fun mobInteract(player: Player, hand: InteractionHand): InteractionResult {
        //hand MAIN_HAND, drops
        //cloak CHEST, drops
        //head HEAD, drops
        //eyes custom, does not drop
        val playerHandStack = player.getItemInHand(hand)
        val golemHandStack = this.getItemInHand(InteractionHand.MAIN_HAND)
        val golemHatStack = this.getItemBySlot(EquipmentSlot.HEAD)
        val golemChestStack = this.getItemBySlot(EquipmentSlot.CHEST)
        if (!playerHandStack.isEmpty) {
            if (golemHandStack.isEmpty && !golemChestStack.isEmpty) {
                //give golem item
                this.setItemInHand(InteractionHand.MAIN_HAND, playerHandStack.copyWithCount(1))
                playerHandStack.consume(1, player)
                this.playSound(SoundEvents.ITEM_PICKUP, 1f, 1f)
                wasGivenItem = true
                return InteractionResult.SUCCESS
            } else if (golemHatStack.isEmpty && getEquipmentSlotForItem(playerHandStack) == EquipmentSlot.HEAD) {
                //give golem hat
                this.setItemSlot(EquipmentSlot.HEAD, playerHandStack.copyWithCount(1))
                playerHandStack.consume(1, player)
                this.playSound(SoundEvents.ITEM_PICKUP, 1f, 1f)
                return InteractionResult.SUCCESS
            } else if (playerHandStack.`is`(DuskItemTags.TUFF_GOLEM_CLOAK) && playerHandStack != golemChestStack) {
                //give golem cloak
                this.spit(golemChestStack)
                this.setItemSlot(EquipmentSlot.CHEST, playerHandStack.copyWithCount(1))
                playerHandStack.consume(1, player)
                this.playSound(SoundEvents.ITEM_PICKUP, 1f, 1f)
                return InteractionResult.SUCCESS
            } else if (playerHandStack.`is`(DuskItemTags.TUFF_GOLEM_EYES)) {
                //give golem eye color
                setEyeBlock(playerHandStack)
                return InteractionResult.SUCCESS_NO_ITEM_USED
            }
        } else if (hand == InteractionHand.MAIN_HAND && playerHandStack.isEmpty) {
            if (!golemHandStack.isEmpty) {
                //take golem item
                this.setItemSlot(EquipmentSlot.MAINHAND, ItemStack.EMPTY)
                this.swing(InteractionHand.MAIN_HAND)
                player.addItem(golemHandStack)
                wasGivenItem = false
                this.playSound(SoundEvents.ITEM_PICKUP, 1f, 0f)
                return InteractionResult.SUCCESS
            } else if (!golemHatStack.isEmpty) {
                //take golem hat
                this.setItemSlot(EquipmentSlot.HEAD, ItemStack.EMPTY)
                player.addItem(golemHatStack)
                this.playSound(SoundEvents.ITEM_PICKUP, 1f, 0f)
                return InteractionResult.SUCCESS
            }
        }
        return super.mobInteract(player, hand)
    }

    override fun canTakeItem(stack: ItemStack): Boolean {
        val equipmentSlot = this.getEquipmentSlotForItem(stack)
        return if (!getItemBySlot(equipmentSlot).isEmpty) {
            false
        } else {
            equipmentSlot == EquipmentSlot.MAINHAND && super.canTakeItem(stack)
        }
    }

    override fun canHoldItem(stack: ItemStack): Boolean = canPickUpItem()

    fun canPickUpItem(): Boolean =
        state == wanderingState &&
                !wasGivenItem &&
                this.getItemBySlot(EquipmentSlot.MAINHAND).isEmpty &&
                !this.getItemBySlot(EquipmentSlot.CHEST).isEmpty

    override fun pickUpItem(item: ItemEntity) {
        val itemStack = item.item
        if (this.canHoldItem(itemStack)) {
            val i = itemStack.count
            if (i > 1) {
                this.dropItem(itemStack.split(i - 1))
            }
            this.spit(this.getItemBySlot(EquipmentSlot.MAINHAND))
            this.onItemPickup(item)
            this.setItemSlot(EquipmentSlot.MAINHAND, itemStack.split(1))
            this.setGuaranteedDrop(EquipmentSlot.MAINHAND)
            this.take(item, itemStack.count)
            this.playSound(SoundEvents.ITEM_PICKUP, 1f, 1f)
            item.discard()
        }
    }

    private fun dropItem(stack: ItemStack) {
        val itemEntity = ItemEntity(this.level(), this.x, this.y, this.z, stack)
        level().addFreshEntity(itemEntity)
    }

    fun spit(stack: ItemStack) {
        if (!stack.isEmpty && !level().isClientSide) {
            val itemEntity = ItemEntity(
                this.level(),
                this.x + this.lookAngle.x,
                this.y + 1.0,
                this.z + this.lookAngle.z, stack
            )
            itemEntity.setPickUpDelay(40)
            itemEntity.setThrower(this)
            this.playSound(SoundEvents.ITEM_PICKUP, 1f, 0f)
            level().addFreshEntity(itemEntity)
        }
    }

    override fun isPushable(): Boolean {
        return state == statueState or risingState && super.isPushable()
    }

    fun isHoldingItem(): Boolean {
        return this.hasItemInSlot(EquipmentSlot.MAINHAND)
    }

    var summonedPos: BlockPos
        get() = entityData[SUMMON_POS]
        set(summonedPos) {
            entityData[SUMMON_POS] = summonedPos
        }

    var wasGivenItem: Boolean
        get() = entityData[WAS_GIVEN_ITEM]
        set(wasGivenItem) {
            entityData[WAS_GIVEN_ITEM] = wasGivenItem
        }
    var eyeBlock: String
        get() = entityData[EYE_BLOCK]
        set(eyeBlock) {
            entityData[EYE_BLOCK] = eyeBlock
        }

    private fun setEyeBlock(item: ItemStack) {
        entityData.set(EYE_BLOCK, BuiltInRegistries.ITEM.getKey(item.item).path)
    }

    var statueTicks: Int
        get() = entityData[STATUE_TICKS]
        set(statueTicks) {
            entityData[STATUE_TICKS] = statueTicks
        }

    override fun getHurtSound(source: DamageSource): SoundEvent? {
        return SoundEvents.IRON_GOLEM_HURT
    }

    override fun getDeathSound(): SoundEvent? {
        return SoundEvents.IRON_GOLEM_DEATH
    }

    val wanderingState = 0
    val tiredState = 1
    val risingState = 2
    val statueState = 3

    var state: Int
        get() = entityData[GOLEM_STATE]
        set(state) {
            entityData[GOLEM_STATE] = state
        }

    private fun updateAnimationStates() {
        when (state) {
            wanderingState, tiredState -> {
                statueAnimationState.stop()
                risingAnimationState.stop()
            }

            risingState -> {
                statueAnimationState.stop()
                risingAnimationState.startIfStopped(this.tickCount)
            }

            statueState -> {
                if (this.tickCount < 20)
                    statueAnimationState.fastForward(1, 100f)
                else
                    statueAnimationState.startIfStopped(this.tickCount)
                risingAnimationState.stop()
            }

            else -> {
                statueAnimationState.stop()
                risingAnimationState.stop()
            }
        }
    }

    fun setStateStatue() {
        this.gameEvent(GameEvent.ENTITY_ACTION)
        state = statueState
        this.stateTicks = 0
    }

    fun setStateRise() {
        this.gameEvent(GameEvent.ENTITY_ACTION)
        state = risingState
        statueTicks = 0
        this.stateTicks = 0
    }

    fun setStateTire() {
        state = tiredState
        statueTicks = random.nextInt(9600) + 1200
        this.stateTicks = 0
    }

    fun setStateWander() {
        state = wanderingState
        this.stateTicks = 0
    }

    companion object {
        private val GOLEM_STATE: EntityDataAccessor<Int> =
            SynchedEntityData.defineId(TuffGolemEntity::class.java, EntityDataSerializers.INT)

        private val STATUE_TICKS: EntityDataAccessor<Int> =
            SynchedEntityData.defineId(TuffGolemEntity::class.java, EntityDataSerializers.INT)
        private val SUMMON_POS: EntityDataAccessor<BlockPos> =
            SynchedEntityData.defineId(TuffGolemEntity::class.java, EntityDataSerializers.BLOCK_POS)
        private val WAS_GIVEN_ITEM: EntityDataAccessor<Boolean> =
            SynchedEntityData.defineId(TuffGolemEntity::class.java, EntityDataSerializers.BOOLEAN)
        private val EYE_BLOCK: EntityDataAccessor<String> =
            SynchedEntityData.defineId(TuffGolemEntity::class.java, EntityDataSerializers.STRING)

        val risingLength = 20

        fun createAttributes(): AttributeSupplier.Builder {
            return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 10.0)
                .add(Attributes.ARMOR, 10.0)
                .add(Attributes.MOVEMENT_SPEED, 0.15)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1.0)
        }
    }
}