package org.teamvoided.dusk_debris.block.big

import net.minecraft.core.BlockPos
import net.minecraft.util.RandomSource
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.LanternBlock
import net.minecraft.world.level.block.state.BlockState
import org.teamvoided.dusk_debris.particle.color.SpiralParticleEffect

class LanternWithSpiralBlock(private val color1: Int, private val color2: Int, settings: Properties) :
    LanternBlock(settings) {
    override fun animateTick(state: BlockState, world: Level, pos: BlockPos, random: RandomSource) {
        super.animateTick(state, world, pos, random)
        if (state.getValue(HANGING)) {
            world.addParticle(
                SpiralParticleEffect(color1, color2),
                pos.x + 0.5,
                pos.y + 0.8,
                pos.z + 0.5,
                1.0,
                -1.0,
                1.0
            )
        } else {
            world.addParticle(
                SpiralParticleEffect(color1, color2),
                pos.x + 0.5,
                pos.y + 0.6,
                pos.z + 0.5,
                1.0,
                1.0,
                1.0
            )
        }
    }
}