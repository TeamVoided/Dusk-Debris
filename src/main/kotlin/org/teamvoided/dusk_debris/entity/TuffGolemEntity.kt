package org.teamvoided.dusk_debris.entity

import net.minecraft.entity.*
import net.minecraft.entity.ai.goal.*
import net.minecraft.entity.attribute.DefaultAttributeContainer
import net.minecraft.entity.attribute.EntityAttributes
import net.minecraft.entity.damage.DamageSource
import net.minecraft.entity.data.DataTracker
import net.minecraft.entity.data.TrackedData
import net.minecraft.entity.data.TrackedDataHandlerRegistry
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.entity.effect.StatusEffects
import net.minecraft.entity.mob.MobEntity
import net.minecraft.entity.passive.GolemEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtCompound
import net.minecraft.registry.Registries
import net.minecraft.registry.tag.ItemTags
import net.minecraft.sound.SoundEvent
import net.minecraft.sound.SoundEvents
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.math.BlockPos
import net.minecraft.world.LocalDifficulty
import net.minecraft.world.ServerWorldAccess
import net.minecraft.world.World
import net.minecraft.world.event.GameEvent
import org.teamvoided.dusk_debris.data.tags.DuskEntityTypeTags
import org.teamvoided.dusk_debris.data.tags.DuskItemTags
import org.teamvoided.dusk_debris.entity.ai.goal.PickupAndDropItemGoal
import org.teamvoided.dusk_debris.entity.ai.goal.ShowOffGoal
import org.teamvoided.dusk_debris.entity.ai.goal.TuffGolemHome
import java.util.*
import kotlin.jvm.optionals.getOrNull


class TuffGolemEntity(entityType: EntityType<out TuffGolemEntity>, world: World) : GolemEntity(entityType, world) {
    var stateTicks: Int = 0
    val statueAnimationState: AnimationState = AnimationState()
    val risingAnimationState: AnimationState = AnimationState()

    init {
        this.setCanPickUpLoot(true)
        Arrays.fill(this.armorDropChances, 2f)
        Arrays.fill(this.handDropChances, 2f)
        this.bodyArmorDropChance = 2f
    }

    override fun initGoals() {
        goalSelector.add(0, EscapeDangerGoal(this, 2.0))
        goalSelector.add(1, TuffGolemHome(this, 1.0))
        goalSelector.add(2, WanderAroundFarGoal(this, 1.0, 1f))
        goalSelector.add(
            3, ShowOffGoal(
                this,
                this.isHoldingItem() && this.state < 1,
                { entity -> entity.type.isIn(DuskEntityTypeTags.DUSK_SKELETON_RETREATS) },
                0.01
            )
        )
        goalSelector.add(4, LookAtEntityGoal(this, PlayerEntity::class.java, 6f))
        goalSelector.add(5, LookAroundGoal(this))
        goalSelector.add(10, PickupAndDropItemGoal(this, canPickUpItem() || navigation.isIdle, 0.001))
    }

