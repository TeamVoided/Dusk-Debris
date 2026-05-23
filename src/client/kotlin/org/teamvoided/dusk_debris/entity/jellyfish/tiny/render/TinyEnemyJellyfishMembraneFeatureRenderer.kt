package org.teamvoided.dusk_debris.entity.jellyfish.tiny.render

import com.mojang.blaze3d.vertex.PoseStack
import net.minecraft.client.Minecraft
import net.minecraft.client.model.geom.EntityModelSet
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.RenderType
import net.minecraft.client.renderer.entity.LivingEntityRenderer
import net.minecraft.client.renderer.entity.RenderLayerParent
import net.minecraft.client.renderer.entity.layers.RenderLayer
import org.teamvoided.dusk_debris.entity.DuskEntityModelLayers
import org.teamvoided.dusk_debris.entity.TinyEnemyJellyfishEntity
import org.teamvoided.dusk_debris.entity.jellyfish.tiny.TinyEnemyJellyfishEntityRenderer.Companion.TEXTURE_MESOGLEA
import org.teamvoided.dusk_debris.entity.jellyfish.tiny.model.TinyEnemyJellyfishCoreModel
import org.teamvoided.dusk_debris.entity.jellyfish.tiny.model.TinyEnemyJellyfishModel

class TinyEnemyJellyfishMembraneFeatureRenderer(
    context: RenderLayerParent<TinyEnemyJellyfishEntity, TinyEnemyJellyfishCoreModel>,
    loader: EntityModelSet
) : RenderLayer<TinyEnemyJellyfishEntity, TinyEnemyJellyfishCoreModel>(context) {
    private val model = TinyEnemyJellyfishModel(loader.bakeLayer(DuskEntityModelLayers.TINY_ENEMY_JELLYFISH_MESOGLEA))

    override fun render(
        matrices: PoseStack,
        vertexConsumers: MultiBufferSource,
        light: Int, //i
        entity: TinyEnemyJellyfishEntity,
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
                vertexConsumers.getBuffer(RenderType.outline(TEXTURE_MESOGLEA))
            } else {
                vertexConsumers.getBuffer(RenderType.entityTranslucent(TEXTURE_MESOGLEA))
            }

            this.parentModel.copyPropertiesTo(this.model)
            model.prepareMobModel(entity, limbAngle, limbDistance, tickDelta)
            model.setupAnim(entity, limbAngle, limbDistance, age, headYaw, headPitch)
            model.renderToBuffer(matrices, vertexConsumer, light, LivingEntityRenderer.getOverlayCoords(entity, 0.0f))
        }
    }
}