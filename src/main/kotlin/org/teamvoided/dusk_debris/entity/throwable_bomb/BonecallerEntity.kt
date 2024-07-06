package org.teamvoided.dusk_debris.entity.throwable_bomb

import net.minecraft.component.DataComponentTypes
import net.minecraft.component.type.DyedColorComponent
import net.minecraft.entity.EntityType
import net.minecraft.entity.EquipmentSlot
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.SpawnReason
import net.minecraft.entity.mob.SkeletonEntity
import net.minecraft.item.Item
import net.minecraft.particle.ParticleEffect
import net.minecraft.scoreboard.Team
import net.minecraft.server.world.ServerWorld
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.util.SpawnUtil
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.MathHelper
import net.minecraft.world.World
import org.joml.Vector3f
import org.teamvoided.dusk_debris.init.DuskEntities
import org.teamvoided.dusk_debris.init.DuskItems
import org.teamvoided.dusk_debris.particle.BonecallerParticleEffect
import java.awt.Color
import kotlin.random.Random

open class BonecallerEntity : AbstractThrwowableBombEntity {
    var owner: LivingEntity? = null

    constructor(entityType: EntityType<out BonecallerEntity>, world: World) : super(entityType, world)

    constructor(world: World) : super(DuskEntities.BONECALLER, world)
    constructor(entityType: EntityType<out BonecallerEntity>, owner: LivingEntity?, world: World) :
            super(entityType, owner, world) {
        this.owner = owner
    }

    constructor(owner: LivingEntity?, world: World) :
            super(DuskEntities.BONECALLER, owner, world) {
        this.owner = owner
    }

    constructor(x: Double, y: Double, z: Double, world: World) :
            super(DuskEntities.BONECALLER, x, y, z, world)

    constructor(entityType: EntityType<out BonecallerEntity>, x: Double, y: Double, z: Double, world: World) :
            super(entityType, x, y, z, world)

    override fun explode() {
        world.playSound(
            this,
            this.blockPos,
            SoundEvents.BLOCK_GLASS_BREAK,
            SoundCategory.BLOCKS,
            0.7f,
            0.0f + world.random.nextFloat() * 0.2f
        )
        bonecall(owner)
        super.explode()
    }

    open fun bonecall(livingEntity: LivingEntity?) {
        if (!world.isClient) {
            val serverWorld = this.world as ServerWorld
            val team: Team? = livingEntity?.scoreboardTeam
            val bandanaColor = if (team != null) team.color.colorValue!! else bandanaColors()
            if (!world.isClient) {
                for (ignored in 0..2) {
                    val skeletonEntity = SkeletonEntity(getCalledEntity() as EntityType<out SkeletonEntity>, world)
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
                    doOtherSkeletonThingsAndColor(skeletonEntity, bandanaColor)
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

    open fun getSummonPos(
        entityType: SkeletonEntity,
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

    open fun doOtherSkeletonThings(skeletonEntity: SkeletonEntity) {}

    open fun doOtherSkeletonThingsAndColor(skeletonEntity: SkeletonEntity, bandanaColor: Int) {
        doOtherSkeletonThings(skeletonEntity)
        val bandana = DuskItems.BONECALLER_BANDANA.defaultStack
        bandana.set(
            DataComponentTypes.DYED_COLOR,
            DyedColorComponent(bandanaColor, true)
        )
        skeletonEntity.equipStack(EquipmentSlot.HEAD, bandana)
    }

    open fun getCalledEntity(): EntityType<*> = EntityType.SKELETON

    override fun getDefaultItem(): Item {
        return DuskItems.BONECALLER_ITEM
    }

    open val color1: Color = Color(0xBC3235)
    open val color2: Color = Color(0x1B6666)
    open fun bandanaColors(): Int {
        val random = 100
        val default = 50
        val one = Math.random() * random
        val two = Math.random() * random - one
        val three = random - one - two
        val r = default + one.toInt()
        val g = if (two > 0) default + two.toInt() else default
        val b = if (three > 0) default + three.toInt() else default
        return Color(r, g, b).rgb
    }

    override fun getTrailingParticle(): ParticleEffect = BonecallerParticleEffect(0xEFC90B, 0x935D26)
    fun Color.lerp(other: Color, amount: Float): Color {
        return Color(
            (this.red * (1 - amount) + other.red * amount).toInt(),
            (this.green * (1 - amount) + other.green * amount).toInt(),
            (this.blue * (1 - amount) + other.blue * amount).toInt(),
            (this.alpha * (1 - amount) + other.alpha * amount).toInt()
        )
    }
}