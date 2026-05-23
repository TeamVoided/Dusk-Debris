package org.teamvoided.dusk_debris.entity.spell

import net.minecraft.core.particles.ParticleOptions
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.syncher.EntityDataAccessor
import net.minecraft.network.syncher.EntityDataSerializers
import net.minecraft.network.syncher.SynchedEntityData
import net.minecraft.server.level.ServerLevel
import net.minecraft.util.Mth
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.entity.EntityDimensions
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.Pose
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile
import net.minecraft.world.entity.projectile.ProjectileUtil
import net.minecraft.world.item.enchantment.EnchantmentHelper
import net.minecraft.world.level.Level
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.EntityHitResult
import net.minecraft.world.phys.HitResult
import net.minecraft.world.phys.Vec3
import org.teamvoided.dusk_debris.init.DuskEntities
import org.teamvoided.dusk_debris.init.DuskParticles
import kotlin.math.sqrt

class VengefulSpiritEntity : AbstractHurtingProjectile {
    constructor(entityType: EntityType<out AbstractHurtingProjectile>, world: Level) : super(entityType, world)

    constructor(world: Level, owner: LivingEntity, velocity: Vec3) :
            super(DuskEntities.VENGEFUL_SPIRIT, owner, velocity, world)

    constructor(world: Level, x: Double, y: Double, z: Double, velocity: Vec3) :
            super(DuskEntities.VENGEFUL_SPIRIT, x, y, z, velocity, world)

    private var despawnDistance: Int

    init {
        despawnDistance = DEFAULT_DESPAWN_DISTANCE
        accelerationPower = 0.1
    }


    override fun defineSynchedData(builder: SynchedEntityData.Builder) {
        super.defineSynchedData(builder)
        builder.define(SIZE, SIZE_DEFAULT)
    }

    override fun readAdditionalSaveData(nbt: CompoundTag) {
        super.readAdditionalSaveData(nbt)
        if (nbt.contains(SIZE_KEY))
            this.size = nbt.getFloat(SIZE_KEY)
    }

    override fun addAdditionalSaveData(nbt: CompoundTag) {
        super.addAdditionalSaveData(nbt)
        nbt.putFloat(SIZE_KEY, size)
    }

    override fun tick() {
        ProjectileUtil.rotateTowardsMovement(this, 1f)
        super.tick()
        if (moveDist > despawnDistance) {
            level().broadcastEntityEvent(this, 60.toByte())
            if (!this.level().isClientSide)
                this.discard()
        } else if (level().isClientSide) {
            repeat(random.nextInt(3) + 1) {
                val pos = Vec3(
                    (random.nextDouble() - 0.5),
                    (random.nextDouble() - 0.5),
                    (random.nextDouble() - 0.5)
                ).normalize().scale(this.size * 1.25).add(this.x, this.eyeY, this.z)
                val velocity = deltaMovement.scale(-0.1)
                level().addParticle(
                    getParticle(),
                    pos.x, pos.y, pos.z,
                    velocity.x, velocity.y, velocity.z
                )
            }
        }

        val x = this.x - this.xo
        val y = this.y - this.yo
        val z = this.z - this.zo
        this.moveDist += sqrt(x * x + y * y + z * z).toFloat()
    }

    override fun onHit(hitResult: HitResult) {
        super.onHit(hitResult)
        if (!level().isClientSide) {
            if (noPhysics) {
                despawnDistance = NO_CLIPPED_DESPAWN_DISTANCE
            } else {
                level().broadcastEntityEvent(this, 60.toByte())
                this.discard()
            }
        }
    }

    override fun onHitEntity(entityHitResult: EntityHitResult) {
        super.onHitEntity(entityHitResult)
        if (level() is ServerLevel) {
            val entity = entityHitResult.entity
            if (entity != null) {
                val fireTicks = entity.remainingFireTicks
                entity.igniteForSeconds(5f)
                val damageSource = this.damageSources().indirectMagic(this, owner)
                if (!entity.hurt(damageSource, 5f)) {
                    entity.setRemainingFireTicks(fireTicks)
                } else {
                    val serverWorld = level() as ServerLevel
                    EnchantmentHelper.doPostAttackEffects(serverWorld, entity, damageSource)
                }
            }
        }
    }

    override fun onHitBlock(blockHitResult: BlockHitResult) {
        super.onHitBlock(blockHitResult)
        if (!this.level().isClientSide) {
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
        if (level().isClientSide) {
            repeat(60) {
                val rand = Vec3(
                    (random.nextDouble() - 0.5),
                    (random.nextDouble() - 0.5),
                    (random.nextDouble() - 0.5)
                ).normalize()
                val pos = rand.scale(this.size * 1.25).add(this.x, this.eyeY, this.z)
                val velocity = rand.scale(radius)
                level().addParticle(
                    getParticle(),
                    pos.x, pos.y, pos.z,
                    velocity.x, velocity.y, velocity.z,
                )
            }
        }
    }

    override fun handleEntityEvent(status: Byte) {
        if (status.toInt() == 60) addExplosionParticles()
        else super.handleEntityEvent(status)
    }

    var size: Float
        get() = entityData.get(SIZE) as Float
        set(float) {
            val old = size
            val new = Mth.clamp(float, SIZE_BOUNDS.first, SIZE_BOUNDS.second)
            this.yOld = this.y
            this.setPos(this.x, this.y - (new - old), this.z)
            entityData.set(SIZE, new)
        }

    fun getParticle(): ParticleOptions = DuskParticles.SPELL

    override fun shouldBurn(): Boolean = false
    override fun getTrailParticle(): ParticleOptions? = null
    override fun hurt(source: DamageSource, amount: Float): Boolean = false

    override fun getDimensions(pose: Pose): EntityDimensions {
        val supr = super.getDimensions(pose)
        val width = size * supr.width
        val height = size * supr.height
        return EntityDimensions.scalable(width, height).withEyeHeight(height / 2)
    }

    override fun onSyncedDataUpdated(data: EntityDataAccessor<*>) {
        if (SIZE == data) {
            this.refreshDimensions()
        }
        super.onSyncedDataUpdated(data)
    }

    companion object {
        private val SIZE: EntityDataAccessor<Float> =
            SynchedEntityData.defineId(VengefulSpiritEntity::class.java, EntityDataSerializers.FLOAT)
        private const val SIZE_DEFAULT = 0.5f
        private val SIZE_BOUNDS = (0.1f to 30f)
        private const val SIZE_KEY = "size"

        private const val DEFAULT_DESPAWN_DISTANCE = 200
        private const val NO_CLIPPED_DESPAWN_DISTANCE = 100
    }
}