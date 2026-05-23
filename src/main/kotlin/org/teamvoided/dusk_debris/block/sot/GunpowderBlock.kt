package org.teamvoided.dusk_debris.block.sot

import com.google.common.collect.ImmutableMap
import com.google.common.collect.Maps.newEnumMap
import com.google.common.collect.Maps.newHashMap
import com.google.common.collect.Sets
import com.mojang.serialization.MapCodec
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.Direction.Plane
import net.minecraft.core.particles.ParticleTypes
import net.minecraft.server.level.ServerLevel
import net.minecraft.stats.Stats
import net.minecraft.util.RandomSource
import net.minecraft.world.InteractionHand
import net.minecraft.world.ItemInteractionResult
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.Level
import net.minecraft.world.level.LevelAccessor
import net.minecraft.world.level.LevelReader
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.HorizontalDirectionalBlock
import net.minecraft.world.level.block.TntBlock
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.world.level.block.state.properties.Property
import net.minecraft.world.level.block.state.properties.RedstoneSide
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.shapes.CollisionContext
import net.minecraft.world.phys.shapes.Shapes
import net.minecraft.world.phys.shapes.VoxelShape
import org.teamvoided.dusk_debris.data.tags.DuskBlockTags
import org.teamvoided.dusk_debris.data.tags.DuskItemTags


class GunpowderBlock(settings: Properties) : Block(settings) {
    private val dotState: BlockState
    private var powderIgnites = true

    init {
        this.registerDefaultState(
            stateDefinition.any()
                .setValue(WIRE_CONNECTION_NORTH, RedstoneSide.NONE)
                .setValue(WIRE_CONNECTION_SOUTH, RedstoneSide.NONE)
                .setValue(WIRE_CONNECTION_EAST, RedstoneSide.NONE)
                .setValue(WIRE_CONNECTION_WEST, RedstoneSide.NONE)
                .setValue(LIT, false)
        )
        this.dotState = defaultBlockState()
            .setValue(WIRE_CONNECTION_NORTH, RedstoneSide.SIDE)
            .setValue(WIRE_CONNECTION_SOUTH, RedstoneSide.SIDE)
            .setValue(WIRE_CONNECTION_EAST, RedstoneSide.SIDE)
            .setValue(WIRE_CONNECTION_WEST, RedstoneSide.SIDE)

        for (blockState in stateDefinition.possibleStates) {
            if (blockState.getValue(LIT) == false) {
                SHAPES[blockState] = getShapeForState(blockState)
            }
        }
    }

