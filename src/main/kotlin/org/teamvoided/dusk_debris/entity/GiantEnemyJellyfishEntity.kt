package org.teamvoided.dusk_debris.entity

import com.mojang.serialization.Dynamic
import net.minecraft.core.BlockPos
import net.minecraft.network.protocol.game.DebugPackets
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.tags.DamageTypeTags
import net.minecraft.util.Mth
import net.minecraft.util.RandomSource
import net.minecraft.util.TimeUtil
import net.minecraft.util.valueproviders.UniformInt
import net.minecraft.world.Difficulty
import net.minecraft.world.DifficultyInstance
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.entity.*
import net.minecraft.world.entity.ai.Brain
import net.minecraft.world.entity.ai.attributes.AttributeSupplier
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.entity.ai.control.FlyingMoveControl
import net.minecraft.world.entity.ai.memory.MemoryModuleType
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation
import net.minecraft.world.entity.ai.navigation.PathNavigation
import net.minecraft.world.level.Level
import net.minecraft.world.level.LevelAccessor
import net.minecraft.world.level.ServerLevelAccessor
import net.minecraft.world.phys.Vec3
import org.teamvoided.dusk_debris.data.tags.DuskFluidTags
import org.teamvoided.dusk_debris.util.Utils.DEG_TO_RAD
import java.util.*

