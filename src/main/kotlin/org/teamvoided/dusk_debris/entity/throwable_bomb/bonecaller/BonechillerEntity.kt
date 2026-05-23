package org.teamvoided.dusk_debris.entity.throwable_bomb.bonecaller

import net.minecraft.core.component.DataComponents
import net.minecraft.server.level.ServerLevel
import net.minecraft.util.SpawnUtil
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.MobSpawnType
import net.minecraft.world.entity.monster.Stray
import net.minecraft.world.item.Item
import net.minecraft.world.item.component.DyedItemColor
import net.minecraft.world.level.Level
import net.minecraft.world.scores.PlayerTeam
import org.teamvoided.dusk_debris.entity.throwable_bomb.BonecallerEntity
import org.teamvoided.dusk_debris.init.DuskBlocks
import org.teamvoided.dusk_debris.init.DuskEntities
import org.teamvoided.dusk_debris.init.DuskItems
import java.awt.Color

open class BonechillerEntity : BonecallerEntity {

    constructor(entityType: EntityType<out BonechillerEntity>, world: Level) : super(entityType, world)

    constructor(world: Level) : super(DuskEntities.BONECHILLER, world)
    constructor(owner: LivingEntity?, world: Level) :
            super(DuskEntities.BONECHILLER, owner, world) {
        this.owner = owner
    }

    constructor(x: Double, y: Double, z: Double, world: Level) :
            super(DuskEntities.BONECHILLER, x, y, z, world)

    override fun getCalledEntity(serverWorld: ServerLevel, bandanaColor: Int, team: PlayerTeam?) {
        val bandana = DuskItems.BONECALLER_BANDANA.defaultInstance
        val strayEntity = Stray(EntityType.STRAY as EntityType<out Stray>, level())
        val spawnPos = getSummonPos(
            strayEntity,
            MobSpawnType.MOB_SUMMONED,
            serverWorld,
            blockPosition(),
            20,
            3,
            6,
            SpawnUtil.Strategy.ON_TOP_OF_COLLIDER
        )
        strayEntity.moveTo(spawnPos, 0f, 0.0f)
        strayEntity.finalizeSpawn(
            serverWorld,
            this.level().getCurrentDifficultyAt(this.blockPosition()),
            MobSpawnType.MOB_SUMMONED,
            null
        )
        bandana.set(
            DataComponents.DYED_COLOR,
            DyedItemColor(bandanaColor, true)
        )
        strayEntity.setItemSlot(EquipmentSlot.HEAD, bandana)
        if (team != null) {
            serverWorld.scoreboard.addPlayerToTeam(strayEntity.scoreboardName, team)
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
        level().addFreshEntity(strayEntity)
    }

    override fun getDefaultItem(): Item {
        return DuskBlocks.BONECHILLER_BLOCK.asItem()
    }

    override fun bandanaColors(): Int {
        val hue = 180f + (Math.random().toFloat() * 90f)
        val saturation = 0.2f + (Math.random().toFloat() * 0.6f)
        val value = 0.2f + (Math.random().toFloat() * 0.6f)
        return Color.HSBtoRGB(hue / 360, saturation, value)
    }
    override val color1: Color = Color(0xE6ECED)
    override val color2: Color = Color(0xA2B6C4)
}