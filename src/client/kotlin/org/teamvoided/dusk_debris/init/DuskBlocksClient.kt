package org.teamvoided.dusk_debris.init

import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry
import net.minecraft.block.Block
import net.minecraft.client.color.block.BlockColorProvider
import net.minecraft.client.color.world.BiomeColors
import net.minecraft.client.color.world.FoliageColors
import net.minecraft.client.color.world.GrassColors
import net.minecraft.client.render.RenderLayer
import org.teamvoided.dusk_debris.block.sot.GunpowderBlock

object DuskBlocksClient {
    fun init() {
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


        registerTint({ _, world, pos, _ ->
            if (world != null && pos != null) BiomeColors.getGrassColor(world, pos)
            else GrassColors.getDefault()
        }, *DuskBlocks.GRASS_TINT_BLOCKS.toTypedArray())
        DuskBlocks.CUTOUT_BLOCKS.forEach { BlockRenderLayerMap.INSTANCE.putBlock(it, RenderLayer.getCutout()) }
        DuskBlocks.TRANSLUCENT_BLOCKS.forEach {
            BlockRenderLayerMap.INSTANCE.putBlock(it, RenderLayer.getTranslucent())
        }
    }

    private fun registerTint(provider: BlockColorProvider, vararg blocks: Block) =
        ColorProviderRegistry.BLOCK.register(provider, *blocks)
}