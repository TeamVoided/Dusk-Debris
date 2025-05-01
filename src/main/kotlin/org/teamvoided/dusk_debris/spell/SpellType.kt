package org.teamvoided.dusk_debris.spell

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import net.minecraft.entity.LivingEntity
import net.minecraft.registry.Holder
import net.minecraft.registry.Registries
import net.minecraft.text.MutableText
import net.minecraft.text.Style
import net.minecraft.text.Text
import net.minecraft.text.Texts
import net.minecraft.util.Formatting
import net.minecraft.util.Util
import net.minecraft.util.math.Vec3d
import net.minecraft.util.random.RandomGenerator
import net.minecraft.world.World
import org.teamvoided.dusk_debris.init.DuskParticles
import org.teamvoided.dusk_debris.init.DuskRegistries
import org.teamvoided.dusk_debris.spell.settings.GenericSpellSettings

abstract class SpellType<SS : SpellSettings>(configCodec: Codec<SS>) {
    private val codec: MapCodec<Spell<SS, SpellType<SS>>> = configCodec.fieldOf("settings")
        .xmap({ Spell(this, it) }, { it.settings })

    fun getCodec() = codec

    fun id() = DuskRegistries.SPELL_TYPE.getId(this)!!

    open fun castRequirements(castor: LivingEntity, settings: SS): Boolean = true

    open fun onCast(castor: LivingEntity, settings: SS) {}

    open fun castTick(castor: LivingEntity, settings: SS) {}

    open fun onCastEnd(castor: LivingEntity, settings: SS) {}

    abstract fun actualSpell(castor: LivingEntity, settings: SS)

    open fun nonEntityBehavior(world: World, random: RandomGenerator, pos: Vec3d, rotation: Vec3d, settings: SS) {
        if (world.isClient) {
            repeat(10) {
                val particlePos = Vec3d(
                    (random.nextDouble() - 0.5),
                    (random.nextDouble() - 0.5),
                    (random.nextDouble() - 0.5)
                ).add(pos)
                val velocity = rotation.multiply(0.1)
                world.addParticle(
                    DuskParticles.DRAINED_SOUL,
                    particlePos.x, particlePos.y, particlePos.z,
                    velocity.x, velocity.y, velocity.z
                )
            }
        }
    }

    companion object {
        fun getFullName(spell: Holder<Spell<*, *>>): Text {
            val mutableText: MutableText = (spell.value().settings as GenericSpellSettings).description.copy()
            Texts.setStyleIfAbsent(mutableText, Style.EMPTY.withColor(Formatting.WHITE))
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