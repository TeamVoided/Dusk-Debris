package org.teamvoided.dusk_debris.world.gen.configured_feature

import com.mojang.serialization.Codec
import net.minecraft.block.Blocks
import net.minecraft.util.math.BlockPos
import net.minecraft.world.gen.feature.DefaultFeatureConfig
import net.minecraft.world.gen.feature.Feature
import net.minecraft.world.gen.feature.SingleStateFeatureConfig
import net.minecraft.world.gen.feature.util.FeatureContext
import java.lang.Integer.max
import kotlin.math.abs
import kotlin.math.absoluteValue

class RockFeature(codec: Codec<DefaultFeatureConfig>) : Feature<DefaultFeatureConfig>(codec) {
    override fun place(context: FeatureContext<DefaultFeatureConfig>): Boolean {
        var rockOrigin = context.origin
        val structureWorldAccess = context.world
        val random = context.random
        val singleStateFeatureConfig = context.config

        //for (i in 0..2) {
        val x = 2 //random.nextInt(4)
        val y = 1 //random.nextInt(4)
        val z = 2 //random.nextInt(4)
        //val radius = (x + y + z) / 3 + 0.5
        if (x == 0 && y == 0 && z == 0) {
            structureWorldAccess.setBlockState(rockOrigin, Blocks.COBBLESTONE.defaultState, 3)
        } else {
            BlockPos.iterate(BlockPos(-x, -y, -z), BlockPos(x, y, z)).forEach {
                val abs = abs(it.x) / x + abs(it.y) / y + abs(it.z) / z
                val sqr = it.x * it.x + it.y * it.y + it.z * it.z
                if (abs <= 1) {
                    structureWorldAccess.setBlockState(rockOrigin.add(it), Blocks.COBBLESTONE.defaultState, 3)
                } else {
                    structureWorldAccess.setBlockState(rockOrigin.add(it), Blocks.BLACK_STAINED_GLASS.defaultState, 3)
                }
                sqr.div(2)
            }
        }

        rockOrigin = rockOrigin.add(
            random.nextInt(x) - x / 2,
            -random.nextInt(y),
            random.nextInt(z) - z / 2
        )
        //}

        return true
    }
}