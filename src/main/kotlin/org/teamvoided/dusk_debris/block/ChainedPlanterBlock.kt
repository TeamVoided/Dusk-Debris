package org.teamvoided.dusk_debris.block

import com.mojang.serialization.MapCodec
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalBlockTags
import net.minecraft.block.*
import net.minecraft.item.ItemPlacementContext
import net.minecraft.state.StateManager
import net.minecraft.state.property.Properties
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.shape.VoxelShape
import net.minecraft.util.shape.VoxelShapes
import net.minecraft.world.BlockView
import net.minecraft.world.WorldAccess

open class ChainedPlanterBlock(settings: Settings) : WaterloggableBlock(settings) {
    public override fun getCodec(): MapCodec<ChainedPlanterBlock> {
        return CODEC
    }

    init {
        this.defaultState = stateManager.defaultState
            .with(Properties.HANGING, false)
    }

    override fun getOutlineShape(
        state: BlockState,
        world: BlockView,
        pos: BlockPos,
        context: ShapeContext
    ): VoxelShape = SHAPE

    override fun getPlacementState(ctx: ItemPlacementContext): BlockState =
        super.getPlacementState(ctx).with(Properties.HANGING, shouldHang(ctx.world, ctx.blockPos))

    override fun getStateForNeighborUpdate(
        state: BlockState,
        direction: Direction,
        neighborState: BlockState,
        world: WorldAccess,
        pos: BlockPos,
        neighborPos: BlockPos
    ): BlockState = super.getStateForNeighborUpdate(
        state.with(Properties.HANGING, shouldHang(world, pos)),
        direction,
        neighborState,
        world,
        pos,
        neighborPos
    )

    open fun shouldHang(world: WorldAccess, pos: BlockPos): Boolean {
        return (sideCoversSmallSquare(world, pos.up(2), Direction.DOWN) &&
                !sideCoversSmallSquare(world, pos, Direction.UP))
        //val worldBlock = world.getBlockState(pos.up(2))
        //return (worldBlock.isIn(ConventionalBlockTags.CHAINS) &&
        //        !(worldBlock.contains(Properties.AXIS) && worldBlock.get(Properties.AXIS) != Direction.Axis.Y))
    }

    override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) {
        builder.add(Properties.HANGING)
        super.appendProperties(builder)
    }

    companion object {
        val SHAPE: VoxelShape = VoxelShapes.union(
            createCuboidShape(0.0, 4.0, 0.0, 16.0, 16.0, 16.0),
            createCuboidShape(3.0, 0.0, 3.0, 13.0, 4.0, 13.0)
        )

        val CODEC: MapCodec<ChainedPlanterBlock> = createCodec(::ChainedPlanterBlock)
    }
}