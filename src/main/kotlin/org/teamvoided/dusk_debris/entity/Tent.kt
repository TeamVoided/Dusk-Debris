//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//
package org.teamvoided.dusk_debris.entity

import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.data.DataTracker
import net.minecraft.entity.data.TrackedData
import net.minecraft.entity.data.TrackedDataHandlerRegistry
import net.minecraft.entity.projectile.PersistentProjectileEntity
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World
import org.teamvoided.dusk_debris.init.DuskParticles
import org.teamvoided.dusk_debris.util.addParticle
import java.util.*
import kotlin.math.cos
import kotlin.math.sin

class Tent : PersistentProjectileEntity {
    constructor (entityType: EntityType<out Tent>, world: World) : super(entityType, world)

    constructor(world: World, x: Double, y: Double, z: Double, stack: ItemStack, weapon: ItemStack) : super(
        EntityType.ARROW,
        x,
        y,
        z,
        world,
        stack,
        weapon
    )

    constructor(world: World, owner: LivingEntity, stack: ItemStack, weapon: ItemStack) : super(
        EntityType.ARROW,
        owner,
        world,
        stack,
        weapon
    )

    private var targetUuid: UUID? = null

    override fun setStack(stack: ItemStack) {
        super.setStack(stack)
    }

    override fun tick() {
        super.tick()
        if (world.isClient) {
            this.spawnParticles()
        } else if (this.inGround && (this.inGroundTime != 0) && (this.inGroundTime >= 600)) {
            world.sendEntityStatus(this, 0.toByte())
        }
        if (targetUuid == null && this.owner != null) {
            val orbitOffset = (owner!!.width) + 2
            val targetPos = Vec3d(
                orbitOffset * sin(age.toDouble() / 20),
                owner!!.height / 2.0,
                orbitOffset * cos(age.toDouble() / 20)
            ).add(this.owner!!.pos)

            this.setVelocity(targetPos.subtract(pos))
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

    private fun spawnParticles() {
        world.addParticle(
            DuskParticles.DRAINED_SOUL,
            pos.add(
                (random.nextDouble() - 0.5) * 0.6,
                (random.nextDouble() - 0.5) * 0.6,
                (random.nextDouble() - 0.5) * 0.6
            ),
            Vec3d(
                (random.nextDouble() - 0.5) * 0.1,
                (random.nextDouble() - 0.5) * 0.1,
                (random.nextDouble() - 0.5) * 0.1
            )
        )
    }

    var target: UUID?
        get() = targetUuid
        set(target) {
            targetUuid = target
        }

    override fun onHit(target: LivingEntity) {
        super.onHit(target)
    }

    override fun getDefaultItemStack(): ItemStack {
        return ItemStack(Items.ARROW)
    }


    companion object {
    }
}
