package org.teamvoided.dusk_debris.entity.throwable_bomb.bonecaller

import net.minecraft.server.level.ServerLevel
import net.minecraft.util.SpawnUtil
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.MobSpawnType
import net.minecraft.world.item.Item
import net.minecraft.world.level.Level
import net.minecraft.world.scores.PlayerTeam
import org.teamvoided.dusk_debris.entity.GloomEntity
import org.teamvoided.dusk_debris.entity.throwable_bomb.BonecallerEntity
import org.teamvoided.dusk_debris.init.DuskBlocks
import org.teamvoided.dusk_debris.init.DuskEntities
import java.awt.Color

open class ShadecallerEntity : BonecallerEntity {

    constructor(entityType: EntityType<out ShadecallerEntity>, world: Level) : super(entityType, world)

    constructor(world: Level) : super(DuskEntities.SHADECALLER, world)
    constructor(owner: LivingEntity?, world: Level) :
            super(DuskEntities.SHADECALLER, owner, world) {
        this.owner = owner
    }

    constructor(x: Double, y: Double, z: Double, world: Level) :
            super(DuskEntities.SHADECALLER, x, y, z, world)

    override fun getCalledEntity(serverWorld: ServerLevel, bandanaColor: Int, team: PlayerTeam?) {
        val GloomEntity = GloomEntity(DuskEntities.GLOOM as EntityType<out GloomEntity>, level())
        val spawnPos = getSummonPos(
            GloomEntity,
            MobSpawnType.MOB_SUMMONED,
            serverWorld,
            blockPosition(),
            20,
            3,
            6,
            SpawnUtil.Strategy.ON_TOP_OF_COLLIDER
        )
        GloomEntity.moveTo(spawnPos, 0f, 0.0f)
        GloomEntity.finalizeSpawn(
            serverWorld,
            this.level().getCurrentDifficultyAt(this.blockPosition()),
            MobSpawnType.MOB_SUMMONED,
            null
        )
        if (team != null) {
            serverWorld.scoreboard.addPlayerToTeam(GloomEntity.scoreboardName, team)
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
        level().addFreshEntity(GloomEntity)
    }

    override val color1: Color = Color(0x2B2B2B)
    override val color2: Color = Color(0x0F0F0F)
    override fun getDefaultItem(): Item {
        return DuskBlocks.SHADECALLER_BLOCK.asItem()
    }
}