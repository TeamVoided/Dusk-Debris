package org.teamvoided.dusk_debris.block

import net.minecraft.block.BlockState
import net.minecraft.block.MossBlock
import net.minecraft.registry.Holder
import net.minecraft.registry.Registry
import net.minecraft.registry.RegistryKeys
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.random.RandomGenerator
import net.minecraft.world.World
import net.minecraft.world.WorldView
import net.minecraft.world.gen.feature.ConfiguredFeature
import net.minecraft.world.gen.feature.UndergroundConfiguredFeatures

class OvergrowthBlock(settings: Settings) : MossBlock(settings) {

    //override fun isFertilizable(world: WorldView, pos: BlockPos, state: BlockState): Boolean {
    //    Direction.entries.forEach { if (world.getBlockState(pos.offset(it)).isAir) return true }
    //    return false
    //}

    override fun fertilize(world: ServerWorld, random: RandomGenerator, pos: BlockPos, state: BlockState) {
        world.registryManager
            .getOptional(RegistryKeys.CONFIGURED_FEATURE)
            .flatMap { it.getHolder(FEATURE) }
            .ifPresent { (it.value()).generate(world, world.chunkManager.chunkGenerator, random, pos.up()) }
    }

    companion object {
        private val FEATURE = UndergroundConfiguredFeatures.MOSS_PATCH_BONE_MEAL
    }
}