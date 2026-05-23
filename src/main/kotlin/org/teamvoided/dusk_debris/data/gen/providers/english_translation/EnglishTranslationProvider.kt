package org.teamvoided.dusk_debris.data.gen.providers.english_translation

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.EntityType
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import org.teamvoided.dusk_debris.data.DuskSpells
import org.teamvoided.dusk_debris.data.tags.DuskFluidTags
import org.teamvoided.dusk_debris.data.tags.DuskItemTags
import org.teamvoided.dusk_debris.init.DuskEntities
import org.teamvoided.dusk_debris.init.DuskItems
import org.teamvoided.dusk_debris.init.DuskTabs
import org.teamvoided.dusk_debris.init.DuskTabs.getKey
import java.util.concurrent.CompletableFuture

@Suppress("MemberVisibilityCanBePrivate")
class EnglishTranslationProvider(o: FabricDataOutput, r: CompletableFuture<HolderLookup.Provider>) :
    FabricLanguageProvider(o, r) {

    override fun generateTranslations(lookup: HolderLookup.Provider, gen: TranslationBuilder) {
        DuskItems.ITEMS.forEach { gen.add(it.descriptionId, genLang(it.id)) }
//        DuskBlocks.BLOCKS.forEach { gen.add(it.translationKey, genLang(it.id)) }
        DuskEntities.ENTITIES.forEach { gen.add(it.descriptionId, genLang(it.id)) }
        DuskItemTags.ITEM_TAGS.forEach { gen.add(it.translationKey, genLang(it.location)) }
        DuskFluidTags.FLUID_TAGS.forEach { gen.add(it.translationKey, genLang(it.location)) }

        DuskSpells.SPELLS.forEach { gen.add(DuskSpells.spellTranslation(it).string, genLang(it.location())) }

        gen.add("container.treasure_chest", "Treasure Chest")

        DamageTypeTranslations.translations(gen)
        PaintingTranslations.translations(gen)

        //getKey(DuskTabs.DUSK_TAB)?.let { gen.add(it, "Dusk Items") }
        getKey(DuskTabs.EVERYTHING)?.let { gen.add(it, "Dusk Items") }
        getKey(DuskTabs.SPELLS)?.let { gen.add(it, "Dusk Spells") }
    }

    private fun genLang(identifier: ResourceLocation): String =
        identifier.path.split("_").joinToString(" ") { it.replaceFirstChar(Char::uppercaseChar) }

    val Item.id get() = BuiltInRegistries.ITEM.getKey(this)
    val Block.id get() = BuiltInRegistries.BLOCK.getKey(this)
    val EntityType<*>.id get() = BuiltInRegistries.ENTITY_TYPE.getKey(this)
}