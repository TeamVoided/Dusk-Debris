package org.teamvoided.dusk_debris.data.gen.providers

import net.minecraft.block.Block
import net.minecraft.block.Blocks
import net.minecraft.component.EnchantmentEffectComponentTypes
import net.minecraft.enchantment.Enchantment
import net.minecraft.enchantment.Enchantments
import net.minecraft.enchantment.LevelBasedValue
import net.minecraft.enchantment.effect.*
import net.minecraft.entity.EntityType
import net.minecraft.entity.EquipmentSlotGroup
import net.minecraft.entity.attribute.EntityAttributeModifier
import net.minecraft.entity.attribute.EntityAttributes
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
import net.minecraft.predicate.entity.*
import net.minecraft.predicate.item.ItemPredicate
import net.minecraft.registry.*
import net.minecraft.registry.tag.*
import net.minecraft.sound.SoundEvents
import net.minecraft.util.Identifier
import net.minecraft.util.math.Vec3d
import net.minecraft.util.math.Vec3i
import net.minecraft.util.math.float_provider.ConstantFloatProvider
import net.minecraft.util.math.float_provider.UniformFloatProvider
import net.minecraft.world.World
import net.minecraft.world.event.GameEvent
import net.minecraft.world.gen.blockpredicate.BlockPredicate
import net.minecraft.world.gen.stateprovider.BlockStateProvider
import org.teamvoided.dusk_debris.data.DuskEnchantments
import org.teamvoided.dusk_debris.data.tags.DuskBlockTags
import org.teamvoided.dusk_debris.data.tags.DuskEnchantmentTags
import org.teamvoided.dusk_debris.util.*
import java.util.*
import java.util.function.Function

object EnchantmentsProvider {

