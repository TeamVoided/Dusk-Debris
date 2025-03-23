package org.teamvoided.dusk_debris.init

import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry
import net.minecraft.client.render.entity.EmptyEntityRenderer
import net.minecraft.client.render.entity.FlyingItemEntityRenderer
import org.teamvoided.dusk_debris.entity.DuskEntityLists
import org.teamvoided.dusk_debris.entity.gunpowder_barrel.GunpowderBarrelEntityRenderer
import org.teamvoided.dusk_debris.entity.jellyfish.tiny.TinyEnemyJellyfishEntityRenderer
import org.teamvoided.dusk_debris.entity.skeleton.GloomEntityRenderer
import org.teamvoided.dusk_debris.entity.skeleton.SkeletonWolfEntityRenderer
import org.teamvoided.dusk_debris.entity.skeleton.WitherSkeletonHorseEntityRenderer
import org.teamvoided.dusk_debris.entity.skeleton.WitherSkeletonWolfEntityRenderer
import org.teamvoided.dusk_debris.entity.tuff_golem.TuffGolemEntityRenderer
import org.teamvoided.dusk_debris.entity.jellyfish.volaphyra.VolaphyraCoreEntityRenderer
import org.teamvoided.dusk_debris.entity.jellyfish.volaphyra.VolaphyraEntityRenderer
import org.teamvoided.dusk_debris.entity.lazer.LazerEntityRenderer
import org.teamvoided.dusk_debris.entity.chill_charge.ChillChargeEntityRenderer
import org.teamvoided.dusk_debris.entity.dice.DiceEntityRenderer
import org.teamvoided.dusk_debris.entity.dust_bunny.DustBunnyEntityRenderer
import org.teamvoided.dusk_debris.entity.flying_pumpkin.FlyingBlockItemEntityRenderer
import org.teamvoided.dusk_debris.entity.piffling.PifflingPumpkinEntityRenderer
i

object DuskEntitiesClient {
    fun init() {
        EntityRendererRegistry.register(DuskEntities.BOX_AREA_EFFECT_CLOUD, ::EmptyEntityRenderer)
        EntityRendererRegistry.register(DuskEntities.LIGHTNING_CLOUD, ::EmptyEntityRenderer)
        EntityRendererRegistry.register(DuskEntities.LAZER_ENTITY, ::LazerEntityRenderer)
        EntityRendererRegistry.register(DuskEntities.GUNPOWDER_BARREL, ::GunpowderBarrelEntityRenderer)
        EntityRendererRegistry.register(DuskEntities.GLOOM, ::GloomEntityRenderer)
        EntityRendererRegistry.register(DuskEntities.SKELETON_WOLF, ::SkeletonWolfEntityRenderer)
        EntityRendererRegistry.register(DuskEntities.WITHER_SKELETON_WOLF, ::WitherSkeletonWolfEntityRenderer)
        EntityRendererRegistry.register(DuskEntities.WITHER_SKELETON_HORSE, ::WitherSkeletonHorseEntityRenderer)
        EntityRendererRegistry.register(DuskEntities.TUFF_GOLEM, ::TuffGolemEntityRenderer)
        EntityRendererRegistry.register(DuskEntities.TWISTING_SOUL_CHARGE, ::EmptyEntityRenderer)
        EntityRendererRegistry.register(DuskEntities.VOLAPHYRA, ::VolaphyraEntityRenderer)
        EntityRendererRegistry.register(DuskEntities.VOLAPHYRA_CORE, ::VolaphyraCoreEntityRenderer)
        EntityRendererRegistry.register(DuskEntities.TINY_ENEMY_JELLYFISH, ::TinyEnemyJellyfishEntityRenderer)


        DuskEntityLists.THROWABLE_BOMB_ENTITIES.forEach {
            EntityRendererRegistry.register(it, ::FlyingItemEntityRenderer)
        }
        // DnD
        EntityRendererRegistry.register(DuskEntities.CHILL_CHARGE, ::ChillChargeEntityRenderer)
        EntityRendererRegistry.register(DuskEntities.DIE, ::DiceEntityRenderer)
//        EntityRendererRegistry.register(DuskEntities.BIRD_TEST, ::BirdEntityRenderer)
        EntityRendererRegistry.register(DuskEntities.FLYING_PUMPKIN, ::FlyingBlockItemEntityRenderer)
        EntityRendererRegistry.register(DuskEntities.DUST_BUNNY, ::DustBunnyEntityRenderer)
        EntityRendererRegistry.register(DuskEntities.PIFFLING_PUMPKIN, ::PifflingPumpkinEntityRenderer)
    }
}