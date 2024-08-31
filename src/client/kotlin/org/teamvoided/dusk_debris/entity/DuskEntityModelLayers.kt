package org.teamvoided.dusk_debris.entity

import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry
import net.minecraft.client.model.Dilation
import net.minecraft.client.model.TexturedModelData
import net.minecraft.client.render.entity.model.BipedArmorEntityModel
import net.minecraft.client.render.entity.model.BipedEntityModel
import net.minecraft.client.render.entity.model.EntityModelLayer
import net.minecraft.client.render.entity.model.HorseEntityModel
import org.teamvoided.dusk_debris.DuskDebris.id
import org.teamvoided.dusk_debris.entity.skeleton.render.GloomEntityModel
import org.teamvoided.dusk_debris.entity.skeleton.render.SkeletonWolfEntityModel.Companion.texturedModelData
import org.teamvoided.dusk_debris.entity.tuff_golem.render.TuffGolemEntityModel

object DuskEntityModelLayers {
    val GLOOM: EntityModelLayer = registerMain("gloomed")
    val GLOOM_EYES: EntityModelLayer = registerMain("gloomed_eyes")
    val GLOOM_OUTER: EntityModelLayer = register("gloomed", "outer")
    val GLOOM_INNER_ARMOR: EntityModelLayer = createInnerArmor("gloomed")
    val GLOOM_OUTER_ARMOR: EntityModelLayer = createOuterArmor("gloomed")
    val SKELETON_WOLF: EntityModelLayer = registerMain("skeleton_wolf")
    val WITHER_SKELETON_WOLF: EntityModelLayer = registerMain("wither_skeleton_wolf")
    val WITHER_SKELETON_HORSE: EntityModelLayer = registerMain("wither_skeleton_horse")

    val TUFF_GOLEM: EntityModelLayer = registerMain("tuff_golem")


    fun init() {
        EntityModelLayerRegistry.registerModelLayer(GLOOM, GloomEntityModel::texturedModelData)
        EntityModelLayerRegistry.registerModelLayer(GLOOM_EYES, GloomEntityModel::texturedModelData)
        EntityModelLayerRegistry.registerModelLayer(GLOOM_INNER_ARMOR, ::createInnerArmor)
        EntityModelLayerRegistry.registerModelLayer(GLOOM_OUTER_ARMOR, ::createOuterArmor)
        EntityModelLayerRegistry.registerModelLayer(GLOOM_OUTER, ::createSkeletonOuterLayer)
        EntityModelLayerRegistry.registerModelLayer(SKELETON_WOLF, ::texturedModelData)
        EntityModelLayerRegistry.registerModelLayer(WITHER_SKELETON_WOLF, ::texturedModelData)
        EntityModelLayerRegistry.registerModelLayer(WITHER_SKELETON_HORSE, ::createHorseLayer)

        EntityModelLayerRegistry.registerModelLayer(TUFF_GOLEM, TuffGolemEntityModel::texturedModelData)
    }

    private fun createInnerArmor(): TexturedModelData =
        TexturedModelData.of(BipedArmorEntityModel.getModelData(Dilation(0.5F)), 64, 32)
    private fun createOuterArmor(): TexturedModelData =
        TexturedModelData.of(BipedArmorEntityModel.getModelData(Dilation(1.0F)), 64, 32)
    private fun createSkeletonOuterLayer(): TexturedModelData =
        TexturedModelData.of(BipedEntityModel.getModelData(Dilation(0.25f), 0.0f), 64, 32)
    private fun createHorseLayer(): TexturedModelData =
        TexturedModelData.of(HorseEntityModel.getModelData(Dilation.NONE), 64, 64)

    private fun registerMain(id: String): EntityModelLayer {
        return register(id, "main")
    }

    private fun createInnerArmor(id: String): EntityModelLayer {
        return register(id, "inner_armor")
    }

    private fun createOuterArmor(id: String): EntityModelLayer {
        return register(id, "outer_armor")
    }

    private fun register(id: String, layer: String): EntityModelLayer {
        val entityModelLayer = create(id, layer)
        return entityModelLayer
    }

    private fun create(id: String, layer: String): EntityModelLayer {
        return EntityModelLayer(id(id), layer)
    }
}