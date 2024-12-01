package org.teamvoided.dusk_debris.block

import com.mojang.serialization.MapCodec
import net.minecraft.block.*
import net.minecraft.util.math.BlockPos
import net.minecraft.util.shape.VoxelShape
import net.minecraft.util.shape.VoxelShapes
import net.minecraft.world.*

class PerculiarRelicBlock(settings: Settings) : MysteriousVesselBlock(settings) {
    override fun getOutlineShape(
        state: BlockState,
        world: BlockView,
        pos: BlockPos,
        context: ShapeContext
    ): VoxelShape {
        return SHAPE
    }
    companion object {
        val CODEC: MapCodec<PerculiarRelicBlock> = createCodec { settings: Settings ->
            PerculiarRelicBlock(
                settings
            )
        }
        val SHAPE: VoxelShape = VoxelShapes.union(
            createCuboidShape(5.5, 0.0, 5.5, 10.5, 9.0, 10.5)
        )
    }
}