package org.teamvoided.dusk_debris.item

import net.minecraft.core.Direction
import net.minecraft.core.Position
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.stats.Stats
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.entity.player.Player
import net.minecraft.world.entity.projectile.Projectile
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.ProjectileItem
import net.minecraft.world.item.ProjectileItem.DispenseConfig
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.DispenserBlock
import net.minecraft.world.phys.Vec3
import org.teamvoided.dusk_debris.entity.ChillChargeEntity

class ChillChargeItem(settings: Properties) : Item(settings), ProjectileItem {
    override fun use(world: Level, user: Player, hand: InteractionHand): InteractionResultHolder<ItemStack> {
        if (!world.isClientSide) {
            val chillChargeEntity = ChillChargeEntity(user, world, user.position().x(), user.eyePosition.y(), user.position().z())
            chillChargeEntity.shootFromRotation(user, user.xRot, user.yRot, 0.0f, 1.5f, 1.0f)
            world.addFreshEntity(chillChargeEntity)
        }

        world.playSound(
            null, user.x, user.y, user.z,
            SoundEvents.WIND_CHARGE_THROW, SoundSource.NEUTRAL,
            0.5f, 0.4f / (world.getRandom().nextFloat() * 0.4f + 0.8f)
        )
        val itemStack = user.getItemInHand(hand)
        user.cooldowns.addCooldown(this, cooldown)
        user.awardStat(Stats.ITEM_USED.get(this))
        itemStack.consume(1, user)
        return InteractionResultHolder.sidedSuccess(itemStack, world.isClientSide)
    }

    override fun asProjectile(world: Level, pos: Position, stack: ItemStack, direction: Direction): Projectile {
        val randomGenerator = world.getRandom()
        val offsetX = randomGenerator.triangle(direction.stepX.toDouble(), 0.11485)
        val offsetY = randomGenerator.triangle(direction.stepY.toDouble(), 0.11485)
        val offsetZ = randomGenerator.triangle(direction.stepZ.toDouble(), 0.11485)
        val vec3d = Vec3(offsetX, offsetY, offsetZ)
        val chillChargeEntity = ChillChargeEntity(world, pos.x(), pos.y(), pos.z(), vec3d)
        chillChargeEntity.setDeltaMovement(vec3d)
        return chillChargeEntity
    }

    override fun shoot(p: Projectile, x: Double, y: Double, z: Double, s: Float, d: Float) = Unit
    override fun createDispenseConfig(): DispenseConfig = DispenseConfig.builder()
        .positionFunction { blockPointer, _ -> DispenserBlock.getDispensePosition(blockPointer, 1.0, Vec3.ZERO) }
        .uncertainty(6.6666665f)
        .power(1.0f)
        .overrideDispenseEvent(1051)
        .build()

    companion object {
        private const val cooldown = 10
    }
}