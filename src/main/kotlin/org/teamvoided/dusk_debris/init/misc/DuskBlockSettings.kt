package org.teamvoided.dusk_debris.init.misc

import net.minecraft.world.level.block.SoundType
import net.minecraft.world.level.block.state.BlockBehaviour.OffsetType
import net.minecraft.world.level.block.state.BlockBehaviour.Properties
import net.minecraft.world.level.material.MapColor
import net.minecraft.world.level.material.PushReaction
import org.teamvoided.dusk_debris.util.luminance

object DuskBlockSettings {
    val MOONCORE: Properties = Properties.of()
        .mapColor(MapColor.COLOR_LIGHT_BLUE)
        .forceSolidOn().noOcclusion()
        .sound(SoundType.AMETHYST_CLUSTER)
        .strength(1.5f)
        .randomTicks()
        .luminance(15)
        .pushReaction(PushReaction.DESTROY)
    val REDSTONE_CRYSTAL: Properties = Properties.of()
        .mapColor(MapColor.COLOR_RED)
        .forceSolidOn().noOcclusion()
        .sound(SoundType.AMETHYST_CLUSTER)
        .strength(1.5f)
        .randomTicks()
        .luminance(9)
        .pushReaction(PushReaction.DESTROY)
    val PAINTED_ROSE: Properties = Properties.of()
        .mapColor(MapColor.COLOR_BLUE)
        .noCollission()
        .randomTicks()
        .instabreak()
        .offsetType(OffsetType.XZ)
        .sound(SoundType.GRASS)
        .pushReaction(PushReaction.DESTROY)

}