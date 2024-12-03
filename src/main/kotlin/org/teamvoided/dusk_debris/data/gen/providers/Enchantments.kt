package org.teamvoided.dusk_debris.data.gen.providers

import net.minecraft.block.Block
import net.minecraft.block.Blocks
import net.minecraft.component.EnchantmentEffectComponentTypes
import net.minecraft.enchantment.Enchantment
import net.minecraft.enchantment.Enchantments
import net.minecraft.enchantment.LevelBasedValue
import net.minecraft.enchantment.effect.*
import net.minecraft.enchantment.effect.SpawnParticles.*
import net.minecraft.entity.EntityType
import net.minecraft.entity.EquipmentSlotGroup
import net.minecraft.entity.damage.DamageType
import net.minecraft.entity.damage.DamageTypes
import net.minecraft.fluid.Fluid
import net.minecraft.item.Item
import net.minecraft.loot.condition.*
import net.minecraft.loot.context.LootContext
import net.minecraft.loot.provider.number.EnchantmentLevelNumberProvider
import net.minecraft.particle.BlockStateParticleEffect
import net.minecraft.particle.DustParticleEffect
import net.minecraft.particle.ParticleEffect
import net.minecraft.particle.ParticleTypes
import net.minecraft.predicate.FluidPredicate
import net.minecraft.predicate.TagPredicate
import net.minecraft.predicate.entity.*
import net.minecraft.predicate.item.ItemPredicate
import net.minecraft.registry.*
import net.minecraft.registry.tag.*
import net.minecraft.sound.SoundEvents
import net.minecraft.util.math.Vec3d
import net.minecraft.util.math.float_provider.ConstantFloatProvider
import net.minecraft.world.World
import org.teamvoided.dusk_debris.data.DuskEnchantments
import org.teamvoided.dusk_debris.data.gen.providers.Enchantments.register
import org.teamvoided.dusk_debris.util.*
import java.util.*
import java.util.function.Function

object Enchantments {
    val ENCHANTMENTS = mutableSetOf<Enchantment>()
    val CURSES = mutableSetOf<Enchantment>()
    val TREASURE = mutableSetOf<Enchantment>()

    val ENCHANTMENT_PARTICLE = mutableSetOf<Enchantment>()

    fun bootstrap(c: BootstrapContext<Enchantment>) {
        val damageType: HolderProvider<DamageType> = c.getRegistryLookup(RegistryKeys.DAMAGE_TYPE)
        val enchantment: HolderProvider<Enchantment> = c.getRegistryLookup(RegistryKeys.ENCHANTMENT)
        val item: HolderProvider<Item> = c.getRegistryLookup(RegistryKeys.ITEM)
        val block: HolderProvider<Block> = c.getRegistryLookup(RegistryKeys.BLOCK)
        val fluid: HolderProvider<Fluid> = c.getRegistryLookup(RegistryKeys.FLUID)

        c.createParticles()
        c.createCurses()
        c.createMinecraftOverrides(enchantment, item, fluid)
    }

    private fun BootstrapContext<Enchantment>.createParticles() {
        this.particleEnchantment(
            DuskEnchantments.CURSE_OF_RA,
            BlockStateParticleEffect(ParticleTypes.FALLING_DUST, Blocks.SAND.defaultState),
            0.35f
        ).curse()
        this.particleEnchantment(
            DuskEnchantments.CURSE_OF_RA_RED,
            BlockStateParticleEffect(ParticleTypes.FALLING_DUST, Blocks.RED_SAND.defaultState),
            0.35f
        ).curse()
        this.particleEnchantment(
            DuskEnchantments.PARTICLE_REDSTONE,
            DustParticleEffect.DEFAULT,
            0.1f
        )
        this.particleEnchantment(
            DuskEnchantments.PARTICLE_OMINOUS,
            ParticleTypes.OMINOUS_SPAWNING,
            0.25f
        )
        this.particleEnchantment(
            DuskEnchantments.PARTICLE_TRIAL_DETECTION,
            ParticleTypes.TRIAL_SPAWNER_DETECTION,
            0.2f
        )
        this.particleEnchantment(
            DuskEnchantments.PARTICLE_TRIAL_DETECTION_OMINOUS,
            ParticleTypes.TRIAL_SPAWNER_DETECTION_OMINOUS,
            0.2f
        )
    }

