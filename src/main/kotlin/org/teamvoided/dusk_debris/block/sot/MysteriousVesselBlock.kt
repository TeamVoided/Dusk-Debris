package org.teamvoided.dusk_debris.block.sot

import com.mojang.serialization.MapCodec
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.LevelAccessor
import net.minecraft.world.level.LevelReader
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.HorizontalDirectionalBlock
import net.minecraft.world.level.block.SimpleWaterloggedBlock
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.world.level.block.state.properties.BooleanProperty
import net.minecraft.world.level.block.state.properties.DirectionProperty
import net.minecraft.world.level.material.FluidState
import net.minecraft.world.level.material.Fluids
import net.minecraft.world.phys.shapes.CollisionContext
import net.minecraft.world.phys.shapes.Shapes
import net.minecraft.world.phys.shapes.VoxelShape

open class MysteriousVesselBlock(settings: Properties) : HorizontalDirectionalBlock(settings), SimpleWaterloggedBlock {
    override fun codec(): MapCodec<out HorizontalDirectionalBlock> {
        return CODEC
    }

    init {
        this.registerDefaultState(
            (stateDefinition.any())
                .setValue(FACING, Direction.NORTH)
                .setValue(WATERLOGGED, false)
        )
    }


    override fun updateShape(
        state: BlockState,
        direction: Direction,
        neighborState: BlockState,
        world: LevelAccessor,
        pos: BlockPos,
        neighborPos: BlockPos
    ): BlockState {
        if (state.getValue(WATERLOGGED)) {
            world.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(world))
        }
        return super.updateShape(
            state,
            direction,
            neighborState,
            world,
            pos,
            neighborPos
        )
    }

    override fun getStateForPlacement(ctx: BlockPlaceContext): BlockState {
        val waterlog = ctx.level.getFluidState(ctx.clickedPos).type === Fluids.WATER
        return defaultBlockState()
            .setValue(FACING, ctx.horizontalDirection.opposite)
            .setValue(WATERLOGGED, waterlog)
    }

    override fun getFluidState(state: BlockState): FluidState {
        return if (state.getValue(WATERLOGGED)) Fluids.WATER.getSource(false)
        else super.getFluidState(state)
    }

    override fun canSurvive(state: BlockState, world: LevelReader, pos: BlockPos): Boolean {
        return canSupportCenter(world, pos.relative(Direction.DOWN), Direction.DOWN.opposite)
    }

    override fun getShape(
        state: BlockState,
        world: BlockGetter,
        pos: BlockPos,
        context: CollisionContext
    ): VoxelShape {
        return SHAPE
    }

    override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block, BlockState>) {
        builder.add(FACING, WATERLOGGED)
    }


    companion object {
        val CODEC: MapCodec<MysteriousVesselBlock> = simpleCodec { settings: Properties ->
            MysteriousVesselBlock(
                settings
            )
        }
        val SHAPE: VoxelShape = Shapes.or(
            box(5.0, 0.0, 5.0, 11.0, 8.0, 11.0),
            box(6.0, 8.0, 6.0, 10.0, 10.0, 10.0)
        )
        val FACING: DirectionProperty = HorizontalDirectionalBlock.FACING
        val WATERLOGGED: BooleanProperty = BlockStateProperties.WATERLOGGED
    }
}