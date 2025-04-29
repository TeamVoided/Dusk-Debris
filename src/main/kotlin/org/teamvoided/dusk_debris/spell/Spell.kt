package org.teamvoided.dusk_debris.spell

import com.mojang.serialization.Codec
import net.minecraft.entity.LivingEntity
import net.minecraft.registry.RegistryCodecs
import net.minecraft.util.dynamic.RegistryElementCodec
import net.minecraft.util.math.Vec3d
import net.minecraft.util.random.RandomGenerator
import net.minecraft.world.World
import org.teamvoided.dusk_debris.init.DuskRegistries
import org.teamvoided.dusk_debris.init.DuskRegistryKeys

class Spell<SS : SpellSettings, S : SpellType<SS>>(val spellType: S, val settings: SS) {
    fun castRequirements(castor: LivingEntity): Boolean = spellType.castRequirements(castor, settings)
    fun onCast(castor: LivingEntity) = spellType.onCast(castor, settings)
    fun castTick(castor: LivingEntity) = spellType.castTick(castor, settings)
    fun onCastEnd(castor: LivingEntity) = spellType.onCastEnd(castor, settings)
    fun actualSpell(castor: LivingEntity) = spellType.actualSpell(castor, settings)
    fun nonEntityBehavior(world: World, random: RandomGenerator, pos: Vec3d, rotation: Vec3d) =
        spellType.nonEntityBehavior(world, random, pos, rotation, settings)

    companion object {
        val CODEC: Codec<Spell<out SpellSettings, out SpellType<out SpellSettings>>> =
            DuskRegistries.SPELL_TYPE.codec.dispatch({ it.spellType }, { it.getCodec() })

        val REGISTRY_CODEC = RegistryElementCodec.of(DuskRegistryKeys.SPELL, CODEC)
        val LIST_CODEC = RegistryCodecs.homogeneousList(DuskRegistryKeys.SPELL, CODEC)
    }
}