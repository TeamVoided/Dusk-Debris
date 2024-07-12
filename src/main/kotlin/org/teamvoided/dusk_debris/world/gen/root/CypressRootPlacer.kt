package org.teamvoided.dusk_debris.world.gen.root

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.block.BlockState
import net.minecraft.registry.tag.BlockTags
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
import org.teamvoided.dusk_debris.util.isInSet
import org.teamvoided.dusk_debris.util.isInTag
import org.teamvoided.dusk_debris.world.gen.root.config.CypressRootConfig
import java.util.*
import java.util.function.BiConsumer

class CypressRootPlacer(
    trunkOffsetY: IntProvider,
    rootProvider: BlockStateProvider,
    aboveRootPlacement: Optional<AboveRootPlacement>,
    private val config: CypressRootConfig
) : RootPlacer(trunkOffsetY, rootProvider, aboveRootPlacement) {
    override fun generate(
        world: TestableWorld,
        replacer: BiConsumer<BlockPos, BlockState>,
        random: RandomGenerator,
        pos: BlockPos,
        trunkPos: BlockPos,
        config: TreeFeatureConfig
    ): Boolean {
        val rootTops = mutableSetOf<BlockPos>()

        Direction.Type.HORIZONTAL.forEach { dir1 ->
            Direction.Type.HORIZONTAL.forEach loop1@{ dir2 ->
                if (dir1 == dir2) return@loop1
                val blockPos = trunkPos.down().offset(dir1).offset(dir2)
                if (canReplace(world, blockPos)) {
                    rootTops.add(blockPos.down(if (random.range(0, 4) == 0) 2 else 1))
                }
            }
            val blockPos = trunkPos.offset(dir1)
            if (canReplace(world, blockPos)) {
                rootTops.add(blockPos.up(if (random.range(0, 3) == 0) 1 else 0))
            }
        }

        rootTops.forEach { this.placeRoot(world, replacer, random, it, config) }
        return true
    }

    override fun canReplace(world: TestableWorld, pos: BlockPos): Boolean {
        return super.canReplace(world, pos)
                || world.isInSet(pos, config.canGrowThrough)
                || world.isInTag(pos, BlockTags.REPLACEABLE)
    }

    override fun placeRoot(
        world: TestableWorld,
        replacer: BiConsumer<BlockPos, BlockState>,
        random: RandomGenerator,
        pos: BlockPos,
        config: TreeFeatureConfig
    ) {
        var mPos = pos
        var x =0
        while (canReplace(world, mPos)) {
            if (x > MAX_ROOT_LENGTH) return
            replacer.accept(mPos, config.trunkProvider.getBlockState(random, mPos))
            x++
            mPos = mPos.down()
        }
    }

    override fun getType(): RootPlacerType<CypressRootPlacer> = DuskWorldgen.CYPRESS_ROOT_PLACER

    companion object {
        const val MAX_ROOT_LENGTH: Int = 15
        val CODEC: MapCodec<CypressRootPlacer> = RecordCodecBuilder.mapCodec {
            rootPlacerCodec(it).and(
                CypressRootConfig.CODEC.fieldOf("cypress_root_placement").forGetter(CypressRootPlacer::config)
            ).apply(it, ::CypressRootPlacer)
        }
    }
}