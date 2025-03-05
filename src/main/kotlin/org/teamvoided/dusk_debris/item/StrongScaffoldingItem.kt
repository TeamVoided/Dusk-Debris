package org.teamvoided.dusk_debris.item

import net.minecraft.block.Block
import net.minecraft.item.BlockItem
import net.minecraft.item.ItemPlacementContext
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import net.minecraft.util.math.Direction

class StrongScaffoldingItem(block: Block, settings: Settings) : BlockItem(block, settings) {
    override fun getPlacementContext(context: ItemPlacementContext): ItemPlacementContext? {
        val blockPos = context.blockPos
        val world = context.world
        var blockState = world.getBlockState(blockPos)
        if (!blockState.isOf(this.block)) {
            return super.getPlacementContext(context)
        } else {
            val direction = if (context.shouldCancelInteraction()) {
                if (context.hitsInsideBlock()) context.side.opposite
                else context.side
            } else {
                if (context.side == Direction.UP) context.playerFacing
                else Direction.UP
            }

            var looper = 0
            val mutable = blockPos.mutableCopy().move(direction)

            while (looper < 7) {
                if (!world.isClient && !world.isInBuildLimit(mutable)) {
                    val playerEntity = context.player
                    val j = world.topY
                    if (playerEntity is ServerPlayerEntity && mutable.y >= j) {
                        playerEntity.sendSystemMessage(
                            Text.translatable("build.tooHigh", *arrayOf<Any>(j - 1)).formatted(Formatting.RED), true
                        )
                    }
                    break
                }

                blockState = world.getBlockState(mutable)
                if (!blockState.isOf(this.block)) {
                    if (blockState.canReplace(context)) {
                        return ItemPlacementContext.offset(context, mutable, direction)
                    }
                    break
                }

                mutable.move(direction)
                if (direction.axis.isHorizontal) {
                    ++looper
                }
            }

            return null
        }
    }

    override fun checkStatePlacement(): Boolean = false
}
