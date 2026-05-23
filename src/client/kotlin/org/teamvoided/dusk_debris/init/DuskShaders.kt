package org.teamvoided.dusk_debris.init

import com.mojang.blaze3d.systems.RenderSystem
import com.mojang.blaze3d.vertex.*
import net.fabricmc.fabric.api.client.rendering.v1.CoreShaderRegistrationCallback
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext
import net.minecraft.client.renderer.RenderStateShard.ShaderStateShard
import net.minecraft.client.renderer.ShaderInstance
import net.minecraft.world.phys.Vec3
import org.joml.Matrix4f
import org.teamvoided.dusk_debris.DuskDebris.id

object DuskShaders {
    private var customType: ShaderInstance? = null

    val STATUE_SHADER: ShaderStateShard = ShaderStateShard { statueRenderType }
    var statueRenderType: ShaderInstance? = null
        private set

    fun init() {
        CoreShaderRegistrationCallback.EVENT.register { ctx ->
            ctx.register(
                id("rendertype_statue"), DefaultVertexFormat.NEW_ENTITY
            ) { statueRenderType = it }
//            ctx.register(id("rendertype_custom"), VertexFormats.POSITION_COLOR) { customType = it }
        }
//        WorldRenderEvents.AFTER_ENTITIES.register(::renderPlane)
    }

    fun grayscale(ctx: WorldRenderContext) = ctx.matrixStack()?.apply {
        val profiler = ctx.profiler()
        profiler.push("duskDebris")
        this.pushPose()
        this.popPose()
        profiler.pop()
        Thread.yield()
    }

    fun renderPlane(ctx: WorldRenderContext) = ctx.matrixStack()?.apply {
        val profiler = ctx.profiler()
        profiler.push("duskDebris")

        val color = 0xffffffff.toInt()
        val tessellator = Tesselator.getInstance()
        val pose = this.last().pose()

        this.pushPose()
        RenderSystem.disableDepthTest()
        RenderSystem.enableBlend()
        RenderSystem.setShader { customType }
        RenderSystem.disableCull()
//        RenderSystem.setShaderTexture(0, EndPortalBlockEntityRenderer.PORTAL_TEXTURE)
//        RenderSystem.setShaderTexture(1, EndPortalBlockEntityRenderer.SKY_TEXTURE)

        RenderSystem.disableCull()
        val camPos = ctx.camera().position
        val camPosY100 = Vec3(camPos.x, 100.0, camPos.y)
        for (x in -100..100) {
            for (z in -100..100) {
                val pos = camPosY100.add(x.toDouble(), 0.0, z.toDouble())
                tessellator.plane(pos, pose, camPos, color)
            }
        }
        this.popPose()

        profiler.pop()
        Thread.yield()
    }

    private fun Tesselator.plane(pos: Vec3, pose: Matrix4f, camPos: Vec3, color: Int) {
        val builder: BufferBuilder = this.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR)
        builder.xyz(pose, pos, camPos).setColor(color)
        builder.xyz(pose, pos.add(0.0, 0.0, 1.0), camPos).setColor(color)
        builder.xyz(pose, pos.add(1.0, 0.0, 1.0), camPos).setColor(color)
        builder.xyz(pose, pos.add(1.0, 0.0, 0.0), camPos).setColor(color)
        builder.build()?.let { BufferUploader.drawWithShader(it) }
    }

    fun VertexConsumer.xyz(model: Matrix4f, vec: Vec3, camera: Vec3 = Vec3.ZERO): VertexConsumer =
        this.addVertex(model, (vec.x - camera.x).toFloat(), (vec.y - camera.y).toFloat(), (vec.z - camera.z).toFloat())

    fun VertexConsumer.normal(vec: Vec3): VertexConsumer =
        this.setNormal(vec.x.toFloat(), vec.y.toFloat(), vec.z.toFloat())
}