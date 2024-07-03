package org.teamvoided.dusk_debris.block

import com.google.common.collect.ImmutableMap
import com.google.common.collect.Maps.newEnumMap
import com.google.common.collect.Maps.newHashMap
import com.google.common.collect.Sets
import com.google.common.collect.UnmodifiableIterator
import com.mojang.serialization.MapCodec
import net.minecraft.block.*
import net.minecraft.block.enums.WireConnection
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemPlacementContext
import net.minecraft.particle.ParticleTypes
import net.minecraft.state.StateManager
import net.minecraft.state.property.BooleanProperty
import net.minecraft.state.property.EnumProperty
import net.minecraft.state.property.Properties
import net.minecraft.state.property.Property
import net.minecraft.util.ActionResult
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.math.Direction.Type
import net.minecraft.util.random.RandomGenerator
import net.minecraft.util.shape.VoxelShape
import net.minecraft.util.shape.VoxelShapes
import net.minecraft.world.*
import org.teamvoided.dusk_debris.data.DuskBlockTags


class GunpowderBlock(settings: Settings) : Block(settings) {
    private val dotState: BlockState
    private var powderIgnites = true

    init {
        this.defaultState = defaultState
            .with(IGNITED, false)
            .with(WIRE_CONNECTION_NORTH, WireConnection.NONE)
            .with(WIRE_CONNECTION_SOUTH, WireConnection.NONE)
            .with(WIRE_CONNECTION_EAST, WireConnection.NONE)
            .with(WIRE_CONNECTION_WEST, WireConnection.NONE)
        this.dotState = stateManager.defaultState
            .with(WIRE_CONNECTION_NORTH, WireConnection.SIDE)
            .with(WIRE_CONNECTION_SOUTH, WireConnection.SIDE)
            .with(WIRE_CONNECTION_EAST, WireConnection.SIDE)
            .with(WIRE_CONNECTION_WEST, WireConnection.SIDE)

        val var2: UnmodifiableIterator<*> = getStateManager().states.iterator()
        while (var2.hasNext()) {
            val blockState = var2.next() as BlockState
            if (blockState.get(IGNITED) == false) {
                SHAPES[blockState] = getShapeForState(blockState)
            }
        }
    }

