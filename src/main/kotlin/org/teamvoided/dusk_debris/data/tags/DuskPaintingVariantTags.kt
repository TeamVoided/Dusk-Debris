package org.teamvoided.dusk_debris.data.tags

import net.minecraft.core.registries.Registries
import net.minecraft.tags.TagKey
import net.minecraft.world.entity.decoration.PaintingVariant
import org.teamvoided.dusk_debris.DuskDebris.id

object DuskPaintingVariantTags {
    val TEST = create("test")
    @JvmField
    val DROPS_SELF = create("drops_self")

    fun create(id: String): TagKey<PaintingVariant> = TagKey.create(Registries.PAINTING_VARIANT, id(id))
}