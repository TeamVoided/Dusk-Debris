package org.teamvoided.dusk_debris.data.gen.tags

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider
import net.minecraft.entity.EntityType
import net.minecraft.registry.HolderLookup
import net.minecraft.registry.tag.EntityTypeTags
import org.teamvoided.dusk_debris.init.DuskEntities
import org.teamvoided.dusk_debris.data.DuskEntityTypeTags
import java.util.concurrent.CompletableFuture

class EntityTypeTagsProvider(output: FabricDataOutput, registriesFuture: CompletableFuture<HolderLookup.Provider>) :
    FabricTagProvider.EntityTypeTagProvider(output, registriesFuture) {
    override fun configure(arg: HolderLookup.Provider) {
        duskTags()
        vanillaTags()
        conventionTags()
    }

    private fun duskTags() {
        getOrCreateTagBuilder(DuskEntityTypeTags.CRAB_ATTACKS)
            .add(EntityType.AXOLOTL)
            .add(EntityType.COD)
            .add(EntityType.SALMON)
            .add(EntityType.PUFFERFISH)
            .add(EntityType.TROPICAL_FISH)
            .add(EntityType.TADPOLE)
            .add(EntityType.DROWNED)
        getOrCreateTagBuilder(DuskEntityTypeTags.THROWABLE_BOMB)
            .add(DuskEntities.FIREBOMB)
            .add(DuskEntities.BLUNDERBOMB)
            .add(DuskEntities.SMOKEBOMB)
            .add(DuskEntities.POCKETPOISON)
            .add(DuskEntities.BLINDBOMB)
        getOrCreateTagBuilder(DuskEntityTypeTags.IS_NOT_AFFECTED_BY_NETHERSHROOM)
            .addOptionalTag(EntityTypeTags.IGNORES_POISON_AND_REGEN)
            .add(EntityType.PIGLIN)
            .add(EntityType.PIGLIN_BRUTE)
            .add(EntityType.HOGLIN)
            .add(EntityType.ZOMBIFIED_PIGLIN)
            .add(EntityType.ZOGLIN)
            .add(EntityType.ENDERMAN)
        getOrCreateTagBuilder(DuskEntityTypeTags.GUNPOWDER_BARREL_DOES_NOT_DAMAGE)
            .add(EntityType.ITEM_FRAME)
            .add(EntityType.GLOW_ITEM_FRAME)
            .add(EntityType.PAINTING)
            .add(EntityType.EXPERIENCE_ORB)
        getOrCreateTagBuilder(DuskEntityTypeTags.BLUNDERBOMB_DOES_NOT_DAMAGE)
            .addOptionalTag(DuskEntityTypeTags.GUNPOWDER_BARREL_DOES_NOT_DAMAGE)
            .add(EntityType.ITEM)
        getOrCreateTagBuilder(DuskEntityTypeTags.FIREBOMB_DOES_NOT_DAMAGE)
            .addOptionalTag(DuskEntityTypeTags.BLUNDERBOMB_DOES_NOT_DAMAGE)
    }

    private fun vanillaTags() {
        getOrCreateTagBuilder(EntityTypeTags.IMPACT_PROJECTILES)
            .addOptionalTag(DuskEntityTypeTags.THROWABLE_BOMB)
        getOrCreateTagBuilder(EntityTypeTags.REDIRECTABLE_PROJECTILE)
//        getOrCreateTagBuilder(EntityTypeTags.AQUATIC)
//            .add(DuskEntities.CRAB)
//        getOrCreateTagBuilder(EntityTypeTags.ARTHROPOD)
//            .add(DuskEntities.CRAB)
//        getOrCreateTagBuilder(EntityTypeTags.CAN_BREATHE_UNDER_WATER)
//            .add(DuskEntities.CRAB)
    }

    private fun conventionTags() {}
}