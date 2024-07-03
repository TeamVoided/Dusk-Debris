////
//// Source code recreated from a .class file by IntelliJ IDEA
//// (powered by FernFlower decompiler)
////
//package net.minecraft.block
//
//import com.google.common.collect.ImmutableMap
//import com.google.common.collect.Maps
//import com.google.common.collect.Sets
//import com.google.common.collect.UnmodifiableIterator
//import com.mojang.serialization.MapCodec
//import net.minecraft.block.enums.WireConnection
//import net.minecraft.entity.player.PlayerEntity
//import net.minecraft.item.ItemPlacementContext
//import net.minecraft.particle.DustParticleEffect
//import net.minecraft.state.StateManager
//import net.minecraft.state.property.EnumProperty
//import net.minecraft.state.property.IntProperty
//import net.minecraft.state.property.Properties
//import net.minecraft.state.property.Property
//import net.minecraft.util.ActionResult
//import net.minecraft.util.BlockMirror
//import net.minecraft.util.BlockRotation
//import net.minecraft.util.Util
//import net.minecraft.util.hit.BlockHitResult
//import net.minecraft.util.math.BlockPos
//import net.minecraft.util.math.Direction
//import net.minecraft.util.math.MathHelper
//import net.minecraft.util.math.Vec3d
//import net.minecraft.util.random.RandomGenerator
//import net.minecraft.util.shape.VoxelShape
//import net.minecraft.util.shape.VoxelShapes
//import net.minecraft.world.BlockView
//import net.minecraft.world.World
//import net.minecraft.world.WorldAccess
//import net.minecraft.world.WorldView
//import kotlin.math.max
//
//class RedstoneWireBlock(settings: Settings?) : Block(settings) {
//    private val dotState: BlockState
//    private var wiresGivePower = true
//
//    public override fun getCodec(): MapCodec<RedstoneWireBlock> {
//        return CODEC
//    }
//
//    private fun getShapeForState(state: BlockState): VoxelShape {
//        var voxelShape = DOT_SHAPE
//        val var3: Iterator<*> = Direction.Type.HORIZONTAL.iterator()
//
//        while (var3.hasNext()) {
//            val direction = var3.next() as Direction
//            val wireConnection =
//                state.get(DIRECTION_TO_WIRE_CONNECTION_PROPERTY[direction] as Property<*>?) as WireConnection
//            if (wireConnection == WireConnection.SIDE) {
//                voxelShape = VoxelShapes.union(
//                    voxelShape,
//                    SHAPES_FLOOR[direction]
//                )
//            } else if (wireConnection == WireConnection.UP) {
//                voxelShape = VoxelShapes.union(
//                    voxelShape,
//                    SHAPES_UP[direction]
//                )
//            }
//        }
//
//        return voxelShape
//    }
//
//    override fun getOutlineShape(
//        state: BlockState,
//        world: BlockView,
//        pos: BlockPos,
//        context: ShapeContext
//    ): VoxelShape {
//        return SHAPES[state.with(POWER, 0)] as VoxelShape
//    }
//
//    override fun getPlacementState(ctx: ItemPlacementContext): BlockState? {
//        return this.getPlacementState(ctx.world, this.dotState, ctx.blockPos)
//    }
//
//    private fun getPlacementState(world: BlockView, state: BlockState, pos: BlockPos): BlockState {
//        var state = state
//        val bl = isNotConnected(state)
//        state = this.getMissingConnections(
//            world,
//            defaultState.with(POWER, state.get(POWER) as Int) as BlockState, pos
//        )
//        if (bl && isNotConnected(state)) {
//            return state
//        } else {
//            val bl2 = (state.get(WIRE_CONNECTION_NORTH) as WireConnection).isConnected
//            val bl3 = (state.get(WIRE_CONNECTION_SOUTH) as WireConnection).isConnected
//            val bl4 = (state.get(WIRE_CONNECTION_EAST) as WireConnection).isConnected
//            val bl5 = (state.get(WIRE_CONNECTION_WEST) as WireConnection).isConnected
//            val bl6 = !bl2 && !bl3
//            val bl7 = !bl4 && !bl5
//            if (!bl5 && bl6) {
//                state = state.with(WIRE_CONNECTION_WEST, WireConnection.SIDE) as BlockState
//            }
//
//            if (!bl4 && bl6) {
//                state = state.with(WIRE_CONNECTION_EAST, WireConnection.SIDE) as BlockState
//            }
//
//            if (!bl2 && bl7) {
//                state = state.with(WIRE_CONNECTION_NORTH, WireConnection.SIDE) as BlockState
//            }
//
//            if (!bl3 && bl7) {
//                state = state.with(WIRE_CONNECTION_SOUTH, WireConnection.SIDE) as BlockState
//            }
//
//            return state
//        }
//    }
//
//    private fun getMissingConnections(world: BlockView, state: BlockState, pos: BlockPos): BlockState {
//        var state = state
//        val bl = !world.getBlockState(pos.up()).isSolidBlock(world, pos)
//        val var5: Iterator<*> = Direction.Type.HORIZONTAL.iterator()
//
//        while (var5.hasNext()) {
//            val direction = var5.next() as Direction
//            if (!(state.get(DIRECTION_TO_WIRE_CONNECTION_PROPERTY[direction] as Property<*>?) as WireConnection).isConnected) {
//                val wireConnection = this.getRenderConnectionType(world, pos, direction, bl)
//                state = state.with<WireConnection, WireConnection>(
//                    DIRECTION_TO_WIRE_CONNECTION_PROPERTY[direction] as Property<*>,
//                    wireConnection
//                ) as BlockState
//            }
//        }
//
//        return state
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
//        if (direction == Direction.DOWN) {
//            return if (!this.canRunOnTop(world, neighborPos, neighborState)) Blocks.AIR.defaultState else state
//        } else if (direction == Direction.UP) {
//            return this.getPlacementState(world, state, pos)
//        } else {
//            val wireConnection = this.getRenderConnectionType(world, pos, direction)
//            return if (wireConnection.isConnected == (state.get(DIRECTION_TO_WIRE_CONNECTION_PROPERTY[direction] as Property<*>?) as WireConnection).isConnected && !isFullyConnected(
//                    state
//                )
//            ) state.with<WireConnection, WireConnection>(
//                DIRECTION_TO_WIRE_CONNECTION_PROPERTY[direction] as Property<*>, wireConnection
//            ) as BlockState else this.getPlacementState(
//                world,
//                (dotState.with(POWER, state.get(POWER) as Int) as BlockState).with<WireConnection, WireConnection>(
//                    DIRECTION_TO_WIRE_CONNECTION_PROPERTY[direction] as Property<*>, wireConnection
//                ) as BlockState, pos
//            )
//        }
//    }
//
//    override fun prepare(state: BlockState, world: WorldAccess, pos: BlockPos, flags: Int, maxUpdateDepth: Int) {
//        val mutable = BlockPos.Mutable()
//        val var7: Iterator<*> = Direction.Type.HORIZONTAL.iterator()
//
//        while (var7.hasNext()) {
//            val direction = var7.next() as Direction
//            val wireConnection =
//                state.get(DIRECTION_TO_WIRE_CONNECTION_PROPERTY[direction] as Property<*>?) as WireConnection
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
//
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
//
//        return if (!connectsTo(blockState, direction) && (blockState.isSolidBlock(
//                world,
//                blockPos
//            ) || !connectsTo(world.getBlockState(blockPos.down())))
//        ) WireConnection.NONE else WireConnection.SIDE
//    }
//
//    override fun canPlaceAt(state: BlockState, world: WorldView, pos: BlockPos): Boolean {
//        val blockPos = pos.down()
//        val blockState = world.getBlockState(blockPos)
//        return this.canRunOnTop(world, blockPos, blockState)
//    }
//
//    private fun canRunOnTop(world: BlockView, pos: BlockPos, floor: BlockState): Boolean {
//        return floor.isSideSolidFullSquare(world, pos, Direction.UP) || floor.isOf(Blocks.HOPPER)
//    }
//
//    private fun update(world: World, pos: BlockPos, state: BlockState) {
//        val i = this.getReceivedRedstonePower(world, pos)
//        if (state.get(POWER) as Int != i) {
//            if (world.getBlockState(pos) === state) {
//                world.setBlockState(pos, state.with(POWER, i) as BlockState, 2)
//            }
//
//            val set: MutableSet<BlockPos> = Sets.newHashSet()
//            set.add(pos)
//            val var6 = Direction.entries.toTypedArray()
//            val var7 = var6.size
//
//            for (var8 in 0 until var7) {
//                val direction = var6[var8]
//                set.add(pos.offset(direction))
//            }
//
//            val var10: Iterator<*> = set.iterator()
//
//            while (var10.hasNext()) {
//                val blockPos = var10.next() as BlockPos
//                world.updateNeighborsAlways(blockPos, this)
//            }
//        }
//    }
//
//    private fun getReceivedRedstonePower(world: World, pos: BlockPos): Int {
//        this.wiresGivePower = false
//        val i = world.getReceivedRedstonePower(pos)
//        this.wiresGivePower = true
//        var j = 0
//        if (i < 15) {
//            val var5: Iterator<*> = Direction.Type.HORIZONTAL.iterator()
//
//            while (true) {
//                while (var5.hasNext()) {
//                    val direction = var5.next() as Direction
//                    val blockPos = pos.offset(direction)
//                    val blockState = world.getBlockState(blockPos)
//                    j = max(j.toDouble(), increasePower(blockState).toDouble()).toInt()
//                    val blockPos2 = pos.up()
//                    if (blockState.isSolidBlock(world, blockPos) && !world.getBlockState(blockPos2)
//                            .isSolidBlock(world, blockPos2)
//                    ) {
//                        j = max(
//                            j.toDouble(),
//                            increasePower(world.getBlockState(blockPos.up())).toDouble()
//                        )
//                            .toInt()
//                    } else if (!blockState.isSolidBlock(world, blockPos)) {
//                        j = max(
//                            j.toDouble(),
//                            increasePower(world.getBlockState(blockPos.down())).toDouble()
//                        )
//                            .toInt()
//                    }
//                }
//
//                return max(i.toDouble(), (j - 1).toDouble()).toInt()
//            }
//        } else {
//            return max(i.toDouble(), (j - 1).toDouble()).toInt()
//        }
//    }
//
//    private fun increasePower(state: BlockState): Int {
//        return if (state.isOf(this)) state.get(POWER) as Int else 0
//    }
//
//    private fun updateNeighbors(world: World, pos: BlockPos) {
//        if (world.getBlockState(pos).isOf(this)) {
//            world.updateNeighborsAlways(pos, this)
//            val var3 = Direction.entries.toTypedArray()
//            val var4 = var3.size
//
//            for (var5 in 0 until var4) {
//                val direction = var3[var5]
//                world.updateNeighborsAlways(pos.offset(direction), this)
//            }
//        }
//    }
//
//    override fun onBlockAdded(state: BlockState, world: World, pos: BlockPos, oldState: BlockState, notify: Boolean) {
//        if (!oldState.isOf(state.block) && !world.isClient) {
//            this.update(world, pos, state)
//            val var6: Iterator<*> = Direction.Type.VERTICAL.iterator()
//
//            while (var6.hasNext()) {
//                val direction = var6.next() as Direction
//                world.updateNeighborsAlways(pos.offset(direction), this)
//            }
//
//            this.updateOffsetNeighbors(world, pos)
//        }
//    }
//
//    override fun onStateReplaced(state: BlockState, world: World, pos: BlockPos, newState: BlockState, moved: Boolean) {
//        if (!moved && !state.isOf(newState.block)) {
//            super.onStateReplaced(state, world, pos, newState, moved)
//            if (!world.isClient) {
//                val var6 = Direction.entries.toTypedArray()
//                val var7 = var6.size
//
//                for (var8 in 0 until var7) {
//                    val direction = var6[var8]
//                    world.updateNeighborsAlways(pos.offset(direction), this)
//                }
//
//                this.update(world, pos, state)
//                this.updateOffsetNeighbors(world, pos)
//            }
//        }
//    }
//
//    private fun updateOffsetNeighbors(world: World, pos: BlockPos) {
//        var var3: Iterator<*> = Direction.Type.HORIZONTAL.iterator()
//
//        var direction: Direction?
//        while (var3.hasNext()) {
//            direction = var3.next() as Direction?
//            this.updateNeighbors(world, pos.offset(direction))
//        }
//
//        var3 = Direction.Type.HORIZONTAL.iterator()
//
//        while (var3.hasNext()) {
//            direction = var3.next() as Direction
//            val blockPos = pos.offset(direction)
//            if (world.getBlockState(blockPos).isSolidBlock(world, blockPos)) {
//                this.updateNeighbors(world, blockPos.up())
//            } else {
//                this.updateNeighbors(world, blockPos.down())
//            }
//        }
//    }
//
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
//
//    override fun getStrongRedstonePower(state: BlockState, world: BlockView, pos: BlockPos, direction: Direction): Int {
//        return if (!this.wiresGivePower) 0 else state.getWeakRedstonePower(world, pos, direction)
//    }
//
//    override fun getWeakRedstonePower(state: BlockState, world: BlockView, pos: BlockPos, direction: Direction): Int {
//        if (this.wiresGivePower && direction != Direction.DOWN) {
//            val i = state.get(POWER) as Int
//            return if (i == 0) {
//                0
//            } else {
//                if (direction != Direction.UP && !(this.getPlacementState(world, state, pos).get(
//                        DIRECTION_TO_WIRE_CONNECTION_PROPERTY[direction.opposite] as Property<*>?
//                    ) as WireConnection).isConnected
//                ) 0 else i
//            }
//        } else {
//            return 0
//        }
//    }
//
//    override fun isRedstonePowerSource(state: BlockState): Boolean {
//        return this.wiresGivePower
//    }
//
//    private fun addPoweredParticles(
//        world: World,
//        random: RandomGenerator,
//        pos: BlockPos,
//        color: Vec3d?,
//        direction: Direction,
//        direction2: Direction,
//        f: Float,
//        g: Float
//    ) {
//        val h = g - f
//        if (!(random.nextFloat() >= 0.2f * h)) {
//            val i = 0.4375f
//            val j = f + h * random.nextFloat()
//            val d =
//                0.5 + (0.4375f * direction.offsetX.toFloat()).toDouble() + (j * direction2.offsetX.toFloat()).toDouble()
//            val e =
//                0.5 + (0.4375f * direction.offsetY.toFloat()).toDouble() + (j * direction2.offsetY.toFloat()).toDouble()
//            val k =
//                0.5 + (0.4375f * direction.offsetZ.toFloat()).toDouble() + (j * direction2.offsetZ.toFloat()).toDouble()
//            world.addParticle(
//                DustParticleEffect(color!!.toVector3f(), 1.0f),
//                pos.x.toDouble() + d,
//                pos.y.toDouble() + e,
//                pos.z.toDouble() + k,
//                0.0,
//                0.0,
//                0.0
//            )
//        }
//    }
//
//    override fun randomDisplayTick(state: BlockState, world: World, pos: BlockPos, random: RandomGenerator) {
//        val i = state.get(POWER) as Int
//        if (i != 0) {
//            val var6: Iterator<*> = Direction.Type.HORIZONTAL.iterator()
//
//            while (var6.hasNext()) {
//                val direction = var6.next() as Direction
//                val wireConnection =
//                    state.get(DIRECTION_TO_WIRE_CONNECTION_PROPERTY[direction] as Property<*>?) as WireConnection
//                when (wireConnection) {
//                    WireConnection.UP -> {
//                        this.addPoweredParticles(
//                            world, random, pos,
//                            COLORS[i], direction, Direction.UP, -0.5f, 0.5f
//                        )
//                        this.addPoweredParticles(
//                            world, random, pos,
//                            COLORS[i], Direction.DOWN, direction, 0.0f, 0.5f
//                        )
//                    }
//
//                    WireConnection.SIDE -> this.addPoweredParticles(
//                        world, random, pos,
//                        COLORS[i], Direction.DOWN, direction, 0.0f, 0.5f
//                    )
//
//                    WireConnection.NONE -> this.addPoweredParticles(
//                        world, random, pos,
//                        COLORS[i], Direction.DOWN, direction, 0.0f, 0.3f
//                    )
//
//                    else -> this.addPoweredParticles(
//                        world, random, pos,
//                        COLORS[i], Direction.DOWN, direction, 0.0f, 0.3f
//                    )
//                }
//            }
//        }
//    }
//
//    override fun rotate(state: BlockState, rotation: BlockRotation): BlockState {
//        return when (rotation) {
//            BlockRotation.CLOCKWISE_180 -> (((state.with(
//                WIRE_CONNECTION_NORTH, state.get(
//                    WIRE_CONNECTION_SOUTH
//                ) as WireConnection
//            ) as BlockState).with(
//                WIRE_CONNECTION_EAST,
//                state.get(WIRE_CONNECTION_WEST) as WireConnection
//            ) as BlockState).with(
//                WIRE_CONNECTION_SOUTH, state.get(WIRE_CONNECTION_NORTH) as WireConnection
//            ) as BlockState).with(
//                WIRE_CONNECTION_WEST, state.get(WIRE_CONNECTION_EAST) as WireConnection
//            ) as BlockState
//
//            BlockRotation.COUNTERCLOCKWISE_90 -> (((state.with(
//                WIRE_CONNECTION_NORTH,
//                state.get(WIRE_CONNECTION_EAST) as WireConnection
//            ) as BlockState).with(
//                WIRE_CONNECTION_EAST, state.get(WIRE_CONNECTION_SOUTH) as WireConnection
//            ) as BlockState).with(
//                WIRE_CONNECTION_SOUTH, state.get(WIRE_CONNECTION_WEST) as WireConnection
//            ) as BlockState).with(
//                WIRE_CONNECTION_WEST, state.get(WIRE_CONNECTION_NORTH) as WireConnection
//            ) as BlockState
//
//            BlockRotation.CLOCKWISE_90 -> (((state.with(
//                WIRE_CONNECTION_NORTH,
//                state.get(WIRE_CONNECTION_WEST) as WireConnection
//            ) as BlockState).with(
//                WIRE_CONNECTION_EAST, state.get(WIRE_CONNECTION_NORTH) as WireConnection
//            ) as BlockState).with(
//                WIRE_CONNECTION_SOUTH, state.get(WIRE_CONNECTION_EAST) as WireConnection
//            ) as BlockState).with(
//                WIRE_CONNECTION_WEST, state.get(WIRE_CONNECTION_SOUTH) as WireConnection
//            ) as BlockState
//
//            else -> state
//        }
//    }
//
//    override fun mirror(state: BlockState, mirror: BlockMirror): BlockState {
//        return when (mirror) {
//            BlockMirror.LEFT_RIGHT -> (state.with(
//                WIRE_CONNECTION_NORTH,
//                state.get(WIRE_CONNECTION_SOUTH) as WireConnection
//            ) as BlockState).with(
//                WIRE_CONNECTION_SOUTH, state.get(WIRE_CONNECTION_NORTH) as WireConnection
//            ) as BlockState
//
//            BlockMirror.FRONT_BACK -> (state.with(
//                WIRE_CONNECTION_EAST,
//                state.get(WIRE_CONNECTION_WEST) as WireConnection
//            ) as BlockState).with(
//                WIRE_CONNECTION_WEST, state.get(WIRE_CONNECTION_EAST) as WireConnection
//            ) as BlockState
//
//            else -> super.mirror(state, mirror)
//        }
//    }
//
//    override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) {
//        builder.add(
//            *arrayOf<Property<*>>(
//                WIRE_CONNECTION_NORTH,
//                WIRE_CONNECTION_EAST,
//                WIRE_CONNECTION_SOUTH,
//                WIRE_CONNECTION_WEST,
//                POWER
//            )
//        )
//    }
//
//    override fun onUse(
//        state: BlockState,
//        world: World,
//        pos: BlockPos,
//        entity: PlayerEntity,
//        hitResult: BlockHitResult
//    ): ActionResult {
//        if (!entity.abilities.allowModifyWorld) {
//            return ActionResult.PASS
//        } else {
//            if (isFullyConnected(state) || isNotConnected(state)) {
//                var blockState = if (isFullyConnected(state)) this.defaultState else this.dotState
//                blockState = blockState.with(POWER, state.get(POWER) as Int) as BlockState
//                blockState = this.getPlacementState(world, blockState, pos)
//                if (blockState !== state) {
//                    world.setBlockState(pos, blockState, 3)
//                    this.updateForNewState(world, pos, state, blockState)
//                    return ActionResult.SUCCESS
//                }
//            }
//
//            return ActionResult.PASS
//        }
//    }
//
//    private fun updateForNewState(world: World, pos: BlockPos, oldState: BlockState, newState: BlockState) {
//        val var5: Iterator<*> = Direction.Type.HORIZONTAL.iterator()
//
//        while (var5.hasNext()) {
//            val direction = var5.next() as Direction
//            val blockPos = pos.offset(direction)
//            if ((oldState.get(DIRECTION_TO_WIRE_CONNECTION_PROPERTY[direction] as Property<*>?) as WireConnection).isConnected != (newState.get(
//                    DIRECTION_TO_WIRE_CONNECTION_PROPERTY[direction] as Property<*>?
//                ) as WireConnection).isConnected && world.getBlockState(blockPos).isSolidBlock(world, blockPos)
//            ) {
//                world.updateNeighborsExcept(blockPos, newState.block, direction.opposite)
//            }
//        }
//    }
//
//    init {
//        this.defaultState = (((((stateManager.defaultState as BlockState).with(
//            WIRE_CONNECTION_NORTH,
//            WireConnection.NONE
//        ) as BlockState).with(
//            WIRE_CONNECTION_EAST, WireConnection.NONE
//        ) as BlockState).with(WIRE_CONNECTION_SOUTH, WireConnection.NONE) as BlockState).with(
//            WIRE_CONNECTION_WEST, WireConnection.NONE
//        ) as BlockState).with(POWER, 0) as BlockState
//        this.dotState =
//            (((defaultState.with(WIRE_CONNECTION_NORTH, WireConnection.SIDE) as BlockState).with(
//                WIRE_CONNECTION_EAST,
//                WireConnection.SIDE
//            ) as BlockState).with(
//                WIRE_CONNECTION_SOUTH, WireConnection.SIDE
//            ) as BlockState).with(WIRE_CONNECTION_WEST, WireConnection.SIDE) as BlockState
//        val var2: UnmodifiableIterator<*> = getStateManager().states.iterator()
//
//        while (var2.hasNext()) {
//            val blockState = var2.next() as BlockState
//            if (blockState.get(POWER) == 0) {
//                SHAPES[blockState] = getShapeForState(blockState)
//            }
//        }
//    }
//
//    companion object {
//        val CODEC: MapCodec<RedstoneWireBlock> = createCodec { settings: Settings? ->
//            RedstoneWireBlock(
//                settings
//            )
//        }
//        val WIRE_CONNECTION_NORTH: EnumProperty<WireConnection> = Properties.NORTH_WIRE_CONNECTION
//        val WIRE_CONNECTION_EAST: EnumProperty<WireConnection> = Properties.EAST_WIRE_CONNECTION
//        val WIRE_CONNECTION_SOUTH: EnumProperty<WireConnection> = Properties.SOUTH_WIRE_CONNECTION
//        val WIRE_CONNECTION_WEST: EnumProperty<WireConnection> = Properties.WEST_WIRE_CONNECTION
//        val POWER: IntProperty = Properties.POWER
//        val DIRECTION_TO_WIRE_CONNECTION_PROPERTY: Map<Direction, EnumProperty<WireConnection>> =
//            Maps.newEnumMap(
//                ImmutableMap.of(
//                    Direction.NORTH,
//                    WIRE_CONNECTION_NORTH,
//                    Direction.EAST,
//                    WIRE_CONNECTION_EAST,
//                    Direction.SOUTH,
//                    WIRE_CONNECTION_SOUTH,
//                    Direction.WEST,
//                    WIRE_CONNECTION_WEST
//                )
//            )
//        protected const val HEIGHT: Int = 1
//        protected const val WEST: Int = 3
//        protected const val EAST: Int = 13
//        protected const val NORTH: Int = 3
//        protected const val SOUTH: Int = 13
//        private val DOT_SHAPE: VoxelShape = createCuboidShape(3.0, 0.0, 3.0, 13.0, 1.0, 13.0)
//        private val SHAPES_FLOOR: Map<Direction, VoxelShape> = Maps.newEnumMap(
//            ImmutableMap.of(
//                Direction.NORTH,
//                createCuboidShape(3.0, 0.0, 0.0, 13.0, 1.0, 13.0),
//                Direction.SOUTH,
//                createCuboidShape(3.0, 0.0, 3.0, 13.0, 1.0, 16.0),
//                Direction.EAST,
//                createCuboidShape(3.0, 0.0, 3.0, 16.0, 1.0, 13.0),
//                Direction.WEST,
//                createCuboidShape(0.0, 0.0, 3.0, 13.0, 1.0, 13.0)
//            )
//        )
//        private val SHAPES_UP: Map<Direction, VoxelShape> = Maps.newEnumMap(
//            ImmutableMap.of(
//                Direction.NORTH, VoxelShapes.union(
//                    SHAPES_FLOOR[Direction.NORTH], createCuboidShape(3.0, 0.0, 0.0, 13.0, 16.0, 1.0)
//                ), Direction.SOUTH, VoxelShapes.union(
//                    SHAPES_FLOOR[Direction.SOUTH], createCuboidShape(3.0, 0.0, 15.0, 13.0, 16.0, 16.0)
//                ), Direction.EAST, VoxelShapes.union(
//                    SHAPES_FLOOR[Direction.EAST], createCuboidShape(15.0, 0.0, 3.0, 16.0, 16.0, 13.0)
//                ), Direction.WEST, VoxelShapes.union(
//                    SHAPES_FLOOR[Direction.WEST], createCuboidShape(0.0, 0.0, 3.0, 1.0, 16.0, 13.0)
//                )
//            )
//        )
//        private val SHAPES: MutableMap<BlockState, VoxelShape> = Maps.newHashMap()
//        private val COLORS = Util.make(
//            arrayOfNulls(16)
//        ) { positions: Array<Vec3d?> ->
//            for (i in 0..15) {
//                val f = i.toFloat() / 15.0f
//                val g = f * 0.6f + (if (f > 0.0f) 0.4f else 0.3f)
//                val h = MathHelper.clamp(f * f * 0.7f - 0.5f, 0.0f, 1.0f)
//                val j = MathHelper.clamp(f * f * 0.6f - 0.7f, 0.0f, 1.0f)
//                positions[i] = Vec3d(g.toDouble(), h.toDouble(), j.toDouble())
//            }
//        } as Array<Vec3d?>
//        private const val PARTICLE_DENSITY = 0.2f
//        private fun isFullyConnected(state: BlockState): Boolean {
//            return (state.get(WIRE_CONNECTION_NORTH) as WireConnection).isConnected && (state.get(WIRE_CONNECTION_SOUTH) as WireConnection).isConnected && (state.get(
//                WIRE_CONNECTION_EAST
//            ) as WireConnection).isConnected && (state.get(WIRE_CONNECTION_WEST) as WireConnection).isConnected
//        }
//
//        private fun isNotConnected(state: BlockState): Boolean {
//            return !(state.get(WIRE_CONNECTION_NORTH) as WireConnection).isConnected && !(state.get(
//                WIRE_CONNECTION_SOUTH
//            ) as WireConnection).isConnected && !(state.get(WIRE_CONNECTION_EAST) as WireConnection).isConnected && !(state.get(
//                WIRE_CONNECTION_WEST
//            ) as WireConnection).isConnected
//        }
//
//        fun connectsTo(state: BlockState, dir: Direction? = null as Direction?): Boolean {
//            if (state.isOf(Blocks.REDSTONE_WIRE)) {
//                return true
//            } else if (state.isOf(Blocks.REPEATER)) {
//                val direction = state.get(RepeaterBlock.FACING) as Direction
//                return direction == dir || direction.opposite == dir
//            } else if (state.isOf(Blocks.OBSERVER)) {
//                return dir == state.get(ObserverBlock.FACING)
//            } else {
//                return state.isRedstonePowerSource && dir != null
//            }
//        }
//
//        fun getWireColor(powerLevel: Int): Int {
//            val vec3d = COLORS[powerLevel]
//            return MathHelper.color(vec3d!!.getX().toFloat(), vec3d.getY().toFloat(), vec3d.getZ().toFloat())
//        }
//    }
//}