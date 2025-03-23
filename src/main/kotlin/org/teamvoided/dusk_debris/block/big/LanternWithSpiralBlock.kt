package org.teamvoided.dusk_debris.block.big

import net.minecraft.block.BlockState
import net.minecraft.block.LanternBlock
import net.minecraft.util.math.BlockPos
import net.minecraft.util.random.RandomGenerator
import net.minecraft.world.World
import org.teamvoided.dusk_debris.particle.SpiralParticleEffect

class LanternWithSpiralBlock(private val color1: Int, private val color2: Int, settings: Settings) :
    LanternBlock(settings) {
    override fun randomDisplayTick(state: BlockState, world: World, pos: BlockPos, random: RandomGenerator) {
        super.randomDisplayTick(state, world, pos, random)
        if (state.get(HANGING)) {
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