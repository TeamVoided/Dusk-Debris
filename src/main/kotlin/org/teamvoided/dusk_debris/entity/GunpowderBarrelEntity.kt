package org.teamvoided.dusk_debris.entity

import net.minecraft.block.BlockState
import net.minecraft.entity.*
import net.minecraft.entity.data.DataTracker
import net.minecraft.entity.data.TrackedData
import net.minecraft.entity.data.TrackedDataHandlerRegistry
import net.minecraft.nbt.NbtCompound
import net.minecraft.nbt.NbtHelper
import net.minecraft.particle.ParticleTypes
import net.minecraft.registry.RegistryKeys
import net.minecraft.util.math.Box
import net.minecraft.world.DimensionTransition
import net.minecraft.world.World
import net.minecraft.world.World.ExplosionSourceType
import net.minecraft.world.explosion.Explosion
import org.teamvoided.dusk_debris.data.tags.DuskBlockTags
import org.teamvoided.dusk_debris.data.tags.DuskEntityTypeTags
import org.teamvoided.dusk_debris.init.DuskBlocks
import org.teamvoided.dusk_debris.init.DuskEntities
import org.teamvoided.dusk_debris.init.DuskSoundEvents
import org.teamvoided.dusk_debris.particle.GunpowderExplosionEmitterParticleEffect
import org.teamvoided.dusk_debris.world.explosion.SpecialExplosionBehavior
import kotlin.math.cos
import kotlin.math.sin

