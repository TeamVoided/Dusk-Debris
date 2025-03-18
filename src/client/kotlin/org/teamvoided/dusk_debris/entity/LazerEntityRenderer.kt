package org.teamvoided.dusk_debris.entity

import com.mojang.blaze3d.vertex.VertexConsumer
import net.minecraft.block.entity.BeaconBlockEntity
import net.minecraft.client.render.OverlayTexture
import net.minecraft.client.render.RenderLayer
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.block.entity.BlockEntityRenderer
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory
import net.minecraft.client.util.ColorUtil
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.Identifier
import net.minecraft.util.math.Axis
import net.minecraft.util.math.MathHelper
import net.minecraft.util.math.Vec3d

class LazerEntityRenderer(ctx: BlockEntityRendererFactory.Context) : BlockEntityRenderer<BeaconBlockEntity> {
    override fun render(
        beaconBlockEntity: BeaconBlockEntity,
        tickDelta: Float,
        matrices: MatrixStack,
        vertexConsumers: VertexConsumerProvider,
        i: Int,
        j: Int
    ) {
        val worldTime = beaconBlockEntity.world!!.time
        val list = beaconBlockEntity.beamSegments
        var segmentBottom = 0

        list.forEachIndexed { idx, it ->
            drawSegment(
                matrices,
                vertexConsumers,
                tickDelta,
                worldTime,
                segmentBottom,
                if (idx == list.size - 1) MAX_BEAM_HEIGHT else it.height,
                it.color
            )
            segmentBottom += it.height
        }
    }

    override fun rendersOutsideBoundingBox(beaconBlockEntity: BeaconBlockEntity): Boolean = true

    override fun getRenderDistance(): Int = 256

    override fun isInRenderDistance(beaconBlockEntity: BeaconBlockEntity, vec3d: Vec3d): Boolean {
        return Vec3d.ofCenter(beaconBlockEntity.pos).multiply(1.0, 0.0, 1.0)
            .withinRange(vec3d.multiply(1.0, 0.0, 1.0), this.renderDistance.toDouble())
    }

    companion object {
        val BEAM_TEXTURE: Identifier = Identifier.ofDefault("textures/entity/beacon_beam.png")
        const val MAX_BEAM_HEIGHT: Int = 1024

        private fun drawSegment(
            matrices: MatrixStack,
            vertexConsumers: VertexConsumerProvider,
            tickDelta: Float,
            time: Long,
            segmentBottom: Int,
            segmentHeight: Int,
            color: Int
        ) {
            drawSegment(
                matrices,
                vertexConsumers,
                BEAM_TEXTURE,
                tickDelta,
                1.0f,
                time,
                segmentBottom,
                segmentHeight,
                color,
                0.2f,
                0.25f
            )
        }

        fun drawSegment(
            matrices: MatrixStack,
            vertexConsumers: VertexConsumerProvider,
            texture: Identifier,
            tickDelta: Float,
            heightScale: Float,
            time: Long,
            segmentBottom: Int,
            segmentHeight: Int,
            color: Int,
            innerRadius: Float,
            outerRadius: Float
        ) {
            val segmentTop = segmentBottom + segmentHeight
            matrices.push()
            matrices.translate(0.5, 0.0, 0.5)
            val spinRate = Math.floorMod(time, 40) + tickDelta
            val spinDir = if (segmentHeight < 0) spinRate else -spinRate
            val spinDec = MathHelper.fractionalPart(spinDir * 0.2f - MathHelper.floor(spinDir * 0.1f))
            matrices.push()
            matrices.rotate(Axis.Y_POSITIVE.rotationDegrees(spinRate * 2.25f - 45.0f))
            val spinDecInv = spinDec - 1f
            var textureAnim = segmentHeight * heightScale * (0.5f / innerRadius) + spinDecInv
            renderBeamLayer(
                matrices,
                vertexConsumers.getBuffer(RenderLayer.getBeaconBeam(texture, false)),
                color,
                segmentBottom,
                segmentTop,
                0f,
                innerRadius,
                innerRadius,
                0f,
                -innerRadius,
                0f,
                0f,
                -innerRadius,
                0f,
                1f,
                textureAnim,
                spinDecInv
            )
            matrices.pop()
            textureAnim = segmentHeight * heightScale + spinDecInv
            renderBeamLayer(
                matrices,
                vertexConsumers.getBuffer(RenderLayer.getBeaconBeam(texture, true)),
                ColorUtil.Argb32.of(32, color),
                segmentBottom,
                segmentTop,
                -outerRadius,
                -outerRadius,
                outerRadius,
                -outerRadius,
                -outerRadius,
                outerRadius,
                outerRadius,
                outerRadius,
                0f,
                1f,
                textureAnim,
                spinDecInv
            )
            matrices.pop()
        }

        private fun renderBeamLayer(
            matrices: MatrixStack,
            vertexConsumers: VertexConsumer,
            argb: Int,
            segmentBottom: Int,
            segmentHeight: Int,
            x1: Float, z1: Float,
            x2: Float, z2: Float,
            x3: Float, z3: Float,
            x4: Float, z4: Float,
            u1: Float, u2: Float,
            v1: Float, v2: Float
        ) {
            val entry = matrices.peek()
            renderBeamFace(entry, vertexConsumers, argb, segmentBottom, segmentHeight, x1, z1, x2, z2, u1, u2, v1, v2)
            renderBeamFace(entry, vertexConsumers, argb, segmentBottom, segmentHeight, x4, z4, x3, z3, u1, u2, v1, v2)
            renderBeamFace(entry, vertexConsumers, argb, segmentBottom, segmentHeight, x2, z2, x4, z4, u1, u2, v1, v2)
            renderBeamFace(entry, vertexConsumers, argb, segmentBottom, segmentHeight, x3, z3, x1, z1, u1, u2, v1, v2)
        }

        private fun renderBeamFace(
            matrices: MatrixStack.Entry,
            vertexConsumers: VertexConsumer,
            argb: Int,
            segmentBottom: Int,
            segmentHeight: Int,
            x1: Float, z1: Float,
            x2: Float, z2: Float,
            u1: Float, u2: Float,
            v1: Float, v2: Float
        ) {
            renderBeamVertex(matrices, vertexConsumers, argb, segmentHeight, x1, z1, u2, v1)
            renderBeamVertex(matrices, vertexConsumers, argb, segmentBottom, x1, z1, u2, v2)
            renderBeamVertex(matrices, vertexConsumers, argb, segmentBottom, x2, z2, u1, v2)
            renderBeamVertex(matrices, vertexConsumers, argb, segmentHeight, x2, z2, u1, v1)
        }

        private fun renderBeamVertex(
            matrices: MatrixStack.Entry,
            vertexConsumers: VertexConsumer,
            argb: Int,
            y: Int,
            x: Float,
            z: Float,
            u: Float,
            v: Float
        ) {
            vertexConsumers
                .xyz(matrices, x, y.toFloat(), z)
                .color(argb)
                .uv0(u, v)
                .uv1(OverlayTexture.DEFAULT_UV)
                .uv2(15728880)
                .normal(matrices, 0.0f, 1.0f, 0.0f)
        }
    }
}
