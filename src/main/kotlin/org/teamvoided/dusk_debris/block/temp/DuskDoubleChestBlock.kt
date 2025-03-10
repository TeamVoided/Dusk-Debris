package org.teamvoided.dusk_debris.block.temp

import com.mojang.serialization.MapCodec
import net.minecraft.block.*
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.entity.BlockEntityTicker
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.block.enums.ChestType
import net.minecraft.entity.ai.pathing.NavigationType
import net.minecraft.entity.mob.PiglinBrain
import net.minecraft.entity.passive.CatEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.fluid.FluidState
import net.minecraft.fluid.Fluids
import net.minecraft.inventory.DoubleInventory
import net.minecraft.inventory.Inventory
import net.minecraft.item.ItemPlacementContext
import net.minecraft.screen.GenericContainerScreenHandler
import net.minecraft.screen.NamedScreenHandlerFactory
import net.minecraft.screen.ScreenHandler
import net.minecraft.server.world.ServerWorld
import net.minecraft.stat.Stat
import net.minecraft.stat.Stats
import net.minecraft.state.StateManager
import net.minecraft.state.property.Properties
import net.minecraft.text.Text
import net.minecraft.util.*
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Box
import net.minecraft.util.math.Direction
import net.minecraft.util.random.RandomGenerator
import net.minecraft.util.shape.VoxelShape
import net.minecraft.world.BlockView
import net.minecraft.world.World
import net.minecraft.world.WorldAccess
import org.teamvoided.dusk_debris.block.not_blocks.ChestPhase
import org.teamvoided.dusk_debris.block.not_blocks.DuskProperties
import org.teamvoided.dusk_debris.block.temp.entity.DuskChestBlockEntity
import org.teamvoided.dusk_debris.init.DuskBlockEntities
import org.teamvoided.dusk_debris.util.rotate
import java.util.*
import java.util.function.BiPredicate
import java.util.function.Supplier

