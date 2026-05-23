package org.teamvoided.dusk_debris.block

import com.mojang.serialization.MapCodec
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.LevelAccessor
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.world.phys.shapes.CollisionContext
import net.minecraft.world.phys.shapes.Shapes
import net.minecraft.world.phys.shapes.VoxelShape

open class ChainedPlanterBlock(settings: Properties) : WaterloggableBlock(settings) {
    public override fun codec(): MapCodec<ChainedPlanterBlock> {
        return CODEC
    }

    init {
        this.registerDefaultState(
            stateDefinition.any()
                .setValue(BlockStateProperties.HANGING, false)
        )
    }

    override fun getShape(
        state: BlockState,
        world: BlockGetter,
        pos: BlockPos,
        context: CollisionContext
    ): VoxelShape = SHAPE

    override fun getStateForPlacement(ctx: BlockPlaceContext): BlockState =
        super.getStateForPlacement(ctx).setValue(BlockStateProperties.HANGING, shouldHang(ctx.level, ctx.clickedPos))

    override fun updateShape(
        state: BlockState,
        direction: Direction,
        neighborState: BlockState,
        world: LevelAccessor,
        pos: BlockPos,
        neighborPos: BlockPos
    ): BlockState = super.updateShape(
        state.setValue(BlockStateProperties.HANGING, shouldHang(world, pos)),
        direction,
        neighborState,
        world,
        pos,
        neighborPos
    )

    open fun shouldHang(world: LevelAccessor, pos: BlockPos): Boolean {
        return (canSupportCenter(world, pos.above(2), Direction.DOWN) &&
                !canSupportCenter(world, pos, Direction.UP))
        //val worldBlock = world.getBlockState(pos.up(2))
        //return (worldBlock.isIn(ConventionalBlockTags.CHAINS) &&
        //        !(worldBlock.contains(Properties.AXIS) && worldBlock.get(Properties.AXIS) != Direction.Axis.Y))
    }

    override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block, BlockState>) {
        builder.add(BlockStateProperties.HANGING)
        super.createBlockStateDefinition(builder)
    }

    companion object {
        val SHAPE: VoxelShape = Shapes.or(
            box(0.0, 4.0, 0.0, 16.0, 16.0, 16.0),
            box(3.0, 0.0, 3.0, 13.0, 4.0, 13.0)
        )

        val CODEC: MapCodec<ChainedPlanterBlock> = simpleCodec(::ChainedPlanterBlock)
    }
}