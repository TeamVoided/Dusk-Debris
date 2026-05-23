package org.teamvoided.dusk_debris.particle

import com.mojang.blaze3d.vertex.VertexConsumer
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.Camera
import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.client.particle.*
import net.minecraft.core.Direction
import net.minecraft.core.particles.SimpleParticleType
import net.minecraft.world.entity.Entity
import net.minecraft.world.phys.Vec3
import org.joml.Vector2d
import org.teamvoided.dusk_debris.block.ExhaustBlock
import org.teamvoided.dusk_debris.util.ParticleHelper
import java.awt.Color
import kotlin.math.max

class ExhaustBlastParticle(
    world: ClientLevel,
    posX: Double,
    posY: Double,
    posZ: Double,
    velX: Double,
    velY: Double,
    velZ: Double,
    private val spriteProvider: SpriteSet,
    private val isWarmup: Boolean = false
) : TextureSheetParticle(world, posX, posY, posZ, velX, velY, velZ) {

    init {
        if (!isWarmup) this.setSpriteFromAge(this.spriteProvider) else this.pickSprite(this.spriteProvider)
        this.lifetime =
            if (isWarmup) ExhaustBlock.WORLD_TIME_MOD * ExhaustBlock.WARMUP_DURATION + 10 + random.nextInt(25)
            else 15 + random.nextInt(15)
        this.quadSize = if (isWarmup) random.nextFloat() * 0.3f + 0.2f else random.nextFloat() * 0.5f + 0.3f
        this.friction = 0.8f
        this.xd = velX
        this.yd = velY
        this.zd = velZ
        this.onGround = this.isWarmup

        val color = ParticleHelper.chooseColor(
            Color(0xA89583),
            Color(0xF1C9AD),
            random
        )
        rCol = color.x
        gCol = color.y
        bCol = color.z
    }

    override fun getRenderType(): ParticleRenderType = ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT


    public override fun getLightColor(tickDelta: Float): Int {
        val upper = if (isWarmup) (15728880 * (age + tickDelta) / (lifetime - 1)).toInt() else 15728880 / 2
        return max(super.getLightColor(tickDelta), upper)
    }

    override fun tick() {
        if (age++ >= this.lifetime) {
            this.remove()
        } else {
            if (!isWarmup) this.setSpriteFromAge(this.spriteProvider)
            this.xo = this.x
            this.yo = this.y
            this.zo = this.z
            if (!this.isWarmup && lifetime - 5 < age) {
                xd *= friction
                yd *= friction
                zd *= friction
            }
            if (this.isWarmup) {
                val mult = ((age + 5) / (lifetime + 5.0))
                this.move(this.xd * mult, this.yd * mult, this.zd * mult)
            } else if (!this.onGround) {
                this.move(this.xd, this.yd, this.zd)
            } else {
                this.x += xd
                this.y += yd
                this.z += zd
            }
        }
    }

    override fun render(vertexConsumer: VertexConsumer, camera: Camera, tickDelta: Float) {
        super.render(vertexConsumer, camera, tickDelta)
    }

    override fun getQuadSize(tickDelta: Float): Float {
        val supr = super.getQuadSize(tickDelta)
        if (isWarmup) return ((age + tickDelta + lifetime) / (2 * lifetime)) * supr
        return supr
    }

    override fun move(x: Double, y: Double, z: Double) {
        if (isWarmup) {
            super.move(x, y, z)
        } else {
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
                this.boundingBox = boundingBox.move(dx, dy, dz)
                this.setLocationFromBoundingbox()
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
    }

    private fun setCollide(direction: Direction) {
        this.onGround = true
        val vel = Vector2d(random.nextDouble() - 0.5, random.nextDouble() - 0.5).normalize()
        val velocity = when (direction) {
            Direction.DOWN -> Vec3(vel.x, -1.0, vel.y)
            Direction.UP -> Vec3(vel.x, 1.0, vel.y)
            Direction.NORTH -> Vec3(vel.x, vel.y, -1.0)
            Direction.SOUTH -> Vec3(vel.x, vel.y, 1.0)
            Direction.WEST -> Vec3(-1.0, vel.x, vel.y)
            Direction.EAST -> Vec3(1.0, vel.x, vel.y)
        }.scale(0.2)
        this.xd = velocity.x
        this.yd = velocity.y
        this.zd = velocity.z
        this.age = this.lifetime - random.nextInt(15) + 5
    }

    @Environment(EnvType.CLIENT)
    class WarmUpFactory(private val spriteProvider: SpriteSet) : ParticleProvider<SimpleParticleType> {
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
            val particle = ExhaustBlastParticle(world, posX, posY, posZ, velX, velY, velZ, this.spriteProvider, true)
            particle.pickSprite(spriteProvider)
            return particle
        }
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
            val particle = ExhaustBlastParticle(world, posX, posY, posZ, velX, velY, velZ, this.spriteProvider)
            particle.pickSprite(spriteProvider)
            return particle
        }
    }
}