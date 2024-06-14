package org.teamvoided.dusk_autumn.init

import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import org.teamvoided.dusk_debris.DuskDebris.id

object DuskEntities {
    //    val CRAB = register(
//        "crab", EntityType.Builder
//            .create(EntityType.EntityFactory(::CrabEntity), SpawnGroup.CREATURE)
//            .setDimensions(0.5f, 0.5f)
//            .maxTrackingRange(10)
//    )
    fun init() {
//        FabricDefaultAttributeRegistry.register(CRAB, CrabEntity.createAttributes().build())
    }

    fun <T : Entity> register(id: String, entityType: EntityType.Builder<T>): EntityType<T> =
        Registry.register(Registries.ENTITY_TYPE, id(id), entityType.build(id))
}