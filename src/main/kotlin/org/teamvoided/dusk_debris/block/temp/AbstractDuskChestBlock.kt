package org.teamvoided.dusk_debris.block.temp

import com.mojang.serialization.MapCodec
import net.minecraft.block.BlockState
import net.minecraft.block.BlockWithEntity
import net.minecraft.block.DoubleBlockProperties
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import java.util.function.Supplier

abstract class AbstractDuskChestBlock<E : BlockEntity> protected constructor(
    settings: Settings,
    protected val entityTypeRetriever: Supplier<BlockEntityType<out E>>
) : BlockWithEntity(settings) {
    abstract override fun getCodec(): MapCodec<out AbstractDuskChestBlock<E>>

    abstract fun getBlockEntitySource(
        state: BlockState,
        world: World,
        pos: BlockPos,
        ignoreBlocked: Boolean
    ): DoubleBlockProperties.PropertySource<out E>
}
