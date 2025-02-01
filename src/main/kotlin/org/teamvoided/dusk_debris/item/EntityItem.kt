package org.teamvoided.dusk_debris.item

import net.minecraft.advancement.criterion.Criteria
import net.minecraft.component.DataComponentTypes
import net.minecraft.component.type.NbtComponent
import net.minecraft.entity.EntityType
import net.minecraft.entity.SpawnReason
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvent
import net.minecraft.stat.Stats
import net.minecraft.util.Hand
import net.minecraft.util.TypedActionResult
import net.minecraft.util.hit.HitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.world.RaycastContext
import net.minecraft.world.World
import net.minecraft.world.WorldAccess
import net.minecraft.world.event.GameEvent
import org.teamvoided.dusk_debris.entity.Pickupable

class EntityItem(
    private val entityType: EntityType<*>,
    private val emptyingSound: SoundEvent,
    settings: Settings
) : Item(settings) {

    override fun use(world: World, user: PlayerEntity, hand: Hand): TypedActionResult<ItemStack> {
        val itemStack = user.getStackInHand(hand)
        val blockHitResult = raycast(world, user, RaycastContext.FluidHandling.SOURCE_ONLY)
        // RaycastContext.FluidHandling.NONE
        if (!(blockHitResult.type == HitResult.Type.MISS || blockHitResult.type != HitResult.Type.BLOCK)) {
            val blockPos = blockHitResult.blockPos
            val direction = blockHitResult.side
            val blockPos2 = blockPos.offset(direction)
            if (world.canPlayerModifyAt(user, blockPos) && user.canPlaceOn(blockPos2, direction, itemStack)) {
                if (user is ServerPlayerEntity) {
                    Criteria.PLACED_BLOCK.trigger(user, blockPos, itemStack)
                }
                onEmptied(user, world, itemStack, blockPos)
                playEmptyingSound(user, world, blockPos)
                user.incrementStat(Stats.USED.getOrCreateStat(this))
                itemStack.consume(1, user)
                return TypedActionResult.success(itemStack, world.isClient())
            }
        }
        return super.use(world, user, hand)
    }

    fun onEmptied(player: PlayerEntity, world: World, stack: ItemStack, pos: BlockPos) {
        if (world is ServerWorld) {
            this.spawnEntity(world, stack, pos)
            world.emitGameEvent(player, GameEvent.ENTITY_PLACE, pos)
        }
    }

    fun playEmptyingSound(player: PlayerEntity, world: WorldAccess, pos: BlockPos) {
        world.playSound(player, pos, this.emptyingSound, SoundCategory.NEUTRAL, 1.0f, 1.0f)
    }

    private fun spawnEntity(world: ServerWorld, stack: ItemStack, pos: BlockPos) {
        val entity =
            entityType.spawnFromItemStack(world, stack, null as PlayerEntity?, pos, SpawnReason.BUCKET, true, false)
        if (entity is Pickupable) {
            val nbtComponent = stack.getOrDefault(DataComponentTypes.BUCKET_ENTITY_DATA, NbtComponent.DEFAULT)
            entity.copyDataFromNbt(nbtComponent.copy())
            entity.placed = true
        }
    }
}
