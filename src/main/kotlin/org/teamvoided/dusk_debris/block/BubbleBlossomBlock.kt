package org.teamvoided.dusk_debris.block

import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.server.level.ServerLevel
import net.minecraft.util.Mth
import net.minecraft.util.RandomSource
import net.minecraft.util.SpawnUtil
import net.minecraft.world.entity.MobCategory
import net.minecraft.world.entity.MobSpawnType
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.Level
import net.minecraft.world.level.LevelAccessor
import net.minecraft.world.level.LevelReader
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.shapes.CollisionContext
import net.minecraft.world.phys.shapes.VoxelShape
import org.teamvoided.dusk_debris.init.DuskEntities
import org.teamvoided.dusk_debris.init.DuskParticles

class BubbleBlossomBlock(settings: Properties) : Block(settings) {
    override fun canSurvive(state: BlockState, world: LevelReader, pos: BlockPos): Boolean {
        return canSupportCenter(world, pos.below(), Direction.UP) && !world.isWaterAt(pos)
    }

    override fun updateShape(
        state: BlockState,
        direction: Direction,
        neighborState: BlockState,
        world: LevelAccessor,
        pos: BlockPos,
        neighborPos: BlockPos
    ): BlockState {
        return if (direction == Direction.DOWN && !this.canSurvive(state, world, pos)) Blocks.AIR.defaultBlockState()
        else super.updateShape(state, direction, neighborState, world, pos, neighborPos)
    }

    override fun animateTick(state: BlockState, world: Level, pos: BlockPos, random: RandomSource) {
        if (random.nextInt(13) == 0) {
            if (random.nextInt(3) == 0) {
                world.addParticle(
                    DuskParticles.PURPLE_BUBBLE,
                    pos.x + 0.5,
                    pos.y + 0.3,
                    pos.z + 0.5,
                    0.0, 0.0, 0.0
                )
            }
            val randPos = BlockPos(
                pos.x + Mth.nextInt(random, -10, 10),
                pos.y + random.nextInt(10),
                pos.z + Mth.nextInt(random, -10, 10)
            )
            val blockState = world.getBlockState(randPos)
            if (!blockState.isCollisionShapeFullBlock(world, randPos)) {
                world.addParticle(
                    DuskParticles.PURPLE_BIOME_BUBBLE,
                    randPos.x + random.nextDouble(),
                    randPos.y + random.nextDouble(),
                    randPos.z + random.nextDouble(),
                    0.0, 0.0, 0.0
                )
            }
        }
    }

    override fun randomTick(state: BlockState, world: ServerLevel, pos: BlockPos, random: RandomSource) {
        if (random.nextInt(1024) == 0) {
            val spawnCap = world.chunkSource.lastSpawnState
            if (spawnCap != null && spawnCap.mobCategoryCounts.getInt(MobCategory.AMBIENT) < MobCategory.AMBIENT.maxInstancesPerChunk) {
                repeat(random.nextInt(4) + 1) {
                    SpawnUtil.trySpawnMob(
                        DuskEntities.TINY_ENEMY_JELLYFISH,
                        MobSpawnType.TRIGGERED,
                        world,
                        pos,
                        20, 5, 6,
                        spawnInAirStrategy
                    )
                }
            }
        }
    }

    override fun getShape(
        state: BlockState,
        world: BlockGetter,
        pos: BlockPos,
        context: CollisionContext
    ): VoxelShape = SHAPE

    companion object {
        private val SHAPE: VoxelShape = box(2.0, 0.0, 2.0, 14.0, 6.0, 14.0)

        val spawnInAirStrategy: SpawnUtil.Strategy =
            SpawnUtil.Strategy { world: ServerLevel, _: BlockPos, _: BlockState, blockPos: BlockPos, blockState2: BlockState ->
                blockState2.getCollisionShape(world, blockPos).isEmpty
            }
    }
}