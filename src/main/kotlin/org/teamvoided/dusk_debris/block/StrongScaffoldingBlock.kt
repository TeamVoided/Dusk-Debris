package org.teamvoided.dusk_debris.block

import com.mojang.serialization.MapCodec
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.server.level.ServerLevel
import net.minecraft.util.RandomSource
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.Level
import net.minecraft.world.level.LevelAccessor
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.SimpleWaterloggedBlock
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.world.level.material.FluidState
import net.minecraft.world.level.material.Fluids
import net.minecraft.world.phys.shapes.CollisionContext
import net.minecraft.world.phys.shapes.Shapes
import net.minecraft.world.phys.shapes.VoxelShape

class StrongScaffoldingBlock(settings: Properties) : Block(settings), SimpleWaterloggedBlock {
    public override fun codec(): MapCodec<StrongScaffoldingBlock> = CODEC

    init {
        this.registerDefaultState(
            stateDefinition.any()
                .setValue(BlockStateProperties.WATERLOGGED, false)
                .setValue(BlockStateProperties.BOTTOM, false)
        )
    }

    override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block, BlockState>) {
        builder.add(BlockStateProperties.WATERLOGGED, BlockStateProperties.BOTTOM)
    }

    override fun getShape(
        state: BlockState,
        world: BlockGetter,
        pos: BlockPos,
        context: CollisionContext
    ): VoxelShape {
        return if (!context.isHoldingItem(state.block.asItem())) {
            if (state.getValue(BlockStateProperties.BOTTOM)) BOTTOM_OUTLINE_SHAPE else NORMAL_OUTLINE_SHAPE
        } else {
            Shapes.block()
        }
    }

    override fun getInteractionShape(state: BlockState, world: BlockGetter, pos: BlockPos): VoxelShape {
        return Shapes.block()
    }

    override fun canBeReplaced(state: BlockState, context: BlockPlaceContext): Boolean {
        return context.itemInHand.`is`(this.asItem())
    }

    override fun getStateForPlacement(ctx: BlockPlaceContext): BlockState? {
        val blockPos = ctx.clickedPos
        val world = ctx.level
        return defaultBlockState()
            .setValue(BlockStateProperties.WATERLOGGED, world.getFluidState(blockPos).type == Fluids.WATER)
            .setValue(BlockStateProperties.BOTTOM, this.shouldHaveBottom(world, blockPos))
    }

    override fun playerWillDestroy(world: Level, pos: BlockPos, state: BlockState, player: Player): BlockState {
        if (player.mainHandItem.`is`(this.asItem()) && world.getBlockState(pos.above()).`is`(this)) {
            world.scheduleTick(pos.above(), this, 1)
        }
        return super.playerWillDestroy(world, pos, state, player)
    }

    override fun updateShape(
        state: BlockState,
        direction: Direction,
        neighborState: BlockState,
        world: LevelAccessor,
        pos: BlockPos,
        neighborPos: BlockPos
    ): BlockState {
        val supr = super.updateShape(state, direction, neighborState, world, pos, neighborPos)
        if (state.getValue(BlockStateProperties.WATERLOGGED)) {
            world.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(world))
        }

        if (direction == Direction.DOWN)
            return supr.setValue(BlockStateProperties.BOTTOM, shouldHaveBottom(world, pos))

        return supr
    }

    override fun tick(state: BlockState, world: ServerLevel, pos: BlockPos, random: RandomSource) {
        Direction.entries.forEach {
            if (it != Direction.DOWN && world.getBlockState(pos.relative(it)).`is`(this)) {
                world.scheduleTick(pos.relative(it), this, 1)
            }
        }
        world.destroyBlock(pos, true)
    }

    override fun getCollisionShape(
        state: BlockState,
        world: BlockGetter,
        pos: BlockPos,
        context: CollisionContext
    ): VoxelShape {
        return if (context.isAbove(Shapes.block(), pos, true) && !context.isDescending) {
            NORMAL_OUTLINE_SHAPE
        } else if (state.getValue(BlockStateProperties.BOTTOM) && context.isAbove(OUTLINE_SHAPE, pos, true))
            COLLISION_SHAPE
        else
            Shapes.empty()

    }

    override fun getFluidState(state: BlockState): FluidState {
        return if (state.getValue(BlockStateProperties.WATERLOGGED))
            Fluids.WATER.getSource(false)
        else
            super.getFluidState(state)
    }

    private fun shouldHaveBottom(world: BlockGetter, pos: BlockPos): Boolean {
        val downState = world.getBlockState(pos.below())
        val fullSquare = downState.isFaceSturdy(world, pos.below(), Direction.UP)

        return !(downState.`is`(this) || fullSquare)
    }

    companion object {
        val CODEC: MapCodec<StrongScaffoldingBlock> = simpleCodec(::StrongScaffoldingBlock)
        private val COLLISION_SHAPE: VoxelShape = box(0.0, 0.0, 0.0, 16.0, 4.0, 16.0)
        private val OUTLINE_SHAPE: VoxelShape = Shapes.block().move(0.0, -1.0, 0.0)
        private val NORMAL_OUTLINE_SHAPE: VoxelShape = Shapes.or(
            box(0.0, 12.0, 0.0, 16.0, 16.0, 16.0),
            leg(),
            leg(12.0),
            leg(0.0, 12.0),
            leg(12.0, 12.0)
        )
        private val BOTTOM_OUTLINE_SHAPE: VoxelShape = Shapes.or(
            COLLISION_SHAPE,
            NORMAL_OUTLINE_SHAPE,
            bottom(0.0, 4.0),
            bottom(4.0, 0.0, true),
            bottom(12.0, 4.0),
            bottom(4.0, 12.0, true)
        )

        private fun leg(offsetX: Double = 0.0, offsetZ: Double = 0.0): VoxelShape {
            return box(
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
            return box(
                minX, 0.0, minZ,
                minX + xSize, 4.0, minZ + zSize
            )
        }
    }
}
