package org.teamvoided.dusk_debris.block

import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.util.RandomSource
import net.minecraft.world.level.block.WeatheringCopper
import net.minecraft.world.level.block.WeatheringCopper.WeatherState
import net.minecraft.world.level.block.state.BlockState

class OxidizableFanBlock(private val oxidizationLevel: WeatherState, strength: Int, settings: Properties) :
    FanBlock(strength, settings), WeatheringCopper {

    override fun randomTick(state: BlockState?, world: ServerLevel?, pos: BlockPos?, random: RandomSource?) {
        this.changeOverTime(state, world, pos, random)
    }

    override fun isRandomlyTicking(state: BlockState): Boolean {
        return WeatheringCopper.getNext(state.block).isPresent
    }

    override fun getAge(): WeatherState {
        return this.oxidizationLevel
    }
}