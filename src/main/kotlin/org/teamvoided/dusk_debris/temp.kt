//
//package org.teamvoided.dusk_debris
//
//import com.google.common.collect.Lists
//import com.google.common.collect.Maps
//import com.mojang.logging.LogUtils
//import net.minecraft.block.piston.PistonBehavior
//import net.minecraft.client.util.ColorUtil.Argb32
//import net.minecraft.component.type.PotionContentsComponent
//import net.minecraft.entity.*
//import net.minecraft.entity.data.DataTracker
//import net.minecraft.entity.data.TrackedData
//import net.minecraft.entity.data.TrackedDataHandlerRegistry
//import net.minecraft.entity.effect.StatusEffect
//import net.minecraft.entity.effect.StatusEffectInstance
//import net.minecraft.nbt.NbtCompound
//import net.minecraft.nbt.NbtElement
//import net.minecraft.nbt.NbtOps
//import net.minecraft.particle.ColoredParticleEffect
//import net.minecraft.particle.ParticleEffect
//import net.minecraft.particle.ParticleTypes
//import net.minecraft.potion.Potion
//import net.minecraft.registry.Holder
//import net.minecraft.server.world.ServerWorld
//import net.minecraft.util.math.MathHelper
//import net.minecraft.world.World
//import org.slf4j.Logger
//import java.util.*
//import java.util.function.Predicate
//import java.util.stream.Stream
//
//open class AreaEffectCloudEntity(entityType: EntityType<out AreaEffectCloudEntity>, world: World) :
//    Entity(entityType, world), Ownable {
//    private var potionContents: PotionContentsComponent
//    private val affectedEntities: MutableMap<Entity, Int>
//    var duration: Int = 600
//    var waitTime: Int = 20
//    private var reapplicationDelay = 20
//    var durationOnUse: Int = 0
//    var radiusOnUse: Float = 0f
//    var radiusGrowth: Float = 0f
//    private var owner: LivingEntity? = null
//    private var ownerUuid: UUID? = null
//
//    constructor(world: World, x: Double, y: Double, z: Double) : this(EntityType.AREA_EFFECT_CLOUD, world) {
//        this.setPosition(x, y, z)
//    }
//
//    override fun initDataTracker(builder: DataTracker.Builder) {
//        builder.add(RADIUS, 3.0f)
//        builder.add(WAITING, false)
//        builder.add(PARTICLE_ID, ColoredParticleEffect.create(ParticleTypes.ENTITY_EFFECT, -1))
//    }
//
//    override fun calculateDimensions() {
//        val d = this.x
//        val e = this.y
//        val f = this.z
//        super.calculateDimensions()
//        this.setPosition(d, e, f)
//    }
//
//    var radius: Float
//        get() = getDataTracker().get(RADIUS) as Float
//        set(radius) {
//            if (!world.isClient) {
//                getDataTracker().set(RADIUS, MathHelper.clamp(radius, 0.0f, 32.0f))
//            }
//        }
//
//    fun setPotionContents(contents: PotionContentsComponent) {
//        this.potionContents = contents
//        this.updateColor()
//    }
//
//    private fun updateColor() {
//        val particleEffect = dataTracker.get(PARTICLE_ID) as ParticleEffect
//        if (particleEffect is ColoredParticleEffect) {
//            val i = if (this.potionContents == PotionContentsComponent.DEFAULT) 0 else potionContents.color
//            dataTracker.set(PARTICLE_ID, ColoredParticleEffect.create(particleEffect.type, Argb32.toOpaque(i)))
//        }
//    }
//
//    fun addEffect(effect: StatusEffectInstance) {
//        this.setPotionContents(potionContents.withEffect(effect))
//    }
//
//    var particleType: ParticleEffect
//        get() = getDataTracker().get(PARTICLE_ID) as ParticleEffect
//        set(particle) {
//            getDataTracker().set(PARTICLE_ID, particle)
//        }
//
//    var isWaiting: Boolean
//        get() = getDataTracker().get(WAITING) as Boolean
//        protected set(waiting) {
//            getDataTracker().set(WAITING, waiting)
//        }
//
//    override fun tick() {
//        super.tick()
//        val bl = this.isWaiting
//        var f = this.radius
//        if (world.isClient) {
//            if (bl && random.nextBoolean()) {
//                return
//            }
//
//            val particleEffect = this.particleType
//            val i: Int
//            val g: Float
//            if (bl) {
//                i = 2
//                g = 0.2f
//            } else {
//                i = MathHelper.ceil(3.1415927f * f * f)
//                g = f
//            }
//
//            for (j in 0 until i) {
//                val h = random.nextFloat() * 6.2831855f
//                val k = MathHelper.sqrt(random.nextFloat()) * g
//                val d = this.x + (MathHelper.cos(h) * k).toDouble()
//                val e = this.y
//                val l = this.z + (MathHelper.sin(h) * k).toDouble()
//                if (particleEffect.type === ParticleTypes.ENTITY_EFFECT) {
//                    if (bl && random.nextBoolean()) {
//                        world.addImportantParticle(
//                            ColoredParticleEffect.create(ParticleTypes.ENTITY_EFFECT, -1),
//                            d,
//                            e,
//                            l,
//                            0.0,
//                            0.0,
//                            0.0
//                        )
//                    } else {
//                        world.addImportantParticle(particleEffect, d, e, l, 0.0, 0.0, 0.0)
//                    }
//                } else if (bl) {
//                    world.addImportantParticle(particleEffect, d, e, l, 0.0, 0.0, 0.0)
//                } else {
//                    world.addImportantParticle(
//                        particleEffect, d, e, l,
//                        (0.5 - random.nextDouble()) * 0.15, 0.009999999776482582,
//                        (0.5 - random.nextDouble()) * 0.15
//                    )
//                }
//            }
//        } else {
//            if (this.age >= this.waitTime + this.duration) {
//                this.discard()
//                return
//            }
//
//            val bl2 = this.age < this.waitTime
//            if (bl != bl2) {
//                this.isWaiting = bl2
//            }
//
//            if (bl2) {
//                return
//            }
//
//            if (this.radiusGrowth != 0.0f) {
//                f += this.radiusGrowth
//                if (f < 0.5f) {
//                    this.discard()
//                    return
//                }
//
//                this.radius = f
//            }
//
//            if (this.age % 5 == 0) {
//                affectedEntities.entries.removeIf { entry: Map.Entry<Entity, Int> -> this.age >= entry.value }
//                if (!potionContents.hasEffects()) {
//                    affectedEntities.clear()
//                } else {
//                    val list: MutableList<StatusEffectInstance> = Lists.newArrayList()
//                    if (potionContents.potion().isPresent) {
//                        val var18: Iterator<*> =
//                            ((potionContents.potion().get() as Holder<*>).value() as Potion).effects.iterator()
//
//                        while (var18.hasNext()) {
//                            val statusEffectInstance = var18.next() as StatusEffectInstance
//                            list.add(
//                                StatusEffectInstance(
//                                    statusEffectInstance.effectType,
//                                    statusEffectInstance.mapDuration { ix: Int -> ix / 4 },
//                                    statusEffectInstance.amplifier,
//                                    statusEffectInstance.isAmbient,
//                                    statusEffectInstance.shouldShowParticles()
//                                )
//                            )
//                        }
//                    }
//
//                    list.addAll(potionContents.customEffects())
//                    val list2 = world.getNonSpectatingEntities(
//                        LivingEntity::class.java, this.bounds
//                    )
//                    if (list2.isNotEmpty()) {
//                        val var21: Iterator<*> = list2.iterator()
//
//                        while (true) {
//                            var o: Double
//                            var livingEntity: LivingEntity
//                            do {
//                                var var10000: Stream<*>
//                                do {
//                                    do {
//                                        do {
//                                            if (!var21.hasNext()) {
//                                                return
//                                            }
//
//                                            livingEntity = var21.next() as LivingEntity
//                                        } while (affectedEntities.containsKey(livingEntity))
//                                    } while (!livingEntity.isAffectedBySplashPotions)
//
//                                    var10000 = list.stream()
//                                    Objects.requireNonNull(livingEntity)
//                                } while (var10000.noneMatch(Predicate{ effect: * ->
//                                        livingEntity.canHaveStatusEffect(
//                                            effect
//                                        )
//                                    }))
//
//                                val m = livingEntity.x - this.x
//                                val n = livingEntity.z - this.z
//                                o = m * m + n * n
//                            } while (!(o <= (f * f).toDouble()))
//
//                            affectedEntities[livingEntity] = this.age + this.reapplicationDelay
//                            val var14: Iterator<*> = list.iterator()
//
//                            while (var14.hasNext()) {
//                                val statusEffectInstance2 = var14.next() as StatusEffectInstance
//                                if ((statusEffectInstance2.effectType.value() as StatusEffect).isInstant) {
//                                    (statusEffectInstance2.effectType.value() as StatusEffect).applyInstantEffect(
//                                        this,
//                                        this.getOwner(), livingEntity, statusEffectInstance2.amplifier, 0.5
//                                    )
//                                } else {
//                                    livingEntity.addStatusEffect(StatusEffectInstance(statusEffectInstance2), this)
//                                }
//                            }
//
//                            if (this.radiusOnUse != 0.0f) {
//                                f += this.radiusOnUse
//                                if (f < 0.5f) {
//                                    this.discard()
//                                    return
//                                }
//
//                                this.radius = f
//                            }
//
//                            if (this.durationOnUse != 0) {
//                                this.duration += this.durationOnUse
//                                if (this.duration <= 0) {
//                                    this.discard()
//                                    return
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//        }
//    }
//
//    fun setOwner(owner: LivingEntity) {
//        this.owner = owner
//        this.ownerUuid = owner?.getUuid()
//    }
//
//    override fun getOwner(): LivingEntity? {
//        if (this.owner == null && (this.ownerUuid != null) && world is ServerWorld) {
//            val entity = (world as ServerWorld).getEntity(this.ownerUuid)
//            if (entity is LivingEntity) {
//                this.owner = entity
//            }
//        }
//
//        return this.owner
//    }
//
//    override fun readCustomDataFromNbt(nbt: NbtCompound) {
//        this.age = nbt.getInt("Age")
//        this.duration = nbt.getInt("Duration")
//        this.waitTime = nbt.getInt("WaitTime")
//        this.reapplicationDelay = nbt.getInt("ReapplicationDelay")
//        this.durationOnUse = nbt.getInt("DurationOnUse")
//        this.radiusOnUse = nbt.getFloat("RadiusOnUse")
//        this.radiusGrowth = nbt.getFloat("RadiusPerTick")
//        this.radius = nbt.getFloat("Radius")
//        if (nbt.containsUuid("Owner")) {
//            this.ownerUuid = nbt.getUuid("Owner")
//        }
//
//        val registryOps = this.registryManager.createSerializationContext(NbtOps.INSTANCE)
//        if (nbt.contains("Particle", 10)) {
//            ParticleTypes.TYPE_CODEC.parse(registryOps, nbt["Particle"]).resultOrPartial { string: String ->
//                LOGGER.warn(
//                    "Failed to parse area effect cloud particle options: '{}'",
//                    string
//                )
//            }.ifPresent { particle: ParticleEffect ->
//                this.particleType =
//                    particle
//            }
//        }
//
//        if (nbt.contains("potion_contents")) {
//            PotionContentsComponent.CODEC.parse(registryOps, nbt["potion_contents"])
//                .resultOrPartial { string: String ->
//                    LOGGER.warn(
//                        "Failed to parse area effect cloud potions: '{}'",
//                        string
//                    )
//                }.ifPresent { contents: PotionContentsComponent ->
//                    this.setPotionContents(
//                        contents
//                    )
//                }
//        }
//    }
//
//    override fun writeCustomDataToNbt(nbt: NbtCompound) {
//        nbt.putInt("Age", this.age)
//        nbt.putInt("Duration", this.duration)
//        nbt.putInt("WaitTime", this.waitTime)
//        nbt.putInt("ReapplicationDelay", this.reapplicationDelay)
//        nbt.putInt("DurationOnUse", this.durationOnUse)
//        nbt.putFloat("RadiusOnUse", this.radiusOnUse)
//        nbt.putFloat("RadiusPerTick", this.radiusGrowth)
//        nbt.putFloat("Radius", this.radius)
//        val registryOps = this.registryManager.createSerializationContext(NbtOps.INSTANCE)
//        nbt.put(
//            "Particle", ParticleTypes.TYPE_CODEC.encodeStart(
//                registryOps,
//                particleType
//            ).getOrThrow() as NbtElement
//        )
//        if (this.ownerUuid != null) {
//            nbt.putUuid("Owner", this.ownerUuid)
//        }
//
//        if (potionContents != PotionContentsComponent.DEFAULT) {
//            val nbtElement =
//                PotionContentsComponent.CODEC.encodeStart(registryOps, this.potionContents).getOrThrow() as NbtElement
//            nbt.put("potion_contents", nbtElement)
//        }
//    }
//
//    override fun onTrackedDataSet(data: TrackedData<*>) {
//        if (RADIUS == data) {
//            this.calculateDimensions()
//        }
//
//        super.onTrackedDataSet(data)
//    }
//
//    override fun getPistonBehavior(): PistonBehavior {
//        return PistonBehavior.IGNORE
//    }
//
//    override fun getDimensions(pose: EntityPose): EntityDimensions {
//        return EntityDimensions.changing(this.radius * 2.0f, 0.5f)
//    }
//
//    init {
//        this.potionContents = PotionContentsComponent.DEFAULT
//        this.affectedEntities = Maps.newHashMap()
//        this.noClip = true
//    }
//
//    companion object {
//        private val LOGGER: Logger = LogUtils.getLogger()
//        private const val APPLY_EFFECT_PER_TICK = 5
//        private val RADIUS: TrackedData<Float> =
//            DataTracker.registerData(AreaEffectCloudEntity::class.java, TrackedDataHandlerRegistry.FLOAT)
//        private val WAITING: TrackedData<Boolean> =
//            DataTracker.registerData(AreaEffectCloudEntity::class.java, TrackedDataHandlerRegistry.BOOLEAN)
//        private val PARTICLE_ID: TrackedData<ParticleEffect> = DataTracker.registerData(
//            AreaEffectCloudEntity::class.java, TrackedDataHandlerRegistry.PARTICLE
//        )
//        private const val MAX_RADIUS = 32.0f
//        private const val MIN_RADIUS = 0.5f
//        private const val DEFAULT_RADIUS = 3.0f
//        const val DEFAULT_WIDTH: Float = 6.0f
//        const val HEIGHT: Float = 0.5f
//    }
//}