    override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block, BlockState>) {
        builder.add(
            LIT,
            WIRE_CONNECTION_NORTH,
            WIRE_CONNECTION_SOUTH,
            WIRE_CONNECTION_EAST,
            WIRE_CONNECTION_WEST
        )
    }

    override fun getStateForPlacement(ctx: BlockPlaceContext): BlockState? {
        var state = super.getStateForPlacement(ctx) ?: return null
        val world = ctx.level
        for (direction in Plane.HORIZONTAL) {
            val pos = ctx.clickedPos.relative(direction)
            val levelState = world.getBlockState(pos)
            if (levelState.isRedstoneConductor(world, pos)) {
                val upState = world.getBlockState(pos.above())
                if (connectsTo(upState)) {
                    state = state.setValue(DIRECTION_TO_WIRE_CONNECTION_PROPERTY[direction], RedstoneSide.UP)
                }
            } else {
                val downState = world.getBlockState(pos.below())
                if (connectsTo(downState) || connectsTo(levelState)) {
                    state = state.setValue(DIRECTION_TO_WIRE_CONNECTION_PROPERTY[direction], RedstoneSide.SIDE)
                }
            }
        }

        return state
    }

    override fun useItemOn(
        stack: ItemStack,
        state: BlockState,
        world: Level,
        pos: BlockPos,
        entity: Player,
        hand: InteractionHand,
        hitResult: BlockHitResult
    ): ItemInteractionResult {
        if (!stack.`is`(DuskItemTags.IGNITES_GUNPOWDER)) {
            return super.useItemOn(stack, state, world, pos, entity, hand, hitResult)
        } else {
            world.scheduleTick(pos, this, 0)
            val item = stack.item
            if (stack.isDamageableItem) {
                stack.hurtAndBreak(1, entity, LivingEntity.getSlotForHand(hand))
            } else {
                stack.consume(1, entity)
            }

            entity.awardStat(Stats.ITEM_USED.get(item))
            return ItemInteractionResult.sidedSuccess(world.isClientSide)
        }
    }

    override fun tick(state: BlockState, world: ServerLevel, pos: BlockPos, random: RandomSource) {
        if (state.getValue(LIT)) {
            for (direction in Plane.HORIZONTAL) {
                val offsetPos = pos.relative(direction)
                val tntState = world.getBlockState(offsetPos)
                if (tntState.block is TntBlock) {
                    TntBlock.explode(world, offsetPos)
                    world.removeBlock(offsetPos, false)
                }
            }
            world.destroyBlock(pos, false)
        } else {
            world.setBlockAndUpdate(pos, state.setValue(LIT, true))
            world.scheduleTick(pos, this, gunpowderIgniteDelayDestruction())
        }
    }

    private fun getShapeForState(state: BlockState): VoxelShape {
        var voxelShape = DOT_SHAPE

        for (direction in Plane.HORIZONTAL) {
            val wireConnection = state.getValue(DIRECTION_TO_WIRE_CONNECTION_PROPERTY[direction])
            if (wireConnection == RedstoneSide.SIDE) {
                voxelShape = Shapes.or(voxelShape, SHAPES_FLOOR[direction])
            } else if (wireConnection == RedstoneSide.UP) {
                voxelShape = Shapes.or(voxelShape, SHAPES_UP[direction])
            }
        }

        return voxelShape
    }

    override fun getShape(
        state: BlockState,
        world: BlockGetter,
        pos: BlockPos,
        context: CollisionContext
    ): VoxelShape {
        return SHAPES[state.setValue(LIT, false)] as VoxelShape
    }

    override fun canSurvive(state: BlockState, world: LevelReader, pos: BlockPos): Boolean {
        val blockPos = pos.below()
        val blockState = world.getBlockState(blockPos)
        return this.canRunOnTop(world, blockPos, blockState)
    }

    override fun updateShape(
        state: BlockState,
        direction: Direction,
        neighborState: BlockState,
        world: LevelAccessor,
        blockPos: BlockPos,
        neighborPos: BlockPos
    ): BlockState {

        if (direction == Direction.DOWN || direction == Direction.UP) return state
        var outputState = state
        for (dir in Plane.HORIZONTAL) {
            val pos = blockPos.relative(direction)
            val levelState = world.getBlockState(pos)
            if (levelState.isRedstoneConductor(world, pos)) {
                val upState = world.getBlockState(pos.above())
                if (connectsTo(upState)) {
                    outputState = state.setValue(DIRECTION_TO_WIRE_CONNECTION_PROPERTY[direction], RedstoneSide.UP)
                    if (ignite(upState)) {
                        world.scheduleTick(blockPos, this, gunpowderIgniteDelay())
                        println("ignite1, ${pos.above()}")
                    }
                }
            } else {
                val downState = world.getBlockState(pos.below())
                if (connectsTo(downState) || connectsTo(levelState)) {
                    outputState = state.setValue(DIRECTION_TO_WIRE_CONNECTION_PROPERTY[direction], RedstoneSide.SIDE)
                    if (ignite(downState)) {
                        world.scheduleTick(blockPos, this, gunpowderIgniteDelay())
                        println("ignite2, ${pos.below()}")
                    }
                    if (ignite(levelState)) {
                        world.scheduleTick(blockPos, this, gunpowderIgniteDelay())
                        println("ignite3, $pos")
                    }
                } else {
                    outputState = state.setValue(DIRECTION_TO_WIRE_CONNECTION_PROPERTY[direction], RedstoneSide.NONE)
                }
            }
        }
        return outputState
//        if (direction == Direction.DOWN) {
//            return if (!this.canRunOnTop(world, neighborPos, neighborState)) Blocks.AIR.defaultState else state
//        } else if (direction == Direction.UP) {
//            return this.getPlacementState(world, state, pos)
//        } else {
//            val wireConnection = this.getRenderConnectionType(world, pos, direction)
//            return if (wireConnection.isConnected ==
//                state.get(DIRECTION_TO_WIRE_CONNECTION_PROPERTY[direction]).isConnected &&
//                !isFullyConnected(state)
//            ) state.with(DIRECTION_TO_WIRE_CONNECTION_PROPERTY[direction], wireConnection)
//            else this.getPlacementState(
//                world,
//                dotState.with(IGNITED, state.get(IGNITED))
//                    .with(DIRECTION_TO_WIRE_CONNECTION_PROPERTY[direction], wireConnection), pos
//            )
//        }
    }

