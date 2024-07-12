package org.teamvoided.dusk_debris.block.voided.sign

import net.minecraft.block.WoodType
import net.minecraft.block.sign.SignBlock
import net.minecraft.util.Identifier

class VoidSignBlock(override val texture: Identifier, woodType: WoodType, settings: Settings) :
    SignBlock(woodType, settings.solid()), VoidSign

