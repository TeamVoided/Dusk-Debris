package org.teamvoided.dusk_debris.data.gen.providers

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider
import net.minecraft.block.Block
import net.minecraft.entity.EntityType
import net.minecraft.item.Item
import net.minecraft.registry.HolderLookup
import net.minecraft.registry.Registries
import net.minecraft.util.Identifier
import org.teamvoided.dusk_debris.data.DuskPaintingVariants
import org.teamvoided.dusk_debris.entity.DuskEntityLists
import org.teamvoided.dusk_debris.init.DuskBlocks
import org.teamvoided.dusk_debris.init.DuskEntities
import org.teamvoided.dusk_debris.item.DuskItemLists
import org.teamvoided.dusk_debris.init.DuskItemGroups.DUSK_TAB
import org.teamvoided.dusk_debris.init.DuskItemGroups.getKey
import org.teamvoided.dusk_debris.init.DuskItems
import java.util.concurrent.CompletableFuture

@Suppress("MemberVisibilityCanBePrivate")
class EnglishTranslationProvider(o: FabricDataOutput, r: CompletableFuture<HolderLookup.Provider>) :
    FabricLanguageProvider(o, r) {

    val items = listOf(
        DuskItems.BLACKSTONE_SWORD,
        DuskItems.BLACKSTONE_PICKAXE,
        DuskItems.BLACKSTONE_AXE,
        DuskItems.BLACKSTONE_SHOVEL,
        DuskItems.BLACKSTONE_HOE,
        DuskItems.BLUE_NETHERSHROOM,
        DuskItems.PURPLE_NETHERSHROOM,
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
    ) +
            DuskItemLists.THROWABLE_BOMB_ITEM_LIST +
            DuskItemLists.GUNPOWDER_BARREL_ITEM_LIST +
            DuskItemLists.DECORATIVE_GOLD_SKELETON_ITEM_LIST +
            DuskItemLists.SPAWN_EGGS_ITEM_LIST
    val blocks = listOf(
        DuskBlocks.GUNPOWDER
    )
    val entities = listOf(
        DuskEntities.GUNPOWDER_BARREL,
        DuskEntities.BOX_AREA_EFFECT_CLOUD
    ) +
            DuskEntityLists.THROWABLE_BOMB_ENTITIES +
            DuskEntityLists.DUSK_SKELETON_ENTITIES

    override fun generateTranslations(lookup: HolderLookup.Provider, gen: TranslationBuilder) {
        items.forEach { gen.add(it.translationKey, genLang(it.id)) }
        blocks.forEach { gen.add(it.translationKey, genLang(it.id)) }
        entities.forEach { gen.add(it.translationKey, genLang(it.id)) }

        gen.add("painting.dusk_debris.l_b_r.title", "L. Briggsy R.")
        gen.add("painting.dusk_debris.l_b_r.author", "Original Model By Ivan Yosifov")

        getKey(DUSK_TAB)?.let { gen.add(it, "Dusk Items") }
    }

    private fun genLang(identifier: Identifier): String =
        identifier.path.split("_").joinToString(" ") { it.replaceFirstChar(Char::uppercaseChar) }

    val Item.id get() = Registries.ITEM.getId(this)
    val Block.id get() = Registries.BLOCK.getId(this)
    val EntityType<*>.id get() = Registries.ENTITY_TYPE.getId(this)
}