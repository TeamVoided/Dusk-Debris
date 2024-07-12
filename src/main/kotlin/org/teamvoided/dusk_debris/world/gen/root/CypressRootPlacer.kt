package org.teamvoided.dusk_debris.world.gen.root

import com.google.common.collect.Lists
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.block.BlockState
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.math.int_provider.IntProvider
import net.minecraft.util.random.RandomGenerator
import net.minecraft.world.TestableWorld
import net.minecraft.world.gen.feature.TreeFeatureConfig
import net.minecraft.world.gen.root.AboveRootPlacement
import net.minecraft.world.gen.root.RootPlacer
import net.minecraft.world.gen.root.RootPlacerType
import net.minecraft.world.gen.stateprovider.BlockStateProvider
import org.teamvoided.dusk_debris.init.DuskWorldgen
import org.teamvoided.dusk_debris.world.gen.root.config.CypressRootConfig
import java.util.*
import java.util.function.BiConsumer

class CypressRootPlacer(
    trunkOffsetY: IntProvider,
    rootProvider: BlockStateProvider,
    aboveRootPlacement: Optional<AboveRootPlacement>,
    private val cypressRootPlacement: CypressRootConfig
) :
    RootPlacer(trunkOffsetY, rootProvider, aboveRootPlacement) {
    override fun generate(
        world: TestableWorld,
        replacer: BiConsumer<BlockPos, BlockState>,
        random: RandomGenerator,
        pos: BlockPos,
        trunkPos: BlockPos,
        config: TreeFeatureConfig
    ): Boolean {
        val list: MutableList<BlockPos> = Lists.newArrayList()
        val mutable = pos.mutableCopy()

        while (mutable.y < trunkPos.y) {
            if (!this.canReplace(world, mutable)) {
                return false
            }

            mutable.move(Direction.UP)
        }

        list.add(trunkPos.down())
        var var9: Iterator<*> = Direction.Type.HORIZONTAL.iterator()

        while (var9.hasNext()) {
            val direction = var9.next() as Direction
            val blockPos = trunkPos.offset(direction)
            val list2: MutableList<BlockPos> = Lists.newArrayList()
            if (!this.canGrow(world, random, blockPos, direction, trunkPos, list2, 0)) {
                return false
            }

            list.addAll(list2)
            list.add(trunkPos.offset(direction))
        }

        var9 = list.iterator()

        while (var9.hasNext()) {
            this.placeRoot(world, replacer, random, var9.next(), config)
        }

        return true
    }

    private fun canGrow(
        world: TestableWorld,
        random: RandomGenerator,
        pos: BlockPos,
        direction: Direction,
        origin: BlockPos,
        potentialRootPositions: MutableList<BlockPos>,
        rootLength: Int
    ): Boolean {
        val i = cypressRootPlacement.maxRootLength()
        if (rootLength != i && potentialRootPositions.size <= i) {
            val list = this.getPotentialRootPositions(pos, direction, random, origin)
            val var10: Iterator<*> = list.iterator()

            while (var10.hasNext()) {
                val blockPos = var10.next() as BlockPos
                if (this.canReplace(world, blockPos)) {
                    potentialRootPositions.add(blockPos)
                    if (!this.canGrow(
                            world,
                            random,
                            blockPos,
                            direction,
                            origin,
                            potentialRootPositions,
                            rootLength + 1
                        )
                    ) {
                        return false
                    }
                }
            }

            return true
        } else {
            return false
        }
    }

    protected fun getPotentialRootPositions(
        pos: BlockPos,
        direction: Direction?,
        random: RandomGenerator,
        origin: BlockPos?
    ): List<BlockPos> {
        val blockPos = pos.down()
        val blockPos2 = pos.offset(direction)
        val i = pos.getManhattanDistance(origin)
        val j = cypressRootPlacement.maxRootWidth()
        val f = cypressRootPlacement.randomSkewChance()
        return if (i > j - 3 && i <= j) {
            if (random.nextFloat() < f) java.util.List.of(blockPos, blockPos2.down()) else java.util.List.of(blockPos)
        } else if (i > j) {
            java.util.List.of(blockPos)
        } else if (random.nextFloat() < f) {
            java.util.List.of(blockPos)
        } else {
            if (random.nextBoolean()) java.util.List.of(blockPos2) else java.util.List.of(blockPos)
        }
    }

    override fun canReplace(world: TestableWorld, pos: BlockPos): Boolean {
        return super.canReplace(world, pos) || world.testBlockState(
            pos
        ) { block: BlockState -> block.isIn(cypressRootPlacement.canGrowThrough()) }
    }

    override fun placeRoot(
        world: TestableWorld,
        replacer: BiConsumer<BlockPos, BlockState>,
        random: RandomGenerator,
        pos: BlockPos,
        config: TreeFeatureConfig
    ) {
        if (world.testBlockState(pos) { block: BlockState ->
                block.isIn(cypressRootPlacement.muddyRootsIn())
            }) {
            val blockState = cypressRootPlacement.muddyRootsProvider().getBlockState(random, pos)
            replacer.accept(pos, this.applyWaterlogging(world, pos, blockState))
        } else {
            super.placeRoot(world, replacer, random, pos, config)
        }
    }

    override fun getType(): RootPlacerType<*> {
        return DuskWorldgen.CYPRESS_ROOT_PLACER
    }

    companion object {
        const val MAX_ROOT_WIDTH: Int = 8
        const val MAX_ROOT_LENGTH: Int = 15
        val CODEC: MapCodec<CypressRootPlacer> =
            RecordCodecBuilder.mapCodec { instance: RecordCodecBuilder.Instance<CypressRootPlacer> ->
                rootPlacerCodec(instance).and(
                    CypressRootConfig.CODEC.fieldOf("cypress_root_placement")
                        .forGetter { placer: CypressRootPlacer -> placer.cypressRootPlacement }
                ).apply(
                    instance
                ) { trunkOffsetY: IntProvider, rootProvider: BlockStateProvider, aboveRootPlacement: Optional<AboveRootPlacement>, cypressRootPlacement: CypressRootConfig ->
                    CypressRootPlacer(
                        trunkOffsetY,
                        rootProvider,
                        aboveRootPlacement,
                        cypressRootPlacement
                    )
                }
            }
    }
}