class GiantEnemyJellyfishEntity(entityType: EntityType<GiantEnemyJellyfishEntity>, world: Level) :
    AbstractJellyfishEntity(entityType, world) {

    init {
        this.moveControl = FlyingMoveControl(this, 20, true)
    }

    override fun finalizeSpawn(
        world: ServerLevelAccessor,
        difficulty: DifficultyInstance,
        spawnReason: MobSpawnType,
        entityData: SpawnGroupData?
    ): SpawnGroupData? {
        val random = world.random
        Objects.requireNonNull(random)
        this.getAttribute(Attributes.SCALE)!!.baseValue = generateSizeBonus(random).toDouble()

        GiantEnemyJellyfishBrain.setCurrentPosAsHome(this)
        return super.finalizeSpawn(world, difficulty, spawnReason, entityData)
    }

    override fun customServerAiStep() {
        this.level().profiler.push("giantEnemyJellyfishBrain")
        (brain as Brain<GiantEnemyJellyfishEntity>).tick(this.level() as ServerLevel, this)
        this.level().profiler.pop()
        this.level().profiler.push("giantEnemyJellyfishActivityUpdate")
        GiantEnemyJellyfishBrain.updateActivities(this)
        super.customServerAiStep()
    }

    override fun makeBrain(dynamic: Dynamic<*>): Brain<*> {
        return GiantEnemyJellyfishBrain.create(this, brainProvider().makeBrain(dynamic))
    }

    override fun brainProvider(): Brain.Provider<GiantEnemyJellyfishEntity> {
        return GiantEnemyJellyfishBrain.createProfile()
    }

    override fun sendDebugPackets() {
        super.sendDebugPackets()
        DebugPackets.sendEntityBrain(this)
    }

    override fun createNavigation(world: Level): PathNavigation {
        val birdNavigation = FlyingPathNavigation(this, world)
        birdNavigation.setCanOpenDoors(false)
        birdNavigation.setCanFloat(true)
        birdNavigation.setCanPassDoors(true)
        return birdNavigation
    }

    override fun travel(movementInput: Vec3) {
        if (this.isControlledByLocalInstance) {
//            if (this.isTouchingWater) {
//                this.updateVelocity(0.02f, movementInput)
//                this.move(MovementType.SELF, this.velocity)
//                this.velocity = velocity.multiply(0.8)
//            } else
            if (this.isInLava) {
                this.moveRelative(0.02f, movementInput)
                this.move(MoverType.SELF, this.deltaMovement)
                this.setDeltaMovement(deltaMovement.scale(0.5))
            } else {
                this.moveRelative(this.speed, movementInput)
                this.move(MoverType.SELF, this.deltaMovement)
                this.setDeltaMovement(deltaMovement.scale(0.91))
            }
        }

        this.calculateEntityAnimation(false)
    }

    override fun getDefaultDimensions(pose: Pose): EntityDimensions {
        val dimensions = super.getDefaultDimensions(pose)
        return if (!isProtectedByMembrane()) dimensions.scale(0.37f)
        else dimensions
    }

    override fun isPushable(): Boolean {
        return !this.isProtectedByMembrane() && super.isPushable()
    }

    fun isProtectedByMembrane(): Boolean = true


    override fun startPersistentAngerTimer() {
        this.remainingPersistentAngerTime = ANGER_TIME_RANGE.sample(this.random);
    }

    fun getRecentAttacker(): Optional<LivingEntity> {
        return getBrain().getMemory(MemoryModuleType.HURT_BY)
            .map { obj: DamageSource -> obj.entity }
            .filter { entity: Entity? -> entity is LivingEntity }
            .map { entity: Entity? -> entity as LivingEntity }
    }

    override fun hurt(source: DamageSource, amount: Float): Boolean {
        if (isInvulnerableTo(source)) {
            if (source.entity != null) {
                val entity: Entity = source.entity!!
                launchFromFacing(entity, -(amount * 0.2f + 0.5f))
            }
            return this.isRemoved || this.isInvulnerable
        } else return super.hurt(source, amount)
    }

    override fun actuallyHurt(source: DamageSource, amount: Float) {
        super.actuallyHurt(source, amount)
    }

    override fun isInvulnerableTo(damageSource: DamageSource): Boolean {
        return if (
            isProtectedByMembrane() &&
            !damageSource.`is`(DamageTypeTags.BYPASSES_INVULNERABILITY) &&
            !damageSource.isCreativePlayer
        ) {
            true
        } else {
            super.isInvulnerableTo(damageSource)
        }
    }

    private fun launchFromFacing(entity: Entity, mult: Float) {
        val pitchSin: Double = Mth.sin(entity.xRot * DEG_TO_RAD).toDouble()
        val pitchCos: Double = Mth.cos(entity.xRot * DEG_TO_RAD).toDouble()
        val yawSin: Double = Mth.sin(entity.yRot * DEG_TO_RAD).toDouble()
        val yawCos: Double = Mth.cos(entity.yRot * DEG_TO_RAD).toDouble()
        entity.push(
            -yawSin * pitchCos * mult,
            -pitchSin * mult,
            yawCos * pitchCos * mult
        )
    }

    fun playRoarSound() {
        this.makeSound(SoundEvents.WARDEN_ROAR)
    }

    override fun updateAnimations() {
            this.idleAnimationState.startIfStopped(this.tickCount)
    }

    private fun generateSizeBonus(random: RandomSource): Float { //biased to bottom
        return 1f + random.nextFloat() * random.nextFloat() * 1.66667f
    }

    fun canSpawn(
        type: EntityType<GiantEnemyJellyfishEntity>,
        world: LevelAccessor,
        spawnReason: MobSpawnType,
        pos: BlockPos,
        random: RandomSource
    ): Boolean {
        if (world.difficulty != Difficulty.PEACEFUL) {
            if (MobSpawnType.isSpawner(spawnReason)) {
                return true
            } else if (random.nextInt(20) == 0) {
                return !(!world.getFluidState(pos).`is`(DuskFluidTags.ACID) ||
                        !world.getFluidState(pos.above()).`is`(DuskFluidTags.ACID) ||
                        !world.getFluidState(pos.above(2)).`is`(DuskFluidTags.ACID))
            }
        }
        return false
    }


    companion object {

        val ANGER_TIME_RANGE: UniformInt = TimeUtil.rangeOfSeconds(40, 79)
        val VULNERABLE_TIME: UniformInt = TimeUtil.rangeOfSeconds(40, 79)

        fun createAttributes(): AttributeSupplier.Builder {
            return AbstractJellyfishEntity.createAttributes()
                .add(Attributes.MAX_HEALTH, 30.0)
                .add(Attributes.ATTACK_DAMAGE, 5.0)
        }
    }
}