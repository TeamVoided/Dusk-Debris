package org.teamvoided.dusk_debris.data.gen.tags

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalBiomeTags
import net.minecraft.registry.HolderLookup
import net.minecraft.registry.RegistryKeys
import net.minecraft.world.biome.Biome
import net.minecraft.world.biome.Biomes
import org.teamvoided.dusk_debris.data.tags.DuskBiomeTags
import java.util.concurrent.CompletableFuture

class BiomeTagsProvider(o: FabricDataOutput, r: CompletableFuture<HolderLookup.Provider>) :
    FabricTagProvider<Biome>(o, RegistryKeys.BIOME, r) {
    override fun configure(arg: HolderLookup.Provider) {
        duskTags()
        vanillaTags()
        conventionTags()
    }

    fun duskTags() {
        getOrCreateTagBuilder(DuskBiomeTags.TEST)
            .add(Biomes.SOUL_SAND_VALLEY)
        getOrCreateTagBuilder(DuskBiomeTags.CRIMSON)
            .add(Biomes.CRIMSON_FOREST)
        getOrCreateTagBuilder(DuskBiomeTags.WARPED)
            .add(Biomes.WARPED_FOREST)


        getOrCreateTagBuilder(DuskBiomeTags.FOG_0_100)
            .forceAddTag(ConventionalBiomeTags.IS_SWAMP)
            .add(Biomes.LUSH_CAVES)
        getOrCreateTagBuilder(DuskBiomeTags.FOG_0_50)
            .add(Biomes.DEEP_DARK)
        getOrCreateTagBuilder(DuskBiomeTags.FOG_0_10)
            .add(Biomes.DRIPSTONE_CAVES)
        getOrCreateTagBuilder(DuskBiomeTags.FOG_20_100)
            .add(Biomes.DARK_FOREST)
//            .add(Biomes.PALE_GARDEN)
    }

    fun vanillaTags() {
    }

    fun conventionTags() {
    }
}