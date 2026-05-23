package org.teamvoided.dusk_debris.block

import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.level.LevelAccessor
import net.minecraft.world.level.block.state.BlockState
import org.teamvoided.dusk_debris.block.not_blocks.DuskProperties

class MysticalStreamBlock(settings: Properties) : AbstractMysticalPowerBlock(settings) {
    override fun updateShape(
        state: BlockState,
        direction: Direction,
        neighborState: BlockState,
        world: LevelAccessor,
        pos: BlockPos,
        neighborPos: BlockPos
    ): BlockState {
        if (neighborState.block is AbstractMysticalPowerBlock &&
            !state.getValue(DuskProperties.ACTIVE) &&
            neighborState.getValue(DuskProperties.ACTIVE)
        )
            scheduleTick(state.block, world, pos)
        return super.updateShape(state, direction, neighborState, world, pos, neighborPos)
    }
}