package org.teamvoided.dusk_debris.block

import com.mojang.serialization.MapCodec
import net.minecraft.block.Block
import net.minecraft.block.BlockRenderType
import net.minecraft.block.BlockState
import net.minecraft.block.BlockWithEntity
import net.minecraft.block.entity.*
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemPlacementContext
import net.minecraft.particle.ParticleTypes
import net.minecraft.server.world.ServerWorld
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.state.StateManager
import net.minecraft.state.property.BooleanProperty
import net.minecraft.state.property.DirectionProperty
import net.minecraft.state.property.Properties
import net.minecraft.util.BlockMirror
import net.minecraft.util.BlockRotation
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.random.RandomGenerator
import net.minecraft.world.World
import org.teamvoided.dusk_debris.block.entity.FanBlockEntity
import org.teamvoided.dusk_debris.init.DuskBlockEntities
import org.teamvoided.dusk_debris.init.DuskParticles

open class FanBlock(val strength: Int, settings: Settings) :
    BlockWithEntity(settings) {
    public override fun getCodec(): MapCodec<FanBlock> {
        return CODEC
    }

    init {
        this.defaultState = stateManager.defaultState
            .with(ACTIVE, false)
            .with(POWERED, false)
            .with(FACING, Direction.UP)
    }

    override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) {
        super.appendProperties(builder)
        builder.add(ACTIVE, POWERED, FACING)
    }

    override fun createBlockEntity(pos: BlockPos, state: BlockState): BlockEntity? {
        return FanBlockEntity(pos, state)
    }

    override fun getRenderType(state: BlockState): BlockRenderType {
        return BlockRenderType.MODEL
    }

    override fun getPlacementState(ctx: ItemPlacementContext): BlockState {
        return defaultState.with(FACING, ctx.side)
    }

    override fun onBlockAdded(state: BlockState, world: World, pos: BlockPos, oldState: BlockState, notify: Boolean) {
        if (oldState.block != state.block && world is ServerWorld) {
            this.setState(state, world, pos)
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
        if (world is ServerWorld) {
            this.setState(state, world, pos)
        }
    }

    fun setState(state: BlockState, world: ServerWorld, pos: BlockPos?) {
        val bl = world.isReceivingRedstonePower(pos)
        if (bl != state.get(POWERED)) {
            var blockState = state
            if (!state.get(POWERED)) {
                blockState = state.cycle(ACTIVE)
                world.playSound(
                    null as PlayerEntity?,
                    pos,
                    if (blockState.get(ACTIVE)) SoundEvents.BLOCK_COPPER_BULB_TURN_ON
                    else SoundEvents.BLOCK_COPPER_BULB_TURN_OFF,
                    SoundCategory.BLOCKS
                )
            }
            world.setBlockState(pos, blockState.with(POWERED, bl), 3)
        }
    }


    override fun <T : BlockEntity> getTicker(
        world: World,
        state: BlockState,
        type: BlockEntityType<T>
    ): BlockEntityTicker<T>? {
        return if (state.get(ACTIVE)) {
            checkType(
                type,
                DuskBlockEntities.FAN_BLOCK,
                if (world.isClient) FanBlockEntity::clientTick
                else FanBlockEntity::serverTick
            )
        } else {
            null
        }
    }

    override fun rotate(state: BlockState, rotation: BlockRotation): BlockState {
        return state.with(FACING, rotation.rotate(state.get(FACING)))
    }

    override fun mirror(state: BlockState, mirror: BlockMirror): BlockState {
        return state.rotate(mirror.getRotation(state.get(FACING)))
    }

    override fun hasComparatorOutput(state: BlockState): Boolean = true

    override fun getComparatorOutput(state: BlockState, world: World, pos: BlockPos): Int {
        return if (world.getBlockState(pos).get(ACTIVE)) 15 else 0
    }

    override fun randomDisplayTick(state: BlockState, world: World, pos: BlockPos, random: RandomGenerator) {
//        if (state.get(ACTIVE)) {
//            val centerPos = pos.add(state.get(FACING).vector).ofCenter()
//            world.addParticle(
//                ParticleTypes.CLOUD,
//                true,
//                centerPos.x,
//                centerPos.y,
//                centerPos.z,
//                0.0,
//                0.0,
//                0.0
//            )
//        }
        super.randomDisplayTick(state, world, pos, random)
    }

    companion object {
        val CODEC: MapCodec<FanBlock> = createCodec { settings: Settings ->
            FanBlock(
                15,
                settings
            )
        }
        val POWERED: BooleanProperty = Properties.POWERED
        val ACTIVE: BooleanProperty = BooleanProperty.of("active")
        val FACING: DirectionProperty = Properties.FACING
    }
}