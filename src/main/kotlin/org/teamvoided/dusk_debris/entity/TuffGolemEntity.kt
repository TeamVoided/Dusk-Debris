package org.teamvoided.dusk_debris.entity

import io.netty.buffer.ByteBuf
import net.minecraft.entity.*
import net.minecraft.entity.ai.goal.Goal
import net.minecraft.entity.ai.goal.LookAroundGoal
import net.minecraft.entity.ai.goal.LookAtEntityGoal
import net.minecraft.entity.ai.goal.WanderAroundFarGoal
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
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.codec.PacketCodecs
import net.minecraft.registry.Registries
import net.minecraft.registry.tag.ItemTags
import net.minecraft.sound.SoundEvent
import net.minecraft.sound.SoundEvents
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.StringIdentifiable
import net.minecraft.util.collection.IdListUtil
import net.minecraft.util.math.BlockPos
import net.minecraft.world.LocalDifficulty
import net.minecraft.world.ServerWorldAccess
import net.minecraft.world.World
import net.minecraft.world.event.GameEvent
import org.teamvoided.dusk_debris.entity.ai.goal.PickupAndDropItemGoal
import org.teamvoided.dusk_debris.entity.ai.goal.TuffGolemHome
import org.teamvoided.dusk_debris.entity.data.DuskTrackedDataHandlerRegistry
import java.util.*
import java.util.function.IntFunction
import kotlin.jvm.optionals.getOrNull


class TuffGolemEntity(entityType: EntityType<out TuffGolemEntity>, world: World) : GolemEntity(entityType, world) {
    val statueState: AnimationState = AnimationState()
    val risingState: AnimationState = AnimationState()

    init {
        this.setCanPickUpLoot(true)
        Arrays.fill(this.armorDropChances, 2f)
        Arrays.fill(this.handDropChances, 2f)
        this.bodyArmorDropChance = 2f
    }

    override fun initGoals() {
        goalSelector.add(1, TuffGolemHome(this, 1.0))
        goalSelector.add(2, WanderAroundFarGoal(this, 1.0, 1f))
        goalSelector.add(3, LookAtEntityGoal(this, PlayerEntity::class.java, 6f))
        goalSelector.add(4, LookAroundGoal(this))
        goalSelector.add(5, PickupAndDropItemGoal(this, 0.1))
    }

    override fun initialize(
        world: ServerWorldAccess,
        difficulty: LocalDifficulty,
        spawnReason: SpawnReason,
        entityData: EntityData?
    ): EntityData? {
        setNewCoordinate(this.blockPos)
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
        builder.add(GOLEM_STATE, TuffGolemState.STATUE)
        builder.add(STATUE_TICKS, 100)
        builder.add(SUMMON_POS, Optional.empty())
        builder.add(WAS_GIVEN_ITEM, false)
        builder.add(EYE_BLOCK, "default")
    }

    override fun writeCustomDataToNbt(nbt: NbtCompound) {
        super.writeCustomDataToNbt(nbt)
        nbt.putInt("StatueTicks", this.getStatueTicks())
        nbt.putString("GolemState", this.state!!.asString())
//        if (this.getSummonedPos() != null) {
//            val posCompound = NbtCompound()
//            BlockPos.CODEC.encode(this.getSummonedPos(), NbtOps.INSTANCE, posCompound)
//            nbt.put("SummonedPos", posCompound)
//        }
        nbt.putBoolean("WasGivenItem", this.wasGivenItem())
        nbt.putString("EyeBlock", this.getEyeBlock())
    }

    override fun readCustomDataFromNbt(nbt: NbtCompound) {
        super.readCustomDataFromNbt(nbt)
        this.state = TuffGolemState.fromName(nbt.getString("GolemState"))
        if (nbt.contains("StatueTicks")) {
            this.setStatueTicks(nbt.getInt("StatueTicks"))
        }
//        if (nbt.contains("SummonedPos")) {
//            val posCompound = nbt.get("SummonedPos")
//            this.setNewCoordinate(BlockPos.CODEC.decode(NbtOps.INSTANCE, posCompound).getOrThrow().first)
//        }
        if (nbt.contains("WasGivenItem")) {
            this.setWasGivenItem(nbt.getBoolean("WasGivenItem"))
        }
        if (nbt.contains("EyeBlock")) {
            this.setEyeBlock(nbt.getString("EyeBlock"))
        }
    }

