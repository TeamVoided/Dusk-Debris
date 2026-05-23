package org.teamvoided.dusk_debris.block.mixin

import net.minecraft.core.Direction
import net.minecraft.world.level.block.Block
import net.minecraft.world.phys.shapes.VoxelShape
import org.teamvoided.dusk_debris.util.rotate

object SignShapes {
    val WALL_SHAPE: VoxelShape = Block.box(0.0, 4.0, 0.0, 16.0, 12.0, 2.0)
    @JvmStatic
    fun getWallShape(facing: Direction): VoxelShape = WALL_SHAPE.rotate(facing.get2DDataValue())

//    private val post = Block.createCuboidShape(7.0, 0.0, 7.0, 9.0, 8.0, 9.0)
//    @JvmStatic
//    val DEFAULT: VoxelShape = VoxelShapes.union(
//        post,
//        Block.createCuboidShape(7.0, 8.0, 0.0, 9.0, 16.0, 16.0)
//    )
//    @JvmStatic
//    val SHAPE: VoxelShape = Block.createCuboidShape(4.0, 0.0, 4.0, 12.0, 16.0, 12.0)

//    val ROTATION_1 = VoxelShapes.union(
//        post,
//        Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 2.0, 16.0)
//    )
//    val ROTATION_2 = VoxelShapes.union(
//        post,
//        Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 2.0, 16.0)
//    )
//    val ROTATION_3 = VoxelShapes.union(
//        post,
//        Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 2.0, 16.0)
//    )
}