class DuskDoubleChestBlock(settings: Settings, supplier: Supplier<BlockEntityType<out DuskChestBlockEntity>>) :
    AbstractDuskChestBlock<DuskChestBlockEntity>(settings, supplier), Waterloggable {
    init {
        this.defaultState =
            stateManager.defaultState
                .with(Properties.HORIZONTAL_FACING, Direction.NORTH)
                .with(Properties.CHEST_TYPE, ChestType.SINGLE)
                .with(DuskProperties.CHEST_PHASE, ChestPhase.CLOSED)
                .with(Properties.WATERLOGGED, false)
                .with(DuskProperties.LID, false)
    }

    public override fun getCodec(): MapCodec<out DuskDoubleChestBlock> = CODEC

    override fun getRenderType(state: BlockState): BlockRenderType {
        return BlockRenderType.ANIMATED
    }

    override fun getStateForNeighborUpdate(
        state: BlockState,
        direction: Direction,
        neighborState: BlockState,
        world: WorldAccess,
        pos: BlockPos,
        neighborPos: BlockPos
    ): BlockState {
        if (state.get(Properties.WATERLOGGED)) {
            world.scheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world))
        }

        if (neighborState.isOf(this) && direction.axis.isHorizontal) {
            val chestType = neighborState.get(Properties.CHEST_TYPE)
            if (state.get(Properties.CHEST_TYPE) == ChestType.SINGLE &&
                chestType != ChestType.SINGLE &&
                state.get(Properties.HORIZONTAL_FACING) == neighborState.get(Properties.HORIZONTAL_FACING) &&
                getFacing(neighborState) == direction.opposite
            ) {
                return state.with(Properties.CHEST_TYPE, chestType.opposite)
            }
        } else if (getFacing(state) == direction) {
            return state.with(Properties.CHEST_TYPE, ChestType.SINGLE)
        }

        return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos)
    }

    override fun getOutlineShape(
        state: BlockState,
        world: BlockView,
        pos: BlockPos,
        context: ShapeContext
    ): VoxelShape {
        if (state.get(Properties.CHEST_TYPE) == ChestType.SINGLE) {
            return SINGLE_SHAPE
        } else {
            return when (getFacing(state)) {
                Direction.NORTH -> DOUBLE_SHAPE
                Direction.SOUTH -> DOUBLE_SHAPE.rotate(2)
                Direction.WEST -> DOUBLE_SHAPE.rotate(1)
                Direction.EAST -> DOUBLE_SHAPE.rotate(3)
                else -> DOUBLE_SHAPE
            }
        }
    }

    override fun getPlacementState(ctx: ItemPlacementContext): BlockState? {
        var chestType = ChestType.SINGLE
        var direction = ctx.playerFacing.opposite
        val fluidState = ctx.world.getFluidState(ctx.blockPos)
        val bl = ctx.shouldCancelInteraction()
        val direction2 = ctx.side
        if (direction2.axis.isHorizontal && bl) {
            val neighborChestDir = this.getNeighborChestDirection(ctx, direction2.opposite)
            if (neighborChestDir != null && neighborChestDir.axis !== direction2.axis) {
                direction = neighborChestDir
                chestType =
                    if (neighborChestDir.rotateYCounterclockwise() == direction2.opposite) ChestType.RIGHT
                    else ChestType.LEFT
            }
        }

        if (chestType == ChestType.SINGLE && !bl) {
            if (direction == this.getNeighborChestDirection(ctx, direction.rotateYClockwise())) {
                chestType = ChestType.LEFT
            } else if (direction == this.getNeighborChestDirection(ctx, direction.rotateYCounterclockwise())) {
                chestType = ChestType.RIGHT
            }
        }

        return defaultState
            .with(Properties.HORIZONTAL_FACING, direction)
            .with(Properties.CHEST_TYPE, chestType)
            .with(Properties.WATERLOGGED, fluidState.fluid == Fluids.WATER)
    }

    override fun getFluidState(state: BlockState): FluidState {
        return if (state.get(Properties.WATERLOGGED)) Fluids.WATER.getStill(false) else super.getFluidState(state)
    }

    private fun getNeighborChestDirection(ctx: ItemPlacementContext, dir: Direction): Direction? {
        val blockState = ctx.world.getBlockState(ctx.blockPos.offset(dir))
        return if (blockState.isOf(this) && blockState.get(Properties.CHEST_TYPE) == ChestType.SINGLE) blockState.get(
            Properties.HORIZONTAL_FACING
        ) else null
    }

    override fun onStateReplaced(state: BlockState, world: World, pos: BlockPos, newState: BlockState, moved: Boolean) {
        ItemScatterer.scatterInventory(state, newState, world, pos)
        super.onStateReplaced(state, world, pos, newState, moved)
    }

    override fun onUse(
        state: BlockState,
        world: World,
        pos: BlockPos,
        entity: PlayerEntity,
        hitResult: BlockHitResult
    ): ActionResult {
        if (world.isClient) {
            return ActionResult.SUCCESS
        } else {
            val namedScreenHandlerFactory = this.createScreenHandlerFactory(state, world, pos)
            if (namedScreenHandlerFactory != null) {
                entity.openHandledScreen(namedScreenHandlerFactory)
                entity.incrementStat(this.openStat)
                PiglinBrain.onGuardedBlockInteracted(entity, true)
            }

            return ActionResult.CONSUME
        }
    }

    protected val openStat: Stat<Identifier>
        get() = Stats.CUSTOM.getOrCreateStat(Stats.OPEN_CHEST)

    val expectedEntityType: BlockEntityType<out DuskChestBlockEntity>
        get() = entityTypeRetriever.get()

    override fun getBlockEntitySource(
        state: BlockState,
        world: World,
        pos: BlockPos,
        ignoreBlocked: Boolean
    ): DoubleBlockProperties.PropertySource<out DuskChestBlockEntity> {
        val biPredicate: BiPredicate<WorldAccess, BlockPos> =
            if (ignoreBlocked) {
                BiPredicate { _, _ -> false }
            } else {
                BiPredicate(DuskDoubleChestBlock::isChestBlocked)
            }

        return DoubleBlockProperties.toPropertySource(
            this.entityTypeRetriever.get(),
            DuskDoubleChestBlock::getDoubleBlockType,
            DuskDoubleChestBlock::getFacing,
            Properties.HORIZONTAL_FACING,
            state,
            world,
            pos,
            biPredicate
        )
    }

    override fun createScreenHandlerFactory(
        state: BlockState,
        world: World,
        pos: BlockPos
    ): NamedScreenHandlerFactory? {
        return getBlockEntitySource(state, world, pos, false)
            .apply(NAME_RETRIEVER)
            .orElse(null)
    }

    override fun createBlockEntity(pos: BlockPos, state: BlockState): BlockEntity = DuskChestBlockEntity(pos, state)

    override fun <T : BlockEntity> getTicker(
        world: World,
        state: BlockState,
        type: BlockEntityType<T>
    ): BlockEntityTicker<T>? {
        return if (world.isClient) checkType(
            type,
            expectedEntityType, DuskChestBlockEntity::tick
        ) else null
    }

    override fun hasComparatorOutput(state: BlockState): Boolean = true

    override fun getComparatorOutput(state: BlockState, world: World, pos: BlockPos): Int {
        return ScreenHandler.calculateComparatorOutput(getInventory(this, state, world, pos, false))
    }

    override fun rotate(state: BlockState, rotation: BlockRotation): BlockState =
        state.with(Properties.HORIZONTAL_FACING, rotation.rotate(state.get(Properties.HORIZONTAL_FACING)))


    override fun mirror(state: BlockState, mirror: BlockMirror): BlockState =
        state.rotate(mirror.getRotation(state.get(Properties.HORIZONTAL_FACING)))


    override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) {
        builder.add(
            Properties.HORIZONTAL_FACING,
            Properties.CHEST_TYPE,
            DuskProperties.CHEST_PHASE,
            Properties.WATERLOGGED,
            DuskProperties.LID
        )
    }

    override fun canPathfindThrough(state: BlockState, navigationType: NavigationType): Boolean {
        return false
    }

    override fun scheduledTick(state: BlockState, world: ServerWorld, pos: BlockPos, random: RandomGenerator) {
        val blockEntity = world.getBlockEntity(pos)!!
        if (blockEntity is DuskChestBlockEntity) {
            blockEntity.onScheduledTick()
        }
    }

    companion object {
        val CODEC: MapCodec<DuskDoubleChestBlock> =
            createCodec { settings: Settings -> DuskDoubleChestBlock(settings) { DuskBlockEntities.STONE_CHEST } }
        val DOUBLE_SHAPE: VoxelShape =
            createCuboidShape(1.0, 0.0, 0.0, 15.0, 14.0, 15.0)
        val SINGLE_SHAPE: VoxelShape =
            createCuboidShape(1.0, 0.0, 1.0, 15.0, 14.0, 15.0)

        private val INVENTORY_RETRIEVER: DoubleBlockProperties.PropertyRetriever<DuskChestBlockEntity, Optional<Inventory>> =
            object : DoubleBlockProperties.PropertyRetriever<DuskChestBlockEntity, Optional<Inventory>> {
                override fun getFromBoth(
                    chestBlockEntity: DuskChestBlockEntity,
                    chestBlockEntity2: DuskChestBlockEntity
                ): Optional<Inventory> {
                    return Optional.of(DoubleInventory(chestBlockEntity, chestBlockEntity2))
                }

                override fun getFrom(chestBlockEntity: DuskChestBlockEntity): Optional<Inventory> {
                    return Optional.of(chestBlockEntity)
                }

                override fun getFallback(): Optional<Inventory> {
                    return Optional.empty()
                }
            }

        private val NAME_RETRIEVER: DoubleBlockProperties.PropertyRetriever<DuskChestBlockEntity, Optional<NamedScreenHandlerFactory>> =
            object :
                DoubleBlockProperties.PropertyRetriever<DuskChestBlockEntity, Optional<NamedScreenHandlerFactory>> {
                override fun getFromBoth(
                    chestBlockEntity: DuskChestBlockEntity,
                    chestBlockEntity2: DuskChestBlockEntity
                ): Optional<NamedScreenHandlerFactory> {
                    val inventory: Inventory = DoubleInventory(chestBlockEntity, chestBlockEntity2)
                    return Optional.of<NamedScreenHandlerFactory>(object : NamedScreenHandlerFactory {
                        override fun createMenu(
                            i: Int,
                            playerInventory: PlayerInventory,
                            playerEntity: PlayerEntity
                        ): ScreenHandler? {
                            if (chestBlockEntity.checkUnlocked(playerEntity) &&
                                chestBlockEntity2.checkUnlocked(playerEntity)
                            ) {
                                chestBlockEntity.setupLoot(playerInventory.player)
                                chestBlockEntity2.setupLoot(playerInventory.player)
                                return GenericContainerScreenHandler.createGeneric9x6(i, playerInventory, inventory)
                            } else {
                                return null
                            }
                        }

                        override fun getDisplayName(): Text {
                            return if (chestBlockEntity.hasCustomName())
                                chestBlockEntity.displayName
                            else if (chestBlockEntity2.hasCustomName())
                                chestBlockEntity2.displayName
                            else chestBlockEntity.getDoubleContainerName()
                        }
                    })
                }

                override fun getFrom(chestBlockEntity: DuskChestBlockEntity): Optional<NamedScreenHandlerFactory> {
                    return Optional.of(chestBlockEntity)
                }

                override fun getFallback(): Optional<NamedScreenHandlerFactory> {
                    return Optional.empty()
                }
            }

        fun getDoubleBlockType(state: BlockState): DoubleBlockProperties.Type {
            val chestType = state.get(Properties.CHEST_TYPE)
            return if (chestType == ChestType.SINGLE) {
                DoubleBlockProperties.Type.SINGLE
            } else {
                if (chestType == ChestType.RIGHT) DoubleBlockProperties.Type.FIRST
                else DoubleBlockProperties.Type.SECOND
            }
        }

        fun getFacing(state: BlockState): Direction {
            val direction = state.get(Properties.HORIZONTAL_FACING)
            return if (state.get(Properties.CHEST_TYPE) == ChestType.LEFT) direction.rotateYClockwise()
            else direction.rotateYCounterclockwise()
        }

        fun getInventory(
            block: DuskDoubleChestBlock,
            state: BlockState,
            world: World,
            pos: BlockPos,
            ignoreBlocked: Boolean
        ): Inventory? {
            return (block.getBlockEntitySource(state, world, pos, ignoreBlocked)
                .apply(INVENTORY_RETRIEVER)).orElse(null)
        }

        fun isChestBlocked(world: WorldAccess, pos: BlockPos): Boolean {
            return hasBlockOnTop(world, pos) || hasOcelotOnTop(world, pos)
        }

        private fun hasBlockOnTop(world: BlockView, pos: BlockPos): Boolean {
            val blockPos = pos.up()
            return world.getBlockState(blockPos).isSolidBlock(world, blockPos)
        }

        private fun hasOcelotOnTop(world: WorldAccess, pos: BlockPos): Boolean {
            val list = world.getNonSpectatingEntities(
                CatEntity::class.java,
                Box(
                    pos.x.toDouble(),
                    (pos.y + 1).toDouble(),
                    pos.z.toDouble(),
                    (pos.x + 1).toDouble(),
                    (pos.y + 2).toDouble(),
                    (pos.z + 1).toDouble()
                )
            )
            if (list.isNotEmpty()) {
                val var3: Iterator<CatEntity> = list.iterator()

                while (var3.hasNext()) {
                    val catEntity = var3.next()
                    if (catEntity.isInSittingPose) {
                        return true
                    }
                }
            }

            return false
        }
    }
}
