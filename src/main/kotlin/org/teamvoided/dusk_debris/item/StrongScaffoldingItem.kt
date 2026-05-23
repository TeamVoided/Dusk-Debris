package org.teamvoided.dusk_debris.item

import net.minecraft.ChatFormatting
import net.minecraft.core.Direction
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.level.block.Block

class StrongScaffoldingItem(block: Block, settings: Properties) : BlockItem(block, settings) {
    override fun updatePlacementContext(context: BlockPlaceContext): BlockPlaceContext? {
        val blockPos = context.clickedPos
        val world = context.level
        var blockState = world.getBlockState(blockPos)
        if (!blockState.`is`(this.block)) {
            return super.updatePlacementContext(context)
        } else {
            val direction = if (context.isSecondaryUseActive) {
                if (context.isInside) context.clickedFace.opposite
                else context.clickedFace
            } else {
                if (context.clickedFace == Direction.UP) context.horizontalDirection
                else Direction.UP
            }

            var looper = 0
            val mutable = blockPos.mutable().move(direction)

            while (looper < 7) {
                if (!world.isClientSide && !world.isInWorldBounds(mutable)) {
                    val playerEntity = context.player
                    val j = world.maxBuildHeight
                    if (playerEntity is ServerPlayer && mutable.y >= j) {
                        playerEntity.sendSystemMessage(
                            Component.translatable("build.tooHigh", *arrayOf<Any>(j - 1)).withStyle(ChatFormatting.RED), true
                        )
                    }
                    break
                }

                blockState = world.getBlockState(mutable)
                if (!blockState.`is`(this.block)) {
                    if (blockState.canBeReplaced(context)) {
                        return BlockPlaceContext.at(context, mutable, direction)
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

    override fun mustSurvive(): Boolean = false
}
