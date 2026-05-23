package org.teamvoided.dusk_debris.entity.throwable_bomb.bonecaller

import net.minecraft.core.component.DataComponents
import net.minecraft.server.level.ServerLevel
import net.minecraft.util.SpawnUtil
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.MobSpawnType
import net.minecraft.world.entity.monster.Bogged
import net.minecraft.world.item.Item
import net.minecraft.world.item.component.DyedItemColor
import net.minecraft.world.level.Level
import net.minecraft.world.scores.PlayerTeam
import org.teamvoided.dusk_debris.entity.throwable_bomb.BonecallerEntity
import org.teamvoided.dusk_debris.init.DuskBlocks
import org.teamvoided.dusk_debris.init.DuskEntities
import org.teamvoided.dusk_debris.init.DuskItems
import java.awt.Color

open class BogcallerEntity : BonecallerEntity {

    constructor(entityType: EntityType<out BogcallerEntity>, world: Level) : super(entityType, world)

    constructor(world: Level) : super(DuskEntities.BOGCALLER, world)
    constructor(owner: LivingEntity?, world: Level) :
            super(DuskEntities.BOGCALLER, owner, world) {
        this.owner = owner
    }

    constructor(x: Double, y: Double, z: Double, world: Level) :
            super(DuskEntities.BOGCALLER, x, y, z, world)

    override fun getCalledEntity(serverWorld: ServerLevel, bandanaColor: Int, team: PlayerTeam?) {
        //overide this to get your own
        val bandana = DuskItems.BONECALLER_BANDANA.defaultInstance
        val boggedEntity = Bogged(EntityType.BOGGED as EntityType<out Bogged>, level())
        val spawnPos = getSummonPos(
            boggedEntity,
            MobSpawnType.MOB_SUMMONED,
            serverWorld,
            blockPosition(),
            20,
            3,
            6,
            SpawnUtil.Strategy.ON_TOP_OF_COLLIDER
        )
        boggedEntity.moveTo(spawnPos, 0f, 0.0f)
        boggedEntity.finalizeSpawn(
            serverWorld,
            this.level().getCurrentDifficultyAt(this.blockPosition()),
            MobSpawnType.MOB_SUMMONED,
            null
        )
        bandana.set(
            DataComponents.DYED_COLOR,
            DyedItemColor(bandanaColor, true)
        )
        boggedEntity.setItemSlot(EquipmentSlot.HEAD, bandana)
        if (team != null) {
            serverWorld.scoreboard.addPlayerToTeam(boggedEntity.scoreboardName, team)
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
        level().addFreshEntity(boggedEntity)
    }

    override fun getDefaultItem(): Item {
        return DuskBlocks.BOGCALLER_BLOCK.asItem()
    }

    override fun bandanaColors(): Int {
        val hue = 70f + (Math.random().toFloat() * 210f)
        val saturation = 0.4f + (Math.random().toFloat() * 0.4f)
        val value = 0.1f + (Math.random().toFloat() * 0.4f)
        return Color.HSBtoRGB(hue / 360, saturation, value)
    }

    override val color1: Color = Color(0xEDE8BD)
    override val color2: Color = Color(0x93BA77)
}