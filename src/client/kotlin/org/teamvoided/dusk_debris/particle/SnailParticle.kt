package org.teamvoided.dusk_debris.particle

import com.mojang.blaze3d.vertex.VertexConsumer
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.particle.*
import net.minecraft.client.render.Camera
import net.minecraft.client.world.ClientWorld
import net.minecraft.entity.Entity
import net.minecraft.particle.DefaultParticleType
import net.minecraft.util.math.Direction
import net.minecraft.util.math.MathHelper.lerp
import net.minecraft.util.math.Vec3d
import org.joml.Quaternionf
import org.joml.Vector2d
import org.teamvoided.dusk_debris.util.Utils

class SnailParticle(
    world: ClientWorld,
    posX: Double,
    posY: Double,
    posZ: Double,
    velX: Double,
    velY: Double,
    velZ: Double,
    private val spriteProvider: SpriteProvider,
) : SpriteBillboardParticle(world, posX, posY, posZ, velX, velY, velZ) {
    private var attachment: Direction = Direction.DOWN

    init {
        this.setSpriteForAge(spriteProvider)
        this.maxAge = 200 + random.nextInt(100)
        this.scale = random.nextFloat() * 0.3f + 0.5f
        this.velocityMultiplier = 0.8f
        this.velocityX = velX
        this.velocityY = velY
        this.velocityZ = velZ
        this.gravityStrength = 5f
    }

    override fun getType(): ParticleTextureSheet = ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT

    public override fun getBrightness(tickDelta: Float): Int {
        return super.getBrightness(tickDelta)
    }

    override fun tick() {
        if (age++ >= this.maxAge) {
            this.markDead()
        } else {
            this.prevPosX = this.x
            this.prevPosY = this.y
            this.prevPosZ = this.z
            println(attachment)
            when (attachment) {
                Direction.DOWN -> this.velocityY -= 0.04 * gravityStrength
                Direction.UP -> this.velocityY = 0.05
                Direction.EAST -> this.velocityX = 0.05
                Direction.WEST -> this.velocityX = -0.05
                Direction.SOUTH -> this.velocityZ = 0.05
                Direction.NORTH -> this.velocityZ = -0.05
            }
            this.move(this.velocityX, this.velocityY, this.velocityZ)
        }
    }

    override fun move(x: Double, y: Double, z: Double) {
        var dx = x
        var dy = y
        var dz = z
        if ((dx != 0.0 || dy != 0.0 || dz != 0.0) && (dx * dx + dy * dy + dz * dz < 10000)) {
            val vec3d = Entity.adjustSingleAxisMovementForCollisions(
                null as Entity?,
                Vec3d(dx, dy, dz),
                this.boundingBox,
                this.world,
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
            this.boundingBox = boundingBox.offset(dx, dy, dz)
            this.repositionFromBoundingBox()
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
            Direction.Axis.Y -> Vec3d(vel.x, 0.0, vel.y)
            Direction.Axis.Z -> Vec3d(vel.x, vel.y, 0.0)
            Direction.Axis.X -> Vec3d(0.0, vel.x, vel.y)
        }.multiply(0.1)
        this.velocityX = velocity.x
        this.velocityY = velocity.y
        this.velocityZ = velocity.z
    }

    override fun buildGeometry(vertexConsumer: VertexConsumer, camera: Camera, tickDelta: Float) {
        val quaternionf = Quaternionf()
        // x is pitch, top and bottom
        // y is yaw, left to right
        // z is roll, side to side
        when (this.attachment.axis) {
            Direction.Axis.Z ->
                if (camera.pos.y < this.y) quaternionf.rotationX(Utils.rotate90)
                else quaternionf.rotationX(Utils.rotate270)

            Direction.Axis.Y ->
                if (camera.pos.x < this.x) quaternionf.rotationY(Utils.rotate270)
                else quaternionf.rotationY(Utils.rotate90)

            Direction.Axis.X ->
                if (camera.pos.z < this.z) quaternionf.rotationY(Utils.rotate180)
        }
        if (this.angle != 0f) {
            quaternionf.rotateZ(lerp(tickDelta, this.prevAngle, this.angle))
        }
        this.method_60373(vertexConsumer, camera, quaternionf, tickDelta)
    }

    @Environment(EnvType.CLIENT)
    class Factory(private val spriteProvider: SpriteProvider) : ParticleFactory<DefaultParticleType> {
        override fun createParticle(
            defaultParticleType: DefaultParticleType,
            world: ClientWorld,
            posX: Double,
            posY: Double,
            posZ: Double,
            velX: Double,
            velY: Double,
            velZ: Double,
        ): Particle {
            val particle = SnailParticle(world, posX, posY, posZ, velX, velY, velZ, this.spriteProvider)
            particle.setSprite(spriteProvider)
            return particle
        }
    }
}