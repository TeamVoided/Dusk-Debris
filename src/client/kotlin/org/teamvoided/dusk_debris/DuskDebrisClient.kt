package org.teamvoided.dusk_debris

import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry
import net.minecraft.client.render.RenderLayer
import net.minecraft.client.render.entity.EmptyEntityRenderer
import net.minecraft.client.render.entity.FlyingItemEntityRenderer
import net.minecraft.component.type.DyedColorComponent
import net.minecraft.item.ItemStack
import org.teamvoided.dusk_debris.DuskDebris.log
import org.teamvoided.dusk_debris.block.DuskBlockLists
import org.teamvoided.dusk_debris.block.GunpowderBlock
import org.teamvoided.dusk_debris.entity.DuskEntityLists.THROWABLE_BOMB_ENTITIES
import org.teamvoided.dusk_debris.entity.gunpowder_barrel.GunpowderBarrelEntityRenderer
import org.teamvoided.dusk_debris.entity.skeleton.GloomEntityRenderer
import org.teamvoided.dusk_debris.init.DuskBlocks
import org.teamvoided.dusk_debris.init.DuskEntities
import org.teamvoided.dusk_debris.init.DuskItems
import org.teamvoided.dusk_debris.init.DuskParticles
import org.teamvoided.dusk_debris.particle.*
import org.teamvoided.dusk_debris.render.entity.model.DuskEntityModelLayers

@Suppress("unused")
object DuskDebrisClient {

    val cutoutBlock = listOf(
        DuskBlocks.GUNPOWDER,
        DuskBlocks.BLUE_NETHERSHROOM,
        DuskBlocks.PURPLE_NETHERSHROOM,
        DuskBlocks.CYPRESS_DOOR,
        DuskBlocks.CYPRESS_TRAPDOOR,
        DuskBlocks.CHARRED_DOOR,
        DuskBlocks.CHARRED_TRAPDOOR,
    ) + DuskBlockLists.THROWABLE_BOMB_BLOCK_LIST +
            DuskBlockLists.RIBBON_BLOCKS_LIST
//    val translucentBlock = listOf()

    fun init() {
        log.info("Hello from Client")
        DuskEntityModelLayers.init()


        ParticleFactoryRegistry.getInstance()
            .register(DuskParticles.TOXIC_SMOKE_PARTICLE, ToxicSmokeParticle::Factory)
        ParticleFactoryRegistry.getInstance()
            .register(DuskParticles.GUNPOWDER_EXPLOSION_SMOKE, GunpowderExplosionSmokeParticle::Factory)
        ParticleFactoryRegistry.getInstance()
            .register(DuskParticles.GUNPOWDER_EXPLOSION_EMMITER, GunpowderExplosionEmitterParticle.Factory())
        ParticleFactoryRegistry.getInstance()
            .register(DuskParticles.BLUNDERBOMB, BlunderbombParticle.Factory())
        ParticleFactoryRegistry.getInstance()
            .register(DuskParticles.FIREBOMB, FirebombParticle.Factory())
        ParticleFactoryRegistry.getInstance()
            .register(DuskParticles.BONECALLER, BonecallerParticle::Factory)

        ColorProviderRegistry.ITEM.register(
            { itemStack: ItemStack, i: Int ->
                DyedColorComponent.getColorOrDefault(itemStack, 0x7F7F7F)
            }, DuskItems.BONECALLER_BANDANA
        )
        ColorProviderRegistry.BLOCK.register(
            { state, _, _, _ -> if (state.get(GunpowderBlock.LIT)) 0xFF9F32 else 0x383838 },
            DuskBlocks.GUNPOWDER
        )

        EntityRendererRegistry.register(DuskEntities.BOX_AREA_EFFECT_CLOUD, ::EmptyEntityRenderer)
        EntityRendererRegistry.register(DuskEntities.GUNPOWDER_BARREL, ::GunpowderBarrelEntityRenderer)
        EntityRendererRegistry.register(DuskEntities.GLOOM, ::GloomEntityRenderer)

        THROWABLE_BOMB_ENTITIES.forEach {
            EntityRendererRegistry.register(it, ::FlyingItemEntityRenderer)
        }

        cutoutBlock.forEach {
            BlockRenderLayerMap.INSTANCE.putBlock(it, RenderLayer.getCutout())
        }
//        translucentBlock.forEach {
//            BlockRenderLayerMap.INSTANCE.putBlock(it, RenderLayer.getTranslucent())
//        }
    }
}
