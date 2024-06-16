package org.teamvoided.voidlib.woodset.block.sign

import net.minecraft.block.sign.SignType
import net.minecraft.block.sign.WallSignBlock
import net.minecraft.util.Identifier

class VoidWallSignBlock(override val texture: Identifier, woodType: SignType, settings: Settings) :
    WallSignBlock(woodType, settings.solid()), VoidSign

