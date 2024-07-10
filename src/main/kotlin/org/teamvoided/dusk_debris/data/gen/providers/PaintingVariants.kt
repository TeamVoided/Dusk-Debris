package org.teamvoided.dusk_debris.data.gen.providers

import net.minecraft.entity.decoration.painting.PaintingVariant
import net.minecraft.registry.BootstrapContext
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.util.Identifier
import org.teamvoided.dusk_debris.data.DuskPaintingVariants.FLAMEHEART_APPEARS
import org.teamvoided.dusk_debris.data.DuskPaintingVariants.LIVE_BRIGGSY_REACTION

object PaintingVariants {
    fun bootstrap(c: BootstrapContext<PaintingVariant>) {
        c.registerPaintingVariant(LIVE_BRIGGSY_REACTION, 3, 2)
        c.registerPaintingVariant(FLAMEHEART_APPEARS, 4, 4)
    }

    fun BootstrapContext<PaintingVariant>.registerPaintingVariant(
        registryKey: RegistryKey<PaintingVariant>,
        width: Int,
        height: Int
    ) {
        this.register(registryKey, PaintingVariant(width, height, registryKey.value))
    }

    fun getKey(name: String): RegistryKey<PaintingVariant> {
        return RegistryKey.of(RegistryKeys.PAINTING_VARIANT, Identifier.ofDefault(name))
    }
}