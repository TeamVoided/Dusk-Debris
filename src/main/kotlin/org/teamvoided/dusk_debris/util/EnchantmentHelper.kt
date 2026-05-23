package org.teamvoided.dusk_debris.util

import net.minecraft.advancements.critereon.*
import net.minecraft.core.HolderGetter
import net.minecraft.resources.ResourceKey
import net.minecraft.tags.TagKey
import net.minecraft.world.entity.EntityType
import net.minecraft.world.item.Item
import net.minecraft.world.item.enchantment.Enchantment
import net.minecraft.world.level.material.Fluid
import org.teamvoided.dusk_debris.data.DuskEnchantments


fun ResourceKey<Enchantment>.curse(): ResourceKey<Enchantment> {
    DuskEnchantments.CURSES.add(this)
    return this
}

fun ResourceKey<Enchantment>.treasure(): ResourceKey<Enchantment> {
    DuskEnchantments.TREASURE.add(this)
    return this
}

fun ResourceKey<Enchantment>.particle(): ResourceKey<Enchantment> {
    DuskEnchantments.ENCHANTMENT_PARTICLE.add(this)
    return this
}


fun entityIsInTag(tag: TagKey<EntityType<*>>): EntityPredicate {
    return EntityPredicate.Builder.entity().entityType(EntityTypePredicate.of(tag)).build()
}

fun HolderGetter<Fluid>.entityIsInFluidTag(tag: TagKey<Fluid>): EntityPredicate {
    return EntityPredicate.Builder.entity().located(
        LocationPredicate.Builder.location().setFluid(
            FluidPredicate.Builder.fluid().of(this.getOrThrow(tag))
        )
    ).build()
}

fun itemIsInTag(tag: TagKey<Item>): ItemPredicate.Builder {
    return ItemPredicate.Builder.item().of(tag)
}