    fun bootstrap(c: BootstrapContext<Enchantment>) {
        val damageType: HolderProvider<DamageType> = c.getRegistryLookup(RegistryKeys.DAMAGE_TYPE)
        val enchantment: HolderProvider<Enchantment> = c.getRegistryLookup(RegistryKeys.ENCHANTMENT)
        val item: HolderProvider<Item> = c.getRegistryLookup(RegistryKeys.ITEM)
        val block: HolderProvider<Block> = c.getRegistryLookup(RegistryKeys.BLOCK)
        val fluid: HolderProvider<Fluid> = c.getRegistryLookup(RegistryKeys.FLUID)

        c.createParticles()
        c.createCurses()
        c.createMinecraftOverrides(enchantment, item, fluid)


        c.register(
            DuskEnchantments.SONIC_BURST,
            Enchantment.builder(
                Enchantment.createProperties(
                    item.getTagOrThrow(ItemTags.BOW_ENCHANTABLE),
                    2,
                    1,
                    Enchantment.cost(10, 20),
                    Enchantment.cost(60, 20),
                    4,
                    EquipmentSlotGroup.ANY
                )
            ).addEffect(
                EnchantmentEffectComponentTypes.POST_ATTACK,
                EnchantmentEffectTarget.DAMAGING_ENTITY,
                EnchantmentEffectTarget.VICTIM,
                Explode(
                    false,
                    Optional.of(damageType.getHolderOrThrow(DamageTypes.SONIC_BOOM)),
                    Optional.of(LevelBasedValue.constant(1.25f)),
                    block.getTag(BlockTags.BLOCKS_WIND_CHARGE_EXPLOSIONS)
                        .map<HolderSet<Block>>(Function.identity<HolderSet.NamedSet<Block>>()),
                    Vec3d.ZERO,
                    LevelBasedValue.constant(1.5f),
                    false,
                    World.ExplosionSourceType.TRIGGER,
                    ParticleTypes.SONIC_BOOM,
                    ParticleTypes.SONIC_BOOM,
                    Holder.createDirect(SoundEvents.ENTITY_WARDEN_SONIC_BOOM)
                ),
                EntityPropertiesLootCondition.builder(
                    LootContext.EntityTarget.DIRECT_ATTACKER,
                    EntityPredicate.Builder.create().tagged(EntityTypeTags.IMPACT_PROJECTILES)
                )
            ).addEffect(
                EnchantmentEffectComponentTypes.POST_ATTACK,
                EnchantmentEffectTarget.ATTACKER,
                EnchantmentEffectTarget.ATTACKER,
                DamageItem(LevelBasedValue.constant(128f))
//            ).addEffect(
//                EnchantmentEffectComponentTypes.TICK,
//                SpawnParticles(
//                    ParticleTypes.SONIC_BOOM,
//                    SpawnParticles.entityPosition(0f),
//                    SpawnParticles.entityPosition(0f),
//                    SpawnParticles.VelocitySource(0f, ConstantFloatProvider.ZERO),
//                    SpawnParticles.VelocitySource(0f, ConstantFloatProvider.ZERO),
//                    ConstantFloatProvider.create(1f)
//                ),
//                EntityPropertiesLootCondition.builder(
//                    LootContext.EntityTarget.THIS,
//                    EntityPredicate.Builder.create().tagged(EntityTypeTags.IMPACT_PROJECTILES)
//                )
            )
        )

//        Enchantments.register(
//            context, Enchantments.SOUL_SPEED, Enchantment.builder(
//                Enchantment.createProperties(
//                    holderProvider3.getTagOrThrow(
//                        ItemTags.FOOT_ARMOR_ENCHANTABLE
//                    ), 1, 3, Enchantment.cost(10, 10), Enchantment.cost(25, 10), 8, *arrayOf<EquipmentSlotGroup>(
//                        EquipmentSlotGroup.FEET
//                    )
//                )
//            ).addEffect<EnchantmentLocationBasedEffect>(
//                EnchantmentEffectComponentTypes.LOCATION_CHANGED,
//                EnchantmentAttribute(
//                    Identifier.ofDefault("enchantment.soul_speed"),
//                    EntityAttributes.GENERIC_MOVEMENT_SPEED,
//                    LevelBasedValue.linear(0.0405f, 0.0105f),
//                    EntityAttributeModifier.Operation.ADD_VALUE
//                ),
//                AllOfLootCondition.builder(
//                    *arrayOf<LootCondition.Builder>(
//                        InvertedLootCondition.builder(
//                            EntityPropertiesLootCondition.builder(
//                                LootContext.EntityTarget.THIS, EntityPredicate.Builder.create().vehicle(
//                                    EntityPredicate.Builder.create()
//                                )
//                            )
//                        ), AnyOfLootCondition.create(
//                            *arrayOf<LootCondition.Builder>(
//                                AllOfLootCondition.builder(
//                                    *arrayOf<LootCondition.Builder>(
//                                        CheckEnchantmentActiveLootCondition.activeBuilder(),
//                                        EntityPropertiesLootCondition.builder(
//                                            LootContext.EntityTarget.THIS, EntityPredicate.Builder.create().flags(
//                                                EntityFlagsPredicate.Builder.create().method_59919(false)
//                                            )
//                                        ),
//                                        AnyOfLootCondition.create(
//                                            *arrayOf<LootCondition.Builder>(
//                                                EntityPropertiesLootCondition.builder(
//                                                    LootContext.EntityTarget.THIS,
//                                                    EntityPredicate.Builder.create().method_60611(
//                                                        LocationPredicate.Builder.create().block(
//                                                            net.minecraft.predicate.BlockPredicate.Builder.create().tag(
//                                                                BlockTags.SOUL_SPEED_BLOCKS
//                                                            )
//                                                        )
//                                                    )
//                                                ), EntityPropertiesLootCondition.builder(
//                                                    LootContext.EntityTarget.THIS,
//                                                    EntityPredicate.Builder.create().flags(
//                                                        EntityFlagsPredicate.Builder.create().method_59918(false)
//                                                    ).build()
//                                                )
//                                            )
//                                        )
//                                    )
//                                ), AllOfLootCondition.builder(
//                                    *arrayOf<LootCondition.Builder>(
//                                        CheckEnchantmentActiveLootCondition.inactiveBuilder(),
//                                        EntityPropertiesLootCondition.builder(
//                                            LootContext.EntityTarget.THIS,
//                                            EntityPredicate.Builder.create().method_60611(
//                                                LocationPredicate.Builder.create().block(
//                                                    net.minecraft.predicate.BlockPredicate.Builder.create().tag(
//                                                        BlockTags.SOUL_SPEED_BLOCKS
//                                                    )
//                                                )
//                                            ).flags(EntityFlagsPredicate.Builder.create().method_59919(false))
//                                        )
//                                    )
//                                )
//                            )
//                        )
//                    )
//                )
//            ).addEffect<EnchantmentLocationBasedEffect>(
//                EnchantmentEffectComponentTypes.LOCATION_CHANGED,
//                EnchantmentAttribute(
//                    Identifier.ofDefault("enchantment.soul_speed"),
//                    EntityAttributes.GENERIC_MOVEMENT_EFFICIENCY,
//                    LevelBasedValue.constant(1.0f),
//                    EntityAttributeModifier.Operation.ADD_VALUE
//                ),
//                EntityPropertiesLootCondition.builder(
//                    LootContext.EntityTarget.THIS, EntityPredicate.Builder.create().method_60611(
//                        LocationPredicate.Builder.create().block(
//                            net.minecraft.predicate.BlockPredicate.Builder.create().tag(
//                                BlockTags.SOUL_SPEED_BLOCKS
//                            )
//                        )
//                    )
//                )
//            ).addEffect<EnchantmentLocationBasedEffect>(
//                EnchantmentEffectComponentTypes.LOCATION_CHANGED,
//                DamageItem(LevelBasedValue.constant(1.0f)),
//                AllOfLootCondition.builder(
//                    *arrayOf<LootCondition.Builder>(
//                        RandomChanceLootCondition.method_60310(
//                            EnchantmentLevelNumberProvider.method_60313(
//                                LevelBasedValue.constant(0.04f)
//                            )
//                        ), EntityPropertiesLootCondition.builder(
//                            LootContext.EntityTarget.THIS, EntityPredicate.Builder.create().flags(
//                                EntityFlagsPredicate.Builder.create().method_59918(true)
//                            ).method_60611(
//                                LocationPredicate.Builder.create().block(
//                                    net.minecraft.predicate.BlockPredicate.Builder.create().tag(
//                                        BlockTags.SOUL_SPEED_BLOCKS
//                                    )
//                                )
//                            )
//                        )
//                    )
//                )
//            ).addEffect<EnchantmentEntityEffect>(
//                EnchantmentEffectComponentTypes.TICK,
//                SpawnParticles(
//                    ParticleTypes.SOUL,
//                    SpawnParticles.inBoundingBox(),
//                    SpawnParticles.entityPosition(0.1f),
//                    SpawnParticles.scaledVelocity(-0.2f),
//                    SpawnParticles.fixedVelocity(
//                        ConstantFloatProvider.create(0.1f)
//                    ),
//                    ConstantFloatProvider.create(1.0f)
//                ), EntityPropertiesLootCondition.builder(
//                    LootContext.EntityTarget.THIS, builder
//                )
//            ).addEffect<EnchantmentEntityEffect>(
//                EnchantmentEffectComponentTypes.TICK,
//                PlaySound(
//                    SoundEvents.PARTICLE_SOUL_ESCAPE,
//                    ConstantFloatProvider.create(0.6f),
//                    UniformFloatProvider.create(0.6f, 1.0f)
//                ),
//                AllOfLootCondition.builder(
//                    *arrayOf<LootCondition.Builder>(
//                        RandomChanceLootCondition.method_932(0.35f),
//                        EntityPropertiesLootCondition.builder(LootContext.EntityTarget.THIS, builder)
//                    )
//                )
//            )
//        )

    }

