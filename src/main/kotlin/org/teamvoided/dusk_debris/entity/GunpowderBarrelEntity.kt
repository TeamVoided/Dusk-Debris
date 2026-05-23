package org.teamvoided.dusk_debris.entity

import net.minecraft.core.particles.ParticleTypes
import net.minecraft.core.registries.Registries
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.NbtUtils
import net.minecraft.network.syncher.EntityDataAccessor
import net.minecraft.network.syncher.EntityDataSerializers
import net.minecraft.network.syncher.SynchedEntityData
import net.minecraft.world.entity.*
import net.minecraft.world.level.Explosion
import net.minecraft.world.level.Level
import net.minecraft.world.level.Level.ExplosionInteraction
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.portal.DimensionTransition
import net.minecraft.world.phys.AABB
import org.teamvoided.dusk_debris.data.tags.DuskBlockTags
import org.teamvoided.dusk_debris.data.tags.DuskEntityTypeTags
import org.teamvoided.dusk_debris.init.DuskBlocks
import org.teamvoided.dusk_debris.init.DuskEntities
import org.teamvoided.dusk_debris.init.DuskSoundEvents
import org.teamvoided.dusk_debris.particle.color.GunpowderExplosionEmitterParticleEffect
import org.teamvoided.dusk_debris.world.explosion.SpecialExplosionBehavior
import kotlin.math.cos
import kotlin.math.sin

