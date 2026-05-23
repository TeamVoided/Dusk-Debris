package org.teamvoided.dusk_debris.data.tags

import net.minecraft.core.registries.Registries
import net.minecraft.tags.TagKey
import net.minecraft.world.damagesource.DamageType
import org.teamvoided.dusk_debris.DuskDebris

object DuskDamageTypeTags {

    val BYPASSES_GLOOM_RESISTANCE = create("bypasses_gloomed_resistance")
    val BYPASSES_GREED_RESISTANCE = create("bypasses_greed_resistance")


    fun create(id: String): TagKey<DamageType> = TagKey.create(Registries.DAMAGE_TYPE, DuskDebris.id(id))
    fun create(modId: String, path: String): TagKey<DamageType> = TagKey.create(
        Registries.DAMAGE_TYPE,
        DuskDebris.id(modId, path)
    )

}