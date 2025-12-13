package org.teamvoided.dusk_debris.block

import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.server.world.ServerWorld
import net.minecraft.state.StateManager
import net.minecraft.util.math.BlockPos
import net.minecraft.util.random.RandomGenerator
import net.minecraft.world.WorldAccess
import org.teamvoided.dusk_debris.block.not_blocks.DuskProperties

abstract class AbstractMysticalPowerBlock(settings: Settings) : Block(settings) {
    init {
        this.defaultState = stateManager.defaultState.with(DuskProperties.ACTIVE, false)
    }

    override fun scheduledTick(state: BlockState, world: ServerWorld, pos: BlockPos, random: RandomGenerator) {
        val isActive = state.get(DuskProperties.ACTIVE)
        world.setBlockState(pos, state.with(DuskProperties.ACTIVE, !isActive), 2)
        if (!isActive) scheduleTick(state.block, world, pos, DISABLE_DELAY)
        super.scheduledTick(state, world, pos, random)
    }

    fun scheduleTick(block: Block, world: WorldAccess, pos: BlockPos, delay: Int = ACTIVATION_DELAY) =
        world.scheduleBlockTick(pos, block, delay)

    override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) {
        builder.add(DuskProperties.ACTIVE)
    }

    companion object {
        const val ACTIVATION_DELAY = 2
        const val DISABLE_DELAY = ACTIVATION_DELAY * 10
    }
}