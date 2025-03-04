package org.teamvoided.dusk_debris.block

import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.ScaffoldingBlock
import net.minecraft.state.StateManager
import net.minecraft.state.property.Properties
import net.minecraft.state.property.Property
import org.teamvoided.dusk_debris.block.not_blocks.DuskProperties

class StrongScaffoldingBlock(settings: Settings) : ScaffoldingBlock(settings) {

    override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) {
        builder.add(DuskProperties.DISTANCE_0_14, Properties.WATERLOGGED, Properties.BOTTOM)
    }

    companion object {
        @JvmStatic
        fun strongScaffoldingProperties(
            instance: StateManager.Builder<Block, BlockState>,
            properties: Array<Property<*>>
        ): StateManager.Builder<Block, BlockState> {
            properties.forEach {
                if (it == Properties.DISTANCE_0_7) {
                    instance.add(DuskProperties.DISTANCE_0_14)
                } else {
                    instance.add(it)
                }
            }
            return instance
        }
//        @JvmStatic
//        fun getDefaultScaffoldingState(block: ScaffoldingBlock, blockState: BlockState): BlockState? {
//            var defaultState = block.stateManager.defaultState
//            blockState.entries.forEach {
//                if (it != Properties.DISTANCE_0_7) {
//                    defaultState.with(it.component1(), blockState.get(it.component1()))
//                }
//            }
//            return defaultState
//        }
    }
}