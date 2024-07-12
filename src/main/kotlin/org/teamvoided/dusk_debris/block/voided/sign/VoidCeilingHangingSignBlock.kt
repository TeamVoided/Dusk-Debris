package org.teamvoided.dusk_debris.block.voided.sign

import net.minecraft.block.WoodType
import net.minecraft.block.sign.CeilingHangingSignBlock
import net.minecraft.util.Identifier

class VoidCeilingHangingSignBlock(override val texture: Identifier, woodType: WoodType, settings: Settings) :
    CeilingHangingSignBlock( woodType, settings.solid()), VoidSign

