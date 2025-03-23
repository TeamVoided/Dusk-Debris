package org.teamvoided.dusk_debris.entity

import net.minecraft.block.BlockState
import net.minecraft.entity.FlyingItemEntity

interface FlyingBlockItemEntity : FlyingItemEntity {
    fun getState(): BlockState
}
