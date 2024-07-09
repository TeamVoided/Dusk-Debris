package org.teamvoided.dusk_debris.data.gen.tags

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider
import net.minecraft.entity.decoration.painting.PaintingVariant
import net.minecraft.entity.decoration.painting.PaintingVariants
import net.minecraft.registry.HolderLookup
import net.minecraft.registry.RegistryKeys
import org.teamvoided.dusk_debris.data.tags.DuskPaintingVariantTags
import org.teamvoided.dusk_debris.data.DuskPaintingVariants
import java.util.concurrent.CompletableFuture

class PaintingVariantProvider(o: FabricDataOutput, r: CompletableFuture<HolderLookup.Provider>) :
    FabricTagProvider<PaintingVariant>(o, RegistryKeys.PAINTING_VARIANT, r) {
    override fun configure(arg: HolderLookup.Provider) {
        duskTags()
        vanillaTags()
        conventionTags()
    }

    fun duskTags() {
        getOrCreateTagBuilder(DuskPaintingVariantTags.DROPS_SELF)
            .add(DuskPaintingVariants.LIVE_BRIGGSY_REACTION)
            .add(PaintingVariants.WATER)
            .add(PaintingVariants.WIND)
            .add(PaintingVariants.EARTH)
            .add(PaintingVariants.FIRE)
    }

    fun vanillaTags() {
    }

    fun conventionTags() {
    }
}