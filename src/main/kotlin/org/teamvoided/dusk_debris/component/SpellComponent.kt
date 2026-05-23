package org.teamvoided.dusk_debris.component

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.core.Holder
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.chat.Component
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.item.Item
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.item.component.TooltipProvider
import org.teamvoided.dusk_debris.spell.Spell
import org.teamvoided.dusk_debris.spell.SpellType
import java.util.function.Consumer

class SpellComponent(val spell: Holder<Spell<*, *>>) : TooltipProvider {
    override fun addToTooltip(
        context: Item.TooltipContext,
        tooltipConsumer: Consumer<Component>,
        config: TooltipFlag
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
        val PACKET_CODEC: StreamCodec<RegistryFriendlyByteBuf, SpellComponent> =
            StreamCodec.composite(
                Spell.ENTRY_PACKET_CODEC, { it.spell },
                ::SpellComponent
            )
    }
}