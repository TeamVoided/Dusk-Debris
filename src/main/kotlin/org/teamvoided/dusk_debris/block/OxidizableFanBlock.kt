package org.teamvoided.dusk_debris.block

import com.mojang.serialization.MapCodec
import net.minecraft.block.BlockState
import net.minecraft.block.CopperGrateBlock
import net.minecraft.block.Oxidizable
import net.minecraft.block.Oxidizable.OxidizationLevel
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.math.BlockPos
import net.minecraft.util.random.RandomGenerator

class OxidizableFanBlock(private val oxidizationLevel: OxidizationLevel, strength: Int, settings: Settings) :
    FanBlock(strength, settings), Oxidizable {

    override fun randomTick(state: BlockState?, world: ServerWorld?, pos: BlockPos?, random: RandomGenerator?) {
        this.tickDegradation(state, world, pos, random)
    }

    override fun getRandomTicks(state: BlockState): Boolean {
        return Oxidizable.getIncreasedOxidationBlock(state.block).isPresent
    }

    override fun getDegradationLevel(): OxidizationLevel {
        return this.oxidizationLevel
    }
}