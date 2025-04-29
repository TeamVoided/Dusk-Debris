package org.teamvoided.dusk_debris.init

import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder
import net.minecraft.registry.Registry
import org.teamvoided.dusk_debris.DuskDebris.id
import org.teamvoided.dusk_debris.spell.Spell

object DuskRegistries {
    fun init() {}
    val SPELL: Registry<Spell<*>> =
        FabricRegistryBuilder.createDefaulted(DuskRegistryKeys.SPELL, id("spell")).buildAndRegister()
}