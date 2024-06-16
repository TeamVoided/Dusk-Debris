package org.teamvoided.voidlib.woodset.block.sign

import net.minecraft.block.sign.SignType
import net.minecraft.block.sign.WallHangingSignBlock
import net.minecraft.util.Identifier

class VoidWallHangingSignBlock(override val texture: Identifier, woodType: SignType, settings: Settings) :
    WallHangingSignBlock(woodType, settings.solid()), VoidSign

