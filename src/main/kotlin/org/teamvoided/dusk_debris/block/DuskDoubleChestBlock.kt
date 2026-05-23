package org.teamvoided.dusk_debris.block

import com.mojang.serialization.MapCodec
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.level.ServerLevel
import net.minecraft.stats.Stat
import net.minecraft.stats.Stats
import net.minecraft.util.RandomSource
import net.minecraft.world.*
import net.minecraft.world.entity.animal.Cat
import net.minecraft.world.entity.monster.piglin.PiglinAi
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.inventory.ChestMenu
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.Level
import net.minecraft.world.level.LevelAccessor
import net.minecraft.world.level.block.*
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityTicker
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.world.level.block.state.properties.ChestType
import net.minecraft.world.level.material.FluidState
import net.minecraft.world.level.material.Fluids
import net.minecraft.world.level.pathfinder.PathComputationType
import net.minecraft.world.phys.AABB
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.shapes.CollisionContext
import net.minecraft.world.phys.shapes.VoxelShape
import org.teamvoided.dusk_debris.block.entity.DuskChestBlockEntity
import org.teamvoided.dusk_debris.block.not_blocks.ChestPhase
import org.teamvoided.dusk_debris.block.not_blocks.DuskProperties
import org.teamvoided.dusk_debris.block.temp.AbstractDuskChestBlock
import org.teamvoided.dusk_debris.init.DuskBlockEntities
import org.teamvoided.dusk_debris.util.rotate
import java.util.*
import java.util.function.BiPredicate
import java.util.function.Supplier

