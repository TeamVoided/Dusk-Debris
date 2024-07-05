package org.teamvoided.dusk_debris.block

import com.mojang.serialization.MapCodec
import net.minecraft.block.*
import net.minecraft.entity.Entity
import net.minecraft.entity.effect.StatusEffect
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.entity.effect.StatusEffects
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.particle.ParticleEffect
import net.minecraft.registry.Holder
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.server.world.ServerWorld
import net.minecraft.sound.SoundCategory
import net.minecraft.state.StateManager
import net.minecraft.state.property.BooleanProperty
import net.minecraft.util.math.BlockPos
import net.minecraft.util.random.RandomGenerator
import net.minecraft.util.shape.VoxelShape
import net.minecraft.world.BlockView
import net.minecraft.world.GameRules
import net.minecraft.world.World
import net.minecraft.world.WorldView
import net.minecraft.world.gen.feature.ConfiguredFeature
import org.teamvoided.dusk_debris.data.DuskBlockTags
import org.teamvoided.dusk_debris.data.DuskEntityTypeTags
import org.teamvoided.dusk_debris.init.DuskEntities
import org.teamvoided.dusk_debris.init.DuskSoundEvents
import org.teamvoided.dusk_debris.data.DuskConfiguredFeatures
import org.teamvoided.dusk_debris.particle.NethershroomSporeParticleEffect
import java.util.*
import kotlin.random.Random

class NethershroomPlantBlock(
    val delay: Int,
    val feature: RegistryKey<ConfiguredFeature<*, *>>,
    val particle: ParticleEffect,
    val statusEffect: Holder<StatusEffect>,
    val hasDoubleEffect: Boolean,
    settings: Settings
) :
    AbstractPlantBlock(settings), Fertilizable {

    public override fun getCodec(): MapCodec<NethershroomPlantBlock> {
        return CODEC
    }

    init {
        this.defaultState = stateManager.defaultState.with(SQUISHED, false)
    }

    override fun getOutlineShape(
        state: BlockState,
        world: BlockView,
        pos: BlockPos,
        context: ShapeContext
    ): VoxelShape {
        return SHAPE
    }

    override fun canPlantOnTop(floor: BlockState, world: BlockView, pos: BlockPos): Boolean {
        return floor.isOpaqueFullCube(world, pos)
    }

    override fun onEntityCollision(state: BlockState, world: World, pos: BlockPos, entity: Entity) {
        if (!state.get(SQUISHED) &&
            entity.isLiving &&
            !entity.isSneaking &&
            !entity.type.isIn(DuskEntityTypeTags.IS_NOT_AFFECTED_BY_NETHERSHROOM)
        ) {
            if ((entity is PlayerEntity || world.gameRules.getBooleanValue(GameRules.DO_MOB_GRIEFING))) {
                world.setBlockState(
                    pos,
                    state.with(SQUISHED, true),
                    3
                )
                world.playSound(
                    null,
                    pos,
                    DuskSoundEvents.BLOCK_NETHERSHROOM_SQUISHED,
                    SoundCategory.BLOCKS,
                    1f,
                    0.9f + world.random.nextFloat() * 0.2f
                )
                world.scheduleBlockTick(pos, this, delay)
            }
        }
        super.onEntityCollision(state, world, pos, entity)
    }

    override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) {
        builder.add(SQUISHED)
    }

    override fun scheduledTick(state: BlockState, world: ServerWorld, pos: BlockPos, random: RandomGenerator) {
        if (state.get(SQUISHED)) {
            val dropChance = 0.2
            world.breakBlock(pos, Random.nextDouble() <= dropChance)
            explode(world, pos, particle, statusEffect, hasDoubleEffect)
        }
    }

    override fun isFertilizable(world: WorldView, pos: BlockPos, state: BlockState): Boolean {
        val belowBlock = world.getBlockState(pos.down())
        return belowBlock.isIn(DuskBlockTags.NETHERSHROOM_GROWABLE_ON)
    }

    override fun canFertilize(world: World, random: RandomGenerator, pos: BlockPos, state: BlockState): Boolean {
        return random.nextFloat().toDouble() < 0.4
    }

    override fun fertilize(world: ServerWorld, random: RandomGenerator, pos: BlockPos, state: BlockState) {
        this.trySpawningBigNethershroom(world, pos, state, random)
    }

    fun trySpawningBigNethershroom(
        world: ServerWorld,
        pos: BlockPos,
        state: BlockState,
        random: RandomGenerator
    ): Boolean {
        val optional: Optional<out Holder<ConfiguredFeature<*, *>>> =
            world.registryManager.get(RegistryKeys.CONFIGURED_FEATURE).getHolder(
                this.feature
            )
        if (optional.isEmpty) {
            return false
        } else {
            world.removeBlock(pos, false)
            if (((optional.get() as Holder<*>).value() as ConfiguredFeature<*, *>).generate(
                    world,
                    world.chunkManager.chunkGenerator,
                    random,
                    pos
                )
            ) {
                return true
            } else {
                world.setBlockState(pos, state, 3)
                return false
            }
        }
    }

