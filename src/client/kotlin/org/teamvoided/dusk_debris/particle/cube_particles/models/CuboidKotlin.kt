package org.teamvoided.dusk_debris.particle.cube_particles.models

import com.mojang.blaze3d.vertex.VertexConsumer
import net.minecraft.client.model.ModelPart
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.math.Direction
import org.joml.Quaternionf
import org.joml.Vector3f
import org.joml.Vector4f

class CuboidKotlin(
    val minU: Float,
    val maxU: Float,
    val minV: Float,
    val maxV: Float,
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
    directions: Set<Direction>
) {
    constructor(
        minU: Float,
        maxU: Float,
        minV: Float,
        maxV: Float,
        sizeX: Float,
        sizeY: Float,
        sizeZ: Float,
        mirror: Boolean,
        directions: Set<Direction>
    ) : this(
        minU,
        maxU,
        minV,
        maxV,
        -sizeX / 2f,
        -sizeY / 2f,
        -sizeZ / 2f,
        sizeX,
        sizeY,
        sizeZ,
        0f,
        0f,
        0f,
        mirror,
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

        val vxyz = ModelPart.Vertex(minX, minY, minZ, 1f, -1f)
        val vXyz = ModelPart.Vertex(maxX, minY, minZ, -1f, 1f)
        val vXYz = ModelPart.Vertex(maxX, maxY, minZ, 1f, 1f)
        val vxYz = ModelPart.Vertex(minX, maxY, minZ, 1f, -1f)
        val vxyZ = ModelPart.Vertex(minX, minY, maxZ, -1f, -1f)
        val vXyZ = ModelPart.Vertex(maxX, minY, maxZ, -1f, 1f)
        val vXYZ = ModelPart.Vertex(maxX, maxY, maxZ, 1f, 1f)
        val vxYZ = ModelPart.Vertex(minX, maxY, maxZ, 1f, -1f)
        //val width =     (textureWidth.toFloat())
        //val widthZ =    (textureWidth.toFloat() + sizeZ)
        //val widthZX =   (textureWidth.toFloat() + sizeZ + sizeX)
        //val widthZXX =  (textureWidth.toFloat() + sizeZ + sizeX + sizeX)
        //val widthZXZ =  (textureWidth.toFloat() + sizeZ + sizeX + sizeZ)
        //val widthZXZX = (textureWidth.toFloat() + sizeZ + sizeX + sizeZ + sizeX)
        //val height =    (textureHeight.toFloat()) / 16f
        //val heightZ =   (textureHeight.toFloat() + sizeZ) / 16f
        //val heightZY =  (textureHeight.toFloat() + sizeZ + sizeY) / 16f
        var side = 0
        if (directions.contains(Direction.DOWN)) {
            sides[side++] = Quad(
                arrayOf(vXyZ, vxyZ, vxyz, vXyz),
                minU,//widthZ,
                maxU,//height,
                minV,//widthZX,
                maxV,//heightZ,
                mirror,
                Direction.DOWN
            )
        }

        if (directions.contains(Direction.UP)) {
            sides[side++] = Quad(
                arrayOf(vXYz, vxYz, vxYZ, vXYZ),
                minU,//widthZX,
                maxU,//heightZ,
                minV,//widthZXX,
                maxV,//height,
                mirror,
                Direction.UP
            )
        }

        if (directions.contains(Direction.WEST)) {
            sides[side++] = Quad(
                arrayOf(vxyz, vxyZ, vxYZ, vxYz),
                minU,// width,
                maxU,// heightZ,
                minV,// widthZ,
                maxV,// heightZY,
                mirror,
                Direction.WEST
            )
        }

        if (directions.contains(Direction.NORTH)) {
            sides[side++] = Quad(
                arrayOf(vXyz, vxyz, vxYz, vXYz),
                minU, //widthZ,
                maxU, //heightZ,
                minV, //widthZX,
                maxV, //heightZY,
                mirror,
                Direction.NORTH
            )
        }

        if (directions.contains(Direction.EAST)) {
            sides[side++] = Quad(
                arrayOf(vXyZ, vXyz, vXYz, vXYZ),
                minU, //widthZX,
                maxU, //heightZ,
                minV, //widthZXZ,
                maxV, //heightZY,
                mirror,
                Direction.EAST
            )
        }

        if (directions.contains(Direction.SOUTH)) {
            sides[side] = Quad(
                arrayOf(vxyZ, vXyZ, vXYZ, vxYZ),
                minU, //widthZXZ,
                maxU, //heightZ,
                minV, //widthZXZX,
                maxV, //heightZY,
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
            quad?.vertices?.forEach { vertex ->
                val vector3f = Vector3f(vertex.u, vertex.v, 0f)
                    .rotate(quaternionf)
                    .mul(10f)
                    .add(x, y, z)

                val vX = vertex.pos.x() / 16f
                val vY = vertex.pos.y() / 16f
                val vZ = vertex.pos.z() / 16f
                val xyz = Vector3f(vertex.u, vertex.v, 0f)
                    .rotate(quaternionf)
                    .mul(particleSize)
                    .add(x, y, z)
                    .add(vX, vY, vZ)
                vertexConsumer
                    .xyz(vector3f.x, vector3f.y, vector3f.z)
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
        mirror: Boolean,
        direction: Direction
    ) {
        val direction: Vector3f

        init {
            vertices[0] = vertices[0].remap(u2, v1)
            vertices[1] = vertices[1].remap(u1, v1)
            vertices[2] = vertices[2].remap(u1, v2)
            vertices[3] = vertices[3].remap(u2, v2)
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