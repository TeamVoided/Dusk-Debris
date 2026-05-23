package org.teamvoided.dusk_debris.entity.block

import com.mojang.blaze3d.vertex.PoseStack
import net.minecraft.client.model.geom.ModelPart
import net.minecraft.client.model.geom.PartPose
import net.minecraft.client.model.geom.builders.CubeListBuilder
import net.minecraft.client.model.geom.builders.LayerDefinition
import net.minecraft.client.model.geom.builders.MeshDefinition
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.RenderType
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider
import net.minecraft.client.resources.model.Material
import net.minecraft.core.Direction
import net.minecraft.util.Mth
import net.minecraft.world.inventory.InventoryMenu
import net.minecraft.world.level.block.entity.BellBlockEntity
import org.teamvoided.dusk_debris.DuskDebris.id
import org.teamvoided.dusk_debris.entity.DuskEntityModelLayers.CELESTAL_BELL

class CelestalBellBlockEntityRenderer(
    ctx: BlockEntityRendererProvider.Context,
) : BlockEntityRenderer<BellBlockEntity> {
    private val bellBody: ModelPart
    init {
        val modelPart = ctx.bakeLayer(CELESTAL_BELL)
        this.bellBody = modelPart.getChild("bell_body")
    }

    override fun render(
        bellBlockEntity: BellBlockEntity,
        f: Float,
        matrices: PoseStack,
        vertexConsumers: MultiBufferSource,
        i: Int,
        j: Int
    ) {
        val g = bellBlockEntity.ticks.toFloat() + f
        var h = 0.0f
        var k = 0.0f
        if (bellBlockEntity.shaking) {
            val l = Mth.sin(g / Math.PI.toFloat()) / (4.0f + g / 3.0f)
            when (bellBlockEntity.clickDirection) {
                Direction.NORTH -> h = -l
                Direction.SOUTH -> h = l
                Direction.EAST -> k = -l
                Direction.WEST -> k = l
                else -> Unit
            }
        }
        bellBody.xRot = h
        bellBody.zRot = k
        val vertexConsumer = CELESTAL_BELL_TEXTURE.buffer(vertexConsumers, RenderType::entitySolid)
        bellBody.render(matrices, vertexConsumer, i, j)
    }

    companion object {
        val CELESTAL_BELL_TEXTURE: Material =
            Material(InventoryMenu.BLOCK_ATLAS, id("entity/celestal_bell/celestal_bell_body"))

        fun getTexturedModelData(): LayerDefinition {
            val modelData = MeshDefinition()
            val modelPartData = modelData.root
            val modelPartData2 = modelPartData.addOrReplaceChild(
                "bell_body",
                CubeListBuilder.create().texOffs(0, 0).addBox(-3.0f, -6.0f, -3.0f, 6.0f, 7.0f, 6.0f),
                PartPose.offset(8.0f, 12.0f, 8.0f)
            )
            modelPartData2.addOrReplaceChild(
                "bell_base",
                CubeListBuilder.create().texOffs(0, 13).addBox(4.0f, 4.0f, 4.0f, 8.0f, 2.0f, 8.0f),
                PartPose.offset(-8.0f, -12.0f, -8.0f)
            )
            return LayerDefinition.create(modelData, 32, 32)
        }
    }
}
