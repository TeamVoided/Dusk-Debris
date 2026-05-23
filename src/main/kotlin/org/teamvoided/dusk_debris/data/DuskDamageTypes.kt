package org.teamvoided.dusk_debris.data

import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.world.damagesource.DamageType
import org.teamvoided.dusk_debris.DuskDebris

object DuskDamageTypes {

    val ACID = create("acid")
    val ELECTRICITY = create("electricity")
    val INDIRECT_ELECTRICITY = create("indirect_electricity")

    fun create(id: String): ResourceKey<DamageType> {
        return ResourceKey.create(Registries.DAMAGE_TYPE, DuskDebris.id(id))
    }
}