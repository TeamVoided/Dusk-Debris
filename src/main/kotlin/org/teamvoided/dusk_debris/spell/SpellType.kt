package org.teamvoided.dusk_debris.spell

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import net.minecraft.ChatFormatting
import net.minecraft.core.Holder
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.ComponentUtils
import net.minecraft.network.chat.MutableComponent
import net.minecraft.network.chat.Style
import net.minecraft.util.RandomSource
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.level.Level
import net.minecraft.world.phys.Vec3
import org.teamvoided.dusk_debris.init.DuskParticles
import org.teamvoided.dusk_debris.init.DuskRegistries
import org.teamvoided.dusk_debris.spell.settings.GenericSpellSettings

abstract class SpellType<SS : SpellSettings>(configCodec: Codec<SS>) {
    private val codec: MapCodec<Spell<SS, SpellType<SS>>> = configCodec.fieldOf("settings")
        .xmap({ Spell(this, it) }, { it.settings })

    fun getCodec() = codec

    fun id() = DuskRegistries.SPELL_TYPE.getKey(this)!!

    open fun castRequirements(castor: LivingEntity, settings: SS): Boolean = true

    open fun onCast(castor: LivingEntity, settings: SS) {}

    open fun castTick(castor: LivingEntity, settings: SS) {}

    open fun onCastEnd(castor: LivingEntity, settings: SS) {}

    abstract fun actualSpell(castor: LivingEntity, settings: SS)

    open fun nonEntityBehavior(world: Level, random: RandomSource, pos: Vec3, rotation: Vec3, settings: SS) {
        if (world.isClientSide) {
            repeat(10) {
                val particlePos = Vec3(
                    (random.nextDouble() - 0.5),
                    (random.nextDouble() - 0.5),
                    (random.nextDouble() - 0.5)
                ).add(pos)
                val velocity = rotation.scale(0.1)
                world.addParticle(
                    DuskParticles.DRAINED_SOUL,
                    particlePos.x, particlePos.y, particlePos.z,
                    velocity.x, velocity.y, velocity.z
                )
            }
        }
    }

    companion object {
        fun getFullName(spell: Holder<Spell<*, *>>): Component {
            val mutableText: MutableComponent = (spell.value().settings as GenericSpellSettings).description.copy()
            ComponentUtils.mergeStyles(mutableText, Style.EMPTY.withColor(ChatFormatting.WHITE))
            return mutableText
        }

        //val PACKET_CODEC = PacketCodecs.fromCodec(DuskRegistries.Spell.codec)
//        val MAP_CODEC: MapCodec<Spell> = RecordCodecBuilder.mapCodec { instance ->
//            instance.group(
//                Codec.INT
//                    .fieldOf("priority") //runs through the registry and sorts by priority before running through cast requirements
//                    .forGetter { it.priority },
//                Codec.INT
//                    .fieldOf("cooldoown") //usually how long until the next spell can be cast
//                    .orElse(8)
//                    .forGetter { it.cooldown },
//            ).apply(instance, ::Spell)
//        }
    }
}