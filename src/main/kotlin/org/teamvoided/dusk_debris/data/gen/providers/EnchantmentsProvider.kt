package org.teamvoided.dusk_debris.data.gen.providers

import net.minecraft.advancements.critereon.EntityEquipmentPredicate
import net.minecraft.advancements.critereon.EntityPredicate
import net.minecraft.advancements.critereon.ItemPredicate
import net.minecraft.advancements.critereon.LocationPredicate
import net.minecraft.core.Holder
import net.minecraft.core.HolderGetter
import net.minecraft.core.HolderSet
import net.minecraft.core.Vec3i
import net.minecraft.core.particles.BlockParticleOption
import net.minecraft.core.particles.DustParticleOptions
import net.minecraft.core.particles.ParticleOptions
import net.minecraft.core.particles.ParticleTypes
import net.minecraft.core.registries.Registries
import net.minecraft.data.worldgen.BootstrapContext
import net.minecraft.resources.ResourceKey
import net.minecraft.sounds.SoundEvents
import net.minecraft.tags.*
import net.minecraft.util.valueproviders.ConstantFloat
import net.minecraft.util.valueproviders.UniformFloat
import net.minecraft.world.damagesource.DamageType
import net.minecraft.world.damagesource.DamageTypes
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.EquipmentSlotGroup
import net.minecraft.world.item.Item
import net.minecraft.world.item.enchantment.Enchantment
import net.minecraft.world.item.enchantment.EnchantmentEffectComponents
import net.minecraft.world.item.enchantment.EnchantmentTarget
import net.minecraft.world.item.enchantment.LevelBasedValue
import net.minecraft.world.item.enchantment.effects.*
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.gameevent.GameEvent
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider
import net.minecraft.world.level.material.Fluid
import net.minecraft.world.level.storage.loot.LootContext
import net.minecraft.world.level.storage.loot.predicates.*
import net.minecraft.world.level.storage.loot.providers.number.EnchantmentLevelProvider
import net.minecraft.world.phys.Vec3
import org.teamvoided.dusk_debris.data.DuskEnchantments
import org.teamvoided.dusk_debris.data.tags.DuskBlockTags
import org.teamvoided.dusk_debris.data.tags.DuskEnchantmentTags
import org.teamvoided.dusk_debris.util.entityIsInFluidTag
import org.teamvoided.dusk_debris.util.entityIsInTag
import org.teamvoided.dusk_debris.util.itemIsInTag
import java.util.*
import java.util.function.Function

object EnchantmentsProvider {

