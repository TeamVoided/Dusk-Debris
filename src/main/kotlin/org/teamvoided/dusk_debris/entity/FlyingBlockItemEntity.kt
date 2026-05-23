package org.teamvoided.dusk_debris.entity

import net.minecraft.world.entity.projectile.ItemSupplier
import net.minecraft.world.level.block.state.BlockState

interface FlyingBlockItemEntity : ItemSupplier {
    fun getState(): BlockState
}
