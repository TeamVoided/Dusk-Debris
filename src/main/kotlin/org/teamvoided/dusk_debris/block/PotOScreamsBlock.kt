package org.teamvoided.dusk_debris.block

import net.minecraft.block.BlockState
import net.minecraft.block.DecoratedPotBlock
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.particle.ParticleTypes
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.ItemInteractionResult
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.MathHelper
import net.minecraft.world.World
import net.minecraft.world.WorldView
import org.teamvoided.dusks_and_dungeons.init.DnDBlocks

class PotOScreamsBlock(settings: Settings) : DecoratedPotBlock(settings) {

    override fun onInteract(
        stack: ItemStack,
        state: BlockState,
        world: World,
        pos: BlockPos,
        entity: PlayerEntity,
        hand: Hand,
        hitResult: BlockHitResult
    ): ItemInteractionResult {
        scare(world, pos)
        return super.onInteract(stack, state, world, pos, entity, hand, hitResult)
    }

    override fun onUse(
        state: BlockState,
        world: World,
        pos: BlockPos,
        entity: PlayerEntity,
        hitResult: BlockHitResult
    ): ActionResult {
        scare(world, pos)
        return super.onUse(state, world, pos, entity, hitResult)
    }

    override fun onBreak(world: World, pos: BlockPos, state: BlockState, player: PlayerEntity): BlockState {
        scare(world, pos, 0.25, 0.1, 0.4, 3)
        return super.onBreak(world, pos, state, player)
    }

    private fun scare(
        world: World,
        pos: BlockPos,
        offsetY: Double = 1.25,
        velXZ: Double = 0.03,
        velYMax: Double = 0.2,
        countMult: Int = 1
    ) {
        val random = world.getRandom()
        repeat(random.rangeInclusive(2, 5 * countMult)) {
            world.playSound(null, pos, SoundEvents.ENTITY_ENDERMAN_SCREAM, SoundCategory.BLOCKS, 1f, 0f)
        }
        repeat(random.rangeInclusive(25 * countMult, 100 * countMult)) {
            world.addParticle(
                ParticleTypes.SOUL,
                pos.x + 0.5 + (MathHelper.nextDouble(random, -0.125, 0.125)),
                pos.y + offsetY,
                pos.z + 0.5 + (MathHelper.nextDouble(random, -0.125, 0.125)),
                MathHelper.nextDouble(random, -velXZ, velXZ),
                MathHelper.nextDouble(random, 0.01, velYMax),
                MathHelper.nextDouble(random, -velXZ, velXZ)
            )
        }
    }

    override fun getPickStack(world: WorldView, pos: BlockPos, state: BlockState): ItemStack {
        val stack = ItemStack(DnDBlocks.POT_O_SCREAMS)
        stack.applyComponents(super.getPickStack(world, pos, state).components)
        return stack
    }
}