//    override fun neighborUpdate(
//        state: BlockState,
//        world: World,
//        pos: BlockPos,
//        block: Block,
//        fromPos: BlockPos,
//        notify: Boolean
//    ) {
//        if (!world.isClient) {
//            if (state.canPlaceAt(world, pos)) {
//                this.update(world, pos, state)
//            } else {
//                dropStacks(state, world, pos)
//                world.removeBlock(pos, false)
//            }
//        }
//    }

    override fun updateIndirectNeighbourShapes(state: BlockState, world: LevelAccessor, pos: BlockPos, flags: Int, maxUpdateDepth: Int) {
//        val mutable = BlockPos.Mutable()
//
//        for (direction in Type.HORIZONTAL) {
//            val wireConnection =
//                state.get(DIRECTION_TO_WIRE_CONNECTION_PROPERTY[direction])
//            if (wireConnection != WireConnection.NONE && !world.getBlockState(mutable.set(pos, direction)).isOf(this)) {
//                mutable.move(Direction.DOWN)
//                val blockState = world.getBlockState(mutable)
//                if (blockState.isOf(this)) {
//                    val blockPos = mutable.offset(direction.opposite)
//                    world.updateNeighbor(
//                        direction.opposite,
//                        world.getBlockState(blockPos),
//                        mutable,
//                        blockPos,
//                        flags,
//                        maxUpdateDepth
//                    )
//                }
//
//                mutable.set(pos, direction).move(Direction.UP)
//                val blockState2 = world.getBlockState(mutable)
//                if (blockState2.isOf(this)) {
//                    val blockPos2 = mutable.offset(direction.opposite)
//                    world.updateNeighbor(
//                        direction.opposite,
//                        world.getBlockState(blockPos2),
//                        mutable,
//                        blockPos2,
//                        flags,
//                        maxUpdateDepth
//                    )
//                }
//            }
//        }
    }

    override fun onPlace(state: BlockState, world: Level, pos: BlockPos, oldState: BlockState, notify: Boolean) {
        if (!oldState.`is`(state.block) && !world.isClientSide) {
            this.update(world, pos, state)
            for (direction in Plane.VERTICAL) {
                world.updateNeighborsAt(pos.relative(direction), this)
            }
            this.updateOffsetNeighbors(world, pos)
        }
    }

    override fun onRemove(state: BlockState, world: Level, pos: BlockPos, newState: BlockState, moved: Boolean) {
        if (!moved && !state.`is`(newState.block)) {
            super.onRemove(state, world, pos, newState, moved)
            if (!world.isClientSide) {
                val var6 = Direction.entries.toTypedArray()
                val var7 = var6.size

                for (var8 in 0 until var7) {
                    val direction = var6[var8]
                    world.updateNeighborsAt(pos.relative(direction), this)
                }
                this.update(world, pos, state)
                this.updateOffsetNeighbors(world, pos)
            }
        }
    }

    override fun animateTick(state: BlockState, world: Level, pos: BlockPos, random: RandomSource) {
        if (state.getValue(LIT)) {
            for (direction in Plane.HORIZONTAL) {
                val wireConnection =
                    state.getValue(DIRECTION_TO_WIRE_CONNECTION_PROPERTY[direction] as Property<*>)
                when (wireConnection) {
                    RedstoneSide.UP -> {
                        this.addIgnitedParticles(
                            world, random, pos, direction, Direction.UP, -0.5f, 0.5f
                        )
                        this.addIgnitedParticles(
                            world, random, pos, Direction.DOWN, direction, 0.0f, 0.5f
                        )
                    }

                    RedstoneSide.SIDE -> this.addIgnitedParticles(
                        world, random, pos, Direction.DOWN, direction, 0.0f, 0.5f
                    )

                    RedstoneSide.NONE -> this.addIgnitedParticles(
                        world, random, pos, Direction.DOWN, direction, 0.0f, 0.3f
                    )

                    else -> this.addIgnitedParticles(
                        world, random, pos, Direction.DOWN, direction, 0.0f, 0.3f
                    )
                }
            }
        }
    }
