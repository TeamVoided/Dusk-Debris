package org.teamvoided.dusk_debris.spell.config

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder

open class SimpleSpellConfig(val priority: Int = 20, val cooldown: Int = 8) : SpellConfig {
    companion object {
        val MAP_CODEC: MapCodec<SimpleSpellConfig> = RecordCodecBuilder.mapCodec { instance ->
            instance.group(
                Codec.INT
                    .fieldOf("priority") //runs through the registry and sorts by priority before running through cast requirements
                    .forGetter { it.priority },
                Codec.INT
                    .fieldOf("cooldoown") //usually how long until the next spell can be cast
                    .orElse(8)
                    .forGetter { it.cooldown },
            ).apply(instance, ::SimpleSpellConfig)
        }
    }

}