package org.teamvoided.dusk_debris.data.gen.tags

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalEntityTypeTags
import net.minecraft.entity.EntityType
import net.minecraft.registry.HolderLookup
import net.minecraft.registry.tag.EntityTypeTags
import org.teamvoided.dusk_debris.data.tags.DuskEntityTypeTags
import org.teamvoided.dusk_debris.entity.DuskEntityLists.DUSK_SKELETON_ENTITIES
import org.teamvoided.dusk_debris.entity.DuskEntityLists.THROWABLE_BOMB_ENTITIES
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
            .add(*THROWABLE_BOMB_ENTITIES.toTypedArray())
        getOrCreateTagBuilder(DuskEntityTypeTags.DUSK_SKELETON_ATTACKS)
            .add(EntityType.IRON_GOLEM)
            .add(EntityType.SNOW_GOLEM)
            .add(EntityType.DROWNED)
            .add(EntityType.PIGLIN)
            .add(EntityType.PIGLIN_BRUTE)
        getOrCreateTagBuilder(DuskEntityTypeTags.DUSK_SKELETON_RETREATS)
            .add(EntityType.WOLF)
        getOrCreateTagBuilder(DuskEntityTypeTags.IS_NOT_AFFECTED_BY_NETHERSHROOM)
            .forceAddTag(EntityTypeTags.IGNORES_POISON_AND_REGEN)
            .add(EntityType.PIGLIN)
            .add(EntityType.PIGLIN_BRUTE)
            .add(EntityType.HOGLIN)
            .add(EntityType.ZOMBIFIED_PIGLIN)
            .add(EntityType.ZOGLIN)
            .add(EntityType.ENDERMAN)
        getOrCreateTagBuilder(DuskEntityTypeTags.FANS_DONT_AFFECT)
            .forceAddTag(ConventionalEntityTypeTags.BOSSES)
            .add(EntityType.WIND_CHARGE)
            .add(EntityType.BREEZE_WIND_CHARGE)
            .add(EntityType.BREEZE)
        getOrCreateTagBuilder(DuskEntityTypeTags.GEYSERS_DONT_PROPEL)
            .forceAddTag(ConventionalEntityTypeTags.BOSSES)
        getOrCreateTagBuilder(DuskEntityTypeTags.GUNPOWDER_BARREL_DOES_NOT_DAMAGE)
            .add(EntityType.ITEM_FRAME)
            .add(EntityType.GLOW_ITEM_FRAME)
            .add(EntityType.PAINTING)
            .add(EntityType.EXPERIENCE_ORB)
        getOrCreateTagBuilder(DuskEntityTypeTags.BLUNDERBOMB_DOES_NOT_DAMAGE)
            .forceAddTag(DuskEntityTypeTags.GUNPOWDER_BARREL_DOES_NOT_DAMAGE)
            .add(EntityType.ITEM)
        getOrCreateTagBuilder(DuskEntityTypeTags.FIREBOMB_DOES_NOT_DAMAGE)
            .forceAddTag(DuskEntityTypeTags.BLUNDERBOMB_DOES_NOT_DAMAGE)
    }

    private fun vanillaTags() {
        getOrCreateTagBuilder(EntityTypeTags.SKELETONS)
            .add(*DUSK_SKELETON_ENTITIES.toTypedArray())
        getOrCreateTagBuilder(EntityTypeTags.NO_ANGER_FROM_WIND_CHARGE)
            .add(*DUSK_SKELETON_ENTITIES.toTypedArray())
//        getOrCreateTagBuilder(EntityTypeTags.DEFLECTS_PROJECTILES)
//           .add(DuskEntities.GREED)
        getOrCreateTagBuilder(EntityTypeTags.IMPACT_PROJECTILES)
            .forceAddTag(DuskEntityTypeTags.THROWABLE_BOMB)
//        getOrCreateTagBuilder(EntityTypeTags.AQUATIC)
//            .add(DuskEntities.CRAB)
//        getOrCreateTagBuilder(EntityTypeTags.ARTHROPOD)
//            .add(DuskEntities.CRAB)
//        getOrCreateTagBuilder(EntityTypeTags.CAN_BREATHE_UNDER_WATER)
//            .add(DuskEntities.CRAB)
    }

    private fun conventionTags() {}
}