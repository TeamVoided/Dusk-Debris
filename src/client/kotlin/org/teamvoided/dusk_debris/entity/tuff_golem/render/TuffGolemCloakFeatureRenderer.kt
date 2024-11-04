package org.teamvoided.dusk_debris.entity.tuff_golem.render

import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.entity.feature.FeatureRenderer
import net.minecraft.client.render.entity.feature.FeatureRendererContext
import net.minecraft.client.render.entity.model.EntityModelLoader
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.entity.EquipmentSlot
import net.minecraft.item.Items
import net.minecraft.registry.Registries
import net.minecraft.util.Identifier
import org.teamvoided.dusk_debris.DuskDebris.id
import org.teamvoided.dusk_debris.entity.DuskEntityModelLayers
import org.teamvoided.dusk_debris.entity.TuffGolemEntity
import org.teamvoided.dusk_debris.entity.tuff_golem.model.TuffGolemCloakModel
import org.teamvoided.dusk_debris.entity.tuff_golem.model.TuffGolemEntityModel

class TuffGolemCloakFeatureRenderer(
    context: FeatureRendererContext<TuffGolemEntity, TuffGolemEntityModel>,
    loader: EntityModelLoader
) : FeatureRenderer<TuffGolemEntity, TuffGolemEntityModel>(context) {
    private val model: TuffGolemCloakModel =
        TuffGolemCloakModel(loader.getModelPart(DuskEntityModelLayers.TUFF_GOLEM_ROBE))

    override fun render(
        matrices: MatrixStack,
        vertexConsumers: VertexConsumerProvider,
        i: Int,
        tuffGolemEntity: TuffGolemEntity,
        f: Float,
        g: Float,
        h: Float,
        j: Float,
        k: Float,
        l: Float
    ) {
        val cloakBlock = tuffGolemEntity.getEquippedStack(EquipmentSlot.CHEST)
        if (!cloakBlock.isEmpty) {
            render(
                this.contextModel,
                this.model,
                tuffGolemCloakTextureId(Registries.ITEM.getId(cloakBlock.item).path),
                matrices,
                vertexConsumers,
                i,
                tuffGolemEntity,
                f,
                g,
                j,
                k,
                l,
                h,
                -1
            )
        }
    }

    companion object {
        private fun tuffGolemCloakTextureId(string: String): Identifier =
            id("textures/entity/tuff_golem/cloak/$string.png")
    }
}