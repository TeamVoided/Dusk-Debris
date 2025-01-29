package org.teamvoided.dusk_debris.entity

import net.minecraft.block.BlockState
import net.minecraft.entity.EntityType
import net.minecraft.entity.MovementType
import net.minecraft.entity.attribute.DefaultAttributeContainer
import net.minecraft.entity.attribute.EntityAttributes
import net.minecraft.entity.mob.Angerable
import net.minecraft.entity.mob.HostileEntity
import net.minecraft.nbt.NbtCompound
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World
import java.util.*

abstract class AbstractJellyfishEntity(entityType: EntityType<out AbstractJellyfishEntity>, world: World) :
    HostileEntity(entityType, world), Angerable {
    var angerTicks = 0
    var targetUuid: UUID? = null

    init {
        this.setNoGravity(true)
    }

//    override fun initDataTracker(builder: DataTracker.Builder) {
//        super.initDataTracker(builder)
//    }

    override fun writeCustomDataToNbt(nbt: NbtCompound) {
        super.writeCustomDataToNbt(nbt)
        this.writeAngerToNbt(nbt)
    }

    override fun readCustomDataFromNbt(nbt: NbtCompound) {
        super.readCustomDataFromNbt(nbt)
        this.readAngerFromNbt(this.world, nbt)
    }

    override fun move(movementType: MovementType, movement: Vec3d) {
        super.move(movementType, movement)
        checkBlockCollision()
    }

    override fun tickMovement() {
//        if (world.isClient) {
//                world.addParticle(
//                    ParticleTypes.PORTAL,
//                    this.getParticleX(0.5),
//                    this.randomBodyY - 0.25,
//                    this.getParticleZ(0.5),
//                    (random.nextDouble() - 0.5) * 2.0,
//                    -random.nextDouble(),
//                    (random.nextDouble() - 0.5) * 2.0
//                )
//        }

        this.jumping = false
        if (!world.isClient) {
            this.tickAngerLogic(world as ServerWorld, true)
        }
        super.tickMovement()
    }

    override fun isClimbing(): Boolean = false

    override fun fall(fallDistance: Double, onGround: Boolean, landedState: BlockState, landedPosition: BlockPos) {}

    override fun playStepSound(pos: BlockPos, state: BlockState) {}

    override fun setAngerTime(ticks: Int) {
        this.angerTicks = ticks
    }

    override fun getAngerTime(): Int {
        return this.angerTicks
    }

    override fun setAngryAt(uuid: UUID?) {
        this.targetUuid = uuid
    }

    override fun getAngryAt(): UUID? {
        return this.targetUuid
    }

    override fun canTarget(type: EntityType<*>): Boolean {
        return true
    }

    companion object {
        const val GRAVITY_VALUE = 0.003

        fun createAttributes(): DefaultAttributeContainer.Builder {
            return HostileEntity.createAttributes()
                .add(EntityAttributes.GENERIC_ARMOR, 30.0)
                .add(EntityAttributes.GENERIC_ARMOR_TOUGHNESS, 20.0)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.3)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 64.0)
                .add(EntityAttributes.GENERIC_GRAVITY, 0.0)
        }
    }


}