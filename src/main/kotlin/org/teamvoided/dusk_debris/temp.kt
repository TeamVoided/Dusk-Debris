////
//// Source code recreated from a .class file by IntelliJ IDEA
//// (powered by FernFlower decompiler)
////
//package net.minecraft.entity.passive
//
//import com.mojang.serialization.Dynamic
//import io.netty.buffer.ByteBuf
//import net.minecraft.block.BlockState
//import net.minecraft.entity.AnimationState
//import net.minecraft.entity.EntityType
//import net.minecraft.entity.LivingEntity
//import net.minecraft.entity.SpawnReason
//import net.minecraft.entity.ai.brain.Brain
//import net.minecraft.entity.ai.brain.MemoryModuleType
//import net.minecraft.entity.ai.control.BodyControl
//import net.minecraft.entity.attribute.DefaultAttributeContainer
//import net.minecraft.entity.attribute.EntityAttributes
//import net.minecraft.entity.damage.DamageSource
//import net.minecraft.entity.data.DataTracker
//import net.minecraft.entity.data.TrackedData
//import org.teamvoided.dusk_debris.entity.data.TrackedDataHandlerRegistry
//import net.minecraft.entity.mob.MobEntity
//import net.minecraft.entity.player.PlayerEntity
//import net.minecraft.item.ItemStack
//import net.minecraft.item.Items
//import net.minecraft.nbt.NbtCompound
//import net.minecraft.network.codec.PacketCodec
//import net.minecraft.network.codec.PacketCodecs
//import net.minecraft.registry.tag.BlockTags
//import net.minecraft.registry.tag.DamageTypeTags
//import net.minecraft.registry.tag.EntityTypeTags
//import net.minecraft.registry.tag.ItemTags
//import net.minecraft.server.network.DebugInfoSender
//import net.minecraft.server.world.ServerWorld
//import net.minecraft.sound.SoundEvent
//import net.minecraft.sound.SoundEvents
//import net.minecraft.util.ActionResult
//import net.minecraft.util.Hand
//import net.minecraft.util.StringIdentifiable
//import net.minecraft.util.TimeHelper
//import net.minecraft.util.collection.IdListUtil
//import net.minecraft.util.collection.IdListUtil.OutOfBoundsHandler
//import net.minecraft.util.math.BlockPos
//import net.minecraft.util.random.RandomGenerator
//import net.minecraft.world.World
//import net.minecraft.world.WorldAccess
//import net.minecraft.world.event.GameEvent
//import java.util.function.IntFunction
//
//class ArmadilloEntity(entityType: EntityType<out AnimalEntity?>?, world: World?) : AnimalEntity(entityType, world) {
//    private var currentStateTicks = 0L
//    val unrollingState: AnimationState = AnimationState()
//    val rollingState: AnimationState = AnimationState()
//    val scaredState: AnimationState = AnimationState()
//    private var scuteSheddingCooldown: Int
//    private var peeking = false
//
//    override fun createChild(world: ServerWorld, entity: PassiveEntity): PassiveEntity? {
//        return EntityType.ARMADILLO.create(world)
//    }
//
//    override fun initDataTracker(builder: DataTracker.Builder) {
//        super.initDataTracker(builder)
//        builder.add(STATE, State.IDLE)
//    }
//
//    val isNotIdle: Boolean
//        get() = dataTracker[STATE] !== State.IDLE
//
//    fun shouldHideInShell(): Boolean {
//        return this.state!!.shouldRollUp(this.currentStateTicks)
//    }
//
//    fun shouldSwitchToScaredState(): Boolean {
//        return this.state === State.ROLLING && this.currentStateTicks > State.ROLLING.lengthTicks.toLong()
//    }
//
//    var state: State?
//        get() = dataTracker[STATE] as State
//        set(state) {
//            dataTracker[STATE] = state
//        }
//
//    override fun sendAiDebugData() {
//        super.sendAiDebugData()
//        DebugInfoSender.sendBrainDebugData(this)
//    }
//
//    override fun onTrackedDataSet(data: TrackedData<*>) {
//        if (STATE == data) {
//            this.currentStateTicks = 0L
//        }
//
//        super.onTrackedDataSet(data)
//    }
//
//    override fun createBrainProfile(): Brain.Profile<ArmadilloEntity> {
//        return ArmadilloBrain.createProfile()
//    }
//
//    override fun deserializeBrain(dynamic: Dynamic<*>?): Brain<*> {
//        return ArmadilloBrain.create(createBrainProfile().deserialize(dynamic))
//    }
//
//    override fun mobTick() {
//        world.profiler.push("armadilloBrain")
//        brain.tick(world as ServerWorld, this)
//        world.profiler.pop()
//        world.profiler.push("armadilloActivityUpdate")
//        ArmadilloBrain.updateActivities(this)
//        world.profiler.pop()
//        if (this.isAlive && !this.isBaby && (--this.scuteSheddingCooldown <= 0)) {
//            this.playSound(
//                SoundEvents.ENTITY_ARMADILLO_SCUTE_DROP,
//                1.0f,
//                (random.nextFloat() - random.nextFloat()) * 0.2f + 1.0f
//            )
//            this.dropItem(Items.ARMADILLO_SCUTE)
//            this.emitGameEvent(GameEvent.ENTITY_PLACE)
//            this.scuteSheddingCooldown = this.nextScuteSheddingCooldown()
//        }
//
//        super.mobTick()
//    }
//
//    private fun nextScuteSheddingCooldown(): Int {
//        return random.nextInt(20 * TimeHelper.SECONDS_PER_MINUTE * 5) + 20 * TimeHelper.SECONDS_PER_MINUTE * 5
//    }
//
//    override fun tick() {
//        super.tick()
//        if (world.isClient()) {
//            this.updateAnimationStates()
//        }
//
//        if (this.isNotIdle) {
//            this.clampHeadRotation()
//        }
//
//        ++this.currentStateTicks
//    }
//
//    override fun getAgeScale(): Float {
//        return if (this.isBaby) 0.6f else 1.0f
//    }
//
//    private fun updateAnimationStates() {
//        when (this.state!!.ordinal) {
//            0 -> {
//                unrollingState.stop()
//                rollingState.stop()
//                scaredState.stop()
//            }
//
//            1 -> {
//                unrollingState.stop()
//                rollingState.start(this.age)
//                scaredState.stop()
//            }
//
//            2 -> {
//                unrollingState.stop()
//                rollingState.stop()
//                if (this.peeking) {
//                    scaredState.stop()
//                    this.peeking = false
//                }
//
//                if (this.currentStateTicks == 0L) {
//                    scaredState.restart(this.age)
//                    scaredState.fastForward(State.SCARED.lengthTicks, 1.0f)
//                } else {
//                    scaredState.start(this.age)
//                }
//            }
//
//            3 -> {
//                unrollingState.start(this.age)
//                rollingState.stop()
//                scaredState.stop()
//            }
//        }
//    }
//
//    override fun handleStatus(status: Byte) {
//        if (status.toInt() == 64 && world.isClient) {
//            this.peeking = true
//            world.playSound(
//                this.x,
//                this.y,
//                this.z, SoundEvents.ENTITY_ARMADILLO_PEEK,
//                this.soundCategory, 1.0f, 1.0f, false
//            )
//        } else {
//            super.handleStatus(status)
//        }
//    }
//
//    override fun isBreedingItem(stack: ItemStack): Boolean {
//        return stack.isIn(ItemTags.ARMADILLO_FOOD)
//    }
//
//    fun isScaredOf(entity: LivingEntity): Boolean {
//        if (!this.bounds.expand(7.0, 2.0, 7.0).intersects(entity.bounds)) {
//            return false
//        } else if (entity.type.isIn(EntityTypeTags.UNDEAD)) {
//            return true
//        } else if (this.attacker === entity) {
//            return true
//        } else if (entity is PlayerEntity) {
//            val playerEntity = entity
//            return if (playerEntity.isSpectator) {
//                false
//            } else {
//                playerEntity.isSprinting || playerEntity.hasVehicle()
//            }
//        } else {
//            return false
//        }
//    }
//
//    override fun writeCustomDataToNbt(nbt: NbtCompound) {
//        super.writeCustomDataToNbt(nbt)
//        nbt.putString("state", this.state!!.asString())
//        nbt.putInt("scute_time", this.scuteSheddingCooldown)
//    }
//
//    override fun readCustomDataFromNbt(nbt: NbtCompound) {
//        super.readCustomDataFromNbt(nbt)
//        this.state =
//            State.fromName(nbt.getString("state"))
//        if (nbt.contains("scute_time")) {
//            this.scuteSheddingCooldown = nbt.getInt("scute_time")
//        }
//    }
//
//    fun startRolling() {
//        if (!this.isNotIdle) {
//            this.stopMoving()
//            this.resetLoveTicks()
//            this.emitGameEvent(GameEvent.ENTITY_ACTION)
//            this.makeSound(SoundEvents.ENTITY_ARMADILLO_ROLL)
//            this.state = State.ROLLING
//        }
//    }
//
//    fun unroll() {
//        if (this.isNotIdle) {
//            this.emitGameEvent(GameEvent.ENTITY_ACTION)
//            this.makeSound(SoundEvents.ENTITY_ARMADILLO_UNROLL_FINISH)
//            this.state = State.IDLE
//        }
//    }
//
//    override fun damage(source: DamageSource, amount: Float): Boolean {
//        var amount = amount
//        if (this.isNotIdle) {
//            amount = (amount - 1.0f) / 2.0f
//        }
//
//        return super.damage(source, amount)
//    }
//
//    override fun applyDamage(source: DamageSource, amount: Float) {
//        super.applyDamage(source, amount)
//        if (!this.isAiDisabled && !this.isDead) {
//            if (source.attacker is LivingEntity) {
//                getBrain().remember(MemoryModuleType.DANGER_DETECTED_RECENTLY, true, 80L)
//                if (this.canStayRolledUp()) {
//                    this.startRolling()
//                }
//            } else if (source.isTypeIn(DamageTypeTags.PANIC_ENVIRONMENTAL_CAUSES)) {
//                this.unroll()
//            }
//        }
//    }
//
//    override fun interactMob(player: PlayerEntity, hand: Hand): ActionResult {
//        val itemStack = player.getStackInHand(hand)
//        if (itemStack.isOf(Items.BRUSH) && this.onBrushed()) {
//            itemStack.damageEquipment(16, player, getHand(hand))
//            return ActionResult.success(world.isClient)
//        } else {
//            return if (this.isNotIdle) ActionResult.FAIL else super.interactMob(player, hand)
//        }
//    }
//
//    override fun growUp(age: Int, overGrow: Boolean) {
//        if (this.isBaby && overGrow) {
//            this.makeSound(SoundEvents.ENTITY_ARMADILLO_EAT)
//        }
//
//        super.growUp(age, overGrow)
//    }
//
//    fun onBrushed(): Boolean {
//        if (this.isBaby) {
//            return false
//        } else {
//            this.dropStack(ItemStack(Items.ARMADILLO_SCUTE))
//            this.emitGameEvent(GameEvent.ENTITY_INTERACT)
//            this.playSound(SoundEvents.ENTITY_ARMADILLO_BRUSH)
//            return true
//        }
//    }
//
//    fun canStayRolledUp(): Boolean {
//        return !this.isPanicking && !this.isInFluid && !this.method_60953() && !this.hasVehicle() && !this.hasPassengers()
//    }
//
//    override fun lovePlayer(player: PlayerEntity?) {
//        super.lovePlayer(player)
//        this.makeSound(SoundEvents.ENTITY_ARMADILLO_EAT)
//    }
//
//    override fun canEat(): Boolean {
//        return super.canEat() && !this.isNotIdle
//    }
//
//    override fun getEatSound(stack: ItemStack): SoundEvent {
//        return SoundEvents.ENTITY_ARMADILLO_EAT
//    }
//
//    override fun getAmbientSound(): SoundEvent? {
//        return if (this.isNotIdle) null else SoundEvents.ENTITY_ARMADILLO_AMBIENT
//    }
//
//    override fun getDeathSound(): SoundEvent? {
//        return SoundEvents.ENTITY_ARMADILLO_DEATH
//    }
//
//    override fun getHurtSound(source: DamageSource): SoundEvent? {
//        return if (this.isNotIdle) SoundEvents.ENTITY_ARMADILLO_HURT_REDUCED else SoundEvents.ENTITY_ARMADILLO_HURT
//    }
//
//    override fun playStepSound(pos: BlockPos, state: BlockState) {
//        this.playSound(SoundEvents.ENTITY_ARMADILLO_STEP, 0.15f, 1.0f)
//    }
//
//    override fun getBodyYawSpeed(): Int {
//        return if (this.isNotIdle) 0 else 32
//    }
//
//    override fun createBodyControl(): BodyControl {
//        return object : BodyControl(this) {
//            override fun tick() {
//                if (!this@ArmadilloEntity.isNotIdle) {
//                    super.tick()
//                }
//            }
//        }
//    }
//
//    init {
//        getNavigation().setCanSwim(true)
//        this.scuteSheddingCooldown = this.nextScuteSheddingCooldown()
//    }
//
//    enum class State(val thisName: String, val isThreatened: Boolean, val lengthTicks: Int, private val id: Int) :
//        StringIdentifiable {
//        IDLE("idle", false, 0, 0) {
//            override fun shouldRollUp(currentStateTicks: Long): Boolean {
//                return false
//            }
//        },
//        ROLLING("rolling", true, 10, 1) {
//            override fun shouldRollUp(currentStateTicks: Long): Boolean {
//                return currentStateTicks > 5L
//            }
//        },
//        SCARED("scared", true, 50, 2) {
//            override fun shouldRollUp(currentStateTicks: Long): Boolean {
//                return true
//            }
//        },
//        UNROLLING("unrolling", true, 30, 3) {
//            override fun shouldRollUp(currentStateTicks: Long): Boolean {
//                return currentStateTicks < 26L
//            }
//        };
//
//        override fun asString(): String {
//            return this.thisName
//        }
//
//        abstract fun shouldRollUp(currentStateTicks: Long): Boolean
//
//        companion object {
//            private val CODEC: StringIdentifiable.EnumCodec<State> =
//                StringIdentifiable.createEnumCodec { entries.toTypedArray() }
//            private val BY_ID: IntFunction<State> = IdListUtil.sortArray(
//                State::id, entries.toTypedArray(), OutOfBoundsHandler.ZERO
//            )
//            val PACKET_CODEC: PacketCodec<ByteBuf, State> = PacketCodecs.indexed(BY_ID, State::id)
//
//            fun fromName(name: String?): State {
//                return CODEC.getOrElse(name, IDLE) as State
//            }
//        }
//    }
//
//    companion object {
//        const val BABY_SCALE: Float = 0.6f
//        const val MAX_HEAD_ROTATION: Float = 32.5f
//        const val SCARE_CHECK_INTERVAL: Int = 80
//        private const val SCARE_CHECK_HORIZONTAL_RANGE = 7.0
//        private const val SCARE_CHECK_VERTICAL_DISTANCE = 2.0
//        private val STATE: TrackedData<State> =
//            DataTracker.registerData(ArmadilloEntity::class.java, TrackedDataHandlerRegistry.ARMADILLO_STATE)
//
//        fun createAttributes(): DefaultAttributeContainer.Builder {
//            return MobEntity.createAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH, 12.0)
//                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.14)
//        }
//
//        fun canSpawn(
//            type: EntityType<ArmadilloEntity?>?,
//            world: WorldAccess,
//            reason: SpawnReason?,
//            pos: BlockPos,
//            random: RandomGenerator?
//        ): Boolean {
//            return world.getBlockState(pos.down())
//                .isIn(BlockTags.ARMADILLO_SPAWNABLE_ON) && isBrightEnoughForNaturalSpawn(world, pos)
//        }
//    }
//}