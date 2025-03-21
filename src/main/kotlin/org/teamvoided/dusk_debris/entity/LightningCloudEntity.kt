//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//
package org.teamvoided.dusk_debris.entity

import com.mojang.logging.LogUtils
import net.minecraft.block.piston.PistonBehavior
import net.minecraft.entity.*
import net.minecraft.entity.data.DataTracker
import net.minecraft.entity.data.TrackedData
import net.minecraft.entity.data.TrackedDataHandlerRegistry
import net.minecraft.nbt.NbtCompound
import net.minecraft.particle.ColoredParticleEffect
import net.minecraft.particle.ParticleEffect
import net.minecraft.particle.ParticleTypes
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.math.MathHelper
import net.minecraft.util.math.Vec3d
import net.minecraft.world.LocalDifficulty
import net.minecraft.world.ServerWorldAccess
import net.minecraft.world.World
import org.slf4j.Logger
import org.teamvoided.dusk_debris.data.DuskDamageTypes
import org.teamvoided.dusk_debris.init.DuskEntities
import org.teamvoided.dusk_debris.util.Utils
import java.lang.Integer.max
import java.util.*

open class LightningCloudEntity(entityType: EntityType<out LightningCloudEntity>, world: World) :
    Entity(entityType, world), Ownable, Initialize {
    private var owner: LivingEntity? = null
    private var ownerUuid: UUID? = null

    constructor(world: World, x: Double, y: Double, z: Double) : this(DuskEntities.LIGHTNING_CLOUD, world) {
        this.setPosition(x, y - radius, z)
    }

    init {
        this.noClip = true
    }

    override fun initDataTracker(builder: DataTracker.Builder) {
        builder.add(WAIT_TIME, DEFAULT_WAIT_TIME)
        builder.add(DURATION, DEFAULT_DURATION)
        builder.add(RADIUS, DEFAULT_RADIUS)
        builder.add(DAMAGE, DEFAULT_DAMAGE)
        builder.add(DELAY_BETWEEN_ACTION, DEFAULT_DELAY_BETWEEN)
        builder.add(PARTICLE_ID, ColoredParticleEffect.create(ParticleTypes.ENTITY_EFFECT, -1))
    }

    override fun readCustomDataFromNbt(nbt: NbtCompound) {
        if (nbt.contains("Age"))
            this.age = nbt.getInt("Age")
        if (nbt.contains("Duration"))
            this.duration = nbt.getInt("Duration")
        if (nbt.contains("WaitTime"))
            this.waitTime = nbt.getInt("WaitTime")
        if (nbt.contains("Radius"))
            this.radius = nbt.getFloat("Radius")
        if (nbt.contains("Damage"))
            this.damage = nbt.getFloat("Damage")
        if (nbt.contains("DelayBetweenAction"))
            this.delayBetweenAction = nbt.getInt("DelayBetweenAction")
        if (nbt.containsUuid("Owner"))
            this.ownerUuid = nbt.getUuid("Owner")
    }

    override fun writeCustomDataToNbt(nbt: NbtCompound) {
        nbt.putInt("Age", this.age)
        nbt.putInt("Duration", this.duration)
        nbt.putInt("WaitTime", this.waitTime)
        nbt.putFloat("Radius", this.radius)
        nbt.putFloat("Damage", this.damage)
        nbt.putInt("DelayBetweenAction", this.delayBetweenAction)
        if (this.ownerUuid != null) {
            nbt.putUuid("Owner", this.ownerUuid)
        }
    }

    override fun calculateDimensions() {
        val x = this.x
        val y = this.y
        val z = this.z
        super.calculateDimensions()
        this.setPosition(x, y, z)
    }

    var radius: Float
        get() = getDataTracker().get(RADIUS) as Float
        set(float) {
            val old = radius
            val new = MathHelper.clamp(float, MIN_RADIUS, MAX_RADIUS)
            this.lastRenderY = this.y
            this.setPosition(this.x, this.y - ((new - old)), this.z)
            getDataTracker().set(RADIUS, new)
        }


    var damage: Float
        get() = getDataTracker().get(DAMAGE) as Float
        set(float) = getDataTracker().set(DAMAGE, float)

    var delayBetweenAction: Int
        get() = getDataTracker().get(DELAY_BETWEEN_ACTION) as Int
        set(int) = getDataTracker().set(DELAY_BETWEEN_ACTION, max(1, int))


    var waitTime: Int
        get() = getDataTracker().get(WAIT_TIME) as Int
        set(int) = getDataTracker().set(WAIT_TIME, int)

    var duration: Int
        get() = getDataTracker().get(DURATION) as Int
        set(int) = getDataTracker().set(DURATION, int)


    var particle: ParticleEffect
        get() = getDataTracker().get(PARTICLE_ID) as ParticleEffect
        set(particle) = getDataTracker().set(PARTICLE_ID, particle)


    override fun tick() {
        super.tick()
        calculateDimensions()
        val isWaiting = age < waitTime
        if (world.isClient) {
            tickClient(isWaiting)
        } else {
            tickServer(isWaiting)
        }
    }

    open fun tickClient(wait: Boolean) {
        val setRadius = this.radius
        if (wait && random.nextBoolean()) {
            return
        }

        val count: Int
        val radius: Float
        if (wait) {
            count = 2
            radius = standingEyeHeight
        } else {
            count = MathHelper.ceil((Utils.rotate180 * setRadius * setRadius) / 5)
            radius = setRadius
        }

        for (j in 0 until count) {
            val randInRadius = MathHelper.sqrt(random.nextFloat()) * radius * 2f
            val inSphere = Vec3d(
                random.nextDouble() - random.nextDouble(),
                random.nextDouble() - random.nextDouble(),
                random.nextDouble() - random.nextDouble()
            ).normalize().multiply(randInRadius.toDouble()).add(x, eyeY, z)
            world.addParticle(particle, inSphere.x, inSphere.y, inSphere.z, 0.0, 0.0, 0.0)
        }
    }

    open fun tickServer(wait: Boolean) {
        if (this.age >= maxAge()) {
            this.discard()
            return
        }
        if (wait) return
        if ((this.age % delayBetweenAction == 0) && !firstUpdate) doDamage()
    }

    open fun doDamage() {
        val source = if (owner != null) {
            this.damageSources.create(DuskDamageTypes.INDIRECT_ELECTRICITY, owner)
        } else {
            this.damageSources.create(DuskDamageTypes.ELECTRICITY)
        }
        val list2 = world.getNonSpectatingEntities(LivingEntity::class.java, this.bounds)
        if (list2.isNotEmpty()) {
            list2.forEach {
                if (it.squaredDistanceTo(pos) <= radius)
                    it.damage(source, damage)
            }
        }
    }

    fun setOwner(owner: LivingEntity) {
        this.owner = owner
        this.ownerUuid = owner.uuid
    }

    override fun getOwner(): LivingEntity? {
        if (this.owner == null && (this.ownerUuid != null) && world is ServerWorld) {
            val entity = (world as ServerWorld).getEntity(this.ownerUuid)
            if (entity is LivingEntity) {
                this.owner = entity
            }
        }

        return this.owner
    }

    fun maxAge(): Int = this.waitTime + this.duration

    override fun initialize(
        world: ServerWorldAccess,
        difficulty: LocalDifficulty,
        spawnReason: SpawnReason,
        entityData: EntityData
    ): EntityData? {

        return entityData
    }

    override fun onTrackedDataSet(data: TrackedData<*>) {
        if (RADIUS == data) {
            this.calculateDimensions()
        }
        super.onTrackedDataSet(data)
    }

    override fun getPistonBehavior(): PistonBehavior = PistonBehavior.IGNORE

    override fun getDimensions(pose: EntityPose): EntityDimensions {
        val dimensions = radius * 2f
        return EntityDimensions.changing(dimensions, dimensions).withEyeHeight(radius)
    }

    companion object {
        private val LOGGER: Logger = LogUtils.getLogger()
        private val RADIUS: TrackedData<Float> = DataTracker.registerData(
            LightningCloudEntity::class.java, TrackedDataHandlerRegistry.FLOAT
        )
        private val DAMAGE: TrackedData<Float> = DataTracker.registerData(
            LightningCloudEntity::class.java, TrackedDataHandlerRegistry.FLOAT
        )
        private val DELAY_BETWEEN_ACTION: TrackedData<Int> = DataTracker.registerData(
            LightningCloudEntity::class.java, TrackedDataHandlerRegistry.INTEGER
        )
        private val DURATION: TrackedData<Int> = DataTracker.registerData(
            LightningCloudEntity::class.java, TrackedDataHandlerRegistry.INTEGER
        )
        private val WAIT_TIME: TrackedData<Int> = DataTracker.registerData(
            LightningCloudEntity::class.java, TrackedDataHandlerRegistry.INTEGER
        )
        private val PARTICLE_ID: TrackedData<ParticleEffect> = DataTracker.registerData(
            LightningCloudEntity::class.java, TrackedDataHandlerRegistry.PARTICLE
        )
        private const val MAX_RADIUS = 32f
        private const val MIN_RADIUS = 0.25f
        private const val DEFAULT_RADIUS = 3f
        private const val DEFAULT_DAMAGE = 4f
        private const val DEFAULT_DELAY_BETWEEN = 5
        private const val DEFAULT_DURATION = 20
        private const val DEFAULT_WAIT_TIME = 20

    }
}
