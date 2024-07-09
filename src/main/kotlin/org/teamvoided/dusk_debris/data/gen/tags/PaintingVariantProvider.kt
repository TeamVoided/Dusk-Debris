package org.teamvoided.dusk_debris.data.gen.tags

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider
import net.minecraft.entity.decoration.painting.PaintingVariant
import net.minecraft.registry.HolderLookup
import net.minecraft.registry.RegistryKeys
import net.minecraft.world.biome.Biome
import net.minecraft.world.biome.Biomes
import org.teamvoided.dusk_debris.data.DuskBiomeTags
import org.teamvoided.dusk_debris.data.DuskBlockTags
import org.teamvoided.dusk_debris.data.DuskPaintingVariantTags
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
    }

    fun vanillaTags() {
    }

    fun conventionTags() {
    }
}