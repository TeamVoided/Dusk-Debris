//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//
package net.minecraft.block

import com.mojang.serialization.MapCodec
import net.minecraft.entity.ai.pathing.NavigationType
import net.minecraft.fluid.FluidState
import net.minecraft.fluid.Fluids
import net.minecraft.item.ItemPlacementContext
import net.minecraft.state.StateManager
import net.minecraft.state.property.BooleanProperty
import net.minecraft.state.property.Properties
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.shape.VoxelShape
import net.minecraft.util.shape.VoxelShapes
import net.minecraft.world.BlockView
import net.minecraft.world.WorldAccess
import net.minecraft.world.WorldView

class LanternBlock(settings: Settings?) : Block(settings), Waterloggable {
    public override fun getCodec(): MapCodec<LanternBlock> {
        return CODEC
    }

    override fun getPlacementState(ctx: ItemPlacementContext): BlockState? {
        val fluidState = ctx.world.getFluidState(ctx.blockPos)
        val var3 = ctx.placementDirections
        val var4 = var3.size

        for (var5 in 0 until var4) {
            val direction = var3[var5]
            if (direction.axis === Direction.Axis.Y) {
                val blockState =
                    defaultState.with(HANGING, direction == Direction.UP) as BlockState
                if (blockState.canPlaceAt(ctx.world, ctx.blockPos)) {
                    return blockState.with(WATERLOGGED, fluidState.fluid === Fluids.WATER) as BlockState
                }
            }
        }

        return null
    }

    override fun getOutlineShape(
        state: BlockState,
        world: BlockView,
        pos: BlockPos,
        context: ShapeContext
    ): VoxelShape {
        return if (state.get(HANGING)) HANGING_SHAPE else STANDING_SHAPE
    }

    override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) {
        builder.add(HANGING, WATERLOGGED)
    }

    override fun canPlaceAt(state: BlockState, world: WorldView, pos: BlockPos): Boolean {
        val direction = attachedDirection(state).opposite
        return sideCoversSmallSquare(world, pos.offset(direction), direction.opposite)
    }

    override fun getStateForNeighborUpdate(
        state: BlockState,
        direction: Direction,
        neighborState: BlockState,
        world: WorldAccess,
        pos: BlockPos,
        neighborPos: BlockPos
    ): BlockState {
        if (state.get(WATERLOGGED) as Boolean) {
            world.scheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world))
        }

        return if (attachedDirection(state).opposite == direction && !state.canPlaceAt(
                world,
                pos
            )
        ) Blocks.AIR.defaultState else super.getStateForNeighborUpdate(
            state,
            direction,
            neighborState,
            world,
            pos,
            neighborPos
        )
    }

    override fun getFluidState(state: BlockState): FluidState {
        return if (state.get(WATERLOGGED) as Boolean) Fluids.WATER.getStill(false) else super.getFluidState(state)
    }

    override fun canPathfindThrough(state: BlockState, navigationType: NavigationType): Boolean {
        return false
    }

    init {
        this.defaultState = ((stateManager.defaultState as BlockState).with(HANGING, false) as BlockState).with(
            WATERLOGGED, false
        ) as BlockState
    }

    companion object {
        val CODEC: MapCodec<LanternBlock> = createCodec { settings: Settings? ->
            LanternBlock(
                settings
            )
        }
        val HANGING: BooleanProperty = Properties.HANGING
        val WATERLOGGED: BooleanProperty = Properties.WATERLOGGED
        protected val STANDING_SHAPE: VoxelShape =
            VoxelShapes.union(
                createCuboidShape(5.0, 0.0, 5.0, 11.0, 7.0, 11.0),
                createCuboidShape(6.0, 7.0, 6.0, 10.0, 9.0, 10.0)
            )
        protected val HANGING_SHAPE: VoxelShape =
            VoxelShapes.union(
                createCuboidShape(5.0, 1.0, 5.0, 11.0, 8.0, 11.0),
                createCuboidShape(6.0, 8.0, 6.0, 10.0, 10.0, 10.0)
            )

        protected fun attachedDirection(state: BlockState): Direction {
            return if (state.get(HANGING) as Boolean) Direction.DOWN else Direction.UP
        }
    }
}