package org.teamvoided.dusk_debris.world.gen.tree.root

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.tags.BlockTags
import net.minecraft.util.RandomSource
import net.minecraft.util.valueproviders.IntProvider
import net.minecraft.world.level.LevelSimulatedReader
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration
import net.minecraft.world.level.levelgen.feature.rootplacers.AboveRootPlacement
import net.minecraft.world.level.levelgen.feature.rootplacers.RootPlacer
import net.minecraft.world.level.levelgen.feature.rootplacers.RootPlacerType
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider
import org.teamvoided.dusk_debris.init.worldgen.trees.DuskTreeStuff
import org.teamvoided.dusk_debris.util.isInSet
import org.teamvoided.dusk_debris.util.isInTag
import org.teamvoided.dusk_debris.world.gen.tree.root.config.CypressRootConfig
import java.util.*
import java.util.function.BiConsumer

class CypressRootPlacer(
    trunkOffsetY: IntProvider,
    rootProvider: BlockStateProvider,
    aboveRootPlacement: Optional<AboveRootPlacement>,
    private val config: CypressRootConfig
) : RootPlacer(trunkOffsetY, rootProvider, aboveRootPlacement) {
    override fun placeRoots(
        world: LevelSimulatedReader,
        replacer: BiConsumer<BlockPos, BlockState>,
        random: RandomSource,
        pos: BlockPos,
        trunkPos: BlockPos,
        config: TreeConfiguration
    ): Boolean {
        val rootTops = mutableSetOf<BlockPos>()

        Direction.Plane.HORIZONTAL.forEach { dir1 ->
            Direction.Plane.HORIZONTAL.forEach loop1@{ dir2 ->
                if (dir1 == dir2) return@loop1
                val blockPos = trunkPos.below().relative(dir1).relative(dir2)
                if (canPlaceRoot(world, blockPos)) {
                    rootTops.add(blockPos.below(if (random.nextInt(0, 4) == 0) 2 else 1))
                }
            }
            val blockPos = trunkPos.relative(dir1)
            if (canPlaceRoot(world, blockPos)) {
                rootTops.add(blockPos.above(if (random.nextInt(0, 3) == 0) 1 else 0))
            }
        }

        rootTops.forEach { this.placeRoot(world, replacer, random, it, config) }
        return true
    }

    override fun canPlaceRoot(world: LevelSimulatedReader, pos: BlockPos): Boolean {
        return super.canPlaceRoot(world, pos)
                || world.isInSet(pos, config.canGrowThrough)
                || world.isInTag(pos, BlockTags.REPLACEABLE)
    }

    override fun placeRoot(
        world: LevelSimulatedReader,
        replacer: BiConsumer<BlockPos, BlockState>,
        random: RandomSource,
        pos: BlockPos,
        config: TreeConfiguration
    ) {
        var mPos = pos
        var x =0
        while (canPlaceRoot(world, mPos)) {
            if (x > MAX_ROOT_LENGTH) return
            replacer.accept(mPos, config.trunkProvider.getState(random, mPos))
            x++
            mPos = mPos.below()
        }
    }

    override fun type(): RootPlacerType<CypressRootPlacer> = DuskTreeStuff.CYPRESS_ROOT_PLACER

    companion object {
        const val MAX_ROOT_LENGTH: Int = 15
        val CODEC: MapCodec<CypressRootPlacer> = RecordCodecBuilder.mapCodec {
            rootPlacerParts(it).and(
                CypressRootConfig.CODEC.fieldOf("cypress_root_placement").forGetter(CypressRootPlacer::config)
            ).apply(it, ::CypressRootPlacer)
        }
    }
}