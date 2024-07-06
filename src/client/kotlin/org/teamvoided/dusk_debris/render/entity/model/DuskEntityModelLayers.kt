package org.teamvoided.dusk_debris.render.entity.model

import net.minecraft.client.render.entity.model.EntityModelLayer
import org.teamvoided.dusk_debris.DuskDebris.id

object DuskEntityModelLayers {
    val GLOOM: EntityModelLayer = registerMain("gloom")
    val GLOOM_INNER_ARMOR: EntityModelLayer = createInnerArmor("gloom")
    val GLOOM_OUTER_ARMOR: EntityModelLayer = createOuterArmor("gloom")
    val GLOOM_OUTER: EntityModelLayer = register("gloom", "outer")


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
//        check(EntityModelLayers.LAYERS.add(entityModelLayer)) { "Duplicate registration for $entityModelLayer" }
        return entityModelLayer
    }
    private fun create(id: String, layer: String): EntityModelLayer {
        return EntityModelLayer(id(id), layer)
    }
}