class GunpowderBarrelEntity(entityType: EntityType<out GunpowderBarrelEntity>, world: Level) :
    Entity(entityType, world), TraceableEntity {
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
        world: Level,
        x: Double,
        y: Double,
        z: Double,
        igniter: LivingEntity?
    ) : this(DuskEntities.GUNPOWDER_BARREL, world) {
        this.setPos(x, y, z)
        val d = world.random.nextDouble() * 6.283
        this.setDeltaMovement(-sin(d) * 0.02, 0.2, -cos(d) * 0.02)
        this.fuse = DEFAULT_FUSE
        this.xo = x
        this.yo = y
        this.zo = z
        this.causingEntity = igniter
    }

    init {
        this.blocksBuilding = false
    }

    override fun defineSynchedData(builder: SynchedEntityData.Builder) {
        builder.define(FUSE, DEFAULT_FUSE)
        builder.define(EXPLOSION_POWER, DEFAULT_EXPLOSION_POWER)
        builder.define(EXPLOSION_RANGE, DEFAULT_EXPLOSION_RANGE)
        builder.define(BLOCK_STATE, DuskBlocks.GUNPOWDER_BARREL.defaultBlockState())
        builder.define(PARTICLE_COLOR, DEFAULT_PARTICLE_COLOR)
    }

    override fun getMovementEmission(): MovementEmission {
        return MovementEmission.NONE
    }

    override fun isPickable(): Boolean {
        return !this.isRemoved
    }

    override fun getDefaultGravity(): Double {
        return 0.04
    }

    override fun tick() {
        this.handlePortal()
        this.applyGravity()
        this.move(MoverType.SELF, this.deltaMovement)
        this.setDeltaMovement(deltaMovement.scale(0.98))
        if (this.isInWater) {
            this.setDeltaMovement(deltaMovement.add(0.0, 0.075, 0.0))
        } else if (this.onGround()) {
            this.setDeltaMovement(deltaMovement.multiply(0.7, -0.5, 0.7))
        }

        val i = this.fuse - if (this.isOnFire || this.isInLava) 3 else 1
        val particle =
            if (blockState.`is`(DuskBlocks.ANCIENT_BLACK_POWDER_BARREL)) ParticleTypes.SOUL_FIRE_FLAME else ParticleTypes.FLAME
        this.fuse = i
        if (i <= 0) {
            this.discard()
            if (!level().isClientSide) {
                this.explode()
            }
        } else {
            this.updateInWaterStateAndDoFluidPushing()
            if (level().isClientSide) {
                level().addParticle(
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
        level().explode(
            this,
            Explosion.getDefaultDamageSource(
                this.level(),
                this
            ),
            if (this.passedThoughPortal) explosionBehaviorPostDimensionChange else explosionBehavior,
            this.x,
            this.getY(0.5),
            this.z,
            explosionPower.toFloat(),
            false,
            ExplosionInteraction.TNT,
            ParticleTypes.SMOKE,
            GunpowderExplosionEmitterParticleEffect(explosionPower * 2f, color),
            DuskSoundEvents.BLOCK_GUNPOWDER_BARREL_EXPLODE
        )
        burnEntities(this.level(), (explosionPower * 0.8).toInt())
    }

    fun burnEntities(world: Level, radius: Int) {
        val entitiesNearby = world.getEntities(
            this, AABB(
                this.x - radius,
                this.y - radius,
                this.z - radius,
                this.x + radius,
                this.y + radius,
                this.z + radius
            )
        ) { obj: Entity -> obj.isAlive && !obj.type.`is`(DuskEntityTypeTags.FIREBOMB_DOES_NOT_DAMAGE) }

        return entitiesNearby.forEach {
            it.remainingFireTicks += 200
        }
    }

    override fun addAdditionalSaveData(nbt: CompoundTag) {
        nbt.putShort(FUSE_KEY, fuse.toShort())
        nbt.putShort(EXPLOSION_POWER_KEY, explosionPower.toShort())
        nbt.putShort(EXPLOSION_RANGE_KEY, explosionRange.toInt().toShort())
        nbt.put(BLOCK_STATE_KEY, NbtUtils.writeBlockState(this.blockState))
    }

    override fun readAdditionalSaveData(nbt: CompoundTag) {
        this.fuse = nbt.getShort(FUSE_KEY).toInt()
        this.explosionPower = nbt.getFloat(EXPLOSION_POWER_KEY).toInt()
        if (nbt.contains(BLOCK_STATE_KEY, 10)) {
            this.blockState = NbtUtils.readBlockState(
                level()
                    .holderLookup(Registries.BLOCK), nbt.getCompound(BLOCK_STATE_KEY)
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

    override fun restoreFrom(original: Entity) {
        super.restoreFrom(original)
        if (original is GunpowderBarrelEntity) {
            this.causingEntity = original.causingEntity
        }
    }

    var fuse: Int
        get() = entityData.get(FUSE)
        set(fuse) {
            entityData.set(FUSE, fuse)
        }
    var explosionPower: Int
        get() = entityData.get(EXPLOSION_POWER)
        set(explosionPower) {
            entityData.set(EXPLOSION_POWER, explosionPower)
        }
    var explosionRange: Int
        get() = entityData.get(EXPLOSION_RANGE)
        set(explosionRange) {
            entityData.set(EXPLOSION_RANGE, explosionRange)
        }
    var blockState: BlockState
        get() = entityData.get(BLOCK_STATE)
        set(state) {
            entityData.set(BLOCK_STATE, state)
        }
    var color: Int
        get() = entityData.get(PARTICLE_COLOR)
        set(color) {
            entityData.set(PARTICLE_COLOR, color)
        }

    private fun hasTraveledDimensions(bl: Boolean) {
        this.passedThoughPortal = bl
    }

    override fun changeDimension(dimensionTransition: DimensionTransition): Entity? {
        val entity = super.changeDimension(dimensionTransition)
        if (entity is GunpowderBarrelEntity) {
            entity.hasTraveledDimensions(true)
        }
        return entity
    }

    companion object {
        private val FUSE: EntityDataAccessor<Int> =
            SynchedEntityData.defineId(GunpowderBarrelEntity::class.java, EntityDataSerializers.INT)
        private val EXPLOSION_POWER: EntityDataAccessor<Int> =
            SynchedEntityData.defineId(GunpowderBarrelEntity::class.java, EntityDataSerializers.INT)
        private val EXPLOSION_RANGE: EntityDataAccessor<Int> =
            SynchedEntityData.defineId(GunpowderBarrelEntity::class.java, EntityDataSerializers.INT)
        private val BLOCK_STATE: EntityDataAccessor<BlockState> =
            SynchedEntityData.defineId(GunpowderBarrelEntity::class.java, EntityDataSerializers.BLOCK_STATE)
        private val PARTICLE_COLOR: EntityDataAccessor<Int> =
            SynchedEntityData.defineId(GunpowderBarrelEntity::class.java, EntityDataSerializers.INT)
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