package org.teamvoided.dusk_debris.util

import net.fabricmc.fabric.api.registry.OxidizableBlocksRegistry
import net.minecraft.block.*
import net.minecraft.block.enums.NoteBlockInstrument
import net.minecraft.block.piston.PistonBehavior
import net.minecraft.sound.BlockSoundGroup
import net.minecraft.util.math.Direction
import org.teamvoided.dusk_debris.block.RibbonBlock
import org.teamvoided.dusk_debris.init.DuskBlocks


fun Block.cutout(): Block {
    DuskBlocks.CUTOUT_BLOCKS.add(this)
    return this
}

fun Block.translucent(): Block {
    DuskBlocks.TRANSLUCENT_BLOCKS.add(this)
    return this
}

fun oxidizeCopperSet(copperList: List<Pair<Block, Block>>) {
    copperList.forEachIndexed { idx, (regular: Block, waxed: Block) ->
        if (idx < copperList.size - 1) {
            OxidizableBlocksRegistry.registerOxidizableBlockPair(
                regular,
                copperList[idx + 1].first
            )
        }
        OxidizableBlocksRegistry.registerWaxableBlockPair(
            regular,
            waxed
        )
    }
}

fun registerSkull(id: String, skullType: SkullBlock.SkullType, instrument: NoteBlockInstrument): Block {
    return DuskBlocks.register(
        id, SkullBlock(
            skullType,
            AbstractBlock.Settings.create().instrument(instrument).strength(1.0f)
                .pistonBehavior(PistonBehavior.DESTROY)
        )
    )
}

fun registerWallSkull(id: String, skullType: SkullBlock.SkullType, dropsLike: Block): Block {
    return DuskBlocks.register(
        id,
        WallSkullBlock(
            skullType,
            AbstractBlock.Settings.create().strength(1.0f).dropsLike(dropsLike)
                .pistonBehavior(PistonBehavior.DESTROY)
        )
    )
}

fun registerRibbon(mapColor: MapColor): Block {
    return RibbonBlock(
        AbstractBlock.Settings.create().mapColor(mapColor).strength(0.1F)
            .sounds(BlockSoundGroup.WOOL).solidBlock(Blocks::nonSolid)
    )
}

fun charredLogOf(topColor: MapColor, sideColor: MapColor): Block {
    return PillarBlock(AbstractBlock.Settings.create().mapColor { state: BlockState ->
        if (state.get(
                PillarBlock.AXIS
            ) === Direction.Axis.Y
        ) topColor else sideColor
    }.instrument(NoteBlockInstrument.BASS).strength(2.0f).sounds(BlockSoundGroup.WOOD))
}