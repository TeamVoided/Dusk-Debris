package org.teamvoided.dusk_debris.entity

import net.minecraft.nbt.CompoundTag
import net.minecraft.network.syncher.SynchedEntityData
import net.minecraft.server.level.ServerLevel
import net.minecraft.util.Mth
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.TraceableEntity
import net.minecraft.world.level.Level
import net.minecraft.world.phys.Vec3
import org.teamvoided.dusk_debris.init.DuskEntities
import org.teamvoided.dusk_debris.init.DuskParticles
import org.teamvoided.dusk_debris.util.addParticle
import java.util.*
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

class TwistingSoulChargeEntity : Entity, TraceableEntity {
    constructor (entityType: EntityType<out TwistingSoulChargeEntity>, world: Level) : super(entityType, world)
    constructor(world: Level, owner: LivingEntity) : super(DuskEntities.TWISTING_SOUL_CHARGE, world) {
        this.setOwner(owner)
    }

    private var ownerUuid: UUID? = null
    private var owner: Entity? = null
    private var targetUuid: UUID? = null
    override fun defineSynchedData(builder: SynchedEntityData.Builder) {}

    override fun readAdditionalSaveData(nbt: CompoundTag) {
        if (nbt.hasUUID("Owner")) {
            this.ownerUuid = nbt.getUUID("Owner")
            this.owner = null
        }
    }

    override fun addAdditionalSaveData(nbt: CompoundTag) {
        if (this.ownerUuid != null) {
            nbt.putUUID("Owner", this.ownerUuid)
        }
    }

    override fun tick() {
        super.tick()
        if (level().isClientSide) {
            this.spawnParticles()
        }
        if (this.owner != null) {
            val orbitOffset = (owner!!.bbWidth) + 2
            val targetPos = Vec3(
                orbitOffset * sin(tickCount.toDouble() / 20),
                owner!!.bbHeight / 2.0,
                orbitOffset * cos(tickCount.toDouble() / 20)
            ).add(this.owner!!.position()).subtract(position())
            val vel2 = deltaMovement.add(targetPos).scale(0.1)
            lerpMotion(vel2.x, vel2.y, vel2.z)

            val h = this.x + this.deltaMovement.x
            val j = this.y + this.deltaMovement.y
            val k = this.z + this.deltaMovement.z

            this.setPos(h, j, k)
            this.checkInsideBlocks()
        }
    }

//    override fun tick() {
//        if ((!world.isClient && this.blockY > world.topY + 30)) {
//            this.discard()
//        } else {
//            val owner = this.owner
//            if (owner != null) {
//                val orbitOffset = (owner.width) + 2
//                this.prevYaw = yaw
//                this.prevPitch = pitch
//                this.yaw = (age / 20f) * -radToDeg - 180f
//                this.pitch = 0f
//                setPosition(
//                    Vec3d(
//                        orbitOffset * sin(age.toDouble() / 20),
//                        owner.height / 2.0,
//                        orbitOffset * cos(age.toDouble() / 20)
//                    ).add(owner.pos)
//                )
//                velocity = Vec3d.ZERO
//            } else {
//                this.discard()
//            }
//            if (!world.isClient && world.isChunkLoaded(this.blockPos)) {
//                val serverWorld = world as ServerWorld
//                serverWorld.spawnParticles(
//                    DuskParticles.DRAINED_SOUL,
//                    pos.add(
//                        (random.nextDouble() - 0.5) * 0.6,
//                        (random.nextDouble() - 0.5) * 0.6,
//                        (random.nextDouble() - 0.5) * 0.6
//                    ),
//                    Vec3d(
//                        (random.nextDouble() - 0.5) * 0.1,
//                        (random.nextDouble() - 0.5) * 0.1,
//                        (random.nextDouble() - 0.5) * 0.1
//                    )
//                )
//            }
//            super.tick()
//        }
//    }


    private fun spawnParticles(velocity: Double = 0.1) {
        level().addParticle(
            DuskParticles.DRAINED_SOUL,
            position().add(
                (random.nextDouble() - 0.5) * 0.6,
                (random.nextDouble() - 0.5) * 0.6,
                (random.nextDouble() - 0.5) * 0.6
            ),
            Vec3(
                (random.nextDouble() - 0.5) * velocity,
                (random.nextDouble() - 0.5) * velocity,
                (random.nextDouble() - 0.5) * velocity
            )
        )
    }

    override fun lerpMotion(x: Double, y: Double, z: Double) {
        this.setDeltaMovement(x, y, z)
        if (this.xRotO == 0.0f && this.yRotO == 0.0f) {
            val d = sqrt(x * x + z * z)
            this.setXRot((Mth.atan2(y, d) * 57.2957763671875).toFloat())
            this.setYRot((Mth.atan2(x, z) * 57.2957763671875).toFloat())
            this.xRotO = this.xRot
            this.yRotO = this.yRot
            this.moveTo(this.x, this.y, this.z, this.yRot, this.xRot)
        }
    }

    var target: UUID?
        get() = targetUuid
        set(target) {
            targetUuid = target
        }


    override fun getOwner(): Entity? {
        if (this.owner != null && !this.owner!!.isRemoved) {
            return this.owner
        } else {
            if (this.ownerUuid != null) {
                val world: Level = this.level()
                if (world is ServerLevel) {
                    val serverWorld: ServerLevel = world
                    this.owner = serverWorld.getEntity(this.ownerUuid)
                    return this.owner
                }
            }
            return null
        }
    }


    fun setOwner(entity: Entity?) {
        if (entity != null) {
            this.ownerUuid = entity.uuid
            this.owner = entity
        }
    }

    companion object {
    }
}
