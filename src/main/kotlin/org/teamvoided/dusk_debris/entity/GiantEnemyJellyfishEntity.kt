package org.teamvoided.dusk_debris.entity

import com.mojang.serialization.Dynamic
import net.minecraft.entity.*
import net.minecraft.entity.ai.brain.Brain
import net.minecraft.entity.ai.brain.MemoryModuleType
import net.minecraft.entity.ai.control.FlightMoveControl
import net.minecraft.entity.ai.pathing.BirdNavigation
import net.minecraft.entity.ai.pathing.EntityNavigation
import net.minecraft.entity.attribute.DefaultAttributeContainer
import net.minecraft.entity.attribute.EntityAttributes
import net.minecraft.entity.damage.DamageSource
import net.minecraft.registry.tag.DamageTypeTags
import net.minecraft.server.network.DebugInfoSender
import net.minecraft.server.world.ServerWorld
import net.minecraft.sound.SoundEvents
import net.minecraft.util.TimeHelper
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.MathHelper
import net.minecraft.util.math.Vec3d
import net.minecraft.util.math.int_provider.UniformIntProvider
import net.minecraft.util.random.RandomGenerator
import net.minecraft.world.*
import org.teamvoided.dusk_debris.data.tags.DuskFluidTags
import org.teamvoided.dusk_debris.util.Utils.degToRad
import java.util.*
import java.util.function.IntUnaryOperator

