package org.teamvoided.dusk_debris.data.gen.providers.english_translation

import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider

object DamageTypeTranslations {
    fun translations(gen: FabricLanguageProvider.TranslationBuilder) {
        gen.defaultDamageTranslaion("acid", "was dissolved")
        gen.defaultDamageTranslaion("electrocuted", "was shocked", " to find something")
    }

    private fun FabricLanguageProvider.TranslationBuilder.defaultDamageTranslaion(
        translationKey: String,
        message: String = "didnt put a string in for the death text",
        extraFlavor: String = "",
        transition: String = "by",
        tryingToEscape: String = "whilst trying to escape",
        using: String = "using"
    ) {
        this.add("death.attack.$translationKey", "%s $message$extraFlavor")
        this.add("death.attack.$translationKey.item", "%s $message $transition %s $using %s")
        this.add("death.attack.$translationKey.player", "%s $message$extraFlavor $tryingToEscape %s")
    }
}