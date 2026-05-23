package org.teamvoided.dusk_debris.entity.lazer

import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.blaze3d.vertex.VertexConsumer
import com.mojang.math.Axis
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.RenderType
import net.minecraft.client.renderer.culling.Frustum
import net.minecraft.client.renderer.entity.EntityRenderer
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.client.renderer.texture.OverlayTexture
import net.minecraft.resources.ResourceLocation
import net.minecraft.util.Mth
import net.minecraft.world.phys.AABB
import net.minecraft.world.phys.Vec3
import org.joml.Vector2f
import org.teamvoided.dusk_debris.entity.LazerEntity

class LazerEntityRenderer(ctx: EntityRendererProvider.Context) : EntityRenderer<LazerEntity>(ctx) {

    override fun shouldRender(
        entity: LazerEntity,
        frustum: Frustum,
        x: Double,
        y: Double,
        z: Double
    ): Boolean {
        if (super.shouldRender(entity, frustum, x, y, z)) {
            return true
        } else {
            val eyePos = entity.eyePosition
            val target = entity.target
            val box = AABB(eyePos, target).inflate(entity.displayRadius.second.toDouble())
            return frustum.isVisible(box)
        }
    }

    override fun render(
        entity: LazerEntity,
        yaw: Float,
        tickDelta: Float,
        matrices: PoseStack,
        vertexConsumers: MultiBufferSource,
        light: Int
    ) {
        val tim = entity.tickCount.toLong()
        val target = entity.fromLerpedPosition(entity.prevTarget, entity.target, tickDelta)
        val position = this.fromLerpedPosition(entity, entity.eyeHeight, tickDelta)
        var vec3d3 = target.subtract(position)
        val len = vec3d3.length().toFloat()
        vec3d3 = vec3d3.normalize()
        val dir = entity.getRotationPitchYaw(vec3d3)
        val rad = entity.lerpRadius(tickDelta) // entity.getBeamRadius(tickDelta)
        drawSegment(
            matrices,
            vertexConsumers,
            getTextureLocation(entity),
            tickDelta,
            tim,
            dir,
            0f,
            len,
            entity.eyeHeight.toDouble(),
            rad.first,
            rad.second
        )
        super.render(entity, yaw, tickDelta, matrices, vertexConsumers, light)
    }

