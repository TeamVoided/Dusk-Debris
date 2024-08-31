package org.teamvoided.dusk_debris.entity.tuff_golem

import net.minecraft.client.render.entity.EntityRendererFactory
import net.minecraft.client.render.entity.MobEntityRenderer
import org.teamvoided.dusk_debris.entity.tuff_golem.render.TuffGolemEntityModel
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.Identifier
import net.minecraft.util.math.Axis
import org.teamvoided.dusk_debris.entity.DuskEntityModelLayers
import org.teamvoided.dusk_debris.entity.TuffGolemEntity
import kotlin.math.abs

class TuffGolemEntityRenderer(context: EntityRendererFactory.Context) :
    MobEntityRenderer<TuffGolemEntity, TuffGolemEntityModel<TuffGolemEntity>>(
        context,
        TuffGolemEntityModel(context.getPart(DuskEntityModelLayers.TUFF_GOLEM)),
        0.7f //shadowRadius
    ) {
    init {
//        this.addFeature(IronGolemCrackFeatureRenderer(this))
//        this.addFeature(IronGolemFlowerFeatureRenderer(this, context.blockRenderManager))
    }

    override fun getTexture(tuffGolemEntity: TuffGolemEntity): Identifier {
        return TEXTURE
    }

    override fun setupTransforms(
        tuffGolemEntity: TuffGolemEntity,
        matrices: MatrixStack,
        animationProgress: Float,
        bodyYaw: Float,
        tickDelta: Float,
        i: Float
    ) {
        super.setupTransforms(tuffGolemEntity, matrices, animationProgress, bodyYaw, tickDelta, i)
        if (!(tuffGolemEntity.limbData.limbDistance.toDouble() < 0.01)) {
            val limbAngle = tuffGolemEntity.limbData.getLimbAngle(tickDelta) + 6.0f
            val rotate = ((abs((limbAngle % 13.0f - 6.5f).toDouble()) - 3.25f) / 3.25f).toFloat()
            matrices.rotate(Axis.Z_POSITIVE.rotationDegrees(6.5f * rotate))
        }
    }

    companion object {
        private val TEXTURE: Identifier = Identifier.ofDefault("textures/entity/tuff_golem/tuff_golem.png")
    }
}