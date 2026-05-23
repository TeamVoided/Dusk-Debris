package org.teamvoided.dusk_debris.block.sot

import com.mojang.serialization.MapCodec
import net.minecraft.core.BlockPos
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.shapes.CollisionContext
import net.minecraft.world.phys.shapes.Shapes
import net.minecraft.world.phys.shapes.VoxelShape

class RoyalCrownBlock(settings: Properties) : MysteriousVesselBlock(settings) {

    override fun getShape(
        state: BlockState,
        world: BlockGetter,
        pos: BlockPos,
        context: CollisionContext
    ): VoxelShape {
        return SHAPE
    }


    companion object {
        val CODEC: MapCodec<RoyalCrownBlock> = simpleCodec { settings: Properties ->
            RoyalCrownBlock(
                settings
            )
        }
        val SHAPE: VoxelShape = Shapes.or(
            box(4.0, 0.0, 4.0, 12.0, 4.0, 12.0)
        )
    }
}