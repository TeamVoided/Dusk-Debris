package org.teamvoided.dusk_debris

import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry
import net.minecraft.client.particle.ParticleFactory
import net.minecraft.client.render.RenderLayer
import net.minecraft.client.render.entity.FlyingItemEntityRenderer
import org.teamvoided.dusk_autumn.init.DuskEntities
import org.teamvoided.dusk_debris.DuskDebris.log
import org.teamvoided.dusk_debris.entity.gunpowder_barrel.GunpowderBarrelEntityRenderer
import org.teamvoided.dusk_debris.init.DuskBlocks
import org.teamvoided.dusk_debris.init.DuskParticles
import org.teamvoided.dusk_debris.particle.BlunderbombEmitterParticle
import org.teamvoided.dusk_debris.particle.GunpowderExplosionEmitterParticle
import org.teamvoided.dusk_debris.particle.GunpowderExplosionSmokeParticle

@Suppress("unused")
object DuskDebrisClient {

    val cutoutBlock = listOf(
        DuskBlocks.BLUNDERBOMB_BLOCK,
        DuskBlocks.FIREBOMB_BLOCK,
        DuskBlocks.CYPRESS_DOOR,
        DuskBlocks.CYPRESS_TRAPDOOR,
        DuskBlocks.CHARRED_DOOR,
        DuskBlocks.CHARRED_TRAPDOOR,
    )
//    val translucentBlock = listOf()

    fun init() {
        log.info("Hello from Client")

        ParticleFactoryRegistry.getInstance()
            .register(DuskParticles.GUNPOWDER_EXPLOSION_SMOKE, ParticleFactoryRegistry.PendingParticleFactory {
                ParticleFactory { _, world, x, y, z, _, _, _ -> GunpowderExplosionSmokeParticle(world, x, y, z, 0.0, 0.0, 0.0, it) }
            })
        ParticleFactoryRegistry.getInstance()
            .register(DuskParticles.GUNPOWDER_EXPLOSION_EMMITER, ParticleFactoryRegistry.PendingParticleFactory {
                ParticleFactory { _, world, x, y, z, _, _, _ -> GunpowderExplosionEmitterParticle(world, x, y, z, 8f) }
            })
        ParticleFactoryRegistry.getInstance()
            .register(DuskParticles.BLUNDERBOMB_EMMITER, ParticleFactoryRegistry.PendingParticleFactory {
                ParticleFactory { _, world, x, y, z, _, _, _ -> BlunderbombEmitterParticle(world, x, y, z) }
            })

        EntityRendererRegistry.register(DuskEntities.GUNPOWDER_BARREL, ::GunpowderBarrelEntityRenderer)
        EntityRendererRegistry.register(DuskEntities.BLUNDERBOMB, ::FlyingItemEntityRenderer)
        EntityRendererRegistry.register(DuskEntities.FIREBOMB, ::FlyingItemEntityRenderer)

        cutoutBlock.forEach {
            BlockRenderLayerMap.INSTANCE.putBlock(it, RenderLayer.getCutout())
        }
//        translucentBlock.forEach {
//            BlockRenderLayerMap.INSTANCE.putBlock(it, RenderLayer.getTranslucent())
//        }
    }
}
