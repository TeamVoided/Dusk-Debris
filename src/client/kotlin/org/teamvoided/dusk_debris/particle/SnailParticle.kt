package org.teamvoided.dusk_debris.particle

import com.mojang.blaze3d.vertex.VertexConsumer
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.Camera
import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.client.particle.*
import net.minecraft.core.Direction
import net.minecraft.core.particles.SimpleParticleType
import net.minecraft.util.Mth.lerp
import net.minecraft.world.entity.Entity
import net.minecraft.world.phys.Vec3
import org.joml.Quaternionf
import org.joml.Vector2d
import org.teamvoided.dusk_debris.util.Utils

class SnailParticle(
    world: ClientLevel,
    posX: Double,
    posY: Double,
    posZ: Double,
    velX: Double,
    velY: Double,
    velZ: Double,
    private val spriteProvider: SpriteSet,
) : TextureSheetParticle(world, posX, posY, posZ, velX, velY, velZ) {
    private var attachment: Direction = Direction.DOWN

    init {
        this.setSpriteFromAge(spriteProvider)
        this.lifetime = 200 + random.nextInt(100)
        this.quadSize = random.nextFloat() * 0.3f + 0.5f
        this.friction = 0.8f
        this.xd = velX
        this.yd = velY
        this.zd = velZ
        this.gravity = 5f
    }

    override fun getRenderType(): ParticleRenderType = ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT

    public override fun getLightColor(tickDelta: Float): Int {
        return super.getLightColor(tickDelta)
    }

    override fun tick() {
        if (age++ >= this.lifetime) {
            this.remove()
        } else {
            this.xo = this.x
            this.yo = this.y
            this.zo = this.z
            println(attachment)
            when (attachment) {
                Direction.DOWN -> this.yd -= 0.04 * gravity
                Direction.UP -> this.yd = 0.05
                Direction.EAST -> this.xd = 0.05
                Direction.WEST -> this.xd = -0.05
                Direction.SOUTH -> this.zd = 0.05
                Direction.NORTH -> this.zd = -0.05
            }
            this.move(this.xd, this.yd, this.zd)
        }
    }

    override fun move(x: Double, y: Double, z: Double) {
        var dx = x
        var dy = y
        var dz = z
        if ((dx != 0.0 || dy != 0.0 || dz != 0.0) && (dx * dx + dy * dy + dz * dz < 10000)) {
            val vec3d = Entity.collideBoundingBox(
                null as Entity?,
                Vec3(dx, dy, dz),
                this.boundingBox,
                this.level,
                listOf()
            )
            dx = vec3d.x
            dy = vec3d.y
            dz = vec3d.z
        }

        if (dx != 0.0 || dy != 0.0 || dz != 0.0) {
            when (attachment.axis) {
                Direction.Axis.Y -> if (dy != 0.0) setCollide(Direction.DOWN)
                Direction.Axis.X -> if (dx != 0.0) setCollide(Direction.DOWN)
                Direction.Axis.Z -> if (dz != 0.0) setCollide(Direction.DOWN)
            }
            this.boundingBox = boundingBox.move(dx, dy, dz)
            this.setLocationFromBoundingbox()
        }
        this.onGround = false
        if (x != dx) setCollide(if (x > 0) Direction.WEST else Direction.EAST)
        if (y != dy) setCollide(if (y > 0) Direction.DOWN else Direction.UP)
        if (z != dz) setCollide(if (z > 0) Direction.NORTH else Direction.SOUTH)
    }

    private fun setCollide(direction: Direction) {
        this.onGround = true
        if (attachment != direction) wallTouchVel(direction)
    }

    private fun wallTouchVel(direction: Direction) {
        this.attachment = direction
        val vel = Vector2d(random.nextDouble() - 0.5, random.nextDouble() - 0.5).normalize()
        val velocity = when (direction.axis) {
            Direction.Axis.Y -> Vec3(vel.x, 0.0, vel.y)
            Direction.Axis.Z -> Vec3(vel.x, vel.y, 0.0)
            Direction.Axis.X -> Vec3(0.0, vel.x, vel.y)
        }.scale(0.1)
        this.xd = velocity.x
        this.yd = velocity.y
        this.zd = velocity.z
    }

    override fun render(vertexConsumer: VertexConsumer, camera: Camera, tickDelta: Float) {
        val quaternionf = Quaternionf()
        // x is pitch, top and bottom
        // y is yaw, left to right
        // z is roll, side to side
        when (this.attachment.axis) {
            Direction.Axis.Z ->
                if (camera.position.y < this.y) quaternionf.rotationX(Utils.rotate90)
                else quaternionf.rotationX(Utils.rotate270)

            Direction.Axis.Y ->
                if (camera.position.x < this.x) quaternionf.rotationY(Utils.rotate270)
                else quaternionf.rotationY(Utils.rotate90)

            Direction.Axis.X ->
                if (camera.position.z < this.z) quaternionf.rotationY(Utils.rotate180)
        }
        if (this.roll != 0f) {
            quaternionf.rotateZ(lerp(tickDelta, this.oRoll, this.roll))
        }
        this.renderRotatedQuad(vertexConsumer, camera, quaternionf, tickDelta)
    }

    @Environment(EnvType.CLIENT)
    class Factory(private val spriteProvider: SpriteSet) : ParticleProvider<SimpleParticleType> {
        override fun createParticle(
            defaultParticleType: SimpleParticleType,
            world: ClientLevel,
            posX: Double,
            posY: Double,
            posZ: Double,
            velX: Double,
            velY: Double,
            velZ: Double,
        ): Particle {
            val particle = SnailParticle(world, posX, posY, posZ, velX, velY, velZ, this.spriteProvider)
            particle.pickSprite(spriteProvider)
            return particle
        }
    }
}