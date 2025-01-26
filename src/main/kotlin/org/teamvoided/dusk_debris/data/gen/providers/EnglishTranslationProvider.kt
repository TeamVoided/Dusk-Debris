package org.teamvoided.dusk_debris.data.gen.providers

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider
import net.minecraft.block.Block
import net.minecraft.entity.EntityType
import net.minecraft.item.Item
import net.minecraft.registry.HolderLookup
import net.minecraft.registry.Registries
import net.minecraft.util.Identifier
import org.teamvoided.dusk_debris.data.gen.providers.english_translation.DamageTypeTranslations
import org.teamvoided.dusk_debris.data.gen.providers.english_translation.PaintingTranslations
import org.teamvoided.dusk_debris.entity.DuskEntityLists
import org.teamvoided.dusk_debris.init.*
import org.teamvoided.dusk_debris.init.DuskTabs.getKey
import java.util.concurrent.CompletableFuture

@Suppress("MemberVisibilityCanBePrivate")
class EnglishTranslationProvider(o: FabricDataOutput, r: CompletableFuture<HolderLookup.Provider>) :
    FabricLanguageProvider(o, r) {
    val entities = listOf(
        DuskEntities.GUNPOWDER_BARREL,
        DuskEntities.BOX_AREA_EFFECT_CLOUD
    ) +
            DuskEntityLists.THROWABLE_BOMB_ENTITIES +
            DuskEntityLists.DUSK_SKELETON_ENTITIES

    override fun generateTranslations(lookup: HolderLookup.Provider, gen: TranslationBuilder) {
        DuskItems.ITEMS.forEach { gen.add(it.translationKey, genLang(it.id)) }
//        DuskBlocks.BLOCKS.forEach { gen.add(it.translationKey, genLang(it.id)) }
        entities.forEach { gen.add(it.translationKey, genLang(it.id)) }

        gen.add("container.treasure_chest", "Treasure Chest")

        DamageTypeTranslations.translations(gen)
        PaintingTranslations.translations(gen)

        getKey(DuskTabs.DUSK_TAB)?.let { gen.add(it, "Dusk Items") }
        getKey(DuskTabs.EVERYTHING)?.let { gen.add(it, "Dusk Items") }
    }

    private fun genLang(identifier: Identifier): String =
        identifier.path.split("_").joinToString(" ") { it.replaceFirstChar(Char::uppercaseChar) }

    val Item.id get() = Registries.ITEM.getId(this)
    val Block.id get() = Registries.BLOCK.getId(this)
    val EntityType<*>.id get() = Registries.ENTITY_TYPE.getId(this)
}