    override fun tick() {
        if (world.isClient()) {
            this.updateAnimationStates()
        }
        if (this.state == TuffGolemState.STATUE) {
            if (getStatueTicks() > 0) {
                val ticks = dataTracker.get(STATUE_TICKS)
                setStatueTicks(ticks - 1)
                if (getStatueTicks() % 400 == 0 &&
                    this.health < this.maxHealth &&
                    !this.hasStatusEffect(StatusEffects.REGENERATION) &&
                    random.nextInt(100) == 0
                ) {
                    this.addStatusEffect(StatusEffectInstance(StatusEffects.REGENERATION, 200))
                }
            } else {
                setStatueTicks(0)

            }
        } else if (getStatueTicks() <= 0 && random.nextInt(10000) == 0) {
            println("------------------------------the chance occured------------------------------")
            setStatueTicks(random.nextInt(9600) + 1200)
            this.state = TuffGolemState.TIRED
        }
        super.tick()
    }

    override fun updateGoalControls() {
        val bl = this.primaryPassenger !is MobEntity
        val bl2 = this.state != TuffGolemState.STATUE
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
            if (golemHandStack.isEmpty) {
                //give golem item
                this.setStackInHand(Hand.MAIN_HAND, playerHandStack.copyWithCount(1))
                playerHandStack.consume(1, player)
                setWasGivenItem(true)
                return ActionResult.SUCCESS
            } else if (golemHatStack.isEmpty && getPreferredEquipmentSlot(playerHandStack) == EquipmentSlot.HEAD) {
                //give golem hat
                this.equipStack(EquipmentSlot.HEAD, playerHandStack.copyWithCount(1))
                playerHandStack.consume(1, player)
                return ActionResult.SUCCESS
            } else if (playerHandStack.isIn(ItemTags.WOOL)) {
                //give golem cloak
                this.spit(golemChestStack)
                this.equipStack(EquipmentSlot.CHEST, playerHandStack.copyWithCount(1))
                playerHandStack.consume(1, player)
                return ActionResult.SUCCESS
            } else if (playerHandStack.isIn(ItemTags.BOATS)) {
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
                setWasGivenItem(false)
                return ActionResult.SUCCESS
            } else if (!golemHatStack.isEmpty) {
                //take golem hat
                this.equipStack(EquipmentSlot.HEAD, ItemStack.EMPTY)
                player.giveItemStack(golemHatStack)
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

    override fun canPickupItem(stack: ItemStack): Boolean {
        return this.state?.canPickUpItem() == true && !wasGivenItem() && this.getEquippedStack(EquipmentSlot.MAINHAND).isEmpty
    }

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
            this.playSound(SoundEvents.ENTITY_FOX_SPIT, 1.0f, 1.0f)
            world.spawnEntity(itemEntity)
        }
    }

    private fun setNewCoordinate(blockPos: BlockPos?) {
        dataTracker.set(SUMMON_POS, Optional.ofNullable(blockPos))
    }

    fun getSummonedPos(): BlockPos? {
        return dataTracker.get(SUMMON_POS).getOrNull()
    }

    fun isHoldingItem(): Boolean {
        return this.hasStackEquipped(EquipmentSlot.MAINHAND)
    }

    private fun setWasGivenItem(wasGivenItem: Boolean) {
        dataTracker.set(WAS_GIVEN_ITEM, wasGivenItem)
    }

    fun wasGivenItem(): Boolean {
        return dataTracker.get(WAS_GIVEN_ITEM)
    }

    private fun setEyeBlock(item: ItemStack) {
        dataTracker.set(EYE_BLOCK, Registries.ITEM.getId(item.item).path)
    }

    private fun setEyeBlock(string: String) {
        dataTracker.set(EYE_BLOCK, string)
    }