//
//Helper Functions I guess
//
//
//
//
//
//
//
//
//

//    private fun getPlacementState(world: BlockView, state: BlockState, pos: BlockPos): BlockState {
//        var state = state
//        val ifIsNotConnected = isNotConnected(state)
//        state = this.getMissingConnections(world, defaultState, pos)
//        if (ifIsNotConnected || isNotConnected(state)) {
//            return state
//        } else {
//            val north = state.get(WIRE_CONNECTION_NORTH).isConnected
//            val south = state.get(WIRE_CONNECTION_SOUTH).isConnected
//            val east = state.get(WIRE_CONNECTION_EAST).isConnected
//            val west = state.get(WIRE_CONNECTION_WEST).isConnected
//            val notNorthSouth = !north && !south
//            val notEastWest = !east && !west
//            if (!west && notNorthSouth) {
//                state = state.with(WIRE_CONNECTION_WEST, WireConnection.SIDE)
//            }
//            if (!east && notNorthSouth) {
//                state = state.with(WIRE_CONNECTION_EAST, WireConnection.SIDE)
//            }
//            if (!north && notEastWest) {
//                state = state.with(WIRE_CONNECTION_NORTH, WireConnection.SIDE)
//            }
//            if (!south && notEastWest) {
//                state = state.with(WIRE_CONNECTION_SOUTH, WireConnection.SIDE)
//            }
//            return state
//        }
//    }

//    private fun isFullyConnected(state: BlockState): Boolean {
//        return state.get(WIRE_CONNECTION_NORTH).isConnected &&
//                state.get(WIRE_CONNECTION_SOUTH).isConnected &&
//                state.get(WIRE_CONNECTION_EAST).isConnected &&
//                state.get(WIRE_CONNECTION_WEST).isConnected
//    }
//
//    private fun isNotConnected(state: BlockState): Boolean {
//        return !state.get(WIRE_CONNECTION_NORTH).isConnected &&
//                !state.get(WIRE_CONNECTION_SOUTH).isConnected &&
//                !state.get(WIRE_CONNECTION_EAST).isConnected &&
//                !state.get(WIRE_CONNECTION_WEST).isConnected
//    }
//
//    private fun getMissingConnections(world: BlockView, state: BlockState, pos: BlockPos): BlockState {
//        var state = state
//        val bl = !world.getBlockState(pos.up()).isSolidBlock(world, pos)
//
//        for (direction in Type.HORIZONTAL) {
//            if (!(state.get(DIRECTION_TO_WIRE_CONNECTION_PROPERTY[direction]) as WireConnection).isConnected) {
//                val wireConnection: WireConnection = this.getRenderConnectionType(world, pos, direction, bl)
//                state = state.with(DIRECTION_TO_WIRE_CONNECTION_PROPERTY[direction], wireConnection)
//            }
//        }
//
//        return state
//    }
//
//    private fun getRenderConnectionType(world: BlockView, pos: BlockPos, direction: Direction): WireConnection {
//        return this.getRenderConnectionType(
//            world,
//            pos,
//            direction,
//            !world.getBlockState(pos.up()).isSolidBlock(world, pos)
//        )
//    }

