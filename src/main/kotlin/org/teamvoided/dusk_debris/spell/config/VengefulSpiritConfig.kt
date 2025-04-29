package org.teamvoided.dusk_debris.spell.config

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder

class VengefulSpiritConfig(val damage: Float, priority: Int = 10, cooldown: Int = 8) :
    SimpleSpellConfig(priority, cooldown) {
    constructor(damage: Float, config: SimpleSpellConfig) : this(damage, config.priority, config.cooldown)

    companion object {
        val CODEC: Codec<VengefulSpiritConfig> = RecordCodecBuilder.create { instance ->
            instance.group(
                Codec.FLOAT
                    .fieldOf("damage")
                    .forGetter { it.damage },
                MAP_CODEC.forGetter { it }
            ).apply(instance, ::VengefulSpiritConfig)
        }
    }
}