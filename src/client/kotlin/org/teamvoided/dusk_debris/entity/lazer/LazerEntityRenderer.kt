package org.teamvoided.dusk_debris.entity.lazer

import com.mojang.blaze3d.vertex.VertexConsumer
import net.minecraft.client.render.Frustum
import net.minecraft.client.render.OverlayTexture
import net.minecraft.client.render.RenderLayer
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.entity.EntityRenderer
import net.minecraft.client.render.entity.EntityRendererFactory
import net.minecraft.client.util.ColorUtil
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.client.util.math.Vector2f
import net.minecraft.util.Identifier
import net.minecraft.util.math.Axis
import net.minecraft.util.math.Box
import net.minecraft.util.math.MathHelper
import org.teamvoided.dusk_debris.entity.LazerEntity

class LazerEntityRenderer(ctx: EntityRendererFactory.Context) : EntityRenderer<LazerEntity>(ctx) {

    override fun shouldRender(
        entity: LazerEntity,
        frustum: Frustum,
        x: Double,
        y: Double,
        z: Double
    ): Boolean {
        return true


        if (super.shouldRender(entity, frustum, x, y, z)) {
            return true
        } else {
            val dir = entity.getRotationVec(1f)
            val vec3d = entity.pos.add(0.0, entity.height / 2.0, 0.0)
            val vec3d2 = vec3d.add(entity.length * dir.x, entity.length * dir.y, entity.length * dir.z)
            val box = Box(vec3d, vec3d2)

            return frustum.isVisible(box)
        }
    }

    override fun render(
        entity: LazerEntity,
        yaw: Float,
        tickDelta: Float,
        matrices: MatrixStack,
        vertexConsumers: VertexConsumerProvider,
        light: Int
    ) {
        val tim = entity.age.toLong()
        val dir = Vector2f(
            MathHelper.lerp(tickDelta, entity.prevYaw, entity.yaw),
            MathHelper.lerp(tickDelta, entity.prevPitch, entity.pitch)
        )
        val len = entity.length
        drawSegment(
            matrices,
            vertexConsumers,
            tickDelta,
            tim,
            dir,
            0,
            MAX_BEAM_HEIGHT,
            -1
        )
        super.render(entity, yaw, tickDelta, matrices, vertexConsumers, light)
    }

//    fun render(
//        beaconBlockEntity: BeaconBlockEntity,
//        tickDelta: Float,
//        matrices: MatrixStack,
//        vertexConsumers: VertexConsumerProvider,
//        i: Int,
//        j: Int
//    ) {
//        val worldTime = beaconBlockEntity.world!!.time
//        val list = beaconBlockEntity.beamSegments
//        var segmentBottom = 0
//
//        list.forEachIndexed { idx, it ->
//            drawSegment(
//                matrices,
//                vertexConsumers,
//                tickDelta,
//                worldTime,
//                segmentBottom,
//                if (idx == list.size - 1) MAX_BEAM_HEIGHT else it.height,
//                it.color
//            )
//            segmentBottom += it.height
//        }
//    }

//    override fun rendersOutsideBoundingBox(beaconBlockEntity: BeaconBlockEntity): Boolean = true

//    override fun getRenderDistance(): Int = 256

//    override fun isInRenderDistance(beaconBlockEntity: BeaconBlockEntity, vec3d: Vec3d): Boolean {
//        return Vec3d.ofCenter(beaconBlockEntity.pos).multiply(1.0, 0.0, 1.0)
//            .withinRange(vec3d.multiply(1.0, 0.0, 1.0), this.renderDistance.toDouble())
//    }


    override fun getTexture(entity: LazerEntity): Identifier = BEAM_TEXTURE

    companion object {
        val BEAM_TEXTURE: Identifier = Identifier.ofDefault("textures/entity/beacon_beam.png")
        const val MAX_BEAM_HEIGHT: Int = 1024

        private fun drawSegment(
            matrices: MatrixStack,
            vertexConsumers: VertexConsumerProvider,
            tickDelta: Float,
            time: Long,
            direction: Vector2f,
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
                direction,
                segmentBottom,
                segmentHeight,
                color,
                0.1f,
                0.15f
            )
        }

        fun drawSegment(
            matrices: MatrixStack,
            vertexConsumers: VertexConsumerProvider,
            texture: Identifier,
            tickDelta: Float,
            heightScale: Float,
            time: Long,
            direction: Vector2f,
            segmentBottom: Int,
            segmentHeight: Int,
            color: Int,
            innerRadius: Float,
            outerRadius: Float
        ) {
            val segmentTop = segmentBottom + segmentHeight
            matrices.push()
            matrices.translate(0.0, 0.25, 0.0)
            val spinRate = Math.floorMod(time, 40) + tickDelta
            val spinDir = if (segmentHeight < 0) spinRate else -spinRate
            val spinDec = MathHelper.fractionalPart(spinDir * 0.2f - MathHelper.floor(spinDir * 0.1f))
            matrices.push()
            matrices.rotateBeam(direction)
            matrices.rotate(Axis.Y_POSITIVE.rotationDegrees(spinRate * 2.25f - 45f))
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
            matrices.rotateBeam(direction)
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

        private fun MatrixStack.rotateBeam(direction: Vector2f) {
            this.rotate(Axis.Y_POSITIVE.rotationDegrees(direction.x - 90.0f))
            this.rotate(Axis.Z_POSITIVE.rotationDegrees(direction.y))
        }

        private fun renderBeamLayer(
            matrices: MatrixStack,
            vertexConsumers: VertexConsumer,
            argb: Int,
            segmentBottom: Int,
            segmentTop: Int,
            x1: Float, z1: Float,
            x2: Float, z2: Float,
            x3: Float, z3: Float,
            x4: Float, z4: Float,
            u1: Float, u2: Float,
            v1: Float, v2: Float
        ) {
            val entry = matrices.peek()
            renderBeamFace(entry, vertexConsumers, argb, segmentBottom, segmentTop, x1, z1, x2, z2, u1, u2, v1, v2)
            renderBeamFace(entry, vertexConsumers, argb, segmentBottom, segmentTop, x4, z4, x3, z3, u1, u2, v1, v2)
            renderBeamFace(entry, vertexConsumers, argb, segmentBottom, segmentTop, x2, z2, x4, z4, u1, u2, v1, v2)
            renderBeamFace(entry, vertexConsumers, argb, segmentBottom, segmentTop, x3, z3, x1, z1, u1, u2, v1, v2)
        }

        private fun renderBeamFace(
            matrices: MatrixStack.Entry,
            vertexConsumers: VertexConsumer,
            argb: Int,
            segmentBottom: Int,
            segmentTop: Int,
            x1: Float, z1: Float,
            x2: Float, z2: Float,
            u1: Float, u2: Float,
            v1: Float, v2: Float
        ) {
            renderBeamVertex(matrices, vertexConsumers, argb, segmentTop, x1, z1, u2, v1)
            renderBeamVertex(matrices, vertexConsumers, argb, segmentBottom, x1, z1, u2, v2)
            renderBeamVertex(matrices, vertexConsumers, argb, segmentBottom, x2, z2, u1, v2)
            renderBeamVertex(matrices, vertexConsumers, argb, segmentTop, x2, z2, u1, v1)
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
