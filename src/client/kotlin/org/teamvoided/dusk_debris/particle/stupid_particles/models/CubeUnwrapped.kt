package org.teamvoided.dusk_debris.particle.stupid_particles.models

import net.minecraft.util.math.Direction
import org.apache.commons.lang3.math.IEEE754rUtils.max
import org.teamvoided.dusk_debris.util.sendMessageIngame
import kotlin.math.abs

class CubeUnwrapped(
    minU: Float,
    maxU: Float,
    minV: Float,
    maxV: Float,
    sizeX: Float = 1f,
    sizeY: Float = 1f,
    sizeZ: Float = 1f,
    directions: Set<Direction> = Direction.entries.toSet()
) : CubeSimple(minU, maxU, minV, maxV, sizeX, sizeY, sizeZ, directions) {

    init {
        var side = 0

        var x = sizeX / 2
        var y = sizeY / 2
        var z = sizeZ / 2

        val corner1 = Vertex(x, y, z)
        val corner2 = Vertex(x, -y, z)
        val corner3 = Vertex(x, y, -z)
        val corner4 = Vertex(x, -y, -z)
        val corner5 = Vertex(-x, y, z)
        val corner6 = Vertex(-x, -y, z)
        val corner7 = Vertex(-x, y, -z)
        val corner8 = Vertex(-x, -y, -z)


        val sizeU = maxU - minU
        val sizeV = maxV - minV

        x = abs(sizeX)
        y = abs(sizeY)
        z = abs(sizeZ)
        val axisU = (z + x + z + x)
        val axisV = (z + y)
        x /= axisU
        y /= axisV
        z /= axisU

        val upUV = UV(
            minU + (sizeU * z),
            minU + (sizeU * (z + x)),
            minV,
            minV + (sizeV * y)
        )
        val downUV = upUV.u(
            minU + (sizeU * (z + x)),
            minU + (sizeU * (z + x + z))
        )
        val eastUV = UV(
            minU,
            minU + (sizeU * z),
            minV + (sizeV * y),
            maxV
        )
        val northUV = eastUV.u(
            minU + (sizeU * (z + x + z)),
            maxU
        )
        val westUV = eastUV.u(
            minU + (sizeU * (z + x)),
            minU + (sizeU * (z + x + z))
        )
        val southUV = eastUV.u(
            minU + (sizeU * z),
            minU + (sizeU * (z + x))
        )

        if (directions.contains(Direction.DOWN)) {
            sides[side++] = Quad(
                arrayOf(corner6, corner8, corner4, corner2),
                downUV,
                Direction.DOWN
            )
        }
        if (directions.contains(Direction.UP)) {
            sides[side++] = Quad(
                arrayOf(corner1, corner3, corner7, corner5),
                upUV,
                Direction.UP
            )
        }
        if (directions.contains(Direction.NORTH)) {
            sides[side++] = Quad(
                arrayOf(corner1, corner5, corner6, corner2),
                northUV,
                Direction.NORTH
            )
        }
        if (directions.contains(Direction.SOUTH)) {
            sides[side++] = Quad(
                arrayOf(corner7, corner3, corner4, corner8),
                southUV,
                Direction.SOUTH
            )
        }
        if (directions.contains(Direction.EAST)) {
            sides[side++] = Quad(
                arrayOf(corner3, corner1, corner2, corner4),
                eastUV,
                Direction.EAST
            )
        }
        if (directions.contains(Direction.WEST)) {
            sides[side] = Quad(
                arrayOf(corner5, corner7, corner8, corner6),
                westUV,
                Direction.WEST
            )
        }
    }
}