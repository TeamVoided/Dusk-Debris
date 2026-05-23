package org.teamvoided.dusk_debris.block.voided.sign

import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.block.CeilingHangingSignBlock
import net.minecraft.world.level.block.state.properties.WoodType

class VoidCeilingHangingSignBlock(override val texture: ResourceLocation, woodType: WoodType, settings: Properties) :
    CeilingHangingSignBlock( woodType, settings.forceSolidOn()), VoidSign

