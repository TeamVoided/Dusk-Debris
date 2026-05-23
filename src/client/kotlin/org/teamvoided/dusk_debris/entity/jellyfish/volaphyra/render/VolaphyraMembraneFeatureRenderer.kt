package org.teamvoided.dusk_debris.entity.jellyfish.volaphyra.render

import com.mojang.blaze3d.vertex.PoseStack
import net.minecraft.client.Minecraft
import net.minecraft.client.model.geom.EntityModelSet
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.RenderType
import net.minecraft.client.renderer.entity.LivingEntityRenderer
import net.minecraft.client.renderer.entity.RenderLayerParent
import net.minecraft.client.renderer.entity.layers.RenderLayer
import org.teamvoided.dusk_debris.entity.AbstractVolaphyraEntity
import org.teamvoided.dusk_debris.entity.DuskEntityModelLayers
import org.teamvoided.dusk_debris.entity.jellyfish.volaphyra.VolaphyraEntityRenderer.Companion.VOLAPHYRA_MESOGLEA
import org.teamvoided.dusk_debris.entity.jellyfish.volaphyra.model.VolaphyraCoreModel
import org.teamvoided.dusk_debris.entity.jellyfish.volaphyra.model.VolaphyraMesogleaModel

class VolaphyraMembraneFeatureRenderer(
    context: RenderLayerParent<AbstractVolaphyraEntity, VolaphyraCoreModel>,
    loader: EntityModelSet
) : RenderLayer<AbstractVolaphyraEntity, VolaphyraCoreModel>(context) {
    private val model = VolaphyraMesogleaModel(loader.bakeLayer(DuskEntityModelLayers.VOLAPHYRA_MESOGLEA))

    override fun render(
        matrices: PoseStack,
        vertexConsumers: MultiBufferSource,
        light: Int, //i
        entity: AbstractVolaphyraEntity,
        limbAngle: Float, //f
        limbDistance: Float, //g
        tickDelta: Float, //h
        age: Float, //j
        headYaw: Float, //k
        headPitch: Float //l
    ) {
        val minecraftClient = Minecraft.getInstance()
        val bl = minecraftClient.shouldEntityAppearGlowing(entity) && entity.isInvisible
        if (!entity.isInvisible || bl) {
            val vertexConsumer = if (bl) {
                vertexConsumers.getBuffer(RenderType.outline(VOLAPHYRA_MESOGLEA))
            } else {
                vertexConsumers.getBuffer(RenderType.entityTranslucent(VOLAPHYRA_MESOGLEA))
            }

            this.parentModel.copyPropertiesTo(this.model)
            model.prepareMobModel(entity, limbAngle, limbDistance, tickDelta)
            model.setupAnim(entity, limbAngle, limbDistance, age, headYaw, headPitch)
            model.renderToBuffer(matrices, vertexConsumer, light, LivingEntityRenderer.getOverlayCoords(entity, 0.0f))
        }
    }
}