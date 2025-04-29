package org.teamvoided.dusk_debris.spell

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder

data class GenericSpellSettings(val priority: Int = 20, val cooldown: Int = 8) : SpellSettings {
    companion object{
        val MAP_CODEC: MapCodec<GenericSpellSettings> = RecordCodecBuilder.mapCodec { instance ->
            instance.group(
                Codec.INT
                    .fieldOf("priority") //runs through the registry and sorts by priority before running through cast requirements
                    .forGetter { it.priority },
                Codec.INT
                    .fieldOf("cooldown") //usually how long until the next spell can be cast
                    .orElse(8)
                    .forGetter { it.cooldown },
            ).apply(instance, ::GenericSpellSettings)
        }
        val CODEC: Codec<GenericSpellSettings> = MAP_CODEC.codec()
    }
}