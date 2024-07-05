package org.teamvoided.dusk_debris.block

import com.google.common.collect.ImmutableMap
import com.google.common.collect.Maps.newEnumMap
import com.google.common.collect.Maps.newHashMap
import com.google.common.collect.Sets
import com.mojang.serialization.MapCodec
import net.minecraft.block.*
import net.minecraft.block.enums.WireConnection
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemPlacementContext
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.particle.ParticleTypes
import net.minecraft.server.world.ServerWorld
import net.minecraft.stat.Stats
import net.minecraft.state.StateManager
import net.minecraft.state.property.Properties
import net.minecraft.state.property.Property
import net.minecraft.util.Hand
import net.minecraft.util.ItemInteractionResult
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.math.Direction.Type
import net.minecraft.util.random.RandomGenerator
import net.minecraft.util.shape.VoxelShape
import net.minecraft.util.shape.VoxelShapes
import net.minecraft.world.BlockView
import net.minecraft.world.World
import net.minecraft.world.WorldAccess
import net.minecraft.world.WorldView
import org.teamvoided.dusk_debris.data.DuskBlockTags
import org.teamvoided.dusk_debris.data.DuskItemTags


class GunpowderBlock(settings: Settings) : Block(settings) {
    private val dotState: BlockState
    private var powderIgnites = true

    init {
        this.defaultState = stateManager.defaultState
            .with(WIRE_CONNECTION_NORTH, WireConnection.NONE)
            .with(WIRE_CONNECTION_SOUTH, WireConnection.NONE)
            .with(WIRE_CONNECTION_EAST, WireConnection.NONE)
            .with(WIRE_CONNECTION_WEST, WireConnection.NONE)
            .with(LIT, false)
        this.dotState = defaultState
            .with(WIRE_CONNECTION_NORTH, WireConnection.SIDE)
            .with(WIRE_CONNECTION_SOUTH, WireConnection.SIDE)
            .with(WIRE_CONNECTION_EAST, WireConnection.SIDE)
            .with(WIRE_CONNECTION_WEST, WireConnection.SIDE)

        for (blockState in getStateManager().states) {
            if (blockState.get(LIT) == false) {
                SHAPES[blockState] = getShapeForState(blockState)
            }
        }
    }