//    private fun getRenderConnectionType(
//        world: BlockView,
//        pos: BlockPos,
//        direction: Direction,
//        isNotSolidBlock: Boolean
//    ): WireConnection {
//        val blockPos = pos.offset(direction)
//        val blockState = world.getBlockState(blockPos)
//        if (isNotSolidBlock) {
//            val bl = blockState.block is TrapdoorBlock || this.canRunOnTop(world, blockPos, blockState)
//            if (bl && connectsTo(world.getBlockState(blockPos.up()))) {
//                if (blockState.isSideSolidFullSquare(world, blockPos, direction.opposite)) {
//                    return WireConnection.UP
//                }
//
//                return WireConnection.SIDE
//            }
//        }
//        return if (!connectsTo(blockState) && (blockState.isSolidBlock(
//                world,
//                blockPos
//            ) || !connectsTo(world.getBlockState(blockPos.down())))
//        ) WireConnection.NONE else WireConnection.SIDE
//    }

    private fun canRunOnTop(world: BlockGetter, pos: BlockPos, floor: BlockState): Boolean {
        return floor.isFaceSturdy(world, pos, Direction.UP) || floor.`is`(Blocks.HOPPER)
    }

    private fun update(world: Level, pos: BlockPos, state: BlockState) {
        val i = this.getReceivedIgnition(world, pos)
        if (state.getValue(LIT) != i) {
            if (world.getBlockState(pos) === state) {
                world.setBlock(pos, state.setValue(LIT, i) as BlockState, 2)
            }

            val set: MutableSet<BlockPos> = Sets.newHashSet()
            set.add(pos)
            val directions = Direction.entries.toTypedArray()

            for (direction in directions) {
                set.add(pos.relative(direction))
            }

            for (blockPos in set) {
                world.updateNeighborsAt(blockPos, this)
            }
        }
    }

    private fun getReceivedIgnition(world: Level, pos: BlockPos): Boolean {
        this.powderIgnites = false
        val receiveIgnite = getReceivedIgnition(pos, world)
        this.powderIgnites = true
        var ignited = false
        if (!receiveIgnite) {
            while (true) {
                for (direction in Plane.HORIZONTAL) {
                    val blockPos = pos.relative(direction)
                    val blockState = world.getBlockState(blockPos)
                    ignited = ignited || ignite(blockState)
                    val blockPos2 = pos.above()
                    if (blockState.isRedstoneConductor(world, blockPos) &&
                        !world.getBlockState(blockPos2).isRedstoneConductor(world, blockPos2)
                    ) {
                        ignited = ignited || ignite(world.getBlockState(blockPos.above()))
                    } else if (!blockState.isRedstoneConductor(world, blockPos)) {
                        ignited = ignited || ignite(world.getBlockState(blockPos.below()))
                    }
                }
                return ignited
            }
        } else {
            return false
        }
    }

    private fun getReceivedIgnition(pos: BlockPos, world: Level): Boolean {
        for (element in UPDATE_SHAPE_ORDER) {
            val j = this.getEmittedIgnition(pos.relative(element), world)
            if (j) {
                return true
            }
        }
        return false
    }

    private fun getEmittedIgnition(pos: BlockPos, world: Level): Boolean {
        val ignited = if (world.getBlockState(pos).`is`(this)) world.getBlockState(pos).getValue(LIT) else false
        return ignited
    }

    //remove plz
    private fun ignite(state: BlockState): Boolean {
        return if (state.`is`(this)) state.getValue(LIT) else false
    }

    private fun updateNeighbors(world: Level, pos: BlockPos) {
        if (world.getBlockState(pos).`is`(this)) {
            world.updateNeighborsAt(pos, this)
            val var3 = Direction.entries.toTypedArray()
            val var4 = var3.size

            for (var5 in 0 until var4) {
                val direction = var3[var5]
                world.updateNeighborsAt(pos.relative(direction), this)
            }
        }
    }

    private fun updateOffsetNeighbors(world: Level, pos: BlockPos) {
        for (direction in Plane.HORIZONTAL) {
            this.updateNeighbors(world, pos.relative(direction))
            val blockPos = pos.relative(direction)
            if (world.getBlockState(blockPos).isRedstoneConductor(world, blockPos)) {
                this.updateNeighbors(world, blockPos.above())
            } else {
                this.updateNeighbors(world, blockPos.below())
            }
        }
    }

    private fun addIgnitedParticles(
        world: Level,
        random: RandomSource,
        pos: BlockPos,
        direction: Direction,
        direction2: Direction,
        f: Float,
        g: Float
    ) {
        val h = g - f
        if (!(random.nextFloat() >= h / 2)) {
            val genericOffset = 0.4375f
            val j = f + h * random.nextFloat()
            val x =
                0.5 + (genericOffset * direction.stepX.toFloat()).toDouble() + (j * direction2.stepX.toFloat()).toDouble()
            val y =
                0.5 + (genericOffset * direction.stepY.toFloat()).toDouble() + (j * direction2.stepY.toFloat()).toDouble()
            val z =
                0.5 + (genericOffset * direction.stepZ.toFloat()).toDouble() + (j * direction2.stepZ.toFloat()).toDouble()
            world.addParticle(
                ParticleTypes.FLAME,
                pos.x.toDouble() + x,
                pos.y.toDouble() + y,
                pos.z.toDouble() + z,
                0.0,
                0.0,
                0.0
            )
        }
    }

    companion object {
        val CODEC: MapCodec<GunpowderBlock> = HorizontalDirectionalBlock.simpleCodec { settings: Properties ->
            GunpowderBlock(
                settings
            )
        }

        fun gunpowderIgniteDelayDestruction(): Int {
            return (10 + Math.random() * 150).toInt()
        }

        fun gunpowderIgniteDelay(): Int {
            return 10
        }

        fun connectsTo(state: BlockState): Boolean {
            return state.`is`(DuskBlockTags.GUNPOWDER_CONNECTS_TO)
        }

        var LIT = BlockStateProperties.LIT
        val WIRE_CONNECTION_NORTH = BlockStateProperties.NORTH_REDSTONE
        val WIRE_CONNECTION_EAST = BlockStateProperties.EAST_REDSTONE
        val WIRE_CONNECTION_SOUTH = BlockStateProperties.SOUTH_REDSTONE
        val WIRE_CONNECTION_WEST = BlockStateProperties.WEST_REDSTONE
        val DIRECTION_TO_WIRE_CONNECTION_PROPERTY =
            newEnumMap(
                ImmutableMap.of(
                    Direction.NORTH, WIRE_CONNECTION_NORTH,
                    Direction.EAST, WIRE_CONNECTION_EAST,
                    Direction.SOUTH, WIRE_CONNECTION_SOUTH,
                    Direction.WEST, WIRE_CONNECTION_WEST
                )
            )
        val DOT_SHAPE = box(3.0, 0.0, 3.0, 13.0, 1.0, 13.0)
        val SHAPES_FLOOR = newEnumMap(
            ImmutableMap.of(
                Direction.NORTH, box(3.0, 0.0, 0.0, 13.0, 1.0, 13.0),
                Direction.SOUTH, box(3.0, 0.0, 3.0, 13.0, 1.0, 16.0),
                Direction.EAST, box(3.0, 0.0, 3.0, 16.0, 1.0, 13.0),
                Direction.WEST, box(0.0, 0.0, 3.0, 13.0, 1.0, 13.0)
            )
        )
        val SHAPES_UP = newEnumMap(
            ImmutableMap.of(
                Direction.NORTH,
                Shapes.or(
                    SHAPES_FLOOR[Direction.NORTH],
                    box(3.0, 0.0, 0.0, 13.0, 16.0, 1.0)
                ),
                Direction.SOUTH,
                Shapes.or(
                    SHAPES_FLOOR[Direction.SOUTH],
                    box(3.0, 0.0, 15.0, 13.0, 16.0, 16.0)
                ),
                Direction.EAST,
                Shapes.or(
                    SHAPES_FLOOR[Direction.EAST],
                    box(15.0, 0.0, 3.0, 16.0, 16.0, 13.0)
                ),
                Direction.WEST,
                Shapes.or(
                    SHAPES_FLOOR[Direction.WEST],
                    box(0.0, 0.0, 3.0, 1.0, 16.0, 13.0)
                )
            )
        )
        val SHAPES = newHashMap<BlockState, VoxelShape>()
    }
}