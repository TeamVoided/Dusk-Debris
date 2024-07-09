package org.teamvoided.dusk_debris.init

import net.fabricmc.fabric.api.`object`.builder.v1.entity.FabricDefaultAttributeRegistry
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
import net.minecraft.entity.SpawnGroup
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import org.teamvoided.dusk_debris.DuskDebris.id
import org.teamvoided.dusk_debris.entity.BoxAreaEffectCloud
import org.teamvoided.dusk_debris.entity.GloomEntity
import org.teamvoided.dusk_debris.entity.GunpowderBarrelEntity
import org.teamvoided.dusk_debris.entity.throwable_bomb.BlunderbombEntity
import org.teamvoided.dusk_debris.entity.throwable_bomb.BonecallerEntity
import org.teamvoided.dusk_debris.entity.throwable_bomb.FirebombEntity
import org.teamvoided.dusk_debris.entity.throwable_bomb.bonecaller.BogcallerEntity
import org.teamvoided.dusk_debris.entity.throwable_bomb.bonecaller.BonechillerEntity
import org.teamvoided.dusk_debris.entity.throwable_bomb.bonecaller.BonewitherEntity
import org.teamvoided.dusk_debris.entity.throwable_bomb.nethershroom_throwable.BlindbombEntity
import org.teamvoided.dusk_debris.entity.throwable_bomb.nethershroom_throwable.PocketpoisonEntity
import org.teamvoided.dusk_debris.entity.throwable_bomb.nethershroom_throwable.SmokebombEntity

object DuskEntities {
    //        val CRAB = register(
//        "crab", EntityType.Builder
//            .create(EntityType.EntityFactory(::CrabEntity), SpawnGroup.CREATURE)
//            .setDimensions(0.5f, 0.5f)
//            .maxTrackingRange(10)
//    )
    val BOX_AREA_EFFECT_CLOUD = register(
        "box_area_effect_cloud",
        EntityType.Builder.create(EntityType.EntityFactory(::BoxAreaEffectCloud), SpawnGroup.MISC).makeFireImmune()
    )
    val GUNPOWDER_BARREL = register(
        "gunpowder_barrel",
        EntityType.Builder.create(EntityType.EntityFactory(::GunpowderBarrelEntity), SpawnGroup.MISC)
            .setDimensions(0.98f, 0.98f)
            .setEyeHeight(0.15f)
    )
    val BLUNDERBOMB = throwableBomb("blunderbomb", ::BlunderbombEntity)
    val FIREBOMB = throwableBomb("firebomb", ::FirebombEntity)
    val BONECALLER = throwableBomb("bonecaller", ::BonecallerEntity)
    val BONECHILLER = throwableBomb("bonechiller", ::BonechillerEntity)
    val BOGCALLER = throwableBomb("bogcaller", ::BogcallerEntity)
    val BONEWITHER = throwableBomb("bonewither", ::BonewitherEntity)
//    val BONESHADER = throwableBomb("boneshader", ::BonewitherEntity)

    val POCKETPOISON = throwableBomb("pocketpoison", ::PocketpoisonEntity)
    val BLINDBOMB = throwableBomb("blindbomb", ::BlindbombEntity)
    val SMOKEBOMB = throwableBomb("smokebomb", ::SmokebombEntity)

    val GLOOM = skeleton("gloomed", ::GloomEntity)


    fun <T : Entity> throwableBomb(id: String, factory: EntityType.EntityFactory<T>): EntityType<T> {
        return register(
            id, EntityType.Builder.create(factory, SpawnGroup.MISC)
                .setDimensions(0.33f, 0.33f)
                .maxTrackingRange(4)
                .trackingTickInterval(10)
                .makeFireImmune()
        )
    }

    fun <T : Entity> skeleton(id: String, factory: EntityType.EntityFactory<T>): EntityType<T> {
        return register(
            id, EntityType.Builder.create(factory, SpawnGroup.MONSTER)
                .setDimensions(0.6f, 1.99f)
                .setEyeHeight(1.74f)
                .vehicleAttachment(-0.7f)
                .maxTrackingRange(8)
        )
    }

    fun init() {
//        FabricDefaultAttributeRegistry.register(CRAB, CrabEntity.createAttributes().build())
        FabricDefaultAttributeRegistry.register(GLOOM, GloomEntity.createAttributes().build())
    }

    fun <T : Entity> register(id: String, entityType: EntityType.Builder<T>): EntityType<T> =
        Registry.register(Registries.ENTITY_TYPE, id(id), entityType.build(id))
}