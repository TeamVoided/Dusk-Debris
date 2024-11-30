package org.teamvoided.dusk_debris.init

import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry
import org.teamvoided.dusk_debris.particle.*

object DuskParticlesClient {
    //ParticleManager
    fun init() {
        ParticleFactoryRegistry.getInstance().register(DuskParticles.TOXIC_SMOKE_PARTICLE, ToxicSmokeParticle::Factory)
        ParticleFactoryRegistry.getInstance()
            .register(DuskParticles.GUNPOWDER_EXPLOSION_SMOKE, GunpowderExplosionSmokeParticle::Factory)
        ParticleFactoryRegistry.getInstance()
            .register(DuskParticles.GUNPOWDER_EXPLOSION_EMMITER, GunpowderExplosionEmitterParticle.Factory())
        ParticleFactoryRegistry.getInstance().register(DuskParticles.BLUNDERBOMB, BlunderbombParticle.Factory())
        ParticleFactoryRegistry.getInstance().register(DuskParticles.FIREBOMB, FirebombParticle.Factory())
        ParticleFactoryRegistry.getInstance().register(DuskParticles.BONECALLER, BonecallerParticle::Factory)
        ParticleFactoryRegistry.getInstance().register(DuskParticles.GEYSER, GeyserParticle::Factory)
        ParticleFactoryRegistry.getInstance().register(DuskParticles.GODHOME, GodhomeParticle::Factory)
        ParticleFactoryRegistry.getInstance().register(DuskParticles.DRAINED_SOUL, DrainedSoulParticle::SmallFactory)
        ParticleFactoryRegistry.getInstance().register(DuskParticles.COSMOS, CosmosParticle::Factory)
        ParticleFactoryRegistry.getInstance().register(DuskParticles.ENTITY_TEST, EntityTestParticle::Factory)

        ParticleFactoryRegistry.getInstance().register(DuskParticles.WIND, WindParticle::Factory)
    }
}