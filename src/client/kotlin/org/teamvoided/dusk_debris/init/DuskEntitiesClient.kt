package org.teamvoided.dusk_debris.init

import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry
import net.minecraft.client.render.entity.EmptyEntityRenderer
import net.minecraft.client.render.entity.FlyingItemEntityRenderer
import org.teamvoided.dusk_debris.entity.DuskEntityLists
import org.teamvoided.dusk_debris.entity.gunpowder_barrel.GunpowderBarrelEntityRenderer
import org.teamvoided.dusk_debris.entity.skeleton.GloomEntityRenderer
import org.teamvoided.dusk_debris.entity.skeleton.SkeletonWolfEntityRenderer
import org.teamvoided.dusk_debris.entity.skeleton.WitherSkeletonHorseEntityRenderer
import org.teamvoided.dusk_debris.entity.skeleton.WitherSkeletonWolfEntityRenderer
import org.teamvoided.dusk_debris.entity.tuff_golem.TuffGolemEntityRenderer
import org.teamvoided.dusk_debris.entity.volaphyra.VolaphyraCoreEntityRenderer
import org.teamvoided.dusk_debris.entity.volaphyra.VolaphyraEntityRenderer

object DuskEntitiesClient {
    fun init() {
        EntityRendererRegistry.register(DuskEntities.BOX_AREA_EFFECT_CLOUD, ::EmptyEntityRenderer)
        EntityRendererRegistry.register(DuskEntities.GUNPOWDER_BARREL, ::GunpowderBarrelEntityRenderer)
        EntityRendererRegistry.register(DuskEntities.GLOOM, ::GloomEntityRenderer)
        EntityRendererRegistry.register(DuskEntities.SKELETON_WOLF, ::SkeletonWolfEntityRenderer)
        EntityRendererRegistry.register(DuskEntities.WITHER_SKELETON_WOLF, ::WitherSkeletonWolfEntityRenderer)
        EntityRendererRegistry.register(DuskEntities.WITHER_SKELETON_HORSE, ::WitherSkeletonHorseEntityRenderer)
        EntityRendererRegistry.register(DuskEntities.TUFF_GOLEM, ::TuffGolemEntityRenderer)
        EntityRendererRegistry.register(DuskEntities.TWISTING_SOUL_CHARGE, ::EmptyEntityRenderer)
        EntityRendererRegistry.register(DuskEntities.VOLAPHYRA, ::VolaphyraEntityRenderer)
        EntityRendererRegistry.register(DuskEntities.VOLAPHYRA_CORE, ::VolaphyraCoreEntityRenderer)

        DuskEntityLists.THROWABLE_BOMB_ENTITIES.forEach {
            EntityRendererRegistry.register(it, ::FlyingItemEntityRenderer)
        }
    }
}