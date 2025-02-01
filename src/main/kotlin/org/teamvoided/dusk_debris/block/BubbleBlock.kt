package org.teamvoided.dusk_debris.block


import com.mojang.serialization.MapCodec
import net.minecraft.block.*
import net.minecraft.entity.Entity
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.ai.pathing.NavigationType
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.particle.DefaultParticleType
import net.minecraft.particle.ParticleTypes
import net.minecraft.server.world.ServerWorld
import net.minecraft.sound.SoundEvent
import net.minecraft.sound.SoundEvents
import net.minecraft.state.StateManager
import net.minecraft.state.property.BooleanProperty
import net.minecraft.state.property.DirectionProperty
import net.minecraft.state.property.Properties
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.random.RandomGenerator
import net.minecraft.util.shape.VoxelShape
import net.minecraft.util.shape.VoxelShapes
import net.minecraft.world.BlockView
import net.minecraft.world.GameRules
import net.minecraft.world.World
import net.minecraft.world.WorldAccess
import org.teamvoided.dusk_debris.block.not_blocks.DuskProperties
import org.teamvoided.dusk_debris.data.tags.DuskEntityTypeTags
import org.teamvoided.dusk_debris.util.rotate
import java.util.*

class BubbleBlock(settings: Settings) : Block(settings), Waterloggable {
    public override fun getCodec(): MapCodec<BubbleBlock> = CODEC

    init {
        this.defaultState = stateManager.defaultState.with(SQUISHED, false)
    }

    override fun onEntityCollision(state: BlockState, world: World, pos: BlockPos, entity: Entity) {
        if (!getPopCheck(state) && !world.isClient && !entity.type.isIn(DuskEntityTypeTags.DONT_POP_FOG_BUBBLES)) {
            if ((world.gameRules.getBooleanValue(GameRules.DO_MOB_GRIEFING) || entity is PlayerEntity) &&
                entity.canModifyAt(world, pos)
            ) {
                setPopped(state, world, pos)
            }
        }
    }

    override fun randomTick(state: BlockState, world: ServerWorld, pos: BlockPos, random: RandomGenerator) {
        val randInt = random.nextInt(127)
        if (randInt == 0 || (state.get(SQUISHED) && randInt <= 8)) {
            world.setBlockState(pos, state.with(SQUISHED, !state.get(SQUISHED)))
            val ofCenter = pos.ofCenter()
            repeat(7) {
                world.addParticle(
                    getBubbleParticle(),
                    true,
                    ofCenter.x,
                    ofCenter.y,
                    ofCenter.z,
                    (random.nextDouble() - 0.5) * 0.2,
                    (random.nextDouble() - 0.5) * 0.2,
                    (random.nextDouble() - 0.5) * 0.2,
                )
            }
        }
        super.randomTick(state, world, pos, random)
    }

    override fun getOutlineShape(
        state: BlockState,
        world: BlockView,
        pos: BlockPos,
        context: ShapeContext
    ): VoxelShape {
        if (!getPopCheck(state))
            return super.getOutlineShape(state, world, pos, context)
        else {
            val facing = state.get(FACING)
            if (facing.axis != Direction.Axis.Y) {
                val rotations = when (facing) {
                    Direction.NORTH -> 0
                    Direction.SOUTH -> 2
                    Direction.WEST -> 3
                    Direction.EAST -> 1
                    else -> 0
                }
                return POPPED_SHAPE.rotate(rotations)
            } else {
                return if (facing == Direction.UP) POPPED_UP_SHAPE
                else POPPED_DOWN_SHAPE
            }
        }
    }

    override fun getCullingShape(state: BlockState, world: BlockView, pos: BlockPos): VoxelShape = VoxelShapes.empty()

    override fun getCollisionShape(
        state: BlockState,
        world: BlockView,
        pos: BlockPos,
        context: ShapeContext
    ): VoxelShape = VoxelShapes.empty()

    override fun isSideInvisible(state: BlockState, stateFrom: BlockState, direction: Direction): Boolean {
        return super.isSideInvisible(state, stateFrom, direction)
    }

    override fun canPathfindThrough(state: BlockState, navigationType: NavigationType): Boolean {
        return true
    }

    fun getBubbleParticle(): DefaultParticleType = ParticleTypes.BUBBLE
    fun getPopCheck(state: BlockState): Boolean = state.get(SQUISHED)
    fun setPopped(state: BlockState, world: World, pos: BlockPos) =
        world.setBlockState(pos, state.with(SQUISHED, true))

    fun poppedProperty() = SQUISHED

    override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) {
        builder.add(poppedProperty(), FACING)
    }

    companion object {
        val CODEC: MapCodec<BubbleBlock> = createCodec(::BubbleBlock)
        val SQUISHED: BooleanProperty = DuskProperties.SQUISHED
        val FACING: DirectionProperty = Properties.FACING
        val POPPED_SHAPE: VoxelShape = createCuboidShape(0.0, 5.0, 5.0, 1.0, 11.0, 11.0)
        val POPPED_UP_SHAPE: VoxelShape = createCuboidShape(5.0, 15.0, 5.0, 11.0, 16.0, 11.0)
        val POPPED_DOWN_SHAPE: VoxelShape = createCuboidShape(5.0, 0.0, 5.0, 11.0, 1.0, 11.0)
    }
}