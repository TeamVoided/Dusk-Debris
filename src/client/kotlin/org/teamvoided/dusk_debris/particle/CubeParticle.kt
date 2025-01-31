package org.teamvoided.dusk_debris.particle

import com.mojang.blaze3d.vertex.VertexConsumer
import net.minecraft.client.particle.*
import net.minecraft.client.render.Camera
import net.minecraft.client.world.ClientWorld
import net.minecraft.util.math.MathHelper
import org.joml.Quaternionf
import org.teamvoided.dusk_debris.util.Utils

abstract class CubeParticle(
    world: ClientWorld,
    x: Double,
    y: Double,
    z: Double,
    velocityX: Double,
    velocityY: Double,
    velocityZ: Double
) : SpriteBillboardParticle(world, x, y, z, velocityX, velocityY, velocityZ) {
//    private var rotation: Vector3f = Vector3f()

    init {}

    override fun buildGeometry(vertexConsumer: VertexConsumer, camera: Camera, tickDelta: Float) =
        this.method_60373(vertexConsumer, camera, Quaternionf(), tickDelta)

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
        this.method_60374(vertexConsumer, quaternionf, posX, posY, posZ + scale, tickDelta)
        this.method_60374(vertexConsumer, quaternionf, posX, posY, posZ - scale, tickDelta)

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

        /* down planes */
        quaternionf.rotateX(Utils.rotate90)
        this.method_60374(vertexConsumer, quaternionf, posX, posY + scale, posZ, tickDelta)
        this.method_60374(vertexConsumer, quaternionf, posX, posY - scale, posZ, tickDelta)

        /*   up planes */
        quaternionf.rotateX(Utils.rotate180)
        this.method_60374(vertexConsumer, quaternionf, posX, posY + scale, posZ, tickDelta)
        this.method_60374(vertexConsumer, quaternionf, posX, posY - scale, posZ, tickDelta)
    }

//    fun createRotatableCube(
//        vertexConsumer: VertexConsumer,
//        camera: Camera,
//        quaternionf: Quaternionf,
//        tickDelta: Float
//    ) {
//        val rotation: Vector3f = Vector3f(Utils.rotate45, Utils.rotate45, Utils.rotate45)
//        //  in rotation
//        //      input x = down/up
//        //      input y = left/right
//        //      input z = rotation
//
//
//        val cameraPos = camera.pos
//        val posX = (MathHelper.lerp(tickDelta.toDouble(), this.prevPosX, this.x) - cameraPos.x).toFloat()
//        val posY = (MathHelper.lerp(tickDelta.toDouble(), this.prevPosY, this.y) - cameraPos.y).toFloat()
//        val posZ = (MathHelper.lerp(tickDelta.toDouble(), this.prevPosZ, this.z) - cameraPos.z).toFloat()
//
//
//        var scale = Vector3f(0f, 0f, scale)
//        if (rotation != Vector3f()) {
//            var x: Float = 0f
//            var y: Float = 0f
//            var z: Float = this.scale
//            var x2: Float
//            var y2: Float
//            var z2: Float
//
//            x2 = x * cos(rotation.z) - z * sin(rotation.z)
//            z2 = x * sin(rotation.z) + z * cos(rotation.z)
//            x = x2
//            z = z2
//
//            y2 = y * cos(rotation.y) - z * sin(rotation.y)
//            z2 = y * sin(rotation.y) + z * cos(rotation.y)
//            y = y2
//            z = z2
//
//            x2 = x * cos(rotation.x) - y * sin(rotation.x)
//            y2 = x * sin(rotation.x) + y * cos(rotation.x)
//            x = x2
//            y = y2
//
//            scale = Vector3f(x, y, z)
//        }
//
//        /* south planes */
//        quaternionf.rotationY(rotation.y).rotateX(rotation.x).rotateZ(rotation.z)
//        this.method_60374(vertexConsumer, quaternionf, posX + scale.x, posY + scale.y, posZ + scale.z, tickDelta)
//        this.method_60374(vertexConsumer, quaternionf, posX - scale.x, posY - scale.y, posZ - scale.z, tickDelta)
//
//
////        this.method_60374(vertexConsumer, quaternionf, posX + scale.x, posY, posZ, tickDelta)
////        this.method_60374(vertexConsumer, quaternionf, posX, posY + scale.y, posZ, tickDelta)
////        this.method_60374(vertexConsumer, quaternionf, posX, posY, posZ + scale.z, tickDelta)
//
//        /* north planes */
//        quaternionf.rotationY(rotation.y - Utils.rotate180).rotateX(-rotation.x).rotateZ(rotation.z)
//        this.method_60374(vertexConsumer, quaternionf, posX + scale.x, posY + scale.y, posZ + scale.z, tickDelta)
//        this.method_60374(vertexConsumer, quaternionf, posX - scale.x, posY - scale.y, posZ - scale.z, tickDelta)
//
//        /* east planes */
//        quaternionf.rotationY(rotation.y + Utils.rotate90).rotateX(rotation.x).rotateZ(rotation.z)
//        this.method_60374(vertexConsumer, quaternionf, posX + scale.z, posY, posZ, tickDelta)
//        this.method_60374(vertexConsumer, quaternionf, posX - scale.z, posY, posZ, tickDelta)
//
//        /* west planes */
//        quaternionf.rotationY(rotation.y + Utils.rotate270).rotateX(rotation.x).rotateZ(rotation.z)
//        this.method_60374(vertexConsumer, quaternionf, posX + scale.z, posY, posZ, tickDelta)
//        this.method_60374(vertexConsumer, quaternionf, posX - scale.z, posY, posZ, tickDelta)
//
//        /*   up planes */
//        quaternionf.rotationY(rotation.y).rotateX(rotation.x + Utils.rotate90).rotateZ(rotation.z)
//        this.method_60374(vertexConsumer, quaternionf, posX, posY + scale.z, posZ, tickDelta)
//        this.method_60374(vertexConsumer, quaternionf, posX, posY - scale.z, posZ, tickDelta)
//
//        /* down planes */
//        quaternionf.rotationY(rotation.y).rotateX(rotation.x + Utils.rotate270).rotateZ(rotation.z)
//        this.method_60374(vertexConsumer, quaternionf, posX, posY + scale.z, posZ, tickDelta)
//        this.method_60374(vertexConsumer, quaternionf, posX, posY - scale.z, posZ, tickDelta)
//    }

