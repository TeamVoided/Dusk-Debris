package org.teamvoided.dusk_debris.block

import com.mojang.serialization.MapCodec
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.util.RandomSource
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.EnumProperty
import org.teamvoided.dusk_debris.block.not_blocks.DuskProperties
import org.teamvoided.dusk_debris.block.not_blocks.GodhomeBronzePhase

open class BronzeBulbBlock(settings: Properties?) : Block(settings) {

    init {
        this.registerDefaultState(stateDefinition.any().setValue(PHASE, GodhomeBronzePhase.SHINING))
    }

    override fun codec(): MapCodec<out BronzeBulbBlock> {
        return CODEC
    }

    override fun onPlace(state: BlockState, world: Level, pos: BlockPos, oldState: BlockState, notify: Boolean) {
        if (oldState.block !== state.block && world is ServerLevel) {
            this.setState(state, world, pos)
        }
    }

    override fun animateTick(state: BlockState, world: Level, pos: BlockPos, random: RandomSource) {
        //if (state.get(PHASE) == GodhomeBronzePhase.RADIANT) ShiftBlock.godhomeStrongParticles(world, pos, 1)
    }

    override fun neighborChanged(
        state: BlockState,
        world: Level,
        pos: BlockPos,
        block: Block,
        fromPos: BlockPos,
        notify: Boolean
    ) {
        if (world is ServerLevel) {
            this.setState(state, world, pos)
        }
    }

    fun setState(state: BlockState, world: ServerLevel, pos: BlockPos?) {
        if (world.hasNeighborSignal(pos)) {
            val power = (world.getBestNeighborSignal(pos) / 7.5).toInt()
            world.setBlockAndUpdate(pos, state.setValue(PHASE, GodhomeBronzePhase.fromInt(power)))
            world.playSound(
                null as Player?,
                pos,
                SoundEvents.COPPER_BULB_TURN_ON,
                SoundSource.BLOCKS,
                1f,
                power / 3f
            )
        }
    }

    override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block, BlockState>) {
        builder.add(PHASE)
    }

    override fun hasAnalogOutputSignal(state: BlockState): Boolean {
        return true
    }

    override fun getAnalogOutputSignal(state: BlockState, world: Level, pos: BlockPos): Int {
        return (state.getValue(PHASE).id * 7.5).toInt()
    }

    companion object {
        val CODEC: MapCodec<BronzeBulbBlock> = simpleCodec(::BronzeBulbBlock)
        val PHASE: EnumProperty<GodhomeBronzePhase> = DuskProperties.GODHOME_BRONZE_PHASE
    }
}