    fun getEyeBlock(): String {
        return dataTracker.get(EYE_BLOCK)
    }

    private fun setStatueTicks(int: Int) {
        dataTracker.set(STATUE_TICKS, int)
    }

    fun getStatueTicks(): Int {
        return dataTracker.get(STATUE_TICKS)
    }

    override fun getHurtSound(source: DamageSource): SoundEvent? {
        return SoundEvents.ENTITY_IRON_GOLEM_HURT
    }

    override fun getDeathSound(): SoundEvent? {
        return SoundEvents.ENTITY_IRON_GOLEM_DEATH
    }

    fun riseAndWander() {
        this.emitGameEvent(GameEvent.ENTITY_ACTION)
        this.state = TuffGolemState.RISING
    }

    var state: TuffGolemState?
        get() = dataTracker[GOLEM_STATE] as TuffGolemState
        set(state) {
            dataTracker[GOLEM_STATE] = state
        }

    private fun updateAnimationStates() {
        when (this.state!!.ordinal) {
            0 or 1 -> {
                statueState.stop()
                risingState.stop()
            }

            2 -> {
                statueState.stop()
                risingState.start(this.age)
            }

            3 -> {
                statueState.start(this.age)
                risingState.stop()
            }
        }
    }

    enum class TuffGolemState(val thisName: String, private val id: Int) :
        StringIdentifiable {
        WANDERING("wandering", 0) {
            override fun isStatueMode(): Boolean = false
            override fun canPickUpItem(): Boolean = true
        },
        TIRED("tired", 1) {
            override fun isStatueMode(): Boolean = false
            override fun canPickUpItem(): Boolean = false
        },
        RISING("rising", 2) {
            override fun isStatueMode(): Boolean = true
            override fun canPickUpItem(): Boolean = false
        },
        STATUE("statue", 3) {
            override fun isStatueMode(): Boolean = true
            override fun canPickUpItem(): Boolean = false
        };

        override fun asString(): String {
            return this.thisName
        }

        abstract fun isStatueMode(): Boolean
        abstract fun canPickUpItem(): Boolean

        companion object {
            private val CODEC: StringIdentifiable.EnumCodec<TuffGolemState> =
                StringIdentifiable.createEnumCodec(TuffGolemState::values)
            private val BY_ID: IntFunction<TuffGolemState> = IdListUtil.sortArray(
                TuffGolemState::id, entries.toTypedArray(), IdListUtil.OutOfBoundsHandler.ZERO
            )
            val PACKET_CODEC: PacketCodec<ByteBuf, TuffGolemState> = PacketCodecs.indexed(BY_ID, TuffGolemState::id)

            fun fromName(name: String): TuffGolemState {
                return CODEC.getOrElse(name, STATUE) as TuffGolemState
            }
        }
    }

    companion object {
        private val GOLEM_STATE: TrackedData<TuffGolemState> =
            DataTracker.registerData(TuffGolemEntity::class.java, DuskTrackedDataHandlerRegistry.TUFF_GOLEM_STATE)

        private val STATUE_TICKS: TrackedData<Int> =
            DataTracker.registerData(TuffGolemEntity::class.java, TrackedDataHandlerRegistry.INTEGER)
        private val SUMMON_POS: TrackedData<Optional<BlockPos>> =
            DataTracker.registerData(TuffGolemEntity::class.java, TrackedDataHandlerRegistry.OPTIONAL_BLOCK_POS)
        private val WAS_GIVEN_ITEM: TrackedData<Boolean> =
            DataTracker.registerData(TuffGolemEntity::class.java, TrackedDataHandlerRegistry.BOOLEAN)
        private val EYE_BLOCK: TrackedData<String> =
            DataTracker.registerData(TuffGolemEntity::class.java, TrackedDataHandlerRegistry.STRING)

        fun createAttributes(): DefaultAttributeContainer.Builder {
            return MobEntity.createAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 10.0)
                .add(EntityAttributes.GENERIC_ARMOR, 10.0)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.15)
                .add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 1.0)
        }
    }
}