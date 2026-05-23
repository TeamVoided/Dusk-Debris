package org.teamvoided.dusk_debris.world.gen.configured_feature

import com.mojang.serialization.Codec
import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.levelgen.LegacyRandomSource
import net.minecraft.world.level.levelgen.WorldgenRandom
import net.minecraft.world.level.levelgen.feature.Feature
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext
import net.minecraft.world.level.levelgen.synth.NormalNoise
import net.minecraft.world.phys.Vec3
import org.teamvoided.dusk_debris.DuskDebris
import org.teamvoided.dusk_debris.util.Utils.rotate360
import org.teamvoided.dusk_debris.world.gen.configured_feature.config.TorusFeatureConfig
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

class TorusFeature(codec: Codec<TorusFeatureConfig>) :
    Feature<TorusFeatureConfig>(codec) {
    override fun place(context: FeaturePlaceContext<TorusFeatureConfig>): Boolean {
        val origin = context.origin()
        val random = context.random()
        val world = context.level()
        val config = context.config()
        val chunkRandom = WorldgenRandom(LegacyRandomSource(world.seed))
        val dps = NormalNoise.create(chunkRandom, -2, *doubleArrayOf(1.0))

        val pitch: Float = config.pitch.sample(random) * rotate360
//        val roll: Float = config.roll[random] * rotate360
        val roll: Float = config.roll.sample(random) * rotate360

        val radiusToRingCenter: Int = config.radiusToRingCenter.sample(random) // random.nextInt(9) + 4
        val ringWidth: Int = config.ringWidth.sample(random) // random.nextInt(4) + 2
        val ringHeight: Int = config.ringHeight.sample(random) //random.nextInt(4) + 2
        val noiseMultiplier: Double =
            ((ringHeight + ringWidth) / 4.0) * config.noiseMultiplier.sample(random)

        val shapeRange = shapeDistanceFromCenter(radiusToRingCenter, ringWidth, ringHeight)
        val iterator: Iterator<BlockPos> = BlockPos.betweenClosed(
            origin.offset(shapeRange, shapeRange, shapeRange),
            origin.offset(-shapeRange, -shapeRange, -shapeRange),
        ).iterator()

        while (iterator.hasNext()) {
            val pos = iterator.next()
            if (world.dimensionType().logicalHeight > pos.y && world.getBlockState(pos).`is`(config.replaceable)) {
                val rotate = rotateShape(pos, origin, pitch, roll)
                if (shape(rotate, radiusToRingCenter, ringWidth, ringHeight, noiseMultiplier, dps)) {
                    this.setBlock(world, pos, config.blockstate.getState(random, pos))
                }
            }
        }

        if (DuskDebris.isDev()) this.setBlock(world, origin, Blocks.GLOWSTONE.defaultBlockState())

        return true
    }

    fun rotateShape(
        inputPos: BlockPos, origin: BlockPos,
        pitch: Float, roll: Float
    ): Vec3 {
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

        return Vec3(x, y, z)
    }

    fun shape(
        rotate: Vec3,
        radiusToRingCenter: Int, ringWidth: Int, ringHeight: Int,
        noiseMultiplier: Double,
        dps: NormalNoise
    ): Boolean {
        val rHrW = ringHeight.toDouble() / ringWidth

        val holeWidth = radiusToRingCenter - sqrt(rotate.x * rotate.x + rotate.z * rotate.z)
        var rHhW = (holeWidth * holeWidth) + ((rotate.y * rotate.y) / (rHrW * rHrW))

        if (noiseMultiplier != 0.0) rHhW += dps.getValue(rotate.x, rotate.y, rotate.z) * noiseMultiplier

        return (ringWidth / 2) * (ringWidth / 2) > rHhW
    }

    fun shapeDistanceFromCenter(radiusToRingCenter: Int, ringWidth: Int, ringHeight: Int): Int {
        val retur = radiusToRingCenter + (Math.max(ringWidth, ringHeight) / 2.0)
//        if (DuskDebris.isDev() && retur > 16) println("the following numbers: $radiusToRingCenter, $ringWidth, $ringHeight, may cause issues if the feature goes two chunks away from the placement")
        return retur.toInt()
    }
}