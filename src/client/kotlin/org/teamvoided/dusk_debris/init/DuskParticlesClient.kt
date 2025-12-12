package org.teamvoided.dusk_debris.init

import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry
import net.minecraft.client.particle.BubblePopParticle
import org.teamvoided.dusk_debris.particle.*
import org.teamvoided.dusk_debris.particle.emmiter.BetweenPointsParticle
import org.teamvoided.dusk_debris.particle.emmiter.BlunderbombParticle
import org.teamvoided.dusk_debris.particle.emmiter.FirebombParticle
import org.teamvoided.dusk_debris.particle.emmiter.GunpowderExplosionEmitterParticle
import org.teamvoided.dusk_debris.particle.cube_particles.CubeParticle
import org.teamvoided.dusk_debris.particle.cube_particles.EinsteinOrbitParticle
import org.teamvoided.dusk_debris.particle.vanilla.AdditionalWaterSuspendParticle

object DuskParticlesClient {
    //ParticleManager
    fun init() {
        val ins = ParticleFactoryRegistry.getInstance()
        ins.register(DuskParticles.TOXIC_SMOKE_PARTICLE, ToxicSmokeParticle::Factory)
        ins.register(DuskParticles.GUNPOWDER_EXPLOSION_SMOKE, GunpowderExplosionSmokeParticle::Factory)
        ins.register(DuskParticles.GUNPOWDER_EXPLOSION_EMMITER, GunpowderExplosionEmitterParticle.Factory())
        ins.register(DuskParticles.BLUNDERBOMB, BlunderbombParticle.Factory())
        ins.register(DuskParticles.FIREBOMB, FirebombParticle.Factory())
        ins.register(DuskParticles.BONECALLER, BonecallerParticle::Factory)
        ins.register(DuskParticles.GEYSER, GeyserParticle::Factory)
        ins.register(DuskParticles.GODHOME, GodhomeParticle::Factory)
        ins.register(DuskParticles.DRAINED_SOUL, DrainedSoulParticle::SmallFactory)
        ins.register(DuskParticles.SPELL, SpellParticle::Factory)
        ins.register(DuskParticles.CUBE, CubeParticle::Factory)

        ins.register(DuskParticles.COSMOS, CosmosParticle::Factory)
        ins.register(DuskParticles.ENTITY_TEST, EntityTestParticle::Factory)
        ins.register(DuskParticles.EINSTEIN_ORBIT, EinsteinOrbitParticle::Factory)

        ins.register(DuskParticles.WIND, WindParticle::Factory)
        ins.register(DuskParticles.EXHAUST_WARMUP, ExhaustBlastParticle::WarmUpFactory)
        ins.register(DuskParticles.EXHAUST_BLAST, ExhaustBlastParticle::Factory)
        ins.register(DuskParticles.SNAIL, SnailParticle::Factory)


        ins.register(DuskParticles.SPARK, ElectricityParticle::Factory)

        ins.register(DuskParticles.UNDERACID, AdditionalWaterSuspendParticle::UnderacidFactory)
        ins.register(DuskParticles.ACID_BUBBLE_POP, BubblePopParticle::Factory)

        ins.register(DuskParticles.FLASH, FlashParticle::Factory)
        ins.register(DuskParticles.RISING_EMBER, RisingEmberParticle::Factory)

        ins.register(DuskParticles.SMALL_PURPLE_BUBBLE_CUBE, BubbleCubeParticle::SmallFactory)
        ins.register(DuskParticles.PURPLE_BIOME_BUBBLE, BiomeBubbleParticle::Factory)
        ins.register(DuskParticles.PURPLE_BUBBLE, DuskBubbleParticle::Factory)

        ins.register(DuskParticles.ASTRAS_FLYING_GOOP, AstrasStrangeGoopParticle::FallingGoopFactory)
        ins.register(DuskParticles.ASTRAS_LANDED_GOOP, AstrasStrangeGoopParticle::LandedGoopFactory)

        ins.register(DuskParticles.SHRIEK_DIRECTIONAL, ShriekDirectionalParticle::Factory)

        ins.register(DuskParticles.BETWEEN_POINTS, BetweenPointsParticle.Factory())

        // DnD
        ins.register(DuskParticles.SPIDERLILY, SpiderlilyPetalParticle::Factory)
        ins.register(DuskParticles.MUSHROOM_LAUNCH, MushroomLaunchParticle::Factory)
        ins.register(DuskParticles.DUST_BUNNY, DustBunnyParticle::Factory)
        ins.register(DuskParticles.SPIRAL, SpiralParticle::Factory)
    }
}
