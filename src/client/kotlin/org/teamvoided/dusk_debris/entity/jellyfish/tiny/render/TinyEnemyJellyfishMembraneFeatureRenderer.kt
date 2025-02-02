package org.teamvoided.dusk_debris.entity.jellyfish.tiny.render

import net.minecraft.client.MinecraftClient
import net.minecraft.client.render.RenderLayer
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.entity.LivingEntityRenderer
import net.minecraft.client.render.entity.feature.FeatureRenderer
import net.minecraft.client.render.entity.feature.FeatureRendererContext
import net.minecraft.client.render.entity.model.EntityModelLoader
import net.minecraft.client.util.math.MatrixStack
import org.teamvoided.dusk_debris.entity.DuskEntityModelLayers
import org.teamvoided.dusk_debris.entity.TinyEnemyJellyfishEntity
import org.teamvoided.dusk_debris.entity.jellyfish.tiny.TinyEnemyJellyfishEntityRenderer.Companion.TEXTURE_MESOGLEA
import org.teamvoided.dusk_debris.entity.jellyfish.tiny.model.TinyEnemyJellyfishCoreModel
import org.teamvoided.dusk_debris.entity.jellyfish.tiny.model.TinyEnemyJellyfishModel

class TinyEnemyJellyfishMembraneFeatureRenderer(
    context: FeatureRendererContext<TinyEnemyJellyfishEntity, TinyEnemyJellyfishCoreModel>,
    loader: EntityModelLoader
) : FeatureRenderer<TinyEnemyJellyfishEntity, TinyEnemyJellyfishCoreModel>(context) {
    private val model = TinyEnemyJellyfishModel(loader.getModelPart(DuskEntityModelLayers.TINY_ENEMY_JELLYFISH_MESOGLEA))

    override fun render(
        matrices: MatrixStack,
        vertexConsumers: VertexConsumerProvider,
        light: Int, //i
        entity: TinyEnemyJellyfishEntity,
        limbAngle: Float, //f
        limbDistance: Float, //g
        tickDelta: Float, //h
        age: Float, //j
        headYaw: Float, //k
        headPitch: Float //l
    ) {
        val minecraftClient = MinecraftClient.getInstance()
        val bl = minecraftClient.hasOutline(entity) && entity.isInvisible
        if (!entity.isInvisible || bl) {
            val vertexConsumer = if (bl) {
                vertexConsumers.getBuffer(RenderLayer.getOutline(TEXTURE_MESOGLEA))
            } else {
                vertexConsumers.getBuffer(RenderLayer.getEntityTranslucent(TEXTURE_MESOGLEA))
            }

            this.contextModel.copyStateTo(this.model)
            model.animateModel(entity, limbAngle, limbDistance, tickDelta)
            model.setAngles(entity, limbAngle, limbDistance, age, headYaw, headPitch)
            model.method_60879(matrices, vertexConsumer, light, LivingEntityRenderer.getOverlay(entity, 0.0f))
        }
    }
}