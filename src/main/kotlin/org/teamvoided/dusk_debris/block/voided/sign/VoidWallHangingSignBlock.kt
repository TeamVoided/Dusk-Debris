package org.teamvoided.dusk_debris.block.voided.sign

import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.block.WallHangingSignBlock
import net.minecraft.world.level.block.state.properties.WoodType

class VoidWallHangingSignBlock(override val texture: ResourceLocation, woodType: WoodType, settings: Properties) :
    WallHangingSignBlock(woodType, settings.forceSolidOn()), VoidSign

