package org.teamvoided.dusk_debris.item

import net.minecraft.advancements.CriteriaTriggers
import net.minecraft.core.BlockPos
import net.minecraft.core.component.DataComponents
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.sounds.SoundEvent
import net.minecraft.sounds.SoundSource
import net.minecraft.stats.Stats
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.MobSpawnType
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.component.CustomData
import net.minecraft.world.level.ClipContext
import net.minecraft.world.level.Level
import net.minecraft.world.level.LevelAccessor
import net.minecraft.world.level.gameevent.GameEvent
import net.minecraft.world.phys.HitResult
import org.teamvoided.dusk_debris.entity.Pickupable

class EntityItem(
    private val entityType: EntityType<*>,
    private val emptyingSound: SoundEvent,
    settings: Properties
) : Item(settings) {

    override fun use(world: Level, user: Player, hand: InteractionHand): InteractionResultHolder<ItemStack> {
        val itemStack = user.getItemInHand(hand)
        val blockHitResult = getPlayerPOVHitResult(world, user, ClipContext.Fluid.SOURCE_ONLY)
        // RaycastContext.FluidHandling.NONE
        if (!(blockHitResult.type == HitResult.Type.MISS || blockHitResult.type != HitResult.Type.BLOCK)) {
            val blockPos = blockHitResult.blockPos
            val direction = blockHitResult.direction
            val blockPos2 = blockPos.relative(direction)
            if (world.mayInteract(user, blockPos) && user.mayUseItemAt(blockPos2, direction, itemStack)) {
                if (user is ServerPlayer) {
                    CriteriaTriggers.PLACED_BLOCK.trigger(user, blockPos, itemStack)
                }
                onEmptied(user, world, itemStack, blockPos)
                playEmptyingSound(user, world, blockPos)
                user.awardStat(Stats.ITEM_USED.get(this))
                itemStack.consume(1, user)
                return InteractionResultHolder.sidedSuccess(itemStack, world.isClientSide)
            }
        }
        return super.use(world, user, hand)
    }

    fun onEmptied(player: Player, world: Level, stack: ItemStack, pos: BlockPos) {
        if (world is ServerLevel) {
            this.spawnEntity(world, stack, pos)
            world.gameEvent(player, GameEvent.ENTITY_PLACE, pos)
        }
    }

    fun playEmptyingSound(player: Player, world: LevelAccessor, pos: BlockPos) {
        world.playSound(player, pos, this.emptyingSound, SoundSource.NEUTRAL, 1.0f, 1.0f)
    }

    private fun spawnEntity(world: ServerLevel, stack: ItemStack, pos: BlockPos) {
        val entity =
            entityType.spawn(world, stack, null as Player?, pos, MobSpawnType.BUCKET, true, false)
        if (entity is Pickupable) {
            val nbtComponent = stack.getOrDefault(DataComponents.BUCKET_ENTITY_DATA, CustomData.EMPTY)
            entity.copyDataFromNbt(nbtComponent.copyTag())
            entity.placed = true
        }
    }
}
