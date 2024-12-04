package org.teamvoided.dusk_debris.data.gen.tags

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider
import net.minecraft.enchantment.Enchantment
import net.minecraft.enchantment.Enchantments
import net.minecraft.registry.HolderLookup
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.tag.EnchantmentTags
import org.teamvoided.dusk_debris.data.DuskEnchantments
import org.teamvoided.dusk_debris.data.gen.providers.EnchantmentsProvider
import org.teamvoided.dusk_debris.data.tags.DuskEnchantmentTags
import java.util.concurrent.CompletableFuture

class EnchantmentTagsProvider(o: FabricDataOutput, r: CompletableFuture<HolderLookup.Provider>) :
    FabricTagProvider<Enchantment>(o, RegistryKeys.ENCHANTMENT, r) {
    override fun configure(arg: HolderLookup.Provider) {
        duskTags()
        vanillaTags()
    }

    fun duskTags() {
        getOrCreateTagBuilder(DuskEnchantmentTags.PARTICLE_EXCLUSIVE_SET)
            .add(EnchantmentsProvider.ENCHANTMENT_PARTICLE)

        getOrCreateTagBuilder(DuskEnchantmentTags.MENDING_EXCLUSIVE_SET)
            .add(Enchantments.MENDING)
            .add(DuskEnchantments.MENDLESS)
        getOrCreateTagBuilder(DuskEnchantmentTags.UNBREAKING_EXCLUSIVE_SET)
            .add(Enchantments.UNBREAKING)
            .add(DuskEnchantments.BREAKING)
    }

    fun vanillaTags() {
        getOrCreateTagBuilder(EnchantmentTags.CURSE)
            .add(EnchantmentsProvider.CURSES)
        getOrCreateTagBuilder(EnchantmentTags.TREASURE)
            .add(EnchantmentsProvider.TREASURE)
        getOrCreateTagBuilder(EnchantmentTags.NON_TREASURE)
            .add(EnchantmentsProvider.ENCHANTMENTS.filterNot(EnchantmentsProvider.TREASURE::contains))
    }

    fun conventionTags() {}

    fun <T> FabricTagProvider<T>.FabricTagBuilder.add(list: Collection<T>): FabricTagProvider<T>.FabricTagBuilder {
        list.forEach { this.add(it) }
        return this
    }
}