package org.teamvoided.dusk_debris.block

import com.mojang.serialization.MapCodec
import net.minecraft.block.*
import net.minecraft.block.entity.BlockEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.BlockItem
import net.minecraft.item.ItemStack
import net.minecraft.state.StateManager
import net.minecraft.state.property.IntProperty
import net.minecraft.util.Hand
import net.minecraft.util.ItemInteractionResult
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.shape.VoxelShape
import net.minecraft.util.shape.VoxelShapes
import net.minecraft.world.BlockView
import net.minecraft.world.World
import org.teamvoided.dusk_debris.util.rotate
import org.teamvoided.dusks_and_dungeons.block.entity.QuarterBlockPileBlockEntity

class QuarterBlockPileBlock(settings: Settings?) : HorizontalFacingBlock(settings), BlockEntityProvider {

    init {
        defaultState = stateManager.defaultState.with(FACING, Direction.NORTH)
    }

    override fun getCollisionShape(
        state: BlockState,
        world: BlockView?,
        pos: BlockPos?,
        context: ShapeContext?
    ): VoxelShape = SHAPES[state.get(BLOCKS)].rotate(state.get(FACING).horizontal)

    override fun onInteract(
        stack: ItemStack,
        state: BlockState?,
        world: World,
        pos: BlockPos?,
        entity: PlayerEntity?,
        hand: Hand?,
        hitResult: BlockHitResult?
    ): ItemInteractionResult {
        val item = stack.item
        if (item is BlockItem) {
            val block = item.block
            if (block is HeavyCoreBlock /*SmallPumpkinBlock || block is SmallCarvedPumpkinBlock*/) {
                val blockEntity = world.getBlockEntity(pos)
                if (blockEntity is QuarterBlockPileBlockEntity) {
                    if (world.isClient) {
                        return ItemInteractionResult.CONSUME
                    }

                    return blockEntity.place(block)
                }
            }
        }
        return super.onInteract(stack, state, world, pos, entity, hand, hitResult)
    }

    override fun getCodec(): MapCodec<out HorizontalFacingBlock> = CODEC

    override fun createBlockEntity(pos: BlockPos?, state: BlockState?): BlockEntity =
        QuarterBlockPileBlockEntity(pos, state)

    override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) {
        builder.add(FACING, BLOCKS)
    }

    companion object {
        val CODEC: MapCodec<QuarterBlockPileBlock> = createCodec(::QuarterBlockPileBlock)
        val BLOCKS: IntProperty = IntProperty.of("blocks", 0, 2)
        val SHAPES = arrayOf(
            VoxelShapes.union(createCuboidShape(4.0, 4.0, 4.0, 12.0, 12.0, 12.0)),
            VoxelShapes.union(createCuboidShape(4.0, 4.0, 0.0, 12.0, 12.0, 16.0)),
            VoxelShapes.union(
                createCuboidShape(4.0, 4.0, 0.0, 12.0, 12.0, 16.0),
                createCuboidShape(4.0, 4.0, 4.0, 12.0, 12.0, 12.0)
            )
        )
    }
}
