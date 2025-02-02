package org.teamvoided.dusk_debris.init

import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes
import net.minecraft.particle.DefaultParticleType
import net.minecraft.particle.ParticleType
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import org.teamvoided.dusk_debris.DuskDebris.id
import org.teamvoided.dusk_debris.particle.*


object DuskParticles {
    val TOXIC_SMOKE_PARTICLE: ParticleType<NethershroomSporeParticleEffect> =
        FabricParticleTypes.complex(NethershroomSporeParticleEffect.CODEC, NethershroomSporeParticleEffect.PACKET_CODEC)
    val GUNPOWDER_EXPLOSION_SMOKE: ParticleType<GunpowderExplosionSmokeParticleEffect> = FabricParticleTypes.complex(
        GunpowderExplosionSmokeParticleEffect.CODEC,
        GunpowderExplosionSmokeParticleEffect.PACKET_CODEC
    )
    val GUNPOWDER_EXPLOSION_EMMITER: ParticleType<GunpowderExplosionEmitterParticleEffect> =
        FabricParticleTypes.complex(
            GunpowderExplosionEmitterParticleEffect.CODEC,
            GunpowderExplosionEmitterParticleEffect.PACKET_CODEC
        )
    val BLUNDERBOMB: DefaultParticleType = FabricParticleTypes.simple()
    val FIREBOMB: DefaultParticleType = FabricParticleTypes.simple()
    val BONECALLER: ParticleType<BonecallerParticleEffect> =
        FabricParticleTypes.complex(BonecallerParticleEffect.CODEC, BonecallerParticleEffect.PACKET_CODEC)
    val GEYSER: DefaultParticleType = FabricParticleTypes.simple()
    val GODHOME: ParticleType<GodhomeParticleEffect> =
        FabricParticleTypes.complex(GodhomeParticleEffect.CODEC, GodhomeParticleEffect.PACKET_CODEC)
    val DRAINED_SOUL: DefaultParticleType = FabricParticleTypes.simple()
    val COSMOS: DefaultParticleType = FabricParticleTypes.simple()
    val ENTITY_TEST: ParticleType<EntityTestParticleEffect> =
        FabricParticleTypes.complex(EntityTestParticleEffect.CODEC, EntityTestParticleEffect.PACKET_CODEC)


    val WIND: ParticleType<WindParticleEffect> =
        FabricParticleTypes.complex(WindParticleEffect.CODEC, WindParticleEffect.PACKET_CODEC)

    val SPARK: DefaultParticleType = FabricParticleTypes.simple()

    val UNDERACID: DefaultParticleType = FabricParticleTypes.simple()
    val ACID_BUBBLE_POP: DefaultParticleType = FabricParticleTypes.simple()

    val STATIONARY_EMITTER: ParticleType<StationaryEmitterParticleEffect> =
        FabricParticleTypes.complex(StationaryEmitterParticleEffect.CODEC, StationaryEmitterParticleEffect.PACKET_CODEC)

    val FLASH: ParticleType<FlashParticleEffect> =
        FabricParticleTypes.complex(FlashParticleEffect.CODEC, FlashParticleEffect.PACKET_CODEC)

    val SMALL_PURPLE_BUBBLE_CUBE: DefaultParticleType = FabricParticleTypes.simple()
    val PURPLE_BIOME_BUBBLE: DefaultParticleType = FabricParticleTypes.simple()
    val PURPLE_BUBBLE: DefaultParticleType = FabricParticleTypes.simple()


    fun init() {
        Registry.register(Registries.PARTICLE_TYPE, id("toxic_smoke_particle"), TOXIC_SMOKE_PARTICLE)
        Registry.register(Registries.PARTICLE_TYPE, id("gunpowder_explosion_smoke"), GUNPOWDER_EXPLOSION_SMOKE)
        Registry.register(Registries.PARTICLE_TYPE, id("gunpowder_explosion_emitter"), GUNPOWDER_EXPLOSION_EMMITER)
        Registry.register(Registries.PARTICLE_TYPE, id("blunderbomb"), BLUNDERBOMB)
        Registry.register(Registries.PARTICLE_TYPE, id("firebomb"), FIREBOMB)
        Registry.register(Registries.PARTICLE_TYPE, id("bonecaller"), BONECALLER)
        Registry.register(Registries.PARTICLE_TYPE, id("geyser"), GEYSER)
        Registry.register(Registries.PARTICLE_TYPE, id("godhome"), GODHOME)
        Registry.register(Registries.PARTICLE_TYPE, id("drained_soul"), DRAINED_SOUL)
        Registry.register(Registries.PARTICLE_TYPE, id("cosmos"), COSMOS)
        Registry.register(Registries.PARTICLE_TYPE, id("entity_test"), ENTITY_TEST)

        Registry.register(Registries.PARTICLE_TYPE, id("wind"), WIND)

        Registry.register(Registries.PARTICLE_TYPE, id("spark"), SPARK)
        Registry.register(Registries.PARTICLE_TYPE, id("underacid"), UNDERACID)
        Registry.register(Registries.PARTICLE_TYPE, id("acid_bubble_pop"), ACID_BUBBLE_POP)

        Registry.register(Registries.PARTICLE_TYPE, id("small_purple_bubble_cube"), SMALL_PURPLE_BUBBLE_CUBE)
        Registry.register(Registries.PARTICLE_TYPE, id("purple_biome_bubble"), PURPLE_BIOME_BUBBLE)
        Registry.register(Registries.PARTICLE_TYPE, id("purple_bubble"), PURPLE_BUBBLE)
    }
}