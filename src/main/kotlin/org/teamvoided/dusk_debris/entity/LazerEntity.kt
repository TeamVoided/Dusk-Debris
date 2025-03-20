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
import net.minecraft.util.Identifier
import net.minecraft.util.math.MathHelper
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World
import org.slf4j.Logger
import org.teamvoided.dusk_debris.data.DuskDamageTypes
import org.teamvoided.dusk_debris.init.DuskEntities
import org.teamvoided.dusk_debris.init.DuskParticles
import org.teamvoided.dusk_debris.util.Utils
import java.util.*
import kotlin.math.cos
import kotlin.math.sin

class LazerEntity(entityType: EntityType<out LazerEntity>, world: World) :
    LightningCloudEntity(entityType, world) {
    private var target: Vec3d
    var prevTarget: Vec3d

    constructor(world: World, x: Double, y: Double, z: Double) : this(DuskEntities.LAZER_ENTITY, world) {
        this.setPosition(x, y, z)
    }

    init {
        this.target = this.pos
        this.prevTarget = this.target
    }

    override fun initDataTracker(builder: DataTracker.Builder) {
        super.initDataTracker(builder)
    }

    override fun readCustomDataFromNbt(nbt: NbtCompound) {
        super.readCustomDataFromNbt(nbt)
    }

    override fun writeCustomDataToNbt(nbt: NbtCompound) {
        super.writeCustomDataToNbt(nbt)
    }

    override fun tick() {
        super.tick()
        setTarget(this.pos.add(0.0, sin(age / 10.0) * 10, 0.0))

//        val x = cos(age / 23.0) * 10
//        val y = cos(age / 5.0)  * 10
//        val z = cos(age / 13.0) * 10
//        setTarget(this.pos.add(x, y, z))
//        val x = 54.5
//        val y = 64.5
//        val z = 1.5
//        setTarget(Vec3d(x, y, z))
    }

    override fun tickClient(wait: Boolean) {

    }

    override fun tickServer(wait: Boolean) {
        super.tickServer(wait)
    }

    override fun doDamage() {
        super.doDamage()
    }

    fun setTarget(vec3d: Vec3d) {
        this.prevTarget = this.target
        this.target = vec3d
    }

    fun getTarget(): Vec3d {
        return this.target
    }

//    var texture: Identifier
//        get() = getDataTracker().get(PARTICLE_ID)
//        set(texture) {
//            getDataTracker().set(PARTICLE_ID, texture)
//        }


    companion object {
    }
}
