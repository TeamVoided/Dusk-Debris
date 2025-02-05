package org.teamvoided.dusk_debris.world.gen.configured_carver.config.debug

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.world.gen.carver.CarverDebugConfig

class LakeCarverDebugConfig(
    debugMode: Boolean,
    airState: BlockState,
    waterState: BlockState,
    lavaState: BlockState,
    barrierState: BlockState,
    val fluidBarrierState: BlockState,
    val fluidState: BlockState
) : CarverDebugConfig(debugMode, airState, waterState, lavaState, barrierState) {


    companion object {

        fun default(debug: Boolean = false): LakeCarverDebugConfig {
            return LakeCarverDebugConfig(
                debug,
                Blocks.ACACIA_BUTTON.defaultState,
                Blocks.BLUE_STAINED_GLASS.defaultState,
                Blocks.ORANGE_STAINED_GLASS.defaultState,
                Blocks.GLASS.defaultState,
                Blocks.WARPED_BUTTON.defaultState,
                Blocks.TINTED_GLASS.defaultState
            )
        }

        val CODEC: Codec<LakeCarverDebugConfig> =
            RecordCodecBuilder.create { instance: RecordCodecBuilder.Instance<LakeCarverDebugConfig> ->
                instance.group(
                    Codec.BOOL.optionalFieldOf("barrier_state", default().isDebugMode)
                        .forGetter { it.isDebugMode },
                    BlockState.CODEC.optionalFieldOf("air_state", default().airState)
                        .forGetter { it.airState },
                    BlockState.CODEC.optionalFieldOf("water_state", default().waterState)
                        .forGetter { it.waterState },
                    BlockState.CODEC.optionalFieldOf("lava_state", default().lavaState)
                        .forGetter { it.lavaState },
                    BlockState.CODEC.optionalFieldOf("barrier_state", default().barrierState)
                        .forGetter { it.barrierState },
                    BlockState.CODEC.optionalFieldOf("fluid_barrier_state", default().fluidBarrierState)
                        .forGetter { it.fluidBarrierState },
                    BlockState.CODEC.optionalFieldOf("fluid_state", default().fluidState)
                        .forGetter { it.fluidState })
                    .apply(instance, ::LakeCarverDebugConfig)
            }
    }
}