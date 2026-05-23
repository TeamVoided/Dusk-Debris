package org.teamvoided.dusk_debris.data.gen.providers.english_translation

import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider
import net.minecraft.resources.ResourceKey
import net.minecraft.world.damagesource.DamageType
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
        key: ResourceKey<DamageType>,
        message: String,
        transition: String = "by",
        tryingToEscape: String = "whilst trying to escape",
        using: String = "using"
    ) {
        val key0 = key.location().path
        this.add("death.attack.$key0", "%s $message")
        this.add("death.attack.$key0.item", "%s $message $transition %s $using %s")
        this.add("death.attack.$key0.player", "%s $message $tryingToEscape %s")
    }

    private fun FabricLanguageProvider.TranslationBuilder.damageTranslaion(
        key: ResourceKey<DamageType>,
        message: String,
        messageItem: String,
        messageAttacker: String = messageItem
    ) {
        val key0 = key.location().path
        this.add("death.attack.$key0", "%s $message")
        this.add("death.attack.$key0.item", "%s $messageItem %s using %s")
        this.add("death.attack.$key0.player", "%s $messageAttacker %s")
    }


    private fun FabricLanguageProvider.TranslationBuilder.damageTranslaion(
        direct: ResourceKey<DamageType>,
        indirect: ResourceKey<DamageType>,
        message: String,
        messageItem: String,
        messageAttacker: String = messageItem
    ) {
        this.directDamageTranslaion(direct, message, messageAttacker)
        this.indirectDamageTranslaion(indirect, message, messageItem)
    }


    private fun FabricLanguageProvider.TranslationBuilder.directDamageTranslaion(
        key: ResourceKey<DamageType>,
        message: String,
        messageAttacker: String
    ) {
        val key0 = key.location().path
        this.add("death.attack.$key0", "%s $message")
        this.add("death.attack.$key0.player", "%s $messageAttacker %s")
    }

    private fun FabricLanguageProvider.TranslationBuilder.indirectDamageTranslaion(
        key: ResourceKey<DamageType>,
        message: String,
        messageItem: String
    ) {
        val key0 = key.location().path
        this.add("death.attack.$key0", "%s $message")
        this.add("death.attack.$key0.item", "%s $messageItem %s using %s")
    }
}