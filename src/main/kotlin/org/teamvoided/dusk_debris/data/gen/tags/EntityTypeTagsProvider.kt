package org.teamvoided.dusk_debris.data.gen.tags

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider
import net.minecraft.entity.EntityType
import net.minecraft.registry.HolderLookup
import net.minecraft.registry.tag.EntityTypeTags
import org.teamvoided.dusk_autumn.init.DuskEntities
import org.teamvoided.dusk_debris.data.DuskEntityTypeTags
import java.util.concurrent.CompletableFuture

class EntityTypeTagsProvider(output: FabricDataOutput, registriesFuture: CompletableFuture<HolderLookup.Provider>) :
    FabricTagProvider.EntityTypeTagProvider(output, registriesFuture) {
    override fun configure(arg: HolderLookup.Provider) {
//        getOrCreateTagBuilder(EntityTypeTags.AQUATIC)
//            .add(DuskEntities.CRAB)
//        getOrCreateTagBuilder(EntityTypeTags.ARTHROPOD)
//            .add(DuskEntities.CRAB)
//        getOrCreateTagBuilder(EntityTypeTags.CAN_BREATHE_UNDER_WATER)
//            .add(DuskEntities.CRAB)
//        getOrCreateTagBuilder(DuskEntityTypeTags.CRAB_ATTACKS)
//            .add(EntityType.AXOLOTL)
//            .add(EntityType.COD)
//            .add(EntityType.SALMON)
//            .add(EntityType.PUFFERFISH)
//            .add(EntityType.TROPICAL_FISH)
//            .add(EntityType.TADPOLE)
//            .add(EntityType.DROWNED)
        getOrCreateTagBuilder(EntityTypeTags.IMPACT_PROJECTILES)
            .add(DuskEntities.FIREBOMB)
            .add(DuskEntities.BLUNDERBOMB)
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
}