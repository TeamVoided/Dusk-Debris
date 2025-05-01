package org.teamvoided.dusk_debris.init

import net.minecraft.component.DataComponentType
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import org.teamvoided.dusk_debris.DuskDebris.id
import org.teamvoided.dusk_debris.component.SpellComponent

object DuskComponents {
    val SPELL = register("spell") { it.codec(SpellComponent.CODEC).build() }
    fun <T> register(
        name: String,
        build: (DataComponentType.Builder<T>) -> DataComponentType<T>
    ): DataComponentType<T> =
        Registry.register(Registries.DATA_COMPONENT_TYPE, id(name), build(DataComponentType.builder()))

    fun init() {}
}