class DuskDoubleChestBlock(settings: Properties, supplier: Supplier<BlockEntityType<out DuskChestBlockEntity>>) :
    AbstractDuskChestBlock<DuskChestBlockEntity>(settings, supplier), SimpleWaterloggedBlock {
    init {
        this.registerDefaultState(
            stateDefinition.any()
                .setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.NORTH)
                .setValue(BlockStateProperties.CHEST_TYPE, ChestType.SINGLE)
                .setValue(DuskProperties.CHEST_PHASE, ChestPhase.CLOSED)
                .setValue(BlockStateProperties.WATERLOGGED, false)
                .setValue(DuskProperties.LID, false)
        )
    }

    public override fun codec(): MapCodec<out DuskDoubleChestBlock> = CODEC

    override fun getRenderShape(state: BlockState): RenderShape = RenderShape.MODEL

    override fun updateShape(
        state: BlockState,
        direction: Direction,
        neighborState: BlockState,
        world: LevelAccessor,
        pos: BlockPos,
        neighborPos: BlockPos
    ): BlockState {
        if (state.getValue(BlockStateProperties.WATERLOGGED)) {
            world.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(world))
        }

        if (neighborState.`is`(this) && direction.axis.isHorizontal) {
            val chestType = neighborState.getValue(BlockStateProperties.CHEST_TYPE)
            if (state.getValue(BlockStateProperties.CHEST_TYPE) == ChestType.SINGLE &&
                chestType != ChestType.SINGLE &&
                state.getValue(BlockStateProperties.HORIZONTAL_FACING) == neighborState.getValue(BlockStateProperties.HORIZONTAL_FACING) &&
                getFacing(neighborState) == direction.opposite
            ) {
                return state.setValue(BlockStateProperties.CHEST_TYPE, chestType.opposite)
            }
        } else if (getFacing(state) == direction) {
            return state.setValue(BlockStateProperties.CHEST_TYPE, ChestType.SINGLE)
        }

        return super.updateShape(state, direction, neighborState, world, pos, neighborPos)
    }

    override fun getShape(
        state: BlockState,
        world: BlockGetter,
        pos: BlockPos,
        context: CollisionContext
    ): VoxelShape {
        return if (state.getValue(BlockStateProperties.CHEST_TYPE) == ChestType.SINGLE) {
            SINGLE_SHAPE
        } else {
            when (getFacing(state)) {
                Direction.NORTH -> DOUBLE_SHAPE.rotate(2)
                Direction.SOUTH -> DOUBLE_SHAPE
                Direction.WEST -> DOUBLE_SHAPE.rotate(3)
                Direction.EAST -> DOUBLE_SHAPE.rotate(1)
                else -> DOUBLE_SHAPE
            }
        }
    }

    override fun getStateForPlacement(ctx: BlockPlaceContext): BlockState? {
        var chestType = ChestType.SINGLE
        var direction = ctx.horizontalDirection.opposite
        val bl = ctx.isSecondaryUseActive
        val placeSide = ctx.clickedFace
        if (placeSide.axis.isHorizontal && bl) {
            val neighborChestDir = this.getNeighborChestDirection(ctx, placeSide.opposite)
            if (neighborChestDir != null && neighborChestDir.axis != placeSide.axis) {
                direction = neighborChestDir
                chestType =
                    if (neighborChestDir.counterClockWise == placeSide.opposite) ChestType.LEFT
                    else ChestType.RIGHT
            }
        }

        if (chestType == ChestType.SINGLE && !bl) {
            if (direction == this.getNeighborChestDirection(ctx, direction.clockWise)) {
                chestType = ChestType.RIGHT
            } else if (direction == this.getNeighborChestDirection(ctx, direction.counterClockWise)) {
                chestType = ChestType.LEFT
            }
        }

        val fluidState = ctx.level.getFluidState(ctx.clickedPos)
        return defaultBlockState()
            .setValue(BlockStateProperties.HORIZONTAL_FACING, direction)
            .setValue(BlockStateProperties.CHEST_TYPE, chestType)
            .setValue(BlockStateProperties.WATERLOGGED, fluidState.type == Fluids.WATER)
    }

    override fun getFluidState(state: BlockState): FluidState {
        return if (state.getValue(BlockStateProperties.WATERLOGGED)) Fluids.WATER.getSource(false) else super.getFluidState(state)
    }

    private fun getNeighborChestDirection(ctx: BlockPlaceContext, dir: Direction): Direction? {
        val blockState = ctx.level.getBlockState(ctx.clickedPos.relative(dir))
        return if (blockState.`is`(this) && blockState.getValue(BlockStateProperties.CHEST_TYPE) == ChestType.SINGLE)
            blockState.getValue(BlockStateProperties.HORIZONTAL_FACING)
        else null
    }

    override fun onRemove(state: BlockState, world: Level, pos: BlockPos, newState: BlockState, moved: Boolean) {
        Containers.dropContentsOnDestroy(state, newState, world, pos)
        super.onRemove(state, world, pos, newState, moved)
    }

    override fun useWithoutItem(
        state: BlockState,
        world: Level,
        pos: BlockPos,
        entity: Player,
        hitResult: BlockHitResult
    ): InteractionResult {
        if (world.isClientSide) {
            return InteractionResult.SUCCESS
        } else {
            val namedScreenHandlerFactory = this.getMenuProvider(state, world, pos)
            if (namedScreenHandlerFactory != null) {
                entity.openMenu(namedScreenHandlerFactory)
                entity.awardStat(this.openStat)
                PiglinAi.angerNearbyPiglins(entity, true)
            }

            return InteractionResult.CONSUME
        }
    }

    protected val openStat: Stat<ResourceLocation>
        get() = Stats.CUSTOM.get(Stats.OPEN_CHEST)

    val expectedEntityType: BlockEntityType<out DuskChestBlockEntity>
        get() = entityTypeRetriever.get()

    override fun getBlockEntitySource(
        state: BlockState,
        world: Level,
        pos: BlockPos,
        ignoreBlocked: Boolean
    ): DoubleBlockCombiner.NeighborCombineResult<out DuskChestBlockEntity> {
        val biPredicate: BiPredicate<LevelAccessor, BlockPos> =
            if (ignoreBlocked) {
                BiPredicate { _, _ -> false }
            } else {
                BiPredicate(Companion::isChestBlocked)
            }

        return DoubleBlockCombiner.combineWithNeigbour(
            this.entityTypeRetriever.get(),
            Companion::getDoubleBlockType,
            Companion::getFacing,
            BlockStateProperties.HORIZONTAL_FACING,
            state,
            world,
            pos,
            biPredicate
        )
    }

    override fun getMenuProvider(
        state: BlockState,
        world: Level,
        pos: BlockPos
    ): MenuProvider? {
        return getBlockEntitySource(state, world, pos, false)
            .apply(NAME_RETRIEVER)
            .orElse(null)
    }

    override fun newBlockEntity(pos: BlockPos, state: BlockState): BlockEntity = DuskChestBlockEntity(pos, state)

    override fun <T : BlockEntity> getTicker(
        world: Level,
        state: BlockState,
        type: BlockEntityType<T>
    ): BlockEntityTicker<T>? {
        return createTickerHelper(type, expectedEntityType, DuskChestBlockEntity::tick)
    }

    override fun hasAnalogOutputSignal(state: BlockState): Boolean = true

    override fun getAnalogOutputSignal(state: BlockState, world: Level, pos: BlockPos): Int {
        return AbstractContainerMenu.getRedstoneSignalFromContainer(getInventory(this, state, world, pos, false))
    }

    override fun rotate(state: BlockState, rotation: Rotation): BlockState =
        state.setValue(BlockStateProperties.HORIZONTAL_FACING, rotation.rotate(state.getValue(BlockStateProperties.HORIZONTAL_FACING)))


    override fun mirror(state: BlockState, mirror: Mirror): BlockState =
        state.rotate(mirror.getRotation(state.getValue(BlockStateProperties.HORIZONTAL_FACING)))


    override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block, BlockState>) {
        builder.add(
            BlockStateProperties.HORIZONTAL_FACING,
            BlockStateProperties.CHEST_TYPE,
            DuskProperties.CHEST_PHASE,
            BlockStateProperties.WATERLOGGED,
            DuskProperties.LID
        )
    }

    override fun isPathfindable(state: BlockState, navigationType: PathComputationType): Boolean {
        return false
    }

    override fun tick(state: BlockState, world: ServerLevel, pos: BlockPos, random: RandomSource) {
        val blockEntity = world.getBlockEntity(pos)!!
        if (blockEntity is DuskChestBlockEntity) {
            blockEntity.onScheduledTick()
        }
    }

    companion object {
        val CODEC: MapCodec<DuskDoubleChestBlock> =
            simpleCodec { settings: Properties -> DuskDoubleChestBlock(settings) { DuskBlockEntities.STONE_CHEST } }
        val DOUBLE_SHAPE: VoxelShape =
            box(1.0, 0.0, 0.0, 15.0, 14.0, 15.0)
        val SINGLE_SHAPE: VoxelShape =
            box(1.0, 0.0, 1.0, 15.0, 14.0, 15.0)

        private val INVENTORY_RETRIEVER: DoubleBlockCombiner.Combiner<DuskChestBlockEntity, Optional<Container>> =
            object : DoubleBlockCombiner.Combiner<DuskChestBlockEntity, Optional<Container>> {
                override fun acceptDouble(
                    chestBlockEntity: DuskChestBlockEntity,
                    chestBlockEntity2: DuskChestBlockEntity
                ): Optional<Container> {
                    return Optional.of(CompoundContainer(chestBlockEntity, chestBlockEntity2))
                }

                override fun acceptSingle(chestBlockEntity: DuskChestBlockEntity): Optional<Container> {
                    return Optional.of(chestBlockEntity)
                }

                override fun acceptNone(): Optional<Container> {
                    return Optional.empty()
                }
            }

        private val NAME_RETRIEVER: DoubleBlockCombiner.Combiner<DuskChestBlockEntity, Optional<MenuProvider>> =
            object :
                DoubleBlockCombiner.Combiner<DuskChestBlockEntity, Optional<MenuProvider>> {
                override fun acceptDouble(
                    chestBlockEntity: DuskChestBlockEntity,
                    chestBlockEntity2: DuskChestBlockEntity
                ): Optional<MenuProvider> {
                    val inventory: Container = CompoundContainer(chestBlockEntity, chestBlockEntity2)
                    return Optional.of<MenuProvider>(object : MenuProvider {
                        override fun createMenu(
                            i: Int,
                            playerInventory: Inventory,
                            playerEntity: Player
                        ): AbstractContainerMenu? {
                            if (chestBlockEntity.canOpen(playerEntity) &&
                                chestBlockEntity2.canOpen(playerEntity)
                            ) {
                                chestBlockEntity.unpackLootTable(playerInventory.player)
                                chestBlockEntity2.unpackLootTable(playerInventory.player)
                                return ChestMenu.sixRows(i, playerInventory, inventory)
                            } else {
                                return null
                            }
                        }

                        override fun getDisplayName(): Component {
                            return if (chestBlockEntity.hasCustomName())
                                chestBlockEntity.displayName
                            else if (chestBlockEntity2.hasCustomName())
                                chestBlockEntity2.displayName
                            else chestBlockEntity.getDoubleContainerName()
                        }
                    })
                }

                override fun acceptSingle(chestBlockEntity: DuskChestBlockEntity): Optional<MenuProvider> {
                    return Optional.of(chestBlockEntity)
                }

                override fun acceptNone(): Optional<MenuProvider> {
                    return Optional.empty()
                }
            }

        fun getDoubleBlockType(state: BlockState): DoubleBlockCombiner.BlockType {
            val chestType = state.getValue(BlockStateProperties.CHEST_TYPE)
            return if (chestType == ChestType.SINGLE) {
                DoubleBlockCombiner.BlockType.SINGLE
            } else {
                if (chestType == ChestType.LEFT) DoubleBlockCombiner.BlockType.FIRST
                else DoubleBlockCombiner.BlockType.SECOND
            }
        }

        fun getFacing(state: BlockState): Direction {
            val direction = state.getValue(BlockStateProperties.HORIZONTAL_FACING)
            return if (state.getValue(BlockStateProperties.CHEST_TYPE) == ChestType.RIGHT) direction.clockWise
            else direction.counterClockWise
        }

        fun getInventory(
            block: DuskDoubleChestBlock,
            state: BlockState,
            world: Level,
            pos: BlockPos,
            ignoreBlocked: Boolean
        ): Container? {
            return block.getBlockEntitySource(state, world, pos, ignoreBlocked)
                .apply(INVENTORY_RETRIEVER)
                .orElse(null)
        }

        fun isChestBlocked(world: LevelAccessor, pos: BlockPos): Boolean {
            return hasBlockOnTop(world, pos) || hasOcelotOnTop(world, pos)
        }

        private fun hasBlockOnTop(world: BlockGetter, pos: BlockPos): Boolean {
            val blockPos = pos.above()
            return world.getBlockState(blockPos).isRedstoneConductor(world, blockPos)
        }

        private fun hasOcelotOnTop(world: LevelAccessor, pos: BlockPos): Boolean {
            val list = world.getEntitiesOfClass(
                Cat::class.java,
                AABB(
                    pos.x.toDouble(),
                    (pos.y + 1).toDouble(),
                    pos.z.toDouble(),
                    (pos.x + 1).toDouble(),
                    (pos.y + 2).toDouble(),
                    (pos.z + 1).toDouble()
                )
            )
            if (list.isNotEmpty()) {
                list.forEach {
                    if (it.isInSittingPose) {
                        return true
                    }
                }
            }

            return false
        }
    }
}
