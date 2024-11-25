package org.teamvoided.dusk_debris.init

import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry
import net.minecraft.client.color.world.BiomeColors
import net.minecraft.client.color.world.FoliageColors
import net.minecraft.client.render.RenderLayer
import org.teamvoided.dusk_debris.block.GunpowderBlock

object DuskBlocksClient {
    fun init(){
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

        DuskBlocks.CUTOUT_BLOCKS.forEach { BlockRenderLayerMap.INSTANCE.putBlock(it, RenderLayer.getCutout()) }
        DuskBlocks.TRANSLUCENT_BLOCKS.forEach {
            BlockRenderLayerMap.INSTANCE.putBlock(
                it,
                RenderLayer.getTranslucent()
            )
        }
    }
}