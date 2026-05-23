package org.teamvoided.dusk_debris.util

import com.mojang.blaze3d.vertex.VertexConsumer
import net.minecraft.world.phys.Vec3
import org.joml.Matrix4f

fun VertexConsumer.xyz(model: Matrix4f, vec: Vec3, camera: Vec3 = Vec3d(0.0)): VertexConsumer =
    this.addVertex(model, (vec.x - camera.x).toFloat(), (vec.y - camera.y).toFloat(), (vec.z - camera.z).toFloat())


fun VertexConsumer.xyz(vec: Vec3, camera: Vec3): VertexConsumer =
    this.addVertex((vec.x - camera.x).toFloat(), (vec.y - camera.y).toFloat(), (vec.z - camera.z).toFloat())

fun VertexConsumer.normal(vec: Vec3): VertexConsumer =
    this.setNormal(vec.x.toFloat(), vec.y.toFloat(), vec.z.toFloat())

fun VertexConsumer.uv0(u: Number, v: Number): VertexConsumer = this.setUv(u.toFloat(), v.toFloat())


val UP = Vec3d(0, 1, 0)

fun Vec3d(num: Number) = Vec3d(num, num, num)
fun Vec3d(x: Number, y: Number, z: Number) = Vec3(x.toDouble(), y.toDouble(), z.toDouble())