package org.teamvoided.dusk_debris.init

import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes
import net.minecraft.particle.DefaultParticleType
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import org.teamvoided.dusk_debris.DuskDebris.id
import org.teamvoided.dusk_debris.particle.*


object DuskParticles {
    val TOXIC_SMOKE_PARTICLE = FabricParticleTypes.complex(NethershroomSporeParticleEffect.CODEC, NethershroomSporeParticleEffect.PACKET_CODEC)
    val GUNPOWDER_EXPLOSION_SMOKE = FabricParticleTypes.complex(GunpowderExplosionSmokeParticleEffect.CODEC, GunpowderExplosionSmokeParticleEffect.PACKET_CODEC)
    val GUNPOWDER_EXPLOSION_EMMITER = FabricParticleTypes.complex(GunpowderExplosionEmitterParticleEffect.CODEC, GunpowderExplosionEmitterParticleEffect.PACKET_CODEC)
    val BLUNDERBOMB: DefaultParticleType = FabricParticleTypes.simple()
    val FIREBOMB: DefaultParticleType = FabricParticleTypes.simple()
    val BONECALLER = FabricParticleTypes.complex(BonecallerParticleEffect.CODEC, BonecallerParticleEffect.PACKET_CODEC)
    val GEYSER: DefaultParticleType = FabricParticleTypes.simple()
    val GODHOME = FabricParticleTypes.complex(GodhomeParticleEffect.CODEC, GodhomeParticleEffect.PACKET_CODEC)
    val DRAINED_SOUL: DefaultParticleType = FabricParticleTypes.simple()
    val COSMOS: DefaultParticleType = FabricParticleTypes.simple()


    val WIND = FabricParticleTypes.complex(WindParticleEffect.CODEC, WindParticleEffect.PACKET_CODEC)

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

        Registry.register(Registries.PARTICLE_TYPE, id("wind"), WIND)
    }
}