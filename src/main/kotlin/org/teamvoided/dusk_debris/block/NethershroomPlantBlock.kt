package org.teamvoided.dusk_debris.block

import com.mojang.serialization.MapCodec
import net.minecraft.block.*
import net.minecraft.component.type.PotionContentsComponent
import net.minecraft.entity.Entity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.particle.ParticleTypes
import net.minecraft.potion.Potion
import net.minecraft.potion.Potions
import net.minecraft.registry.Holder
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
import org.teamvoided.dusk_autumn.init.DuskEntities
import org.teamvoided.dusk_debris.data.DuskBlockTags
import org.teamvoided.dusk_debris.data.DuskEntityTypeTags
import org.teamvoided.dusk_debris.init.DuskSoundEvents
import kotlin.random.Random

class NethershroomPlantBlock(delay: Int, potion: Holder<Potion>, settings: Settings) : AbstractPlantBlock(settings) {
    val potion = potion
    val delay = delay

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
        return floor.isIn(DuskBlockTags.NETHERSHROOM_PLACEABLE_ON)
    }

    override fun onEntityCollision(state: BlockState, world: World, pos: BlockPos, entity: Entity) {
        if (!entity.isSneaking || entity.type.isIn(DuskEntityTypeTags.IS_NOT_AFFECTED_BY_NETHERSHROOM)) {
            if ((entity is PlayerEntity || world.gameRules.getBooleanValue(GameRules.DO_MOB_GRIEFING))) {
                world.setBlockState(
                    pos,
                    state.with(SQUISHED, true),
                    3
                )
                world.playSound(
                    null,
                    pos,
                    DuskSoundEvents.BLOCK_NETHERSHROOM_SQUEEZE,
                    SoundCategory.BLOCKS,
                    1f,
                    0.9f + world.random.nextFloat() * 0.2f
                )
                //KEEP THIS VALUE THE SAME AS THE BLOCK FRAMERATE
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
            explode(world, pos)
        }
    }

    private fun explode(world: World, pos: BlockPos) {
        val dropChance = 0.2
        world.breakBlock(pos, Random.nextDouble() <= dropChance)
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
//            poisonCloud.setPotionContents(PotionContentsComponent())
            poisonCloud.particleType = ParticleTypes.SOUL_FIRE_FLAME
            poisonCloud.setPotionContents(PotionContentsComponent(Potions.POISON))
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
                Potions.POISON,
                settings
            )
        }
        private val SHAPE: VoxelShape = createCuboidShape(5.0, 0.0, 5.0, 11.0, 6.0, 11.0)
        var SQUISHED: BooleanProperty = BooleanProperty.of("squished")
    }
}