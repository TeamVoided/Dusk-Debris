package org.teamvoided.dusk_debris.world.gen.configured_feature

import com.mojang.serialization.Codec
import net.minecraft.core.BlockPos
import net.minecraft.util.RandomSource
import net.minecraft.world.level.WorldGenLevel
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.chunk.ChunkGenerator
import net.minecraft.world.level.levelgen.feature.Feature
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext
import net.minecraft.world.level.levelgen.feature.configurations.VegetationPatchConfiguration
import net.minecraft.world.level.levelgen.placement.PlacedFeature
import java.util.function.Predicate

class VegetationPatchConvertedFeature(codec: Codec<VegetationPatchConfiguration?>?) :
    Feature<VegetationPatchConfiguration>(codec) {
    override fun place(context: FeaturePlaceContext<VegetationPatchConfiguration>): Boolean {
        val structureWorldAccess = context.level()
        val vegetationPatchFeatureConfig = context.config() as VegetationPatchConfiguration
        val randomGenerator = context.random()
        val blockPos = context.origin()
        val predicate = Predicate { state: BlockState -> state.`is`(vegetationPatchFeatureConfig.replaceable) }
        val i = vegetationPatchFeatureConfig.xzRadius.sample(randomGenerator) + 1
        val j = vegetationPatchFeatureConfig.xzRadius.sample(randomGenerator) + 1
        val set = this.placeGroundAndGetPositions(
            structureWorldAccess,
            vegetationPatchFeatureConfig,
            randomGenerator,
            blockPos,
            predicate,
            i,
            j
        )
        this.generateVegetation(context, structureWorldAccess, vegetationPatchFeatureConfig, randomGenerator, set, i, j)
        return set.isNotEmpty()
    }

    protected fun placeGroundAndGetPositions(
        world: WorldGenLevel,
        config: VegetationPatchConfiguration,
        random: RandomSource,
        pos: BlockPos,
        replaceable: Predicate<BlockState>,
        radiusX: Int,
        radiusZ: Int
    ): Set<BlockPos?> {
        val mutable = pos.mutable()
        val mutable2 = mutable.mutable()
        val direction = config.surface.direction
        val direction2 = direction.opposite
        val set: MutableSet<BlockPos> = HashSet()

        for (i in -radiusX..radiusX) {
            val bl = i == -radiusX || i == radiusX

            for (j in -radiusZ..radiusZ) {
                val bl2 = j == -radiusZ || j == radiusZ
                val bl3 = bl || bl2
                val bl4 = bl && bl2
                val bl5 = bl3 && !bl4
                if (!bl4 && (!bl5 || config.extraEdgeColumnChance != 0.0f && !(random.nextFloat() > config.extraEdgeColumnChance))) {
                    mutable.setWithOffset(pos, i, 0, j)
                    var k = 0
                    while (world.isStateAtPosition(mutable) { it.isAir } && k < config.verticalRange) {
                        mutable.move(direction)
                        ++k
                    }

                    k = 0
                    while (world.isStateAtPosition(mutable) { !it.isAir } && k < config.verticalRange) {
                        mutable.move(direction2)
                        ++k
                    }

                    mutable2.setWithOffset(mutable, config.surface.direction)
                    val blockState = world.getBlockState(mutable2)
                    if (world.isEmptyBlock(mutable) &&
                        blockState.isFaceSturdy(world, mutable2, config.surface.direction.opposite)
                    ) {
                        val l =
                            config.depth.sample(random) + (if (config.extraBottomBlockChance > 0.0f && random.nextFloat() < config.extraBottomBlockChance) 1 else 0)
                        val blockPos = mutable2.immutable()
                        val bl6 = this.placeGround(world, config, replaceable, random, mutable2, l)
                        if (bl6) {
                            set.add(blockPos)
                        }
                    }
                }
            }
        }

        return set
    }

    protected fun generateVegetation(
        context: FeaturePlaceContext<VegetationPatchConfiguration>,
        world: WorldGenLevel?,
        config: VegetationPatchConfiguration,
        random: RandomSource,
        positions: Set<BlockPos?>,
        radiusX: Int,
        radiusZ: Int
    ) {
        val var8: Iterator<*> = positions.iterator()

        while (var8.hasNext()) {
            val blockPos = var8.next() as BlockPos
            if (config.vegetationChance > 0.0f && random.nextFloat() < config.vegetationChance) {
                this.generateVegetationFeature(world, config, context.chunkGenerator(), random, blockPos)
            }
        }
    }

    protected fun generateVegetationFeature(
        world: WorldGenLevel?,
        config: VegetationPatchConfiguration,
        generator: ChunkGenerator?,
        random: RandomSource?,
        pos: BlockPos
    ): Boolean {
        return (config.vegetationFeature.value() as PlacedFeature).place(
            world,
            generator,
            random,
            pos.relative(config.surface.direction.opposite)
        )
    }

    protected fun placeGround(
        world: WorldGenLevel,
        config: VegetationPatchConfiguration,
        replaceable: Predicate<BlockState>,
        random: RandomSource?,
        pos: BlockPos.MutableBlockPos,
        depth: Int
    ): Boolean {
        for (i in 0 until depth) {
            val blockState = config.groundState.getState(random, pos)
            val blockState2 = world.getBlockState(pos)
            if (!blockState.`is`(blockState2.block)) {
                if (!replaceable.test(blockState2)) {
                    return i != 0
                }

                world.setBlock(pos, blockState, 2)
                pos.move(config.surface.direction)
            }
        }

        return true
    }
}
