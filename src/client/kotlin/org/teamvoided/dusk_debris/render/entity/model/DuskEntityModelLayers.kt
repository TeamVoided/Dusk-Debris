package org.teamvoided.dusk_debris.render.entity.model

import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry
import net.minecraft.client.render.entity.model.EntityModelLayer
import org.teamvoided.dusk_debris.DuskDebris.id
import org.teamvoided.dusk_debris.render.entity.model.skeleton.GloomEntityModel

object DuskEntityModelLayers {
    val GLOOM: EntityModelLayer = registerMain("gloom")
    val GLOOM_INNER_ARMOR: EntityModelLayer = createInnerArmor("gloom")
    val GLOOM_OUTER_ARMOR: EntityModelLayer = createOuterArmor("gloom")
    val GLOOM_OUTER: EntityModelLayer = register("gloom", "outer")

    fun init(){
        EntityModelLayerRegistry.registerModelLayer(GLOOM, GloomEntityModel::texturedModelData)
        EntityModelLayerRegistry.registerModelLayer(GLOOM_INNER_ARMOR, GloomEntityModel::texturedModelData)
        EntityModelLayerRegistry.registerModelLayer(GLOOM_OUTER_ARMOR, GloomEntityModel::texturedModelData)
        EntityModelLayerRegistry.registerModelLayer(GLOOM_OUTER, GloomEntityModel::texturedModelData)
    }

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