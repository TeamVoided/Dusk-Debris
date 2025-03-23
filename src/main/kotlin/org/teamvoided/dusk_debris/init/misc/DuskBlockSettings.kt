package org.teamvoided.dusk_debris.init.misc

import net.minecraft.block.AbstractBlock.OffsetType
import net.minecraft.block.AbstractBlock.Settings
import net.minecraft.block.AbstractBlock.Settings.copy
import net.minecraft.block.Blocks
import net.minecraft.block.Blocks.BROWN_MUSHROOM_BLOCK
import net.minecraft.block.MapColor
import net.minecraft.block.piston.PistonBehavior
import net.minecraft.sound.BlockSoundGroup

object DuskBlockSettings {
    val MOONCORE: Settings = Settings.create()
        .mapColor(MapColor.LIGHT_BLUE)
        .solid().nonOpaque()
        .sounds(BlockSoundGroup.AMETHYST_CLUSTER)
        .strength(1.5f)
        .ticksRandomly()
        .luminance(15)
        .pistonBehavior(PistonBehavior.DESTROY)
    val REDSTONE_CRYSTAL: Settings = Settings.create()
        .mapColor(MapColor.RED)
        .solid().nonOpaque()
        .sounds(BlockSoundGroup.AMETHYST_CLUSTER)
        .strength(1.5f)
        .ticksRandomly()
        .luminance(9)
        .pistonBehavior(PistonBehavior.DESTROY)
    val PAINTED_ROSE: Settings = Settings.create()
        .mapColor(MapColor.BLUE)
        .noCollision()
        .ticksRandomly()
        .breakInstantly()
        .offsetType(OffsetType.XZ)
        .sounds(BlockSoundGroup.GRASS)
        .pistonBehavior(PistonBehavior.DESTROY)

}