//    private fun applyLingeringPotion(potionContents: PotionContentsComponent) {
//        val areaEffectCloudEntity = AreaEffectCloudEntity(this.getWorld(), this.getX(), this.getY(), this.getZ())
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
//        this.getWorld().spawnEntity(areaEffectCloudEntity)
//    }

    companion object {
        val CODEC: MapCodec<NethershroomPlantBlock> = createCodec { settings: Settings ->
            NethershroomPlantBlock(
                20,
                DuskConfiguredFeatures.HUGE_BLUE_NETHERSHROOM,
                NethershroomSporeParticleEffect(0xffffff),
                StatusEffects.POISON,
                false,
                settings
            )
        }

        fun explode(
            world: World,
            pos: BlockPos,
            particle: ParticleEffect,
            statusEffect: Holder<StatusEffect>,
            hasDoubleEffect: Boolean
        ) {
            world.playSound(
                null,
                pos,
                DuskSoundEvents.BLOCK_NETHERSHROOM_EXPLODE,
                SoundCategory.BLOCKS,
                1f,
                0.8f + world.random.nextFloat() * 0.4f
            )
            val poisonCloud = DuskEntities.BOX_AREA_EFFECT_CLOUD.create(world)
            if (poisonCloud != null) {
                poisonCloud.particleType = particle
                poisonCloud.addEffect(
                    StatusEffectInstance(
                        statusEffect,
                        700
                    )
                )
                if (hasDoubleEffect) poisonCloud.addEffect(
                    StatusEffectInstance(
                        statusEffect,
                        50,
                        1
                    )
                )
                poisonCloud.radius = 4.0f
                poisonCloud.duration = 700
                poisonCloud.waitTime = 10
                poisonCloud.radiusGrowth = -poisonCloud.radius / (poisonCloud.duration.toFloat() * 2)
                poisonCloud.refreshPositionAndAngles(
                    pos.x.toDouble() + 0.5,
                    pos.y.toDouble() + 1 - ((3 * poisonCloud.radius) / 4),
                    pos.z.toDouble() + 0.5,
                    0.0f,
                    0.0f
                )
                world.spawnEntity(poisonCloud)
            }
        }

        fun explode(world: World, pos: BlockPos, particle: ParticleEffect) {
            world.playSound(
                null,
                pos,
                DuskSoundEvents.BLOCK_NETHERSHROOM_EXPLODE,
                SoundCategory.BLOCKS,
                1f,
                0.9f + world.random.nextFloat() * 0.2f
            )
            val poisonCloud = DuskEntities.BOX_AREA_EFFECT_CLOUD.create(world)
            if (poisonCloud != null) {
                poisonCloud.particleType = particle
                poisonCloud.radius = 4.0f
                poisonCloud.duration = 700
                poisonCloud.waitTime = 10
                poisonCloud.radiusGrowth = -poisonCloud.radius / (poisonCloud.duration.toFloat() * 2)
                poisonCloud.refreshPositionAndAngles(
                    pos.x.toDouble() + 0.5,
                    pos.y.toDouble() + 1 - ((3 * poisonCloud.radius) / 4),
                    pos.z.toDouble() + 0.5,
                    0.0f,
                    0.0f
                )
                world.spawnEntity(poisonCloud)
            }
        }

        private val SHAPE: VoxelShape = createCuboidShape(5.0, 0.0, 5.0, 11.0, 6.0, 11.0)
        var SQUISHED: BooleanProperty = BooleanProperty.of("squished")
    }
}