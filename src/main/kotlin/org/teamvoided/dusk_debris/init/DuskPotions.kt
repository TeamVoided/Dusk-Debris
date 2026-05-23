package org.teamvoided.dusk_debris.init

import net.minecraft.core.Holder
import net.minecraft.core.Registry
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.item.alchemy.Potion
import org.teamvoided.dusk_debris.DuskDebris.id

object DuskPotions {
    fun init() = Unit


    private fun register(id: String, entry: Potion): Holder<Potion> =
        Registry.registerForHolder(BuiltInRegistries.POTION, id(id), entry)
}