package org.teamvoided.dusk_debris.world.gen.configured_feature

import com.mojang.serialization.Codec
import net.minecraft.block.Blocks
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3d
import net.minecraft.util.math.noise.DoublePerlinNoiseSampler
import net.minecraft.util.random.LegacySimpleRandom
import net.minecraft.world.gen.ChunkRandom
import net.minecraft.world.gen.feature.Feature
import net.minecraft.world.gen.feature.util.FeatureContext
import org.teamvoided.dusk_debris.DuskDebris
import org.teamvoided.dusk_debris.util.Utils.rotate360
import org.teamvoided.dusk_debris.world.gen.configured_feature.config.TorusFeatureConfig
import kotlin.math.*

class TorusFeature(codec: Codec<TorusFeatureConfig>) :
    Feature<TorusFeatureConfig>(codec) {
    override fun place(context: FeatureContext<TorusFeatureConfig>): Boolean {
        val origin = context.origin
        val random = context.random
        val world = context.world
        val config = context.config
        val chunkRandom = ChunkRandom(LegacySimpleRandom(world.seed))
        val dps = DoublePerlinNoiseSampler.create(chunkRandom, -2, *doubleArrayOf(1.0))

        val pitch: Float = config.pitch[random] * rotate360
//        val roll: Float = config.roll[random] * rotate360
        val roll: Float = config.roll[random] * rotate360

        val radiusToRingCenter: Int = config.radiusToRingCenter[random] // random.nextInt(9) + 4
        val ringWidth: Int = config.ringWidth[random] // random.nextInt(4) + 2
        val ringHeight: Int = config.ringHeight[random] //random.nextInt(4) + 2
        val noiseMultiplier: Double =
            ((ringHeight + ringWidth) / 4.0) * config.noiseMultiplier[random]

        val shapeRange = shapeDistanceFromCenter(radiusToRingCenter, ringWidth, ringHeight)
        val iterator: Iterator<BlockPos> = BlockPos.iterate(
            origin.add(shapeRange, shapeRange, shapeRange),
            origin.add(-shapeRange, -shapeRange, -shapeRange),
        ).iterator()

        while (iterator.hasNext()) {
            val pos = iterator.next()
            if (world.dimension.logicalHeight > pos.y && world.getBlockState(pos).isIn(config.replaceable)) {
                val rotate = rotateShape(pos, origin, pitch, roll)
                if (shape(rotate, radiusToRingCenter, ringWidth, ringHeight, noiseMultiplier, dps)) {
                    this.setBlockState(world, pos, config.blockstate.getBlockState(random, pos))
                }
            }
        }

        if (DuskDebris.isDev()) this.setBlockState(world, origin, Blocks.GLOWSTONE.defaultState)

        return true
    }

    fun rotateShape(
        inputPos: BlockPos, origin: BlockPos,
        pitch: Float, roll: Float
    ): Vec3d {
        var x2: Double
        var y2: Double
        var z2: Double
        var x = (origin.x - inputPos.x).toDouble()
        var y = (origin.y - inputPos.y).toDouble()
        var z = (origin.z - inputPos.z).toDouble()

        x2 = x * cos(pitch) - y * sin(pitch)
        y2 = x * sin(pitch) + y * cos(pitch)
        x = x2
        y = y2

        y2 = y * cos(roll) - z * sin(roll)
        z2 = y * sin(roll) + z * cos(roll)
        y = y2
        z = z2

//        x2 = x * cos(yaw) - z * sin(yaw)
//        z2 = x * sin(yaw) + z * cos(yaw)
//        x = x2
//        z = z2

        return Vec3d(x, y, z)
    }

    fun shape(
        rotate: Vec3d,
        radiusToRingCenter: Int, ringWidth: Int, ringHeight: Int,
        noiseMultiplier: Double,
        dps: DoublePerlinNoiseSampler
    ): Boolean {
        val rHrW = ringHeight.toDouble() / ringWidth

        val holeWidth = radiusToRingCenter - sqrt(rotate.x * rotate.x + rotate.z * rotate.z)
        var rHhW = (holeWidth * holeWidth) + ((rotate.y * rotate.y) / (rHrW * rHrW))

        if (noiseMultiplier != 0.0) rHhW += dps.sample(rotate.x, rotate.y, rotate.z) * noiseMultiplier

        return (ringWidth / 2) * (ringWidth / 2) > rHhW
    }

    fun shapeDistanceFromCenter(radiusToRingCenter: Int, ringWidth: Int, ringHeight: Int): Int {
        val retur = radiusToRingCenter + (Math.max(ringWidth, ringHeight) / 2.0)
//        if (DuskDebris.isDev() && retur > 16) println("the following numbers: $radiusToRingCenter, $ringWidth, $ringHeight, may cause issues if the feature goes two chunks away from the placement")
        return retur.toInt()
    }
}