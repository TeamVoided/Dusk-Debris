//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//
package org.teamvoided.dusk_debris.entity

import com.mojang.logging.LogUtils
import net.minecraft.block.piston.PistonBehavior
import net.minecraft.entity.*
import net.minecraft.entity.data.DataTracker
import net.minecraft.entity.data.TrackedData
import net.minecraft.entity.data.TrackedDataHandlerRegistry
import net.minecraft.nbt.NbtCompound
import net.minecraft.particle.ColoredParticleEffect
import net.minecraft.particle.ParticleEffect
import net.minecraft.particle.ParticleTypes
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.math.MathHelper
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World
import org.slf4j.Logger
import org.teamvoided.dusk_debris.data.DuskDamageTypes
import org.teamvoided.dusk_debris.init.DuskEntities
import org.teamvoided.dusk_debris.init.DuskParticles
import org.teamvoided.dusk_debris.util.Utils
import java.util.*

class LazerEntity(entityType: EntityType<out LazerEntity>, world: World) :
    LightningCloudEntity(entityType, world) {

    constructor(world: World, x: Double, y: Double, z: Double) : this(DuskEntities.LAZER_ENTITY, world) {
        this.setPosition(x, y, z)
    }

    init {
        this.pitch = -0.5f
        this.yaw = 10f
        this.prevPitch = this.pitch
        this.prevYaw = this.yaw
    }

    override fun initDataTracker(builder: DataTracker.Builder) {
        super.initDataTracker(builder)
        builder.add(LENGTH, DEFAULT_LENGTH)
    }

    override fun readCustomDataFromNbt(nbt: NbtCompound) {
        super.readCustomDataFromNbt(nbt)
        this.length = nbt.getInt("Length")
    }

    override fun writeCustomDataToNbt(nbt: NbtCompound) {
        super.writeCustomDataToNbt(nbt)
        nbt.putInt("Length", this.length)
    }

    var length: Int
        get() = getDataTracker().get(LENGTH)
        set(length) {
            if (!world.isClient) {
                getDataTracker().set(LENGTH, length)
            }
        }

    override fun tick() {
        super.tick()
        this.prevPitch = this.pitch
        this.prevYaw = this.yaw
        this.pitch = 0f
        this.yaw = 0f
    }

    override fun tickClient(wait: Boolean) {

    }

    override fun tickServer(wait: Boolean) {
        super.tickServer(wait)
    }

    override fun doDamage() {
        super.doDamage()
    }

    companion object {
        const val DEFAULT_LENGTH = 1
        private val LENGTH: TrackedData<Int> = DataTracker.registerData(
            LazerEntity::class.java, TrackedDataHandlerRegistry.INTEGER
        )
    }
}
