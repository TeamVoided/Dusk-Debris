package org.teamvoided.dusk_debris.data.gen.providers

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider
import net.minecraft.block.Block
import net.minecraft.item.Item
import net.minecraft.registry.HolderLookup
import net.minecraft.registry.Registries
import net.minecraft.util.Identifier
import org.teamvoided.dusk_autumn.init.DuskItemGroups.DUSK_TAB
import org.teamvoided.dusk_autumn.init.DuskItemGroups.getKey
import org.teamvoided.dusk_autumn.init.DuskItems
import java.util.concurrent.CompletableFuture

@Suppress("MemberVisibilityCanBePrivate")
class EnglishTranslationProvider(o: FabricDataOutput, r: CompletableFuture<HolderLookup.Provider>) :
    FabricLanguageProvider(o, r) {

    val items = listOf(
        DuskItems.VOLCANIC_SAND,
        DuskItems.SUSPICIOUS_VOLCANIC_SAND,
        DuskItems.VOLCANIC_SANDSTONE,
        DuskItems.VOLCANIC_SANDSTONE_STAIRS,
        DuskItems.VOLCANIC_SANDSTONE_SLAB,
        DuskItems.VOLCANIC_SANDSTONE_WALL,
        DuskItems.CUT_VOLCANIC_SANDSTONE,
        DuskItems.CUT_VOLCANIC_SANDSTONE_SLAB,
        DuskItems.CHISELED_VOLCANIC_SANDSTONE,
        DuskItems.SMOOTH_VOLCANIC_SANDSTONE,
        DuskItems.SMOOTH_VOLCANIC_SANDSTONE_STAIRS,
        DuskItems.SMOOTH_VOLCANIC_SANDSTONE_SLAB
    )
//    val blocks = listOf(
//    )

    override fun generateTranslations(lookup: HolderLookup.Provider, gen: TranslationBuilder) {
        items.forEach { gen.add(it.translationKey, genLang(it.id)) }
//        blocks.forEach { gen.add(it.translationKey, genLang(it.id)) }


        getKey(DUSK_TAB)?.let { gen.add(it, "Dusk Items") }
    }

    private fun genLang(identifier: Identifier): String =
        identifier.path.split("_").joinToString(" ") { it.replaceFirstChar(Char::uppercaseChar) }

    val Item.id get() = Registries.ITEM.getId(this)
    val Block.id get() = Registries.BLOCK.getId(this)

}