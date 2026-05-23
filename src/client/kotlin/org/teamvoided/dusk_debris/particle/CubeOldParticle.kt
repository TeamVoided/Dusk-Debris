package org.teamvoided.dusk_debris.particle

import com.mojang.blaze3d.vertex.VertexConsumer
import net.minecraft.client.Camera
import net.minecraft.client.Minecraft
import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.client.particle.TextureSheetParticle
import net.minecraft.client.renderer.RenderType
import net.minecraft.client.renderer.texture.OverlayTexture
import net.minecraft.util.Mth
import org.joml.Quaternionf
import org.joml.Vector3f
import org.teamvoided.dusk_debris.util.*

abstract class CubeOldParticle(
    world: ClientLevel, x: Double, y: Double, z: Double,
    velocityX: Double, velocityY: Double, velocityZ: Double
) : TextureSheetParticle(world, x, y, z, velocityX, velocityY, velocityZ) {
//    private var rotation: Vector3f = Vector3f()

    override fun render(vertexConsumer: VertexConsumer, camera: Camera, tickDelta: Float) {

//        this.renderRotatedQuad(vertexConsumer, camera, Quaternionf(), tickDelta)


        val h = Mth.lerp(tickDelta.toDouble(), xo, x)
        val i = Mth.lerp(tickDelta.toDouble(), yo, y)
        val j = Mth.lerp(tickDelta.toDouble(), zo, z)
        val vec = Vec3d(h, i, j)
        val immediate = Minecraft.getInstance().renderBuffers().bufferSource()
        val camPos = camera.position
//        MinecraftClient.getInstance().entityRenderDispatcher.render<Entity>(
//            this.itemEntity, h - campPos.getX(), i - campPos.getY(), j - campPos.getZ(),
//            this.itemEntity.getYaw(), tickDelta, MatrixStack(), immediate,
//            this.dispatcher.getLight<Entity>(this.itemEntity, tickDelta)
//        )
        val light = 255 //  getBrightness(tickDelta)

//        RenderSystem.disableBlend()
//        RenderSystem.depthMask(true)
//        RenderSystem.setShaderTexture(0, SpriteAtlasTexture.PARTICLE_ATLAS_TEXTURE)

//        val tessellator = Tessellator.getInstance()
        //tessellator.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR_TEXTURE_LIGHT_NORMAL)
        val buffer = immediate.getBuffer(RenderType.translucentMovingBlock())

        buffer.xyz(Vec3d(0f).add(vec), camPos)
            .setColor(rCol, gCol, bCol, alpha)
            .setUv(1f, -1f)
            .normal(UP)
            .setLight(light)
        buffer.xyz(Vec3d(0, 0, 1).add(vec), camPos)
            .setColor(rCol, gCol, bCol, alpha)
            .setUv(1f, 1f)
            .normal(UP)
            .setLight(light)
        buffer.xyz(Vec3d(1, 0, 1).add(vec), camPos)
            .setColor(rCol, gCol, bCol, alpha)
            .setUv(-1f, 1f)
            .normal(UP)
            .setLight(light)
        buffer.xyz(Vec3d(1, 0, 0).add(vec), camPos)
            .setColor(rCol, gCol, bCol, alpha)
            .setUv(-1f, -1.0f)
            .normal(UP)
            .setLight(light)
//        BufferRenderer.draw(buffer.end())
//        RenderSystem.depthMask(false)
//        RenderSystem.enableBlend()

        immediate.endBatch()

        /*  val matrixStack = MatrixStack()
          val rot = Quaternionf()
          facingCameraMode.setRotation(rot, camera, tickDelta)
          matrixStack.rotate(rot)
  //        matrixStack.rotate(Axis.Y_POSITIVE.rotationDegrees(180.0f))
          matrixStack.scale(-1.0f, 1.0f, -1.0f)
          matrixStack.translate(0f, 0f, 10.0f)

          matrixStack.translate(x - camera.pos.x, y - camera.pos.y, z - camera.pos.z)

          val color = 0xffffffff.toInt()
          val tessellator = Tessellator.getInstance()
          val pose = matrixStack.peek().model

          val light = 255 //  getBrightness(tickDelta)
          val immediate = MinecraftClient.getInstance().bufferBuilders.entityVertexConsumers
          MinecraftClient.getInstance().itemRenderer.renderItem(
              Items.HEAVY_CORE.defaultStack,
              ModelTransformationMode.NONE, light, OverlayTexture.DEFAULT_UV,
              matrixStack, immediate, world, 0
          )

          RenderSystem.disableBlend()
          RenderSystem.depthMask(true)
  //        RenderSystem.setShaderTexture(0, SpriteAtlasTexture.PARTICLE_ATLAS_TEXTURE)

          val builder = tessellator.begin(DrawMode.QUADS, VertexFormats.POSITION_COLOR)


          val normal = Vector3f(0f, 0f, 1f)
          val camPos = camera.pos

          builder.xyz(pose, Vec3d(0), camPos)
              .color(colorRed, colorGreen, colorBlue, colorAlpha)
  //            .uv0(1f, -1f)
              .uv2(light)
          builder.xyz(pose, Vec3d(0, 0, 1), camPos)
              .color(colorRed, colorGreen, colorBlue, colorAlpha)
  //            .uv0(1f, 1f)
              .uv2(light)
          builder.xyz(pose, Vec3d(1, 0, 1), camPos)
              .color(colorRed, colorGreen, colorBlue, colorAlpha)
  //            .uv0(-1f, 1f)
              .uv2(light)
          builder.xyz(pose, Vec3d(1, 0, 0), camPos)
              .color(colorRed, colorGreen, colorBlue, colorAlpha)
  //            .uv0(-1f, -1.0f)
              .uv2(light)

          builder.end()?.let { BufferRenderer.drawWithShader(it) }
          RenderSystem.depthMask(false)
          RenderSystem.enableBlend()*/
    }

    override fun renderRotatedQuad(
        vertexConsumer: VertexConsumer,
        camera: Camera,
        quaternionf: Quaternionf,
        tickDelta: Float
    ) = createCube(vertexConsumer, camera, quaternionf, tickDelta)

    fun createCube(vertexConsumer: VertexConsumer, camera: Camera, quaternionf: Quaternionf, tickDelta: Float) {
        val cameraPos = camera.position
        val posX = (Mth.lerp(tickDelta.toDouble(), this.xo, this.x) - cameraPos.x).toFloat()
        val posY = (Mth.lerp(tickDelta.toDouble(), this.yo, this.y) - cameraPos.y).toFloat()
        val posZ = (Mth.lerp(tickDelta.toDouble(), this.zo, this.z) - cameraPos.z).toFloat()

        /* south planes */
        this.renderRotatedQuad(vertexConsumer, quaternionf, posX, posY, posZ - quadSize, tickDelta)
        this.renderRotatedQuad(vertexConsumer, quaternionf, posX, posY, posZ + quadSize, tickDelta)

        // region : pain
        /* north planes */
        quaternionf.rotationY(Utils.rotate180)
        this.renderRotatedQuad(vertexConsumer, quaternionf, posX, posY, posZ + quadSize, tickDelta)
        this.renderRotatedQuad(vertexConsumer, quaternionf, posX, posY, posZ - quadSize, tickDelta)

        /* east planes */
        quaternionf.rotationY(Utils.rotate90)
        this.renderRotatedQuad(vertexConsumer, quaternionf, posX + quadSize, posY, posZ, tickDelta)
        this.renderRotatedQuad(vertexConsumer, quaternionf, posX - quadSize, posY, posZ, tickDelta)

        /* west planes */
        quaternionf.rotationY(Utils.rotate270)
        this.renderRotatedQuad(vertexConsumer, quaternionf, posX + quadSize, posY, posZ, tickDelta)
        this.renderRotatedQuad(vertexConsumer, quaternionf, posX - quadSize, posY, posZ, tickDelta)

//        /* down planes */
//        quaternionf.rotateX(Utils.rotate90)
//        vertexConsumer.drawFace(quaternionf, posX, posY + scale, posZ, tickDelta)
//        vertexConsumer.drawFace(quaternionf, posX, posY - scale, posZ, tickDelta)
//
//        /*   up planes */
//        quaternionf.rotateX(Utils.rotate180)
//        vertexConsumer.drawFace(quaternionf, posX, posY + scale, posZ, tickDelta)
//        vertexConsumer.drawFace(quaternionf, posX, posY - scale, posZ, tickDelta)

        // endregion

        /*  matrices.rotate(Axis.X_POSITIVE.rotationDegrees(45.0f))
          matrices.scale(0.05625f, 0.05625f, 0.05625f)
          val vertexConsumer =
              vertexConsumers.getBuffer(RenderLayer.getEntityCutout(this.getTexture(persistentProjectileEntity)))
          val entry = matrices.peek()

          for (u in 0..3) {
              matrices.rotate(Axis.X_POSITIVE.rotationDegrees(90.0f))
              vertex(entry, vertexConsumer, -2, -2, 0, 0.375f, 0.0f, 0, 1, 0, i)
              vertex(entry, vertexConsumer, 1, -2, 0, 0.375f, 0.375f, 0, 1, 0, i)
              vertex(entry, vertexConsumer, 1, 2, 0, 0.0f, 0.375f, 0, 1, 0, i)
              vertex(entry, vertexConsumer, -2, 2, 0, 0.0f, 0.0f, 0, 1, 0, i)
          }

          matrices.rotate(Axis.X_POSITIVE.rotationDegrees(45.0f))
          matrices.translate(0f, -0.5f, -0.5f)
          vertex(entry, vertexConsumer, -2, -1, -1, 0.375f, 0.0f, -1, 0, 0, i)
          vertex(entry, vertexConsumer, -2, -1, 2, 0.75f, 0.0f, -1, 0, 0, i)
          vertex(entry, vertexConsumer, -2, 2, 2, 0.75f, 0.375f, -1, 0, 0, i)
          vertex(entry, vertexConsumer, -2, 2, -1, 0.375f, 0.375f, -1, 0, 0, i)

          vertex(entry, vertexConsumer, -2, 2, -1, 0.375f, 0.0f, 1, 0, 0, i)
          vertex(entry, vertexConsumer, -2, 2, 2, 0.75f, 0.0f, 1, 0, 0, i)
          vertex(entry, vertexConsumer, -2, -1, 2, 0.75f, 0.375f, 1, 0, 0, i)
          vertex(entry, vertexConsumer, -2, -1, -1, 0.375f, 0.375f, 1, 0, 0, i)*/

    }

    fun VertexConsumer.drawFace(
        qRotation: Quaternionf, x: Float, y: Float, z: Float, delta: Float, rot2: Quaternionf = Quaternionf()
    ) {
        val light = getLightColor(delta)

        val pos = Vector3f(x, y, z).rotate(rot2).mul(quadSize)
        val normal = Vector3f(0f, 0f, 1f).rotate(rot2)

        this.drawVert(pos, 1f, -1.0f, normal, light)
        this.drawVert(pos, 1f, 1.0f, normal, light)
        this.drawVert(pos, -1f, 1.0f, normal, light)
        this.drawVert(pos, -1f, -1.0f, normal, light)
    }

    fun VertexConsumer.drawVert(vec3: Vector3f, u: Float, v: Float, normal: Vector3f, light: Int) = this.addVertex(vec3)
        .setColor(-1)
        .setUv(u, v)
        .setOverlay(OverlayTexture.NO_OVERLAY)
        .setLight(light)
        .setNormal(normal.x(), normal.y(), normal.z())
}