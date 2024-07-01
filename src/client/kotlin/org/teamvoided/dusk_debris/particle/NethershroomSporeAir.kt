package org.teamvoided.dusk_debris.particle

import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.particle.*
import net.minecraft.client.world.ClientWorld
import net.minecraft.particle.DefaultParticleType
import net.minecraft.util.math.MathHelper
import java.util.*
@Environment(EnvType.CLIENT)
class NethershroomSporeAir(private val spriteProvider: SpriteProvider) : ParticleFactory<DefaultParticleType> {
    override fun createParticle(
        defaultParticleType: DefaultParticleType,
        world: ClientWorld,
        d: Double,
        e: Double,
        f: Double,
        g: Double,
        h: Double,
        i: Double
    ): Particle {
        val waterSuspendParticle: WaterSuspendParticle = object : WaterSuspendParticle(
            world,
            this.spriteProvider,
            d,
            e,
            f,
            0.0,
            -0.8,
            0.0
        ) {
            override fun getGroup(): Optional<ParticleGroup> {
                return Optional.of(ParticleGroup.SPORE_BLOSSOM_AIR)
            }
        }
        waterSuspendParticle.maxAge = MathHelper.nextBetween(world.random, 500, 1000)
        waterSuspendParticle.gravityStrength = 0.01f
        waterSuspendParticle.setColor(0.32f, 0.5f, 0.22f)
        return waterSuspendParticle
    }
}