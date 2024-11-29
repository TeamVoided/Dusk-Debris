package org.teamvoided.dusk_debris.entity.volaphyra.render

import net.minecraft.client.render.OverlayTexture
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.entity.ParrotEntityRenderer
import net.minecraft.client.render.entity.feature.FeatureRenderer
import net.minecraft.client.render.entity.feature.FeatureRendererContext
import net.minecraft.client.render.entity.model.EntityModelLoader
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.entity.EntityType
import net.minecraft.entity.passive.ParrotEntity
import net.minecraft.nbt.NbtCompound
import net.minecraft.util.Identifier
import org.teamvoided.dusk_debris.DuskDebris
import org.teamvoided.dusk_debris.entity.AbstractVolaphyraEntity
import org.teamvoided.dusk_debris.entity.DuskEntityModelLayers
import org.teamvoided.dusk_debris.entity.volaphyra.model.VolaphyraCoreModel
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
        matrices.push()
        matrices.translate(0f, 0.2f, 0f)
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
        matrices.pop()
    }

    companion object {
        val TEXTURE: Identifier = DuskDebris.id("textures/entity/volaphyra/volaphyra_core.png")
    }
}