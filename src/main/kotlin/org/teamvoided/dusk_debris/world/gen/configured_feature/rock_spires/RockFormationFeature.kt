package org.teamvoided.dusk_debris.world.gen.configured_feature.rock_spires

import com.mojang.serialization.Codec
import net.minecraft.core.BlockPos
import net.minecraft.util.RandomSource
import net.minecraft.world.level.WorldGenLevel
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.levelgen.feature.Feature
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext
import org.teamvoided.dusk_debris.world.gen.configured_feature.config.rock_spires.RockFormationFeatureConfig
import org.teamvoided.dusk_debris.world.gen.noise.FastNoise
import kotlin.math.abs
import kotlin.math.pow

open class RockFormationFeature<T : RockFormationFeatureConfig>(codec: Codec<T>) :
    Feature<T>(codec) {
    override fun place(context: FeaturePlaceContext<T>): Boolean {
        val random = context.random()
        val world = context.level()
        val origin = context.origin()
        val config = context.config()

        val replaceable = config.replaceable
        val widthx: Int = config.sizeXZ.sample(random)
        val widthz: Int = config.sizeXZ.sample(random)
        val height: Int = config.sizeY.sample(random)
        val exponent: Double = config.exponent.sample(random).toDouble()

        if (origin.y + height > world.maxBuildHeight || origin.y - (height - 2) < world.minBuildHeight) return false
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
                    val pos = origin.offset(offset)
                    val worldState = world.getBlockState(pos)
                    if (worldState.`is`(replaceable) && !worldState.`is`(Blocks.CAVE_AIR)) {
                        val vec3d = offset.center
                        val x = abs(vec3d.x / widthx).pow(exponent)
                        val z = abs(vec3d.z / widthz).pow(exponent)
                        val y = abs(vec3d.y / (height * if (offset.y < 0) .5 else 1.0)).pow(exponent)
                        val the = -loopY / height.toDouble()
                        val noise =
                        noiseShaper.GetNoise((pos.center.multiply(1.0, 0.2, 1.0))) * 0.5 * (1 - the * the)
                        val distance = x + y + z
                        if (distance + noise < 1) {
                            val together = getBlock(world, pos, config, random, distance, yDepth, noiseDecorator)
                            when (together.first) {
                                true -> yDepth++
                                false -> yDepth == 10
                                null -> 0
                            }
                            this.setBlock(world, pos, together.second)
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
        world: WorldGenLevel,
        pos: BlockPos,
        config: T,
        random: RandomSource,
        distance: Double,
        yDepth: Int,
        noiseDecorator: FastNoise
    ): Pair<Boolean?, BlockState> {
        return (null to config.rockState.getState(random, pos))
    }
}