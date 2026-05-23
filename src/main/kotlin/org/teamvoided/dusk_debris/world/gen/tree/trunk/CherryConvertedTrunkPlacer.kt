package org.teamvoided.dusk_debris.world.gen.tree.trunk

import com.mojang.serialization.Codec
import com.mojang.serialization.DataResult
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.util.RandomSource
import net.minecraft.util.valueproviders.IntProvider
import net.minecraft.util.valueproviders.UniformInt
import net.minecraft.world.level.LevelSimulatedReader
import net.minecraft.world.level.block.RotatedPillarBlock
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacer
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType
import java.util.function.BiConsumer
import java.util.function.Function
import kotlin.math.abs
import kotlin.math.max

class CherryConvertedTrunkPlacer(
    baseHeight: Int,
    firstRandomHeight: Int,
    secondRandomHeight: Int,
    private val branchCount: IntProvider,
    private val branchHorizontalLength: IntProvider,
    private val branchStartOffsetFromTop: UniformInt,
    private val branchEndOffsetFromTop: IntProvider
) : TrunkPlacer(baseHeight, firstRandomHeight, secondRandomHeight) {
    private val secondBranchStartOffsetFromTop: UniformInt = UniformInt.of(
        branchStartOffsetFromTop.minValue, branchStartOffsetFromTop.maxValue - 1
    )

    override fun type(): TrunkPlacerType<*> {
        return TrunkPlacerType.CHERRY_TRUNK_PLACER
    }

    override fun placeTrunk(
        world: LevelSimulatedReader,
        replacer: BiConsumer<BlockPos, BlockState>,
        random: RandomSource,
        height: Int,
        startPos: BlockPos,
        config: TreeConfiguration
    ): List<FoliagePlacer.FoliageAttachment> {
        setDirtAt(world, replacer, random, startPos.below(), config)
        val i = max(0, (height - 1 + branchStartOffsetFromTop.sample(random)))
        var j = max(0, (height - 1 + secondBranchStartOffsetFromTop.sample(random)))
        if (j >= i) {            ++j        }

        val branchCount = branchCount.sample(random)
        val branches3 = branchCount == 3
        val branchesMoreThan1 = branchCount >= 2
        val l = if (branches3) {
            height
        } else if (branchesMoreThan1) {
            (max(i, j) + 1)
        } else {
            i + 1
        }

        for (m in 0 until l) {
            this.placeLog(world, replacer, random, startPos.above(m), config)
        }

        val list: MutableList<FoliagePlacer.FoliageAttachment> = ArrayList()
        if (branches3) {
            list.add(FoliagePlacer.FoliageAttachment(startPos.above(l), 0, false))
        }

        val mutable = BlockPos.MutableBlockPos()
        val direction = Direction.Plane.HORIZONTAL.getRandomDirection(random)
        val function =
            Function { state: BlockState -> state.trySetValue(RotatedPillarBlock.AXIS, direction.axis) }
        list.add(
            this.generateBranch(
                world,
                replacer,
                random,
                height,
                startPos,
                config,
                function,
                direction,
                i,
                i < l - 1,
                mutable
            )
        )
        if (branchesMoreThan1) {
            list.add(
                this.generateBranch(
                    world,
                    replacer,
                    random,
                    height,
                    startPos,
                    config,
                    function,
                    direction.opposite,
                    j,
                    j < l - 1,
                    mutable
                )
            )
        }

        return list
    }

    private fun generateBranch(
        world: LevelSimulatedReader,
        replacer: BiConsumer<BlockPos, BlockState>,
        random: RandomSource,
        height: Int,
        startPos: BlockPos,
        config: TreeConfiguration,
        withAxis: Function<BlockState, BlockState?>,
        dirHor: Direction,
        branchOffset: Int,
        branchStartOffset: Boolean,
        mutablePos: BlockPos.MutableBlockPos
    ): FoliagePlacer.FoliageAttachment {
        mutablePos.set(startPos).move(Direction.UP, branchOffset)
        val branchHeight = height - 1 + branchEndOffsetFromTop.sample(random)
        val bl = branchStartOffset || branchHeight < branchOffset
        val branchLength = branchHorizontalLength.sample(random) + (if (bl) 1 else 0)
        val endPos = startPos.relative(dirHor, branchLength).above(branchHeight)
        val k = if (bl) 2 else 1

        for (l in 0 until k) {
            this.placeLog(world, replacer, random, mutablePos.move(dirHor), config, withAxis)
        }

        val dirVert = if (endPos.y > mutablePos.y) Direction.UP else Direction.DOWN

        while (true) {
            val manDist = mutablePos.distManhattan(endPos)
            if (manDist == 0) {
                return FoliagePlacer.FoliageAttachment(endPos.above(), 0, false)
            }

            val f = abs((endPos.y - mutablePos.y) / manDist.toFloat())
            val verOrHor = random.nextFloat() < f
            mutablePos.move(if (verOrHor) dirVert else dirHor)
            this.placeLog(
                world,
                replacer,
                random,
                mutablePos,
                config,
                if (verOrHor) Function.identity() else withAxis
            )
        }
    }

    companion object {
        private val VALIDATION_CODEC: Codec<UniformInt> =
            UniformInt.CODEC.codec().validate { provider: UniformInt ->
                if (provider.maxValue - provider.minValue < 1) DataResult.error { "Need at least 2 blocks variation for the branch starts to fit both branches" } else DataResult.success(
                    provider
                )
            }
        val CODEC: MapCodec<CherryConvertedTrunkPlacer> =
            RecordCodecBuilder.mapCodec { instance: RecordCodecBuilder.Instance<CherryConvertedTrunkPlacer> ->
                trunkPlacerParts(instance).and(
                    instance.group(
                        IntProvider.codec(1, 3).fieldOf("branch_count")
                            .forGetter { placer: CherryConvertedTrunkPlacer -> placer.branchCount },
                        IntProvider.codec(2, 16).fieldOf("branch_horizontal_length")
                            .forGetter { placer: CherryConvertedTrunkPlacer -> placer.branchHorizontalLength },
                        IntProvider.validateCodec(-16, 0, VALIDATION_CODEC).fieldOf("branch_start_offset_from_top")
                            .forGetter { placer: CherryConvertedTrunkPlacer -> placer.branchStartOffsetFromTop },
                        IntProvider.codec(-16, 16).fieldOf("branch_end_offset_from_top")
                            .forGetter { placer: CherryConvertedTrunkPlacer -> placer.branchEndOffsetFromTop })
                )
                    .apply(instance) { baseHeight: Int, firstRandomHeight: Int, secondRandomHeight: Int, branchCount: IntProvider, branchHorizontalLength: IntProvider, branchStartOffsetFromTop: UniformInt, branchEndOffsetFromTop: IntProvider ->
                        CherryConvertedTrunkPlacer(
                            baseHeight,
                            firstRandomHeight,
                            secondRandomHeight,
                            branchCount,
                            branchHorizontalLength,
                            branchStartOffsetFromTop,
                            branchEndOffsetFromTop
                        )
                    }
            }
    }
}
