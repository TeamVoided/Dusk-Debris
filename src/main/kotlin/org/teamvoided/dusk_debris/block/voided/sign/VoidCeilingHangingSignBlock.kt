package org.teamvoided.voidlib.woodset.block.sign

import net.minecraft.block.sign.CeilingHangingSignBlock
import net.minecraft.block.sign.SignType
import net.minecraft.util.Identifier

class VoidCeilingHangingSignBlock(override val texture: Identifier, woodType: SignType, settings: Settings) :
    CeilingHangingSignBlock( woodType, settings.solid()), VoidSign

