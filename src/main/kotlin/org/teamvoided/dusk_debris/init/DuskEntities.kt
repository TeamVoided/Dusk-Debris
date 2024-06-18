package org.teamvoided.dusk_autumn.init

import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
import net.minecraft.entity.SpawnGroup
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import org.teamvoided.dusk_debris.DuskDebris.id
import org.teamvoided.dusk_debris.entity.BlunderbombEntity
import org.teamvoided.dusk_debris.entity.FirebombEntity
import org.teamvoided.dusk_debris.entity.GunpowderBarrelEntity

object DuskEntities {
    //        val CRAB = register(
//        "crab", EntityType.Builder
//            .create(EntityType.EntityFactory(::CrabEntity), SpawnGroup.CREATURE)
//            .setDimensions(0.5f, 0.5f)
//            .maxTrackingRange(10)
//    )
    val GUNPOWDER_BARREL = register(
        "gunpowder_barrel",
        EntityType.Builder.create(EntityType.EntityFactory(::GunpowderBarrelEntity), SpawnGroup.MISC)
            .setDimensions(0.98f, 0.98f)
            .setEyeHeight(0.15f)
//            .maxTrackingRange(4)
//            .trackingTickInterval(10)
    )
    val BLUNDERBOMB = register(
        "blunderbomb",
        EntityType.Builder.create(EntityType.EntityFactory(::BlunderbombEntity), SpawnGroup.MISC)
            .setDimensions(0.33f, 0.33f)
            .maxTrackingRange(4)
            .trackingTickInterval(10)
    )
    val FIREBOMB = register(
        "firebomb",
        EntityType.Builder.create(EntityType.EntityFactory(::FirebombEntity), SpawnGroup.MISC)
            .setDimensions(0.33f, 0.33f)
            .maxTrackingRange(4)
            .trackingTickInterval(10)
    )

    fun init() {
//        FabricDefaultAttributeRegistry.register(CRAB, CrabEntity.createAttributes().build())
    }

    fun <T : Entity> register(id: String, entityType: EntityType.Builder<T>): EntityType<T> =
        Registry.register(Registries.ENTITY_TYPE, id(id), entityType.build(id))
}