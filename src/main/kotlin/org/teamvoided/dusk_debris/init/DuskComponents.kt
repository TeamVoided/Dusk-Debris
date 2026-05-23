package org.teamvoided.dusk_debris.init

import net.minecraft.core.Registry
import net.minecraft.core.component.DataComponentType
import net.minecraft.core.registries.BuiltInRegistries
import org.teamvoided.dusk_debris.DuskDebris.id
import org.teamvoided.dusk_debris.component.SpellComponent

object DuskComponents {
    val SPELL = register("spell") { it.persistent(SpellComponent.CODEC).build() }
    fun <T> register(
        name: String,
        build: (DataComponentType.Builder<T>) -> DataComponentType<T>
    ): DataComponentType<T> =
        Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE, id(name), build(DataComponentType.builder()))

    fun init() {}
}