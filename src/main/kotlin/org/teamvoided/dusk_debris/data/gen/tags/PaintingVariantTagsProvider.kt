package org.teamvoided.dusk_debris.data.gen.tags

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.Registries
import net.minecraft.world.entity.decoration.PaintingVariant
import net.minecraft.world.entity.decoration.PaintingVariants
import org.teamvoided.dusk_debris.data.tags.DuskPaintingVariantTags
import org.teamvoided.dusk_debris.data.variants.DuskPaintingVariants
import java.util.concurrent.CompletableFuture

class PaintingVariantTagsProvider(o: FabricDataOutput, r: CompletableFuture<HolderLookup.Provider>) :
    FabricTagProvider<PaintingVariant>(o, Registries.PAINTING_VARIANT, r) {
    override fun addTags(arg: HolderLookup.Provider) {
        duskTags()
        vanillaTags()
        conventionTags()
    }

    fun duskTags() {
        tag(DuskPaintingVariantTags.DROPS_SELF)
            .add(DuskPaintingVariants.LIVE_BRIGGSY_REACTION)
            .add(DuskPaintingVariants.FLAMEHEART_APPEARS)
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