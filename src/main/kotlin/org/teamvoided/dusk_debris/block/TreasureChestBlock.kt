package org.teamvoided.dusk_debris.block

import com.mojang.serialization.MapCodec
import net.minecraft.block.*
import net.minecraft.block.entity.BlockEntity
import net.minecraft.entity.ai.pathing.NavigationType
import net.minecraft.entity.mob.PiglinBrain
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.state.StateManager
import net.minecraft.state.property.Properties
import net.minecraft.state.property.Property
import net.minecraft.util.ActionResult
import net.minecraft.util.ItemScatterer
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.random.RandomGenerator
import net.minecraft.util.shape.VoxelShape
import net.minecraft.world.BlockView
import net.minecraft.world.World
import org.teamvoided.dusk_debris.block.entity.TreasureChestBlockEntity

class TreasureChestBlock(settings: Settings) : BlockWithEntity(settings), Waterloggable {

    init {
        this.defaultState.with(FACING, Direction.NORTH).with(WATERLOGGED, false)
    }

    override fun getCodec(): MapCodec<out BlockWithEntity> = CODEC

    override fun createBlockEntity(pos: BlockPos, state: BlockState): BlockEntity {
        return TreasureChestBlockEntity(pos, state)
    }

    override fun getOutlineShape(
        state: BlockState?,
        world: BlockView?,
        pos: BlockPos?,
        context: ShapeContext?
    ): VoxelShape {
        return SHAPE
    }

    override fun getRenderType(state: BlockState): BlockRenderType = BlockRenderType.ANIMATED
    override fun onUse(
        state: BlockState,
        world: World,
        pos: BlockPos,
        entity: PlayerEntity,
        hitResult: BlockHitResult
    ): ActionResult {
        if (world.isClient) {
            return ActionResult.SUCCESS
        } else {
            val blockEntity = world.getBlockEntity(pos)
            if (blockEntity is TreasureChestBlockEntity) {
                entity.openHandledScreen(blockEntity)
                PiglinBrain.onGuardedBlockInteracted(entity, true)
            }
            return ActionResult.CONSUME
        }
    }

    override fun onStateReplaced(
        state: BlockState,
        world: World,
        pos: BlockPos,
        newState: BlockState,
        moved: Boolean
    ) {
        ItemScatterer.scatterInventory(state, newState, world, pos)
        super.onStateReplaced(state, world, pos, newState, moved)
    }

    override fun scheduledTick(state: BlockState, world: ServerWorld, pos: BlockPos, random: RandomGenerator) {
        val blockEntity = world.getBlockEntity(pos)!!
        if (blockEntity is TreasureChestBlockEntity) {
            blockEntity.tick()
        }
    }
    override fun canPathfindThrough(state: BlockState, navigationType: NavigationType): Boolean {
        return false
    }

    override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) {
        builder.add(FACING, WATERLOGGED)
    }

    companion object {
        val CODEC = createCodec(::TreasureChestBlock)
        protected val SHAPE: VoxelShape =
            createCuboidShape(1.0, 0.0, 1.0, 15.0, 14.0, 15.0)
        val FACING = HorizontalFacingBlock.FACING
        val WATERLOGGED = Properties.WATERLOGGED
    }
}