package org.teamvoided.dusk_debris.block

import net.minecraft.block.BlockState
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.math.BlockPos
import net.minecraft.util.random.RandomGenerator

class MysticalPulseBlock(settings: Settings) : AbstractMysticalPowerBlock(settings) {
    override fun randomTick(state: BlockState, world: ServerWorld, pos: BlockPos, random: RandomGenerator) {
        if (random.nextInt(1) == 0) scheduleTick(state.block, world, pos, 0)
        super.randomTick(state, world, pos, random)
    }
}