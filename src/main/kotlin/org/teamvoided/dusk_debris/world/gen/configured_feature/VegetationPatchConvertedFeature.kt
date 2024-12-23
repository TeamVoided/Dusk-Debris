package org.teamvoided.dusk_debris.world.gen.configured_feature

import com.mojang.serialization.Codec
import net.minecraft.block.AbstractBlock
import net.minecraft.block.BlockState
import net.minecraft.util.math.BlockPos
import net.minecraft.util.random.RandomGenerator
import net.minecraft.world.StructureWorldAccess
import net.minecraft.world.gen.chunk.ChunkGenerator
import net.minecraft.world.gen.feature.Feature
import net.minecraft.world.gen.feature.PlacedFeature
import net.minecraft.world.gen.feature.VegetationPatchFeatureConfig
import net.minecraft.world.gen.feature.util.FeatureContext
import java.util.function.Predicate

class VegetationPatchConvertedFeature(codec: Codec<VegetationPatchFeatureConfig?>?) :
    Feature<VegetationPatchFeatureConfig>(codec) {
    override fun place(context: FeatureContext<VegetationPatchFeatureConfig>): Boolean {
        val structureWorldAccess = context.world
        val vegetationPatchFeatureConfig = context.config as VegetationPatchFeatureConfig
        val randomGenerator = context.random
        val blockPos = context.origin
        val predicate = Predicate { state: BlockState -> state.isIn(vegetationPatchFeatureConfig.replaceable) }
        val i = vegetationPatchFeatureConfig.horizontalRadius[randomGenerator] + 1
        val j = vegetationPatchFeatureConfig.horizontalRadius[randomGenerator] + 1
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
        world: StructureWorldAccess,
        config: VegetationPatchFeatureConfig,
        random: RandomGenerator,
        pos: BlockPos,
        replaceable: Predicate<BlockState>,
        radiusX: Int,
        radiusZ: Int
    ): Set<BlockPos?> {
        val mutable = pos.mutableCopy()
        val mutable2 = mutable.mutableCopy()
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
                    mutable[pos, i, 0] = j
                    var k = 0
                    while (world.testBlockState(mutable) { it.isAir } && k < config.verticalRange) {
                        mutable.move(direction)
                        ++k
                    }

                    k = 0
                    while (world.testBlockState(mutable) { !it.isAir } && k < config.verticalRange) {
                        mutable.move(direction2)
                        ++k
                    }

                    mutable2[mutable] = config.surface.direction
                    val blockState = world.getBlockState(mutable2)
                    if (world.isAir(mutable) &&
                        blockState.isSideSolidFullSquare(world, mutable2, config.surface.direction.opposite)
                    ) {
                        val l =
                            config.depth[random] + (if (config.extraBottomBlockChance > 0.0f && random.nextFloat() < config.extraBottomBlockChance) 1 else 0)
                        val blockPos = mutable2.toImmutable()
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
        context: FeatureContext<VegetationPatchFeatureConfig>,
        world: StructureWorldAccess?,
        config: VegetationPatchFeatureConfig,
        random: RandomGenerator,
        positions: Set<BlockPos?>,
        radiusX: Int,
        radiusZ: Int
    ) {
        val var8: Iterator<*> = positions.iterator()

        while (var8.hasNext()) {
            val blockPos = var8.next() as BlockPos
            if (config.vegetationChance > 0.0f && random.nextFloat() < config.vegetationChance) {
                this.generateVegetationFeature(world, config, context.generator, random, blockPos)
            }
        }
    }

    protected fun generateVegetationFeature(
        world: StructureWorldAccess?,
        config: VegetationPatchFeatureConfig,
        generator: ChunkGenerator?,
        random: RandomGenerator?,
        pos: BlockPos
    ): Boolean {
        return (config.vegetationFeature.value() as PlacedFeature).place(
            world,
            generator,
            random,
            pos.offset(config.surface.direction.opposite)
        )
    }

    protected fun placeGround(
        world: StructureWorldAccess,
        config: VegetationPatchFeatureConfig,
        replaceable: Predicate<BlockState>,
        random: RandomGenerator?,
        pos: BlockPos.Mutable,
        depth: Int
    ): Boolean {
        for (i in 0 until depth) {
            val blockState = config.groundState.getBlockState(random, pos)
            val blockState2 = world.getBlockState(pos)
            if (!blockState.isOf(blockState2.block)) {
                if (!replaceable.test(blockState2)) {
                    return i != 0
                }

                world.setBlockState(pos, blockState, 2)
                pos.move(config.surface.direction)
            }
        }

        return true
    }
}
