package org.teamvoided.dusk_debris.init

import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
import net.minecraft.entity.SpawnGroup
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import org.teamvoided.dusk_debris.DuskDebris.id
import org.teamvoided.dusk_debris.entity.GunpowderBarrelEntity
import org.teamvoided.dusk_debris.entity.BoxAreaEffectCloud
import org.teamvoided.dusk_debris.entity.throwable_bomb.BlunderbombEntity
import org.teamvoided.dusk_debris.entity.throwable_bomb.BonecallerEntity
import org.teamvoided.dusk_debris.entity.throwable_bomb.FirebombEntity
import org.teamvoided.dusk_debris.entity.throwable_bomb.NethershroomThrowableEntity

object DuskEntities {
    //        val CRAB = register(
//        "crab", EntityType.Builder
//            .create(EntityType.EntityFactory(::CrabEntity), SpawnGroup.CREATURE)
//            .setDimensions(0.5f, 0.5f)
//            .maxTrackingRange(10)
//    )
    val BOX_AREA_EFFECT_CLOUD = register(
        "box_area_effect_cloud",
        EntityType.Builder.create(EntityType.EntityFactory(::BoxAreaEffectCloud), SpawnGroup.MISC)
    )
    val GUNPOWDER_BARREL = register(
        "gunpowder_barrel",
        EntityType.Builder.create(EntityType.EntityFactory(::GunpowderBarrelEntity), SpawnGroup.MISC)
            .setDimensions(0.98f, 0.98f)
            .setEyeHeight(0.15f)
//            .maxTrackingRange(4)
//            .trackingTickInterval(10)
    )
    val BLUNDERBOMB = throwableBomb("blunderbomb", ::BlunderbombEntity)
    val FIREBOMB = throwableBomb("firebomb", ::FirebombEntity)
    val BONECALLER = throwableBomb("bonecaller", ::BonecallerEntity)
    val POCKETPOISON = throwableBomb("pocketpoison", ::NethershroomThrowableEntity)
    val BLINDBOMB = throwableBomb("blindbomb", ::NethershroomThrowableEntity)
    val SMOKEBOMB = throwableBomb("smokebomb", ::NethershroomThrowableEntity)

    fun <T : Entity> throwableBomb(id: String, factory: EntityType.EntityFactory<T>): EntityType<T> {
        return register(
            id,
            EntityType.Builder.create(factory, SpawnGroup.MISC)
                .setDimensions(0.33f, 0.33f)
                .maxTrackingRange(4)
                .trackingTickInterval(10)
        )
    }

    fun init() {
//        FabricDefaultAttributeRegistry.register(CRAB, CrabEntity.createAttributes().build())
    }

    fun <T : Entity> register(id: String, entityType: EntityType.Builder<T>): EntityType<T> =
        Registry.register(Registries.ENTITY_TYPE, id(id), entityType.build(id))
}