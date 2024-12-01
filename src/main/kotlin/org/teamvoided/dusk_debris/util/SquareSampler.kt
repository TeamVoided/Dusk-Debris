package org.teamvoided.dusk_debris.util

import net.minecraft.util.math.MathHelper
import net.minecraft.util.math.Vec3d
import org.joml.Vector2d

object SquareSampler {
    private const val BINOMIAL_RADIUS = 2
    private const val BINOMIAL_WIDTH = 6 - 1
    private val WEIGHTS = doubleArrayOf(0.0, 1.0, 4.0, 6.0, 4.0, 1.0, 0.0)

    fun sampleVector2d(position: Vec3d, floatFetcher: FloatFetcher): Vector2d {
        val i = MathHelper.floor(position.getX())
        val j = MathHelper.floor(position.getY())
        val k = MathHelper.floor(position.getZ())
        val d = position.getX() - i
        val e = position.getY() - j
        val f = position.getZ() - k
        var g = 0.0
        var pair = Vector2d()

        for (loopX in 0..BINOMIAL_WIDTH) {
            val h = MathHelper.lerp(d, WEIGHTS[loopX + 1], WEIGHTS[loopX])
            val thingX = i - BINOMIAL_RADIUS + loopX
            for (loopY in 0..BINOMIAL_WIDTH) {
                val o = MathHelper.lerp(e, WEIGHTS[loopY + 1], WEIGHTS[loopY])
                val thingY = j - BINOMIAL_RADIUS + loopY
                for (loopZ in 0..BINOMIAL_WIDTH) {
                    val r = MathHelper.lerp(f, WEIGHTS[loopZ + 1], WEIGHTS[loopZ])
                    val t = h * o * r
                    g += t
                    pair = pair.add(floatFetcher.fetch(thingX, thingY).mul(t))
                }
            }
        }

        pair = pair.mul(1.0 / g)
        return pair
    }

    fun interface FloatFetcher {
        fun fetch(i: Int, j: Int): Vector2d
    }

    fun Pair<Float, Float>.multiply(mult: Double): Pair<Float, Float> {
        return (this.first * mult.toFloat() to this.second * mult.toFloat())
    }

    fun Vector2d.add(add: Double): Vector2d {
        return this.add(add, add)
    }
}