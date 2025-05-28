package org.teamvoided.dusk_debris.util

import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.loot.function.SetCountLootFunction
import net.minecraft.loot.provider.number.ConstantLootNumberProvider
import net.minecraft.loot.provider.number.UniformLootNumberProvider
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.MathHelper
import net.minecraft.util.math.Vec3d
import net.minecraft.util.math.Vec3i
import net.minecraft.world.StructureWorldAccess
import java.util.function.BiConsumer

object Utils {
    const val PI = MathHelper.PI
    const val DEG_TO_RAD = 0.017453292f
    const val RAD_TO_DEG = 57.295776f
    const val rotate30 = PI / 6f
    const val rotate45 = PI / 4f
    const val rotate60 = PI / 3f
    const val rotate90 = MathHelper.HALF_PI
    const val rotate120 = rotate90 + rotate30
    const val rotate135 = rotate90 + rotate45
    const val rotate150 = rotate90 + rotate60
    const val rotate180 = PI
    const val rotate210 = rotate180 + rotate30
    const val rotate225 = rotate180 + rotate45
    const val rotate240 = rotate180 + rotate60
    const val rotate270 = PI * (3f / 2f)
    const val rotate300 = rotate270 + rotate30
    const val rotate315 = rotate270 + rotate45
    const val rotate330 = rotate270 + rotate60
    const val rotate360 = MathHelper.TAU

    fun setCount(x: Number, y: Number) = SetCountLootFunction.builder(uniformNum(x, y))

    fun uniformNum(x: Number, y: Number): UniformLootNumberProvider =
        UniformLootNumberProvider.create(x.toFloat(), y.toFloat())

    fun constantNum(x: Number): ConstantLootNumberProvider =
        ConstantLootNumberProvider.create(x.toFloat())

    fun StructureWorldAccess.placeDebug(pos: BlockPos, block: Int) =
        this.setBlockState(pos, getStateGlass(block), Block.NOTIFY_ALL)


    fun BiConsumer<BlockPos, BlockState>.placeDebug(pos: BlockPos, block: Int) = this.accept(pos, getStateGlass(block))

    fun Vec3i.vec3d(): Vec3d = Vec3d(this.x.toDouble(), this.y.toDouble(), this.z.toDouble())
    fun Vec3d.vec3i(): Vec3i = Vec3i(this.x.toInt(), this.y.toInt(), this.z.toInt())

    fun getStateGlass(block: Int): BlockState {
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

    fun getStateConcrete(block: Int): BlockState {
        return when (block) {
            0 -> Blocks.STONE
            1 -> Blocks.WHITE_CONCRETE
            2 -> Blocks.LIGHT_GRAY_CONCRETE
            3 -> Blocks.GRAY_CONCRETE
            4 -> Blocks.BLACK_CONCRETE
            5 -> Blocks.BROWN_CONCRETE
            6 -> Blocks.RED_CONCRETE
            7 -> Blocks.ORANGE_CONCRETE
            8 -> Blocks.YELLOW_CONCRETE
            9 -> Blocks.LIME_CONCRETE
            10 -> Blocks.GREEN_CONCRETE
            11 -> Blocks.CYAN_CONCRETE
            12 -> Blocks.LIGHT_BLUE_CONCRETE
            13 -> Blocks.BLUE_CONCRETE
            14 -> Blocks.PURPLE_CONCRETE
            15 -> Blocks.MAGENTA_CONCRETE
            16 -> Blocks.PINK_CONCRETE
            else -> Blocks.DEEPSLATE
        }.defaultState
    }
}