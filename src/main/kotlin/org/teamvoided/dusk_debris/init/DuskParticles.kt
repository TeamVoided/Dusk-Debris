package org.teamvoided.dusk_debris.init

import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes
import net.minecraft.client.particle.ParticleFactory
import net.minecraft.particle.DefaultParticleType
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import org.teamvoided.dusk_debris.DuskDebris.id
import org.teamvoided.dusk_debris.particle.GunpowderExplosionEmitterParticle


object DuskParticles {
    val GUNPOWDER_EXPLOSION_EMMITER: DefaultParticleType = FabricParticleTypes.simple()

    fun init() {
        Registry.register(Registries.PARTICLE_TYPE, id("gunpowder_explosion_emmiter"), GUNPOWDER_EXPLOSION_EMMITER)
    }

    fun initClient() {
        ParticleFactoryRegistry.getInstance()
            .register(GUNPOWDER_EXPLOSION_EMMITER, ParticleFactoryRegistry.PendingParticleFactory {
                ParticleFactory { _, world, x, y, z, _, _, _ -> GunpowderExplosionEmitterParticle(world, x, y, z) }
            })
    }
}