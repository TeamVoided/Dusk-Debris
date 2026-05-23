package org.teamvoided.dusk_debris.data.gen.tags

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.tags.EnchantmentTags
import net.minecraft.world.item.enchantment.Enchantment
import net.minecraft.world.item.enchantment.Enchantments
import org.teamvoided.dusk_debris.data.DuskEnchantments
import org.teamvoided.dusk_debris.data.tags.DuskEnchantmentTags
import java.util.concurrent.CompletableFuture

class EnchantmentTagsProvider(o: FabricDataOutput, r: CompletableFuture<HolderLookup.Provider>) :
    FabricTagProvider<Enchantment>(o, Registries.ENCHANTMENT, r) {
    override fun addTags(arg: HolderLookup.Provider) {
        duskTags()
        vanillaTags()
    }

    fun duskTags() {
        getOrCreateTagBuilder(DuskEnchantmentTags.PARTICLE_EXCLUSIVE_SET)
            .add(DuskEnchantments.ENCHANTMENT_PARTICLE)

        getOrCreateTagBuilder(DuskEnchantmentTags.MENDING_EXCLUSIVE_SET)
            .add(Enchantments.MENDING)
            .add(DuskEnchantments.MENDLESS)
        getOrCreateTagBuilder(DuskEnchantmentTags.UNBREAKING_EXCLUSIVE_SET)
            .add(Enchantments.UNBREAKING)
            .add(DuskEnchantments.BREAKING)
    }

    fun vanillaTags() {
        getOrCreateTagBuilder(EnchantmentTags.CURSE)
            .add(DuskEnchantments.CURSES)
        getOrCreateTagBuilder(EnchantmentTags.TREASURE)
            .add(DuskEnchantments.TREASURE)
        getOrCreateTagBuilder(EnchantmentTags.NON_TREASURE)
            .addAll(DuskEnchantments.ENCHANTMENTS.filterNot(DuskEnchantments.TREASURE::contains))
    }

    fun conventionTags() {}

    fun <T> FabricTagProvider<T>.FabricTagBuilder.addList(list: Collection<T>): FabricTagProvider<T>.FabricTagBuilder {
        list.forEach { this.add(it) }
        return this
    }

    fun FabricTagProvider<Enchantment>.FabricTagBuilder.add(list: Collection<ResourceKey<Enchantment>>): FabricTagProvider<Enchantment>.FabricTagBuilder {
        list.forEach { this.add(it) }
        return this
    }
}