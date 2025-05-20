package org.teamvoided.dusk_debris.block

import net.minecraft.block.BlockState
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.WorldAccess
import org.teamvoided.dusk_debris.block.not_blocks.DuskProperties

class MysticalStreamBlock(settings: Settings) : AbstractMysticalPowerBlock(settings) {
    override fun getStateForNeighborUpdate(
        state: BlockState,
        direction: Direction,
        neighborState: BlockState,
        world: WorldAccess,
        pos: BlockPos,
        neighborPos: BlockPos
    ): BlockState {
        if (neighborState.block is AbstractMysticalPowerBlock && neighborState.get(DuskProperties.ACTIVE))
            scheduleTick(state.block, world, pos)
        return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos)
    }
}