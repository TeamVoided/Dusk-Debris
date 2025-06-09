package org.teamvoided.dusk_debris.block.sot

import net.minecraft.block.BlockEntityProvider
import net.minecraft.block.BlockRenderType
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.BlockItem
import net.minecraft.item.ItemPlacementContext
import net.minecraft.item.ItemStack
import net.minecraft.sound.SoundCategory
import net.minecraft.util.Hand
import net.minecraft.util.ItemInteractionResult
import net.minecraft.util.ItemScatterer
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import org.teamvoided.dusk_debris.block.sot.entity.StackedChaliceBlockEntity
import org.teamvoided.dusk_debris.init.DuskBlocks

open class StackedChaliceBlock(settings: Settings) : GildedChaliceBlock(settings), BlockEntityProvider {
    override fun onInteract(
        stack: ItemStack, state: BlockState, world: World, pos: BlockPos,
        player: PlayerEntity, hand: Hand, hitResult: BlockHitResult,
    ): ItemInteractionResult {
        val result = addChalice(stack, state, world, pos, player)
        if (result != null) return result
        return super.onInteract(stack, state, world, pos, player, hand, hitResult)
    }

    override fun getPlacementState(ctx: ItemPlacementContext): BlockState {
        val blockState = ctx.world.getBlockState(ctx.blockPos)
        val block = blockState.block
        if (block is GildedChaliceBlock) {
            return blockState.cycle(CHALICES)
        }
        return super.getPlacementState(ctx)
    }

    override fun getRenderType(state: BlockState): BlockRenderType = BlockRenderType.INVISIBLE

    override fun onSyncedBlockEvent(state: BlockState, world: World, pos: BlockPos, type: Int, data: Int): Boolean {
        super.onSyncedBlockEvent(state, world, pos, type, data)
        val blockEntity = world.getBlockEntity(pos)
        return blockEntity?.onSyncedBlockEvent(type, data) ?: false
    }

    override fun createBlockEntity(pos: BlockPos, state: BlockState): BlockEntity? =
        StackedChaliceBlockEntity(pos, state)

    override fun onStateReplaced(
        state: BlockState, world: World, pos: BlockPos, newState: BlockState, moved: Boolean,
    ) {
        if (!state.isOf(newState.block)) {
            val chaliceBE = world.getBlockEntity(pos)
            if (chaliceBE is StackedChaliceBlockEntity) {
                ItemScatterer.spawn(world, pos, chaliceBE.chalices)
            }
        }
        super.onStateReplaced(state, world, pos, newState, moved)
    }

    companion object {
        fun addChalice(
            stack: ItemStack, state: BlockState, world: World, pos: BlockPos, player: PlayerEntity,
        ): ItemInteractionResult? {
            if (state.block !is GildedChaliceBlock) return null

            val chaliceCount = state.get(CHALICES)
            if (chaliceCount > 3) return null

            val item = stack.item
            if (item !is BlockItem) return null
            if (item.block !is GildedChaliceBlock) return null

            val chaliceBE = world.getBlockEntity(pos)
            if (chaliceBE !is StackedChaliceBlockEntity) return null

            world.setBlockState(pos, state.with(CHALICES, chaliceCount + 1))

            chaliceBE.chalices[chaliceCount] = stack.copyWithCount(1)
            chaliceBE.markDirty()
            if (!player.isCreative) stack.decrement(1)
            world.playSound(pos, state.soundGroup.placeSound, SoundCategory.BLOCKS, 1f, 1f, false)

            return ItemInteractionResult.success(world.isClient)
        }

        fun tryMakeFromBlock(
            stack: ItemStack, existingState: BlockState, world: World, pos: BlockPos, player: PlayerEntity,
        ): ItemInteractionResult? {
            if (existingState.block !is GildedChaliceBlock) return null

            val chaliceCount = existingState.get(CHALICES)
            if (chaliceCount > 3) return null

            val item = stack.item
            if (item !is BlockItem) return null
            if (item.block !is GildedChaliceBlock) return null
            if (existingState.isOf(item.block)) return null

            val oldStack = existingState.block.asItem().defaultStack
            var newState = DuskBlocks.STACKED_CHALICE.getStateWithProperties(existingState)
            newState = newState.with(CHALICES, chaliceCount + 1)
            world.setBlockState(pos, newState)

            val chaliceBE = world.getBlockEntity(pos)
            if (chaliceBE !is StackedChaliceBlockEntity) return null

            for (index in 0 until chaliceCount) {
                chaliceBE.chalices[index] = oldStack.copy()
            }

            chaliceBE.chalices[chaliceCount] = stack.copyWithCount(1)
            chaliceBE.markDirty()
            if (!player.isCreative) stack.decrement(1)
            world.playSound(pos, newState.soundGroup.placeSound, SoundCategory.BLOCKS, 1f, 1f, false)


            return ItemInteractionResult.success(world.isClient)
        }
    }
}