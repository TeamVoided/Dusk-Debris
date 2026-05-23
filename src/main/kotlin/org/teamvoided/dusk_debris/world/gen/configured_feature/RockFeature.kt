package org.teamvoided.dusk_debris.world.gen.configured_feature

import com.mojang.serialization.Codec
import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.levelgen.feature.Feature
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration
import kotlin.math.abs

class RockFeature(codec: Codec<NoneFeatureConfiguration>) : Feature<NoneFeatureConfiguration>(codec) {
    override fun place(context: FeaturePlaceContext<NoneFeatureConfiguration>): Boolean {
        var rockOrigin = context.origin()
        val structureWorldAccess = context.level()
        val random = context.random()
        val singleStateFeatureConfig = context.config()

        //for (i in 0..2) {
        val x = 2 //random.nextInt(4)
        val y = 1 //random.nextInt(4)
        val z = 2 //random.nextInt(4)
        //val radius = (x + y + z) / 3 + 0.5
        if (x == 0 && y == 0 && z == 0) {
            structureWorldAccess.setBlock(rockOrigin, Blocks.COBBLESTONE.defaultBlockState(), 3)
        } else {
            BlockPos.betweenClosed(BlockPos(-x, -y, -z), BlockPos(x, y, z)).forEach {
                val abs = abs(it.x) / x + abs(it.y) / y + abs(it.z) / z
                val sqr = it.x * it.x + it.y * it.y + it.z * it.z
                if (abs <= 1) {
                    structureWorldAccess.setBlock(rockOrigin.offset(it), Blocks.COBBLESTONE.defaultBlockState(), 3)
                } else {
                    structureWorldAccess.setBlock(rockOrigin.offset(it), Blocks.BLACK_STAINED_GLASS.defaultBlockState(), 3)
                }
                sqr.div(2)
            }
        }

        rockOrigin = rockOrigin.offset(
            random.nextInt(x) - x / 2,
            -random.nextInt(y),
            random.nextInt(z) - z / 2
        )
        //}

        return true
    }
}