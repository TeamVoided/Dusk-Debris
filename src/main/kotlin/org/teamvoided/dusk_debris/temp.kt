////
//// Source code recreated from a .class file by IntelliJ IDEA
//// (powered by FernFlower decompiler)
////
//package net.minecraft.block
//
//import com.mojang.serialization.MapCodec
//import it.unimi.dsi.fastutil.floats.Float2FloatFunction
//import net.minecraft.block.DoubleBlockProperties.PropertyRetriever
//import net.minecraft.block.entity.BlockEntity
//import net.minecraft.block.entity.BlockEntityTicker
//import net.minecraft.block.entity.BlockEntityType
//import net.minecraft.block.entity.ChestBlockEntity
//import net.minecraft.block.enums.ChestType
//import net.minecraft.client.block.ChestAnimationProgress
//import net.minecraft.entity.ai.pathing.NavigationType
//import net.minecraft.entity.mob.PiglinBrain
//import net.minecraft.entity.passive.CatEntity
//import net.minecraft.entity.player.PlayerEntity
//import net.minecraft.entity.player.PlayerInventory
//import net.minecraft.fluid.FluidState
//import net.minecraft.fluid.Fluids
//import net.minecraft.inventory.DoubleInventory
//import net.minecraft.inventory.Inventory
//import net.minecraft.item.ItemPlacementContext
//import net.minecraft.screen.GenericContainerScreenHandler
//import net.minecraft.screen.NamedScreenHandlerFactory
//import net.minecraft.screen.ScreenHandler
//import net.minecraft.server.world.ServerWorld
//import net.minecraft.stat.Stat
//import net.minecraft.stat.Stats
//import net.minecraft.state.StateManager
//import net.minecraft.state.property.*
//import net.minecraft.state.property.Properties
//import net.minecraft.text.Text
//import net.minecraft.util.*
//import net.minecraft.util.hit.BlockHitResult
//import net.minecraft.util.math.BlockPos
//import net.minecraft.util.math.Box
//import net.minecraft.util.math.Direction
//import net.minecraft.util.random.RandomGenerator
//import net.minecraft.util.shape.VoxelShape
//import net.minecraft.world.BlockView
//import net.minecraft.world.World
//import net.minecraft.world.WorldAccess
//import java.util.*
//import java.util.function.BiPredicate
//import java.util.function.Function
//import java.util.function.Supplier
//import kotlin.math.max
//
//open class ChestBlock(settings: Settings?, supplier: Supplier<BlockEntityType<out ChestBlockEntity>>?) :
//    AbstractChestBlock<ChestBlockEntity?>(settings, supplier), Waterloggable {
//    public override fun getCodec(): MapCodec<out ChestBlock> {
//        return CODEC
//    }
//
//    override fun getRenderType(state: BlockState): BlockRenderType {
//        return BlockRenderType.ANIMATED
//    }
//
//    override fun getStateForNeighborUpdate(
//        state: BlockState,
//        direction: Direction,
//        neighborState: BlockState,
//        world: WorldAccess,
//        pos: BlockPos,
//        neighborPos: BlockPos
//    ): BlockState {
//        if (state.get(WATERLOGGED) as Boolean) {
//            world.scheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world))
//        }
//
//        if (neighborState.isOf(this) && direction.axis.isHorizontal) {
//            val chestType = neighborState.get(CHEST_TYPE) as ChestType
//            if (state.get(CHEST_TYPE) == ChestType.SINGLE && chestType != ChestType.SINGLE && state.get(
//                    FACING
//                ) == neighborState.get(FACING) && getFacing(
//                    neighborState
//                ) == direction.opposite
//            ) {
//                return state.with(CHEST_TYPE, chestType.opposite) as BlockState
//            }
//        } else if (getFacing(state) == direction) {
//            return state.with(CHEST_TYPE, ChestType.SINGLE) as BlockState
//        }
//
//        return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos)
//    }
//
//    override fun getOutlineShape(
//        state: BlockState,
//        world: BlockView,
//        pos: BlockPos,
//        context: ShapeContext
//    ): VoxelShape {
//        return if (state.get(CHEST_TYPE) == ChestType.SINGLE) {
//            SINGLE_SHAPE
//        } else {
//            when (getFacing(state)) {
//                Direction.NORTH -> DOUBLE_NORTH_SHAPE
//                Direction.SOUTH -> DOUBLE_SOUTH_SHAPE
//                Direction.WEST -> DOUBLE_WEST_SHAPE
//                Direction.EAST -> DOUBLE_EAST_SHAPE
//                else -> DOUBLE_NORTH_SHAPE
//            }
//        }
//    }
//
//    override fun getPlacementState(ctx: ItemPlacementContext): BlockState? {
//        var chestType = ChestType.SINGLE
//        var direction = ctx.playerFacing.opposite
//        val fluidState = ctx.world.getFluidState(ctx.blockPos)
//        val bl = ctx.shouldCancelInteraction()
//        val direction2 = ctx.side
//        if (direction2.axis.isHorizontal && bl) {
//            val direction3 = this.getNeighborChestDirection(ctx, direction2.opposite)
//            if (direction3 != null && direction3.axis !== direction2.axis) {
//                direction = direction3
//                chestType =
//                    if (direction3.rotateYCounterclockwise() == direction2.opposite) ChestType.RIGHT else ChestType.LEFT
//            }
//        }
//
//        if (chestType == ChestType.SINGLE && !bl) {
//            if (direction == this.getNeighborChestDirection(ctx, direction.rotateYClockwise())) {
//                chestType = ChestType.LEFT
//            } else if (direction == this.getNeighborChestDirection(ctx, direction.rotateYCounterclockwise())) {
//                chestType = ChestType.RIGHT
//            }
//        }
//
//        return ((defaultState.with(FACING, direction) as BlockState).with(CHEST_TYPE, chestType) as BlockState).with(
//            WATERLOGGED, fluidState.fluid === Fluids.WATER
//        ) as BlockState
//    }
//
//    override fun getFluidState(state: BlockState): FluidState {
//        return if (state.get(WATERLOGGED) as Boolean) Fluids.WATER.getStill(false) else super.getFluidState(state)
//    }
//
//    private fun getNeighborChestDirection(ctx: ItemPlacementContext, dir: Direction): Direction? {
//        val blockState = ctx.world.getBlockState(ctx.blockPos.offset(dir))
//        return if (blockState.isOf(this) && blockState.get(CHEST_TYPE) == ChestType.SINGLE) blockState.get(FACING) as Direction else null
//    }
//
//    override fun onStateReplaced(state: BlockState, world: World, pos: BlockPos, newState: BlockState, moved: Boolean) {
//        ItemScatterer.scatterInventory(state, newState, world, pos)
//        super.onStateReplaced(state, world, pos, newState, moved)
//    }
//
//    override fun onUse(
//        state: BlockState,
//        world: World,
//        pos: BlockPos,
//        entity: PlayerEntity,
//        hitResult: BlockHitResult
//    ): ActionResult {
//        if (world.isClient) {
//            return ActionResult.SUCCESS
//        } else {
//            val namedScreenHandlerFactory = this.createScreenHandlerFactory(state, world, pos)
//            if (namedScreenHandlerFactory != null) {
//                entity.openHandledScreen(namedScreenHandlerFactory)
//                entity.incrementStat(this.openStat)
//                PiglinBrain.onGuardedBlockInteracted(entity, true)
//            }
//
//            return ActionResult.CONSUME
//        }
//    }
//
//    protected open val openStat: Stat<Identifier?>?
//        get() = Stats.CUSTOM.getOrCreateStat(Stats.OPEN_CHEST)
//
//    val expectedEntityType: BlockEntityType<out ChestBlockEntity>
//        get() = entityTypeRetriever.get() as BlockEntityType<*>
//
//    override fun getBlockEntitySource(
//        state: BlockState,
//        world: World,
//        pos: BlockPos,
//        ignoreBlocked: Boolean
//    ): DoubleBlockProperties.PropertySource<out ChestBlockEntity> {
//        val biPredicate: BiPredicate<*, *>
//        if (ignoreBlocked) {
//            biPredicate = BiPredicate { worldx: Any?, posx: Any? -> false }
//        } else {
//            biPredicate = BiPredicate<*, *> { world: *, pos: * ->
//                isChestBlocked(
//                    world,
//                    pos
//                )
//            }
//        }
//
//        return DoubleBlockProperties.toPropertySource<BlockEntity>(
//            entityTypeRetriever.get() as BlockEntityType<*>,
//            Function { state: BlockState ->
//                getDoubleBlockType(
//                    state
//                )
//            },
//            Function { state: BlockState ->
//                getFacing(
//                    state
//                )
//            }, FACING, state, world, pos, biPredicate
//        )
//    }
//
//    override fun createScreenHandlerFactory(
//        state: BlockState,
//        world: World,
//        pos: BlockPos
//    ): NamedScreenHandlerFactory? {
//        return (getBlockEntitySource(
//            state,
//            world,
//            pos,
//            false
//        ).apply(NAME_RETRIEVER) as Optional<*>).orElse(null as Any?) as NamedScreenHandlerFactory
//    }
//
//    override fun createBlockEntity(pos: BlockPos, state: BlockState): BlockEntity? {
//        return ChestBlockEntity(pos, state)
//    }
//
//    override fun <T : BlockEntity?> getTicker(
//        world: World,
//        state: BlockState,
//        type: BlockEntityType<T>
//    ): BlockEntityTicker<T>? {
//        return if (world.isClient) checkType(
//            type,
//            expectedEntityType
//        ) { world: World?, pos: BlockPos?, state: BlockState?, blockEntity: ChestBlockEntity? ->
//            ChestBlockEntity.clientTick(
//                world,
//                pos,
//                state,
//                blockEntity
//            )
//        } else null
//    }
//
//    override fun hasComparatorOutput(state: BlockState): Boolean {
//        return true
//    }
//
//    override fun getComparatorOutput(state: BlockState, world: World, pos: BlockPos): Int {
//        return ScreenHandler.calculateComparatorOutput(getInventory(this, state, world, pos, false))
//    }
//
//    override fun rotate(state: BlockState, rotation: BlockRotation): BlockState {
//        return state.with(FACING, rotation.rotate(state.get(FACING) as Direction)) as BlockState
//    }
//
//    override fun mirror(state: BlockState, mirror: BlockMirror): BlockState {
//        return state.rotate(mirror.getRotation(state.get(FACING) as Direction))
//    }
//
//    override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) {
//        builder.add(*arrayOf<Property<*>>(FACING, CHEST_TYPE, WATERLOGGED))
//    }
//
//    override fun canPathfindThrough(state: BlockState, navigationType: NavigationType): Boolean {
//        return false
//    }
//
//    override fun scheduledTick(state: BlockState, world: ServerWorld, pos: BlockPos, random: RandomGenerator) {
//        val blockEntity = world.getBlockEntity(pos)!!
//        if (blockEntity is ChestBlockEntity) {
//            blockEntity.onScheduledTick()
//        }
//    }
//
//    init {
//        this.defaultState =
//            (((stateManager.defaultState as BlockState).with(FACING, Direction.NORTH) as BlockState).with(
//                CHEST_TYPE, ChestType.SINGLE
//            ) as BlockState).with(WATERLOGGED, false) as BlockState
//    }
//
//    companion object {
//        val CODEC: MapCodec<ChestBlock> = createCodec { settings: Settings? ->
//            ChestBlock(
//                settings
//            ) { BlockEntityType.CHEST }
//        }
//        val FACING: DirectionProperty = HorizontalFacingBlock.FACING
//        val CHEST_TYPE: EnumProperty<ChestType> = Properties.CHEST_TYPE
//        val WATERLOGGED: BooleanProperty = Properties.WATERLOGGED
//        const val EVENT_SET_OPEN_COUNT: Int = 1
//        protected const val SHAPE_OFFSET: Int = 1
//        protected const val SHAPE_HEIGHT: Int = 14
//        protected val DOUBLE_NORTH_SHAPE: VoxelShape =
//            createCuboidShape(1.0, 0.0, 0.0, 15.0, 14.0, 15.0)
//        protected val DOUBLE_SOUTH_SHAPE: VoxelShape =
//            createCuboidShape(1.0, 0.0, 1.0, 15.0, 14.0, 16.0)
//        protected val DOUBLE_WEST_SHAPE: VoxelShape =
//            createCuboidShape(0.0, 0.0, 1.0, 15.0, 14.0, 15.0)
//        protected val DOUBLE_EAST_SHAPE: VoxelShape =
//            createCuboidShape(1.0, 0.0, 1.0, 16.0, 14.0, 15.0)
//        protected val SINGLE_SHAPE: VoxelShape =
//            createCuboidShape(1.0, 0.0, 1.0, 15.0, 14.0, 15.0)
//        private val INVENTORY_RETRIEVER: PropertyRetriever<ChestBlockEntity, Optional<Inventory>> =
//            object : PropertyRetriever<ChestBlockEntity, Optional<Inventory>> {
//                override fun getFromBoth(
//                    chestBlockEntity: ChestBlockEntity,
//                    chestBlockEntity2: ChestBlockEntity
//                ): Optional<Inventory> {
//                    return Optional.of(DoubleInventory(chestBlockEntity, chestBlockEntity2))
//                }
//
//                override fun getFrom(chestBlockEntity: ChestBlockEntity): Optional<Inventory> {
//                    return Optional.of(chestBlockEntity)
//                }
//
//                override fun getFallback(): Optional<Inventory> {
//                    return Optional.empty()
//                }
//            }
//        private val NAME_RETRIEVER: PropertyRetriever<ChestBlockEntity, Optional<NamedScreenHandlerFactory>> =
//            object : PropertyRetriever<ChestBlockEntity, Optional<NamedScreenHandlerFactory>> {
//                override fun getFromBoth(
//                    chestBlockEntity: ChestBlockEntity,
//                    chestBlockEntity2: ChestBlockEntity
//                ): Optional<NamedScreenHandlerFactory> {
//                    val inventory: Inventory = DoubleInventory(chestBlockEntity, chestBlockEntity2)
//                    return Optional.of<NamedScreenHandlerFactory>(object : NamedScreenHandlerFactory(this) {
//                        override fun createMenu(
//                            i: Int,
//                            playerInventory: PlayerInventory,
//                            playerEntity: PlayerEntity
//                        ): ScreenHandler? {
//                            if (chestBlockEntity.checkUnlocked(playerEntity) && chestBlockEntity2.checkUnlocked(
//                                    playerEntity
//                                )
//                            ) {
//                                chestBlockEntity.setupLoot(playerInventory.player)
//                                chestBlockEntity2.setupLoot(playerInventory.player)
//                                return GenericContainerScreenHandler.createGeneric9x6(i, playerInventory, inventory)
//                            } else {
//                                return null
//                            }
//                        }
//
//                        override fun getDisplayName(): Text {
//                            return if (chestBlockEntity.hasCustomName()) {
//                                chestBlockEntity.displayName
//                            } else {
//                                (if (chestBlockEntity2.hasCustomName()) chestBlockEntity2.displayName else Text.translatable(
//                                    "container.chestDouble"
//                                )) as Text
//                            }
//                        }
//                    })
//                }
//
//                override fun getFrom(chestBlockEntity: ChestBlockEntity): Optional<NamedScreenHandlerFactory> {
//                    return Optional.of(chestBlockEntity)
//                }
//
//                override fun getFallback(): Optional<NamedScreenHandlerFactory> {
//                    return Optional.empty()
//                }
//            }
//
//        fun getDoubleBlockType(state: BlockState): DoubleBlockProperties.Type {
//            val chestType = state.get(CHEST_TYPE) as ChestType
//            return if (chestType == ChestType.SINGLE) {
//                DoubleBlockProperties.Type.SINGLE
//            } else {
//                if (chestType == ChestType.RIGHT) DoubleBlockProperties.Type.FIRST else DoubleBlockProperties.Type.SECOND
//            }
//        }
//
//        fun getFacing(state: BlockState): Direction {
//            val direction = state.get(FACING) as Direction
//            return if (state.get(CHEST_TYPE) == ChestType.LEFT) direction.rotateYClockwise() else direction.rotateYCounterclockwise()
//        }
//
//        fun getInventory(
//            block: ChestBlock,
//            state: BlockState,
//            world: World,
//            pos: BlockPos,
//            ignoreBlocked: Boolean
//        ): Inventory? {
//            return (block.getBlockEntitySource(state, world, pos, ignoreBlocked)
//                .apply(INVENTORY_RETRIEVER) as Optional<*>).orElse(null as Any?) as Inventory
//        }
//
//        fun getAnimationProgressRetriever(progress: ChestAnimationProgress): PropertyRetriever<ChestBlockEntity, Float2FloatFunction> {
//            return object : PropertyRetriever<ChestBlockEntity, Float2FloatFunction> {
//                override fun getFromBoth(
//                    chestBlockEntity: ChestBlockEntity,
//                    chestBlockEntity2: ChestBlockEntity
//                ): Float2FloatFunction {
//                    return Float2FloatFunction { tickDelta: Float ->
//                        max(
//                            chestBlockEntity.getAnimationProgress(tickDelta).toDouble(),
//                            chestBlockEntity2.getAnimationProgress(tickDelta).toDouble()
//                        )
//                            .toFloat()
//                    }
//                }
//
//                override fun getFrom(chestBlockEntity: ChestBlockEntity): Float2FloatFunction {
//                    Objects.requireNonNull(chestBlockEntity)
//                    return Float2FloatFunction { tickDelta: Double ->
//                        chestBlockEntity.getAnimationProgress(
//                            tickDelta.toFloat()
//                        )
//                    }
//                }
//
//                override fun getFallback(): Float2FloatFunction {
//                    val var10000 = progress
//                    Objects.requireNonNull(var10000)
//                    return Float2FloatFunction { tickDelta: Double ->
//                        var10000.getAnimationProgress(
//                            tickDelta.toFloat()
//                        )
//                    }
//                }
//            }
//        }
//
//        fun isChestBlocked(world: WorldAccess, pos: BlockPos): Boolean {
//            return hasBlockOnTop(world, pos) || hasOcelotOnTop(world, pos)
//        }
//
//        private fun hasBlockOnTop(world: BlockView, pos: BlockPos): Boolean {
//            val blockPos = pos.up()
//            return world.getBlockState(blockPos).isSolidBlock(world, blockPos)
//        }
//
//        private fun hasOcelotOnTop(world: WorldAccess, pos: BlockPos): Boolean {
//            val list = world.getNonSpectatingEntities(
//                CatEntity::class.java, Box(
//                    pos.x.toDouble(),
//                    (pos.y + 1).toDouble(), pos.z.toDouble(),
//                    (pos.x + 1).toDouble(),
//                    (pos.y + 2).toDouble(),
//                    (pos.z + 1).toDouble()
//                )
//            )
//            if (!list.isEmpty()) {
//                val var3: Iterator<*> = list.iterator()
//
//                while (var3.hasNext()) {
//                    val catEntity = var3.next() as CatEntity
//                    if (catEntity.isInSittingPose) {
//                        return true
//                    }
//                }
//            }
//
//            return false
//        }
//    }
//}