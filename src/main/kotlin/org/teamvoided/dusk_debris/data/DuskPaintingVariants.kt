package org.teamvoided.dusk_debris.data

import net.minecraft.entity.decoration.painting.PaintingVariant
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import org.teamvoided.dusk_debris.DuskDebris

object DuskPaintingVariants {
    val LIVE_BRIGGSY_REACTION = create("l_b_r")
    val SKELETON_APPEEARS = create("skeleton_appeears")
    fun create(id: String): RegistryKey<PaintingVariant> =
        RegistryKey.of(RegistryKeys.PAINTING_VARIANT, DuskDebris.id(id))
}