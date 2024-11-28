package org.teamvoided.dusk_debris.init

import net.fabricmc.fabric.api.`object`.builder.v1.entity.FabricDefaultAttributeRegistry
import net.minecraft.block.Blocks
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
import net.minecraft.entity.SpawnGroup
import net.minecraft.entity.passive.AbstractHorseEntity
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.util.math.Vec3d
import org.teamvoided.dusk_debris.DuskDebris.id
import org.teamvoided.dusk_debris.entity.*
import org.teamvoided.dusk_debris.entity.throwable_bomb.BlunderbombEntity
import org.teamvoided.dusk_debris.entity.throwable_bomb.BonecallerEntity
import org.teamvoided.dusk_debris.entity.throwable_bomb.FirebombEntity
import org.teamvoided.dusk_debris.entity.throwable_bomb.bonecaller.BogcallerEntity
import org.teamvoided.dusk_debris.entity.throwable_bomb.bonecaller.BonechillerEntity
import org.teamvoided.dusk_debris.entity.throwable_bomb.bonecaller.BonewitherEntity
import org.teamvoided.dusk_debris.entity.throwable_bomb.bonecaller.ShadecallerEntity
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
    val SHADECALLER = throwableBomb("shadecaller", ::ShadecallerEntity)

    val POCKETPOISON = throwableBomb("pocketpoison", ::PocketpoisonEntity)
    val BLINDBOMB = throwableBomb("blindbomb", ::BlindbombEntity)
    val SMOKEBOMB = throwableBomb("smokebomb", ::SmokebombEntity)

    val GLOOM = skeleton("gloomed", ::GloomEntity)

    val SKELETON_WOLF = register(
        "skeleton_wolf",
        EntityType.Builder.create(::SkeletonWolfEntity, SpawnGroup.MONSTER)
            .setDimensions(0.6F, 0.85F)
            .setEyeHeight(0.68F)
            .passengerAttachments(Vec3d(0.0, 0.81875, -0.0625))
            .maxTrackingRange(10)
    )
    val WITHER_SKELETON_WOLF = register(
        "wither_skeleton_wolf",
        EntityType.Builder.create(::WitherSkeletonWolfEntity, SpawnGroup.MONSTER)
            .makeFireImmune()
            .allowSpawningInside(Blocks.WITHER_ROSE)
            .setDimensions(0.7F, 1.02F)
            .setEyeHeight(0.82F)
            .passengerAttachments(Vec3d(0.0, 0.95875, -0.0625))
            .maxTrackingRange(10)
    )
    val WITHER_SKELETON_HORSE = register(
        "wither_skeleton_horse",
        EntityType.Builder.create(::WitherSkeletonHorseEntity, SpawnGroup.MONSTER)
            .makeFireImmune()
            .allowSpawningInside(Blocks.WITHER_ROSE)
            .setDimensions(1.6757812f, 1.92f)
            .setEyeHeight(1.824f)
            .passengerAttachments(1.5825f)
            .maxTrackingRange(10)
    )
    val TUFF_GOLEM = register(
        "tuff_golem", EntityType.Builder.create(::TuffGolemEntity, SpawnGroup.MONSTER)
            .setDimensions(0.7f, 1f)
            .passengerAttachments(1f)
            .maxTrackingRange(10)
    )

    val TWISTING_SOUL_CHARGE = register(
        "twisting_soul_charge", EntityType.Builder.create(::TwistingSoulChargeEntity, SpawnGroup.MISC)
            .setDimensions(0.5F, 0.5F)
            .setEyeHeight(0.13F)
            .maxTrackingRange(4)
            .trackingTickInterval(20)
            .makeFireImmune()
    )

    val VOLAPHYRA = register(
        "volaphyra", EntityType.Builder.create(::VolaphyraEntity, SpawnGroup.MONSTER)
            .setDimensions(1f, 1f)
            .setEyeHeight(0.33333f)
            .passengerAttachments(1f)
            .maxTrackingRange(8)
    )

    val VOLAPHYRA_CORE = register(
        "volaphyra_core", EntityType.Builder.create(::VolaphyraCoreEntity, SpawnGroup.MONSTER)
            .setDimensions(0.5f, 0.5f)
            .setEyeHeight(0.25f)
            .passengerAttachments(0.25f)
            .maxTrackingRange(8)
    )


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
        FabricDefaultAttributeRegistry.register(SKELETON_WOLF, SkeletonWolfEntity.createAttributes().build())
        FabricDefaultAttributeRegistry.register(WITHER_SKELETON_WOLF, SkeletonWolfEntity.createAttributes().build())
        FabricDefaultAttributeRegistry.register(
            WITHER_SKELETON_HORSE,
            AbstractHorseEntity.createBaseAttributes().build()
        )
        FabricDefaultAttributeRegistry.register(TUFF_GOLEM, TuffGolemEntity.createAttributes().build())
        FabricDefaultAttributeRegistry.register(VOLAPHYRA, VolaphyraEntity.createAttributes().build())
        FabricDefaultAttributeRegistry.register(VOLAPHYRA_CORE, VolaphyraCoreEntity.createAttributes().build())
    }

    fun <T : Entity> register(id: String, entityType: EntityType.Builder<T>): EntityType<T> =
        Registry.register(Registries.ENTITY_TYPE, id(id), entityType.build(id))
}