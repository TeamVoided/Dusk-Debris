package org.teamvoided.dusk_debris.block

import com.mojang.serialization.MapCodec
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.state.StateManager
import net.minecraft.state.property.EnumProperty
import net.minecraft.util.math.BlockPos
import net.minecraft.util.random.RandomGenerator
import net.minecraft.world.World
import org.teamvoided.dusk_debris.block.not_blocks.DuskProperties
import org.teamvoided.dusk_debris.block.not_blocks.GodhomeBronzePhase

open class BronzeBulbBlock(settings: Settings?) : Block(settings) {

    init {
        this.defaultState = stateManager.defaultState.with(PHASE, GodhomeBronzePhase.SHINING)
    }

    override fun getCodec(): MapCodec<out BronzeBulbBlock> {
        return CODEC
    }

    override fun onBlockAdded(state: BlockState, world: World, pos: BlockPos, oldState: BlockState, notify: Boolean) {
        if (oldState.block !== state.block && world is ServerWorld) {
            this.setState(state, world, pos)
        }
    }

    override fun randomDisplayTick(state: BlockState, world: World, pos: BlockPos, random: RandomGenerator) {
        if (state.get(PHASE) == GodhomeBronzePhase.RADIANT) ShiftBlock.godhomeStrongParticles(world, pos, 1)
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
        if (world.isReceivingRedstonePower(pos)) {
            val power = (world.getReceivedRedstonePower(pos) / 7.5).toInt()
            world.setBlockState(pos, state.with(PHASE, GodhomeBronzePhase.fromInt(power)))
            world.playSound(
                null as PlayerEntity?,
                pos,
                SoundEvents.BLOCK_COPPER_BULB_TURN_ON,
                SoundCategory.BLOCKS,
                1f,
                power / 3f
            )
        }
    }

    override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) {
        builder.add(PHASE)
    }

    override fun hasComparatorOutput(state: BlockState): Boolean {
        return true
    }

    override fun getComparatorOutput(state: BlockState, world: World, pos: BlockPos): Int {
        return (state.get(PHASE).id * 7.5).toInt()
    }

    companion object {
        val CODEC: MapCodec<BronzeBulbBlock> = createCodec(::BronzeBulbBlock)
        val PHASE: EnumProperty<GodhomeBronzePhase> = DuskProperties.GODHOME_BRONZE_PHASE
    }
}