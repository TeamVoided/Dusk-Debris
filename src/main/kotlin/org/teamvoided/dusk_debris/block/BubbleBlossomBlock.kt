package org.teamvoided.dusk_debris.block

import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.block.ShapeContext
import net.minecraft.entity.SpawnGroup
import net.minecraft.entity.SpawnReason
import net.minecraft.server.world.ServerChunkManager
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.math.MathHelper
import net.minecraft.util.random.RandomGenerator
import net.minecraft.util.shape.VoxelShape
import net.minecraft.world.BlockView
import net.minecraft.world.World
import net.minecraft.world.WorldAccess
import net.minecraft.world.WorldView
import org.teamvoided.dusk_debris.entity.TinyEnemyJellyfishEntity
import org.teamvoided.dusk_debris.init.DuskEntities
import org.teamvoided.dusk_debris.init.DuskParticles
import java.util.stream.Collectors
import java.util.stream.Stream

class BubbleBlossomBlock(settings: Settings) : Block(settings) {
    override fun canPlaceAt(state: BlockState, world: WorldView, pos: BlockPos): Boolean {
        return sideCoversSmallSquare(world, pos.down(), Direction.UP) && !world.isWater(pos)
    }

    override fun getStateForNeighborUpdate(
        state: BlockState,
        direction: Direction,
        neighborState: BlockState,
        world: WorldAccess,
        pos: BlockPos,
        neighborPos: BlockPos
    ): BlockState {
        return if (direction == Direction.DOWN && !this.canPlaceAt(state, world, pos)) Blocks.AIR.defaultState
        else super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos)
    }

    override fun randomDisplayTick(state: BlockState, world: World, pos: BlockPos, random: RandomGenerator) {
        if (random.nextInt(13) == 0)
            world.addParticle(
                DuskParticles.PURPLE_BUBBLE,
                pos.x + 0.5,
                pos.y + 0.3,
                pos.z + 0.5,
                0.0, 0.0, 0.0
            )
        val randPos = BlockPos(
            pos.x + MathHelper.nextInt(random, -10, 10),
            pos.y + random.nextInt(10),
            pos.z + MathHelper.nextInt(random, -10, 10)
        )
        val blockState = world.getBlockState(randPos)
        if (!blockState.isFullCube(world, randPos)) {
            world.addParticle(
                DuskParticles.PURPLE_BIOME_BUBBLE,
                randPos.x + random.nextDouble(),
                randPos.y + random.nextDouble(),
                randPos.z + random.nextDouble(),
                0.0, 0.0, 0.0
            )
        }
    }

    override fun randomTick(state: BlockState, world: ServerWorld, pos: BlockPos, random: RandomGenerator) {
        if (random.nextInt(100) == 0) {
            val spawnCap = world.chunkManager.spawnInfo
            if (spawnCap != null && spawnCap.groupToCount.getInt(SpawnGroup.AMBIENT) < SpawnGroup.AMBIENT.capacity) {
                repeat(random.nextInt(2) + 1) {
                    val randPos = BlockPos(
                        pos.x + MathHelper.nextInt(random, -10, 10),
                        pos.y + random.nextInt(10),
                        pos.z + MathHelper.nextInt(random, -10, 10)
                    )
                    val blockState = world.getBlockState(randPos)
                    if (!blockState.isFullCube(world, randPos)) {
                        val jellyfish = TinyEnemyJellyfishEntity(DuskEntities.TINY_ENEMY_JELLYFISH, world)
                        jellyfish.refreshPositionAndAngles(randPos.ofBottomCenter(), 0f, 0.0f)
                        jellyfish.initialize(world, world.getLocalDifficulty(randPos), SpawnReason.NATURAL, null)
                        world.spawnEntity(jellyfish)
                    }
                }
            }
        }
    }

    override fun getOutlineShape(
        state: BlockState,
        world: BlockView,
        pos: BlockPos,
        context: ShapeContext
    ): VoxelShape {
        return SHAPE
    }

    companion object {
        private val SHAPE: VoxelShape = createCuboidShape(2.0, 0.0, 2.0, 14.0, 6.0, 14.0)
    }
}