    override fun initialize(
        world: ServerWorldAccess,
        difficulty: LocalDifficulty,
        spawnReason: SpawnReason,
        entityData: EntityData?
    ): EntityData? {
        summonedPos = this.blockPos
//        if (this.getStackInHand(Hand.MAIN_HAND) == ItemStack.EMPTY && !SpawnReason.isSpawner(spawnReason)) {
//            getLootTableHand(world.toServerWorld(), this)
//        }
        return super.initialize(world, difficulty, spawnReason, entityData)
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

    override fun initDataTracker(builder: DataTracker.Builder) {
        super.initDataTracker(builder)
        builder.add(GOLEM_STATE, statueState)
        builder.add(STATUE_TICKS, 100)
        builder.add(SUMMON_POS, Optional.empty())
        builder.add(WAS_GIVEN_ITEM, false)
        builder.add(EYE_BLOCK, "default")
    }

    override fun writeCustomDataToNbt(nbt: NbtCompound) {
        super.writeCustomDataToNbt(nbt)
        nbt.putInt("StatueTicks", this.statueTicks)
        nbt.putInt("GolemState", this.state)
//        if (this.getSummonedPos() != null) {
//            val posCompound = NbtCompound()
//            BlockPos.CODEC.encode(this.getSummonedPos(), NbtOps.INSTANCE, posCompound)
//            nbt.put("SummonedPos", posCompound)
//        }
        nbt.putBoolean("WasGivenItem", this.wasGivenItem)
        nbt.putString("EyeBlock", this.eyeBlock)
    }

    override fun readCustomDataFromNbt(nbt: NbtCompound) {
        super.readCustomDataFromNbt(nbt)
        if (nbt.contains("GolemState")) {
            this.state = nbt.getInt("GolemState")
        }
        if (nbt.contains("StatueTicks")) {
            this.statueTicks = nbt.getInt("StatueTicks")
        }
//        if (nbt.contains("SummonedPos")) {
//            val posCompound = nbt.get("SummonedPos")
//            this.setNewCoordinate(BlockPos.CODEC.decode(NbtOps.INSTANCE, posCompound).getOrThrow().first)
//        }
        if (nbt.contains("WasGivenItem")) {
            this.wasGivenItem = nbt.getBoolean("WasGivenItem")
        }
        if (nbt.contains("EyeBlock")) {
            this.eyeBlock = nbt.getString("EyeBlock")
        }
    }

    override fun tick() {
        if (world.isClient()) {
            this.updateAnimationStates()
        }
        if (state == statueState) {
            if (statueTicks > 0) {
                val ticks = dataTracker.get(STATUE_TICKS)
                statueTicks = ticks - 1
                if (statueTicks % 200 == 0 &&
                    this.health < this.maxHealth &&
                    !this.hasStatusEffect(StatusEffects.REGENERATION) &&
                    random.nextInt(100) == 0
                ) {
                    this.addStatusEffect(StatusEffectInstance(StatusEffects.REGENERATION, 200))
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

    override fun updateGoalControls() {
        val bl = this.primaryPassenger !is MobEntity
        val bl2 = state < 2
        goalSelector.setControlEnabled(Goal.Control.MOVE, bl)
        goalSelector.setControlEnabled(Goal.Control.JUMP, bl && bl2)
        goalSelector.setControlEnabled(Goal.Control.LOOK, bl && bl2)
    }

    override fun interactMob(player: PlayerEntity, hand: Hand): ActionResult {
        //hand MAIN_HAND, drops
        //cloak CHEST, drops
        //head HEAD, drops
        //eyes custom, does not drop
        val playerHandStack = player.getStackInHand(hand)
        val golemHandStack = this.getStackInHand(Hand.MAIN_HAND)
        val golemHatStack = this.getEquippedStack(EquipmentSlot.HEAD)
        val golemChestStack = this.getEquippedStack(EquipmentSlot.CHEST)
        if (!playerHandStack.isEmpty) {
            if (golemHandStack.isEmpty && !golemChestStack.isEmpty) {
                //give golem item
                this.setStackInHand(Hand.MAIN_HAND, playerHandStack.copyWithCount(1))
                playerHandStack.consume(1, player)
                this.playSound(SoundEvents.ENTITY_ITEM_PICKUP, 1f, 1f)
                wasGivenItem = true
                return ActionResult.SUCCESS
            } else if (golemHatStack.isEmpty && getPreferredEquipmentSlot(playerHandStack) == EquipmentSlot.HEAD) {
                //give golem hat
                this.equipStack(EquipmentSlot.HEAD, playerHandStack.copyWithCount(1))
                playerHandStack.consume(1, player)
                this.playSound(SoundEvents.ENTITY_ITEM_PICKUP, 1f, 1f)
                return ActionResult.SUCCESS
            } else if (playerHandStack.isIn(DuskItemTags.TUFF_GOLEM_CLOAK)) {
                //give golem cloak
                this.spit(golemChestStack)
                this.equipStack(EquipmentSlot.CHEST, playerHandStack.copyWithCount(1))
                playerHandStack.consume(1, player)
                this.playSound(SoundEvents.ENTITY_ITEM_PICKUP, 1f, 1f)
                return ActionResult.SUCCESS
            } else if (playerHandStack.isIn(DuskItemTags.TUFF_GOLEM_EYES)) {
                //give golem eye color
                setEyeBlock(playerHandStack)
                return ActionResult.SUCCESS_NO_ITEM_USED
            }
        } else if (hand == Hand.MAIN_HAND && playerHandStack.isEmpty) {
            if (!golemHandStack.isEmpty) {
                //take golem item
                this.equipStack(EquipmentSlot.MAINHAND, ItemStack.EMPTY)
                this.swingHand(Hand.MAIN_HAND)
                player.giveItemStack(golemHandStack)
                wasGivenItem = false
                this.playSound(SoundEvents.ENTITY_ITEM_PICKUP, 1f, 0f)
                return ActionResult.SUCCESS
            } else if (!golemHatStack.isEmpty) {
                //take golem hat
                this.equipStack(EquipmentSlot.HEAD, ItemStack.EMPTY)
                player.giveItemStack(golemHatStack)
                this.playSound(SoundEvents.ENTITY_ITEM_PICKUP, 1f, 0f)
                return ActionResult.SUCCESS
            }
        }
        return super.interactMob(player, hand)
    }

    override fun canEquip(stack: ItemStack): Boolean {
        val equipmentSlot = this.getPreferredEquipmentSlot(stack)
        return if (!getEquippedStack(equipmentSlot).isEmpty) {
            false
        } else {
            equipmentSlot == EquipmentSlot.MAINHAND && super.canEquip(stack)
        }
    }

    override fun canPickupItem(stack: ItemStack): Boolean = canPickUpItem()

    fun canPickUpItem(): Boolean =
        state == wanderingState &&
                !wasGivenItem &&
                this.getEquippedStack(EquipmentSlot.MAINHAND).isEmpty &&
                !this.getEquippedStack(EquipmentSlot.CHEST).isEmpty

    override fun loot(item: ItemEntity) {
        val itemStack = item.stack
        if (this.canPickupItem(itemStack)) {
            val i = itemStack.count
            if (i > 1) {
                this.dropItem(itemStack.split(i - 1))
            }
            this.spit(this.getEquippedStack(EquipmentSlot.MAINHAND))
            this.triggerItemPickedUpByEntityCriteria(item)
            this.equipStack(EquipmentSlot.MAINHAND, itemStack.split(1))
            this.updateDropChances(EquipmentSlot.MAINHAND)
            this.sendPickup(item, itemStack.count)
            this.playSound(SoundEvents.ENTITY_ITEM_PICKUP, 1f, 1f)
            item.discard()
        }
    }

    private fun dropItem(stack: ItemStack) {
        val itemEntity = ItemEntity(this.world, this.x, this.y, this.z, stack)
        world.spawnEntity(itemEntity)
    }

    fun spit(stack: ItemStack) {
        if (!stack.isEmpty && !world.isClient) {
            val itemEntity = ItemEntity(
                this.world,
                this.x + this.rotationVector.x,
                this.y + 1.0,
                this.z + this.rotationVector.z, stack
            )
            itemEntity.setPickupDelay(40)
            itemEntity.setThrower(this)
            this.playSound(SoundEvents.ENTITY_ITEM_PICKUP, 1f, 0f)
            world.spawnEntity(itemEntity)
        }
    }

    override fun isPushable(): Boolean {
        return state == statueState or risingState && super.isPushable()
    }

    fun isHoldingItem(): Boolean {
        return this.hasStackEquipped(EquipmentSlot.MAINHAND)
    }

    var summonedPos: BlockPos?
        get() = dataTracker[SUMMON_POS].getOrNull()
        set(summonedPos) {
            dataTracker[SUMMON_POS] = Optional.ofNullable(summonedPos)
        }

    var wasGivenItem: Boolean
        get() = dataTracker[WAS_GIVEN_ITEM]
        set(wasGivenItem) {
            dataTracker[WAS_GIVEN_ITEM] = wasGivenItem
        }
    var eyeBlock: String
        get() = dataTracker[EYE_BLOCK]
        set(eyeBlock) {
            dataTracker[EYE_BLOCK] = eyeBlock
        }

    private fun setEyeBlock(item: ItemStack) {
        dataTracker.set(EYE_BLOCK, Registries.ITEM.getId(item.item).path)
    }

    var statueTicks: Int
        get() = dataTracker[STATUE_TICKS]
        set(statueTicks) {
            dataTracker[STATUE_TICKS] = statueTicks
        }

    override fun getHurtSound(source: DamageSource): SoundEvent? {
        return SoundEvents.ENTITY_IRON_GOLEM_HURT
    }

    override fun getDeathSound(): SoundEvent? {
        return SoundEvents.ENTITY_IRON_GOLEM_DEATH
    }

    val wanderingState = 0
    val tiredState = 1
    val risingState = 2
    val statueState = 3

    var state: Int
        get() = dataTracker[GOLEM_STATE]
        set(state) {
            dataTracker[GOLEM_STATE] = state
        }

    private fun updateAnimationStates() {
        when (state) {
            wanderingState, tiredState -> {
                statueAnimationState.stop()
                risingAnimationState.stop()
            }

            risingState -> {
                statueAnimationState.stop()
                risingAnimationState.start(this.age)
            }

            statueState -> {
                if (this.age < 20)
                    statueAnimationState.fastForward(1, 100f)
                else
                    statueAnimationState.start(this.age)
                risingAnimationState.stop()
            }

            else -> {
                statueAnimationState.stop()
                risingAnimationState.stop()
            }
        }
    }

    fun setStateStatue() {
        this.emitGameEvent(GameEvent.ENTITY_ACTION)
        state = statueState
        this.stateTicks = 0
    }

    fun setStateRise() {
        this.emitGameEvent(GameEvent.ENTITY_ACTION)
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
        private val GOLEM_STATE: TrackedData<Int> =
            DataTracker.registerData(TuffGolemEntity::class.java, TrackedDataHandlerRegistry.INTEGER)

        private val STATUE_TICKS: TrackedData<Int> =
            DataTracker.registerData(TuffGolemEntity::class.java, TrackedDataHandlerRegistry.INTEGER)
        private val SUMMON_POS: TrackedData<Optional<BlockPos>> =
            DataTracker.registerData(TuffGolemEntity::class.java, TrackedDataHandlerRegistry.OPTIONAL_BLOCK_POS)
        private val WAS_GIVEN_ITEM: TrackedData<Boolean> =
            DataTracker.registerData(TuffGolemEntity::class.java, TrackedDataHandlerRegistry.BOOLEAN)
        private val EYE_BLOCK: TrackedData<String> =
            DataTracker.registerData(TuffGolemEntity::class.java, TrackedDataHandlerRegistry.STRING)

        val risingLength = 20

        fun createAttributes(): DefaultAttributeContainer.Builder {
            return MobEntity.createAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 10.0)
                .add(EntityAttributes.GENERIC_ARMOR, 10.0)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.15)
                .add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 1.0)
        }
    }
}