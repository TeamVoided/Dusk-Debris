package org.teamvoided.dusk_debris.util.extra

import net.minecraft.core.BlockPos
import net.minecraft.util.Mth
import net.minecraft.util.RandomSource
import net.minecraft.world.level.WorldGenLevel
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration
import net.minecraft.world.level.levelgen.feature.stateproviders.SimpleStateProvider
import org.teamvoided.dusk_debris.util.Utils

class Tree {
    fun branch(
        config: NoneFeatureConfiguration,
        world: WorldGenLevel,
        random: RandomSource,
        origin: BlockPos,
        width: Int,
        height: Int
    ) {
        val logBlock = SimpleStateProvider.simple(Blocks.WHITE_STAINED_GLASS.defaultBlockState())
        var posY = height - width - random.nextInt(width)
        val height3 = height / 3
        println("$posY, $height3")
        while (posY > height3) {
            val rotation = random.nextFloat() * Utils.rotate360
//            val axis = ((rotation - rotate45) * (180 / pi)) / 90f
//            val axis = if ((rotation + rotate45) % pi > rotate90) {
//                Direction.Axis.Z
//            } else {
//                Direction.Axis.X
//            }

            var rotX = 0
            var rotZ = 0
            val angleY = 1 + random.nextInt(4)
            val the = width / 2
            for (offset in the..the + (0.4 * (height - posY)).toInt()) {
                rotX = (1.5f + Mth.cos(rotation) * offset).toInt()
                rotZ = (1.5f + Mth.sin(rotation) * offset).toInt()
                val blockPos = origin.offset(rotX, posY - offset / angleY, rotZ)
//                this.placeTrunkBlock(world, replacer, random, blockPos, config)
//                this.setBlockState(
//                    world, blockPos,
//                    logBlock.getBlockState(random, blockPos)
////                        .withIfExists(Properties.AXIS, axis)
//                )
            }
            posY -= 1 + random.nextInt(2)
            println("$posY, $height3")
        }
        println(" ")
    }
}