    fun bootstrap(c: BootstrapContext<Enchantment>) {
        val damageType: HolderGetter<DamageType> = c.lookup(Registries.DAMAGE_TYPE)
        val enchantment: HolderGetter<Enchantment> = c.lookup(Registries.ENCHANTMENT)
        val item: HolderGetter<Item> = c.lookup(Registries.ITEM)
        val block: HolderGetter<Block> = c.lookup(Registries.BLOCK)
        val fluid: HolderGetter<Fluid> = c.lookup(Registries.FLUID)

        c.createParticles()
        c.createCurses()
        c.createMinecraftOverrides(enchantment, item, fluid)


        c.register(
            DuskEnchantments.SONIC_BURST,
            Enchantment.enchantment(
                Enchantment.definition(
                    item.getOrThrow(ItemTags.BOW_ENCHANTABLE),
                    2,
                    1,
                    Enchantment.dynamicCost(10, 20),
                    Enchantment.dynamicCost(60, 20),
                    4,
                    EquipmentSlotGroup.ANY
                )
            ).withEffect(
                EnchantmentEffectComponents.POST_ATTACK,
                EnchantmentTarget.DAMAGING_ENTITY,
                EnchantmentTarget.VICTIM,
                ExplodeEffect(
                    false,
                    Optional.of(damageType.getOrThrow(DamageTypes.SONIC_BOOM)),
                    Optional.of(LevelBasedValue.constant(1.25f)),
                    block.get(BlockTags.BLOCKS_WIND_CHARGE_EXPLOSIONS)
                        .map<HolderSet<Block>>(Function.identity<HolderSet.Named<Block>>()),
                    Vec3.ZERO,
                    LevelBasedValue.constant(1.5f),
                    false,
                    Level.ExplosionInteraction.TRIGGER,
                    ParticleTypes.SONIC_BOOM,
                    ParticleTypes.SONIC_BOOM,
                    Holder.direct(SoundEvents.WARDEN_SONIC_BOOM)
                ),
                LootItemEntityPropertyCondition.hasProperties(
                    LootContext.EntityTarget.DIRECT_ATTACKER,
                    EntityPredicate.Builder.entity().of(EntityTypeTags.IMPACT_PROJECTILES)
                )
            ).withEffect(
                EnchantmentEffectComponents.POST_ATTACK,
                EnchantmentTarget.ATTACKER,
                EnchantmentTarget.ATTACKER,
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
//                    EntityAttributes.MOVEMENT_SPEED,
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
//                    EntityAttributes.MOVEMENT_EFFICIENCY,
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
//                        RandomChanceLootCondition.randomChance(
//                            EnchantmentLevelNumberProvider.forEnchantmentLevel(
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
            BlockParticleOption(ParticleTypes.FALLING_DUST, Blocks.SAND.defaultBlockState()),
            0.35f
        )
        this.particleEnchantment(
            DuskEnchantments.CURSE_OF_RA_RED,
            BlockParticleOption(ParticleTypes.FALLING_DUST, Blocks.RED_SAND.defaultBlockState()),
            0.35f
        )
        this.particleEnchantment(
            DuskEnchantments.PARTICLE_REDSTONE,
            DustParticleOptions.REDSTONE,
            0.1f
        )
        this.particleEnchantment(
            DuskEnchantments.PARTICLE_TRIAL,
            ParticleTypes.VAULT_CONNECTION,
            0.25f,
            SpawnParticlesEffect.VelocitySource(0f, UniformFloat.of(-0.1f, 0.1f)),
            SpawnParticlesEffect.VelocitySource(0f, UniformFloat.of(-0.1f, 0.1f))
        )
        this.particleEnchantment(
            DuskEnchantments.PARTICLE_TRIAL_OMINOUS,
            ParticleTypes.OMINOUS_SPAWNING,
            0.25f,
            SpawnParticlesEffect.VelocitySource(0f, UniformFloat.of(-0.1f, 0.1f)),
            SpawnParticlesEffect.VelocitySource(0f, UniformFloat.of(-0.1f, 0.1f))
        )
        this.particleEnchantment(
            DuskEnchantments.PARTICLE_TRIAL_DETECTION,
            ParticleTypes.TRIAL_SPAWNER_DETECTED_PLAYER,
            0.2f
        )
        this.particleEnchantment(
            DuskEnchantments.PARTICLE_TRIAL_DETECTION_OMINOUS,
            ParticleTypes.TRIAL_SPAWNER_DETECTED_PLAYER_OMINOUS,
            0.2f
        )
    }

    private fun BootstrapContext<Enchantment>.createCurses() {
        val damageType: HolderGetter<DamageType> = this.lookup(Registries.DAMAGE_TYPE)
        val enchantment: HolderGetter<Enchantment> = this.lookup(Registries.ENCHANTMENT)
        val item: HolderGetter<Item> = this.lookup(Registries.ITEM)
        val block: HolderGetter<Block> = this.lookup(Registries.BLOCK)
        this.register(
            DuskEnchantments.BREAKING,
            Enchantment.enchantment(
                Enchantment.definition(
                    item.getOrThrow(ItemTags.DURABILITY_ENCHANTABLE),
                    1,
                    3,
                    Enchantment.dynamicCost(5, 8),
                    Enchantment.dynamicCost(55, 8),
                    2,
                    EquipmentSlotGroup.ANY
                )
            )
                .exclusiveWith(enchantment.getOrThrow(DuskEnchantmentTags.UNBREAKING_EXCLUSIVE_SET))
                .withEffect(
                    EnchantmentEffectComponents.ITEM_DAMAGE,
                    MultiplyValue(
                        LevelBasedValue.perLevel(1.25f, 0.25f)
                    ),
                    MatchTool.toolMatches(
                        ItemPredicate.Builder.item().of(ItemTags.ARMOR_ENCHANTABLE)
                    )
                ).withEffect(
                    EnchantmentEffectComponents.ITEM_DAMAGE,
                    MultiplyValue(
                        LevelBasedValue.perLevel(1.5f, 0.5f)
                    ),
                    InvertedLootItemCondition.invert(
                        MatchTool.toolMatches(ItemPredicate.Builder.item().of(ItemTags.ARMOR_ENCHANTABLE))
                    )
                )
        )
        this.register(
            DuskEnchantments.MENDLESS,
            Enchantment.enchantment(
                Enchantment.definition(
                    item.getOrThrow(ItemTags.DURABILITY_ENCHANTABLE),
                    1,
                    1,
                    Enchantment.dynamicCost(5, 8),
                    Enchantment.dynamicCost(55, 8),
                    4,
                    EquipmentSlotGroup.ANY
                )
            ).exclusiveWith(enchantment.getOrThrow(DuskEnchantmentTags.MENDING_EXCLUSIVE_SET))
        )
        this.register(
            DuskEnchantments.CURSE_OF_THE_FUNNY, Enchantment.enchantment(
                Enchantment.definition(
                    item.getOrThrow(ItemTags.ARMOR_ENCHANTABLE),
                    1,
                    4,
                    Enchantment.dynamicCost(5, 0),
                    Enchantment.dynamicCost(50, 0),
                    12,
                    EquipmentSlotGroup.ARMOR
                )
            ).withEffect(
                EnchantmentEffectComponents.TICK,
                ExplodeEffect(
                    false,
                    Optional.of(damageType.getOrThrow(DamageTypes.PLAYER_EXPLOSION)),
                    Optional.of(LevelBasedValue.perLevel(0.1f, 0.5f)),
                    block.get(BlockTags.BLOCKS_WIND_CHARGE_EXPLOSIONS)
                        .map<HolderSet<Block>>(Function.identity<HolderSet.Named<Block>>()),
                    Vec3.ZERO,
                    LevelBasedValue.constant(1.5f),
                    false,
                    Level.ExplosionInteraction.NONE,
                    ParticleTypes.EXPLOSION,
                    ParticleTypes.EXPLOSION_EMITTER,
                    SoundEvents.GENERIC_EXPLODE
                ),
                LootItemRandomChanceCondition.randomChance(
                    EnchantmentLevelProvider.forEnchantmentLevel(
                        LevelBasedValue.perLevel(0.00001f)
                    )
                )
            )
        )
        this.register(
            DuskEnchantments.LIGHTNING_ROD,
            Enchantment.enchantment(
                Enchantment.definition(
                    item.getOrThrow(ItemTags.ARMOR_ENCHANTABLE),
                    2,
                    1,
                    Enchantment.dynamicCost(10, 10),
                    Enchantment.dynamicCost(25, 10),
                    4,
                    EquipmentSlotGroup.ANY
                )
            ).withEffect(
                EnchantmentEffectComponents.TICK,
                SummonEntityEffect(
                    HolderSet.direct(EntityType.LIGHTNING_BOLT.builtInRegistryHolder()),
                    false
                ),
                AllOfCondition.allOf(
                    LootItemRandomChanceCondition.randomChance(
                        EnchantmentLevelProvider.forEnchantmentLevel(
                            LevelBasedValue.perLevel(0.0001f)
                        )
                    ),
                    WeatherCheck.weather().setThundering(true),
                    LootItemEntityPropertyCondition.hasProperties(
                        LootContext.EntityTarget.THIS,
                        EntityPredicate.Builder.entity()
                            .located(LocationPredicate.Builder.location().setCanSeeSky(true))
                    )
                )
            )
        )
        this.register(
            DuskEnchantments.MIDAS,
            Enchantment.enchantment(
                Enchantment.definition(
                    item.getOrThrow(ItemTags.PICKAXES),
                    2,
                    1,
                    Enchantment.dynamicCost(10, 10),
                    Enchantment.dynamicCost(25, 10),
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
        return this.withEffect(
            EnchantmentEffectComponents.HIT_BLOCK,
            ReplaceBlock(
                Vec3i.ZERO,
                Optional.of(BlockPredicate.matchesTag(tag)),
                BlockStateProvider.simple(block),
                Optional.of(GameEvent.BLOCK_CHANGE)
            )
        )
    }


    private fun BootstrapContext<Enchantment>.createMinecraftOverrides(
        enchantment: HolderGetter<Enchantment>,
        item: HolderGetter<Item>,
        fluid: HolderGetter<Fluid>
    ) {
        val item: HolderGetter<Item> = this.lookup(Registries.ITEM)
        this.register(
            DuskEnchantments.IMPALING, Enchantment.enchantment(
                Enchantment.definition(
                    item.getOrThrow(ItemTags.TRIDENT_ENCHANTABLE),
                    2,
                    5,
                    Enchantment.dynamicCost(1, 8),
                    Enchantment.dynamicCost(21, 8),
                    4,
                    EquipmentSlotGroup.MAINHAND
                )
            ).exclusiveWith(enchantment.getOrThrow(EnchantmentTags.DAMAGE_EXCLUSIVE))
                .withEffect(
                    EnchantmentEffectComponents.DAMAGE,
                    AddValue(LevelBasedValue.perLevel(2.5f)),
                    AnyOfCondition.anyOf(
                        LootItemEntityPropertyCondition.hasProperties(
                            LootContext.EntityTarget.THIS,
                            entityIsInTag(EntityTypeTags.SENSITIVE_TO_IMPALING)
                        ),
                        LootItemEntityPropertyCondition.hasProperties(
                            LootContext.EntityTarget.THIS,
                            fluid.entityIsInFluidTag(FluidTags.WATER)
                        ),
                        AllOfCondition.allOf(
                            WeatherCheck.weather().setRaining(true),
                            LootItemEntityPropertyCondition.hasProperties(
                                LootContext.EntityTarget.THIS,
                                EntityPredicate.Builder.entity()
                                    .located(LocationPredicate.Builder.location().setCanSeeSky(true))
                            )
                        )
                    )
                )
        )
    }

    private fun BootstrapContext<Enchantment>.particleEnchantment(
        registryKey: ResourceKey<Enchantment>,
        particle: ParticleOptions,
        chance: Float,
        horizontalVelocity: SpawnParticlesEffect.VelocitySource = SpawnParticlesEffect.VelocitySource(
            0f,
            ConstantFloat.ZERO
        ),
        verticalVelocity: SpawnParticlesEffect.VelocitySource = SpawnParticlesEffect.VelocitySource(0f, ConstantFloat.ZERO)
    ) {
        this.particleEnchantment(
            registryKey,
            particle,
            chance,
            ItemTags.ARMOR_ENCHANTABLE,
            SpawnParticlesEffect.inBoundingBox(),
            SpawnParticlesEffect.inBoundingBox(),
            horizontalVelocity,
            verticalVelocity
        )
    }

    private fun BootstrapContext<Enchantment>.particleEnchantment(
        registryKey: ResourceKey<Enchantment>,
        particle: ParticleOptions,
        chance: Float,
        tag: TagKey<Item> = ItemTags.ARMOR_ENCHANTABLE,
        horizontalPosition: SpawnParticlesEffect.PositionSource = SpawnParticlesEffect.inBoundingBox(),
        verticalPosition: SpawnParticlesEffect.PositionSource = SpawnParticlesEffect.inBoundingBox(),
        horizontalVelocity: SpawnParticlesEffect.VelocitySource = SpawnParticlesEffect.VelocitySource(
            0f,
            ConstantFloat.ZERO
        ),
        verticalVelocity: SpawnParticlesEffect.VelocitySource = SpawnParticlesEffect.VelocitySource(0f, ConstantFloat.ZERO)
    ) {
        val enchantment: HolderGetter<Enchantment> = this.lookup(Registries.ENCHANTMENT)
        val item: HolderGetter<Item> = this.lookup(Registries.ITEM)
        this.register(
            registryKey,
            Enchantment.enchantment(
                Enchantment.definition(
                    item.getOrThrow(tag),
                    1,
                    1,
                    Enchantment.constantCost(1),
                    Enchantment.constantCost(1),
                    1,
                    EquipmentSlotGroup.ANY
                )
            )
                .exclusiveWith(enchantment.getOrThrow(DuskEnchantmentTags.PARTICLE_EXCLUSIVE_SET))
                .withEffect(
                    EnchantmentEffectComponents.TICK,
                    SpawnParticlesEffect(
                        particle,
                        horizontalPosition,
                        verticalPosition,
                        horizontalVelocity,
                        verticalVelocity,
                        ConstantFloat.of(1f)
                    ),
                    AllOfCondition.allOf(
                        LootItemRandomChanceCondition.randomChance(chance),
                        InvertedLootItemCondition.invert(
                            LootItemEntityPropertyCondition.hasProperties(
                                LootContext.EntityTarget.THIS,
                                EntityPredicate.Builder.entity().equipment(
                                    EntityEquipmentPredicate.Builder.equipment()
                                        .mainhand(itemIsInTag(ItemTags.ARMOR_ENCHANTABLE))
                                )
                            )
                        )
                    )
                )
        )
    }

    fun BootstrapContext<Enchantment>.register(
        registryKey: ResourceKey<Enchantment>,
        builder: Enchantment.Builder
    ) {
        this.register(registryKey, builder.build(registryKey.location()))
    }
}