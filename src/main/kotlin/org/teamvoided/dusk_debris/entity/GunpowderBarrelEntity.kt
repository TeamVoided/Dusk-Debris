package org.teamvoided.dusk_debris.entity

import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.entity.*
import net.minecraft.entity.data.DataTracker
import net.minecraft.entity.data.TrackedData
import net.minecraft.entity.data.TrackedDataHandlerRegistry
import net.minecraft.fluid.FluidState
import net.minecraft.nbt.NbtCompound
import net.minecraft.nbt.NbtHelper
import net.minecraft.particle.ParticleTypes
import net.minecraft.registry.RegistryKeys
import net.minecraft.unmapped.C_zbvyjshu
import net.minecraft.util.math.BlockPos
import net.minecraft.world.BlockView
import net.minecraft.world.World
import net.minecraft.world.World.ExplosionSourceType
import net.minecraft.world.explosion.Explosion
import net.minecraft.world.explosion.ExplosionBehavior
import org.teamvoided.dusk_autumn.init.DuskEntities
import org.teamvoided.dusk_debris.init.DuskBlocks
import java.util.*
import kotlin.math.cos
import kotlin.math.sin

class GunpowderBarrelEntity(entityType: EntityType<out GunpowderBarrelEntity>, world: World) :
    Entity(entityType, world), Ownable {
    private var causingEntity: LivingEntity? = null
    private var field_52318 = false

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
        if (this.isOnGround) {
            this.velocity = velocity.multiply(0.7, -0.5, 0.7)
        }

        val i = this.fuse - if (this.isOnFire || this.isInLava) 3 else 1
        this.fuse = i
        if (i <= 0) {
            this.discard()
            if (!world.isClient) {
                this.explode()
            }
        } else {
            this.updateWaterState()
            if (world.isClient) {
                world.addParticle(ParticleTypes.FLAME, this.x, this.y + 1.0, this.z, 0.0, 0.0, 0.0)
            }
        }
    }

    private fun explode() {
        val explosionPower = 4.0f
        world.createExplosion(
            this, Explosion.createDamageSource(
                this.world,
                this
            ), if (this.field_52318) netherPortalDestroyer else null,
            this.x,
            this.getBodyY(0.0625),
            this.z, explosionPower, false, ExplosionSourceType.TRIGGER
        )
    }

    override fun writeCustomDataToNbt(nbt: NbtCompound) {
        nbt.putShort(FUSE_KEY, fuse.toShort())
        nbt.put(BLOCK_STATE_KEY, NbtHelper.fromBlockState(this.blockState))
    }

    override fun readCustomDataFromNbt(nbt: NbtCompound) {
        this.fuse = nbt.getShort(FUSE_KEY).toInt()
        if (nbt.contains(BLOCK_STATE_KEY, 10)) {
            this.blockState = NbtHelper.toBlockState(
                world
                    .filteredLookup(RegistryKeys.BLOCK), nbt.getCompound(BLOCK_STATE_KEY)
            )
        }
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

    var blockState: BlockState
        get() = dataTracker.get(BLOCK_STATE)
        set(state) {
            dataTracker.set(BLOCK_STATE, state)
        }

    private fun method_61174(bl: Boolean) {
        this.field_52318 = bl
    }

    override fun moveToWorld(c_zbvyjshu: C_zbvyjshu): Entity? {
        val entity = super.moveToWorld(c_zbvyjshu)
        if (entity is GunpowderBarrelEntity) {
            entity.method_61174(true)
        }
        return entity
    }

    companion object {
        private val FUSE: TrackedData<Int> =
            DataTracker.registerData(GunpowderBarrelEntity::class.java, TrackedDataHandlerRegistry.INTEGER)
        private val BLOCK_STATE: TrackedData<BlockState> =
            DataTracker.registerData(GunpowderBarrelEntity::class.java, TrackedDataHandlerRegistry.BLOCK_STATE)
        private const val DEFAULT_FUSE = 100
        private const val BLOCK_STATE_KEY = "block_state"
        const val FUSE_KEY: String = "fuse"
        private val netherPortalDestroyer: ExplosionBehavior = object : ExplosionBehavior() {
            override fun canDestroyBlock(
                explosion: Explosion,
                world: BlockView,
                pos: BlockPos,
                state: BlockState,
                power: Float
            ): Boolean {
                return if (state.isOf(Blocks.NETHER_PORTAL)) false else super.canDestroyBlock(
                    explosion,
                    world,
                    pos,
                    state,
                    power
                )
            }
            override fun getBlastResistance(
                explosion: Explosion,
                world: BlockView,
                pos: BlockPos,
                blockState: BlockState,
                fluidState: FluidState
            ): Optional<Float> {
                return if (blockState.isOf(Blocks.NETHER_PORTAL)) Optional.empty() else super.getBlastResistance(
                    explosion,
                    world,
                    pos,
                    blockState,
                    fluidState
                )
            }
        }
    }
}