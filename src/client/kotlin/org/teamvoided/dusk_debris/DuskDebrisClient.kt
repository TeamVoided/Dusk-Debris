package org.teamvoided.dusk_debris

import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry
import net.minecraft.client.render.RenderLayer
import net.minecraft.client.render.entity.FlyingItemEntityRenderer
import net.minecraft.client.render.entity.ItemEntityRenderer
import net.minecraft.client.render.entity.ProjectileEntityRenderer
import org.teamvoided.dusk_autumn.init.DuskEntities
import org.teamvoided.dusk_debris.DuskDebris.log
import org.teamvoided.dusk_debris.entity.BlunderbombEntity
import org.teamvoided.dusk_debris.entity.gunpowder_barrel.GunpowderBarrelEntityRenderer
import org.teamvoided.dusk_debris.init.DuskBlocks

@Suppress("unused")
object DuskDebrisClient{

    val cutoutBlock =  listOf(
        DuskBlocks.BLUNDERBOMB_BLOCK,
        DuskBlocks.CYPRESS_DOOR,
        DuskBlocks.CYPRESS_TRAPDOOR,
        DuskBlocks.CHARRED_DOOR,
        DuskBlocks.CHARRED_TRAPDOOR,
    )

    fun init() {
        log.info("Hello from Client")
        EntityRendererRegistry.register(DuskEntities.GUNPOWDER_BARREL,::GunpowderBarrelEntityRenderer)
        EntityRendererRegistry.register(DuskEntities.BLUNDERBOMB,::FlyingItemEntityRenderer)

        cutoutBlock.forEach {
            BlockRenderLayerMap.INSTANCE.putBlock(it, RenderLayer.getCutout())
        }
    }
}
