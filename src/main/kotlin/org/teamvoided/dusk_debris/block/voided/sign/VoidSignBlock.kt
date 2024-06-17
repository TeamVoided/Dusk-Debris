package org.teamvoided.dusk_debris.block.voided.sign

import net.minecraft.block.sign.SignBlock
import net.minecraft.block.sign.SignType
import net.minecraft.util.Identifier

class VoidSignBlock(override val texture: Identifier, woodType: SignType, settings: Settings) :
    SignBlock(woodType, settings.solid()), VoidSign

