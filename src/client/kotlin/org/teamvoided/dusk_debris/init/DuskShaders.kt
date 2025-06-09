package org.teamvoided.dusk_debris.init

import com.mojang.blaze3d.systems.RenderSystem
import com.mojang.blaze3d.vertex.*
import net.fabricmc.fabric.api.client.rendering.v1.CoreShaderRegistrationCallback
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext
import net.minecraft.client.render.RenderPhase.Shader
import net.minecraft.client.render.ShaderProgram
import net.minecraft.util.math.Vec3d
import org.joml.Matrix4f
import org.teamvoided.dusk_debris.DuskDebris.id

object DuskShaders {
    private var customType: ShaderProgram? = null

    val STATUE_SHADER: Shader = Shader { statueRenderType }
    var statueRenderType: ShaderProgram? = null
        private set

    fun init() {
        CoreShaderRegistrationCallback.EVENT.register { ctx ->
            ctx.register(
                id("rendertype_statue"), VertexFormats.POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL
            ) { statueRenderType = it }
//            ctx.register(id("rendertype_custom"), VertexFormats.POSITION_COLOR) { customType = it }
        }
//        WorldRenderEvents.AFTER_ENTITIES.register(::renderPlane)
    }

    fun grayscale(ctx: WorldRenderContext) = ctx.matrixStack()?.apply {
        val profiler = ctx.profiler()
        profiler.push("duskDebris")
        this.push()
        this.pop()
        profiler.pop()
        Thread.yield()
    }

    fun renderPlane(ctx: WorldRenderContext) = ctx.matrixStack()?.apply {
        val profiler = ctx.profiler()
        profiler.push("duskDebris")

        val color = 0xffffffff.toInt()
        val tessellator = Tessellator.getInstance()
        val pose = this.peek().model

        this.push()
        RenderSystem.disableDepthTest()
        RenderSystem.enableBlend()
        RenderSystem.setShader { customType }
        RenderSystem.disableCull()
//        RenderSystem.setShaderTexture(0, EndPortalBlockEntityRenderer.PORTAL_TEXTURE)
//        RenderSystem.setShaderTexture(1, EndPortalBlockEntityRenderer.SKY_TEXTURE)

        RenderSystem.disableCull()
        val camPos = ctx.camera().pos
        val camPosY100 = Vec3d(camPos.x, 100.0, camPos.y)
        for (x in -100..100) {
            for (z in -100..100) {
                val pos = camPosY100.add(x.toDouble(), 0.0, z.toDouble())
                tessellator.plane(pos, pose, camPos, color)
            }
        }
        this.pop()

        profiler.pop()
        Thread.yield()
    }

    private fun Tessellator.plane(pos: Vec3d, pose: Matrix4f, camPos: Vec3d, color: Int) {
        val builder: BufferBuilder = this.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR)
        builder.xyz(pose, pos, camPos).color(color)
        builder.xyz(pose, pos.add(0.0, 0.0, 1.0), camPos).color(color)
        builder.xyz(pose, pos.add(1.0, 0.0, 1.0), camPos).color(color)
        builder.xyz(pose, pos.add(1.0, 0.0, 0.0), camPos).color(color)
        builder.end()?.let { BufferRenderer.drawWithShader(it) }
    }

    fun VertexConsumer.xyz(model: Matrix4f, vec: Vec3d, camera: Vec3d = Vec3d.ZERO): VertexConsumer =
        this.xyz(model, (vec.x - camera.x).toFloat(), (vec.y - camera.y).toFloat(), (vec.z - camera.z).toFloat())

    fun VertexConsumer.normal(vec: Vec3d): VertexConsumer =
        this.normal(vec.x.toFloat(), vec.y.toFloat(), vec.z.toFloat())
}