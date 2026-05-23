package org.teamvoided.dusk_debris.block.sot

import com.mojang.serialization.MapCodec
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.InteractionHand
import net.minecraft.world.ItemInteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.IntegerProperty
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.shapes.CollisionContext
import net.minecraft.world.phys.shapes.Shapes
import net.minecraft.world.phys.shapes.VoxelShape
import org.teamvoided.dusk_debris.block.not_blocks.DuskProperties
import org.teamvoided.dusk_debris.block.sot.StackedChaliceBlock.Companion.tryMakeFromBlock
import org.teamvoided.dusk_debris.util.rotate

open class GildedChaliceBlock(settings: Properties) : MysteriousVesselBlock(settings) {
    init {
        this.registerDefaultState(
            (stateDefinition.any())
                .setValue(FACING, Direction.NORTH)
                .setValue(CHALICES, 1)
                .setValue(WATERLOGGED, false)
        )
    }

    override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block, BlockState>) {
        super.createBlockStateDefinition(builder)
        builder.add(CHALICES)
    }

    override fun getShape(
        state: BlockState,
        world: BlockGetter,
        pos: BlockPos,
        context: CollisionContext
    ): VoxelShape {
        val rotations = when (state.getValue(FACING)) {
            Direction.NORTH -> 0
            Direction.SOUTH -> 2
            Direction.WEST -> 3
            Direction.EAST -> 1
            else -> 0
        }
        return (when (state.getValue(CHALICES)) {
            1 -> CHALICES_1
            2 -> CHALICES_2
            3 -> CHALICES_3
            4 -> CHALICES_4
            else -> CHALICES_1
        }).rotate(rotations)
    }

    override fun canBeReplaced(state: BlockState, context: BlockPlaceContext): Boolean {
        if (!context.isSecondaryUseActive && state.getValue(CHALICES) < 4) {
            val item = context.itemInHand.item
            if (item is BlockItem && item.block is GildedChaliceBlock) return true
        }
        return super.canBeReplaced(state, context)
    }

    override fun getStateForPlacement(ctx: BlockPlaceContext): BlockState {
        val blockState = ctx.level.getBlockState(ctx.clickedPos)
        if (blockState.`is`(this)) {
            return blockState.cycle(CHALICES)
        }
        return super.getStateForPlacement(ctx)
    }

    // Stacked Chalice Code
    override fun useItemOn(
        stack: ItemStack, state: BlockState, world: Level, pos: BlockPos,
        player: Player, hand: InteractionHand, hitResult: BlockHitResult,
    ): ItemInteractionResult {
        val result = tryMakeFromBlock(stack, state, world, pos, player)
        if (result != null) return result
        return super.useItemOn(stack, state, world, pos, player, hand, hitResult)
    }

    companion object {
        val CODEC: MapCodec<GildedChaliceBlock> = simpleCodec { settings: Properties ->
            GildedChaliceBlock(
                settings
            )
        }
        val CHALICES_1: VoxelShape = Shapes.or(
            box(6.0, 0.0, 6.0, 10.0, 9.0, 10.0)
        )
        val CHALICES_2: VoxelShape = Shapes.or(
            box(2.0, 0.0, 5.0, 6.0, 9.0, 9.0),
            box(10.0, 0.0, 7.0, 14.0, 9.0, 11.0)
        )
        val CHALICES_3: VoxelShape = Shapes.or(
            box(2.0, 0.0, 8.0, 6.0, 9.0, 12.0),
            box(10.0, 0.0, 10.0, 14.0, 9.0, 14.0),
            box(6.0, 0.0, 2.0, 10.0, 9.0, 6.0)
        )
        val CHALICES_4: VoxelShape = Shapes.or(
            box(2.0, 0.0, 8.0, 6.0, 9.0, 12.0),
            box(10.0, 0.0, 10.0, 14.0, 9.0, 14.0),
            box(4.0, 0.0, 2.0, 8.0, 9.0, 6.0),
            box(9.0, 0.0, 3.0, 13.0, 9.0, 7.0)
        )
        val CHALICES: IntegerProperty = DuskProperties.CHALICES
    }
}