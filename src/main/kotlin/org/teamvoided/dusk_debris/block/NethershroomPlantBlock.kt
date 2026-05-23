package org.teamvoided.dusk_debris.block

import com.mojang.serialization.MapCodec
import net.minecraft.core.BlockPos
import net.minecraft.core.Holder
import net.minecraft.core.particles.ParticleOptions
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundSource
import net.minecraft.util.RandomSource
import net.minecraft.world.effect.MobEffect
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.effect.MobEffects
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.GameRules
import net.minecraft.world.level.Level
import net.minecraft.world.level.LevelReader
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.BonemealableBlock
import net.minecraft.world.level.block.BushBlock
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.BooleanProperty
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature
import net.minecraft.world.phys.shapes.CollisionContext
import net.minecraft.world.phys.shapes.VoxelShape
import org.teamvoided.dusk_debris.block.not_blocks.DuskProperties
import org.teamvoided.dusk_debris.data.tags.DuskBlockTags
import org.teamvoided.dusk_debris.data.tags.DuskEntityTypeTags
import org.teamvoided.dusk_debris.data.worldgen.DuskConfiguredFeatures
import org.teamvoided.dusk_debris.init.DuskEntities
import org.teamvoided.dusk_debris.init.DuskSoundEvents
import org.teamvoided.dusk_debris.particle.color.NethershroomSporeParticleEffect
import java.util.*
import kotlin.random.Random

class NethershroomPlantBlock(
    val delay: Int,
    val feature: ResourceKey<ConfiguredFeature<*, *>>,
    val particle: ParticleOptions,
    val statusEffect: Holder<MobEffect>,
    val hasDoubleEffect: Boolean,
    settings: Properties
) :
    BushBlock(settings), BonemealableBlock {

    public override fun codec(): MapCodec<NethershroomPlantBlock> {
        return CODEC
    }

    init {
        this.registerDefaultState(stateDefinition.any().setValue(SQUISHED, false))
    }

    override fun getShape(
        state: BlockState,
        world: BlockGetter,
        pos: BlockPos,
        context: CollisionContext
    ): VoxelShape {
        return SHAPE
    }

    override fun mayPlaceOn(floor: BlockState, world: BlockGetter, pos: BlockPos): Boolean {
        return floor.isSolidRender(world, pos)
    }

    override fun entityInside(state: BlockState, world: Level, pos: BlockPos, entity: Entity) {
        if (!state.getValue(SQUISHED) &&
            entity.showVehicleHealth() &&
            !entity.isShiftKeyDown &&
            !entity.type.`is`(DuskEntityTypeTags.IS_NOT_AFFECTED_BY_NETHERSHROOM)
        ) {
            if ((entity is Player || world.gameRules.getBoolean(GameRules.RULE_MOBGRIEFING))) {
                world.setBlock(
                    pos,
                    state.setValue(SQUISHED, true),
                    3
                )
                world.playSound(
                    null,
                    pos,
                    DuskSoundEvents.BLOCK_NETHERSHROOM_SQUISHED,
                    SoundSource.BLOCKS,
                    1f,
                    0.9f + world.random.nextFloat() * 0.2f
                )
                world.scheduleTick(pos, this, delay)
            }
        }
        super.entityInside(state, world, pos, entity)
    }

    override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block, BlockState>) {
        builder.add(SQUISHED)
    }

    override fun tick(state: BlockState, world: ServerLevel, pos: BlockPos, random: RandomSource) {
        if (state.getValue(SQUISHED)) {
            val dropChance = 0.2
            world.destroyBlock(pos, Random.nextDouble() <= dropChance)
            explode(world, pos, particle, statusEffect, hasDoubleEffect)
        }
    }

    override fun isValidBonemealTarget(world: LevelReader, pos: BlockPos, state: BlockState): Boolean {
        val belowBlock = world.getBlockState(pos.below())
        return belowBlock.`is`(DuskBlockTags.NETHERSHROOM_GROWABLE_ON)
    }

    override fun isBonemealSuccess(world: Level, random: RandomSource, pos: BlockPos, state: BlockState): Boolean {
        return random.nextFloat().toDouble() < 0.4
    }

    override fun performBonemeal(world: ServerLevel, random: RandomSource, pos: BlockPos, state: BlockState) {
        this.trySpawningBigNethershroom(world, pos, state, random)
    }

    fun trySpawningBigNethershroom(
        world: ServerLevel,
        pos: BlockPos,
        state: BlockState,
        random: RandomSource
    ): Boolean {
        val optional: Optional<out Holder<ConfiguredFeature<*, *>>> =
            world.registryAccess().registryOrThrow(Registries.CONFIGURED_FEATURE).getHolder(
                this.feature
            )
        if (optional.isEmpty) {
            return false
        } else {
            world.removeBlock(pos, false)
            if (((optional.get() as Holder<*>).value() as ConfiguredFeature<*, *>).place(
                    world,
                    world.chunkSource.generator,
                    random,
                    pos
                )
            ) {
                return true
            } else {
                world.setBlock(pos, state, 3)
                return false
            }
        }
    }

