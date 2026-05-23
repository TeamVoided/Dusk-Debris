package org.teamvoided.dusk_debris.world.gen.surface_builders

import net.minecraft.core.BlockPos
import net.minecraft.tags.TagKey
import net.minecraft.world.level.biome.Biome
import net.minecraft.world.level.biome.BiomeManager
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.chunk.BlockColumn
import net.minecraft.world.level.chunk.ChunkAccess
import net.minecraft.world.level.levelgen.DensityFunction
import net.minecraft.world.level.levelgen.Heightmap
import net.minecraft.world.level.levelgen.RandomState

object AmethystCave {
    //    var glacierIce: DoublePerlinNoiseSampler? = null
    var finalDensity: DensityFunction? = null

    fun createAmethystCave(
        random: RandomState,
        seaLevel: Int,
        biome: BiomeManager,
        chunk: ChunkAccess,
        blockColumn: BlockColumn,
        x: Int,
        z: Int,
        biomeTag: TagKey<Biome>
    ) {
//        if (glacierIce == null) glacierIce = random.getOrCreateNoiseSampler(DnDNoise.GLACIER_ICE_PICKER)
//        if (finalDensity == null) finalDensity = chunk.settings

        val y = chunk.getHeight(Heightmap.Types.OCEAN_FLOOR_WG, x, z) + 1
        if (biome.getBiome(BlockPos(x, y, z)).`is`(biomeTag)) {

        }
    }

    private fun placeCrystalBlock(yLevel: Int, blockColumn: BlockColumn, finalDensity: Double) {
        val block =
            if (finalDensity < 0.1) Blocks.AMETHYST_BLOCK
            else if (finalDensity < 0.25) Blocks.CALCITE
            else if (finalDensity < 0.35) Blocks.SMOOTH_BASALT
            else null

        if (block != null)
            blockColumn.setBlock(yLevel, block.defaultBlockState())
    }
}