package org.teamvoided.dusk_debris.component

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.client.item.TooltipConfig
import net.minecraft.item.Item
import net.minecraft.item.TooltipAppender
import net.minecraft.network.RegistryByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.registry.Holder
import net.minecraft.text.Text
import org.teamvoided.dusk_debris.spell.Spell
import org.teamvoided.dusk_debris.spell.SpellType
import java.util.function.Consumer

class SpellComponent(val spell: Holder<Spell<*, *>>) : TooltipAppender {
    override fun appendToTooltip(
        context: Item.TooltipContext,
        tooltipConsumer: Consumer<Text>,
        config: TooltipConfig
    ) {
        tooltipConsumer.accept(SpellType.getFullName(spell))
    }

    companion object {
        fun Holder<Spell<*, *>>.toComponent() = SpellComponent(this)

        val CODEC: Codec<SpellComponent> =
            RecordCodecBuilder.create { instance ->
                instance.group(
                    Spell.ENTRY_CODEC
                        .fieldOf("spell")
                        .forGetter { it.spell }
                ).apply(instance) { SpellComponent(it) }
            }
        val PACKET_CODEC: PacketCodec<RegistryByteBuf, SpellComponent> =
            PacketCodec.tuple(
                Spell.ENTRY_PACKET_CODEC, { it.spell },
                ::SpellComponent
            )
    }
}