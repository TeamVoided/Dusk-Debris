package org.teamvoided.dusk_debris.block.voided.sign

import net.minecraft.block.WoodType
import net.minecraft.block.sign.WallSignBlock
import net.minecraft.util.Identifier

class VoidWallSignBlock(override val texture: Identifier, woodType: WoodType, settings: Settings) :
    WallSignBlock(woodType, settings.solid()), VoidSign

