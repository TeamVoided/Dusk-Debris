package org.teamvoided.dusk_debris.block

import com.mojang.serialization.MapCodec
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.server.level.ServerLevel
import net.minecraft.util.RandomSource
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.*
import net.minecraft.world.level.block.DoubleBlockCombiner.NeighborCombineResult
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityTicker
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.entity.ChestBlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.DirectionProperty
import net.minecraft.world.level.pathfinder.PathComputationType
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.shapes.CollisionContext
import net.minecraft.world.phys.shapes.VoxelShape
import org.teamvoided.dusk_debris.init.DuskBlockEntities
import org.teamvoided.dusks_and_dungeons.block.entity.ChestOSoulsBlockEntity

class ChestOSoulsBlock(
    settings: Properties?,
) : AbstractChestBlock<ChestOSoulsBlockEntity>(settings, { DuskBlockEntities.CHEST_O_SOULS }) {

    init {
        registerDefaultState(stateDefinition.any().setValue(FACING, Direction.NORTH))
    }

    override fun codec(): MapCodec<out AbstractChestBlock<ChestOSoulsBlockEntity>> = CODEC

    override fun getShape(
        state: BlockState?,
        world: BlockGetter?,
        pos: BlockPos?,
        context: CollisionContext?
    ): VoxelShape = SHAPE

    override fun getStateForPlacement(ctx: BlockPlaceContext): BlockState =
        defaultBlockState().setValue(EnderChestBlock.FACING, ctx.horizontalDirection.opposite)

    override fun useWithoutItem(
        state: BlockState?,
        world: Level,
        pos: BlockPos?,
        player: Player,
        hitResult: BlockHitResult?
    ): InteractionResult {
        val blockEntity = world.getBlockEntity(pos)
        if (blockEntity is ChestOSoulsBlockEntity) {
            if (blockEntity.isOpen()) {
                return InteractionResult.PASS
            }

            if (world.isClientSide) {
                return InteractionResult.CONSUME
            }

            blockEntity.open(player)
            return InteractionResult.SUCCESS
        }
        return super.useWithoutItem(state, world, pos, player, hitResult)
    }

    override fun newBlockEntity(pos: BlockPos?, state: BlockState?): BlockEntity = ChestOSoulsBlockEntity(pos, state)

    override fun <T : BlockEntity?> getTicker(
        world: Level,
        state: BlockState?,
        type: BlockEntityType<T>?
    ): BlockEntityTicker<T>? =
        createTickerHelper(type, DuskBlockEntities.CHEST_O_SOULS, ChestOSoulsBlockEntity::tick)

    override fun combine(
        state: BlockState, world: Level, pos: BlockPos, ignoreBlocked: Boolean
    ): NeighborCombineResult<out ChestBlockEntity>? {
        return object : NeighborCombineResult<ChestBlockEntity> {

            override fun <T : Any?> apply(propertyRetriever: DoubleBlockCombiner.Combiner<in ChestBlockEntity, T>): T {
                return propertyRetriever.acceptNone()
            }
        }
    }

    override fun tick(state: BlockState?, world: ServerLevel, pos: BlockPos?, random: RandomSource?) {
        val blockEntity = world.getBlockEntity(pos)

        if (blockEntity is ChestOSoulsBlockEntity) {
            blockEntity.onScheduledTick()
        }
    }

    override fun getRenderShape(state: BlockState?): RenderShape = RenderShape.ENTITYBLOCK_ANIMATED

    override fun rotate(state: BlockState, rotation: Rotation): BlockState =
        state.setValue(FACING, rotation.rotate(state.getValue(FACING)))

    override fun mirror(state: BlockState, mirror: Mirror): BlockState =
        state.rotate(mirror.getRotation(state.getValue(FACING)))

    override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block?, BlockState?>) {
        builder.add(FACING)
    }

    override fun isPathfindable(state: BlockState?, navigationType: PathComputationType?): Boolean = false

    companion object {
        val FACING: DirectionProperty = HorizontalDirectionalBlock.FACING;
        val CODEC: MapCodec<ChestOSoulsBlock> = simpleCodec(::ChestOSoulsBlock)
        val SHAPE: VoxelShape = box(1.0, 0.0, 1.0, 15.0, 14.0, 15.0)
    }
}
