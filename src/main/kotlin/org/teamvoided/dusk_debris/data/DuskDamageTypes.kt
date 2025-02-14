package org.teamvoided.dusk_debris.data

import net.minecraft.entity.damage.DamageType
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import org.teamvoided.dusk_debris.DuskDebris

object DuskDamageTypes {

    val ACID = create("acid")
    val ELECTRICITY = create("electricity")
    val INDIRECT_ELECTRICITY = create("indirect_electricity")

    fun create(id: String): RegistryKey<DamageType> {
        return RegistryKey.of(RegistryKeys.DAMAGE_TYPE, DuskDebris.id(id))
    }
}