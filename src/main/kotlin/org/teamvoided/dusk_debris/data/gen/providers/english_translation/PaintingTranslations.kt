package org.teamvoided.dusk_debris.data.gen.providers.english_translation

import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider

object PaintingTranslations {
    fun translations(gen: FabricLanguageProvider.TranslationBuilder) {
        gen.defaultPaintingTranslaion("l_b_r", "L. Briggsy R.", "Original Model By Ivan Yosifov")
        gen.defaultPaintingTranslaion("skeleton_appears", "The Skeleton", "Appears")
    }

    private fun FabricLanguageProvider.TranslationBuilder.defaultPaintingTranslaion(
        painting: String,
        title: String,
        author: String
    ) {
        this.add("painting.dusk_debris.$painting.title", title)
        this.add("painting.dusk_debris.$painting.author", author)
    }
}