package org.teamvoided.dusk_debris.block

import net.minecraft.block.BlockState
import net.minecraft.block.MushroomBlock
import net.minecraft.entity.Entity
import net.minecraft.entity.effect.StatusEffect
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.particle.ParticleEffect
import net.minecraft.registry.Holder
import net.minecraft.server.world.ServerWorld
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.util.math.BlockPos
import net.minecraft.util.random.RandomGenerator
import net.minecraft.world.GameRules
import net.minecraft.world.World
import org.teamvoided.dusk_debris.block.NethershroomPlantBlock.Companion.explode
import org.teamvoided.dusk_debris.data.DuskEntityTypeTags
import org.teamvoided.dusk_debris.init.DuskSoundEvents
import kotlin.random.Random

class NethershroomBlock(
    val delay: Int,
    val particle: ParticleEffect,
    val statusEffect: Holder<StatusEffect>,
    val hasDoubleEffect: Boolean,
    settings: Settings
) : MushroomBlock(settings) {

    private fun tryExplode(world: World, state: BlockState, pos: BlockPos, entity: Entity, inverseChance: Int) {
        if (
            entity.isLiving &&
            !entity.isSneaking &&
            !entity.type.isIn(DuskEntityTypeTags.IS_NOT_AFFECTED_BY_NETHERSHROOM)
        ) {
            if (!world.isClient && world.random.nextInt(inverseChance) == 0 && state.isOf(this)) {
                if ((entity is PlayerEntity || world.gameRules.getBooleanValue(GameRules.DO_MOB_GRIEFING))) {
                    world.playSound(
                        null,
                        pos,
                        DuskSoundEvents.BLOCK_NETHERSHROOM_BLOCK_SQUISHED,
                        SoundCategory.BLOCKS,
                        1f,
                        0.9f + world.random.nextFloat() * 0.2f
                    )
                    world.scheduleBlockTick(pos, this, delay)
                }
            }
        }
    }

    override fun onSteppedOn(world: World, pos: BlockPos, state: BlockState, entity: Entity) {
        tryExplode(world, state, pos, entity, 500)
        super.onSteppedOn(world, pos, state, entity)
    }

    override fun onLandedUpon(world: World, state: BlockState, pos: BlockPos, entity: Entity, fallDistance: Float) {
        tryExplode(world, state, pos, entity, 10)
        super.onLandedUpon(world, state, pos, entity, fallDistance)
    }

    override fun scheduledTick(state: BlockState, world: ServerWorld, pos: BlockPos, random: RandomGenerator) {
        explode(world, pos, particle, statusEffect, hasDoubleEffect)
    }

    override fun randomDisplayTick(state: BlockState, world: World, pos: BlockPos, random: RandomGenerator) {
//        if (random.nextDouble() >= 0.8) {
//            val x = pos.x
//            val y = pos.y
//            val z = pos.z
//            val xOffset = x.toDouble() + random.nextDouble()
//            val yOffset = y.toDouble()
//            val zOffset = z.toDouble() + random.nextDouble()
//            world.addParticle(
//                ParticleTypes.FALLING_SPORE_BLOSSOM,
//                xOffset,
//                yOffset,
//                zOffset,
//                0.0,
//                0.0,
//                0.0
//            )
//
//
//            val mutable = BlockPos.Mutable()
//            val particleRange = 10
//            val blockState = world.getBlockState(mutable)
//            mutable[
//                x + MathHelper.nextInt(random, -particleRange, particleRange),
//                y + MathHelper.nextInt(random, -particleRange - (particleRange / 2), particleRange - (particleRange / 2))] =
//                z + MathHelper.nextInt(random, -particleRange, particleRange)
//            if (!blockState.isFullCube(world, mutable)) {
//                world.addParticle(
//                    ParticleTypes.SPORE_BLOSSOM_AIR,
//                    mutable.x.toDouble() + random.nextDouble(),
//                    mutable.y.toDouble() + random.nextDouble(),
//                    mutable.z.toDouble() + random.nextDouble(),
//                    0.0,
//                    0.0,
//                    0.0
//                )
//            }
//        }
    }
}