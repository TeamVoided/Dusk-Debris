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
import org.joml.Vector2d
import org.teamvoided.dusk_debris.util.ParticleHelper
import org.teamvoided.dusk_debris.util.Utils
import org.teamvoided.dusk_debris.util.Utils.PI
import voidlib.devin.world.gen.max
import java.awt.Color
import kotlin.math.max

class ExhaustBlastParticle(
    world: ClientWorld,
    posX: Double,
    posY: Double,
    posZ: Double,
    velX: Double,
    velY: Double,
    velZ: Double,
    private val spriteProvider: SpriteProvider,
    private val isWarmup: Boolean = false
) : SpriteBillboardParticle(world, posX, posY, posZ, velX, velY, velZ) {

    init {
        this.setSpriteForAge(this.spriteProvider)
        this.maxAge = if (isWarmup) 40 else 15 + random.nextInt(15)
        this.scale = random.nextFloat() * 0.5f + 0.3f
        this.velocityMultiplier = 0.8f
        this.velocityX = velX
        this.velocityY = velY
        this.velocityZ = velZ
        this.onGround = this.isWarmup

        val color = ParticleHelper.chooseColor(
            Color(0xA89583),
            Color(0xF1C9AD), random
        )
        colorRed = color.x
        colorGreen = color.y
        colorBlue = color.z
    }

    override fun getType(): ParticleTextureSheet = ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT


    public override fun getBrightness(tickDelta: Float): Int {
        val upper = if (isWarmup) (15728880 * (age + tickDelta) / (maxAge - 1)).toInt() else 15728880 / 3
        return max(super.getBrightness(tickDelta), upper)
    }

    override fun tick() {
        if (age++ >= this.maxAge) {
            this.markDead()
        } else {
            this.setSpriteForAge(this.spriteProvider)
            this.prevPosX = this.x
            this.prevPosY = this.y
            this.prevPosZ = this.z
            if (maxAge - 5 < age) {
                velocityX *= velocityMultiplier
                velocityY *= velocityMultiplier
                velocityZ *= velocityMultiplier
            }
            if (!this.onGround) {
                this.move(this.velocityX, this.velocityY, this.velocityZ)
            } else {
                this.x += velocityX
                this.y += velocityY
                this.z += velocityZ
            }
        }
    }

    override fun getSize(tickDelta: Float): Float {
        return if (isWarmup && 20 > age) super.getSize(tickDelta) * (((age + tickDelta) / 21) / 2 + 0.5f)
        else super.getSize(tickDelta)
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
            this.boundingBox = boundingBox.offset(dx, dy, dz)
            this.repositionFromBoundingBox()
        }

        if (x != dx) {
            setCollide(if (x > 0) Direction.WEST else Direction.EAST)
        }

        if (y != dy) {
            setCollide(if (y > 0) Direction.DOWN else Direction.UP)
        }

        if (z != dz) {
            setCollide(if (z > 0) Direction.NORTH else Direction.SOUTH)
        }
    }

    private fun setCollide(direction: Direction) {
        this.onGround = true
        val vel = Vector2d(random.nextDouble() - 0.5, random.nextDouble() - 0.5).normalize()
        val velocity = when (direction) {
            Direction.DOWN -> Vec3d(vel.x, -1.0, vel.y)
            Direction.UP -> Vec3d(vel.x, 1.0, vel.y)
            Direction.NORTH -> Vec3d(vel.x, vel.y, -1.0)
            Direction.SOUTH -> Vec3d(vel.x, vel.y, 1.0)
            Direction.WEST -> Vec3d(-1.0, vel.x, vel.y)
            Direction.EAST -> Vec3d(1.0, vel.x, vel.y)
        }.multiply(0.1)
        this.velocityX = velocity.x
        this.velocityY = velocity.y
        this.velocityZ = velocity.z
        this.age = this.maxAge - 10
    }

    @Environment(EnvType.CLIENT)
    class WarmUpFactory(private val spriteProvider: SpriteProvider) : ParticleFactory<DefaultParticleType> {
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
            val particle = ExhaustBlastParticle(world, posX, posY, posZ, velX, velY, velZ, this.spriteProvider, true)
            particle.setSprite(spriteProvider)
            return particle
        }
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
            val particle = ExhaustBlastParticle(world, posX, posY, posZ, velX, velY, velZ, this.spriteProvider)
            particle.setSprite(spriteProvider)
            return particle
        }
    }
}