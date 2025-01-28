package org.teamvoided.dusk_debris.entity

import com.google.common.collect.ImmutableList
import com.mojang.serialization.Dynamic
import net.minecraft.entity.*
import net.minecraft.entity.ai.brain.Brain
import net.minecraft.entity.ai.brain.MemoryModuleType
import net.minecraft.entity.ai.brain.sensor.SensorType
import net.minecraft.entity.ai.pathing.BirdNavigation
import net.minecraft.entity.ai.pathing.EntityNavigation
import net.minecraft.entity.attribute.DefaultAttributeContainer
import net.minecraft.entity.attribute.EntityAttributes
import net.minecraft.entity.damage.DamageSource
import net.minecraft.entity.mob.HostileEntity
import net.minecraft.entity.passive.AllayBrain
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

class GiantEnemyJellyfishEntity(entityType: EntityType<out AbstractVolaphyraEntity>, world: World) :
    AbstractJellyfishEntity(entityType, world) {

    override fun initialize(
        world: ServerWorldAccess,
        difficulty: LocalDifficulty,
        spawnReason: SpawnReason,
        entityData: EntityData?
    ): EntityData? {
        GiantEnemyJellyfishBrain.setCurrentPosAsHome(this)
        return super.initialize(world, difficulty, spawnReason, entityData)
    }

    override fun mobTick() {
//        this.world.profiler.push("giantEnemyJellyfishBrain")
//        getBrain().tick(world as ServerWorld, this)
//        world.profiler.pop()
//        world.profiler.push("allayActivityUpdate")
//        GiantEnemyJellyfishBrain.updateActivities(this)
//        world.profiler.pop()
        super.mobTick()
    }

//    override fun deserializeBrain(dynamic: Dynamic<*>): Brain<*> {
//        return GiantEnemyJellyfishBrain.create(createBrainProfile().deserialize(dynamic))
//    }

    override fun getBrain(): Brain<*> {
        return super.getBrain()
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
//                this.velocity = velocity.multiply(0.800000011920929)
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


    override fun chooseRandomAngerTime() {
        this.angerTime = ANGER_TIME_RANGE.get(this.random);
    }

    override fun createBrainProfile(): Brain.Profile<Nothing> {
        return Brain.createProfile(MEMORY_MODULES, SENSORS);
    }

    override fun sendAiDebugData() {
        super.sendAiDebugData()
        DebugInfoSender.sendBrainDebugData(this)
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

//    override fun damage(source: DamageSource?, amount: Float): Boolean {
//        return if (isInvulnerable) this.isRemoved() || this.invulnerable
//        else super.damage(source, amount)
//    }

    override fun applyEnchantmentsToDamage(source: DamageSource, amount: Float): Float {
        if (source.attacker != null) {
            val entity: Entity = source.attacker!!
            launchFromFacing(entity, -(amount * 0.2f + 0.5f))
        }

        return super.applyEnchantmentsToDamage(source, amount)
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

    fun playAngrySound() {
        this.makeSound(SoundEvents.ENTITY_PIGLIN_BRUTE_ANGRY)
    }


    companion object {
        val SENSORS = ImmutableList.of(
            SensorType.NEAREST_LIVING_ENTITIES,
            SensorType.NEAREST_PLAYERS,
            SensorType.NEAREST_ITEMS,
            SensorType.HURT_BY,
//            SensorType.PIGLIN_BRUTE_SPECIFIC_SENSOR
        )
        val MEMORY_MODULES: ImmutableList<MemoryModuleType<*>> = ImmutableList.of<MemoryModuleType<*>>(
            MemoryModuleType.LOOK_TARGET,
            MemoryModuleType.DOORS_TO_CLOSE,
            MemoryModuleType.MOBS,
            MemoryModuleType.VISIBLE_MOBS,
            MemoryModuleType.NEAREST_VISIBLE_PLAYER,
            MemoryModuleType.NEAREST_VISIBLE_TARGETABLE_PLAYER,
            MemoryModuleType.NEAREST_VISIBLE_ADULT_PIGLINS,
            MemoryModuleType.NEARBY_ADULT_PIGLINS,
            MemoryModuleType.HURT_BY,
            MemoryModuleType.HURT_BY_ENTITY,
            MemoryModuleType.WALK_TARGET,
            MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE,
            *arrayOf<MemoryModuleType<*>>(
                MemoryModuleType.ATTACK_TARGET,
                MemoryModuleType.ATTACK_COOLING_DOWN,
                MemoryModuleType.INTERACTION_TARGET,
                MemoryModuleType.PATH,
                MemoryModuleType.ANGRY_AT,
                MemoryModuleType.NEAREST_VISIBLE_NEMESIS,
                MemoryModuleType.HOME
            )
        )

        val ANGER_TIME_RANGE: UniformIntProvider = TimeHelper.betweenSeconds(40, 79)

        fun createAttributes(): DefaultAttributeContainer.Builder {
            return AbstractJellyfishEntity.createAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 30.0)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 5.0)
        }
    }
}