package org.teamvoided.dusk_debris.block.sot

import com.mojang.serialization.MapCodec
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.server.level.ServerLevel
import net.minecraft.util.RandomSource
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.LevelAccessor
import net.minecraft.world.level.LevelReader
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.world.level.block.state.properties.IntegerProperty
import net.minecraft.world.phys.shapes.CollisionContext
import net.minecraft.world.phys.shapes.Shapes
import net.minecraft.world.phys.shapes.VoxelShape
import org.teamvoided.dusk_debris.util.rotate

class CoinStackBlock(settings: Properties) : MysteriousVesselBlock(settings) {
    init {
        this.registerDefaultState(
            (stateDefinition.any())
                .setValue(FACING, Direction.NORTH)
                .setValue(LAYERS, 1)
                .setValue(WATERLOGGED, false)
        )
    }

    override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block, BlockState>) {
        super.createBlockStateDefinition(builder)
        builder.add(LAYERS)
    }

    override fun canSurvive(state: BlockState, world: LevelReader, pos: BlockPos): Boolean {
        val blockStateDown = world.getBlockState(pos.below())
        if (blockStateDown.`is`(this) && blockStateDown.getValue(LAYERS) == 8) {
            return true
        }
        return super.canSurvive(state, world, pos)
    }

    override fun getStateForPlacement(ctx: BlockPlaceContext): BlockState {
        val blockState = ctx.level.getBlockState(ctx.clickedPos)
        val blockStateDown = ctx.level.getBlockState(ctx.clickedPos.below())
        if (blockState.`is`(this)) {
            return blockState.cycle(LAYERS)
        } else if (blockStateDown.`is`(this)) {
            return super.getStateForPlacement(ctx).setValue(FACING, blockStateDown.getValue(FACING))
        }
        return super.getStateForPlacement(ctx)
    }

    override fun getShape(
        state: BlockState,
        world: BlockGetter,
        pos: BlockPos,
        context: CollisionContext
    ): VoxelShape {
        val rotations = when (state.getValue(FACING)) {
            Direction.NORTH -> 0
            Direction.SOUTH -> 2
            Direction.WEST -> 3
            Direction.EAST -> 1
            else -> 0
        }
        return (when (state.getValue(LAYERS)) {
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

    override fun canBeReplaced(state: BlockState, context: BlockPlaceContext): Boolean {
        if (!context.isSecondaryUseActive && context.itemInHand.item === asItem() && state.getValue(LAYERS) < 8) {
            return true
        }
        return super.canBeReplaced(state, context)
    }

    override fun updateShape(
        state: BlockState,
        direction: Direction,
        neighborState: BlockState,
        world: LevelAccessor,
        pos: BlockPos,
        neighborPos: BlockPos
    ): BlockState {
        if (!state.canSurvive(world, pos)) {
            world.scheduleTick(pos, this, 1)
        }
        return super.updateShape(state, direction, neighborState, world, pos, neighborPos)
    }

    override fun tick(state: BlockState, world: ServerLevel, pos: BlockPos?, random: RandomSource?) {
        if (!state.canSurvive(world, pos)) {
            world.destroyBlock(pos, true)
        }
    }

    companion object {
        val CODEC: MapCodec<CoinStackBlock> = simpleCodec { settings: Properties ->
            CoinStackBlock(
                settings
            )
        }
        val COINS_1: VoxelShape = coinShape(1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0)
        val COINS_2: VoxelShape = coinShape(1.0, 2.0, 3.0, 2.0, 2.0, 1.0, 1.0)
        val COINS_3: VoxelShape = coinShape(3.0, 2.0, 5.0, 4.0, 3.0, 2.0, 1.0)
        val COINS_4: VoxelShape = coinShape(4.0, 5.0, 6.0, 7.0, 5.0, 4.0, 3.0)
        val COINS_5: VoxelShape = coinShape(6.0, 5.0, 8.0, 7.0, 7.0, 6.0, 5.0)
        val COINS_6: VoxelShape = coinShape(9.0, 6.0, 10.0, 8.0, 11.0, 9.0, 7.0)
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
            return Shapes.or(
                box(2.0, 0.0, 1.0, 6.0, coinBottomRight, 5.0),
                box(8.0, 0.0, 2.0, 11.0, coinBottomCenter, 5.0),
                box(6.0, 0.0, 5.0, 9.0, coinTrueCenter, 8.0),
                box(9.0, 0.0, 5.0, 13.0, coinCenterRight, 9.0),
                box(3.0, 0.0, 8.0, 7.0, coinTopRight, 12.0),
                box(6.0, 0.0, 12.0, 9.0, coinTopCenter, 15.0),
                box(11.0, 0.0, 10.0, 14.0, coinTopLeft, 13.0)
            )
        }

        val LAYERS: IntegerProperty = BlockStateProperties.LAYERS
    }
}