    private fun BootstrapContext<Enchantment>.createParticles() {
        this.particleEnchantment(
            DuskEnchantments.CURSE_OF_RA,
            BlockStateParticleEffect(ParticleTypes.FALLING_DUST, Blocks.SAND.defaultState),
            0.35f
        )
        this.particleEnchantment(
            DuskEnchantments.CURSE_OF_RA_RED,
            BlockStateParticleEffect(ParticleTypes.FALLING_DUST, Blocks.RED_SAND.defaultState),
            0.35f
        )
        this.particleEnchantment(
            DuskEnchantments.PARTICLE_REDSTONE,
            DustParticleEffect.DEFAULT,
            0.1f
        )
        this.particleEnchantment(
            DuskEnchantments.PARTICLE_TRIAL,
            ParticleTypes.VAULT_CONNECTION,
            0.25f,
            SpawnParticles.VelocitySource(0f, UniformFloatProvider.create(-0.1f, 0.1f)),
            SpawnParticles.VelocitySource(0f, UniformFloatProvider.create(-0.1f, 0.1f))
        )
        this.particleEnchantment(
            DuskEnchantments.PARTICLE_TRIAL_OMINOUS,
            ParticleTypes.OMINOUS_SPAWNING,
            0.25f,
            SpawnParticles.VelocitySource(0f, UniformFloatProvider.create(-0.1f, 0.1f)),
            SpawnParticles.VelocitySource(0f, UniformFloatProvider.create(-0.1f, 0.1f))
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
        val enchantment: HolderProvider<Enchantment> = this.getRegistryLookup(RegistryKeys.ENCHANTMENT)
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
            )
                .withExclusiveSet(enchantment.getTagOrThrow(DuskEnchantmentTags.UNBREAKING_EXCLUSIVE_SET))
                .addEffect(
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
        )
        this.register(
            DuskEnchantments.MENDLESS,
            Enchantment.builder(
                Enchantment.createProperties(
                    item.getTagOrThrow(ItemTags.DURABILITY_ENCHANTABLE),
                    1,
                    1,
                    Enchantment.cost(5, 8),
                    Enchantment.cost(55, 8),
                    4,
                    EquipmentSlotGroup.ANY
                )
            ).withExclusiveSet(enchantment.getTagOrThrow(DuskEnchantmentTags.MENDING_EXCLUSIVE_SET))
        )
        this.register(
            DuskEnchantments.CURSE_OF_THE_FUNNY, Enchantment.builder(
                Enchantment.createProperties(
                    item.getTagOrThrow(ItemTags.ARMOR_ENCHANTABLE),
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
                    Optional.of(LevelBasedValue.linear(0.1f, 0.5f)),
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
        )
        this.register(
            DuskEnchantments.LIGHTNING_ROD,
            Enchantment.builder(
                Enchantment.createProperties(
                    item.getTagOrThrow(ItemTags.ARMOR_ENCHANTABLE),
                    2,
                    1,
                    Enchantment.cost(10, 10),
                    Enchantment.cost(25, 10),
                    4,
                    EquipmentSlotGroup.ANY
                )
            ).addEffect(
                EnchantmentEffectComponentTypes.TICK,
                SummonEntity(
                    HolderSet.createDirect(EntityType.LIGHTNING_BOLT.builtInRegistryHolder),
                    false
                ),
                AllOfLootCondition.builder(
                    RandomChanceLootCondition.method_60310(
                        EnchantmentLevelNumberProvider.method_60313(
                            LevelBasedValue.perLevel(0.0001f)
                        )
                    ),
                    WeatherCheckLootCondition.create().thundering(true),
                    EntityPropertiesLootCondition.builder(
                        LootContext.EntityTarget.THIS,
                        EntityPredicate.Builder.create()
                            .location(LocationPredicate.Builder.create().method_60275(true))
                    )
                )
            )
        )
        this.register(
            DuskEnchantments.MIDAS,
            Enchantment.builder(
                Enchantment.createProperties(
                    item.getTagOrThrow(ItemTags.PICKAXES),
                    2,
                    1,
                    Enchantment.cost(10, 10),
                    Enchantment.cost(25, 10),
                    4,
                    EquipmentSlotGroup.ANY
                )
            ).addBlockChange(
                Blocks.DEEPSLATE_GOLD_ORE,
                DuskBlockTags.MIDAS_DEEPSLATE_GOLD_ORE
            ).addBlockChange(
                Blocks.GOLD_ORE,
                DuskBlockTags.MIDAS_GOLD_ORE
            ).addBlockChange(
                Blocks.NETHER_GOLD_ORE,
                DuskBlockTags.MIDAS_NETHER_GOLD_ORE
            ).addBlockChange(
                Blocks.RAW_GOLD_BLOCK,
                DuskBlockTags.MIDAS_RAW_GOLD_BLOCK
            ).addBlockChange(
                Blocks.GOLD_BLOCK,
                DuskBlockTags.MIDAS_GOLD_BLOCK
            ).addBlockChange(
                Blocks.LIGHT_WEIGHTED_PRESSURE_PLATE,
                DuskBlockTags.MIDAS_GOLD_PRESSURE_PLATE
            ).addBlockChange(
                Blocks.GILDED_BLACKSTONE,
                DuskBlockTags.MIDAS_GILDED_BLACKSTONE
            )
        )
    }

    private fun Enchantment.Builder.addBlockChange(
        block: Block,
        tag: TagKey<Block>
    ): Enchantment.Builder {
        return this.addEffect(
            EnchantmentEffectComponentTypes.HIT_BLOCK,
            ReplaceBlock(
                Vec3i.ZERO,
                Optional.of(BlockPredicate.matchingBlockTags(tag)),
                BlockStateProvider.of(block),
                Optional.of(GameEvent.BLOCK_CHANGE)
            )
        )
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
        horizontalVelocity: SpawnParticles.VelocitySource = SpawnParticles.VelocitySource(
            0f,
            ConstantFloatProvider.ZERO
        ),
        verticalVelocity: SpawnParticles.VelocitySource = SpawnParticles.VelocitySource(0f, ConstantFloatProvider.ZERO)
    ) {
        this.particleEnchantment(
            registryKey,
            particle,
            chance,
            ItemTags.ARMOR_ENCHANTABLE,
            SpawnParticles.inBoundingBox(),
            SpawnParticles.inBoundingBox(),
            horizontalVelocity,
            verticalVelocity
        )
    }

    private fun BootstrapContext<Enchantment>.particleEnchantment(
        registryKey: RegistryKey<Enchantment>,
        particle: ParticleEffect,
        chance: Float,
        tag: TagKey<Item> = ItemTags.ARMOR_ENCHANTABLE,
        horizontalPosition: SpawnParticles.PositionSource = SpawnParticles.inBoundingBox(),
        verticalPosition: SpawnParticles.PositionSource = SpawnParticles.inBoundingBox(),
        horizontalVelocity: SpawnParticles.VelocitySource = SpawnParticles.VelocitySource(
            0f,
            ConstantFloatProvider.ZERO
        ),
        verticalVelocity: SpawnParticles.VelocitySource = SpawnParticles.VelocitySource(0f, ConstantFloatProvider.ZERO)
    ) {
        val enchantment: HolderProvider<Enchantment> = this.getRegistryLookup(RegistryKeys.ENCHANTMENT)
        val item: HolderProvider<Item> = this.getRegistryLookup(RegistryKeys.ITEM)
        this.register(
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
        )
    }

    fun BootstrapContext<Enchantment>.register(
        registryKey: RegistryKey<Enchantment>,
        builder: Enchantment.Builder
    ) {
        this.register(registryKey, builder.build(registryKey.value))
    }
}