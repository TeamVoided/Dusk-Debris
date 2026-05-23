package org.teamvoided.dusk_debris.block


import com.mojang.serialization.MapCodec
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.particles.ParticleTypes
import net.minecraft.core.particles.SimpleParticleType
import net.minecraft.server.level.ServerLevel
import net.minecraft.util.RandomSource
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.GameRules
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.SimpleWaterloggedBlock
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.world.level.block.state.properties.BooleanProperty
import net.minecraft.world.level.block.state.properties.DirectionProperty
import net.minecraft.world.level.pathfinder.PathComputationType
import net.minecraft.world.phys.shapes.CollisionContext
import net.minecraft.world.phys.shapes.Shapes
import net.minecraft.world.phys.shapes.VoxelShape
import org.teamvoided.dusk_debris.block.not_blocks.DuskProperties
import org.teamvoided.dusk_debris.data.tags.DuskEntityTypeTags
import org.teamvoided.dusk_debris.util.rotate

class BubbleBlock(settings: Properties) : Block(settings), SimpleWaterloggedBlock {
    public override fun codec(): MapCodec<BubbleBlock> = CODEC

    init {
        this.registerDefaultState(stateDefinition.any().setValue(SQUISHED, false))
    }

    override fun entityInside(state: BlockState, world: Level, pos: BlockPos, entity: Entity) {
        if (!getPopCheck(state) && !world.isClientSide && !entity.type.`is`(DuskEntityTypeTags.DONT_POP_FOG_BUBBLES)) {
            if ((world.gameRules.getBoolean(GameRules.RULE_MOBGRIEFING) || entity is Player) &&
                entity.mayInteract(world, pos)
            ) {
                setPopped(state, world, pos)
            }
        }
    }

    override fun randomTick(state: BlockState, world: ServerLevel, pos: BlockPos, random: RandomSource) {
        val randInt = random.nextInt(127)
        if (randInt == 0 || (state.getValue(SQUISHED) && randInt <= 8)) {
            world.setBlockAndUpdate(pos, state.setValue(SQUISHED, !state.getValue(SQUISHED)))
            val ofCenter = pos.center
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

    override fun getShape(
        state: BlockState,
        world: BlockGetter,
        pos: BlockPos,
        context: CollisionContext
    ): VoxelShape {
        if (!getPopCheck(state))
            return super.getShape(state, world, pos, context)
        else {
            val facing = state.getValue(FACING)
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

    override fun getOcclusionShape(state: BlockState, world: BlockGetter, pos: BlockPos): VoxelShape = Shapes.empty()

    override fun getCollisionShape(
        state: BlockState,
        world: BlockGetter,
        pos: BlockPos,
        context: CollisionContext
    ): VoxelShape = Shapes.empty()

    override fun skipRendering(state: BlockState, stateFrom: BlockState, direction: Direction): Boolean {
        return super.skipRendering(state, stateFrom, direction)
    }

    override fun isPathfindable(state: BlockState, navigationType: PathComputationType): Boolean {
        return true
    }

    fun getBubbleParticle(): SimpleParticleType = ParticleTypes.BUBBLE
    fun getPopCheck(state: BlockState): Boolean = state.getValue(SQUISHED)
    fun setPopped(state: BlockState, world: Level, pos: BlockPos) =
        world.setBlockAndUpdate(pos, state.setValue(SQUISHED, true))

    fun poppedProperty() = SQUISHED

    override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block, BlockState>) {
        builder.add(poppedProperty(), FACING)
    }

    companion object {
        val CODEC: MapCodec<BubbleBlock> = simpleCodec(::BubbleBlock)
        val SQUISHED: BooleanProperty = DuskProperties.SQUISHED
        val FACING: DirectionProperty = BlockStateProperties.FACING
        val POPPED_SHAPE: VoxelShape = box(0.0, 5.0, 5.0, 1.0, 11.0, 11.0)
        val POPPED_UP_SHAPE: VoxelShape = box(5.0, 15.0, 5.0, 11.0, 16.0, 11.0)
        val POPPED_DOWN_SHAPE: VoxelShape = box(5.0, 0.0, 5.0, 11.0, 1.0, 11.0)
    }
}