package org.teamvoided.dusk_debris.data.gen.tags

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalBiomeTags
import net.minecraft.registry.HolderLookup
import net.minecraft.registry.RegistryKeys
import net.minecraft.world.biome.Biome
import net.minecraft.world.biome.Biomes
import org.teamvoided.dusk_debris.data.tags.DuskBiomeTags
import org.teamvoided.dusk_debris.init.worldgen.DuskBiomes
import java.util.concurrent.CompletableFuture

class BiomeTagsProvider(o: FabricDataOutput, r: CompletableFuture<HolderLookup.Provider>) :
    FabricTagProvider<Biome>(o, RegistryKeys.BIOME, r) {
    override fun configure(arg: HolderLookup.Provider) {
        duskTags()
        vanillaTags()
        conventionTags()
        snifferTags()
    }

    fun duskTags() {
        getOrCreateTagBuilder(DuskBiomeTags.TEST)
            .add(Biomes.SOUL_SAND_VALLEY)
        getOrCreateTagBuilder(DuskBiomeTags.CRIMSON)
            .add(Biomes.CRIMSON_FOREST)
        getOrCreateTagBuilder(DuskBiomeTags.WARPED)
            .add(Biomes.WARPED_FOREST)
        getOrCreateTagBuilder(DuskBiomeTags.ASHEN)
            .add(Biomes.BASALT_DELTAS)
        getOrCreateTagBuilder(DuskBiomeTags.SOUL_VALLEY)
            .add(Biomes.SOUL_SAND_VALLEY)


        getOrCreateTagBuilder(DuskBiomeTags.FOG_START_0)
            .forceAddTag(ConventionalBiomeTags.IS_SWAMP)
            .add(Biomes.LUSH_CAVES)
            .add(Biomes.DEEP_DARK)
            .add(Biomes.DRIPSTONE_CAVES)
            .add(DuskBiomes.BOREAL_VALLEY)
        getOrCreateTagBuilder(DuskBiomeTags.FOG_START_20)
            .add(Biomes.DARK_FOREST)
//            .add(Biomes.PALE_GARDEN)

        getOrCreateTagBuilder(DuskBiomeTags.FOG_END_20)
            .add(Biomes.DRIPSTONE_CAVES)
        getOrCreateTagBuilder(DuskBiomeTags.FOG_END_80)
            .add(DuskBiomes.BOREAL_VALLEY)
        getOrCreateTagBuilder(DuskBiomeTags.FOG_BOREAL_VALLEY)
            .add(DuskBiomes.BOREAL_VALLEY)
    }

    fun vanillaTags() {
    }

    fun conventionTags() {
    }

    fun snifferTags() {
        getOrCreateTagBuilder(DuskBiomeTags.SNIFFER_BRIGHT)
            .forceAddTag(ConventionalBiomeTags.IS_JUNGLE)
            .forceAddTag(ConventionalBiomeTags.IS_MUSHROOM)
        getOrCreateTagBuilder(DuskBiomeTags.SNIFFER_SWAMP)
            .add(Biomes.SWAMP)
        getOrCreateTagBuilder(DuskBiomeTags.SNIFFER_MANGROVE_SWAMP)
            .add(Biomes.MANGROVE_SWAMP)
        getOrCreateTagBuilder(DuskBiomeTags.SNIFFER_BADLANDS)
            .forceAddTag(ConventionalBiomeTags.IS_BADLANDS)
        getOrCreateTagBuilder(DuskBiomeTags.SNIFFER_BIRCH)
            .forceAddTag(ConventionalBiomeTags.IS_BIRCH_FOREST)
        getOrCreateTagBuilder(DuskBiomeTags.SNIFFER_COLD)
            .forceAddTag(ConventionalBiomeTags.IS_COLD)
        getOrCreateTagBuilder(DuskBiomeTags.SNIFFER_WARM)
            .forceAddTag(ConventionalBiomeTags.IS_HOT_OVERWORLD)
            .add(Biomes.NETHER_WASTES)
        getOrCreateTagBuilder(DuskBiomeTags.SNIFFER_CRIMSON)
            .forceAddTag(DuskBiomeTags.CRIMSON)
        getOrCreateTagBuilder(DuskBiomeTags.SNIFFER_WARPED)
            .forceAddTag(DuskBiomeTags.WARPED)
        getOrCreateTagBuilder(DuskBiomeTags.SNIFFER_ASHEN)
            .forceAddTag(DuskBiomeTags.WARPED)
        getOrCreateTagBuilder(DuskBiomeTags.SNIFFER_PINK)
            .add(Biomes.CHERRY_GROVE)
        getOrCreateTagBuilder(DuskBiomeTags.SNIFFER_CHERRY)
            .add(Biomes.CHERRY_GROVE)
        getOrCreateTagBuilder(DuskBiomeTags.SNIFFER_SNOW)
            .forceAddTag(ConventionalBiomeTags.IS_SNOWY)
        getOrCreateTagBuilder(DuskBiomeTags.SNIFFER_FROZEN)
            .forceAddTag(ConventionalBiomeTags.IS_SNOWY)
        getOrCreateTagBuilder(DuskBiomeTags.SNIFFER_DEEP_DARK)
            .add(Biomes.DEEP_DARK)

    }

//    fun tag(
//        tag: TagKey<Biome>,
//        vararg biomes: RegistryKey<Biome>
//    ): FabricTagBuilder {
//        val tag1 = getOrCreateTagBuilder(tag)
//        biomes.forEach { tag1.add(it) }
//        return tag1
//    }
}