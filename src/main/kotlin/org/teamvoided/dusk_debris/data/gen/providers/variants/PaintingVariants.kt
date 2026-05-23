package org.teamvoided.dusk_debris.data.gen.providers.variants

import net.minecraft.data.worldgen.BootstrapContext
import net.minecraft.resources.ResourceKey
import net.minecraft.world.entity.decoration.PaintingVariant
import org.teamvoided.dusk_debris.data.variants.DuskPaintingVariants.FLAMEHEART_APPEARS
import org.teamvoided.dusk_debris.data.variants.DuskPaintingVariants.LIVE_BRIGGSY_REACTION

object PaintingVariants {
    fun bootstrap(c: BootstrapContext<PaintingVariant>) {
        c.registerPaintingVariant(LIVE_BRIGGSY_REACTION, 3, 2)
        c.registerPaintingVariant(FLAMEHEART_APPEARS, 4, 4)
    }

    private fun BootstrapContext<PaintingVariant>.registerPaintingVariant(
        registryKey: ResourceKey<PaintingVariant>,
        width: Int,
        height: Int
    ) {
        this.register(registryKey, PaintingVariant(width, height, registryKey.location()))
    }
}