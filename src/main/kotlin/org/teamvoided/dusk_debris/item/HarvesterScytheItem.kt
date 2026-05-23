package org.teamvoided.dusk_debris.item

import net.minecraft.core.BlockPos
import net.minecraft.stats.Stats
import net.minecraft.tags.ItemTags
import net.minecraft.util.Mth
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.entity.EquipmentSlotGroup
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.ai.attributes.AttributeModifier
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.entity.player.Player
import net.minecraft.world.entity.projectile.Projectile
import net.minecraft.world.item.*
import net.minecraft.world.item.component.ItemAttributeModifiers
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.CropBlock
import net.minecraft.world.level.block.state.BlockState
import org.teamvoided.dusk_debris.DuskDebris.id
import org.teamvoided.dusk_debris.entity.projectile.FlyingPumpkinProjectile
import java.util.function.Predicate

class HarvesterScytheItem(toolMaterial: Tier, settings: Properties) : SwordItem(toolMaterial, settings) {
    constructor(settings: Properties) : this(DuskToolMaterials.HARVESTER_SCYTHE, settings)

    fun getHeldProjectiles(): Predicate<ItemStack> {
        return this.getProjectiles()
    }

    fun getProjectiles(): Predicate<ItemStack> = AMMO


    override fun canAttackBlock(state: BlockState, world: Level, pos: BlockPos, miner: Player): Boolean {
        return if (miner.isCreative)
            false
        else if (state.block is CropBlock)
            (state.block as CropBlock).isMaxAge(state)
        /*        else if (!state.getOrEmpty(Properties.AGE_1).isEmpty)
                    state.get(Properties.AGE_1) >= Properties.AGE_1_MAX
                else if (!state.getOrEmpty(Properties.AGE_2).isEmpty)
                    state.get(Properties.AGE_2) >= Properties.AGE_2_MAX
                else if (!state.getOrEmpty(Properties.AGE_3).isEmpty)
                    state.get(Properties.AGE_3) >= Properties.AGE_3_MAX
                else if (!state.getOrEmpty(Properties.AGE_4).isEmpty)
                    state.get(Properties.AGE_4) >= Properties.AGE_4_MAX
                else if (!state.getOrEmpty(Properties.AGE_5).isEmpty)
                    state.get(Properties.AGE_5) >= Properties.AGE_5_MAX
                else if (!state.getOrEmpty(Properties.AGE_7).isEmpty)
                    state.get(Properties.AGE_7) >= Properties.AGE_7_MAX
                else if (!state.getOrEmpty(Properties.AGE_15).isEmpty)
                    state.get(Properties.AGE_15) >= Properties.AGE_15_MAX
                else if (!state.getOrEmpty(Properties.AGE_25).isEmpty)
                    state.get(Properties.AGE_25) >= Properties.AGE_25_MAX*/
        else
            true
    }

    override fun use(world: Level, user: Player, hand: InteractionHand): InteractionResultHolder<ItemStack> {
        val itemStack = user.getItemInHand(hand)
        val pumpkin = getItemsFromInventory(itemStack, user)
        if (pumpkin != ItemStack.EMPTY) {
            if (!world.isClientSide) {
                val flyingPumpkin = FlyingPumpkinProjectile(user, world, pumpkin, itemStack)
                flyingPumpkin.setShootVelocity(user.xRot, user.yRot, 0.0f, 3f, 0.0f)
                world.addFreshEntity(flyingPumpkin)
            }
            user.awardStat(Stats.ITEM_USED.get(this))
            itemStack.hurtAndBreak(1, user, hand.toSlot())
            if (!user.isCreative) user.cooldowns.addCooldown(this, 20)
            return InteractionResultHolder.sidedSuccess(itemStack, world.isClientSide)
        }
        return InteractionResultHolder.pass(itemStack)
    }

    fun getItemsFromInventory(stack: ItemStack, entity: LivingEntity): ItemStack {
        if (entity is Player) {
            var predicate = (stack.item as HarvesterScytheItem).getHeldProjectiles()
            val itemStack = ProjectileWeaponItem.getHeldProjectile(entity, predicate)
            if (!itemStack.isEmpty) {
                return itemStack
            } else {
                predicate = (stack.item as HarvesterScytheItem).getProjectiles()

                for (i in 0 until entity.inventory.containerSize) {
                    val itemStack2: ItemStack = entity.inventory.getItem(i)
                    if (predicate.test(itemStack2)) {
                        return itemStack2
                    }
                }
                return if (entity.abilities.instabuild) defaultAmmo() else ItemStack.EMPTY
            }
        } else return defaultAmmo()
    }

    fun defaultAmmo(): ItemStack = Items.HEAVY_CORE.defaultInstance //ItemStack(DnDBlocks.SMALL_CARVED_PUMPKIN)

    companion object {
        val AMMO: Predicate<ItemStack> =
            Predicate { stack: ItemStack -> stack.`is`(ItemTags.ARROWS) }

        fun makeAttributes(): ItemAttributeModifiers =
            createAttributes(DuskToolMaterials.HARVESTER_SCYTHE, 3, -2.4f)
                .withModifierAdded(
                    Attributes.BLOCK_INTERACTION_RANGE, AttributeModifier(
                        id("base_block_range"), 3.0, AttributeModifier.Operation.ADD_VALUE
                    ), EquipmentSlotGroup.MAINHAND
                )
                .withModifierAdded(
                    Attributes.ENTITY_INTERACTION_RANGE, AttributeModifier(
                        id("base_entity_range"), 1.5, AttributeModifier.Operation.ADD_VALUE
                    ), EquipmentSlotGroup.MAINHAND
                )

        // TODO  replace with voidlib
        fun InteractionHand.toSlot() = if (this == InteractionHand.MAIN_HAND) EquipmentSlot.MAINHAND else EquipmentSlot.OFFHAND
        fun Projectile.setShootVelocity(pitch: Float, yaw: Float, roll: Float, speed: Float, modifierXYZ: Float) {
            val f = -Mth.sin(yaw * (Math.PI.toFloat() / 180)) * Mth.cos(pitch * (Math.PI.toFloat() / 180))
            val g = -Mth.sin((pitch + roll) * (Math.PI.toFloat() / 180))
            val h = Mth.cos(yaw * (Math.PI.toFloat() / 180)) * Mth.cos(pitch * (Math.PI.toFloat() / 180))
            this.shoot(f.toDouble(), g.toDouble(), h.toDouble(), speed, modifierXYZ)
        }
    }
}
