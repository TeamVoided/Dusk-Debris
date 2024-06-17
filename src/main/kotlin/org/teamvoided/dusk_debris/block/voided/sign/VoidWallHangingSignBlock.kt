package org.teamvoided.dusk_debris.block.voided.sign

import net.minecraft.block.sign.SignType
import net.minecraft.block.sign.WallHangingSignBlock
import net.minecraft.util.Identifier
import org.teamvoided.dusk_debris.block.voided.sign.VoidSign

class VoidWallHangingSignBlock(override val texture: Identifier, woodType: SignType, settings: Settings) :
    WallHangingSignBlock(woodType, settings.solid()), VoidSign