    private fun BootstrapContext<Enchantment>.createCurses() {
        val damageType: HolderProvider<DamageType> = this.getRegistryLookup(RegistryKeys.DAMAGE_TYPE)
        val item: HolderProvider<Item> = this.getRegistryLookup(RegistryKeys.ITEM)
        val block: HolderProvider<Block> = this.getRegistryLookup(RegistryKeys.BLOCK)
        this.register(
            DuskEnchantments.BREAKING,
            Enchantment.builder(
                Enchantment.createProperties(
                    item.getTagOrThrow(ItemTags.DURABILITY_ENCHANTABLE),
                    1,
                    3,
                    Enchantment.cost(5, 8),
                    Enchantment.cost(55, 8),
                    2,
                    EquipmentSlotGroup.ANY
                )
            ).addEffect(
                EnchantmentEffectComponentTypes.ITEM_DAMAGE,
                MultiplyValue(
                    LevelBasedValue.linear(1.25f, 0.25f)
                ),
                MatchToolLootCondition.builder(
                    ItemPredicate.Builder.create().tag(ItemTags.ARMOR_ENCHANTABLE)
                )
            ).addEffect(
                EnchantmentEffectComponentTypes.ITEM_DAMAGE,
                MultiplyValue(
                    LevelBasedValue.linear(1.5f, 0.5f)
                ),
                InvertedLootCondition.builder(
                    MatchToolLootCondition.builder(ItemPredicate.Builder.create().tag(ItemTags.ARMOR_ENCHANTABLE))
                )
            )
        ).curse()
        this.register(
            DuskEnchantments.CURSE_OF_THE_FUNNY, Enchantment.builder(
                Enchantment.createProperties(
                    item.getTagOrThrow(
                        ItemTags.ARMOR_ENCHANTABLE
                    ),
                    1,
                    4,
                    Enchantment.cost(5, 0),
                    Enchantment.cost(50, 0),
                    12,
                    EquipmentSlotGroup.ARMOR
                )
            ).addEffect(
                EnchantmentEffectComponentTypes.TICK,
                Explode(
                    false,
                    Optional.of(damageType.getHolderOrThrow(DamageTypes.PLAYER_EXPLOSION)),
                    Optional.of(
                        LevelBasedValue.linear(0.000033f, 0.000016f)
                    ),
                    block.getTag(BlockTags.BLOCKS_WIND_CHARGE_EXPLOSIONS)
                        .map<HolderSet<Block>>(Function.identity<HolderSet.NamedSet<Block>>()),
                    Vec3d.ZERO,
                    LevelBasedValue.constant(1.5f),
                    false,
                    World.ExplosionSourceType.NONE,
                    ParticleTypes.EXPLOSION,
                    ParticleTypes.EXPLOSION_EMITTER,
                    SoundEvents.ENTITY_GENERIC_EXPLODE
                ),
                RandomChanceLootCondition.method_60310(
                    EnchantmentLevelNumberProvider.method_60313(
                        LevelBasedValue.perLevel(0.00001f)
                    )
                )
            )
        ).curse()
    }

    private fun BootstrapContext<Enchantment>.createMinecraftOverrides(
        enchantment: HolderProvider<Enchantment>,
        item: HolderProvider<Item>,
        fluid: HolderProvider<Fluid>
    ) {
        val item: HolderProvider<Item> = this.getRegistryLookup(RegistryKeys.ITEM)
        this.register(
            DuskEnchantments.IMPALING, Enchantment.builder(
                Enchantment.createProperties(
                    item.getTagOrThrow(ItemTags.TRIDENT_ENCHANTABLE),
                    2,
                    5,
                    Enchantment.cost(1, 8),
                    Enchantment.cost(21, 8),
                    4,
                    EquipmentSlotGroup.MAINHAND
                )
            ).withExclusiveSet(enchantment.getTagOrThrow(EnchantmentTags.DAMAGE_EXCLUSIVE_SET))
                .addEffect(
                    EnchantmentEffectComponentTypes.DAMAGE,
                    AddValue(LevelBasedValue.perLevel(2.5f)),
                    AnyOfLootCondition.create(
                        EntityPropertiesLootCondition.builder(
                            LootContext.EntityTarget.THIS,
                            entityIsInTag(EntityTypeTags.SENSITIVE_TO_IMPALING)
                        ),
                        EntityPropertiesLootCondition.builder(
                            LootContext.EntityTarget.THIS,
                            fluid.entityIsInFluidTag(FluidTags.WATER)
                        ),
                        AllOfLootCondition.builder(
                            WeatherCheckLootCondition.create().raining(true),
                            EntityPropertiesLootCondition.builder(
                                LootContext.EntityTarget.THIS,
                                EntityPredicate.Builder.create()
                                    .location(LocationPredicate.Builder.create().method_60275(true))
                            )
                        )
                    )
                )
        )
    }

    private fun BootstrapContext<Enchantment>.particleEnchantment(
        registryKey: RegistryKey<Enchantment>,
        particle: ParticleEffect,
        chance: Float,
        tag: TagKey<Item> = ItemTags.ARMOR_ENCHANTABLE,
        horizontalPosition: PositionSource = inBoundingBox(),
        verticalPosition: PositionSource = inBoundingBox(),
        horizontalVelocity: VelocitySource = VelocitySource(0f, ConstantFloatProvider.ZERO),
        verticalVelocity: VelocitySource = VelocitySource(0f, ConstantFloatProvider.ZERO)
    ): Enchantment {
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
            ).addEffect(
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

    private fun entityIsInTag(tag: TagKey<EntityType<*>>): EntityPredicate {
        return EntityPredicate.Builder.create().type(EntityTypePredicate.createTagged(tag)).build()
    }

    private fun HolderProvider<Fluid>.entityIsInFluidTag(tag: TagKey<Fluid>): EntityPredicate {
        return EntityPredicate.Builder.create().location(
            LocationPredicate.Builder.create().fluid(
                FluidPredicate.Builder.create().method_35222(this.getTagOrThrow(tag))
            )
        ).build()
    }

    private fun itemIsInTag(tag: TagKey<Item>): ItemPredicate.Builder {
        return ItemPredicate.Builder.create().tag(tag)
    }

    private fun BootstrapContext<Enchantment>.register(
        registryKey: RegistryKey<Enchantment>,
        builder: Enchantment.Builder
    ): Enchantment {
        val enchantment = this.register(registryKey, builder.build(registryKey.value))
        ENCHANTMENTS.add(enchantment.value())
        return enchantment.value()
    }
}