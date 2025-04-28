package org.teamvoided.dusk_debris.init

import net.fabricmc.fabric.api.`object`.builder.v1.entity.FabricDefaultAttributeRegistry
import net.minecraft.block.Blocks
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
import net.minecraft.entity.SpawnGroup
import net.minecraft.entity.mob.MobEntity
import net.minecraft.entity.passive.AbstractHorseEntity
import net.minecraft.item.Item
import net.minecraft.item.SpawnEggItem
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.util.math.Vec3d
import org.teamvoided.dusk_debris.DuskDebris.id
import org.teamvoided.dusk_debris.entity.*
import org.teamvoided.dusk_debris.entity.projectile.FlyingPumpkinProjectile
import org.teamvoided.dusk_debris.entity.spell.VengefulSpiritEntity
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
    val ENTITIES = mutableSetOf<EntityType<*>>()

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
    val LIGHTNING_CLOUD = register(
        "lightning_cloud",
        EntityType.Builder.create(EntityType.EntityFactory(::LightningCloudEntity), SpawnGroup.MISC).makeFireImmune()
    )
    val LAZER_ENTITY = register(
        "lazer_entity",
        EntityType.Builder.create(EntityType.EntityFactory(::LazerEntity), SpawnGroup.MISC).makeFireImmune()
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

    val GLOOM = skeleton("gloomed", 0x222222, 0x222222, ::GloomEntity)

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
        "volaphyra", 0xFEFCFF, 0x37CE56, EntityType.Builder.create(::VolaphyraEntity, SpawnGroup.MONSTER)
            .setDimensions(1f, 1f)
            .setEyeHeight(0.33333f)
            .passengerAttachments(1f)
            .maxTrackingRange(8)
    )

    val VOLAPHYRA_CORE = register(
        "volaphyra_core", 0x37CE56, 0x37CE56, EntityType.Builder.create(::VolaphyraCoreEntity, SpawnGroup.MONSTER)
            .setDimensions(0.5f, 0.5f)
            .setEyeHeight(0.25f)
            .passengerAttachments(0.25f)
            .maxTrackingRange(8)
    )
    val TINY_ENEMY_JELLYFISH = register(
        "tiny_enemy_jellyfish", 0xECEAED, 0x9AF1B2, EntityType.Builder.create(::TinyEnemyJellyfishEntity, SpawnGroup.AMBIENT)
            .setDimensions(0.5f, 0.5f)
            .setEyeHeight(0.25f)
            .passengerAttachments(0.5f)
            .maxTrackingRange(8)
    )
    val VENGEFUL_SPIRIT = register(
        "vengeful_spirit", EntityType.Builder.create(::VengefulSpiritEntity, SpawnGroup.MISC)
            .setDimensions(1f, 1f)
            .setEyeHeight(0.5f)
            .passengerAttachments(1f)
            .maxTrackingRange(4)
            .trackingTickInterval(20)
            .makeFireImmune()
    )

    /// DnD Entities
    val CHILL_CHARGE = register(
        "chill_charge",
        EntityType.Builder.create(EntityType.EntityFactory(::ChillChargeEntity), SpawnGroup.MISC)
            .setDimensions(0.3125F, 0.3125F)
            .setEyeHeight(0F)
            .maxTrackingRange(4)
            .trackingTickInterval(10)
    )

//    val BIRD_TEST = register(
//        "bird",
//        EntityType.Builder.create(EntityType.EntityFactory(::BirdEntity), SpawnGroup.CREATURE)
//            .setDimensions(0.3125F, 0.625F)
//            .setEyeHeight(0.55F)
//            .maxTrackingRange(4)
//            .trackingTickInterval(10)
//    )

    val DIE = register(
        "die",
        EntityType.Builder.create(EntityType.EntityFactory(::DiceEntity), SpawnGroup.MISC)
            .setDimensions(0.5F, 0.5F)
            .setEyeHeight(0F)
            .maxTrackingRange(4)
            .trackingTickInterval(10)
    )
    val FLYING_PUMPKIN = register(
        "flying_pumpkin",
        EntityType.Builder.create(EntityType.EntityFactory(::FlyingPumpkinProjectile), SpawnGroup.MISC)
            .setDimensions(0.5F, 0.5F)
            .setEyeHeight(0.25F)
            .maxTrackingRange(4)
            .trackingTickInterval(10)
    )
    val DUST_BUNNY = register(
        "dust_bunny",
        EntityType.Builder.create(EntityType.EntityFactory(::DustBunnyEntity), SpawnGroup.MONSTER)
            .setDimensions(0.8f, 0.8f)
            .setEyeHeight(0.4f)
            .passengerAttachments(0.7375f)
            .vehicleAttachment(0.04f)
            .maxTrackingRange(8)
            .makeFireImmune()
    )
    val PIFFLING_PUMPKIN = register(
        "piffling_pumpkin",
        EntityType.Builder.create(EntityType.EntityFactory(::PifflingPumpkinEntity), SpawnGroup.MONSTER)
            .setDimensions(0.5f, 0.9f)
            .setEyeHeight(0.6f)
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

    fun <T : MobEntity> skeleton(
        id: String,
        priCol: Int,
        secCol: Int,
        factory: EntityType.EntityFactory<T>
    ): EntityType<T> {
        return register(
            id, priCol, secCol, EntityType.Builder.create(factory, SpawnGroup.MONSTER)
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
        FabricDefaultAttributeRegistry.register(
            TINY_ENEMY_JELLYFISH,
            TinyEnemyJellyfishEntity.createAttributes().build()
        )
        // DnD Entities
        //        FabricDefaultAttributeRegistry.register(BIRD_TEST, BirdEntity.createAttributes().build())
        FabricDefaultAttributeRegistry.register(DUST_BUNNY, DustBunnyEntity.createAttributes().build())
        FabricDefaultAttributeRegistry.register(PIFFLING_PUMPKIN, PifflingPumpkinEntity.createAttributes().build())
    }

    fun <T : MobEntity> register(
        id: String,
        priCol: Int,
        secCol: Int,
        entityType: EntityType.Builder<T>
    ): EntityType<T> {
        val regEntityType = register(id, entityType)
        DuskItems.register(id + "_spawn_egg", (SpawnEggItem(regEntityType, priCol, secCol, Item.Settings())))
        return regEntityType
    }

    fun <T : Entity> register(id: String, entityType: EntityType.Builder<T>): EntityType<T> {
        val regEntityType = Registry.register(Registries.ENTITY_TYPE, id(id), entityType.build(id))
        ENTITIES.add(regEntityType)
        return regEntityType
    }
}