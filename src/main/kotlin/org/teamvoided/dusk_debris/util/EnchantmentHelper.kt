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
import org.teamvoided.dusk_debris.data.DuskEnchantments
import org.teamvoided.dusk_debris.data.gen.providers.EnchantmentsProvider
import org.teamvoided.dusk_debris.data.gen.providers.EnchantmentsProvider.register
import org.teamvoided.dusk_debris.data.tags.DuskEnchantmentTags


fun RegistryKey<Enchantment>.curse(): RegistryKey<Enchantment> {
    DuskEnchantments.CURSES.add(this)
    return this
}

fun RegistryKey<Enchantment>.treasure(): RegistryKey<Enchantment> {
    DuskEnchantments.TREASURE.add(this)
    return this
}

fun RegistryKey<Enchantment>.particle(): RegistryKey<Enchantment> {
    DuskEnchantments.ENCHANTMENT_PARTICLE.add(this)
    return this
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

