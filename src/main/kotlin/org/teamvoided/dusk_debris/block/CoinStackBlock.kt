package org.teamvoided.dusk_debris.block

import com.mojang.serialization.MapCodec
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.ShapeContext
import net.minecraft.item.ItemPlacementContext
import net.minecraft.state.StateManager
import net.minecraft.state.property.IntProperty
import net.minecraft.state.property.Properties
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.shape.VoxelShape
import net.minecraft.util.shape.VoxelShapes
import net.minecraft.world.BlockView
import org.teamvoided.dusk_debris.util.rotate

class CoinStackBlock(settings: Settings) : MysteriousVesselBlock(settings) {
    init {
        this.defaultState =
            (stateManager.defaultState)
                .with(FACING, Direction.NORTH)
                .with(LAYERS, 1)
                .with(WATERLOGGED, false)
    }

    override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) {
        super.appendProperties(builder)
        builder.add(LAYERS)
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
        return (when (state.get(LAYERS)) {
            1 -> COINS_1
            2 -> COINS_2
            3 -> COINS_3
            4 -> COINS_4
            5 -> COINS_5
            6 -> COINS_6
            7 -> COINS_7
            8 -> COINS_8
            else -> COINS_1
        }).rotate(rotations)
    }

    override fun canReplace(state: BlockState, context: ItemPlacementContext): Boolean {
        if (!context.shouldCancelInteraction() && context.stack.item === asItem() && state.get(LAYERS) < 8) {
            return true
        }
        return super.canReplace(state, context)
    }

    override fun getPlacementState(ctx: ItemPlacementContext): BlockState {
        val blockState = ctx.world.getBlockState(ctx.blockPos)
        if (blockState.isOf(this)) {
            return blockState.cycle(LAYERS)
        }
        return super.getPlacementState(ctx)
    }

    companion object {
        val CODEC: MapCodec<CoinStackBlock> = createCodec { settings: Settings ->
            CoinStackBlock(
                settings
            )
        }
        val COINS_1: VoxelShape = coinShape(1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0)
        val COINS_2: VoxelShape = coinShape(1.0, 2.0, 3.0, 2.0, 2.0, 1.0, 1.0)
        val COINS_3: VoxelShape = coinShape(3.0, 2.0, 5.0, 4.0, 3.0, 2.0, 1.0)
        val COINS_4: VoxelShape = coinShape(4.0, 5.0, 6.0, 7.0, 5.0, 4.0, 3.0)
        val COINS_5: VoxelShape = coinShape(6.0, 5.0, 8.0, 7.0, 7.0, 6.0, 5.0)
        val COINS_6: VoxelShape = coinShape(8.0, 6.0, 10.0, 8.0, 11.0, 9.0, 7.0)
        val COINS_7: VoxelShape = coinShape(12.0, 10.0, 13.0, 14.0, 14.0, 13.0, 11.0)
        val COINS_8: VoxelShape = coinShape(16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0)

        //named after the placement, not the texture
        fun coinShape(
            coinBottomRight: Double,
            coinBottomCenter: Double,
            coinTrueCenter: Double,
            coinCenterRight: Double,
            coinTopRight: Double,
            coinTopCenter: Double,
            coinTopLeft: Double
        ): VoxelShape {
            return VoxelShapes.union(
                createCuboidShape(2.0, 0.0, 1.0, 6.0, coinBottomRight, 5.0),
                createCuboidShape(8.0, 0.0, 2.0, 11.0, coinBottomCenter, 5.0),
                createCuboidShape(6.0, 0.0, 5.0, 9.0, coinTrueCenter, 8.0),
                createCuboidShape(9.0, 0.0, 5.0, 13.0, coinCenterRight, 9.0),
                createCuboidShape(3.0, 0.0, 8.0, 7.0, coinTopRight, 12.0),
                createCuboidShape(6.0, 0.0, 12.0, 9.0, coinTopCenter, 15.0),
                createCuboidShape(11.0, 0.0, 10.0, 14.0, coinTopLeft, 13.0)
            )
        }

        val LAYERS: IntProperty = Properties.LAYERS
    }
}