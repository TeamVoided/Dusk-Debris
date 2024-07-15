////
//// Source code recreated from a .class file by IntelliJ IDEA
//// (powered by FernFlower decompiler)
////
//package net.minecraft.entity.passive
//
//import net.minecraft.block.BlockState
//import net.minecraft.component.DataComponentTypes
//import net.minecraft.component.EnchantmentEffectComponentTypes
//import net.minecraft.enchantment.EnchantmentHelper
//import net.minecraft.entity.*
//import net.minecraft.entity.ai.goal.*
//import net.minecraft.entity.ai.pathing.PathNodeType
//import net.minecraft.entity.attribute.DefaultAttributeContainer
//import net.minecraft.entity.attribute.EntityAttributes
//import net.minecraft.entity.damage.DamageSource
//import net.minecraft.entity.data.DataTracker
//import net.minecraft.entity.data.TrackedData
//import net.minecraft.entity.data.TrackedDataHandlerRegistry
//import net.minecraft.entity.decoration.ArmorStandEntity
//import net.minecraft.entity.mob.*
//import net.minecraft.entity.player.PlayerEntity
//import net.minecraft.item.*
//import net.minecraft.nbt.NbtCompound
//import net.minecraft.particle.ItemStackParticleEffect
//import net.minecraft.particle.ParticleTypes
//import net.minecraft.recipe.Ingredient
//import net.minecraft.registry.Holder
//import net.minecraft.registry.RegistryKey
//import net.minecraft.registry.RegistryKeys
//import net.minecraft.registry.tag.BlockTags
//import net.minecraft.registry.tag.DamageTypeTags
//import net.minecraft.registry.tag.ItemTags
//import net.minecraft.server.world.ServerWorld
//import net.minecraft.sound.SoundEvent
//import net.minecraft.sound.SoundEvents
//import net.minecraft.unmapped.C_fudcfuiw
//import net.minecraft.util.*
//import net.minecraft.util.math.BlockPos
//import net.minecraft.util.math.MathHelper
//import net.minecraft.util.math.Vec3d
//import net.minecraft.util.math.int_provider.UniformIntProvider
//import net.minecraft.util.random.RandomGenerator
//import net.minecraft.world.LocalDifficulty
//import net.minecraft.world.ServerWorldAccess
//import net.minecraft.world.World
//import net.minecraft.world.WorldAccess
//import net.minecraft.world.event.GameEvent
//import java.util.*
//import java.util.function.Predicate
//import java.util.function.Supplier
//import kotlin.math.max
//import kotlin.math.min
//
//class WolfEntity(entityType: EntityType<out WolfEntity?>?, world: World?) :
//    TameableEntity(entityType, world), Angerable, VariantProvider<Holder<WolfVariant>> {
//    private var begAnimationProgress = 0f
//    private var lastBegAnimationProgress = 0f
//    var isFurWet: Boolean = false
//        private set
//    private var canShakeWaterOff = false
//    private var shakeProgress = 0f
//    private var lastShakeProgress = 0f
//    private var targetUuid: UUID? = null
//
//    override fun initGoals() {
//        goalSelector.add(1, SwimGoal(this))
//        goalSelector.add(1, C_zxvudoic(1.5, DamageTypeTags.PANIC_ENVIRONMENTAL_CAUSES))
//        goalSelector.add(2, SitGoal(this))
//        goalSelector.add(
//            3, AvoidLlamaGoal(
//                this,
//                LlamaEntity::class.java, 24.0f, 1.5, 1.5
//            )
//        )
//        goalSelector.add(4, PounceAtTargetGoal(this, 0.4f))
//        goalSelector.add(5, MeleeAttackGoal(this, 1.0, true))
//        goalSelector.add(6, FollowOwnerGoal(this, 1.0, 10.0f, 2.0f))
//        goalSelector.add(7, AnimalMateGoal(this, 1.0))
//        goalSelector.add(8, WanderAroundFarGoal(this, 1.0))
//        goalSelector.add(9, WolfBegGoal(this, 8.0f))
//        goalSelector.add(
//            10, LookAtEntityGoal(
//                this,
//                PlayerEntity::class.java, 8.0f
//            )
//        )
//        goalSelector.add(10, LookAroundGoal(this))
//        targetSelector.add(1, TrackOwnerAttackerGoal(this))
//        targetSelector.add(2, AttackWithOwnerGoal(this))
//        targetSelector.add(
//            3,
//            RevengeGoal(this, *arrayOfNulls(0)).setGroupRevenge(*arrayOfNulls(0))
//        )
//        targetSelector.add(
//            4, TargetGoal(
//                this,
//                PlayerEntity::class.java, 10, true, false,
//                Predicate { entity: * -> this.shouldAngerAt(entity) })
//        )
//        targetSelector.add(
//            5, UntamedTargetGoal<Any?>(
//                this,
//                AnimalEntity::class.java, false, FOLLOW_TAMED_PREDICATE
//            )
//        )
//        targetSelector.add(
//            6, UntamedTargetGoal<Any?>(
//                this,
//                TurtleEntity::class.java, false, TurtleEntity.BABY_TURTLE_ON_LAND_FILTER
//            )
//        )
//        targetSelector.add(
//            7, TargetGoal<Any?>(
//                this,
//                AbstractSkeletonEntity::class.java, false
//            )
//        )
//        targetSelector.add(8, UniversalAngerGoal<Any?>(this, true))
//    }
//
//    val texture: Identifier
//        get() {
//            val wolfVariant = this.variant.value() as WolfVariant
//            return if (this.isTamed) {
//                wolfVariant.tameTexture
//            } else {
//                if (this.hasAngerTime()) wolfVariant.angryTexture else wolfVariant.wildTexture
//            }
//        }
//
//    override fun getVariant(): Holder<WolfVariant> {
//        return dataTracker.get(VARIANT) as Holder<*>
//    }
//
//    override fun setVariant(holder: Holder<WolfVariant>) {
//        dataTracker.set(VARIANT, holder)
//    }
//
//    override fun initDataTracker(builder: DataTracker.Builder) {
//        super.initDataTracker(builder)
//        val dynamicRegistryManager = this.registryManager
//        val registry = dynamicRegistryManager.get(RegistryKeys.WOLF_VARIANT)
//        val var10001: TrackedData<*> = VARIANT
//        val var10002: Optional<*> = registry.getHolder(WolfVariants.field_51588)
//        Objects.requireNonNull(registry)
//        builder.add<Holder<*>>(var10001, var10002.or(Supplier { registry.method_60385() }).orElseThrow() as Holder<*>)
//        builder.add(BEGGING, false)
//        builder.add(COLLAR_COLOR, DyeColor.RED.id)
//        builder.add(ANGER_TIME, 0)
//    }
//
//    override fun playStepSound(pos: BlockPos, state: BlockState) {
//        this.playSound(SoundEvents.ENTITY_WOLF_STEP, 0.15f, 1.0f)
//    }
//
//    override fun writeCustomDataToNbt(nbt: NbtCompound) {
//        super.writeCustomDataToNbt(nbt)
//        nbt.putByte("CollarColor", collarColor.id.toByte())
//        this.variant.key.ifPresent { registryKey: RegistryKey<WolfVariant> ->
//            nbt.putString("variant", registryKey.value.toString())
//        }
//        this.writeAngerToNbt(nbt)
//    }
//
//    override fun readCustomDataFromNbt(nbt: NbtCompound) {
//        super.readCustomDataFromNbt(nbt)
//        Optional.ofNullable(Identifier.tryParse(nbt.getString("variant"))).map { identifier: Identifier? ->
//            RegistryKey.of(
//                RegistryKeys.WOLF_VARIANT,
//                identifier
//            )
//        }.flatMap { registryKey: RegistryKey<WolfVariant>? ->
//            this.registryManager
//                .get(RegistryKeys.WOLF_VARIANT).getHolder(registryKey)
//        }.ifPresent { holder: Holder.Reference<WolfVariant> ->
//            this.variant =
//                holder
//        }
//        if (nbt.contains("CollarColor", 99)) {
//            this.collarColor = DyeColor.byId(nbt.getInt("CollarColor"))
//        }
//
//        this.readAngerFromNbt(this.world, nbt)
//    }
//
//    override fun initialize(
//        world: ServerWorldAccess,
//        difficulty: LocalDifficulty,
//        spawnReason: SpawnReason,
//        entityData: EntityData?
//    ): EntityData? {
//        var entityData = entityData
//        val holder = world.getBiome(this.blockPos)
//        val holder2: Holder<*>
//        if (entityData is WolfData) {
//            holder2 = entityData.variant
//        } else {
//            holder2 = WolfVariants.fromBiome(this.registryManager, holder)
//            entityData = WolfData(holder2)
//        }
//
//        this.setVariant(holder2)
//        return super.initialize(world, difficulty, spawnReason, entityData as EntityData?)
//    }
//
//    override fun getAmbientSound(): SoundEvent? {
//        return if (this.hasAngerTime()) {
//            SoundEvents.ENTITY_WOLF_GROWL
//        } else if (random.nextInt(3) == 0) {
//            if (this.isTamed && this.health < 20.0f) SoundEvents.ENTITY_WOLF_WHINE else SoundEvents.ENTITY_WOLF_PANT
//        } else {
//            SoundEvents.ENTITY_WOLF_AMBIENT
//        }
//    }
//
//    override fun getHurtSound(source: DamageSource): SoundEvent? {
//        return if (this.shouldArmorAbsorbDamage(source)) SoundEvents.ITEM_WOLF_ARMOR_DAMAGE else SoundEvents.ENTITY_WOLF_HURT
//    }
//
//    override fun getDeathSound(): SoundEvent? {
//        return SoundEvents.ENTITY_WOLF_DEATH
//    }
//
//    override fun getSoundVolume(): Float {
//        return 0.4f
//    }
//
//    override fun tickMovement() {
//        super.tickMovement()
//        if (!world.isClient && this.isFurWet && !this.canShakeWaterOff && !this.isNavigating && this.isOnGround) {
//            this.canShakeWaterOff = true
//            this.shakeProgress = 0.0f
//            this.lastShakeProgress = 0.0f
//            world.sendEntityStatus(this, 8.toByte())
//        }
//
//        if (!world.isClient) {
//            this.tickAngerLogic(world as ServerWorld, true)
//        }
//    }
//
//    override fun tick() {
//        super.tick()
//        if (this.isAlive) {
//            this.lastBegAnimationProgress = this.begAnimationProgress
//            if (this.isBegging) {
//                this.begAnimationProgress += (1.0f - this.begAnimationProgress) * 0.4f
//            } else {
//                this.begAnimationProgress += (0.0f - this.begAnimationProgress) * 0.4f
//            }
//
//            if (this.isWet) {
//                this.isFurWet = true
//                if (this.canShakeWaterOff && !world.isClient) {
//                    world.sendEntityStatus(this, 56.toByte())
//                    this.resetShake()
//                }
//            } else if ((this.isFurWet || this.canShakeWaterOff) && this.canShakeWaterOff) {
//                if (this.shakeProgress == 0.0f) {
//                    this.playSound(
//                        SoundEvents.ENTITY_WOLF_SHAKE,
//                        this.soundVolume,
//                        (random.nextFloat() - random.nextFloat()) * 0.2f + 1.0f
//                    )
//                    this.emitGameEvent(GameEvent.ENTITY_ACTION)
//                }
//
//                this.lastShakeProgress = this.shakeProgress
//                this.shakeProgress += 0.05f
//                if (this.lastShakeProgress >= 2.0f) {
//                    this.isFurWet = false
//                    this.canShakeWaterOff = false
//                    this.lastShakeProgress = 0.0f
//                    this.shakeProgress = 0.0f
//                }
//
//                if (this.shakeProgress > 0.4f) {
//                    val f = this.y.toFloat()
//                    val i = (MathHelper.sin((this.shakeProgress - 0.4f) * 3.1415927f) * 7.0f).toInt()
//                    val vec3d = this.velocity
//
//                    for (j in 0 until i) {
//                        val g = (random.nextFloat() * 2.0f - 1.0f) * this.width * 0.5f
//                        val h = (random.nextFloat() * 2.0f - 1.0f) * this.width * 0.5f
//                        world.addParticle(
//                            ParticleTypes.WATER_SPLASH,
//                            this.x + g.toDouble(),
//                            (f + 0.8f).toDouble(),
//                            this.z + h.toDouble(), vec3d.x, vec3d.y, vec3d.z
//                        )
//                    }
//                }
//            }
//        }
//    }
//
//    private fun resetShake() {
//        this.canShakeWaterOff = false
//        this.shakeProgress = 0.0f
//        this.lastShakeProgress = 0.0f
//    }
//
//    override fun onDeath(source: DamageSource) {
//        this.isFurWet = false
//        this.canShakeWaterOff = false
//        this.lastShakeProgress = 0.0f
//        this.shakeProgress = 0.0f
//        super.onDeath(source)
//    }
//
//    fun getFurWetBrightnessMultiplier(tickDelta: Float): Float {
//        return min(
//            (0.75f + MathHelper.lerp(
//                tickDelta,
//                this.lastShakeProgress,
//                this.shakeProgress
//            ) / 2.0f * 0.25f).toDouble(), 1.0
//        )
//            .toFloat()
//    }
//
//    fun getShakeAnimationProgress(tickDelta: Float, offset: Float): Float {
//        var f = (MathHelper.lerp(tickDelta, this.lastShakeProgress, this.shakeProgress) + offset) / 1.8f
//        if (f < 0.0f) {
//            f = 0.0f
//        } else if (f > 1.0f) {
//            f = 1.0f
//        }
//
//        return MathHelper.sin(f * 3.1415927f) * MathHelper.sin(f * 3.1415927f * 11.0f) * 0.15f * 3.1415927f
//    }
//
//    fun getBegAnimationProgress(tickDelta: Float): Float {
//        return MathHelper.lerp(tickDelta, this.lastBegAnimationProgress, this.begAnimationProgress) * 0.15f * 3.1415927f
//    }
//
//    override fun getLookPitchSpeed(): Int {
//        return if (this.isInSittingPose) 20 else super.getLookPitchSpeed()
//    }
//
//    override fun damage(source: DamageSource, amount: Float): Boolean {
//        if (this.isInvulnerableTo(source)) {
//            return false
//        } else {
//            if (!world.isClient) {
//                this.isSitting = false
//            }
//
//            return super.damage(source, amount)
//        }
//    }
//
//    override fun canUseSlot(slot: EquipmentSlot): Boolean {
//        return true
//    }
//
//    override fun applyDamage(source: DamageSource, amount: Float) {
//        if (!this.shouldArmorAbsorbDamage(source)) {
//            super.applyDamage(source, amount)
//        } else {
//            val itemStack = this.bodyArmor
//            val i = itemStack.damage
//            val j = itemStack.maxDamage
//            itemStack.damageEquipment(MathHelper.ceil(amount), this, EquipmentSlot.BODY)
//            if (C_fudcfuiw.field_49211.method_57283(i, j) != C_fudcfuiw.field_49211.method_57284(this.bodyArmor)) {
//                this.playSound(SoundEvents.ITEM_WOLF_ARMOR_CRACK)
//                val var7 = this.world
//                if (var7 is ServerWorld) {
//                    var7.spawnParticles(
//                        ItemStackParticleEffect(ParticleTypes.ITEM, Items.ARMADILLO_SCUTE.defaultStack),
//                        this.x,
//                        this.y + 1.0,
//                        this.z, 20, 0.2, 0.1, 0.2, 0.1
//                    )
//                }
//            }
//        }
//    }
//
//    private fun shouldArmorAbsorbDamage(source: DamageSource): Boolean {
//        return this.hasArmor() && !source.isTypeIn(DamageTypeTags.BYPASSES_WOLF_ARMOR)
//    }
//
//    override fun applyTamingSideEffects() {
//        if (this.isTamed) {
//            getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH)!!.baseValue = 40.0
//            this.health = 40.0f
//        } else {
//            getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH)!!.baseValue = 8.0
//        }
//    }
//
//    protected override fun damageArmor(source: DamageSource, amount: Float) {
//        this.damageEquipment(source, amount, *arrayOf(EquipmentSlot.BODY))
//    }
//
//    override fun interactMob(player: PlayerEntity, hand: Hand): ActionResult {
//        val itemStack = player.getStackInHand(hand)
//        val item = itemStack.item
//        if (world.isClient && (!this.isBaby || !this.isBreedingItem(itemStack))) {
//            val bl =
//                this.isOwner(player) || this.isTamed || (itemStack.isOf(Items.BONE) && !this.isTamed && !this.hasAngerTime())
//            return if (bl) ActionResult.CONSUME else ActionResult.PASS
//        } else if (this.isTamed) {
//            if (this.isBreedingItem(itemStack) && this.health < this.maxHealth) {
//                itemStack.consume(1, player)
//                val f = itemStack.get(DataComponentTypes.FOOD)?.nutrition()?.toFloat() ?: 1.0f
//                this.heal(2.0f * f)
//                return ActionResult.success(world.isClient())
//            } else {
//                if (item is DyeItem) {
//                    if (this.isOwner(player)) {
//                        val dyeColor = item.color
//                        if (dyeColor != this.collarColor) {
//                            this.collarColor = dyeColor
//                            itemStack.consume(1, player)
//                            return ActionResult.SUCCESS
//                        }
//
//                        return super.interactMob(player, hand)
//                    }
//                }
//
//                if (itemStack.isOf(Items.WOLF_ARMOR) && this.isOwner(player) && bodyArmor.isEmpty && !this.isBaby) {
//                    this.equipBodyArmor(itemStack.copyWithCount(1))
//                    itemStack.consume(1, player)
//                    return ActionResult.SUCCESS
//                } else {
//                    val itemStack2: ItemStack
//                    if (!itemStack.isOf(Items.SHEARS) || !this.isOwner(
//                            player
//                        ) || !this.hasArmor() || EnchantmentHelper.has(
//                            this.bodyArmor,
//                            EnchantmentEffectComponentTypes.PREVENT_ARMOR_CHANGE
//                        ) && !player.isCreative
//                    ) {
//                        if (((ArmorMaterials.ARMADILLO.value() as ArmorMaterial).repairIngredient()
//                                .get() as Ingredient).test(itemStack) && this.isInSittingPose && this.hasArmor() && this.isOwner(
//                                player
//                            ) && bodyArmor.isDamaged
//                        ) {
//                            itemStack.decrement(1)
//                            this.playSound(SoundEvents.ITEM_WOLF_ARMOR_REPAIR)
//                            itemStack2 = this.bodyArmor
//                            val i = (itemStack2.maxDamage.toFloat() * 0.125f).toInt()
//                            itemStack2.damage = max(0.0, (itemStack2.damage - i).toDouble()).toInt()
//                            return ActionResult.SUCCESS
//                        } else {
//                            val actionResult = super.interactMob(player, hand)
//                            if (!actionResult.isAccepted && this.isOwner(player)) {
//                                this.isSitting = !this.isSitting
//                                this.jumping = false
//                                navigation.stop()
//                                this.target = null as LivingEntity?
//                                return ActionResult.SUCCESS_NO_ITEM_USED
//                            } else {
//                                return actionResult
//                            }
//                        }
//                    } else {
//                        itemStack.damageEquipment(1, player, getHand(hand))
//                        this.playSound(SoundEvents.ITEM_ARMOR_UNEQUIP_WOLF)
//                        itemStack2 = this.bodyArmor
//                        this.equipBodyArmor(ItemStack.EMPTY)
//                        this.dropStack(itemStack2)
//                        return ActionResult.SUCCESS
//                    }
//                }
//            }
//        } else if (itemStack.isOf(Items.BONE) && !this.hasAngerTime()) {
//            itemStack.consume(1, player)
//            this.tryTame(player)
//            return ActionResult.SUCCESS
//        } else {
//            return super.interactMob(player, hand)
//        }
//    }
//
//    private fun tryTame(player: PlayerEntity) {
//        if (random.nextInt(3) == 0) {
//            this.setOwner(player)
//            navigation.stop()
//            this.target = null as LivingEntity?
//            this.isSitting = true
//            world.sendEntityStatus(this, 7.toByte())
//        } else {
//            world.sendEntityStatus(this, 6.toByte())
//        }
//    }
//
//    override fun handleStatus(status: Byte) {
//        if (status.toInt() == 8) {
//            this.canShakeWaterOff = true
//            this.shakeProgress = 0.0f
//            this.lastShakeProgress = 0.0f
//        } else if (status.toInt() == 56) {
//            this.resetShake()
//        } else {
//            super.handleStatus(status)
//        }
//    }
//
//    val tailAngle: Float
//        get() {
//            if (this.hasAngerTime()) {
//                return 1.5393804f
//            } else if (this.isTamed) {
//                val f = this.maxHealth
//                val g = (f - this.health) / f
//                return (0.55f - g * 0.4f) * 3.1415927f
//            } else {
//                return 0.62831855f
//            }
//        }
//
//    override fun isBreedingItem(stack: ItemStack): Boolean {
//        return stack.isIn(ItemTags.WOLF_FOOD)
//    }
//
//    override fun getLimitPerChunk(): Int {
//        return 8
//    }
//
//    override fun getAngerTime(): Int {
//        return dataTracker.get(ANGER_TIME) as Int
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
//    var collarColor: DyeColor
//        get() = DyeColor.byId((dataTracker.get(COLLAR_COLOR) as Int))
//        private set(color) {
//            dataTracker.set(COLLAR_COLOR, color.id)
//        }
//
//    fun hasArmor(): Boolean {
//        return bodyArmor.isOf(Items.WOLF_ARMOR)
//    }
//
//    override fun createChild(world: ServerWorld, passiveEntity: PassiveEntity): WolfEntity? {
//        val wolfEntity = EntityType.WOLF.create(world)
//        if (wolfEntity != null && passiveEntity is WolfEntity) {
//            if (random.nextBoolean()) {
//                wolfEntity.variant = this.variant
//            } else {
//                wolfEntity.variant = passiveEntity.variant
//            }
//
//            if (this.isTamed) {
//                wolfEntity.ownerUuid = this.ownerUuid
//                wolfEntity.setTamed(true, true)
//                if (random.nextBoolean()) {
//                    wolfEntity.collarColor = collarColor
//                } else {
//                    wolfEntity.collarColor = passiveEntity.collarColor
//                }
//            }
//        }
//
//        return wolfEntity
//    }
//
//    override fun canBreedWith(other: AnimalEntity): Boolean {
//        if (other === this) {
//            return false
//        } else if (!this.isTamed) {
//            return false
//        } else if (other !is WolfEntity) {
//            return false
//        } else {
//            val wolfEntity = other
//            return if (!wolfEntity.isTamed) {
//                false
//            } else if (wolfEntity.isInSittingPose) {
//                false
//            } else {
//                this.isInLove && wolfEntity.isInLove
//            }
//        }
//    }
//
//    var isBegging: Boolean
//        get() = dataTracker.get(BEGGING) as Boolean
//        set(begging) {
//            dataTracker.set(BEGGING, begging)
//        }
//
//    override fun canAttackWithOwner(target: LivingEntity, owner: LivingEntity): Boolean {
//        if (target !is CreeperEntity && target !is GhastEntity && target !is ArmorStandEntity) {
//            if (target is WolfEntity) {
//                val wolfEntity = target
//                return !wolfEntity.isTamed || wolfEntity.owner !== owner
//            } else {
//                if (target is PlayerEntity) {
//                    if (owner is PlayerEntity) {
//                        if (!owner.shouldDamagePlayer(target)) {
//                            return false
//                        }
//                    }
//                }
//
//                if (target is AbstractHorseEntity) {
//                    if (target.isTame) {
//                        return false
//                    }
//                }
//
//                val var10000: Boolean
//                if (target is TameableEntity) {
//                    if (target.isTamed) {
//                        var10000 = false
//                        return var10000
//                    }
//                }
//
//                var10000 = true
//                return var10000
//            }
//        } else {
//            return false
//        }
//    }
//
//    override fun method_5931(): Boolean {
//        return !this.hasAngerTime()
//    }
//
//    public override fun getLeashOffset(): Vec3d {
//        return Vec3d(0.0, (0.6f * this.standingEyeHeight).toDouble(), (this.width * 0.4f).toDouble())
//    }
//
//    init {
//        this.setTamed(false, false)
//        this.addPathfindingPenalty(PathNodeType.POWDER_SNOW, -1.0f)
//        this.addPathfindingPenalty(PathNodeType.DANGER_POWDER_SNOW, -1.0f)
//    }
//
//    private inner class AvoidLlamaGoal<T : LivingEntity?>(
//        private val wolf: WolfEntity,
//        fleeFromType: Class<T>?,
//        distance: Float,
//        slowSpeed: Double,
//        fastSpeed: Double
//    ) :
//        FleeEntityGoal<T>(wolf, fleeFromType, distance, slowSpeed, fastSpeed) {
//        override fun canStart(): Boolean {
//            return if (super.canStart() && targetEntity is LlamaEntity) {
//                !wolf.isTamed && this.isScaredOf(targetEntity as LlamaEntity?)
//            } else {
//                false
//            }
//        }
//
//        private fun isScaredOf(llama: LlamaEntity?): Boolean {
//            return llama!!.strength >= random.nextInt(5)
//        }
//
//        override fun start() {
//            this@WolfEntity.target = null as LivingEntity?
//            super.start()
//        }
//
//        override fun tick() {
//            this@WolfEntity.target = null as LivingEntity?
//            super.tick()
//        }
//    }
//
//    class WolfData(val variant: Holder<WolfVariant>) : PassiveData(false)
//    companion object {
//        private val BEGGING: TrackedData<Boolean> =
//            DataTracker.registerData(WolfEntity::class.java, TrackedDataHandlerRegistry.BOOLEAN)
//        private val COLLAR_COLOR: TrackedData<Int> =
//            DataTracker.registerData(WolfEntity::class.java, TrackedDataHandlerRegistry.INTEGER)
//        private val ANGER_TIME: TrackedData<Int> =
//            DataTracker.registerData(WolfEntity::class.java, TrackedDataHandlerRegistry.INTEGER)
//        private val VARIANT: TrackedData<Holder<WolfVariant>> = DataTracker.registerData(
//            WolfEntity::class.java, TrackedDataHandlerRegistry.WOLF_VARIANT
//        )
//        val FOLLOW_TAMED_PREDICATE: Predicate<LivingEntity> =
//            Predicate { entity: LivingEntity ->
//                val entityType = entity.type
//                entityType === EntityType.SHEEP || entityType === EntityType.RABBIT || entityType === EntityType.FOX
//            }
//        private const val WILD_MAX_HEALTH = 8.0f
//        private const val TAMED_MAX_HEALTH = 40.0f
//        private const val ARMOR_REPAIR_UNIT = 0.125f
//        private val ANGER_TIME_RANGE: UniformIntProvider = TimeHelper.betweenSeconds(20, 39)
//        fun createAttributes(): DefaultAttributeContainer.Builder {
//            return MobEntity.createAttributes().add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.30000001192092896)
//                .add(EntityAttributes.GENERIC_MAX_HEALTH, 8.0).add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 4.0)
//        }
//
//        fun canSpawn(
//            type: EntityType<WolfEntity?>?,
//            world: WorldAccess,
//            spawnReason: SpawnReason?,
//            pos: BlockPos,
//            random: RandomGenerator?
//        ): Boolean {
//            return world.getBlockState(pos.down()).isIn(BlockTags.WOLVES_SPAWNABLE_ON) && isBrightEnoughForNaturalSpawn(
//                world,
//                pos
//            )
//        }
//    }
//}