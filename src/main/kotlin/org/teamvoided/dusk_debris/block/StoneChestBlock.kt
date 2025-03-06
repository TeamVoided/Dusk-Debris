package org.teamvoided.dusk_debris.block

import net.minecraft.block.Block
import net.minecraft.block.BlockRenderType
import net.minecraft.block.BlockState
import net.minecraft.block.ChestBlock
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.entity.BlockEntityTicker
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.block.entity.ChestBlockEntity
import net.minecraft.item.ItemPlacementContext
import net.minecraft.stat.Stat
import net.minecraft.stat.Stats
import net.minecraft.state.StateManager
import net.minecraft.state.property.Properties
import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import org.teamvoided.dusk_debris.block.entity.StoneChestBlockEntity
import org.teamvoided.dusk_debris.block.not_blocks.ChestPhase
import org.teamvoided.dusk_debris.block.not_blocks.DuskProperties
import java.util.function.Supplier

class StoneChestBlock(settings: Settings, supplier: Supplier<BlockEntityType<StoneChestBlockEntity>>) :
    ChestBlock(settings, supplier as Supplier<BlockEntityType<out ChestBlockEntity>>) {

    init {
        this.stateManager.defaultState
            .with(DuskProperties.CHEST_PHASE, ChestPhase.CLOSED)
            .with(DuskProperties.LID, false)
    }

    override fun getPlacementState(ctx: ItemPlacementContext): BlockState? {
        return super.getPlacementState(ctx)?.with(DuskProperties.LID, false)
    }

    override fun <T : BlockEntity> getTicker(
        world: World,
        state: BlockState,
        type: BlockEntityType<T>
    ): BlockEntityTicker<T>? {
        return if (world.isClient)
            checkType(type, this.expectedEntityType, StoneChestBlockEntity::tick)
        else
            null
    }

    override fun getExpectedEntityType(): BlockEntityType<StoneChestBlockEntity> {
        return entityTypeRetriever.get() as BlockEntityType<StoneChestBlockEntity>
    }

    override fun createBlockEntity(pos: BlockPos, state: BlockState): BlockEntity = StoneChestBlockEntity(pos, state)

    override fun getRenderType(state: BlockState): BlockRenderType = BlockRenderType.MODEL

    override fun getOpenStat(): Stat<Identifier> = Stats.CUSTOM.getOrCreateStat(Stats.OPEN_CHEST)

    override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) {
        super.appendProperties(builder)
        builder.add(DuskProperties.CHEST_PHASE, DuskProperties.LID)
    }
}