package org.teamvoided.dusk_debris.util

import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.loot.function.SetCountLootFunction
import net.minecraft.loot.provider.number.ConstantLootNumberProvider
import net.minecraft.loot.provider.number.UniformLootNumberProvider
import net.minecraft.util.math.BlockPos
import net.minecraft.world.StructureWorldAccess
import java.util.function.BiConsumer

object Utils {
    const val pi = 3.1415927f
    const val degToRad = 0.017453292f
    const val radToDeg = 57.29578f
    const val rotate45 = 0.785f
    const val rotate90 = 1.571f
    const val rotate135 = 2.356f
    const val rotate180 = 3.142f
    const val rotate225 = 3.927f
    const val rotate270 = 4.712f
    const val rotate315 = 5.498f
    const val rotate360 = 6.284f
    fun setCount(x: Number, y: Number) = SetCountLootFunction.builder(uniformNum(x, y))

    fun uniformNum(x: Number, y: Number): UniformLootNumberProvider =
        UniformLootNumberProvider.create(x.toFloat(), y.toFloat())

    fun constantNum(x: Number): ConstantLootNumberProvider =
        ConstantLootNumberProvider.create(x.toFloat())

    fun StructureWorldAccess.placeDebug(pos: BlockPos, block: Int) =
        this.setBlockState(pos, getState(block), Block.NOTIFY_ALL)


    fun BiConsumer<BlockPos, BlockState>.placeDebug(pos: BlockPos, block: Int) = this.accept(pos, getState(block))


    fun getState(block: Int): BlockState {
        return when (block) {
            0 -> Blocks.GLASS
            1 -> Blocks.WHITE_STAINED_GLASS
            2 -> Blocks.LIGHT_GRAY_STAINED_GLASS
            3 -> Blocks.GRAY_STAINED_GLASS
            4 -> Blocks.BLACK_STAINED_GLASS
            5 -> Blocks.BROWN_STAINED_GLASS
            6 -> Blocks.RED_STAINED_GLASS
            7 -> Blocks.ORANGE_STAINED_GLASS
            8 -> Blocks.YELLOW_STAINED_GLASS
            9 -> Blocks.LIME_STAINED_GLASS
            10 -> Blocks.GREEN_STAINED_GLASS
            11 -> Blocks.CYAN_STAINED_GLASS
            12 -> Blocks.LIGHT_BLUE_STAINED_GLASS
            13 -> Blocks.BLUE_STAINED_GLASS
            14 -> Blocks.PURPLE_STAINED_GLASS
            15 -> Blocks.MAGENTA_STAINED_GLASS
            16 -> Blocks.PINK_STAINED_GLASS
            else -> Blocks.TINTED_GLASS
        }.defaultState
    }
}