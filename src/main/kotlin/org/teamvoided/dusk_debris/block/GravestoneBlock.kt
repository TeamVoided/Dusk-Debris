package org.teamvoided.dusk_debris.block

import com.mojang.serialization.MapCodec
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.LevelAccessor
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.HorizontalDirectionalBlock
import net.minecraft.world.level.block.SimpleWaterloggedBlock
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.world.level.block.state.properties.BooleanProperty
import net.minecraft.world.level.material.FluidState
import net.minecraft.world.level.material.Fluids
import net.minecraft.world.level.pathfinder.PathComputationType
import net.minecraft.world.phys.shapes.CollisionContext
import net.minecraft.world.phys.shapes.VoxelShape
import org.teamvoided.dusk_debris.util.centerGravestoneShape
import org.teamvoided.dusk_debris.util.gravestoneShape
import org.teamvoided.dusk_debris.util.rotate

open class GravestoneBlock(val shape: VoxelShape, val centerShape: VoxelShape, settings: Properties) :
    HorizontalDirectionalBlock(settings), SimpleWaterloggedBlock {
    init {
        this.registerDefaultState(
            stateDefinition.any()
                .setValue(BlockStateProperties.WATERLOGGED, false)
                .setValue(CENTERED, true)
                .setValue(FACING, Direction.NORTH)
        )
    }

    public override fun codec(): MapCodec<GravestoneBlock> = CODEC
    override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block, BlockState>) {
        super.createBlockStateDefinition(builder)
        builder.add(BlockStateProperties.WATERLOGGED)
        builder.add(CENTERED)
        builder.add(FACING)
    }

    override fun updateShape(
        state: BlockState, direction: Direction, neighborState: BlockState,
        world: LevelAccessor, pos: BlockPos, neighborPos: BlockPos
    ): BlockState {
        if (state.getValue(BlockStateProperties.WATERLOGGED)) {
            world.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(world))
        }
        return super.updateShape(state, direction, neighborState, world, pos, neighborPos)
    }

    override fun getFluidState(state: BlockState): FluidState {
        return if (state.getValue(BlockStateProperties.WATERLOGGED)) Fluids.WATER.getSource(false)
        else super.getFluidState(state)
    }

    override fun getStateForPlacement(ctx: BlockPlaceContext): BlockState {
        val fluidState = ctx.level.getFluidState(ctx.clickedPos)
        val player = ctx.player?.isShiftKeyDown == true
        return defaultBlockState()
            .setValue(BlockStateProperties.WATERLOGGED, fluidState.`is`(Fluids.WATER))
            .setValue(CENTERED, !player)
            .setValue(FACING, ctx.horizontalDirection.opposite)
    }

    override fun getShape(
        state: BlockState, world: BlockGetter, pos: BlockPos, context: CollisionContext
    ): VoxelShape {
        val rotations = state.getValue(FACING).get2DDataValue()
        val shape = if (state.getValue(CENTERED)) centerShape else shape
        return shape.rotate(rotations)
    }

    override fun isPathfindable(state: BlockState, navigationType: PathComputationType): Boolean = false

    companion object {
        val CENTERED: BooleanProperty = BooleanProperty.create("centered")
        val CODEC: MapCodec<GravestoneBlock> =
            simpleCodec { GravestoneBlock(gravestoneShape, centerGravestoneShape, it) }
    }
}