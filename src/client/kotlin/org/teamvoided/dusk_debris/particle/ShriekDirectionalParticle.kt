package org.teamvoided.dusk_debris.particle

import com.mojang.blaze3d.vertex.VertexConsumer
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.particle.Particle
import net.minecraft.client.particle.ParticleFactory
import net.minecraft.client.particle.ShriekParticle
import net.minecraft.client.particle.SpriteProvider
import net.minecraft.client.render.Camera
import net.minecraft.client.world.ClientWorld
import net.minecraft.util.math.Direction
import net.minecraft.util.math.MathHelper
import org.joml.Quaternionf
import org.joml.Vector3f
import org.teamvoided.dusk_debris.util.Utils

class ShriekDirectionalParticle(
    world: ClientWorld, x: Double, y: Double, z: Double,
    private val direction: Direction,
    delay: Int
) : ShriekParticle(world, x, y, z, delay) {
    init {
        this.velocityX = direction.vector.x * 0.1
        this.velocityY = direction.vector.y * 0.1
        this.velocityZ = direction.vector.z * 0.1
    }

    override fun buildGeometry(vertexConsumer: VertexConsumer, camera: Camera, tickDelta: Float) {
        if (this.delay <= 0) {
            this.colorAlpha = 1.0f - MathHelper.clamp((age + tickDelta) / maxAge, 0.0f, 1.0f)
            val rotate = directionalRotation()
            val quaternionf = Quaternionf()
            quaternionf.rotationYXZ(rotate.y, rotate.x, rotate.z)
            this.method_60373(vertexConsumer, camera, quaternionf, tickDelta)
            quaternionf.rotationYXZ(rotate.y - Utils.rotate180, -rotate.x, rotate.z)
            this.method_60373(vertexConsumer, camera, quaternionf, tickDelta)
        }
    }

    fun directionalRotation(): Vector3f {
        return when (direction) {
            Direction.UP, Direction.DOWN -> Vector3f(-Utils.rotate60, 0f, 0f)
            Direction.NORTH -> Vector3f(-Utils.rotate30, 0f, 0f)
            Direction.SOUTH -> Vector3f(Utils.rotate30, 0f, 0f)
            Direction.EAST -> Vector3f(Utils.rotate30, Utils.rotate90, 0f)
            Direction.WEST -> Vector3f(-Utils.rotate30, Utils.rotate90, 0f)
        }
    }

    @Environment(EnvType.CLIENT)
    class Factory(private val spriteProvider: SpriteProvider) : ParticleFactory<ShriekDirectionalParticleEffect> {
        override fun createParticle(
            type: ShriekDirectionalParticleEffect,
            world: ClientWorld,
            posX: Double,
            posY: Double,
            posZ: Double,
            velX: Double,
            velY: Double,
            velZ: Double,
        ): Particle {
            val particle = ShriekDirectionalParticle(world, posX, posY, posZ, type.direction, type.delay)
            particle.setSprite(spriteProvider)
            return particle
        }
    }
}