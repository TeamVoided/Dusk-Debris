package org.teamvoided.dusk_debris.block

import net.minecraft.block.BlockState
import net.minecraft.block.LeavesBlock
import net.minecraft.entity.Entity
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.entity.effect.StatusEffects
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.registry.tag.EntityTypeTags
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import org.teamvoided.dusk_debris.data.tags.DuskItemTags

class PoisonLeavesBlock(settings: Settings) : LeavesBlock(settings) {
    override fun onSteppedOn(world: World, pos: BlockPos, state: BlockState, entity: Entity) {
        if (entity is LivingEntity && !entity.type.isIn(EntityTypeTags.UNDEAD)) applyEffect(entity)
        super.onSteppedOn(world, pos, state, entity)
    }

    override fun onBlockBreakStart(state: BlockState, world: World, pos: BlockPos, player: PlayerEntity) {
        if (player.mainHandStack.isIn(DuskItemTags.LEAVES_DONT_POISON)) applyEffect(player)
        super.onBlockBreakStart(state, world, pos, player)
    }

    private fun applyEffect(entity: LivingEntity) =
        entity.addStatusEffect(StatusEffectInstance(StatusEffects.POISON, 100))
}