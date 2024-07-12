package org.teamvoided.dusk_debris

import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry
import net.minecraft.client.color.world.BiomeColors
import net.minecraft.client.color.world.FoliageColors
import net.minecraft.client.render.RenderLayer
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories
import org.teamvoided.dusk_debris.entity.block.ChestBlockEntityRenderer
import net.minecraft.client.render.entity.EmptyEntityRenderer
import net.minecraft.client.render.entity.FlyingItemEntityRenderer
import net.minecraft.component.type.DyedColorComponent
import net.minecraft.item.ItemStack
import org.teamvoided.dusk_debris.DuskDebris.log
import org.teamvoided.dusk_debris.block.DuskBlockLists.CUTOUT_BLOCKS
import org.teamvoided.dusk_debris.block.GunpowderBlock
import org.teamvoided.dusk_debris.entity.DuskEntityLists.THROWABLE_BOMB_ENTITIES
import org.teamvoided.dusk_debris.entity.DuskEntityModelLayers
import org.teamvoided.dusk_debris.entity.gunpowder_barrel.GunpowderBarrelEntityRenderer
import org.teamvoided.dusk_debris.entity.skeleton.GloomEntityRenderer
import org.teamvoided.dusk_debris.init.*
import org.teamvoided.dusk_debris.particle.*

@Suppress("unused")
object DuskDebrisClient {
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
        ParticleFactoryRegistry.getInstance()
            .register(DuskParticles.GEYSER, GeyserParticle::Factory)

        ColorProviderRegistry.ITEM.register(
            { itemStack: ItemStack, i: Int ->
                DyedColorComponent.getColorOrDefault(itemStack, 0x7F7F7F)
            }, DuskItems.BONECALLER_BANDANA
        )
        ColorProviderRegistry.ITEM.register(
            { _, _ -> FoliageColors.getDefaultColor() },
            DuskItems.CYPRESS_LEAVES
        )
        ColorProviderRegistry.BLOCK.register(
            { _, world, pos, _ ->
                if (world != null && pos != null) BiomeColors.getFoliageColor(world, pos)
                else FoliageColors.getColor(0.8, 0.4)
            },
            DuskBlocks.CYPRESS_LEAVES
        )
        ColorProviderRegistry.BLOCK.register(
            { state, _, _, _ -> if (state.get(GunpowderBlock.LIT)) 0xFF9F32 else 0x383838 },
            DuskBlocks.GUNPOWDER
        )

        EntityRendererRegistry.register(DuskEntities.BOX_AREA_EFFECT_CLOUD, ::EmptyEntityRenderer)
        EntityRendererRegistry.register(DuskEntities.GUNPOWDER_BARREL, ::GunpowderBarrelEntityRenderer)
        EntityRendererRegistry.register(DuskEntities.GLOOM, ::GloomEntityRenderer)
//        BuiltinItemRendererRegistry.INSTANCE.register(DuskItems.STRAY_SKULL)

        BlockEntityRendererFactories.register(DuskBlockEntities.TREASURE_CHEST, ::ChestBlockEntityRenderer)


        THROWABLE_BOMB_ENTITIES.forEach {
            EntityRendererRegistry.register(it, ::FlyingItemEntityRenderer)
        }

        CUTOUT_BLOCKS.forEach {
            BlockRenderLayerMap.INSTANCE.putBlock(it, RenderLayer.getCutout())
        }
//        translucentBlock.forEach {
//            BlockRenderLayerMap.INSTANCE.putBlock(it, RenderLayer.getTranslucent())
//        }
    }
}