class GiantEnemyJellyfishEntity(entityType: EntityType<GiantEnemyJellyfishEntity>, world: World) :
    AbstractJellyfishEntity(entityType, world) {

    init {
        this.moveControl = FlightMoveControl(this, 20, true)
    }

    override fun initialize(
        world: ServerWorldAccess,
        difficulty: LocalDifficulty,
        spawnReason: SpawnReason,
        entityData: EntityData?
    ): EntityData? {
        val random = world.random
        Objects.requireNonNull(random)
        this.getAttributeInstance(EntityAttributes.GENERIC_SCALE)!!.baseValue = generateSizeBonus(random).toDouble()

        GiantEnemyJellyfishBrain.setCurrentPosAsHome(this)
        return super.initialize(world, difficulty, spawnReason, entityData)
    }

    override fun mobTick() {
        this.getWorld().profiler.push("giantEnemyJellyfishBrain")
        (brain as Brain<GiantEnemyJellyfishEntity>).tick(this.getWorld() as ServerWorld, this)
        this.getWorld().profiler.pop()
        this.getWorld().profiler.push("giantEnemyJellyfishActivityUpdate")
        GiantEnemyJellyfishBrain.updateActivities(this)
        super.mobTick()
    }

    override fun deserializeBrain(dynamic: Dynamic<*>): Brain<*> {
        return GiantEnemyJellyfishBrain.create(this, createBrainProfile().deserialize(dynamic))
    }

    override fun createBrainProfile(): Brain.Profile<GiantEnemyJellyfishEntity> {
        return GiantEnemyJellyfishBrain.createProfile()
    }

    override fun sendAiDebugData() {
        super.sendAiDebugData()
        DebugInfoSender.sendBrainDebugData(this)
    }

    override fun createNavigation(world: World): EntityNavigation {
        val birdNavigation = BirdNavigation(this, world)
        birdNavigation.setCanPathThroughDoors(false)
        birdNavigation.setCanSwim(true)
        birdNavigation.setCanEnterOpenDoors(true)
        return birdNavigation
    }

    override fun travel(movementInput: Vec3d) {
        if (this.isLogicalSideForUpdatingMovement) {
//            if (this.isTouchingWater) {
//                this.updateVelocity(0.02f, movementInput)
//                this.move(MovementType.SELF, this.velocity)
//                this.velocity = velocity.multiply(0.8)
//            } else
            if (this.isInLava) {
                this.updateVelocity(0.02f, movementInput)
                this.move(MovementType.SELF, this.velocity)
                this.velocity = velocity.multiply(0.5)
            } else {
                this.updateVelocity(this.movementSpeed, movementInput)
                this.move(MovementType.SELF, this.velocity)
                this.velocity = velocity.multiply(0.91)
            }
        }

        this.updateLimbs(false)
    }

    override fun getDefaultDimensions(pose: EntityPose): EntityDimensions {
        val dimensions = super.getDefaultDimensions(pose)
        return if (!isProtectedByMembrane()) dimensions.scaled(0.37f)
        else dimensions
    }

    override fun isPushable(): Boolean {
        return !this.isProtectedByMembrane() && super.isPushable()
    }

    fun isProtectedByMembrane(): Boolean = true


    override fun chooseRandomAngerTime() {
        this.angerTime = ANGER_TIME_RANGE.get(this.random);
    }

    fun getRecentAttacker(): Optional<LivingEntity> {
        return getBrain().getOptionalMemory(MemoryModuleType.HURT_BY)
            .map { obj: DamageSource -> obj.attacker }
            .filter { entity: Entity? -> entity is LivingEntity }
            .map { entity: Entity? -> entity as LivingEntity }
    }

    override fun damage(source: DamageSource, amount: Float): Boolean {
        if (isInvulnerableTo(source)) {
            if (source.attacker != null) {
                val entity: Entity = source.attacker!!
                launchFromFacing(entity, -(amount * 0.2f + 0.5f))
            }
            return this.isRemoved || this.isInvulnerable
        } else return super.damage(source, amount)
    }

    override fun applyDamage(source: DamageSource, amount: Float) {
        super.applyDamage(source, amount)
    }

    override fun isInvulnerableTo(damageSource: DamageSource): Boolean {
        return if (
            isProtectedByMembrane() &&
            !damageSource.isTypeIn(DamageTypeTags.BYPASSES_INVULNERABILITY) &&
            !damageSource.isSourceCreativePlayer
        ) {
            true
        } else {
            super.isInvulnerableTo(damageSource)
        }
    }

    private fun launchFromFacing(entity: Entity, mult: Float) {
        val pitchSin: Double = MathHelper.sin(entity.pitch * degToRad).toDouble()
        val pitchCos: Double = MathHelper.cos(entity.pitch * degToRad).toDouble()
        val yawSin: Double = MathHelper.sin(entity.yaw * degToRad).toDouble()
        val yawCos: Double = MathHelper.cos(entity.yaw * degToRad).toDouble()
        entity.addVelocity(
            -yawSin * pitchCos * mult,
            -pitchSin * mult,
            yawCos * pitchCos * mult
        )
    }

    fun playRoarSound() {
        this.makeSound(SoundEvents.ENTITY_WARDEN_ROAR)
    }

    private fun generateSizeBonus(random: RandomGenerator): Float { //biased to bottom
        return 1f + random.nextFloat() * random.nextFloat() * 1.66667f
    }

    fun canSpawn(
        type: EntityType<GiantEnemyJellyfishEntity>,
        world: WorldAccess,
        spawnReason: SpawnReason,
        pos: BlockPos,
        random: RandomGenerator
    ): Boolean {
        if (world.difficulty != Difficulty.PEACEFUL) {
            if (SpawnReason.isSpawner(spawnReason)) {
                return true
            } else if (random.nextInt(20) == 0) {
                return !(!world.getFluidState(pos).isIn(DuskFluidTags.ACID) ||
                        !world.getFluidState(pos.up()).isIn(DuskFluidTags.ACID) ||
                        !world.getFluidState(pos.up(2)).isIn(DuskFluidTags.ACID))
            }
        }
        return false
    }


    companion object {

        val ANGER_TIME_RANGE: UniformIntProvider = TimeHelper.betweenSeconds(40, 79)
        val VULNERABLE_TIME: UniformIntProvider = TimeHelper.betweenSeconds(40, 79)

        fun createAttributes(): DefaultAttributeContainer.Builder {
            return AbstractJellyfishEntity.createAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 30.0)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 5.0)
        }
    }
}