package org.teamvoided.dusk_debris.block

import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.util.RandomSource
import net.minecraft.world.level.LevelAccessor
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import org.teamvoided.dusk_debris.block.not_blocks.DuskProperties

abstract class AbstractMysticalPowerBlock(settings: Properties) : Block(settings) {
    init {
        this.registerDefaultState(stateDefinition.any().setValue(DuskProperties.ACTIVE, false))
    }

    override fun tick(state: BlockState, world: ServerLevel, pos: BlockPos, random: RandomSource) {
        val isActive = state.getValue(DuskProperties.ACTIVE)
        world.setBlock(pos, state.setValue(DuskProperties.ACTIVE, !isActive), 2)
        if (!isActive) scheduleTick(state.block, world, pos, DISABLE_DELAY)
        super.tick(state, world, pos, random)
    }

    fun scheduleTick(block: Block, world: LevelAccessor, pos: BlockPos, delay: Int = ACTIVATION_DELAY) =
        world.scheduleTick(pos, block, delay)

    override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block, BlockState>) {
        builder.add(DuskProperties.ACTIVE)
    }

    companion object {
        const val ACTIVATION_DELAY = 2
        const val DISABLE_DELAY = ACTIVATION_DELAY * 10
    }
}