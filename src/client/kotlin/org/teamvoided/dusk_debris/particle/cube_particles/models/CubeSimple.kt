package org.teamvoided.dusk_debris.particle.cube_particles.models

import com.mojang.blaze3d.vertex.VertexConsumer
import net.minecraft.core.Direction
import org.joml.Quaternionf
import org.joml.Vector3f
import org.joml.Vector4f

open class CubeSimple(
    minU: Float,
    maxU: Float,
    minV: Float,
    maxV: Float,
    sizeX: Float,
    sizeY: Float,
    sizeZ: Float,
    directions: Set<Direction> = Direction.entries.toSet()
) {
    constructor(
        minU: Float,
        maxU: Float,
        minV: Float,
        maxV: Float,
        size: Float,
        directions: Set<Direction> = Direction.entries.toSet()
    ) : this(minU, maxU, minV, maxV, size, size, size, directions)

    val sides: Array<Quad?> = arrayOfNulls(directions.size)

    init {
        var side = 0

        val radX = sizeX / 2
        val radY = sizeY / 2
        val radZ = sizeZ / 2

        val corner1 = Vertex(radX, radY, radZ)
        val corner2 = Vertex(radX, -radY, radZ)
        val corner3 = Vertex(radX, radY, -radZ)
        val corner4 = Vertex(radX, -radY, -radZ)
        val corner5 = Vertex(-radX, radY, radZ)
        val corner6 = Vertex(-radX, -radY, radZ)
        val corner7 = Vertex(-radX, radY, -radZ)
        val corner8 = Vertex(-radX, -radY, -radZ)

        val defaultUV = UV(minU, maxU, minV, maxV)

        if (directions.contains(Direction.DOWN)) {
            sides[side++] = Quad(
                arrayOf(corner6, corner8, corner4, corner2),
                defaultUV,
                Direction.DOWN
            )
        }
        if (directions.contains(Direction.UP)) {
            sides[side++] = Quad(
                arrayOf(corner1, corner3, corner7, corner5),
                defaultUV,
                Direction.UP
            )
        }
        if (directions.contains(Direction.NORTH)) {
            sides[side++] = Quad(
                arrayOf(corner1, corner5, corner6, corner2),
                defaultUV,
                Direction.NORTH
            )
        }
        if (directions.contains(Direction.SOUTH)) {
            sides[side++] = Quad(
                arrayOf(corner7, corner3, corner4, corner8),
                defaultUV,
                Direction.SOUTH
            )
        }
        if (directions.contains(Direction.EAST)) {
            sides[side++] = Quad(
                arrayOf(corner3, corner1, corner2, corner4),
                defaultUV,
                Direction.EAST
            )
        }
        if (directions.contains(Direction.WEST)) {
            sides[side] = Quad(
                arrayOf(corner5, corner7, corner8, corner6),
                defaultUV,
                Direction.WEST
            )
        }
    }

    class Quad(
        val vertices: Array<Vertex>,
        minU: Float,
        maxU: Float,
        minV: Float,
        maxV: Float,
        direction: Direction
    ) {
        constructor(vertices: Array<Vertex>, uv: UV, direction: Direction) :
                this(vertices, uv.minU, uv.maxU, uv.minV, uv.maxV, direction)

        val direction: Vector3f = direction.step()

        init {
            vertices[0] = vertices[0].remap(maxU, minV)
            vertices[1] = vertices[1].remap(minU, minV)
            vertices[2] = vertices[2].remap(minU, maxV)
            vertices[3] = vertices[3].remap(maxU, maxV)
        }
    }

    data class UV(val minU: Float, val maxU: Float, val minV: Float, val maxV: Float) {
        fun u(minU: Float, maxU: Float): UV = UV(minU, maxU, minV, maxV)
        fun v(minV: Float, maxV: Float): UV = UV(minU, maxU, minV, maxV)
    }

    fun renderCube(
        vertexConsumer: VertexConsumer,
        quaternionf: Quaternionf,
        x: Float,
        y: Float,
        z: Float,
        color: Vector4f,
        brightness: Int,
        size: Float,
    ) = renderCube(vertexConsumer, quaternionf, x, y, z, color, brightness, Vector3f(size))

    fun renderCube(
        vertexConsumer: VertexConsumer,
        quaternionf: Quaternionf,
        x: Float,
        y: Float,
        z: Float,
        color: Vector4f,
        brightness: Int,
        size: Vector3f,
    ) {
        this.sides.forEach { quad ->
            quad?.vertices?.forEach { vertex ->
                val vector3f = Vector3f(vertex.pos)
                    .rotate(quaternionf)
                    .mul(size)
                    .add(x, y, z)
                vertexConsumer
                    .addVertex(vector3f.x, vector3f.y, vector3f.z)
                    .setUv(vertex.u, vertex.v)
                    .setColor(color.x, color.y, color.z, color.w)
                    .setLight(brightness)
            }
        }
    }

    class Vertex(val pos: Vector3f, val u: Float = 0f, val v: Float = 0f) {
        constructor(x: Float, y: Float, z: Float, u: Float = 0f, v: Float = 0f) :
                this(Vector3f(x, y, z), u, v)

        fun remap(u: Float = 0f, v: Float = 0f): Vertex {
            return Vertex(this.pos, u, v)
        }
    }
}