class GunpowderBarrelEntity(entityType: EntityType<out GunpowderBarrelEntity>, world: World) :
    Entity(entityType, world), Ownable {
    private var causingEntity: LivingEntity? = null
    private var passedThoughPortal = false
    private val explosionBehavior: SpecialExplosionBehavior = SpecialExplosionBehavior(
        DuskBlockTags.GUNPOWDER_BARREL_DESTROYS,
        DuskEntityTypeTags.GUNPOWDER_BARREL_DOES_NOT_DAMAGE,
        explosionRange.toFloat(),
        explosionPower / 4f,
        explosionPower * 6f
    )
    private val explosionBehaviorPostDimensionChange: SpecialExplosionBehavior = SpecialExplosionBehavior(
        DuskBlockTags.BLUNDERBOMB_DESTROYS,
        DuskEntityTypeTags.GUNPOWDER_BARREL_DOES_NOT_DAMAGE,
        explosionRange.toFloat(),
        explosionPower / 4f,
        explosionPower * 6f
    )

    constructor(
        world: World,
        x: Double,
        y: Double,
        z: Double,
        igniter: LivingEntity?
    ) : this(DuskEntities.GUNPOWDER_BARREL, world) {
        this.setPosition(x, y, z)
        val d = world.random.nextDouble() * 6.283
        this.setVelocity(-sin(d) * 0.02, 0.2, -cos(d) * 0.02)
        this.fuse = DEFAULT_FUSE
        this.prevX = x
        this.prevY = y
        this.prevZ = z
        this.causingEntity = igniter
    }

    init {
        this.inanimate = false
    }

    override fun initDataTracker(builder: DataTracker.Builder) {
        builder.add(FUSE, DEFAULT_FUSE)
        builder.add(EXPLOSION_POWER, DEFAULT_EXPLOSION_POWER)
        builder.add(EXPLOSION_RANGE, DEFAULT_EXPLOSION_RANGE)
        builder.add(BLOCK_STATE, DuskBlocks.GUNPOWDER_BARREL.defaultState)
        builder.add(PARTICLE_COLOR, DEFAULT_PARTICLE_COLOR)
    }

    override fun getMoveEffect(): MoveEffect {
        return MoveEffect.NONE
    }

    override fun collides(): Boolean {
        return !this.isRemoved
    }

    override fun getDefaultGravity(): Double {
        return 0.04
    }

    override fun tick() {
        this.tickPortalTeleportation()
        this.applyGravity()
        this.move(MovementType.SELF, this.velocity)
        this.velocity = velocity.multiply(0.98)
        if (this.isTouchingWater) {
            this.velocity = velocity.add(0.0, 0.075, 0.0)
        } else if (this.isOnGround) {
            this.velocity = velocity.multiply(0.7, -0.5, 0.7)
        }

        val i = this.fuse - if (this.isOnFire || this.isInLava) 3 else 1
        val particle =
            if (blockState.isOf(DuskBlocks.ANCIENT_BLACK_POWDER_BARREL)) ParticleTypes.SOUL_FIRE_FLAME else ParticleTypes.FLAME
        this.fuse = i
        if (i <= 0) {
            this.discard()
            if (!world.isClient) {
                this.explode()
            }
        } else {
            this.updateWaterState()
            if (world.isClient) {
                world.addParticle(
                    particle,
                    this.x,
                    this.y + 1.0,
                    this.z,
                    0.0,
                    0.0,
                    0.0
                )
            }
        }
    }

    private fun explode() {
        world.createExplosion(
            this,
            Explosion.createDamageSource(
                this.world,
                this
            ),
            if (this.passedThoughPortal) explosionBehaviorPostDimensionChange else explosionBehavior,
            this.x,
            this.getBodyY(0.5),
            this.z,
            explosionPower.toFloat(),
            false,
            ExplosionSourceType.TNT,
            ParticleTypes.SMOKE,
            GunpowderExplosionEmitterParticleEffect(explosionPower * 2f, color),
            DuskSoundEvents.BLOCK_GUNPOWDER_BARREL_EXPLODE
        )
        burnEntities(this.world, (explosionPower * 0.8).toInt())
    }

    fun burnEntities(world: World, radius: Int) {
        val entitiesNearby = world.getOtherEntities(
            this, Box(
                this.x - radius,
                this.y - radius,
                this.z - radius,
                this.x + radius,
                this.y + radius,
                this.z + radius
            )
        ) { obj: Entity -> obj.isAlive && !obj.type.isIn(DuskEntityTypeTags.FIREBOMB_DOES_NOT_DAMAGE) }

        return entitiesNearby.forEach {
            it.fireTicks += 200
        }
    }

    override fun writeCustomDataToNbt(nbt: NbtCompound) {
        nbt.putShort(FUSE_KEY, fuse.toShort())
        nbt.putShort(EXPLOSION_POWER_KEY, explosionPower.toShort())
        nbt.putShort(EXPLOSION_RANGE_KEY, explosionRange.toInt().toShort())
        nbt.put(BLOCK_STATE_KEY, NbtHelper.fromBlockState(this.blockState))
    }

    override fun readCustomDataFromNbt(nbt: NbtCompound) {
        this.fuse = nbt.getShort(FUSE_KEY).toInt()
        this.explosionPower = nbt.getFloat(EXPLOSION_POWER_KEY).toInt()
        if (nbt.contains(BLOCK_STATE_KEY, 10)) {
            this.blockState = NbtHelper.toBlockState(
                world
                    .filteredLookup(RegistryKeys.BLOCK), nbt.getCompound(BLOCK_STATE_KEY)
            )
        }
    }

    fun setProperties(power: Int, range: Int, blockState: BlockState, color: Int) {
        this.explosionPower = power
        this.explosionRange = range
        this.blockState = blockState
        this.color = color
    }

    override fun getOwner(): LivingEntity? {
        return this.causingEntity
    }

    override fun copyFrom(original: Entity) {
        super.copyFrom(original)
        if (original is GunpowderBarrelEntity) {
            this.causingEntity = original.causingEntity
        }
    }

    var fuse: Int
        get() = dataTracker.get(FUSE)
        set(fuse) {
            dataTracker.set(FUSE, fuse)
        }
    var explosionPower: Int
        get() = dataTracker.get(EXPLOSION_POWER)
        set(explosionPower) {
            dataTracker.set(EXPLOSION_POWER, explosionPower)
        }
    var explosionRange: Int
        get() = dataTracker.get(EXPLOSION_RANGE)
        set(explosionRange) {
            dataTracker.set(EXPLOSION_RANGE, explosionRange)
        }
    var blockState: BlockState
        get() = dataTracker.get(BLOCK_STATE)
        set(state) {
            dataTracker.set(BLOCK_STATE, state)
        }
    var color: Int
        get() = dataTracker.get(PARTICLE_COLOR)
        set(color) {
            dataTracker.set(PARTICLE_COLOR, color)
        }

    private fun hasTraveledDimensions(bl: Boolean) {
        this.passedThoughPortal = bl
    }

    override fun moveToWorld(dimensionTransition: DimensionTransition): Entity? {
        val entity = super.moveToWorld(dimensionTransition)
        if (entity is GunpowderBarrelEntity) {
            entity.hasTraveledDimensions(true)
        }
        return entity
    }

    companion object {
        private val FUSE: TrackedData<Int> =
            DataTracker.registerData(GunpowderBarrelEntity::class.java, TrackedDataHandlerRegistry.INTEGER)
        private val EXPLOSION_POWER: TrackedData<Int> =
            DataTracker.registerData(GunpowderBarrelEntity::class.java, TrackedDataHandlerRegistry.INTEGER)
        private val EXPLOSION_RANGE: TrackedData<Int> =
            DataTracker.registerData(GunpowderBarrelEntity::class.java, TrackedDataHandlerRegistry.INTEGER)
        private val BLOCK_STATE: TrackedData<BlockState> =
            DataTracker.registerData(GunpowderBarrelEntity::class.java, TrackedDataHandlerRegistry.BLOCK_STATE)
        private val PARTICLE_COLOR: TrackedData<Int> =
            DataTracker.registerData(GunpowderBarrelEntity::class.java, TrackedDataHandlerRegistry.INTEGER)
        private const val DEFAULT_FUSE = 100
        private const val DEFAULT_EXPLOSION_POWER = 4
        private const val DEFAULT_EXPLOSION_RANGE = 4
        private const val DEFAULT_PARTICLE_COLOR = 0xffffff
        private const val BLOCK_STATE_KEY = "block_state"
        const val FUSE_KEY: String = "fuse"
        const val EXPLOSION_RANGE_KEY: String = "explosion_range"
        const val EXPLOSION_POWER_KEY: String = "explosion_power"
    }
}