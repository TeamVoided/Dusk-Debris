package org.teamvoided.dusk_debris.data.gen.tags

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider
import net.minecraft.entity.damage.DamageType
import net.minecraft.registry.HolderLookup
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.tag.DamageTypeTags
import org.teamvoided.dusk_debris.data.DuskDamageTypeTags
import java.util.concurrent.CompletableFuture

class DamageTypeTagsProvider(o: FabricDataOutput, r: CompletableFuture<HolderLookup.Provider>) :
    FabricTagProvider<DamageType>(o, RegistryKeys.DAMAGE_TYPE, r) {
    override fun configure(arg: HolderLookup.Provider) {
        duskTags()
        vanillaTags()
//        conventionTags() these dont exist :(
    }

    fun duskTags() {
        getOrCreateTagBuilder(DuskDamageTypeTags.BYPASSES_GLOOM_RESISTANCE)
            .forceAddTag(DamageTypeTags.BYPASSES_INVULNERABILITY)
//        will probably add other things here
    }

    fun vanillaTags() {
    }

    fun conventionTags() {}
}