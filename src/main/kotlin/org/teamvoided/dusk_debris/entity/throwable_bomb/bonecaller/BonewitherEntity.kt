package org.teamvoided.dusk_debris.entity.throwable_bomb.bonecaller

import net.minecraft.core.component.DataComponents
import net.minecraft.server.level.ServerLevel
import net.minecraft.util.SpawnUtil
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.MobSpawnType
import net.minecraft.world.entity.monster.WitherSkeleton
import net.minecraft.world.item.Item
import net.minecraft.world.item.component.DyedItemColor
import net.minecraft.world.level.Level
import net.minecraft.world.scores.PlayerTeam
import org.teamvoided.dusk_debris.entity.throwable_bomb.BonecallerEntity
import org.teamvoided.dusk_debris.init.DuskBlocks
import org.teamvoided.dusk_debris.init.DuskEntities
import org.teamvoided.dusk_debris.init.DuskItems
import java.awt.Color

open class BonewitherEntity : BonecallerEntity {

    constructor(entityType: EntityType<out BonewitherEntity>, world: Level) : super(entityType, world)

    constructor(world: Level) : super(DuskEntities.BONEWITHER, world)
    constructor(owner: LivingEntity?, world: Level) :
            super(DuskEntities.BONEWITHER, owner, world) {
        this.owner = owner
    }

    constructor(x: Double, y: Double, z: Double, world: Level) :
            super(DuskEntities.BONEWITHER, x, y, z, world)

    override fun getCalledEntity(serverWorld: ServerLevel, bandanaColor: Int, team: PlayerTeam?) {
        //overide this to get your own
        val bandana = DuskItems.BONECALLER_BANDANA.defaultInstance
        val WitherSkeletonEntity = WitherSkeleton(EntityType.WITHER_SKELETON as EntityType<out WitherSkeleton>, level())
        val spawnPos = getSummonPos(
            WitherSkeletonEntity,
            MobSpawnType.MOB_SUMMONED,
            serverWorld,
            blockPosition(),
            20,
            3,
            6,
            SpawnUtil.Strategy.ON_TOP_OF_COLLIDER
        )
        WitherSkeletonEntity.moveTo(spawnPos, 0f, 0.0f)
        WitherSkeletonEntity.finalizeSpawn(
            serverWorld,
            this.level().getCurrentDifficultyAt(this.blockPosition()),
            MobSpawnType.MOB_SUMMONED,
            null
        )
        bandana.set(
            DataComponents.DYED_COLOR,
            DyedItemColor(bandanaColor, true)
        )
        WitherSkeletonEntity.setItemSlot(EquipmentSlot.HEAD, bandana)
        if (team != null) {
            serverWorld.scoreboard.addPlayerToTeam(WitherSkeletonEntity.scoreboardName, team)
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
        level().addFreshEntity(WitherSkeletonEntity)
    }

    override val color1: Color = Color(0x232326)
    override val color2: Color = Color(0x2C4551)
    override fun bandanaColors(): Int {
        val f = (Math.random().toFloat())
        return color1.lerp(color2, f).rgb
    }
    override fun getDefaultItem(): Item {
        return DuskBlocks.BONEWITHER_BLOCK.asItem()
    }
}