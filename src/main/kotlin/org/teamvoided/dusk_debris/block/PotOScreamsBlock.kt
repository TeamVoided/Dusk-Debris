package org.teamvoided.dusk_debris.block

import net.minecraft.core.BlockPos
import net.minecraft.core.particles.ParticleTypes
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.util.Mth
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.ItemInteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.LevelReader
import net.minecraft.world.level.block.DecoratedPotBlock
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.BlockHitResult
import org.teamvoided.dusk_debris.init.DuskBlocks

class PotOScreamsBlock(settings: Properties) : DecoratedPotBlock(settings) {

    override fun useItemOn(
        stack: ItemStack,
        state: BlockState,
        world: Level,
        pos: BlockPos,
        entity: Player,
        hand: InteractionHand,
        hitResult: BlockHitResult
    ): ItemInteractionResult {
        scare(world, pos)
        return super.useItemOn(stack, state, world, pos, entity, hand, hitResult)
    }

    override fun useWithoutItem(
        state: BlockState,
        world: Level,
        pos: BlockPos,
        entity: Player,
        hitResult: BlockHitResult
    ): InteractionResult {
        scare(world, pos)
        return super.useWithoutItem(state, world, pos, entity, hitResult)
    }

    override fun playerWillDestroy(world: Level, pos: BlockPos, state: BlockState, player: Player): BlockState {
        scare(world, pos, 0.25, 0.1, 0.4, 3)
        return super.playerWillDestroy(world, pos, state, player)
    }

    private fun scare(
        world: Level,
        pos: BlockPos,
        offsetY: Double = 1.25,
        velXZ: Double = 0.03,
        velYMax: Double = 0.2,
        countMult: Int = 1
    ) {
        val random = world.getRandom()
        repeat(random.nextIntBetweenInclusive(2, 5 * countMult)) {
            world.playSound(null, pos, SoundEvents.ENDERMAN_SCREAM, SoundSource.BLOCKS, 1f, 0f)
        }
        repeat(random.nextIntBetweenInclusive(25 * countMult, 100 * countMult)) {
            world.addParticle(
                ParticleTypes.SOUL,
                pos.x + 0.5 + (Mth.nextDouble(random, -0.125, 0.125)),
                pos.y + offsetY,
                pos.z + 0.5 + (Mth.nextDouble(random, -0.125, 0.125)),
                Mth.nextDouble(random, -velXZ, velXZ),
                Mth.nextDouble(random, 0.01, velYMax),
                Mth.nextDouble(random, -velXZ, velXZ)
            )
        }
    }

    override fun getCloneItemStack(world: LevelReader, pos: BlockPos, state: BlockState): ItemStack {
        val stack = ItemStack(DuskBlocks.POT_O_SCREAMS)
        stack.applyComponents(super.getCloneItemStack(world, pos, state).components)
        return stack
    }
}