    private fun fromLerpedPosition(entity: LazerEntity, yOffset: Float, delta: Float): Vec3 {
        val d = Mth.lerp(delta.toDouble(), entity.xOld, entity.x)
        val e = Mth.lerp(delta.toDouble(), entity.yOld, entity.y) + yOffset
        val f = Mth.lerp(delta.toDouble(), entity.zOld, entity.z)
        return Vec3(d, e, f)
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


    override fun getTextureLocation(entity: LazerEntity): ResourceLocation = entity.getTexture()

    companion object {
        private fun drawSegment(
            matrices: PoseStack,
            vertexConsumers: MultiBufferSource,
            texture: ResourceLocation,
            tickDelta: Float,
            time: Long,
            direction: Vector2f,
            segmentBottom: Float,
            segmentHeight: Float,
            entityRadius: Double,
            innerRadius: Float,
            outerRadius: Float,
        ) {
            drawSegment(
                matrices,
                vertexConsumers,
                texture,
                tickDelta,
                1f,
                time,
                direction,
                segmentBottom,
                segmentHeight,
                entityRadius,
                innerRadius,
                outerRadius
            )
        }

        fun drawSegment(
            matrices: PoseStack,
            vertexConsumers: MultiBufferSource,
            texture: ResourceLocation,
            tickDelta: Float,
            heightScale: Float,
            time: Long,
            direction: Vector2f,
            segmentBottom: Float,
            segmentHeight: Float,
            entityRadius: Double,
            innerRadius: Float,
            outerRadius: Float
        ) {
            val segmentTop = segmentBottom + segmentHeight
            matrices.pushPose()
            matrices.translate(0.0, entityRadius, 0.0)
            val spinRate = time + tickDelta
            val spinDir = if (segmentHeight < 0) spinRate else -spinRate
            val spinDec = Mth.frac(spinDir * 0.1f)

            matrices.pushPose()
            matrices.rotateBeam(direction)
            //matrices.rotate(Axis.Y_POSITIVE.rotationDegrees(spinRate * 2.25f - 45f))
            val spinDecInv = spinDec - 1f
            val textureAnim = segmentHeight * heightScale + spinDecInv
            renderBeamLayer(
                matrices,
                vertexConsumers,
                texture,
                texture,
                false,
                -1,
                segmentBottom - innerRadius,
                segmentTop,
                innerRadius,
                innerRadius,
                innerRadius,
                -innerRadius,
                -innerRadius,
                innerRadius,
                -innerRadius,
                -innerRadius,
                .5f + innerRadius,
                .5f - innerRadius,
                textureAnim,
                spinDecInv - innerRadius
            )
            matrices.popPose()
            matrices.rotateBeam(direction)
            val outerRadius2 = outerRadius / 2f
            renderBeamLayer(
                matrices,
                vertexConsumers,
                texture,
                texture,
                true,
                -1,//ColorUtil.Argb32.of(32, -1),
                segmentBottom - outerRadius,
                segmentTop + outerRadius2,
                -outerRadius,
                -outerRadius,
                outerRadius,
                -outerRadius,
                -outerRadius,
                outerRadius,
                outerRadius,
                outerRadius,
                .5f + outerRadius,
                .5f - outerRadius,
                textureAnim + outerRadius2,
                spinDecInv - outerRadius
            )
            matrices.popPose()
        }

        private fun PoseStack.rotateBeam(direction: Vector2f) {
//            this.rotate(Axis.Z_POSITIVE.rotationDegrees(direction.x - 0f))
//            this.rotate(Axis.X_POSITIVE.rotationDegrees(direction.y + 90f))

            this.mulPose(Axis.YP.rotationDegrees((1.5707964f - direction.y) * 57.295776f))
            this.mulPose(Axis.XP.rotationDegrees(direction.x * 57.295776f))
        }

        private fun renderBeamLayer(
            matrices: PoseStack,
            vertexConsumers: MultiBufferSource,
            sideTexture: ResourceLocation,
            endTexture: ResourceLocation,
            invert: Boolean,
            argb: Int,
            segmentBottom: Float,
            segmentTop: Float,
            x1: Float, z1: Float,
            x2: Float, z2: Float,
            x3: Float, z3: Float,
            x4: Float, z4: Float,
            u1: Float, u2: Float,
            v1: Float, v2: Float
        ) {
            val side = vertexConsumers.getBuffer(RenderType.beaconBeam(sideTexture, invert))
            val entry = matrices.last()
            renderBeamFace(entry, side, argb, segmentBottom, segmentTop, x1, z1, x2, z2, u1, u2, v1, v2)
            renderBeamFace(entry, side, argb, segmentBottom, segmentTop, x4, z4, x3, z3, u1, u2, v1, v2)
            renderBeamFace(entry, side, argb, segmentBottom, segmentTop, x2, z2, x4, z4, u1, u2, v1, v2)
            renderBeamFace(entry, side, argb, segmentBottom, segmentTop, x3, z3, x1, z1, u1, u2, v1, v2)


            val end = vertexConsumers.getBuffer(RenderType.beaconBeam(endTexture, invert))
            val sB = if (invert) segmentBottom else -segmentBottom
            val sT = if (invert) -segmentTop else segmentTop
            val radius = u1 - 0.5f
            matrices.mulPose(Axis.XP.rotationDegrees(if (invert) -90f else 90f))
            renderBeamFace(
                matrices.last(), end, argb,
                -radius, radius,
                -radius, sB,
                radius, sB,
                u1, u2,
                u1, u2
            )
            matrices.mulPose(Axis.XP.rotationDegrees(180f))
            renderBeamFace(
                matrices.last(), end, argb,
                -radius, radius,
                -radius, sT,
                radius, sT,
                u1, u2,
                u1, u2
            )
        }

        private fun renderBeamFace(
            matrices: PoseStack.Pose,
            vertexConsumers: VertexConsumer,
            argb: Int,
            segmentBottom: Float,
            segmentTop: Float,
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
            matrices: PoseStack.Pose,
            vertexConsumers: VertexConsumer,
            argb: Int,
            y: Float,
            x: Float,
            z: Float,
            u: Float,
            v: Float
        ) {
            vertexConsumers
                .addVertex(matrices, x, y, z)
                .setColor(argb)
                .setUv(u, v)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(15728880)
                .setNormal(matrices, 0.0f, 1.0f, 0.0f)
        }
    }
}
