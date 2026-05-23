package org.teamvoided.dusk_debris.block.sot

import com.mojang.serialization.MapCodec
import net.minecraft.core.BlockPos
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.shapes.CollisionContext
import net.minecraft.world.phys.shapes.Shapes
import net.minecraft.world.phys.shapes.VoxelShape

class PerculiarRelicBlock(settings: Properties) : MysteriousVesselBlock(settings) {
    override fun getShape(
        state: BlockState,
        world: BlockGetter,
        pos: BlockPos,
        context: CollisionContext
    ): VoxelShape {
        return SHAPE
    }
    companion object {
        val CODEC: MapCodec<PerculiarRelicBlock> = simpleCodec { settings: Properties ->
            PerculiarRelicBlock(
                settings
            )
        }
        val SHAPE: VoxelShape = Shapes.or(
            box(5.5, 0.0, 5.5, 10.5, 9.0, 10.5)
        )
    }
}