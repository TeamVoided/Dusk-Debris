package org.teamvoided.dusk_debris.entity.helper

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.core.Holder
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import org.teamvoided.dusk_debris.spell.Spell
import java.util.*
import kotlin.jvm.optionals.getOrNull

data class SpellController(
    private var spell: Holder<Spell<*, *>>? = null,
    var spellTicksLeft: Int = 0
) {
    fun setSpell(livingEntity: LivingEntity, spell: Holder<Spell<*, *>>?) {
        this.spell = spell
        if (this.spell != null) this.spell!!.value().onCast(livingEntity)
    }

    fun tick(entity: LivingEntity) {
        if (spell != null) {
            spell!!.value().castTick(entity)
            if (spellTicksLeft <= 0) {
                spell!!.value().onCastEnd(entity)
                spellTicksLeft = 0
                spell = null
            } else {
                spellTicksLeft--
            }
            (entity as? Player)?.displayClientMessage(Component.literal("ticks left: $spellTicksLeft"), true)
        }
    }

    companion object {
        val CODEC: Codec<SpellController> = RecordCodecBuilder.create { instance ->
            instance.group(
                Spell.REGISTRY_CODEC
                    .optionalFieldOf("spell")
                    .forGetter { Optional.ofNullable(it.spell) },
                Codec.INT
                    .fieldOf("spell_ticks_left")
                    .orElse(0)
                    .forGetter { it.spellTicksLeft },
            ).apply(instance) { spell, tick -> SpellController(spell.getOrNull(), tick) }
        }
    }
}