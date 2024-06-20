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
import net.minecraft.sound.SoundEvents
import net.minecraft.unmapped.C_zbvyjshu
import net.minecraft.world.World
import net.minecraft.world.World.ExplosionSourceType
import net.minecraft.world.explosion.Explosion
import org.teamvoided.dusk_autumn.init.DuskEntities
import org.teamvoided.dusk_debris.data.DuskBlockTags
import org.teamvoided.dusk_debris.data.DuskEntityTypeTags
import org.teamvoided.dusk_debris.init.DuskBlocks
import org.teamvoided.dusk_debris.particle.DuskParticleEffect
import org.teamvoided.dusk_debris.world.explosion.GunpowderBarrelExplosionBehavior
import kotlin.math.cos
import kotlin.math.sin

class GunpowderBarrelEntity(entityType: EntityType<out GunpowderBarrelEntity>, world: World) :
    Entity(entityType, world), Ownable {
    private var causingEntity: LivingEntity? = null
    private var passedThoughPortal = false
    private val explosionBehavior: GunpowderBarrelExplosionBehavior = GunpowderBarrelExplosionBehavior(
        DuskBlockTags.GUNPOWDER_BARREL_DESTROYS,
        DuskEntityTypeTags.GUNPOWDER_BARREL_DOES_NOT_DAMAGE,
        explosionKnockback,
        1f
    )
    private val explosionBehaviorPostDimensionChange: GunpowderBarrelExplosionBehavior =
        GunpowderBarrelExplosionBehavior(
            DuskBlockTags.BLUNDERBOMB_DESTROYS,
            DuskEntityTypeTags.GUNPOWDER_BARREL_DOES_NOT_DAMAGE,
            explosionKnockback,
            1f
        )

    init {
        this.inanimate = false
    }

    constructor(
        world: World,
        x: Double,
        y: Double,
        z: Double,
        igniter: LivingEntity?
    ) : this(DuskEntities.GUNPOWDER_BARREL, world) {
        this.setPosition(x, y, z)
        val d = world.random.nextDouble() * 6.2831854820251465
        this.setVelocity(-sin(d) * 0.02, 0.20000000298023224, -cos(d) * 0.02)
        this.fuse = DEFAULT_FUSE
        this.prevX = x
        this.prevY = y
        this.prevZ = z
        this.causingEntity = igniter
    }

    override fun initDataTracker(builder: DataTracker.Builder) {
        builder.add(FUSE, DEFAULT_FUSE)
        builder.add(EXPLOSION_POWER, DEFAULT_EXPLOSION_POWER)
        builder.add(EXPLOSION_KNOCKBACK, DEFAULT_EXPLOSION_KNOCKBACK)
        builder.add(BLOCK_STATE, DuskBlocks.GUNPOWDER_BARREL.defaultState)
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
        this.method_60698()
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
            ), if (this.passedThoughPortal) explosionBehaviorPostDimensionChange else explosionBehavior,
            this.x,
            this.getBodyY(0.0625),
            this.z,
            explosionPower.toFloat(),
            false,
            ExplosionSourceType.TNT,
            ParticleTypes.SMOKE,
            DuskParticleEffect(2.0f),
            SoundEvents.ENTITY_GENERIC_EXPLODE
        )
    }

    override fun writeCustomDataToNbt(nbt: NbtCompound) {
        nbt.putShort(FUSE_KEY, fuse.toShort())
        nbt.putShort(EXPLOSION_POWER_KEY, explosionPower.toShort())
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

    fun setProperties(power: Int, knockback: Float, blockState: BlockState) {
        this.explosionPower = power
        this.explosionKnockback = knockback
        this.blockState = blockState
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
    private var explosionPower: Int
        get() = dataTracker.get(EXPLOSION_POWER)
        set(explosionPower) {
            dataTracker.set(EXPLOSION_POWER, explosionPower)
        }
    var explosionKnockback: Float
        get() = dataTracker.get(EXPLOSION_KNOCKBACK)
        set(explosionKnockback) {
            dataTracker.set(EXPLOSION_KNOCKBACK, explosionKnockback)
        }
    var blockState: BlockState
        get() = dataTracker.get(BLOCK_STATE)
        set(state) {
            dataTracker.set(BLOCK_STATE, state)
        }

    private fun hasTraveledDimensions(bl: Boolean) {
        this.passedThoughPortal = bl
    }

    override fun moveToWorld(c_zbvyjshu: C_zbvyjshu): Entity? {
        val entity = super.moveToWorld(c_zbvyjshu)
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
        private val EXPLOSION_KNOCKBACK: TrackedData<Float> =
            DataTracker.registerData(GunpowderBarrelEntity::class.java, TrackedDataHandlerRegistry.FLOAT)
        private val BLOCK_STATE: TrackedData<BlockState> =
            DataTracker.registerData(GunpowderBarrelEntity::class.java, TrackedDataHandlerRegistry.BLOCK_STATE)
        private const val DEFAULT_FUSE = 100
        private const val DEFAULT_EXPLOSION_POWER = 4
        private const val DEFAULT_EXPLOSION_KNOCKBACK = 1f
        private const val BLOCK_STATE_KEY = "block_state"
        const val FUSE_KEY: String = "fuse"
        const val EXPLOSION_KNOCKBACK_KEY: String = "explosion_knockback_multiplier"
        const val EXPLOSION_POWER_KEY: String = "explosion_power"
    }
}