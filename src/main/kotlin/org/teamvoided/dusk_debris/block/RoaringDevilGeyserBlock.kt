package org.teamvoided.dusk_debris.block

import com.mojang.serialization.MapCodec
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.state.StateManager
import net.minecraft.state.property.BooleanProperty

class RoaringDevilGeyserBlock(settings: Settings) :
    Block(settings) {
    public override fun getCodec(): MapCodec<RoaringDevilGeyserBlock> {
        return CODEC
    }

    init {
        this.defaultState =
            stateManager.defaultState
                .with(ACTIVE, false)
    }

    override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) {
        super.appendProperties(builder)
        builder.add(ACTIVE)
    }

    companion object {
        val CODEC: MapCodec<RoaringDevilGeyserBlock> = createCodec { settings: Settings ->
            RoaringDevilGeyserBlock(
                settings
            )
        }
        val ACTIVE: BooleanProperty = BooleanProperty.of("active")
    }
}