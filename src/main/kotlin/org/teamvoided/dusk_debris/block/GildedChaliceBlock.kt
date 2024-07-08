package org.teamvoided.dusk_debris.block

import com.mojang.serialization.MapCodec
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.ShapeContext
import net.minecraft.state.StateManager
import net.minecraft.state.property.BooleanProperty
import net.minecraft.state.property.IntProperty
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.shape.VoxelShape
import net.minecraft.util.shape.VoxelShapes
import net.minecraft.world.BlockView
import org.teamvoided.dusk_debris.util.rotate

class GildedChaliceBlock(settings: Settings) : MysteriousVesselBlock(settings) {


    init {
        this.defaultState =
            (stateManager.defaultState)
                .with(FACING, Direction.NORTH)
                .with(CHALICES, 1)
                .with(WATERLOGGED, false)
    }

    override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) {
        super.appendProperties(builder)
        builder.add(CHALICES)
    }

    override fun getOutlineShape(
        state: BlockState,
        world: BlockView,
        pos: BlockPos,
        context: ShapeContext
    ): VoxelShape {
        val rotations = when (state.get(FACING)) {
            Direction.NORTH -> 0
            Direction.SOUTH -> 2
            Direction.WEST -> 3
            Direction.EAST -> 1
            else -> 0
        }
        return (when (state.get(CHALICES)) {
            1 -> CHALICES_1
            2 -> CHALICES_2
            3 -> CHALICES_3
            4 -> CHALICES_4
            else -> CHALICES_1
        }).rotate(rotations)
    }

    companion object {
        val CODEC: MapCodec<GildedChaliceBlock> = createCodec { settings: Settings ->
            GildedChaliceBlock(
                settings
            )
        }
        val CHALICES_1: VoxelShape = VoxelShapes.union(
            createCuboidShape(6.0, 0.0, 6.0, 10.0, 9.0, 10.0)
        )
        val CHALICES_2: VoxelShape = VoxelShapes.union(
            createCuboidShape(6.0, 0.0, 10.0, 10.0, 9.0, 14.0),
            createCuboidShape(6.0, 0.0, 2.0, 10.0, 9.0, 6.0)
        )
        val CHALICES_3: VoxelShape = VoxelShapes.union(
            createCuboidShape(6.0, 0.0, 6.0, 10.0, 9.0, 10.0)
        )
        val CHALICES_4: VoxelShape = VoxelShapes.union(
            createCuboidShape(6.0, 0.0, 6.0, 10.0, 9.0, 10.0)
        )
        val CHALICES: IntProperty = IntProperty.of("chalices", 1, 4);
    }
}