package org.teamvoided.dusk_debris.entity

import net.minecraft.entity.EntityData
import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.SpawnReason
import net.minecraft.entity.damage.DamageTypes
import net.minecraft.util.Identifier
import net.minecraft.util.math.Box
import net.minecraft.util.math.MathHelper.lerp
import net.minecraft.util.math.Vec3d
import net.minecraft.world.LocalDifficulty
import net.minecraft.world.ServerWorldAccess
import net.minecraft.world.World
import org.joml.Vector2f
import org.teamvoided.dusk_debris.init.DuskEntities
import kotlin.math.acos
import kotlin.math.atan2

class LazerEntity(entityType: EntityType<out LazerEntity>, world: World) :
    LightningCloudEntity(entityType, world) {
    var target: Vec3d
    var prevTarget: Vec3d
    var displayRadius: Pair<Float, Float>
    var prevDisplayRadius: Pair<Float, Float>

    constructor(world: World, x: Double, y: Double, z: Double) : this(DuskEntities.LAZER_ENTITY, world) {
        this.setPosition(x, y, z)
    }

    init {
        this.target = Vec3d.ZERO
        this.prevTarget = this.target
        this.displayRadius = (0f to 0f)
        this.prevDisplayRadius = this.displayRadius
    }

    override fun tick() {
        this.prevTarget = this.target
        if (age % delayBetweenAction == 0) {
            val raycast = this.raycast(MAX_LENGTH, 1f, false)
            this.target = raycast.pos
        }
        super.tick()
        this.pitch = 0f//sin(age / 20f) * 45f + 90f
        this.yaw = 0f//sin(age / 90f) * 90f
    }

    override fun tickClient(wait: Boolean) {
        this.prevDisplayRadius = this.displayRadius
        this.displayRadius = getBeamRadius()
    }

    override fun tickServer(wait: Boolean) {
        super.tickServer(wait)
    }

    override fun doDamage() {
        val box = Box(this.eyePos, this.target).expand(this.radius.toDouble())
        val owner = if (this.owner != null) this.owner else this
        val source = this.damageSources.create(DamageTypes.INDIRECT_MAGIC, owner)

//        val source = if (this.owner != null) {
//            this.damageSources.create(DuskDamageTypes.INDIRECT_ELECTRICITY, this.owner)
//        } else {
//            this.damageSources.create(DuskDamageTypes.ELECTRICITY)
//        }
        val entities = world.getNonSpectatingEntities(LivingEntity::class.java, box)
        if (entities.isNotEmpty()) {
            entities.forEach {
                it.damage(source, damage)
            }
        }
    }

    fun fromLerpedPosition(pos1: Vec3d, pos2: Vec3d, delta: Float): Vec3d {
        val x = lerp(delta.toDouble(), pos1.x, pos2.x)
        val y = lerp(delta.toDouble(), pos1.y, pos2.y)
        val z = lerp(delta.toDouble(), pos1.z, pos2.z)
        return Vec3d(x, y, z)
    }

    fun getRotationPitchYaw(vec3d: Vec3d): Vector2f {
        val pitch = acos(vec3d.y).toFloat()
        val yaw = atan2(vec3d.z, vec3d.x).toFloat()
        return Vector2f(pitch, yaw)
    }

    private fun setRotationPitchYaw(vec3d: Vec3d) {
        val pitchYaw = getRotationPitchYaw(vec3d)
        this.pitch = pitchYaw.x
        this.yaw = pitchYaw.y
    }

    //    var texture: Identifier
//        get() = getDataTracker().get(PARTICLE_ID)
//        set(texture) {
//            getDataTracker().set(PARTICLE_ID, texture)
//        }
//        if (prevTarget != target) {
//            val vec3d = target.subtract(pos).normalize()
//            setRotationPitchYaw(vec3d)
//        }
    override fun initialize(
        world: ServerWorldAccess,
        difficulty: LocalDifficulty,
        spawnReason: SpawnReason,
        entityData: EntityData
    ): EntityData? {
        val entityData2 = super.initialize(world, difficulty, spawnReason, entityData)

        val raycast = this.raycast(MAX_LENGTH, 1f, false)
        this.target = raycast.pos
        this.prevTarget = this.target

        return entityData2
    }

    fun getTexture(): Identifier = Identifier.ofDefault("textures/entity/beacon_beam.png")

    private fun getBeamRadius(): Pair<Float, Float> {
        return (radius to radius * 1.25f)
//        val inner: Float
//        val outer: Float
//        val s = 10
//        val threshold = this.waitTime + this.duration - s
//        if (this.age >= threshold) {
//            val t = (s + 1 - ((age + tickDelta) - threshold)) / s
//            inner = this.radius * t
//            outer = 5f
//        } else if (this.age < this.waitTime) {
//            inner = 0.5f
//            outer = 1f
//        } else {
//            val u = 1 / 16f
//            val t = this.radius + (sin((age + tickDelta) * 1.5f) * u)
//            inner = t
//            outer = t + (1f / 8f)
//        }
//        return (inner to outer)
    }

    fun lerpRadius(tickDelta: Float): Pair<Float, Float> {
        val inner = lerp(tickDelta, this.prevDisplayRadius.first, this.displayRadius.first)
        val outer = lerp(tickDelta, this.prevDisplayRadius.second, this.displayRadius.second)
        return (inner to outer)
    }


    companion object {
        const val MAX_LENGTH = 128.0
    }
}
