package org.teamvoided.dusk_debris.world.gen.surface_builders

import net.minecraft.block.Blocks
import net.minecraft.registry.tag.TagKey
import net.minecraft.util.math.BlockPos
import net.minecraft.world.Heightmap
import net.minecraft.world.biome.Biome
import net.minecraft.world.biome.source.BiomeAccess
import net.minecraft.world.chunk.Chunk
import net.minecraft.world.gen.DensityFunction
import net.minecraft.world.gen.RandomState
import net.minecraft.world.gen.chunk.BlockColumn

object AmethystCave {
    //    var glacierIce: DoublePerlinNoiseSampler? = null
    var finalDensity: DensityFunction? = null

    fun createAmethystCave(
        random: RandomState,
        seaLevel: Int,
        biome: BiomeAccess,
        chunk: Chunk,
        blockColumn: BlockColumn,
        x: Int,
        z: Int,
        biomeTag: TagKey<Biome>
    ) {
//        if (glacierIce == null) glacierIce = random.getOrCreateNoiseSampler(DnDNoise.GLACIER_ICE_PICKER)
        if (finalDensity == null) finalDensity = chunk.settings

        val y = chunk.sampleHeightmap(Heightmap.Type.OCEAN_FLOOR_WG, x, z) + 1
        if (biome.getBiome(BlockPos(x, y, z)).isIn(biomeTag)) {

        }
    }

    private fun placeCrystalBlock(yLevel: Int, blockColumn: BlockColumn, finalDensity: Double) {
        val block =
            if (finalDensity < 0.1) Blocks.AMETHYST_BLOCK
            else if (finalDensity < 0.25) Blocks.CALCITE
            else if (finalDensity < 0.35) Blocks.SMOOTH_BASALT
            else null

        if (block != null)
            blockColumn.setState(yLevel, block.defaultState)
    }
}