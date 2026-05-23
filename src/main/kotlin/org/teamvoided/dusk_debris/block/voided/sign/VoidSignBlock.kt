package org.teamvoided.dusk_debris.block.voided.sign

import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.block.StandingSignBlock
import net.minecraft.world.level.block.state.properties.WoodType

class VoidSignBlock(override val texture: ResourceLocation, woodType: WoodType, settings: Properties) :
    StandingSignBlock(woodType, settings.forceSolidOn()), VoidSign

