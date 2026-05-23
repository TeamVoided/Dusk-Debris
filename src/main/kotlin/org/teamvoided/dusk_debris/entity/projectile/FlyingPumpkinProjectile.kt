package org.teamvoided.dusk_debris.entity.projectile

import net.minecraft.core.particles.BlockParticleOption
import net.minecraft.core.particles.ParticleTypes
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvent
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.player.Player
import net.minecraft.world.entity.projectile.AbstractArrow
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.Vec3
import org.teamvoided.dusk_debris.entity.FlyingBlockItemEntity
import org.teamvoided.dusk_debris.init.DuskEntities
import org.teamvoided.dusk_debris.util.spawnParticles

open class FlyingPumpkinProjectile : AbstractArrow, FlyingBlockItemEntity {
    constructor(entityType: EntityType<out FlyingPumpkinProjectile>, world: Level) : super(entityType, world)
    constructor(owner: Player, world: Level, stack: ItemStack, weapon: ItemStack?) :
            super(DuskEntities.FLYING_PUMPKIN, owner, world, stack, weapon) {
        state = stack
    }

    override fun onHitBlock(blockHitResult: BlockHitResult) {
        super.onHitBlock(blockHitResult)
        if (!level().isClientSide) {
            particles(level())
            this.discard()
        }
    }

    override fun onClientRemoval() {
        level().playLocalSound(
            position().x,
            position().y,
            position().z,
            SoundEvents.SLIME_BLOCK_BREAK,
            SoundSource.NEUTRAL,
            1f,
            random.nextFloat() * 0.3f,
            false
        )
        super.onClientRemoval()
    }

    fun particles(world: Level) {
        if (!world.isClientSide) {
            val serverWorld = world as ServerLevel
            repeat(90) {
                serverWorld.spawnParticles(
                    BlockParticleOption(ParticleTypes.BLOCK, getState()),
                    position(),
                    Vec3(
                        (random.nextDouble() * 2.0 - 1.0),
                        (random.nextDouble() * 2.0 - 1.0),
                        (random.nextDouble() * 2.0 - 1.0)
                    ).normalize().scale(random.nextDouble() * 0.5)
                )
            }
        }
    }

    override fun tryPickup(player: Player?): Boolean {
        return false
    }

    override fun getDefaultHitGroundSoundEvent(): SoundEvent = SoundEvents.WOOD_BREAK
    override fun getDefaultPickupItem(): ItemStack =
        Items.HEAVY_CORE.defaultInstance// DnDBlocks.SMALL_CARVED_PUMPKIN.asItem().defaultStack

    override fun getState(): BlockState {
        return if (state.item is BlockItem) {
            (state.item as BlockItem).block.defaultBlockState()
        } else Blocks.HEAVY_CORE.defaultBlockState()//DnDBlocks.SMALL_CARVED_PUMPKIN.defaultState
    }

    override fun getItem(): ItemStack? = Items.HEAVY_CORE.defaultInstance

    companion object {
        var state: ItemStack = Items.HEAVY_CORE.defaultInstance //DnDBlocks.SMALL_CARVED_PUMPKIN.asItem().defaultStack
    }
}
