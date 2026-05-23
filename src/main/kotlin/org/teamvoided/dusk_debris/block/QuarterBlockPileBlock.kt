package org.teamvoided.dusk_debris.block

import com.mojang.serialization.MapCodec
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.InteractionHand
import net.minecraft.world.ItemInteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.EntityBlock
import net.minecraft.world.level.block.HeavyCoreBlock
import net.minecraft.world.level.block.HorizontalDirectionalBlock
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.IntegerProperty
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.shapes.CollisionContext
import net.minecraft.world.phys.shapes.Shapes
import net.minecraft.world.phys.shapes.VoxelShape
import org.teamvoided.dusk_debris.util.rotate
import org.teamvoided.dusks_and_dungeons.block.entity.QuarterBlockPileBlockEntity

class QuarterBlockPileBlock(settings: Properties?) : HorizontalDirectionalBlock(settings), EntityBlock {

    init {
        registerDefaultState(stateDefinition.any().setValue(FACING, Direction.NORTH))
    }

    override fun getCollisionShape(
        state: BlockState,
        world: BlockGetter?,
        pos: BlockPos?,
        context: CollisionContext?
    ): VoxelShape = SHAPES[state.getValue(BLOCKS)].rotate(state.getValue(FACING).get2DDataValue())

    override fun useItemOn(
        stack: ItemStack,
        state: BlockState?,
        world: Level,
        pos: BlockPos?,
        entity: Player?,
        hand: InteractionHand?,
        hitResult: BlockHitResult?
    ): ItemInteractionResult {
        val item = stack.item
        if (item is BlockItem) {
            val block = item.block
            if (block is HeavyCoreBlock /*SmallPumpkinBlock || block is SmallCarvedPumpkinBlock*/) {
                val blockEntity = world.getBlockEntity(pos)
                if (blockEntity is QuarterBlockPileBlockEntity) {
                    if (world.isClientSide) {
                        return ItemInteractionResult.CONSUME
                    }

                    return blockEntity.place(block)
                }
            }
        }
        return super.useItemOn(stack, state, world, pos, entity, hand, hitResult)
    }

    override fun codec(): MapCodec<out HorizontalDirectionalBlock> = CODEC

    override fun newBlockEntity(pos: BlockPos?, state: BlockState?): BlockEntity =
        QuarterBlockPileBlockEntity(pos, state)

    override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block, BlockState>) {
        builder.add(FACING, BLOCKS)
    }

    companion object {
        val CODEC: MapCodec<QuarterBlockPileBlock> = simpleCodec(::QuarterBlockPileBlock)
        val BLOCKS: IntegerProperty = IntegerProperty.create("blocks", 0, 2)
        val SHAPES = arrayOf(
            Shapes.or(box(4.0, 4.0, 4.0, 12.0, 12.0, 12.0)),
            Shapes.or(box(4.0, 4.0, 0.0, 12.0, 12.0, 16.0)),
            Shapes.or(
                box(4.0, 4.0, 0.0, 12.0, 12.0, 16.0),
                box(4.0, 4.0, 4.0, 12.0, 12.0, 12.0)
            )
        )
    }
}
