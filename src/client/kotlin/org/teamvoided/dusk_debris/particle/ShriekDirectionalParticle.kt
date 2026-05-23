package org.teamvoided.dusk_debris.particle

import com.mojang.blaze3d.vertex.VertexConsumer
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.Camera
import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.client.particle.Particle
import net.minecraft.client.particle.ParticleProvider
import net.minecraft.client.particle.ShriekParticle
import net.minecraft.client.particle.SpriteSet
import net.minecraft.core.Direction
import net.minecraft.util.Mth
import org.joml.Quaternionf
import org.joml.Vector3f
import org.teamvoided.dusk_debris.util.Utils

class ShriekDirectionalParticle(
    world: ClientLevel, x: Double, y: Double, z: Double,
    private val direction: Direction,
    delay: Int
) : ShriekParticle(world, x, y, z, delay) {
    init {
        this.xd = direction.normal.x * 0.1
        this.yd = direction.normal.y * 0.1
        this.zd = direction.normal.z * 0.1
    }

    override fun render(vertexConsumer: VertexConsumer, camera: Camera, tickDelta: Float) {
        if (this.delay <= 0) {
            this.alpha = 1.0f - Mth.clamp((age + tickDelta) / lifetime, 0.0f, 1.0f)
            val rotate = directionalRotation()
            val quaternionf = Quaternionf()
            quaternionf.rotationYXZ(rotate.y, rotate.x, rotate.z)
            this.renderRotatedQuad(vertexConsumer, camera, quaternionf, tickDelta)
            quaternionf.rotationYXZ(rotate.y - Utils.rotate180, -rotate.x, rotate.z)
            this.renderRotatedQuad(vertexConsumer, camera, quaternionf, tickDelta)
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
    class Factory(private val spriteProvider: SpriteSet) : ParticleProvider<ShriekDirectionalParticleEffect> {
        override fun createParticle(
            type: ShriekDirectionalParticleEffect,
            world: ClientLevel,
            posX: Double,
            posY: Double,
            posZ: Double,
            velX: Double,
            velY: Double,
            velZ: Double,
        ): Particle {
            val particle = ShriekDirectionalParticle(world, posX, posY, posZ, type.direction, type.delay)
            particle.pickSprite(spriteProvider)
            return particle
        }
    }
}