package org.teamvoided.dusk_debris.block

import net.minecraft.core.BlockPos
import net.minecraft.core.component.DataComponents
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.tags.ItemTags
import net.minecraft.util.Mth
import net.minecraft.world.InteractionHand
import net.minecraft.world.ItemInteractionResult
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.entity.player.Player
import net.minecraft.world.entity.projectile.AbstractArrow
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.ProjectileItem
import net.minecraft.world.item.component.ItemAttributeModifiers
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.Vec3
import net.minecraft.world.phys.shapes.CollisionContext
import net.minecraft.world.phys.shapes.EntityCollisionContext
import net.minecraft.world.phys.shapes.Shapes
import net.minecraft.world.phys.shapes.VoxelShape
import org.teamvoided.dusk_debris.init.DuskParticles
import org.teamvoided.dusk_debris.mixin.AbstractArrowAccessor
import org.teamvoided.dusk_debris.util.Utils.DEG_TO_RAD

class MushroomLaunchBlock(settings: Properties) : Block(settings) {
    override fun fallOn(world: Level, state: BlockState, pos: BlockPos, entity: Entity, fallDistance: Float) {
        if (entity.isSuppressingBounce && !entity.isShiftKeyDown) {
            super.fallOn(world, state, pos, entity, fallDistance)
        } else {
            entity.causeFallDamage(fallDistance, 0f, world.damageSources().fall())
            playBounce(entity.deltaMovement.y.toFloat(), world, entity.blockPosition())
        }
    }

    override fun updateEntityAfterFallOn(world: BlockGetter, entity: Entity) {
        if (entity.isSuppressingBounce && !entity.isShiftKeyDown) {
            super.updateEntityAfterFallOn(world, entity)
        } else {
            this.bounce(entity)
            if (entity is Player && !entity.mainHandItem.isEmpty && entity.getAttackStrengthScale(0f) >= 1) {
                entity.resetAttackStrengthTicker()
            }
        }
    }

    override fun getCollisionShape(
        state: BlockState,
        world: BlockGetter,
        pos: BlockPos,
        context: CollisionContext
    ): VoxelShape {
        val entity = (context as EntityCollisionContext).entity
        return if (entity != null && (entity is AbstractArrow)) {
            Shapes.empty()
        } else {
            Shapes.block()
//            COLLISION_SHAPE
        }
    }

    override fun entityInside(state: BlockState, world: Level, pos: BlockPos, entity: Entity) {
        if ((entity is AbstractArrow)) {
            entity.setOnGround(false)
            (entity as AbstractArrowAccessor).setInGround(false)
            entity.setDeltaMovement(0.0,1.0,0.0)
            entity.push(0.0, 1.0, 0.0)
        } else super.entityInside(state, world, pos, entity)
    }

    override fun useItemOn(
        stack: ItemStack,
        state: BlockState,
        world: Level,
        pos: BlockPos,
        entity: Player,
        hand: InteractionHand,
        hitResult: BlockHitResult
    ): ItemInteractionResult {
        if (stack.item !is BlockItem && stack.item !is ProjectileItem) {
            if (entity.deltaMovement.y < 0.1) {
                launch(stack, state, world, pos, entity)
                return ItemInteractionResult.SUCCESS
            }
            entity.resetAttackStrengthTicker()
        }
        return super.useItemOn(stack, state, world, pos, entity, hand, hitResult)
    }

    override fun attack(state: BlockState, world: Level, pos: BlockPos, player: Player) {
        val stack = player.mainHandItem
        if (stack.`is`(ItemTags.WEAPON_ENCHANTABLE) || stack.isEmpty) {
            launch(stack, state, world, pos, player)
            player.resetAttackStrengthTicker()
        }
        super.attack(state, world, pos, player)
    }

    fun launch(stack: ItemStack, state: BlockState, world: Level, pos: BlockPos, entity: Player) {
        val cooldown = entity.getAttackStrengthScale(0.5f)
        val mult: Float = if (cooldown > 0.9f) getAttackDamageWith(entity, stack).toFloat() * 0.2f
        else 0.001f
        playBounce(mult - 0.1f, world, pos)
        launchFromFacing(entity, -(mult + 0.5))
        fallOn(world, state, pos, entity, entity.fallDistance)
        if (mult > 3) {
            explodeBlock(world, pos)
        } else if (mult > 0.75) {
            launchParticles(world, pos, world.random.nextInt((mult * 50).toInt()), mult.toDouble())
        }
        entity.resetAttackStrengthTicker()
    }

    private fun getAttackDamageWith(entity: LivingEntity, weapon: ItemStack): Double {
        val attributeModifiersComponent = weapon.getOrDefault(
            DataComponents.ATTRIBUTE_MODIFIERS,
            ItemAttributeModifiers.EMPTY
        )
        val extra = //literally just the mace
            weapon.item.getAttackDamageBonus(entity, 0f, entity.damageSources().mobAttack(entity)) // .playerAttack(entity)
        return attributeModifiersComponent.compute(
            entity.getAttributeBaseValue(Attributes.ATTACK_DAMAGE),
            EquipmentSlot.MAINHAND
        ) + extra
    }

    private fun bounce(entity: Entity) {
        val vec3d = entity.deltaMovement
        if (vec3d.y < 0.0) {
            val mult = if (entity is LivingEntity) 1.0 else 0.8
            entity.setDeltaMovement(vec3d.x, -vec3d.y * mult, vec3d.z)
        }
    }

    private fun launchFromFacing(entity: Entity, mult: Double) {
        val pitchSin: Double = Mth.sin(entity.xRot * DEG_TO_RAD).toDouble()
        val pitchCos: Double = Mth.cos(entity.xRot * DEG_TO_RAD).toDouble()
        val yawSin: Double = Mth.sin(entity.yRot * DEG_TO_RAD).toDouble()
        val yawCos: Double = Mth.cos(entity.yRot * DEG_TO_RAD).toDouble()
        entity.push(
            -yawSin * pitchCos * mult,
            -pitchSin * mult,
            yawCos * pitchCos * mult
        )
    }

    fun explodeBlock(world: Level, pos: BlockPos) {
        val rand = world.random
        if (rand.nextInt(5) == 0) world.destroyBlock(pos, rand.nextInt(5) != 0)
        launchParticles(world, pos, rand.nextInt(50) + 50)
    }

    fun launchParticles(world: Level, pos: BlockPos, count: Int, multiplier: Double = 1.0) {
        val rand = world.random
        val centerBlock: Vec3 = pos.center
        repeat(count) {
            val velocity = Vec3(
                (rand.nextDouble() - rand.nextDouble()) * multiplier,
                (rand.nextDouble() - rand.nextDouble()) * multiplier,
                (rand.nextDouble() - rand.nextDouble()) * multiplier,
            )
            world.addParticle(
                DuskParticles.MUSHROOM_LAUNCH,
                centerBlock.x,
                centerBlock.y,
                centerBlock.z,
                velocity.x,
                velocity.y,
                velocity.z,
            )
        }
    }

    fun playBounce(pitch: Float, world: Level, pos: BlockPos) {
        world.playSound(
            null as Player?,
            pos,
            SoundEvents.SHROOMLIGHT_BREAK,
            SoundSource.BLOCKS,
            1.0f,
            pitch
        )
    }
    companion object{
        val COLLISION_SHAPE = box(4.0, 4.0, 4.0, 12.0, 12.0, 12.0)
    }
}
