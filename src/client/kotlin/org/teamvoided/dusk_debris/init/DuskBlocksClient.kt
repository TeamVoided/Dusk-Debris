package org.teamvoided.dusk_debris.init

import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry
import net.minecraft.client.color.block.BlockColor
import net.minecraft.client.renderer.BiomeColors
import net.minecraft.client.renderer.RenderType
import net.minecraft.world.level.FoliageColor
import net.minecraft.world.level.GrassColor
import net.minecraft.world.level.block.Block
import org.teamvoided.dusk_debris.block.sot.GunpowderBlock

object DuskBlocksClient {
    fun init() {
        ColorProviderRegistry.BLOCK.register(
            { _, world, pos, _ ->
                if (world != null && pos != null) BiomeColors.getAverageFoliageColor(world, pos)
                else FoliageColor.get(0.8, 0.4)
            },
            DuskBlocks.CYPRESS_LEAVES
        )
        ColorProviderRegistry.BLOCK.register(
            { state, _, _, _ -> if (state.getValue(GunpowderBlock.LIT)) 0xFF9F32 else 0x383838 },
            DuskBlocks.GUNPOWDER
        )


        registerTint({ _, world, pos, _ ->
            if (world != null && pos != null) BiomeColors.getAverageGrassColor(world, pos)
            else GrassColor.getDefaultColor()
        }, *DuskBlocks.GRASS_TINT_BLOCKS.toTypedArray())
        DuskBlocks.CUTOUT_BLOCKS.forEach { BlockRenderLayerMap.INSTANCE.putBlock(it, RenderType.cutout()) }
        DuskBlocks.TRANSLUCENT_BLOCKS.forEach {
            BlockRenderLayerMap.INSTANCE.putBlock(it, RenderType.translucent())
        }
    }

    private fun registerTint(provider: BlockColor, vararg blocks: Block) =
        ColorProviderRegistry.BLOCK.register(provider, *blocks)
}