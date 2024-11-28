package org.teamvoided.dusk_debris.entity.volaphyra.render

import org.teamvoided.dusk_debris.entity.volaphyra.model.VolaphyraCoreModel
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.entity.feature.FeatureRenderer
import net.minecraft.client.render.entity.feature.FeatureRendererContext
import net.minecraft.client.render.entity.model.EntityModelLoader
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.Identifier
import org.teamvoided.dusk_debris.DuskDebris
import org.teamvoided.dusk_debris.entity.AbstractVolaphyraEntity
import org.teamvoided.dusk_debris.entity.DuskEntityModelLayers
import org.teamvoided.dusk_debris.entity.volaphyra.model.VolaphyraEntityModel

class VolaphyraCoreFeatureRenderer(
    context: FeatureRendererContext<AbstractVolaphyraEntity, VolaphyraEntityModel>,
    loader: EntityModelLoader
) : FeatureRenderer<AbstractVolaphyraEntity, VolaphyraEntityModel>(context) {
    private val model = VolaphyraCoreModel(loader.getModelPart(DuskEntityModelLayers.VOLAPHYRA_INNER))

    override fun render(
        matrices: MatrixStack,
        vertexConsumers: VertexConsumerProvider,
        light: Int,
        entity: AbstractVolaphyraEntity,
        limbAngle: Float,
        limbDistance: Float,
        tickDelta: Float,
        age: Float,
        headYaw: Float,
        headPitch: Float
    ) {
        render(
            this.contextModel,
            this.model,
            TEXTURE,
            matrices,
            vertexConsumers,
            light,
            entity,
            limbAngle,
            limbDistance,
            age,
            headYaw,
            headPitch,
            tickDelta,
            -1
        )
    }

    companion object {
        val TEXTURE: Identifier = DuskDebris.id("textures/entity/volaphyra/volaphyra_core.png")
    }
}