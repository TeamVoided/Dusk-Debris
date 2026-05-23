package org.teamvoided.dusk_debris.block.voided.sign

import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.block.WallSignBlock
import net.minecraft.world.level.block.state.properties.WoodType

class VoidWallSignBlock(override val texture: ResourceLocation, woodType: WoodType, settings: Properties) :
    WallSignBlock(woodType, settings.forceSolidOn()), VoidSign

