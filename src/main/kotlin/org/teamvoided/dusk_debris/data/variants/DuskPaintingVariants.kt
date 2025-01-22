package org.teamvoided.dusk_debris.data.variants

import net.minecraft.entity.decoration.painting.PaintingVariant
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import org.teamvoided.dusk_debris.DuskDebris
import org.teamvoided.dusk_debris.DuskDebris.id

object DuskPaintingVariants {
    val LIVE_BRIGGSY_REACTION = create("l_b_r")
    val FLAMEHEART_APPEARS = create("skeleton_appears")
    fun create(id: String): RegistryKey<PaintingVariant> = RegistryKey.of(RegistryKeys.PAINTING_VARIANT, id(id))
}