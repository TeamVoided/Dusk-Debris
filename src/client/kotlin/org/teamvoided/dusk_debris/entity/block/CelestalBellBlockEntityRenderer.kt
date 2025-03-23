package org.teamvoided.dusk_debris.entity.block

import net.minecraft.block.entity.BellBlockEntity
import net.minecraft.client.model.*
import net.minecraft.client.render.RenderLayer
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.block.entity.BlockEntityRenderer
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory
import net.minecraft.client.resource.Material
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.screen.PlayerScreenHandler
import net.minecraft.util.math.Direction
import net.minecraft.util.math.MathHelper
import org.teamvoided.dusk_debris.DuskDebris.id
import org.teamvoided.dusk_debris.entity.DuskEntityModelLayers.CELESTAL_BELL

class CelestalBellBlockEntityRenderer(
    ctx: BlockEntityRendererFactory.Context,
) : BlockEntityRenderer<BellBlockEntity> {
    private val bellBody: ModelPart
    init {
        val modelPart = ctx.getLayerModelPart(CELESTAL_BELL)
        this.bellBody = modelPart.getChild("bell_body")
    }

    override fun render(
        bellBlockEntity: BellBlockEntity,
        f: Float,
        matrices: MatrixStack,
        vertexConsumers: VertexConsumerProvider,
        i: Int,
        j: Int
    ) {
        val g = bellBlockEntity.ringTicks.toFloat() + f
        var h = 0.0f
        var k = 0.0f
        if (bellBlockEntity.ringing) {
            val l = MathHelper.sin(g / Math.PI.toFloat()) / (4.0f + g / 3.0f)
            when (bellBlockEntity.lastSideHit) {
                Direction.NORTH -> h = -l
                Direction.SOUTH -> h = l
                Direction.EAST -> k = -l
                Direction.WEST -> k = l
                else -> Unit
            }
        }
        bellBody.pitch = h
        bellBody.roll = k
        val vertexConsumer = CELESTAL_BELL_TEXTURE.getVertexConsumer(vertexConsumers, RenderLayer::getEntitySolid)
        bellBody.render(matrices, vertexConsumer, i, j)
    }

    companion object {
        val CELESTAL_BELL_TEXTURE: Material =
            Material(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, id("entity/celestal_bell/celestal_bell_body"))

        fun getTexturedModelData(): TexturedModelData {
            val modelData = ModelData()
            val modelPartData = modelData.root
            val modelPartData2 = modelPartData.addChild(
                "bell_body",
                ModelPartBuilder.create().uv(0, 0).cuboid(-3.0f, -6.0f, -3.0f, 6.0f, 7.0f, 6.0f),
                ModelTransform.pivot(8.0f, 12.0f, 8.0f)
            )
            modelPartData2.addChild(
                "bell_base",
                ModelPartBuilder.create().uv(0, 13).cuboid(4.0f, 4.0f, 4.0f, 8.0f, 2.0f, 8.0f),
                ModelTransform.pivot(-8.0f, -12.0f, -8.0f)
            )
            return TexturedModelData.of(modelData, 32, 32)
        }
    }
}
