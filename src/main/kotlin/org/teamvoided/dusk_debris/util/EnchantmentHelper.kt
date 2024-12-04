package org.teamvoided.dusk_debris.util

import net.minecraft.component.EnchantmentEffectComponentTypes
import net.minecraft.enchantment.Enchantment
import net.minecraft.enchantment.effect.SpawnParticles
import net.minecraft.entity.EntityType
import net.minecraft.entity.EquipmentSlotGroup
import net.minecraft.fluid.Fluid
import net.minecraft.item.Item
import net.minecraft.loot.condition.AllOfLootCondition
import net.minecraft.loot.condition.EntityPropertiesLootCondition
import net.minecraft.loot.condition.InvertedLootCondition
import net.minecraft.loot.condition.RandomChanceLootCondition
import net.minecraft.loot.context.LootContext
import net.minecraft.particle.ParticleEffect
import net.minecraft.predicate.FluidPredicate
import net.minecraft.predicate.entity.EntityEquipmentPredicate
import net.minecraft.predicate.entity.EntityPredicate
import net.minecraft.predicate.entity.EntityTypePredicate
import net.minecraft.predicate.entity.LocationPredicate
import net.minecraft.predicate.item.ItemPredicate
import net.minecraft.registry.BootstrapContext
import net.minecraft.registry.HolderProvider
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.tag.ItemTags
import net.minecraft.registry.tag.TagKey
import net.minecraft.util.math.float_provider.ConstantFloatProvider
import org.teamvoided.dusk_debris.data.gen.providers.EnchantmentsProvider
import org.teamvoided.dusk_debris.data.gen.providers.EnchantmentsProvider.register
import org.teamvoided.dusk_debris.data.tags.DuskEnchantmentTags


fun Enchantment.curse(): Enchantment {
    EnchantmentsProvider.CURSES.add(this)
    return this
}

fun Enchantment.treasure(): Enchantment {
    EnchantmentsProvider.TREASURE.add(this)
    return this
}

fun Enchantment.particle(): Enchantment {
    EnchantmentsProvider.ENCHANTMENT_PARTICLE.add(this)
    return this
}

fun BootstrapContext<Enchantment>.particleEnchantment(
    registryKey: RegistryKey<Enchantment>,
    particle: ParticleEffect,
    chance: Float,
    tag: TagKey<Item> = ItemTags.ARMOR_ENCHANTABLE,
    horizontalPosition: SpawnParticles.PositionSource = SpawnParticles.inBoundingBox(),
    verticalPosition: SpawnParticles.PositionSource = SpawnParticles.inBoundingBox(),
    horizontalVelocity: SpawnParticles.VelocitySource = SpawnParticles.VelocitySource(0f, ConstantFloatProvider.ZERO),
    verticalVelocity: SpawnParticles.VelocitySource = SpawnParticles.VelocitySource(0f, ConstantFloatProvider.ZERO)
): Enchantment {
    val enchantment: HolderProvider<Enchantment> = this.getRegistryLookup(RegistryKeys.ENCHANTMENT)
    val item: HolderProvider<Item> = this.getRegistryLookup(RegistryKeys.ITEM)
    return this.register(
        registryKey,
        Enchantment.builder(
            Enchantment.createProperties(
                item.getTagOrThrow(tag),
                1,
                1,
                Enchantment.cost(1),
                Enchantment.cost(1),
                1,
                EquipmentSlotGroup.ANY
            )
        )
            .withExclusiveSet(enchantment.getTagOrThrow(DuskEnchantmentTags.PARTICLE_EXCLUSIVE_SET))
            .addEffect(
                EnchantmentEffectComponentTypes.TICK,
                SpawnParticles(
                    particle,
                    horizontalPosition,
                    verticalPosition,
                    horizontalVelocity,
                    verticalVelocity,
                    ConstantFloatProvider.create(1f)
                ),
                AllOfLootCondition.builder(
                    RandomChanceLootCondition.method_932(chance),
                    InvertedLootCondition.builder(
                        EntityPropertiesLootCondition.builder(
                            LootContext.EntityTarget.THIS,
                            EntityPredicate.Builder.create().equipment(
                                EntityEquipmentPredicate.Builder.create()
                                    .mainhand(itemIsInTag(ItemTags.ARMOR_ENCHANTABLE))
                            )
                        )
                    )
                )
            )
    ).particle().treasure()
}


fun entityIsInTag(tag: TagKey<EntityType<*>>): EntityPredicate {
    return EntityPredicate.Builder.create().type(EntityTypePredicate.createTagged(tag)).build()
}

fun HolderProvider<Fluid>.entityIsInFluidTag(tag: TagKey<Fluid>): EntityPredicate {
    return EntityPredicate.Builder.create().location(
        LocationPredicate.Builder.create().fluid(
            FluidPredicate.Builder.create().method_35222(this.getTagOrThrow(tag))
        )
    ).build()
}

fun itemIsInTag(tag: TagKey<Item>): ItemPredicate.Builder {
    return ItemPredicate.Builder.create().tag(tag)
}

