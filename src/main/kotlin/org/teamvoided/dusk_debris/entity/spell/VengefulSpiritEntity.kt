package org.teamvoided.dusk_debris.entity.spell

import net.minecraft.enchantment.EnchantmentHelper
import net.minecraft.entity.EntityDimensions
import net.minecraft.entity.EntityPose
import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.damage.DamageSource
import net.minecraft.entity.data.DataTracker
import net.minecraft.entity.data.TrackedData
import net.minecraft.entity.data.TrackedDataHandlerRegistry
import net.minecraft.entity.projectile.ExplosiveProjectileEntity
import net.minecraft.entity.projectile.ProjectileUtil
import net.minecraft.nbt.NbtCompound
import net.minecraft.particle.ParticleEffect
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.hit.EntityHitResult
import net.minecraft.util.hit.HitResult
import net.minecraft.util.math.MathHelper
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World
import org.teamvoided.dusk_debris.init.DuskEntities
import org.teamvoided.dusk_debris.init.DuskParticles
import kotlin.math.sqrt

class VengefulSpiritEntity : ExplosiveProjectileEntity {
    constructor(entityType: EntityType<out ExplosiveProjectileEntity>, world: World) : super(entityType, world)

    constructor(world: World, owner: LivingEntity, velocity: Vec3d) :
            super(DuskEntities.VENGEFUL_SPIRIT, owner, velocity, world)

    constructor(world: World, x: Double, y: Double, z: Double, velocity: Vec3d) :
            super(DuskEntities.VENGEFUL_SPIRIT, x, y, z, velocity, world)

    private var despawnDistance: Int

    init {
        despawnDistance = DEFAULT_DESPAWN_DISTANCE
        accelerationPower = 0.1
    }


    override fun initDataTracker(builder: DataTracker.Builder) {
        super.initDataTracker(builder)
        builder.add(SIZE, SIZE_DEFAULT)
    }

    override fun readCustomDataFromNbt(nbt: NbtCompound) {
        super.readCustomDataFromNbt(nbt)
        if (nbt.contains(SIZE_KEY))
            this.size = nbt.getFloat(SIZE_KEY)
    }

    override fun writeCustomDataToNbt(nbt: NbtCompound) {
        super.writeCustomDataToNbt(nbt)
        nbt.putFloat(SIZE_KEY, size)
    }

    override fun tick() {
        ProjectileUtil.rotateTowardsMovement(this, 1f)
        super.tick()
        if (distanceTraveled > despawnDistance) {
            world.sendEntityStatus(this, 60.toByte())
            if (!this.world.isClient)
                this.discard()
        } else if (world.isClient) {
            repeat(random.nextInt(3) + 1) {
                val pos = Vec3d(
                    (random.nextDouble() - 0.5),
                    (random.nextDouble() - 0.5),
                    (random.nextDouble() - 0.5)
                ).normalize().multiply(this.size * 1.25).add(this.x, this.eyeY, this.z)
                val velocity = velocity.multiply(-0.1)
                world.addParticle(
                    getParticle(),
                    pos.x, pos.y, pos.z,
                    velocity.x, velocity.y, velocity.z
                )
            }
        }

        val x = this.x - this.prevX
        val y = this.y - this.prevY
        val z = this.z - this.prevZ
        this.distanceTraveled += sqrt(x * x + y * y + z * z).toFloat()
    }

    override fun onCollision(hitResult: HitResult) {
        super.onCollision(hitResult)
        if (!world.isClient) {
            if (noClip) {
                despawnDistance = NO_CLIPPED_DESPAWN_DISTANCE
            } else {
                world.sendEntityStatus(this, 60.toByte())
                this.discard()
            }
        }
    }

    override fun onEntityHit(entityHitResult: EntityHitResult) {
        super.onEntityHit(entityHitResult)
        if (world is ServerWorld) {
            val entity = entityHitResult.entity
            if (entity != null) {
                val fireTicks = entity.fireTicks
                entity.setOnFireForSeconds(5f)
                val damageSource = this.damageSources.indirectMagic(this, owner)
                if (!entity.damage(damageSource, 5f)) {
                    entity.fireTicks = fireTicks
                } else {
                    val serverWorld = world as ServerWorld
                    EnchantmentHelper.onEntityDamaged(serverWorld, entity, damageSource)
                }
            }
        }
    }

    override fun onBlockHit(blockHitResult: BlockHitResult) {
        super.onBlockHit(blockHitResult)
        if (!this.world.isClient) {
            //if ((owner !is MobEntity || this.world.gameRules.getBooleanValue(GameRules.DO_MOB_GRIEFING))) {
            //    val blockPos = blockHitResult.blockPos.offset(blockHitResult.side)
            //    if (this.world.isAir(blockPos)) {
            //        this.world.setBlockState(blockPos, AbstractFireBlock.getState(this.world, blockPos));
            //    }
            //}
        }
    }

    fun addExplosionParticles() {
        val radius = 0.3
        if (world.isClient) {
            repeat(60) {
                val rand = Vec3d(
                    (random.nextDouble() - 0.5),
                    (random.nextDouble() - 0.5),
                    (random.nextDouble() - 0.5)
                ).normalize()
                val pos = rand.multiply(this.size * 1.25).add(this.x, this.eyeY, this.z)
                val velocity = rand.multiply(radius)
                world.addParticle(
                    getParticle(),
                    pos.x, pos.y, pos.z,
                    velocity.x, velocity.y, velocity.z,
                )
            }
        }
    }

    override fun handleStatus(status: Byte) {
        if (status.toInt() == 60) addExplosionParticles()
        else super.handleStatus(status)
    }

    var size: Float
        get() = getDataTracker().get(SIZE) as Float
        set(float) {
            val old = size
            val new = MathHelper.clamp(float, SIZE_BOUNDS.first, SIZE_BOUNDS.second)
            this.lastRenderY = this.y
            this.setPosition(this.x, this.y - (new - old), this.z)
            getDataTracker().set(SIZE, new)
        }

    fun getParticle(): ParticleEffect = DuskParticles.SPELL

    override fun isBurning(): Boolean = false
    override fun getParticleType(): ParticleEffect? = null
    override fun damage(source: DamageSource, amount: Float): Boolean = false

    override fun getDimensions(pose: EntityPose): EntityDimensions {
        val supr = super.getDimensions(pose)
        val width = size * supr.width
        val height = size * supr.height
        return EntityDimensions.changing(width, height).withEyeHeight(height / 2)
    }

    override fun onTrackedDataSet(data: TrackedData<*>) {
        if (SIZE == data) {
            this.calculateDimensions()
        }
        super.onTrackedDataSet(data)
    }

    companion object {
        private val SIZE: TrackedData<Float> =
            DataTracker.registerData(VengefulSpiritEntity::class.java, TrackedDataHandlerRegistry.FLOAT)
        private const val SIZE_DEFAULT = 0.5f
        private val SIZE_BOUNDS = (0.1f to 30f)
        private const val SIZE_KEY = "size"

        private const val DEFAULT_DESPAWN_DISTANCE = 200
        private const val NO_CLIPPED_DESPAWN_DISTANCE = 100
    }
}