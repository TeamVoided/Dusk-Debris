package org.teamvoided.dusk_debris.entity.throwable_bomb.bonecaller

import net.minecraft.entity.EntityType
import net.minecraft.entity.EquipmentSlot
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.SpawnReason
import net.minecraft.entity.mob.WitherSkeletonEntity
import net.minecraft.item.Item
import net.minecraft.particle.ParticleEffect
import net.minecraft.scoreboard.Team
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.SpawnUtil
import net.minecraft.world.World
import org.teamvoided.dusk_debris.entity.GloomEntity
import org.teamvoided.dusk_debris.entity.throwable_bomb.BonecallerEntity
import org.teamvoided.dusk_debris.init.DuskBlocks
import org.teamvoided.dusk_debris.init.DuskEntities
import org.teamvoided.dusk_debris.init.DuskItems
import org.teamvoided.dusk_debris.particle.BonecallerParticleEffect
import java.awt.Color

open class ShadecallerEntity : BonecallerEntity {

    constructor(entityType: EntityType<out ShadecallerEntity>, world: World) : super(entityType, world)

    constructor(world: World) : super(DuskEntities.SHADECALLER, world)
    constructor(owner: LivingEntity?, world: World) :
            super(DuskEntities.SHADECALLER, owner, world) {
        this.owner = owner
    }

    constructor(x: Double, y: Double, z: Double, world: World) :
            super(DuskEntities.SHADECALLER, x, y, z, world)

    override fun getCalledEntity(serverWorld: ServerWorld, bandanaColor: Int, team: Team?) {
        val GloomEntity = GloomEntity(DuskEntities.GLOOM as EntityType<out GloomEntity>, world)
        val spawnPos = getSummonPos(
            GloomEntity,
            SpawnReason.MOB_SUMMONED,
            serverWorld,
            blockPos,
            20,
            3,
            6,
            SpawnUtil.Strategy.field_39401
        )
        GloomEntity.refreshPositionAndAngles(spawnPos, 0f, 0.0f)
        GloomEntity.initialize(
            serverWorld,
            this.world.getLocalDifficulty(this.blockPos),
            SpawnReason.MOB_SUMMONED,
            null
        )
        if (team != null) {
            serverWorld.scoreboard.addPlayerToTeam(GloomEntity.profileName, team)
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
        world.spawnEntity(GloomEntity)
    }

    override val color1: Color = Color(0x2B2B2B)
    override val color2: Color = Color(0x0F0F0F)
    override fun getDefaultItem(): Item {
        return DuskBlocks.SHADECALLER_BLOCK.asItem()
    }
}