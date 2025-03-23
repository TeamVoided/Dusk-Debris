package org.teamvoided.dusk_debris.block


import net.minecraft.block.*
import net.minecraft.block.enums.DoubleBlockHalf
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.fluid.FluidState
import net.minecraft.fluid.Fluids
import net.minecraft.item.ItemPlacementContext
import net.minecraft.item.ItemStack
import net.minecraft.state.StateManager
import net.minecraft.state.property.BooleanProperty
import net.minecraft.state.property.DirectionProperty
import net.minecraft.state.property.EnumProperty
import net.minecraft.state.property.Properties
import net.minecraft.util.BlockMirror
import net.minecraft.util.BlockRotation
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.shape.VoxelShape
import net.minecraft.world.BlockView
import net.minecraft.world.World
import net.minecraft.world.WorldAccess
import net.minecraft.world.WorldView

open class TallDirectionalBlock(settings: Settings) :
    Block(settings), Waterloggable {

    init {
        this.defaultState =
            defaultState
                .with(FACING, Direction.UP)
                .with(HALF, DoubleBlockHalf.LOWER)
                .with(WATERLOGGED, false)
    }

    override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) {
        builder.add(FACING, HALF, WATERLOGGED)
    }

    override fun getOutlineShape(
        state: BlockState,
        world: BlockView,
        pos: BlockPos,
        context: ShapeContext
    ): VoxelShape {
        val direction = state.get(FACING).axis
        return when (direction) {
            Direction.Axis.Z -> zShape
            Direction.Axis.X -> xShape
            Direction.Axis.Y -> yShape
            else -> yShape
        }
    }

    override fun canPlaceAt(state: BlockState, world: WorldView, pos: BlockPos): Boolean {
        val direction = state.get(FACING)
        val blockPos = pos.offset(direction.opposite)
        val blockState = world.getBlockState(blockPos)

        return if (state.get(HALF) == DoubleBlockHalf.LOWER) {
            blockState.isSideSolidFullSquare(world, blockPos, direction)
        } else {
            blockState.isOf(this) && blockState.get(HALF) == DoubleBlockHalf.LOWER
        }
    }

    override fun getStateForNeighborUpdate(
        state: BlockState,
        direction: Direction,
        neighborState: BlockState,
        world: WorldAccess,
        pos: BlockPos,
        neighborPos: BlockPos
    ): BlockState {
        if (state.get(WATERLOGGED)) {
            world.scheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world))
        }
        val blockstate = state.get(FACING)
        val blockstateOther =
            world.getBlockState(pos.offset(getDirectionTowardsOtherPart(state.get(HALF), blockstate)))
        return if (
            blockstateOther.isOf(this) &&
            state.canPlaceAt(world, pos)
        ) {
            state
        } else Blocks.AIR.defaultState
    }

    override fun onBreak(world: World, pos: BlockPos, state: BlockState, player: PlayerEntity): BlockState {
        if (!world.isClient && player.isCreative) {
            val crystalHalf = state.get(HALF)
            if (crystalHalf == DoubleBlockHalf.LOWER) {
                val blockPos = pos.offset(
                    getDirectionTowardsOtherPart(crystalHalf, state.get(FACING))
                )
                val blockState = world.getBlockState(blockPos)
                if (blockState.isOf(this) && blockState.get(HALF) == DoubleBlockHalf.UPPER) {
                    world.setBlockState(blockPos, Blocks.AIR.defaultState, 35)
                    world.syncWorldEvent(player, 2001, blockPos, getRawIdFromState(blockState))
                }
            }
        }
        return super.onBreak(world, pos, state, player)
    }

    override fun getPlacementState(ctx: ItemPlacementContext): BlockState? {
        val direction = ctx.side
        val world: WorldAccess = ctx.world
        val blockPos = ctx.blockPos
        val blockPos2 = blockPos.offset(direction)
        return if (world.getBlockState(blockPos2).canReplace(ctx) &&
            blockPos2.y < world.topY && blockPos2.y > world.bottomY &&
            world.worldBorder.contains(blockPos2)
        ) defaultState
            .with(FACING, direction)
            .with(WATERLOGGED, world.getFluidState(blockPos).fluid == Fluids.WATER)
        else null
    }

    override fun onPlaced(world: World, pos: BlockPos, state: BlockState, placer: LivingEntity?, itemStack: ItemStack) {
        super.onPlaced(world, pos, state, placer, itemStack)
        if (!world.isClient) {
            val blockPos = pos.offset(state.get(FACING))
            world.setBlockState(
                blockPos,
                state
                    .with(HALF, DoubleBlockHalf.UPPER)
                    .with(WATERLOGGED, world.getFluidState(blockPos).fluid == Fluids.WATER),
                3
            )
            world.updateNeighbors(pos, Blocks.AIR)
            state.updateNeighbors(world, pos, 3)
        }
    }

    override fun rotate(state: BlockState, rotation: BlockRotation): BlockState {
        return state.with(FACING, rotation.rotate(state.get(FACING)))
    }

    override fun mirror(state: BlockState, mirror: BlockMirror): BlockState {
        return state.rotate(mirror.getRotation(state.get(FACING)))
    }

    override fun getFluidState(state: BlockState): FluidState {
        return if (state.get(WATERLOGGED)) Fluids.WATER.getStill(false) else super.getFluidState(state)
    }

    companion object {
        val FACING: DirectionProperty = Properties.FACING
        val HALF: EnumProperty<DoubleBlockHalf> = Properties.DOUBLE_BLOCK_HALF
        val WATERLOGGED: BooleanProperty = Properties.WATERLOGGED

        val yShape = createCuboidShape(
            2.0, 0.0, 2.0,
            14.0, 16.0, 14.0
        )
        val xShape = createCuboidShape(
            0.0, 2.0, 2.0,
            16.0, 14.0, 14.0
        )
        val zShape = createCuboidShape(
            2.0, 2.0, 0.0,
            14.0, 14.0, 16.0
        )

        fun getDirectionTowardsOtherPart(part: DoubleBlockHalf, direction: Direction): Direction {
            return if (part == DoubleBlockHalf.LOWER) direction else direction.opposite
        }

        fun getOppositeCrystalState(part: BlockState): DoubleBlockHalf {
            return if (part.get(HALF) == DoubleBlockHalf.LOWER) DoubleBlockHalf.UPPER else DoubleBlockHalf.LOWER
        }
    }
}