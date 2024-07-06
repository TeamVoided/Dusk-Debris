//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//
package net.minecraft.block

import com.mojang.serialization.Codec.stringResolver
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.entity.BlockEntityTicker
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.block.entity.SkullBlockEntity
import net.minecraft.entity.EntityType
import net.minecraft.entity.EquipmentSlot
import net.minecraft.entity.ai.pathing.NavigationType
import net.minecraft.item.Equippable
import net.minecraft.item.ItemPlacementContext
import net.minecraft.state.StateManager
import net.minecraft.state.properties.RotationSegment
import net.minecraft.state.property.BooleanProperty
import net.minecraft.state.property.IntProperty
import net.minecraft.state.property.Properties
import net.minecraft.util.BlockMirror
import net.minecraft.util.BlockRotation
import net.minecraft.util.math.BlockPos
import net.minecraft.util.shape.VoxelShape
import net.minecraft.util.shape.VoxelShapes
import net.minecraft.world.BlockView
import net.minecraft.world.World

class SkeletonSkullBlock(skullEntity: EntityType<*>, settings: Settings) :
    BlockWithEntity(settings),
    Equippable {
    init {
        this.defaultState = (stateManager.defaultState)
            .with(POWERED, false)
            .with(ROTATION, 0)
    }

    override fun getCodec(): MapCodec<out BlockWithEntity>? {
        return null
    }

    override fun createBlockEntity(pos: BlockPos, state: BlockState): BlockEntity {
        return SkullBlockEntity(pos, state)
    }

    override fun <T : BlockEntity> getTicker(
        worldAccess: World,
        blockState: BlockState,
        type: BlockEntityType<T>
    ): BlockEntityTicker<T>? {
        if (worldAccess.isClient) {
            val ticksInWorld = false
            if (ticksInWorld) {
                return checkType(
                    type, BlockEntityType.SKULL
                ) { world: World, pos: BlockPos, state: BlockState, skull: SkullBlockEntity ->
                    SkullBlockEntity.clientTick(
                        world,
                        pos,
                        state,
                        skull
                    )
                }
            }
        }

        return null
    }

    override fun canPathfindThrough(state: BlockState, navigationType: NavigationType): Boolean {
        return false
    }

    override fun getPreferredSlot(): EquipmentSlot {
        return EquipmentSlot.HEAD
    }

    override fun getOutlineShape(
        state: BlockState,
        world: BlockView,
        pos: BlockPos,
        context: ShapeContext
    ): VoxelShape {
        return SHAPE
    }

    override fun getCullingShape(state: BlockState, world: BlockView, pos: BlockPos): VoxelShape {
        return VoxelShapes.empty()
    }

    override fun rotate(state: BlockState, rotation: BlockRotation): BlockState {
        return state.with(
            ROTATION, rotation.rotate(
                state.get(ROTATION),
                ROTATIONS
            )
        )
    }

    override fun mirror(state: BlockState, mirror: BlockMirror): BlockState {
        return state.with(
            ROTATION, mirror.mirror(
                state.get(ROTATION),
                ROTATIONS
            )
        )
    }

    override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) {
        builder.add(POWERED)
        builder.add(ROTATION)
    }

    override fun getPlacementState(ctx: ItemPlacementContext): BlockState {
        return super.getPlacementState(ctx)!!
            .with(ROTATION, RotationSegment.convertToSegment(ctx.playerYaw))
            .with(POWERED, ctx.world.isReceivingRedstonePower(ctx.blockPos))
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
            val bl = world.isReceivingRedstonePower(pos)
            if (bl != state.get(POWERED)) {
                world.setBlockState(pos, state.with(POWERED, bl), 2)
            }
        }
    }

    companion object {
        val POWERED: BooleanProperty = Properties.POWERED
        val ROTATION_BITMASK: Int = RotationSegment.getMaxSegmentIndex()
        private val ROTATIONS = ROTATION_BITMASK + 1
        val ROTATION: IntProperty = Properties.ROTATION
        protected val SHAPE: VoxelShape = createCuboidShape(4.0, 0.0, 4.0, 12.0, 8.0, 12.0)
        protected val PIGLIN_SKULL_SHAPE: VoxelShape =
            createCuboidShape(3.0, 0.0, 3.0, 13.0, 8.0, 13.0)
    }
}