package org.teamvoided.dusk_debris.block

import com.mojang.serialization.MapCodec
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.item.ItemPlacementContext
import net.minecraft.state.StateManager
import net.minecraft.state.property.DirectionProperty
import net.minecraft.state.property.Properties
import net.minecraft.util.BlockMirror
import net.minecraft.util.BlockRotation
import net.minecraft.util.math.Direction

open class SixWayFacingBlock(settings: Settings) : Block(settings) {
    public override fun getCodec(): MapCodec<out SixWayFacingBlock> {
        return CODEC
    }

    init {
        this.defaultState = defaultState.with(FACING, Direction.UP)
    }

    override fun getPlacementState(ctx: ItemPlacementContext): BlockState {
        return defaultState.with(FACING, ctx.side)
    }
    override fun rotate(state: BlockState, rotation: BlockRotation): BlockState {
        return state.with(FACING, rotation.rotate(state.get(FACING)))
    }

    override fun mirror(state: BlockState, mirror: BlockMirror): BlockState {
        return state.rotate(mirror.getRotation(state.get(FACING)))
    }

    override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) {
        builder.add(FACING)
    }

    companion object {
        val CODEC: MapCodec<SixWayFacingBlock> = createCodec(::SixWayFacingBlock)
        val FACING: DirectionProperty = Properties.FACING
    }
}