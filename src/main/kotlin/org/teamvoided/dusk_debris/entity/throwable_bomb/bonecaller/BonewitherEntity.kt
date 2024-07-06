package org.teamvoided.dusk_debris.entity.throwable_bomb.bonecaller

import net.minecraft.component.DataComponentTypes
import net.minecraft.component.type.DyedColorComponent
import net.minecraft.entity.EntityType
import net.minecraft.entity.EquipmentSlot
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.SpawnReason
import net.minecraft.entity.mob.SkeletonEntity
import net.minecraft.entity.mob.WitherSkeletonEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.particle.ParticleEffect
import net.minecraft.particle.ParticleTypes
import net.minecraft.scoreboard.Team
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.SpawnUtil
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.MathHelper
import net.minecraft.world.World
import org.teamvoided.dusk_debris.entity.throwable_bomb.BonecallerEntity
import org.teamvoided.dusk_debris.init.DuskEntities
import org.teamvoided.dusk_debris.init.DuskItems
import org.teamvoided.dusk_debris.particle.BonecallerParticleEffect
import java.awt.Color

open class BonewitherEntity : BonecallerEntity {

    constructor(entityType: EntityType<out BonewitherEntity>, world: World) : super(entityType, world)

    constructor(world: World) : super(DuskEntities.BONEWITHER, world)
    constructor(owner: LivingEntity?, world: World) :
            super(DuskEntities.BONEWITHER, owner, world) {
        this.owner = owner
    }

    constructor(x: Double, y: Double, z: Double, world: World) :
            super(DuskEntities.BONEWITHER, x, y, z, world)

    override fun bonecall(livingEntity: LivingEntity?) {
        if (!world.isClient) {
            val serverWorld = this.world as ServerWorld
            val team: Team? = livingEntity?.scoreboardTeam
            val bandanaColor = if (team != null) team.color.colorValue!! else bandanaColors()
            if (!world.isClient) {
                for (ignored in 0..2) {
                    val skeletonEntity = WitherSkeletonEntity(getCalledEntity(), world)
                    val spawnPos = getSummonPos(
                        skeletonEntity, SpawnReason.MOB_SUMMONED, serverWorld,
                        blockPos, 20, 3, 6, SpawnUtil.Strategy.field_39401
                    )
                    skeletonEntity.refreshPositionAndAngles(spawnPos, 0f, 0.0f)
                    skeletonEntity.initialize(
                        serverWorld,
                        this.world.getLocalDifficulty(this.blockPos),
                        SpawnReason.MOB_SUMMONED,
                        null
                    )
                    val bandana = DuskItems.BONECALLER_BANDANA.defaultStack
                    bandana.set(
                        DataComponentTypes.DYED_COLOR,
                        DyedColorComponent(bandanaColor, true)
                    )
                    skeletonEntity.equipStack(EquipmentSlot.HEAD, bandana)
                    if (team != null) {
                        serverWorld.scoreboard.addPlayerToTeam(skeletonEntity.profileName, team)
                    }
                    serverWorld.spawnParticles(
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
                    world.spawnEntity(skeletonEntity)
                }
            }
            serverWorld.spawnParticles(
                getTrailingParticle(),
                blockPos.x + 0.5,
                blockPos.y.toDouble(),
                blockPos.z + 0.5,
                20,
                0.0,
                0.0,
                0.0,
                1.0
            )
        }
    }

    fun getSummonPos(
        entityType: WitherSkeletonEntity,
        reason: SpawnReason,
        world: ServerWorld,
        pos: BlockPos,
        attempts: Int,
        rangeXZ: Int,
        rangeY: Int,
        spawnStrategy: SpawnUtil.Strategy
    ): BlockPos {
        val mutable = pos.mutableCopy()
        for (l in 0 until attempts) {
            val x = MathHelper.nextBetween(world.random, -rangeXZ, rangeXZ)
            val z = MathHelper.nextBetween(world.random, -rangeXZ, rangeXZ)
            mutable[pos, x, rangeY] = z
            if (world.worldBorder.contains(mutable) && SpawnUtil.method_42121(world, rangeY, mutable, spawnStrategy)) {
                if (entityType.canSpawn(world, reason) &&
                    world.doesNotIntersectEntities(entityType)
                ) {
                    return mutable
                }
            }
        }
        return pos
    }

    override val color1: Color = Color(0x515353)
    override val color2: Color = Color(0x303233)
    override fun bandanaColors(): Int {
        val f = (Math.random().toFloat())
        return color1.lerp(color2, f).rgb
    }

    override fun getCalledEntity() = EntityType.WITHER_SKELETON
    override fun getDefaultItem(): Item {
        return DuskItems.BONEWITHER_ITEM
    }

    override fun getTrailingParticle(): ParticleEffect = BonecallerParticleEffect(0x515353, 0x303233)
}