package org.teamvoided.dusk_debris.entity.throwable_bomb

import net.minecraft.core.BlockPos
import net.minecraft.core.component.DataComponents
import net.minecraft.core.particles.ParticleOptions
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.util.Mth
import net.minecraft.util.SpawnUtil
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.MobSpawnType
import net.minecraft.world.entity.monster.AbstractSkeleton
import net.minecraft.world.entity.monster.Skeleton
import net.minecraft.world.item.Item
import net.minecraft.world.item.component.DyedItemColor
import net.minecraft.world.level.Level
import net.minecraft.world.scores.PlayerTeam
import org.teamvoided.dusk_debris.init.DuskBlocks
import org.teamvoided.dusk_debris.init.DuskEntities
import org.teamvoided.dusk_debris.init.DuskItems
import org.teamvoided.dusk_debris.particle.color.BonecallerParticleEffect
import java.awt.Color

open class BonecallerEntity : AbstractThrwowableBombEntity {
    var owner: LivingEntity? = null

    constructor(entityType: EntityType<out BonecallerEntity>, world: Level) : super(entityType, world)

    constructor(world: Level) : super(DuskEntities.BONECALLER, world)
    constructor(entityType: EntityType<out BonecallerEntity>, owner: LivingEntity?, world: Level) :
            super(entityType, owner, world) {
        this.owner = owner
    }

    constructor(owner: LivingEntity?, world: Level) :
            super(DuskEntities.BONECALLER, owner, world) {
        this.owner = owner
    }

    constructor(x: Double, y: Double, z: Double, world: Level) :
            super(DuskEntities.BONECALLER, x, y, z, world)

    constructor(entityType: EntityType<out BonecallerEntity>, x: Double, y: Double, z: Double, world: Level) :
            super(entityType, x, y, z, world)

    override fun explode() {
        level().playSound(
            this,
            this.blockPosition(),
            SoundEvents.GLASS_BREAK,
            SoundSource.BLOCKS,
            0.7f,
            0.0f + level().random.nextFloat() * 0.2f
        )
        bonecall(owner)
        super.explode()
    }

    open fun bonecall(livingEntity: LivingEntity?) {
        if (!level().isClientSide) {
            val serverWorld = this.level() as ServerLevel
            val team: PlayerTeam? = livingEntity?.team
            val bandanaColor = if (team != null) team.color.color!! else bandanaColors()
            if (!level().isClientSide) {
                for (ignored in 1..3) {
                    getCalledEntity(serverWorld, bandanaColor, team)
                }
            }
            serverWorld.sendParticles(
                getTrailingParticle(),
                blockPosition().x + 0.5,
                blockPosition().y.toDouble(),
                blockPosition().z + 0.5,
                20,
                0.0,
                0.0,
                0.0,
                1.0
            )
        }
    }

    open fun getCalledEntity(serverWorld: ServerLevel, bandanaColor: Int, team: PlayerTeam?) {
        //overide this to get your own
        val bandana = DuskItems.BONECALLER_BANDANA.defaultInstance
        val skeletonEntity = Skeleton(EntityType.SKELETON as EntityType<out Skeleton>, level())
        val spawnPos = getSummonPos(
            skeletonEntity,
            MobSpawnType.MOB_SUMMONED,
            serverWorld,
            blockPosition(),
            20,
            3,
            6,
            SpawnUtil.Strategy.ON_TOP_OF_COLLIDER
        )
        skeletonEntity.moveTo(spawnPos, 0f, 0.0f)
        skeletonEntity.finalizeSpawn(
            serverWorld,
            this.level().getCurrentDifficultyAt(this.blockPosition()),
            MobSpawnType.MOB_SUMMONED,
            null
        )
        bandana.set(
            DataComponents.DYED_COLOR,
            DyedItemColor(bandanaColor, true)
        )
        skeletonEntity.setItemSlot(EquipmentSlot.HEAD, bandana)
        if (team != null) {
            serverWorld.scoreboard.addPlayerToTeam(skeletonEntity.scoreboardName, team)
        }
        serverWorld.sendParticles(
            getTrailingParticle(),
            spawnPos.x + 0.5,
            spawnPos.y.toDouble(),
            spawnPos.z + 0.5,
            20,
            0.0,
            0.0,
            0.0,
            1.0
        )
        level().addFreshEntity(skeletonEntity)
    }

    open fun getSummonPos(
        entityType: AbstractSkeleton,
        reason: MobSpawnType,
        world: ServerLevel,
        pos: BlockPos,
        attempts: Int,
        rangeXZ: Int,
        rangeY: Int,
        spawnStrategy: SpawnUtil.Strategy
    ): BlockPos {
        //overide this to get your own
        val mutable = pos.mutable()
        for (l in 0 until attempts) {
            val x = Mth.randomBetweenInclusive(world.random, -rangeXZ, rangeXZ)
            val z = Mth.randomBetweenInclusive(world.random, -rangeXZ, rangeXZ)
            mutable.setWithOffset(pos, x, rangeY, z)
            if (world.worldBorder.isWithinBounds(mutable) && SpawnUtil.moveToPossibleSpawnPosition(world, rangeY, mutable, spawnStrategy)) {
                if (entityType.checkSpawnRules(world, reason) &&
                    world.isUnobstructed(entityType)
                ) {
                    return mutable
                }
            }
        }
        return pos
    }

    override fun getDefaultItem(): Item {
        return DuskBlocks.BONECALLER_BLOCK.asItem()
    }

    open fun bandanaColors(): Int {
        val hue = (Math.random().toFloat() * 360f)
        val saturation = 0.4f + (Math.random().toFloat() * 0.5f)
        val value: Float = 0.1f + Math.random().toFloat() * 0.8f
        val h = hue % 1f
        val s = Math.clamp(saturation, 0f, 1f)
        val v = Math.clamp(value, 0f, 1f)
        return Color.HSBtoRGB(h, s, v)
    }

    open val color1: Color = Color(0xEFC90B)
    open val color2: Color = Color(0x935D26)

    override fun getTrailingParticle(): ParticleOptions = BonecallerParticleEffect(color1.rgb, color2.rgb)
    fun Color.lerp(other: Color, amount: Float): Color {
        return Color(
            (this.red * (1 - amount) + other.red * amount).toInt(),
            (this.green * (1 - amount) + other.green * amount).toInt(),
            (this.blue * (1 - amount) + other.blue * amount).toInt(),
            (this.alpha * (1 - amount) + other.alpha * amount).toInt()
        )
    }
}