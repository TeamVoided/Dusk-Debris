package org.teamvoided.dusk_debris.util

import net.minecraft.util.math.MathHelper
import net.minecraft.util.math.Vec3d

object SquareSampler {
    private const val BINOMIAL_RADIUS = 2
    private const val BINOMIAL_WIDTH = 6
    private val WEIGHTS = doubleArrayOf(0.0, 1.0, 4.0, 6.0, 4.0, 1.0, 0.0)

    fun samplePair(position: Vec3d, floatFetcher: FloatFetcher): Pair<Float, Float> {
        val i = MathHelper.floor(position.getX())
        val j = MathHelper.floor(position.getY())
        val k = MathHelper.floor(position.getZ())
        val d = position.getX() - i.toDouble()
        val e = position.getY() - j.toDouble()
        val f = position.getZ() - k.toDouble()
        var g = 0.0
        var pair = (0f to 0f)

        for (loopX in 0..5) {
            val h = MathHelper.lerp(d, WEIGHTS[loopX + 1], WEIGHTS[loopX])
            val thingX = i - 2 + loopX
            for (loopY in 0..5) {
                val o = MathHelper.lerp(e, WEIGHTS[loopY + 1], WEIGHTS[loopY])
                val thingY = j - 2 + loopY
                for (loopZ in 0..5) {
                    val r = MathHelper.lerp(f, WEIGHTS[loopZ + 1], WEIGHTS[loopZ])
                    val thingZ = k - 2 + loopZ
                    val t = h * o * r
                    g += t
                    pair = pair.add(floatFetcher.fetch(thingX, thingY).multiply(t))
                }
            }
        }

        pair = pair.multiply(1.0 / g)
        return pair
    }

    fun interface FloatFetcher {
        fun fetch(i: Int, j: Int): Pair<Float, Float>
    }

    fun Pair<Float, Float>.multiply(mult: Double): Pair<Float, Float> {
        return (this.first * mult.toFloat() to this.second * mult.toFloat())
    }

    fun Pair<Float, Float>.add(plus: Pair<Float, Float>): Pair<Float, Float> {
        return (this.first + plus.first to this.second + plus.second)
    }
}