package org.teamvoided.dusk_debris.block

import com.mojang.serialization.MapCodec
import net.minecraft.block.*
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.entity.BlockEntityTicker
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.fluid.FluidState
import net.minecraft.fluid.Fluids
import net.minecraft.item.ItemPlacementContext
import net.minecraft.server.world.ServerWorld
import net.minecraft.state.StateManager
import net.minecraft.state.property.IntProperty
import net.minecraft.state.property.Properties
import net.minecraft.util.BlockMirror
import net.minecraft.util.BlockRotation
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.random.RandomGenerator
import net.minecraft.util.shape.VoxelShape
import net.minecraft.util.shape.VoxelShapes
import net.minecraft.world.BlockView
import net.minecraft.world.World
import net.minecraft.world.WorldAccess
import org.teamvoided.dusks_and_dungeons.block.entity.BunnyGraveBlockEntity
import org.teamvoided.dusks_and_dungeons.init.DnDBlockEntities
import org.teamvoided.dusks_and_dungeons.util.rotate

class BunnyGraveBlock(settings: Settings) : BlockWithEntity(settings), Waterloggable {
    init {
        this.defaultState = stateManager.defaultState
            .with(DUST, 0)
            .with(Properties.HORIZONTAL_FACING, Direction.NORTH)
            .with(Properties.WATERLOGGED, false)
    }

    override fun getCodec(): MapCodec<out BlockWithEntity> = CODEC

    override fun createBlockEntity(pos: BlockPos, state: BlockState): BlockEntity {
        return BunnyGraveBlockEntity(pos, state)
    }

    override fun getRenderType(state: BlockState): BlockRenderType {
        return BlockRenderType.MODEL
    }

    override fun <T : BlockEntity> getTicker(
        world: World,
        state: BlockState,
        type: BlockEntityType<T>
    ): BlockEntityTicker<T>? {
        return checkType(
            type,
            DnDBlockEntities.BUNNY_GRAVE,
            if (!world.isClient)
                BunnyGraveBlockEntity::serverTick
            else null
        )
    }

    override fun randomTick(state: BlockState, world: ServerWorld, pos: BlockPos, random: RandomGenerator) {
        val dustState = state.get(DUST)
        if (random.nextFloat() < 0.1 && dustState < maxDust) {
            world.setBlockState(pos, state.with(DUST, dustState + 1))
        }
        super.randomTick(state, world, pos, random)
    }

    override fun getPlacementState(ctx: ItemPlacementContext): BlockState {
        val waterlogged = ctx.world.getFluidState(ctx.blockPos).fluid === Fluids.WATER
        return defaultState
            .with(Properties.HORIZONTAL_FACING, ctx.playerFacing.opposite)
            .with(Properties.WATERLOGGED, waterlogged)
    }

    override fun getOutlineShape(
        state: BlockState, world: BlockView, pos: BlockPos, context: ShapeContext
    ): VoxelShape {
        val rotations = state.get(Properties.HORIZONTAL_FACING).horizontal
        return SHAPE.rotate(rotations)
    }

    override fun getStateForNeighborUpdate(
        state: BlockState, direction: Direction, neighborState: BlockState,
        world: WorldAccess, pos: BlockPos, neighborPos: BlockPos
    ): BlockState {
        if (state.get(Properties.WATERLOGGED))
            world.scheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world))
        return state
    }

    override fun getFluidState(state: BlockState): FluidState {
        return if (state.get(Properties.WATERLOGGED)) Fluids.WATER.getStill(false) else super.getFluidState(state)
    }

    override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) {
        super.appendProperties(builder)
        builder.add(DUST, Properties.HORIZONTAL_FACING, Properties.WATERLOGGED)
    }

    override fun rotate(state: BlockState, rotation: BlockRotation): BlockState {
        return state.with(Properties.HORIZONTAL_FACING, rotation.rotate(state.get(Properties.HORIZONTAL_FACING)))
    }

    override fun mirror(state: BlockState, mirror: BlockMirror): BlockState {
        return state.rotate(mirror.getRotation(state.get(Properties.HORIZONTAL_FACING)))
    }

    companion object {
        val CODEC: MapCodec<BunnyGraveBlock> = createCodec(::BunnyGraveBlock)
        const val maxDust = 7
        val DUST: IntProperty = IntProperty.of("dust", 0, maxDust)
        val SHAPE: VoxelShape = VoxelShapes.union(
            createCuboidShape(1.0, 0.0, 1.0, 15.0, 2.0, 15.0),
            createCuboidShape(5.0, 2.0, 1.0, 11.0, 14.0, 11.0)
        )
    }
}