//    private fun applyLingeringPotion(potionContents: PotionContentsComponent) {
//        val areaEffectCloudEntity = AreaEffectCloudEntity(this.getLevel(), this.getX(), this.getY(), this.getZ())
//        val var4: Entity = this.getOwner()
//        if (var4 is LivingEntity) {
//            areaEffectCloudEntity.owner = var4
//        }
//
//        areaEffectCloudEntity.radius = 3.0f
//        areaEffectCloudEntity.radiusOnUse = -0.5f
//        areaEffectCloudEntity.waitTime = 10
//        areaEffectCloudEntity.radiusGrowth = -areaEffectCloudEntity.radius / areaEffectCloudEntity.duration.toFloat()
//        areaEffectCloudEntity.setPotionContents(potionContents)
//        this.getLevel().spawnEntity(areaEffectCloudEntity)
//    }

    companion object {
        val CODEC: MapCodec<NethershroomPlantBlock> = simpleCodec { settings: Properties ->
            NethershroomPlantBlock(
                20,
                DuskConfiguredFeatures.HUGE_BLUE_NETHERSHROOM,
                NethershroomSporeParticleEffect(0xffffff),
                MobEffects.POISON,
                false,
                settings
            )
        }

        fun explode(
            world: Level,
            pos: BlockPos,
            particle: ParticleOptions,
            statusEffect: Holder<MobEffect>,
            hasDoubleEffect: Boolean
        ) {
            world.playSound(
                null,
                pos,
                DuskSoundEvents.BLOCK_NETHERSHROOM_EXPLODE,
                SoundSource.BLOCKS,
                1f,
                0.8f + world.random.nextFloat() * 0.4f
            )
            val poisonCloud = DuskEntities.BOX_AREA_EFFECT_CLOUD.create(world)
            if (poisonCloud != null) {
                poisonCloud.particle = particle
                poisonCloud.addEffect(
                    MobEffectInstance(
                        statusEffect,
                        700
                    )
                )
                if (hasDoubleEffect) poisonCloud.addEffect(
                    MobEffectInstance(
                        statusEffect,
                        50,
                        1
                    )
                )
                poisonCloud.radius = 4.0f
                poisonCloud.duration = 700
                poisonCloud.waitTime = 10
                poisonCloud.setRadiusPerTick(-poisonCloud.radius / (poisonCloud.duration.toFloat() * 2))
                poisonCloud.moveTo(
                    pos.x.toDouble() + 0.5,
                    pos.y.toDouble() + 1 - ((3 * poisonCloud.radius) / 4),
                    pos.z.toDouble() + 0.5,
                    0.0f,
                    0.0f
                )
                world.addFreshEntity(poisonCloud)
            }
        }

        fun explode(world: Level, pos: BlockPos, particle: ParticleOptions) {
            world.playSound(
                null,
                pos,
                DuskSoundEvents.BLOCK_NETHERSHROOM_EXPLODE,
                SoundSource.BLOCKS,
                1f,
                0.9f + world.random.nextFloat() * 0.2f
            )
            val poisonCloud = DuskEntities.BOX_AREA_EFFECT_CLOUD.create(world)
            if (poisonCloud != null) {
                poisonCloud.particle = particle
                poisonCloud.radius = 4.0f
                poisonCloud.duration = 700
                poisonCloud.waitTime = 10
                poisonCloud.setRadiusPerTick(-poisonCloud.radius / (poisonCloud.duration.toFloat() * 2))
                poisonCloud.moveTo(
                    pos.x.toDouble() + 0.5,
                    pos.y.toDouble() + 1 - ((3 * poisonCloud.radius) / 4),
                    pos.z.toDouble() + 0.5,
                    0.0f,
                    0.0f
                )
                world.addFreshEntity(poisonCloud)
            }
        }

        private val SHAPE: VoxelShape = box(5.0, 0.0, 5.0, 11.0, 6.0, 11.0)
        val SQUISHED: BooleanProperty = DuskProperties.SQUISHED
    }
}