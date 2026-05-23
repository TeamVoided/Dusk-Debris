package org.teamvoided.dusk_debris.entity.tuff_golem.render

import com.mojang.blaze3d.vertex.PoseStack
import net.minecraft.client.model.geom.EntityModelSet
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.entity.RenderLayerParent
import net.minecraft.client.renderer.entity.layers.RenderLayer
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.EquipmentSlot
import org.teamvoided.dusk_debris.DuskDebris.id
import org.teamvoided.dusk_debris.entity.DuskEntityModelLayers
import org.teamvoided.dusk_debris.entity.TuffGolemEntity
import org.teamvoided.dusk_debris.entity.tuff_golem.model.TuffGolemCloakModel
import org.teamvoided.dusk_debris.entity.tuff_golem.model.TuffGolemEntityModel

class TuffGolemCloakFeatureRenderer(
    context: RenderLayerParent<TuffGolemEntity, TuffGolemEntityModel>,
    loader: EntityModelSet
) : RenderLayer<TuffGolemEntity, TuffGolemEntityModel>(context) {
    private val model: TuffGolemCloakModel =
        TuffGolemCloakModel(loader.bakeLayer(DuskEntityModelLayers.TUFF_GOLEM_ROBE))

    override fun render(
        matrices: PoseStack,
        vertexConsumers: MultiBufferSource,
        i: Int,
        tuffGolemEntity: TuffGolemEntity,
        f: Float,
        g: Float,
        h: Float,
        j: Float,
        k: Float,
        l: Float
    ) {
        val cloakBlock = tuffGolemEntity.getItemBySlot(EquipmentSlot.CHEST)
        if (!cloakBlock.isEmpty) {
            coloredCutoutModelCopyLayerRender(
                this.parentModel,
                this.model,
                tuffGolemCloakTextureId(BuiltInRegistries.ITEM.getKey(cloakBlock.item).path),
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
        private fun tuffGolemCloakTextureId(string: String): ResourceLocation =
            id("textures/entity/tuff_golem/cloak/$string.png")
    }
}