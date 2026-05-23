package org.teamvoided.dusk_debris.block

import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.util.RandomSource
import net.minecraft.world.level.block.state.BlockState

class MysticalPulseBlock(settings: Properties) : AbstractMysticalPowerBlock(settings) {
    override fun randomTick(state: BlockState, world: ServerLevel, pos: BlockPos, random: RandomSource) {
        if (random.nextInt(1) == 0) scheduleTick(state.block, world, pos, 0)
        super.randomTick(state, world, pos, random)
    }
}