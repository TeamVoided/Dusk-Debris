package org.teamvoided.dusk_debris.data.gen.providers.english_translation

import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider
import net.minecraft.entity.damage.DamageType
import net.minecraft.registry.RegistryKey
import org.teamvoided.dusk_debris.data.DuskDamageTypes

object DamageTypeTranslations {
    fun translations(gen: FabricLanguageProvider.TranslationBuilder) {
        gen.defaultDamageTranslaion(DuskDamageTypes.ACID, "was dissolved")
        gen.damageTranslaion(
            DuskDamageTypes.ELECTRICITY,
            DuskDamageTypes.INDIRECT_ELECTRICITY,
            "made a shocking discovery",
            "was shocked to find"
        )
    }

    private fun FabricLanguageProvider.TranslationBuilder.defaultDamageTranslaion(
        key: RegistryKey<DamageType>,
        message: String,
        transition: String = "by",
        tryingToEscape: String = "whilst trying to escape",
        using: String = "using"
    ) {
        val key0 = key.value.path
        this.add("death.attack.$key0", "%s $message")
        this.add("death.attack.$key0.item", "%s $message $transition %s $using %s")
        this.add("death.attack.$key0.player", "%s $message $tryingToEscape %s")
    }

    private fun FabricLanguageProvider.TranslationBuilder.damageTranslaion(
        key: RegistryKey<DamageType>,
        message: String,
        messageItem: String,
        messageAttacker: String = messageItem
    ) {
        val key0 = key.value.path
        this.add("death.attack.$key0", "%s $message")
        this.add("death.attack.$key0.item", "%s $messageItem %s using %s")
        this.add("death.attack.$key0.player", "%s $messageAttacker %s")
    }


    private fun FabricLanguageProvider.TranslationBuilder.damageTranslaion(
        direct: RegistryKey<DamageType>,
        indirect: RegistryKey<DamageType>,
        message: String,
        messageItem: String,
        messageAttacker: String = messageItem
    ) {
        this.directDamageTranslaion(direct, message, messageAttacker)
        this.indirectDamageTranslaion(indirect, message, messageItem)
    }


    private fun FabricLanguageProvider.TranslationBuilder.directDamageTranslaion(
        key: RegistryKey<DamageType>,
        message: String,
        messageAttacker: String
    ) {
        val key0 = key.value.path
        this.add("death.attack.$key0", "%s $message")
        this.add("death.attack.$key0.player", "%s $messageAttacker %s")
    }

    private fun FabricLanguageProvider.TranslationBuilder.indirectDamageTranslaion(
        key: RegistryKey<DamageType>,
        message: String,
        messageItem: String
    ) {
        val key0 = key.value.path
        this.add("death.attack.$key0", "%s $message")
        this.add("death.attack.$key0.item", "%s $messageItem %s using %s")
    }
}