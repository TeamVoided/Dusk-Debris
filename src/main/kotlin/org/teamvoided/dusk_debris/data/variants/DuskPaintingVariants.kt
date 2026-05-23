package org.teamvoided.dusk_debris.data.variants

import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.world.entity.decoration.PaintingVariant
import org.teamvoided.dusk_debris.DuskDebris.id

object DuskPaintingVariants {
    val LIVE_BRIGGSY_REACTION = create("l_b_r")
    val FLAMEHEART_APPEARS = create("skeleton_appears")
    fun create(id: String): ResourceKey<PaintingVariant> = ResourceKey.create(Registries.PAINTING_VARIANT, id(id))
}