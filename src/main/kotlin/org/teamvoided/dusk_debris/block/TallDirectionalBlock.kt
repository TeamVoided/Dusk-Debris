package org.teamvoided.dusk_debris.block


import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.Level
import net.minecraft.world.level.LevelAccessor
import net.minecraft.world.level.LevelReader
import net.minecraft.world.level.block.*
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.*
import net.minecraft.world.level.material.FluidState
import net.minecraft.world.level.material.Fluids
import net.minecraft.world.phys.shapes.CollisionContext
import net.minecraft.world.phys.shapes.VoxelShape

open class TallDirectionalBlock(settings: Properties) :
    Block(settings), SimpleWaterloggedBlock {

    init {
        this.registerDefaultState(
            defaultBlockState()
                .setValue(FACING, Direction.UP)
                .setValue(HALF, DoubleBlockHalf.LOWER)
                .setValue(WATERLOGGED, false)
        )
    }

    override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block, BlockState>) {
        builder.add(FACING, HALF, WATERLOGGED)
    }

    override fun getShape(
        state: BlockState,
        world: BlockGetter,
        pos: BlockPos,
        context: CollisionContext
    ): VoxelShape {
        val direction = state.getValue(FACING).axis
        return when (direction) {
            Direction.Axis.Z -> zShape
            Direction.Axis.X -> xShape
            Direction.Axis.Y -> yShape
            else -> yShape
        }
    }

    override fun canSurvive(state: BlockState, world: LevelReader, pos: BlockPos): Boolean {
        val direction = state.getValue(FACING)
        val blockPos = pos.relative(direction.opposite)
        val blockState = world.getBlockState(blockPos)

        return if (state.getValue(HALF) == DoubleBlockHalf.LOWER) {
            blockState.isFaceSturdy(world, blockPos, direction)
        } else {
            blockState.`is`(this) && blockState.getValue(HALF) == DoubleBlockHalf.LOWER
        }
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
        val blockstate = state.getValue(FACING)
        val blockstateOther =
            world.getBlockState(pos.relative(getDirectionTowardsOtherPart(state.getValue(HALF), blockstate)))
        return if (
            blockstateOther.`is`(this) &&
            state.canSurvive(world, pos)
        ) {
            state
        } else Blocks.AIR.defaultBlockState()
    }

    override fun playerWillDestroy(world: Level, pos: BlockPos, state: BlockState, player: Player): BlockState {
        if (!world.isClientSide && player.isCreative) {
            val crystalHalf = state.getValue(HALF)
            if (crystalHalf == DoubleBlockHalf.LOWER) {
                val blockPos = pos.relative(
                    getDirectionTowardsOtherPart(crystalHalf, state.getValue(FACING))
                )
                val blockState = world.getBlockState(blockPos)
                if (blockState.`is`(this) && blockState.getValue(HALF) == DoubleBlockHalf.UPPER) {
                    world.setBlock(blockPos, Blocks.AIR.defaultBlockState(), 35)
                    world.levelEvent(player, 2001, blockPos, getId(blockState))
                }
            }
        }
        return super.playerWillDestroy(world, pos, state, player)
    }

    override fun getStateForPlacement(ctx: BlockPlaceContext): BlockState? {
        val direction = ctx.clickedFace
        val world: LevelAccessor = ctx.level
        val blockPos = ctx.clickedPos
        val blockPos2 = blockPos.relative(direction)
        return if (world.getBlockState(blockPos2).canBeReplaced(ctx) &&
            blockPos2.y < world.maxBuildHeight && blockPos2.y > world.minBuildHeight &&
            world.worldBorder.isWithinBounds(blockPos2)
        ) defaultBlockState()
            .setValue(FACING, direction)
            .setValue(WATERLOGGED, world.getFluidState(blockPos).type == Fluids.WATER)
        else null
    }

    override fun setPlacedBy(world: Level, pos: BlockPos, state: BlockState, placer: LivingEntity?, itemStack: ItemStack) {
        super.setPlacedBy(world, pos, state, placer, itemStack)
        if (!world.isClientSide) {
            val blockPos = pos.relative(state.getValue(FACING))
            world.setBlock(
                blockPos,
                state
                    .setValue(HALF, DoubleBlockHalf.UPPER)
                    .setValue(WATERLOGGED, world.getFluidState(blockPos).type == Fluids.WATER),
                3
            )
            world.blockUpdated(pos, Blocks.AIR)
            state.updateNeighbourShapes(world, pos, 3)
        }
    }

    override fun rotate(state: BlockState, rotation: Rotation): BlockState {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)))
    }

    override fun mirror(state: BlockState, mirror: Mirror): BlockState {
        return state.rotate(mirror.getRotation(state.getValue(FACING)))
    }

    override fun getFluidState(state: BlockState): FluidState {
        return if (state.getValue(WATERLOGGED)) Fluids.WATER.getSource(false) else super.getFluidState(state)
    }

    companion object {
        val FACING: DirectionProperty = BlockStateProperties.FACING
        val HALF: EnumProperty<DoubleBlockHalf> = BlockStateProperties.DOUBLE_BLOCK_HALF
        val WATERLOGGED: BooleanProperty = BlockStateProperties.WATERLOGGED

        val yShape = box(
            2.0, 0.0, 2.0,
            14.0, 16.0, 14.0
        )
        val xShape = box(
            0.0, 2.0, 2.0,
            16.0, 14.0, 14.0
        )
        val zShape = box(
            2.0, 2.0, 0.0,
            14.0, 14.0, 16.0
        )

        fun getDirectionTowardsOtherPart(part: DoubleBlockHalf, direction: Direction): Direction {
            return if (part == DoubleBlockHalf.LOWER) direction else direction.opposite
        }

        fun getOppositeCrystalState(part: BlockState): DoubleBlockHalf {
            return if (part.getValue(HALF) == DoubleBlockHalf.LOWER) DoubleBlockHalf.UPPER else DoubleBlockHalf.LOWER
        }
    }
}