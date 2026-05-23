package org.teamvoided.dusk_debris.init

import net.fabricmc.fabric.api.`object`.builder.v1.entity.FabricDefaultAttributeRegistry
import net.minecraft.core.Registry
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.Mob
import net.minecraft.world.entity.MobCategory
import net.minecraft.world.entity.animal.horse.AbstractHorse
import net.minecraft.world.item.Item
import net.minecraft.world.item.SpawnEggItem
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.phys.Vec3
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
        EntityType.Builder.of(EntityType.EntityFactory(::BoxAreaEffectCloud), MobCategory.MISC).fireImmune()
    )
    val LIGHTNING_CLOUD = register(
        "lightning_cloud",
        EntityType.Builder.of(EntityType.EntityFactory(::LightningCloudEntity), MobCategory.MISC).fireImmune()
    )
    val LAZER_ENTITY = register(
        "lazer_entity",
        EntityType.Builder.of(EntityType.EntityFactory(::LazerEntity), MobCategory.MISC).fireImmune()
    )
    val GUNPOWDER_BARREL = register(
        "gunpowder_barrel",
        EntityType.Builder.of(EntityType.EntityFactory(::GunpowderBarrelEntity), MobCategory.MISC)
            .sized(0.98f, 0.98f)
            .eyeHeight(0.15f)
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
        EntityType.Builder.of(::SkeletonWolfEntity, MobCategory.MONSTER)
            .sized(0.6F, 0.85F)
            .eyeHeight(0.68F)
            .passengerAttachments(Vec3(0.0, 0.81875, -0.0625))
            .clientTrackingRange(10)
    )
    val WITHER_SKELETON_WOLF = register(
        "wither_skeleton_wolf",
        EntityType.Builder.of(::WitherSkeletonWolfEntity, MobCategory.MONSTER)
            .fireImmune()
            .immuneTo(Blocks.WITHER_ROSE)
            .sized(0.7F, 1.02F)
            .eyeHeight(0.82F)
            .passengerAttachments(Vec3(0.0, 0.95875, -0.0625))
            .clientTrackingRange(10)
    )
    val WITHER_SKELETON_HORSE = register(
        "wither_skeleton_horse",
        EntityType.Builder.of(::WitherSkeletonHorseEntity, MobCategory.MONSTER)
            .fireImmune()
            .immuneTo(Blocks.WITHER_ROSE)
            .sized(1.6757812f, 1.92f)
            .eyeHeight(1.824f)
            .passengerAttachments(1.5825f)
            .clientTrackingRange(10)
    )
    val TUFF_GOLEM = register(
        "tuff_golem", EntityType.Builder.of(::TuffGolemEntity, MobCategory.MONSTER)
            .sized(0.7f, 1f)
            .passengerAttachments(1f)
            .clientTrackingRange(10)
    )

    val TWISTING_SOUL_CHARGE = register(
        "twisting_soul_charge", EntityType.Builder.of(::TwistingSoulChargeEntity, MobCategory.MISC)
            .sized(0.5F, 0.5F)
            .eyeHeight(0.13F)
            .clientTrackingRange(4)
            .updateInterval(20)
            .fireImmune()
    )

    val VOLAPHYRA = register(
        "volaphyra", 0xFEFCFF, 0x37CE56, EntityType.Builder.of(::VolaphyraEntity, MobCategory.MONSTER)
            .sized(1f, 1f)
            .eyeHeight(0.33333f)
            .passengerAttachments(1f)
            .clientTrackingRange(8)
    )

    val VOLAPHYRA_CORE = register(
        "volaphyra_core", 0x37CE56, 0x37CE56, EntityType.Builder.of(::VolaphyraCoreEntity, MobCategory.MONSTER)
            .sized(0.5f, 0.5f)
            .eyeHeight(0.25f)
            .passengerAttachments(0.25f)
            .clientTrackingRange(8)
    )
    val TINY_ENEMY_JELLYFISH = register(
        "tiny_enemy_jellyfish", 0xECEAED, 0x9AF1B2, EntityType.Builder.of(::TinyEnemyJellyfishEntity, MobCategory.AMBIENT)
            .sized(0.5f, 0.5f)
            .eyeHeight(0.25f)
            .passengerAttachments(0.5f)
            .clientTrackingRange(8)
    )
    val VENGEFUL_SPIRIT = register(
        "vengeful_spirit", EntityType.Builder.of(::VengefulSpiritEntity, MobCategory.MISC)
            .sized(1f, 1f)
            .eyeHeight(0.5f)
            .passengerAttachments(1f)
            .clientTrackingRange(4)
            .updateInterval(20)
            .fireImmune()
    )

    /// DnD Entities
    val CHILL_CHARGE = register(
        "chill_charge",
        EntityType.Builder.of(EntityType.EntityFactory(::ChillChargeEntity), MobCategory.MISC)
            .sized(0.3125F, 0.3125F)
            .eyeHeight(0F)
            .clientTrackingRange(4)
            .updateInterval(10)
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
        EntityType.Builder.of(EntityType.EntityFactory(::DiceEntity), MobCategory.MISC)
            .sized(0.5F, 0.5F)
            .eyeHeight(0F)
            .clientTrackingRange(4)
            .updateInterval(10)
    )
    val FLYING_PUMPKIN = register(
        "flying_pumpkin",
        EntityType.Builder.of(EntityType.EntityFactory(::FlyingPumpkinProjectile), MobCategory.MISC)
            .sized(0.5F, 0.5F)
            .eyeHeight(0.25F)
            .clientTrackingRange(4)
            .updateInterval(10)
    )
    val DUST_BUNNY = register(
        "dust_bunny",
        EntityType.Builder.of(EntityType.EntityFactory(::DustBunnyEntity), MobCategory.MONSTER)
            .sized(0.8f, 0.8f)
            .eyeHeight(0.4f)
            .passengerAttachments(0.7375f)
            .ridingOffset(0.04f)
            .clientTrackingRange(8)
            .fireImmune()
    )
    val PIFFLING_PUMPKIN = register(
        "piffling_pumpkin",
        EntityType.Builder.of(EntityType.EntityFactory(::PifflingPumpkinEntity), MobCategory.MONSTER)
            .sized(0.5f, 0.9f)
            .eyeHeight(0.6f)
            .clientTrackingRange(8)
    )


    fun <T : Entity> throwableBomb(id: String, factory: EntityType.EntityFactory<T>): EntityType<T> {
        return register(
            id, EntityType.Builder.of(factory, MobCategory.MISC)
                .sized(0.33f, 0.33f)
                .clientTrackingRange(4)
                .updateInterval(10)
                .fireImmune()
        )
    }

    fun <T : Mob> skeleton(
        id: String,
        priCol: Int,
        secCol: Int,
        factory: EntityType.EntityFactory<T>
    ): EntityType<T> {
        return register(
            id, priCol, secCol, EntityType.Builder.of(factory, MobCategory.MONSTER)
                .sized(0.6f, 1.99f)
                .eyeHeight(1.74f)
                .ridingOffset(-0.7f)
                .clientTrackingRange(8)
        )
    }

    fun init() {
//        FabricDefaultAttributeRegistry.register(CRAB, CrabEntity.createAttributes().build())
        FabricDefaultAttributeRegistry.register(GLOOM, GloomEntity.createAttributes().build())
        FabricDefaultAttributeRegistry.register(SKELETON_WOLF, SkeletonWolfEntity.createAttributes().build())
        FabricDefaultAttributeRegistry.register(WITHER_SKELETON_WOLF, SkeletonWolfEntity.createAttributes().build())
        FabricDefaultAttributeRegistry.register(
            WITHER_SKELETON_HORSE,
            AbstractHorse.createBaseHorseAttributes().build()
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

    fun <T : Mob> register(
        id: String,
        priCol: Int,
        secCol: Int,
        entityType: EntityType.Builder<T>
    ): EntityType<T> {
        val regEntityType = register(id, entityType)
        DuskItems.register(id + "_spawn_egg", (SpawnEggItem(regEntityType, priCol, secCol, Item.Properties())))
        return regEntityType
    }

    fun <T : Entity> register(id: String, entityType: EntityType.Builder<T>): EntityType<T> {
        val regEntityType = Registry.register(BuiltInRegistries.ENTITY_TYPE, id(id), entityType.build(id))
        ENTITIES.add(regEntityType)
        return regEntityType
    }
}