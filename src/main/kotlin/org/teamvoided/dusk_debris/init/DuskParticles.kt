package org.teamvoided.dusk_debris.init

import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes
import net.minecraft.particle.DefaultParticleType
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import org.teamvoided.dusk_debris.DuskDebris.id
import org.teamvoided.dusk_debris.particle.FloatInputParticleEffect


object DuskParticles {
    val GUNPOWDER_EXPLOSION_SMOKE: DefaultParticleType = FabricParticleTypes.simple()
    val GUNPOWDER_EXPLOSION_EMMITER = FabricParticleTypes.complex(FloatInputParticleEffect.CODEC, FloatInputParticleEffect.PACKET_CODEC)
    val BLUNDERBOMB_EMMITER: DefaultParticleType = FabricParticleTypes.simple()

    fun init() {
        Registry.register(Registries.PARTICLE_TYPE, id("gunpowder_explosion_smoke"), GUNPOWDER_EXPLOSION_SMOKE)
        Registry.register(Registries.PARTICLE_TYPE, id("gunpowder_explosion_emitter"), GUNPOWDER_EXPLOSION_EMMITER)
        Registry.register(Registries.PARTICLE_TYPE, id("blunderbomb_emitter"), BLUNDERBOMB_EMMITER)
    }
}