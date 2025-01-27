//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//
package org.teamvoided.dusk_debris.entity

import com.google.common.collect.Maps
import com.mojang.logging.LogUtils
import net.minecraft.block.piston.PistonBehavior
import net.minecraft.entity.*
import net.minecraft.entity.damage.DamageSource
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
import net.minecraft.world.World
import org.slf4j.Logger
import org.teamvoided.dusk_debris.data.DuskDamageTypes
import org.teamvoided.dusk_debris.init.DuskEntities
import org.teamvoided.dusk_debris.init.DuskParticles
import java.util.*

class LightningCloudEntity(entityType: EntityType<out LightningCloudEntity>, world: World) :
    Entity(entityType, world), Ownable {
    var duration: Int = 20
    var waitTime: Int = 20
    private var owner: LivingEntity? = null
    private var ownerUuid: UUID? = null

    constructor(world: World, x: Double, y: Double, z: Double) : this(DuskEntities.LIGHTNING_CLOUD, world) {
        this.setPosition(x, y, z)
    }

    override fun initDataTracker(builder: DataTracker.Builder) {
        builder.add(RADIUS, DEFAULT_RADIUS)
        builder.add(DAMAGE, DEFAULT_DAMAGE)
        builder.add(WAITING, false)
        builder.add(PARTICLE_ID, ColoredParticleEffect.create(ParticleTypes.ENTITY_EFFECT, -1))
    }

    init {
        this.noClip = true
    }

    override fun calculateDimensions() {
        val x = this.x
        val y = this.y
        val z = this.z
        super.calculateDimensions()
        this.setPosition(x, y, z)
    }

    var radius: Float
        get() = getDataTracker().get(RADIUS)
        set(radius) {
            if (!world.isClient) {
                getDataTracker().set(RADIUS, MathHelper.clamp(radius, MIN_RADIUS, MAX_RADIUS))
            }
        }

    var damage: Float
        get() = getDataTracker().get(DAMAGE)
        set(damage) {
            if (!world.isClient) {
                getDataTracker().set(DAMAGE, damage)
            }
        }

    var isWaiting: Boolean
        get() = getDataTracker().get(WAITING) as Boolean
        protected set(waiting) {
            getDataTracker().set(WAITING, waiting)
        }

    override fun tick() {
        super.tick()
        val wait = this.isWaiting
        if (world.isClient) {
            val setRadius = this.radius
            if (wait && random.nextBoolean()) {
                return
            }

            val particleEffect = DuskParticles.SPARK
            val count: Int
            val radius: Float
            if (wait) {
                count = 2
                radius = 0.2f
            } else {
                count = MathHelper.ceil((3.1415927f * setRadius * setRadius) / 5)
                radius = setRadius
            }

            for (j in 0 until count) {
                val randInRadius = MathHelper.sqrt(random.nextFloat()) * radius * 1.5f
                val inSphere = Vec3d(
                    random.nextDouble() - random.nextDouble(),
                    random.nextDouble() - random.nextDouble(),
                    random.nextDouble() - random.nextDouble()
                ).normalize().multiply(randInRadius.toDouble()).add(x, y + setRadius / 2, z)
                world.addParticle(particleEffect, inSphere.x, inSphere.y, inSphere.z, 0.0, 0.0, 0.0)
            }
        } else {
            if (this.age >= this.waitTime + this.duration) {
                this.discard()
                return
            }

            if (wait != this.age < this.waitTime) {
                this.isWaiting = wait
            }

            if (wait) {
                return
            }

//            if (this.radiusGrowth != 0.0f) {
//                rad += this.radiusGrowth
//                if (rad < 0.5f) {
//                    this.discard()
//                    return
//                }
//
//                this.radius = rad
//            }

            if (this.age % delayBetweenDamage == 0) {
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

    override fun readCustomDataFromNbt(nbt: NbtCompound) {
        this.age = nbt.getInt("Age")
        this.duration = nbt.getInt("Duration")
        this.waitTime = nbt.getInt("WaitTime")
        this.radius = nbt.getFloat("Radius")
        this.damage = nbt.getFloat("Damage")
        if (nbt.containsUuid("Owner")) {
            this.ownerUuid = nbt.getUuid("Owner")
        }
    }

    override fun writeCustomDataToNbt(nbt: NbtCompound) {
        nbt.putInt("Age", this.age)
        nbt.putInt("Duration", this.duration)
        nbt.putInt("WaitTime", this.waitTime)
        nbt.putFloat("Radius", this.radius)
        nbt.putFloat("Damage", this.damage)
        if (this.ownerUuid != null) {
            nbt.putUuid("Owner", this.ownerUuid)
        }
    }

    override fun onTrackedDataSet(data: TrackedData<*>) {
        if (RADIUS == data) {
            this.calculateDimensions()
        }
        super.onTrackedDataSet(data)
    }

    override fun getPistonBehavior(): PistonBehavior {
        return PistonBehavior.IGNORE
    }

    override fun getDimensions(pose: EntityPose): EntityDimensions {
        val dimensions = radius * 2f
        return EntityDimensions.changing(dimensions, dimensions)
    }

    companion object {
        private val delayBetweenDamage = 5

        private val LOGGER: Logger = LogUtils.getLogger()
        private const val APPLY_EFFECT_PER_TICK = 5
        private val RADIUS: TrackedData<Float> = DataTracker.registerData(
            LightningCloudEntity::class.java, TrackedDataHandlerRegistry.FLOAT
        )
        private val DAMAGE: TrackedData<Float> = DataTracker.registerData(
            LightningCloudEntity::class.java, TrackedDataHandlerRegistry.FLOAT
        )
        private val WAITING: TrackedData<Boolean> = DataTracker.registerData(
            LightningCloudEntity::class.java, TrackedDataHandlerRegistry.BOOLEAN
        )
        private val PARTICLE_ID: TrackedData<ParticleEffect> = DataTracker.registerData(
            LightningCloudEntity::class.java, TrackedDataHandlerRegistry.PARTICLE
        )
        private const val MAX_RADIUS = 32f
        private const val MIN_RADIUS = 0.25f
        private const val DEFAULT_RADIUS = 3f
        private const val DEFAULT_DAMAGE = 4f
        const val DEFAULT_WIDTH: Float = 6f
        const val HEIGHT: Float = 0.5f
    }
}
