package org.teamvoided.dusk_debris.block

import com.mojang.serialization.MapCodec
import net.minecraft.block.*
import net.minecraft.fluid.FluidState
import net.minecraft.fluid.Fluids
import net.minecraft.item.ItemPlacementContext
import net.minecraft.state.StateManager
import net.minecraft.state.property.BooleanProperty
import net.minecraft.state.property.DirectionProperty
import net.minecraft.state.property.Properties
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.shape.VoxelShape
import net.minecraft.util.shape.VoxelShapes
import net.minecraft.world.*

class RoyalCrownBlock(settings: Settings) : MysteriousVesselBlock(settings) {

    override fun getOutlineShape(
        state: BlockState,
        world: BlockView,
        pos: BlockPos,
        context: ShapeContext
    ): VoxelShape {
        return SHAPE
    }


    companion object {
        val CODEC: MapCodec<RoyalCrownBlock> = createCodec { settings: Settings ->
            RoyalCrownBlock(
                settings
            )
        }
        val SHAPE: VoxelShape = VoxelShapes.union(
            createCuboidShape(4.0, 0.0, 4.0, 12.0, 4.0, 12.0)
        )
    }
}