//    fun createCube(vertexConsumer: VertexConsumer, camera: Camera, quaternionf: Quaternionf, tickDelta: Float) {
//        val cameraPos = camera.pos
//        val posX = (MathHelper.lerp(tickDelta.toDouble(), this.prevPosX, this.x) - cameraPos.x).toFloat()
//        val posY = (MathHelper.lerp(tickDelta.toDouble(), this.prevPosY, this.y) - cameraPos.y).toFloat()
//        val posZ = (MathHelper.lerp(tickDelta.toDouble(), this.prevPosZ, this.z) - cameraPos.z).toFloat()
//        val quatSouth = quaternionf
//        val quatNorth = quatSouth.rotationY(Utils.rotate180)
//        val quatEast = quatNorth.rotationY(Utils.rotate90)
//        val quatWest = quatEast.rotationY(Utils.rotate270)
//        val quatDown = quatWest.rotateX(Utils.rotate90)
//        val quatUp = quatDown.rotateX(Utils.rotate180)
//
//        this.method_60374(vertexConsumer, quatSouth, posX, posY, posZ + scale, tickDelta)
//        this.method_60374(vertexConsumer, quatNorth, posX, posY, posZ + scale, tickDelta)
//        this.method_60374(vertexConsumer, quatEast, posX + scale, posY, posZ, tickDelta)
//        this.method_60374(vertexConsumer, quatWest, posX + scale, posY, posZ, tickDelta)
//        this.method_60374(vertexConsumer, quatDown, posX, posY + scale, posZ, tickDelta)
//        this.method_60374(vertexConsumer, quatUp, posX, posY + scale, posZ, tickDelta)
//
//        this.method_60374(vertexConsumer, quatSouth, posX, posY, posZ - scale, tickDelta)
//        this.method_60374(vertexConsumer, quatNorth, posX, posY, posZ - scale, tickDelta)
//        this.method_60374(vertexConsumer, quatEast, posX - scale, posY, posZ, tickDelta)
//        this.method_60374(vertexConsumer, quatWest, posX - scale, posY, posZ, tickDelta)
//        this.method_60374(vertexConsumer, quatDown, posX, posY - scale, posZ, tickDelta)
//        this.method_60374(vertexConsumer, quatUp, posX, posY - scale, posZ, tickDelta)
//    }
}