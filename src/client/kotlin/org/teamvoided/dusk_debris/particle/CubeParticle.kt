package org.teamvoided.dusk_debris.particle

import com.mojang.blaze3d.vertex.*
import net.minecraft.client.MinecraftClient
import net.minecraft.client.particle.SpriteBillboardParticle
import net.minecraft.client.render.Camera
import net.minecraft.client.render.OverlayTexture
import net.minecraft.client.render.RenderLayer
import net.minecraft.client.world.ClientWorld
import net.minecraft.util.math.MathHelper
import org.joml.Quaternionf
import org.joml.Vector3f
import org.teamvoided.dusk_debris.util.*

abstract class CubeParticle(
    world: ClientWorld, x: Double, y: Double, z: Double,
    velocityX: Double, velocityY: Double, velocityZ: Double
) : SpriteBillboardParticle(world, x, y, z, velocityX, velocityY, velocityZ) {
//    private var rotation: Vector3f = Vector3f()

    override fun buildGeometry(vertexConsumer: VertexConsumer, camera: Camera, tickDelta: Float) {

//        this.method_60373(vertexConsumer, camera, Quaternionf(), tickDelta)


        val h = MathHelper.lerp(tickDelta.toDouble(), prevPosX, x)
        val i = MathHelper.lerp(tickDelta.toDouble(), prevPosY, y)
        val j = MathHelper.lerp(tickDelta.toDouble(), prevPosZ, z)
        val vec = Vec3d(h, i, j)
        val immediate = MinecraftClient.getInstance().bufferBuilders.entityVertexConsumers
        val camPos = camera.pos
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
        val buffer = immediate.getBuffer(RenderLayer.getTranslucentMovingBlock())

        buffer.xyz(Vec3d(0f).add(vec), camPos)
            .color(colorRed, colorGreen, colorBlue, colorAlpha)
            .uv0(1f, -1f)
            .normal(UP)
            .uv2(light)
        buffer.xyz(Vec3d(0, 0, 1).add(vec), camPos)
            .color(colorRed, colorGreen, colorBlue, colorAlpha)
            .uv0(1f, 1f)
            .normal(UP)
            .uv2(light)
        buffer.xyz(Vec3d(1, 0, 1).add(vec), camPos)
            .color(colorRed, colorGreen, colorBlue, colorAlpha)
            .uv0(-1f, 1f)
            .normal(UP)
            .uv2(light)
        buffer.xyz(Vec3d(1, 0, 0).add(vec), camPos)
            .color(colorRed, colorGreen, colorBlue, colorAlpha)
            .uv0(-1f, -1.0f)
            .normal(UP)
            .uv2(light)
//        BufferRenderer.draw(buffer.end())
//        RenderSystem.depthMask(false)
//        RenderSystem.enableBlend()

        immediate.draw()

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

    override fun method_60373(
        vertexConsumer: VertexConsumer,
        camera: Camera,
        quaternionf: Quaternionf,
        tickDelta: Float
    ) = createCube(vertexConsumer, camera, quaternionf, tickDelta)

    fun createCube(vertexConsumer: VertexConsumer, camera: Camera, quaternionf: Quaternionf, tickDelta: Float) {
        val cameraPos = camera.pos
        val posX = (MathHelper.lerp(tickDelta.toDouble(), this.prevPosX, this.x) - cameraPos.x).toFloat()
        val posY = (MathHelper.lerp(tickDelta.toDouble(), this.prevPosY, this.y) - cameraPos.y).toFloat()
        val posZ = (MathHelper.lerp(tickDelta.toDouble(), this.prevPosZ, this.z) - cameraPos.z).toFloat()

        /* south planes */
        this.method_60374(vertexConsumer, quaternionf, posX, posY, posZ - scale, tickDelta)
        this.method_60374(vertexConsumer, quaternionf, posX, posY, posZ + scale, tickDelta)

        // region : pain
        /* north planes */
        quaternionf.rotationY(Utils.rotate180)
        this.method_60374(vertexConsumer, quaternionf, posX, posY, posZ + scale, tickDelta)
        this.method_60374(vertexConsumer, quaternionf, posX, posY, posZ - scale, tickDelta)

        /* east planes */
        quaternionf.rotationY(Utils.rotate90)
        this.method_60374(vertexConsumer, quaternionf, posX + scale, posY, posZ, tickDelta)
        this.method_60374(vertexConsumer, quaternionf, posX - scale, posY, posZ, tickDelta)

        /* west planes */
        quaternionf.rotationY(Utils.rotate270)
        this.method_60374(vertexConsumer, quaternionf, posX + scale, posY, posZ, tickDelta)
        this.method_60374(vertexConsumer, quaternionf, posX - scale, posY, posZ, tickDelta)

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
        val light = getBrightness(delta)

        val pos = Vector3f(x, y, z).rotate(rot2).mul(scale)
        val normal = Vector3f(0f, 0f, 1f).rotate(rot2)

        this.drawVert(pos, 1f, -1.0f, normal, light)
        this.drawVert(pos, 1f, 1.0f, normal, light)
        this.drawVert(pos, -1f, 1.0f, normal, light)
        this.drawVert(pos, -1f, -1.0f, normal, light)
    }

    fun VertexConsumer.drawVert(vec3: Vector3f, u: Float, v: Float, normal: Vector3f, light: Int) = this.xyz(vec3)
        .color(-1)
        .uv0(u, v)
        .uv1(OverlayTexture.DEFAULT_UV)
        .uv2(light)
        .normal(normal.x(), normal.y(), normal.z())
}