package org.teamvoided.dusk_debris.data.tags

import net.minecraft.entity.damage.DamageType
import net.minecraft.fluid.Fluid
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.tag.TagKey
import org.teamvoided.dusk_debris.DuskDebris

object DuskFluidTags {

    val ACID = create("acid")


    fun create(id: String): TagKey<Fluid> = TagKey.of(RegistryKeys.FLUID, DuskDebris.id(id))
    fun create(modId: String, path: String): TagKey<Fluid> = TagKey.of(RegistryKeys.FLUID,
        DuskDebris.id(modId, path)
    )

}