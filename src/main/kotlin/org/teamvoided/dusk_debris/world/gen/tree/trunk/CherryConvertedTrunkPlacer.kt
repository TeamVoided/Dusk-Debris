package org.teamvoided.dusk_debris.world.gen.tree.trunk

import com.mojang.serialization.Codec
import com.mojang.serialization.DataResult
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.block.BlockState
import net.minecraft.block.PillarBlock
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.math.int_provider.IntProvider
import net.minecraft.util.math.int_provider.UniformIntProvider
import net.minecraft.util.random.RandomGenerator
import net.minecraft.world.TestableWorld
import net.minecraft.world.gen.feature.TreeFeatureConfig
import net.minecraft.world.gen.foliage.FoliagePlacer
import net.minecraft.world.gen.trunk.TrunkPlacer
import net.minecraft.world.gen.trunk.TrunkPlacerType
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
    private val branchStartOffsetFromTop: UniformIntProvider,
    private val branchEndOffsetFromTop: IntProvider
) : TrunkPlacer(baseHeight, firstRandomHeight, secondRandomHeight) {
    private val secondBranchStartOffsetFromTop: UniformIntProvider = UniformIntProvider.create(
        branchStartOffsetFromTop.min, branchStartOffsetFromTop.max - 1
    )

    override fun getType(): TrunkPlacerType<*> {
        return TrunkPlacerType.CHERRY_TRUNK_PLACER
    }

    override fun generate(
        world: TestableWorld,
        replacer: BiConsumer<BlockPos, BlockState>,
        random: RandomGenerator,
        height: Int,
        startPos: BlockPos,
        config: TreeFeatureConfig
    ): List<FoliagePlacer.TreeNode> {
        setToDirt(world, replacer, random, startPos.down(), config)
        val i = max(0, (height - 1 + branchStartOffsetFromTop[random]))
        var j = max(0, (height - 1 + secondBranchStartOffsetFromTop[random]))
        if (j >= i) {            ++j        }

        val branchCount = branchCount[random]
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
            this.placeTrunkBlock(world, replacer, random, startPos.up(m), config)
        }

        val list: MutableList<FoliagePlacer.TreeNode> = ArrayList()
        if (branches3) {
            list.add(FoliagePlacer.TreeNode(startPos.up(l), 0, false))
        }

        val mutable = BlockPos.Mutable()
        val direction = Direction.Type.HORIZONTAL.random(random)
        val function =
            Function { state: BlockState -> state.withIfExists(PillarBlock.AXIS, direction.axis) }
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
        world: TestableWorld,
        replacer: BiConsumer<BlockPos, BlockState>,
        random: RandomGenerator,
        height: Int,
        startPos: BlockPos,
        config: TreeFeatureConfig,
        withAxis: Function<BlockState, BlockState?>,
        dirHor: Direction,
        branchOffset: Int,
        branchStartOffset: Boolean,
        mutablePos: BlockPos.Mutable
    ): FoliagePlacer.TreeNode {
        mutablePos.set(startPos).move(Direction.UP, branchOffset)
        val branchHeight = height - 1 + branchEndOffsetFromTop[random]
        val bl = branchStartOffset || branchHeight < branchOffset
        val branchLength = branchHorizontalLength[random] + (if (bl) 1 else 0)
        val endPos = startPos.offset(dirHor, branchLength).up(branchHeight)
        val k = if (bl) 2 else 1

        for (l in 0 until k) {
            this.placeTrunkBlock(world, replacer, random, mutablePos.move(dirHor), config, withAxis)
        }

        val dirVert = if (endPos.y > mutablePos.y) Direction.UP else Direction.DOWN

        while (true) {
            val manDist = mutablePos.getManhattanDistance(endPos)
            if (manDist == 0) {
                return FoliagePlacer.TreeNode(endPos.up(), 0, false)
            }

            val f = abs((endPos.y - mutablePos.y) / manDist.toFloat())
            val verOrHor = random.nextFloat() < f
            mutablePos.move(if (verOrHor) dirVert else dirHor)
            this.placeTrunkBlock(
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
        private val VALIDATION_CODEC: Codec<UniformIntProvider> =
            UniformIntProvider.CODEC.codec().validate { provider: UniformIntProvider ->
                if (provider.max - provider.min < 1) DataResult.error { "Need at least 2 blocks variation for the branch starts to fit both branches" } else DataResult.success(
                    provider
                )
            }
        val CODEC: MapCodec<CherryConvertedTrunkPlacer> =
            RecordCodecBuilder.mapCodec { instance: RecordCodecBuilder.Instance<CherryConvertedTrunkPlacer> ->
                fillTrunkPlacerFields(instance).and(
                    instance.group(
                        IntProvider.method_35004(1, 3).fieldOf("branch_count")
                            .forGetter { placer: CherryConvertedTrunkPlacer -> placer.branchCount },
                        IntProvider.method_35004(2, 16).fieldOf("branch_horizontal_length")
                            .forGetter { placer: CherryConvertedTrunkPlacer -> placer.branchHorizontalLength },
                        IntProvider.method_49103(-16, 0, VALIDATION_CODEC).fieldOf("branch_start_offset_from_top")
                            .forGetter { placer: CherryConvertedTrunkPlacer -> placer.branchStartOffsetFromTop },
                        IntProvider.method_35004(-16, 16).fieldOf("branch_end_offset_from_top")
                            .forGetter { placer: CherryConvertedTrunkPlacer -> placer.branchEndOffsetFromTop })
                )
                    .apply(instance) { baseHeight: Int, firstRandomHeight: Int, secondRandomHeight: Int, branchCount: IntProvider, branchHorizontalLength: IntProvider, branchStartOffsetFromTop: UniformIntProvider, branchEndOffsetFromTop: IntProvider ->
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
