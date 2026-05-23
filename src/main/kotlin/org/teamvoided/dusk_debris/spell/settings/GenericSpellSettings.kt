package org.teamvoided.dusk_debris.spell.settings

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.ComponentSerialization
import org.teamvoided.dusk_debris.spell.SpellSettings

data class GenericSpellSettings(val description: Component, val priority: Int = 20, val cooldown: Int = 8) : SpellSettings {

    companion object {
        val MAP_CODEC: MapCodec<GenericSpellSettings> = RecordCodecBuilder.mapCodec { instance ->
            instance.group(
                ComponentSerialization.CODEC
                    .fieldOf("description")
                    .forGetter { it.description },
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

        //val PACKET_CODEC: PacketCodec<RegistryByteBuf, GenericSpellSettings> =
        //    PacketCodec.tuple(
        //        TextCodecs.UNLIMITED_TEXT_PACKET_CODEC, { it.description },
        //        ::GenericSpellSettings
        //)
    }
}