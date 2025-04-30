package org.teamvoided.dusk_debris.init

import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes.complex
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes.simple
import net.minecraft.particle.DefaultParticleType
import net.minecraft.particle.ParticleType
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import org.teamvoided.dusk_debris.DuskDebris.id
import org.teamvoided.dusk_debris.particle.*


object DuskParticles {
    val TOXIC_SMOKE_PARTICLE: ParticleType<NethershroomSporeParticleEffect> =
        complex(NethershroomSporeParticleEffect.CODEC, NethershroomSporeParticleEffect.PACKET_CODEC)
    val GUNPOWDER_EXPLOSION_SMOKE: ParticleType<GunpowderExplosionSmokeParticleEffect> = complex(
        GunpowderExplosionSmokeParticleEffect.CODEC,
        GunpowderExplosionSmokeParticleEffect.PACKET_CODEC
    )
    val GUNPOWDER_EXPLOSION_EMMITER: ParticleType<GunpowderExplosionEmitterParticleEffect> = complex(
        GunpowderExplosionEmitterParticleEffect.CODEC,
        GunpowderExplosionEmitterParticleEffect.PACKET_CODEC
    )
    val BLUNDERBOMB: DefaultParticleType = simple()
    val FIREBOMB: DefaultParticleType = simple()
    val BONECALLER: ParticleType<BonecallerParticleEffect> =
        complex(BonecallerParticleEffect.CODEC, BonecallerParticleEffect.PACKET_CODEC)
    val GEYSER: DefaultParticleType = simple()
    val GODHOME: ParticleType<GodhomeParticleEffect> =
        complex(GodhomeParticleEffect.CODEC, GodhomeParticleEffect.PACKET_CODEC)
    val DRAINED_SOUL: DefaultParticleType = simple()
    val CUBE: DefaultParticleType = simple()


    val COSMOS: DefaultParticleType = simple()
    val ENTITY_TEST: ParticleType<EntityTestParticleEffect> =
        complex(EntityTestParticleEffect.CODEC, EntityTestParticleEffect.PACKET_CODEC)


    val WIND: ParticleType<WindParticleEffect> =
        complex(WindParticleEffect.CODEC, WindParticleEffect.PACKET_CODEC)

    val SPARK: DefaultParticleType = simple()

    val UNDERACID: DefaultParticleType = simple()
    val ACID_BUBBLE_POP: DefaultParticleType = simple()

    val STATIONARY_EMITTER: ParticleType<StationaryEmitterParticleEffect> =
        complex(StationaryEmitterParticleEffect.CODEC, StationaryEmitterParticleEffect.PACKET_CODEC)

    val FLASH: ParticleType<FlashParticleEffect> =
        complex(FlashParticleEffect.CODEC, FlashParticleEffect.PACKET_CODEC)

    val SMALL_PURPLE_BUBBLE_CUBE: DefaultParticleType = simple()
    val PURPLE_BIOME_BUBBLE: DefaultParticleType = simple()
    val PURPLE_BUBBLE: DefaultParticleType = simple()

    val ASTRAS_FLYING_GOOP: ParticleType<GoopFlyingParticleEffect> =
        complex(GoopFlyingParticleEffect.CODEC, GoopFlyingParticleEffect.PACKET_CODEC)
    val ASTRAS_LANDED_GOOP: ParticleType<GoopLandedParticleEffect> =
        complex(GoopLandedParticleEffect.CODEC, GoopLandedParticleEffect.PACKET_CODEC)

    val SHRIEK_DIRECTIONAL: ParticleType<ShriekDirectionalParticleEffect> =
        complex(ShriekDirectionalParticleEffect.CODEC, ShriekDirectionalParticleEffect.PACKET_CODEC)

    val BETWEEN_POINTS: ParticleType<BetweenPointsParticleEffect> =
        complex(BetweenPointsParticleEffect.CODEC, BetweenPointsParticleEffect.PACKET_CODEC)

    // DnD
    val SPIDERLILY: DefaultParticleType = simple()
    val MUSHROOM_LAUNCH: DefaultParticleType = simple()
    val DUST_BUNNY: ParticleType<DustBunnyParticleEffect> =
        complex(DustBunnyParticleEffect.CODEC, DustBunnyParticleEffect.PACKET_CODEC)
    val SPIRAL: ParticleType<SpiralParticleEffect> =
        complex(SpiralParticleEffect.CODEC, SpiralParticleEffect.PACKET_CODEC)

    fun init() {
        register("toxic_smoke_particle", TOXIC_SMOKE_PARTICLE)
        register("gunpowder_explosion_smoke", GUNPOWDER_EXPLOSION_SMOKE)
        register("gunpowder_explosion_emitter", GUNPOWDER_EXPLOSION_EMMITER)
        register("blunderbomb", BLUNDERBOMB)
        register("firebomb", FIREBOMB)
        register("bonecaller", BONECALLER)
        register("geyser", GEYSER)
        register("godhome", GODHOME)
        register("drained_soul", DRAINED_SOUL)
        register("cube", CUBE)

        register("cosmos", COSMOS)
        register("entity_test", ENTITY_TEST)

        register("wind", WIND)

        register("spark", SPARK)
        register("underacid", UNDERACID)
        register("acid_bubble_pop", ACID_BUBBLE_POP)

        register("small_purple_bubble_cube", SMALL_PURPLE_BUBBLE_CUBE)
        register("purple_biome_bubble", PURPLE_BIOME_BUBBLE)
        register("purple_bubble", PURPLE_BUBBLE)

        register("goop_flying", ASTRAS_FLYING_GOOP)
        register("goop_landed", ASTRAS_LANDED_GOOP)

        register("shriek_directional", SHRIEK_DIRECTIONAL)

        register("between_points", BETWEEN_POINTS)

        // DnD
        register("spiderlily", SPIDERLILY)
        register("mushroom_launch", MUSHROOM_LAUNCH)
        register("dust_bunny", DUST_BUNNY)
        register("spiral", SPIRAL)
    }

    fun register(id: String, particleType: ParticleType<*>) =
        Registry.register(Registries.PARTICLE_TYPE, id(id), particleType)
}