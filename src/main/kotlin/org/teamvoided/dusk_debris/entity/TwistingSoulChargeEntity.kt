//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//
package org.teamvoided.dusk_debris.entity

import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.Ownable
import net.minecraft.entity.data.DataTracker
import net.minecraft.nbt.NbtCompound
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.math.MathHelper
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World
import org.teamvoided.dusk_debris.init.DuskEntities
import org.teamvoided.dusk_debris.init.DuskParticles
import org.teamvoided.dusk_debris.util.Utils.radToDeg
import org.teamvoided.dusk_debris.util.addParticle
import java.util.*
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

class TwistingSoulChargeEntity : Entity, Ownable {
    constructor (entityType: EntityType<out TwistingSoulChargeEntity>, world: World) : super(entityType, world)

    constructor(entityType: EntityType<out TwistingSoulChargeEntity>, world: World, owner: LivingEntity) :
            super(entityType, world) {
        this.owner = owner
    }


    constructor(world: World, x: Double, y: Double, z: Double) : super(DuskEntities.TWISTING_SOUL_CHARGE, world) {
        this.setPosition(x, y, z)
    }

    constructor(world: World, owner: LivingEntity) : super(DuskEntities.TWISTING_SOUL_CHARGE, world) {
        this.setPosition(x, y, z)
        this.owner = owner
    }

    private var ownerUuid: UUID? = null
    private var owner: Entity? = null
    private var targetUuid: UUID? = null
    override fun initDataTracker(builder: DataTracker.Builder) {}

    override fun readCustomDataFromNbt(nbt: NbtCompound) {
        if (nbt.containsUuid("Owner")) {
            this.ownerUuid = nbt.getUuid("Owner")
            this.owner = null
        }
    }

    override fun writeCustomDataToNbt(nbt: NbtCompound) {
        if (this.ownerUuid != null) {
            nbt.putUuid("Owner", this.ownerUuid)
        }
    }

    override fun tick() {
        super.tick()
        if (world.isClient) {
            this.spawnParticles()
        }
        if (targetUuid == null) {

        } else if (this.owner != null) {
            val orbitOffset = (owner!!.width) + 2
            val targetPos = Vec3d(
                orbitOffset * sin(age.toDouble() / 20),
                owner!!.height / 2.0,
                orbitOffset * cos(age.toDouble() / 20)
            ).add(this.owner!!.pos).subtract(pos)
            val vel2 = velocity.add(targetPos).multiply(0.1)
            addVelocityInternal(vel2)
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
        world.addParticle(
            DuskParticles.DRAINED_SOUL,
            pos.add(
                (random.nextDouble() - 0.5) * 0.6,
                (random.nextDouble() - 0.5) * 0.6,
                (random.nextDouble() - 0.5) * 0.6
            ),
            Vec3d(
                (random.nextDouble() - 0.5) * velocity,
                (random.nextDouble() - 0.5) * velocity,
                (random.nextDouble() - 0.5) * velocity
            )
        )
    }

    var target: UUID?
        get() = targetUuid
        set(target) {
            targetUuid = target
        }


    override fun getOwner(): Entity? {
        if (this.owner != null && !this.owner!!.isRemoved) {
            return this.owner;
        } else {
            if (this.ownerUuid != null) {
                val world: World = this.world
                if (world is ServerWorld) {
                    val serverWorld: ServerWorld = world
                    this.owner = serverWorld.getEntity(this.ownerUuid)
                    return this.owner;
                }
            }
            return null
        }
    }


    fun setOwner(entity: Entity?) {
        if (entity != null) {
            this.ownerUuid = entity.uuid
            this.owner = entity;
        }
    }

    companion object {
    }
}
