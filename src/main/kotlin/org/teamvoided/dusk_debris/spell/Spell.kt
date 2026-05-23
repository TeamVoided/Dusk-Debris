package org.teamvoided.dusk_debris.spell

import com.mojang.serialization.Codec
import net.minecraft.core.RegistryCodecs
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.resources.RegistryFileCodec
import net.minecraft.resources.RegistryFixedCodec
import net.minecraft.util.RandomSource
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.level.Level
import net.minecraft.world.phys.Vec3
import org.teamvoided.dusk_debris.init.DuskRegistries
import org.teamvoided.dusk_debris.init.DuskRegistryKeys

class Spell<SS : SpellSettings, S : SpellType<SS>>(val spellType: S, val settings: SS) {
    fun castRequirements(castor: LivingEntity): Boolean = spellType.castRequirements(castor, settings)
    fun onCast(castor: LivingEntity) = spellType.onCast(castor, settings)
    fun castTick(castor: LivingEntity) = spellType.castTick(castor, settings)
    fun onCastEnd(castor: LivingEntity) = spellType.onCastEnd(castor, settings)
    fun actualSpell(castor: LivingEntity) = spellType.actualSpell(castor, settings)
    fun nonEntityBehavior(world: Level, random: RandomSource, pos: Vec3, rotation: Vec3) =
        spellType.nonEntityBehavior(world, random, pos, rotation, settings)

    companion object {
        val CODEC: Codec<Spell<out SpellSettings, out SpellType<out SpellSettings>>> =
            DuskRegistries.SPELL_TYPE.byNameCodec().dispatch({ it.spellType }, { it.getCodec() })

        val ENTRY_CODEC = RegistryFixedCodec.create(DuskRegistryKeys.SPELL)
        val ENTRY_PACKET_CODEC = ByteBufCodecs.holderRegistry(DuskRegistryKeys.SPELL)

        val REGISTRY_CODEC = RegistryFileCodec.create(DuskRegistryKeys.SPELL, CODEC)
        val LIST_CODEC = RegistryCodecs.homogeneousList(DuskRegistryKeys.SPELL, CODEC)
    }
}