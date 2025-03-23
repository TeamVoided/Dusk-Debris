package org.teamvoided.dusk_debris.block

import com.mojang.serialization.MapCodec
import net.minecraft.block.*
import net.minecraft.block.DoubleBlockProperties.PropertySource
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.entity.BlockEntityTicker
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.block.entity.ChestBlockEntity
import net.minecraft.entity.ai.pathing.NavigationType
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemPlacementContext
import net.minecraft.server.world.ServerWorld
import net.minecraft.state.StateManager
import net.minecraft.state.property.DirectionProperty
import net.minecraft.util.ActionResult
import net.minecraft.util.BlockMirror
import net.minecraft.util.BlockRotation
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.random.RandomGenerator
import net.minecraft.util.shape.VoxelShape
import net.minecraft.world.BlockView
import net.minecraft.world.World
import org.teamvoided.dusk_debris.init.DuskBlockEntities
import org.teamvoided.dusks_and_dungeons.block.entity.ChestOSoulsBlockEntity

class ChestOSoulsBlock(
    settings: Settings?,
) : AbstractChestBlock<ChestOSoulsBlockEntity>(settings, { DuskBlockEntities.CHEST_O_SOULS }) {

    init {
        defaultState = stateManager.defaultState.with(FACING, Direction.NORTH)
    }

    override fun getCodec(): MapCodec<out AbstractChestBlock<ChestOSoulsBlockEntity>> = CODEC

    override fun getOutlineShape(
        state: BlockState?,
        world: BlockView?,
        pos: BlockPos?,
        context: ShapeContext?
    ): VoxelShape = SHAPE

    override fun getPlacementState(ctx: ItemPlacementContext): BlockState =
        defaultState.with(EnderChestBlock.FACING, ctx.playerFacing.opposite)

    override fun onUse(
        state: BlockState?,
        world: World,
        pos: BlockPos?,
        player: PlayerEntity,
        hitResult: BlockHitResult?
    ): ActionResult {
        val blockEntity = world.getBlockEntity(pos)
        if (blockEntity is ChestOSoulsBlockEntity) {
            if (blockEntity.isOpen()) {
                return ActionResult.PASS
            }

            if (world.isClient) {
                return ActionResult.CONSUME
            }

            blockEntity.open(player)
            return ActionResult.SUCCESS
        }
        return super.onUse(state, world, pos, player, hitResult)
    }

    override fun createBlockEntity(pos: BlockPos?, state: BlockState?): BlockEntity = ChestOSoulsBlockEntity(pos, state)

    override fun <T : BlockEntity?> getTicker(
        world: World,
        state: BlockState?,
        type: BlockEntityType<T>?
    ): BlockEntityTicker<T>? =
        checkType(type, DuskBlockEntities.CHEST_O_SOULS, ChestOSoulsBlockEntity::tick)

    override fun getBlockEntitySource(
        state: BlockState, world: World, pos: BlockPos, ignoreBlocked: Boolean
    ): PropertySource<out ChestBlockEntity>? {
        return object : PropertySource<ChestBlockEntity> {

            override fun <T : Any?> apply(propertyRetriever: DoubleBlockProperties.PropertyRetriever<in ChestBlockEntity, T>): T {
                return propertyRetriever.fallback
            }
        }
    }

    override fun scheduledTick(state: BlockState?, world: ServerWorld, pos: BlockPos?, random: RandomGenerator?) {
        val blockEntity = world.getBlockEntity(pos)

        if (blockEntity is ChestOSoulsBlockEntity) {
            blockEntity.onScheduledTick()
        }
    }

    override fun getRenderType(state: BlockState?): BlockRenderType = BlockRenderType.ANIMATED

    override fun rotate(state: BlockState, rotation: BlockRotation): BlockState =
        state.with(FACING, rotation.rotate(state.get(FACING)))

    override fun mirror(state: BlockState, mirror: BlockMirror): BlockState =
        state.rotate(mirror.getRotation(state.get(FACING)))

    override fun appendProperties(builder: StateManager.Builder<Block?, BlockState?>) {
        builder.add(FACING)
    }

    override fun canPathfindThrough(state: BlockState?, navigationType: NavigationType?): Boolean = false

    companion object {
        val FACING: DirectionProperty = HorizontalFacingBlock.FACING;
        val CODEC: MapCodec<ChestOSoulsBlock> = createCodec(::ChestOSoulsBlock)
        val SHAPE: VoxelShape = createCuboidShape(1.0, 0.0, 1.0, 15.0, 14.0, 15.0)
    }
}
