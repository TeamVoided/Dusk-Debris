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

class PerculiarRelicBlock(settings: Settings) : MysteriousVesselBlock(settings) {

    init {
        this.defaultState =
            (stateManager.defaultState)
                .with(FACING, Direction.NORTH)
                .with(WATERLOGGED, false)
    }

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
        val FACING: DirectionProperty = HorizontalFacingBlock.FACING
        val WATERLOGGED: BooleanProperty = Properties.WATERLOGGED
    }
}