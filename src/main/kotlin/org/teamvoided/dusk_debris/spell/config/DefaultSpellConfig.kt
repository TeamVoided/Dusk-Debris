package org.teamvoided.dusk_debris.spell.config

import com.mojang.serialization.Codec
import net.minecraft.world.gen.feature.DefaultFeatureConfig

open class DefaultSpellConfig : SpellConfig {

    companion object {
        val INSTANCE: DefaultSpellConfig = DefaultSpellConfig()
        val CODEC: Codec<DefaultSpellConfig> = Codec.unit { INSTANCE }

        //val MAP_CODEC: MapCodec<DefaultSpellConfig> = RecordCodecBuilder.mapCodec { instance ->
        //    instance.group(
        //        Codec.INT
        //            .fieldOf("priority") //runs through the registry and sorts by priority before running through cast requirements
        //            .forGetter { it.priority },
        //        Codec.INT
        //            .fieldOf("cooldoown") //usually how long until the next spell can be cast
        //            .orElse(8)
        //            .forGetter { it.cooldown },
        //    ).apply(instance, ::DefaultSpellConfig)
        //}
    }
}
