package org.teamvoided.dusk_debris.particle.stupid_particles.abstracts

import com.mojang.blaze3d.vertex.VertexConsumer
import net.minecraft.client.particle.Particle
import net.minecraft.client.particle.ParticleTextureSheet
import net.minecraft.client.render.Camera
import net.minecraft.client.world.ClientWorld

open class CubeTypeParticle : Particle {
    protected var scale: Float

    protected constructor(world: ClientWorld, x: Double, y: Double, z: Double) : super(world, x, y, z) {
        this.scale = 1f
    }

    protected constructor(
        world: ClientWorld,
        x: Double, y: Double, z: Double,
        xVel: Double, yVel: Double, zVel: Double
    ) : super(world, x, y, z, xVel, yVel, zVel) {
        this.scale = 1f
    }

    override fun buildGeometry(vertexConsumer: VertexConsumer, camera: Camera, tickDelta: Float) {
        TODO("Not yet implemented")
    }

    fun drawFromEntityModel(camera: Camera, tickDelta: Float) {}


    override fun getType(): ParticleTextureSheet = ParticleTextureSheet.CUSTOM
}