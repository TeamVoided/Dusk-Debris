package org.teamvoided.dusk_debris.init

import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry
import net.minecraft.client.renderer.entity.NoopRenderer
import net.minecraft.client.renderer.entity.ThrownItemRenderer
import org.teamvoided.dusk_debris.entity.DuskEntityLists
import org.teamvoided.dusk_debris.entity.chill_charge.ChillChargeEntityRenderer
import org.teamvoided.dusk_debris.entity.dice.DiceEntityRenderer
import org.teamvoided.dusk_debris.entity.dust_bunny.DustBunnyEntityRenderer
import org.teamvoided.dusk_debris.entity.flying_pumpkin.FlyingBlockItemEntityRenderer
import org.teamvoided.dusk_debris.entity.gunpowder_barrel.GunpowderBarrelEntityRenderer
import org.teamvoided.dusk_debris.entity.jellyfish.tiny.TinyEnemyJellyfishEntityRenderer
import org.teamvoided.dusk_debris.entity.jellyfish.volaphyra.VolaphyraCoreEntityRenderer
import org.teamvoided.dusk_debris.entity.jellyfish.volaphyra.VolaphyraEntityRenderer
import org.teamvoided.dusk_debris.entity.lazer.LazerEntityRenderer
import org.teamvoided.dusk_debris.entity.magic.vengeful_spirit.VengefulSpiritRenderer
import org.teamvoided.dusk_debris.entity.piffling.PifflingPumpkinEntityRenderer
import org.teamvoided.dusk_debris.entity.skeleton.gloom.GloomEntityRenderer
import org.teamvoided.dusk_debris.entity.skeleton.horse.WitherSkeletonHorseEntityRenderer
import org.teamvoided.dusk_debris.entity.skeleton.wolf.SkeletonWolfEntityRenderer
import org.teamvoided.dusk_debris.entity.skeleton.wolf.WitherSkeletonWolfEntityRenderer
import org.teamvoided.dusk_debris.entity.tuff_golem.TuffGolemEntityRenderer

object DuskEntitiesClient {
    fun init() {
        EntityRendererRegistry.register(DuskEntities.BOX_AREA_EFFECT_CLOUD, ::NoopRenderer)
        EntityRendererRegistry.register(DuskEntities.LIGHTNING_CLOUD, ::NoopRenderer)
        EntityRendererRegistry.register(DuskEntities.LAZER_ENTITY, ::LazerEntityRenderer)
        EntityRendererRegistry.register(DuskEntities.GUNPOWDER_BARREL, ::GunpowderBarrelEntityRenderer)
        EntityRendererRegistry.register(DuskEntities.GLOOM, ::GloomEntityRenderer)
        EntityRendererRegistry.register(DuskEntities.SKELETON_WOLF, ::SkeletonWolfEntityRenderer)
        EntityRendererRegistry.register(DuskEntities.WITHER_SKELETON_WOLF, ::WitherSkeletonWolfEntityRenderer)
        EntityRendererRegistry.register(DuskEntities.WITHER_SKELETON_HORSE, ::WitherSkeletonHorseEntityRenderer)
        EntityRendererRegistry.register(DuskEntities.TUFF_GOLEM, ::TuffGolemEntityRenderer)
        EntityRendererRegistry.register(DuskEntities.TWISTING_SOUL_CHARGE, ::NoopRenderer)
        EntityRendererRegistry.register(DuskEntities.VOLAPHYRA, ::VolaphyraEntityRenderer)
        EntityRendererRegistry.register(DuskEntities.VOLAPHYRA_CORE, ::VolaphyraCoreEntityRenderer)
        EntityRendererRegistry.register(DuskEntities.TINY_ENEMY_JELLYFISH, ::TinyEnemyJellyfishEntityRenderer)

        EntityRendererRegistry.register(DuskEntities.VENGEFUL_SPIRIT, ::VengefulSpiritRenderer)

        DuskEntityLists.THROWABLE_BOMB_ENTITIES.forEach {
            EntityRendererRegistry.register(it, ::ThrownItemRenderer)
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