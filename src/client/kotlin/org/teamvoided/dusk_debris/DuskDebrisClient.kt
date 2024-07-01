package org.teamvoided.dusk_debris

import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry
import net.minecraft.client.particle.ParticleManager.SpriteAwareFactory
import net.minecraft.client.particle.SpriteProvider
import net.minecraft.client.particle.WaterSuspendParticle
import net.minecraft.client.render.RenderLayer
import net.minecraft.client.render.entity.EmptyEntityRenderer
import net.minecraft.client.render.entity.FlyingItemEntityRenderer
import org.teamvoided.dusk_debris.DuskDebris.log
import org.teamvoided.dusk_debris.block.DuskBlockLists
import org.teamvoided.dusk_debris.entity.gunpowder_barrel.GunpowderBarrelEntityRenderer
import org.teamvoided.dusk_debris.init.DuskBlocks
import org.teamvoided.dusk_debris.init.DuskEntities
import org.teamvoided.dusk_debris.init.DuskParticles
import org.teamvoided.dusk_debris.particle.BlunderbombEmitterParticle
import org.teamvoided.dusk_debris.particle.GunpowderExplosionEmitterParticle
import org.teamvoided.dusk_debris.particle.GunpowderExplosionSmokeParticle

@Suppress("unused")
object DuskDebrisClient {

    val cutoutBlock = listOf(
        DuskBlocks.BLUE_NETHERSHROOM,
        DuskBlocks.PURPLE_NETHERSHROOM,
        DuskBlocks.CYPRESS_DOOR,
        DuskBlocks.CYPRESS_TRAPDOOR,
        DuskBlocks.CHARRED_DOOR,
        DuskBlocks.CHARRED_TRAPDOOR,
    ) + DuskBlockLists.THROWABLE_BLOCK_LIST +
            DuskBlockLists.RIBBON_BLOCKS_LIST
//    val translucentBlock = listOf()

    fun init() {
        log.info("Hello from Client")


//        ParticleFactoryRegistry.getInstance()
//            .register(DuskParticles.BLUE_NETHERSHROOM_SPORE, WaterSuspendParticle::SporeBlossomAirFactory)
//        ParticleFactoryRegistry.getInstance()
//            .register(DuskParticles.PURPLE_NETHERSHROOM_SPORE, WaterSuspendParticle::NethershroomSporeAir)
        ParticleFactoryRegistry.getInstance()
            .register(DuskParticles.GUNPOWDER_EXPLOSION_SMOKE, GunpowderExplosionSmokeParticle::Factory)
        ParticleFactoryRegistry.getInstance()
            .register(DuskParticles.GUNPOWDER_EXPLOSION_EMMITER, GunpowderExplosionEmitterParticle.Factory())
        ParticleFactoryRegistry.getInstance()
            .register(DuskParticles.BLUNDERBOMB_EMMITER, BlunderbombEmitterParticle::Factory)
        ParticleFactoryRegistry.getInstance()
            .register(DuskParticles.BLUNDERBOMB_EMMITER, BlunderbombEmitterParticle::Factory)

        EntityRendererRegistry.register(DuskEntities.BOX_AREA_EFFECT_CLOUD, ::EmptyEntityRenderer)
        EntityRendererRegistry.register(DuskEntities.GUNPOWDER_BARREL, ::GunpowderBarrelEntityRenderer)
        EntityRendererRegistry.register(DuskEntities.BLUNDERBOMB, ::FlyingItemEntityRenderer)
        EntityRendererRegistry.register(DuskEntities.FIREBOMB, ::FlyingItemEntityRenderer)
        EntityRendererRegistry.register(DuskEntities.BONECALLER, ::FlyingItemEntityRenderer)

        cutoutBlock.forEach {
            BlockRenderLayerMap.INSTANCE.putBlock(it, RenderLayer.getCutout())
        }
//        translucentBlock.forEach {
//            BlockRenderLayerMap.INSTANCE.putBlock(it, RenderLayer.getTranslucent())
//        }
    }
}
