package org.teamvoided.dusk_debris

import com.mojang.blaze3d.vertex.VertexConsumer
import net.minecraft.client.render.Frustum
import net.minecraft.client.render.OverlayTexture
import net.minecraft.client.render.RenderLayer
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.entity.EntityRendererFactory
import net.minecraft.client.render.entity.MobEntityRenderer
import net.minecraft.client.render.entity.model.EntityModelLayer
import net.minecraft.client.render.entity.model.EntityModelLayers
import net.minecraft.client.render.entity.model.GuardianEntityModel
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.mob.GuardianEntity
import net.minecraft.util.Identifier
import net.minecraft.util.math.Axis
import net.minecraft.util.math.Box
import net.minecraft.util.math.MathHelper
import net.minecraft.util.math.Vec3d
import kotlin.math.acos
import kotlin.math.atan2

class Temp protected constructor(ctx: EntityRendererFactory.Context, shadowRadius: Float, layer: EntityModelLayer) :
    MobEntityRenderer<GuardianEntity, GuardianEntityModel>(
        ctx,
        GuardianEntityModel(ctx.getPart(layer)),
        shadowRadius
    ) {
    constructor(context: EntityRendererFactory.Context) : this(context, 0.5f, EntityModelLayers.GUARDIAN)

    override fun shouldRender(
        guardianEntity: GuardianEntity,
        frustum: Frustum,
        x: Double,
       y: Double,
        z: Double
    ): Boolean {
        if (super.shouldRender(guardianEntity, frustum, x, y, z)) {
            return true
        } else {
            if (guardianEntity.hasBeamTarget()) {
                val livingEntity = guardianEntity.beamTarget
                if (livingEntity != null) {
                    val vec3d = this.fromLerpedPosition(livingEntity, livingEntity.height.toDouble() * 0.5, 1.0f)
                    val vec3d2 =
                        this.fromLerpedPosition(guardianEntity, guardianEntity.standingEyeHeight.toDouble(), 1.0f)
                    return frustum.isVisible(Box(vec3d2.x, vec3d2.y, vec3d2.z, vec3d.x, vec3d.y, vec3d.z))
                }
            }

            return false
        }
    }

    private fun fromLerpedPosition(entity: LivingEntity, yOffset: Double, delta: Float): Vec3d {
        val d = MathHelper.lerp(delta.toDouble(), entity.lastRenderX, entity.x)
        val e = MathHelper.lerp(delta.toDouble(), entity.lastRenderY, entity.y) + yOffset
        val f = MathHelper.lerp(delta.toDouble(), entity.lastRenderZ, entity.z)
        return Vec3d(d, e, f)
    }

    override fun render(
        guardianEntity: GuardianEntity,
        yaw: Float,
        tickDelta: Float,
        matrices: MatrixStack,
        vertexConsumers: VertexConsumerProvider,
        i: Int
    ) {
        super.render(guardianEntity, yaw, tickDelta, matrices, vertexConsumers, i)
        val livingEntity = guardianEntity.beamTarget
        if (livingEntity != null) {
            val h = guardianEntity.getBeamProgress(tickDelta)
            val j = guardianEntity.beamTicks + tickDelta
            val k = j * 0.5f % 1.0f
            val eyeHeight = guardianEntity.standingEyeHeight
            matrices.push()
            matrices.translate(0.0f, eyeHeight, 0.0f)
            val vec3d = this.fromLerpedPosition(livingEntity, livingEntity.height.toDouble() * 0.5, tickDelta)
            val vec3d2 = this.fromLerpedPosition(guardianEntity, eyeHeight.toDouble(), tickDelta)
            var vec3d3 = vec3d.subtract(vec3d2)
            val m = (vec3d3.length() + 1.0).toFloat()
            vec3d3 = vec3d3.normalize()
            val n = acos(vec3d3.y).toFloat()
            val o = atan2(vec3d3.z, vec3d3.x).toFloat()
            matrices.rotate(Axis.Y_POSITIVE.rotationDegrees((1.5707964f - o) * 57.295776f))
            matrices.rotate(Axis.X_POSITIVE.rotationDegrees(n * 57.295776f))
            val q = j * 0.05f * -1.5f
            val r = h * h
            val s = 64 + (r * 191.0f).toInt()
            val t = 32 + (r * 191.0f).toInt()
            val u = 128 - (r * 64.0f).toInt()
            val v = 0.2f
            val w = 0.282f
            val x = MathHelper.cos(q + 2.3561945f) * 0.282f
            val y = MathHelper.sin(q + 2.3561945f) * 0.282f
            val z = MathHelper.cos(q + 0.7853982f) * 0.282f
            val aa = MathHelper.sin(q + 0.7853982f) * 0.282f
            val ab = MathHelper.cos(q + 3.926991f) * 0.282f
            val ac = MathHelper.sin(q + 3.926991f) * 0.282f
            val ad = MathHelper.cos(q + 5.4977875f) * 0.282f
            val ae = MathHelper.sin(q + 5.4977875f) * 0.282f
            val af = MathHelper.cos(q + 3.1415927f) * 0.2f
            val ag = MathHelper.sin(q + 3.1415927f) * 0.2f
            val ah = MathHelper.cos(q + 0.0f) * 0.2f
            val ai = MathHelper.sin(q + 0.0f) * 0.2f
            val aj = MathHelper.cos(q + 1.5707964f) * 0.2f
            val ak = MathHelper.sin(q + 1.5707964f) * 0.2f
            val al = MathHelper.cos(q + 4.712389f) * 0.2f
            val am = MathHelper.sin(q + 4.712389f) * 0.2f
            val ao = 0.0f
            val ap = 0.4999f
            val aq = -1.0f + k
            val ar = m * 2.5f + aq
            val vertexConsumer = vertexConsumers.getBuffer(LAYER)
            val entry = matrices.peek()
            vertex(vertexConsumer, entry, af, m, ag, s, t, u, 0.4999f, ar)
            vertex(vertexConsumer, entry, af, 0.0f, ag, s, t, u, 0.4999f, aq)
            vertex(vertexConsumer, entry, ah, 0.0f, ai, s, t, u, 0.0f, aq)
            vertex(vertexConsumer, entry, ah, m, ai, s, t, u, 0.0f, ar)
            vertex(vertexConsumer, entry, aj, m, ak, s, t, u, 0.4999f, ar)
            vertex(vertexConsumer, entry, aj, 0.0f, ak, s, t, u, 0.4999f, aq)
            vertex(vertexConsumer, entry, al, 0.0f, am, s, t, u, 0.0f, aq)
            vertex(vertexConsumer, entry, al, m, am, s, t, u, 0.0f, ar)
            var `as` = 0.0f
            if (guardianEntity.age % 2 == 0) {
                `as` = 0.5f
            }

            vertex(vertexConsumer, entry, x, m, y, s, t, u, 0.5f, `as` + 0.5f)
            vertex(vertexConsumer, entry, z, m, aa, s, t, u, 1.0f, `as` + 0.5f)
            vertex(vertexConsumer, entry, ad, m, ae, s, t, u, 1.0f, `as`)
            vertex(vertexConsumer, entry, ab, m, ac, s, t, u, 0.5f, `as`)
            matrices.pop()
        }
    }

    override fun getTexture(guardianEntity: GuardianEntity): Identifier {
        return TEXTURE
    }

    companion object {
        private val TEXTURE: Identifier = Identifier.ofDefault("textures/entity/guardian.png")
        private val EXPLOSION_BEAM_TEXTURE: Identifier = Identifier.ofDefault("textures/entity/guardian_beam.png")
        private val LAYER: RenderLayer = RenderLayer.getEntityCutoutNoCull(EXPLOSION_BEAM_TEXTURE)

        private fun vertex(
            vertexConsumer: VertexConsumer,
            entry: MatrixStack.Entry,
            x: Float,
            y: Float,
            z: Float,
            red: Int,
            green: Int,
            blue: Int,
            u: Float,
            v: Float
        ) {
            vertexConsumer.xyz(entry, x, y, z).color(red, green, blue, 255).uv0(u, v).uv1(OverlayTexture.DEFAULT_UV)
                .uv2(15728880).normal(entry, 0.0f, 1.0f, 0.0f)
        }
    }
}
