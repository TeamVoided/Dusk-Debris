package org.teamvoided.dusk_debris.block

import net.minecraft.core.BlockPos
import net.minecraft.tags.EntityTypeTags
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.effect.MobEffects
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.LeavesBlock
import net.minecraft.world.level.block.state.BlockState
import org.teamvoided.dusk_debris.data.tags.DuskItemTags

class PoisonLeavesBlock(settings: Properties) : LeavesBlock(settings) {
    override fun stepOn(world: Level, pos: BlockPos, state: BlockState, entity: Entity) {
        if (entity is LivingEntity && !entity.type.`is`(EntityTypeTags.UNDEAD)) applyEffect(entity)
        super.stepOn(world, pos, state, entity)
    }

    override fun attack(state: BlockState, world: Level, pos: BlockPos, player: Player) {
        if (player.mainHandItem.`is`(DuskItemTags.LEAVES_DONT_POISON)) applyEffect(player)
        super.attack(state, world, pos, player)
    }

    private fun applyEffect(entity: LivingEntity) =
        entity.addEffect(MobEffectInstance(MobEffects.POISON, 100))
}