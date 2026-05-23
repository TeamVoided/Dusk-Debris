package org.teamvoided.dusk_debris.init

import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder
import net.minecraft.core.Registry
import org.teamvoided.dusk_debris.spell.SpellType

object DuskRegistries {
    fun init() {}
    val SPELL_TYPE: Registry<SpellType<*>> = FabricRegistryBuilder.createSimple(DuskRegistryKeys.SPELL_TYPE).buildAndRegister()
}