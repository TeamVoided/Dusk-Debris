package org.teamvoided.dusk_debris.block

import net.minecraft.core.BlockPos
import net.minecraft.core.registries.Registries
import net.minecraft.data.worldgen.features.CaveFeatures
import net.minecraft.server.level.ServerLevel
import net.minecraft.util.RandomSource
import net.minecraft.world.level.block.MossBlock
import net.minecraft.world.level.block.state.BlockState

class OvergrowthBlock(settings: Properties) : MossBlock(settings) {

    //override fun isFertilizable(world: WorldView, pos: BlockPos, state: BlockState): Boolean {
    //    Direction.entries.forEach { if (world.getBlockState(pos.offset(it)).isAir) return true }
    //    return false
    //}

    override fun performBonemeal(world: ServerLevel, random: RandomSource, pos: BlockPos, state: BlockState) {
        world.registryAccess()
            .registry(Registries.CONFIGURED_FEATURE)
            .flatMap { it.getHolder(FEATURE) }
            .ifPresent { (it.value()).place(world, world.chunkSource.generator, random, pos.above()) }
    }

    companion object {
        private val FEATURE = CaveFeatures.MOSS_PATCH_BONEMEAL
    }
}