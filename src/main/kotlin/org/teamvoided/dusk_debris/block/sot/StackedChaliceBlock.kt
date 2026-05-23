package org.teamvoided.dusk_debris.block.sot

import net.minecraft.core.BlockPos
import net.minecraft.sounds.SoundSource
import net.minecraft.world.Containers
import net.minecraft.world.InteractionHand
import net.minecraft.world.ItemInteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.EntityBlock
import net.minecraft.world.level.block.RenderShape
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.BlockHitResult
import org.teamvoided.dusk_debris.block.sot.entity.StackedChaliceBlockEntity
import org.teamvoided.dusk_debris.init.DuskBlocks

open class StackedChaliceBlock(settings: Properties) : GildedChaliceBlock(settings), EntityBlock {
    override fun useItemOn(
        stack: ItemStack, state: BlockState, world: Level, pos: BlockPos,
        player: Player, hand: InteractionHand, hitResult: BlockHitResult,
    ): ItemInteractionResult {
        val result = addChalice(stack, state, world, pos, player)
        if (result != null) return result
        return super.useItemOn(stack, state, world, pos, player, hand, hitResult)
    }

    override fun getStateForPlacement(ctx: BlockPlaceContext): BlockState {
        val blockState = ctx.level.getBlockState(ctx.clickedPos)
        val block = blockState.block
        if (block is GildedChaliceBlock) {
            return blockState.cycle(CHALICES)
        }
        return super.getStateForPlacement(ctx)
    }

    override fun getRenderShape(state: BlockState): RenderShape = RenderShape.INVISIBLE

    override fun triggerEvent(state: BlockState, world: Level, pos: BlockPos, type: Int, data: Int): Boolean {
        super.triggerEvent(state, world, pos, type, data)
        val blockEntity = world.getBlockEntity(pos)
        return blockEntity?.triggerEvent(type, data) ?: false
    }

    override fun newBlockEntity(pos: BlockPos, state: BlockState): BlockEntity? =
        StackedChaliceBlockEntity(pos, state)

    override fun onRemove(
        state: BlockState, world: Level, pos: BlockPos, newState: BlockState, moved: Boolean,
    ) {
        if (!state.`is`(newState.block)) {
            val chaliceBE = world.getBlockEntity(pos)
            if (chaliceBE is StackedChaliceBlockEntity) {
                Containers.dropContents(world, pos, chaliceBE.chalices)
            }
        }
        super.onRemove(state, world, pos, newState, moved)
    }

    companion object {
        fun addChalice(
            stack: ItemStack, state: BlockState, world: Level, pos: BlockPos, player: Player,
        ): ItemInteractionResult? {
            if (state.block !is GildedChaliceBlock) return null

            val chaliceCount = state.getValue(CHALICES)
            if (chaliceCount > 3) return null

            val item = stack.item
            if (item !is BlockItem) return null
            if (item.block !is GildedChaliceBlock) return null

            val chaliceBE = world.getBlockEntity(pos)
            if (chaliceBE !is StackedChaliceBlockEntity) return null

            world.setBlockAndUpdate(pos, state.setValue(CHALICES, chaliceCount + 1))

            chaliceBE.chalices[chaliceCount] = stack.copyWithCount(1)
            chaliceBE.setChanged()
            if (!player.isCreative) stack.shrink(1)
            world.playLocalSound(pos, state.soundType.placeSound, SoundSource.BLOCKS, 1f, 1f, false)

            return ItemInteractionResult.sidedSuccess(world.isClientSide)
        }

        fun tryMakeFromBlock(
            stack: ItemStack, existingState: BlockState, world: Level, pos: BlockPos, player: Player,
        ): ItemInteractionResult? {
            if (existingState.block !is GildedChaliceBlock) return null

            val chaliceCount = existingState.getValue(CHALICES)
            if (chaliceCount > 3) return null

            val item = stack.item
            if (item !is BlockItem) return null
            if (item.block !is GildedChaliceBlock) return null
            if (existingState.`is`(item.block)) return null

            val oldStack = existingState.block.asItem().defaultInstance
            var newState = DuskBlocks.STACKED_CHALICE.withPropertiesOf(existingState)
            newState = newState.setValue(CHALICES, chaliceCount + 1)
            world.setBlockAndUpdate(pos, newState)

            val chaliceBE = world.getBlockEntity(pos)
            if (chaliceBE !is StackedChaliceBlockEntity) return null

            for (index in 0 until chaliceCount) {
                chaliceBE.chalices[index] = oldStack.copy()
            }

            chaliceBE.chalices[chaliceCount] = stack.copyWithCount(1)
            chaliceBE.setChanged()
            if (!player.isCreative) stack.shrink(1)
            world.playLocalSound(pos, newState.soundType.placeSound, SoundSource.BLOCKS, 1f, 1f, false)


            return ItemInteractionResult.sidedSuccess(world.isClientSide)
        }
    }
}