    override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) {
        builder.add(
            IGNITED,
            WIRE_CONNECTION_NORTH,
            WIRE_CONNECTION_SOUTH,
            WIRE_CONNECTION_EAST,
            WIRE_CONNECTION_WEST
        )
    }

    private fun getShapeForState(state: BlockState): VoxelShape {
        var voxelShape = DOT_SHAPE
        val var3: Iterator<*> = Type.HORIZONTAL.iterator()

        while (var3.hasNext()) {
            val direction = var3.next()
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
        return SHAPES[state.with(IGNITED, false)] as VoxelShape
    }

    override fun getPlacementState(ctx: ItemPlacementContext): BlockState {
        return this.getPlacementState(ctx.world, this.dotState, ctx.blockPos)
    }

    override fun canPlaceAt(state: BlockState, world: WorldView, pos: BlockPos): Boolean {
        val blockPos = pos.down()
        val blockState = world.getBlockState(blockPos)
        return this.canRunOnTop(world, blockPos, blockState)
    }


    override fun onUse(
        state: BlockState,
        world: World,
        pos: BlockPos,
        entity: PlayerEntity,
        hitResult: BlockHitResult
    ): ActionResult {
        if (!entity.abilities.allowModifyWorld) {
            return ActionResult.PASS
        } else {
            if (isFullyConnected(state) || isNotConnected(state)) {
                var blockState: BlockState =
                    if (isFullyConnected(state)) this.defaultState else this.dotState
                blockState = blockState.with(IGNITED, state.get(IGNITED))
                blockState = this.getPlacementState(world, blockState, pos)
                if (blockState !== state) {
                    world.setBlockState(pos, blockState, 3)
                    this.updateForNewState(world, pos, state, blockState)
                    return ActionResult.SUCCESS
                }
            }

            return ActionResult.PASS
        }
    }

    override fun getStateForNeighborUpdate(
        state: BlockState,
        direction: Direction,
        neighborState: BlockState,
        world: WorldAccess,
        pos: BlockPos,
        neighborPos: BlockPos
    ): BlockState {
        if (direction == Direction.DOWN) {
            return if (!this.canRunOnTop(world, neighborPos, neighborState)) Blocks.AIR.defaultState else state
        } else if (direction == Direction.UP) {
            return this.getPlacementState(world, state, pos)
        } else {
            val wireConnection = this.getRenderConnectionType(world, pos, direction)
            return if (wireConnection.isConnected ==
                state.get(DIRECTION_TO_WIRE_CONNECTION_PROPERTY[direction]).isConnected &&
                !isFullyConnected(state)
            ) state.with(DIRECTION_TO_WIRE_CONNECTION_PROPERTY[direction], wireConnection)
            else this.getPlacementState(
                world,
                dotState.with(IGNITED, state.get(IGNITED))
                    .with(DIRECTION_TO_WIRE_CONNECTION_PROPERTY[direction], wireConnection), pos
            )
        }
    }

    override fun prepare(state: BlockState, world: WorldAccess, pos: BlockPos, flags: Int, maxUpdateDepth: Int) {
        val mutable = BlockPos.Mutable()
        val var7: Iterator<*> = Type.HORIZONTAL.iterator()

        while (var7.hasNext()) {
            val direction = var7.next() as Direction
            val wireConnection =
                state.get(DIRECTION_TO_WIRE_CONNECTION_PROPERTY[direction])
            if (wireConnection != WireConnection.NONE && !world.getBlockState(mutable.set(pos, direction)).isOf(this)) {
                mutable.move(Direction.DOWN)
                val blockState = world.getBlockState(mutable)
                if (blockState.isOf(this)) {
                    val blockPos = mutable.offset(direction.opposite)
                    world.updateNeighbor(
                        direction.opposite,
                        world.getBlockState(blockPos),
                        mutable,
                        blockPos,
                        flags,
                        maxUpdateDepth
                    )
                }

                mutable.set(pos, direction).move(Direction.UP)
                val blockState2 = world.getBlockState(mutable)
                if (blockState2.isOf(this)) {
                    val blockPos2 = mutable.offset(direction.opposite)
                    world.updateNeighbor(
                        direction.opposite,
                        world.getBlockState(blockPos2),
                        mutable,
                        blockPos2,
                        flags,
                        maxUpdateDepth
                    )
                }
            }
        }
    }

    override fun onBlockAdded(state: BlockState, world: World, pos: BlockPos, oldState: BlockState, notify: Boolean) {
        if (!oldState.isOf(state.block) && !world.isClient) {
            this.update(world, pos, state)
            val var6: Iterator<*> = Type.VERTICAL.iterator()

            while (var6.hasNext()) {
                val direction = var6.next() as Direction
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

    override fun neighborUpdate(
        state: BlockState,
        world: World,
        pos: BlockPos,
        block: Block,
        fromPos: BlockPos,
        notify: Boolean
    ) {
        if (!world.isClient) {
            if (state.canPlaceAt(world, pos)) {
                this.update(world, pos, state)
            } else {
                dropStacks(state, world, pos)
                world.removeBlock(pos, false)
            }
        }
    }

    override fun randomDisplayTick(state: BlockState, world: World, pos: BlockPos, random: RandomGenerator) {
        val i = state.get(IGNITED)
        if (i) {
            val var6: Iterator<*> = Type.HORIZONTAL.iterator()

            while (var6.hasNext()) {
                val direction = var6.next() as Direction
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

    private fun getPlacementState(world: BlockView, state: BlockState, pos: BlockPos): BlockState {
        var state = state
        val bl = isNotConnected(state)
        state = this.getMissingConnections(world, defaultState.with(IGNITED, state.get(IGNITED)), pos)
        if (bl && isNotConnected(state)) {
            return state
        } else {
            val bl2 = state.get(WIRE_CONNECTION_NORTH).isConnected
            val bl3 = state.get(WIRE_CONNECTION_SOUTH).isConnected
            val bl4 = state.get(WIRE_CONNECTION_EAST).isConnected
            val bl5 = state.get(WIRE_CONNECTION_WEST).isConnected
            val bl6 = !bl2 && !bl3
            val bl7 = !bl4 && !bl5
            if (!bl5 && bl6) {
                state = state.with(WIRE_CONNECTION_WEST, WireConnection.SIDE)
            }
            if (!bl4 && bl6) {
                state = state.with(WIRE_CONNECTION_EAST, WireConnection.SIDE)
            }
            if (!bl2 && bl7) {
                state = state.with(WIRE_CONNECTION_NORTH, WireConnection.SIDE)
            }
            if (!bl3 && bl7) {
                state = state.with(WIRE_CONNECTION_SOUTH, WireConnection.SIDE)
            }
            return state
        }
    }

    private fun isFullyConnected(state: BlockState): Boolean {
        return state.get(WIRE_CONNECTION_NORTH).isConnected &&
                state.get(WIRE_CONNECTION_SOUTH).isConnected &&
                state.get(WIRE_CONNECTION_EAST).isConnected &&
                state.get(WIRE_CONNECTION_WEST).isConnected
    }

    private fun isNotConnected(state: BlockState): Boolean {
        return !state.get(WIRE_CONNECTION_NORTH).isConnected &&
                !state.get(WIRE_CONNECTION_SOUTH).isConnected &&
                !state.get(WIRE_CONNECTION_EAST).isConnected &&
                !state.get(WIRE_CONNECTION_WEST).isConnected
    }

    private fun getMissingConnections(world: BlockView, state: BlockState, pos: BlockPos): BlockState {
        var state = state
        val bl = !world.getBlockState(pos.up()).isSolidBlock(world, pos)
        val var5: Iterator<*> = Type.HORIZONTAL.iterator()

        while (var5.hasNext()) {
            val direction = var5.next() as Direction
            if (!(state.get(DIRECTION_TO_WIRE_CONNECTION_PROPERTY[direction]) as WireConnection).isConnected) {
                val wireConnection: WireConnection = this.getRenderConnectionType(world, pos, direction, bl)
                state = state.with(DIRECTION_TO_WIRE_CONNECTION_PROPERTY[direction], wireConnection)
            }
        }

        return state
    }

    private fun getRenderConnectionType(world: BlockView, pos: BlockPos, direction: Direction): WireConnection {
        return this.getRenderConnectionType(
            world,
            pos,
            direction,
            !world.getBlockState(pos.up()).isSolidBlock(world, pos)
        )
    }

    private fun getRenderConnectionType(
        world: BlockView,
        pos: BlockPos,
        direction: Direction,
        isNotSolidBlock: Boolean
    ): WireConnection {
        val blockPos = pos.offset(direction)
        val blockState = world.getBlockState(blockPos)
        if (isNotSolidBlock) {
            val bl = blockState.block is TrapdoorBlock || this.canRunOnTop(world, blockPos, blockState)
            if (bl && connectsTo(world.getBlockState(blockPos.up()))) {
                if (blockState.isSideSolidFullSquare(world, blockPos, direction.opposite)) {
                    return WireConnection.UP
                }

                return WireConnection.SIDE
            }
        }
        return if (!connectsTo(blockState) && (blockState.isSolidBlock(
                world,
                blockPos
            ) || !connectsTo(world.getBlockState(blockPos.down())))
        ) WireConnection.NONE else WireConnection.SIDE
    }

    private fun canRunOnTop(world: BlockView, pos: BlockPos, floor: BlockState): Boolean {
        return floor.isSideSolidFullSquare(world, pos, Direction.UP) || floor.isOf(Blocks.HOPPER)
    }

    private fun update(world: World, pos: BlockPos, state: BlockState) {
        val i = this.getReceivedIgnition(world, pos)
        if (state.get(IGNITED) != i) {
            if (world.getBlockState(pos) === state) {
                world.setBlockState(pos, state.with(IGNITED, i) as BlockState, 2)
            }

            val set: MutableSet<BlockPos> = Sets.newHashSet()
            set.add(pos)
            val directions = Direction.entries.toTypedArray()

            for (direction in directions) {
                set.add(pos.offset(direction))
            }

            val var10: Iterator<*> = set.iterator()

            while (var10.hasNext()) {
                val blockPos = var10.next() as BlockPos
                world.updateNeighborsAlways(blockPos, this)
            }
        }
    }

    private fun updateForNewState(world: World, pos: BlockPos, oldState: BlockState, newState: BlockState) {
        val var5: Iterator<*> = Type.HORIZONTAL.iterator()
        while (var5.hasNext()) {
            val direction = var5.next() as Direction
            val blockPos = pos.offset(direction)
            if (oldState.get(DIRECTION_TO_WIRE_CONNECTION_PROPERTY[direction]).isConnected !=
                newState.get(DIRECTION_TO_WIRE_CONNECTION_PROPERTY[direction]).isConnected &&
                world.getBlockState(blockPos).isSolidBlock(world, blockPos)
            ) {
                world.updateNeighborsExcept(blockPos, newState.block, direction.opposite)
            }
        }
    }

    private fun getReceivedIgnition(world: World, pos: BlockPos): Boolean {
        this.powderIgnites = false
        val i = getReceivedIgnition(pos, world)
        this.powderIgnites = true
        var j = false
        if (!i) {
            val var5: Iterator<*> = Type.HORIZONTAL.iterator()
            while (true) {
                while (var5.hasNext()) {
                    val direction = var5.next() as Direction
                    val blockPos = pos.offset(direction)
                    val blockState = world.getBlockState(blockPos)
                    j = j || ignite(blockState)
                    val blockPos2 = pos.up()
                    if (blockState.isSolidBlock(world, blockPos) &&
                        !world.getBlockState(blockPos2).isSolidBlock(world, blockPos2)
                    ) {
                        j = j || ignite(world.getBlockState(blockPos.up()))
                    } else if (!blockState.isSolidBlock(world, blockPos)) {
                        j = j || ignite(world.getBlockState(blockPos.down()))
                    }
                }
                return true
            }
        } else {
            return false
        }
    }

    private fun getReceivedIgnition(pos: BlockPos, world: World): Boolean {
        val var3 = DIRECTIONS
        for (element in var3) {
            val j = this.getEmittedIgnition(pos.offset(element), world)
            if (j) {
                return true
            }
        }
        return false
    }

    private fun getEmittedIgnition(pos: BlockPos, world: World): Boolean {
        val blockState: BlockState = world.getBlockState(pos)
        val i = getEmittedStrongIgnition(pos, world)
        return if (blockState.isSolidBlock(world, pos))
            i || this.getReceivedStrongIgnition(pos, world)
        else i
    }

    private fun getReceivedStrongIgnition(pos: BlockPos, world: World): Boolean {
        val ignite = getEmittedStrongIgnition(pos.down(), world) ||
                getEmittedStrongIgnition(pos.up(), world) ||
                getEmittedStrongIgnition(pos.north(), world) ||
                getEmittedStrongIgnition(pos.south(), world) ||
                getEmittedStrongIgnition(pos.west(), world) ||
                getEmittedStrongIgnition(pos.east(), world)
        return ignite
    }
//Is this done for efficiency?
//    fun getReceivedStrongIgnition(pos: BlockPos, world: World): Boolean {
//        var ignite = false
//        ignite = ignite || this.getEmittedStrongIgnition(pos.down(), Direction.DOWN, world)
//        if (ignite) {
//            return true
//        } else {
//            ignite = false || this.getEmittedStrongIgnition(pos.up(), Direction.UP, world)
//            if (ignite) {
//                return true
//            } else {
//                ignite = false || this.getEmittedStrongIgnition(pos.north(), Direction.NORTH, world)
//                if (ignite) {
//                    return true
//                } else {
//                    ignite = false || this.getEmittedStrongIgnition(pos.south(), Direction.SOUTH, world)
//                    if (ignite) {
//                        return true
//                    } else {
//                        ignite = false || this.getEmittedStrongIgnition(pos.west(), Direction.WEST, world)
//                        if (ignite) {
//                            return true
//                        } else {
//                            ignite = false || this.getEmittedStrongIgnition(pos.east(), Direction.EAST, world)
//                            return ignite
//                        }
//                    }
//                }
//            }
//        }
//    }

    private fun getEmittedStrongIgnition(pos: BlockPos, world: World): Boolean {
        val ignited = if (world.getBlockState(pos).isOf(this)) world.getBlockState(pos).get(IGNITED) else false
        return ignited
    }

    private fun ignite(state: BlockState): Boolean {
        return if (state.isOf(this)) state.get(IGNITED) else false
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
        var var3: Iterator<*> = Type.HORIZONTAL.iterator()

        var direction: Direction
        while (var3.hasNext()) {
            direction = var3.next() as Direction
            this.updateNeighbors(world, pos.offset(direction))
        }

        var3 = Type.HORIZONTAL.iterator()

        while (var3.hasNext()) {
            direction = var3.next() as Direction
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
        if (!(random.nextFloat() >= 0.2f * h)) {
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

        fun connectsTo(state: BlockState): Boolean {
            return state.isIn(DuskBlockTags.GUNPOWDER_CONNECTS_TO)
        }

        var IGNITED: BooleanProperty = BooleanProperty.of("ignited")
        val WIRE_CONNECTION_NORTH = Properties.NORTH_WIRE_CONNECTION
        val WIRE_CONNECTION_EAST = Properties.EAST_WIRE_CONNECTION
        val WIRE_CONNECTION_SOUTH = Properties.SOUTH_WIRE_CONNECTION
        val WIRE_CONNECTION_WEST = Properties.WEST_WIRE_CONNECTION
        val DIRECTION_TO_WIRE_CONNECTION_PROPERTY =
            newEnumMap<Direction, EnumProperty<WireConnection>>(
                ImmutableMap.of<Direction, EnumProperty<WireConnection>>(
                    Direction.NORTH, WIRE_CONNECTION_NORTH,
                    Direction.EAST, WIRE_CONNECTION_EAST,
                    Direction.SOUTH, WIRE_CONNECTION_SOUTH,
                    Direction.WEST, WIRE_CONNECTION_WEST
                )
            )
        val DOT_SHAPE = createCuboidShape(3.0, 0.0, 3.0, 13.0, 1.0, 13.0)
        val SHAPES_FLOOR = newEnumMap<Direction, VoxelShape>(
            ImmutableMap.of(
                Direction.NORTH, createCuboidShape(3.0, 0.0, 0.0, 13.0, 1.0, 13.0),
                Direction.SOUTH, createCuboidShape(3.0, 0.0, 3.0, 13.0, 1.0, 16.0),
                Direction.EAST, createCuboidShape(3.0, 0.0, 3.0, 16.0, 1.0, 13.0),
                Direction.WEST, createCuboidShape(0.0, 0.0, 3.0, 13.0, 1.0, 13.0)
            )
        )
        val SHAPES_UP = newEnumMap<Direction, VoxelShape>(
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