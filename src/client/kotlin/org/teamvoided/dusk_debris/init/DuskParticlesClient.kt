package org.teamvoided.dusk_debris.init

import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry
import net.minecraft.client.particle.BubblePopParticle
import org.teamvoided.dusk_debris.particle.*
import org.teamvoided.dusk_debris.particle.emmiter.BetweenPointsParticle
import org.teamvoided.dusk_debris.particle.emmiter.BlunderbombParticle
import org.teamvoided.dusk_debris.particle.emmiter.FirebombParticle
import org.teamvoided.dusk_debris.particle.emmiter.GunpowderExplosionEmitterParticle
import org.teamvoided.dusk_debris.particle.stupid_particles.CubeParticle
import org.teamvoided.dusk_debris.particle.stupid_particles.EinsteinOrbitParticle
import org.teamvoided.dusk_debris.particle.vanilla.AdditionalWaterSuspendParticle

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
        ParticleFactoryRegistry.getInstance().register(DuskParticles.SPELL, SpellParticle::Factory)
        ParticleFactoryRegistry.getInstance().register(DuskParticles.CUBE, CubeParticle::Factory)

        ParticleFactoryRegistry.getInstance().register(DuskParticles.COSMOS, CosmosParticle::Factory)
        ParticleFactoryRegistry.getInstance().register(DuskParticles.ENTITY_TEST, EntityTestParticle::Factory)
        ParticleFactoryRegistry.getInstance().register(DuskParticles.EINSTEIN_ORBIT, EinsteinOrbitParticle::Factory)

        ParticleFactoryRegistry.getInstance().register(DuskParticles.WIND, WindParticle::Factory)

        ParticleFactoryRegistry.getInstance().register(DuskParticles.SPARK, ElectricityParticle::Factory)

        ParticleFactoryRegistry.getInstance()
            .register(DuskParticles.UNDERACID, AdditionalWaterSuspendParticle::UnderacidFactory)
        ParticleFactoryRegistry.getInstance().register(DuskParticles.ACID_BUBBLE_POP, BubblePopParticle::Factory)

        ParticleFactoryRegistry.getInstance().register(DuskParticles.FLASH, FlashParticle::Factory)

        ParticleFactoryRegistry.getInstance()
            .register(DuskParticles.SMALL_PURPLE_BUBBLE_CUBE, BubbleCubeParticle::SmallFactory)
        ParticleFactoryRegistry.getInstance().register(DuskParticles.PURPLE_BIOME_BUBBLE, BiomeBubbleParticle::Factory)
        ParticleFactoryRegistry.getInstance().register(DuskParticles.PURPLE_BUBBLE, DuskBubbleParticle::Factory)

        ParticleFactoryRegistry.getInstance()
            .register(DuskParticles.ASTRAS_FLYING_GOOP, AstrasStrangeGoopParticle::FallingGoopFactory)
        ParticleFactoryRegistry.getInstance()
            .register(DuskParticles.ASTRAS_LANDED_GOOP, AstrasStrangeGoopParticle::LandedGoopFactory)

        ParticleFactoryRegistry.getInstance()
            .register(DuskParticles.SHRIEK_DIRECTIONAL, ShriekDirectionalParticle::Factory)

        ParticleFactoryRegistry.getInstance().register(DuskParticles.BETWEEN_POINTS, BetweenPointsParticle.Factory())

        // DnD
        ParticleFactoryRegistry.getInstance().register(DuskParticles.SPIDERLILY, SpiderlilyPetalParticle::Factory)
        ParticleFactoryRegistry.getInstance().register(DuskParticles.MUSHROOM_LAUNCH, MushroomLaunchParticle::Factory)
        ParticleFactoryRegistry.getInstance().register(DuskParticles.DUST_BUNNY, DustBunnyParticle::Factory)
        ParticleFactoryRegistry.getInstance().register(DuskParticles.SPIRAL, SpiralParticle::Factory)
    }
}
