package org.teamvoided.dusk_debris.util

import net.minecraft.util.Mth
import net.minecraft.world.phys.Vec3
import org.joml.Vector2d

object SquareSampler {
    private const val BINOMIAL_RADIUS = 2
    private const val BINOMIAL_WIDTH = 6 - 1
    private val WEIGHTS = doubleArrayOf(0.0, 1.0, 4.0, 6.0, 4.0, 1.0, 0.0)

    fun sampleVector2d(position: Vec3, floatFetcher: FloatFetcher): Vector2d {
        val i = Mth.floor(position.x())
        val j = Mth.floor(position.y())
        val k = Mth.floor(position.z())
        val d = position.x() - i
        val e = position.y() - j
        val f = position.z() - k
        var g = 0.0
        var pair = Vector2d()

        for (loopX in 0..BINOMIAL_WIDTH) {
            val h = Mth.lerp(d, WEIGHTS[loopX + 1], WEIGHTS[loopX])
            val thingX = i - BINOMIAL_RADIUS + loopX
            for (loopY in 0..BINOMIAL_WIDTH) {
                val o = Mth.lerp(e, WEIGHTS[loopY + 1], WEIGHTS[loopY])
                val thingY = j - BINOMIAL_RADIUS + loopY
                for (loopZ in 0..BINOMIAL_WIDTH) {
                    val r = Mth.lerp(f, WEIGHTS[loopZ + 1], WEIGHTS[loopZ])
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