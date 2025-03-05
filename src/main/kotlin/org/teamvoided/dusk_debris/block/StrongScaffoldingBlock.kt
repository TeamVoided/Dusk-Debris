package org.teamvoided.dusk_debris.block

import com.mojang.serialization.MapCodec
import net.minecraft.block.*
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.fluid.FluidState
import net.minecraft.fluid.Fluids
import net.minecraft.item.ItemPlacementContext
import net.minecraft.server.world.ServerWorld
import net.minecraft.state.StateManager
import net.minecraft.state.property.BooleanProperty
import net.minecraft.state.property.Properties
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.random.RandomGenerator
import net.minecraft.util.shape.VoxelShape
import net.minecraft.util.shape.VoxelShapes
import net.minecraft.world.BlockView
import net.minecraft.world.World
import net.minecraft.world.WorldAccess

class StrongScaffoldingBlock(settings: Settings) : Block(settings), Waterloggable {
    public override fun getCodec(): MapCodec<StrongScaffoldingBlock> = CODEC

    init {
        this.defaultState = stateManager.defaultState
            .with(Properties.WATERLOGGED, false)
            .with(Properties.BOTTOM, false)
    }

    override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) {
        builder.add(Properties.WATERLOGGED, Properties.BOTTOM)
    }

    override fun getOutlineShape(
        state: BlockState,
        world: BlockView,
        pos: BlockPos,
        context: ShapeContext
    ): VoxelShape {
        return if (!context.isHolding(state.block.asItem())) {
            if (state.get(Properties.BOTTOM)) BOTTOM_OUTLINE_SHAPE else NORMAL_OUTLINE_SHAPE
        } else {
            VoxelShapes.fullCube()
        }
    }

    override fun getRaycastShape(state: BlockState, world: BlockView, pos: BlockPos): VoxelShape {
        return VoxelShapes.fullCube()
    }

    override fun canReplace(state: BlockState, context: ItemPlacementContext): Boolean {
        return context.stack.isOf(this.asItem())
    }

    override fun getPlacementState(ctx: ItemPlacementContext): BlockState? {
        val blockPos = ctx.blockPos
        val world = ctx.world
        return defaultState
            .with(Properties.WATERLOGGED, world.getFluidState(blockPos).fluid == Fluids.WATER)
            .with(Properties.BOTTOM, this.shouldHaveBottom(world, blockPos))
    }

    override fun onBreak(world: World, pos: BlockPos, state: BlockState, player: PlayerEntity): BlockState {
        if (player.mainHandStack.isOf(this.asItem()) && world.getBlockState(pos.up()).isOf(this)) {
            world.scheduleBlockTick(pos.up(), this, 1)
        }
        return super.onBreak(world, pos, state, player)
    }

    override fun getStateForNeighborUpdate(
        state: BlockState,
        direction: Direction,
        neighborState: BlockState,
        world: WorldAccess,
        pos: BlockPos,
        neighborPos: BlockPos
    ): BlockState {
        val supr = super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos)
        if (state.get(Properties.WATERLOGGED)) {
            world.scheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world))
        }

        if (direction == Direction.DOWN)
            return supr.with(Properties.BOTTOM, shouldHaveBottom(world, pos))

        return supr
    }

    override fun scheduledTick(state: BlockState, world: ServerWorld, pos: BlockPos, random: RandomGenerator) {
        Direction.entries.forEach {
            if (it != Direction.DOWN && world.getBlockState(pos.offset(it)).isOf(this)) {
                world.scheduleBlockTick(pos.offset(it), this, 1)
            }
        }
        world.breakBlock(pos, true)
    }

    override fun getCollisionShape(
        state: BlockState,
        world: BlockView,
        pos: BlockPos,
        context: ShapeContext
    ): VoxelShape {
        return if (context.isAbove(VoxelShapes.fullCube(), pos, true) && !context.isDescending) {
            NORMAL_OUTLINE_SHAPE
        } else if (state.get(Properties.BOTTOM) && context.isAbove(OUTLINE_SHAPE, pos, true))
            COLLISION_SHAPE
        else
            VoxelShapes.empty()

    }

    override fun getFluidState(state: BlockState): FluidState {
        return if (state.get(Properties.WATERLOGGED))
            Fluids.WATER.getStill(false)
        else
            super.getFluidState(state)
    }

    private fun shouldHaveBottom(world: BlockView, pos: BlockPos): Boolean {
        val downState = world.getBlockState(pos.down())
        val fullSquare = downState.isSideSolidFullSquare(world, pos.down(), Direction.UP)

        return !(downState.isOf(this) || fullSquare)
    }

    companion object {
        val CODEC: MapCodec<StrongScaffoldingBlock> = createCodec(::StrongScaffoldingBlock)
        private val COLLISION_SHAPE: VoxelShape = createCuboidShape(0.0, 0.0, 0.0, 16.0, 4.0, 16.0)
        private val OUTLINE_SHAPE: VoxelShape = VoxelShapes.fullCube().offset(0.0, -1.0, 0.0)
        private val NORMAL_OUTLINE_SHAPE: VoxelShape = VoxelShapes.union(
            createCuboidShape(0.0, 12.0, 0.0, 16.0, 16.0, 16.0),
            leg(),
            leg(12.0),
            leg(0.0, 12.0),
            leg(12.0, 12.0)
        )
        private val BOTTOM_OUTLINE_SHAPE: VoxelShape = VoxelShapes.union(
            COLLISION_SHAPE,
            NORMAL_OUTLINE_SHAPE,
            bottom(0.0, 4.0),
            bottom(4.0, 0.0, true),
            bottom(12.0, 4.0),
            bottom(4.0, 12.0, true)
        )

        private fun leg(offsetX: Double = 0.0, offsetZ: Double = 0.0): VoxelShape {
            return createCuboidShape(
                offsetX, 0.0, offsetZ,
                offsetX + 4, 16.0, offsetZ + 4
            )
        }

        private fun bottom(minX: Double, minZ: Double, z: Boolean = false): VoxelShape {
            val xSize: Int
            val zSize: Int
            if (z) {
                xSize = 4
                zSize = 8
            } else {
                xSize = 8
                zSize = 4
            }
            return createCuboidShape(
                minX, 0.0, minZ,
                minX + xSize, 4.0, minZ + zSize
            )
        }
    }
}
