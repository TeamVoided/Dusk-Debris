package org.teamvoided.dusk_debris.block.temp

import com.mojang.serialization.MapCodec
import net.minecraft.core.BlockPos
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.BaseEntityBlock
import net.minecraft.world.level.block.DoubleBlockCombiner
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import java.util.function.Supplier

abstract class AbstractDuskChestBlock<E : BlockEntity> protected constructor(
    settings: Properties,
    protected val entityTypeRetriever: Supplier<BlockEntityType<out E>>
) : BaseEntityBlock(settings) {
    abstract override fun codec(): MapCodec<out AbstractDuskChestBlock<E>>

    abstract fun getBlockEntitySource(
        state: BlockState,
        world: Level,
        pos: BlockPos,
        ignoreBlocked: Boolean
    ): DoubleBlockCombiner.NeighborCombineResult<out E>
}
