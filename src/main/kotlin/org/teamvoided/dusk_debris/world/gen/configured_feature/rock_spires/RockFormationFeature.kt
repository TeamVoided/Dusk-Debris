package org.teamvoided.dusk_debris.world.gen.configured_feature.rock_spires

import com.mojang.serialization.Codec
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.registry.tag.BlockTags
import net.minecraft.util.math.BlockPos
import net.minecraft.util.random.RandomGenerator
import net.minecraft.world.StructureWorldAccess
import net.minecraft.world.gen.feature.DefaultFeatureConfig
import net.minecraft.world.gen.feature.Feature
import net.minecraft.world.gen.feature.util.FeatureContext
import org.teamvoided.dusk_debris.world.gen.configured_feature.config.rock_spires.RockFormationFeatureConfig
import org.teamvoided.dusk_debris.world.gen.configured_feature.config.rock_spires.SurfaceFormationFeatureConfig
import org.teamvoided.dusk_debris.world.gen.noise.FastNoise
import kotlin.math.*

open class RockFormationFeature<T: RockFormationFeatureConfig>(codec: Codec<T>) :
    Feature<T>(codec) {
    override fun place(context: FeatureContext<T>): Boolean {
        val random = context.random
        val world = context.world
        val origin = context.origin
        val config = context.config

        val widthx: Int = config.sizeXZ[random]
        val widthz: Int = config.sizeXZ[random]
        val height: Int = config.sizeY[random]
        val exponent: Double = config.exponent[random].toDouble()

        if (origin.y + height > world.topY || origin.y - (height - 2) < world.bottomY) return false
        val noiseShaper = FastNoise(world.seed.toInt())
        noiseShaper.SetFractalType(FastNoise.FractalType.FBM)
        noiseShaper.SetNoiseType(FastNoise.NoiseType.Perlin) //Cubic
        noiseShaper.SetFrequency(0.15f)
        val noiseDecorator = FastNoise(world.seed.toInt())
        noiseDecorator.SetFractalType(FastNoise.FractalType.FBM)
        noiseDecorator.SetNoiseType(FastNoise.NoiseType.Perlin) //Cubic
        noiseDecorator.SetFrequency(0.15f)

        var yDepth: Int
        for (loopX in -widthx until widthx) {
            for (loopZ in -widthz until widthz) {
                yDepth = 0
                for (loopY in -height until height / 2) {
                    val offset = BlockPos(loopX, -loopY, loopZ)
                    val pos = origin.add(offset)
                    val worldState = world.getBlockState(pos)
                    if (worldState.isIn(BlockTags.REPLACEABLE) && !worldState.isOf(Blocks.CAVE_AIR)) {
                        val vec3d = offset.ofCenter()
                        val x = abs(vec3d.x / widthx).pow(exponent)
                        val z = abs(vec3d.z / widthz).pow(exponent)
                        val y = abs(vec3d.y / (height * if (offset.y < 0) .5 else 1.0)).pow(exponent)
                        val the = -loopY / height.toDouble()
                        val noise =
                            noiseShaper.GetNoise((pos.ofCenter().multiply(1.0, 0.2, 1.0))) * 0.5 * (1 - the * the)
                        val distance = x + y + z
                        if (distance + noise < 1) {
                            val together = getBlock(world, pos, config, random, distance, yDepth, noiseDecorator)
                            when (together.first) {
                                true -> yDepth++
                                false -> yDepth == 10
                                null -> 0
                            }
                            this.setBlockState(world, pos, together.second)
                        } else {
                            yDepth = 0
                        }
                    } else if (loopY > 0) {
                        break
                    }
                }
            }
        }
        return true
    }

    open fun getBlock(
        world: StructureWorldAccess,
        pos: BlockPos,
        config: T,
        random: RandomGenerator,
        distance: Double,
        yDepth: Int,
        noiseDecorator: FastNoise
    ): Pair<Boolean?, BlockState> {
        return (null to config.rockState.getBlockState(random, pos))
    }
}