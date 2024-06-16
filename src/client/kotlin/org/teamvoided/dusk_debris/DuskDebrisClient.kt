package org.teamvoided.dusk_debris

import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry
import net.minecraft.client.render.RenderLayer
import org.teamvoided.dusk_autumn.init.DuskEntities
import org.teamvoided.dusk_debris.DuskDebris.log
import org.teamvoided.dusk_debris.entity.gunpowder_barrel.GunpowderBarrelEntityRenderer
import org.teamvoided.dusk_debris.init.DuskBlocks

@Suppress("unused")
object DuskDebrisClient{

    val cutoutBlock =  listOf(
        DuskBlocks.CYPRUS_DOOR,
        DuskBlocks.CYPRUS_TRAPDOOR,
        DuskBlocks.CHARRED_DOOR,
        DuskBlocks.CHARRED_TRAPDOOR,
    )

    fun init() {
        log.info("Hello from Client")
        EntityRendererRegistry.register(DuskEntities.GUNPOWDER_BARREL,::GunpowderBarrelEntityRenderer)

        cutoutBlock.forEach {
            BlockRenderLayerMap.INSTANCE.putBlock(it, RenderLayer.getCutout())
        }
    }
}
