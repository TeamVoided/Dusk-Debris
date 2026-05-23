//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//
package org.teamvoided.dusk_debris.entity

import com.mojang.logging.LogUtils
import net.minecraft.core.particles.ColorParticleOption
import net.minecraft.core.particles.ParticleOptions
import net.minecraft.core.particles.ParticleTypes
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.syncher.EntityDataAccessor
import net.minecraft.network.syncher.EntityDataSerializers
import net.minecraft.network.syncher.SynchedEntityData
import net.minecraft.server.level.ServerLevel
import net.minecraft.util.Mth
import net.minecraft.world.DifficultyInstance
import net.minecraft.world.entity.*
import net.minecraft.world.level.Level
import net.minecraft.world.level.ServerLevelAccessor
import net.minecraft.world.level.material.PushReaction
import net.minecraft.world.phys.Vec3
import org.slf4j.Logger
import org.teamvoided.dusk_debris.data.DuskDamageTypes
import org.teamvoided.dusk_debris.init.DuskEntities
import org.teamvoided.dusk_debris.util.Utils
import java.lang.Integer.max
import java.util.*

open class LightningCloudEntity(entityType: EntityType<out LightningCloudEntity>, world: Level) :
    Entity(entityType, world), TraceableEntity, Initialize {
    private var owner: LivingEntity? = null
    private var ownerUuid: UUID? = null

    constructor(world: Level, x: Double, y: Double, z: Double) : this(DuskEntities.LIGHTNING_CLOUD, world) {
        this.setPos(x, y - radius, z)
    }

    init {
        this.noPhysics = true
    }

    override fun defineSynchedData(builder: SynchedEntityData.Builder) {
        builder.define(WAIT_TIME, DEFAULT_WAIT_TIME)
        builder.define(DURATION, DEFAULT_DURATION)
        builder.define(RADIUS, DEFAULT_RADIUS)
        builder.define(DAMAGE, DEFAULT_DAMAGE)
        builder.define(DELAY_BETWEEN_ACTION, DEFAULT_DELAY_BETWEEN)
        builder.define(PARTICLE_ID, ColorParticleOption.create(ParticleTypes.ENTITY_EFFECT, -1))
    }

    override fun readAdditionalSaveData(nbt: CompoundTag) {
        if (nbt.contains("Age"))
            this.tickCount = nbt.getInt("Age")
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
        if (nbt.hasUUID("Owner"))
            this.ownerUuid = nbt.getUUID("Owner")
    }

    override fun addAdditionalSaveData(nbt: CompoundTag) {
        nbt.putInt("Age", this.tickCount)
        nbt.putInt("Duration", this.duration)
        nbt.putInt("WaitTime", this.waitTime)
        nbt.putFloat("Radius", this.radius)
        nbt.putFloat("Damage", this.damage)
        nbt.putInt("DelayBetweenAction", this.delayBetweenAction)
        if (this.ownerUuid != null) {
            nbt.putUUID("Owner", this.ownerUuid)
        }
    }

    override fun refreshDimensions() {
        val x = this.x
        val y = this.y
        val z = this.z
        super.refreshDimensions()
        this.setPos(x, y, z)
    }

    var radius: Float
        get() = entityData.get(RADIUS) as Float
        set(float) {
            val old = radius
            val new = Mth.clamp(float, MIN_RADIUS, MAX_RADIUS)
            this.yOld = this.y
            this.setPos(this.x, this.y - ((new - old)), this.z)
            entityData.set(RADIUS, new)
        }


    var damage: Float
        get() = entityData.get(DAMAGE) as Float
        set(float) = entityData.set(DAMAGE, float)

    var delayBetweenAction: Int
        get() = entityData.get(DELAY_BETWEEN_ACTION) as Int
        set(int) = entityData.set(DELAY_BETWEEN_ACTION, max(1, int))


    var waitTime: Int
        get() = entityData.get(WAIT_TIME) as Int
        set(int) = entityData.set(WAIT_TIME, int)

    var duration: Int
        get() = entityData.get(DURATION) as Int
        set(int) = entityData.set(DURATION, int)


    var particle: ParticleOptions
        get() = entityData.get(PARTICLE_ID) as ParticleOptions
        set(particle) = entityData.set(PARTICLE_ID, particle)


    override fun tick() {
        super.tick()
        refreshDimensions()
        val isWaiting = tickCount < waitTime
        if (level().isClientSide) {
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
            radius = eyeHeight
        } else {
            count = Mth.ceil((Utils.rotate180 * setRadius * setRadius) / 5)
            radius = setRadius
        }

        for (j in 0 until count) {
            val randInRadius = Mth.sqrt(random.nextFloat()) * radius * 2f
            val inSphere = Vec3(
                random.nextDouble() - random.nextDouble(),
                random.nextDouble() - random.nextDouble(),
                random.nextDouble() - random.nextDouble()
            ).normalize().scale(randInRadius.toDouble()).add(x, eyeY, z)
            level().addParticle(particle, inSphere.x, inSphere.y, inSphere.z, 0.0, 0.0, 0.0)
        }
    }

    open fun tickServer(wait: Boolean) {
        if (this.tickCount >= maxAge()) {
            this.discard()
            return
        }
        if (wait) return
        if ((this.tickCount % delayBetweenAction == 0) && !firstTick) doDamage()
    }

    open fun doDamage() {
        val source = if (owner != null) {
            this.damageSources().source(DuskDamageTypes.INDIRECT_ELECTRICITY, owner)
        } else {
            this.damageSources().source(DuskDamageTypes.ELECTRICITY)
        }
        val list2 = level().getEntitiesOfClass(LivingEntity::class.java, this.boundingBox)
        if (list2.isNotEmpty()) {
            list2.forEach {
                if (it.distanceToSqr(position()) <= radius)
                    it.hurt(source, damage)
            }
        }
    }

    fun setOwner(owner: LivingEntity) {
        this.owner = owner
        this.ownerUuid = owner.uuid
    }

    override fun getOwner(): LivingEntity? {
        if (this.owner == null && (this.ownerUuid != null) && level() is ServerLevel) {
            val entity = (level() as ServerLevel).getEntity(this.ownerUuid)
            if (entity is LivingEntity) {
                this.owner = entity
            }
        }

        return this.owner
    }

    fun maxAge(): Int = this.waitTime + this.duration

    override fun initialize(
        world: ServerLevelAccessor,
        difficulty: DifficultyInstance,
        spawnReason: MobSpawnType,
        entityData: SpawnGroupData
    ): SpawnGroupData? {

        return entityData
    }

    override fun onSyncedDataUpdated(data: EntityDataAccessor<*>) {
        if (RADIUS == data) {
            this.refreshDimensions()
        }
        super.onSyncedDataUpdated(data)
    }

    override fun getPistonPushReaction(): PushReaction = PushReaction.IGNORE

    override fun getDimensions(pose: Pose): EntityDimensions {
        val dimensions = radius * 2f
        return EntityDimensions.scalable(dimensions, dimensions).withEyeHeight(radius)
    }

    companion object {
        private val LOGGER: Logger = LogUtils.getLogger()
        private val RADIUS: EntityDataAccessor<Float> = SynchedEntityData.defineId(
            LightningCloudEntity::class.java, EntityDataSerializers.FLOAT
        )
        private val DAMAGE: EntityDataAccessor<Float> = SynchedEntityData.defineId(
            LightningCloudEntity::class.java, EntityDataSerializers.FLOAT
        )
        private val DELAY_BETWEEN_ACTION: EntityDataAccessor<Int> = SynchedEntityData.defineId(
            LightningCloudEntity::class.java, EntityDataSerializers.INT
        )
        private val DURATION: EntityDataAccessor<Int> = SynchedEntityData.defineId(
            LightningCloudEntity::class.java, EntityDataSerializers.INT
        )
        private val WAIT_TIME: EntityDataAccessor<Int> = SynchedEntityData.defineId(
            LightningCloudEntity::class.java, EntityDataSerializers.INT
        )
        private val PARTICLE_ID: EntityDataAccessor<ParticleOptions> = SynchedEntityData.defineId(
            LightningCloudEntity::class.java, EntityDataSerializers.PARTICLE
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
