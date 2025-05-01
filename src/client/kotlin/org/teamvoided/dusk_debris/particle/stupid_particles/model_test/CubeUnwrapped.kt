package org.teamvoided.dusk_debris.particle.stupid_particles.model_test

import com.mojang.blaze3d.vertex.VertexConsumer
import net.minecraft.client.model.ModelPart
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.math.Direction
import org.joml.Quaternionf
import org.joml.Vector3f
import org.joml.Vector4f

class CubeUnwrapped(
    textureWidth: Int,
    textureHeight: Int,
    private val minX: Float,    //offset from source
    private val minY: Float,    //offset from source
    private val minZ: Float,    //offset from source
    sizeX: Float,               //size
    sizeY: Float,               //size
    sizeZ: Float,               //size
    dilationX: Float,           //dilation
    dilationY: Float,           //dilation
    dilationZ: Float,           //dilation
    mirror: Boolean,
    squishU: Float,
    squishV: Float,
    directions: Set<Direction>
) {
    constructor(
        textureWidth: Int,
        textureHeight: Int,
        sizeX: Float,
        sizeY: Float,
        sizeZ: Float,
        mirror: Boolean,
        squishU: Float,
        squishV: Float,
        directions: Set<Direction>
    ) : this(
        textureWidth,
        textureHeight,
        -sizeX / 2,
        -sizeY / 2,
        -sizeZ / 2,
        sizeX,
        sizeY,
        sizeZ,
        0f,
        0f,
        0f,
        mirror,
        squishU,
        squishV,
        directions
    )


    private val sides: Array<Quad?>
    private val maxX: Float = minX + sizeX
    private val maxY: Float = minY + sizeY
    private val maxZ: Float = minZ + sizeZ

    init {
        var minX = this.minX
        var minY = this.minY
        var minZ = this.minZ
        var maxX = this.maxX
        var maxY = this.maxY
        var maxZ = this.maxZ
        this.sides = arrayOfNulls(directions.size)
        minX -= dilationX
        minY -= dilationY
        minZ -= dilationZ
        maxX += dilationX
        maxY += dilationY
        maxZ += dilationZ
        if (mirror) {
            val temp = maxX
            maxX = minX
            minX = temp
        }

        val vxyz = ModelPart.Vertex(minX, minY, minZ, 0f, 0f)
        val vXyz = ModelPart.Vertex(maxX, minY, minZ, 0f, 8f)
        val vXYz = ModelPart.Vertex(maxX, maxY, minZ, 8f, 8f)
        val vxYz = ModelPart.Vertex(minX, maxY, minZ, 8f, 0f)
        val vxyZ = ModelPart.Vertex(minX, minY, maxZ, 0f, 0f)
        val vXyZ = ModelPart.Vertex(maxX, minY, maxZ, 0f, 8f)
        val vXYZ = ModelPart.Vertex(maxX, maxY, maxZ, 8f, 8f)
        val vxYZ = ModelPart.Vertex(minX, maxY, maxZ, 8f, 0f)
        val width = textureWidth.toFloat()
        val widthZ = textureWidth.toFloat() + sizeZ
        val widthZX = textureWidth.toFloat() + sizeZ + sizeX
        val widthZXX = textureWidth.toFloat() + sizeZ + sizeX + sizeX
        val widthZXZ = textureWidth.toFloat() + sizeZ + sizeX + sizeZ
        val widthZXZX = textureWidth.toFloat() + sizeZ + sizeX + sizeZ + sizeX
        val height = textureHeight.toFloat()
        val heightZ = textureHeight.toFloat() + sizeZ
        val heightZY = textureHeight.toFloat() + sizeZ + sizeY
        var side = 0
        if (directions.contains(Direction.DOWN)) {
            sides[side++] = Quad(
                arrayOf(vXyZ, vxyZ, vxyz, vXyz),
                widthZ,
                height,
                widthZX,
                heightZ,
                squishU,
                squishV,
                mirror,
                Direction.DOWN
            )
        }

        if (directions.contains(Direction.UP)) {
            sides[side++] = Quad(
                arrayOf(vXYz, vxYz, vxYZ, vXYZ),
                widthZX,
                heightZ,
                widthZXX,
                height,
                squishU,
                squishV,
                mirror,
                Direction.UP
            )
        }

        if (directions.contains(Direction.WEST)) {
            sides[side++] = Quad(
                arrayOf(vxyz, vxyZ, vxYZ, vxYz),
                width,
                heightZ,
                widthZ,
                heightZY,
                squishU,
                squishV,
                mirror,
                Direction.WEST
            )
        }

        if (directions.contains(Direction.NORTH)) {
            sides[side++] = Quad(
                arrayOf(vXyz, vxyz, vxYz, vXYz),
                widthZ,
                heightZ,
                widthZX,
                heightZY,
                squishU,
                squishV,
                mirror,
                Direction.NORTH
            )
        }

        if (directions.contains(Direction.EAST)) {
            sides[side++] = Quad(
                arrayOf(vXyZ, vXyz, vXYz, vXYZ),
                widthZX,
                heightZ,
                widthZXZ,
                heightZY,
                squishU,
                squishV,
                mirror,
                Direction.EAST
            )
        }

        if (directions.contains(Direction.SOUTH)) {
            sides[side] = Quad(
                arrayOf(vxyZ, vXyZ, vXYZ, vxYZ),
                widthZXZ,
                heightZ,
                widthZXZX,
                heightZY,
                squishU,
                squishV,
                mirror,
                Direction.SOUTH
            )
        }
    }

    fun renderCuboid(entry: MatrixStack.Entry, vertexConsumer: VertexConsumer, light: Int, overlay: Int, color: Int) {
        val matrix4f = entry.model
        val vector3f = Vector3f()
        this.sides.forEach { quad ->
            val vector3f2 = entry.transformNormal(quad!!.direction, vector3f)
            val normalX = vector3f2.x()
            val normalY = vector3f2.y()
            val normalZ = vector3f2.z()
            quad.vertices.forEach { vertex ->
                val j = vertex.pos.x() / 16f
                val k = vertex.pos.y() / 16f
                val l = vertex.pos.z() / 16f
                val vector3f3 = matrix4f.transformPosition(j, k, l, vector3f)
                vertexConsumer.addVertex(
                    vector3f3.x(),
                    vector3f3.y(),
                    vector3f3.z(),
                    color,
                    vertex.u,
                    vertex.v,
                    overlay,
                    light,
                    normalX,
                    normalY,
                    normalZ
                )
            }
        }
    }

    fun renderCube(
        vertexConsumer: VertexConsumer,
        quaternionf: Quaternionf,
        x: Float,
        y: Float,
        z: Float,
        color: Vector4f,
        brightness: Int,
        particleSize: Float
    ) {
        this.sides.forEach { quad ->
            quad!!.vertices.forEach { vertex ->
                val vX = vertex.pos.x() / 16f
                val vY = vertex.pos.y() / 16f
                val vZ = vertex.pos.z() / 16f
                val xyz = Vector3f(vertex.u, vertex.v, 0.0f)
                    .rotate(quaternionf)
                    .mul(particleSize)
                    .add(x, y, z).add(vX, vY, vZ)
                vertexConsumer
                    .xyz(xyz.x, xyz.y, xyz.z)
                    .uv0(vertex.u, vertex.v)
                    .color(color.x, color.y, color.z, color.w)
                    .uv2(brightness)
            }
        }
    }


    //may change wrap layout later
    class Quad(
        val vertices: Array<ModelPart.Vertex>,
        u1: Float,
        v1: Float,
        u2: Float,
        v2: Float,
        squishU: Float,
        squishV: Float,
        mirror: Boolean,
        direction: Direction
    ) {
        val direction: Vector3f

        init {
            val f = 0f // / squishU
            val g = 0f // / squishV
            vertices[0] = vertices[0].remap(u2 / squishU - f, v1 / squishV + g)
            vertices[1] = vertices[1].remap(u1 / squishU + f, v1 / squishV + g)
            vertices[2] = vertices[2].remap(u1 / squishU + f, v2 / squishV - g)
            vertices[3] = vertices[3].remap(u2 / squishU - f, v2 / squishV - g)
            if (mirror) {
                val i = vertices.size

                for (j in 0 until i / 2) {
                    val vertex = vertices[j]
                    vertices[j] = vertices[i - 1 - j]
                    vertices[i - 1 - j] = vertex
                }
            }

            this.direction = direction.unitVector
            if (mirror) {
                this.direction.mul(-1f, 1f, 1f)
            }
        }
    }
}