    override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) {
        builder.add(
            LIT,
            WIRE_CONNECTION_NORTH,
            WIRE_CONNECTION_SOUTH,
            WIRE_CONNECTION_EAST,
            WIRE_CONNECTION_WEST
        )
    }

    override fun getPlacementState(ctx: ItemPlacementContext): BlockState? {
        var state = super.getPlacementState(ctx) ?: return null
        val world = ctx.world
        for (direction in Type.HORIZONTAL) {
            val pos = ctx.blockPos.offset(direction)
            val levelState = world.getBlockState(pos)
            if (levelState.isSolidBlock(world, pos)) {
                val upState = world.getBlockState(pos.up())
                if (connectsTo(upState)) {
                    state = state.with(DIRECTION_TO_WIRE_CONNECTION_PROPERTY[direction], WireConnection.UP)
                }
            } else {
                val downState = world.getBlockState(pos.down())
                if (connectsTo(downState) || connectsTo(levelState)) {
                    state = state.with(DIRECTION_TO_WIRE_CONNECTION_PROPERTY[direction], WireConnection.SIDE)
                }
            }
        }

        return state
    }

    override fun onInteract(
        stack: ItemStack,
        state: BlockState,
        world: World,
        pos: BlockPos,
        entity: PlayerEntity,
        hand: Hand,
        hitResult: BlockHitResult
    ): ItemInteractionResult {
        if (!stack.isIn(DuskItemTags.IGNITES_GUNPOWDER)) {
            return super.onInteract(stack, state, world, pos, entity, hand, hitResult)
        } else {
            world.scheduleBlockTick(pos, this, 0)
            val item = stack.item
            if (stack.isDamageable) {
                stack.damageEquipment(1, entity, LivingEntity.getHand(hand))
            } else {
                stack.consume(1, entity)
            }

            entity.incrementStat(Stats.USED.getOrCreateStat(item))
            return ItemInteractionResult.success(world.isClient)
        }
    }

    override fun scheduledTick(state: BlockState, world: ServerWorld, pos: BlockPos, random: RandomGenerator) {
        if (state.get(LIT)) {
            for (direction in Type.HORIZONTAL) {
                val offsetPos = pos.offset(direction)
                val tntState = world.getBlockState(offsetPos)
                if (tntState.block is TntBlock) {
                    TntBlock.primeTnt(world, offsetPos)
                    world.removeBlock(offsetPos, false)
                }
            }
            world.breakBlock(pos, false)
        } else {
            world.setBlockState(pos, state.with(LIT, true))
            world.scheduleBlockTick(pos, this, gunpowderIgniteDelayDestruction())
        }
    }

    private fun getShapeForState(state: BlockState): VoxelShape {
        var voxelShape = DOT_SHAPE

        for (direction in Type.HORIZONTAL) {
            val wireConnection = state.get(DIRECTION_TO_WIRE_CONNECTION_PROPERTY[direction])
            if (wireConnection == WireConnection.SIDE) {
                voxelShape = VoxelShapes.union(voxelShape, SHAPES_FLOOR[direction])
            } else if (wireConnection == WireConnection.UP) {
                voxelShape = VoxelShapes.union(voxelShape, SHAPES_UP[direction])
            }
        }

        return voxelShape
    }

    override fun getOutlineShape(
        state: BlockState,
        world: BlockView,
        pos: BlockPos,
        context: ShapeContext
    ): VoxelShape {
        return SHAPES[state.with(LIT, false)] as VoxelShape
    }

    override fun canPlaceAt(state: BlockState, world: WorldView, pos: BlockPos): Boolean {
        val blockPos = pos.down()
        val blockState = world.getBlockState(blockPos)
        return this.canRunOnTop(world, blockPos, blockState)
    }

    override fun getStateForNeighborUpdate(
        state: BlockState,
        direction: Direction,
        neighborState: BlockState,
        world: WorldAccess,
        blockPos: BlockPos,
        neighborPos: BlockPos
    ): BlockState {

        if (direction == Direction.DOWN || direction == Direction.UP) return state
        var outputState = state
        for (dir in Type.HORIZONTAL) {
            val pos = blockPos.offset(direction)
            val levelState = world.getBlockState(pos)
            if (levelState.isSolidBlock(world, pos)) {
                val upState = world.getBlockState(pos.up())
                if (connectsTo(upState)) {
                    outputState = state.with(DIRECTION_TO_WIRE_CONNECTION_PROPERTY[direction], WireConnection.UP)
                    if (ignite(upState)) {
                        world.scheduleBlockTick(blockPos, this, gunpowderIgniteDelay())
                        println("ignite1, ${pos.up()}")
                    }
                }
            } else {
                val downState = world.getBlockState(pos.down())
                if (connectsTo(downState) || connectsTo(levelState)) {
                    outputState = state.with(DIRECTION_TO_WIRE_CONNECTION_PROPERTY[direction], WireConnection.SIDE)
                    if (ignite(downState)) {
                        world.scheduleBlockTick(blockPos, this, gunpowderIgniteDelay())
                        println("ignite2, ${pos.down()}")
                    }
                    if (ignite(levelState)) {
                        world.scheduleBlockTick(blockPos, this, gunpowderIgniteDelay())
                        println("ignite3, $pos")
                    }
                } else {
                    outputState = state.with(DIRECTION_TO_WIRE_CONNECTION_PROPERTY[direction], WireConnection.NONE)
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

    override fun prepare(state: BlockState, world: WorldAccess, pos: BlockPos, flags: Int, maxUpdateDepth: Int) {
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

    override fun onBlockAdded(state: BlockState, world: World, pos: BlockPos, oldState: BlockState, notify: Boolean) {
        if (!oldState.isOf(state.block) && !world.isClient) {
            this.update(world, pos, state)
            for (direction in Type.VERTICAL) {
                world.updateNeighborsAlways(pos.offset(direction), this)
            }
            this.updateOffsetNeighbors(world, pos)
        }
    }

    override fun onStateReplaced(state: BlockState, world: World, pos: BlockPos, newState: BlockState, moved: Boolean) {
        if (!moved && !state.isOf(newState.block)) {
            super.onStateReplaced(state, world, pos, newState, moved)
            if (!world.isClient) {
                val var6 = Direction.entries.toTypedArray()
                val var7 = var6.size

                for (var8 in 0 until var7) {
                    val direction = var6[var8]
                    world.updateNeighborsAlways(pos.offset(direction), this)
                }
                this.update(world, pos, state)
                this.updateOffsetNeighbors(world, pos)
            }
        }
    }

    override fun randomDisplayTick(state: BlockState, world: World, pos: BlockPos, random: RandomGenerator) {
        if (state.get(LIT)) {
            for (direction in Type.HORIZONTAL) {
                val wireConnection =
                    state.get(DIRECTION_TO_WIRE_CONNECTION_PROPERTY[direction] as Property<*>)
                when (wireConnection) {
                    WireConnection.UP -> {
                        this.addIgnitedParticles(
                            world, random, pos, direction, Direction.UP, -0.5f, 0.5f
                        )
                        this.addIgnitedParticles(
                            world, random, pos, Direction.DOWN, direction, 0.0f, 0.5f
                        )
                    }

                    WireConnection.SIDE -> this.addIgnitedParticles(
                        world, random, pos, Direction.DOWN, direction, 0.0f, 0.5f
                    )

                    WireConnection.NONE -> this.addIgnitedParticles(
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

    private fun canRunOnTop(world: BlockView, pos: BlockPos, floor: BlockState): Boolean {
        return floor.isSideSolidFullSquare(world, pos, Direction.UP) || floor.isOf(Blocks.HOPPER)
    }

    private fun update(world: World, pos: BlockPos, state: BlockState) {
        val i = this.getReceivedIgnition(world, pos)
        if (state.get(LIT) != i) {
            if (world.getBlockState(pos) === state) {
                world.setBlockState(pos, state.with(LIT, i) as BlockState, 2)
            }

            val set: MutableSet<BlockPos> = Sets.newHashSet()
            set.add(pos)
            val directions = Direction.entries.toTypedArray()

            for (direction in directions) {
                set.add(pos.offset(direction))
            }

            for (blockPos in set) {
                world.updateNeighborsAlways(blockPos, this)
            }
        }
    }

    private fun getReceivedIgnition(world: World, pos: BlockPos): Boolean {
        this.powderIgnites = false
        val receiveIgnite = getReceivedIgnition(pos, world)
        this.powderIgnites = true
        var ignited = false
        if (!receiveIgnite) {
            while (true) {
                for (direction in Type.HORIZONTAL) {
                    val blockPos = pos.offset(direction)
                    val blockState = world.getBlockState(blockPos)
                    ignited = ignited || ignite(blockState)
                    val blockPos2 = pos.up()
                    if (blockState.isSolidBlock(world, blockPos) &&
                        !world.getBlockState(blockPos2).isSolidBlock(world, blockPos2)
                    ) {
                        ignited = ignited || ignite(world.getBlockState(blockPos.up()))
                    } else if (!blockState.isSolidBlock(world, blockPos)) {
                        ignited = ignited || ignite(world.getBlockState(blockPos.down()))
                    }
                }
                return ignited
            }
        } else {
            return false
        }
    }

    private fun getReceivedIgnition(pos: BlockPos, world: World): Boolean {
        for (element in DIRECTIONS) {
            val j = this.getEmittedIgnition(pos.offset(element), world)
            if (j) {
                return true
            }
        }
        return false
    }

    private fun getEmittedIgnition(pos: BlockPos, world: World): Boolean {
        val ignited = if (world.getBlockState(pos).isOf(this)) world.getBlockState(pos).get(LIT) else false
        return ignited
    }

    //remove plz
    private fun ignite(state: BlockState): Boolean {
        return if (state.isOf(this)) state.get(LIT) else false
    }

    private fun updateNeighbors(world: World, pos: BlockPos) {
        if (world.getBlockState(pos).isOf(this)) {
            world.updateNeighborsAlways(pos, this)
            val var3 = Direction.entries.toTypedArray()
            val var4 = var3.size

            for (var5 in 0 until var4) {
                val direction = var3[var5]
                world.updateNeighborsAlways(pos.offset(direction), this)
            }
        }
    }

    private fun updateOffsetNeighbors(world: World, pos: BlockPos) {
        for (direction in Type.HORIZONTAL) {
            this.updateNeighbors(world, pos.offset(direction))
            val blockPos = pos.offset(direction)
            if (world.getBlockState(blockPos).isSolidBlock(world, blockPos)) {
                this.updateNeighbors(world, blockPos.up())
            } else {
                this.updateNeighbors(world, blockPos.down())
            }
        }
    }

    private fun addIgnitedParticles(
        world: World,
        random: RandomGenerator,
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
                0.5 + (genericOffset * direction.offsetX.toFloat()).toDouble() + (j * direction2.offsetX.toFloat()).toDouble()
            val y =
                0.5 + (genericOffset * direction.offsetY.toFloat()).toDouble() + (j * direction2.offsetY.toFloat()).toDouble()
            val z =
                0.5 + (genericOffset * direction.offsetZ.toFloat()).toDouble() + (j * direction2.offsetZ.toFloat()).toDouble()
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
        val CODEC: MapCodec<GunpowderBlock> = HorizontalFacingBlock.createCodec { settings: Settings ->
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
            return state.isIn(DuskBlockTags.GUNPOWDER_CONNECTS_TO)
        }

        var LIT = Properties.LIT
        val WIRE_CONNECTION_NORTH = Properties.NORTH_WIRE_CONNECTION
        val WIRE_CONNECTION_EAST = Properties.EAST_WIRE_CONNECTION
        val WIRE_CONNECTION_SOUTH = Properties.SOUTH_WIRE_CONNECTION
        val WIRE_CONNECTION_WEST = Properties.WEST_WIRE_CONNECTION
        val DIRECTION_TO_WIRE_CONNECTION_PROPERTY =
            newEnumMap(
                ImmutableMap.of(
                    Direction.NORTH, WIRE_CONNECTION_NORTH,
                    Direction.EAST, WIRE_CONNECTION_EAST,
                    Direction.SOUTH, WIRE_CONNECTION_SOUTH,
                    Direction.WEST, WIRE_CONNECTION_WEST
                )
            )
        val DOT_SHAPE = createCuboidShape(3.0, 0.0, 3.0, 13.0, 1.0, 13.0)
        val SHAPES_FLOOR = newEnumMap(
            ImmutableMap.of(
                Direction.NORTH, createCuboidShape(3.0, 0.0, 0.0, 13.0, 1.0, 13.0),
                Direction.SOUTH, createCuboidShape(3.0, 0.0, 3.0, 13.0, 1.0, 16.0),
                Direction.EAST, createCuboidShape(3.0, 0.0, 3.0, 16.0, 1.0, 13.0),
                Direction.WEST, createCuboidShape(0.0, 0.0, 3.0, 13.0, 1.0, 13.0)
            )
        )
        val SHAPES_UP = newEnumMap(
            ImmutableMap.of(
                Direction.NORTH,
                VoxelShapes.union(
                    SHAPES_FLOOR[Direction.NORTH],
                    createCuboidShape(3.0, 0.0, 0.0, 13.0, 16.0, 1.0)
                ),
                Direction.SOUTH,
                VoxelShapes.union(
                    SHAPES_FLOOR[Direction.SOUTH],
                    createCuboidShape(3.0, 0.0, 15.0, 13.0, 16.0, 16.0)
                ),
                Direction.EAST,
                VoxelShapes.union(
                    SHAPES_FLOOR[Direction.EAST],
                    createCuboidShape(15.0, 0.0, 3.0, 16.0, 16.0, 13.0)
                ),
                Direction.WEST,
                VoxelShapes.union(
                    SHAPES_FLOOR[Direction.WEST],
                    createCuboidShape(0.0, 0.0, 3.0, 1.0, 16.0, 13.0)
                )
            )
        )
        val SHAPES = newHashMap<BlockState, VoxelShape>()
    }
}