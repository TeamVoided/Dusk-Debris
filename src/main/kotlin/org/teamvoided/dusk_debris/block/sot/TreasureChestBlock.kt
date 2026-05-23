package org.teamvoided.dusk_debris.block.sot

import com.mojang.serialization.MapCodec
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.server.level.ServerLevel
import net.minecraft.util.RandomSource
import net.minecraft.world.Containers
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.monster.piglin.PiglinAi
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.*
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.world.level.pathfinder.PathComputationType
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.shapes.CollisionContext
import net.minecraft.world.phys.shapes.VoxelShape
import org.teamvoided.dusk_debris.block.entity.TreasureChestBlockEntity

class TreasureChestBlock(settings: Properties) : BaseEntityBlock(settings), SimpleWaterloggedBlock {

    init {
        this.defaultBlockState().setValue(FACING, Direction.NORTH).setValue(WATERLOGGED, false)
    }

    override fun codec(): MapCodec<out BaseEntityBlock> = CODEC

    override fun newBlockEntity(pos: BlockPos, state: BlockState): BlockEntity {
        return TreasureChestBlockEntity(pos, state)
    }

    override fun getShape(
        state: BlockState?,
        world: BlockGetter?,
        pos: BlockPos?,
        context: CollisionContext?
    ): VoxelShape {
        return SHAPE
    }

    override fun getRenderShape(state: BlockState): RenderShape = RenderShape.ENTITYBLOCK_ANIMATED
    override fun useWithoutItem(
        state: BlockState,
        world: Level,
        pos: BlockPos,
        entity: Player,
        hitResult: BlockHitResult
    ): InteractionResult {
        if (world.isClientSide) {
            return InteractionResult.SUCCESS
        } else {
            val blockEntity = world.getBlockEntity(pos)
            if (blockEntity is TreasureChestBlockEntity) {
                entity.openMenu(blockEntity)
                PiglinAi.angerNearbyPiglins(entity, true)
            }
            return InteractionResult.CONSUME
        }
    }

    override fun onRemove(
        state: BlockState,
        world: Level,
        pos: BlockPos,
        newState: BlockState,
        moved: Boolean
    ) {
        Containers.dropContentsOnDestroy(state, newState, world, pos)
        super.onRemove(state, world, pos, newState, moved)
    }

    override fun tick(state: BlockState, world: ServerLevel, pos: BlockPos, random: RandomSource) {
        val blockEntity = world.getBlockEntity(pos)!!
        if (blockEntity is TreasureChestBlockEntity) {
            blockEntity.tick()
        }
    }
    override fun isPathfindable(state: BlockState, navigationType: PathComputationType): Boolean {
        return false
    }

    override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block, BlockState>) {
        builder.add(FACING, WATERLOGGED)
    }

    companion object {
        val CODEC = simpleCodec(::TreasureChestBlock)
        protected val SHAPE: VoxelShape =
            box(1.0, 0.0, 1.0, 15.0, 14.0, 15.0)
        val FACING = HorizontalDirectionalBlock.FACING
        val WATERLOGGED = BlockStateProperties.WATERLOGGED
    }
}